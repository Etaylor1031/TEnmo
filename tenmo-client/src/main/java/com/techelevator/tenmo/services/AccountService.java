package com.techelevator.tenmo.services;


//  Import libraries / modules
import java.math.BigDecimal;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.techelevator.tenmo.model.AuthenticatedUser;

public class AccountService {


    //instance variable declaration
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    //constructor
    public AccountService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        baseUrl= url;
    }

    // Gets the user balance and prints out the amount
    public BigDecimal getBalance() {
        BigDecimal balance = new BigDecimal(0);
        try {
            balance = restTemplate.exchange(baseUrl + "balance/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
            System.out.println("The Current Balance is : $" + balance);
        } catch (RestClientException e) {
            System.out.println("Error Please Try Again ");
        }
        return balance;
    }


    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }





}
