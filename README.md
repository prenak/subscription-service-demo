# Demo Subscription Service
#### Demo Subscription Service is a Spring Boot application that provides REST interfaces for subscribing to the products and manage user subscriptions. It supports the following functionalities:

## Functionalities
- Fetch the list of available products and filter them based on the applicable voucher code.
- Fetch the information on a single product.
- Subscribe to a product with a voucher code if applicable.
- Fetch all the subscriptions made by the customer.
- Pause, resume and cancel the subscription on a product.

## Technology Stacks
- Java 11
- Spring Boot 2.4
- Spring Security with Basic Authentication
- Spring Data JPA
- Hibernate
- H2 In-memory Db, MySQL
- Maven
- Docker and Docker-compose

> This project supports 2 profiles: 'local' and 'qa'. Both profiles support injecting the mock data into the db on start-up.

## How to  Run
###Starting the application in local:
To start the application in local, run com.tst.shop.subscription.SubscriptionServiceApplication as Java application on passing environment variable: spring.profiles.active=local.

### Run in Docker
You can build the image and run the container with Docker. Application would run with 'qa' profile in Docker.
1. Build project
```bash
cd  subscription-service-demo
mvn clean package
```
2. Build images and run containers
```bash
docker-compose  -f  docker-compose.yaml  up
```
### Application monitoring:
Application health and info can be monitored via actuator management context.
- http://localhost:8089/actuator/health
- http://localhost:8089/actuator/info

## REST Endpoints
###1.	GET  /api/product[?voucherCode=FLAT10]
Retrieves the list of products. Also, products can be filtered based on a valid voucher code. Returns an error if the voucher code is invalid.  
Auth: None  
Example: http://localhost:8089/api/product OR http://localhost:8089/api/product?voucherCode=NY25OFF

###2.	GET  /api/product/{productId}
Fetch information related to a single product. Returns an error if product id is invalid.  
Auth: None  
Example: http://localhost:8089/api/product/5

###3.	POST   /api/subscription/subscribe
Subscribe to a product with a voucher code if applicable. This endpoint is open to only premium and trial customers. Returns error if –
- Invalid customer or customer has no role as mentioned below.
- Product not found.
- Voucher code is invalid.
- Customer has already subscribed to this product.

Auth: Authenticated customer having role – CUSTOMER, CUSTOMER_ON_TRIAL  
Example: http://localhost:8089/api/subscription/subscribe  (Refer API reference guide document for more info)

###4.	GET   /api/subscription
Fetch all the subscriptions made by the customer. Returns error if invalid customer or customer has no roles as mentioned below.  
Auth: Authenticated customer having role – CUSTOMER, CUSTOMER_ON_TRIAL  
Example: http://localhost:8089/api/subscription  (Refer API reference guide document for more info)

###5.	PATCH   /api/subscription/{subscriptionId}/pause
Pause a valid subscription made by the customer. This endpoint is open to premium customers only.
Returns error if –
- Invalid customer or customer has no role as mentioned below.
- Subscription not linked to the customer actioning it.
- Subscription is cancelled.
- Customer has already paused this subscription.  
  Auth: Authenticated customer having role – CUSTOMER  
  Example: http://localhost:8089/api/subscription/1/pause  (Refer API reference guide document for more info)

###6.	PATCH   /api/subscription/{subscriptionId}/resume
Resume a valid subscription previously paused by the customer. This endpoint is open to premium customers only.
Returns error if –
- Invalid customer or customer has no role as mentioned below.
- Subscription not linked to the customer actioning it.
- Subscription is cancelled.
- Subscription is already ongoing.  
  Auth: Authenticated customer having role – CUSTOMER  
  Example: http://localhost:8089/api/subscription/1/resume  (Refer API reference guide document for more info)

###7.	DELETE   /api/subscription/{subscriptionId}
Cancel a valid and active subscription. Returns error if invalid customer or customer has no roles as mentioned below. Returns error if –
- Invalid customer or customer has no role as mentioned below.
- Subscription not linked to the customer actioning it.
- If subscription is cancelled already.  
  Auth: Authenticated customer having role – CUSTOMER, CUSTOMER_ON_TRIAL  
  Example: http://localhost:8089/api/subscription/1  (Refer API reference guide document for more info)

##H2 In-memory DB:
Local profile uses H2 in memory database to store the data which can be accessed using H2 console.  
To open the H2 console, open http://localhost:8089/api/h2 in the browser.  

##Demo-Subscription-Service-API-Reference-Guide.pdf
> For more details please refer Demo-Subscription-Service-API-Reference-Guide.pdf document.  






