#!/usr/bin/env bash

mvn clean test -Dtest=**/APIIntegrityTest.java -Dfilename=Open-Data-v3_E2E_Tests.csv
mvn clean test -Dtest=**/APIIntegrityTest.java -Dfilename=AF001EW_v3_E2E_Tests.csv
mvn test -Dtest=**/CSVFilterTest.java -Dfilename=Open-Data-v3_E2E_Tests.csv
mvn clean test -Dtest=**/APIIntegrityTest.java -Dfilename=AF001EW_v3_E2E_Tests.csv
#mvn test -Dtest=**/APITest.java -Dbackend=real

#mvn test -Dtest=**/MetaDataEditorTest.java -Dfilename=Open-Data-v3_E2E_Tests.csv
#mvn test -Dtest=**/ValidateAPIStubTest.java


