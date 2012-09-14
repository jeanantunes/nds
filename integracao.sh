#!/bin/bash

#folders=( "backup" "2707" "3107" "0108" "0208" "0308" "0608" "0708" "0808" "0908" "1008" "1308" "1408" "1508" "1608" "1708" "2008" "2108" "2208" "2308" "2408" "2708" "2808" "2908" "3008" "3108" "0309" "0409" "0509" "0609" "1009" )
folders=( "0509" "0609" "1009" )
emsDServer=( 112 109 110 111 114 125 126 113 135 )
emsDClient=( 112 109 110 111 114 125 126 113 135 )
emsDClient_mdc=( 119 118 117 116 108 106 107 197 198 )

for i in "${folders[@]}"
do
   :
   rm /opt/interface/prodin/06248116
   ln -s ~/Public/campinas/$i/ /opt/interface/prodin/06248116

	for j in "${emsDServer[@]}"
	do
	   :
		CMD="java -jar /opt/ndistrib/ndsi-couchdbinterface/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron ${j}"
		LOG="/opt/ndistrib/log/log_dserver_${i}_${j}.txt"
		echo "${CMD} >> ${LOG}"
		$CMD >> $LOG
	done


	for j in "${emsDClient[@]}"
	do
	   :
		CMD="java -jar /opt/ndistrib/ndsi-engine/ndsi-engine.jar br.com.abril.nds.integracao.ems0${j}.route.EMS0${j}Route"
		LOG="/opt/ndistrib/log/log_dclient_${i}_${j}.txt"
		echo "${CMD} >> ${LOG}"
		$CMD >> $LOG
	done


	for j in "${emsDClient_mdc[@]}"
	do
	   :
		CMD="java -jar /opt/ndistrib/ndsi-engine/ndsi-engine.jar br.com.abril.nds.integracao.ems0${j}.route.EMS0${j}Route"
		LOG="/opt/ndistrib/log/log_mdc_${i}_${j}.txt"
		echo "${CMD} >> ${LOG}"
		$CMD >> $LOG
	done
done


## Imagem
#sudo mount -t smbfs //abwbw2k01/images /mnt/images -o username=t30541,password=Pestinha@09,uid=t30541,gid=t30541
#java -jar /opt/ndistrib/ndsi-couchdbinterface/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 134


## CEP
#java -jar /opt/ndistrib/ndsi-couchdbinterface/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 185
#java -jar /opt/ndistrib/ndsi-engine/ndsi-engine.jar br.com.abril.nds.integracao.ems0185.route.EMS0185Route













##SERVER
##PRODIM
## Integra editor no couchdb DServer
#java -jar /opt/ndistrib/ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 112
## Integra publicacao no couchdb DServer
#java -jar /opt/ndistrib/ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 109
## Integra produto_edicao no couchdb DServer
#java -jar /opt/ndistrib/ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 110
## Integra Lançamentos Programados no couchdb DServer
#java -jar /opt/ndistrib/ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 111
## Integra Recolhimentos_Programados no couchdb DServer
#java -jar /opt/ndistrib/ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 114
## Integra Chamada de Capa no couchdb DServer
#java -jar /opt/ndistrib/ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 125
## Integra CÎáÎõÎådigo de Barras no couchdb DServer
#java -jar /opt/ndistrib/ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 126
## Integra Desconto Distribuidor no couchdb DServer
#java -jar /opt/ndistrib/ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 113
##Nota
#java -jar /opt/ndistrib/ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 135
##IMAGENS
##java -jar ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 134
## CEP
##java -jar ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 185


##DCLIENT
##PRODIM
## Recupera Editor no couchdb e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0112.route.EMS0112Route
## Recupera publicacao no couchdb e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0109.route.EMS0109Route
## Recupera produto no couchdb e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0110.route.EMS0110Route
## Recupera Lançamentos Programados no couchdb e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0111.route.EMS0111Route
## Recupera Chamada de Capa no couchdb e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0114.route.EMS0114Route
## Recupera Chamada de Capa no couchdb e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0125.route.EMS0125Route
## Recupera CÎáÎõÎådigo de Barras no couchdb e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0126.route.EMS0126Route
## Recupera Desconto Distribuidor no couchdb e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0113.route.EMS0113Route
##Notas
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0135.route.EMS0135Route
## CEP
##java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0185.route.EMS0185Route




##MDC
## Recupera PRODUTO no arquivo do mdc e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0119.route.EMS0119Route
## Recupera PREÇO no arquivo do mdc e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0118.route.EMS0118Route
## Recupera Cota no arquivo do mdc e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0117.route.EMS0117Route
## Recupera Banca no arquivo do mdc e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0116.route.EMS0116Route
## Recupera MATRIZ LANCAMENTO RECOLHIMENTO no arquivo do mdc e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0108.route.EMS0108Route
## Recupera ESTUDO no arquivo do mdc e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0106.route.EMS0106Route
## Recupera ESTUDO COTA no arquivo do mdc e persiste no nds
#java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0107.route.EMS0107Route

