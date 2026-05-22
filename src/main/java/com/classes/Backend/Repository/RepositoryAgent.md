# Repository Layer Documentation

> **AI Agent Reference Guide** - This document serves as the source of truth for all Spring Data JPA Repositories in the Coaching Class Discovery Platform.

## Overview

The Repository layer contains all Spring Data JPA Repository interfaces that provide data access operations for Domain entities. Each repository extends `JpaRepository<EntityType, String>` where the String ID is the UUID-based `identifier` field.

**Technology Stack:**
- Spring Boot 4.0.5
- Spring Data JPA
- PostgreSQL
- UUID-based identifiers (field name: `identifier`)

---

## 📋 Table of Contents

1. [Update Rules](#-update-rules-must-read)
2. [Package Structure](#-package-structure)
3. [Repository-Domain Mapping](#-repository-domain-mapping)
4. [Common Patterns](#-common-patterns)
5. [Master Repositories](#-master-repositories)
6. [Institute Repositories](#-institute-repositories)
7. [Course Repositories](#-course-repositories)
8. [Faculty Repository](#-faculty-repository)
9. [Results Repositories](#-results-repositories)
11. [Reviews Repositories](#-reviews-repositories)
12. [Users Repositories](#-users-repositories)
13. [Leads Repository](#-leads-repository)
14. [Media Repository](#-media-repository)
15. [Subscription Repositories](#-subscription-repositories)
16. [Notification Repositories](#-notification-repositories)

---

## 🔄 Update Rules (MUST READ)

When modifying any repository file, you MUST update this documentation:

### 1. File Modification Rules

| Change Type | Required Action |
|-------------|-----------------|
| **Add new finder method** | Update repository section with method signature, parameters, and return type |
| **Remove finder method** | Remove from repository section, check if referenced in Cross-Reference |
| **Modify method signature** | Update method documentation, mark as "Modified: YYYY-MM-DD" |
| **Add new repository** | Add complete section with all methods, update Package Structure |
| **Delete repository** | Mark as DEPRECATED, move to Deprecated section |
| **Add query annotation** | Document the custom query with explanation |

### 2. Cross-Reference Update Rules

- When a repository is added for a new entity, update the Repository-Domain Mapping table
- When finder methods reference other entities, link to the Domain entity in DomainAgent.md
- Update method count in repository headers

### 3. Formatting Rules

- Use `code` formatting for method names, class names, and parameters
- Use **bold** for important notes
- Use > blockquotes for warnings about deprecated methods
- Include method signature with full types
- Document return values and their meaning

### 4. Cross-Reference Rules

- Link to Domain entities using: [EntityName](../Domain/DomainAgent.md#entityname)
- Link to related Repositories using: [RepositoryName](#repositoryname)
- Use relative paths for cross-references

---

## 📦 Package Structure

```
com.classes.Backend.Repository/
├── master/                         # Master/Reference data repositories
│   ├── CityRepository.java
│   ├── ExamTypeRepository.java
│   ├── StreamRepository.java
│   └── SubjectRepository.java
├── institute/                      # Institute core repositories
│   ├── BranchRepository.java
│   ├── InstituteFacilityRepository.java
│   └── InstituteRepository.java
├── course/                         # Course-related repositories
│   └── InstituteCourseRepository.java
├── faculty/                        # Faculty repository
│   └── FacultyRepository.java
├── results/                        # Results & achievements repositories
│   ├── AwardAndRecognitionRepository.java
│   └── ResultRepository.java
├── reviews/                        # Reviews & ratings repositories
│   ├── InstituteResponseRepository.java
│   ├── ReviewRepository.java
│   └── ReviewVoteRepository.java
├── users/                          # User management repositories
│   ├── BookmarkRepository.java
│   ├── UserInstituteAssociationRepository.java
│   └── UserRepository.java
├── leads/                          # Leads & inquiries repository
│   └── InquiryRepository.java
├── media/                          # Media repository
│   └── MediaRepository.java
├── subscription/                   # Subscription repositories
│   ├── InstituteSubscriptionRepository.java
│   └── SubscriptionPlanRepository.java
└── notification/                   # Notification & FAQ repositories
    ├── FaqRepository.java
    └── NotificationRepository.java
```

---

## 🗺️ Repository-Domain Mapping

| Repository | Domain Entity | Location in DomainAgent.md |
|------------|---------------|---------------------------|
| `AwardAndRecognitionRepository` | [AwardAndRecognition](../Domain/DomainAgent.md#awardandrecognition) | Domain/results |
| `BookmarkRepository` | [Bookmark](../Domain/DomainAgent.md#bookmark) | Domain/users |
| `BranchRepository` | [Branch](../Domain/DomainAgent.md#branch) | Domain/institute |
| `CityRepository` | [City](../Domain/DomainAgent.md#city) | Domain/master |
| `ExamTypeRepository` | [ExamType](../Domain/DomainAgent.md#examtype) | Domain/master |
| `FacultyRepository` | [Faculty](../Domain/DomainAgent.md#faculty) | Domain/faculty |
| `FaqRepository` | [Faq](../Domain/DomainAgent.md#faq) | Domain/notification |
| `InstituteCourseRepository` | [InstituteCourse](../Domain/DomainAgent.md#institutecourse) | Domain/course |
| `InstituteFacilityRepository` | [InstituteFacility](../Domain/DomainAgent.md#institutefacility) | Domain/institute |
| `InstituteRepository` | [Institute](../Domain/DomainAgent.md#institute) | Domain/institute |
| `InstituteResponseRepository` | [InstituteResponse](../Domain/DomainAgent.md#instituteresponse) | Domain/reviews |
| `InstituteSubscriptionRepository` | [InstituteSubscription](../Domain/DomainAgent.md#institutesubscription) | Domain/subscription |
| `InquiryRepository` | [Inquiry](../Domain/DomainAgent.md#inquiry) | Domain/leads |
| `MediaRepository` | [Media](../Domain/DomainAgent.md#media) | Domain/media |
| `NotificationRepository` | [Notification](../Domain/DomainAgent.md#notification) | Domain/notification |
| `ResultRepository` | [Result](../Domain/DomainAgent.md#result) | Domain/results |
| `ReviewRepository` | [Review](../Domain/DomainAgent.md#review) | Domain/reviews |
| `ReviewVoteRepository` | [ReviewVote](../Domain/DomainAgent.md#reviewvote) | Domain/reviews |
| `StreamRepository` | [Stream](../Domain/DomainAgent.md#stream) | Domain/master |
| `SubjectRepository` | [Subject](../Domain/DomainAgent.md#subject) | Domain/master |
| `SubscriptionPlanRepository` | [SubscriptionPlan](../Domain/DomainAgent.md#subscriptionplan) | Domain/subscription |
| `UserInstituteAssociationRepository` | [UserInstituteAssociation](../Domain/DomainAgent.md#userinstituteassociation) | Domain/users |
| `UserRepository` | [User](../Domain/DomainAgent.md#user) | Domain/users |

---

## 🔧 Common Patterns

### Standard Repository Methods (Inherited from JpaRepository)

All repositories inherit these methods:

| Method | Description |
|--------|-------------|
| `save(T entity)` | Save or update entity |
| `findById(String id)` | Find by identifier |
| `existsById(String id)` | Check existence |
| `findAll()` | Find all entities |
| `findAllById(Iterable<String> ids)` | Find all by IDs |
| `count()` | Count all entities |
| `deleteById(String id)` | Delete by ID |
| `delete(T entity)` | Delete entity |
| `deleteAll()` | Delete all |

### Custom Finder Method Patterns

Spring Data JPA derives queries from method names:

```java
// Find by single field
List<Entity> findByFieldName(Type value);
Optional<Entity> findByFieldName(Type value);

// Find with boolean flag
List<Entity> findByIsActiveTrue();
List<Entity> findByIsVerifiedTrue();

// Find by multiple fields (AND)
List<Entity> findByField1AndField2(Type1 value1, Type2 value2);

// Find by related entity identifier
List<Entity> findByParentEntityIdentifier(String identifier);

// Find with comparison
List<Entity> findByFieldGreaterThan(Type value);
```

### Repository Interface Template

```java
package com.classes.Backend.Repository.packagename;

import com.classes.Backend.Domain.packagename.EntityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntityNameRepository extends JpaRepository<EntityName, String> {
    // Custom finder methods here
}
```

---

## 📊 Master Repositories

### CityRepository
**Package:** `com.classes.Backend.Repository.master`

**Entity:** [City](../Domain/DomainAgent.md#city)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByNameAndState(String name, String state)` | `Optional<City>` | Find city by exact name and state |
| `findByState(String state)` | `List<City>` | Find all cities in a state |
| `findByIsMetroTrue()` | `List<City>` | Find all metro cities |

**Usage Examples:**
```java
// Find Mumbai in Maharashtra
Optional<City> mumbai = cityRepository.findByNameAndState("Mumbai", "Maharashtra");

// Get all metro cities for featured listings
List<City> metros = cityRepository.findByIsMetroTrue();
```

---

### ExamTypeRepository
**Package:** `com.classes.Backend.Repository.master`

**Entity:** [ExamType](../Domain/DomainAgent.md#examtype)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findBySlug(String slug)` | `Optional<ExamType>` | Find by URL-friendly slug |
| `findByStreamIdentifier(String streamIdentifier)` | `List<ExamType>` | Find all exams for a stream |

**Usage Examples:**
```java
// Find JEE Main exam
Optional<ExamType> jee = examTypeRepository.findBySlug("jee-main");

// Get all exams for Science stream
List<ExamType> scienceExams = examTypeRepository.findByStreamIdentifier(scienceId);
```

---

### StreamRepository
**Package:** `com.classes.Backend.Repository.master`

**Entity:** [Stream](../Domain/DomainAgent.md#stream)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findBySlug(String slug)` | `Optional<Stream>` | Find by URL-friendly slug |

**Usage Examples:**
```java
// Find Science stream
Optional<Stream> science = streamRepository.findBySlug("science");
```

---

### SubjectRepository
**Package:** `com.classes.Backend.Repository.master`

**Entity:** [Subject](../Domain/DomainAgent.md#subject)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findBySlug(String slug)` | `Optional<Subject>` | Find by URL-friendly slug |
| `findByStreamIdentifier(String streamIdentifier)` | `List<Subject>` | Find all subjects for a stream |

**Usage Examples:**
```java
// Find Physics subject
Optional<Subject> physics = subjectRepository.findBySlug("physics");

// Get all subjects for Science stream
List<Subject> scienceSubjects = subjectRepository.findByStreamIdentifier(scienceId);
```

---

## 🏢 Institute Repositories

### InstituteRepository
**Package:** `com.classes.Backend.Repository.institute`

**Entity:** [Institute](../Domain/DomainAgent.md#institute)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findBySlug(String slug)` | `Optional<Institute>` | Find by URL-friendly slug |
| `findByType(InstituteType type)` | `List<Institute>` | Find by type (OFFLINE/ONLINE/HYBRID) |
| `findByOwnershipType(OwnershipType ownershipType)` | `List<Institute>` | Find by ownership type |
| `findBySubscriptionTier(SubscriptionTier tier)` | `List<Institute>` | Find by subscription tier |
| `findByIsVerifiedTrue()` | `List<Institute>` | Find all verified institutes |
| `findByIsFeaturedTrue()` | `List<Institute>` | Find all featured institutes |
| `findByIsActiveTrue()` | `List<Institute>` | Find all active institutes |
| `findByParentInstituteIdentifier(String parentId)` | `List<Institute>` | Find all franchises of a parent institute |

**Usage Examples:**
```java
// Find institute by slug
Optional<Institute> institute = instituteRepository.findBySlug("allen-career-institute");

// Get all featured institutes for homepage
List<Institute> featured = instituteRepository.findByIsFeaturedTrue();

// Get all franchises of a parent institute
List<Institute> franchises = instituteRepository.findByParentInstituteIdentifier(parentId);
```

---

### BranchRepository
**Package:** `com.classes.Backend.Repository.institute`

**Entity:** [Branch](../Domain/DomainAgent.md#branch)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByInstituteIdentifier(String instituteIdentifier)` | `List<Branch>` | Find all branches of an institute |
| `findByCityIdentifier(String cityIdentifier)` | `List<Branch>` | Find all branches in a city |
| `findByInstituteIdentifierAndIsMainBranchTrue(String instituteId)` | `Optional<Branch>` | Find main branch of an institute |
| `findByIsOnlineOnlyTrue()` | `List<Branch>` | Find all online-only branches |
| `findByIsActiveTrue()` | `List<Branch>` | Find all active branches |

**Usage Examples:**
```java
// Get all branches of an institute
List<Branch> branches = branchRepository.findByInstituteIdentifier(instituteId);

// Find main branch
Optional<Branch> mainBranch = branchRepository.findByInstituteIdentifierAndIsMainBranchTrue(instituteId);

// Get all branches in Mumbai
List<Branch> mumbaiBranches = branchRepository.findByCityIdentifier(mumbaiId);
```

---

### InstituteFacilityRepository
**Package:** `com.classes.Backend.Repository.institute`

**Entity:** [InstituteFacility](../Domain/DomainAgent.md#institutefacility)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByInstituteIdentifier(String instituteIdentifier)` | `Optional<InstituteFacility>` | Find facilities of an institute |

**Usage Examples:**
```java
// Get facilities of an institute
Optional<InstituteFacility> facilities = instituteFacilityRepository.findByInstituteIdentifier(instituteId);
```

---

## 📚 Course Repository

### InstituteCourseRepository
**Package:** `com.classes.Backend.Repository.course`

**Entity:** [InstituteCourse](../Domain/DomainAgent.md#institutecourse)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByInstituteIdentifier(String instituteIdentifier)` | `List<InstituteCourse>` | Find all course offerings of an institute |
| `findByBranchIdentifier(String branchIdentifier)` | `List<InstituteCourse>` | Find courses at a specific branch |

| `findByAdmissionOpenTrue()` | `List<InstituteCourse>` | Find courses with open admissions |
| `findByIsActiveTrue()` | `List<InstituteCourse>` | Find all active institute courses |

**Usage Examples:**
```java
// Get all course offerings of an institute
List<InstituteCourse> courses = instituteCourseRepository.findByInstituteIdentifier(instituteId);

// Find courses with open admissions
List<InstituteCourse> openCourses = instituteCourseRepository.findByAdmissionOpenTrue();
```

---

## 👨‍🏫 Faculty Repository

### FacultyRepository
**Package:** `com.classes.Backend.Repository.faculty`

**Entity:** [Faculty](../Domain/DomainAgent.md#faculty)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByInstituteIdentifier(String instituteIdentifier)` | `List<Faculty>` | Find all faculty of an institute |
| `findByIitIimBackgroundTrue()` | `List<Faculty>` | Find IIT/IIM background faculty |
| `findByNitBackgroundTrue()` | `List<Faculty>` | Find NIT background faculty |
| `findByIsActiveTrue()` | `List<Faculty>` | Find all active faculty |
| `findByExperienceYearsGreaterThan(Integer years)` | `List<Faculty>` | Find experienced faculty |

**Usage Examples:**
```java
// Get all faculty of an institute
List<Faculty> faculty = facultyRepository.findByInstituteIdentifier(instituteId);

// Find IITian faculty (marketing highlight)
List<Faculty> iitFaculty = facultyRepository.findByIitIimBackgroundTrue();

// Find highly experienced faculty
List<Faculty> seniorFaculty = facultyRepository.findByExperienceYearsGreaterThan(10);
```

---

## 🏆 Results Repositories

### ResultRepository
**Package:** `com.classes.Backend.Repository.results`

**Entity:** [Result](../Domain/DomainAgent.md#result)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByInstituteIdentifier(String instituteIdentifier)` | `List<Result>` | Find all results of an institute |
| `findByExamTypeIdentifier(String examTypeIdentifier)` | `List<Result>` | Find results for an exam type |
| `findByExamYear(Integer examYear)` | `List<Result>` | Find results by year |
| `findByInstituteIdentifierAndExamYear(String instituteId, Integer year)` | `List<Result>` | Find institute results by year |
| `findByIsFeaturedTrue()` | `List<Result>` | Find featured results |
| `findByIsVerifiedTrue()` | `List<Result>` | Find verified results |
| `findByRankOrScoreType(RankOrScoreType type)` | `List<Result>` | Find by rank/score type |

**Usage Examples:**
```java
// Get all results of an institute
List<Result> results = resultRepository.findByInstituteIdentifier(instituteId);

// Find JEE 2024 results for an institute
List<Result> jee2024 = resultRepository.findByInstituteIdentifierAndExamYear(instituteId, 2024);

// Get featured results for homepage
List<Result> featuredResults = resultRepository.findByIsFeaturedTrue();
```

---

### AwardAndRecognitionRepository
**Package:** `com.classes.Backend.Repository.results`

**Entity:** [AwardAndRecognition](../Domain/DomainAgent.md#awardandrecognition)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByInstituteIdentifier(String instituteIdentifier)` | `List<AwardAndRecognition>` | Find all awards of an institute |
| `findByYear(Integer year)` | `List<AwardAndRecognition>` | Find awards by year |
| `findByIsVerifiedTrue()` | `List<AwardAndRecognition>` | Find verified awards |

**Usage Examples:**
```java
// Get all awards of an institute
List<AwardAndRecognition> awards = awardAndRecognitionRepository.findByInstituteIdentifier(instituteId);

// Find 2023 awards
List<AwardAndRecognition> awards2023 = awardAndRecognitionRepository.findByYear(2023);
```

---

## ⭐ Reviews Repositories

### ReviewRepository
**Package:** `com.classes.Backend.Repository.reviews`

**Entity:** [Review](../Domain/DomainAgent.md#review)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByInstituteIdentifier(String instituteIdentifier)` | `List<Review>` | Find all reviews of an institute |
| `findByUserIdentifier(String userIdentifier)` | `List<Review>` | Find all reviews by a user |
| `findByStatus(ReviewStatus status)` | `List<Review>` | Find by status (PENDING/APPROVED/etc.) |
| `findByInstituteIdentifierAndStatus(String instituteId, ReviewStatus status)` | `List<Review>` | Find institute reviews by status |
| `findByStandardWhenEnrolled(Standard standard)` | `List<Review>` | Find reviews by enrolled standard |
| `findByWouldRecommendTrue()` | `List<Review>` | Find recommended reviews |
| `findByIsVerifiedStudentTrue()` | `List<Review>` | Find verified student reviews |

**Usage Examples:**
```java
// Get all approved reviews of an institute
List<Review> reviews = reviewRepository.findByInstituteIdentifierAndStatus(instituteId, ReviewStatus.APPROVED);

// Find recommended reviews
List<Review> recommended = reviewRepository.findByWouldRecommendTrue();

// Get reviews by a user
List<Review> userReviews = reviewRepository.findByUserIdentifier(userId);
```

---

### ReviewVoteRepository
**Package:** `com.classes.Backend.Repository.reviews`

**Entity:** [ReviewVote](../Domain/DomainAgent.md#reviewvote)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByReviewIdentifier(String reviewIdentifier)` | `List<ReviewVote>` | Find all votes on a review |
| `findByUserIdentifier(String userIdentifier)` | `List<ReviewVote>` | Find all votes by a user |
| `findByReviewIdentifierAndUserIdentifier(String reviewId, String userId)` | `Optional<ReviewVote>` | Check if user voted on a review |
| `findByVote(VoteType vote)` | `List<ReviewVote>` | Find votes by type (HELPFUL/NOT_HELPFUL) |

**Usage Examples:**
```java
// Get all votes on a review
List<ReviewVote> votes = reviewVoteRepository.findByReviewIdentifier(reviewId);

// Check if user already voted
Optional<ReviewVote> existingVote = reviewVoteRepository.findByReviewIdentifierAndUserIdentifier(reviewId, userId);
```

---

### InstituteResponseRepository
**Package:** `com.classes.Backend.Repository.reviews`

**Entity:** [InstituteResponse](../Domain/DomainAgent.md#instituteresponse)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByReviewIdentifier(String reviewIdentifier)` | `Optional<InstituteResponse>` | Find response to a review |
| `findByInstituteIdentifier(String instituteIdentifier)` | `List<InstituteResponse>` | Find all responses by an institute |
| `findByRespondedBy(String respondedBy)` | `List<InstituteResponse>` | Find all responses by a user |

**Usage Examples:**
```java
// Find institute's response to a review
Optional<InstituteResponse> response = instituteResponseRepository.findByReviewIdentifier(reviewId);

// Get all responses by an institute
List<InstituteResponse> responses = instituteResponseRepository.findByInstituteIdentifier(instituteId);
```

---

## 👤 Users Repositories

### UserRepository
**Package:** `com.classes.Backend.Repository.users`

**Entity:** [User](../Domain/DomainAgent.md#user)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByEmail(String email)` | `Optional<User>` | Find by email address |
| `findByPhone(String phone)` | `Optional<User>` | Find by phone number |
| `findByRole(UserRole role)` | `List<User>` | Find users by role |
| `findByCityIdentifier(String cityIdentifier)` | `List<User>` | Find users in a city |
| `findByIsActiveTrue()` | `List<User>` | Find all active users |
| `existsByEmail(String email)` | `boolean` | Check if email exists |
| `existsByPhone(String phone)` | `boolean` | Check if phone exists |

**Usage Examples:**
```java
// Find user by email
Optional<User> user = userRepository.findByEmail("student@example.com");

// Check if email is available
boolean emailExists = userRepository.existsByEmail("student@example.com");

// Find all institute admins
List<User> admins = userRepository.findByRole(UserRole.INSTITUTE_ADMIN);
```

---

### UserInstituteAssociationRepository
**Package:** `com.classes.Backend.Repository.users`

**Entity:** [UserInstituteAssociation](../Domain/DomainAgent.md#userinstituteassociation)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByUserIdentifier(String userIdentifier)` | `List<UserInstituteAssociation>` | Find all institute associations of a user |
| `findByInstituteIdentifier(String instituteIdentifier)` | `List<UserInstituteAssociation>` | Find all users associated with an institute |
| `findByUserIdentifierAndInstituteIdentifier(String userId, String instituteId)` | `Optional<UserInstituteAssociation>` | Find specific association |
| `findByRole(InstituteStaffRole role)` | `List<UserInstituteAssociation>` | Find by role (OWNER/ADMIN/STAFF) |
| `findByIsActiveTrue()` | `List<UserInstituteAssociation>` | Find active associations |

**Usage Examples:**
```java
// Find all institutes a user is associated with
List<UserInstituteAssociation> associations = userInstituteAssociationRepository.findByUserIdentifier(userId);

// Find owner of an institute
Optional<UserInstituteAssociation> owner = userInstituteAssociationRepository
    .findByInstituteIdentifier(instituteId)
    .stream()
    .filter(a -> a.getRole() == InstituteStaffRole.OWNER)
    .findFirst();
```

---

### BookmarkRepository
**Package:** `com.classes.Backend.Repository.users`

**Entity:** [Bookmark](../Domain/DomainAgent.md#bookmark)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByUserIdentifier(String userIdentifier)` | `List<Bookmark>` | Find all bookmarks of a user |
| `findByUserIdentifierAndEntityType(String userId, BookmarkEntityType type)` | `List<Bookmark>` | Find bookmarks by type |
| `findByUserIdentifierAndEntityTypeAndEntityIdentifier(String userId, BookmarkEntityType type, String entityId)` | `Optional<Bookmark>` | Check specific bookmark |
| `existsByUserIdentifierAndEntityTypeAndEntityIdentifier(String userId, BookmarkEntityType type, String entityId)` | `boolean` | Check if bookmark exists |

**Usage Examples:**
```java
// Get all bookmarks of a user
List<Bookmark> bookmarks = bookmarkRepository.findByUserIdentifier(userId);

// Get only institute bookmarks
List<Bookmark> instituteBookmarks = bookmarkRepository.findByUserIdentifierAndEntityType(userId, BookmarkEntityType.INSTITUTE);

// Check if user bookmarked an institute
boolean isBookmarked = bookmarkRepository.existsByUserIdentifierAndEntityTypeAndEntityIdentifier(userId, BookmarkEntityType.INSTITUTE, instituteId);
```

---

## 📞 Leads Repository

### InquiryRepository
**Package:** `com.classes.Backend.Repository.leads`

**Entity:** [Inquiry](../Domain/DomainAgent.md#inquiry)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByInstituteIdentifier(String instituteIdentifier)` | `List<Inquiry>` | Find all inquiries for an institute |
| `findByBranchIdentifier(String branchIdentifier)` | `List<Inquiry>` | Find inquiries for a branch |
| `findByCourseIdentifier(String courseIdentifier)` | `List<Inquiry>` | Find inquiries for a course |
| `findByUserIdentifier(String userIdentifier)` | `List<Inquiry>` | Find inquiries by a user |
| `findByStatus(InquiryStatus status)` | `List<Inquiry>` | Find by status (NEW/CONTACTED/etc.) |
| `findBySource(InquirySource source)` | `List<Inquiry>` | Find by source (LISTING_PAGE/etc.) |
| `findByAssignedTo(String assignedTo)` | `List<Inquiry>` | Find inquiries assigned to staff |
| `findByInstituteIdentifierAndStatus(String instituteId, InquiryStatus status)` | `List<Inquiry>` | Find institute inquiries by status |

**Usage Examples:**
```java
// Get all new inquiries for an institute
List<Inquiry> newInquiries = inquiryRepository.findByInstituteIdentifierAndStatus(instituteId, InquiryStatus.NEW);

// Find inquiries assigned to a staff member
List<Inquiry> myInquiries = inquiryRepository.findByAssignedTo(staffUserId);

// Find inquiries from course page
List<Inquiry> coursePageLeads = inquiryRepository.findBySource(InquirySource.COURSE_PAGE);
```

---

## 🖼️ Media Repository

### MediaRepository
**Package:** `com.classes.Backend.Repository.media`

**Entity:** [Media](../Domain/DomainAgent.md#media)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByInstituteIdentifier(String instituteIdentifier)` | `List<Media>` | Find all media of an institute |
| `findByBranchIdentifier(String branchIdentifier)` | `List<Media>` | Find media of a branch |
| `findByEntityType(MediaEntityType entityType)` | `List<Media>` | Find by entity type |
| `findByMediaType(MediaType mediaType)` | `List<Media>` | Find by media type (IMAGE/VIDEO/etc.) |
| `findByInstituteIdentifierAndEntityType(String instituteId, MediaEntityType type)` | `List<Media>` | Find institute media by type |
| `findByIsFeaturedTrue()` | `List<Media>` | Find featured media |

**Usage Examples:**
```java
// Get all media of an institute
List<Media> media = mediaRepository.findByInstituteIdentifier(instituteId);

// Get faculty photos
List<Media> facultyPhotos = mediaRepository.findByInstituteIdentifierAndEntityType(instituteId, MediaEntityType.FACULTY);

// Get featured images for gallery
List<Media> featured = mediaRepository.findByIsFeaturedTrue();
```

---

## 💳 Subscription Repositories

### SubscriptionPlanRepository
**Package:** `com.classes.Backend.Repository.subscription`

**Entity:** [SubscriptionPlan](../Domain/DomainAgent.md#subscriptionplan)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByName(SubscriptionTier name)` | `Optional<SubscriptionPlan>` | Find by tier name (FREE/BASIC/etc.) |

**Usage Examples:**
```java
// Get premium plan details
Optional<SubscriptionPlan> premiumPlan = subscriptionPlanRepository.findByName(SubscriptionTier.PREMIUM);
```

---

### InstituteSubscriptionRepository
**Package:** `com.classes.Backend.Repository.subscription`

**Entity:** [InstituteSubscription](../Domain/DomainAgent.md#institutesubscription)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByInstituteIdentifier(String instituteIdentifier)` | `Optional<InstituteSubscription>` | Find subscription of an institute |
| `findByPlanIdentifier(String planIdentifier)` | `List<InstituteSubscription>` | Find institutes with a plan |
| `findByIsActiveTrue()` | `List<InstituteSubscription>` | Find active subscriptions |

**Usage Examples:**
```java
// Get subscription of an institute
Optional<InstituteSubscription> subscription = instituteSubscriptionRepository.findByInstituteIdentifier(instituteId);

// Find all institutes with premium plan
List<InstituteSubscription> premiumInstitutes = instituteSubscriptionRepository.findByPlanIdentifier(premiumPlanId);
```

---

## 🔔 Notification Repositories

### NotificationRepository
**Package:** `com.classes.Backend.Repository.notification`

**Entity:** [Notification](../Domain/DomainAgent.md#notification)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByUserIdentifier(String userIdentifier)` | `List<Notification>` | Find all notifications of a user |
| `findByUserIdentifierAndIsReadFalse(String userIdentifier)` | `List<Notification>` | Find unread notifications |
| `findByType(NotificationType type)` | `List<Notification>` | Find by type |

**Usage Examples:**
```java
// Get all notifications of a user
List<Notification> notifications = notificationRepository.findByUserIdentifier(userId);

// Get unread notification count
List<Notification> unread = notificationRepository.findByUserIdentifierAndIsReadFalse(userId);
int unreadCount = unread.size();
```

---

### FaqRepository
**Package:** `com.classes.Backend.Repository.notification`

**Entity:** [Faq](../Domain/DomainAgent.md#faq)

**Methods:**

| Method | Return Type | Description |
|--------|-------------|-------------|
| `findByInstituteIdentifier(String instituteIdentifier)` | `List<Faq>` | Find all FAQs of an institute |
| `findByInstituteIdentifierAndIsActiveTrue(String instituteIdentifier)` | `List<Faq>` | Find active FAQs |

**Usage Examples:**
```java
// Get all FAQs of an institute
List<Faq> faqs = faqRepository.findByInstituteIdentifier(instituteId);

// Get active FAQs only
List<Faq> activeFaqs = faqRepository.findByInstituteIdentifierAndIsActiveTrue(instituteId);
```

---

## 📝 Changelog

| Date | Changes |
|------|---------|
| 2026-04-06 | Initial creation of all repository interfaces and documentation |

---

> **Note for AI Agents:** When reading this file, use it as the canonical reference for the repository layer. Cross-reference with [DomainAgent.md](../Domain/DomainAgent.md) for entity details. If you find discrepancies between actual code and this documentation, update this file to match the code (unless the code is clearly buggy).
