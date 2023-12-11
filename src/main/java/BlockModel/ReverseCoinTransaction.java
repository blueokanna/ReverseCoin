package BlockModel;

import ConnectionAPI.TransactionInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.util.LinkedHashMap;

public class ReverseCoinTransaction implements Serializable ,TransactionInterface{

    private static final long serialVersionUID = 1145141919810L;
    private String Sender;
    private String receiver;
    private double amount;
    private double Txfee;
    private String signature;

    public ReverseCoinTransaction() {

    }
    public ReverseCoinTransaction(String Sender, String receiver, double amount, double Txfee) {
        this.Sender = Sender;
        this.receiver = receiver;
        this.amount = amount;
        this.Txfee = Txfee;
    }
    public ReverseCoinTransaction(String Sender, String receiver, double amount, double Txfee, String signature) {
        this.Sender = Sender;
        this.receiver = receiver;
        this.amount = amount;
        this.Txfee = Txfee;
        this.signature = signature;
    }

    private LinkedHashMap<String, Object> JsonMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("Sender", Sender);
        map.put("Receiver", receiver);
        map.put("Amount", amount);
        map.put("GasFee", Txfee);
        map.put("Signature", signature);
        return map;
    }

    @Override
    public String getSender() {
        return Sender;
    }

    @Override
    public void setSender(String Sender) {
        this.Sender = Sender;
    }

    @Override
    public String getReceiver() {
        return receiver;
    }

    @Override
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public double getTxfee() {
        return Txfee;
    }

    @Override
    public void setTxfee(double Txfee) {
        this.Txfee = Txfee;
    }

    @Override
    public String getSignature() {
        return signature;
    }

    @Override
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toJsonTransactionString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(JsonMap());
    }

}
