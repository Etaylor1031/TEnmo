package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.pojos.UserPojo;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
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

    public UserPojo[] getUsers() {
        UserPojo[] users = null;
        try {
            ResponseEntity<UserPojo[]> response = restTemplate.exchange(
                    API_BASE_URL + "users",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    UserPojo[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to get Users.");
            BasicLogger.log(e.getMessage());
        }

        return users;
    }

    public Transfer getTransferDetails(int transferId) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    API_BASE_URL + "transfers/details/"  + transferId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to get Transfer Details.");
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
                    Transfer[].class);

            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to get Transfers.");
            BasicLogger.log(e.getMessage());
        }

        return transfers;
    }

    public Transfer[] getPendingTransfers() {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(
                    API_BASE_URL + "transfers/pending/" + currentUser.getUser().getId(),
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class);

            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to retrieve pending requests. Please try again.");
            BasicLogger.log(e.getMessage());
        }

        return transfers;
    }

    public Transfer sendBucks(Transfer newTransfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(newTransfer);
        Transfer returnedTransfer = null;

        try {
            returnedTransfer = restTemplate.postForObject(API_BASE_URL + "send", entity, Transfer.class);

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to send transfer. Please try again.");
            BasicLogger.log(e.getMessage());
        }

        return returnedTransfer;
    }

    public Transfer requestBucks(Transfer newTransfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(newTransfer);
        Transfer returnedTransfer = null;

        try {
            returnedTransfer = restTemplate.postForObject(API_BASE_URL + "request", entity, Transfer.class);

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to request transfer. Please try again.");
            BasicLogger.log(e.getMessage());
        }

        return returnedTransfer;
    }

    public boolean updateTransfer(int transferId, int transferStatus) {
        Transfer updatedTransfer = getTransferDetails(transferId);
        updatedTransfer.setTransferStatus(transferStatus);
        updatedTransfer.setTransferStatusDescription(TransferStatus.textTransferStatus(updatedTransfer.getTransferStatus()));

        HttpEntity<Transfer> entity = makeTransferEntity(updatedTransfer);
        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL + "transfers/" + transferId, entity);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Failed to update transfer");
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
}
