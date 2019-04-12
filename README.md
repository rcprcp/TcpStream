# TcpStream
Writes records to specific, local, tcp port.  I use this for testing the TCP Server Origin on StreamSets Data Collector.

Currently, there are no command line parameters. It has been enhanced to use a command file.
The PORT number is still hard coded as 55,555 

Build: 
    
    http://github.com/rcprcp/TcpStream.git
    cd TcpStream
    mvn clean package
    cd target

To run the program interactively: `java -jar tcpstream-1.0-SNAPSHOT-jar-with-dependencies.jar`

Or run it from the command line with an input file to configure the tests: 
`nohup java -jar tcpstream-1.0-SNAPSHOT-jar-with-dependencies.jar < tcp-stream-input.txt &`
(output to nohup.out by default)
