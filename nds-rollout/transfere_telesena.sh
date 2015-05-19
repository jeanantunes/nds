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




echo
echo '12) EXPORTA DUMP.' `date +%T`
#mysql -h$AMBIENTE -u$DBUSER -p$DBPASS $BASE < $DIR/$1/carga_ajustes.sql
mysqldump -h$AMBIENTE -u$DBUSER -p$DBPASS --single-transaction --routines --triggers telesena | sed -e '/^\/\*\!50013 DEFINER/d' | sed -e 's/\/\*\!50017 DEFINER\=`root`@`localhost`\*\/ //g' | sed 's/DEFINER\=`root`@`localhost`//g' > $DIRBKP/telesena.sql
echo
echo '13) SCP PARA DEVWEB.' `date +%T`
scp -i /home/dguerra/.ssh/kp-hom-distb.pem -o "StrictHostKeyChecking no" $DIRBKP/telesena.sql douglas@10.129.28.111:/home/douglas
echo
echo '14) Publica base TELESENA no RDS de Producao.' `date +%T`
ssh -i /home/dguerra/.ssh/kp-hom-distb.pem -o "StrictHostKeyChecking no" douglas@10.129.28.111 "/home/douglas/publica_dump.sh telesena"
echo
echo 'Fim.' `date`

