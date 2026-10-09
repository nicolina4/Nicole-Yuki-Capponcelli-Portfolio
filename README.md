# Nicole Yuki Capponcelli: Portfolio

Computer Engineering student at the University of Naples Federico II.

This repository collects a selection of projects I worked on during my degree. They cover three different areas: database design, software engineering and embedded programming on microcontrollers. Each folder has its own README with more details.

## Projects

| Project | Area | Main tools |
| --- | --- | --- |
| [Pass-par-tout: Smart Mobility](./Dabatase-course-project) | Database design | SQL, PL/SQL, ER modelling |
| [Poesiamo](./Software-engineering-course-project) | Software engineering | Java, Swing, MySQL, UML |
| [Rubik's Cube on STM32](./ST-Microelectronics-campus-project) | Embedded systems | C, ChibiOS, uGFX, STM32 Nucleo |

### Pass-par-tout: Smart Mobility
A relational database for an electronic motorway toll system. A single device can be linked to up to two cars of the same customer, and every journey is billed using the entry and exit toll booths. The project goes from requirements to conceptual, logical and physical design, and includes the full SQL script with triggers, views and stored procedures that enforce the business rules.

### Poesiamo
A Java desktop platform for publishing and sharing short poems, developed for the Software Engineering course. Authors publish poems with tags and collections, follow a feed, leave hearts and comments and see their own statistics, while administrators generate activity reports. The project includes the full documentation, from requirements analysis and UML modelling to testing, and the implementation built with the BCED pattern on a MySQL database.

### Rubik's Cube on STM32
Firmware for an STM32G474RE Nucleo board that draws the net of a Rubik's Cube on an ILI9341 display and animates it back to the solved state when the joystick is pressed, then plays a short melody through the on-chip DAC.
