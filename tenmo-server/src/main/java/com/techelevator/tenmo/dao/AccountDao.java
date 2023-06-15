package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    List<Transfer> findTransfersByUserId(int userId);

    List<Transfer> findPendingTransfersByUserId(int userId);

    BigDecimal findBalanceByUserId(int userId);

    Transfer findTransferByTransferId(int transferId);

    Transfer saveTransfer(Transfer transfer);

    void updateTransferStatus(Transfer transfer);

    void updateBalance(int id, BigDecimal newSenderBalance);


    Account findAccountByUserId(int userId);

    List<Transfer> getTransfersByUserId(int userId);

    Transfer getTransferDetails(int transferId);
}


