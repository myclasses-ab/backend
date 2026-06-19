// package com.classes.Backend.config;

// import java.math.BigDecimal;
// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.UUID;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component;
// import org.springframework.transaction.annotation.Transactional;

// import com.classes.Backend.Domain.course.InstituteCourse;
// import com.classes.Backend.Domain.enums.BookmarkEntityType;
// import com.classes.Backend.Domain.enums.ExamLevel;
// import com.classes.Backend.Domain.enums.InquirySource;
// import com.classes.Backend.Domain.enums.InquiryStatus;
// import com.classes.Backend.Domain.enums.InstituteStaffRole;
// import com.classes.Backend.Domain.enums.InstituteType;
// import com.classes.Backend.Domain.enums.LeadDistributionStatus;
// import com.classes.Backend.Domain.enums.LeadSource;
// import com.classes.Backend.Domain.enums.LeadStatus;
// import com.classes.Backend.Domain.enums.MediaEntityType;
// import com.classes.Backend.Domain.enums.MediaType;
// import com.classes.Backend.Domain.enums.NotificationType;
// import com.classes.Backend.Domain.enums.OwnershipType;
// import com.classes.Backend.Domain.enums.RankOrScoreType;
// import com.classes.Backend.Domain.enums.ReviewStatus;
// import com.classes.Backend.Domain.enums.Standard;
// import com.classes.Backend.Domain.enums.SubscriptionTier;
// import com.classes.Backend.Domain.enums.UserRole;
// import com.classes.Backend.Domain.enums.VoteType;
// import com.classes.Backend.Domain.faculty.Faculty;
// import com.classes.Backend.Domain.institute.Branch;
// import com.classes.Backend.Domain.institute.Institute;
// import com.classes.Backend.Domain.institute.InstituteFacility;
// import com.classes.Backend.Domain.leads.Inquiry;
// import com.classes.Backend.Domain.leads.Lead;
// import com.classes.Backend.Domain.leads.LeadDistribution;
// import com.classes.Backend.Domain.master.City;
// import com.classes.Backend.Domain.master.ExamType;
// import com.classes.Backend.Domain.master.Stream;
// import com.classes.Backend.Domain.master.Subject;
// import com.classes.Backend.Domain.media.Media;
// import com.classes.Backend.Domain.notification.Faq;
// import com.classes.Backend.Domain.notification.Notification;
// import com.classes.Backend.Domain.results.AwardAndRecognition;
// import com.classes.Backend.Domain.results.Result;
// import com.classes.Backend.Domain.reviews.InstituteResponse;
// import com.classes.Backend.Domain.reviews.Review;
// import com.classes.Backend.Domain.reviews.ReviewVote;
// import com.classes.Backend.Domain.subscription.InstituteSubscription;
// import com.classes.Backend.Domain.subscription.SubscriptionPlan;
// import com.classes.Backend.Domain.users.Bookmark;
// import com.classes.Backend.Domain.users.User;
// import com.classes.Backend.Domain.users.UserInstituteAssociation;
// import com.classes.Backend.Service.course.InstituteCourseService;
// import com.classes.Backend.Service.faculty.FacultyService;
// import com.classes.Backend.Service.institute.BranchService;
// import com.classes.Backend.Service.institute.InstituteFacilityService;
// import com.classes.Backend.Service.institute.InstituteService;
// import com.classes.Backend.Service.leads.InquiryService;
// import com.classes.Backend.Service.leads.LeadDistributionService;
// import com.classes.Backend.Service.leads.LeadService;
// import com.classes.Backend.Service.master.CityService;
// import com.classes.Backend.Service.master.ExamTypeService;
// import com.classes.Backend.Service.master.StreamService;
// import com.classes.Backend.Service.master.SubjectService;
// import com.classes.Backend.Service.media.MediaService;
// import com.classes.Backend.Service.notification.FaqService;
// import com.classes.Backend.Service.notification.NotificationService;
// import com.classes.Backend.Service.results.AwardAndRecognitionService;
// import com.classes.Backend.Service.results.ResultService;
// import com.classes.Backend.Service.reviews.InstituteResponseService;
// import com.classes.Backend.Service.reviews.ReviewService;
// import com.classes.Backend.Service.reviews.ReviewVoteService;
// import com.classes.Backend.Service.subscription.InstituteSubscriptionService;
// import com.classes.Backend.Service.subscription.SubscriptionPlanService;
// import com.classes.Backend.Service.users.BookmarkService;
// import com.classes.Backend.Service.users.UserInstituteAssociationService;
// import com.classes.Backend.Service.users.UserService;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Component
// @RequiredArgsConstructor
// public class DataSeeder implements CommandLineRunner {

//     private final SubscriptionPlanService SUBSCRIPTION_PLAN_SERVICE;
//     private final CityService CITY_SERVICE;
//     private final StreamService STREAM_SERVICE;
//     private final SubjectService SUBJECT_SERVICE;
//     private final ExamTypeService EXAM_TYPE_SERVICE;
//     private final UserService USER_SERVICE;
//     private final InstituteService INSTITUTE_SERVICE;
//     private final BranchService BRANCH_SERVICE;
//     private final InstituteFacilityService INSTITUTE_FACILITY_SERVICE;
//     private final InstituteSubscriptionService INSTITUTE_SUBSCRIPTION_SERVICE;
//     private final UserInstituteAssociationService USER_INSTITUTE_ASSOCIATION_SERVICE;
//     private final InstituteCourseService INSTITUTE_COURSE_SERVICE;
//     private final FacultyService FACULTY_SERVICE;
//     private final ResultService RESULT_SERVICE;
//     private final AwardAndRecognitionService AWARD_AND_RECOGNITION_SERVICE;
//     private final ReviewService REVIEW_SERVICE;
//     private final ReviewVoteService REVIEW_VOTE_SERVICE;
//     private final InstituteResponseService INSTITUTE_RESPONSE_SERVICE;
//     private final FaqService FAQ_SERVICE;
//     private final MediaService MEDIA_SERVICE;
//     private final InquiryService INQUIRY_SERVICE;
//     private final LeadService LEAD_SERVICE;
//     private final LeadDistributionService LEAD_DISTRIBUTION_SERVICE;
//     private final NotificationService NOTIFICATION_SERVICE;
//     private final BookmarkService BOOKMARK_SERVICE;
//     private final PasswordEncoder PASSWORD_ENCODER;

//     private final Map<String, String> ids = new HashMap<>();

//     @Override
//     @Transactional
//     public void run(String... args) throws Exception {
//         log.info("========== DATA SEEDING STARTED ==========");

//         // Idempotency check
//         if (INSTITUTE_SERVICE.findBySlug("abhishek-classes").isPresent()) {
//             log.info("Data already seeded. Skipping.");
//             return;
//         }

//         seedSubscriptionPlans();
//         seedCities();
//         seedStreams();
//         seedSubjects();
//         seedExamTypes();
//         seedStudentUsers();
//         seedSuperAdmin();
//         seedInstitutes();
//         seedBranches();
//         seedInstituteFacilities();
//         seedInstituteSubscriptions();
//         seedAdminUsers();
//         seedUserInstituteAssociations();
//         seedInstituteCourses();
//         seedFaculty();
//         seedResults();
//         seedAwardsAndRecognitions();
//         seedReviews();
//         seedReviewVotes();
//         seedInstituteResponses();
//         seedFaqs();
//         seedMedia();
//         seedInquiries();
//         seedLeads();
//         seedLeadDistributions();
//         seedNotifications();
//         seedBookmarks();

//         log.info("========== DATA SEEDING COMPLETED SUCCESSFULLY ==========");
//     }

//     private void put(String key, String value) {
//         ids.put(key, value);
//     }

//     private String get(String key) {
//         return ids.get(key);
//     }

//     private String uuid() {
//         return UUID.randomUUID().toString();
//     }

//     // ==================== SUBSCRIPTION PLANS ====================
//     private void seedSubscriptionPlans() {
//         log.info("Seeding subscription plans...");

//         SubscriptionPlan free = new SubscriptionPlan();
//         free.setIdentifier(uuid()); put("plan-free", free.getIdentifier());
//         free.setName(SubscriptionTier.FREE);
//         free.setPriceMonthly(new BigDecimal("0"));
//         free.setPriceYearly(new BigDecimal("0"));
//         free.setMaxBranches(1);
//         free.setMaxCourses(3);
//         free.setMaxFaculty(5);
//         free.setMaxMediaUploads(10);
//         free.setCanRespondToReviews(false);
//         free.setCanViewLeads(false);
//         free.setCanFeatureResults(false);
//         free.setPriorityInSearch(0);
//         free.setBadgeShown("Free");
//         free.setIsActive(true);

//         SubscriptionPlan basic = new SubscriptionPlan();
//         basic.setIdentifier(uuid()); put("plan-basic", basic.getIdentifier());
//         basic.setName(SubscriptionTier.BASIC);
//         basic.setPriceMonthly(new BigDecimal("1999"));
//         basic.setPriceYearly(new BigDecimal("19990"));
//         basic.setMaxBranches(2);
//         basic.setMaxCourses(10);
//         basic.setMaxFaculty(15);
//         basic.setMaxMediaUploads(50);
//         basic.setCanRespondToReviews(true);
//         basic.setCanViewLeads(true);
//         basic.setCanFeatureResults(false);
//         basic.setPriorityInSearch(1);
//         basic.setBadgeShown("Basic");
//         basic.setIsActive(true);

//         SubscriptionPlan premium = new SubscriptionPlan();
//         premium.setIdentifier(uuid()); put("plan-premium", premium.getIdentifier());
//         premium.setName(SubscriptionTier.PREMIUM);
//         premium.setPriceMonthly(new BigDecimal("4999"));
//         premium.setPriceYearly(new BigDecimal("49990"));
//         premium.setMaxBranches(5);
//         premium.setMaxCourses(25);
//         premium.setMaxFaculty(50);
//         premium.setMaxMediaUploads(200);
//         premium.setCanRespondToReviews(true);
//         premium.setCanViewLeads(true);
//         premium.setCanFeatureResults(true);
//         premium.setPriorityInSearch(2);
//         premium.setBadgeShown("Premium");
//         premium.setIsActive(true);

//         SubscriptionPlan featured = new SubscriptionPlan();
//         featured.setIdentifier(uuid()); put("plan-featured", featured.getIdentifier());
//         featured.setName(SubscriptionTier.FEATURED);
//         featured.setPriceMonthly(new BigDecimal("9999"));
//         featured.setPriceYearly(new BigDecimal("99990"));
//         featured.setMaxBranches(10);
//         featured.setMaxCourses(50);
//         featured.setMaxFaculty(100);
//         featured.setMaxMediaUploads(500);
//         featured.setCanRespondToReviews(true);
//         featured.setCanViewLeads(true);
//         featured.setCanFeatureResults(true);
//         featured.setPriorityInSearch(3);
//         featured.setBadgeShown("Featured");
//         featured.setIsActive(true);

//         SUBSCRIPTION_PLAN_SERVICE.saveAll(List.of(free, basic, premium, featured));
//         log.info("Subscription plans seeded.");
//     }

//     // ==================== CITIES ====================
//     private void seedCities() {
//         log.info("Seeding cities...");

//         City mumbai = new City();
//         mumbai.setIdentifier(uuid()); put("city-mumbai", mumbai.getIdentifier());
//         mumbai.setName("Mumbai");
//         mumbai.setState("Maharashtra");
//         mumbai.setStateCode("MH");
//         mumbai.setPincodePrefix("400");
//         mumbai.setIsMetro(true);
//         mumbai.setLatitude(new BigDecimal("19.0760"));
//         mumbai.setLongitude(new BigDecimal("72.8777"));

//         City pune = new City();
//         pune.setIdentifier(uuid()); put("city-pune", pune.getIdentifier());
//         pune.setName("Pune");
//         pune.setState("Maharashtra");
//         pune.setStateCode("MH");
//         pune.setPincodePrefix("411");
//         pune.setIsMetro(true);
//         pune.setLatitude(new BigDecimal("18.5204"));
//         pune.setLongitude(new BigDecimal("73.8567"));

//         City delhi = new City();
//         delhi.setIdentifier(uuid()); put("city-delhi", delhi.getIdentifier());
//         delhi.setName("New Delhi");
//         delhi.setState("Delhi");
//         delhi.setStateCode("DL");
//         delhi.setPincodePrefix("110");
//         delhi.setIsMetro(true);
//         delhi.setLatitude(new BigDecimal("28.6139"));
//         delhi.setLongitude(new BigDecimal("77.2090"));

//         City bangalore = new City();
//         bangalore.setIdentifier(uuid()); put("city-bangalore", bangalore.getIdentifier());
//         bangalore.setName("Bangalore");
//         bangalore.setState("Karnataka");
//         bangalore.setStateCode("KA");
//         bangalore.setPincodePrefix("560");
//         bangalore.setIsMetro(true);
//         bangalore.setLatitude(new BigDecimal("12.9716"));
//         bangalore.setLongitude(new BigDecimal("77.5946"));

//         City kota = new City();
//         kota.setIdentifier(uuid()); put("city-kota", kota.getIdentifier());
//         kota.setName("Kota");
//         kota.setState("Rajasthan");
//         kota.setStateCode("RJ");
//         kota.setPincodePrefix("324");
//         kota.setIsMetro(false);
//         kota.setLatitude(new BigDecimal("25.2138"));
//         kota.setLongitude(new BigDecimal("75.8648"));

//         City nagpur = new City();
//         nagpur.setIdentifier(uuid()); put("city-nagpur", nagpur.getIdentifier());
//         nagpur.setName("Nagpur");
//         nagpur.setState("Maharashtra");
//         nagpur.setStateCode("MH");
//         nagpur.setPincodePrefix("440");
//         nagpur.setIsMetro(false);
//         nagpur.setLatitude(new BigDecimal("21.1458"));
//         nagpur.setLongitude(new BigDecimal("79.0882"));

//         City thane = new City();
//         thane.setIdentifier(uuid()); put("city-thane", thane.getIdentifier());
//         thane.setName("Thane");
//         thane.setState("Maharashtra");
//         thane.setStateCode("MH");
//         thane.setPincodePrefix("400");
//         thane.setIsMetro(false);
//         thane.setLatitude(new BigDecimal("19.2183"));
//         thane.setLongitude(new BigDecimal("72.9781"));

//         CITY_SERVICE.saveAll(List.of(mumbai, pune, delhi, bangalore, kota, nagpur, thane));
//         log.info("Cities seeded.");
//     }

//     // ==================== STREAMS ====================
//     private void seedStreams() {
//         log.info("Seeding streams...");

//         Stream science = new Stream();
//         science.setIdentifier(uuid()); put("stream-science", science.getIdentifier());
//         science.setName("Science");
//         science.setSlug("science");
//         science.setDescription("Science stream covering Physics, Chemistry, Mathematics, and Biology for competitive exams");
//         science.setIconUrl("science.jpg");
//         science.setIsActive(true);
//         science.setDisplayOrder(1);

//         Stream commerce = new Stream();
//         commerce.setIdentifier(uuid()); put("stream-commerce", commerce.getIdentifier());
//         commerce.setName("Commerce");
//         commerce.setSlug("commerce");
//         commerce.setDescription("Commerce stream covering Accountancy, Business Studies, Economics, and Mathematics");
//         commerce.setIconUrl("commerce.jpg");
//         commerce.setIsActive(true);
//         commerce.setDisplayOrder(2);

//         Stream arts = new Stream();
//         arts.setIdentifier(uuid()); put("stream-arts", arts.getIdentifier());
//         arts.setName("Arts/Humanities");
//         arts.setSlug("arts-humanities");
//         arts.setDescription("Arts stream covering History, Geography, Political Science, Psychology, and Sociology");
//         arts.setIconUrl("arts.jpg");
//         arts.setIsActive(true);
//         arts.setDisplayOrder(3);

//         STREAM_SERVICE.saveAll(List.of(science, commerce, arts));
//         log.info("Streams seeded.");
//     }

//     // ==================== SUBJECTS ====================
//     private void seedSubjects() {
//         log.info("Seeding subjects...");

//         Subject physics = new Subject();
//         physics.setIdentifier(uuid()); put("sub-physics", physics.getIdentifier());
//         physics.setName("Physics");
//         physics.setSlug("physics");
//         physics.setStreamIdentifier(get("stream-science"));
//         physics.setIsActive(true);

//         Subject chemistry = new Subject();
//         chemistry.setIdentifier(uuid()); put("sub-chemistry", chemistry.getIdentifier());
//         chemistry.setName("Chemistry");
//         chemistry.setSlug("chemistry");
//         chemistry.setStreamIdentifier(get("stream-science"));
//         chemistry.setIsActive(true);

//         Subject mathematics = new Subject();
//         mathematics.setIdentifier(uuid()); put("sub-mathematics", mathematics.getIdentifier());
//         mathematics.setName("Mathematics");
//         mathematics.setSlug("mathematics");
//         mathematics.setStreamIdentifier(get("stream-science"));
//         mathematics.setIsActive(true);

//         Subject biology = new Subject();
//         biology.setIdentifier(uuid()); put("sub-biology", biology.getIdentifier());
//         biology.setName("Biology");
//         biology.setSlug("biology");
//         biology.setStreamIdentifier(get("stream-science"));
//         biology.setIsActive(true);

//         Subject accounts = new Subject();
//         accounts.setIdentifier(uuid()); put("sub-accounts", accounts.getIdentifier());
//         accounts.setName("Accountancy");
//         accounts.setSlug("accountancy");
//         accounts.setStreamIdentifier(get("stream-commerce"));
//         accounts.setIsActive(true);

//         Subject bst = new Subject();
//         bst.setIdentifier(uuid()); put("sub-bst", bst.getIdentifier());
//         bst.setName("Business Studies");
//         bst.setSlug("business-studies");
//         bst.setStreamIdentifier(get("stream-commerce"));
//         bst.setIsActive(true);

//         Subject economics = new Subject();
//         economics.setIdentifier(uuid()); put("sub-economics", economics.getIdentifier());
//         economics.setName("Economics");
//         economics.setSlug("economics");
//         economics.setStreamIdentifier(get("stream-commerce"));
//         economics.setIsActive(true);

//         Subject english = new Subject();
//         english.setIdentifier(uuid()); put("sub-english", english.getIdentifier());
//         english.setName("English");
//         english.setSlug("english");
//         english.setStreamIdentifier(get("stream-arts"));
//         english.setIsActive(true);

//         SUBJECT_SERVICE.saveAll(List.of(physics, chemistry, mathematics, biology, accounts, bst, economics, english));
//         log.info("Subjects seeded.");
//     }

//     // ==================== EXAM TYPES ====================
//     private void seedExamTypes() {
//         log.info("Seeding exam types...");

//         ExamType jeeMain = new ExamType();
//         jeeMain.setIdentifier(uuid()); put("exam-jee-main", jeeMain.getIdentifier());
//         jeeMain.setName("JEE Main");
//         jeeMain.setSlug("jee-main");
//         jeeMain.setStreamIdentifier(get("stream-science"));
//         jeeMain.setStandard("11");
//         jeeMain.setConductingBody("National Testing Agency (NTA)");
//         jeeMain.setExamLevel(ExamLevel.NATIONAL);
//         jeeMain.setDescription("Joint Entrance Examination Main for admission to NITs, IIITs and other engineering colleges");
//         jeeMain.setIsActive(true);
//         jeeMain.setDisplayOrder(1);

