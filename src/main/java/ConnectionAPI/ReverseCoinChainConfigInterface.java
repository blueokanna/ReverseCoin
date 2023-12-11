package ConnectionAPI;

import BlockModel.ReverseCoinBlock;
import BlockModel.ReverseCoinTransaction;
import java.util.concurrent.CopyOnWriteArrayList;
import org.java_websocket.WebSocket;

public interface ReverseCoinChainConfigInterface {

    int getDifficulty();

    int getPorts();

    String getAddressPort();

    CopyOnWriteArrayList<ReverseCoinBlock> getBlockList();

    CopyOnWriteArrayList<ReverseCoinTransaction> getTransactionsList();

    CopyOnWriteArrayList<WebSocket> getSocketsList();

    ReverseCoinBlock getLatestBlock();
    
    String SHA3with256Hash(String input);

    void setDifficulty(int difficulty);

    void setPorts(int ports);

    void setAddressPort(String AddressPort);

    void setBlockList(CopyOnWriteArrayList<ReverseCoinBlock> BlockList);

    void setTransactionsList(CopyOnWriteArrayList<ReverseCoinTransaction> TransactionsList);

    void setSocketsList(CopyOnWriteArrayList<WebSocket> socketsList);
}
