package ConnectionAPI;

public interface ReverseCoinBlockEventListener {

    String scanBlock(ReverseCoinChainConfigInterface config) throws Exception;

    String scanData(ReverseCoinChainConfigInterface config) throws Exception;

    String createGenesisBlock(ReverseCoinBlockInterface blocks, ReverseCoinChainConfigInterface config) throws Exception;

    String createNewBlock(NewReverseCoinBlockInterface addnewblock, ReverseCoinChainConfigInterface config) throws Exception;
}