//         ExamType jeeAdv = new ExamType();
//         jeeAdv.setIdentifier(uuid()); put("exam-jee-adv", jeeAdv.getIdentifier());
//         jeeAdv.setName("JEE Advanced");
//         jeeAdv.setSlug("jee-advanced");
//         jeeAdv.setStreamIdentifier(get("stream-science"));
//         jeeAdv.setStandard("12");
//         jeeAdv.setConductingBody("IITs (Rotating)");
//         jeeAdv.setExamLevel(ExamLevel.NATIONAL);
//         jeeAdv.setDescription("Joint Entrance Examination Advanced for admission to Indian Institutes of Technology");
//         jeeAdv.setIsActive(true);
//         jeeAdv.setDisplayOrder(2);

//         ExamType neet = new ExamType();
//         neet.setIdentifier(uuid()); put("exam-neet", neet.getIdentifier());
//         neet.setName("NEET");
//         neet.setSlug("neet");
//         neet.setStreamIdentifier(get("stream-science"));
//         neet.setStandard("12");
//         neet.setConductingBody("National Testing Agency (NTA)");
//         neet.setExamLevel(ExamLevel.NATIONAL);
//         neet.setDescription("National Eligibility cum Entrance Test for admission to MBBS/BDS courses");
//         neet.setIsActive(true);
//         neet.setDisplayOrder(3);

//         ExamType mhtCet = new ExamType();
//         mhtCet.setIdentifier(uuid()); put("exam-mht-cet", mhtCet.getIdentifier());
//         mhtCet.setName("MHT CET");
//         mhtCet.setSlug("mht-cet");
//         mhtCet.setStreamIdentifier(get("stream-science"));
//         mhtCet.setStandard("12");
//         mhtCet.setConductingBody("State CET Cell, Maharashtra");
//         mhtCet.setExamLevel(ExamLevel.STATE);
//         mhtCet.setDescription("Maharashtra Common Entrance Test for engineering and pharmacy admissions in Maharashtra");
//         mhtCet.setIsActive(true);
//         mhtCet.setDisplayOrder(4);

//         ExamType cbse = new ExamType();
//         cbse.setIdentifier(uuid()); put("exam-cbse", cbse.getIdentifier());
//         cbse.setName("CBSE Board");
//         cbse.setSlug("cbse-board");
//         cbse.setStreamIdentifier(get("stream-science"));
//         cbse.setStandard("10");
//         cbse.setConductingBody("Central Board of Secondary Education");
//         cbse.setExamLevel(ExamLevel.NATIONAL);
//         cbse.setDescription("Central Board of Secondary Education examinations for classes 10 and 12");
//         cbse.setIsActive(true);
//         cbse.setDisplayOrder(5);

//         EXAM_TYPE_SERVICE.saveAll(List.of(jeeMain, jeeAdv, neet, mhtCet, cbse));
//         log.info("Exam types seeded.");
//     }

//     // ==================== STUDENT USERS ====================
//     private void seedStudentUsers() {
//         log.info("Seeding student users...");

//         User student1 = new User();
//         student1.setIdentifier(uuid()); put("user-student-1", student1.getIdentifier());
//         student1.setFullName("Rahul Sharma");
//         student1.setEmail("rahul.sharma@email.com");
//         student1.setPhone("9876543210");
//         student1.setPhoneVerified(true);
//         student1.setEmailVerified(true);
//         student1.setPasswordHash(PASSWORD_ENCODER.encode("student123"));
//         student1.setAvatarUrl("https://example.com/avatars/rahul.jpg");
//         student1.setRole(UserRole.STUDENT);
//         student1.setCurrentStandard(Standard.STANDARD_12);
//         student1.setTargetExamIdentifiers(List.of(get("exam-jee-main"), get("exam-jee-adv")));
//         student1.setCityIdentifier(get("city-mumbai"));
//         student1.setState("Maharashtra");
//         student1.setPincode("400001");
//         student1.setSchoolCollegeName("St. Xavier's College, Mumbai");
//         student1.setPreferredLanguage("English");
//         student1.setIsActive(true);

//         User student2 = new User();
//         student2.setIdentifier(uuid()); put("user-student-2", student2.getIdentifier());
//         student2.setFullName("Priya Patel");
//         student2.setEmail("priya.patel@email.com");
//         student2.setPhone("9876543211");
//         student2.setPhoneVerified(true);
//         student2.setEmailVerified(true);
//         student2.setPasswordHash(PASSWORD_ENCODER.encode("student123"));
//         student2.setAvatarUrl("https://example.com/avatars/priya.jpg");
//         student2.setRole(UserRole.STUDENT);
//         student2.setCurrentStandard(Standard.DROPPER);
//         student2.setTargetExamIdentifiers(List.of(get("exam-neet")));
//         student2.setCityIdentifier(get("city-kota"));
//         student2.setState("Rajasthan");
//         student2.setPincode("324005");
//         student2.setSchoolCollegeName("Allen Career Institute");
//         student2.setPreferredLanguage("English");
//         student2.setIsActive(true);

//         User student3 = new User();
//         student3.setIdentifier(uuid()); put("user-student-3", student3.getIdentifier());
//         student3.setFullName("Ankit Mishra");
//         student3.setEmail("ankit.mishra@email.com");
//         student3.setPhone("9876543212");
//         student3.setPhoneVerified(true);
//         student3.setEmailVerified(true);
//         student3.setPasswordHash(PASSWORD_ENCODER.encode("student123"));
//         student3.setAvatarUrl("https://example.com/avatars/ankit.jpg");
//         student3.setRole(UserRole.STUDENT);
//         student3.setCurrentStandard(Standard.STANDARD_11);
//         student3.setTargetExamIdentifiers(List.of(get("exam-jee-main")));
//         student3.setCityIdentifier(get("city-nagpur"));
//         student3.setState("Maharashtra");
//         student3.setPincode("440001");
//         student3.setSchoolCollegeName("Kendriya Vidyalaya, Nagpur");
//         student3.setPreferredLanguage("Hindi");
//         student3.setIsActive(true);

//         User student4 = new User();
//         student4.setIdentifier(uuid()); put("user-student-4", student4.getIdentifier());
//         student4.setFullName("Sneha Gupta");
//         student4.setEmail("sneha.gupta@email.com");
//         student4.setPhone("9876543213");
//         student4.setPhoneVerified(true);
//         student4.setEmailVerified(true);
//         student4.setPasswordHash(PASSWORD_ENCODER.encode("student123"));
//         student4.setAvatarUrl("https://example.com/avatars/sneha.jpg");
//         student4.setRole(UserRole.STUDENT);
//         student4.setCurrentStandard(Standard.STANDARD_12);
//         student4.setTargetExamIdentifiers(List.of(get("exam-neet"), get("exam-mht-cet")));
//         student4.setCityIdentifier(get("city-pune"));
//         student4.setState("Maharashtra");
//         student4.setPincode("411001");
//         student4.setSchoolCollegeName("Fergusson College, Pune");
//         student4.setPreferredLanguage("English");
//         student4.setIsActive(true);

//         User student5 = new User();
//         student5.setIdentifier(uuid()); put("user-student-5", student5.getIdentifier());
//         student5.setFullName("Vikram Rao");
//         student5.setEmail("vikram.rao@email.com");
//         student5.setPhone("9876543214");
//         student5.setPhoneVerified(true);
//         student5.setEmailVerified(true);
//         student5.setPasswordHash(PASSWORD_ENCODER.encode("student123"));
//         student5.setAvatarUrl("https://example.com/avatars/vikram.jpg");
//         student5.setRole(UserRole.STUDENT);
//         student5.setCurrentStandard(Standard.STANDARD_10);
//         student5.setTargetExamIdentifiers(List.of(get("exam-cbse")));
//         student5.setCityIdentifier(get("city-delhi"));
//         student5.setState("Delhi");
//         student5.setPincode("110001");
//         student5.setSchoolCollegeName("Delhi Public School");
//         student5.setPreferredLanguage("English");
//         student5.setIsActive(true);

//         USER_SERVICE.saveAll(List.of(student1, student2, student3, student4, student5));
//         log.info("Student users seeded.");
//     }

//     // ==================== SUPER ADMIN ====================
//     private void seedSuperAdmin() {
//         log.info("Seeding super admin...");

//         User superAdmin = new User();
//         superAdmin.setIdentifier(uuid()); put("user-super-admin", superAdmin.getIdentifier());
//         superAdmin.setFullName("Super Admin");
//         superAdmin.setEmail("superadmin@myclasses.com");
//         superAdmin.setPhone("9999999999");
//         superAdmin.setPhoneVerified(true);
//         superAdmin.setEmailVerified(true);
//         superAdmin.setPasswordHash(PASSWORD_ENCODER.encode("superadmin123"));
//         superAdmin.setRole(UserRole.SUPER_ADMIN);
//         superAdmin.setIsActive(true);

//         USER_SERVICE.save(superAdmin);
//         log.info("Super admin seeded.");
//     }

//     // ==================== INSTITUTES ====================
//     private void seedInstitutes() {
//         log.info("Seeding institutes...");

//         Institute abhishek = new Institute();
//         abhishek.setIdentifier(uuid()); put("inst-abhishek", abhishek.getIdentifier());
//         abhishek.setName("Abhishek Classes");
//         abhishek.setSlug("abhishek-classes");
//         abhishek.setTagline("Where Knowledge Meets Excellence");
//         abhishek.setDescription("Abhishek Classes is a premier coaching institute in Nagpur, specializing in JEE, NEET, and foundation courses. With over 15 years of excellence, we have produced thousands of successful engineers and doctors. Our experienced faculty, comprehensive study material, and rigorous test series make us the preferred choice for competitive exam preparation in Central India.");
//         abhishek.setFoundedYear(2009);
//         abhishek.setLogoUrl("https://example.com/logos/abhishek.png");
//         abhishek.setBannerUrl("https://example.com/banners/abhishek-banner.jpg");
//         abhishek.setWebsiteUrl("https://www.abhishekclasses.in");
//         abhishek.setEmail("info@abhishekclasses.in");
//         abhishek.setPhonePrimary("0712-2456789");
//         abhishek.setWhatsappNumber("9823456789");
//         abhishek.setType(InstituteType.HYBRID);
//         abhishek.setOwnershipType(OwnershipType.COMPANY);
//         abhishek.setIsFranchise(false);
//         abhishek.setAverageRating(new BigDecimal("4.6"));
//         abhishek.setTotalReviews(1250);
//         abhishek.setTotalStudentsEnrolled(15000);
//         abhishek.setYearsOfExperience(15);
//         abhishek.setIsVerified(true);
//         abhishek.setIsFeatured(true);
//         abhishek.setIsActive(true);
//         abhishek.setSubscriptionTier(SubscriptionTier.PREMIUM);
//         abhishek.setMetaTitle("Abhishek Classes - Best JEE & NEET Coaching in Nagpur");
//         abhishek.setMetaDescription("Join Abhishek Classes for JEE Main, JEE Advanced, and NEET preparation. Top faculty, study material, and proven results in Nagpur.");

//         Institute vinayak = new Institute();
//         vinayak.setIdentifier(uuid()); put("inst-vinayak", vinayak.getIdentifier());
//         vinayak.setName("Vinayak Classes");
//         vinayak.setSlug("vinayak-classes");
//         vinayak.setTagline("Transforming Aspirations into Achievements");
//         vinayak.setDescription("Vinayak Classes is Mumbai's trusted name for JEE, NEET, and MHT-CET coaching. Established in 2005, we have built a legacy of academic excellence with our result-oriented approach, expert faculty, and personalized mentoring. Our state-of-the-art infrastructure and digital learning platform ensure students get the best preparation experience.");
//         vinayak.setFoundedYear(2005);
//         vinayak.setLogoUrl("https://example.com/logos/vinayak.png");
//         vinayak.setBannerUrl("https://example.com/banners/vinayak-banner.jpg");
//         vinayak.setWebsiteUrl("https://www.vinayakclasses.com");
//         vinayak.setEmail("contact@vinayakclasses.com");
//         vinayak.setPhonePrimary("022-26785555");
//         vinayak.setWhatsappNumber("9834567890");
//         vinayak.setType(InstituteType.HYBRID);
//         vinayak.setOwnershipType(OwnershipType.PARTNERSHIP);
//         vinayak.setIsFranchise(false);
//         vinayak.setAverageRating(new BigDecimal("4.5"));
//         vinayak.setTotalReviews(980);
//         vinayak.setTotalStudentsEnrolled(12000);
//         vinayak.setYearsOfExperience(19);
//         vinayak.setIsVerified(true);
//         vinayak.setIsFeatured(true);
//         vinayak.setIsActive(true);
//         vinayak.setSubscriptionTier(SubscriptionTier.PREMIUM);
//         vinayak.setMetaTitle("Vinayak Classes - Best Coaching for JEE, NEET & MHT-CET in Mumbai");
//         vinayak.setMetaDescription("Vinayak Classes offers comprehensive coaching for JEE, NEET, and MHT-CET in Mumbai. Expert faculty and proven track record.");

//         Institute prerna = new Institute();
//         prerna.setIdentifier(uuid()); put("inst-prerna", prerna.getIdentifier());
//         prerna.setName("Prerna Academy");
//         prerna.setSlug("prerna-academy");
//         prerna.setTagline("Inspiring Excellence Every Day");
//         prerna.setDescription("Prerna Academy in Kota is a rising star in competitive exam coaching. Founded by alumni of IIT Bombay and AIIMS Delhi, we bring a fresh perspective to JEE and NEET preparation. Our small batch sizes ensure personalized attention for every student.");
//         prerna.setFoundedYear(2018);
//         prerna.setLogoUrl("https://example.com/logos/prerna.png");
//         prerna.setBannerUrl("https://example.com/banners/prerna-banner.jpg");
//         prerna.setWebsiteUrl("https://www.prernaacademy.in");
//         prerna.setEmail("info@prernaacademy.in");
//         prerna.setPhonePrimary("0744-2429999");
//         prerna.setWhatsappNumber("9823456788");
//         prerna.setType(InstituteType.OFFLINE);
//         prerna.setOwnershipType(OwnershipType.INDIVIDUAL);
//         prerna.setIsFranchise(false);
//         prerna.setAverageRating(new BigDecimal("4.2"));
//         prerna.setTotalReviews(320);
//         prerna.setTotalStudentsEnrolled(3500);
//         prerna.setYearsOfExperience(6);
//         prerna.setIsVerified(true);
//         prerna.setIsFeatured(false);
//         prerna.setIsActive(true);
//         prerna.setSubscriptionTier(SubscriptionTier.FREE);
//         prerna.setMetaTitle("Prerna Academy Kota - JEE & NEET Coaching");
//         prerna.setMetaDescription("Prerna Academy offers personalized JEE and NEET coaching in Kota with small batch sizes and expert faculty.");

//         Institute bright = new Institute();
//         bright.setIdentifier(uuid()); put("inst-bright", bright.getIdentifier());
//         bright.setName("Bright Career Institute");
//         bright.setSlug("bright-career-institute");
//         bright.setTagline("Your Pathway to Success");
//         bright.setDescription("Bright Career Institute in Delhi provides quality coaching for JEE, NEET, and CBSE board exams. With a focus on conceptual clarity and regular assessment, we help students achieve their academic goals. Our affordable fee structure makes quality education accessible to all.");
//         bright.setFoundedYear(2012);
//         bright.setLogoUrl("https://example.com/logos/bright.png");
//         bright.setBannerUrl("https://example.com/banners/bright-banner.jpg");
//         bright.setWebsiteUrl("https://www.brightcareer.in");
//         bright.setEmail("contact@brightcareer.in");
//         bright.setPhonePrimary("011-45678900");
//         bright.setWhatsappNumber("9812345678");
//         bright.setType(InstituteType.OFFLINE);
//         bright.setOwnershipType(OwnershipType.COMPANY);
//         bright.setIsFranchise(false);
//         bright.setAverageRating(new BigDecimal("4.0"));
//         bright.setTotalReviews(560);
//         bright.setTotalStudentsEnrolled(8000);
//         bright.setYearsOfExperience(12);
//         bright.setIsVerified(true);
//         bright.setIsFeatured(false);
//         bright.setIsActive(true);
//         bright.setSubscriptionTier(SubscriptionTier.BASIC);
//         bright.setMetaTitle("Bright Career Institute Delhi - Affordable JEE & NEET Coaching");
//         bright.setMetaDescription("Bright Career Institute offers affordable JEE and NEET coaching in Delhi with focus on conceptual clarity.");

//         INSTITUTE_SERVICE.saveAll(List.of(abhishek, vinayak, prerna, bright));
//         log.info("Institutes seeded.");
//     }

//     // ==================== BRANCHES ====================
//     private void seedBranches() {
//         log.info("Seeding branches...");

//         // Abhishek Classes branches
//         Branch abhishekMain = new Branch();
//         abhishekMain.setIdentifier(uuid()); put("branch-abhishek-main", abhishekMain.getIdentifier());
//         abhishekMain.setInstituteIdentifier(get("inst-abhishek"));
//         abhishekMain.setName("Abhishek Classes - Nagpur Main Centre");
//         abhishekMain.setIsMainBranch(true);
//         abhishekMain.setIsOnlineOnly(false);
//         abhishekMain.setAddressLine1("Plot 45, Laxmi Nagar");
//         abhishekMain.setAddressLine2("Near VNIT Gate");
//         abhishekMain.setLandmark("Opposite HDFC Bank");
//         abhishekMain.setCityIdentifier(get("city-nagpur"));
//         abhishekMain.setCityName("Nagpur");
//         abhishekMain.setState("Maharashtra");
//         abhishekMain.setPincode("440022");
//         abhishekMain.setLatitude(new BigDecimal("21.1458"));
//         abhishekMain.setLongitude(new BigDecimal("79.0882"));
//         abhishekMain.setGoogleMapsUrl("https://maps.google.com/?q=21.1458,79.0882");
//         abhishekMain.setPhone("0712-2456789");
//         abhishekMain.setEmail("nagpur@abhishekclasses.in");
//         abhishekMain.setTotalAreaSqft(35000);
//         abhishekMain.setTotalClassrooms(80);
//         abhishekMain.setSeatingCapacity(3000);
//         abhishekMain.setOperatingHoursStart(LocalTime.parse("07:00:00"));
//         abhishekMain.setOperatingHoursEnd(LocalTime.parse("21:00:00"));
//         abhishekMain.setOperatingDays("Monday-Saturday");
//         abhishekMain.setIsActive(true);

//         Branch abhishekPune = new Branch();
//         abhishekPune.setIdentifier(uuid()); put("branch-abhishek-pune", abhishekPune.getIdentifier());
//         abhishekPune.setInstituteIdentifier(get("inst-abhishek"));
//         abhishekPune.setName("Abhishek Classes - Pune Centre");
//         abhishekPune.setIsMainBranch(false);
//         abhishekPune.setIsOnlineOnly(false);
//         abhishekPune.setAddressLine1("Office 301, Sunrise Plaza");
//         abhishekPune.setAddressLine2("FC Road, Shivaji Nagar");
//         abhishekPune.setLandmark("Near Fergusson College");
//         abhishekPune.setCityIdentifier(get("city-pune"));
//         abhishekPune.setCityName("Pune");
//         abhishekPune.setState("Maharashtra");
//         abhishekPune.setPincode("411004");
//         abhishekPune.setLatitude(new BigDecimal("18.5204"));
//         abhishekPune.setLongitude(new BigDecimal("73.8567"));
//         abhishekPune.setGoogleMapsUrl("https://maps.google.com/?q=18.5204,73.8567");
//         abhishekPune.setPhone("020-25554444");
//         abhishekPune.setEmail("pune@abhishekclasses.in");
//         abhishekPune.setTotalAreaSqft(20000);
//         abhishekPune.setTotalClassrooms(45);
//         abhishekPune.setSeatingCapacity(1500);
//         abhishekPune.setOperatingHoursStart(LocalTime.parse("08:00:00"));
//         abhishekPune.setOperatingHoursEnd(LocalTime.parse("20:00:00"));
//         abhishekPune.setOperatingDays("Monday-Saturday");
//         abhishekPune.setIsActive(true);

