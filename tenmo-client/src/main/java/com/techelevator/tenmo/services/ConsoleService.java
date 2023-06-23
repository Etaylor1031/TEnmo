package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.pojos.UserPojo;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printBalance(BigDecimal balance) {
        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Your current account balance is: $" + balance);
        System.out.println("-------------------------------------------");
    }

    public void printTransferHistory(String currentUserName, Transfer[] transfers) {
        final String ID = "ID";
        final String FROM_TO = "From/To";
        final String AMOUNT = "Amount";

        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Current User: " + currentUserName);
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.printf("%-10s %-20s %-20s\n", ID, FROM_TO, AMOUNT);
        System.out.println("-------------------------------------------");
        for (Transfer transfer : transfers) {
            String fromTo = null;
            if(currentUserName.equals(transfer.getFromUserName()))
                fromTo = "To: " + transfer.getToUserName();
            else if(currentUserName.equals(transfer.getToUserName()))
                fromTo = "From: " + transfer.getFromUserName();

            //System.out.printf("%-10d From: %-15s To: %-15s $ %-10s\n", transfer.getTransferId(), transfer.getFromUserName(), transfer.getToUserName(), transfer.getTransferAmount());
            System.out.printf("%-10d %-20s $ %-20s\n", transfer.getTransferId(), fromTo, transfer.getTransferAmount());
        }
        System.out.println("-------------------------------------------");
    }

    public void printPendingTransfers(String currentUserName, Transfer[] transfers) {
        final String ID = "ID";
        final String FROM_TO = "To";
        final String AMOUNT = "Amount";

        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Current User: " + currentUserName);
        System.out.println("-------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.printf("%-10s %-20s %-20s\n", ID, FROM_TO, AMOUNT);
        System.out.println("-------------------------------------------");
        for (Transfer transfer : transfers) {
            System.out.printf("%-10d %-20s $ %-20s\n", transfer.getTransferId(), transfer.getToUserName(), transfer.getTransferAmount());
        }
        System.out.println("-------------------------------------------");
    }

    public void printTransferDetails(Transfer transfer) {
        if(transfer == null) {
            System.out.println("Transfer is empty");
            return;
        }

        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------------------");
        System.out.printf("Id: %d\n", transfer.getTransferId());
        System.out.printf("From: %s\n", transfer.getFromUserName());
        System.out.printf("To: %s\n", transfer.getToUserName());
        System.out.printf("Type: %s\n", transfer.getTransferTypeDescription());
        System.out.printf("Status: %s\n", transfer.getTransferStatusDescription());
        System.out.printf("Amount: $%s\n", transfer.getTransferAmount());
        System.out.println("-------------------------------------------");
    }

    public void printUsers(UserPojo[] users) {
        final String ID = "ID";
        final String NAME = "Name";

        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.printf("%-10s %-20s\n", ID, NAME);
        System.out.println("-------------------------------------------");
        for (UserPojo user : users) {
            System.out.printf("%-10d %-20s\n", user.getUserId(), user.getUsername());
        }
        System.out.println("-------------------------------------------");
    }

    public int promptForTransferId(Transfer[] transfers) {
        while(true) {
            System.out.print("Please enter Transfer ID to view details (0 to cancel): ");
            int transferId = scanner.nextInt();
            if(transferId == 0)
                return 0;

            if (!transferIdExists(transfers, transferId)) {
                System.out.println("Transfer ID does not exist. Please enter a valid Transfer ID.");
            }

            else
                return transferId;
        }
    }

    public Transfer promptForSendTransferData(int fromUserId, BigDecimal balance, UserPojo[] users) {
        int toUserId;
        BigDecimal amount;

        while(true) {
            System.out.print("Enter ID of user you are sending to (0 to cancel): ");
            toUserId = scanner.nextInt();
            if(toUserId == 0)
                return null;

            if(fromUserId == toUserId) {
                System.out.println("You cannot send to yourself. Please enter a valid User ID.");
                continue;
            }

            if(!userIdExists(users, toUserId))
                System.out.println("User does not exist. Please enter a valid User ID.");

            else
                break;

        }

        while(true) {
            System.out.print("Enter amount: ");
            amount = scanner.nextBigDecimal();
            if(amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                System.out.println("Invalid Transfer Amount. Please enter amount greater than 0.");
            }

            else if(amount.compareTo(balance) > 0) {
                System.out.println("Insufficient funds. Please enter an amount less than your balance.");
            }

            else {
                break;
            }
         }

       return new Transfer(TransferType.SEND, TransferStatus.APPROVED, fromUserId, toUserId, amount);
    }

    public Transfer promptForRequestTransferData(int toUserId, BigDecimal balance, UserPojo[] users) {
        int fromUserId;
        BigDecimal amount;

        while(true) {
            System.out.print("Enter ID of user you are requesting from (0 to cancel): ");
            fromUserId = scanner.nextInt();
            if(fromUserId == 0)
                return null;

            if(fromUserId == toUserId) {
                System.out.println("You cannot request to yourself. Please enter a valid User ID.");
                continue;
            }


            if(!userIdExists(users, fromUserId)) {
                System.out.println("User does not exist. Please enter a valid User ID.");
            }

            else
                break;
        }

        while(true) {
            System.out.print("Enter amount: ");
            amount = scanner.nextBigDecimal();
            if(amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                System.out.println("Invalid Transfer Amount. Please enter amount greater than 0.");
            }

            else if(amount.compareTo(balance) > 0) {
                System.out.println("Insufficient funds. Please enter an amount less than your balance.");
            }

            else {
                break;
            }
        }

        return new Transfer(TransferType.REQUEST, TransferStatus.PENDING, fromUserId, toUserId, amount);
    }

    public int promptForTransferIdToApproveOrReject(Transfer[] pendingTransfers) {
        while(true) {
            System.out.print("Please enter Transfer ID to approve/reject (0 to cancel): ");
            int transferId = scanner.nextInt();
            if(transferId == 0)
                return 0;

            if (!transferIdExists(pendingTransfers, transferId)) {
                System.out.println("Transfer ID does not exist. Please enter a valid Transfer ID.");
            }

            else
                return transferId;
        }
    }

    public void printApproveOrRejectMenu() {
        System.out.println("-------------------------------------------");
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Exit");
        System.out.println("-------------------------------------------");
    }

    public int promptForApproveOrRejectMenu() {
        System.out.print("Please choose an option: ");
        int choice = scanner.nextInt();
        return choice;
    }


    private boolean userIdExists(UserPojo[] users, int userId) {
        for(UserPojo user : users) {
            if(user.getUserId() == userId)
                return true;
        }

        return false;
    }

    private boolean transferIdExists(Transfer[] transfers, int transferId) {
        for(Transfer transfer : transfers) {
            if(transfer.getTransferId() == transferId)
                return true;
        }

        return false;
    }
}
