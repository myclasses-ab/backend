package com.classes.Backend.Service.subscription;

import com.classes.Backend.Domain.enums.CreditTopUpStatus;
import com.classes.Backend.Domain.enums.CreditTransactionType;
import com.classes.Backend.Domain.enums.InstituteStaffRole;
import com.classes.Backend.Domain.enums.NotificationType;
import com.classes.Backend.Domain.institute.Institute;
import com.classes.Backend.Domain.notification.Notification;
import com.classes.Backend.Domain.subscription.CreditTopUpRequest;
import com.classes.Backend.Domain.users.UserInstituteAssociation;
import com.classes.Backend.Repository.subscription.CreditTopUpRequestRepository;
import com.classes.Backend.Service.institute.InstituteService;
import com.classes.Backend.Service.notification.NotificationService;
import com.classes.Backend.Service.users.UserInstituteAssociationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CreditTopUpRequestServiceImpl implements CreditTopUpRequestService {

    private final CreditTopUpRequestRepository CREDIT_TOP_UP_REQUEST_REPOSITORY;
    private final CreditServiceImpl CREDIT_SERVICE_IMPL;
    private final NotificationService NOTIFICATION_SERVICE;
    private final UserInstituteAssociationService USER_INSTITUTE_ASSOCIATION_SERVICE;
    private final InstituteService INSTITUTE_SERVICE;

    @Value("${credits.rupee-per-token:10}")
    private Integer rupeePerToken;

    @Override
    @Transactional
    public CreditTopUpRequest createRequest(String instituteIdentifier, Integer requestedCredits, String transactionIdLast6) {
        if (requestedCredits == null || requestedCredits <= 0) {
            throw new IllegalArgumentException("Requested credits must be greater than zero");
        }
        if (transactionIdLast6 == null || transactionIdLast6.length() != 6 || !transactionIdLast6.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Transaction ID last 6 digits must be exactly 6 alphanumeric characters");
        }

        // Prevent duplicate submissions within 24 hours
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        List<CreditTopUpRequest> recentDuplicates = CREDIT_TOP_UP_REQUEST_REPOSITORY
                .findByInstituteIdentifierAndTransactionIdLast6AndCreatedAtAfter(
                        instituteIdentifier, transactionIdLast6, twentyFourHoursAgo);
        if (!recentDuplicates.isEmpty()) {
            throw new IllegalStateException("A top-up request with this transaction ID was already submitted recently");
        }

        CreditTopUpRequest request = new CreditTopUpRequest();
        request.setInstituteIdentifier(instituteIdentifier);
        request.setRequestedCredits(requestedCredits);
        request.setAmountInRupees(requestedCredits * rupeePerToken);
        request.setTransactionIdLast6(transactionIdLast6.toUpperCase());
        request.setStatus(CreditTopUpStatus.PENDING);

        return CREDIT_TOP_UP_REQUEST_REPOSITORY.save(request);
    }

    @Override
    @Transactional
    public CreditTopUpRequest approveRequest(String identifier, String approvedBy, String adminNotes) {
        CreditTopUpRequest request = findById(identifier);
        if (request.getStatus() != CreditTopUpStatus.PENDING) {
            throw new IllegalStateException("Only PENDING requests can be approved");
        }

        CREDIT_SERVICE_IMPL.grantCredits(
                request.getInstituteIdentifier(),
                request.getRequestedCredits(),
                CreditTransactionType.GRANTED,
                "Credits purchased via QR payment (TXN: ..." + request.getTransactionIdLast6() + ")",
                request.getIdentifier()
        );

        request.setStatus(CreditTopUpStatus.APPROVED);
        request.setApprovedBy(approvedBy);
        request.setApprovedAt(LocalDateTime.now());
        request.setAdminNotes(adminNotes);
        CreditTopUpRequest savedRequest = CREDIT_TOP_UP_REQUEST_REPOSITORY.save(request);
        createCreditTopUpApprovedNotification(savedRequest);
        return savedRequest;
    }

    private void createCreditTopUpApprovedNotification(CreditTopUpRequest request) {
        String instituteIdentifier = request.getInstituteIdentifier();
        if (instituteIdentifier == null || instituteIdentifier.isBlank()) {
            return;
        }

        List<String> recipientUserIdentifiers = resolveInstituteOwnerIdentifiers(instituteIdentifier);
        if (recipientUserIdentifiers.isEmpty()) {
            return;
        }

        Integer credits = request.getRequestedCredits();
        String creditsText = credits != null ? credits + " credits" : "credits";

        for (String userIdentifier : recipientUserIdentifiers) {
            Notification notification = new Notification();
            notification.setUserIdentifier(userIdentifier);
            notification.setType(NotificationType.CREDIT_TOP_UP_APPROVED);
            notification.setTitle("Credit top-up approved");
            notification.setBody("Your top-up of " + creditsText + " has been approved.");
            notification.setEntityType("CREDIT_TOP_UP");
            notification.setEntityIdentifier(request.getIdentifier());
            notification.setIsRead(false);
            NOTIFICATION_SERVICE.save(notification);
        }
    }

    private List<String> resolveInstituteOwnerIdentifiers(String instituteIdentifier) {
        List<UserInstituteAssociation> associations = USER_INSTITUTE_ASSOCIATION_SERVICE
                .findByInstituteIdentifier(instituteIdentifier);

        List<String> ownerIdentifiers = associations.stream()
                .filter(assoc -> Boolean.TRUE.equals(assoc.getIsActive()))
                .filter(assoc -> assoc.getRole() == InstituteStaffRole.OWNER)
                .map(UserInstituteAssociation::getUserIdentifier)
                .filter(userId -> userId != null && !userId.isBlank())
                .distinct()
                .collect(Collectors.toList());

        if (!ownerIdentifiers.isEmpty()) {
            return ownerIdentifiers;
        }

        INSTITUTE_SERVICE.findById(instituteIdentifier)
                .map(Institute::getCreatedBy)
                .filter(createdBy -> createdBy != null && !createdBy.isBlank())
                .ifPresent(ownerIdentifiers::add);

        return ownerIdentifiers;
    }

    @Override
    @Transactional
    public CreditTopUpRequest rejectRequest(String identifier, String adminNotes) {
        CreditTopUpRequest request = findById(identifier);
        if (request.getStatus() != CreditTopUpStatus.PENDING) {
            throw new IllegalStateException("Only PENDING requests can be rejected");
        }
        request.setStatus(CreditTopUpStatus.REJECTED);
        request.setAdminNotes(adminNotes);
        return CREDIT_TOP_UP_REQUEST_REPOSITORY.save(request);
    }

    @Override
    public List<CreditTopUpRequest> findByInstitute(String instituteIdentifier) {
        return CREDIT_TOP_UP_REQUEST_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    @Override
    public List<CreditTopUpRequest> findPending() {
        return CREDIT_TOP_UP_REQUEST_REPOSITORY.findByStatus(CreditTopUpStatus.PENDING);
    }

    @Override
    public List<CreditTopUpRequest> findAll() {
        return CREDIT_TOP_UP_REQUEST_REPOSITORY.findAll();
    }

    @Override
    public CreditTopUpRequest findById(String identifier) {
        return CREDIT_TOP_UP_REQUEST_REPOSITORY.findById(identifier)
                .orElseThrow(() -> new RuntimeException("CreditTopUpRequest not found with identifier: " + identifier));
    }
}
