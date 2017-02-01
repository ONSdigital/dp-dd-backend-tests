#!/usr/bin/env bash

mvn clean test -Dfileupload=https://upload.publishing.discovery.onsdigital.co.uk -Dfilename=AF001EW-sample.csv -Dtest=**/APIIntegrityTest.java -Dbackend=real
mvn test -Dtest=**/APITest.java -Dbackend=real
mvn test -Dtest=**/CSVFilterTest.java -Djobcreator=https://discovery.onsdigital.co.uk/dd/api/jobs -Dbackend=real
mvn test -Dtest=**/ValidateAPIStubTest.java


