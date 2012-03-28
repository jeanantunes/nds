package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao;
import br.com.abril.nds.repository.RotaRoteiroOperacaoRepository;

@Repository
public class RotaRoteiroOperacaoRepositoryImpl  extends AbstractRepository<RotaRoteiroOperacao,Long> implements RotaRoteiroOperacaoRepository {
	
	public RotaRoteiroOperacaoRepositoryImpl() {
		super(RotaRoteiroOperacao.class);
	}

	@Override
	public RotaRoteiroOperacao obterRoterioImpressaoDividaCota(Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select rotaRoteiro ")
		   .append(" from RotaRoteiroOperacao  rotaRoteiro ")
		   .append(" join rotaRoteiro.cota cota ")
		   .append(" where cota.numeroCota = :numeroCota ")
		   .append(" and rotaRoteiro.tipoOperacao =:tipoOperacao ");
		   
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("numeroCota", numeroCota);
		query.setParameter("tipoOperacao",RotaRoteiroOperacao.TipoOperacao.IMPRESSAO_DIVIDA);
		query.setMaxResults(1);
		
		return (RotaRoteiroOperacao) query.uniqueResult();
	}
}
