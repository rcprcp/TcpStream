# TcpStream
Writes records to specific, local, tcp port.  I use this for testing the TCP Server Origin on StreamSets Data Collector.

Currently, there are no command line parameters. It has been enhanced to use a command file. 

Compile with `mvn clean install` and you should have an executable jar "tcpstream-1.0-SNAPSHOT-jar-with-dependencies.jar" in the target sub directory.  

To run the program interactively: `java -jar tcpstream-1.0-SNAPSHOT-jar-with-dependencies.jar`

Or run it from the command line with an input file to configure the tests: 
`nohup java -jar tcpstream-1.0-SNAPSHOT-jar-with-dependencies.jar < tcp-stream-input.txt &`
(output to nohup.out by default)
