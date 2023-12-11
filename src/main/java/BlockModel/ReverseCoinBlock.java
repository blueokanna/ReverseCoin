package BlockModel;

import java.io.Serializable;
import com.google.gson.GsonBuilder;
import java.util.concurrent.CopyOnWriteArrayList;
import ConnectionAPI.ReverseCoinBlockInterface;

public class ReverseCoinBlock implements Serializable, ReverseCoinBlockInterface {

    private static final long serialVersionUID = 1145141919810L;
    private byte version;
    private int index;
    private String PreviousHash;
    private CopyOnWriteArrayList<ReverseCoinTransaction> TransactionsData;
    private String TransactionHash;
    private long TimeStamp;
    private long nonce;
    private int difficulty;
    private String thisBlockHash;

    @Override
    public byte getVersion() {
        return version;
    }

    @Override
    public void setVersion(byte version) {
        this.version = version;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String getPreviousHash() {
        return PreviousHash;
    }

    @Override
    public void setPreviousHash(String PreviousHash) {
        this.PreviousHash = PreviousHash;
    }

    @Override
    public String getTransactionHash() {
        return TransactionHash;
    }

    @Override
    public void setTransactionHash(String TransactionHash) {
        this.TransactionHash = TransactionHash;
    }    

    @Override
    public CopyOnWriteArrayList<ReverseCoinTransaction> getTransactionsData() {
        return TransactionsData;
    }

    @Override
    public void setTransactionsData(CopyOnWriteArrayList<ReverseCoinTransaction> TransactionsData) {
        this.TransactionsData = TransactionsData;
    }

    @Override
    public long getTimeStamp() {
        return TimeStamp;
    }

    @Override
    public void setTimeStamp(long TimeStamp) {
        this.TimeStamp = TimeStamp;
    }

    @Override
    public int getDifficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public long getNonce() {
        return nonce;
    }

    @Override
    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    @Override
    public String getThisBlockHash() {
        return thisBlockHash;
    }

    @Override
    public void setThisBlockHash(String thisBlockHash) {
        this.thisBlockHash = thisBlockHash;
    }

    @Override
    public String toBlockJsonString(ReverseCoinBlock blocks) {
        return new GsonBuilder().create().toJson(blocks);
    }

}
