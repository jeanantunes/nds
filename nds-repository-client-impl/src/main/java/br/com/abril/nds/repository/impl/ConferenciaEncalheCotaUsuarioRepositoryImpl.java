package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.ConferenciaEncalheCotaUsuario;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ConferenciaEncalheCotaUsuarioRepository;

@Repository
public class ConferenciaEncalheCotaUsuarioRepositoryImpl extends AbstractRepositoryModel<ConferenciaEncalheCotaUsuario, Long> 
		implements ConferenciaEncalheCotaUsuarioRepository {

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
		
		if(numeroCota == null) throw new ValidacaoException(TipoMensagem.WARNING, "Número da Cota não informado.");
		
		String hql = "delete ConferenciaEncalheCotaUsuario where numeroCota = :numeroCota";
		Query query = getSession().createQuery(hql);
		query.setParameter("numeroCota", numeroCota);
		
		return query.executeUpdate();
	}

	@Override
	public int removerPorLogin(String login) {
		
		if(login == null) throw new ValidacaoException(TipoMensagem.WARNING, "Login não informado.");
		
		String hql = "delete ConferenciaEncalheCotaUsuario where login = :login";
		Query query = getSession().createQuery(hql);
		query.setParameter("login", login);
		
		return query.executeUpdate();
	}

	@Override
	public int removerPorSessionId(String sessionId) {
		
		if(sessionId == null) throw new ValidacaoException(TipoMensagem.WARNING, "Sessão não informada.");
		
		String hql = "delete from ConferenciaEncalheCotaUsuario where sessionId = :sessionId";
		Query query = getSession().createQuery(hql);
		query.setParameter("sessionId", sessionId);
		
		return query.executeUpdate();
	}

	@Override
	public int removerTodos() {
		
		String hql = "delete from ConferenciaEncalheCotaUsuario";
		Query query = getSession().createQuery(hql);
		
		return query.executeUpdate();
	}
	
}