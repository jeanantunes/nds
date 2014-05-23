package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.enums.TipoFlag;
import br.com.abril.nds.model.cadastro.FlagPendenteAtivacao;
import br.com.abril.nds.model.fiscal.TipoEntidadeDestinoFlag;
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
	public FlagPendenteAtivacao obterPor(TipoFlag tipoFlag, TipoEntidadeDestinoFlag tipoEntidadeDestinoFlag, Long idAlterado) {
		
		Criteria criteria = getSession().createCriteria(FlagPendenteAtivacao.class);
		criteria.add(Restrictions.eq("nome", tipoFlag));
		criteria.add(Restrictions.eq("tipo", tipoEntidadeDestinoFlag));
		criteria.add(Restrictions.eq("idAlterado", idAlterado));
		
		return (FlagPendenteAtivacao) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FlagPendenteAtivacao> obterPor(TipoEntidadeDestinoFlag tipoEntidadeDestinoFlag) {
		
		Criteria criteria = getSession().createCriteria(FlagPendenteAtivacao.class);
		criteria.add(Restrictions.eq("tipo", tipoEntidadeDestinoFlag));
		
		return criteria.list();
	}
	
}