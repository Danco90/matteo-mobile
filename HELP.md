#### Configuration :

You can easily configure your fonoapi url path from property file (src/main/resources/application.properties)
It's all about the FonaAPI URL along with the path of the url to generate a token needed to access the Api.
Default response is considered in the event of service down.

Here are the required details: 

It should query the API to 
a. create an order to book a device by passing the following input parameters
- brand - Pass the Mobile Device Brand (example : "Samsung", "Apple")
- device - Pass nearly relevant mobile device name (example : "Galaxy S8" or "iPhone 11)
a.1) check first if the mobile availability in the stock/Store.The order will succeed only if the given mobile phone is available.
a.2) Return the order response with the mobile availability along with its caracteristics.

b. return a device (previous update order) by passing the following inputs
- orderId (found in the order)
- productId (for the book order)
b. respond with a confirmation message with returnDate. An error is returned in the event of wrong input.

● User 1 submit a booking request for a given mobile phone by passing the following input parameters brand and/or deviceName)
curl -X POST localhost:8080/orders -H 'Content-type:application/json' -d '{"customerName": "Enrico","customerSurname": "Bianchi","brand": "Samsung", "model": "Galaxy S8"}'
Outcome: {"deviceName":"Galaxy S8","brand":"Samsung","available":true,"technology":"GSM / CDMA / HSPA / EVDO / LTE","bookingDate":"2023-07-16T13:14:29.187784","customer":{"id":1,"name":"Matteo","surname":"Daniele"},"_2g_bands":"1900 MHz","_3g_bands":"850 MHz","_4g_bands":"2300 MHz"}


● User 2 submits a booking request for same mobile phone typology
curl -X POST localhost:8080/orders -H 'Content-type:application/json' -d '{"customerName": "Enrico","customerSurname": "Bianchi","brand": "Samsung", "model": "Galaxy S8"}'

● Additional user submits a booking request for a mobile no longer available
curl -X POST localhost:8080/orders -H 'Content-type:application/json' -d '{"customerName": "Elio","customerSurname": "Profumo","brand": "Samsung", "model": "Galaxy S8"}'

● User submits a request for returning the reserved item given orderId and itemId. If successful, item returns again available
curl -X PUT localhost:8080/orders/1 -H 'Content-type:application/json' -d '{"itemId": 2 }'

#### Clone project
Open the command prompt/Terminal to run the code clone the repository using below command -
git clone https://github.com/Danco90/matteo-mobile.git

#### Compile the code-
mvn clean install


#### Unit test
mvn test

#### Test the Application by running the executable in a terminal :
cd $PROJECT/target
java -jar matteo-bt-mobile-0.0.1-SNAPSHOT.jar

or simply run the MatteomobileApplication.java in your IDEA

### H2 Database
1. After running the spring boot app,
   login to the console
* url: http://localhost:8080/h2-console
* login: jdbc:h2:mem:testdb

2. Monitor and validate the data (post rest service call ) by executing the following queries in bulk

* SELECT * FROM CUSTOMER ;
* SELECT * FROM MOBILE ;

* SELECT * FROM MOBILE_ORDER ;

* SELECT * FROM ORDER_MOBILE_RELATIONSHIP ;