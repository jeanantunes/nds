#/bin/bash


##############
## ROLLOUT  ##
############## 

NOME_ARQUIVO_DUMP=dump-`date +%y%m%d%H%M%S`.sql
NOME_ARQUIVO=`date +%m%d%`
NOME_DIRETORIO=`date +%y+%m%d%`
DIRBKP=/opt/rollout
BASE=db_00757374
S1=00757374
DIR=`pwd` 


# **** Layout ****
#LAMBIENTE=rds-mql-dev-prv-distb-01.c4gu9vovwusc.sa-east-1.rds.amazonaws.com
#LDBUSER=awsuser
#LDBPASS=dgbdistb01mgr

# **** Layout ****
LAMBIENTE=10.129.28.137
LDBUSER=root
LDBPASS=abril@123

# **** Local ****
#AMBIENTE=localhost
#DBUSER=root
#DBPASS=root

# **** Carga ****
AMBIENTE=10.129.28.137
DBUSER=root
DBPASS=abril@123


# **** Producao ****
#AMBIENTE=rds-mql-prd-prv-distb-01.c4gu9vovwusc.sa-east-1.rds.amazonaws.com
#DBUSER=awsuser
#DBPASS=dgbdistb01mgr



clear
echo
echo '1) INICIALIZA.' `date`
echo
echo '2) GERA UMA ESTRUTURA DA BASE.' `date +%T`
echo "Extrai Estrutura atualizada <s ou n>?"
read sn
if [ $sn = "s" ] ; then
echo
rm $DIRBKP/comum/carga_estrutura.sql
#mysqldump -h$LAMBIENTE -u$LDBUSER -p$LDBPASS --no-data 'nds_lab3' --routines --triggers   | sed -e '/^\/\*\!50013 DEFINER/d' | sed -e 's/\/\*\!50017 DEFINER\=`awsuser`@`%`\*\/ //g' | sed 's/DEFINER\=`awsuser`@`%`//g' | sed 's/ AUTO_INCREMENT=[0-9]*\b/ AUTO_INCREMENT=0 /' > $DIRBKP/comum/carga_estrutura.sql
#mysqldump -h$LAMBIENTE -u$LDBUSER -p$LDBPASS --no-data 'nds_lab3' --routines --triggers   | sed -e '/^\/\*\!50013 DEFINER/d' | sed -e 's/\/\*\!50017 DEFINER\=`root`@`%`\*\/ //g' | sed 's/DEFINER\=`root`@`%`//g' | sed 's/ AUTO_INCREMENT=[0-9]*\b/ AUTO_INCREMENT=0 /' > $DIRBKP/comum/carga_estrutura.sql
mysqldump -h$LAMBIENTE -u$LDBUSER -p$LDBPASS --single-transaction --no-data --routines --triggers nds_lab3 | sed -e '/^\/\*\!50013 DEFINER/d' | sed -e 's/\/\*\!50017 DEFINER\=`root`@`%`\*\/ //g' | sed 's/DEFINER\=`root`@`localhost`//g' | sed 's/ AUTO_INCREMENT=[0-9]*\b/ AUTO_INCREMENT=0 /' > $DIRBKP/comum/carga_estrutura.sql
fi
echo
echo '3) EXCLUI A BASE '$BASE `date +%T`
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS -e "drop database $BASE"
echo
echo '4) CRIA A BASE '$BASE `date +%T`
mysqladmin -h$AMBIENTE -u$DBUSER -p$DBPASS create $BASE
echo
echo '5) IMPORTA ESTRUTURA DA BASE.' `date +%T`
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS $BASE < $DIRBKP/comum/carga_estrutura.sql
echo
echo '6) CARREGA DADOS INICIAIS.' `date +%T`
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS $BASE < $DIR/$S1/carga_inicial.sql
echo
echo '7) CARREGA DADOS DE CARGA DE TIPOS.' `date +%T`
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS $BASE < $DIR/comum/carga_tipos.sql
echo
echo '8) COPIA ARQUIVOS PARA BASE.' `date +%T`
echo "Copia os arquivos ? <s ou n>"
read sn
if [ $sn = "s" ] ; then

