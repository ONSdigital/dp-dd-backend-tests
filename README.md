dp-dd-backend-test
====================

To clone the tests:

````
git clone https://github.com/ONSdigital/dp-dd-backend-test 
````
Contains API Tests and End to End UI tests

API Tests: <File Upload-> CSV Splitter -> Database Loader-> Metadata Api>
1. Checks whether the dataset already exists
2. If the dataset does not exist, uploads the dataset from the CSV folder in the project
3. Reads the dimensions and options in the CSV
4. Compares the dimension and options in the CSV with that in the API

Filter Tests: <Job Creator-> CSV Filter ->CSV Transformer [gzip file format]>
1. Gets the Dimensions and options from the CSV
2. Randomly picks up few filter options from the dimensions and options
3. Creates a filter from those random filter options
4. Sends the JSON request of the filter to the Job Creator
5. Gets the Job ID
6. Pings for the job status every 500 seconds for the next 60 seconds
7. Fails if there is a timeout
8. If the response is a success, downloads the file
9. Validates the filtered file for all the filtered options

The test runs for these files: 'Open-data-v3_E2E_Tests.csv' & AF001EW_E2E_Tests.csv

To run on local
Ensure that all the micro services, Kafka topics are registered and running

````
./run_local.sh
````

To run on develop environment
````
./run_develop.sh
````


End to End UI Tests:

On local & develop
1. Checks for the dataset in the environment
2. If the dataset does not exist, uploads the dataset
3. Metadata Editor Tests:
    a. Checks for the dataresource. If it is not available creates it
    b. If the dataresource already exists, checks whether it is mapped to the dataset
    c. Maps the dataresource to the dataset if not mapped
4. Opens the base url and selects the title it has created while creating/mapping the dataresource
5. Downloads the complete dataset and compares the number of lines with the original file in the CSV folder
6. Filters using the customise options, downloads the file to a temp location
7. Validates the filtered file for all the filtered options
   

To run on local
Ensure that all the backend and frontend services are running
Test the backend services are working by following the instructions for the API Tests

````
./run_e2etests_local.sh
````

To run on develop environment
````
./run_e2etests_develop.sh
````   
