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
   $ git clone https://github.com/SubhabrataDas/blox-assessment.git
   $ cd blox-assessment
   $ gradle build
   $ java -jar .\build\libs\blox-assessment.jar

  ```
The above steps will do the build, run the test cases and start the server. 

## Services

The application exposes various services for the operations.

| Service           | Desciption                                                                                                                                
| -------------    |:--------------------------------------------------------------------------------------------------------------------------------------------      
| Coin Service     | This service can used for onboarding a coin and for updating the metadata like price                                                        
| Customer Service | A simple service to onboard a customer. The customer id is used for buying and selling of coins                                            
| Order Service    | This service is used for submitting an order for buying or selling a coin                                                                    
| Ledger Service   | This service is used finally to record the transactions done by the user. This tracks the amount payed, final balance after each transactio

The swagger for the service can be accessed here - [Swagger](http://localhost:8080/swagger-ui.html)

## User Interface

There is a simple user interface to see the apis in operation. This is developed in Angular. 
The UI is package inside the Spring Boot app. A WebFluxConfigurer has been added to allow the urls for the angular to be served from the server.

1. The first step is to create an customer. Since there is no login developed, there is a simple login button on the customers grid.
    This impersonates a login and sets the customer id as the logged identifier

![Create-Customer](https://github.com/user-attachments/assets/cc9b4cba-215c-4eee-8aa4-cc548b03b84a)

2. Then we can register a coin by clicking the [Register coin](http://localhost:8080/coin-add-page) . This will save the coin in the database. This impersonates an exchange for coins.
   The customer can then buy a coin or sell a coin to the exchange. The metadata of the coin has information like the price , how many of the coins are present and the commission to be paid
   for each transaction

   ![Register-Coin](https://github.com/user-attachments/assets/4dfa63e7-7b49-4628-ab5e-688bf7e46da6)

4. Once a coin is registered to the exchange , we can buy a coin from the exchange by placing an order. Similarly selling a coin is also possible.
   The coins bought cannot be more than the registered number of coins in the exchange.
   The coins to be sold , cannot be more than the amount coins present with the customer.

   ![Coin-Buy](https://github.com/user-attachments/assets/0117b475-e2f5-485b-a57c-206be0c504e0)
   
5. Once a order for buy/sell of a coin is submitted, we do some valiations in the backed to check the number of coins etc. In case the validations passes, a ledger is created.
   This process is synchronised using a [Saga Manager](https://docs.axoniq.io/reference-guide/v/3.1/part-ii-domain-logic/sagas). This takes care also of reverting the coins back to the
   previous state in case validations fail. The orders can be checked in the [order page](http://localhost:8080/order-status) page
   
  ![Order-Creation](https://github.com/user-attachments/assets/ff625ddd-9c2f-412d-b7b3-c0c8f1927736)

   ![Order-Status](https://github.com/user-attachments/assets/49eb9f3b-7e3a-4b1b-8517-4b41b1975d52)

6. Finally customer can go to the [Ledger](http://localhost:8080/get-customer-legder) and check the transactions done by the customer. This provides a summary of the transactions and the final state of the payments.
   This also tracks the number of coins in the customers possession. Since the operations requires the customer id, you might be asked to go the customer screen and click login for the customer for whom the operation is done.
   In case there is only one customer, clicking the login is not required. 

![Ledger](https://github.com/user-attachments/assets/3096fe49-a4d3-47b8-8e2b-4a22ec8066a5)

