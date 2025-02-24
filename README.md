# User Manager

**User Manager** is a Spring Boot REST API for managing user data. It provides endpoints for CRUD operations, user authentication, data validation, and more.

## Table of Contents
1. [Project Description](#project-description)
2. [Technologies Used](#technologies-used)
3. [Setup Instructions](#setup-instructions)
4. [API Endpoints](#api-endpoints)
5. [Database Configuration](#database-configuration)
6. [Running the Application](#running-the-application)
7. [Contributing](#contributing)
8. [License](#license)

## Project Description
Készíts egy Spring boot alapú REST API-t, amely ügyfél adatok tárolására és kezelésére alkalmas.

- Az alkalmazás rendelkezzen adatbázissal, amelyet maga hoz létre és tölt fel tesztadatokkal;
- Az ügyfél entitás rendelkezzen a valós életben szokványos tulajdonságokkal;
- A REST API nyújtson CRUD metódusokat;
- Az adatok legyenek validálva;
- A REST API rendelkezzen authentikációval;
- Legyen egy végpont, amely visszaadja az ügyfelek átlagéletkorát. A számítást a JPA segítségével (query-vel) valósítsd meg;
- Legyen egy végpont, amely visszaad minden 18 és 40 év közötti ügyfelet. A számítást streaming segítségével valósítsd meg;
- Bizonyítsd az alkalmazás helyes működését!

## Technologies Used
- **Java 23**
- **Spring Boot 3.4.3**
  - Spring Boot Starter Data JPA
  - Spring Boot Starter JDBC
  - Spring Boot Starter Security
  - Spring Boot Starter Web
- **H2 Database (for runtime)**
- **MySQL (for production)**
- **Lombok**
- **Maven**

## Setup Instructions
1. **Clone the repository:**
   ```sh
   git clone https://github.com/dandobai/user-handler.git
   cd user-manager
2. **Configure MySQL Database:**
- Update the application.properties file with your MySQL database connection details.
3. **Build the project**
    ```sh
    mvn clean install
4. **Run the application**
    ```sh
    mvn spring-boot:run

## API Endpoints  

The following endpoints are available for interacting with the User Manager API:  

### User Management  
- **GET** `/users` - Retrieve all users  
- **GET** `/users/{id}` - Retrieve a user by ID  
- **POST** `/users` - Create a new user  
- **PUT** `/users/{id}` - Update an existing user  
- **DELETE** `/users/{id}` - Delete a user by ID  

### Statistical Queries  
- **GET** `/users/average-age` - Retrieve the average age of all users  
- **GET** `/users/age-range` - Retrieve users aged between 18 and 40  

## Database Configuration  

### H2 Database (for testing)  
No additional configuration needed. The H2 database is used for runtime by default.  

### MySQL Database (for production)  
Update your `application.properties` file with your MySQL database connection details:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yourdatabase
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

## Running the Application

To run the application, use the following Maven command:

```sh
mvn spring-boot:run

## Contributing

Contributions are welcome! Please submit a pull request or open an issue to discuss any changes.

## License

This project is licensed under the **GNU General Public License (GPL)** - see the LICENSE file for details.