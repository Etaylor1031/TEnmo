package com.techelevator.tenmo.model;

public class TransferType {

    public static final int SEND = 1;
    public static final int REQUEST = 2;
    public static String textTransferType(int type) {
        if (type == 1) { return "Send"; }
        if (type == 2) { return "Request"; }
        return "Unknown";
    }
}
