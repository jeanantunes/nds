#/bin/bash

##############
## ROLLOUT  ##
##############
clear 

NOME_ARQUIVO_DUMP=dump-`date +%y%m%d%H%M%S`.sql
NOME_ARQUIVO=`date +%m%d%`
NOME_DIRETORIO=`date +%y+%m%d%`
DIRBKP=/opt/rollout
BASE=db_$1

echo
echo '1) INICIALIZA.'
rm $DIRBKP/estrutura.sql
echo
echo '2) GERA UMA ESTRUTURA DA BASE MAIS ATUALIZADA.'
mysqldump -hrds-mql-prd-prv-distb-01.c4gu9vovwusc.sa-east-1.rds.amazonaws.com -uawsuser -pdgbdistb01mgr --no-data 'db_09795816' --routines --triggers   | sed -e '/^\/\*\!50013 DEFINER/d' | sed -e 's/\/\*\!50017 DEFINER\=`awsuser`@`%`\*\/ //g' | sed 's/DEFINER\=`awsuser`@`%`//g' > $DIRBKP/estrutura.sql
echo
echo '3) EXCLUI A BASE '$BASE
cd /var/lib
chmod -R 777 mysql
cd /var/lib/mysql/$BASE
rm -f *.NEW *.dsf *.edi *.pub *.prd *.lan *.rec *.chc *.cdb *.nre *.nfe *.par *.CAR *.CARGA *.TXT
cd /opt/rollout
mysql -hlocalhost -uroot -proot $BASE -e "drop database $BASE"
echo
echo '4) CRIA A BASE '$BASE
mysqladmin -hlocalhost -uroot -proot create $BASE
cd /var/lib
chmod -R 777 mysql
cd /var/lib/mysql/$BASE
echo
echo '5) IMPORTANDO A ESTRUTURA DA BASE.'
mysql -uroot -proot db_$1 < $DIRBKP/estrutura.sql
echo
echo '6) IMPORTA DADOS DE INICIALIZAÇÃO DO DISTRIBUIDOR E CARGA DE TIPOS.'
mysql -uroot -proot db_$1 < $DIRBKP/$1/carga_inicial.sql
echo
echo '7) COPIA OS ARQUIVOS DE CARGA.'
cp /opt/rollout/$1/cargas/$2/DISTRIBUIDOR/* /var/lib/mysql/$BASE
cp /opt/rollout/$1/cargas/$2/PRODIN/DINAP/* /var/lib/mysql/$BASE
cp /opt/rollout/$1/cargas/$2/PRODIN/FC/* /var/lib/mysql/$BASE
cp /opt/rollout/$1/cargas/$2/MDC/* /var/lib/mysql/$BASE
cp /opt/rollout/$1/cargas/$2/OUTROS/* /var/lib/mysql/$BASE
echo
echo '8) CRIA TABELAS DO DISTRIBUIDOR.'
mysql -uroot -proot db_$1 < $DIRBKP/rollout_tabelas.sql
#echo
#echo '8) CRIA TABELAS DE IMPORTAÇÃO DE ARQUIVOS.'
#mysql -uroot -proot db_$1 < $DIRBKP/tabelas_carga.sql
#echo
#echo '1.9) EXECUTANDO INTERFACES SERVER.'
#mysql -hlocalhost -uroot -proot $BASE -e "call rollout($1,@p_out_saida)"
#echo
#echo '1.10) EXECUTANDO INTERFACES CLIENT.'
#mysql -hlocalhost -uroot -proot $BASE -e "call rollout($1,@p_out_saida)"
#echo
#echo '1.8) EXECUTANDO SCRIPTS ROLLOUT.'
#mysql -uroot -proot db_$1 < $DIRBKP/rollout_scripts.sql
#echo
#echo '2) EXECUTANDO A PROCEDURE DE ROLLOUT.'
#mysql -hlocalhost -uroot -proot $BASE -e "call rollout($1,@p_out_saida)"
#echo
echo 'x) GERA UM DUMP DA BASE DO DISTRIBUIDOR.'
mysqldump -hlocalhost -uroot -proot --routines --triggers $BASE  | sed -e '/^\/\*\!50013 DEFINER/d' | sed -e 's/\/\*\!50017 DEFINER\=`root`@`%`\*\/ //g' | sed 's/DEFINER\=`root`@`%`//g' > $DIRBKP/$1/dumps/$NOME_ARQUIVO_DUMP
tar cjvf $DIRBKP/$1/dumps/$NOME_ARQUIVO.tar.bz2 $DIRBKP/$1/dumps/$NOME_ARQUIVO_DUMP
rm $DIRBKP/$1/dumps/$NOME_ARQUIVO_DUMP
#rm $DIRBKP/estrutura.sql
echo 'Finalizado.'







