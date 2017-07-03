#!/bin/bash
ARCHIVES=-Dtc.archives=bin/Prod-TomcatKnowledgeBase.yaml
java ${ARCHIVES} -jar ./TomcatInstanceGenerator-1.0.0.3-SNAPSHOT.jar ${@}
