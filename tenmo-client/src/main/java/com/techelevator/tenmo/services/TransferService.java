package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private final String API_BASE_URL;
    private final AuthenticatedUser currentUser;
    private final RestTemplate restTemplate;

    public TransferService(String apiUrl, AuthenticatedUser currentUser) {
        this.API_BASE_URL = apiUrl;
        this.currentUser = currentUser;
        this.restTemplate = new RestTemplate();
    }

    public void viewCurrentBalance() {
        AccountService accountService = new AccountService(API_BASE_URL, currentUser);
        BigDecimal balance = accountService.getBalance();
        System.out.println("Your current balance is: $" + balance);
    }

    public Transfer getTransferDetails(int transferId) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfers/details/"  + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public Transfer[] getTransfers() {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(
                    API_BASE_URL + "transfers/" + currentUser.getUser().getId(),
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class
            );
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return transfers;
    }

    public void viewPendingRequests() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity entity = new HttpEntity(headers);

            ResponseEntity<Transfer[]> response = restTemplate.exchange(
                    API_BASE_URL + "transfers/pending",
                    HttpMethod.GET,
                    entity,
                    Transfer[].class
            );

            Transfer[] transfers = response.getBody();
            System.out.println("Pending Requests:");
            for (Transfer transfer : transfers) {
                System.out.println(transfer.toString());
            }
        } catch (RestClientException e) {
            System.out.println("Failed to retrieve pending requests. Please try again.");
        }
    }

    public void sendBucks() {
        Scanner scanner = new Scanner(System.in);

        // Get user input
        System.out.print("Enter recipient's user ID: ");
        int toUserId = scanner.nextInt();
        System.out.print("Enter amount to send: ");
        BigDecimal amount = scanner.nextBigDecimal();

        // Create the transfer object
        Transfer transfer = new Transfer(currentUser.getUser().getId(), toUserId, amount);

        // Send the transfer
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

            ResponseEntity<Transfer> response = restTemplate.exchange(
                    API_BASE_URL + "transfers",
                    HttpMethod.POST,
                    entity,
                    Transfer.class
            );

            Transfer sentTransfer = response.getBody();
            if (sentTransfer != null) {
                System.out.println("Transfer sent successfully!");
            } else {
                System.out.println("Failed to send transfer. Please try again.");
            }
        } catch (RestClientException e) {
            System.out.println("Failed to send transfer. Please try again.");
        }
    }

    public void requestBucks() {
        Scanner scanner = new Scanner(System.in);

        // Get user input
        System.out.print("Enter sender's user ID: ");
        int fromUserId = scanner.nextInt();
        System.out.print("Enter amount to request: ");
        BigDecimal amount = scanner.nextBigDecimal();

        // Create the transfer object
        Transfer transfer = new Transfer(fromUserId, currentUser.getUser().getId(), amount);

        // Send the transfer request
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

            ResponseEntity<Transfer> response = restTemplate.exchange(
                    API_BASE_URL + "transfers/request",
                    HttpMethod.POST,
                    entity,
                    Transfer.class
            );

            Transfer requestedTransfer = response.getBody();
            if (requestedTransfer != null) {
                System.out.println("Transfer request sent successfully!");
            } else {
                System.out.println("Failed to send transfer request. Please try again.");
            }
        } catch (RestClientException e) {
            System.out.println("Failed to send transfer request. Please try again.");
        }
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
}
