#!/usr/bin/env bash
mvn clean test -PBackEnd-Tests -Dfilename=Open-Data-v3_E2E_Tests.csv -Denv=local
mvn test -PBackEnd-Tests -Dfilename=AF001EW_v3_E2E_Tests.csv -Denv=local




