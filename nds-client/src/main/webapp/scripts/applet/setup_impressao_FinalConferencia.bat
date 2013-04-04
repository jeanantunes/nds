@echo off 
rem caminho da sua instalação java, no meu caso é portable 
cd C:\Program Files\Java\jdk1.7.0_15\bin

set runDir = "C:\Users\robson\NDS\nds-client\src\main\webapp\scripts\applet\"

rem echo diretorio %runDir%

rem Gerar chave 
rem o arquivo .keystore, armazena sua chave de segurança 
rem 123456 é a senha 
rem ImpressaoFinalizacaoEncalhe é meu apelido do software 

echo pass 123456
keytool -delete -alias ImpressaoFinalizacaoEncalhe -keystore 123456

keytool -genkey -v -keypass 123456 -storepass 123456 -alias ImpressaoFinalizacaoEncalhe -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=abril, OU=abril, O=abril, L=Sao Paulo - BR, ST=Sao Paulo, C=BR"

rem Assinar 
rem Deve ser assinados todos os .jar da pasta lib inclusive o principal fora da pasta lib 

jarsigner -verbose -keypass 123456 -storepass 123456 "C:\Users\robson\NDS\nds-client\src\main\webapp\scripts\applet\ImpressaoFinalizacaoEncalhe.jar" ImpressaoFinalizacaoEncalhe 

rem Verificar se a assinatura está correta 

jarsigner -verify -verbose -certs "C:\Users\robson\NDS\nds-client\src\main\webapp\scripts\applet\ImpressaoFinalizacaoEncalhe.jar" > "C:\Users\robson\NDS\nds-client\src\main\webapp\scripts\applet\logAssinatura.txt" 
