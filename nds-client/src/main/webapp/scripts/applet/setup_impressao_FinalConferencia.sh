#!/bin/bash

dir="$PWD"

echo "Gerar chave"
echo "o arquivo .keystore, armazena sua chave de segurança"
echo "123456 é a senha "
echo "ImpressaoFinalizacaoEncalheApplet é meu apelido do software"

echo "pass 123456"
keytool -delete -alias ImpressaoFinalizacaoEncalheApplet -keystore ~/.keystore -storepass 123456

keytool -genkey -v -keypass 123456 -storepass 123456 -alias ImpressaoFinalizacaoEncalheApplet -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=abril, OU=abril, O=abril, L=Sao Paulo - BR, ST=Sao Paulo, C=BR"

echo "Assinar "
echo "Deve ser assinados todos os .jar da pasta lib inclusive o principal fora da pasta lib "

jarsigner -verbose -keypass 123456 -storepass 123456 "ImpressaoFinalizacaoEncalheApplet.jar" ImpressaoFinalizacaoEncalheApplet 

echo "Verificar se a assinatura está correta "

jarsigner -verify -verbose -certs "ImpressaoFinalizacaoEncalheApplet.jar" > "logAssinaturaSh.log" 