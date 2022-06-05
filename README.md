# API Test Automation Example with Rest-assured 
![RA](https://rest-assured.io/img/name-transparent.png)

## Overview
This automation example is uses the sample API Server **Pet Store** to domestrate the API Test Automation.
The project uses the Java **Rest Assured** package for testing automating the REST API  services.

## API Sample Server
This API automation is based on the pet store sample hosted at https://petstore3.swagger.io. 

## Requirements
- Java JDK 8+
- Apache Maven
- Rest Assured Library (automatically downaloaded with the Project POM)

## Test Scenarios

|Level	| Resource	| Scenario	| Pros/Cons |
|-----	| --------  | ---------	| ---------	|
|Unit (Component) | Pet | Add Pet to store | Pro: Contract service can be tested throughly <br/> Con : this is con |	
||Pet |	Get pet by Status|	Pro - Each pet data can be validated <br/>Con - Pets with required status should exists" |
||Store|	Create New Order For Pet|	Pro - New Order service can be tested fully <br/>Con: Pets should exists in store"|
||User	|Get User Login To System |	Pro - User login can be used as sanity check |
||||
|Integration|	Pet, Store|	Add Pet to Store and Place Order|	Pro: Integration of two (or more) resources is validated <br/>Con: Indvidual service working is pre-requiste"|
||||
|EndToEnd |	User, Pet, Store |	User Login,Search Pet and PlaceOrder |	Pro: System level testing is valided <br/>Con: Depends on integration of services"|
 
## Automation Framework
- Automation framework is based on Rest-assured library functions
- Java Objects are created for request payload and response
- JSON Requests are Serialized with Java Objects & JSON Responses are DeSerailized with Java Objects
- Rest-assured RequestSpecification object builds the requests from Java Request Object and send the requests to API server
- Rest-assured Response object receives the repsonse from the API serves and deserialized to Java Response Object
- Java Response Object data is validated using Assertions of AssertJ Library

## Automation Project Structure
 - /src/main/java/testbase 
    - TestBase and Configuration Java Classes
    - /endpoints  - To send different types of requests
    - /models  - Requests and Response Java Data Objects
 - /src/test/java
    - /components - Individual resouce level api tests
    - /integration - Integration tests of more than one resource
    - /e2e - End to end usecase tests
 - /src/test/resouces
    - config
    - files

## Executing Tests
- Check the API Pet Store Sample Server is running
- Clone or download this repository
- Open cmd & cd to the repository directory
- Build the project - `mvn install`
- To Run all tests - `mvn clean test`
- To Run specific tests update POM configuration or use pattern - `mvn test -Dtest=Store*Test`
- To Run Tests and get HTML report  - `mvn clean site`

## Automation Report 
 - HTML report is generated in project's `/target/site/surefire-report.html`
 - XML report is generated in project's `/target/surefire-reports`

## Logs
 - Console log is generated ins system's console output
 - Log file is generated in project's `/target/logs/Info.log` 
