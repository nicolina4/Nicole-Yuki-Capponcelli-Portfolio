# Poesiamo — UML Diagrams

Diagrams converted from the Visual Paradigm project `Poesiamo.vpp` to [Mermaid](https://mermaid.js.org/), rendered natively by GitHub.

## Contents

- **Use Case Diagram**
  - [UCDiagram](#ucdiagram)
- **Class Diagrams**
  - [CD](#cd)
  - [CD-Project](#cd-project)
  - [Boundary](#boundary)
  - [Controller](#controller)
  - [Entity](#entity)
  - [Database](#database)
  - [CDRefined](#cdrefined)
- **Sequence Diagrams**
  - [sdGenerateReport](#sdgeneratereport)
  - [sdGenerateReportProject](#sdgeneratereportproject)
  - [sdPublishPoemsProject](#sdpublishpoemsproject)
  - [sdViewStatisticsProject](#sdviewstatisticsproject)
  - [sdViewStatistics](#sdviewstatistics)
  - [sdPublishPoem](#sdpublishpoem)
- **Deployment Diagram**
  - [Deployment Diagram](#deployment-diagram)

## Use Case Diagram

### UCDiagram

```mermaid
flowchart LR
    A_Author["👤 Author"]:::actor
    A_User["👤 User"]:::actor
    A_Email_Service["👤 Email Service"]:::actor
    A_Administrator["👤 Administrator"]:::actor
    subgraph SYS["System PoemSharing"]
        UC_UploadProfilePicture(["UploadProfilePicture"])
        UC_EditProfile(["EditProfile"])
        UC_ViewFeed(["ViewFeed"])
        UC_SelectCollection(["SelectCollection"])
        UC_PublishPoem(["PublishPoem"])
        UC_AssignTags(["AssignTags"])
        UC_UpdateFeed(["UpdateFeed"])
        UC_AssignVisibility(["AssignVisibility"])
        UC_ViewPublishedPoems(["ViewPublishedPoems"])
        UC_EnterCollectionDescription(["EnterCollectionDescription"])
        UC_ViewStatistics(["ViewStatistics"])
        UC_CreateCollection(["CreateCollection"])
        UC_ViewCollection(["ViewCollection"])
        UC_LeaveHeart(["LeaveHeart"])
        UC_Login(["Login"])
        UC_SendConfirmation(["SendConfirmation"])
        UC_LeaveComment(["LeaveComment"])
        UC_Registration(["Registration"])
        UC_GenerateReport(["GenerateReport"])
        UC_EnterReportData(["EnterReportData"])
    end
    A_Author == "generalization" ==> A_User
    A_Administrator == "generalization" ==> A_User
    A_User --- UC_Registration
    UC_Registration -. «include» .-> UC_SendConfirmation
    A_Email_Service --- UC_Registration
    A_User --- UC_Login
    A_Author --- UC_EditProfile
    UC_UploadProfilePicture -. "«extend»<br/>EditProfile" .-> UC_EditProfile
    A_Author --- UC_PublishPoem
    UC_PublishPoem -. «include» .-> UC_AssignVisibility
    UC_PublishPoem -. «include» .-> UC_AssignTags
    UC_PublishPoem -. «include» .-> UC_SelectCollection
    A_Author --- UC_CreateCollection
    UC_CreateCollection -. «include» .-> UC_EnterCollectionDescription
    A_Author --- UC_ViewPublishedPoems
    A_Author --- UC_ViewFeed
    UC_UpdateFeed -. "«extend»" .-> UC_ViewFeed
    A_Author --- UC_LeaveHeart
    A_Author --- UC_LeaveComment
    A_Author --- UC_ViewStatistics
    A_Administrator --- UC_GenerateReport
    UC_GenerateReport -. «include» .-> UC_EnterReportData
    A_Author --- UC_ViewCollection
    classDef actor fill:#fff,stroke:#333,stroke-width:2px
```

## Class Diagrams

### CD

```mermaid
classDiagram
    direction TB
    class Comment {
        -text : string
        -date : LocalDateTime
        -id : long
        -commentAuthor : AuthorEntity
    }
    class User {
        -id : long
        -email : string
        -password : string
        -registrationDate : LocalDateTime
    }
    class Poem {
        -title : string
        -text : string
        -tag : ArrayList
        -visibility : boolean
        -publicationDate : LocalDateTime
        -poemId : long
        -author : AuthorEntity
        -collection : CollectionEntity
        -hearts : int
        -comments : ArrayList
    }
    class Administrator
    class Author {
        -firstName : string
        -lastName : string
        -bio : string
        -profileImage : string
        -publishedPoems : ArrayList
        -authorCollections : ArrayList
    }
    class Collection {
        -title : string
        -description : string
        -id : long
        -poemList : ArrayList
        -collectionAuthor : AuthorEntity
    }
    User <|-- Administrator
    User <|-- Author
    Poem "0..*" --* "1" Author : publish
    Comment "0..*" --* "1" Poem : receives
    Poem "0..*" --* "1" Collection : contains
    Author "1" *-- "0..*" Collection : has
    Comment "0..*" -- "1" Author : posts
```

### CD-Project

```mermaid
classDiagram
    direction TB
    namespace pkg_Controller {
        class Controller {
            <<Singleton>>
            +registerAuthor()
            +loginAuthor()
            +loginAdministrator()
            +logout()
            +publishPoem()
            +viewStatistics()
            +giveHeart()
            +commentPoem()
            +generatePoemsInRangeReport()
            +generateActiveAuthorsReport()
            +generateMostUsedTagsReport()
            +generateMostInteractedPoemsReport()
        }
    }
    namespace pkg_Boundary {
        class UserBoundary {
            +logout()
        }
        class AdministratorBoundary {
            +showPoemReportDialog()
            +generateMostActiveAuthorsReport()
            +generateMostUsedTagsReport()
            +generateMostInteractedPoemsReport()
        }
        class AuthorBoundary {
            +createStatisticsPanel()
            +createPublishPanel()
        }
    }
    namespace pkg_Entity {
        class User {
            -id : long
            -email : string
            -password : string
            -registrationDate : LocalDateTime
            +registerUser() boolean
            +login() boolean
            +updateProfile() boolean
            +deleteAccount() boolean
        }
        class Comment {
            -text : string
            -date : LocalDateTime
            -id : long
            -commentAuthor : AuthorEntity
            +createComment() boolean
            +editComment() boolean
            +deleteComment() boolean
            +viewComment() string
        }
        class Poem {
            -title : string
            -text : string
            -tag : ArrayList
            -visibility : boolean
            -publicationDate : LocalDateTime
            -poemId : long
            -author : AuthorEntity
            -collection : CollectionEntity
            -hearts : int
            -comments : ArrayList
            +addHeart() boolean
            +addComment() boolean
            +getLastThreeComments() ArrayList
            +editPoem() boolean
            +deletePoem() boolean
            +changeVisibility() boolean
            +hasTag() boolean
            +getCommentCount() int
            +getInteractionCount() int
        }
        class Author {
            -firstName : string
            -lastName : string
            -bio : string
            -profileImage : string
            -publishedPoems : ArrayList
            -authorCollections : ArrayList
            +publishPoem() long
            +viewFeed() ArrayList
            +giveHeart() boolean
            +commentPoem() boolean
            +viewStatistics() string
        }
        class Administrator {
            +viewAllUsers() ArrayList
            +viewAllAuthors() ArrayList
            +deleteUser() boolean
            +generatePoemsInRangeReport() void
            +generateActiveAuthorsReport() void
            +generateMostUsedTagsReport() void
            +generateMostInteractedPoemsReport() void
        }
        class Collection {
            -title : string
            -description : string
            -id : long
            -poemList : ArrayList
            -collectionAuthor : AuthorEntity
            +addPoem() boolean
            +removePoem() boolean
            +editCollection() boolean
            +deleteCollection() boolean
            +getPoemCount() int
            +getMostLikedPoem() PoemEntity
            +getSortedPublicPoems() ArrayList
            +searchPoemsByTitle() ArrayList
            +viewCollection() void
        }
    }
    namespace pkg_Database {
        class CommentDAO {
            +createComment() boolean
            +readComment() void
            +updateComment() void
            +deleteComment() void
        }
        class CollectionDAO {
            +createCollection() long
            +readCollection() void
            +updateCollection() void
            +deleteCollection() void
            +readPoemList() void
        }
        class DBConnectionManager {
            -conn : Connection
            +getConnection() Connection
            +closeConnection() void
            +selectQuery(query : string) ResultSet
            +updateQuery(query : string) int
            +updateQueryReturnGeneratedKey(query : string) Integer
            +selectQueryWithParams(query : string, params : Object) ResultSet
        }
        class UserDAO {
            +createUser() boolean
            +updateUser() boolean
            +deleteUser() boolean
        }
        class PoemDAO {
            +createPoem() boolean
            +readPoem() void
            +readAuthor() void
            +readCollection() void
            +readComments() void
            +updatePoem() boolean
            +deletePoem() boolean
            +addHeart() void
            +addComment() boolean
            +getTotalHeartsByAuthor() int
            +getTotalCommentsByAuthor() int
            +getTopPoemByAuthor() PoemDAO
        }
        class AdministratorDAO {
            -generatePoemsInRangeReport() void
            -generateMostActiveAuthorsReport() void
            -generateMostUsedTagsReport() void
            -generateMostInteractedPoemsReport() void
        }
        class AuthorDAO {
            +readAuthor() void
            +readPublishedPoemList() void
            +readAuthorCollectionList() void
        }
    }
    CommentDAO ..> DBConnectionManager : «use»
    CollectionDAO ..> DBConnectionManager : «use»
    PoemDAO ..> DBConnectionManager : «use»
    UserDAO <|-- AuthorDAO
    UserDAO <|-- AdministratorDAO
    UserDAO ..> DBConnectionManager : «use»
    User <|-- Administrator
    User <|-- Author
    Poem "0..*" --* "1" Author : publish
    Comment "0..*" --* "1" Poem : receives
    Poem "0..*" --* "1" Collection : contains
    Author "1" *-- "0..*" Collection : has
    Comment "0..*" -- "1" Author : posts
```

Package dependencies:

```mermaid
flowchart LR
    Boundary["📦 Boundary"] -. «use» .-> Controller["📦 Controller"]
    Controller["📦 Controller"] -. «use» .-> Entity["📦 Entity"]
    Entity["📦 Entity"] -. «use» .-> Database["📦 Database"]
```

### Boundary

```mermaid
classDiagram
    direction TB
    namespace pkg_Boundary {
        class UserBoundary {
            +logout()
        }
        class AdministratorBoundary {
            +showPoemReportDialog()
            +generateMostActiveAuthorsReport()
            +generateMostUsedTagsReport()
            +generateMostInteractedPoemsReport()
        }
        class AuthorBoundary {
            +createStatisticsPanel()
            +createPublishPanel()
        }
    }
```

### Controller

```mermaid
classDiagram
    direction TB
    namespace pkg_Controller {
        class Controller {
            <<Singleton>>
            +registerAuthor()
            +loginAuthor()
            +loginAdministrator()
            +logout()
            +publishPoem()
            +viewStatistics()
            +giveHeart()
            +commentPoem()
            +generatePoemsInRangeReport()
            +generateActiveAuthorsReport()
            +generateMostUsedTagsReport()
            +generateMostInteractedPoemsReport()
        }
    }
```

### Entity

```mermaid
classDiagram
    direction TB
    namespace pkg_Entity {
        class User {
            -id : long
            -email : string
            -password : string
            -registrationDate : LocalDateTime
            +registerUser() boolean
            +login() boolean
            +updateProfile() boolean
            +deleteAccount() boolean
        }
        class Comment {
            -text : string
            -date : LocalDateTime
            -id : long
            -commentAuthor : AuthorEntity
            +createComment() boolean
            +editComment() boolean
            +deleteComment() boolean
            +viewComment() string
        }
        class Poem {
            -title : string
            -text : string
            -tag : ArrayList
            -visibility : boolean
            -publicationDate : LocalDateTime
            -poemId : long
            -author : AuthorEntity
            -collection : CollectionEntity
            -hearts : int
            -comments : ArrayList
            +addHeart() boolean
            +addComment() boolean
            +getLastThreeComments() ArrayList
            +editPoem() boolean
            +deletePoem() boolean
            +changeVisibility() boolean
            +hasTag() boolean
            +getCommentCount() int
            +getInteractionCount() int
        }
        class Author {
            -firstName : string
            -lastName : string
            -bio : string
            -profileImage : string
            -publishedPoems : ArrayList
            -authorCollections : ArrayList
            +publishPoem() long
            +viewFeed() ArrayList
            +giveHeart() boolean
            +commentPoem() boolean
            +viewStatistics() string
        }
        class Administrator {
            +viewAllUsers() ArrayList
            +viewAllAuthors() ArrayList
            +deleteUser() boolean
            +generatePoemsInRangeReport() void
            +generateActiveAuthorsReport() void
            +generateMostUsedTagsReport() void
            +generateMostInteractedPoemsReport() void
        }
        class Collection {
            -title : string
            -description : string
            -id : long
            -poemList : ArrayList
            -collectionAuthor : AuthorEntity
            +addPoem() boolean
            +removePoem() boolean
            +editCollection() boolean
            +deleteCollection() boolean
            +getPoemCount() int
            +getMostLikedPoem() PoemEntity
            +getSortedPublicPoems() ArrayList
            +searchPoemsByTitle() ArrayList
            +viewCollection() void
        }
    }
    User <|-- Administrator
    User <|-- Author
    Poem "0..*" --* "1" Author : publish
    Poem "0..*" --* "1" Collection : contains
    Author "1" *-- "0..*" Collection : has
    Comment "0..*" -- "1" Author : posts
    Poem "1" *-- "0..*" Comment : receives
```

### Database

```mermaid
classDiagram
    direction TB
    namespace pkg_Database {
        class CommentDAO {
            +createComment() boolean
            +readComment() void
            +updateComment() void
            +deleteComment() void
        }
        class CollectionDAO {
            +createCollection() long
            +readCollection() void
            +updateCollection() void
            +deleteCollection() void
            +readPoemList() void
        }
        class DBConnectionManager {
            -conn : Connection
            +getConnection() Connection
            +closeConnection() void
            +selectQuery(query : string) ResultSet
            +updateQuery(query : string) int
            +updateQueryReturnGeneratedKey(query : string) Integer
            +selectQueryWithParams(query : string, params : Object) ResultSet
        }
        class UserDAO {
            +createUser() boolean
            +updateUser() boolean
            +deleteUser() boolean
        }
        class PoemDAO {
            +createPoem() boolean
            +readPoem() void
            +readAuthor() void
            +readCollection() void
            +readComments() void
            +updatePoem() boolean
            +deletePoem() boolean
            +addHeart() void
            +addComment() boolean
            +getTotalHeartsByAuthor() int
            +getTotalCommentsByAuthor() int
            +getTopPoemByAuthor() PoemDAO
        }
        class AdministratorDAO {
            -generatePoemsInRangeReport() void
            -generateMostActiveAuthorsReport() void
            -generateMostUsedTagsReport() void
            -generateMostInteractedPoemsReport() void
        }
        class AuthorDAO {
            +readAuthor() void
            +readPublishedPoemList() void
            +readAuthorCollectionList() void
        }
    }
    CommentDAO ..> DBConnectionManager : «use»
    CollectionDAO ..> DBConnectionManager : «use»
    PoemDAO ..> DBConnectionManager : «use»
    UserDAO <|-- AuthorDAO
    UserDAO <|-- AdministratorDAO
    UserDAO ..> DBConnectionManager : «use»
```

### CDRefined

```mermaid
classDiagram
    direction TB
    class User {
        -id : long
        -email : string
        -password : string
        -registrationDate : LocalDateTime
        +registerUser() boolean
        +login() boolean
        +updateProfile() boolean
        +deleteAccount() boolean
    }
    class Comment {
        -text : string
        -date : LocalDateTime
        -id : long
        -commentAuthor : AuthorEntity
        +createComment() boolean
        +editComment() boolean
        +deleteComment() boolean
        +viewComment() string
    }
    class Poem {
        -title : string
        -text : string
        -tag : ArrayList
        -visibility : boolean
        -publicationDate : LocalDateTime
        -poemId : long
        -author : AuthorEntity
        -collection : CollectionEntity
        -hearts : int
        -comments : ArrayList
        +addHeart() boolean
        +addComment() boolean
        +getLastThreeComments() ArrayList
        +editPoem() boolean
        +deletePoem() boolean
        +changeVisibility() boolean
        +hasTag() boolean
        +getCommentCount() int
        +getInteractionCount() int
    }
    class Author {
        -firstName : string
        -lastName : string
        -bio : string
        -profileImage : string
        -publishedPoems : ArrayList
        -authorCollections : ArrayList
        +publishPoem() long
        +viewFeed() ArrayList
        +giveHeart() boolean
        +commentPoem() boolean
        +viewStatistics() string
    }
    class Administrator {
        +viewAllUsers() ArrayList
        +viewAllAuthors() ArrayList
        +deleteUser() boolean
        +generatePoemsInRangeReport() void
        +generateActiveAuthorsReport() void
        +generateMostUsedTagsReport() void
        +generateMostInteractedPoemsReport() void
    }
    class Collection {
        -title : string
        -description : string
        -id : long
        -poemList : ArrayList
        -collectionAuthor : AuthorEntity
        +addPoem() boolean
        +removePoem() boolean
        +editCollection() boolean
        +deleteCollection() boolean
        +getPoemCount() int
        +getMostLikedPoem() PoemEntity
        +getSortedPublicPoems() ArrayList
        +searchPoemsByTitle() ArrayList
        +viewCollection() void
    }
    User <|-- Administrator
    User <|-- Author
    Poem "0..*" --* "1" Author : publish
    Poem "0..*" --* "1" Collection : contains
    Author "1" *-- "0..*" Collection : has
    Comment "0..*" -- "1" Author : posts
    Poem "1" *-- "0..*" Comment : receives
```

## Sequence Diagrams

### sdGenerateReport

```mermaid
sequenceDiagram
    actor P1 as Administrator
    participant P2 as AdministratorBoundary
    participant P3 as Controller
    participant P4 as AdministratorEntity
    P1->>P2: 1: enterReportSection()
    alt PoemsInRangeReport
        P2-->>P1: 1.1: requestDates
        P1->>P2: 2: enterDates()
        alt validDates
            P1->>P2: 3: generateReport()
            P2->>P3: 3.1: generateReport()
            P3->>P4: 3.1.1: generateReport()
            P4->>P4: 3.1.1.1: retrieveData()
            P4-->>P3: 3.1.1.2: report
            P3-->>P2: 3.2: report
            P2-->>P1: 3.3: reportPresentation
        else invalidDates
            P2-->>P1: 3.4: ERROR: Invalid dates
        end
    else MostActiveAuthorsReport
        P1->>P2: 4: generateReport()
        P2->>P3: 4.1: generateReport()
        P3->>P4: 4.1.1: generateReport()
        P4->>P4: 4.1.1.1: retrieveData()
        P4-->>P3: 4.1.1.2: report
        P3-->>P2: 4.2: report
        P2-->>P1: 4.3: reportPresentation
    else MostUsedTagsReport
        P1->>P2: 5: generateReport()
        P2->>P3: 5.1: generateReport()
        P3->>P4: 5.1.1: generateReport()
        P4->>P4: 5.1.1.1: retrieveData()
        P4-->>P3: 5.1.1.2: report
        P3-->>P2: 5.2: report
        P2-->>P1: 5.3: reportPresentation
    else MostInteractedPoemsReport
        P1->>P2: 6: generateReport()
        P2->>P3: 6.1: generateReport()
        P3->>P4: 6.1.1: generateReport()
        P4->>P4: 6.1.1.1: retrieveData()
        P4-->>P3: 6.1.1.2: report
        P3-->>P2: 6.2: report
        P2-->>P1: 6.3: reportPresentation
    end
```

### sdGenerateReportProject

```mermaid
sequenceDiagram
    actor P1 as Administrator
    participant P2 as AdministratorBoundary
    participant P3 as Controller
    participant P4 as AdministratorEntity
    participant P5 as ADAO : AdministratorDAO
    participant P6 as ADAO : AdministratorDAO
    participant P7 as ADAO : AdministratorDAO
    participant P8 as ADAO : AdministratorDAO
    participant P9 as DBConnectionManager
    P1->>P2: 1: createReportPanel()
    alt poemsReportBtn
        P1->>P2: 2: showPoemReportDialog()
        P2-->>P1: 2.1: requestDates
        alt generateBtn&&(endDate>startDate)
            P1->>P2: 3: generateReport()
            P2->>P3: 3.1: controller.generatePoemsInRangeReport(startDate, endDate)
            alt currentAdministrator!=null
                P5->>P9: 3.1.1.2.1: selectQueryWithParams(query, start, end)
                P5-->>P4: 3.1.1.3: reportDTO
                P3->>P4: 3.1.1: currentAdministrator.generatePoemsInRangeReport(startDate, endDate)
                P4->>P5: 3.1.1.1: «create»
                P4->>P5: 3.1.1.2: ADAO.generatePoemsInRangeReport(startDate, endDate)#59;
                P9-->>P5: 3.1.1.2.2: reportDTO
                P4-xP5: 3.1.1.4: destroy()
                P4-->>P3: 3.1.1.5: reportDTO
                P3-->>P2: 3.1.1.5.1: reportDTO
                P2-->>P1: 3.1.1.5.1.1: reportPresentation
            else
                P3-->>P2: 4: ERROR: Administrator not logged in
                P2-->>P1: 4.1: ERROR: Administrator not logged in
            end
        else generateBtn&&(endDate<startDate)
            P1->>P2: 5: generateBtn.addActionListener()
            P2-->>P1: 5.1: ERROR: The end date cannot be earlier than the start date.
        else cancelBtn
            P1->>P2: 6: cancelBtn.addActionListener()
            P2-->>P1: 6.1: reportPanel
        end
    else authorsReportBtn
        P1->>P2: 7: generateMostActiveAuthorsReport()
        P2->>P3: 7.1: controller.generateActiveAuthorsReport()
        alt currentAdministrator==null
            P3-->>P2: 7.2: ERROR: Administrator not logged in
            P2-->>P1: 7.3: ERROR: Administrator not logged in
        else
            P6->>P9: 7.3.2.1: selectQuery( "SELECT a.FirstName, a.LastName, COUNT(p.ID) AS NumPoems FROM Authors a " +"JOIN Poems p ON a.UserID = p.UserID GROUP BY a.UserID " +"ORDER BY NumPoems DESC LIMIT 5")
            P6-->>P4: 7.3.3: AuthorActivityDTO
            P3->>P4: 7.3: currentAdministrator.generateActiveAuthorsReport()
            P4->>P6: 7.3.1: «create»
            P4->>P6: 7.3.2: ADAO.generateMostActiveAuthorsReport()
            P9-->>P6: 7.3.2.2: AuthorActivityDTO
            P4-xP6: 7.3.4: destroy()
            P4-->>P3: 7.3.5: report
            P3-->>P2: 7.4: report
            alt report.isEmpty()
                P2-->>P1: 7.4.1: ERROR: No authors found.
            else
                P2-->>P1: 7.4.2: reportPresentation
            end
        end
    else tagsReportBtn
        P1->>P2: 8: generateMostUsedTagsReport()
        P2->>P3: 8.1: controller.generateMostUsedTagsReport()
        alt currentAdministrator== null
            P3-->>P2: 8.2: ERROR: Administrator not logged in
            P2-->>P1: 8.3: ERROR: Administrator not logged in
        else
            P7->>P9: 8.3.2.1: selectQuery("SELECT t.TagName, COUNT(pt.PoemID) AS NumUses FROM Tag t " + "JOIN PoemTag pt ON t.IDTag = pt.IDTag GROUP BY t.TagName " + "ORDER BY NumUses DESC LIMIT 10")
            P7-->>P4: 8.3.3: report
            P3->>P4: 8.3: currentAdministrator.generateMostUsedTagsReport()
            P4->>P7: 8.3.1: «create»
            P4->>P7: 8.3.2: ADAO.generateMostUsedTagsReport()
            P9-->>P7: 8.3.2.2: report
            P4-xP7: 8.3.4: destroy()
            P4-->>P3: 8.3.5: report
            P3-->>P2: 8.4: report
            alt report.isEmpty()
                P2-->>P1: 8.4.1: ERROR: No tags found
            else
                P2-->>P1: 8.4.2: reportPresentation
            end
        end
    else interactionsReportBtn
        P1->>P2: 9: generateMostInteractedPoemsReport()
        P2->>P3: 9.1: controller.generateMostInteractedPoemsReport()
        alt currentAdministrator==null
            P3-->>P2: 9.2: ERROR: Administrator not logged in
            P2-->>P1: 9.3: ERROR: Administrator not logged in
        else
            P8->>P9: 9.3.2.1: selectQuery("SELECT p.Title, p.NumHearts, " + " COUNT(DISTINCT c.ID) AS NumComments, " + " (p.NumHearts + COUNT(DISTINCT c.ID)) AS TotInteractions " + " FROM Poems p " + " LEFT JOIN Comments c ON p.ID = c.PoemID " + " GROUP BY p.ID, p.Title, p.NumHearts " + " ORDER BY TotInteractions DESC LIMIT 5"#59;)
            P8-->>P4: 9.3.3: report
            P3->>P4: 9.3: currentAdministrator.generateMostInteractedPoemsReport()
            P4->>P8: 9.3.1: «create»
            P4->>P8: 9.3.2: ADAO.generateMostInteractedPoemsReport()
            P9-->>P8: 9.3.2.2: report
            P4-xP8: 9.3.4: destroy()
            P4-->>P3: 9.3.5: report
            P3-->>P2: 9.4: report
            alt report.isEmpty()
                P2-->>P1: 9.4.1: ERROR: No poems found with significant interactions.
            else
                P2-->>P1: 9.4.2: reportPresentation
            end
        end
    end
```

### sdPublishPoemsProject

```mermaid
sequenceDiagram
    actor P1 as Author
    participant P2 as AuthorBoundary
    participant P3 as Controller
    participant P4 as AuthorEntity
    participant P5 as PENT : PoemEntity
    participant P6 as PDAO : PoemDAO
    participant P7 as RDAO : CollectionDAO
    participant P8 as cm : DBConnectionManager
    P1->>P2: 1: createPublishPanel()
    P2->>P3: 1.1: controller.publishPoem(title,text,tags,publish,collectionTitle,collectionDescription,createNewCollection)
    P3->>P3: 1.1.1: validateFields
    alt baseFieldsValid
        P5-->>P4: 2: PENT
        P6-->>P4: 5: PDAO
        P5-->>P4: 2.2: data
        P6-->>P4: 5.2: data
        P6->>P8: 5.3.1: getConnection()
        P3->>P4: 1.1.2: currentAuthor.getID()
        P4-->>P3: 1.1.3: ID
        P6->>P8: 5.3.3: conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        P3->>P4: 1.1.4: currentAuthor.publishPoem(title, text, normalizedTags, publish,collectionTitle, createNewCollection ? collectionDescription : null)
        P4->>P5: 1.1.4.1: «create»
        P6->>P8: 5.3.5: close()
        P6-->>P4: 5.3.6: success
        P4->>P5: 2.1: setData()
        opt collectionTitleDoesNotExist
            P7-->>P4: 4: RDAO
            P7->>P8: 4.1.1: getConnection()
            P7->>P8: 4.1.3: conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            P7->>P8: 4.1.5: close()
            P7-->>P4: 4.1.6: collectionId
            P4->>P7: 3: «create»
            P4->>P7: 4.1: RDAO.createCollection(collectionTitle, collectionDescription, this.UserIdE)
            P8-->>P7: 4.1.2: conn
            P8-->>P7: 4.1.4: collectionId
            P4-xP7: 4.2: destroy()
        end
        P5-->>P4: 4.4: collectionId
        P4->>P5: 4.3: setCollection()
        P4-xP5: 4.5: destroy()
        P4->>P6: 4.6: «create»
        P4->>P6: 5.1: configurePoemData()
        P4->>P6: 5.3: PDAO.createPoem(title, text, tag, publish,publicationDate, this.UserIdE, collectionId)#59;
        P8-->>P6: 5.3.2: conn
        P8-->>P6: 5.3.4: success
        P4-xP6: 5.4: destroy()
        P4->>P4: 5.5: this.publishedPoems.add(PENT)
        P4->>P4: 5.6: updateCollections()
        P4-->>P3: 5.7: poemId
        P3->>P3: 5.7.1: reloadCurrentAuthor()
        P3-->>P2: 5.7.2: poemPublished
        P2-->>P1: 5.7.2.1: poemPublished
    else baseFieldsInvalid
        P3-->>P2: 5.7.3: ERROR: At least one of the fields is not valid
        P2-->>P1: 5.7.3.1: ERROR: At least one of the fields is not valid
    end
```

### sdViewStatisticsProject

```mermaid
sequenceDiagram
    actor P1 as Author
    participant P2 as AuthorBoundary
    participant P3 as Controller
    participant P4 as AuthorEntity
    participant P5 as mostLikedPoem : PoemEntity
    participant P6 as stats : StatisticsDTO
    participant P7 as PoemDAO
    participant P8 as topPDAO : PoemDAO
    participant P9 as DBConnectionManager
    P1->>P2: 1: createStatisticsPanel()
    P2->>P3: 1.1: viewStatistics()
    alt currentAuthor==null
        P3-->>P2: 1.2: ERROR: you must be logged in as an author
        P2-->>P1: 1.3: ERROR: you must be logged in as an author
    else
        P6-->>P4: 7: stats
        P8-->>P7: 1.3.5.6: topPDAO
        P3->>P4: 1.3: getStatistics()
        P6-->>P2: 7.1.1.6: totalHearts
        P4->>P7: 1.3.1: getTotalHeartsByAuthor(authorId)
        P7->>P9: 1.3.1.1: selectQuery("SELECT SUM(P.NumHearts) AS TotHearts FROM Poems P WHERE P.UserID = " + userId)
        P6-->>P2: 7.1.1.8: totalComments
        P9-->>P7: 1.3.1.2: rs
        P7-->>P4: 1.3.2: totalHearts
        P6-->>P2: 7.1.1.10: topPoem
        P4->>P7: 1.3.3: getTotalCommentsByAuthor(authorId)
        P7->>P9: 1.3.3.1: selectQuery("SELECT COUNT(Co.ID) AS TotComments FROM Comments Co " +"JOIN Poems P ON Co.PoemID = P.ID WHERE P.UserID = " + userId)
        P9-->>P7: 1.3.3.2: rs
        P7-->>P4: 1.3.4: totalComments
        P4->>P7: 1.3.5: getTopPoemByAuthor(authorId)
        P7->>P9: 1.3.5.1: selectQuery("SELECT ID, Title, NumHearts FROM Poems " +"WHERE UserID = " + userId +" ORDER BY NumHearts DESC, PublicationDate DESC LIMIT 1")
        P9-->>P7: 1.3.5.2: rs
        P7->>P8: 1.3.5.3: «create»
        P7->>P8: 1.3.5.4: setTitle(title)
        P7->>P8: 1.3.5.5: setHearts(hearts)
        P7-->>P4: 1.3.6: topPDAO
        opt topPDAO!=null
            P5-->>P4: 5.1: mostLikedPoem
            P4->>P5: 2: «create»
            P4->>P5: 3: setPoemId(topPoemDAO.getPoemIdD())
            P4->>P5: 4: setTitle(topPoemDAO.getTitle())
            P4->>P5: 5: setHearts(topPoemDAO.getHearts())
        end
        P4->>P6: 6: «create»
        P4-->>P3: 7.1: stats
        P3-->>P2: 7.1.1: stats
        P2->>P4: 7.1.1.1: getPublishedPoems()
        P4-->>P2: 7.1.1.2: ArrayList<PoemEntity>
        P2->>P4: 7.1.1.3: getAuthorCollections()
        P4-->>P2: 7.1.1.4: ArrayList<CollectionEntity>
        P2->>P6: 7.1.1.5: getTotalHearts()
        P2->>P6: 7.1.1.7: getTotalComments()
        P2->>P6: 7.1.1.9: getMostLikedPoem()
        opt topPoem!=null
            P5-->>P2: 7.1.1.12: numHearts
            P5-->>P2: 7.1.1.14: title
            P2->>P5: 7.1.1.11: getHearts()
            P2->>P5: 7.1.1.13: getTitle()
        end
        P2-->>P1: 7.1.1.15: statisticsView
    end
```

### sdViewStatistics

```mermaid
sequenceDiagram
    actor P1 as Author
    participant P2 as AuthorBoundary
    participant P3 as Controller
    participant P4 as AuthorEntity
    P1->>P2: 1: createStatisticsPanel()
    P2->>P3: 1.1: viewStatistics()
    alt currentAuthor==null
        P3-->>P2: 1.2: ERROR: you must be logged in as an author
        P2-->>P1: 1.3: ERROR: you must be logged in as an author
    else
        P3->>P4: 1.3: getStatistics()
        P4->>P4: 1.3.1: findStatistics()
        P4-->>P3: 1.3.2: statistics
        P3-->>P2: 1.4: statistics
        P2-->>P1: 1.4.1: statisticsView
    end
```

### sdPublishPoem

```mermaid
sequenceDiagram
    actor P1 as Author
    participant P2 as AuthorBoundary
    participant P3 as Controller
    participant P4 as PoemEntity
    participant P5 as CollectionEntity
    P1->>P2: 1: enterPoemData
    P2->>P3: 1.1: publishPoem(poemData)
    P3->>P3: 1.1.1: validateData()
    alt validData
        opt newCollection
            P3->>P5: 1.1.2: createCollection()
            P5->>P3: 1.1.2.1: collection
        end
        P3->>P4: 1.1.2.1.1: createPoem(poemData)
        P4->>P4: 1.1.2.1.1.1: publishPoem()
        P4-->>P3: 1.1.2.1.1.2: poemPublished
        P3-->>P2: 1.2: poemPublished
        P2-->>P1: 1.3: poemPublished
    else invalidData
        P3-->>P2: 1.3: Error: Invalid data
        P2-->>P1: 1.3.1: Error: Invalid data
    end
```

## Deployment Diagram

### Deployment Diagram

```mermaid
flowchart LR
    N_ClientMobile["«device»<br/><b>ClientMobile</b><br/><small>IOS/ Android</small>"]
    N_ClientDesktop["«device»<br/><b>ClientDesktop</b><br/><small>Web Browser</small>"]
    N_NotificationServer["«executionEnvironment»<br/><b>NotificationServer</b><br/><small>- push API<br/>- Web Socket Manager</small>"]
    N_ApplicationServer["«executionEnvironment»<br/><b>ApplicationServer</b><br/><small>- Authentication<br/>- User Management<br/>- Poem Management<br/>- Interactions<br/>- Statistics/Reports</small>"]
    N_DBMS[("<b>DBMS</b>")]
    N_ApplicationServer -- "JDBC" --- N_DBMS
    N_ClientMobile -- "WEB SOCKET/PUSH" --- N_NotificationServer
    N_ClientDesktop -- "WEB SOCKET" --- N_NotificationServer
    N_NotificationServer --- N_ApplicationServer
```
