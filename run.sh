#!/usr/bin/env bash
mvn clean test -Dfileupload=https://upload.publishing.discovery.onsdigital.co.uk -Dfilename=ReMapped-AF001EW.csv -Dtest=**/APIIntegrityTest.java -Dbackend=real
mvn test -Dtest=**/APITest.java -Dbackend=real
mvn test -Dtest=**/CSVFilterTest.java -Djobcreator=https://discovery.onsdigital.co.uk/dd/api/jobs -Dbackend=real