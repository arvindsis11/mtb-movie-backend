
# Movie booking app--fix readme

Online Shopping Application allows the customers to register and login and search products. Admin shall view the orders placed and add/delete any products to the online system and update the product availability status

The core modules of online shopping app are:

1. Customer registration and login

2. Search the products

3. Admin can view the product and update the product status

4. Admin to add new products to the system


# Steps to run api:



#### STEP 1: DOWNLOAD AND INSTALL KAFKA
https://kafka.apache.org/downloads

#### STEP 2: START THE KAFKA ENVIRONMENT
#### 2.1 Start the ZooKeeper service
```bash
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```

#### 2.2 Start the Kafka broker service
```bash
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

#### STEP 3: CREATE A TOPIC TO STORE YOUR EVENTS
```bash
#.\bin\windows\kafka-topics.bat --create --topic topic_demo --bootstrap-server localhost:9092
```
#note: no need to create topics it's integrated into api

#### STEP 4: WRITE SOME EVENTS INTO THE TOPIC
```bash
.\bin\windows\kafka-console-producer.bat --topic topic_demo --bootstrap-server localhost:9092
```
note: no need to push messages into topic it's integrated into api
hello world
topic demo

#### STEP 5:  READ THE EVENTS:::IMPORTANT
```bash
.\bin\windows\kafka-console-consumer.bat --topic topic_demo --from-beginning --bootstrap-server localhost:9092
```
hello world
topic demo

#### STEP 6: START THE MICROSERVICE

#### Logging:
#### STEP-1: START ELASTIC SEARCH
```bash
C:\LoggingTools\elasticsearch-8.5.2\bin>elasticsearch.bat
```

#### STEP-2:START LOGSTASH
```bash
C:\LoggingTools\logstash-8.5.2\bin>logstash.bat -f logstash.conf
```


## API Reference

#### Get all items

```http
  POST /api/v1.0/shopping/register
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `api_key` | `string` | **Required**. Your API key |

#### Get item

```http
 GET /api/v1.0/ shopping /login
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `string` | **Required**. Id of item to fetch |

```http
GET /api/v1.0/ shopping /<customername>/forgot
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `string` | **Required**. Id of item to fetch |


```http
 POST /api/v1.0/ shopping /<productname>/add
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `string` | **Required**. Id of item to fetch |

```http
 PATCH /api/v1.0/shopping /<productname>/update/<id>
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `string` | **Required**. Id of item to fetch |

```http
DELETE /api/v1.0/ shopping /<productname>/delete/<id>
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `productname`      | `string` | **Required**. Id of item to fetch |




## 🚀 About Me

