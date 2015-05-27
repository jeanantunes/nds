package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.enums.Dominio;
import br.com.abril.nds.enums.Flag;
import br.com.abril.nds.model.cadastro.FlagPendenteAtivacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FlagPendenteAtivacaoRepository;

@Repository
public class FlagPendenteAtivacaoRepositoryImpl extends AbstractRepositoryModel<FlagPendenteAtivacao, Long> implements FlagPendenteAtivacaoRepository {

	public FlagPendenteAtivacaoRepositoryImpl() {
		super(FlagPendenteAtivacao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FlagPendenteAtivacao> liberarFlag(FlagPendenteAtivacao flagPendenteAtivacao) {
		
		StringBuilder hql  = new StringBuilder();
		
		hql.append("from FlagPendenteAtivacao fpa ");
		
		Query query = getSession().createQuery(hql.toString());
		
		return query.list();
	}

	@Override
	public FlagPendenteAtivacao obterPor(Flag flag, Long idAlterado) {
		
		Criteria criteria = getSession().createCriteria(FlagPendenteAtivacao.class);
		criteria.add(Restrictions.eq("flag", flag));
		criteria.add(Restrictions.eq("idAlterado", idAlterado));
		
		return (FlagPendenteAtivacao) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FlagPendenteAtivacao> obterPor(Dominio dominio) {
		
		Criteria criteria = getSession().createCriteria(FlagPendenteAtivacao.class);
		criteria.add(Restrictions.eq("dominio", dominio));
		
		return criteria.list();
	}
	
}