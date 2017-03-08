#!/usr/bin/env bash

mvn clean test -Dtest=**/APIIntegrityTest.java -Dfilename=Open-Data-v3_Local.csv -Denv=local
STATUS=$?
if [ $STATUS -eq 0 ]; then
echo "Open-Data-v3_Local.csv API Integrity tests passed"
echo "Open-Data-v3_Local.csv Triggering CSV Filter tests passed"
mvn test -Dtest=**/CSVFilterTest.java -Dfilename=Open-Data-v3_Local.csv -Denv=local
STATUS=$?
else
echo "*** Tests Failed *** "
fi
if [ $STATUS -eq 0 ]; then
echo "Open-Data-v3_Local.csv CSV Filter tests passed"
echo "AF001EW_v3_small_Local.csv Triggering API Integrity tests"
mvn test -Dtest=**/APIIntegrityTest.java -Dfilename=AF001EW_v3_small_Local.csv -Denv=local
else
echo "*** Tests Failed *** "
fi
if [ $STATUS -eq 0 ]; then
echo "AF001EW_v3_small_Local.csv API Integrity tests passed"
echo "AF001EW_v3_small_Local.csv Triggering AF001EW_v3_small_Local.csv.."
mvn test -Dtest=**/CSVFilterTest.java -Dfilename=AF001EW_v3_small_Local.csv -Denv=local
else
echo "*** Tests Failed *** "
fi
if [ $STATUS -eq 0 ]; then
echo "AF001EW_v3_small_Local.csv CSV Filter tests passed"
echo "*** ALL TESTS PASSED ***"
else
echo "*** Tests Failed *** "
fi
#mvn clean test -Dtest=**/APIIntegrityTest.java -Dfilename=Open-Data-v3_Local.csv -Denv=local
#mvn test -Dtest=**/CSVFilterTest.java -Dfilename=Open-Data-v3_Local.csv -Denv=local
#mvn test -Dtest=**/APIIntegrityTest.java -Dfilename=AF001EW_v3_small_Local.csv -Denv=local
#mvn test -Dtest=**/CSVFilterTest.java -Dfilename=AF001EW_v3_small_Local.csv -Denv=local

