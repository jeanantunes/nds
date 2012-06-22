package br.com.abril.nds.repository.impl;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.repository.BaixaCobrancaRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.financeiro.BaixaCobranca}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class BaixaCobrancaRepositoryImpl extends AbstractRepository<BaixaCobranca,Long> implements BaixaCobrancaRepository {
	
	/**
	 * Construtor padrão
	 */
	public BaixaCobrancaRepositoryImpl() {
		super(BaixaCobranca.class);
	}

	/**
	 * Busca a última baixa automática realizada no dia
	 * @param dataOperacao
	 * @return BaixaAutomatica
	 */
	@Override
	public Date buscarUltimaBaixaAutomaticaDia(Date dataOperacao) {
		Criteria criteria = getSession().createCriteria(HistoricoSituacaoCota.class);
		criteria.add(Restrictions.eq("dataBaixa", dataOperacao));
		criteria.add(Restrictions.isNotNull("nomeArquivo"));
		criteria.setProjection(Projections.max("dataBaixa"));
		return (Date) criteria.uniqueResult();
	}

	/**
	 * Busca o dia da última baixa automática realizada
	 * @return Date
	 */
	@Override
	public Date buscarDiaUltimaBaixaAutomatica() {
		Criteria criteria = getSession().createCriteria(Expedicao.class);
		criteria.setProjection(Projections.max("dataBaixa"));
		criteria.add(Restrictions.isNotNull("nomeArquivo"));
		return (Date) criteria.uniqueResult();
	}

}
