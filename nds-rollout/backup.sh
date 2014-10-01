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
echo
echo '1) GERA UMA BACKUP DA BASE MAIS ATUALIZADA.'
mysqldump -hrds-mql-prd-prv-distb-01.c4gu9vovwusc.sa-east-1.rds.amazonaws.com -uawsuser -pdgbdistb01mgr 'db_09795816' --routines --triggers   | sed -e '/^\/\*\!50013 DEFINER/d' | sed -e 's/\/\*\!50017 DEFINER\=`awsuser`@`%`\*\/ //g' | sed 's/DEFINER\=`awsuser`@`%`//g' > $DIRBKP/santos_interfaces.sql
echo
