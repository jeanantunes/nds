#/bin/bash


##############
## ROLLOUT  ##
############## 

NOME_ARQUIVO_DUMP=dump-`date +%y%m%d%H%M%S`.sql
NOME_ARQUIVO=`date +%m%d%`
NOME_DIRETORIO=`date +%y+%m%d%`
DIRBKP=/opt/rollout
BASE=db_$1

# **** Local ****
#AMBIENTE=localhost
#DBUSER=root
#DBPASS=root

# **** Carga ****
AMBIENTE=10.129.28.137
DBUSER=root
DBPASS=abril@123
# mysql -h10.129.28.137 -uroot -pabril@123 db_05318019

# **** Producao ****
#AMBIENTE=rds-mql-prd-prv-distb-01.c4gu9vovwusc.sa-east-1.rds.amazonaws.com
#DBUSER=awsuser
#DBPASS=pdgbdistb01mgr

clear
echo
echo '1) INICIALIZA.' `date`
echo
echo '2) GERA UMA ESTRUTURA DA BASE.' `date +%T`
echo "Extrai Estrutura atualizada <s ou n>?"
read sn
if [ $sn = "s" ] ; then
rm $DIRBKP/comum/carga_estrutura.sql
mysqldump -hrds-mql-prd-prv-distb-01.c4gu9vovwusc.sa-east-1.rds.amazonaws.com -uawsuser -pdgbdistb01mgr --no-data 'db_09795816' --routines --triggers   | sed -e '/^\/\*\!50013 DEFINER/d' | sed -e 's/\/\*\!50017 DEFINER\=`awsuser`@`%`\*\/ //g' | sed 's/DEFINER\=`awsuser`@`%`//g' | sed 's/ AUTO_INCREMENT=[0-9]*\b/ AUTO_INCREMENT=0 /' > $DIRBKP/comum/carga_estrutura.sql
fi
echo
echo '3) EXCLUI A BASE '$BASE `date +%T`
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS -e "drop database $BASE"
echo
echo '4) CRIA A BASE '$BASE `date +%T`
mysqladmin -h$AMBIENTE -u$DBUSER -p$DBPASS create $BASE
echo
echo '5) IMPORTA ESTRUTURA DA BASE.' `date +%T`
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS db_$1 < $DIRBKP/comum/carga_estrutura.sql
echo
echo '6) CARREGA DADOS INICIAIS.' `date +%T`
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS db_$1 < $DIRBKP/$1/carga_inicial.sql
echo
echo '7) CARREGA DADOS DE CARGA DE TIPOS.' `date +%T`
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS db_$1 < $DIRBKP/comum/carga_tipos.sql
echo
echo '8) COPIA ARQUIVOS PARA BASE.' `date +%T`
echo "Copia os arquivos <s ou n>?"
read sn
if [ $sn = "s" ] ; then

rm $DIRBKP/load_files/*

cp $DIRBKP/$1/cargas/$2/DISTRIBUIDOR/* $DIRBKP/load_files
cp $DIRBKP/$1/cargas/$2/PRODIN/DINAP/* $DIRBKP/load_files
cp $DIRBKP/$1/cargas/$2/PRODIN/FC/* $DIRBKP/load_files
cp $DIRBKP/$1/cargas/$2/MDC/* $DIRBKP/load_files
cp $DIRBKP/$1/cargas/$2/OUTROS/* $DIRBKP/load_files

ls $DIRBKP/load_files | awk '{print "iconv -f iso-8859-1 -t utf-8 '$DIRBKP'/load_files/"$1" > '$DIRBKP'/load_files/"$1"_r"| "sh"}'
find $DIRBKP/load_files -type f ! \( -iname "*_r" \) -exec rm {} \;
ls $DIRBKP/load_files | sed 's/_r//g' | awk '{print "mv '$DIRBKP'/load_files/"$1"_r " "'$DIRBKP'/load_files/"$1| "sh"}'
find $DIRBKP/load_files -type f \( -iname "*~" \) -exec rm {} \;

ssh -i /home/dguerra/.ssh/kp-hom-distb.pem douglas@$AMBIENTE "sudo rm $DIRBKP/load_files/*"
scp -i /home/dguerra/.ssh/kp-hom-distb.pem $DIRBKP/load_files/* douglas@$AMBIENTE:$DIRBKP/load_files

#scp -i /home/dguerra/.ssh/kp-hom-distb.pem $DIRBKP/$1/cargas/$2/DISTRIBUIDOR/* douglas@$AMBIENTE:$DIRBKP/load_files
#scp -i /home/dguerra/.ssh/kp-hom-distb.pem $DIRBKP/$1/cargas/$2/PRODIN/DINAP/* douglas@$AMBIENTE:$DIRBKP/load_files
#scp -i /home/dguerra/.ssh/kp-hom-distb.pem $DIRBKP/$1/cargas/$2/PRODIN/FC/* douglas@$AMBIENTE:$DIRBKP/load_files
#scp -i /home/dguerra/.ssh/kp-hom-distb.pem $DIRBKP/$1/cargas/$2/MDC/* douglas@$AMBIENTE:$DIRBKP/load_files
#scp -i /home/dguerra/.ssh/kp-hom-distb.pem $DIRBKP/$1/cargas/$2/OUTROS/* douglas@$AMBIENTE:$DIRBKP/load_files
#ssh -i /home/dguerra/.ssh/kp-hom-distb.pem douglas@$AMBIENTE "ls $DIRBKP/load_files | awk '{print "iconv -f iso-8859-1 -t utf-8 $DIRBKP/load_files/"$1" > $DIRBKP/load_files/"$1".cnv"| "sh"}'"

fi
echo
echo '9) CARREGA PRODIN MDC.' `date +%T`
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS db_$1 < $DIRBKP/comum/carga_prodin_mdc.sql
echo
echo '10) CARREGA MOVIMENTAÇÕES.' `date +%T`
echo "Executar Movimentações <s ou n>?"
read sn
if [ $sn = "s" ] ; then
mysql -h$AMBIENTE -u$DBUSER -p$DBPASS db_$1 < $DIRBKP/comum/carga_movimentos_v2.sql
echo ''
fi	
echo
echo 'Fim.' `date`

#mysql -h10.129.28.137 -uroot -pabril@123 db_05318019 < /opt/rollout/comum/carga_movimentos_v2.sql
#ssh -i /home/dguerra/.ssh/kp-hom-distb.pem douglas@10.129.28.137 "sudo rm /opt/rollout/load_files/COTA.NEW"
#scp -i /home/dguerra/.ssh/kp-hom-distb.pem /opt/rollout/05318019/cargas/20140822/MDC/COTA.NEW douglas@10.129.28.137:/opt/rollout/load_files

