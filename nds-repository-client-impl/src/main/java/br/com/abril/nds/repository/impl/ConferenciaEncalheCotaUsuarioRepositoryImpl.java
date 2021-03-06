package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.ConferenciaEncalheCotaUsuario;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ConferenciaEncalheCotaUsuarioRepository;

@Repository
public class ConferenciaEncalheCotaUsuarioRepositoryImpl extends AbstractRepositoryModel<ConferenciaEncalheCotaUsuario, Long> 
		implements ConferenciaEncalheCotaUsuarioRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConferenciaEncalheCotaUsuarioRepositoryImpl.class);
	
	public ConferenciaEncalheCotaUsuarioRepositoryImpl() {
		super(ConferenciaEncalheCotaUsuario.class);
	}

	@Override
	public ConferenciaEncalheCotaUsuario obterPorNumeroCota(Integer numeroCota) {
		
		Criteria c = this.getSession().createCriteria(ConferenciaEncalheCotaUsuario.class);
		c.add(Restrictions.eq("numeroCota", numeroCota));
		
		return (ConferenciaEncalheCotaUsuario) c.uniqueResult();
	}

	@Override
	public ConferenciaEncalheCotaUsuario obterPorLogin(String login) {
		
		Criteria c = this.getSession().createCriteria(ConferenciaEncalheCotaUsuario.class);
		c.add(Restrictions.eq("login", login));
		
		return (ConferenciaEncalheCotaUsuario) c.uniqueResult();
	}

	@Override
	public ConferenciaEncalheCotaUsuario obterPorSessionId(String sessionId) {
		
		Criteria c = this.getSession().createCriteria(ConferenciaEncalheCotaUsuario.class);
		c.add(Restrictions.eq("sessionId", sessionId));
		
		return (ConferenciaEncalheCotaUsuario) c.uniqueResult();
	}

	@Override
	public int removerPorNumeroCota(Integer numeroCota) {
		LOGGER.warn("REMOVENDO TRAVA CONFERENCIA ENCALHE por  cota "+numeroCota);
		if(numeroCota == null) throw new ValidacaoException(TipoMensagem.WARNING, "Número da Cota não informado.");
		
		String hql = "delete ConferenciaEncalheCotaUsuario where numeroCota = :numeroCota";
		Query query = getSession().createQuery(hql);
		query.setParameter("numeroCota", numeroCota);
		
		return query.executeUpdate();
	}

	@Override
	public int removerPorLogin(String login) {
		LOGGER.warn("REMOVENDO TRAVA CONFERENCIA ENCALHE por login "+login);
		if(login == null) throw new ValidacaoException(TipoMensagem.WARNING, "Login não informado.");
		
		String hql = "delete ConferenciaEncalheCotaUsuario where login = :login";
		Query query = getSession().createQuery(hql);
		query.setParameter("login", login);
		
		return query.executeUpdate();
	}

	@Override
	public int removerPorSessionId(String sessionId) {
		LOGGER.warn("REMOVENDO TRAVA CONFERENCIA ENCALHE por sessao "+sessionId);
		if(sessionId == null) throw new ValidacaoException(TipoMensagem.WARNING, "Sessão não informada.");
		
		String hql = "delete from ConferenciaEncalheCotaUsuario where sessionId = :sessionId";
		Query query = getSession().createQuery(hql);
		query.setParameter("sessionId", sessionId);
		
		return query.executeUpdate();
	}

	@Override
	public int removerTodos() {
		LOGGER.warn("REMOVENDO TRAVA CONFERENCIA ENCALHE todos ");
		String hql = "delete from ConferenciaEncalheCotaUsuario";
		Query query = getSession().createQuery(hql);
		
		return query.executeUpdate();
	}
	
}