#!/usr/bin/env bash

mvn clean test -Dfilename=Open-Data-v3_E2E_Tests.csv -Dtest=**/APIIntegrityTest.java
mvn test -Dtest=**/APITest.java -Dbackend=real
mvn test -Dtest=**/CSVFilterTest.java -Dfilename=Open-Data-v3_E2E_Tests.csv
mvn test -Dtest=**/ValidateAPIStubTest.java