//         // Vinayak Classes branches
//         Branch vinayakMain = new Branch();
//         vinayakMain.setIdentifier(uuid()); put("branch-vinayak-main", vinayakMain.getIdentifier());
//         vinayakMain.setInstituteIdentifier(get("inst-vinayak"));
//         vinayakMain.setName("Vinayak Classes - Mumbai Main Centre");
//         vinayakMain.setIsMainBranch(true);
//         vinayakMain.setIsOnlineOnly(false);
//         vinayakMain.setAddressLine1("201, Horizon Tower");
//         vinayakMain.setAddressLine2("Borivali West");
//         vinayakMain.setLandmark("Near Borivali Station");
//         vinayakMain.setCityIdentifier(get("city-mumbai"));
//         vinayakMain.setCityName("Mumbai");
//         vinayakMain.setState("Maharashtra");
//         vinayakMain.setPincode("400092");
//         vinayakMain.setLatitude(new BigDecimal("19.2307"));
//         vinayakMain.setLongitude(new BigDecimal("72.8567"));
//         vinayakMain.setGoogleMapsUrl("https://maps.google.com/?q=19.2307,72.8567");
//         vinayakMain.setPhone("022-26785555");
//         vinayakMain.setEmail("mumbai@vinayakclasses.com");
//         vinayakMain.setTotalAreaSqft(40000);
//         vinayakMain.setTotalClassrooms(90);
//         vinayakMain.setSeatingCapacity(3500);
//         vinayakMain.setOperatingHoursStart(LocalTime.parse("07:00:00"));
//         vinayakMain.setOperatingHoursEnd(LocalTime.parse("21:00:00"));
//         vinayakMain.setOperatingDays("Monday-Saturday");
//         vinayakMain.setIsActive(true);

//         Branch vinayakThane = new Branch();
//         vinayakThane.setIdentifier(uuid()); put("branch-vinayak-thane", vinayakThane.getIdentifier());
//         vinayakThane.setInstituteIdentifier(get("inst-vinayak"));
//         vinayakThane.setName("Vinayak Classes - Thane Centre");
//         vinayakThane.setIsMainBranch(false);
//         vinayakThane.setIsOnlineOnly(false);
//         vinayakThane.setAddressLine1("501, Dynasty Business Park");
//         vinayakThane.setAddressLine2("Thane West");
//         vinayakThane.setLandmark("Near Thane Station");
//         vinayakThane.setCityIdentifier(get("city-thane"));
//         vinayakThane.setCityName("Thane");
//         vinayakThane.setState("Maharashtra");
//         vinayakThane.setPincode("400601");
//         vinayakThane.setLatitude(new BigDecimal("19.2183"));
//         vinayakThane.setLongitude(new BigDecimal("72.9781"));
//         vinayakThane.setGoogleMapsUrl("https://maps.google.com/?q=19.2183,72.9781");
//         vinayakThane.setPhone("022-25445555");
//         vinayakThane.setEmail("thane@vinayakclasses.com");
//         vinayakThane.setTotalAreaSqft(25000);
//         vinayakThane.setTotalClassrooms(55);
//         vinayakThane.setSeatingCapacity(1800);
//         vinayakThane.setOperatingHoursStart(LocalTime.parse("08:00:00"));
//         vinayakThane.setOperatingHoursEnd(LocalTime.parse("20:00:00"));
//         vinayakThane.setOperatingDays("Monday-Saturday");
//         vinayakThane.setIsActive(true);

//         // Prerna Academy branch
//         Branch prernaMain = new Branch();
//         prernaMain.setIdentifier(uuid()); put("branch-prerna-main", prernaMain.getIdentifier());
//         prernaMain.setInstituteIdentifier(get("inst-prerna"));
//         prernaMain.setName("Prerna Academy - Kota Main Centre");
//         prernaMain.setIsMainBranch(true);
//         prernaMain.setIsOnlineOnly(false);
//         prernaMain.setAddressLine1("12-A, Rajeev Gandhi Nagar");
//         prernaMain.setAddressLine2("Near Allen Samyak");
//         prernaMain.setLandmark("Landmark City");
//         prernaMain.setCityIdentifier(get("city-kota"));
//         prernaMain.setCityName("Kota");
//         prernaMain.setState("Rajasthan");
//         prernaMain.setPincode("324005");
//         prernaMain.setLatitude(new BigDecimal("25.2138"));
//         prernaMain.setLongitude(new BigDecimal("75.8648"));
//         prernaMain.setGoogleMapsUrl("https://maps.google.com/?q=25.2138,75.8648");
//         prernaMain.setPhone("0744-2429999");
//         prernaMain.setEmail("kota@prernaacademy.in");
//         prernaMain.setTotalAreaSqft(15000);
//         prernaMain.setTotalClassrooms(35);
//         prernaMain.setSeatingCapacity(900);
//         prernaMain.setOperatingHoursStart(LocalTime.parse("07:30:00"));
//         prernaMain.setOperatingHoursEnd(LocalTime.parse("20:30:00"));
//         prernaMain.setOperatingDays("Monday-Saturday");
//         prernaMain.setIsActive(true);

//         // Bright Career branch
//         Branch brightMain = new Branch();
//         brightMain.setIdentifier(uuid()); put("branch-bright-main", brightMain.getIdentifier());
//         brightMain.setInstituteIdentifier(get("inst-bright"));
//         brightMain.setName("Bright Career Institute - Delhi Main Centre");
//         brightMain.setIsMainBranch(true);
//         brightMain.setIsOnlineOnly(false);
//         brightMain.setAddressLine1("45, Lajpat Nagar");
//         brightMain.setAddressLine2("Near Metro Station");
//         brightMain.setLandmark("Opposite Pizza Hut");
//         brightMain.setCityIdentifier(get("city-delhi"));
//         brightMain.setCityName("New Delhi");
//         brightMain.setState("Delhi");
//         brightMain.setPincode("110024");
//         brightMain.setLatitude(new BigDecimal("28.5700"));
//         brightMain.setLongitude(new BigDecimal("77.2400"));
//         brightMain.setGoogleMapsUrl("https://maps.google.com/?q=28.5700,77.2400");
//         brightMain.setPhone("011-45678900");
//         brightMain.setEmail("delhi@brightcareer.in");
//         brightMain.setTotalAreaSqft(22000);
//         brightMain.setTotalClassrooms(50);
//         brightMain.setSeatingCapacity(1600);
//         brightMain.setOperatingHoursStart(LocalTime.parse("08:00:00"));
//         brightMain.setOperatingHoursEnd(LocalTime.parse("20:00:00"));
//         brightMain.setOperatingDays("Monday-Saturday");
//         brightMain.setIsActive(true);

//         BRANCH_SERVICE.saveAll(List.of(abhishekMain, abhishekPune, vinayakMain, vinayakThane, prernaMain, brightMain));
//         log.info("Branches seeded.");
//     }

//     // ==================== INSTITUTE FACILITIES ====================
//     private void seedInstituteFacilities() {
//         log.info("Seeding institute facilities...");

//         InstituteFacility abhishekFac = new InstituteFacility();
//         abhishekFac.setIdentifier(uuid());
//         abhishekFac.setInstituteIdentifier(get("inst-abhishek"));
//         abhishekFac.setHasLibrary(true);
//         abhishekFac.setHasHostel(true);
//         abhishekFac.setHasCanteen(true);
//         abhishekFac.setHasTransport(true);
//         abhishekFac.setHasAcClassrooms(true);
//         abhishekFac.setHasDigitalBoards(true);
//         abhishekFac.setHasLaboratory(true);
//         abhishekFac.setHasStudyRoom(true);
//         abhishekFac.setHasWifi(true);
//         abhishekFac.setHasCctv(true);
//         abhishekFac.setHasOnlinePortal(true);
//         abhishekFac.setHasDoubtSessions(true);
//         abhishekFac.setHasMockTestSeries(true);
//         abhishekFac.setHasStudyMaterial(true);
//         abhishekFac.setHasCrashCourses(true);
//         abhishekFac.setHasScholarshipProgram(true);
//         abhishekFac.setHasFreeDemoClass(true);
//         abhishekFac.setHasParentTeacherMeetings(true);
//         abhishekFac.setHasPerformanceTracking(true);
//         abhishekFac.setStudentToTeacherRatio("25:1");
//         abhishekFac.setNotes("State-of-the-art infrastructure with AC classrooms, digital boards, well-stocked library, hostel facilities, and online learning portal.");

//         InstituteFacility vinayakFac = new InstituteFacility();
//         vinayakFac.setIdentifier(uuid());
//         vinayakFac.setInstituteIdentifier(get("inst-vinayak"));
//         vinayakFac.setHasLibrary(true);
//         vinayakFac.setHasHostel(false);
//         vinayakFac.setHasCanteen(true);
//         vinayakFac.setHasTransport(true);
//         vinayakFac.setHasAcClassrooms(true);
//         vinayakFac.setHasDigitalBoards(true);
//         vinayakFac.setHasLaboratory(true);
//         vinayakFac.setHasStudyRoom(true);
//         vinayakFac.setHasWifi(true);
//         vinayakFac.setHasCctv(true);
//         vinayakFac.setHasOnlinePortal(true);
//         vinayakFac.setHasDoubtSessions(true);
//         vinayakFac.setHasMockTestSeries(true);
//         vinayakFac.setHasStudyMaterial(true);
//         vinayakFac.setHasCrashCourses(true);
//         vinayakFac.setHasScholarshipProgram(true);
//         vinayakFac.setHasFreeDemoClass(true);
//         vinayakFac.setHasParentTeacherMeetings(true);
//         vinayakFac.setHasPerformanceTracking(true);
//         vinayakFac.setStudentToTeacherRatio("30:1");
//         vinayakFac.setNotes("Modern infrastructure with AC classrooms, digital learning resources, comprehensive test series, and integrated online platform.");

//         InstituteFacility prernaFac = new InstituteFacility();
//         prernaFac.setIdentifier(uuid());
//         prernaFac.setInstituteIdentifier(get("inst-prerna"));
//         prernaFac.setHasLibrary(true);
//         prernaFac.setHasHostel(true);
//         prernaFac.setHasCanteen(true);
//         prernaFac.setHasTransport(false);
//         prernaFac.setHasAcClassrooms(true);
//         prernaFac.setHasDigitalBoards(true);
//         prernaFac.setHasLaboratory(true);
//         prernaFac.setHasStudyRoom(true);
//         prernaFac.setHasWifi(true);
//         prernaFac.setHasCctv(true);
//         prernaFac.setHasOnlinePortal(false);
//         prernaFac.setHasDoubtSessions(true);
//         prernaFac.setHasMockTestSeries(true);
//         prernaFac.setHasStudyMaterial(true);
//         prernaFac.setHasCrashCourses(true);
//         prernaFac.setHasScholarshipProgram(false);
//         prernaFac.setHasFreeDemoClass(true);
//         prernaFac.setHasParentTeacherMeetings(true);
//         prernaFac.setHasPerformanceTracking(true);
//         prernaFac.setStudentToTeacherRatio("20:1");
//         prernaFac.setNotes("Focused learning environment with smaller batch sizes, dedicated doubt sessions, and comprehensive study materials.");

//         InstituteFacility brightFac = new InstituteFacility();
//         brightFac.setIdentifier(uuid());
//         brightFac.setInstituteIdentifier(get("inst-bright"));
//         brightFac.setHasLibrary(true);
//         brightFac.setHasHostel(false);
//         brightFac.setHasCanteen(true);
//         brightFac.setHasTransport(false);
//         brightFac.setHasAcClassrooms(true);
//         brightFac.setHasDigitalBoards(false);
//         brightFac.setHasLaboratory(true);
//         brightFac.setHasStudyRoom(true);
//         brightFac.setHasWifi(true);
//         brightFac.setHasCctv(true);
//         brightFac.setHasOnlinePortal(false);
//         brightFac.setHasDoubtSessions(true);
//         brightFac.setHasMockTestSeries(true);
//         brightFac.setHasStudyMaterial(true);
//         brightFac.setHasCrashCourses(true);
//         brightFac.setHasScholarshipProgram(true);
//         brightFac.setHasFreeDemoClass(true);
//         brightFac.setHasParentTeacherMeetings(false);
//         brightFac.setHasPerformanceTracking(false);
//         brightFac.setStudentToTeacherRatio("40:1");
//         brightFac.setNotes("Quality coaching with affordable fee structure, regular tests, and experienced faculty.");

//         INSTITUTE_FACILITY_SERVICE.saveAll(List.of(abhishekFac, vinayakFac, prernaFac, brightFac));
//         log.info("Institute facilities seeded.");
//     }

//     // ==================== INSTITUTE SUBSCRIPTIONS ====================
//     private void seedInstituteSubscriptions() {
//         log.info("Seeding institute subscriptions...");

//         InstituteSubscription abhishekSub = new InstituteSubscription();
//         abhishekSub.setIdentifier(uuid());
//         abhishekSub.setInstituteIdentifier(get("inst-abhishek"));
//         abhishekSub.setPlanIdentifier(get("plan-premium"));
//         abhishekSub.setStartDate(LocalDate.of(2024, 1, 1));
//         abhishekSub.setEndDate(LocalDate.of(2026, 12, 31));
//         abhishekSub.setIsActive(true);
//         abhishekSub.setPaymentReference("TXN_ABHISHEK_PREM_2024");

//         InstituteSubscription vinayakSub = new InstituteSubscription();
//         vinayakSub.setIdentifier(uuid());
//         vinayakSub.setInstituteIdentifier(get("inst-vinayak"));
//         vinayakSub.setPlanIdentifier(get("plan-premium"));
//         vinayakSub.setStartDate(LocalDate.of(2024, 3, 1));
//         vinayakSub.setEndDate(LocalDate.of(2026, 12, 31));
//         vinayakSub.setIsActive(true);
//         vinayakSub.setPaymentReference("TXN_VINAYAK_PREM_2024");

//         InstituteSubscription prernaSub = new InstituteSubscription();
//         prernaSub.setIdentifier(uuid());
//         prernaSub.setInstituteIdentifier(get("inst-prerna"));
//         prernaSub.setPlanIdentifier(get("plan-free"));
//         prernaSub.setStartDate(LocalDate.of(2025, 1, 1));
//         prernaSub.setEndDate(LocalDate.of(2025, 12, 31));
//         prernaSub.setIsActive(true);
//         prernaSub.setPaymentReference("FREE_PLAN_2025");

//         InstituteSubscription brightSub = new InstituteSubscription();
//         brightSub.setIdentifier(uuid());
//         brightSub.setInstituteIdentifier(get("inst-bright"));
//         brightSub.setPlanIdentifier(get("plan-basic"));
//         brightSub.setStartDate(LocalDate.of(2024, 6, 1));
//         brightSub.setEndDate(LocalDate.of(2025, 12, 31));
//         brightSub.setIsActive(true);
//         brightSub.setPaymentReference("TXN_BRIGHT_BASIC_2024");

//         INSTITUTE_SUBSCRIPTION_SERVICE.saveAll(List.of(abhishekSub, vinayakSub, prernaSub, brightSub));
//         log.info("Institute subscriptions seeded.");
//     }

//     // ==================== ADMIN USERS ====================
//     private void seedAdminUsers() {
//         log.info("Seeding admin users...");

//         User abhishekAdmin = new User();
//         abhishekAdmin.setIdentifier(uuid()); put("user-abhishek-admin", abhishekAdmin.getIdentifier());
//         abhishekAdmin.setFullName("Abhishek Sharma");
//         abhishekAdmin.setEmail("abhishek@gmail.com");
//         abhishekAdmin.setPhone("9876543215");
//         abhishekAdmin.setPhoneVerified(true);
//         abhishekAdmin.setEmailVerified(true);
//         abhishekAdmin.setPasswordHash(PASSWORD_ENCODER.encode("Abhishek"));
//         abhishekAdmin.setAvatarUrl("https://example.com/avatars/abhishek.jpg");
//         abhishekAdmin.setRole(UserRole.INSTITUTE_ADMIN);
//         abhishekAdmin.setCityIdentifier(get("city-nagpur"));
//         abhishekAdmin.setState("Maharashtra");
//         abhishekAdmin.setPincode("440022");
//         abhishekAdmin.setPreferredLanguage("English");
//         abhishekAdmin.setIsActive(true);

//         User vinayakAdmin = new User();
//         vinayakAdmin.setIdentifier(uuid()); put("user-vinayak-admin", vinayakAdmin.getIdentifier());
//         vinayakAdmin.setFullName("Vinayak Patil");
//         vinayakAdmin.setEmail("vinayak@gmail.com");
//         vinayakAdmin.setPhone("9876543216");
//         vinayakAdmin.setPhoneVerified(true);
//         vinayakAdmin.setEmailVerified(true);
//         vinayakAdmin.setPasswordHash(PASSWORD_ENCODER.encode("Vinayak"));
//         vinayakAdmin.setAvatarUrl("https://example.com/avatars/vinayak.jpg");
//         vinayakAdmin.setRole(UserRole.INSTITUTE_ADMIN);
//         vinayakAdmin.setCityIdentifier(get("city-mumbai"));
//         vinayakAdmin.setState("Maharashtra");
//         vinayakAdmin.setPincode("400092");
//         vinayakAdmin.setPreferredLanguage("English");
//         vinayakAdmin.setIsActive(true);

//         USER_SERVICE.saveAll(List.of(abhishekAdmin, vinayakAdmin));
//         log.info("Admin users seeded.");
//     }

//     // ==================== USER-INSTITUTE ASSOCIATIONS ====================
//     private void seedUserInstituteAssociations() {
//         log.info("Seeding user-institute associations...");

//         UserInstituteAssociation abhishekAssoc = new UserInstituteAssociation();
//         abhishekAssoc.setIdentifier(uuid());
//         abhishekAssoc.setUserIdentifier(get("user-abhishek-admin"));
//         abhishekAssoc.setInstituteIdentifier(get("inst-abhishek"));
//         abhishekAssoc.setRole(InstituteStaffRole.OWNER);
//         abhishekAssoc.setIsActive(true);

//         UserInstituteAssociation vinayakAssoc = new UserInstituteAssociation();
//         vinayakAssoc.setIdentifier(uuid());
//         vinayakAssoc.setUserIdentifier(get("user-vinayak-admin"));
//         vinayakAssoc.setInstituteIdentifier(get("inst-vinayak"));
//         vinayakAssoc.setRole(InstituteStaffRole.OWNER);
//         vinayakAssoc.setIsActive(true);

//         USER_INSTITUTE_ASSOCIATION_SERVICE.saveAll(List.of(abhishekAssoc, vinayakAssoc));
//         log.info("User-institute associations seeded.");
//     }

