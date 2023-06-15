package com.techelevator.tenmo.model;

import com.techelevator.tenmo.pojos.UserPojo;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private TransferType transferType;
    private TransferStatus transferStatus;
    private UserPojo fromUser;
    private UserPojo toUser;
    private BigDecimal transferAmount;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
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
