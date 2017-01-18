#!/usr/bin/env bash
mvn clean test -Dfileupload=https://upload.publishing.discovery.onsdigital.co.uk -Dfilename=ReMapped-AF001EW.csv -Dtest=**/APIIntegrityTest.java
