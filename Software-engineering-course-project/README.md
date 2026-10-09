# Poesiamo

**Poesiamo** is a software application designed for publishing short poems. Developed as part of the **Software Engineering** course, this project covers the entire software development lifecycle, from initial architectural design and formal documentation to complete implementation and database integration.

## 👥 Team & My Contribution
Although this was a university group project, **I personally took ownership of and executed the vast majority of the workload**, specifically focusing on:
- **Software Documentation & Modeling:** Authored comprehensive engineering documentation, including Use Case diagrams, Class diagrams, and Sequence diagrams.
- **UI/UX & Frontend Development:** Designed and implemented the Graphical User Interface (GUI) from scratch, ensuring a seamless user experience and robust input handling.
- **Core Business Logic:** Programmed the core Java backend architecture and handled full integration with the relational database.

## 🛠️ Tech Stack
- **Language:** Java
- **Database:** MySQL (designed via MySQL Workbench)
- **GUI Framework:** Java Swing
- **Methodology:** Object-Oriented Analysis and Design (OOAD), UML Modeling

## 📌 Key Features & Software Constraints
The application enforces strict business logic and validation rules to ensure data integrity, providing informative error messages for the following scenarios:
- **Access Control:** Restricts unauthorized actions, prompting unregistered users to sign up or log in.
- **Authentication Security:** Validates credentials and handles incorrect password attempts securely.
- **Character Limit Enforcement:** Automatically rejects poems exceeding the **500-character limit** (a core software constraint).
- **Syntax & Tag Validation:** Parses inputs for special characters used to define tags and collection names, preventing formatting errors and indexing issues.

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- MySQL Server

### Installation & Run
1. Clone the repository:
   ```bash
   git clone https://github.com
   ```
2. Import the database schema provided in the `/database` directory into MySQL Workbench.
3. Open the project in your preferred IDE (e.g., IntelliJ, Eclipse) and run the main application file.