//     // ==================== INSTITUTE COURSES ====================
//     private void seedInstituteCourses() {
//         log.info("Seeding institute courses...");

//         // Abhishek Classes courses
//         InstituteCourse abhiJee12 = new InstituteCourse();
//         abhiJee12.setIdentifier(uuid()); put("ic-abhi-jee-12", abhiJee12.getIdentifier());
//         abhiJee12.setInstituteIdentifier(get("inst-abhishek"));
//         abhiJee12.setBranchIdentifier(get("branch-abhishek-main"));
//         abhiJee12.setCustomName("JEE (Main + Advanced) - 12th Pass");
//         abhiJee12.setFeeMin(new BigDecimal("120000"));
//         abhiJee12.setFeeMax(new BigDecimal("150000"));
//         abhiJee12.setFeeDescription("Comprehensive one-year program for 12th pass students. Includes study material, test series, and online portal access. Hostel charges extra.");
//         abhiJee12.setScholarshipAvailable(true);
//         abhiJee12.setScholarshipDetails("Up to 50% scholarship based on entrance test. Additional scholarships for board toppers.");
//         abhiJee12.setDurationMonths(12);
//         abhiJee12.setStudyMaterialIncluded(true);
//         abhiJee12.setTestSeriesIncluded(true);
//         abhiJee12.setOnlineClassesAvailable(true);
//         abhiJee12.setRecordedLecturesAvailable(true);
//         abhiJee12.setIsActive(true);
//         abhiJee12.setAdmissionOpen(true);

//         InstituteCourse abhiNeet12 = new InstituteCourse();
//         abhiNeet12.setIdentifier(uuid()); put("ic-abhi-neet-12", abhiNeet12.getIdentifier());
//         abhiNeet12.setInstituteIdentifier(get("inst-abhishek"));
//         abhiNeet12.setBranchIdentifier(get("branch-abhishek-main"));
//         abhiNeet12.setCustomName("NEET - 12th Pass");
//         abhiNeet12.setFeeMin(new BigDecimal("110000"));
//         abhiNeet12.setFeeMax(new BigDecimal("140000"));
//         abhiNeet12.setFeeDescription("Complete NEET preparation for 12th pass students. Covers Physics, Chemistry, and Biology with focus on NCERT.");
//         abhiNeet12.setScholarshipAvailable(true);
//         abhiNeet12.setScholarshipDetails("Scholarship up to 40% based on previous academic performance.");
//         abhiNeet12.setDurationMonths(12);
//         abhiNeet12.setStudyMaterialIncluded(true);
//         abhiNeet12.setTestSeriesIncluded(true);
//         abhiNeet12.setOnlineClassesAvailable(true);
//         abhiNeet12.setRecordedLecturesAvailable(false);
//         abhiNeet12.setIsActive(true);
//         abhiNeet12.setAdmissionOpen(true);

//         InstituteCourse abhiDropper = new InstituteCourse();
//         abhiDropper.setIdentifier(uuid()); put("ic-abhi-dropper", abhiDropper.getIdentifier());
//         abhiDropper.setInstituteIdentifier(get("inst-abhishek"));
//         abhiDropper.setBranchIdentifier(get("branch-abhishek-main"));
//         abhiDropper.setCustomName("JEE Droppers Batch");
//         abhiDropper.setFeeMin(new BigDecimal("130000"));
//         abhiDropper.setFeeMax(new BigDecimal("160000"));
//         abhiDropper.setFeeDescription("Intensive one-year program for droppers targeting JEE. Focus on advanced problem-solving.");
//         abhiDropper.setScholarshipAvailable(true);
//         abhiDropper.setScholarshipDetails("Up to 30% scholarship based on previous JEE Main percentile.");
//         abhiDropper.setDurationMonths(12);
//         abhiDropper.setStudyMaterialIncluded(true);
//         abhiDropper.setTestSeriesIncluded(true);
//         abhiDropper.setOnlineClassesAvailable(true);
//         abhiDropper.setRecordedLecturesAvailable(true);
//         abhiDropper.setIsActive(true);
//         abhiDropper.setAdmissionOpen(true);

//         InstituteCourse abhiFoundation = new InstituteCourse();
//         abhiFoundation.setIdentifier(uuid()); put("ic-abhi-foundation", abhiFoundation.getIdentifier());
//         abhiFoundation.setInstituteIdentifier(get("inst-abhishek"));
//         abhiFoundation.setBranchIdentifier(get("branch-abhishek-pune"));
//         abhiFoundation.setCustomName("Foundation Course - Class 9 & 10");
//         abhiFoundation.setFeeMin(new BigDecimal("60000"));
//         abhiFoundation.setFeeMax(new BigDecimal("80000"));
//         abhiFoundation.setFeeDescription("Two-year foundation program for classes 9-10 students. Builds strong fundamentals for future competitive exams.");
//         abhiFoundation.setScholarshipAvailable(false);
//         abhiFoundation.setDurationMonths(24);
//         abhiFoundation.setStudyMaterialIncluded(true);
//         abhiFoundation.setTestSeriesIncluded(true);
//         abhiFoundation.setOnlineClassesAvailable(false);
//         abhiFoundation.setRecordedLecturesAvailable(false);
//         abhiFoundation.setIsActive(true);
//         abhiFoundation.setAdmissionOpen(true);

//         InstituteCourse abhiCrash = new InstituteCourse();
//         abhiCrash.setIdentifier(uuid()); put("ic-abhi-crash", abhiCrash.getIdentifier());
//         abhiCrash.setInstituteIdentifier(get("inst-abhishek"));
//         abhiCrash.setBranchIdentifier(get("branch-abhishek-main"));
//         abhiCrash.setCustomName("JEE Crash Course");
//         abhiCrash.setFeeMin(new BigDecimal("35000"));
//         abhiCrash.setFeeMax(new BigDecimal("45000"));
//         abhiCrash.setFeeDescription("Intensive 3-month crash course for last-minute JEE preparation.");
//         abhiCrash.setScholarshipAvailable(false);
//         abhiCrash.setDurationMonths(3);
//         abhiCrash.setStudyMaterialIncluded(true);
//         abhiCrash.setTestSeriesIncluded(true);
//         abhiCrash.setOnlineClassesAvailable(true);
//         abhiCrash.setRecordedLecturesAvailable(true);
//         abhiCrash.setIsActive(true);
//         abhiCrash.setAdmissionOpen(true);

//         // Vinayak Classes courses
//         InstituteCourse vinJeeAdv = new InstituteCourse();
//         vinJeeAdv.setIdentifier(uuid()); put("ic-vin-jee-adv", vinJeeAdv.getIdentifier());
//         vinJeeAdv.setInstituteIdentifier(get("inst-vinayak"));
//         vinJeeAdv.setBranchIdentifier(get("branch-vinayak-main"));
//         vinJeeAdv.setCustomName("JEE Advanced Mastery Program");
//         vinJeeAdv.setFeeMin(new BigDecimal("180000"));
//         vinJeeAdv.setFeeMax(new BigDecimal("220000"));
//         vinJeeAdv.setFeeDescription("Elite JEE Advanced preparation with personalized mentoring, advanced problem-solving, and All India Test Series.");
//         vinJeeAdv.setScholarshipAvailable(true);
//         vinJeeAdv.setScholarshipDetails("Up to 60% scholarship through entrance test. Reserved seats for top performers.");
//         vinJeeAdv.setDurationMonths(12);
//         vinJeeAdv.setStudyMaterialIncluded(true);
//         vinJeeAdv.setTestSeriesIncluded(true);
//         vinJeeAdv.setOnlineClassesAvailable(true);
//         vinJeeAdv.setRecordedLecturesAvailable(true);
//         vinJeeAdv.setIsActive(true);
//         vinJeeAdv.setAdmissionOpen(true);

//         InstituteCourse vinNeet = new InstituteCourse();
//         vinNeet.setIdentifier(uuid()); put("ic-vin-neet", vinNeet.getIdentifier());
//         vinNeet.setInstituteIdentifier(get("inst-vinayak"));
//         vinNeet.setBranchIdentifier(get("branch-vinayak-main"));
//         vinNeet.setCustomName("NEET Ultimate Program");
//         vinNeet.setFeeMin(new BigDecimal("160000"));
//         vinNeet.setFeeMax(new BigDecimal("190000"));
//         vinNeet.setFeeDescription("Premium NEET program with AI-based test analysis, comprehensive study material, and digital resources.");
//         vinNeet.setScholarshipAvailable(true);
//         vinNeet.setScholarshipDetails("Scholarship up to 50% through talent hunt exam.");
//         vinNeet.setDurationMonths(12);
//         vinNeet.setStudyMaterialIncluded(true);
//         vinNeet.setTestSeriesIncluded(true);
//         vinNeet.setOnlineClassesAvailable(true);
//         vinNeet.setRecordedLecturesAvailable(true);
//         vinNeet.setIsActive(true);
//         vinNeet.setAdmissionOpen(true);

//         InstituteCourse vinMhtcet = new InstituteCourse();
//         vinMhtcet.setIdentifier(uuid()); put("ic-vin-mhtcet", vinMhtcet.getIdentifier());
//         vinMhtcet.setInstituteIdentifier(get("inst-vinayak"));
//         vinMhtcet.setBranchIdentifier(get("branch-vinayak-thane"));
//         vinMhtcet.setCustomName("MHT-CET Comprehensive");
//         vinMhtcet.setFeeMin(new BigDecimal("80000"));
//         vinMhtcet.setFeeMax(new BigDecimal("100000"));
//         vinMhtcet.setFeeDescription("Complete MHT-CET preparation for Maharashtra state engineering and pharmacy admissions.");
//         vinMhtcet.setScholarshipAvailable(true);
//         vinMhtcet.setScholarshipDetails("Up to 30% scholarship for meritorious students.");
//         vinMhtcet.setDurationMonths(8);
//         vinMhtcet.setStudyMaterialIncluded(true);
//         vinMhtcet.setTestSeriesIncluded(true);
//         vinMhtcet.setOnlineClassesAvailable(true);
//         vinMhtcet.setRecordedLecturesAvailable(false);
//         vinMhtcet.setIsActive(true);
//         vinMhtcet.setAdmissionOpen(true);

//         InstituteCourse vinFoundation = new InstituteCourse();
//         vinFoundation.setIdentifier(uuid()); put("ic-vin-foundation", vinFoundation.getIdentifier());
//         vinFoundation.setInstituteIdentifier(get("inst-vinayak"));
//         vinFoundation.setBranchIdentifier(get("branch-vinayak-main"));
//         vinFoundation.setCustomName("Foundation Course - Class 11");
//         vinFoundation.setFeeMin(new BigDecimal("90000"));
//         vinFoundation.setFeeMax(new BigDecimal("110000"));
//         vinFoundation.setFeeDescription("Two-year integrated program for class 11 students covering school syllabus along with competitive exam preparation.");
//         vinFoundation.setScholarshipAvailable(false);
//         vinFoundation.setDurationMonths(24);
//         vinFoundation.setStudyMaterialIncluded(true);
//         vinFoundation.setTestSeriesIncluded(true);
//         vinFoundation.setOnlineClassesAvailable(true);
//         vinFoundation.setRecordedLecturesAvailable(true);
//         vinFoundation.setIsActive(true);
//         vinFoundation.setAdmissionOpen(true);

//         // Prerna Academy courses
//         InstituteCourse prernaJee = new InstituteCourse();
//         prernaJee.setIdentifier(uuid()); put("ic-prerna-jee", prernaJee.getIdentifier());
//         prernaJee.setInstituteIdentifier(get("inst-prerna"));
//         prernaJee.setBranchIdentifier(get("branch-prerna-main"));
//         prernaJee.setCustomName("JEE Complete Program");
//         prernaJee.setFeeMin(new BigDecimal("90000"));
//         prernaJee.setFeeMax(new BigDecimal("110000"));
//         prernaJee.setFeeDescription("Comprehensive JEE preparation with experienced faculty from top institutes.");
//         prernaJee.setScholarshipAvailable(true);
//         prernaJee.setScholarshipDetails("Up to 40% scholarship based on entrance test.");
//         prernaJee.setDurationMonths(12);
//         prernaJee.setStudyMaterialIncluded(true);
//         prernaJee.setTestSeriesIncluded(true);
//         prernaJee.setOnlineClassesAvailable(false);
//         prernaJee.setRecordedLecturesAvailable(false);
//         prernaJee.setIsActive(true);
//         prernaJee.setAdmissionOpen(true);

//         // Bright Career courses
//         InstituteCourse brightJee = new InstituteCourse();
//         brightJee.setIdentifier(uuid()); put("ic-bright-jee", brightJee.getIdentifier());
//         brightJee.setInstituteIdentifier(get("inst-bright"));
//         brightJee.setBranchIdentifier(get("branch-bright-main"));
//         brightJee.setCustomName("JEE Foundation + Advanced");
//         brightJee.setFeeMin(new BigDecimal("75000"));
//         brightJee.setFeeMax(new BigDecimal("95000"));
//         brightJee.setFeeDescription("Affordable JEE coaching with focus on concept building and regular practice.");
//         brightJee.setScholarshipAvailable(true);
//         brightJee.setScholarshipDetails("Fee waivers up to 25% for deserving students.");
//         brightJee.setDurationMonths(12);
//         brightJee.setStudyMaterialIncluded(true);
//         brightJee.setTestSeriesIncluded(true);
//         brightJee.setOnlineClassesAvailable(false);
//         brightJee.setRecordedLecturesAvailable(false);
//         brightJee.setIsActive(true);
//         brightJee.setAdmissionOpen(true);

//         INSTITUTE_COURSE_SERVICE.saveAll(List.of(
//                 abhiJee12, abhiNeet12, abhiDropper, abhiFoundation, abhiCrash,
//                 vinJeeAdv, vinNeet, vinMhtcet, vinFoundation,
//                 prernaJee, brightJee
//         ));
//         log.info("Institute courses seeded.");
//     }

//     // ==================== FACULTY ====================
//     private void seedFaculty() {
//         log.info("Seeding faculty...");

//         // Abhishek Classes faculty
//         Faculty abhiFac1 = new Faculty();
//         abhiFac1.setIdentifier(uuid()); put("fac-abhi-1", abhiFac1.getIdentifier());
//         abhiFac1.setInstituteIdentifier(get("inst-abhishek"));
//         abhiFac1.setName("Dr. Ramesh Iyer");
//         abhiFac1.setPhotoUrl("https://example.com/faculty/ramesh.jpg");
//         abhiFac1.setDesignation("HOD Physics");
//         abhiFac1.setQualification("Ph.D. Physics, IIT Bombay");
//         abhiFac1.setExperienceYears(18);
//         abhiFac1.setBio("Dr. Ramesh Iyer is a renowned Physics educator with 18 years of teaching experience. He specializes in mechanics and electrodynamics and has mentored over 5000 JEE qualifiers.");
//         abhiFac1.setSpecialization("Mechanics, Electrodynamics, Modern Physics");
//         abhiFac1.setIitIimBackground(true);
//         abhiFac1.setNitBackground(false);
//         abhiFac1.setAchievements("Published 12 research papers in international journals. Guided 3 students to AIR under 100 in JEE Advanced.");
//         abhiFac1.setFormerInstitutes("IIT Bombay (Research), Bansal Classes Kota");
//         abhiFac1.setStudentRating(new BigDecimal("4.9"));
//         abhiFac1.setIsActive(true);
//         abhiFac1.setDisplayOrder(1);
//         abhiFac1.setSubjectIdentifiers(List.of(get("sub-physics")));
//         abhiFac1.setExamTypeIdentifiers(List.of(get("exam-jee-main"), get("exam-jee-adv")));

//         Faculty abhiFac2 = new Faculty();
//         abhiFac2.setIdentifier(uuid()); put("fac-abhi-2", abhiFac2.getIdentifier());
//         abhiFac2.setInstituteIdentifier(get("inst-abhishek"));
//         abhiFac2.setName("Prof. Sunita Deshmukh");
//         abhiFac2.setPhotoUrl("https://example.com/faculty/sunita.jpg");
//         abhiFac2.setDesignation("HOD Chemistry");
//         abhiFac2.setQualification("M.Sc. Chemistry, NIT Nagpur");
//         abhiFac2.setExperienceYears(15);
//         abhiFac2.setBio("Prof. Sunita Deshmukh brings 15 years of expertise in Organic and Physical Chemistry. Her teaching methodology focuses on conceptual clarity and problem-solving strategies.");
//         abhiFac2.setSpecialization("Organic Chemistry, Physical Chemistry, Inorganic Chemistry");
//         abhiFac2.setIitIimBackground(false);
//         abhiFac2.setNitBackground(true);
//         abhiFac2.setAchievements("Co-authored 2 Chemistry textbooks. 200+ students selected in NEET with ranks under 1000.");
//         abhiFac2.setFormerInstitutes("NIT Nagpur, Resonance Kota");
//         abhiFac2.setStudentRating(new BigDecimal("4.8"));
//         abhiFac2.setIsActive(true);
//         abhiFac2.setDisplayOrder(2);
//         abhiFac2.setSubjectIdentifiers(List.of(get("sub-chemistry")));
//         abhiFac2.setExamTypeIdentifiers(List.of(get("exam-jee-main"), get("exam-jee-adv"), get("exam-neet")));

//         Faculty abhiFac3 = new Faculty();
//         abhiFac3.setIdentifier(uuid()); put("fac-abhi-3", abhiFac3.getIdentifier());
//         abhiFac3.setInstituteIdentifier(get("inst-abhishek"));
//         abhiFac3.setName("Dr. Anand Kumar");
//         abhiFac3.setPhotoUrl("https://example.com/faculty/anand.jpg");
//         abhiFac3.setDesignation("HOD Mathematics");
//         abhiFac3.setQualification("Ph.D. Mathematics, IIT Madras");
//         abhiFac3.setExperienceYears(20);
//         abhiFac3.setBio("Dr. Anand Kumar is a Mathematics wizard with 20 years of teaching experience. His unique approach to calculus and algebra has helped countless students crack JEE with top ranks.");
//         abhiFac3.setSpecialization("Calculus, Algebra, Coordinate Geometry");
//         abhiFac3.setIitIimBackground(true);
//         abhiFac3.setNitBackground(false);
//         abhiFac3.setAchievements("5 students secured AIR under 50 in JEE Advanced. Known for making complex math simple.");
//         abhiFac3.setFormerInstitutes("IIT Madras, FIITJEE Delhi");
//         abhiFac3.setStudentRating(new BigDecimal("4.9"));
//         abhiFac3.setIsActive(true);
//         abhiFac3.setDisplayOrder(3);
//         abhiFac3.setSubjectIdentifiers(List.of(get("sub-mathematics")));
//         abhiFac3.setExamTypeIdentifiers(List.of(get("exam-jee-main"), get("exam-jee-adv")));

