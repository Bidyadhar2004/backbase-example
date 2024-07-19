# Account Service

## Overview

The Account Service is a RESTful API that manages account information. It provides endpoints to create, update, retrieve, and list accounts with support for pagination. This service is built using Spring Boot and integrates with a inmemory H2 database.

## Features

- **Create Account**: Create a new account.
- **Update Account**: Update existing account information.
- **Get Account**: Retrieve account details by account number or name.
- **Get All Accounts**: List all accounts with pagination support.

Note: Above REST end points are secured. So, accessing end points User needs to be authenticated first. For this have provided below additional endpoints , user has to log-in first and then access above end points.

- **Login User** :  User should login by provided JWT token.
- **Log out USer**: User should log out after complete their operation.


## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- A inmemory database (e.g., H2)

### Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/Bidyadhar2004/backbase-example.git
   cd accountservice
2. **Build the Project**
   mvn clean install
3. **Run the Application**
   mvn clean install
4. **java -jar target/account-service.jar**
   java -jar target/accountservice-0.0.1-SNAPSHOT.jar
5. **Swagger UI end Point URL for testing**
   http://localhost:8080/swagger-ui/index.html

