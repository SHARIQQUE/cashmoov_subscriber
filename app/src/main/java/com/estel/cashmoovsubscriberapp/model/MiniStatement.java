package com.estel.cashmoovsubscriberapp.model;

public class MiniStatement {
    private int id;
    private int alertValue;
    private int allocatedValue;
    private int balance;
    private int maxTransValue;
    private int maxValue;
    private int minTransValue;
    private int minValue;
    private int notifyMisdns;
    private double value;
    private String code;
    private String currencyCode;
    private String currencyName;
    private String currencySymbol;
    private String walletOwnerCode;
    private String walletOwnerName;
    private String walletTypeCode;
    private String walletTypeName;
    public int commissionWallet;

    public void setValue(double value) {
        this.value = value;
    }

    public MiniStatement(int id, int alertValue, int allocatedValue, int balance, int maxTransValue, int maxValue, int minTransValue, int minValue, int notifyMisdns, double value, String code, String currencyCode, String currencyName, String currencySymbol, String walletOwnerCode, String walletOwnerName, String walletTypeCode, String walletTypeName, int commissionWallet) {
        this.id = id;
        this.alertValue = alertValue;
        this.allocatedValue = allocatedValue;
        this.balance = balance;
        this.maxTransValue = maxTransValue;
        this.maxValue = maxValue;
        this.minTransValue = minTransValue;
        this.minValue = minValue;
        this.notifyMisdns = notifyMisdns;
        this.value = value;
        this.code = code;
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
        this.walletOwnerCode = walletOwnerCode;
        this.walletOwnerName = walletOwnerName;
        this.walletTypeCode = walletTypeCode;
        this.walletTypeName = walletTypeName;
        this.commissionWallet = commissionWallet;
    }

    public MiniStatement(int id, int alertValue, int allocatedValue, int balance, int maxTransValue, int maxValue, int minTransValue, int minValue, int notifyMisdns, int value, String code, String currencyCode, String currencyName, String currencySymbol, String walletOwnerCode, String walletOwnerName, String walletTypeCode, String walletTypeName, int commissionWallet) {
        this.id = id;
        this.alertValue = alertValue;
        this.allocatedValue = allocatedValue;
        this.balance = balance;
        this.maxTransValue = maxTransValue;
        this.maxValue = maxValue;
        this.minTransValue = minTransValue;
        this.minValue = minValue;
        this.notifyMisdns = notifyMisdns;
        this.value = value;
        this.code = code;
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
        this.walletOwnerCode = walletOwnerCode;
        this.walletOwnerName = walletOwnerName;
        this.walletTypeCode = walletTypeCode;
        this.walletTypeName = walletTypeName;
        this.commissionWallet = commissionWallet;
    }

    public int getCommissionWallet() {
        return commissionWallet;
    }

    public void setCommissionWallet(int commissionWallet) {
        this.commissionWallet = commissionWallet;
    }

    public MiniStatement(int id, int alertValue, int allocatedValue, int balance, int maxTransValue, int maxValue, int minTransValue,
                         int minValue, int notifyMisdns, double value, String code, String currencyCode, String currencyName, String currencySymbol,
                         String walletOwnerCode, String walletOwnerName, String walletTypeCode, String walletTypeName) {
        this.id = id;
        this.alertValue = alertValue;
        this.allocatedValue = allocatedValue;
        this.balance = balance;
        this.maxTransValue = maxTransValue;
        this.maxValue = maxValue;
        this.minTransValue = minTransValue;
        this.minValue = minValue;
        this.notifyMisdns = notifyMisdns;
        this.value = value;
        this.code = code;
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
        this.walletOwnerCode = walletOwnerCode;
        this.walletOwnerName = walletOwnerName;
        this.walletTypeCode = walletTypeCode;
        this.walletTypeName = walletTypeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlertValue() {
        return alertValue;
    }

    public void setAlertValue(int alertValue) {
        this.alertValue = alertValue;
    }

    public int getAllocatedValue() {
        return allocatedValue;
    }

    public void setAllocatedValue(int allocatedValue) {
        this.allocatedValue = allocatedValue;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getMaxTransValue() {
        return maxTransValue;
    }

    public void setMaxTransValue(int maxTransValue) {
        this.maxTransValue = maxTransValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinTransValue() {
        return minTransValue;
    }

    public void setMinTransValue(int minTransValue) {
        this.minTransValue = minTransValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getNotifyMisdns() {
        return notifyMisdns;
    }

    public void setNotifyMisdns(int notifyMisdns) {
        this.notifyMisdns = notifyMisdns;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getWalletOwnerCode() {
        return walletOwnerCode;
    }

    public void setWalletOwnerCode(String walletOwnerCode) {
        this.walletOwnerCode = walletOwnerCode;
    }

    public String getWalletOwnerName() {
        return walletOwnerName;
    }

    public void setWalletOwnerName(String walletOwnerName) {
        this.walletOwnerName = walletOwnerName;
    }

    public String getWalletTypeCode() {
        return walletTypeCode;
    }

    public void setWalletTypeCode(String walletTypeCode) {
        this.walletTypeCode = walletTypeCode;
    }

    public String getWalletTypeName() {
        return walletTypeName;
    }

    public void setWalletTypeName(String walletTypeName) {
        this.walletTypeName = walletTypeName;
    }
}