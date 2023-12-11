package top.pulselink.java.reversecoin;

import ReverseCoinBlockChainGeneration.MiningReverseCoinBlock;
import ReverseCoinBlockChainGeneration.NewBlockCreation;
import com.google.gson.GsonBuilder;
import ConnectionAPI.NewReverseCoinBlockInterface;
import ConnectionAPI.ReverseCoinBlockEventListener;
import ConnectionAPI.ReverseCoinChainConfigInterface;
import ConnectionAPI.ReverseCoinBlockInterface;

public class BlockEventManager implements ReverseCoinBlockEventListener {

    @Override
    public String scanBlock(ReverseCoinChainConfigInterface config) throws Exception {
        return new GsonBuilder().setPrettyPrinting().create().toJson(config.getBlockList());
    }

    @Override
    public String scanData(ReverseCoinChainConfigInterface config) throws Exception {
        return new GsonBuilder().setPrettyPrinting().create().toJson(config.getTransactionsList());
    }

    @Override
    public String createGenesisBlock(ReverseCoinBlockInterface blocks, ReverseCoinChainConfigInterface config) throws Exception {
        new NewBlockCreation().createGenesisBlock(blocks, config);
        return new GsonBuilder().setPrettyPrinting().create().toJson(config.getLatestBlock());

    }

    @Override
    public String createNewBlock(NewReverseCoinBlockInterface addnewblock, ReverseCoinChainConfigInterface config) throws Exception {
        new MiningReverseCoinBlock().mineBlock(addnewblock, config);
        return new GsonBuilder().setPrettyPrinting().create().toJson(config.getLatestBlock());
    }

}
