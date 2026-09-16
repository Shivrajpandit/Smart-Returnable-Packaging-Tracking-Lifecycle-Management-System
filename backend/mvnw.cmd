@REM ----------------------------------------------------------------------------
@REM SRPT-LMS Maven Wrapper Launcher
@REM ----------------------------------------------------------------------------
@ECHO OFF
SETLOCAL

SET "DIRNAME=%~dp0"
IF "%DIRNAME%" == "" SET "DIRNAME=."

SET "MAVEN_HOME=%DIRNAME%.mvn\apache-maven-3.9.6"
SET "MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd"

IF NOT EXIST "%MAVEN_CMD%" (
    ECHO Downloading and configuring local Maven binary...
    powershell -Command "Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip' -OutFile '%DIRNAME%.mvn\maven.zip'; Expand-Archive -Path '%DIRNAME%.mvn\maven.zip' -DestinationPath '%DIRNAME%.mvn' -Force; Remove-Item '%DIRNAME%.mvn\maven.zip' -Force"
)

"%MAVEN_CMD%" %*
EXIT /B %ERRORLEVEL%
