# Crypto Application

This is an demo application made for buying and selling coins.

User can register a coin and then upate the medata like price using Rest Apis.
The application also allows an user to buy and sell coins.
The record of the transactions are stored in a ledger which shows the money payed and the final balance after transactions.

## Dependencies
The project depends on Axon server. 
The Axon server used for testing is [AxonServer-2024.0.4](https://docs.axoniq.io/reference-guide/release-notes/rn-axon-server/rn-as-major-releases#release-2024.1).
Please download the server and then start it by following steps here -[Axon Server Quickstart](https://docs.axoniq.io/reference-guide/getting-started/quick-start)

## Installation

1. Please perform the following steps as mentioned below

 ```
   $ git clone git clone https://github.com/SubhabrataDas/blox-assessment.git
   $ cd blox-assessment
   $ gradle build
   $ java -jar .\build\libs\blox-assessment.jar

  ```
The above steps will do the build, run the test cases and start the server. 
