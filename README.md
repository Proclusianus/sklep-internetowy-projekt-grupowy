Here is the updated, comprehensive README.md file. It reflects the latest changes, including the Wallet system, "Bobux" currency logic, TERYT integration, and the Hybrid Auction/Buy Now system.

Wielkie Akcje i Transakcje (Great Actions & Transactions)

![alt text](https://img.shields.io/badge/Java-17%2B-orange)


![alt text](https://img.shields.io/badge/Spring_Boot-3.x-green)


![alt text](https://img.shields.io/badge/Thymeleaf-Renderer-blue)


![alt text](https://img.shields.io/badge/Tailwind_CSS-Styling-cyan)


![alt text](https://img.shields.io/badge/Database-MariaDB%2FMySQL-lightgrey)

A robust E-commerce and Auction Marketplace built with Spring Boot. This application simulates a real-world platform allowing users to trade items using an internal currency ("Bobux"), participate in live auctions, and manage a digital wallet with detailed transaction history.

🚀 Key Features
🛒 Hybrid Marketplace

Three Sale Modes:

Buy Now: Standard e-commerce purchasing.

Auction: Time-based bidding system.

Hybrid: Items available for both immediate purchase and bidding.

Product Management: Users can list items with descriptions, categories, and image URLs.

Session-based Cart: Lightweight shopping cart managed in the user session.

💰 Financial System (Wallet & "Bobux")

Internal Currency: All transactions use Bobux.

Digital Wallet: Every user has a wallet that stores their balance.

Top-up Simulation: Users can fund their wallets using simulated:

Credit Cards (Validation of number format).

Bank Accounts (IBAN validation).

Gift Cards (Redemption logic).

Double-Entry Transaction History:

When a purchase is made, the system generates separate transaction records for the Buyer (Debit) and the Seller (Credit).

Transactions are linked to specific Orders, Cards, or Bank Accounts for full auditability.

🔨 Auction Logic

Real-time Bidding: Validation ensures new bids are higher than the current price.

Auto-Closing: A scheduled background task (@Scheduled) monitors expiration dates.

Ownership Transfer: When an auction ends, ownership of the product is automatically transferred to the winner.

👤 Advanced User Management

Dual Account Types:

Individuals: Validated via personal details.

Companies: Validated via NIP (Tax ID) and KRS (Court Register) algorithms.

TERYT Integration (Polish Address Registry):

Registration forms use dynamic, cascading dropdowns (Voivodeship -> Powiat -> Gmina -> City -> Street) based on the official TERYT database.

Supports foreign addresses with different validation rules.

Security: Custom UserDetailsService, BCrypt password hashing, and session persistence in the database.

🛠️ Technology Stack

Backend: Java 17, Spring Boot 3 (Web, Security, Data JPA, Mail, Scheduling).

Database: MariaDB or MySQL (Production), H2 (Testing).

ORM: Hibernate (JPA).

Frontend: Thymeleaf (Server-Side Rendering).

Styling: Tailwind CSS (Utility-first framework).

Scripts: JavaScript + TomSelect (for dynamic searchable dropdowns).

Testing: JUnit 5, Mockito, Spring Boot Test (@WebMvcTest, @SpringBootTest).

📂 Project Structure

controllers: Handles MVC views and REST API endpoints.

services: Contains core business logic (Payment processing, Auction rules, TERYT logic).

repository: Data access layer.

entities: Database models (User, Product, Wallet, TransactionData, TERYT tables).

dto: Data Transfer Objects for forms and API requests.

other: Validators (NIP, IBAN, RegEx) and Security configuration.

⚙️ Configuration & Setup
Prerequisites

JDK 17 or higher.

Maven.

MariaDB or MySQL Server.

1. Database Initialization (Crucial!)

This application relies on TERYT dictionary tables (teryt_voivodeships, teryt_powiats, etc.).

Create a database named wielkie_akcje.

You MUST import the TERYT SQL data into your database before running the app. Without this, the registration page will not load.

2. Application Properties

Configure src/main/resources/application.properties:

code
Properties
download
content_copy
expand_less
# Database Configuration
spring.datasource.url=jdbc:mariadb://localhost:3306/wielkie_akcje
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Mail Configuration (For registration tokens)
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your_email
spring.mail.password=your_email_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# App Config
app.base-url=http://localhost:8080
3. Running the App
code
Bash
download
content_copy
expand_less
# Clone the repository
git clone https://github.com/your-username/wielkie-akcje-transakcje.git

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

Access the application at: http://localhost:8080

🧪 Testing

The project includes a comprehensive test suite covering:

Unit Tests: Validators, Service logic (Auction rules, Payment calculations).

Integration Tests: Full process scenarios (Registration -> Top-up -> Purchase) using an in-memory H2 database.

To run tests:

code
Bash
download
content_copy
expand_less
mvn test
📖 Usage Guide (Happy Path)

Registration: Go to /register. Select "Individual" or "Company". Notice the dynamic TERYT address fields.

Wallet: Log in and go to "Płatności" (Wallet).

Add a simulated Credit Card or Bank Account.

Use "Doładuj konto" to add Bobux funds.

Listing: Go to "Sprzedaj przedmiot". Choose "Kup Teraz", "Licytacja", or "Oba".

Buying:

Shop: Add items to Cart -> Checkout -> Select Payment Method -> Pay.

Auction: Place a bid higher than the current price. Wait for the auction to end (simulated by @Scheduled task or manual trigger).

History: Check "Historia Transakcji" to see the debit/credit operations.

🔒 API Endpoints

The application also exposes REST endpoints for specific operations:

POST /api/register - JSON-based registration.

POST /api/login - API Authentication.

GET /api/teryt/* - Endpoints for fetching address data (Voivodeships, Powiats, etc.).

GET /api/user/profile - Fetch current user profile data.


Base Scripts


Create

SET NAMES utf8mb4;

-- 1. Tabela: personal_data
CREATE TABLE IF NOT EXISTS `personal_data` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255),
  `surname` VARCHAR(255),
  `phone_number` VARCHAR(20)
);

-- 2. Tabela: countries
CREATE TABLE IF NOT EXISTS `countries` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name_pl` VARCHAR(255),
  `code_iso_alpha2` VARCHAR(2),
  `admin_area_level_1_label` VARCHAR(255),
  `admin_area_level_2_label` VARCHAR(255),
  `admin_area_level_3_label` VARCHAR(255),
  `admin_area_level_4_label` VARCHAR(255)
);

-- 3. Tabela: address
CREATE TABLE IF NOT EXISTS `address` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `address_type` VARCHAR(50),
  `street` VARCHAR(255),
  `house_number` VARCHAR(20),
  `apartment_number` VARCHAR(20),
  `city` VARCHAR(255),
  `zip_code` VARCHAR(20),
  `country` VARCHAR(255),
  `business_delivery_address_id` BIGINT,
  `personal_data_id` BIGINT,
  `administrative_area_level_1` VARCHAR(255),
  `administrative_area_level_2` VARCHAR(255),
  `administrative_area_level_3` VARCHAR(255),
  `administrative_area_level_4` VARCHAR(255)
);

-- 4. Tabela: contact_person
CREATE TABLE IF NOT EXISTS `contact_person` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `contact_person_type` VARCHAR(50),
  `name` VARCHAR(255),
  `surname` VARCHAR(255),
  `email` VARCHAR(255),
  `phone_number` VARCHAR(20)
);

-- 5. Tabela: business_data
CREATE TABLE IF NOT EXISTS `business_data` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `firm_name` VARCHAR(255),
  `krs_id` VARCHAR(50),
  `nip` VARCHAR(50),
  `contact_person_id` BIGINT,
  `hq_address_id` BIGINT
);

-- 6. Tabela: user_data
CREATE TABLE IF NOT EXISTS `user_data` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(255),
  `email` VARCHAR(255),
  `password_hash` VARCHAR(255),
  `account_type` VARCHAR(50),
  `enabled` BIT(1) DEFAULT 1,
  `wallet_id` BIGINT,
  `personal_data_id` BIGINT,
  `business_data_id` BIGINT,
  `admin_data_id` BIGINT
);

-- 7. Tabela: sessions
CREATE TABLE IF NOT EXISTS `sessions` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `session` VARCHAR(255),
  `user_id` BIGINT
);

-- 8. Tabela: confirmation_tokens (brak danych w dumpie, struktura standardowa)
CREATE TABLE IF NOT EXISTS `confirmation_tokens` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `token` VARCHAR(255),
  `user_id` BIGINT,
  `created_at` DATETIME,
  `expires_at` DATETIME
);

-- 9. Tabela: product
-- Uwaga: Tabela zawiera zduplikowane nazwy kolumn (np. name/nazwa, price/cena) zgodnie z INSERT
CREATE TABLE IF NOT EXISTS `product` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255),
  `nazwa` VARCHAR(255),
  `description` TEXT,
  `opis` TEXT,
  `price` DECIMAL(10,2),
  `cena` DECIMAL(10,2),
  `category` VARCHAR(255),
  `kategoria` VARCHAR(255),
  `image_url` VARCHAR(512),
  `sell_type` VARCHAR(50),
  `seller_id` BIGINT
);

-- 10. Tabela: auction
CREATE TABLE IF NOT EXISTS `auction` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `aktywna` BIT(1),
  `aktualna_cena` DECIMAL(10,2),
  `cena_wywolawcza` DECIMAL(10,2),
  `data_zakonczenia` DATETIME,
  `product_id` BIGINT,
  `sprzedajacy_user_id` BIGINT
);

-- 11. Tabela: oferta
CREATE TABLE IF NOT EXISTS `oferta` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `kwota` DECIMAL(10,2),
  `data_zlozenia` DATETIME,
  `aukcja_id` BIGINT,
  `licytujacy_id` BIGINT
);

-- 12. Tabela: shop_order
CREATE TABLE IF NOT EXISTS `shop_order` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `order_date` DATETIME,
  `total_amount` DECIMAL(10,2),
  `status` VARCHAR(50),
  `delivery_address_snapshot` TEXT,
  `buyer_id` BIGINT,
  `auction_receipt_id` BIGINT
);

-- 13. Tabela: order_item
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `price_at_purchase` DECIMAL(10,2),
  `product_id` BIGINT,
  `order_id` BIGINT
);

-- 14. Tabela: bank_account_data
CREATE TABLE IF NOT EXISTS `bank_account_data` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `iban` VARCHAR(34),
  `wallet_id` BIGINT
);

-- 15. Tabela: card_data
CREATE TABLE IF NOT EXISTS `card_data` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `card_number_masked` VARCHAR(20),
  `card_number_hash` VARCHAR(255),
  `wallet_id` BIGINT
);

-- 16. Tabela: gift_card_data
CREATE TABLE IF NOT EXISTS `gift_card_data` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `code` VARCHAR(50),
  `amount_to_add` DECIMAL(10,2)
);

-- 17. Tabela: transaction_history
CREATE TABLE IF NOT EXISTS `transaction_history` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `amount` DECIMAL(10,2),
  `balance_after` DECIMAL(10,2),
  `created_at` DATETIME,
  `description` VARCHAR(255),
  `transaction_type` VARCHAR(50),
  `wallet_id` BIGINT,
  `shop_order_id` BIGINT,
  `order_data_id` BIGINT,
  `used_bank_account_id` BIGINT,
  `used_card_id` BIGINT,
  `giftcard_id` BIGINT
);


Insert

/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.address
LIMIT 0, 50000

-- Date: 2025-11-26 20:18
*/
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (1,'ADRES_DOMOWY','1','Polakowice','AFGANISTAN','1','warszawska','22-311',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (2,'ADRES_DOMOWY','3','asd','AFGANISTAN','3','awd','22112',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (3,'ADRES_FIRMY','','aaa','ALBANIA','1','aaa','231',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (4,'ADRESY_DOSTAWCZE_FIRMY','','Wrzosowice','','2','sigmy','11111',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (5,'ADRES_DOMOWY','1','asd','ALGIERIA','11','dwas','22222',NULL,3,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (6,'ADRES_FIRMY','','b','ALGIERIA','1','b','44444',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (7,'ADRESY_DOSTAWCZE_FIRMY','','TEST','ANGOLA','1','a','21311',2,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (8,'ADRES_DOMOWY','','','','','sddd','',NULL,4,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (9,'ADRES_DOMOWY','','','','','','',NULL,5,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (10,'ADRES_DOMOWY','','','POLSKA','','','',NULL,6,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (11,'ADRES_DOMOWY','sadsa','dasdsa','LUKSEMBURG','asdasd','asasdsa','asdsad',NULL,7,'dweasd',NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (12,'ADRES_DOMOWY','2','Drezno','NIEMCY','2','niewiem','2222',NULL,8,'Saksonia',NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (13,'ADRES_DOMOWY','2','Paryz','FRANCJA','2','degaułla','2222',NULL,9,'reżjon parisjen','pari','nie wiem','tez nie wiem');
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (14,'ADRES_DOMOWY','2','Paryz','POLSKA','2','degaułla','2222',NULL,10,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (15,'ADRES_DOMOWY','','','POLSKA','','','',NULL,11,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (16,'ADRES_DOMOWY','','Bartodzieje Małe','POLSKA','hgdfh','ftghdfggd','hgfhd',NULL,12,'KUJAWSKO-POMORSKIE','Bydgoszcz','Bydgoszcz',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (17,'ADRES_DOMOWY','','kghjk','ALBANIA','hkgjh','hgukhjgk','uhgkuhk',NULL,13,'hjgk',NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (18,'ADRES_FIRMY','','dfgdfg','AFGANISTAN','dfgfdgfdg','dfgdfgdf','dfgfdgdfg',NULL,NULL,'fgdf',NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (19,'ADRESY_DOSTAWCZE_FIRMY',NULL,NULL,'POLSKA',NULL,NULL,NULL,3,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (20,'ADRES_FIRMY','','dfgdfg','POLSKA','dfgfdgfdg','dfgdfgdf','dfgfdgdfg',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (21,'ADRESY_DOSTAWCZE_FIRMY',NULL,NULL,'PERU',NULL,NULL,NULL,4,NULL,'dolnego peru','spaniae','cordobanova',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (22,'ADRES_FIRMY','','',NULL,'','','',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (23,'ADRESY_DOSTAWCZE_FIRMY','','czikago','STANY ZJEDNOCZONE','111','pomaranczowa','nwm',5,NULL,'Iłinojs','czikago',NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (24,'ADRESY_DOSTAWCZE_FIRMY','','al-czikago','AFGANISTAN','11','sadf','333',5,NULL,'alhumda',NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (25,'ADRESY_DOSTAWCZE_FIRMY','','Biłgoraj','POLSKA','546456','ul. Akacjowa','5654',5,NULL,'LUBELSKIE','biłgorajski','Biłgoraj',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (26,'ADRES_FIRMY','','',NULL,'','','',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (27,'ADRESY_DOSTAWCZE_FIRMY','','sdfsdaf','AFGANISTAN','sdfdsa','sdfdsf','dsfdsf',6,NULL,'fadsf',NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (28,'ADRESY_DOSTAWCZE_FIRMY','','Bolesławiec','POLSKA','fdsfsdfdsf','ul. \"Hubala\" Majora','fsdfsdfds',6,NULL,'DOLNOŚLĄSKIE','bolesławiecki','Bolesławiec',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (29,'ADRES_FIRMY','','hdfgh','PERU','gfhhgf','gfhdfgh','gfhfgh',NULL,NULL,'hgfg','hfg','hdfgh',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (30,'ADRESY_DOSTAWCZE_FIRMY','','sdfsdaf','AFGANISTAN','sdfdsa','sdfdsf','dsfdsf',7,NULL,'fadsf',NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (31,'ADRESY_DOSTAWCZE_FIRMY','','Bolesławiec','POLSKA','fdsfsdfdsf','ul. \"Hubala\" Majora','fsdfsdfds',7,NULL,'DOLNOŚLĄSKIE','bolesławiecki','Bolesławiec',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (32,'ADRES_FIRMY','','Brodnica','POLSKA','kyukg','ul. 700-lecia','uijy',NULL,NULL,'KUJAWSKO-POMORSKIE','brodnicki','Brodnica',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (33,'ADRESY_DOSTAWCZE_FIRMY','','gcbgxcvb','ANGOLA','vbvbvc','bgvbcvbg','ggcvbgcv',8,NULL,'bcvgbg',NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (34,'ADRES_DOMOWY','vfvfv','Bolesławiec','POLSKA','vfgvffd','ul. Agatowa','xvdf',NULL,14,'DOLNOŚLĄSKIE','bolesławiecki','Bolesławiec',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (35,'ADRES_DOMOWY','','Bolesławiec','POLSKA','7','ul. Agatowa','07-342',NULL,15,'DOLNOŚLĄSKIE','bolesławiecki','Bolesławiec',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (36,'ADRES_DOMOWY','','Bolesławiec','POLSKA','7','ul. Agatowa','07-342',NULL,16,'DOLNOŚLĄSKIE','bolesławiecki','Bolesławiec',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (37,'ADRES_DOMOWY','','Bolesławiec','POLSKA','7','ul. Agatowa','07-342',NULL,17,'DOLNOŚLĄSKIE','bolesławiecki','Bolesławiec',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (51,'ADRES_FIRMY',NULL,'','','','','',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (52,'ADRES_DOMOWY','','','','','','',NULL,31,NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (55,'ADRES_DOMOWY','31','Bobrowo','POLSKA','12','Pier','01-2101',NULL,34,'KUJAWSKO-POMORSKIE','brodnicki','Bobrowo',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (56,'ADRES_DOMOWY','','Bolesławiec','POLSKA','24','ul. Akacjowa','05-809',NULL,35,'','','','');
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (63,'ADRES_DOMOWY','31','Bielawa','POLSKA','12','ul. Akacjowa','01-2101',NULL,42,'DOLNOŚLĄSKIE','dzierżoniowski','Bielawa',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (79,'ADRES_DOMOWY','31','Kolonia Las','POLSKA','12','ul Jelenia','01-099',NULL,58,'ŚLĄSKIE','bielski','Czechowice-Dziedzice',NULL);
INSERT INTO `` (`id`,`address_type`,`apartment_number`,`city`,`country`,`house_number`,`street`,`zip_code`,`business_delivery_address_id`,`personal_data_id`,`administrative_area_level_1`,`administrative_area_level_2`,`administrative_area_level_3`,`administrative_area_level_4`) VALUES (80,'ADRES_DOMOWY','4B','Warszawa','POLSKA','15','ul. Sezamkowa','00-999',NULL,59,'MAZOWIECKIE','Warszawa','Bemowo',NULL);
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.auction
LIMIT 0, 50000

-- Date: 2025-11-26 20:20
*/
INSERT INTO `` (`id`,`aktywna`,`aktualna_cena`,`cena_wywolawcza`,`data_zakonczenia`,`product_id`,`sprzedajacy_user_id`) VALUES (3,'1',342.00,150.00,'2025-12-03 15:35:11.000000',1,1);
INSERT INTO `` (`id`,`aktywna`,`aktualna_cena`,`cena_wywolawcza`,`data_zakonczenia`,`product_id`,`sprzedajacy_user_id`) VALUES (4,'1',501.00,500.00,'2025-12-03 15:35:11.000000',2,1);
INSERT INTO `` (`id`,`aktywna`,`aktualna_cena`,`cena_wywolawcza`,`data_zakonczenia`,`product_id`,`sprzedajacy_user_id`) VALUES (6,'1',800.00,800.00,'2025-12-03 15:35:11.000000',8,1);
INSERT INTO `` (`id`,`aktywna`,`aktualna_cena`,`cena_wywolawcza`,`data_zakonczenia`,`product_id`,`sprzedajacy_user_id`) VALUES (7,'1',300.00,300.00,'2025-12-03 15:35:11.000000',13,1);
INSERT INTO `` (`id`,`aktywna`,`aktualna_cena`,`cena_wywolawcza`,`data_zakonczenia`,`product_id`,`sprzedajacy_user_id`) VALUES (8,'1',26000.00,2500.00,'2025-12-03 15:35:11.000000',14,1);
INSERT INTO `` (`id`,`aktywna`,`aktualna_cena`,`cena_wywolawcza`,`data_zakonczenia`,`product_id`,`sprzedajacy_user_id`) VALUES (9,'1',100.00,100.00,'2025-12-03 15:35:11.000000',21,1);
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.bank_account_data
LIMIT 0, 50000

-- Date: 2025-11-26 20:20
*/
INSERT INTO `` (`id`,`iban`,`wallet_id`) VALUES (1,'PL67203000030002000111111001',7);
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.business_data
LIMIT 0, 50000

-- Date: 2025-11-26 20:21
*/
INSERT INTO `` (`id`,`firm_name`,`krs_id`,`nip`,`contact_person_id`,`hq_address_id`) VALUES (1,NULL,'222','111',1,3);
INSERT INTO `` (`id`,`firm_name`,`krs_id`,`nip`,`contact_person_id`,`hq_address_id`) VALUES (2,'b','111','555',2,6);
INSERT INTO `` (`id`,`firm_name`,`krs_id`,`nip`,`contact_person_id`,`hq_address_id`) VALUES (3,'januszexpłytki','dawdds','dsadsadasd',3,18);
INSERT INTO `` (`id`,`firm_name`,`krs_id`,`nip`,`contact_person_id`,`hq_address_id`) VALUES (4,'januszexziemniaki','dawddshfg','dsadsadasdfgh',4,20);
INSERT INTO `` (`id`,`firm_name`,`krs_id`,`nip`,`contact_person_id`,`hq_address_id`) VALUES (5,'Ręczniki z Czikago','44444','55564575',5,22);
INSERT INTO `` (`id`,`firm_name`,`krs_id`,`nip`,`contact_person_id`,`hq_address_id`) VALUES (6,'jgyufjghj','8678678675876','768686',6,26);
INSERT INTO `` (`id`,`firm_name`,`krs_id`,`nip`,`contact_person_id`,`hq_address_id`) VALUES (7,'jgyufjghjgfhfgh','8678678675876gfhgfh','768686hfghgfh',7,29);
INSERT INTO `` (`id`,`firm_name`,`krs_id`,`nip`,`contact_person_id`,`hq_address_id`) VALUES (8,'gukhjiukuhgjfyghj','7898797896896786','67867987979879',8,32);
INSERT INTO `` (`id`,`firm_name`,`krs_id`,`nip`,`contact_person_id`,`hq_address_id`) VALUES (9,'','','',9,51);
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.card_data
LIMIT 0, 50000

-- Date: 2025-11-26 20:21
*/
INSERT INTO `` (`id`,`card_number_hash`,`wallet_id`,`card_number_masked`) VALUES (1,NULL,7,'************6969');
INSERT INTO `` (`id`,`card_number_hash`,`wallet_id`,`card_number_masked`) VALUES (2,NULL,7,'************6969');
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.confirmation_tokens
LIMIT 0, 50000

-- Date: 2025-11-26 20:22
*/
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.contact_person
LIMIT 0, 50000

-- Date: 2025-11-26 20:22
*/
INSERT INTO `` (`id`,`contact_person_type`,`email`,`name`,`phone_number`,`surname`) VALUES (1,'FIRMA','janusz.sigma@onet.pl','janusz','+48222111333','rodak');
INSERT INTO `` (`id`,`contact_person_type`,`email`,`name`,`phone_number`,`surname`) VALUES (2,'FIRMA','ab@b.b','ab','+48222111333','ab');
INSERT INTO `` (`id`,`contact_person_type`,`email`,`name`,`phone_number`,`surname`) VALUES (3,'FIRMA','janusz.sigmamale@onet.pl','jaro','+48222111211','sapieha');
INSERT INTO `` (`id`,`contact_person_type`,`email`,`name`,`phone_number`,`surname`) VALUES (4,'FIRMA','janusz.sigmamale@onet.pl','jaro','+48222111211','sapieha');
INSERT INTO `` (`id`,`contact_person_type`,`email`,`name`,`phone_number`,`surname`) VALUES (5,'FIRMA','nwm@nwm.nwm','nwm','+48222111333','nwm');
INSERT INTO `` (`id`,`contact_person_type`,`email`,`name`,`phone_number`,`surname`) VALUES (6,'FIRMA','jafdsafsdnusz.sigma@onet.pl','fsdfasd','+48222111333','fdsafsdfd');
INSERT INTO `` (`id`,`contact_person_type`,`email`,`name`,`phone_number`,`surname`) VALUES (7,'FIRMA','jafdsafsdnusz.sigma@onet.pl','fsdfasd','+48222111333','fdsafsdfd');
INSERT INTO `` (`id`,`contact_person_type`,`email`,`name`,`phone_number`,`surname`) VALUES (8,'FIRMA','jgyjyg@fresdfs.jftyhf','uyjhj','+48222111333','gyfjg');
INSERT INTO `` (`id`,`contact_person_type`,`email`,`name`,`phone_number`,`surname`) VALUES (9,NULL,'','','','');
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.countries
LIMIT 0, 50000

-- Date: 2025-11-26 20:23
*/
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (1,'AFGANISTAN','AF','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (2,'ALBANIA','AL','County',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (3,'ALGIERIA','DZ','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (4,'ANDORA','AD','Parish',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (5,'ANGOLA','AO','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (6,'ANGUILLA','AI','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (7,'ANTARKTYKA','AQ',NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (8,'ANTIGUA I BARBUDA','AG','Parish / Dependency',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (10,'ARABIA SAUDYJSKA','SA','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (11,'ARGENTYNA','AR','Provincia','Departamento',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (12,'ARMENIA','AM','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (13,'ARUBA','AW','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (14,'AUSTRALIA','AU','State / Territory','Local Government Area',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (15,'AUSTRIA','AT','Bundesland',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (16,'AZERBEJDŻAN','AZ','District / City',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (17,'BAHAMY','BS','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (18,'BAHRAJN','BH','Governorate',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (19,'BANGLADESZ','BD','Division','District',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (20,'BARBADOS','BB','Parish',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (21,'BELGIA','BE','Region','Provincie',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (22,'BELIZE','BZ','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (23,'BENIN','BJ','Department',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (24,'BERMUDY','BM','Parish',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (25,'BHUTAN','BT','Dzongkhag','Gewog',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (26,'BIAŁORUŚ','BY','Oblast',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (27,'BOLIWIA','BO','Departamento','Provincia',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (28,'BOŚNIA I HERCEGOWINA','BA','Canton / Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (29,'BOTSWANA','BW','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (30,'BRAZYLIA','BR','Estado','Município',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (31,'BRUNEI DARUSSALAM','BN','Daerah','Mukim',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (32,'BRYTYJSKIE TERYTORIUM OCEANU INDYJSKIEGO','IO',NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (33,'BRYTYJSKIE WYSPY DZIEWICZE','VG','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (34,'BUŁGARIA','BG','Oblast',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (35,'BURKINA FASO','BF','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (36,'BURUNDI','BI','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (37,'CHILE','CL','Región','Provincia','Comuna',NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (38,'CHINY','CN','Province / Municipality','Prefecture / City',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (39,'CHORWACJA','HR','Županija',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (40,'CYPR','CY','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (41,'CZAD','TD','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (42,'CZECHY','CZ','Kraj',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (43,'DANIA','DK','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (44,'DOMINIKA','DM','Parish',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (45,'DOMINIKANA','DO','Provincia','Municipio',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (46,'DŻIBUTI','DJ','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (47,'EGIPT','EG','Governorate',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (48,'EKWADOR','EC','Provincia','Cantón',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (49,'ERYTREA','ER','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (50,'ESTONIA','EE','Maakond',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (51,'ETIOPIA','ET','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (52,'FALKLANDY','FK','Camp / Stanley',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (53,'FIDŻI','FJ','Division','Province',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (54,'FILIPINY','PH','Region','Province',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (55,'FINLANDIA','FI','Maakunta / Landskap',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (56,'FRANCJA','FR','Région','Département','Arrondissement','Commune');
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (57,'FRANCUSKIE TERYTORIA POŁUDNIOWE','TF','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (58,'GABON','GA','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (59,'GAMBIA','GM','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (60,'GEORGIA POŁÓDNIOWA I SANDWICH POŁÓDNIOWY','GS',NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (61,'GHANA','GH','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (62,'GIBRALTAR','GI','Major Residential Area',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (63,'GRECJA','GR','Decentralized Administration','Periphery',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (64,'GRENADA','GD','Parish',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (65,'GRENLANDIA','GL','Kommune',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (66,'GUAM','GU','Village',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (67,'GUJANA','GY','Region','Neighbourhood Democratic Council',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (68,'GUJANA FRANCUSKA','GF','Arrondissement','Commune',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (69,'GWADELUPA','GP','Arrondissement','Commune',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (70,'GWATEMALA','GT','Departamento','Municipio',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (71,'GWINEA','GN','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (72,'GWINEA RÓWNIKOWA','GQ','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (73,'GWINEA-BISSAU','GW','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (74,'HAITI','HT','Département','Arrondissement','Commune',NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (75,'HISZPANIA','ES','Comunidad Autónoma','Provincia',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (76,'HOLANDIA','NL','Provincie',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (77,'HONDURAS','HN','Departamento',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (78,'HONGKONG','HK','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (79,'INDIE','IN','State / Union Territory','District',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (80,'INDONEZJA','ID','Province','Regency / City',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (81,'IRAK','IQ','Governorate',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (82,'IRAN','IR','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (83,'IRLANDIA','IE','Province','County',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (84,'ISLANDIA','IS','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (85,'IZRAEL','IL','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (86,'JAMAJKA','JM','Parish',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (87,'JAPONIA','JP','Prefecture',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (88,'JEMEN','YE','Governorate',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (89,'JORDANIA','JO','Governorate',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (91,'KAJMANY','KY','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (92,'KAMBODŻA','KH','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (93,'KAMERUN','CM','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (94,'KANADA','CA','Province / Territory',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (95,'KATAR','QA','Municipality',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (96,'KAZACHSTAN','KZ','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (97,'KENIA','KE','County',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (98,'KIRGISTAN','KG','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (99,'KIRIBATI','KI','Island group','Island council',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (100,'KOLUMBIA','CO','Departamento','Municipio',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (101,'KOMORY','KM','Island',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (102,'KONGO','CG','Department',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (103,'KOREAŃSKA REPUBLIKA LUDOWO-DEMOKRATYCZNA','KP','Province / Directly-governed city','City / County',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (104,'KOSTARYKA','CR','Provincia','Cantón',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (105,'KUBA','CU','Provincia','Municipio',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (106,'KUWEJT','KW','Governorate',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (107,'LAOS','LA','Province','District',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (108,'LESOTHO','LS','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (109,'LIBAN','LB','Governorate','District',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (110,'LIBERIA','LR','County',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (111,'LIBIA','LY','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (112,'LIECHTENSTEIN','LI','Municipality',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (113,'LITWA','LT','Apskritis',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (114,'LUKSEMBURG','LU','Canton',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (115,'ŁOTWA','LV','Novads',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (116,'MACEDONIA','MK','Municipality',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (117,'MADAGASKAR','MG','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (118,'MAJOTTA','YT','Commune',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (119,'MAKAO','MO','Parish',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (120,'MALAWI','MW','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (121,'MALEDIWY','MV','Atoll / City',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (122,'MALEZJA','MY','State / Federal Territory',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (123,'MALI','ML','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (124,'MALTA','MT','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (125,'MAŁE ODDALONE WYSPY STANÓW ZJEDNOCZONYCH','UM',NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (126,'MAROKO','MA','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (127,'MARTYNIKA','MQ','Arrondissement','Commune',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (128,'MAURETANIA','MR','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (129,'MAURITIUS','MU','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (130,'MEKSYK','MX','Estado','Municipio',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (131,'MIKRONEZJA (, FEDERACJA PAŃSTW )','FM','State','Municipality',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (132,'MOŁDAWIA','MD','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (133,'MONAKO','MC','Quartier',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (134,'MONGOLIA','MN','Aimag / Capital City','Sum',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (135,'MONTSERRAT','MS','Parish',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (136,'MOZAMBIK','MZ','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (137,'MYANMAR','MM','Region / State','District',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (138,'NAMIBIA','NA','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (139,'NAURU','NR','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (140,'NEPAL','NP','Province','District',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (141,'NIEMCY','DE','Land',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (142,'NIGER','NE','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (143,'NIGERIA','NG','State','Local Government Area',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (144,'NIKARAGUA','NI','Departamento',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (145,'NIUE','NU','Village',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (146,'NORFOLK','NF',NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (147,'NORWEGIA','NO','Fylke',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (148,'NOWA KALEDONIA','NC','Province','Commune',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (149,'NOWA ZELANDIA','NZ','Region','Territorial Authority',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (150,'OMAN','OM','Governorate',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (151,'PAKISTAN','PK','Province / Territory','Division',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (152,'PALAU','PW','State',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (153,'PALESTYNA','PS','Governorate',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (154,'PANAMA','PA','Provincia','Distrito',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (155,'PAPUA-NOWA GWINEA','PG','Province','District',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (156,'PARAGWAJ','PY','Departamento','Distrito',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (157,'PERU','PE','Departamento','Provincia','Distrito',NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (158,'PITCAIRN','PN',NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (159,'POLINEZJA FRANCUSKA','PF','Subdivision','Commune',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (160,'POLSKA','PL','Województwo','Powiat','Gmina',NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (161,'PORTORYKO','PR','Municipio',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (162,'PORTUGALIA','PT','Distrito',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (163,'PÓŁNOCNE MARIANY','MP','Municipality',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (164,'REPUBLIKA KOREI','KR','Province / Special City',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (165,'REPUBLIKA POŁUDNIOWEJ AFRYKI','ZA','Province','District / Municipality',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (166,'REPUBLIKA ŚRODKOWO-AFRYKAŃSKA','CF','Prefecture',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (167,'REPUBLIKA ZIELONEGO PRZYLĄDKA','CV','Municipality',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (168,'REUNION','RE','Arrondissement','Commune',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (169,'ROSJA','RU','Federal Subject',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (170,'RUANDA','RW','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (171,'RUMUNIA','RO','Județ',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (172,'SAHARA ZACHODNIA','EH','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (173,'SAINT KITTS I NEVIS','KN','Parish',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (174,'SAINT LUCIA','LC','Quarter',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (175,'SAINT PIERRE I MIQUELON','PM','Commune',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (176,'SAINT VINCENT I GRENADYNY','VC','Parish',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (177,'SALWADOR','SV','Departamento',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (178,'SAMOA','WS','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (179,'SAMOA AMERYKAŃSKIE','AS','District','County',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (180,'SAN MARINO','SM','Municipality',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (181,'SENEGAL','SN','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (182,'SESZELE','SC','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (183,'SIERRA LEONE','SL','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (184,'SINGAPUR','SG','Region','Planning Area',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (185,'SŁOWACJA','SK','Kraj',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (186,'SOMALIA','SO','Federal Member State',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (187,'SRI LANKA','LK','Province','District',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (188,'STANY ZJEDNOCZONE','US','State','County',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (190,'SUAZI','SZ','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (191,'SUDAN','SD','State',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (192,'SURINAM','SR','Distrikt','Ressort',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (193,'SVALBARD I JAN MAYEN','SJ',NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (194,'SYRIA','SY','Governorate',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (195,'SZWAJCARIA','CH','Kanton',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (196,'SZWECJA','SE','Län',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (197,'ŚWIĘTA HELENA','SH','Administrative area','District',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (198,'TADŻYKISTAN','TJ','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (199,'TAJLANDIA','TH','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (200,'TAJWAN','TW','Special municipality / County','District / City',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (201,'TANZANIA','TZ','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (202,'TIMOR WSCHODNI','TP','Município',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (203,'TOGO','TG','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (204,'TOKELAU','TK','Atoll',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (205,'TONGA','TO','Division','District',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (206,'TRYNIDAD I TOBAGO','TT','Regional corporation / Municipality',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (207,'TUNEZJA','TN','Governorate',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (208,'TURCJA','TR','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (209,'TURKMENISTAN','TM','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (210,'TURKS I CAICOS','TC','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (211,'TUVALU','TV','Island council',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (212,'UGANDA','UG','Region','District',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (213,'UKRAINA','UA','Oblast',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (214,'URUGWAJ','UY','Departamento',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (215,'UZBEKISTAN','UZ','Region',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (216,'VANUATU','VU','Province','Municipality',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (217,'WALLIS I FUTUNA','WF','Kingdom',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (218,'WATYKAN','VA',NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (219,'WENEZUELA','VE','Estado','Municipio',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (220,'WĘGRY','HU','Megye',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (221,'WIELKA BRYTANIA','GB','Country','County',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (222,'WIETNAM','VN','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (223,'WŁOCHY','IT','Regione','Provincia',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (224,'WYBRZEŻE KOŚCI SŁONIOWEJ','CI','District',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (225,'WYSPA BOUVETA','BV',NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (226,'WYSPA BOŻEGO NARODZENIA','CX',NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (227,'WYSPY COOKA','CK','Island council',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (228,'WYSPY DZIEWICZE STANÓW ZJEDNOCZONYCH','VI','District','Sub-district',NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (229,'WYSPY HEARD I MCDONALDA','HM',NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (230,'WYSPY KOKOSOWE','CC',NULL,NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (231,'WYSPY MARSHALLA','MH','Municipality',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (232,'WYSPY OWCZE','FO','Kommuna',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (233,'WYSPY SALOMONA','SB','Province / Capital Territory',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (234,'WYSPY ŚWIĘTEGO TOMASZA I KSIĄŻĘCA','ST','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (236,'ZAMBIA','ZM','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (237,'ZIMBABWE','ZW','Province',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (238,'ZJEDNOCZONE EMIRATY ARABSKIE','AE','Emirate',NULL,NULL,NULL);
INSERT INTO `` (`id`,`name_pl`,`code_iso_alpha2`,`admin_area_level_1_label`,`admin_area_level_2_label`,`admin_area_level_3_label`,`admin_area_level_4_label`) VALUES (256,'RZECZPOSPOLITA RZYMSKA','RR','Praefectura Praetorio','Dioecesis','Provincia',NULL);
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.gift_card_data
LIMIT 0, 50000

-- Date: 2025-11-26 20:23
*/
INSERT INTO `` (`id`,`amount_to_add`,`code`) VALUES (1,50,'STUDENT');
INSERT INTO `` (`id`,`amount_to_add`,`code`) VALUES (2,100,'INŻYNIER');
INSERT INTO `` (`id`,`amount_to_add`,`code`) VALUES (3,200,'MAGISTER');
INSERT INTO `` (`id`,`amount_to_add`,`code`) VALUES (4,500,'DOKTOR');
INSERT INTO `` (`id`,`amount_to_add`,`code`) VALUES (5,1000,'PROFESOR');
INSERT INTO `` (`id`,`amount_to_add`,`code`) VALUES (6,5000,'REKTOR');
INSERT INTO `` (`id`,`amount_to_add`,`code`) VALUES (7,10000,'MINISTER OBRONY NARODOWEJ');
INSERT INTO `` (`id`,`amount_to_add`,`code`) VALUES (8,100000,'PREZYDENT');
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.oferta
LIMIT 0, 50000

-- Date: 2025-11-26 20:23
*/
INSERT INTO `` (`id`,`data_zlozenia`,`kwota`,`aukcja_id`,`licytujacy_id`) VALUES (6,'2025-11-26 15:42:12.304815',501.00,4,43);
INSERT INTO `` (`id`,`data_zlozenia`,`kwota`,`aukcja_id`,`licytujacy_id`) VALUES (7,'2025-11-26 15:42:40.318741',26000.00,8,43);
INSERT INTO `` (`id`,`data_zlozenia`,`kwota`,`aukcja_id`,`licytujacy_id`) VALUES (8,'2025-11-26 15:48:55.809665',151.00,3,43);
INSERT INTO `` (`id`,`data_zlozenia`,`kwota`,`aukcja_id`,`licytujacy_id`) VALUES (9,'2025-11-26 16:01:30.860199',211.00,3,43);
INSERT INTO `` (`id`,`data_zlozenia`,`kwota`,`aukcja_id`,`licytujacy_id`) VALUES (10,'2025-11-26 16:37:57.877370',341.00,3,43);
INSERT INTO `` (`id`,`data_zlozenia`,`kwota`,`aukcja_id`,`licytujacy_id`) VALUES (11,'2025-11-26 17:25:27.781970',342.00,3,54);
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.oferta
LIMIT 0, 50000

-- Date: 2025-11-26 20:23
*/
INSERT INTO `` (`id`,`data_zlozenia`,`kwota`,`aukcja_id`,`licytujacy_id`) VALUES (6,'2025-11-26 15:42:12.304815',501.00,4,43);
INSERT INTO `` (`id`,`data_zlozenia`,`kwota`,`aukcja_id`,`licytujacy_id`) VALUES (7,'2025-11-26 15:42:40.318741',26000.00,8,43);
INSERT INTO `` (`id`,`data_zlozenia`,`kwota`,`aukcja_id`,`licytujacy_id`) VALUES (8,'2025-11-26 15:48:55.809665',151.00,3,43);
INSERT INTO `` (`id`,`data_zlozenia`,`kwota`,`aukcja_id`,`licytujacy_id`) VALUES (9,'2025-11-26 16:01:30.860199',211.00,3,43);
INSERT INTO `` (`id`,`data_zlozenia`,`kwota`,`aukcja_id`,`licytujacy_id`) VALUES (10,'2025-11-26 16:37:57.877370',341.00,3,43);
INSERT INTO `` (`id`,`data_zlozenia`,`kwota`,`aukcja_id`,`licytujacy_id`) VALUES (11,'2025-11-26 17:25:27.781970',342.00,3,54);
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.order_item
LIMIT 0, 50000

-- Date: 2025-11-26 20:24
*/
INSERT INTO `` (`id`,`price_at_purchase`,`product_id`,`order_id`) VALUES (1,2499.00,1,1);
INSERT INTO `` (`id`,`price_at_purchase`,`product_id`,`order_id`) VALUES (2,2499.00,1,2);
INSERT INTO `` (`id`,`price_at_purchase`,`product_id`,`order_id`) VALUES (3,2499.00,1,3);
INSERT INTO `` (`id`,`price_at_purchase`,`product_id`,`order_id`) VALUES (4,350.00,2,4);
INSERT INTO `` (`id`,`price_at_purchase`,`product_id`,`order_id`) VALUES (5,350.00,2,5);
INSERT INTO `` (`id`,`price_at_purchase`,`product_id`,`order_id`) VALUES (6,4500.00,4,6);
INSERT INTO `` (`id`,`price_at_purchase`,`product_id`,`order_id`) VALUES (7,4500.00,4,7);
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.personal_data
LIMIT 0, 50000

-- Date: 2025-11-26 20:25
*/
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (1,'Dżon','+48111222333','Blendzior');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (2,'Don','+48333222111','Bon');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (3,'wae','+48444555666','dwa');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (4,'aefea','+48444555666','dwadwad');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (5,'','+48444555666','');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (6,'małpa','+48999888777','małpowicz');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (7,'dwadaw','+48999888777','dsadsadaw');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (8,'asdwadaswd','+48444555662','dfsfefesr');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (9,'asdwadaswd','+48444555662','dfsfefesr');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (10,'asdwadaswd','+48444555662','dfsfefesr');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (11,'drgdrhjgyfhfgy','+48444555666','ygfjhuygjf');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (12,'gyujguyhuyg','+48444555666','jgyjgyufjgy');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (13,'uhgkj','+48999888777','uhjgvk');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (14,'dfgsdfg','+48999888722','rgdsgr');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (15,'test','+48123456789','test');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (16,'test','+48123456789','test');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (17,'test','+48123456789','test');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (31,'Maurycy','+48123456787','Tychmanowicz');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (34,'Kumpan','+48665865904','Majster');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (35,'test','+48123456789','test');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (42,'Skrum','+48123456789','Majster');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (58,'Krzychu','+48123456789','Sitko');
INSERT INTO `` (`id`,`name`,`phone_number`,`surname`) VALUES (59,'Jan','+48123456789','Kowalski');
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.product
LIMIT 0, 50000

-- Date: 2025-11-26 20:25
*/
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (1,'Konsola PlayStation 5 Slim',2499.00,'Nowa konsola w oryginalnym pudełku. Wersja z napędem Blu-ray. Dołączam dwa pady i grę FIFA 24.','Elektronika',1,'LICYTACJA',150.00,NULL,'',NULL,'https://images.unsplash.com/photo-1606813907291-d86efa9b94db?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (2,'Ręcznie malowany wazon z lat 60.',350.00,'Piękny, ceramiczny wazon w stylu vintage. Brak uszczerbków, stan kolekcjonerski. Wysokość 30cm.','Kolekcje i Sztuka',1,'OBA',500.00,NULL,'',NULL,'https://images.unsplash.com/photo-1581594693702-fbdc51b2763b?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (4,'Laptop Gamingowy',4500.00,'Wydajny laptop do gier','Elektronika',1,'KUP_TERAZ',4500.00,'Elektronika','Laptop Gamingowy','Wydajny laptop do gier','https://images.unsplash.com/photo-1603302576837-37561b2e2302?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (5,'Smartfon XYZ',1200.00,'Telefon z dobrym aparatem','Elektronika',1,'KUP_TERAZ',1200.00,'Elektronika','Smartfon XYZ','Telefon z dobrym aparatem','https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (6,'Książka Java',80.00,'Nauka programowania od podstaw','Edukacja',1,'KUP_TERAZ',80.00,'Edukacja','Książka Java','Nauka programowania od podstaw','https://images.unsplash.com/photo-1532012197267-da84d127e765?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (8,'Stół dębowy',800.00,'Solidny stół do jadalni','Dom',1,'OBA',800.00,'Dom','Stół dębowy','Solidny stół do jadalni','https://images.unsplash.com/photo-1577140917170-285929fb55b7?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (9,'Słuchawki BT',200.00,'Bezprzewodowe słuchawki z redukcją szumów','Elektronika',1,'KUP_TERAZ',200.00,'Elektronika','Słuchawki BT','Bezprzewodowe słuchawki z redukcją szumów','https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (10,'Monitor 24 cale',600.00,'Monitor biurowy IPS','Elektronika',1,'KUP_TERAZ',600.00,'Elektronika','Monitor 24 cale','Monitor biurowy IPS','https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (11,'Klawiatura mechaniczna',350.00,'Podświetlenie RGB, przełączniki Blue','Elektronika',1,'KUP_TERAZ',350.00,'Elektronika','Klawiatura mechaniczna','Podświetlenie RGB, przełączniki Blue','https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (12,'Myszka bezprzewodowa',150.00,'Ergonomiczna mysz do pracy','Elektronika',1,'KUP_TERAZ',150.00,'Elektronika','Myszka bezprzewodowa','Ergonomiczna mysz do pracy','https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (13,'Kamera sportowa',900.00,'Nagrywanie 4K, wodoodporna','Elektronika',1,'LICYTACJA',300.00,'Elektronika','Kamera sportowa','Nagrywanie 4K, wodoodporna','https://images.unsplash.com/photo-1526660690293-bcd32dc3b123?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (14,'Dron',2500.00,'Zasięg 5km, kamera 4K','Elektronika',1,'OBA',2500.00,'Elektronika','Dron','Zasięg 5km, kamera 4K','https://images.unsplash.com/photo-1473968512647-3e447244af8f?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (15,'Konsola do gier',2100.00,'Zestaw z dwoma padami','Elektronika',1,'KUP_TERAZ',2100.00,'Elektronika','Konsola do gier','Zestaw z dwoma padami','https://images.unsplash.com/photo-1592840496694-26d035b52b48?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (16,'Tablet graficzny',1100.00,'Idealny dla grafików','Elektronika',1,'KUP_TERAZ',1100.00,'Elektronika','Tablet graficzny','Idealny dla grafików','https://images.unsplash.com/photo-1558655146-d09347e92766?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (17,'Zegarek sportowy',700.00,'GPS, pulsometr','Sport',1,'KUP_TERAZ',700.00,'Sport','Zegarek sportowy','GPS, pulsometr','https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (18,'Hantle 2x10kg',150.00,'Zestaw do ćwiczeń w domu','Sport',1,'KUP_TERAZ',150.00,'Sport','Hantle 2x10kg','Zestaw do ćwiczeń w domu','https://images.unsplash.com/photo-1584735935682-2f2b69dff9d2?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (19,'Mata do jogi',60.00,'Antypoślizgowa mata','Sport',1,'KUP_TERAZ',60.00,'Sport','Mata do jogi','Antypoślizgowa mata','https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (20,'Piłka nożna',100.00,'Oficjalny rozmiar meczowy','Sport',1,'KUP_TERAZ',100.00,'Sport','Piłka nożna','Oficjalny rozmiar meczowy','https://images.unsplash.com/photo-1614632537423-1e6c2e7e0aab?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (21,'Buty biegowe',300.00,'Rozmiar 42, bardzo wygodne','Sport',1,'LICYTACJA',100.00,'Sport','Buty biegowe','Rozmiar 42, bardzo wygodne','https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (22,'Kurtka przeciwdeszczowa',250.00,'Rozmiar L, idealna na jesień','Moda',1,'KUP_TERAZ',250.00,'Moda','Kurtka przeciwdeszczowa','Rozmiar L, idealna na jesień','https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (23,'Plecak turystyczny',400.00,'Pojemność 60L','Turystyka',1,'KUP_TERAZ',400.00,'Turystyka','Plecak turystyczny','Pojemność 60L','https://images.unsplash.com/photo-1553062407-98eeb64c6a62?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (24,'Namiot 2-osobowy',350.00,'Lekki namiot na wyprawy','Turystyka',1,'KUP_TERAZ',350.00,'Turystyka','Namiot 2-osobowy','Lekki namiot na wyprawy','https://images.unsplash.com/photo-1478131143081-80f7f84ca84d?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (25,'Śpiwór puchowy',500.00,'Komfort do -5 stopni','Turystyka',1,'KUP_TERAZ',500.00,'Turystyka','Śpiwór puchowy','Komfort do -5 stopni','https://images.unsplash.com/photo-1517824806704-9040b037703b?q=80&w=600');
INSERT INTO `` (`id`,`name`,`price`,`description`,`category`,`seller_id`,`sell_type`,`cena`,`kategoria`,`nazwa`,`opis`,`image_url`) VALUES (29,'Odkurzacz pionowy',950.00,'Bezprzewodowy, duża moc','Dom',1,'KUP_TERAZ',950.00,'Dom','Odkurzacz pionowy','Bezprzewodowy, duża moc','https://images.unsplash.com/photo-1527515545081-5db817172677?w=600');
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.sessions
LIMIT 0, 50000

-- Date: 2025-11-26 20:25
*/
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (32,'8ce8dcc7-3e3c-45b8-bc8b-0139c3379ab3',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (33,'5d1f487a-9b9d-4beb-b3cb-a067ad339300',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (34,'35ed45c6-7c24-4a7a-8831-56ce89981763',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (35,'aa235b21-946d-4a9a-8bb6-46d16c089e00',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (36,'e76e87a8-51be-494f-b2c0-b0c926447c51',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (37,'352a5fa3-2f41-47fa-b1bc-b51e659534d7',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (38,'a18e6cc9-a2c6-4afb-ae28-e5d81421d0ff',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (39,'f1971239-1840-4e37-84c6-0d903687f22a',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (40,'060741b7-de59-4c73-9b06-9612c1d4ef45',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (41,'6c3e6b67-a88b-4f2c-ab5c-8d176c1ca712',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (42,'54140429-b202-40a9-a29e-496859658079',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (43,'3ce15065-3b03-45d6-8c7a-bbd52a6f3d0f',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (44,'1f9e2931-63b4-4e10-8261-55944a5996e7',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (45,'37723bfb-e63b-4820-9980-9f0c483963e0',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (46,'f71b9f9a-f46b-422f-a52a-53cf607530ab',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (47,'86c457f8-e7bd-43da-a82f-422923098b56',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (48,'65e04195-d340-4d4a-94f0-7f711345eee9',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (49,'b8e168dd-e465-4413-bbbc-8e98d20e96df',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (50,'1973d2c3-1f49-4bf0-9fd9-dea8df84c719',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (51,'e4032e81-d5ce-4d1e-a0a8-4f2ea02d60d3',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (52,'6885caec-f1f9-4dd1-8d21-189ebd23fecd',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (53,'2aedc1ef-8a8a-4d8a-93e0-b0f358d286c7',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (54,'1d77aa96-3fbf-4473-9956-63b561116235',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (55,'95b26f27-de2d-4a70-8f71-57398ac67284',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (56,'f47f8baf-7b4f-458f-ba87-083dd978a5cb',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (58,'15139e1c-dac5-4ded-8408-4bd3e69abb69',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (59,'1cf08006-b248-46d6-adf7-101f1c39b2ad',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (60,'093d6e6b-2903-4417-8724-4429743cf707',4);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (64,'5c4f6a72-bf4d-454f-abf2-d42259966193',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (65,'602d5159-8cec-4b58-9206-9655797aa511',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (66,'75e9e9f0-a61a-4e08-be1b-0a1ef0744000',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (67,'4c1c2d4d-9d71-415e-b065-ee0b5e080bcd',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (68,'e67ee2e7-b639-4a9d-bedf-e11a255a6660',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (69,'aa339d2d-f1e9-486c-958c-2613b2684419',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (70,'a102d45e-5565-4278-83c0-b8fb872d2924',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (71,'b72e9eff-b567-484e-8f9a-e4f4ad9d5137',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (72,'d3d1f732-d5a7-4feb-b8b1-917da02b85a8',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (73,'b051454f-9b49-4e4d-ba3a-a3602b58537f',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (74,'fdd3ea5d-5110-4ccb-8039-c5a82ec92fce',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (75,'3db53c06-1adc-4857-828c-48e9ffd5def7',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (76,'73e2a3d8-9717-4cfa-9d57-52f008c65f9a',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (77,'43ff4763-b4e8-4e36-a2f8-d67ff5ab4a02',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (78,'c0abf1d0-9aba-4db2-8c3c-1a766bca8e7a',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (79,'5df778da-56cb-4e76-a8a6-a2b66bafd294',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (80,'51647aa2-88be-42e5-ac3f-b42b84717722',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (81,'acbccf5b-ceee-4565-a2b9-bd1b532a35fd',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (83,'2765572d-78fd-423f-9032-67cc7bb3bdc4',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (84,'71f70b63-8184-4c35-b10b-bc9303d9b88a',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (85,'db405bc8-5484-4bc4-99ba-8b5c7b2260bc',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (87,'df914010-57c4-4f89-8ee1-3ab57551a77a',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (88,'efe931e8-74e5-4053-bb75-900cc81044cd',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (89,'1c357c24-4fc8-4d23-a4aa-1cf741b17ef8',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (90,'74da612c-5d4d-4b1e-8484-553803670df6',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (93,'ba8db423-8f67-4256-a03b-5f05e91ae9e0',54);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (94,'24626e11-0c7c-4a77-b2fb-4a76de921051',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (97,'c8318b91-e2e8-402a-9fe1-4d5ac028663a',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (98,'dc006836-bb1c-4268-8ce3-d430a6c2c22c',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (99,'38ee0b71-bdc5-4614-b10b-0fad3f1a4fa6',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (100,'b9563104-bc7b-4109-807e-54b408daafc3',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (102,'b7f22b2e-7fd5-4ac9-942f-9280630c8350',54);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (103,'3c08ede8-3711-4c7e-aed1-fa5604a8a995',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (104,'64470b94-4dcf-4943-8e25-2db8505d26bd',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (105,'e8bb20c4-43a8-4703-a1b0-dc0dcb45f6f0',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (106,'fe14ada1-6ea1-4a7a-8f76-37d95ab75833',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (107,'9344dec8-eba3-43e5-920c-5d84fac27fc5',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (108,'f7792495-d00f-42ca-af4f-dcb86da3aa93',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (109,'3e7a21d4-d16c-4910-a7ef-45798e778895',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (110,'b6df532c-b238-4162-b8fa-25d007be8576',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (111,'4f9c296f-ee55-4a34-9613-f5b78a7012b3',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (112,'6c753046-ff7c-4691-b599-8d1ce978c45d',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (113,'f6880430-d35e-4c33-a5f9-beb4aeb72c38',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (114,'cb6be93d-1595-4e0f-9036-db18684a1ef0',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (115,'f2a7b718-62df-4467-8eb2-19c647291e7d',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (116,'0b68c6c5-b9fe-412f-9b33-5fd1008f0e59',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (117,'2a6e6e0a-2b02-4c76-9cb3-16c8ca081c98',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (118,'8a964526-b6b2-4fcf-810b-17da6d162c33',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (119,'88096b1f-0692-4ec4-9574-202644ca7e32',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (120,'5b079bf2-cbbb-44ce-a1f7-1c9c13c4a96f',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (121,'d2dddd65-9ff4-4227-a68e-4d87db0a4423',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (122,'35e54ae5-944a-4d35-940a-51006456de14',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (123,'11636154-6c25-4841-8977-7e9d19ce2ce1',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (124,'fbfd3151-3f93-4286-abf1-62d85d1148c7',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (125,'3cbc678e-df76-47d7-ac43-d00c57e5f51d',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (126,'4f14d2b1-3990-4497-9bc7-9183c50ed01c',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (127,'5e4dc0d3-2199-4e51-a0a8-de98ddd5b32a',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (128,'3a7f7e27-7600-40bb-8f96-0edf9c7e5378',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (129,'3a517df3-ea35-4237-9bf4-f93a864c14bb',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (131,'ba60dab7-82a8-4448-adbb-5c78451d85ac',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (132,'9cbaca1c-7371-4a57-b6a2-6ab4ae923c23',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (133,'4e147ea0-3832-4bc1-a3b1-ba065ca6636e',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (134,'19b4c89b-3292-49e0-b1bb-8a57e0ebf1a0',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (135,'56b34b44-769d-4b62-b729-86e5cfce7544',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (136,'b63b084f-dff5-46f9-9e10-32a50e701992',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (138,'0cb28b02-14b4-4e19-8406-3d1826f3fcf0',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (140,'abe58804-aa97-4d22-a651-223d407bc2f4',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (141,'308a21dd-bcd4-40ec-a201-aa307051d3da',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (142,'bb7f6fdd-75e9-4fef-bdb2-ac63ff30ec4b',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (143,'061a9767-579f-4608-822d-1b358a2ac553',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (145,'926525c9-359f-49bb-9328-d04c37164551',54);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (146,'0b29a9e3-6f99-4b2d-b3f7-214f085f0248',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (147,'48a27f20-3886-464c-bdb3-6efbf38978d0',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (148,'4c49ae7d-dc08-4c1f-99a4-a9128d35d87f',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (149,'a656c1df-27e3-46ed-9ddf-5347404d8539',43);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (151,'8bf2614b-8b5a-4c85-bddf-fbffe51fca63',70);
INSERT INTO `` (`id`,`session`,`user_id`) VALUES (153,'1c62b92b-a894-48c0-8f67-967a62e22263',43);
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.shop_order
LIMIT 0, 50000

-- Date: 2025-11-26 20:26
*/
INSERT INTO `` (`id`,`order_date`,`total_amount`,`status`,`delivery_address_snapshot`,`buyer_id`,`auction_receipt_id`) VALUES (1,'2025-11-22 13:50:23.617217',2499.00,'OPLACONE','Skrum Majster\nPier 12\n01-2101 Bobrowo\nTel: +48665865904',43,NULL);
INSERT INTO `` (`id`,`order_date`,`total_amount`,`status`,`delivery_address_snapshot`,`buyer_id`,`auction_receipt_id`) VALUES (2,'2025-11-22 14:00:21.100906',2499.00,'OPLACONE','Skrum Majster\nPier 12\n01-2101 Bobrowo\nTel: +48665865904',43,NULL);
INSERT INTO `` (`id`,`order_date`,`total_amount`,`status`,`delivery_address_snapshot`,`buyer_id`,`auction_receipt_id`) VALUES (3,'2025-11-24 22:27:26.121227',2499.00,'OPLACONE','Kumpan Majster\nPier 12\n01-2101 Bobrowo\nTel: +48665865904',43,NULL);
INSERT INTO `` (`id`,`order_date`,`total_amount`,`status`,`delivery_address_snapshot`,`buyer_id`,`auction_receipt_id`) VALUES (4,'2025-11-25 21:02:11.580516',350.00,'OPLACONE','Kumpan Majster\nPier 12\n01-2101 Bobrowo\nTel: +48665865904',43,NULL);
INSERT INTO `` (`id`,`order_date`,`total_amount`,`status`,`delivery_address_snapshot`,`buyer_id`,`auction_receipt_id`) VALUES (5,'2025-11-26 17:26:38.779401',350.00,'OPLACONE','Skrum Majster\nul. Akacjowa 12\n01-2101 Bielawa\nTel: +48123456789',54,NULL);
INSERT INTO `` (`id`,`order_date`,`total_amount`,`status`,`delivery_address_snapshot`,`buyer_id`,`auction_receipt_id`) VALUES (6,'2025-11-26 18:45:40.260522',4500.00,'NIEOPLACONE','Kumpan Majster\nPier 12\n01-2101 Bobrowo\nTel: +48665865904',43,NULL);
INSERT INTO `` (`id`,`order_date`,`total_amount`,`status`,`delivery_address_snapshot`,`buyer_id`,`auction_receipt_id`) VALUES (7,'2025-11-26 19:19:06.062479',4500.00,'NIEOPLACONE','Kumpan Majster\nPier 12\n01-2101 Bobrowo\nTel: +48665865904',43,NULL);
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.transaction_history
LIMIT 0, 50000

-- Date: 2025-11-26 20:28
*/
INSERT INTO `` (`id`,`amount`,`balance_after`,`created_at`,`description`,`transaction_type`,`order_data_id`,`used_bank_account_id`,`used_card_id`,`giftcard_id`,`wallet_id`,`shop_order_id`) VALUES (1,50,70,'2025-11-25 19:46:11.977081','Doładowanie kodem: STUDENT','TOP_UP_GIFT_CARD',NULL,NULL,NULL,1,7,NULL);
INSERT INTO `` (`id`,`amount`,`balance_after`,`created_at`,`description`,`transaction_type`,`order_data_id`,`used_bank_account_id`,`used_card_id`,`giftcard_id`,`wallet_id`,`shop_order_id`) VALUES (2,5000,5070,'2025-11-25 19:46:35.025528','Doładowanie kodem: REKTOR','TOP_UP_GIFT_CARD',NULL,NULL,NULL,6,7,NULL);
INSERT INTO `` (`id`,`amount`,`balance_after`,`created_at`,`description`,`transaction_type`,`order_data_id`,`used_bank_account_id`,`used_card_id`,`giftcard_id`,`wallet_id`,`shop_order_id`) VALUES (3,100,5170,'2025-11-25 20:22:16.298132','Doładowanie kodem: INŻYNIER','TOP_UP_GIFT_CARD',NULL,NULL,NULL,2,7,NULL);
INSERT INTO `` (`id`,`amount`,`balance_after`,`created_at`,`description`,`transaction_type`,`order_data_id`,`used_bank_account_id`,`used_card_id`,`giftcard_id`,`wallet_id`,`shop_order_id`) VALUES (4,1000,6170,'2025-11-25 20:40:02.472740','Doładowanie kartą: ************6969','TOP_UP_CARD',NULL,NULL,1,NULL,7,NULL);
INSERT INTO `` (`id`,`amount`,`balance_after`,`created_at`,`description`,`transaction_type`,`order_data_id`,`used_bank_account_id`,`used_card_id`,`giftcard_id`,`wallet_id`,`shop_order_id`) VALUES (5,2000,8170,'2025-11-25 20:40:07.909952','Doładowanie z konta: PL67203000030002000111111001','TOP_UP_BANK_ACC',NULL,1,NULL,NULL,7,NULL);
INSERT INTO `` (`id`,`amount`,`balance_after`,`created_at`,`description`,`transaction_type`,`order_data_id`,`used_bank_account_id`,`used_card_id`,`giftcard_id`,`wallet_id`,`shop_order_id`) VALUES (6,200,8370,'2025-11-25 20:59:22.367064','Doładowanie kodem: MAGISTER','TOP_UP_GIFT_CARD',NULL,NULL,NULL,3,7,NULL);
INSERT INTO `` (`id`,`amount`,`balance_after`,`created_at`,`description`,`transaction_type`,`order_data_id`,`used_bank_account_id`,`used_card_id`,`giftcard_id`,`wallet_id`,`shop_order_id`) VALUES (7,500,8870,'2025-11-25 21:46:21.995444','Doładowanie kodem: DOKTOR','TOP_UP_GIFT_CARD',NULL,NULL,NULL,4,7,NULL);
INSERT INTO `` (`id`,`amount`,`balance_after`,`created_at`,`description`,`transaction_type`,`order_data_id`,`used_bank_account_id`,`used_card_id`,`giftcard_id`,`wallet_id`,`shop_order_id`) VALUES (8,1000,9870,'2025-11-25 21:46:49.215175','Doładowanie kartą: ************6969','TOP_UP_CARD',NULL,NULL,2,NULL,7,NULL);
/*
-- Query: SELECT * FROM wielkie_akcje_i_transakcje.user_data
LIMIT 0, 50000

-- Date: 2025-11-26 20:28
*/
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (1,'OSOBA_FIZYCZNA','test@test.pl','testhaslo','testowa',NULL,NULL,59,'1',24);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (2,'OSOBA_FIZYCZNA','test@sussybaka.com','$2a$10$OIOUe71ezX86nfhOnQJ/4OIkKqGmjy/cRMcbtrTkKGfema0QBfpvu','testowy',NULL,NULL,1,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (3,'OSOBA_FIZYCZNA','registracione@testificate.it','$2a$10$gFdXirMMCtmqV2AmSRRfJO1HfMSkpX3KqxwfOjYUIW47/0DoIrZ46','donatello',NULL,NULL,2,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (4,'OSOBA_FIZYCZNA','test@example.com','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjRPN.oxLhu/1U6eb8.y.yGZ.w/2w6.','testuser',NULL,9,31,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (5,'FIRMA','januszex@onet.pl','$2a$10$oEjC5Lf9TcmK5ii6SRIDbej4hCqUl31EARx3SHPYn8rBAaSDs5EfK','januszex',NULL,1,NULL,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (6,'OSOBA_FIZYCZNA','a@a.a','$2a$10$PSYxU6aHTIckgC6IhDlomeGlOIlHfxaLzeNcaFKSQO4IezKvZu3ka','a',NULL,NULL,3,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (7,'FIRMA','b@b.b','$2a$10$tQ/0ktl4lD05dYWFDWLxTOUFEcEWWO4PxWXT1Sb.8jaAhQpULWlDm','b',NULL,2,NULL,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (8,'OSOBA_FIZYCZNA','aaa@b.pl','$2a$10$JL7Y4Q5tWt.FKIde0PcuAubeM.KoUAybVT0sk4oKqmcUZ0l4TCqme','cc',NULL,NULL,4,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (9,'OSOBA_FIZYCZNA','c@c.c','$2a$10$wdTYaJ49rBMXkGkhjSQ8fu8chcuNkT5O0ULZPVshF9IWAaerNXvxS','ccc',NULL,NULL,5,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (10,'OSOBA_FIZYCZNA','małpa@małpa.małpa','$2a$10$bSbD1rn0XYfZ6QziobKrouxc6de.4RUitmQI4vL/t/EtZOdPzOsP.','małpa',NULL,NULL,6,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (11,'OSOBA_FIZYCZNA','skibid@i.pl','$2a$10$N9gYxmwl8KQyf/GmefjzAeMnBD7fmut202me75jGG55f3BCnhxs/K','aasdaw',NULL,NULL,7,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (12,'OSOBA_FIZYCZNA','aaa@sadb.pl','$2a$10$ksZQs86nXJstFgsrOVOjV.LzFuNZ0itkLrLAfhVVl64MNvZqMV1.a','dasdasdawderes',NULL,NULL,8,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (13,'OSOBA_FIZYCZNA','ddddaaa@sadb.pl','$2a$10$GOcmaofYwxHQeAVrUgxE5eCNxuVRWTrhZExCf2qGKP.IKeH1s4RIq','dasdasdawderesdasdsa',NULL,NULL,9,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (14,'OSOBA_FIZYCZNA','dddddawdaaa@sadb.pl','$2a$10$TIsbw/6vHX/OqiK.EZfLB.eCkGiEKRk2RK3IKA1uyRn4vs1HlmQm.','dasdasdawderesdasdsadawd',NULL,NULL,10,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (15,'OSOBA_FIZYCZNA','aiofhseofuhsefopilsejfoise@sdfg.fsd','$2a$10$fO4qkXRsEIF8v/22/XUZf.zcsy6FCK18QfKIFkljswqj8TnYZqcbO','ewsfesfartdd',NULL,NULL,11,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (16,'OSOBA_FIZYCZNA','dgfhfdghdfthtfdhfdhg@rgdthftdhfdth.htfh','$2a$10$u3oyVyLCB2X2Cb6S3J36xe099MF7CFvegh7T37dHQpfg1Venh1MDG','htfdhtfhtf',NULL,NULL,12,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (17,'OSOBA_FIZYCZNA','dgfbhdfgdrsgd@fsdfsdfas.fsdf','$2a$10$KjQIM2NYEUe1sbl7kK/mEOYzIZ5Spu4iuXQmISh521jolUYXlzLxa','tfhgd',NULL,NULL,13,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (18,'FIRMA','dgfbhdfgdrsgdjtygjtgf@fsdfsdfas.fsdf','$2a$10$Dr5Xlntwvwvf8go6Xj92D.kX7V01Fj63k4yeisU53nEf.GH1gPYaW','tfhgdfyjugyu',NULL,3,NULL,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (19,'FIRMA','dgfbhdfgdrsgdjtygjtgf@fsdgghfgfsdfas.fsdf','$2a$10$B0jigo8ZzOfM2TT0jCWntO78U35bpDz/scXfoDfe.5bEV6WnHz/ha','tfhgdfyjugyuhgf',NULL,4,NULL,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (20,'FIRMA','recznikizczkiago@gmail.com','$2a$10$ILFES0o4CL2m4VUJTl5ujeuJIHRvptcYnBBU0N95m1lC5xY/9Sjne','kkkkkkkkkk',NULL,5,NULL,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (21,'FIRMA','sergasrfsd@fsdfdsaf.fdasfdas','$2a$10$.gxGvj7VaGz3xmFygqNU0.htlBGa2hg2dE37oQZfO3sW76vZbWCe.','fsdafesdrgtdthgtsdf',NULL,6,NULL,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (22,'FIRMA','sergasrfhgfhgfsd@fsdfdsaf.fdasfdas','$2a$10$7rMvvvAfP3QGXTfhV.sQ1.eZRMKZlcDwp7v8Bqe7iIxN/aMq9S9zm','fsdafesdrgtdthgtsfghgfhdf',NULL,7,NULL,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (23,'FIRMA','gxfgrdgrdgfcg@grfsdgdfg.ghdrgsdrgrdfgtfdh','$2a$10$ZVkKoSQCC8BSvCu2hQh6t.wf17HEE8F9HTsX3H54xYFeLsCdI8eTq','ftdhdygfhjgyjf',NULL,8,NULL,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (24,'OSOBA_FIZYCZNA','ghjfghjygfj@gdfsg.gfgdf','$2a$10$kqIQ/WNi.MN/hRL8JOJwIuH8QlQqCX16AuF5HrFT49r0.lkLfGYxu','hydtfhragdsrgdt',NULL,NULL,14,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (25,'OSOBA_FIZYCZNA','ex@gmail.com','$2a$10$SPCjAZ3lTEmLtd5DLFmU7OsMBTjiuC.0fLOssg3coITAbmCc0oaKK','test',NULL,NULL,15,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (26,'OSOBA_FIZYCZNA','example_test@gmail.com','$2a$10$un6baXdtQMhb.1hVsdvuE.oo.7frJZB7IWZrMib0cWH2V4bbEf/Xm','test21431231',NULL,NULL,16,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (27,'OSOBA_FIZYCZNA','example_tes1t@gmail.com','$2a$10$uTMaqpNS9xAH/LDdMEeAbu6KoEXoYgnz/9mvg5MdkEB3PVRfaFbyq','test214312311',NULL,NULL,17,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (43,'OSOBA_FIZYCZNA','minox67922@dwakm.com','$2a$10$NMrWAlOH8tsnI.XhtfBQBOj36MT8RN.Np6xk997p2luKYPFzZc0Eu','minors',NULL,NULL,34,'1',7);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (44,'OSOBA_FIZYCZNA','nowy@test.pl','Admin1234!','nowy_user',NULL,NULL,NULL,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (46,'OSOBA_FIZYCZNA','nowy1@example.com','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjRPN.oxLhu/1U6eb8.y.yGZ.w/2w6.!','nowy_user1',NULL,NULL,NULL,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (54,'OSOBA_FIZYCZNA','Yuli_yanti1986@yahoo.com','$2a$10$w2qZkHGVOwTfXUCtPLhJm./rI/GS1sHLBZ09.cGFC0BAHOMOyc6WO','Yuli',NULL,NULL,42,'1',NULL);
INSERT INTO `` (`id`,`account_type`,`email`,`password_hash`,`username`,`admin_data_id`,`business_data_id`,`personal_data_id`,`enabled`,`wallet_id`) VALUES (70,'OSOBA_FIZYCZNA','lojafe3201@bablace.com','$2a$10$ZTnCtHnixKEwCF02QmVn3OMf9PVsYjtluRYKH/pZX540UXGO9c6q.','Lojafe',NULL,NULL,58,'1',23);

Drop

DROP TABLE IF EXISTS `transaction_history`;
DROP TABLE IF EXISTS `shop_order`;
DROP TABLE IF EXISTS `sessions`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `personal_data`;
DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `oferta`;
DROP TABLE IF EXISTS `gift_card_data`;
DROP TABLE IF EXISTS `countries`;
DROP TABLE IF EXISTS `contact_person`;
DROP TABLE IF EXISTS `confirmation_tokens`;
DROP TABLE IF EXISTS `card_data`;
DROP TABLE IF EXISTS `business_data`;
DROP TABLE IF EXISTS `bank_account_data`;
DROP TABLE IF EXISTS `auction`;
DROP TABLE IF EXISTS `address`;
DROP TABLE IF EXISTS `user_data`;


Project created for educational purposes, demonstrating complex business logic implementation in Spring Boot.