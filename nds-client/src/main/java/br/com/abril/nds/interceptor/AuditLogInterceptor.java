package br.com.abril.nds.interceptor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.abril.nds.client.util.AuditoriaUtil;
import br.com.abril.nds.dto.auditoria.AuditoriaDTO;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.util.TipoOperacaoSQL;
import br.com.abril.nds.model.cadastro.Distribuidor;

public class AuditLogInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = -1965590377590000239L;
	
	private static final String DB_NAME = "db_logs";

	private Set<AuditoriaDTO> audit = new HashSet<AuditoriaDTO>();

	@Autowired
	private CouchDbProperties properties;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, org.hibernate.type.Type[] types) {

		this.validarAndamnetoFechamentoDiario();
		
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 

		AuditoriaDTO auditoriaDTO = AuditoriaUtil.generateAuditoriaDTO(
			entity, null, entity.getClass().getSimpleName(), Thread.currentThread(), user, TipoOperacaoSQL.INSERT
		);
		
		audit.add(auditoriaDTO);

		return false;
	}
	
	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, org.hibernate.type.Type[] types) {

		this.validarAndamnetoFechamentoDiario();
		
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 

		AuditoriaDTO auditoriaDTO = AuditoriaUtil.generateAuditoriaDTO(
			null, entity, entity.getClass().getSimpleName(), Thread.currentThread(), user, TipoOperacaoSQL.DELETE
		);

		audit.add(auditoriaDTO);
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, org.hibernate.type.Type[] types) {

		this.validarAndamnetoFechamentoDiario();
		
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		
		getSession().beginTransaction();
		
		Object oldEntity = getSession().get(entity.getClass(), id);
		
		getSession().close();

		AuditoriaDTO auditoriaDTO = AuditoriaUtil.generateAuditoriaDTO(
			entity, oldEntity, entity.getClass().getSimpleName(), Thread.currentThread(), user, TipoOperacaoSQL.UPDATE
		);
		
		audit.add(auditoriaDTO);

		return false;
	}
	
	@Override
	public String onPrepareStatement(String sql) {
		
		if (sql != null && !sql.trim().isEmpty()) {
			
			if (sql.trim().toUpperCase().startsWith("DELETE") 
					|| sql.trim().toUpperCase().startsWith("UPDATE")) {
				
				this.validarAndamnetoFechamentoDiario();
			}
		}
		
		return super.onPrepareStatement(sql);
	}

	@Override
	public void afterTransactionCompletion(Transaction tx) {

		if (this.audit.isEmpty()) {

			return;
		}
	
		CouchDbClient client = new CouchDbClient(
			DB_NAME,
			true,
			this.properties.getProtocol(), 
			this.properties.getHost(), 
			this.properties.getPort(), 
			this.properties.getUsername(), 
			this.properties.getPassword()
		);

		for (Iterator<AuditoriaDTO> auditedEntities = audit.iterator(); auditedEntities.hasNext();) {

			AuditoriaDTO auditoria = auditedEntities.next();

			client.save(auditoria);
		}
		
		 this.audit = new HashSet<AuditoriaDTO>();
	}
	
	private Session getSession() {
		
		SessionFactory bean = applicationContext.getBean(SessionFactory.class);
		
		return bean.openSession();
	}
	
	private void validarAndamnetoFechamentoDiario() {
		
		Query query = getSession().createQuery("from Distribuidor");
		
		query.setMaxResults(1);
		
		Distribuidor distribuidor = (Distribuidor) query.uniqueResult();
		
		if (distribuidor != null 
				&& Boolean.TRUE.equals(distribuidor.getFechamentoDiarioEmAndamento())) {
			
			throw new RuntimeException("Fechamento diário em andamento! Por favor aguarde.");
		}
	}
	
}