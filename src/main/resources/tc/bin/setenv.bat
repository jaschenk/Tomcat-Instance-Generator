@echo off
rem # -----------------------------------------------------------
rem #  Tomcat Instance Environment Settings
rem # -----------------------------------------------------------

rem
rem  *************************************************************************
rem  Tomcat Instance Generation
rem  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
rem
rem   Instance UUID: ${TOMCAT_INSTANCE_UUID}
rem   Instance Name: ${TOMCAT_INSTANCE_NAME}
rem     Environment: ${TOMCAT_ENVIRONMENT_NAME}
rem
rem  *************************************************************************
rem

rem Set  JAVA JVM Options
${JVM_OPTS}

rem Set Global Instance Properties
${INSTANCE_PROPERTIES}

rem Set Runtime Management Properties
${INSTANCE_MANAGEMENT_PROPERTIES}

rem #
rem pause
rem
rem ########################################################################################
rem Additional Settings for Garbage Collection Processing ...
rem
rem    -XX:SurvivorRatio=<ratio>
rem    -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled
rem    -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=<percent>
rem    -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark
rem    -XX:+PrintGCDateStamps -verbose:gc -XX:+PrintGCDetails -Xloggc:"<path to log>"
rem    -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M
rem    -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=<path to dump>`date`.hprof
rem
rem #######################################################################################