//         Faculty abhiFac4 = new Faculty();
//         abhiFac4.setIdentifier(uuid()); put("fac-abhi-4", abhiFac4.getIdentifier());
//         abhiFac4.setInstituteIdentifier(get("inst-abhishek"));
//         abhiFac4.setName("Dr. Priya Sharma");
//         abhiFac4.setPhotoUrl("https://example.com/faculty/priya.jpg");
//         abhiFac4.setDesignation("HOD Biology");
//         abhiFac4.setQualification("MBBS, AIIMS Delhi");
//         abhiFac4.setExperienceYears(12);
//         abhiFac4.setBio("Dr. Priya Sharma is an AIIMS graduate with a passion for teaching Biology. Her NCERT-focused approach has helped numerous students achieve top ranks in NEET.");
//         abhiFac4.setSpecialization("Zoology, Botany, Human Physiology");
//         abhiFac4.setIitIimBackground(false);
//         abhiFac4.setNitBackground(false);
//         abhiFac4.setAchievements("150+ students in top medical colleges. Expert in NEET Biology strategy.");
//         abhiFac4.setFormerInstitutes("AIIMS Delhi, Aakash Institute");
//         abhiFac4.setStudentRating(new BigDecimal("4.7"));
//         abhiFac4.setIsActive(true);
//         abhiFac4.setDisplayOrder(4);
//         abhiFac4.setSubjectIdentifiers(List.of(get("sub-biology")));
//         abhiFac4.setExamTypeIdentifiers(List.of(get("exam-neet")));

//         Faculty abhiFac5 = new Faculty();
//         abhiFac5.setIdentifier(uuid()); put("fac-abhi-5", abhiFac5.getIdentifier());
//         abhiFac5.setInstituteIdentifier(get("inst-abhishek"));
//         abhiFac5.setName("Prof. Rajesh Patel");
//         abhiFac5.setPhotoUrl("https://example.com/faculty/rajesh.jpg");
//         abhiFac5.setDesignation("Senior Faculty - Foundation");
//         abhiFac5.setQualification("M.Sc. Physics, University of Mumbai");
//         abhiFac5.setExperienceYears(10);
//         abhiFac5.setBio("Prof. Rajesh Patel specializes in foundation courses for classes 9-10. His interactive teaching style makes science fun and accessible for young minds.");
//         abhiFac5.setSpecialization("Foundation Physics, Chemistry, Mathematics");
//         abhiFac5.setIitIimBackground(false);
//         abhiFac5.setNitBackground(false);
//         abhiFac5.setAchievements("Developed unique foundation course curriculum adopted by multiple coaching institutes.");
//         abhiFac5.setFormerInstitutes("Catalyser Nagpur");
//         abhiFac5.setStudentRating(new BigDecimal("4.6"));
//         abhiFac5.setIsActive(true);
//         abhiFac5.setDisplayOrder(5);
//         abhiFac5.setSubjectIdentifiers(List.of(get("sub-physics"), get("sub-chemistry"), get("sub-mathematics")));
//         abhiFac5.setExamTypeIdentifiers(List.of(get("exam-cbse")));

//         // Vinayak Classes faculty
//         Faculty vinFac1 = new Faculty();
//         vinFac1.setIdentifier(uuid()); put("fac-vin-1", vinFac1.getIdentifier());
//         vinFac1.setInstituteIdentifier(get("inst-vinayak"));
//         vinFac1.setName("Dr. Vikram Mehta");
//         vinFac1.setPhotoUrl("https://example.com/faculty/vikram.jpg");
//         vinFac1.setDesignation("Director & HOD Physics");
//         vinFac1.setQualification("Ph.D. Physics, IIT Kanpur");
//         vinFac1.setExperienceYears(22);
//         vinFac1.setBio("Dr. Vikram Mehta is the founder-director of Vinayak Classes. With 22 years of experience, he has shaped the careers of thousands of engineers and doctors.");
//         vinFac1.setSpecialization("Advanced Physics, Problem Solving, JEE Strategy");
//         vinFac1.setIitIimBackground(true);
//         vinFac1.setNitBackground(false);
//         vinFac1.setAchievements("Founded Vinayak Classes in 2005. 10 students secured AIR under 10 in JEE Advanced.");
//         vinFac1.setFormerInstitutes("IIT Kanpur, Bansal Classes Kota");
//         vinFac1.setStudentRating(new BigDecimal("4.9"));
//         vinFac1.setIsActive(true);
//         vinFac1.setDisplayOrder(1);
//         vinFac1.setSubjectIdentifiers(List.of(get("sub-physics")));
//         vinFac1.setExamTypeIdentifiers(List.of(get("exam-jee-main"), get("exam-jee-adv")));

//         Faculty vinFac2 = new Faculty();
//         vinFac2.setIdentifier(uuid()); put("fac-vin-2", vinFac2.getIdentifier());
//         vinFac2.setInstituteIdentifier(get("inst-vinayak"));
//         vinFac2.setName("Prof. Neha Gupta");
//         vinFac2.setPhotoUrl("https://example.com/faculty/neha.jpg");
//         vinFac2.setDesignation("HOD Chemistry");
//         vinFac2.setQualification("M.Sc. Chemistry, IIT Roorkee");
//         vinFac2.setExperienceYears(16);
//         vinFac2.setBio("Prof. Neha Gupta is an expert in Organic Chemistry with a teaching career spanning 16 years. Her mnemonic techniques make chemistry memorable.");
//         vinFac2.setSpecialization("Organic Chemistry, Reaction Mechanisms, NEET Chemistry");
//         vinFac2.setIitIimBackground(true);
//         vinFac2.setNitBackground(false);
//         vinFac2.setAchievements("Developed popular organic chemistry mnemonic series. 300+ NEET selections.");
//         vinFac2.setFormerInstitutes("IIT Roorkee, Allen Career Institute");
//         vinFac2.setStudentRating(new BigDecimal("4.8"));
//         vinFac2.setIsActive(true);
//         vinFac2.setDisplayOrder(2);
//         vinFac2.setSubjectIdentifiers(List.of(get("sub-chemistry")));
//         vinFac2.setExamTypeIdentifiers(List.of(get("exam-jee-main"), get("exam-jee-adv"), get("exam-neet")));

//         Faculty vinFac3 = new Faculty();
//         vinFac3.setIdentifier(uuid()); put("fac-vin-3", vinFac3.getIdentifier());
//         vinFac3.setInstituteIdentifier(get("inst-vinayak"));
//         vinFac3.setName("Dr. Arun Joshi");
//         vinFac3.setPhotoUrl("https://example.com/faculty/arun.jpg");
//         vinFac3.setDesignation("HOD Mathematics");
//         vinFac3.setQualification("Ph.D. Mathematics, IISc Bangalore");
//         vinFac3.setExperienceYears(19);
//         vinFac3.setBio("Dr. Arun Joshi brings deep mathematical insights from IISc Bangalore. His problem-solving workshops are legendary among JEE aspirants.");
//         vinFac3.setSpecialization("Advanced Calculus, Number Theory, Combinatorics");
//         vinFac3.setIitIimBackground(true);
//         vinFac3.setNitBackground(false);
//         vinFac3.setAchievements("8 students in JEE Advanced top 100. Author of 3 Mathematics problem books.");
//         vinFac3.setFormerInstitutes("IISc Bangalore, FIITJEE Mumbai");
//         vinFac3.setStudentRating(new BigDecimal("4.9"));
//         vinFac3.setIsActive(true);
//         vinFac3.setDisplayOrder(3);
//         vinFac3.setSubjectIdentifiers(List.of(get("sub-mathematics")));
//         vinFac3.setExamTypeIdentifiers(List.of(get("exam-jee-main"), get("exam-jee-adv")));

//         Faculty vinFac4 = new Faculty();
//         vinFac4.setIdentifier(uuid()); put("fac-vin-4", vinFac4.getIdentifier());
//         vinFac4.setInstituteIdentifier(get("inst-vinayak"));
//         vinFac4.setName("Dr. Meera Iyer");
//         vinFac4.setPhotoUrl("https://example.com/faculty/meera.jpg");
//         vinFac4.setDesignation("HOD Biology");
//         vinFac4.setQualification("MD, AIIMS Delhi");
//         vinFac4.setExperienceYears(14);
//         vinFac4.setBio("Dr. Meera Iyer is an MD from AIIMS with a passion for teaching Biology. Her clinical insights make Biology come alive for NEET aspirants.");
//         vinFac4.setSpecialization("Human Physiology, Genetics, Ecology");
//         vinFac4.setIitIimBackground(false);
//         vinFac4.setNitBackground(false);
//         vinFac4.setAchievements("200+ students in top medical colleges. Known for making complex biological concepts simple.");
//         vinFac4.setFormerInstitutes("AIIMS Delhi, Aakash Institute Mumbai");
//         vinFac4.setStudentRating(new BigDecimal("4.8"));
//         vinFac4.setIsActive(true);
//         vinFac4.setDisplayOrder(4);
//         vinFac4.setSubjectIdentifiers(List.of(get("sub-biology")));
//         vinFac4.setExamTypeIdentifiers(List.of(get("exam-neet")));

//         Faculty vinFac5 = new Faculty();
//         vinFac5.setIdentifier(uuid()); put("fac-vin-5", vinFac5.getIdentifier());
//         vinFac5.setInstituteIdentifier(get("inst-vinayak"));
//         vinFac5.setName("Prof. Sanjay Kulkarni");
//         vinFac5.setPhotoUrl("https://example.com/faculty/sanjay.jpg");
//         vinFac5.setDesignation("MHT-CET Specialist");
//         vinFac5.setQualification("M.Tech, COEP Pune");
//         vinFac5.setExperienceYears(11);
//         vinFac5.setBio("Prof. Sanjay Kulkarni is Maharashtra's leading MHT-CET coach. His deep understanding of the state exam pattern has helped thousands secure admissions in top Maharashtra colleges.");
//         vinFac5.setSpecialization("MHT-CET Physics, Chemistry, Mathematics");
//         vinFac5.setIitIimBackground(false);
//         vinFac5.setNitBackground(true);
//         vinFac5.setAchievements("5000+ students cleared MHT-CET with top percentile. Developed MHT-CET specific test series.");
//         vinFac5.setFormerInstitutes("COEP Pune, Vibrant Academy");
//         vinFac5.setStudentRating(new BigDecimal("4.7"));
//         vinFac5.setIsActive(true);
//         vinFac5.setDisplayOrder(5);
//         vinFac5.setSubjectIdentifiers(List.of(get("sub-physics"), get("sub-chemistry"), get("sub-mathematics")));
//         vinFac5.setExamTypeIdentifiers(List.of(get("exam-mht-cet")));

//         FACULTY_SERVICE.saveAll(List.of(
//                 abhiFac1, abhiFac2, abhiFac3, abhiFac4, abhiFac5,
//                 vinFac1, vinFac2, vinFac3, vinFac4, vinFac5
//         ));
//         log.info("Faculty seeded.");
//     }

//     // ==================== RESULTS ====================
//     private void seedResults() {
//         log.info("Seeding results...");

//         // Abhishek Classes results
//         Result abhiRes1 = new Result();
//         abhiRes1.setIdentifier(uuid());
//         abhiRes1.setInstituteIdentifier(get("inst-abhishek"));
//         abhiRes1.setExamTypeIdentifier(get("exam-jee-adv"));
//         abhiRes1.setExamYear(2024);
//         abhiRes1.setStudentName("Aditya Sharma");
//         abhiRes1.setStudentPhotoUrl("https://example.com/students/aditya.jpg");
//         abhiRes1.setRankOrScoreType(RankOrScoreType.AIR_RANK);
//         abhiRes1.setValue("87");
//         abhiRes1.setCollegeAdmitted("IIT Bombay");
//         abhiRes1.setTestimonialQuote("Abhishek Classes transformed my approach to problem-solving. The faculty's guidance was invaluable in achieving my dream rank.");
//         abhiRes1.setIsVerified(true);
//         abhiRes1.setIsFeatured(true);
//         abhiRes1.setDisplayOrder(1);

//         Result abhiRes2 = new Result();
//         abhiRes2.setIdentifier(uuid());
//         abhiRes2.setInstituteIdentifier(get("inst-abhishek"));
//         abhiRes2.setExamTypeIdentifier(get("exam-jee-main"));
//         abhiRes2.setExamYear(2024);
//         abhiRes2.setStudentName("Neha Verma");
//         abhiRes2.setStudentPhotoUrl("https://example.com/students/neha.jpg");
//         abhiRes2.setRankOrScoreType(RankOrScoreType.PERCENTILE);
//         abhiRes2.setValue("99.87");
//         abhiRes2.setCollegeAdmitted("NIT Trichy");
//         abhiRes2.setTestimonialQuote("The test series at Abhishek Classes was exactly like the actual JEE. It prepared me perfectly for exam day.");
//         abhiRes2.setIsVerified(true);
//         abhiRes2.setIsFeatured(true);
//         abhiRes2.setDisplayOrder(2);

//         Result abhiRes3 = new Result();
//         abhiRes3.setIdentifier(uuid());
//         abhiRes3.setInstituteIdentifier(get("inst-abhishek"));
//         abhiRes3.setExamTypeIdentifier(get("exam-neet"));
//         abhiRes3.setExamYear(2024);
//         abhiRes3.setStudentName("Rohan Joshi");
//         abhiRes3.setStudentPhotoUrl("https://example.com/students/rohan.jpg");
//         abhiRes3.setRankOrScoreType(RankOrScoreType.AIR_RANK);
//         abhiRes3.setValue("342");
//         abhiRes3.setCollegeAdmitted("AIIMS Delhi");
//         abhiRes3.setTestimonialQuote("Dr. Priya Sharma's Biology classes were a game-changer. Her NCERT-focused approach helped me secure a top rank in NEET.");
//         abhiRes3.setIsVerified(true);
//         abhiRes3.setIsFeatured(true);
//         abhiRes3.setDisplayOrder(3);

//         Result abhiRes4 = new Result();
//         abhiRes4.setIdentifier(uuid());
//         abhiRes4.setInstituteIdentifier(get("inst-abhishek"));
//         abhiRes4.setExamTypeIdentifier(get("exam-jee-adv"));
//         abhiRes4.setExamYear(2023);
//         abhiRes4.setStudentName("Karan Mehta");
//         abhiRes4.setStudentPhotoUrl("https://example.com/students/karan.jpg");
//         abhiRes4.setRankOrScoreType(RankOrScoreType.AIR_RANK);
//         abhiRes4.setValue("156");
//         abhiRes4.setCollegeAdmitted("IIT Delhi");
//         abhiRes4.setTestimonialQuote("The doubt sessions and personal mentoring at Abhishek Classes helped me overcome my weak areas in Mathematics.");
//         abhiRes4.setIsVerified(true);
//         abhiRes4.setIsFeatured(false);
//         abhiRes4.setDisplayOrder(4);

//         // Vinayak Classes results
//         Result vinRes1 = new Result();
//         vinRes1.setIdentifier(uuid());
//         vinRes1.setInstituteIdentifier(get("inst-vinayak"));
//         vinRes1.setExamTypeIdentifier(get("exam-jee-adv"));
//         vinRes1.setExamYear(2024);
//         vinRes1.setStudentName("Aarav Patel");
//         vinRes1.setStudentPhotoUrl("https://example.com/students/aarav.jpg");
//         vinRes1.setRankOrScoreType(RankOrScoreType.AIR_RANK);
//         vinRes1.setValue("23");
//         vinRes1.setCollegeAdmitted("IIT Bombay");
//         vinRes1.setTestimonialQuote("Vinayak Classes' advanced problem-solving workshops pushed me beyond my limits. Dr. Vikram Mehta's guidance was extraordinary.");
//         vinRes1.setIsVerified(true);
//         vinRes1.setIsFeatured(true);
//         vinRes1.setDisplayOrder(1);

//         Result vinRes2 = new Result();
//         vinRes2.setIdentifier(uuid());
//         vinRes2.setInstituteIdentifier(get("inst-vinayak"));
//         vinRes2.setExamTypeIdentifier(get("exam-neet"));
//         vinRes2.setExamYear(2024);
//         vinRes2.setStudentName("Isha Gupta");
//         vinRes2.setStudentPhotoUrl("https://example.com/students/isha.jpg");
//         vinRes2.setRankOrScoreType(RankOrScoreType.AIR_RANK);
//         vinRes2.setValue("89");
//         vinRes2.setCollegeAdmitted("AIIMS Delhi");
//         vinRes2.setTestimonialQuote("The biology faculty at Vinayak Classes is exceptional. Dr. Meera Iyer's clinical insights made all the difference.");
//         vinRes2.setIsVerified(true);
//         vinRes2.setIsFeatured(true);
//         vinRes2.setDisplayOrder(2);

//         Result vinRes3 = new Result();
//         vinRes3.setIdentifier(uuid());
//         vinRes3.setInstituteIdentifier(get("inst-vinayak"));
//         vinRes3.setExamTypeIdentifier(get("exam-mht-cet"));
//         vinRes3.setExamYear(2024);
//         vinRes3.setStudentName("Rahul Deshmukh");
//         vinRes3.setStudentPhotoUrl("https://example.com/students/rahul.jpg");
//         vinRes3.setRankOrScoreType(RankOrScoreType.PERCENTILE);
//         vinRes3.setValue("99.95");
//         vinRes3.setCollegeAdmitted("COEP Pune");
//         vinRes3.setTestimonialQuote("Prof. Sanjay Kulkarni's MHT-CET specific coaching was perfect. The test series matched the actual exam pattern exactly.");
//         vinRes3.setIsVerified(true);
//         vinRes3.setIsFeatured(true);
//         vinRes3.setDisplayOrder(3);

//         Result vinRes4 = new Result();
//         vinRes4.setIdentifier(uuid());
//         vinRes4.setInstituteIdentifier(get("inst-vinayak"));
//         vinRes4.setExamTypeIdentifier(get("exam-jee-main"));
//         vinRes4.setExamYear(2023);
//         vinRes4.setStudentName("Priya Nair");
//         vinRes4.setStudentPhotoUrl("https://example.com/students/priya.jpg");
//         vinRes4.setRankOrScoreType(RankOrScoreType.PERCENTILE);
//         vinRes4.setValue("99.72");
//         vinRes4.setCollegeAdmitted("NIT Surathkal");
//         vinRes4.setTestimonialQuote("The online classes and recorded lectures at Vinayak Classes helped me study at my own pace while maintaining quality.");
//         vinRes4.setIsVerified(true);
//         vinRes4.setIsFeatured(false);
//         vinRes4.setDisplayOrder(4);

//         RESULT_SERVICE.saveAll(List.of(abhiRes1, abhiRes2, abhiRes3, abhiRes4, vinRes1, vinRes2, vinRes3, vinRes4));
//         log.info("Results seeded.");
//     }

//     // ==================== AWARDS & RECOGNITIONS ====================
//     private void seedAwardsAndRecognitions() {
//         log.info("Seeding awards and recognitions...");

//         AwardAndRecognition abhiAward1 = new AwardAndRecognition();
//         abhiAward1.setIdentifier(uuid());
//         abhiAward1.setInstituteIdentifier(get("inst-abhishek"));
//         abhiAward1.setTitle("Best Coaching Institute in Central India");
//         abhiAward1.setIssuingBody("Education Today Magazine");
//         abhiAward1.setYear(2024);
//         abhiAward1.setDescription("Recognized as the best coaching institute in Central India for JEE and NEET preparation based on student success rates and faculty quality.");
//         abhiAward1.setCertificateUrl("https://example.com/awards/abhishek-2024.jpg");
//         abhiAward1.setIsVerified(true);
//         abhiAward1.setDisplayOrder(1);

