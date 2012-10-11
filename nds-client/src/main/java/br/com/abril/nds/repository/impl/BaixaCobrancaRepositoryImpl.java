package br.com.abril.nds.repository.impl;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.BaixaManual;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.repository.BaixaCobrancaRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.financeiro.BaixaCobranca}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class BaixaCobrancaRepositoryImpl extends AbstractRepositoryModel<BaixaCobranca,Long> implements BaixaCobrancaRepository {
	
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
		Criteria criteria = getSession().createCriteria(BaixaAutomatica.class);
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
		Criteria criteria = getSession().createCriteria(BaixaAutomatica.class);
		criteria.setProjection(Projections.max("dataBaixa"));
		criteria.add(Restrictions.isNotNull("nomeArquivo"));
		return (Date) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CobrancaVO> buscarCobrancasBaixadas(Integer numCota, String nossoNumero) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cast(cobranca.id as string) as codigo, ");
		hql.append(" pessoa.nome as nome, ");
		hql.append(" cast(cobranca.dataEmissao as string) as dataEmissao, ");
		hql.append(" cast(cobranca.dataVencimento as string) as dataVencimento, ");
		hql.append(" cast(cobranca.valor as string) as valor ");
		hql.append(" FROM BaixaManual baixaManual ");
		hql.append(" JOIN baixaManual.cobranca cobranca ");
		hql.append(" JOIN cobranca.divida divida ");
		hql.append(" JOIN divida.cota cota ");
		hql.append(" JOIN cota.pessoa pessoa ");
		hql.append(" WHERE baixaManual.statusAprovacao = :statusAprovacao ");
		hql.append(" AND cobranca.statusCobranca = :statusCobranca ");
		hql.append(" AND divida.status = :statusDivida ");
		hql.append(" AND cota.numeroCota = :numCota ");
		
		if(nossoNumero !=  null && !nossoNumero.trim().isEmpty()){
			hql.append(" AND cobranca.nossoNumero = :nossoNumero ");
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("numCota", numCota);
		query.setParameter("statusCobranca", StatusCobranca.PAGO);
		query.setParameter("statusDivida", StatusDivida.QUITADA);
		query.setParameter("statusAprovacao", StatusAprovacao.PENDENTE);
		
		if(nossoNumero !=  null && !nossoNumero.trim().isEmpty()){
			query.setParameter("nossoNumero", nossoNumero);
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(CobrancaVO.class));
		
		return query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BaixaCobranca obterUltimaBaixaCobranca(Long idCobranca) {

		Criteria criteria = getSession().createCriteria(BaixaCobranca.class);
		
		criteria.add(Restrictions.eq("cobranca.id", idCobranca));
		criteria.addOrder(Order.desc("dataBaixa"));
		
		criteria.setMaxResults(1);
		
		return (BaixaCobranca) criteria.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<BaixaManual> obterBaixasManual(List<Long> idsCobranca) {
		
		Criteria criteria = getSession().createCriteria(BaixaManual.class);
		
		criteria.add(Restrictions.in("cobranca.id", idsCobranca));
		
		return criteria.list();
	}
	
}
