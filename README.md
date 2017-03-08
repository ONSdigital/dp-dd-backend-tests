dp-dd-backend-test
====================

To clone the tests:

````
git clone https://github.com/ONSdigital/dp-dd-backend-test 
````

End to End tests that tests the following:
1. Upload the dataset from the csvs folder if the Dataset is not in the API
2. Compares the 'Dimensions' and 'Filter Options' from the CSV with those in the API
3. Creates a JSON for filtering the dataset by using the values from the CSV
4. Sends the request to the job creator
5. Validates the filtered file that it contains only the filter options

On Local: runs with 'Open-data-v3_Local.csv'
On Develop: runs with 'Open-data-v3_E2E_Tests.csv' & AF001EW_E2E_Tests.csv

Pre-requisites
==============
Running it local:

Have all the micro services running locally

````
./run_local.sh
````

Running it pointing to the develop env

````
./run_develop.sh
````
