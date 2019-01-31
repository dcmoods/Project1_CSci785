# Project1_CSci785 Distributed Database

This is a java based application meant to demoonstrate the core concepts of client server architecture in a distributated environment. The application makes use of standard java Sockets, IO, and Threads.  There are three servers in total (Phone, Email, Name) and one client which is used to communicated with the servers. The name server is intended to be the entry point to the other servers from the client returning connection information.

## Getting Started

Pull down a local copy of the project and open in eclipse or any other java base idea.  

### Prerequisites

The system must have java installed.  

```
java version 1.8.0_151 or greated is required
```

### Installing

Open the project in eclipse by importing the repository.
There should be a list of the four java applications. 
TCPClient
TCPEmailServer
TCPNameServer
TCPPhoneServer

Start by lauching the server applications first.

```
 Right Click TCPEmailServer project -> "Run as" -> Java Application. 
```

Repeat for the TCPPhone and TCPName Servers, as well as the TCPClient. The Client applicaiton must be last. 

```
Right Click TCPClient project -> "Run as" -> Java Application. 
```

In the command prompt for the client enter:
```
get phone alan
```

You should see a response of:
```
701-111-2222
```

## Running the projects from JAR files

If running the servers from the complied jar files, then open a command propmt, navigate to each folder and issue the following:

```
java -jar [FileName].jar
```
This must be done for each project, so a total of four command propmts must be opened. 

## The easy way

Double click the Batch file included to Project_Jar directory and all the command prompts will launched automatically. When finished just close the windows to terminate. 

## Built With

* [Java](https://www.oracle.com/technetwork/java/javase/downloads/index.html) - Language
* [Eclipse](https://www.eclipse.org/) - IDE 

## Authors

* **Michael Moody** - *Initial work* - [dcmoods](https://github.com/dcmoods)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

