TEnmo RESTful API Server and Command-Line Application
TEnmo is an online payment service that allows users to transfer a virtual currency called "TE bucks" between friends. This project involves the development of a RESTful API server and a command-line application to facilitate various operations related to user registration, authentication, balance inquiries, and money transfers.

Use Cases
The following use cases are implemented in the TEnmo application:

User Registration: Users can register themselves by providing a username and password. A new registered user is assigned an initial balance of 1,000 TE Bucks.

User Login: Users can log in using their registered username and password. Upon successful login, an authentication token is generated, which needs to be included in subsequent interactions with the system.

Account Balance Inquiry: Authenticated users can view their current account balance.

Sending TE Bucks: Authenticated users can send a specific amount of TE Bucks to another registered user. Users can choose from a list of available recipients, and the transfer deducts the amount from the sender's account and adds it to the receiver's account.

Viewing Transfers: Authenticated users can view the transfers they have sent or received. The transfers are listed with their IDs, sender/receiver names, and transfer amounts.

Transfer Details: Authenticated users can retrieve detailed information about any transfer by providing the transfer ID.

Database Schema
The TEnmo application uses a PostgreSQL database with the following schema:

tenmo_user: Stores user login information.

user_id: Unique identifier of the user.
username: User's username for login.
password_hash: Hashed version of the user's password.
role: User's role within the system.
account: Stores user accounts and balances.

account_id: Unique identifier of the account.
user_id: Foreign key referencing the tenmo_user table.
balance: Amount of TE bucks in the account.
transfer_type: Stores types of transfers.

transfer_type_id: Unique identifier of the transfer type.
transfer_type_desc: Description of the transfer type.
transfer_status: Stores statuses of transfers.

transfer_status_id: Unique identifier of the transfer status.
transfer_status_desc: Description of the transfer status.
transfer: Stores transfer details.

transfer_id: Unique identifier of the transfer.
transfer_type_id: Foreign key referencing the transfer_type table.
transfer_status_id: Foreign key referencing the transfer_status table.
account_from: Foreign key referencing the account table for the sender's account.
account_to: Foreign key referencing the account table for the receiver's account.
amount: Amount of TE bucks transferred.
How to Set Up the Database
To set up the database for the TEnmo application:

Create a new PostgreSQL database named tenmo.
Run the tenmo.sql script provided in the database folder to create the necessary tables and seed initial data.
Authentication
The user registration and authentication functionality for the system has already been implemented. Upon successful login, an authentication token is generated for the user, which needs to be included in subsequent interactions with the system as a header.

Command-Line Application
The command-line application provides an interface for users to interact with the TEnmo system. It displays a menu of options and prompts the user for input to perform different operations, such as viewing account balance, sending TE bucks, and viewing transfers.

To run the command-line application:

Set up the database and ensure it is running.
Start the TEnmo RESTful API server.
Run the TEnmo command-line application.
The command-line application interacts with the TEnmo RESTful API server by making HTTP requests to perform different operations. The API server provides endpoints for user registration, login, account balance inquiry, TE bucks transfer, and transfer retrieval.

You can refer to the API documentation for details on the available endpoints and their usage.

Please note that the TEnmo RESTful API server and command-line application are separate components that communicate with each other over HTTP. The API server needs to be running and accessible for the command-line application to function properly.