//         AwardAndRecognition abhiAward2 = new AwardAndRecognition();
//         abhiAward2.setIdentifier(uuid());
//         abhiAward2.setInstituteIdentifier(get("inst-abhishek"));
//         abhiAward2.setTitle("Excellence in STEM Education Award");
//         abhiAward2.setIssuingBody("Maharashtra State Education Board");
//         abhiAward2.setYear(2023);
//         abhiAward2.setDescription("Awarded for outstanding contribution to STEM education and producing consistent top ranks in competitive examinations.");
//         abhiAward2.setCertificateUrl("https://example.com/awards/abhishek-2023.jpg");
//         abhiAward2.setIsVerified(true);
//         abhiAward2.setDisplayOrder(2);

//         AwardAndRecognition vinAward1 = new AwardAndRecognition();
//         vinAward1.setIdentifier(uuid());
//         vinAward1.setInstituteIdentifier(get("inst-vinayak"));
//         vinAward1.setTitle("Top JEE Coaching Institute in Maharashtra");
//         vinAward1.setIssuingBody("Times Education Excellence Awards");
//         vinAward1.setYear(2024);
//         vinAward1.setDescription("Ranked #1 JEE coaching institute in Maharashtra based on AIR ranks, student testimonials, and infrastructure.");
//         vinAward1.setCertificateUrl("https://example.com/awards/vinayak-2024.jpg");
//         vinAward1.setIsVerified(true);
//         vinAward1.setDisplayOrder(1);

//         AwardAndRecognition vinAward2 = new AwardAndRecognition();
//         vinAward2.setIdentifier(uuid());
//         vinAward2.setInstituteIdentifier(get("inst-vinayak"));
//         vinAward2.setTitle("Best NEET Coaching - Western India");
//         vinAward2.setIssuingBody("Medical Education Council of India");
//         vinAward2.setYear(2023);
//         vinAward2.setDescription("Recognized for exceptional NEET coaching results with highest selection ratio in Western India.");
//         vinAward2.setCertificateUrl("https://example.com/awards/vinayak-2023.jpg");
//         vinAward2.setIsVerified(true);
//         vinAward2.setDisplayOrder(2);

//         AwardAndRecognition vinAward3 = new AwardAndRecognition();
//         vinAward3.setIdentifier(uuid());
//         vinAward3.setInstituteIdentifier(get("inst-vinayak"));
//         vinAward3.setTitle("Innovation in Digital Learning");
//         vinAward3.setIssuingBody("EdTech India Summit");
//         vinAward3.setYear(2024);
//         vinAward3.setDescription("Awarded for innovative use of technology in coaching with advanced online portal and AI-based test analysis.");
//         vinAward3.setCertificateUrl("https://example.com/awards/vinayak-edtech-2024.jpg");
//         vinAward3.setIsVerified(true);
//         vinAward3.setDisplayOrder(3);

//         AWARD_AND_RECOGNITION_SERVICE.saveAll(List.of(abhiAward1, abhiAward2, vinAward1, vinAward2, vinAward3));
//         log.info("Awards and recognitions seeded.");
//     }

//     // ==================== REVIEWS ====================
//     private void seedReviews() {
//         log.info("Seeding reviews...");

//         Review abhiRev1 = new Review();
//         abhiRev1.setIdentifier(uuid()); put("rev-abhi-1", abhiRev1.getIdentifier());
//         abhiRev1.setInstituteIdentifier(get("inst-abhishek"));
//         abhiRev1.setUserIdentifier(get("user-student-1"));
//         abhiRev1.setCourseTaken("JEE (Main + Advanced) - 12th Pass");
//         abhiRev1.setStandardWhenEnrolled(Standard.STANDARD_12);
//         abhiRev1.setReviewTitle("Best JEE coaching in Nagpur!");
//         abhiRev1.setReviewText("I joined Abhishek Classes for my JEE preparation and it was the best decision I made. The faculty is extremely knowledgeable and the study material is top-notch. Dr. Ramesh Iyer's Physics classes are legendary. The test series helped me improve my speed and accuracy significantly.");
//         abhiRev1.setPros("Excellent faculty, comprehensive study material, regular tests, doubt sessions");
//         abhiRev1.setCons("Hostel food could be better, sometimes batches are crowded");
//         abhiRev1.setOverallRating(new BigDecimal("4.8"));
//         abhiRev1.setFacultyRating(new BigDecimal("5.0"));
//         abhiRev1.setStudyMaterialRating(new BigDecimal("4.5"));
//         abhiRev1.setInfrastructureRating(new BigDecimal("4.0"));
//         abhiRev1.setFeeValueRating(new BigDecimal("4.5"));
//         abhiRev1.setOnlineSupportRating(new BigDecimal("4.0"));
//         abhiRev1.setResultAchievementRating(new BigDecimal("4.8"));
//         abhiRev1.setWouldRecommend(true);
//         abhiRev1.setStatus(ReviewStatus.APPROVED);
//         abhiRev1.setIsVerifiedStudent(true);

//         Review abhiRev2 = new Review();
//         abhiRev2.setIdentifier(uuid()); put("rev-abhi-2", abhiRev2.getIdentifier());
//         abhiRev2.setInstituteIdentifier(get("inst-abhishek"));
//         abhiRev2.setUserIdentifier(get("user-student-2"));
//         abhiRev2.setCourseTaken("NEET - 12th Pass");
//         abhiRev2.setStandardWhenEnrolled(Standard.DROPPER);
//         abhiRev2.setReviewTitle("Excellent NEET preparation");
//         abhiRev2.setReviewText("As a dropper, I needed intensive coaching and Abhishek Classes delivered exactly that. Dr. Priya Sharma's Biology classes were outstanding. The NCERT-focused approach helped me score 680+ in NEET.");
//         abhiRev2.setPros("Focused Biology teaching, regular tests, good study material");
//         abhiRev2.setCons("Limited online resources, no hostel in Pune branch");
//         abhiRev2.setOverallRating(new BigDecimal("4.5"));
//         abhiRev2.setFacultyRating(new BigDecimal("4.8"));
//         abhiRev2.setStudyMaterialRating(new BigDecimal("4.5"));
//         abhiRev2.setInfrastructureRating(new BigDecimal("3.8"));
//         abhiRev2.setFeeValueRating(new BigDecimal("4.5"));
//         abhiRev2.setOnlineSupportRating(new BigDecimal("3.5"));
//         abhiRev2.setResultAchievementRating(new BigDecimal("4.7"));
//         abhiRev2.setWouldRecommend(true);
//         abhiRev2.setStatus(ReviewStatus.APPROVED);
//         abhiRev2.setIsVerifiedStudent(true);

//         Review abhiRev3 = new Review();
//         abhiRev3.setIdentifier(uuid()); put("rev-abhi-3", abhiRev3.getIdentifier());
//         abhiRev3.setInstituteIdentifier(get("inst-abhishek"));
//         abhiRev3.setUserIdentifier(get("user-student-3"));
//         abhiRev3.setCourseTaken("Foundation Course - Class 9 & 10");
//         abhiRev3.setStandardWhenEnrolled(Standard.STANDARD_10);
//         abhiRev3.setReviewTitle("Great foundation for competitive exams");
//         abhiRev3.setReviewText("I joined the foundation course in class 9 and it really built my concepts strong. Prof. Rajesh Patel makes science so interesting. The course prepared me well for future competitive exams.");
//         abhiRev3.setPros("Conceptual teaching, interactive classes, good foundation");
//         abhiRev3.setCons("Classes are only on weekends, limited extra activities");
//         abhiRev3.setOverallRating(new BigDecimal("4.3"));
//         abhiRev3.setFacultyRating(new BigDecimal("4.5"));
//         abhiRev3.setStudyMaterialRating(new BigDecimal("4.2"));
//         abhiRev3.setInfrastructureRating(new BigDecimal("4.0"));
//         abhiRev3.setFeeValueRating(new BigDecimal("4.5"));
//         abhiRev3.setOnlineSupportRating(new BigDecimal("3.8"));
//         abhiRev3.setResultAchievementRating(new BigDecimal("4.2"));
//         abhiRev3.setWouldRecommend(true);
//         abhiRev3.setStatus(ReviewStatus.APPROVED);
//         abhiRev3.setIsVerifiedStudent(true);

//         Review abhiRev4 = new Review();
//         abhiRev4.setIdentifier(uuid()); put("rev-abhi-4", abhiRev4.getIdentifier());
//         abhiRev4.setInstituteIdentifier(get("inst-abhishek"));
//         abhiRev4.setUserIdentifier(get("user-student-4"));
//         abhiRev4.setCourseTaken("JEE Droppers Batch");
//         abhiRev4.setStandardWhenEnrolled(Standard.DROPPER);
//         abhiRev4.setReviewTitle("Transformed my JEE preparation");
//         abhiRev4.setReviewText("The droppers batch at Abhishek Classes was exactly what I needed. The intensive schedule, daily practice papers, and personalized mentoring helped me improve my rank from 15,000 to under 500.");
//         abhiRev4.setPros("Intensive coaching, daily tests, personal mentoring");
//         abhiRev4.setCons("Very demanding schedule, less personal time");
//         abhiRev4.setOverallRating(new BigDecimal("4.7"));
//         abhiRev4.setFacultyRating(new BigDecimal("4.9"));
//         abhiRev4.setStudyMaterialRating(new BigDecimal("4.6"));
//         abhiRev4.setInfrastructureRating(new BigDecimal("4.2"));
//         abhiRev4.setFeeValueRating(new BigDecimal("4.5"));
//         abhiRev4.setOnlineSupportRating(new BigDecimal("4.0"));
//         abhiRev4.setResultAchievementRating(new BigDecimal("4.9"));
//         abhiRev4.setWouldRecommend(true);
//         abhiRev4.setStatus(ReviewStatus.APPROVED);
//         abhiRev4.setIsVerifiedStudent(true);

//         Review abhiRev5 = new Review();
//         abhiRev5.setIdentifier(uuid()); put("rev-abhi-5", abhiRev5.getIdentifier());
//         abhiRev5.setInstituteIdentifier(get("inst-abhishek"));
//         abhiRev5.setUserIdentifier(get("user-student-5"));
//         abhiRev5.setCourseTaken("JEE Crash Course");
//         abhiRev5.setStandardWhenEnrolled(Standard.STANDARD_12);
//         abhiRev5.setReviewTitle("Good crash course but fast paced");
//         abhiRev5.setReviewText("The crash course covered important topics well but the pace was very fast. Good for revision but not for learning new concepts. The test series was helpful.");
//         abhiRev5.setPros("Good revision, helpful test series, experienced faculty");
//         abhiRev5.setCons("Very fast pace, less time for doubts");
//         abhiRev5.setOverallRating(new BigDecimal("3.8"));
//         abhiRev5.setFacultyRating(new BigDecimal("4.2"));
//         abhiRev5.setStudyMaterialRating(new BigDecimal("4.0"));
//         abhiRev5.setInfrastructureRating(new BigDecimal("3.5"));
//         abhiRev5.setFeeValueRating(new BigDecimal("4.0"));
//         abhiRev5.setOnlineSupportRating(new BigDecimal("3.5"));
//         abhiRev5.setResultAchievementRating(new BigDecimal("3.8"));
//         abhiRev5.setWouldRecommend(true);
//         abhiRev5.setStatus(ReviewStatus.APPROVED);
//         abhiRev5.setIsVerifiedStudent(false);

//         // Vinayak Classes reviews
//         Review vinRev1 = new Review();
//         vinRev1.setIdentifier(uuid()); put("rev-vin-1", vinRev1.getIdentifier());
//         vinRev1.setInstituteIdentifier(get("inst-vinayak"));
//         vinRev1.setUserIdentifier(get("user-student-1"));
//         vinRev1.setCourseTaken("JEE Advanced Mastery Program");
//         vinRev1.setStandardWhenEnrolled(Standard.STANDARD_12);
//         vinRev1.setReviewTitle("Outstanding JEE coaching in Mumbai");
//         vinRev1.setReviewText("Vinayak Classes offers the best JEE coaching in Mumbai. Dr. Vikram Mehta's Physics classes are extraordinary. The advanced problem-solving workshops pushed me to think beyond standard problems.");
//         vinRev1.setPros("World-class faculty, advanced problem solving, excellent test series");
//         vinRev1.setCons("Fee is on the higher side, Thane branch has limited courses");
//         vinRev1.setOverallRating(new BigDecimal("4.9"));
//         vinRev1.setFacultyRating(new BigDecimal("5.0"));
//         vinRev1.setStudyMaterialRating(new BigDecimal("4.8"));
//         vinRev1.setInfrastructureRating(new BigDecimal("4.5"));
//         vinRev1.setFeeValueRating(new BigDecimal("4.2"));
//         vinRev1.setOnlineSupportRating(new BigDecimal("4.5"));
//         vinRev1.setResultAchievementRating(new BigDecimal("4.9"));
//         vinRev1.setWouldRecommend(true);
//         vinRev1.setStatus(ReviewStatus.APPROVED);
//         vinRev1.setIsVerifiedStudent(true);

//         Review vinRev2 = new Review();
//         vinRev2.setIdentifier(uuid()); put("rev-vin-2", vinRev2.getIdentifier());
//         vinRev2.setInstituteIdentifier(get("inst-vinayak"));
//         vinRev2.setUserIdentifier(get("user-student-2"));
//         vinRev2.setCourseTaken("NEET Ultimate Program");
//         vinRev2.setStandardWhenEnrolled(Standard.DROPPER);
//         vinRev2.setReviewTitle("Best NEET coaching with clinical insights");
//         vinRev2.setReviewText("Dr. Meera Iyer's Biology classes at Vinayak Classes are unmatched. Her clinical insights from AIIMS made Biology so much more interesting. I scored 705 in NEET!");
//         vinRev2.setPros("Exceptional Biology faculty, clinical insights, comprehensive material");
//         vinRev2.setCons("Chemistry classes could be more interactive");
//         vinRev2.setOverallRating(new BigDecimal("4.7"));
//         vinRev2.setFacultyRating(new BigDecimal("4.9"));
//         vinRev2.setStudyMaterialRating(new BigDecimal("4.6"));
//         vinRev2.setInfrastructureRating(new BigDecimal("4.3"));
//         vinRev2.setFeeValueRating(new BigDecimal("4.2"));
//         vinRev2.setOnlineSupportRating(new BigDecimal("4.4"));
//         vinRev2.setResultAchievementRating(new BigDecimal("4.9"));
//         vinRev2.setWouldRecommend(true);
//         vinRev2.setStatus(ReviewStatus.APPROVED);
//         vinRev2.setIsVerifiedStudent(true);

//         Review vinRev3 = new Review();
//         vinRev3.setIdentifier(uuid()); put("rev-vin-3", vinRev3.getIdentifier());
//         vinRev3.setInstituteIdentifier(get("inst-vinayak"));
//         vinRev3.setUserIdentifier(get("user-student-3"));
//         vinRev3.setCourseTaken("Foundation Course - Class 11");
//         vinRev3.setStandardWhenEnrolled(Standard.STANDARD_11);
//         vinRev3.setReviewTitle("Solid foundation for competitive exams");
//         vinRev3.setReviewText("The foundation course at Vinayak Classes helped me build strong fundamentals. The integrated approach covering school syllabus and competitive exam prep is very effective.");
//         vinRev3.setPros("Integrated approach, good faculty, regular assessments");
//         vinRev3.setCons("Long class hours, less time for self-study");
//         vinRev3.setOverallRating(new BigDecimal("4.4"));
//         vinRev3.setFacultyRating(new BigDecimal("4.5"));
//         vinRev3.setStudyMaterialRating(new BigDecimal("4.4"));
//         vinRev3.setInfrastructureRating(new BigDecimal("4.2"));
//         vinRev3.setFeeValueRating(new BigDecimal("4.3"));
//         vinRev3.setOnlineSupportRating(new BigDecimal("4.0"));
//         vinRev3.setResultAchievementRating(new BigDecimal("4.3"));
//         vinRev3.setWouldRecommend(true);
//         vinRev3.setStatus(ReviewStatus.APPROVED);
//         vinRev3.setIsVerifiedStudent(true);

//         Review vinRev4 = new Review();
//         vinRev4.setIdentifier(uuid()); put("rev-vin-4", vinRev4.getIdentifier());
//         vinRev4.setInstituteIdentifier(get("inst-vinayak"));
//         vinRev4.setUserIdentifier(get("user-student-4"));
//         vinRev4.setCourseTaken("MHT-CET Comprehensive");
//         vinRev4.setStandardWhenEnrolled(Standard.STANDARD_12);
//         vinRev4.setReviewTitle("Best MHT-CET coaching in Maharashtra");
//         vinRev4.setReviewText("Prof. Sanjay Kulkarni is the best MHT-CET coach in Maharashtra. His deep understanding of the exam pattern and state-specific strategies helped me secure 99.95 percentile.");
//         vinRev4.setPros("MHT-CET specialist, state exam focus, excellent test series");
//         vinRev4.setCons("Limited JEE focus in MHT-CET batch");
//         vinRev4.setOverallRating(new BigDecimal("4.6"));
//         vinRev4.setFacultyRating(new BigDecimal("4.9"));
//         vinRev4.setStudyMaterialRating(new BigDecimal("4.5"));
//         vinRev4.setInfrastructureRating(new BigDecimal("4.0"));
//         vinRev4.setFeeValueRating(new BigDecimal("4.5"));
//         vinRev4.setOnlineSupportRating(new BigDecimal("4.0"));
//         vinRev4.setResultAchievementRating(new BigDecimal("4.8"));
//         vinRev4.setWouldRecommend(true);
//         vinRev4.setStatus(ReviewStatus.APPROVED);
//         vinRev4.setIsVerifiedStudent(true);

//         Review vinRev5 = new Review();
//         vinRev5.setIdentifier(uuid()); put("rev-vin-5", vinRev5.getIdentifier());
//         vinRev5.setInstituteIdentifier(get("inst-vinayak"));
//         vinRev5.setUserIdentifier(get("user-student-5"));
//         vinRev5.setCourseTaken("JEE Advanced Mastery Program");
//         vinRev5.setStandardWhenEnrolled(Standard.STANDARD_12);
//         vinRev5.setReviewTitle("Premium coaching with great results");
//         vinRev5.setReviewText("Vinayak Classes provides premium coaching experience. The faculty, infrastructure, and study material are all top quality. The online portal is very helpful for revision.");
//         vinRev5.setPros("Premium infrastructure, excellent faculty, good online support");
//         vinRev5.setCons("High fees, Borivali location is far for some students");
//         vinRev5.setOverallRating(new BigDecimal("4.5"));
//         vinRev5.setFacultyRating(new BigDecimal("4.7"));
//         vinRev5.setStudyMaterialRating(new BigDecimal("4.6"));
//         vinRev5.setInfrastructureRating(new BigDecimal("4.5"));
//         vinRev5.setFeeValueRating(new BigDecimal("4.0"));
//         vinRev5.setOnlineSupportRating(new BigDecimal("4.5"));
//         vinRev5.setResultAchievementRating(new BigDecimal("4.6"));
//         vinRev5.setWouldRecommend(true);
//         vinRev5.setStatus(ReviewStatus.APPROVED);
//         vinRev5.setIsVerifiedStudent(false);

//         REVIEW_SERVICE.saveAll(List.of(
//                 abhiRev1, abhiRev2, abhiRev3, abhiRev4, abhiRev5,
//                 vinRev1, vinRev2, vinRev3, vinRev4, vinRev5
//         ));
//         log.info("Reviews seeded.");
//     }

//     // ==================== REVIEW VOTES ====================
//     private void seedReviewVotes() {
//         log.info("Seeding review votes...");

