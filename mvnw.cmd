@echo off
setlocal
set MAVEN_VERSION=3.8.8
set MAVEN_HOME=%~dp0.mvn\apache-maven-%MAVEN_VERSION%\
set PATH=%MAVEN_HOME%\bin;%PATH%
mvn -s .mvn\settings.xml %*