@echo off & setlocal enabledelayedexpansion

set CONFIG_NAME="../config/remote-config.xml"
set EXCUTE_MAIN="com.clyy.DeployApplication"
set CLASSPATH="../lib/*"

:begin
	choice /C YN /M "请确认是否继续"
	if %errorlevel%==1 goto continue
	if %errorlevel%==2 goto end

:continue
	java -Dremote.config=%CONFIG_NAME% -classpath %CLASSPATH% %EXCUTE_MAIN%
	goto begin

:end
	echo 终止处理
pause
