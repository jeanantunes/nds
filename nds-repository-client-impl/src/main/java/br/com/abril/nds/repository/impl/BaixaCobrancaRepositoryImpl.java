package br.com.abril.nds.repository.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.BaixaManual;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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

	@Override
	public Long countBuscarCobrancasBaixadas(FiltroConsultaDividasCotaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		HashMap<String, Object> param =  new HashMap<>();
		
		hql.append("select count(baixaManual.id) ");

		this.whereFromBuscarCobrancasBaixadas(hql, param, filtro);
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, param);
		return (Long) query.uniqueResult();
	}
	
	private void whereFromBuscarCobrancasBaixadas(StringBuilder hql, HashMap<String, Object> param, FiltroConsultaDividasCotaDTO filtro) {
		
		Integer numCota = filtro.getNumeroCota();
		String nossoNumero = filtro.getNossoNumero();
		
		hql.append(" FROM BaixaManual baixaManual ");
		hql.append(" JOIN baixaManual.cobranca cobranca ");
		hql.append(" JOIN cobranca.divida divida ");
		hql.append(" JOIN divida.cota cota ");
		hql.append(" JOIN cota.pessoa pessoa ");
		hql.append(" WHERE baixaManual.statusAprovacao = :statusAprovacao ");
		hql.append(" AND cota.numeroCota = :numCota ");
		
		if(nossoNumero !=  null && !nossoNumero.trim().isEmpty()){
			hql.append(" AND cobranca.nossoNumero = :nossoNumero ");
		}
		
		param.put("numCota", numCota);
		param.put("statusAprovacao", StatusAprovacao.PENDENTE);
			
		if(nossoNumero !=  null && !nossoNumero.trim().isEmpty()){
			param.put("nossoNumero", nossoNumero);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CobrancaVO> buscarCobrancasBaixadas(FiltroConsultaDividasCotaDTO filtro) {
		
		HashMap<String, Object> param =  new HashMap<>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cast(cobranca.id as string) as codigo, ");
		hql.append(" coalesce(pessoa.nome, pessoa.razaoSocial) as nome, ");
		hql.append(" cast(cobranca.dataEmissao as string) as dataEmissao, ");
		hql.append(" cast(cobranca.dataVencimento as string) as dataVencimento, ");
		hql.append(" cast(cobranca.valor as string) as valor, ");
		hql.append(" cobranca.nossoNumero as nossoNumero, ");
		hql.append(" cota.numeroCota as numeroCota ");
				
		this.whereFromBuscarCobrancasBaixadas(hql, param, filtro);
	
		if (filtro.getPaginacao() != null) {
			
			String sortOrder = " " + filtro.getPaginacao().getSortOrder();
			
			hql.append(" order by ");
			hql.append(filtro.getOrdenacaoColuna());
			hql.append(filtro.getPaginacao().getSortOrder() == null ? " asc " : sortOrder);
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, param);

		if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {

				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {

				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
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

    @Override
    public String obterDescricaoBaixaPorCobranca(Long idCobranca) {
        
        Query query = this.getSession().createQuery(
                "select b.observacao from BaixaCobranca b join b.cobranca c where c.id = :idCobranca");
        
        query.setParameter("idCobranca", idCobranca);
        
        return (String) query.uniqueResult();
    }
}
