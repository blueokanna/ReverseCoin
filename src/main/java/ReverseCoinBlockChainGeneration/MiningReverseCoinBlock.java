package ReverseCoinBlockChainGeneration;

import BlockModel.CommandCode;
import BlockModel.PeerMessages;
import BlockModel.PerformRingSign;
import BlockModel.ReverseCoinBlock;
import BlockModel.ReverseCoinTransaction;
import BlockModel.TimeStampGenerator;
import ReverseCoinChainNetwork.P2PNetwork;
import com.google.gson.GsonBuilder;
import java.security.SecureRandom;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import ConnectionAPI.NewReverseCoinBlockInterface;
import ConnectionAPI.ReverseCoinChainConfigInterface;

public class MiningReverseCoinBlock {

    private volatile long times = new TimeStampGenerator().TimeToSync();
    private volatile int numberGenerations;
    private volatile long nonce;
    private volatile ExecutorService executor;
    private volatile ReverseCoinBlock blocks = new ReverseCoinBlock();
    

    public ReverseCoinBlock mineBlock(NewReverseCoinBlockInterface addnewblock, ReverseCoinChainConfigInterface config) {
        System.out.println("Mining New Block Now......");

        try {
            String target = new String(new char[config.getDifficulty()]).replace('\0', '0');

            int numThreads = Runtime.getRuntime().availableProcessors() / 2;
            executor = Executors.newFixedThreadPool(numThreads);
            CompletionService<ReverseCoinBlock> completionService = new ExecutorCompletionService<>(executor);

            CountDownLatch latch = new CountDownLatch((int) Math.pow(numThreads, numThreads));

            for (int i = 0; i < (int) Math.pow(numThreads, numThreads); i++) {
                completionService.submit(() -> miningThread(addnewblock, config, target, latch));
            }

            latch.await();

            for (int i = 0; i < numThreads; i++) {
                Future<ReverseCoinBlock> future = completionService.poll();
                if (future != null && future.get() != null) {
                    blocks = future.get();
                    numThreads++;
                    break;
                }
            }

            executor.shutdown();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Mining was interrupted: " + e.getMessage());
        } catch (ExecutionException e) {
            System.out.println("Error occurred during mining: " + e.getMessage());
        }

        return blocks;
    }

    private ReverseCoinBlock miningThread(NewReverseCoinBlockInterface addnewblock, ReverseCoinChainConfigInterface config, String target, CountDownLatch latch) {
        try {
            while (true) {
                String transationHash = toTransJsonString(NewBlockTransData());
                String text = config.getLatestBlock().getThisBlockHash() + transationHash + nonce;
                String localHash = config.SHA3with256Hash(config.SHA3with256Hash(text));

                if (addnewblock.checkBlocksHash(localHash, target, config)) {
                    final long currentNonceValue = nonce;
                    String previousHash = config.getLatestBlock().getThisBlockHash();
                    ReverseCoinBlock newBlocks = addnewblock.generatedNewBlock(previousHash, NewBlockTransData(), transationHash, currentNonceValue, localHash, config);
                    broadcastNewBlock(newBlocks, config);

                    return newBlocks;
                }
                nonce++;
            }
        } catch (Exception e) {
            System.out.println("Error Message : " + e.getMessage());
        } finally {
            latch.countDown();
        }
        return null;
    }

    private void broadcastNewBlock(ReverseCoinBlock blocks, ReverseCoinChainConfigInterface config) {
        PeerMessages peermessageSend = new PeerMessages();
        peermessageSend.setCommandCode(CommandCode.ReturnLastestBlock);
        peermessageSend.setMessage(new GsonBuilder().create().toJson(blocks));
        new P2PNetwork().BroadCastBlockMessage(new GsonBuilder().setPrettyPrinting().create().toJson(peermessageSend), config);
    }

    private CopyOnWriteArrayList<ReverseCoinTransaction> NewBlockTransData() {
        SecureRandom secureRandom = new SecureRandom();
        numberGenerations = Math.abs(secureRandom.nextInt());
        while (numberGenerations > 100) {
            numberGenerations /= 2;
        }

        List<ReverseCoinTransaction> TransData = new CopyOnWriteArrayList<>();
        TransData.add(new ReverseCoinTransaction("PetaBlock_Sender1", "PetaBlock_Receiver1", 1145141919810L, numberGenerations / 2));

        String Sender = TransData.get(0).getSender();
        String Receiver = TransData.get(0).getReceiver();
        double Amount = TransData.get(0).getAmount();
        double gasFee = TransData.get(0).getTxfee();

        String data = Sender + Receiver + Amount + gasFee + times;

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

    public String toTransJsonString(CopyOnWriteArrayList<ReverseCoinTransaction> data) {
        return new GsonBuilder().create().toJson(data);
    }
}
