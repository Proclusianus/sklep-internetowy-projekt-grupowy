# Setup Guide - Wielkie Akcje i Transakcje

This guide provides step-by-step instructions to set up and run the application locally.

## 📋 Prerequisites

Ensure you have the following installed on your machine:

1.  **Java JDK 21** (Verify with `java -version`)
2.  **Maven 3.6+** (Verify with `mvn -version`)
3.  **MariaDB** or **MySQL Server**
4.  A Git client

---

## 🗄️ Database Setup

This application requires a specific database schema and **TERYT dictionary data** (Polish administrative addresses) to function correctly.

1.  **Create the Database:**
    Open your database client (e.g., HeidiSQL, DBeaver, or terminal) and run Bulding Script
    

2.  **Import TERYT Data (Crucial):**
    Insert Data into Datbase by Insert Script

---

## ⚙️ Application Configuration

1.  Navigate to `src/main/resources/`.
2.  Open or create the `application.properties` file.
3.  Update the configuration to match your local environment:

```properties
# --- Database Configuration ---
spring.datasource.url=jdbc:mariadb://localhost:3306/wielkie_akcje
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect

# --- Mail Configuration (Required for Account Activation) ---
# You can use a real SMTP server or a tool like Mailtrap.io for testing
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL_USER
spring.mail.password=YOUR_EMAIL_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# --- App General Config ---
app.base-url=http://localhost:8080
# Cleanup task interval (1 hour in ms)
cleanup.task.rate.ms=3600000

Building and Running
1. Build the Project

Open a terminal in the project root directory and run:

mvn clean install

This will compile the code, run tests, and build the JAR file.

Accessing the Application
Once the application has started, open your web browser and navigate to:

if your configuration is set to host locally

http://localhost:8080

or if you host it remotely please verify ip and replace localhost

Running Tests
To execute the test suite (Unit & Integration tests), run:

mvn test

❗ Troubleshooting
Registration form empty/not loading:
Ensure you have imported the TERYT SQL data into the database.
Check the browser console for API errors (404 on /api/teryt/...).
Duplicate Entry Error during payment:
Ensure your database schema has updated constraints. If you see Duplicate entry errors in transaction_history, refer to the migration guide to change the OneToOne relation to ManyToOne.

