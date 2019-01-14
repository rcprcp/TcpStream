# TcpStream
Writes records to specific, local, tcp port.  I use this for testing the TCP Server Origin on StreamSets Data Collector.

Currently, there are no command line parameters. Maybe we'll change that.

Modify the record count and the port in the source, recompile with `mvn clean install` and you should have an executable jar "tcpstream-1.0-SNAPSHOT-jar-with-dependencies.jar" in the target sub directory.  

Run the program with `java -jar tcpstream-1.0-SNAPSHOT-jar-with-dependencies.jar`
