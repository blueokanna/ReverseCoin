# ReverseCoin 区块链项目 - [English Doc](https://github.com/blueokanna/ReverseCoin/blob/main/README.md)

[![Apache Java](https://img.shields.io/badge/logo-apache-yellow?logo=apache-maven)](https://www.apache.org/foundation/marks/)
[![License](http://img.shields.io/:license-apache-green.svg?style=flat)](https://www.apache.org/licenses/)
[![Hits](https://hits.sh/github.com/blueokanna/ReverseCoin.git.svg?color=fe7d37)](https://hits.sh/github.com/blueokanna/ReverseCoin.git/)

> 在本项目中使用的是 **Apache 2.0** 开源协议，详细的使用说明方法将会在 **Wiki** 里面为大家介绍 (非 Springboot 的Java 项目)，同时喜欢这个项目的朋友不妨点个 **Star** ⭐

## 1. 区块链的基本原理

区块链是一种去中心化的分布式数据库技术，它的核心原理包括以下几个关键概念：

1. **去中心化和分布式存储**：区块链没有一个中心化的管理机构，而是由网络中的许多节点共同维护和管理。这些节点都存储着相同的数据副本，形成一个分布式的数据库。

2. **区块**：区块是区块链中的基本单位，它包含着交易数据和其他元数据。这些交易数据可以是加密货币的转账记录或者智能合约的执行结果等。每个区块都包含了前一个区块的信息，形成了一个链式结构。

3. **加密哈希函数**：区块链使用加密哈希函数将区块中的信息转化为固定长度的字符串。这个哈希值不仅可以验证数据的完整性，还可以确保数据的不可篡改性。

4. **共识机制**：为了确保网络中的节点对区块链的状态达成一致，区块链采用各种共识机制，如工作量证明（PoW）、权益证明（PoS）等。共识机制确保了新区块的添加需要达到网络中的一致同意。

5. **分布式记账**：一旦新的区块经过共识被加入到链上，所有节点都会更新自己的本地副本，这样整个网络就能保持一致的状态。
***
## 2. 区块链架构层级示意图

本项目的区块链使用详情可以阅读 **WiKi** 页面：[PetaBlock 区块链使用手册](https://github.com/blueokanna/ReverseCoin/wiki/PetaBlock-%E5%8C%BA%E5%9D%97%E9%93%BE%E4%BD%BF%E7%94%A8%E6%89%8B%E5%86%8C)

<p align="start">
  <img src="https://github.com/blueokanna/ReverseCoin/assets/56761243/a2b4d8bd-7bf3-41d0-9dc5-059e1fe4cd71" alt="1">
</p>

----

您可以在 Github Issue 处报告项目的漏洞。这边也会努力确保及时处理问题，但根据不同时区的时间影响，可能需要一段时间才能及时回复或者更新错误、漏洞代码。

最后需要感谢 **洋葱骑士 / dce-blockchain** 项目的参考和本项目所使用开源的 **Maven** 库开发者
