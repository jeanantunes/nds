#/bin/bash


##############
## ROLLOUT  ##
############## 

NOME_ARQUIVO_DUMP=dump-`date +%y%m%d%H%M%S`.sql
NOME_ARQUIVO=`date +%m%d%`
NOME_DIRETORIO=`date +%y+%m%d%`
DIRBKP=/opt/rollout
BASE=db_05318019
S1=05318019
DIR=`pwd` 


# **** Layout ****
LAMBIENTE=rds-mql-dev-prv-distb-01.c4gu9vovwusc.sa-east-1.rds.amazonaws.com
LDBUSER=awsuser
LDBPASS=dgbdistb01mgr

# **** Layout ****
#LAMBIENTE=10.129.28.137
#LDBUSER=root
#LDBPASS=abril@123

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



rm $DIRBKP/load_files/*

cp $DIRBKP/$S1/cargas/20150925/DISTRIBUIDOR/* $DIRBKP/load_files
cp $DIRBKP/$S1/cargas/20150925/PRODIN/DINAP/* $DIRBKP/load_files
cp $DIRBKP/$S1/cargas/20150925/PRODIN/FC/* $DIRBKP/load_files
cp $DIRBKP/$S1/cargas/20150925/MDC/* $DIRBKP/load_files
cp $DIRBKP/$S1/cargas/20150925/OUTROS/* $DIRBKP/load_files

ls $DIRBKP/load_files | awk '{print "iconv -f iso-8859-1 -t utf-8 '$DIRBKP'/load_files/"$S1" > '$DIRBKP'/load_files/"$S1"_r"| "sh"}'
find $DIRBKP/load_files -type f ! \( -iname "*_r" \) -exec rm {} \;
ls $DIRBKP/load_files | sed 's/_r//g' | awk '{print "mv '$DIRBKP'/load_files/"$S1"_r " "'$DIRBKP'/load_files/"$S1| "sh"}'
find $DIRBKP/load_files -type f \( -iname "*~" \) -exec rm {} \;

ssh -i /home/dguerra/.ssh/kp-hom-distb.pem -o "StrictHostKeyChecking no" douglas@$AMBIENTE "sudo rm $DIRBKP/load_files/*"
scp -i /home/dguerra/.ssh/kp-hom-distb.pem -o "StrictHostKeyChecking no" $DIRBKP/load_files/* douglas@$AMBIENTE:$DIRBKP/load_files


echo 'Fim.' `date`

