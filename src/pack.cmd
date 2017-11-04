@echo off

set "JAVA_HOME=C:\Java\jdk1.7.0_51"
set "PATH=%JAVA_HOME%\bin;%PATH%"

del /Q px_ramaxel2.jar

jar cvf px_ramaxel2.jar -C ./ /

echo Done

echo package terminated!