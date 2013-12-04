package br.com.abril.nds.interceptor.auditoria;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.abril.nds.client.auditoria.AuditoriaUtil;
import br.com.abril.nds.dto.auditoria.AuditoriaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoOperacaoSQL;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.model.cadastro.Distribuidor;

public class EntityInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = -1965590377590000239L;

	private static final String DB_NAME = "db_auditoria";

	private Set<AuditoriaDTO> audit = new HashSet<AuditoriaDTO>();

	@Autowired
	private CouchDbProperties properties;

	@Autowired
	private ApplicationContext applicationContext;

	private SessionFactory sessionFactory;

	private Session session;

	private boolean liberarLock = false;

	public EntityInterceptor() {

	}

	public EntityInterceptor(ApplicationContext applicationContext) {

		this.applicationContext = applicationContext;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, org.hibernate.type.Type[] types) {

		if (!validarLiberacaoLock(entity, propertyNames)) {
			this.validarAndamnetoFechamentoDiario();
		} else {
			liberarLock = true;
		}

		// Necessario pois a integracao ira usar os servicos
		if (null != SecurityContextHolder.getContext().getAuthentication()) {
			Object user = SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

			AuditoriaDTO auditoriaDTO = AuditoriaUtil.generateAuditoriaDTO(
					entity, null, entity.getClass().getSimpleName(),
					Thread.currentThread(), user, TipoOperacaoSQL.INSERT);

			audit.add(auditoriaDTO);
		}

		return false;
	}

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, org.hibernate.type.Type[] types) {

		this.validarAndamnetoFechamentoDiario();

		// Necessario pois a integracao ira usar os servicos
		if (null != SecurityContextHolder.getContext().getAuthentication()) {
			Object user = SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

			AuditoriaDTO auditoriaDTO = AuditoriaUtil.generateAuditoriaDTO(
					null, entity, entity.getClass().getSimpleName(),
					Thread.currentThread(), user, TipoOperacaoSQL.DELETE);

			audit.add(auditoriaDTO);
		}
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, org.hibernate.type.Type[] types) {

		if (!validarLiberacaoLock(entity, propertyNames)) {
			this.validarAndamnetoFechamentoDiario();
		} else {
			liberarLock = true;
		}

		// Necessario pois a integracao ira usar os servicos
		if (null != SecurityContextHolder.getContext().getAuthentication()) {

			Object user = SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			Session session = this.getNewSession();

			Object oldEntity = session.get(entity.getClass(), id);

			AuditoriaDTO auditoriaDTO = AuditoriaUtil.generateAuditoriaDTO(
					entity, oldEntity, entity.getClass().getSimpleName(),
					Thread.currentThread(), user, TipoOperacaoSQL.UPDATE);

			session.close();

			audit.add(auditoriaDTO);
		}
		return false;
	}

	@Override
	public String onPrepareStatement(String sql) {

		if (sql != null && !sql.trim().isEmpty()) {

			if (sql.trim().toUpperCase()
					.startsWith(TipoOperacaoSQL.DELETE.getOperacao())
					|| sql.trim().toUpperCase()
							.startsWith(TipoOperacaoSQL.UPDATE.getOperacao())
					|| sql.trim().toUpperCase()
							.startsWith(TipoOperacaoSQL.INSERT.getOperacao())) {
				if (!liberarLock) {
					this.validarAndamnetoFechamentoDiario();
				}
			}
		}

		return super.onPrepareStatement(sql);
	}

	@Override
	public void afterTransactionCompletion(Transaction tx) {

		if (this.audit.isEmpty()) {

			return;
		}

		CouchDbClient client = new CouchDbClient(DB_NAME, true,
				this.properties.getProtocol(), this.properties.getHost(),
				this.properties.getPort(), this.properties.getUsername(),
				this.properties.getPassword());

		for (Iterator<AuditoriaDTO> auditedEntities = audit.iterator(); auditedEntities
				.hasNext();) {

			AuditoriaDTO auditoria = auditedEntities.next();

			client.save(auditoria);
		}

		this.audit = new HashSet<AuditoriaDTO>();
	}

	private SessionFactory getSessionFactory() {

		if (this.session == null) {

			this.sessionFactory = this.applicationContext
					.getBean(SessionFactory.class);
		}

		return this.sessionFactory;
	}

	private Session getSession() {

		if (this.session == null) {

			SessionFactory sessionFactory = getSessionFactory();

			this.session = sessionFactory.openSession();
			this.session.sessionWithOptions().noInterceptor();
		}

		return this.session;
	}

	private Session getNewSession() {

		Session session = getSessionFactory().openSession();

		session.sessionWithOptions().noInterceptor();

		return session;
	}

	private void validarAndamnetoFechamentoDiario() {

		getSession().flush();
		getSession().clear();

		Query query = getSession().createQuery(
				"select o.fechamentoDiarioEmAndamento from Distribuidor o");

		query.setMaxResults(1);

		Boolean fechamentoDiarioEmAndamento = (Boolean) query.uniqueResult();

		if (fechamentoDiarioEmAndamento != null
				&& Boolean.TRUE.equals(fechamentoDiarioEmAndamento)) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Fechamento diario em andamento! Por favor aguarde.");
		}
	}

	private boolean validarLiberacaoLock(Object entity, String[] propertyNames) {
		boolean liberar = false;
		for (Object objeto : propertyNames) {
			if (objeto.equals("fechamentoDiarioEmAndamento")
					&& entity instanceof Distribuidor) {
				liberar = true;
			}
		}
		return liberar;
	}
}