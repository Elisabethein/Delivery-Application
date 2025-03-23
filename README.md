# Delivery Fee Calculation Application

This is a Spring boot project written in Java. The project is for the internship trial task at Fujitsu Estonia.

## Project Description

The project is a delivery fee calculation application. The project is a RESTful web service that calculates the delivery fee based on the input parameters.
The input parameters are:
- City
- Vehicle type
- Datetime (optional, format: yyyy-MM-dd HH:mm:ss)

## Project Structure

The project has only backend part. The project has the following packages:

1. Components
   * Contains the Data Initialization component, which initializes the data for the application.
   * The weather data is fetched from ilmateenistus.ee and saved to the database. This is a cron job that runs every 60 minutes, 15 minutes past the hour.
   * Additionally, base fee, extra fee, vehicle, and station data is initialized.
2. Configurations
    * Contains the security configuration and the RestTemplate configuration.
    * The security configuration allows access from all origins and disables CSRF. If the application is deployed to a production environment, the security configuration should be updated, and the CORS configuration should be more restrictive.
3. Controllers
    * Contains the controller for the delivery fee calculation, and partial CRUD operations for the base and extra fees.
    * BaseFeeController contains the GET, UPDATE and DELETE operations for the base fee.
    * ExtraFeeController contains the GET, UPDATE and DELETE operations for the extra fee.
    * DeliveryFeeController contains the GET operation for the delivery fee calculation.
4. Entities
    * Contains the entities for the base fee, extra fee, station, vehicle, and weather.
    * Weather entity is used to store the weather data fetched from ilmateenistus.ee.
    * BaseFee entity is used to store the base fee data.
    * ExtraFee entity is used to store the extra fee data.
    * Station entity is used to store the station data.
    * Vehicle entity is used to store the vehicle data.
    * ExtraFeeVehicleMapping entity is used to store the mapping between the extra fee and the vehicle.
5. Enums
    * Contains the enums for the city, vehicle types, and extra fee rule types.
    * City enum contains the cities where the delivery fee calculation is available.
    * VehicleType enum contains the vehicle types.
    * ExtraFeeRuleType enum contains the extra fee rule types.
    * Enums were used to restrict the input.
6. Exceptions
    * Contains the custom exceptions for the application.
    * Exceptions include type not found exceptions and invalid input exceptions.
7. Repositories
    * Contains the repositories for the base fee, extra fee, station, vehicle, and weather.
8. Services
    * Contains the services for the base fee, extra fee, station, vehicle, and weather.
9. Utils
    * Contains the utility class for mapping cities to stations.

Personal Notes regarding the structure and logic:
- The application allows access from all origins. If the application was developed for a production environment, the access should be restricted (e.g., only allow access from the frontend application), and possibly use JWT for authentication.
- The CRUD operations for the base and extra fees are partial. The application does not have the create operation for the base and extra fees as the vehicle types and stations are predefined. If the application was developed for a production environment, the create operation should be added for the base and extra fees, and the vehicle types and city data should not be used as predefined enums.
- The application uses enums to restrict the input. If the application was developed for a production environment, the enums should be replaced with database tables.

## API Endpoints

Base URL: `http://localhost:8080/api`

The project has the following API endpoints:

1. Delivery Fee Calculation
* GET `/delivery-fee`
* Description: Calculates the delivery fee based on weather conditions, city, and vehicle type.
* Parameters:
  * city: City (String, required)
  * vehicleType: Vehicle type (String, required)
  * datetime: Datetime (String, optional, format: yyyy-MM-dd HH:mm:ss)
* Responses:
   * 200 OK: Returns the delivery fee.
   * 400 Bad Request: Returns an error message on invalid parameters or missing data.
   * 500 Internal Server Error: Returns an error message on internal server error.

2. Base Fee Management

GET `/base-fee/all`
* Description: Returns all base fees.
* Responses:
   * 200 OK: Returns all base fees.


PUT `/base-fee/update`
* Description: Updates the base fee for a given city and vehicle type.
* Parameters:
  * city: City (String, required)
  * vehicleType: Vehicle type (String, required)
  * fee: Fee (Double, required)
* Responses:
   * 200 OK: Returns the updated base fee.
   * 400 Bad Request: Returns an error message on invalid parameters or missing data.
   * 500 Internal Server Error: Returns an error message on internal server error.


DELETE `/base-fee/delete`
* Description: Deletes the base fee for a given city and vehicle type.
* Parameters:
  * city: City (String, required)
  * vehicleType: Vehicle type (String, required)
* Responses:
   * 200 OK: Returns a success message.
   * 400 Bad Request: Returns an error message on invalid parameters or missing data.
   * 500 Internal Server Error: Returns an error message on internal server error.

3. Extra Fee Management

GET `/extra-fee/all`
* Description: Returns all extra fees.
* Responses:
   * 200 OK: Returns all extra fees.


PUT `/extra-fee/update`
* Description: Updates the extra fee for a given city, vehicle type, and rule type.
* Parameters:
  * city: City (String, required)
  * vehicleType: Vehicle type (String, required)
  * ruleType: Rule type (String, required)
  * fee: Fee (Double, required)
* Responses:
   * 200 OK: Returns the updated extra fee.
   * 400 Bad Request: Returns an error message on invalid parameters or missing data.
   * 500 Internal Server Error: Returns an error message on internal server error.


DELETE `/extra-fee/delete`
* Description: Deletes the extra fee for a given city, vehicle type, and rule type.
* Parameters:
  * city: City (String, required)
  * vehicleType: Vehicle type (String, required)
  * ruleType: Rule type (String, required)
* Responses:
   * 200 OK: Returns a success message.
   * 400 Bad Request: Returns an error message on invalid parameters or missing data.
   * 500 Internal Server Error: Returns an error message on internal server error.

   
## Technologies

The project uses the following technologies:

1. Java 23
2. Spring Boot
3. Gradle
4. H2 Database
5. Docker

## Tests

The project has unit tests for the controllers. The tests are written using JUnit and Mockito.
The business logic of calculating the delivery fee, and CRUD operations for the base and extra fees are tested.

## How to Run the Application

### Running the Application Locally

1. Clone the repository.
2. Open the project in an IDE.
3. Run the application.
4. The application will start at `http://localhost:8080`.
5. Use Postman or any other API testing tool to test the API endpoints.

### Running the Application in Docker

1. Clone the repository.
2. Open the terminal.
3. Run the following command in the root of the project:
    ```
    docker-compose up -d
    ```
4. The application will start at `http://localhost:8080`.
5. Use Postman or any other API testing tool to test the API endpoints.


   In case you want to stop the application, you can run the following command:
   ```
    docker-compose down
   ```
