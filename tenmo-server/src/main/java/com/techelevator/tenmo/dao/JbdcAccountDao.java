package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.pojos.UserPojo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JbdcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JbdcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> findTransfersByUserId(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        return transfers;
    }

    @Override
    public List<Transfer> findPendingTransfersByUserId(int userId) {
        List<Transfer> pendingTransfers = new ArrayList<>();
        return pendingTransfers;
    }

    @Override
    public BigDecimal findBalanceByUserId(int userId) {
        BigDecimal balance = null;
        Account account = null;
        return balance;
    }

    @Override
    public Transfer findTransferByTransferId(int transferId) {
        Transfer transfer = null;
        return transfer;
    }

    @Override
    public Transfer saveTransfer(Transfer transfer) {
        return transfer;
    }

    @Override
    public void updateTransferStatus(Transfer transfer) {

    }

    private Account findAccountByUserId(int userId) {
        Account account = null;
        return account;
    }

    private UserPojo findUserByAccountId(int accountId) {
        UserPojo user = null;
        return user;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        return transfer;
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        return account;
    }
}
