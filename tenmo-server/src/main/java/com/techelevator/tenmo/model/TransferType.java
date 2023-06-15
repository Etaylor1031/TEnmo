package com.techelevator.tenmo.model;

public class TransferType {

    public static final int SEND = 1;
    public static final int REQUEST = 2;
    private int transferTypeId;
    private String transferTypeLabel;

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getTransferTypeLabel() {
        return transferTypeLabel;
    }

    public void setTransferTypeLabel(String transferTypeLabel) {
        this.transferTypeLabel = transferTypeLabel;
    }
}
