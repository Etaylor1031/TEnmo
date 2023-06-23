package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                consoleService.printMessage("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        consoleService.printMessage("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            consoleService.printMessage("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                consoleService.printMessage("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        AccountService accountService = new AccountService(API_BASE_URL, currentUser);
        consoleService.printBalance(accountService.getBalance());
    }

    private void viewTransferHistory() {
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);
        consoleService.printTransferHistory(currentUser.getUser().getUsername(), transferService.getTransfers());
        int transferId = consoleService.promptForTransferId();
        consoleService.printTransferDetails(transferService.getTransferDetails(transferId));
    }
    private void viewPendingRequests() {
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);
        consoleService.printPendingTransfers(currentUser.getUser().getUsername(), transferService.getPendingTransfers());
        int transferId = consoleService.promptForTransferIdToApproveOrReject();
        if(transferId == 0)
            return;

        int choice = -1;
        while (choice != 0) {
            consoleService.printApproveOrRejectMenu();
            choice = consoleService.promptForApproveOrRejectMenu();
            if (choice == 1) {
                transferService.updateTransfer(transferId, TransferStatus.APPROVED);
                break;
            } else if (choice == 2) {
                transferService.updateTransfer(transferId, TransferStatus.REJECTED);
                break;
            } else if (choice == 0) {
                continue;
            } else {
                consoleService.printMessage("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void sendBucks() {
        AccountService accountService = new AccountService(API_BASE_URL, currentUser);
        BigDecimal balance = accountService.getBalance();
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);
        consoleService.printUsers(transferService.getUsers());
        Transfer transferEnteredByUser = consoleService.promptForSendTransferData(currentUser.getUser().getId(), balance);
        Transfer transfer = transferService.sendBucks(transferEnteredByUser);
        consoleService.printTransferDetails(transfer);
    }

    private void requestBucks() {
        AccountService accountService = new AccountService(API_BASE_URL, currentUser);
        BigDecimal balance = accountService.getBalance();
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);
        consoleService.printUsers(transferService.getUsers());
        Transfer transferEnteredByUser = consoleService.promptForRequestTransferData(currentUser.getUser().getId(), balance);
        Transfer transfer = transferService.requestBucks(transferEnteredByUser);
        consoleService.printTransferDetails(transfer);
    }
}
