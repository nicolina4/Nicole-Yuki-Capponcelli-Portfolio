# Pass-par-tout: Smart Mobility

Database project for the Databases course at the University of Naples Federico II (Prof. Vincenzo Moscato).

Team: Giorgio de Pertis, Marco de Pasquale, Nicole Yuki Capponcelli.

## The idea

Pass-par-tout is an electronic motorway toll system. A customer receives a device that can be linked to at most two of their cars. Every time the car enters and leaves the motorway, the system records the entry toll booth, the exit toll booth and the times of both passages, and the journey is billed to the customer's payment method (a credit card or a bank account).

## What is in this folder

| File | Content |
| --- | --- |
| `Pass-par-tout_Smart_Mobility_EN.pdf` | Full project report (23 pages) |
| `SQL-queries.txt` | SQL script: schema, sample data, triggers, views and stored procedures |

## Design process

The report follows the classic database design steps:

1. **Requirements**: data and operation specifications, then a restructuring of the requirements into a glossary of the information to be stored.
2. **Conceptual design**: ER schema built with a mixed strategy, starting top down from the core entities (device, car, journey) and refining bottom up with toll booths, customers and payment methods. Payment method is modelled as a total, disjoint generalisation of credit card and bank account.
3. **Logical design**: removal of composite attributes and of the generalisation, then translation into the relational model.
4. **Physical design**: `CREATE TABLE` statements with primary keys, foreign keys and referential actions (`ON DELETE CASCADE`, `ON DELETE SET NULL`).
5. **Application design**: three tier architecture (data, application, presentation).
6. **Optimisation**: strict two phase locking for concurrency control, and log based recovery with checkpoints and dumps for reliability.

## Schema

Seven tables: `CLIENTI`, `AUTOMOBILI`, `DISPOSITIVI`, `TRAGITTI`, `CASELLI`, `CARTE_DI_CREDITO`, `CONTI_CORRENTI`.

## Business rules enforced with triggers

| Trigger | Rule |
| --- | --- |
| `DATE_TRAGITTO_CONSISTENTI` | The exit time of a journey must be later than the entry time |
| `NUMERO_AUTO` | A device can be linked to at most two cars, all belonging to the same customer |
| `SCADENZA_CARTA` | An expired credit card cannot be registered |
| `CLIENTE_MAGGIORENNE` | Customers must be at least 18 years old |
| `METODO_PAGAMENTO_ESISTENTE` | A car can be registered only if its owner has a payment method |
| `LIMITE_CARTE` | Each customer can register only one credit card |
| `LIMITE_CONTI` | Each customer can register only one bank account |

## Views and stored procedures

Views:
- `TRAGITTI_PER_CLIENTE`: number of journeys for each customer
- `METODI_DI_PAGAMENTO`: payment methods registered by each customer
- `Tragitti_Proprietari_con_ogni_Automobile`: journeys made with each car, for owners only
- `TRAGITTI_NON_CONCLUSI`: journeys still in progress, grouped by entry toll booth

Stored procedures:
- `TRAGITTI_TOTALI`: returns the number of journeys made by a given customer
- `USCITA_PIU_TRAFFICATA`: finds the busiest exit, based on the journeys still in progress, so drivers can avoid it
- `Inserimento_Automobile`: inserts a new car inside a transaction, with rollback on error

Each trigger, view and procedure in the script is followed by a commented example that shows how to test it.

## Running the script

The script uses Oracle SQL and PL/SQL (`SYSDATE`, `TO_DATE`, `MONTHS_BETWEEN`, `DBMS_OUTPUT`), so it can be run on Oracle Database, for example a local Oracle XE instance or Oracle Live SQL. Some sample dates are written as `'18-Sep-2001'`, which relies on the default `DD-MON-YYYY` date format of the session.

## Tools

SQL, PL/SQL, Oracle Database, ER modelling.
