#!/usr/bin/env bash

echo "running data discovery frontend tests"
mvn clean test -P FrontEnd-Tests
