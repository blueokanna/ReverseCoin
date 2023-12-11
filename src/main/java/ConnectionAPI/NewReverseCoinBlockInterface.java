package ConnectionAPI;

import BlockModel.ReverseCoinBlock;
import BlockModel.ReverseCoinTransaction;
import java.util.concurrent.CopyOnWriteArrayList;

public interface NewReverseCoinBlockInterface {

    boolean addNewBlocks(ReverseCoinBlock newBlock, ReverseCoinChainConfigInterface config);

    boolean checkPetaBlock(ReverseCoinBlock newBlock, ReverseCoinBlock previousBlock, ReverseCoinChainConfigInterface config);

    ReverseCoinBlock generatedNewBlock(String preHash, CopyOnWriteArrayList<ReverseCoinTransaction> TransactionsData, String TransactionHash, long nonce, String setBlockHash, ReverseCoinChainConfigInterface config);

    void updatePetaChain(CopyOnWriteArrayList<ReverseCoinBlock> newChain, ReverseCoinChainConfigInterface config);

    boolean isUpdateBlockChain(CopyOnWriteArrayList<ReverseCoinBlock> petachain, ReverseCoinChainConfigInterface config);
    
    boolean isSortedByIndex(ReverseCoinChainConfigInterface config);

    boolean checkBlocksHash(String hash, String target, ReverseCoinChainConfigInterface config);

}
