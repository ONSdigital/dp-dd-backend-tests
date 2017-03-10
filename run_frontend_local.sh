#!/usr/bin/env bash
echo "running data discovery frontend tests"
mvn clean test -P FrontEnd-Tests -Dbase_url=http://localhost:20000/dd
