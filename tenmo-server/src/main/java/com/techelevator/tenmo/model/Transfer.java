package com.techelevator.tenmo.model;

import com.techelevator.tenmo.pojos.UserPojo;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private int transferType;
    private int transferStatus;
    private UserPojo fromUser;
    private UserPojo toUser;
    private BigDecimal transferAmount;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public int getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }

    public UserPojo getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserPojo fromUser) {
        this.fromUser = fromUser;
    }

    public UserPojo getToUser() {
        return toUser;
    }

    public void setToUser(UserPojo toUser) {
        this.toUser = toUser;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }
}
