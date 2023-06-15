package com.techelevator.tenmo.model;

public class TransferStatus {
    public static final int APPROVED = 1;
    public static final int PENDING = 2;
    public static final int REJECTED = 3;
    private int transferStatusId;
    private String transferStatusLabel;

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferStatusLabel() {
        return transferStatusLabel;
    }

    public void setTransferStatusLabel(String transferStatusLabel) {
        this.transferStatusLabel = transferStatusLabel;
    }
}
