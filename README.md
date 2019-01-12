# tradesys-engine

microservices/stockmarket - microservice pulling financial data from many different providers based on reactive java streams (Spring Reactor) and streaming it down to Kafka (Java8,Spring,SpringReactor,Kafka)

microservices/storageengine - microservice pulling data from Kafka and storing it to MongoDB, also providing rest-api to view/modify historical data (Java8,Spring,Kafka,MongoDB)
