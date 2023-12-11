package top.pulselink.java.reversecoin;

import BlockModel.PeerMessages;
import BlockModel.ReverseCoinBlock;
import ClientSeverMach.P2PClient;
import ClientSeverMach.P2PServer;
import ReverseCoinBlockChainGeneration.BlockChainConfig;
import ReverseCoinBlockChainGeneration.NewBlockCreation;
import ReverseCoinChainNetwork.P2PNetwork;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ConnectionAPI.NewReverseCoinBlockInterface;
import ConnectionAPI.ReverseCoinBlockEventListener;
import ConnectionAPI.ReverseCoinChainConfigInterface;
import ConnectionAPI.ReverseCoinBlockInterface;

public class ReverseCoin {

    private volatile BlockChainConfig blockConfig = new BlockChainConfig();
    private volatile ReverseCoinBlock block = new ReverseCoinBlock();
    private volatile NewBlockCreation newBlock = new NewBlockCreation();
    private volatile PeerMessages message = new PeerMessages();
    private volatile ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private volatile P2PNetwork network = new P2PNetwork();
    private static final Logger logger = LoggerFactory.getLogger(ReverseCoin.class);

    public ReverseCoin(String address, int port, int difficulty) {
        blockConfig.setDifficulty(difficulty);
        blockConfig.setPorts(port);
        blockConfig.setAddressPort(address);
        initializeConnections();
        startInputReader(block, new BlockEventManager(), newBlock, blockConfig, address);
    }

    private synchronized void startInputReader(ReverseCoinBlockInterface block, ReverseCoinBlockEventListener listener, NewReverseCoinBlockInterface addnewblock, ReverseCoinChainConfigInterface configParams, String addressPort) {
        if (!executor.isShutdown() && !executor.isTerminated()) {
            CompletableFuture.runAsync(() -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                try {
                    String str;
                    while ((str = reader.readLine()) != null) {
                        handleEvent(str, block, listener, addnewblock, configParams, addressPort);
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }, executor);
        }
    }

    private void initializeConnections() {
        printNodeInfo(blockConfig);
        CompletableFuture<Void> serverInitFuture = CompletableFuture.runAsync(() -> initP2PServer());
        CompletableFuture<Void> clientConnectFuture = CompletableFuture.runAsync(() -> connectToPeer());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdownNow();
            try {
                executor.awaitTermination(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }));

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(serverInitFuture, clientConnectFuture);
        allTasks.join();

    }

    private void printNodeInfo(ReverseCoinChainConfigInterface configParams) {
        int difficulty = configParams.getDifficulty();
        int ports = configParams.getPorts();
        String addressPort = configParams.getAddressPort();

        System.out.println("------------------\nPetaBlock Difficulty: " + difficulty + "\n------------------");
        System.out.println("------------------\nP2P Port: " + ports + "\n------------------");
        System.out.println("------------------\nP2P Network IP with Port: " + addressPort + "\n------------------");
        System.out.println();
    }

    private void initP2PServer() {
        P2PServer server = new P2PServer();
        server.initP2PServer(blockConfig.getPorts(), blockConfig, newBlock, message, network);
    }

    private void connectToPeer() {
        P2PClient client = new P2PClient();
        client.connectToPeer(blockConfig.getAddressPort(), blockConfig, newBlock, message, network);
    }

    private void handleEvent(String event, ReverseCoinBlockInterface block, ReverseCoinBlockEventListener listener, NewReverseCoinBlockInterface addnewblock, ReverseCoinChainConfigInterface configParams, String addressPort) {
        CompletableFuture<Void> eventFuture = null;

        switch (event) {
            case "scanblock" ->
                eventFuture = handleScanBlock(listener, configParams);
            case "blockdata" ->
                eventFuture = handleScanData(listener, configParams);
            case "mineblock" -> {
                synchronized (configParams) {
                    if (configParams.getLatestBlock() == null) {
                        eventFuture = handleCreateNewBlockAfterFirstBlock(block, listener, addnewblock, configParams);
                    } else {
                        eventFuture = handleCreateNewBlock(listener, addnewblock, configParams);
                    }
                }
            }

            default -> {
                logger.info("Invalid Input");
                return;
            }
        }
        if (eventFuture != null) {
            eventFuture.thenAccept(result -> {
            }).exceptionally(ex -> {
                logger.error(ex.getMessage());
                return null;
            });
        } else {
            logger.info("No matching event found");
        }
    }

    private CompletableFuture<Void> handleCreateNewBlockAfterFirstBlock(ReverseCoinBlockInterface block, ReverseCoinBlockEventListener listener, NewReverseCoinBlockInterface addnewblock, ReverseCoinChainConfigInterface configParams) {
        return handleCreateFirstBlock(listener, block, configParams)
                .thenRunAsync(() -> handleCreateNewBlock(listener, addnewblock, configParams));
    }

    private CompletableFuture<Void> handleScanBlock(ReverseCoinBlockEventListener listener, ReverseCoinChainConfigInterface configParams) {
        return CompletableFuture.runAsync(() -> {
            try {
                String scannedBlock = listener.scanBlock(configParams);
                System.out.println("ScanBlock: " + scannedBlock);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }, executor);
    }

    private CompletableFuture<Void> handleScanData(ReverseCoinBlockEventListener listener, ReverseCoinChainConfigInterface configParams) {
        return CompletableFuture.runAsync(() -> {
            try {
                String scannedData = listener.scanData(configParams);
                System.out.println("ScanData: " + scannedData);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }, executor);
    }

    private CompletableFuture<Void> handleCreateFirstBlock(ReverseCoinBlockEventListener listener, ReverseCoinBlockInterface block, ReverseCoinChainConfigInterface configParams) {
        return CompletableFuture.runAsync(() -> {
            try {
                String firstBlockCommand = listener.createGenesisBlock(block, configParams);
                System.out.println("Create Genesis PetaBlock: " + firstBlockCommand);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }, executor);
    }

    private CompletableFuture<Void> handleCreateNewBlock(ReverseCoinBlockEventListener listener, NewReverseCoinBlockInterface addnewblock, ReverseCoinChainConfigInterface configParams) {
        return CompletableFuture.runAsync(() -> {
            try {
                String newBlockCommand = listener.createNewBlock(addnewblock, configParams);
                System.out.println("Create a new PetaBlock: " + newBlockCommand);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }, executor);
    }

    public static void main(String[] args) {
        int difficulty = 3;
        int ports = 18601;
        String addressPort = "ws://172.20.26.115:18602";
        new ReverseCoin(addressPort, ports, difficulty);

    }
}
