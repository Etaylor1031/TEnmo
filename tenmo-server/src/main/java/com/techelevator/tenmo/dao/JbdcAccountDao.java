package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public class JbdcAccountDao implements AccountDao {
    @Override
    public List<Transfer> findTransfersByUserId(int userId) {
        return null;
    }

    @Override
    public List<Transfer> findPendingTransfersByUserId(int userId) {
        return null;
    }

    @Override
    public BigDecimal findBalanceByUserId(int userId) {
        return null;
    }

    @Override
    public Transfer findTransferByTransferId(int transferId) {
        return null;
    }

    @Override
    public Transfer saveTransfer(Transfer transfer) {
        return null;
    }

    @Override
    public void updateTransferStatus(Transfer transfer) {

    }
}
