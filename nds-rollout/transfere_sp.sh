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
mysqldump -h$AMBIENTE -u$DBUSER -p$DBPASS --single-transaction --routines --triggers db_00757374 | sed -e '/^\/\*\!50013 DEFINER/d' | sed -e 's/\/\*\!50017 DEFINER\=`root`@`%`\*\/ //g' | sed 's/DEFINER\=`root`@`%`//g' | sed -e 's/\/\*\!50017 DEFINER\=`root`@`localhost`\*\/ //g' | sed 's/DEFINER\=`root`@`localhost`//g' > $DIRBKP/db_00757374.sql

#echo '12) EXPORTA DUMP.' `date +%T`
#mysqladmin -hrds-mql-prd-prv-distb-01.c4gu9vovwusc.sa-east-1.rds.amazonaws.com -uawsuser -pdgbdistb01mgr --force drop db_00757374

#mysqladmin -hrds-mql-prd-prv-distb-01.c4gu9vovwusc.sa-east-1.rds.amazonaws.com -uawsuser -pdgbdistb01mgr create db_00757374

#mysql -hrds-mql-prd-prv-distb-01.c4gu9vovwusc.sa-east-1.rds.amazonaws.com -uawsuser -pdgbdistb01mgr db_00757374 < $DIRBKP/db_00757374.sql

echo '13) SCP PARA DEVWEB.' `date +%T`
scp -i /home/dguerra/.ssh/kp-hom-distb.pem -o "StrictHostKeyChecking no" $DIRBKP/db_00757374.sql douglas@10.129.28.111:/home/douglas
echo
echo '14) Publica base SAO JOSE no RDS de Producao.' `date +%T`
ssh -i /home/dguerra/.ssh/kp-hom-distb.pem -o "StrictHostKeyChecking no" douglas@10.129.28.111 "/home/douglas/publica_dump.sh db_00757374"


echo
echo 'Fim.' `date`

