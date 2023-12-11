package ConnectionAPI;

public interface TransactionInterface {

    String getSender();

    String getReceiver();

    double getAmount();

    double getTxfee();

    String getSignature();

    String toJsonTransactionString();

    void setSender(String Sender);

    void setReceiver(String receiver);

    void setAmount(double amount);

    void setTxfee(double Txfee);

    void setSignature(String signature);
}
