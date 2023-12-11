package ReverseCoinBlockChainGeneration;

import BlockModel.PerformRingSign;
import BlockModel.ReverseCoinBlock;
import BlockModel.ReverseCoinTransaction;
import BlockModel.TimeStampGenerator;
import com.google.gson.GsonBuilder;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ConnectionAPI.NewReverseCoinBlockInterface;
import ConnectionAPI.ReverseCoinChainConfigInterface;
import ConnectionAPI.ReverseCoinBlockInterface;

public class NewBlockCreation implements NewReverseCoinBlockInterface {

    private final String previousHash = "0000000000000000000000000000000000000000000000000000000000000000";
    private volatile long timestamp = new TimeStampGenerator().TimeToSync();
    private volatile ReverseCoinBlock ReverseBlockHeader = new ReverseCoinBlock();
    private volatile ExecutorService executor;
    private volatile long nonce = 0;

    private CopyOnWriteArrayList<ReverseCoinTransaction> GenesisBlockTransData() {
        SecureRandom secureRandom = new SecureRandom();
        int numberGenerations = Math.abs(secureRandom.nextInt());
        while (numberGenerations > 100) {
            numberGenerations /= 2;
        }

        String Sender = "ReverseCoin";
        String Receiver = "BlockChain";
        double Amount = 114514;
        double gasFee = 1919.810;

        String data = Sender + Receiver + Amount + gasFee + timestamp;

        byte[] Signature = new PerformRingSign().performRingSign(data, numberGenerations, numberGenerations / 2);
        try {
            ReverseCoinTransaction transData = new ReverseCoinTransaction(Sender, Receiver, Amount, gasFee, Base64.getEncoder().encodeToString(Signature));
            CopyOnWriteArrayList<ReverseCoinTransaction> transList = new CopyOnWriteArrayList<>();
            transList.add(transData);
            return transList;
        } catch (Exception ex) {
            ex.getMessage();
        }
        return null;
    }

    public ReverseCoinBlock createGenesisBlock(ReverseCoinBlockInterface block, ReverseCoinChainConfigInterface config) throws Exception {
        System.out.println("Mining Genesis Block Now......");
        int index = 1;

        CopyOnWriteArrayList<CompletableFuture<Void>> futures = new CopyOnWriteArrayList<>();
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2);

        for (int i = 0; i < index; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

                while (true) {
                    String text = previousHash + GenesisBlockTransData() + nonce;
                    String hash = config.SHA3with256Hash(config.SHA3with256Hash(text));

                    if (hash != null && hash.startsWith(new String(new char[config.getDifficulty()]).replace('\0', '0'))) {
                        ReverseBlockHeader.setVersion((byte) 0x01);
                        ReverseBlockHeader.setIndex(index);
                        ReverseBlockHeader.setPreviousHash(previousHash);
                        ReverseBlockHeader.setTransactionsData(GenesisBlockTransData());
                        ReverseBlockHeader.setTimeStamp(timestamp);
                        ReverseBlockHeader.setDifficulty(config.getDifficulty());
                        ReverseBlockHeader.setNonce(nonce);
                        ReverseBlockHeader.setThisBlockHash(hash);
                        return;

                    }
                    nonce++;
                }
            }, executor);
            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        allOf.join();

        executor.shutdown();

        config.getTransactionsList().addAll(ReverseBlockHeader.getTransactionsData());
        config.getBlockList().add(ReverseBlockHeader);
        return ReverseBlockHeader;
    }

    @Override
    public ReverseCoinBlock generatedNewBlock(String preHash, CopyOnWriteArrayList<ReverseCoinTransaction> TransactionsData, String TransactionHash, long nonce, String setBlockHash, ReverseCoinChainConfigInterface config) {
        ReverseCoinBlock petablock = new ReverseCoinBlock();
        petablock.setVersion((byte) 0x01);
        petablock.setIndex(config.getBlockList().size() + 1);
        petablock.setPreviousHash(preHash);
        petablock.setTransactionHash(TransactionHash);
        petablock.setTransactionsData(TransactionsData);
        petablock.setTimeStamp(timestamp);
        petablock.setDifficulty(config.getDifficulty());
        petablock.setNonce(nonce);
        petablock.setThisBlockHash(setBlockHash);
        if (addNewBlocks(petablock, config)) {
            return petablock;
        }
        return null;
    }

    @Override
    public boolean addNewBlocks(ReverseCoinBlock newBlock, ReverseCoinChainConfigInterface config) {
        if (checkPetaBlock(newBlock, config.getLatestBlock(), config)) {
            config.getTransactionsList().addAll(newBlock.getTransactionsData());
            config.getBlockList().add(newBlock);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkPetaBlock(ReverseCoinBlock newReverseBlock, ReverseCoinBlock previousBlock, ReverseCoinChainConfigInterface config) {
        if (!previousBlock.getThisBlockHash().equals(newReverseBlock.getPreviousHash())) {
            System.out.println("Previous PetaBlock Hash is not matched");
            return false;
        } else {
            String data = newReverseBlock.getPreviousHash() + newReverseBlock.getTransactionHash() + newReverseBlock.getNonce();
            String calHash = config.SHA3with256Hash(config.SHA3with256Hash(data));
            if (!calHash.equals(newReverseBlock.getThisBlockHash())) {
                System.out.println("Index is: " + config.getBlockList().size());
                System.out.println("New PetaBlock verification failed, Hash value verification error!");
                return false;
            }
        }
        System.out.println("ReverseCoin verification successful！");
        return true;
    }

    @Override
    public void updatePetaChain(CopyOnWriteArrayList<ReverseCoinBlock> newChain, ReverseCoinChainConfigInterface config) {
        try {
            CopyOnWriteArrayList<ReverseCoinBlock> oldBlockChain = config.getBlockList();
            CopyOnWriteArrayList<ReverseCoinTransaction> transactionOnChain = config.getTransactionsList();

            if (isUpdateBlockChain(newChain, config) && newChain.size() > oldBlockChain.size()) {
                oldBlockChain = newChain;
                transactionOnChain.clear();

                oldBlockChain.forEach(block -> {
                    transactionOnChain.addAll(block.getTransactionsData());
                });

                config.setBlockList(oldBlockChain);
                config.setTransactionsList(transactionOnChain);
                System.out.println("Updated ReverseCoin-BlockChain: " + new GsonBuilder().setPrettyPrinting().create().toJson(config.getBlockList()));
            } else {
                System.out.println("The ReverseCoin-BlockChain is Invalid");
            }
        } catch (Exception ex) {
            System.out.println("Error updating ReverseCoin-BlockChain: " + ex.getMessage());
        }
    }

    @Override
    public boolean isUpdateBlockChain(CopyOnWriteArrayList<ReverseCoinBlock> petachain, ReverseCoinChainConfigInterface config) {
        int count = 1;
        ReverseCoinBlock previousBlock = petachain.get(0);

        while (count < petachain.size()) {
            ReverseCoinBlock petablock = petachain.get(count);
            if (!checkPetaBlock(petablock, previousBlock, config)) {
                return false;
            }
            previousBlock = petablock;
            count++;
        }
        return true;
    }

    @Override
    public boolean isSortedByIndex(ReverseCoinChainConfigInterface config) {
        CopyOnWriteArrayList<ReverseCoinBlock> block = config.getBlockList();
        for (int i = 1; i < block.size(); i++) {
            if (block.get(i - 1).getIndex() > block.get(i).getIndex()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkBlocksHash(String hash, String target, ReverseCoinChainConfigInterface config) {
        return hash.substring(0, config.getDifficulty()).equals(target);
    }

}
