package com.techelevator.tenmo.model;

public class TransferStatus {
    public static final int APPROVED = 1;
    public static final int PENDING = 2;
    public static final int REJECTED = 3;

    public static String textTransferStatus(int status) {
        if (status == 1) { return "Approved"; }
        if (status == 2) { return "Pending"; }
        if (status == 3) { return "Rejected"; }
        return "Unknown";
    }
}
