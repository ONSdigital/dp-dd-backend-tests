#!/usr/bin/env bash
echo "Running Front End Smoke Tests on Discovery"
mvn clean test -Dtest=**/ArmedForcesTests.java -Dbase_url=https://discovery.onsdigital.co.uk/dd
