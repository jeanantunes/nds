package br.com.abril.nds.integracao.ems0107.route;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0107.inbound.EMS0107Input;
import br.com.abril.nds.integracao.ems0107.processor.EMS0107MessageProcessor;
import br.com.abril.nds.integracao.engine.AbstractRoute;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.RouteInterface;
import br.com.abril.nds.integracao.engine.data.FixedLengthRouteTemplate;

@Component
@Scope("prototype")
public class EMS0107Route extends FixedLengthRouteTemplate implements AbstractRoute {
	@Autowired
	private EMS0107MessageProcessor messageProcessor;	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void onStart() {
		// MUDA PARA MEMORIA
		executeNativeSqlIgnoreError("ALTER TABLE ndsi_ems0107_tmp ENGINE = MYISAM");
		
		// MATA A FK PARA COTA SE EXISTIR
		executeNativeSqlIgnoreError("ALTER TABLE ndsi_ems0107_tmp DROP FOREIGN KEY fk_ndsi_ems0107_tmp_cota");
		
		executeNativeSqlIgnoreError("TRUNCATE TABLE ndsi_ems0107_tmp");
	}
	
	private void executeNativeSqlIgnoreError(String sql) {
		try {
			entityManager.createNativeQuery(sql).executeUpdate();
			entityManager.flush();
		} catch (Exception e) {
			// FK NAO EXISTE, IGNORA O Statement
		}
	}
	
	@Override
	public void setupTypeMapping() {
		setTypeMapping(EMS0107Input.class);
	}

	@Override
	public String getUri() {
		return "EMS0107";
	}

	@Override
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	} 

	@Override
	public String getFileFilterExpression() {
		return (String) getParameters().get("NDSI_EMS0107_IN_FILEMASK");
	}
	
	/*@Override
	public String getInboundFolder() {
		return (String) getParameters().get("NDSI_EMS0107_INBOUND");
	}
	
	@Override
	public String getArchiveFolder() {
		return (String) getParameters().get("NDSI_EMS0107_ARCHIVE");
	}*/
	
	@Override
	public boolean isCommitAtEnd() {
		return false;
	}
	
	@Override
	public boolean isBulkLoad() {
		return true;
	}
	
	@Override
	public RouteInterface getRouteInterface() {
		return RouteInterface.EMS0107;
	}
}