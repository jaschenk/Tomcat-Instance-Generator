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

## Runtime Configuration
* To drive the selection of Tomcat Archive's to be available for download, a new externalized configuration
  file has been added. This allows externalizing the downloadable point releases from the specified mirror location.

* Specifying this configuration is performed by a runtime Java Property named: **tc.archives**.  This property value
  must reference the fully qualified file-system path of the **YAML Tomcat Archives Configuration** file.

###  YAML Tomcat Archives Configuration Format
  ```YAML
     apacheMirrorHeadUrl: http://apache.cs.utah.edu/tomcat
     tomcatVersion_8.5.16: apache-tomcat-8.5.16 v8.5 true 9992463 13bf717a94a7b8d5296e678a70004a65f0c0409f
  ```

  * Where **apacheMirrorHeadUrl** provides the current Apache Mirror, proper mirror URL must be specified or archive may not
  be found.

  * Where **tomcatVersion_8.5.xx** represents a multi-valued row for a specific Tomcat point release archive.
    * The First parameter is the name of the actual Archive less, the ".zip" suffix.
    * The Second parameter is the Short Name of the Archive.
    * The Third parameter is an boolean, indicating if archive is available or not. Example, 'true' or 'false'.
    * The Forth parameter is the Size in bytes of the actual archive. Used to validate Download of archive.
    * The Fifth parameter is the Check Sum of the actual archive, which was generated using the
      **jeffaschenk.tomcat.util.FileCheckSumUtility** runtime utility class.
      This value is used to validate Download of archive.


## Configuration Aspects
* Instance Name, Environment, Tomcat Version
* Ports
* SSL Security
* JVM Options, including core JVM Memory Allocations
* Instance Properties
* Instance Management Properties
* YAML Configuration Management

## Customizations
* Creates any new Directories in Instance Head
  * 'properties' 
* Adds Additional Libraries into 'lib' Directory
* Adds Additional 'webapps' Directory
* Updates 'manager' App Configuration to allow Remote Deployments.
* Configures Files in Tomcat 'conf' Directory, server.xml ...
* Generates new 'setenv.bat', 'setenv.sh' scripts for customizing Environment

## Updates
* Static Definition Removed for externalized configuration:
  * Apache Mirror specified using runtime configuration YAML properties.
* Ability to add point release archives that are available for download and approved by your DevOps Organization.

## Errata
* No Full Validation against Memory Configuration selected!
* Static Definitions:
  * ManageCat License Key
* Internet access required, as initial copy of Apache Tomcat is Downloaded if not detected from previous generation.
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
Building primary Artifact, which will contain a full dependency bundle.  Simple perform the following command:
  ``` 
      mvn clean package 
  ```
  
  To build the JavaFX Artifact to execute on Windows, I have used IntelliJ to create an Artifact bundle containing an executable Windows bootstrap program wrapper.  For Linux, you simple use the supplied scripts in the distribution. 

## Running
Obtain the Release from ![here](https://github.com/jaschenk/Tomcat-Instance-Generator/releases/download/v1.0.0.2/Tomcat-Instance-Generator_distribution_20170427.zip). Use the applicable scripts in the 'app' directory of the distribution.  For Bootstrapping the GUI on Windows, please use the pre-built executable to properly Bootstrap JavaFX, which is found directly in the distribution.

## Main UI
![Tomcat Instance Generator UI](https://raw.githubusercontent.com/jaschenk/Tomcat-Instance-Generator/externalize/doc/images/TomcatInstanceGenerator_02.png)

