#!/bin/bash

CLASSPATH=target/jigasi-1.1-SNAPSHOT.jar
CLASSPATH=${CLASSPATH}:../.m2/repository/org/apache/httpcomponents/httpcore/4.4.15/httpcore-4.4.15.jar
CLASSPATH=${CLASSPATH}:../.m2/repository/org/apache/httpcomponents/httpclient/4.5.13/httpclient-4.5.13.jar
CLASSPATH=${CLASSPATH}:../.m2/repository/org/jitsi/jitsi-utils/1.0-126-g02b0c86/jitsi-utils-1.0-126-g02b0c86.jar
CLASSPATH=${CLASSPATH}:../.m2/repository/commons-logging/commons-logging/1.2/commons-logging-1.2.jar
# CLASSPATH=${CLASSPATH}:../.m2/repository/com/google/http-client/google-http-client-gson/1.41.7/google-http-client-gson-1.41.7.jar
CLASSPATH=${CLASSPATH}:../.m2/repository/com/google/code/gson/gson/2.9.0/gson-2.9.0.jar

echo "============================================================="

java -cp ${CLASSPATH} org.jitsi.jigasi.transcription.CustomRemoteTranslationService $1 $2

echo "============================================================="

java -cp ${CLASSPATH} org.jitsi.jigasi.transcription.CustomLocalCTranslationService "http://localhost:5000/translate" DUMMY

echo "============================================================="

