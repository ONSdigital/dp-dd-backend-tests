#!/usr/bin/env bash

mvn clean test -Dtest=**/APIIntegrityTest.java -Dfilename=Open-Data-v3_E2E_Tests.csv -Denv=local
mvn clean test -Dtest=**/APIIntegrityTest.java -Dfilename=AF001EW_v3_E2E_Tests.csv -Denv=local
mvn test -Dtest=**/CSVFilterTest.java -Dfilename=Open-Data-v3_E2E_Tests.csv -Denv=local
mvn clean test -Dtest=**/CSVFilterTest.java -Dfilename=AF001EW_v3_E2E_Tests.csv -Denv=local



