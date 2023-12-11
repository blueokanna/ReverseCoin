package ConnectionAPI;

import BlockModel.ReverseCoinBlock;
import BlockModel.ReverseCoinTransaction;
import java.util.concurrent.CopyOnWriteArrayList;

public interface ReverseCoinBlockInterface {

    byte getVersion();

    int getIndex();

    String getPreviousHash();

    String getTransactionHash();

    CopyOnWriteArrayList<ReverseCoinTransaction> getTransactionsData();

    long getTimeStamp();

    int getDifficulty();

    long getNonce();

    String getThisBlockHash();

    String toBlockJsonString(ReverseCoinBlock blocks);

    void setVersion(byte version);

    void setIndex(int index);

    void setPreviousHash(String PreviousHash);

    void setTransactionHash(String TransactionHash);

    void setTransactionsData(CopyOnWriteArrayList<ReverseCoinTransaction> TransactionHash);

    void setTimeStamp(long TimeStamp);

    void setDifficulty(int difficulty);

    void setNonce(long nonce);

    void setThisBlockHash(String thisBlockHash);

}
