package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.pojos.UserPojo;
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
    public BigDecimal getBalanceByUserId(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    @Override
    public BigDecimal subtractBalance(int userId, BigDecimal amountToSubtract) {
        Account account = findAccountByUserId(userId);
        BigDecimal newBalance = account.getBalance().subtract(amountToSubtract);
        updateBalance(account.getAccountId(), newBalance);
        return newBalance;
    }

    @Override
    public BigDecimal addBalance(int UserId, BigDecimal amountToAdd) {
        Account account = findAccountByUserId(UserId);
        BigDecimal newBalance = account.getBalance().add(amountToAdd);
        updateBalance(account.getAccountId(), newBalance);
        return newBalance;
    }

    @Override
    public List<UserPojo> getUsers() {
        List<UserPojo> users = new ArrayList<>();
        String sql = "SELECT user_id, username FROM tenmo_user";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            UserPojo userPojo = mapRowToUser(results);
            users.add(userPojo);
        }
        return users;
    }

    @Override
    public Transfer getTransferDetails(int transferId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        }
        return null;
    }

    @Override
    public List<Transfer> getTransfersByUserId(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE account_from = ? OR account_to = ?";
        int accountId = findAccountByUserId(userId).getAccountId();
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer saveTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) returning transfer_id";

        int fromAccountId = findAccountByUserId(transfer.getFromUser()).getAccountId();
        int toAccountId = findAccountByUserId(transfer.getToUser()).getAccountId();

        Integer newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferType(), transfer.getTransferStatus(), fromAccountId,
                toAccountId, transfer.getTransferAmount());
        transfer.setTransferId(newTransferId);
        transfer.setTransferTypeDescription(TransferType.textTransferType(transfer.getTransferType()));
        transfer.setTransferStatusDescription(TransferStatus.textTransferStatus(transfer.getTransferStatus()));
        transfer.setFromUserName(getUserNameByUserId(transfer.getFromUser()));
        transfer.setToUserName((getUserNameByUserId(transfer.getToUser())));
        return transfer;
    }

    @Override
    public List<Transfer> getPendingTransfersByUserId(int userId) {
        List<Transfer> pendingTransfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE account_from = ? AND transfer_status_id = ?";
        int accountId = findAccountByUserId(userId).getAccountId();
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, TransferStatus.PENDING);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            pendingTransfers.add(transfer);
        }
        return pendingTransfers;
    }

    @Override
    public Transfer updateTransferStatus(Transfer transfer, int transferId) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?";
        jdbcTemplate.update(sql, transfer.getTransferStatus(), transferId);
        return transfer;
    }

    private void updateBalance(int accountId, BigDecimal newSenderBalance) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, newSenderBalance, accountId);
    }

    private String getUserNameByUserId(int userId) {
        String sql = "SELECT username FROM tenmo_user WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, userId);
    }

    private Account findAccountByUserId(int userId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToAccount(results);
        }
        return null;
    }

    private UserPojo findUserByAccountId(int accountId) {
        String sql = "SELECT tenmo_user.user_id, username FROM tenmo_user JOIN account ON tenmo_user.user_id = account.user_id WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            return mapRowToUser(results);
        }
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();

        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferType(results.getInt("transfer_type_id"));
        transfer.setTransferStatus(results.getInt("transfer_status_id"));

        UserPojo fromUser = findUserByAccountId(results.getInt("account_from"));
        transfer.setFromUser(fromUser.getUserId());
        transfer.setFromUserName(fromUser.getUsername());

        UserPojo toUser = findUserByAccountId(results.getInt("account_to"));
        transfer.setToUser(toUser.getUserId());
        transfer.setToUserName(toUser.getUsername());
        transfer.setTransferAmount(results.getBigDecimal("amount"));
        transfer.setTransferTypeDescription(TransferType.textTransferType(transfer.getTransferType()));
        transfer.setTransferStatusDescription(TransferStatus.textTransferStatus(transfer.getTransferStatus()));

        return transfer;
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