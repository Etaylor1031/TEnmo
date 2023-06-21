package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private AccountDao accountDao;
    private UserDao userDao;
    public TransferController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/balance/{userId}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int userId) {
        BigDecimal balance = accountDao.findBalanceByUserId(userId);
        if(balance == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Balance not found");
        } else {
            return balance;
        }
    }

    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferDetails(@PathVariable int transferId) {
        Transfer transfer = accountDao.getTransferDetails(transferId);
        if(transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found");
        } else {
            return transfer;
        }
    }

    @RequestMapping(path = "/transfers/{userId}", method = RequestMethod.GET)
    public List<Transfer> listTransfersByUserId(@PathVariable int userId) {
        return accountDao.getTransfersByUserId(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public Transfer send(@RequestBody Transfer transfer) {
        String validationFailure = checkValidTransaction(transfer);
        if (validationFailure != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validationFailure);
        }


        accountDao.subtractBalance(transfer.getFromUser(), transfer.getAmount());
        accountDao.addBalance(transfer.getToUser(), transfer.getAmount());

        return accountDao.saveTransfer(transfer);
    }

    private String checkValidTransaction(Transfer transfer) {
        if(transfer.getFromUser() == transfer.getToUser()) {
            return "Can't send to yourself";
        }

        if(transfer.getAmount().compareTo(accountDao.findBalanceByUserId(transfer.getFromUser())) == 1) {
            return "Insufficient funds";
        }


        if(transfer.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            return "Invalid Transfer Amount";
        }

        return null;
    }

    /*
    @RequestMapping(path = "/accounts", method = RequestMethod.GET)
    public List<Account> listAccounts() {
        return accountDao.getAllAccounts();
    }
     */

}
