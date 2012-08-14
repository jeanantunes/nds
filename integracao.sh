#SERVER
#PRODIM
# Integra editor no couchdb DServer
java -jar ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 112
# Integra publicacao no couchdb DServer
java -jar ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 109
# Integra produto_edicao no couchdb DServer
java -jar ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 110
# Integra LanÁamentos Programados no couchdb DServer
java -jar ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 111
# Integra Recolhimentos_Programados  no couchdb DServer
java -jar ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 114
# Integra Chamada de Capa  no couchdb DServer
java -jar ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 125
# Integra CŒ·ŒıŒÂdigo de Barras  no couchdb DServer
java -jar ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 126
# Integra Desconto Distribuidor  no couchdb DServer
java -jar ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 113
# IMAGENS
java -jar ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 134
# CEP
java -jar ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT.jar cron 185

#DCLIENT
#PRODIM
# Recupera Editor no couchdb e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0112.route.EMS0112Route
# Recupera publicacao no couchdb e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0109.route.EMS0109Route
# Recupera produto no couchdb e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0110.route.EMS0110Route
# Recupera LanÁamentos Programados no couchdb  e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0111.route.EMS0111Route
# Recupera Chamada de Capa  no couchdb  e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0114.route.EMS0114Route
# Recupera Chamada de Capa  no couchdb  e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0125.route.EMS0125Route
# Recupera CŒ·ŒıŒÂdigo de Barras  no couchdb  e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0126.route.EMS0126Route
# Recupera Desconto Distribuidor  no couchdb  e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0113.route.EMS0113Route
# CEP
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0185.route.EMS0185Route

#MDC
# Recupera PRODUTO no arquivo do mdc e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0119.route.EMS0119Route
# Recupera PRE«O no arquivo do mdc e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0118.route.EMS0118Route
# Recupera Cota no arquivo do mdc e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0117.route.EMS0117Route
# Recupera Banca no arquivo do mdc e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0116.route.EMS0116Route
# Recupera MATRIZ LANCAMENTO RECOLHIMENTO no arquivo do mdc e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0108.route.EMS0108Route
# Recupera ESTUDO no arquivo do mdc e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0106.route.EMS0106Route
# Recupera ESTUDO COTA no arquivo do mdc e persiste no nds
java -jar ndsi-engine/target/ndsi-engine.jar br.com.abril.nds.integracao.ems0107.route.EMS0107Route
