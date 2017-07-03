@echo off
set ARCHIVES=-Dtc.archives=bin\Prod-TomcatKnowledgeBase.yaml
java %ARCHIVES% -jar target\TomcatInstanceGenerator-1.0.0.3-SNAPSHOT.jar src\test\resources\yaml\Test-TomcatInstance_WithValidationFailure.yaml