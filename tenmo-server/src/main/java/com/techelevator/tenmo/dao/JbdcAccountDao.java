package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.pojos.UserPojo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

public class JbdcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JbdcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    private Account findAccountByUserId(int userId) {
        return null;
    }

    private UserPojo findUserByAccountId(int accountId) {
        return null;
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
