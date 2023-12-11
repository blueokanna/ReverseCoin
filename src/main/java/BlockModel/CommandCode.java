package BlockModel;

public enum CommandCode {
    /*
    查询最后一个区块
     */
    CheckLastestBlock(100),
     /*
    查询整个区块链
     */
    CheckWholeChain(101),
     /*
    返回一个最新的区块
     */
    ReturnLastestBlock(102),
    /*
    返回整个区块链
     */
    ReturnLastestBlockChain(103);

    private final int code;

    CommandCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
