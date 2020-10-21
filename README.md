[![Build Status](https://travis-ci.com/hatf0/partiql_jdbc.svg?branch=master)](https://travis-ci.com/hatf0/partiql_jdbc)
[![codecov](https://codecov.io/gh/hatf0/partiql_jdbc/branch/master/graph/badge.svg)](https://codecov.io/gh/hatf0/partiql_jdbc)

# PartiQL JDBC Driver
This is an experimental PartiQL JDBC Driver. 

## Status
As stated previously, this driver should be considered experimental, and is nowhere near production-ready. 

The API itself is not subject to change (as JDBC drivers have a stable API), however, major internal components may be subject to change (and massive refactoring). 

## Building
To build this project, in a new terminal window, run:
```bash
$ ./gradlew build
```
This will build the driver, and produce a .jar file in the `build/libs` directory.