rm $DIRBKP/load_files/*

cp $DIRBKP/$S1/cargas/$1/DISTRIBUIDOR/* $DIRBKP/load_files
cp $DIRBKP/$S1/cargas/$1/PRODIN/DINAP/* $DIRBKP/load_files
cp $DIRBKP/$S1/cargas/$1/PRODIN/FC/* $DIRBKP/load_files
cp $DIRBKP/$S1/cargas/$1/MDC/* $DIRBKP/load_files
cp $DIRBKP/$S1/cargas/$1/OUTROS/* $DIRBKP/load_files

ls $DIRBKP/load_files | awk '{print "iconv -f iso-8859-1 -t utf-8 '$DIRBKP'/load_files/"$S1" > '$DIRBKP'/load_files/"$S1"_r"| "sh"}'
find $DIRBKP/load_files -type f ! \( -iname "*_r" \) -exec rm {} \;
ls $DIRBKP/load_files | sed 's/_r//g' | awk '{print "mv '$DIRBKP'/load_files/"$S1"_r " "'$DIRBKP'/load_files/"$S1| "sh"}'
find $DIRBKP/load_files -type f \( -iname "*~" \) -exec rm {} \;


ssh -i /home/dguerra/.ssh/kp-hom-distb.pem -o "StrictHostKeyChecking no" douglas@$AMBIENTE "sudo rm $DIRBKP/load_files/*"
scp -i /home/dguerra/.ssh/kp-hom-distb.pem -o "StrictHostKeyChecking no" $DIRBKP/load_files/* douglas@$AMBIENTE:$DIRBKP/load_files

fi
echo
echo '9) TIPO DISTRIBUIDOR.' `date +%T`
echo "Tipo do Distribuidor: Agência ou Praça ? <a ou p>"
read td
if [ $td = "a" ] ; then
echo 'CARREGA PRODIN AGENCIA' `date +%T`
cd /
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS $BASE < $DIR/comum/carga_prodin_prd_agencia.sql
else
echo 'CARREGA PRODIN PRACA' `date +%T`
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS $BASE < $DIR/comum/carga_prodin_prd_praca.sql
fi
echo
echo '10) CARREGA PRODIN MDC.' `date +%T`
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS $BASE < $DIR/comum/carga_prodin_mdc.sql
echo
echo '11) CARREGA MOVIMENTAÇÕES.' `date +%T`
echo "Executar Movimentações ? <s ou n>"
read sn
if [ $sn = "s" ] ; then
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS $BASE < $DIR/comum/carga_movimentos.sql
echo ''
fi	
echo
echo '11) CARREGA PRODIN MDC.' `date +%T`
#mysql -h$AMBIENTE -u$DBUSER -p$DBPASS $BASE < $DIR/$1/carga_ajustes.sql
echo
echo '12) EXPORTA DUMP.' `date +%T`
#mysql -h$AMBIENTE -u$DBUSER -p$DBPASS $BASE < $DIR/$1/carga_ajustes.sql
mysqldump -h$AMBIENTE -u$DBUSER -p$DBPASS --single-transaction --routines --triggers db_00757374 | sed -e '/^\/\*\!50013 DEFINER/d' | sed -e 's/\/\*\!50017 DEFINER\=`root`@`localhost`\*\/ //g' | sed 's/DEFINER\=`root`@`localhost`//g' > $DIRBKP/telesena.sql
echo
echo '13) SCP PARA DEVWEB.' `date +%T`
scp -i /home/dguerra/.ssh/kp-hom-distb.pem -o "StrictHostKeyChecking no" $DIRBKP/telesena.sql douglas@10.129.28.111:/home/douglas
echo
echo '14) Publica base TELESENA no RDS de Producao.' `date +%T`
ssh -i /home/dguerra/.ssh/kp-hom-distb.pem -o "StrictHostKeyChecking no" douglas@10.129.28.111 "/home/douglas/publica_dump.sh telesena"
echo
echo 'Fim.' `date`

