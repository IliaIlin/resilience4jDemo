# Resilience4j Demo

## Prerequisites
Java

## How to build and run the code
From the root of repository execute following to build the jar file:
`./gradlew build` for Unix and `gradlew.bat build` for Windows

To run execute the following:
`java --add-opens=java.base/java.net=ALL-UNNAMED --add-opens java.base/sun.net.www.protocol.https=ALL-UNNAMED -jar Resilience4jDemo-1.0-SNAPSHOT-standalone.jar`