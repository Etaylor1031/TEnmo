package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.pojos.UserPojo;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
//@PreAuthorize("isAuthenticated()")
public class TransferController {
    private AccountDao accountDao;
    public TransferController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/balance/{id}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int id) {
        BigDecimal balance = accountDao.getBalanceByUserId(id);
        if(balance == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Balance not found");
        } else {
            return balance;
        }
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<UserPojo> listUsers() {
        return accountDao.getUsers();
    }

    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public List<Transfer> listTransfers(@PathVariable int id) {
        return accountDao.getTransfersByUserId(id);
    }

    @RequestMapping(path = "transfers/details/{id}", method = RequestMethod.GET)
    public Transfer showTransferDetails(@PathVariable int id) {
        return accountDao.getTransferDetails(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public Transfer send(@RequestBody Transfer transfer) {
        String validationFailure = checkValidTransaction(transfer);
        if (validationFailure != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validationFailure);
        }

        accountDao.saveTransfer(transfer);
        accountDao.subtractBalance(transfer.getFromUser(), transfer.getTransferAmount());
        accountDao.addBalance(transfer.getToUser(), transfer.getTransferAmount());

        return transfer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/request", method = RequestMethod.POST)
    public Transfer request(@RequestBody Transfer transfer) {
        return accountDao.saveTransfer(transfer);
    }

    @RequestMapping(path = "/transfers/pending/{id}", method = RequestMethod.GET)
    public List<Transfer> listPendingTransfers(@PathVariable int id) {
        return accountDao.getPendingTransfersByUserId(id);
    }

    @RequestMapping(path = "/transfers/{transferId}", method = RequestMethod.PUT)
    public Transfer update(@RequestBody Transfer transfer, @PathVariable int transferId) {
        Transfer updatedTransfer= accountDao.updateTransferStatus(transfer, transferId);

        if(transfer.getTransferStatus() == TransferStatus.APPROVED) {
            accountDao.subtractBalance(transfer.getFromUser(), transfer.getTransferAmount());
            accountDao.addBalance(transfer.getToUser(), transfer.getTransferAmount());
        }

        if (updatedTransfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer Not Found");
        } else {
            return transfer;
        }
    }

    private String checkValidTransaction(Transfer transfer) {
        if(transfer.getFromUser() == transfer.getToUser()) {
            return "Can't send to yourself";
        }

        if(transfer.getTransferAmount().compareTo(accountDao.getBalanceByUserId(transfer.getFromUser())) > 0) {
            return "Insufficient funds";
        }

        if(transfer.getTransferAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            return "Invalid Transfer Amount";
        }

        return null;
    }

}