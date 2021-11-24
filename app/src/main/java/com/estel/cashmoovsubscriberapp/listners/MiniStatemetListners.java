package com.estel.cashmoovsubscriberapp.listners;

public interface MiniStatemetListners {
    void onMiniStatementListItemClick(String transactionTypeName, String fromWalletOwnerName, String walletOwnerMssdn, String currencySymbol, double fromAmount, String transactionId, String creationDate, String status);

}
