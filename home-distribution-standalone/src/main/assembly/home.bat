@echo off

rem #################################################################
rem #
rem # Sismics Home standalone startup script.
rem #
rem # Author: Jean-Marc Tremeaux <jm.tremeaux@gmail.com>
rem #
rem #################################################################

rem The directory where Home will create its files (database, index...)
rem Make sure it is writable.
rem Leave it empty to use the users's directory (default: %APPDATA%\Sismics\Home)
set HOME_HOME=%APPDATA%\Sismics\Home

rem The host name or IP address on which to bind Home. Only relevant if you have
rem multiple network interfaces and want to make Home available on only one of them.
rem The default value 0.0.0.0 will bind Home to all available network interfaces.
rem set HOME_HOST=0.0.0.0

rem The port on which Home will listen for incoming HTTP traffic. Default: 4001.
rem set HOME_PORT=4001

rem The context path (i.e., the last part of the Home URL).  Typically "/" or "/home".
rem Default: "/".
rem set HOME_CONTEXT_PATH=/

rem The memory limit (max Java heap size) in megabytes.
set HOME_MAX_MEMORY=150

java -Xmx%HOME_MAX_MEMORY%m  -Dhome.home=%HOME_HOME% -Dhome.host=%HOME_HOST% -Dhome.port=%HOME_PORT% -Dhome.contextPath=%HOME_CONTEXT_PATH% -jar home-standalone.jar
