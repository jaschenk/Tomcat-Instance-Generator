#!/bin/sh
# -----------------------------------------------------------
#  Tomcat Instance Environment Settings
# -----------------------------------------------------------

#
#  *************************************************************************
#  Tomcat Instance Generation
#  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#
#   Instance UUID: ${TOMCAT_INSTANCE_UUID}
#   Instance Name: ${TOMCAT_INSTANCE_NAME}
#     Environment: ${TOMCAT_ENVIRONMENT_NAME}
#
#  *************************************************************************
#

# Set JAVA JVM Options
${JVM_OPTS}

# Set Global Instance Properties
${INSTANCE_PROPERTIES}

# Set Runtime Management Properties
${INSTANCE_MANAGEMENT_PROPERTIES}

export JAVA_OPTS
#
#
# ########################################################################################
# Additional Settings for Garbage Collection Processing ...
#
#    -XX:SurvivorRatio=<ratio>
#    -XX:+UseConcMarkSweepGC -XX:+CMSParallel#arkEnabled
#    -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=<percent>
#    -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBefore#ark
#    -XX:+PrintGCDateStamps -verbose:gc -XX:+PrintGCDetails -Xloggc:"<path to log>"
#    -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M
#    -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=<path to dump>`date`.hprof
#
# #######################################################################################