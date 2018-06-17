@echo off & setlocal enabledelayedexpansion

set CONFIG_NAME="../config/remote-config.xml"
set EXCUTE_MAIN="com.clyy.DeployApplication"
set CLASSPATH="../lib/*"

java -Dremote.config=%CONFIG_NAME% -classpath %CLASSPATH% %EXCUTE_MAIN%

pause