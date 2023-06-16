package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.pojos.UserPojo;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> findTransfersByUserId(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, from_user_id, to_user_id, amount " +
                "FROM transfers " +
                "WHERE from_user_id = ? OR to_user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public List<Transfer> findPendingTransfersByUserId(int userId) {
        List<Transfer> pendingTransfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, from_user_id, to_user_id, amount " +
                "FROM transfers " +
                "WHERE (from_user_id = ? OR to_user_id = ?) AND transfer_status_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId, TransferStatus.PENDING);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            pendingTransfers.add(transfer);
        }
        return pendingTransfers;
    }

    @Override
    public BigDecimal findBalanceByUserId(int userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    @Override
    public Transfer findTransferByTransferId(int transferId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, from_user_id, to_user_id, amount " +
                "FROM transfers " +
                "WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        }
        return null;
    }


    @Override
    public Transfer saveTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, from_user_id, to_user_id, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";

        try {
            Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class,
                    transfer.getTransferType(),
                    transfer.getTransferStatus(),
                    transfer.getFromUser(),
                    transfer.getToUser(),
                    transfer.getTransferAmount());

            if (transferId != null) {
                transfer.setTransferId(transferId);
                return transfer;
            } else {
                // Handle the case where the transfer ID is null (e.g., database error)
                throw new RuntimeException("Failed to save transfer. Please try again.");
            }
        } catch (DataAccessException e) {
            // Handle the exception appropriately
            throw new RuntimeException("Failed to save transfer. Please try again.", e);
        }
    }


    @Override
    public void updateTransferStatus(Transfer transfer) {
        String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?";
        jdbcTemplate.update(sql, transfer.getTransferStatus(), transfer.getTransferId());
    }

    @Override
    public void updateBalance(int id, BigDecimal newSenderBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, newSenderBalance, id);
    }


    @Override
    public List<Transfer> getTransfersByUserId(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, from_user_id, to_user_id, amount " +
                "FROM transfers " +
                "WHERE from_user_id = ? OR to_user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransferDetails(int transferId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, from_user_id, to_user_id, amount " +
                "FROM transfers " +
                "WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        }
        return null;
    }


    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM accounts";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public int getUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try {
            Integer userId = jdbcTemplate.queryForObject(sql, Integer.class, username);
            return userId != null ? userId : 0; // Return 0 if userId is null
        } catch (EmptyResultDataAccessException e) {
            return 0; // Return 0 if the username is not found
        }
    }



    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferType(results.getInt("transfer_type_id"));
        transfer.setTransferStatus(results.getInt("transfer_status_id"));
        transfer.setFromUser(results.getInt("from_user_id"));
        transfer.setToUser(results.getInt("to_user_id"));
        transfer.setTransferAmount(results.getBigDecimal("amount"));
        return transfer;
    }

    private Account findAccountByUserId(int userId) {
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToAccount(results);
        }
        return null;
    }

    private UserPojo findUserByAccountId(int accountId) {
        String sql = "SELECT user_id, username FROM users JOIN accounts ON users.user_id = accounts.user_id WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            return mapRowToUser(results);
        }
        return null;
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountId(results.getInt("account_id"));
        account.setUser(findUserByAccountId(results.getInt("account_id")));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }

    private UserPojo mapRowToUser(SqlRowSet results) {
        UserPojo user = new UserPojo();
        user.setUserId(results.getInt("user_id"));
        user.setUsername(results.getString("username"));
        return user;
    }
}
