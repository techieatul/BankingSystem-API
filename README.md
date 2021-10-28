# Banking System API

> REST API created in Spring Boot to simulate a Banking System





This REST API allows you to recreate a banking system with the following functionalities:

- Make transactions between different types of accounts
- Make transactions between a Third Party and an different types of accounts
- Check your accounts balance
- Automatically update balance generating interests and fees if applicable

## Setup

To run this project locally do the following after cloning the project:

1. Create the database: `bankingsys`
2. Edit the application.properties file in both the /main folder and the /test folder  and insert your own credentials
3. Launch the application by running `mvn spring-boot:run` in your terminal (point your terminal towards your project folder) 



## API documentation

Depending on the route you are trying to access, you will need different privileges. These are the routes you can try and the permissions you will need to perform the action

### Admin routes

| Method | Route                               | Response                                    | User Role |
| ------ | ----------------------------------- | ------------------------------------------- | --------- |
| GET    | /admin/account/id/{id}              | Get account by account ID.                  | Admin     |
| GET    | /admin/account/{id}/balance         | Get balance by account ID.                  | Admin     |
| GET    | /admin/account/{id}/accounts        | Get all Accounts of a user by User ID.      | Admin     |
| POST   | admin/account/{id}/balance          | Update Balance of an account by Account ID. | Admin     |
| POST   | /admin/create-account-holder        | Create a new Accountholder.                 | Admin     |
| GET    | /admin/account-holders              | Get all Account Holders                     | Admin     |
| POST   | /admin/checking-account/new/        | Create a new Checking Account               | Admin     |
| GET    | /admin/checking-accounts            | Get all checking Accounts                   | Admin     |
| POST   | /credit-card/new                    | Create a new Credit Card                    | Admin     |
| GET    | /credit-cards                       | Get all Credit Cards                        | Admin     |
| POST   | /admin/savings-account/new          | Create new Savings Account                  | Admin     |
| GET    | /admin/savings-accounts             | Get All Savings Accounts                    | Admin     |
| POST   | /admin/student-checking-account/new | Create new Checking Account                 | Admin     |
| GET    | /admin/student-checking-accounts    | Get all student checking Accounts           | Admin     |

### User routes

| Method | Route                     | Response                            | User Role     |
| ------ | ------------------------- | ----------------------------------- | ------------- |
| GET    | /my-accounts/{id}/balance | Get Balance                         | AccountHolder |
| POST   | /transfer                 | Make a transaction between accounts | AccountHolder |

### User routes

| Method | Route                     | Response                            | User Role     |
| ------ | ------------------------- | ----------------------------------- | ------------- |
| GET    | /my-accounts/{id}/balance | Get Balance                         | AccountHolder |
| POST   | /transfer                 | Make a transaction between accounts | AccountHolder |



## Tech Stack

[Spring](https://spring.io/)

[MySQL](https://www.mysql.com/)




