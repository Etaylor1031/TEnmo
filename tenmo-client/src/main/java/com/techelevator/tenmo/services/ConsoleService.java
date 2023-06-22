package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
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
        System.out.println("Your current account balance is: $" + balance);
    }

    public void printTransferHistory(String currentUserName, Transfer[] transfers) {
        final String ID = "ID";
        final String FROM_TO = "From/To";
        final String AMOUNT = "Amount";

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
    }

    public void printTransferDetails(Transfer transfer) {
        System.out.println("-------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------------------");
        System.out.printf("Id: %d\n", transfer.getTransferId());
        System.out.printf("From: %s\n", transfer.getFromUserName());
        System.out.printf("To: %s\n", transfer.getToUserName());
        System.out.printf("Type: %s\n", transfer.getTransferTypeDescription());
        System.out.printf("Status: %s\n", transfer.getTransferStatusDescription());
        System.out.printf("Amount: $%s\n", transfer.getTransferAmount());
    }

    public void printUsers(UserPojo[] users) {
        final String ID = "ID";
        final String NAME = "Name";

        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.printf("%-10s %-20s\n", ID, NAME);
        System.out.println("-------------------------------------------");
        for (UserPojo user : users) {
            System.out.printf("%-10d %-20s\n", user.getUserId(), user.getUsername());
        }
    }

    public int promptForTransferId() {
        System.out.print("Please enter Transfer ID to view details: ");
        int transferId = scanner.nextInt();
        return transferId;
    }

    public Transfer promptForTransferData(int fromUserId) {
        // Get user input
        System.out.print("Enter ID of user you are sending to: ");
        int toUserId = scanner.nextInt();
        System.out.print("Enter amount: ");
        BigDecimal amount = scanner.nextBigDecimal();

       return new Transfer(fromUserId, toUserId, amount);
    }

}