//         ReviewVote rv1 = new ReviewVote();
//         rv1.setIdentifier(uuid());
//         rv1.setReviewIdentifier(get("rev-abhi-1"));
//         rv1.setUserIdentifier(get("user-student-2"));
//         rv1.setVote(VoteType.HELPFUL);

//         ReviewVote rv2 = new ReviewVote();
//         rv2.setIdentifier(uuid());
//         rv2.setReviewIdentifier(get("rev-abhi-1"));
//         rv2.setUserIdentifier(get("user-student-3"));
//         rv2.setVote(VoteType.HELPFUL);

//         ReviewVote rv3 = new ReviewVote();
//         rv3.setIdentifier(uuid());
//         rv3.setReviewIdentifier(get("rev-abhi-2"));
//         rv3.setUserIdentifier(get("user-student-1"));
//         rv3.setVote(VoteType.HELPFUL);

//         ReviewVote rv4 = new ReviewVote();
//         rv4.setIdentifier(uuid());
//         rv4.setReviewIdentifier(get("rev-vin-1"));
//         rv4.setUserIdentifier(get("user-student-2"));
//         rv4.setVote(VoteType.HELPFUL);

//         ReviewVote rv5 = new ReviewVote();
//         rv5.setIdentifier(uuid());
//         rv5.setReviewIdentifier(get("rev-vin-1"));
//         rv5.setUserIdentifier(get("user-student-3"));
//         rv5.setVote(VoteType.HELPFUL);

//         ReviewVote rv6 = new ReviewVote();
//         rv6.setIdentifier(uuid());
//         rv6.setReviewIdentifier(get("rev-vin-2"));
//         rv6.setUserIdentifier(get("user-student-1"));
//         rv6.setVote(VoteType.HELPFUL);

//         ReviewVote rv7 = new ReviewVote();
//         rv7.setIdentifier(uuid());
//         rv7.setReviewIdentifier(get("rev-abhi-5"));
//         rv7.setUserIdentifier(get("user-student-4"));
//         rv7.setVote(VoteType.NOT_HELPFUL);

//         REVIEW_VOTE_SERVICE.saveAll(List.of(rv1, rv2, rv3, rv4, rv5, rv6, rv7));
//         log.info("Review votes seeded.");
//     }

//     // ==================== INSTITUTE RESPONSES ====================
//     private void seedInstituteResponses() {
//         log.info("Seeding institute responses...");

//         InstituteResponse resp1 = new InstituteResponse();
//         resp1.setIdentifier(uuid());
//         resp1.setReviewIdentifier(get("rev-abhi-1"));
//         resp1.setInstituteIdentifier(get("inst-abhishek"));
//         resp1.setResponseText("Thank you Rahul for your kind words! We are thrilled to hear about your positive experience. We constantly work on improving our hostel facilities and will take your feedback seriously.");
//         resp1.setRespondedBy(get("user-abhishek-admin"));

//         InstituteResponse resp2 = new InstituteResponse();
//         resp2.setIdentifier(uuid());
//         resp2.setReviewIdentifier(get("rev-abhi-2"));
//         resp2.setInstituteIdentifier(get("inst-abhishek"));
//         resp2.setResponseText("Congratulations Priya on your NEET success! We are proud of your achievement. We are expanding our online resources and hostel facilities in Pune to serve our students better.");
//         resp2.setRespondedBy(get("user-abhishek-admin"));

//         InstituteResponse resp3 = new InstituteResponse();
//         resp3.setIdentifier(uuid());
//         resp3.setReviewIdentifier(get("rev-vin-1"));
//         resp3.setInstituteIdentifier(get("inst-vinayak"));
//         resp3.setResponseText("Thank you for your wonderful review! We strive to maintain the highest standards of coaching. We are continuously adding more courses at our Thane branch to serve students better.");
//         resp3.setRespondedBy(get("user-vinayak-admin"));

//         InstituteResponse resp4 = new InstituteResponse();
//         resp4.setIdentifier(uuid());
//         resp4.setReviewIdentifier(get("rev-vin-2"));
//         resp4.setInstituteIdentifier(get("inst-vinayak"));
//         resp4.setResponseText("Congratulations on your NEET score! Dr. Meera Iyer and the entire team are delighted. We are working on making Chemistry classes more interactive with hands-on experiments.");
//         resp4.setRespondedBy(get("user-vinayak-admin"));

//         INSTITUTE_RESPONSE_SERVICE.saveAll(List.of(resp1, resp2, resp3, resp4));
//         log.info("Institute responses seeded.");
//     }

//     // ==================== FAQS ====================
//     private void seedFaqs() {
//         log.info("Seeding FAQs...");

//         // Abhishek Classes FAQs
//         Faq abhiFaq1 = new Faq();
//         abhiFaq1.setIdentifier(uuid());
//         abhiFaq1.setInstituteIdentifier(get("inst-abhishek"));
//         abhiFaq1.setQuestion("What courses does Abhishek Classes offer?");
//         abhiFaq1.setAnswer("We offer comprehensive courses for JEE Main, JEE Advanced, NEET, Foundation (Classes 9-10), and Crash Courses. Each course is designed with a focus on conceptual clarity and extensive practice.");
//         abhiFaq1.setDisplayOrder(1);
//         abhiFaq1.setIsActive(true);

//         Faq abhiFaq2 = new Faq();
//         abhiFaq2.setIdentifier(uuid());
//         abhiFaq2.setInstituteIdentifier(get("inst-abhishek"));
//         abhiFaq2.setQuestion("What is the batch size at Abhishek Classes?");
//         abhiFaq2.setAnswer("Our batch sizes are maintained at 25:1 student-to-teacher ratio to ensure personalized attention for every student.");
//         abhiFaq2.setDisplayOrder(2);
//         abhiFaq2.setIsActive(true);

//         Faq abhiFaq3 = new Faq();
//         abhiFaq3.setIdentifier(uuid());
//         abhiFaq3.setInstituteIdentifier(get("inst-abhishek"));
//         abhiFaq3.setQuestion("Do you provide hostel facilities?");
//         abhiFaq3.setAnswer("Yes, we provide separate hostel facilities for boys and girls at our Nagpur main centre with nutritious food, Wi-Fi, and 24/7 security.");
//         abhiFaq3.setDisplayOrder(3);
//         abhiFaq3.setIsActive(true);

//         Faq abhiFaq4 = new Faq();
//         abhiFaq4.setIdentifier(uuid());
//         abhiFaq4.setInstituteIdentifier(get("inst-abhishek"));
//         abhiFaq4.setQuestion("What is the fee structure for JEE coaching?");
//         abhiFaq4.setAnswer("Our JEE coaching fees range from Rs. 1,20,000 to Rs. 1,50,000 per year. We also offer scholarships up to 50% based on entrance test performance.");
//         abhiFaq4.setDisplayOrder(4);
//         abhiFaq4.setIsActive(true);

//         Faq abhiFaq5 = new Faq();
//         abhiFaq5.setIdentifier(uuid());
//         abhiFaq5.setInstituteIdentifier(get("inst-abhishek"));
//         abhiFaq5.setQuestion("Do you offer online classes?");
//         abhiFaq5.setAnswer("Yes, we offer hybrid learning with both offline and online classes. All lectures are recorded and available on our online portal for revision.");
//         abhiFaq5.setDisplayOrder(5);
//         abhiFaq5.setIsActive(true);

//         Faq abhiFaq6 = new Faq();
//         abhiFaq6.setIdentifier(uuid());
//         abhiFaq6.setInstituteIdentifier(get("inst-abhishek"));
//         abhiFaq6.setQuestion("How can I apply for a scholarship?");
//         abhiFaq6.setAnswer("Scholarships are awarded based on our entrance test (Abhishek Talent Search Exam) conducted monthly. Students can register online or visit our centre.");
//         abhiFaq6.setDisplayOrder(6);
//         abhiFaq6.setIsActive(true);

//         // Vinayak Classes FAQs
//         Faq vinFaq1 = new Faq();
//         vinFaq1.setIdentifier(uuid());
//         vinFaq1.setInstituteIdentifier(get("inst-vinayak"));
//         vinFaq1.setQuestion("What makes Vinayak Classes different from other coaching institutes?");
//         vinFaq1.setAnswer("Vinayak Classes stands out with our IIT-alumni faculty, AI-based test analysis, personalized mentoring, and proven track record of top ranks in JEE and NEET.");
//         vinFaq1.setDisplayOrder(1);
//         vinFaq1.setIsActive(true);

//         Faq vinFaq2 = new Faq();
//         vinFaq2.setIdentifier(uuid());
//         vinFaq2.setInstituteIdentifier(get("inst-vinayak"));
//         vinFaq2.setQuestion("What are the class timings?");
//         vinFaq2.setAnswer("We offer flexible batch timings - Morning (7 AM - 1 PM), Afternoon (2 PM - 8 PM), and Weekend batches for working students.");
//         vinFaq2.setDisplayOrder(2);
//         vinFaq2.setIsActive(true);

//         Faq vinFaq3 = new Faq();
//         vinFaq3.setIdentifier(uuid());
//         vinFaq3.setInstituteIdentifier(get("inst-vinayak"));
//         vinFaq3.setQuestion("Do you provide study material?");
//         vinFaq3.setAnswer("Yes, we provide comprehensive study material designed by our expert faculty. It includes theory notes, solved examples, practice problems, and previous year papers.");
//         vinFaq3.setDisplayOrder(3);
//         vinFaq3.setIsActive(true);

//         Faq vinFaq4 = new Faq();
//         vinFaq4.setIdentifier(uuid());
//         vinFaq4.setInstituteIdentifier(get("inst-vinayak"));
//         vinFaq4.setQuestion("What is your success rate in JEE Advanced?");
//         vinFaq4.setAnswer("Over the last 5 years, 85% of our JEE Advanced students have secured ranks under 10,000, with multiple students in the top 100.");
//         vinFaq4.setDisplayOrder(4);
//         vinFaq4.setIsActive(true);

//         Faq vinFaq5 = new Faq();
//         vinFaq5.setIdentifier(uuid());
//         vinFaq5.setInstituteIdentifier(get("inst-vinayak"));
//         vinFaq5.setQuestion("Can I switch between offline and online classes?");
//         vinFaq5.setAnswer("Yes, our hybrid model allows students to attend classes offline and access recorded lectures online. You can switch between modes as per your convenience.");
//         vinFaq5.setDisplayOrder(5);
//         vinFaq5.setIsActive(true);

//         Faq vinFaq6 = new Faq();
//         vinFaq6.setIdentifier(uuid());
//         vinFaq6.setInstituteIdentifier(get("inst-vinayak"));
//         vinFaq6.setQuestion("Do you offer demo classes?");
//         vinFaq6.setAnswer("Yes, we offer free demo classes for all courses. You can register on our website or visit any of our centres to schedule a demo.");
//         vinFaq6.setDisplayOrder(6);
//         vinFaq6.setIsActive(true);

//         FAQ_SERVICE.saveAll(List.of(
//                 abhiFaq1, abhiFaq2, abhiFaq3, abhiFaq4, abhiFaq5, abhiFaq6,
//                 vinFaq1, vinFaq2, vinFaq3, vinFaq4, vinFaq5, vinFaq6
//         ));
//         log.info("FAQs seeded.");
//     }

//     // ==================== MEDIA ====================
//     private void seedMedia() {
//         log.info("Seeding media...");

//         // Abhishek Classes media
//         Media abhiLogo = new Media();
//         abhiLogo.setIdentifier(uuid());
//         abhiLogo.setInstituteIdentifier(get("inst-abhishek"));
//         abhiLogo.setEntityType(MediaEntityType.INSTITUTE);
//         abhiLogo.setMediaType(MediaType.IMAGE);
//         abhiLogo.setUrl("https://example.com/logos/abhishek.png");
//         abhiLogo.setThumbnailUrl("https://example.com/logos/abhishek-thumb.png");
//         abhiLogo.setCaption("Abhishek Classes Logo");
//         abhiLogo.setAltText("Abhishek Classes Official Logo");
//         abhiLogo.setIsFeatured(true);
//         abhiLogo.setDisplayOrder(1);
//         abhiLogo.setFileSizeKb(45);
//         abhiLogo.setUploadedBy(get("user-abhishek-admin"));

//         Media abhiBanner = new Media();
//         abhiBanner.setIdentifier(uuid());
//         abhiBanner.setInstituteIdentifier(get("inst-abhishek"));
//         abhiBanner.setEntityType(MediaEntityType.INSTITUTE);
//         abhiBanner.setMediaType(MediaType.IMAGE);
//         abhiBanner.setUrl("https://example.com/banners/abhishek-banner.jpg");
//         abhiBanner.setThumbnailUrl("https://example.com/banners/abhishek-banner-thumb.jpg");
//         abhiBanner.setCaption("Abhishek Classes Campus");
//         abhiBanner.setAltText("Abhishek Classes Main Campus Building");
//         abhiBanner.setIsFeatured(true);
//         abhiBanner.setDisplayOrder(2);
//         abhiBanner.setFileSizeKb(180);
//         abhiBanner.setUploadedBy(get("user-abhishek-admin"));

//         Media abhiClassroom = new Media();
//         abhiClassroom.setIdentifier(uuid());
//         abhiClassroom.setInstituteIdentifier(get("inst-abhishek"));
//         abhiClassroom.setBranchIdentifier(get("branch-abhishek-main"));
//         abhiClassroom.setEntityType(MediaEntityType.BRANCH);
//         abhiClassroom.setMediaType(MediaType.IMAGE);
//         abhiClassroom.setUrl("https://example.com/media/abhishek-classroom.jpg");
//         abhiClassroom.setThumbnailUrl("https://example.com/media/abhishek-classroom-thumb.jpg");
//         abhiClassroom.setCaption("Smart Classroom at Nagpur Centre");
//         abhiClassroom.setAltText("Air-conditioned smart classroom with digital boards");
//         abhiClassroom.setIsFeatured(false);
//         abhiClassroom.setDisplayOrder(1);
//         abhiClassroom.setFileSizeKb(120);
//         abhiClassroom.setUploadedBy(get("user-abhishek-admin"));

//         Media abhiLab = new Media();
//         abhiLab.setIdentifier(uuid());
//         abhiLab.setInstituteIdentifier(get("inst-abhishek"));
//         abhiLab.setBranchIdentifier(get("branch-abhishek-main"));
//         abhiLab.setEntityType(MediaEntityType.FACILITY);
//         abhiLab.setMediaType(MediaType.IMAGE);
//         abhiLab.setUrl("https://example.com/media/abhishek-lab.jpg");
//         abhiLab.setThumbnailUrl("https://example.com/media/abhishek-lab-thumb.jpg");
//         abhiLab.setCaption("Physics and Chemistry Laboratory");
//         abhiLab.setAltText("Well-equipped science laboratory");
//         abhiLab.setIsFeatured(false);
//         abhiLab.setDisplayOrder(2);
//         abhiLab.setFileSizeKb(150);
//         abhiLab.setUploadedBy(get("user-abhishek-admin"));

//         // Vinayak Classes media
//         Media vinLogo = new Media();
//         vinLogo.setIdentifier(uuid());
//         vinLogo.setInstituteIdentifier(get("inst-vinayak"));
//         vinLogo.setEntityType(MediaEntityType.INSTITUTE);
//         vinLogo.setMediaType(MediaType.IMAGE);
//         vinLogo.setUrl("https://example.com/logos/vinayak.png");
//         vinLogo.setThumbnailUrl("https://example.com/logos/vinayak-thumb.png");
//         vinLogo.setCaption("Vinayak Classes Logo");
//         vinLogo.setAltText("Vinayak Classes Official Logo");
//         vinLogo.setIsFeatured(true);
//         vinLogo.setDisplayOrder(1);
//         vinLogo.setFileSizeKb(50);
//         vinLogo.setUploadedBy(get("user-vinayak-admin"));

//         Media vinBanner = new Media();
//         vinBanner.setIdentifier(uuid());
//         vinBanner.setInstituteIdentifier(get("inst-vinayak"));
//         vinBanner.setEntityType(MediaEntityType.INSTITUTE);
//         vinBanner.setMediaType(MediaType.IMAGE);
//         vinBanner.setUrl("https://example.com/banners/vinayak-banner.jpg");
//         vinBanner.setThumbnailUrl("https://example.com/banners/vinayak-banner-thumb.jpg");
//         vinBanner.setCaption("Vinayak Classes Mumbai Campus");
//         vinBanner.setAltText("Vinayak Classes Main Building in Mumbai");
//         vinBanner.setIsFeatured(true);
//         vinBanner.setDisplayOrder(2);
//         vinBanner.setFileSizeKb(200);
//         vinBanner.setUploadedBy(get("user-vinayak-admin"));

//         Media vinClassroom = new Media();
//         vinClassroom.setIdentifier(uuid());
//         vinClassroom.setInstituteIdentifier(get("inst-vinayak"));
//         vinClassroom.setBranchIdentifier(get("branch-vinayak-main"));
//         vinClassroom.setEntityType(MediaEntityType.BRANCH);
//         vinClassroom.setMediaType(MediaType.IMAGE);
//         vinClassroom.setUrl("https://example.com/media/vinayak-classroom.jpg");
//         vinClassroom.setThumbnailUrl("https://example.com/media/vinayak-classroom-thumb.jpg");
//         vinClassroom.setCaption("Modern Classroom at Borivali Centre");
//         vinClassroom.setAltText("Spacious classroom with projector and AC");
//         vinClassroom.setIsFeatured(false);
//         vinClassroom.setDisplayOrder(1);
//         vinClassroom.setFileSizeKb(130);
//         vinClassroom.setUploadedBy(get("user-vinayak-admin"));

//         Media vinLibrary = new Media();
//         vinLibrary.setIdentifier(uuid());
//         vinLibrary.setInstituteIdentifier(get("inst-vinayak"));
//         vinLibrary.setBranchIdentifier(get("branch-vinayak-main"));
//         vinLibrary.setEntityType(MediaEntityType.FACILITY);
//         vinLibrary.setMediaType(MediaType.IMAGE);
//         vinLibrary.setUrl("https://example.com/media/vinayak-library.jpg");
//         vinLibrary.setThumbnailUrl("https://example.com/media/vinayak-library-thumb.jpg");
//         vinLibrary.setCaption("Library and Study Area");
//         vinLibrary.setAltText("Well-stocked library with study cubicles");
//         vinLibrary.setIsFeatured(false);
//         vinLibrary.setDisplayOrder(2);
//         vinLibrary.setFileSizeKb(160);
//         vinLibrary.setUploadedBy(get("user-vinayak-admin"));

//         MEDIA_SERVICE.saveAll(List.of(
//                 abhiLogo, abhiBanner, abhiClassroom, abhiLab,
//                 vinLogo, vinBanner, vinClassroom, vinLibrary
//         ));
//         log.info("Media seeded.");
//     }

//     // ==================== INQUIRIES ====================
//     private void seedInquiries() {
//         log.info("Seeding inquiries...");

