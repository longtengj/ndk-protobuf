@echo off

echo 清除原来存在的文件......

del /f /s /q *.cc
del /f /s /q *.cpp
del /f /s /q *.h


echo 正在生成文件.......



rem protoc -I=.\  --cpp_out=.\  .\upmsgstruct.proto
rem protoc -I=.\  --cpp_out=.\  .\upmsg.proto

for /r . %%i in (*.proto) do (


protoc -I=.\  --cpp_out=.\  .\%%~nxi


)





echo 文件生成完毕,请等待......

rename *.cc  *.cpp


copy /Y  .\*.h  .\..\cpp\app\*.h


copy  /Y  .\*.cpp  .\..\cpp\app\*.cpp


echo 文件复制完毕! 备份当前目录下的文件...


rmdir /S /Q  .\bak

mkdir .\bak

copy /Y  .\*.h  .\bak\*.h


copy  /Y  .\*.cpp  .\bak\*.cpp


del /f  /q *.cc
del /f  /q *.cpp
del /f  /q *.h

echo 备份完毕!


pause