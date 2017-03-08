#!/usr/bin/env bash

mvn clean test -Dtest=**/APIIntegrityTest.java -Dfilename=Open-Data-v3_Local.csv -Denv=local
mvn test -Dtest=**/CSVFilterTest.java -Dfilename=Open-Data-v3_Local.csv -Denv=local




