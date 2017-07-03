#!/bin/bash
ARCHIVES=-Dtc.archives=bin/Prod-TomcatKnowledgeBase.yaml
java -Dcom.sun.javafx.fontSize=10.5 \
     ${ARCHIVES} \
     -cp ./TomcatInstanceGenerator-1.0.0.3-SNAPSHOT.jar jeffaschenk.tomcat.instance.generator.ui.Main
