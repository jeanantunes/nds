package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.NegociacaoDividaDetalheVO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CobrancaRepository;

@Repository
public class CobrancaRepositoryImpl extends AbstractRepositoryModel<Cobranca, Long> implements CobrancaRepository {

	public CobrancaRepositoryImpl() {
		super(Cobranca.class);		
	}

	public Date obterDataAberturaDividas(Long idCota) {
		
		Criteria criteria = getSession().createCriteria(Cobranca.class,"cobranca");
		criteria.createAlias("cobranca.cota", "cota");
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("statusCobranca", StatusCobranca.NAO_PAGO));
		criteria.setProjection(Projections.min("dataVencimento"));			
		
		return (Date) criteria.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota, boolean obtemCobrancaOrigemNegociacao) {		
		
		Criteria criteria = getSession().createCriteria(Cobranca.class,"cobranca");
		
		criteria.createAlias("cobranca.cota", "cota");
		criteria.createAlias("cobranca.divida", "divida");
		
		criteria.add(Restrictions.eq("divida.origemNegociacao", obtemCobrancaOrigemNegociacao));
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("statusCobranca", StatusCobranca.NAO_PAGO));
		criteria.add(Restrictions.isNull("divida.dividaRaiz"));
		criteria.addOrder(Order.asc("dataVencimento"));
		
		return criteria.list();				
	}
	
	/**
	 * Obtem cobrancas em aberto que não estejam associadas a 
	 * operacao de encalhe em questão (caso flag seja true)
	 * 
	 * @param idCota
	 * @param idControleConfEncCota
	 * 
	 * @return List - DebitoCreditoCotaDTO
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DebitoCreditoCotaDTO> obterCobrancasDaCotaEmAbertoAssociacaoConferenciaEncalhe(Long idCota, Long idControleConfEncCota, Date data) {
		
		StringBuffer sql = new StringBuffer();
			
		sql.append(" SELECT cobranca.VALOR as valor, cobranca.DT_EMISSAO as dataLancamento "); 
		
		sql.append(" FROM COBRANCA cobranca ");
		
		sql.append(" inner join DIVIDA d on ( 	");
		sql.append(" 	cobranca.DIVIDA_ID = d.id 	");
		sql.append(" ) ");
		
		if(idControleConfEncCota != null) {

			sql.append(" left join COBRANCA_CONTROLE_CONFERENCIA_ENCALHE_COTA cc on ( ");
			sql.append(" 	cc.CONTROLE_CONF_ENCALHE_COTA_ID = :idControleConfEncCota and	");
			sql.append(" 	cc.COBRANCA_ID = cobranca.ID	");
			sql.append(" )	");
			
		}
		
		
		sql.append(" inner join COTA cota on ( 	");
		sql.append(" 	cota.ID =  cobranca.COTA_ID	");
		sql.append(" ) ");
		
		sql.append(" WHERE ");
		
		sql.append(" cobranca.STATUS_COBRANCA = :statusCobranca and ");

		sql.append(" cota.id = :idCota and ");
		
		sql.append(" d.ORIGEM_NEGOCIACAO = :origemNegociacao and ");
		
		sql.append(" cobranca.DT_VENCIMENTO <= :data ");
		
		if(idControleConfEncCota != null) {
			sql.append(" and cc.id is null ");
		}
		
		
		sql.append(" GROUP BY cobranca.id ORDER BY cobranca.DT_VENCIMENTO ASC ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DebitoCreditoCotaDTO.class));
		
		query.setParameter("idCota", idCota);
		
		if(idControleConfEncCota != null) {
			query.setParameter("idControleConfEncCota", idControleConfEncCota);
		}
		
		query.setParameter("statusCobranca", StatusCobranca.NAO_PAGO.name());
		
		query.setParameter("origemNegociacao", false);
		
		query.setParameter("data", data);
		
		return query.list();
		
	}
	
	
	public Cobranca obterCobrancaPorNossoNumero(String nossoNumero){
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select c from Cobranca c where c.nossoNumero = :nossoNumero ");
		
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("nossoNumero", nossoNumero);
		
		return (Cobranca) query.uniqueResult();
		
	}
	
	public void incrementarVia(String... nossoNumero){
		Query query = this.getSession().createQuery("update Cobranca set vias = vias + 1 where nossoNumero IN (:nossoNumero)");
		query.setParameterList("nossoNumero", nossoNumero);
		query.executeUpdate();
	}
	
	
	/**
	 * Método responsável por obter a quantidade de cobrancas
	 * @param filtro
	 * @return quantidade: quantidade de cobrancas
	 */
	@Override
	public long obterQuantidadeCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro){
		long quantidade = 0;
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(c) from Cobranca c where ");		
		hql.append(" c.cota.numeroCota = :ncota ");
		
		if (filtro.getDataVencimento()!=null){
		    hql.append(" and c.dataVencimento <= :vcto ");
		}
		
		if (filtro.getStatusCobranca()!=null){
			hql.append(" and c.statusCobranca = :status ");
		}
		
		if (filtro.isAcumulaDivida()){
		    hql.append(" and ( (c.divida.acumulada = :acumulada) or (c.divida.data = :data) )");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("ncota", filtro.getNumeroCota());
		
		if (filtro.getDataVencimento()!=null){
		    query.setDate("vcto", filtro.getDataVencimento());
		}
		
		if (filtro.getStatusCobranca()!=null){
		    query.setParameter("status", filtro.getStatusCobranca());
		}
		
		if (filtro.isAcumulaDivida()){
			query.setParameter("acumulada", filtro.isAcumulaDivida());
			query.setParameter("data", filtro.getDataVencimento());
		}
		
		quantidade = (Long) query.uniqueResult();
		return quantidade;
	}
	
	/**
	 * Método responsável por obter uma lista de cobrancas
	 * @param filtro
	 * @return query.list(): lista de cobrancas
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Cobranca> obterCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();
		hql.append(" select c from Cobranca c ");		
		hql.append(" left join c.baixasCobranca baixa ");
		hql.append(" left join c.divida divida ");
		hql.append(" where c.cota.numeroCota = :ncota ");
		hql.append(" and baixa.statusAprovacao is null ");
		hql.append(" and divida.status != :statusPendenteInadimplencia ");
		
		
		if (filtro.getDataVencimento()!=null){
		    hql.append(" and c.dataVencimento <= :vcto ");
		}
		
		if (filtro.getStatusCobranca()!=null){
			hql.append(" and c.statusCobranca = :status ");
		}
		
		if (filtro.isAcumulaDivida()){
		    hql.append(" and ( (c.divida.acumulada = :acumulada) or (c.divida.data = :data) )");
		}

		if (filtro.getOrdenacaoColuna() != null) {
			switch (filtro.getOrdenacaoColuna()) {
				case NUMERO_COTA:
					hql.append(" order by c.cota.numeroCota ");
					break;
				case NOME_COTA:
					hql.append(" order by c.cota.pessoa.nome ");
					break;
				case DATA_EMISSAO:
					hql.append(" order by c.dataEmissao ");
					break;
				case DATA_VENCIMENTO:
					hql.append(" order by c.dataVencimento ");
					break;
				case VALOR:
					hql.append(" order by c.valor ");
					break;
				default:
					break;
			}
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}	
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("ncota", filtro.getNumeroCota());
		
		if (filtro.getDataVencimento()!=null){
		    query.setDate("vcto", filtro.getDataVencimento());
		}
		
		if (filtro.getStatusCobranca()!=null){
		    query.setParameter("status", filtro.getStatusCobranca());
		}
		
		if (filtro.isAcumulaDivida()){
			query.setParameter("acumulada", filtro.isAcumulaDivida());
			query.setParameter("data", filtro.getDataVencimento());
		}
		
		query.setParameter("statusPendenteInadimplencia", StatusDivida.PENDENTE_INADIMPLENCIA);

        if (filtro.getPaginacao() != null) {
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}

		return (List<Cobranca>)query.list();
	}

	@Override
	public void excluirCobrancaPorIdDivida(Long idDivida) {
		
		Query query = this.getSession().createQuery("delete from Cobranca where divida.id = :idDivida");
		
		query.setParameter("idDivida", idDivida);
		
		query.executeUpdate();
	}

	
	/**
	 * Método responsável por obter uma lista de cobrancas ordenadas por data de vencimento
	 * @param List<Long>: Id's de cobranças
	 * @return query.list(): lista de cobrancas
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Cobranca> obterCobrancasOrdenadasPorVencimento(List<Long> idCobrancas) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from Cobranca c ");		
		for(int i=0; i< idCobrancas.size(); i++){
			if(i==0){
				hql.append(" where c.id = "+idCobrancas.get(i));
			}
			else{
		        hql.append(" or c.id = "+idCobrancas.get(i));
			}
		}
		hql.append(" order by c.dataVencimento, c.valor ");
		
		Query query = super.getSession().createQuery(hql.toString());

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cobranca> obterCobrancasPorIDS(List<Long> listaCobrancas) {
		
		Criteria criteria = super.getSession().createCriteria(Cobranca.class);
		
		criteria.add(Restrictions.in("id", listaCobrancas));
		
		return criteria.list();
	}
	
	@Override
	public BigDecimal obterValorCobrancasQuitadasPorData(Date data){
		
		StringBuilder hql = new StringBuilder("select sum(c.valor) ");
		hql.append(" from Cobranca c ")
		   .append(" where c.dataPagamento is not null ");
		
		if (data != null){
			
			hql.append(" and c.dataVencimento <= :data ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (data != null){
			
			query.setParameter("data", data);
		}
		
		return (BigDecimal) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cobranca> obterCobrancasEfetuadaNaDataOperacaoDistribuidor(Date dataOperacao) {
		
		Criteria criteria = this.getSession().createCriteria(Cobranca.class);
		criteria.add(Restrictions.eq("dataEmissao", dataOperacao));
	
		return criteria.list();
	}

	@Override
	public String obterNossoNumeroPorMovimentoFinanceiroCota(Long idMovimentoFinanceiro) {
		
		StringBuilder hql = new StringBuilder("select cob.nossoNumero from Cobranca cob ");
		hql.append(" join cob.divida.consolidado.movimentos mov ")
		   .append(" where mov.id = :idMovimentoFinanceiro");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idMovimentoFinanceiro", idMovimentoFinanceiro);
		
		return (String) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NegociacaoDividaDetalheVO> obterDetalhesCobranca(Long idCobranca) {
		

		StringBuilder hql = new StringBuilder("select ");
		hql.append(" m.valor as valor, c.dataEmissao as data, case when m.observacao is null then '' else m.observacao end as observacao")
		   .append(" from Cobranca c ")
		   .append(" join c.divida.consolidado.movimentos m ")
		   .append(" where c.id = :idCobranca ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCobranca", idCobranca);
		query.setResultTransformer(new AliasToBeanResultTransformer(NegociacaoDividaDetalheVO.class));
		
		return query.list();
	}

	@Override
	public void atualizarVias(Boleto boleto) {
		// Atualiza a via do boleto
		if (boleto.getVias() != null) {
			boleto.setVias(boleto.getVias()+1);
			this.alterar(boleto);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoCobranca> obterTiposCobrancaCadastradas() {
		
		StringBuilder hql = new StringBuilder("select distinct f.tipoCobranca ");
		hql.append(" from PoliticaCobranca p ")
		   .append(" join p.formaCobranca f ")
		   .append(" where p.ativo = :indAtivo ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("indAtivo", true);
		
		return query.list();
	}
}
