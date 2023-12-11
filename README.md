# ReverseCoin Blockchain Project - [中文文档](https://github.com/blueokanna/ReverseCoin/blob/main/README_zh.md)

[![Apache Java](https://img.shields.io/badge/logo-apache-yellow?logo=apache-maven)](https://www.apache.org/foundation/marks/)
[![License](http://img.shields.io/:license-apache-green.svg?style=flat)](https://www.apache.org/licenses/)
[![Hits](https://hits.sh/github.com/blueokanna/ReverseCoin.git.svg?color=fe7d37)](https://hits.sh/github.com/blueokanna/ReverseCoin.git/)

> In this project, we use the **Apache 2.0** open source protocol, detailed instructions will be introduced in the **Wiki**, and if you like this project, you may want to click a **Star** ⭐.

## 1. The Basics of Blockchain

Blockchain is a decentralised distributed database technology, and its core principles include the following key concepts:

1. **Decentralised and Distributed Storage**: instead of having a centralised governing body, blockchain is maintained and managed by many nodes in the network working together. These nodes all store copies of the same data, forming a distributed database.

2. **Block**: A block is the basic unit in a blockchain that contains transaction data and other metadata. These transaction data can be cryptocurrency transfer records or smart contract execution results, etc. Each block contains information from the previous block, forming a chain structure.

3. **Cryptographic Hash Function**: Blockchain uses a cryptographic hash function to convert the information in a block into a fixed-length string. This hash not only verifies the integrity of the data, but also ensures that the data is not tampered with.

4. **Consensus Mechanism**: In order to ensure that the nodes in the network agree on the state of the blockchain, the blockchain uses various consensus mechanisms, such as Proof of Work (PoW), Proof of Stake (PoS), and so on. Consensus mechanisms ensure that the addition of new blocks requires unanimous agreement in the network.

5. **Distributed Bookkeeping**: once a new block is added to the chain by consensus, all nodes update their local copies so that the entire network maintains a consistent state.

    ![1](https://github.com/blueokanna/ReverseCoin/assets/56761243/a13814ed-51cb-4d8d-8f83-d8cceb3ff10f)

----
For more details, you can read the **WiKi** page: [ReverseCoin User Manual](https://github.com/blueokanna/ReverseCoin/wiki/PetaBlock-Blockchain-User-Manual)

You can report vulnerabilities in the project at Github Issues. This side will also try to ensure that issues are handled in a timely manner, but depending on the time impact of different time zones, it may take a while to respond or update the bug or vulnerability code in a timely manner.

Finally, we would to thank the developers of the **洋葱骑士/dce-blockchain** project for the reference and Java open source **Maven** library used in this project.
