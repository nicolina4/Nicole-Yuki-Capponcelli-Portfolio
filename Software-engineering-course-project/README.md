# Poesiamo

Project for the Software Engineering course at the University of Naples Federico II (Prof. A. R. Fasolino, academic year 2024/25).

Team: Nicole Yuki Capponcelli, Daniele De Gregorio, Riccardo Cozzarelli.

## The idea

Poesiamo is a desktop platform for writing and sharing short poems. Registered authors can publish poems of up to 500 characters, give them a title and one or more thematic tags, and group them into collections. Poems can be public or private, and public ones appear in the feed of other authors, who can leave a heart or a comment. Each author has a statistics page, while administrators can generate reports on the activity of the platform.

## My contribution

This was a group project, but I took charge of most of the work, in particular:

- **Documentation and modelling**: requirements analysis, use case diagrams, class diagrams and sequence diagrams.
- **Graphical interface**: design and implementation of the GUI, including input validation and error messages.
- **Application logic**: the Java code of the core features and the integration with the database.

## Features

**Authors**
- Registration and login with email and password
- Editable profile with personal data and a short biography
- Publishing a poem with title, text, tags and visibility (public or private), choosing an existing collection or creating a new one on the spot
- Feed with the five most recent public poems by other authors
- Hearts and comments on other authors' poems, with the last three comments shown under each poem
- Pop-up notifications when someone leaves a heart or a comment on one of your poems
- Statistics: total hearts received, total comments received, most liked poem

**Administrators**
- Number of poems published in a chosen time interval
- Most active authors
- Most used tags
- Poems with the most interactions
- List and management of registered users

## Input validation

Every field is checked before reaching the database, with a clear error message for each case. Some examples:

- Only logged in authors can publish, like or comment.
- Poems and comments longer than 500 characters are rejected.
- Every poem needs at least one tag, and tags can contain only letters, numbers, hyphens and underscores, up to 30 characters.
- A new collection needs a description, and its title cannot match an existing collection of the same author.
- Names, emails and passwords are checked for format and length.

## Architecture

The code follows the BCED pattern (Boundary, Control, Entity, Database):

| Package | Content |
| --- | --- |
| `boundary` | Swing windows: `MainFrame` (entry point), `UtenteBoundary`, `AutoreBoundary`, `AmministratoreBoundary`, and `NotificationManager` for the pop-up notifications |
| `control` | `Controller`, a singleton that holds the session and the application logic, including all input validation |
| `entity` | `Utente`, `Autore`, `Amministratore`, `Poesia`, `Raccolta`, `Commento` |
| `database` | One DAO for each entity, plus `DBConnectionManager` for the JDBC connection |
| `DTO` | Objects used to move aggregated data to the interface: statistics, interval report, author activity |
| `test` | Test class that runs 16 error cases of the poem publishing feature |

## Documentation

The report (`Poesiamo(English Version) - Capponcelli, De Gregorio, Cozzarelli.pdf`, 52 pages) covers the whole development process:

1. **Requirements analysis**: noun and verb analysis of the specification, requirement validation, glossary, and classification into functional, data and other requirements.
2. **Use case modelling**: actors, use cases, scenarios and the mapping between use cases and requirements.
3. **Analysis models**: class diagram and sequence diagrams for the three main features (publishing a poem, viewing statistics, generating a report).
4. **Functional test plan** for the same three features.
5. **Design**: refined class diagram, translation of classes and associations, BCED pattern and design sequence diagrams.
6. **Implementation**: description of each package and deployment diagram.
7. **Testing**: structural testing with cyclomatic complexity, unit tests and functional tests.

The UML models are also available as a Visual Paradigm project in `Poesiamo.vpp`.

## Running it

Requirements: JDK 9 or later (the project uses `module-info.java`), MySQL Server and MySQL Connector/J.

1. Create a MySQL database named `DBSoftware` on `localhost:3306`.
2. Set the database user and password in `src/database/DBConnectionManager.java`.
3. Add MySQL Connector/J to the module path of the project.
4. Run `boundary.MainFrame`.

## Tools

Java, Swing, MySQL, JDBC, MySQL Workbench, UML, Visual Paradigm.
