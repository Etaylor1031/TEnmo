package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.pojos.UserPojo;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    BigDecimal getBalanceByUserId(int userId);
    void updateBalance(int id, BigDecimal newSenderBalance);
    BigDecimal subtractBalance(int accountId, BigDecimal amountToSubtract);
    BigDecimal addBalance(int accountId, BigDecimal amountToAdd);
    List<UserPojo> getUsers();
    Transfer getTransferDetails(int transferId);
    List<Transfer> getTransfersByUserId(int userId);
    Transfer save(Transfer transfer);
    List<Transfer> getPendingTransfersByUserId(int userId);
    Transfer updateTransferStatus(Transfer transfer, int transferId);
    String getUserNameByUserId(int userId);
}