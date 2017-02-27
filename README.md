# Tomcat Instance Generator Utility Overview
This project provides an easy approach to generating a new Apache Tomcat Instance for your use in
'Your' Eco-System.

This project uses JavaFX to render a UI to allow a user to enter various Tomcat settings and properties and 
to generate a new Tomcat Instance based upon the setting entered.

Upon Generation of a new Instance, a yaml file will be generated to represent a Tomcat Instance.  This yaml file,
can then be used as input to generating subsequent Tomcat Instances, by specifying the yaml file as input to
the CLI utility or can easily be imported using the UI.

Provides easy configuration of a Tomcat Instance.
* GUI Mode -- Intuitive Tabbed UI for easy configuration of Key Areas
  * Can Import YAML Tomcat Instance Configurations
  When Instance Generated, YAML Configuration Exported.
* CLI Mode -- Driven by reading YAML Tomcat Instance Configurations
* Runs on WIN or *NIX Environments

##Configuration Aspects
* Instance Name, Environment, Tomcat Version
* Ports
* SSL Security
* JVM Options, including core JVM Memory Allocations
* Instance Properties
* Instance Management Properties
* YAML Configuration Management

##Customizations
* Creates any new Directories in Instance Head
  * 'properties' 
* Adds Additional Libraries into 'lib' Directory
* Adds Additional 'webapps' Directory
* Updates 'manager' App Configuration to allow Remote Deployments.
* Configures Files in Tomcat 'conf' Directory, server.xml ...
* Generates new 'setenv.bat', 'setenv.sh' scripts for customizing Environment

##Errata
* No Full Validation against Memory Configuration selected!
* Static Definitions:
  * Apache Mirror used: http://apache.mirrors.pair.com/tomcat
  * ManageCat License Key
* Internet access required, as initial copy of Apache Tomcat is Downloaded if not detected from previous generation.
* Currently only V8.5.11 is available for download.
  * To add a version, simple modify the 'Default Definitions' class to add the new version specifications.
    * Name, Length of Archive, SHA-1 Hash of Artifact.
* Tool has capability of multiple versions, but V9 is not at a complete or final milestone.
* ManageCat: Restart Agent still needs some manual changes.

## Local Setup
Simply ensure you have the necessary Pre-requisites and you should be good to go...

### Pre-requisites

1. Java [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
2. Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files for Java 8 should be
applied.  [Download Here.](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
3. Maven

## Building
Building primary Artifact, simple perform the following command:
   {{{ 
      mvn clean package 
   }}}

## Running
Use the applicable scripts in the 'bin' directory.  To Bootstrap the GUI on
Windows Environment, please use the pre-built executable to properly Bootstrap JavaFX.


![Tomcat Instance Generator UI](https://raw.githubusercontent.com/jaschenk/Tomcat-Instance-Generator/doc/images/TomcatInstanceGenerator_01.png)

