package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    List<Transfer> findTransfersByUserId(int userId);

    List<Transfer> findPendingTransfersByUserId(int userId);

    BigDecimal findBalanceByUserId(int userId);

    Account findAccountByAccountId(int accountId);

    BigDecimal subtractBalance(int accountId, BigDecimal amountToSubtract);

    BigDecimal addBalance(int accountId, BigDecimal amountToAdd);

    Transfer findTransferByTransferId(int transferId);

    Transfer saveTransfer(Transfer transfer);

    void updateTransferStatus(Transfer transfer);

    void updateBalance(int id, BigDecimal newSenderBalance);

    List<Transfer> getTransfersByUserId(int userId);
    List<Transfer> getTransfersByAccountId(int accountId);

    Transfer getTransferDetails(int transferId);

    int getUserIdByUsername(String username);
}


