rm -rf /opt/ndistrib/bin
mkdir -p /opt/ndistrib/bin
rm -rf /opt/ndistrib/data
mkdir -p /opt/ndistrib/data
rm -rf /opt/ndistrib/log
mkdir -p /opt/ndistrib/log


cp /mnt/HDU_84646/Projetos/NDS/NDS/ndsi-engine/src/main/java/br/com/abril/nds/integracao/ems0185/shell/cep-export /opt/ndistrib/bin
sudo chmod 777 /opt/ndistrib/bin/cep-export
cp /mnt/Dados/Dropbox/Treelog\ \(1\)/Base\ de\ dados\ CEP/dnecom.mdb /opt/ndistrib/data

rm -rf /opt/ndistrib/ndsi-engine
mkdir -p /opt/ndistrib/ndsi-engine
cp /mnt/HDU_84646/Projetos/NDS/NDS/ndsi-engine/target/ndsi-engine-standalone.tar.gz /opt/ndistrib/ndsi-engine
cd /opt/ndistrib/ndsi-engine
tar -zxvf ndsi-engine-standalone.tar.gz


rm -rf /opt/ndistrib/ndsi-couchdbinterface
mkdir -p /opt/ndistrib/ndsi-couchdbinterface
cp /mnt/HDU_84646/Projetos/NDS/NDS/ndsi-couchdbinterface/target/ndsi-couchdbinterface-0.0.1-SNAPSHOT-standalone.tar.gz /opt/ndistrib/ndsi-couchdbinterface
cd /opt/ndistrib/ndsi-couchdbinterface
tar -zxvf ndsi-couchdbinterface-0.0.1-SNAPSHOT-standalone.tar.gz

cp /mnt/HDU_84646/Projetos/NDS/NDS/integracao.sh /opt
