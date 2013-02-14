package br.com.abril.nds.integracao.ems0185.processor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;
import br.com.abril.nds.integracao.util.SqlScriptRunner;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;

@Component
public class EMS0185MessageProcessor extends AbstractRepository implements MessageProcessor  {
	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0185MessageProcessor.class);
	
	@Autowired
	private CouchDbProperties couchDbProperties;

	@Autowired
	private DataSource dataSourceIcd;

	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {
		CouchDbClient dbClient = null;
		
		Connection connection = null;
		
		try {			
			dbClient = getCouchDBClient();
			
			connection = dataSourceIcd.getConnection();
			
			// VERIFICA SE EXISTE ATUALIZACAO DE CEP NO COUCHDB
			if (dbClient.contains("AtualizacaoCep")) {
				// OBTEM O ANEXO DO COUCHDB
				InputStream doc = dbClient.find("AtualizacaoCep/dnecom");
				
				File file = new File(System.getProperty("java.io.tmpdir") + "/dnecom.tar.gz");
				
				FileOutputStream out = new FileOutputStream(file);
				
				// SALVA O ANEXO NA PASTA TMP
				IOUtils.copy(doc, out);
				
				out.flush();
				out.close();
				
				// EXTRAI O ARQUIVO tar.gz
				TarGZipUnArchiver ua = new TarGZipUnArchiver();
				
				ua.enableLogging(new ConsoleLogger(org.codehaus.plexus.logging.Logger.LEVEL_INFO, "console"));
				ua.setSourceFile(file);
				ua.setDestDirectory(file.getParentFile());
				ua.extract();
				
				// CARREGA UTILIZANDO o MYSQL CLIENT
				SqlScriptRunner sqlScriptRunner = new SqlScriptRunner(connection, false, false);
				
				sqlScriptRunner.runScript(new FileReader(System.getProperty("java.io.tmpdir") + "/dnecom-mysql.sql"));
				
				dbClient.remove(dbClient.find(IntegracaoDocument.class, "AtualizacaoCep"));
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e.getMessage(), e);
		}
		finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
			
			if (dbClient != null) {
				dbClient.shutdown();
			}			
		}
	}
	
	private CouchDbClient getCouchDBClient() {
		
		return new CouchDbClient(
				"db_integracao",
				true,
				couchDbProperties.getProtocol(),
				couchDbProperties.getHost(),
				couchDbProperties.getPort(),
				couchDbProperties.getUsername(),
				couchDbProperties.getPassword()
		);
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}