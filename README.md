# TEnmo RESTful API Server and Command-Line Application

TEnmo is an online payment service that enables users to transfer a virtual currency called "TE bucks" among friends. This project consists of a RESTful API server and a command-line application that facilitate various operations, including user registration, authentication, balance inquiries, and money transfers.

## Use Cases

The TEnmo application supports the following use cases:

1. **User Registration**: Users can register by providing a username and password. Upon registration, a new user is assigned an initial balance of 1,000 TE Bucks.

2. **User Login**: Users can log in using their registered username and password. Successful login generates an authentication token that must be included in subsequent interactions with the system.

3. **Account Balance Inquiry**: Authenticated users can view their current account balance.

4. **TE Bucks Transfer**: Authenticated users can send a specific amount of TE Bucks to another registered user. Users can select a recipient from a list, and the transfer deducts the amount from the sender's account and adds it to the receiver's account.

5. **Transfer Viewing**: Authenticated users can view the transfers they have sent or received. Transfers are listed with their IDs, sender/receiver names, and transfer amounts.

6. **Transfer Details**: Authenticated users can retrieve detailed information about any transfer by providing the transfer ID.

## Database Schema

The TEnmo application utilizes a PostgreSQL database with the following schema:

- **tenmo_user**: Stores user login information.
  - `user_id`: Unique identifier of the user.
  - `username`: User's username for login.
  - `password_hash`: Hashed version of the user's password.
  - `role`: User's role within the system.

- **account**: Stores user account information.
  - `account_id`: Unique identifier of the account.
  - `user_id`: Foreign key referencing the `tenmo_user` table, identifies the account owner.
  - `balance`: The amount of TE bucks in the account.

- **transfer_type**: Stores the types of transfers.
  - `transfer_type_id`: Unique identifier of the transfer type.
  - `transfer_type_desc`: Description of the transfer type.

- **transfer_status**: Stores the statuses of transfers.
  - `transfer_status_id`: Unique identifier of the transfer status.
  - `transfer_status_desc`: Description of the transfer status.

- **transfer**: Stores information about TE bucks transfers.
  - `transfer_id`: Unique identifier of the transfer.
  - `transfer_type_id`: Foreign key referencing the `transfer_type` table, identifies the transfer type.
  - `transfer_status_id`: Foreign key referencing the `transfer_status` table, identifies the transfer status.
  - `account_from`: Foreign key referencing the `account` table, identifies the sender's account.
  - `account_to`: Foreign key referencing the `account` table, identifies the receiver's account.
  - `amount`: The amount of TE bucks being transferred.

## Getting Started

1. Make sure you have a PostgreSQL database named `tenmo` set up and running.
2. Start the TEnmo RESTful API server.
3. Run the TEnmo command-line application.

The command-line application interacts with the TEnmo RESTful API server by making HTTP requests to perform different operations. The API server provides endpoints for user registration, login, account balance inquiry, TE bucks transfer, and transfer retrieval.

You can refer to the API documentation for details on the available endpoints and their usage.

Please note that the TEnmo RESTful API server and command-line application are separate components that communicate with each other over HTTP. The API server needs to be running and accessible for the command-line application to function properly.
