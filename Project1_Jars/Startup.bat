@echo off
cd EmailServer

echo Lanching Email server
start "Email" java -jar EmailServer.jar

cd ../PhoneServer
echo Lanching Phone server
start "Phone" java -jar PhoneServer.jar


cd ../NameServer
echo Lanching Name Server
start "Name" java -jar NameServer.jar


cd ..
start "CLient" java -jar Client.jar