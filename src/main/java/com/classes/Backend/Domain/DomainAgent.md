# Domain Layer Documentation

> **AI Agent Reference Guide** - This document serves as the source of truth for all Domain entities in the Coaching Class Discovery Platform.

## Overview

The Domain layer contains all JPA Entity classes that map to database tables. This is a coaching class discovery platform (like CollegeDunia) for Science/Commerce streams targeting 11th-12th std students (JEE, NEET, MHT-CET, etc.).

**Technology Stack:**
- Spring Boot 4.0.5
- Spring Data JPA
- PostgreSQL
- Lombok for boilerplate reduction
- UUID-based identifiers (field name: `identifier`, NOT `id`)

---

## 📋 Table of Contents

1. [Update Rules](#-update-rules-must-read)
2. [Package Structure](#-package-structure)
3. [ENUM Types](#-enum-types-reference)
4. [Entity Navigation Map](#-entity-navigation-map)
5. [Master/Reference Entities](#-masterreference-entities)
6. [Institute Entities](#-institute-entities)
7. [Course Entity](#-course-entity)
8. [Faculty Entity](#-faculty-entity)
9. [Results Entities](#-results-entities)
11. [Reviews Entities](#-reviews-entities)
12. [Users Entities](#-users-entities)
13. [Leads Entity](#-leads-entity)
14. [Media Entity](#-media-entity)
15. [Subscription Entities](#-subscription-entities)
16. [Notification & FAQ Entities](#-notification--faq-entities)

---

## 🔄 Update Rules (MUST READ)

When modifying any entity file, you MUST update this documentation:

### 1. File Modification Rules

| Change Type | Required Action |
|-------------|-----------------|
| **Add new field** | Update entity section with field name, type, and description |
| **Remove field** | Remove from entity section and check Navigation Map |
| **Modify field** | Update field type/description, mark as "Modified: YYYY-MM-DD" |
| **Add new entity** | Add complete section with Navigation Map relationships |
| **Delete entity** | Mark as DEPRECATED, move to Deprecated section |
| **Add/Modify ENUM** | Update ENUM Types Reference section |

### 2. Navigation Map Update Rules

- **Identifier fields**: When adding `*Identifier` field, add to Navigation Map
- **List<String> fields**: Mark as "Collection relationship" in Navigation Map
- **ENUM fields**: Mark as "Uses ENUM: [EnumName]" in Navigation Map

### 3. Formatting Rules

- Use `code` formatting for field names and class names
- Use **bold** for important notes
- Use > blockquotes for warnings
- Keep entities in alphabetical order within their package
- Always include the `@Entity` annotation in code examples

### 4. Cross-Reference Rules

When Entity A references Entity B (via identifier):
- In Entity A's section: "References: [Entity B](#link)"
- In Entity B's section: "Referenced by: [Entity A](#link)"

---

## 📦 Package Structure

```
com.classes.Backend.Domain/
├── enums/                          # All ENUM definitions
│   ├── BookmarkEntityType.java
│   ├── CourseType.java
│   ├── ExamLevel.java
│   ├── InquirySource.java
│   ├── InquiryStatus.java
│   ├── InstituteStaffRole.java
│   ├── InstituteType.java
│   ├── MediaEntityType.java
│   ├── MediaType.java
│   ├── NotificationType.java
│   ├── OwnershipType.java
│   ├── ProficiencyLevel.java
│   ├── RankOrScoreType.java
│   ├── ReviewStatus.java
│   ├── Standard.java
│   ├── SubscriptionTier.java
│   ├── UserRole.java
│   └── VoteType.java
├── master/                         # Reference/Master data
│   ├── City.java
│   ├── ExamType.java
│   ├── Stream.java
│   └── Subject.java
├── institute/                      # Institute core entities
│   ├── Branch.java
│   ├── Institute.java
│   └── InstituteFacility.java
├── course/                         # Course-related entities
│   └── InstituteCourse.java
├── faculty/                        # Faculty management
│   └── Faculty.java
├── results/                        # Results & achievements
│   ├── AwardAndRecognition.java
│   └── Result.java
├── reviews/                        # Reviews & ratings
│   ├── InstituteResponse.java
│   ├── Review.java
│   └── ReviewVote.java
├── users/                          # User management
│   ├── Bookmark.java
│   ├── User.java
│   └── UserInstituteAssociation.java
├── leads/                          # Leads & inquiries
│   └── Inquiry.java
├── media/                          # Media management
│   └── Media.java
├── subscription/                   # Subscriptions
│   ├── InstituteSubscription.java
│   └── SubscriptionPlan.java
└── notification/                   # Notifications & FAQs
    ├── Faq.java
    └── Notification.java
```

---

## 🔣 ENUM Types Reference

| ENUM Name | Values | Used By |
|-----------|--------|---------|
| `BookmarkEntityType` | INSTITUTE, COURSE | Bookmark |
| `CourseType` | REGULAR, CRASH, WEEKEND, ONLINE, DISTANCE, HYBRID | — |
| `ExamLevel` | STATE, NATIONAL, INTERNATIONAL | ExamType |
| `InquirySource` | LISTING_PAGE, COURSE_PAGE, CHAT, CALLBACK_REQUEST, DIRECT | Inquiry |
| `InquiryStatus` | NEW, CONTACTED, FOLLOW_UP, ENROLLED, NOT_INTERESTED, DROPPED | Inquiry |
| `InstituteStaffRole` | OWNER, ADMIN, STAFF | UserInstituteAssociation |
| `InstituteType` | OFFLINE, ONLINE, HYBRID | Institute |
| `MediaEntityType` | INSTITUTE, BRANCH, FACULTY, RESULT, FACILITY, EVENT | Media |
| `MediaType` | IMAGE, VIDEO, DOCUMENT, YOUTUBE_LINK | Media |
| `NotificationType` | INQUIRY_RECEIVED, REVIEW_APPROVED, ADMISSION_REMINDER, SYSTEM | Notification |
| `OwnershipType` | INDIVIDUAL, PARTNERSHIP, COMPANY, FRANCHISE | Institute |
| `ProficiencyLevel` | PRIMARY, SECONDARY | Faculty (for subject proficiency) |
| `RankOrScoreType` | AIR_RANK, STATE_RANK, PERCENTILE, MARKS, SELECTION | Result |
| `ReviewStatus` | PENDING, APPROVED, REJECTED, FLAGGED | Review |
| `Standard` | STANDARD_10, STANDARD_11, STANDARD_12, DROPPER, STANDARD_11_AND_12, GRADUATE, OTHER | User, Review |
| `SubscriptionTier` | FREE, BASIC, PREMIUM, FEATURED | Institute, SubscriptionPlan |
| `UserRole` | STUDENT, PARENT, INSTITUTE_ADMIN, INSTITUTE_STAFF, SUPER_ADMIN, CONTENT_MANAGER | User |
| `VoteType` | HELPFUL, NOT_HELPFUL | ReviewVote |

---

## 🗺️ Entity Navigation Map

### Master/Reference Entities

```
Stream (1) ───< (N) ExamType
    │
    └──< (N) Subject

City (1) ───< (N) Institute (via cityIdentifier)
         ───< (N) User (via cityIdentifier)
```

### Institute Ecosystem

```
Institute (1) ───< (N) Branch
            │
            ├── (1) InstituteFacility
            │
            ├──< (N) InstituteCourse
            │
            ├──< (N) Faculty
            │       │
            │       ├── Collection: subjectIdentifiers → Subject
            │       └── Collection: examTypeIdentifiers → ExamType
            │
            ├──< (N) Result
            │       │
            │       └── (1) ExamType (via examTypeIdentifier)
            │
            ├──< (N) AwardAndRecognition
            │
            │       │
            │       ├── (?) City (via cityIdentifier)
            │       └── (?) ExamType (via examTypeIdentifier)
            │
            ├──< (N) Review
            │       │
            │       ├── (1) User (via userIdentifier)
            │       ├── (1) InstituteResponse
            │       └──< (N) ReviewVote
            │
            ├──< (N) Inquiry
            │       │
            │       ├── (?) User (via userIdentifier)
            │       ├── (?) Branch (via branchIdentifier)
            │       └── (?) InstituteCourse (via courseIdentifier)
            │
            ├──< (N) Media
            │
            ├──< (N) Faq
            │
            ├── (1) InstituteSubscription
            │       │
            │       └── (1) SubscriptionPlan (via planIdentifier)
            │
            └──< (N) UserInstituteAssociation
                    │
                    └── (1) User (via userIdentifier)
```

### User Ecosystem

```
User (1) ───< (N) Review
         │
         ├──< (N) ReviewVote
         │
         ├──< (N) Bookmark
         │       │
         │       └── Points to: Institute / InstituteCourse
         │
         ├──< (N) Inquiry
         │
         ├──< (N) UserInstituteAssociation
         │       │
         │       └── (1) Institute (via instituteIdentifier)
         │
         ├──< (N) Notification
         │
         └── Collection: targetExamIdentifiers → ExamType
```

---

## 📊 Master/Reference Entities

### City
**Package:** `com.classes.Backend.Domain.master`

**Purpose:** Location master data for institutes and users.

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `name` | String (200) | City name |
| `state` | String (200) | State name |
| `stateCode` | String (10) | State code |
| `pincodePrefix` | String (10) | Pincode prefix |
| `isMetro` | Boolean | Is metro city |
| `latitude` | BigDecimal(10,7) | GPS latitude |
| `longitude` | BigDecimal(10,7) | GPS longitude |

**Navigation:**
- Referenced by: [Institute](#institute), [User](#user), [Branch](#branch) (via cityName denormalized)

---

### ExamType
**Package:** `com.classes.Backend.Domain.master`

**Purpose:** Target competitive exams (JEE, NEET, MHT-CET, etc.)

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `name` | String (100) | Exam name |
| `slug` | String (100) | URL-friendly identifier |
| `streamIdentifier` | String | → Stream.identifier |
| `standard` | String (10) | "11", "12", "Dropper", "Both" |
| `conductingBody` | String (200) | NTA, MHT-CET Cell, etc. |
| `examLevel` | ENUM | STATE, NATIONAL, INTERNATIONAL |
| `description` | TEXT | Full description |
| `isActive` | Boolean | Is active |
| `displayOrder` | Integer | Sort order |

**Navigation:**
- References: [Stream](#stream) (via streamIdentifier)
- Referenced by: [Faculty](#faculty), [Result](#result), [User](#user)

---

### Stream
**Package:** `com.classes.Backend.Domain.master`

**Purpose:** Core academic streams - category root (Science, Commerce, Arts)

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `name` | String (100) | Stream name |
| `slug` | String (100) | URL-friendly identifier |
| `description` | TEXT | Description |
| `iconUrl` | String (500) | Icon URL |
| `isActive` | Boolean | Is active |
| `displayOrder` | Integer | Sort order |

**Navigation:**
- Referenced by: [ExamType](#examtype), [Subject](#subject)

---

### Subject
**Package:** `com.classes.Backend.Domain.master`

**Purpose:** Individual subjects (Physics, Chemistry, Maths, Biology, etc.)

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `name` | String (100) | Subject name |
| `slug` | String (100) | URL-friendly identifier |
| `streamIdentifier` | String | → Stream.identifier |
| `isActive` | Boolean | Is active |

**Navigation:**
- References: [Stream](#stream) (via streamIdentifier)
- Referenced by: [Faculty](#faculty)

---

## 🏢 Institute Entities

### Institute
**Package:** `com.classes.Backend.Domain.institute`

**Purpose:** Primary entity - represents a coaching institute brand

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `name` | String (300) | Institute name |
| `slug` | String (300) | URL-friendly identifier |
| `tagline` | String (500) | Marketing tagline |
| `description` | TEXT | Full about section |
| `foundedYear` | Integer | Year founded |
| `logoUrl` | String (500) | Logo image URL |
| `bannerUrl` | String (500) | Banner image URL |
| `websiteUrl` | String (500) | Website URL |
| `email` | String (255) | Contact email |
| `phonePrimary` | String (20) | Primary phone |
| `whatsappNumber` | String (20) | WhatsApp number |
| `type` | ENUM | OFFLINE, ONLINE, HYBRID |
| `ownershipType` | ENUM | INDIVIDUAL, PARTNERSHIP, COMPANY, FRANCHISE |
| `isFranchise` | Boolean | Is franchise |
| `parentInstituteIdentifier` | String | → Institute.identifier (self-ref) |
| `averageRating` | BigDecimal(3,2) | Denormalized rating |
| `totalReviews` | Integer | Total review count |
| `totalStudentsEnrolled` | Integer | Self-reported enrollment |
| `yearsOfExperience` | Integer | Computed from foundedYear |
| `isVerified` | Boolean | Admin verified |
| `isFeatured` | Boolean | Paid feature flag |
| `isActive` | Boolean | Is active |
| `subscriptionTier` | ENUM | FREE, BASIC, PREMIUM, FEATURED |
| `metaTitle` | String (300) | SEO title |
| `metaDescription` | String (500) | SEO description |
| `createdAt` | LocalDateTime | Creation timestamp |
| `updatedAt` | LocalDateTime | Update timestamp |
| `createdBy` | String | → User.identifier |

**Navigation:**
- Self-reference: [Institute](#institute) (parent/child franchise)
- References: [User](#user) (createdBy)
- Referenced by: [Branch](#branch), [InstituteFacility](#institutefacility), [InstituteCourse](#institutecourse), [Faculty](#faculty), [Result](#result), [AwardAndRecognition](#awardandrecognition), [Review](#review), [Inquiry](#inquiry), [Media](#media), [Faq](#faq), [InstituteSubscription](#institutesubscription), [UserInstituteAssociation](#userinstituteassociation)

---

### Branch
**Package:** `com.classes.Backend.Domain.institute`

**Purpose:** Physical/online locations of an institute

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `instituteIdentifier` | String | → Institute.identifier |
| `name` | String (300) | Branch name |
| `isMainBranch` | Boolean | Is main branch |
| `isOnlineOnly` | Boolean | Is online only |
| `addressLine1` | String (500) | Address line 1 |
| `addressLine2` | String (500) | Address line 2 |
| `landmark` | String (300) | Landmark |
| `cityIdentifier` | String | → City.identifier |
| `cityName` | String (200) | Denormalized city name |
| `state` | String (200) | State |
| `pincode` | String (10) | Pincode |
| `latitude` | BigDecimal(10,7) | GPS latitude |
| `longitude` | BigDecimal(10,7) | GPS longitude |
| `googleMapsUrl` | String (1000) | Google Maps link |
| `phone` | String (20) | Branch phone |
| `email` | String (255) | Branch email |
| `totalAreaSqft` | Integer | Area in sqft |
| `totalClassrooms` | Integer | Number of classrooms |
| `seatingCapacity` | Integer | Seating capacity |
| `operatingHoursStart` | LocalTime | Opening time |
| `operatingHoursEnd` | LocalTime | Closing time |
| `operatingDays` | String (100) | "Mon-Sat", etc. |
| `isActive` | Boolean | Is active |
| `createdAt` | LocalDateTime | Creation timestamp |

**Navigation:**
- References: [Institute](#institute), [City](#city)
- Referenced by: [InstituteCourse](#institutecourse), [Media](#media), [Inquiry](#inquiry)

---

### InstituteFacility
**Package:** `com.classes.Backend.Domain.institute`

**Purpose:** Infrastructure/amenities offered by institute (single row per institute)

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `instituteIdentifier` | String | → Institute.identifier |
| `hasLibrary` | Boolean | Library available |
| `hasHostel` | Boolean | Hostel available |
| `hasCanteen` | Boolean | Canteen available |
| `hasTransport` | Boolean | Transport available |
| `hasAcClassrooms` | Boolean | AC classrooms |
| `hasDigitalBoards` | Boolean | Digital boards |
| `hasLaboratory` | Boolean | Laboratory |
| `hasStudyRoom` | Boolean | Study room |
| `hasWifi` | Boolean | WiFi |
| `hasCctv` | Boolean | CCTV |
| `hasOnlinePortal` | Boolean | Online portal |
| `hasDoubtSessions` | Boolean | Doubt sessions |
| `hasMockTestSeries` | Boolean | Mock tests |
| `hasStudyMaterial` | Boolean | Study material |
| `hasCrashCourses` | Boolean | Crash courses |
| `hasScholarshipProgram` | Boolean | Scholarship program |
| `hasFreeDemoClass` | Boolean | Free demo |
| `hasParentTeacherMeetings` | Boolean | PTM available |
| `hasPerformanceTracking` | Boolean | Performance tracking |
| `studentToTeacherRatio` | String (20) | e.g., "30:1" |
| `notes` | TEXT | Additional notes |
| `updatedAt` | LocalDateTime | Update timestamp |

**Navigation:**
- References: [Institute](#institute)

---

## 📚 Course Entity

### InstituteCourse
**Package:** `com.classes.Backend.Domain.course`

**Purpose:** Institute's specific offering of a course template

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `instituteIdentifier` | String | → Institute.identifier |
| `branchIdentifier` | String | → Branch.identifier (nullable) |
| `customName` | String (300) | Institute's custom name |
| `feeMin` | BigDecimal(12,2) | Minimum fee |
| `feeMax` | BigDecimal(12,2) | Maximum fee |
| `feeDescription` | TEXT | Installment info |
| `scholarshipAvailable` | Boolean | Scholarship available |
| `scholarshipDetails` | TEXT | Scholarship details |
| `durationMonths` | Integer | Institute's duration |
| `studyMaterialIncluded` | Boolean | Study material included |
| `testSeriesIncluded` | Boolean | Test series included |
| `onlineClassesAvailable` | Boolean | Online classes |
| `recordedLecturesAvailable` | Boolean | Recorded lectures |
| `isActive` | Boolean | Is active |
| `admissionOpen` | Boolean | Admission open |
| `createdAt` | LocalDateTime | Creation timestamp |
| `updatedAt` | LocalDateTime | Update timestamp |

**Navigation:**
- References: [Institute](#institute), [Branch](#branch)
- Referenced by: [Bookmark](#bookmark), [Inquiry](#inquiry)

---

## 👨‍🏫 Faculty Entity

### Faculty
**Package:** `com.classes.Backend.Domain.faculty`

**Purpose:** Teachers and educators at an institute

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `instituteIdentifier` | String | → Institute.identifier |
| `name` | String (200) | Faculty name |
| `photoUrl` | String (500) | Photo URL |
| `designation` | String (200) | e.g., "Senior Faculty", "HOD - Physics" |
| `qualification` | String (500) | e.g., "B.Tech IIT Bombay" |
| `experienceYears` | Integer | Years of experience |
| `bio` | TEXT | Biography |
| `specialization` | TEXT | Specialization notes |
| `iitIimBackground` | Boolean | IIT/IIM background flag |
| `nitBackground` | Boolean | NIT background flag |
| `achievements` | TEXT | Achievements |
| `formerInstitutes` | TEXT | Previous work |
| `studentRating` | BigDecimal(3,2) | Student rating |
| `isActive` | Boolean | Is active |
| `displayOrder` | Integer | Display order |
| `createdAt` | LocalDateTime | Creation timestamp |
| `subjectIdentifiers` | List<String> | → Subject.identifiers |
| `examTypeIdentifiers` | List<String> | → ExamType.identifiers |

**Navigation:**
- References: [Institute](#institute), [Subject](#subject) (collection), [ExamType](#examtype) (collection)
- Referenced by: [Media](#media)

---

## 🏆 Results Entities

### Result
**Package:** `com.classes.Backend.Domain.results`

**Purpose:** Documented exam results/selections by students

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `instituteIdentifier` | String | → Institute.identifier |
| `examTypeIdentifier` | String | → ExamType.identifier |
| `examYear` | Integer | Exam year (required) |
| `studentName` | String (200) | Student name (can be anonymized) |
| `studentPhotoUrl` | String (500) | Student photo |
| `rankOrScoreType` | ENUM | AIR_RANK, STATE_RANK, PERCENTILE, etc. |
| `value` | String (100) | "AIR 47", "99.8%ile", etc. |
| `collegeAdmitted` | String (300) | e.g., "IIT Bombay - CSE" |
| `testimonialQuote` | TEXT | Student testimonial |
| `isVerified` | Boolean | Is verified |
| `isFeatured` | Boolean | Is featured |
| `displayOrder` | Integer | Display order |
| `createdAt` | LocalDateTime | Creation timestamp |

**Navigation:**
- References: [Institute](#institute), [ExamType](#examtype)
- Referenced by: [Media](#media)

---

### AwardAndRecognition
**Package:** `com.classes.Backend.Domain.results`

**Purpose:** External awards and certifications

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `instituteIdentifier` | String | → Institute.identifier |
| `title` | String (500) | Award title |
| `issuingBody` | String (300) | Who gave the award |
| `year` | Integer | Year received |
| `description` | TEXT | Description |
| `certificateUrl` | String (500) | Certificate image URL |
| `isVerified` | Boolean | Is verified |
| `displayOrder` | Integer | Display order |
| `createdAt` | LocalDateTime | Creation timestamp |

**Navigation:**
- References: [Institute](#institute)

---

## ⭐ Reviews Entities

### Review
**Package:** `com.classes.Backend.Domain.reviews`

**Purpose:** Student/parent reviews of institutes

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `instituteIdentifier` | String | → Institute.identifier |
| `userIdentifier` | String | → User.identifier |
| `courseTaken` | String (200) | Course taken |
| `standardWhenEnrolled` | ENUM | STANDARD_11, STANDARD_12, DROPPER |
| `reviewTitle` | String (300) | Review title |
| `reviewText` | TEXT | Review content |
| `pros` | TEXT | Pros |
| `cons` | TEXT | Cons |
| `overallRating` | BigDecimal(3,2) | Overall rating (1.0-5.0) |
| `facultyRating` | BigDecimal(3,2) | Faculty rating |
| `studyMaterialRating` | BigDecimal(3,2) | Study material rating |
| `infrastructureRating` | BigDecimal(3,2) | Infrastructure rating |
| `feeValueRating` | BigDecimal(3,2) | Fee value rating |
| `onlineSupportRating` | BigDecimal(3,2) | Online support rating |
| `resultAchievementRating` | BigDecimal(3,2) | Results rating |
| `wouldRecommend` | Boolean | Would recommend |
| `status` | ENUM | PENDING, APPROVED, REJECTED, FLAGGED |
| `adminNotes` | TEXT | Admin notes |
| `helpfulCount` | Integer | Helpful votes |
| `reportedCount` | Integer | Report count |
| `isVerifiedStudent` | Boolean | Verified student |
| `createdAt` | LocalDateTime | Creation timestamp |
| `updatedAt` | LocalDateTime | Update timestamp |

**Navigation:**
- References: [Institute](#institute), [User](#user)
- Referenced by: [ReviewVote](#reviewvote), [InstituteResponse](#instituteresponse)

---

### ReviewVote
**Package:** `com.classes.Backend.Domain.reviews`

**Purpose:** Like/dislike votes on reviews

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `reviewIdentifier` | String | → Review.identifier |
| `userIdentifier` | String | → User.identifier |
| `vote` | ENUM | HELPFUL, NOT_HELPFUL |
| `createdAt` | LocalDateTime | Creation timestamp |

**Navigation:**
- References: [Review](#review), [User](#user)

---

### InstituteResponse
**Package:** `com.classes.Backend.Domain.reviews`

**Purpose:** Institute's official response to a review

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `reviewIdentifier` | String | → Review.identifier |
| `instituteIdentifier` | String | → Institute.identifier |
| `responseText` | TEXT | Response content |
| `respondedBy` | String | → User.identifier |
| `createdAt` | LocalDateTime | Creation timestamp |
| `updatedAt` | LocalDateTime | Update timestamp |

**Navigation:**
- References: [Review](#review), [Institute](#institute), [User](#user)

---

## 👤 Users Entities

### User
**Package:** `com.classes.Backend.Domain.users`

**Purpose:** All platform users - students, parents, institute staff, admins

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `fullName` | String (200) | Full name |
| `email` | String (255) | Email (unique) |
| `phone` | String (20) | Phone (unique) |
| `phoneVerified` | Boolean | Phone verified |
| `emailVerified` | Boolean | Email verified |
| `passwordHash` | String (500) | Password hash |
| `avatarUrl` | String (500) | Avatar URL |
| `role` | ENUM | STUDENT, PARENT, INSTITUTE_ADMIN, etc. |
| `currentStandard` | ENUM | STANDARD_10, STANDARD_11, etc. |
| `targetExamIdentifiers` | List<String> | → ExamType.identifiers |
| `cityIdentifier` | String | → City.identifier |
| `state` | String (200) | State |
| `pincode` | String (10) | Pincode |
| `schoolCollegeName` | String (300) | School/College name |
| `preferredLanguage` | String (50) | Preferred language |
| `isActive` | Boolean | Is active |
| `lastLoginAt` | LocalDateTime | Last login |
| `createdAt` | LocalDateTime | Creation timestamp |
| `updatedAt` | LocalDateTime | Update timestamp |

**Navigation:**
- References: [City](#city), [ExamType](#examtype) (collection)
- Referenced by: [Review](#review), [ReviewVote](#reviewvote), [InstituteResponse](#instituteresponse), [Inquiry](#inquiry), [Bookmark](#bookmark), [UserInstituteAssociation](#userinstituteassociation), [Notification](#notification), [Institute](#institute) (createdBy)

---

### UserInstituteAssociation
**Package:** `com.classes.Backend.Domain.users`

**Purpose:** Links institute admins/staff to their institutes

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `userIdentifier` | String | → User.identifier |
| `instituteIdentifier` | String | → Institute.identifier |
| `role` | ENUM | OWNER, ADMIN, STAFF |
| `isActive` | Boolean | Is active |
| `createdAt` | LocalDateTime | Creation timestamp |

**Navigation:**
- References: [User](#user), [Institute](#institute)

---

### Bookmark
**Package:** `com.classes.Backend.Domain.users`

**Purpose:** Users saving institutes or courses

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `userIdentifier` | String | → User.identifier |
| `entityType` | ENUM | INSTITUTE, COURSE |
| `entityIdentifier` | String | → Institute/InstituteCourse.identifier |
| `createdAt` | LocalDateTime | Creation timestamp |

**Navigation:**
- References: [User](#user)
- Polymorphic reference to: [Institute](#institute), [InstituteCourse](#institutecourse)

---

## 📞 Leads Entity

### Inquiry
**Package:** `com.classes.Backend.Domain.leads`

**Purpose:** Contact/admission interest captured from users

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `instituteIdentifier` | String | → Institute.identifier |
| `branchIdentifier` | String | → Branch.identifier (nullable) |
| `courseIdentifier` | String | → InstituteCourse.identifier (nullable) |
| `userIdentifier` | String | → User.identifier (nullable) |
| `name` | String (200) | Contact name |
| `email` | String (255) | Contact email |
| `phone` | String (20) | Contact phone |
| `standard` | String (10) | Standard |
| `targetExam` | String (200) | Target exam |
| `message` | TEXT | Inquiry message |
| `source` | ENUM | LISTING_PAGE, COURSE_PAGE, etc. |
| `status` | ENUM | NEW, CONTACTED, FOLLOW_UP, etc. |
| `assignedTo` | String | → User.identifier (staff) |
| `instituteNotes` | TEXT | Institute notes |
| `utmSource` | String (200) | UTM source |
| `utmMedium` | String (200) | UTM medium |
| `utmCampaign` | String (200) | UTM campaign |
| `createdAt` | LocalDateTime | Creation timestamp |
| `updatedAt` | LocalDateTime | Update timestamp |

**Navigation:**
- References: [Institute](#institute), [Branch](#branch), [InstituteCourse](#institutecourse), [User](#user) (user & assignedTo)

---

## 🖼️ Media Entity

### Media
**Package:** `com.classes.Backend.Domain.media`

**Purpose:** Photos, videos, documents uploaded by institutes

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `instituteIdentifier` | String | → Institute.identifier |
| `branchIdentifier` | String | → Branch.identifier (nullable) |
| `entityType` | ENUM | INSTITUTE, BRANCH, FACULTY, RESULT, etc. |
| `mediaType` | ENUM | IMAGE, VIDEO, DOCUMENT, YOUTUBE_LINK |
| `url` | String (1000) | Media URL |
| `thumbnailUrl` | String (1000) | Thumbnail URL |
| `caption` | String (500) | Caption |
| `altText` | String (300) | Alt text |
| `isFeatured` | Boolean | Is featured |
| `displayOrder` | Integer | Display order |
| `fileSizeKb` | Integer | File size in KB |
| `uploadedBy` | String | → User.identifier |
| `createdAt` | LocalDateTime | Creation timestamp |

**Navigation:**
- References: [Institute](#institute), [Branch](#branch), [User](#user)
- Polymorphic based on entityType: [Faculty](#faculty), [Result](#result), etc.

---

## 💳 Subscription Entities

### SubscriptionPlan
**Package:** `com.classes.Backend.Domain.subscription`

**Purpose:** Platform tier definitions

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `name` | ENUM | FREE, BASIC, PREMIUM, FEATURED |
| `priceMonthly` | BigDecimal(10,2) | Monthly price |
| `priceYearly` | BigDecimal(10,2) | Yearly price |
| `maxBranches` | Integer | Max branches allowed |
| `maxCourses` | Integer | Max courses allowed |
| `maxFaculty` | Integer | Max faculty allowed |
| `maxMediaUploads` | Integer | Max media uploads |
| `canRespondToReviews` | Boolean | Can respond to reviews |
| `canViewLeads` | Boolean | Can view leads |
| `canFeatureResults` | Boolean | Can feature results |
| `priorityInSearch` | Integer | Search priority level |
| `badgeShown` | String (100) | Badge text |
| `isActive` | Boolean | Is active |

**Navigation:**
- Referenced by: [InstituteSubscription](#institutesubscription), [Institute](#institute) (subscriptionTier field)

---

### InstituteSubscription
**Package:** `com.classes.Backend.Domain.subscription`

**Purpose:** Active subscription for each institute

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `instituteIdentifier` | String | → Institute.identifier |
| `planIdentifier` | String | → SubscriptionPlan.identifier |
| `startDate` | LocalDate | Start date |
| `endDate` | LocalDate | End date |
| `isActive` | Boolean | Is active |
| `paymentReference` | String (300) | Payment reference |
| `createdAt` | LocalDateTime | Creation timestamp |

**Navigation:**
- References: [Institute](#institute), [SubscriptionPlan](#subscriptionplan)

---

## 🔔 Notification & FAQ Entities

### Notification
**Package:** `com.classes.Backend.Domain.notification`

**Purpose:** Platform notifications for users

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `userIdentifier` | String | → User.identifier |
| `type` | ENUM | INQUIRY_RECEIVED, REVIEW_APPROVED, etc. |
| `title` | String (300) | Notification title |
| `body` | TEXT | Notification body |
| `entityType` | String (100) | Related entity type |
| `entityIdentifier` | String | Related entity identifier |
| `isRead` | Boolean | Is read |
| `createdAt` | LocalDateTime | Creation timestamp |

**Navigation:**
- References: [User](#user)

---

### Faq
**Package:** `com.classes.Backend.Domain.notification`

**Purpose:** Frequently asked questions for an institute

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `identifier` | String (UUID) | Primary key |
| `instituteIdentifier` | String | → Institute.identifier |
| `question` | TEXT | Question |
| `answer` | TEXT | Answer |
| `displayOrder` | Integer | Display order |
| `isActive` | Boolean | Is active |
| `createdAt` | LocalDateTime | Creation timestamp |

**Navigation:**
- References: [Institute](#institute)

---

## 📝 Changelog

| Date | Changes |
|------|---------|
| 2026-04-06 | Initial creation of all domain entities |

---

> **Note for AI Agents:** When reading this file, use it as the canonical reference for the domain model. If you find discrepancies between actual code and this documentation, update this file to match the code (unless the code is clearly buggy).
