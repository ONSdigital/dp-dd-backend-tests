dp-dd-backend-test
====================
Integration tests for backend components - CSV Chopper, DB Uploader and API.

Pre-requisites
==============
Edit the values in the local_config.yml to point to the following apps
1. CSV Splitter
2. DB-Postgres
3. API Endpoint
Ensure that the above apps are running.
Kafka and zookeeper are set up
The CSV file is uploaded to the S3 bucket
Include the CSV file under /resources/csvs

To clone the project

````
git clone https://github.com/ONSdigital/dp-dd-backend-test 
````

To run the tests
````
mvn clean test
````


Runs the backendTest

Init Setup
==========
Deletes the following tables:
````
dimensional_data_point
dimensional_data_set
dimensional_data_set_concept_system
````

What it does
============

Gets the number of rows from the CSV file
Chops the CSV file and populates the Kafka queue
DBLoader reads the Kafka queue and uploads the database
Asserts the database rows and the rows in the CSV file
Calls the dataset api and confirms the title of the dataset 