//         Inquiry inq1 = new Inquiry();
//         inq1.setIdentifier(uuid());
//         inq1.setInstituteIdentifier(get("inst-abhishek"));
//         inq1.setBranchIdentifier(get("branch-abhishek-main"));
//         inq1.setCourseIdentifier(get("ic-abhi-jee-12"));
//         inq1.setUserIdentifier(get("user-student-1"));
//         inq1.setName("Rahul Sharma");
//         inq1.setEmail("rahul.sharma@email.com");
//         inq1.setPhone("9876543210");
//         inq1.setStandard("12");
//         inq1.setTargetExam("JEE Main & Advanced");
//         inq1.setMessage("I am interested in the JEE coaching program. Can you provide details about the batch timings and fee structure? I am currently in 12th standard and want to start preparation immediately.");
//         inq1.setSource(InquirySource.LISTING_PAGE);
//         inq1.setStatus(InquiryStatus.CONTACTED);
//         inq1.setAssignedTo(get("user-abhishek-admin"));
//         inq1.setInstituteNotes("Called the student. He is interested in morning batch. Sent fee brochure via email.");

//         Inquiry inq2 = new Inquiry();
//         inq2.setIdentifier(uuid());
//         inq2.setInstituteIdentifier(get("inst-abhishek"));
//         inq2.setBranchIdentifier(get("branch-abhishek-main"));
//         inq2.setCourseIdentifier(get("ic-abhi-neet-12"));
//         inq2.setUserIdentifier(get("user-student-2"));
//         inq2.setName("Priya Patel");
//         inq2.setEmail("priya.patel@email.com");
//         inq2.setPhone("9876543211");
//         inq2.setStandard("DROPPER");
//         inq2.setTargetExam("NEET");
//         inq2.setMessage("I am a dropper looking for intensive NEET coaching. What is the batch size and do you provide hostel facilities? I am from Kota and willing to relocate to Nagpur.");
//         inq2.setSource(InquirySource.COURSE_PAGE);
//         inq2.setStatus(InquiryStatus.FOLLOW_UP);
//         inq2.setAssignedTo(get("user-abhishek-admin"));
//         inq2.setInstituteNotes("Student is from Kota. Interested in hostel. Sent hostel details and arranged virtual campus tour.");

//         Inquiry inq3 = new Inquiry();
//         inq3.setIdentifier(uuid());
//         inq3.setInstituteIdentifier(get("inst-abhishek"));
//         inq3.setBranchIdentifier(get("branch-abhishek-pune"));
//         inq3.setCourseIdentifier(get("ic-abhi-foundation"));
//         inq3.setUserIdentifier(get("user-student-3"));
//         inq3.setName("Ankit Mishra");
//         inq3.setEmail("ankit.mishra@email.com");
//         inq3.setPhone("9876543212");
//         inq3.setStandard("10");
//         inq3.setTargetExam("JEE Foundation");
//         inq3.setMessage("My son is in class 10 and I want to enroll him in the foundation course. What is the duration and fee? Do you have weekend batches?");
//         inq3.setSource(InquirySource.CALLBACK_REQUEST);
//         inq3.setStatus(InquiryStatus.NEW);
//         inq3.setAssignedTo(get("user-abhishek-admin"));

//         Inquiry inq4 = new Inquiry();
//         inq4.setIdentifier(uuid());
//         inq4.setInstituteIdentifier(get("inst-vinayak"));
//         inq4.setBranchIdentifier(get("branch-vinayak-main"));
//         inq4.setCourseIdentifier(get("ic-vin-jee-adv"));
//         inq4.setUserIdentifier(get("user-student-1"));
//         inq4.setName("Rahul Sharma");
//         inq4.setEmail("rahul.sharma@email.com");
//         inq4.setPhone("9876543210");
//         inq4.setStandard("12");
//         inq4.setTargetExam("JEE Advanced");
//         inq4.setMessage("I want to know about the JEE Advanced Mastery Program. What is the success rate and who are the faculty members? Also, do you provide scholarship?");
//         inq4.setSource(InquirySource.LISTING_PAGE);
//         inq4.setStatus(InquiryStatus.CONTACTED);
//         inq4.setAssignedTo(get("user-vinayak-admin"));
//         inq4.setInstituteNotes("Student is comparing with other institutes. Shared faculty profiles and success stories. Scholarship test scheduled for next week.");

//         Inquiry inq5 = new Inquiry();
//         inq5.setIdentifier(uuid());
//         inq5.setInstituteIdentifier(get("inst-vinayak"));
//         inq5.setBranchIdentifier(get("branch-vinayak-thane"));
//         inq5.setCourseIdentifier(get("ic-vin-mhtcet"));
//         inq5.setUserIdentifier(get("user-student-4"));
//         inq5.setName("Sneha Gupta");
//         inq5.setEmail("sneha.gupta@email.com");
//         inq5.setPhone("9876543213");
//         inq5.setStandard("12");
//         inq5.setTargetExam("MHT-CET");
//         inq5.setMessage("I am interested in the MHT-CET comprehensive course at Thane centre. What are the batch timings and when does the next batch start?");
//         inq5.setSource(InquirySource.COURSE_PAGE);
//         inq5.setStatus(InquiryStatus.ENROLLED);
//         inq5.setAssignedTo(get("user-vinayak-admin"));
//         inq5.setInstituteNotes("Student enrolled in afternoon batch. Fee paid in full. Admission confirmed.");

//         Inquiry inq6 = new Inquiry();
//         inq6.setIdentifier(uuid());
//         inq6.setInstituteIdentifier(get("inst-vinayak"));
//         inq6.setBranchIdentifier(get("branch-vinayak-main"));
//         inq6.setCourseIdentifier(get("ic-vin-neet"));
//         inq6.setUserIdentifier(get("user-student-2"));
//         inq6.setName("Priya Patel");
//         inq6.setEmail("priya.patel@email.com");
//         inq6.setPhone("9876543211");
//         inq6.setStandard("DROPPER");
//         inq6.setTargetExam("NEET");
//         inq6.setMessage("I am looking for NEET coaching in Mumbai. Can you tell me about the faculty, especially for Biology? Also, what is the fee and do you offer installment options?");
//         inq6.setSource(InquirySource.CHAT);
//         inq6.setStatus(InquiryStatus.FOLLOW_UP);
//         inq6.setAssignedTo(get("user-vinayak-admin"));
//         inq6.setInstituteNotes("Student is a dropper from Kota. Interested in Dr. Meera Iyer's Biology classes. Shared fee structure and EMI options.");

//         INQUIRY_SERVICE.saveAll(List.of(inq1, inq2, inq3, inq4, inq5, inq6));
//         log.info("Inquiries seeded.");
//     }

//     // ==================== LEADS ====================
//     private void seedLeads() {
//         log.info("Seeding leads...");

//         Lead lead1 = new Lead();
//         lead1.setIdentifier(uuid());
//         lead1.setUserIdentifier(get("user-student-1"));
//         lead1.setPhone("9876543210");
//         lead1.setFullName("Rahul Sharma");
//         lead1.setCityIdentifier(get("city-mumbai"));
//         lead1.setExamTypeIdentifier(get("exam-jee-main"));
//         lead1.setSearchedQuery("best JEE coaching in Mumbai");
//         lead1.setVisitedInstituteIdentifier(get("inst-vinayak"));
//         lead1.setVisitedInstituteName("Vinayak Classes");
//         lead1.setSource(LeadSource.SEARCH);
//         lead1.setStatus(LeadStatus.CONTACTED);
//         lead1.setIsActive(true);

//         Lead lead2 = new Lead();
//         lead2.setIdentifier(uuid());
//         lead2.setUserIdentifier(get("user-student-2"));
//         lead2.setPhone("9876543211");
//         lead2.setFullName("Priya Patel");
//         lead2.setCityIdentifier(get("city-kota"));
//         lead2.setExamTypeIdentifier(get("exam-neet"));
//         lead2.setSearchedQuery("NEET coaching with hostel Nagpur");
//         lead2.setVisitedInstituteIdentifier(get("inst-abhishek"));
//         lead2.setVisitedInstituteName("Abhishek Classes");
//         lead2.setSource(LeadSource.INSTITUTE_DETAIL);
//         lead2.setStatus(LeadStatus.QUALIFIED);
//         lead2.setIsActive(true);

//         Lead lead3 = new Lead();
//         lead3.setIdentifier(uuid());
//         lead3.setUserIdentifier(get("user-student-3"));
//         lead3.setPhone("9876543212");
//         lead3.setFullName("Ankit Mishra");
//         lead3.setCityIdentifier(get("city-nagpur"));
//         lead3.setExamTypeIdentifier(get("exam-jee-main"));
//         lead3.setSearchedQuery("foundation course class 10 Nagpur");
//         lead3.setVisitedInstituteIdentifier(get("inst-abhishek"));
//         lead3.setVisitedInstituteName("Abhishek Classes");
//         lead3.setSource(LeadSource.SEARCH);
//         lead3.setStatus(LeadStatus.SENT_TO_INSTITUTE);
//         lead3.setIsActive(true);

//         Lead lead4 = new Lead();
//         lead4.setIdentifier(uuid());
//         lead4.setUserIdentifier(get("user-student-4"));
//         lead4.setPhone("9876543213");
//         lead4.setFullName("Sneha Gupta");
//         lead4.setCityIdentifier(get("city-pune"));
//         lead4.setExamTypeIdentifier(get("exam-mht-cet"));
//         lead4.setSearchedQuery("MHT-CET coaching Thane");
//         lead4.setVisitedInstituteIdentifier(get("inst-vinayak"));
//         lead4.setVisitedInstituteName("Vinayak Classes");
//         lead4.setSource(LeadSource.COURSE_DETAIL);
//         lead4.setStatus(LeadStatus.ENROLLED);
//         lead4.setIsActive(true);

//         Lead lead5 = new Lead();
//         lead5.setIdentifier(uuid());
//         lead5.setPhone("9876543220");
//         lead5.setFullName("Mohit Kumar");
//         lead5.setCityIdentifier(get("city-delhi"));
//         lead5.setExamTypeIdentifier(get("exam-jee-main"));
//         lead5.setSearchedQuery("affordable JEE coaching Delhi");
//         lead5.setVisitedInstituteIdentifier(get("inst-bright"));
//         lead5.setVisitedInstituteName("Bright Career Institute");
//         lead5.setSource(LeadSource.SEARCH);
//         lead5.setStatus(LeadStatus.NEW);
//         lead5.setIsActive(true);

//         Lead lead6 = new Lead();
//         lead6.setIdentifier(uuid());
//         lead6.setPhone("9876543221");
//         lead6.setFullName("Aarti Singh");
//         lead6.setCityIdentifier(get("city-kota"));
//         lead6.setExamTypeIdentifier(get("exam-jee-adv"));
//         lead6.setSearchedQuery("small batch JEE coaching Kota");
//         lead6.setVisitedInstituteIdentifier(get("inst-prerna"));
//         lead6.setVisitedInstituteName("Prerna Academy");
//         lead6.setSource(LeadSource.COMPARE);
//         lead6.setStatus(LeadStatus.NEW);
//         lead6.setIsActive(true);

//         LEAD_SERVICE.saveAll(List.of(lead1, lead2, lead3, lead4, lead5, lead6));
//         log.info("Leads seeded.");
//     }

//     // ==================== LEAD DISTRIBUTIONS ====================
//     private void seedLeadDistributions() {
//         log.info("Seeding lead distributions...");

//         LeadDistribution ld1 = new LeadDistribution();
//         ld1.setIdentifier(uuid());
//         ld1.setUserIdentifier(get("user-student-1"));
//         ld1.setUserName("Rahul Sharma");
//         ld1.setUserPhone("9876543210");
//         ld1.setInstituteIdentifier(get("inst-vinayak"));
//         ld1.setInstituteName("Vinayak Classes");
//         ld1.setDistributedBy(get("user-super-admin"));
//         ld1.setStatus(LeadDistributionStatus.CONTACTED);
//         ld1.setNotes("High potential lead. Student from Mumbai, interested in JEE Advanced.");
//         ld1.setInstituteNotes("Student contacted. Scheduled counseling session for tomorrow.");

//         LeadDistribution ld2 = new LeadDistribution();
//         ld2.setIdentifier(uuid());
//         ld2.setUserIdentifier(get("user-student-2"));
//         ld2.setUserName("Priya Patel");
//         ld2.setUserPhone("9876543211");
//         ld2.setInstituteIdentifier(get("inst-abhishek"));
//         ld2.setInstituteName("Abhishek Classes");
//         ld2.setDistributedBy(get("user-super-admin"));
//         ld2.setStatus(LeadDistributionStatus.VIEWED);
//         ld2.setNotes("Dropper student from Kota. Looking for NEET coaching with hostel.");
//         ld2.setInstituteNotes("Student profile viewed. Preparing customized offer with hostel package.");

//         LeadDistribution ld3 = new LeadDistribution();
//         ld3.setIdentifier(uuid());
//         ld3.setUserIdentifier(get("user-student-4"));
//         ld3.setUserName("Sneha Gupta");
//         ld3.setUserPhone("9876543213");
//         ld3.setInstituteIdentifier(get("inst-vinayak"));
//         ld3.setInstituteName("Vinayak Classes");
//         ld3.setDistributedBy(get("user-super-admin"));
//         ld3.setStatus(LeadDistributionStatus.CONVERTED);
//         ld3.setNotes("Student from Pune, interested in MHT-CET. High conversion probability.");
//         ld3.setInstituteNotes("Lead converted! Student enrolled in MHT-CET Thane batch. Fee paid.");

//         LeadDistribution ld4 = new LeadDistribution();
//         ld4.setIdentifier(uuid());
//         ld4.setUserIdentifier(get("user-student-3"));
//         ld4.setUserName("Ankit Mishra");
//         ld4.setUserPhone("9876543212");
//         ld4.setInstituteIdentifier(get("inst-abhishek"));
//         ld4.setInstituteName("Abhishek Classes");
//         ld4.setDistributedBy(get("user-super-admin"));
//         ld4.setStatus(LeadDistributionStatus.PENDING);
//         ld4.setNotes("Foundation course enquiry from Nagpur. Parent contacted on behalf of student.");

//         LEAD_DISTRIBUTION_SERVICE.saveAll(List.of(ld1, ld2, ld3, ld4));
//         log.info("Lead distributions seeded.");
//     }

//     // ==================== NOTIFICATIONS ====================
//     private void seedNotifications() {
//         log.info("Seeding notifications...");

//         Notification notif1 = new Notification();
//         notif1.setIdentifier(uuid());
//         notif1.setUserIdentifier(get("user-abhishek-admin"));
//         notif1.setType(NotificationType.INQUIRY_RECEIVED);
//         notif1.setTitle("New Inquiry Received");
//         notif1.setBody("You have received a new inquiry from Rahul Sharma for JEE coaching.");
//         notif1.setEntityType("INQUIRY");
//         notif1.setEntityIdentifier("inquiry-001");
//         notif1.setIsRead(false);

//         Notification notif2 = new Notification();
//         notif2.setIdentifier(uuid());
//         notif2.setUserIdentifier(get("user-vinayak-admin"));
//         notif2.setType(NotificationType.INQUIRY_RECEIVED);
//         notif2.setTitle("New Inquiry Received");
//         notif2.setBody("You have received a new inquiry from Sneha Gupta for MHT-CET coaching.");
//         notif2.setEntityType("INQUIRY");
//         notif2.setEntityIdentifier("inquiry-002");
//         notif2.setIsRead(true);

//         Notification notif3 = new Notification();
//         notif3.setIdentifier(uuid());
//         notif3.setUserIdentifier(get("user-student-1"));
//         notif3.setType(NotificationType.REVIEW_APPROVED);
//         notif3.setTitle("Review Approved");
//         notif3.setBody("Your review for Abhishek Classes has been approved and is now live.");
//         notif3.setEntityType("REVIEW");
//         notif3.setEntityIdentifier(get("rev-abhi-1"));
//         notif3.setIsRead(false);

//         Notification notif4 = new Notification();
//         notif4.setIdentifier(uuid());
//         notif4.setUserIdentifier(get("user-student-2"));
//         notif4.setType(NotificationType.REVIEW_APPROVED);
//         notif4.setTitle("Review Approved");
//         notif4.setBody("Your review for Vinayak Classes has been approved and is now live.");
//         notif4.setEntityType("REVIEW");
//         notif4.setEntityIdentifier(get("rev-vin-2"));
//         notif4.setIsRead(true);

//         Notification notif5 = new Notification();
//         notif5.setIdentifier(uuid());
//         notif5.setUserIdentifier(get("user-abhishek-admin"));
//         notif5.setType(NotificationType.ADMISSION_REMINDER);
//         notif5.setTitle("Admission Deadline Approaching");
//         notif5.setBody("The admission deadline for JEE Droppers Batch is approaching. 5 seats remaining.");
//         notif5.setEntityType("COURSE");
//         notif5.setEntityIdentifier(get("ic-abhi-dropper"));
//         notif5.setIsRead(false);

//         Notification notif6 = new Notification();
//         notif6.setIdentifier(uuid());
//         notif6.setUserIdentifier(get("user-vinayak-admin"));
//         notif6.setType(NotificationType.SYSTEM);
//         notif6.setTitle("Subscription Renewal Reminder");
//         notif6.setBody("Your PREMIUM subscription will expire in 30 days. Renew now to continue enjoying all features.");
//         notif6.setEntityType("SUBSCRIPTION");
//         notif6.setEntityIdentifier("sub-vinayak-001");
//         notif6.setIsRead(false);

//         NOTIFICATION_SERVICE.saveAll(List.of(notif1, notif2, notif3, notif4, notif5, notif6));
//         log.info("Notifications seeded.");
//     }

//     // ==================== BOOKMARKS ====================
//     private void seedBookmarks() {
//         log.info("Seeding bookmarks...");

//         Bookmark bm1 = new Bookmark();
//         bm1.setIdentifier(uuid());
//         bm1.setUserIdentifier(get("user-student-1"));
//         bm1.setEntityType(BookmarkEntityType.INSTITUTE);
//         bm1.setEntityIdentifier(get("inst-abhishek"));

//         Bookmark bm2 = new Bookmark();
//         bm2.setIdentifier(uuid());
//         bm2.setUserIdentifier(get("user-student-1"));
//         bm2.setEntityType(BookmarkEntityType.INSTITUTE);
//         bm2.setEntityIdentifier(get("inst-vinayak"));

//         Bookmark bm3 = new Bookmark();
//         bm3.setIdentifier(uuid());
//         bm3.setUserIdentifier(get("user-student-2"));
//         bm3.setEntityType(BookmarkEntityType.INSTITUTE);
//         bm3.setEntityIdentifier(get("inst-abhishek"));

//         Bookmark bm4 = new Bookmark();
//         bm4.setIdentifier(uuid());
//         bm4.setUserIdentifier(get("user-student-3"));
//         bm4.setEntityType(BookmarkEntityType.INSTITUTE);
//         bm4.setEntityIdentifier(get("inst-vinayak"));

//         Bookmark bm5 = new Bookmark();
//         bm5.setIdentifier(uuid());
//         bm5.setUserIdentifier(get("user-student-4"));
//         bm5.setEntityType(BookmarkEntityType.INSTITUTE);
//         bm5.setEntityIdentifier(get("inst-vinayak"));

//         Bookmark bm6 = new Bookmark();
//         bm6.setIdentifier(uuid());
//         bm6.setUserIdentifier(get("user-student-5"));
//         bm6.setEntityType(BookmarkEntityType.INSTITUTE);
//         bm6.setEntityIdentifier(get("inst-bright"));

//         BOOKMARK_SERVICE.saveAll(List.of(bm1, bm2, bm3, bm4, bm5, bm6));
//         log.info("Bookmarks seeded.");
//     }
// }
