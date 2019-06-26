#! /bin/sh

BASE=`dirname $0`
CP="$BASE"/zip4j-2.0.jar:"$BASE"/deepunzip-1.0-SNAPSHOT.jar
java -classpath "$CP" ru.zinal.deepunzip.Tool $@

# End Of File
