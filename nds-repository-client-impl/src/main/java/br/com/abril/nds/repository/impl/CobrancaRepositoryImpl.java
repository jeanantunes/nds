package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.CobrancaVO;
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

	@Override
    public Date obterDataAberturaDividas(Long idCota) {
		
		Criteria criteria = getSession().createCriteria(Cobranca.class,"cobranca");
		criteria.createAlias("cobranca.cota", "cota");
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("statusCobranca", StatusCobranca.NAO_PAGO));
		criteria.setProjection(Projections.min("dataVencimento"));			
		
		return (Date) criteria.uniqueResult();
	}
	
	
	@Override
    @SuppressWarnings("unchecked")
	public List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota, boolean obtemCobrancaOrigemNegociacao, Date data) {		
		
		Criteria criteria = getSession().createCriteria(Cobranca.class,"cobranca");
		
		criteria.createAlias("cobranca.cota", "cota");
		criteria.createAlias("cobranca.divida", "divida");
		
		criteria.add(Restrictions.eq("divida.origemNegociacao", obtemCobrancaOrigemNegociacao));
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("statusCobranca", StatusCobranca.NAO_PAGO));
		criteria.add(Restrictions.isNull("cobranca.dataPagamento"));
		
		criteria.add(Restrictions.lt("dataVencimento", data));
		
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
	
	
	@Override
    public Cobranca obterCobrancaPorNossoNumero(String nossoNumero){
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select c from Cobranca c where c.nossoNumero = :nossoNumero ");
		
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("nossoNumero", nossoNumero);
		
		return (Cobranca) query.uniqueResult();
		
	}
	
	@Override
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
		
		StringBuilder sql = new StringBuilder(" select count(*) as contagem from ( ");
		
		sql.append(this.obterConsultaCobrancasPorCota(filtro));
		
		sql.append(" ) as cobrancas ");

		Query query = this.createQueryCobrancasPorCota(filtro, sql.toString());
		
		((SQLQuery) query).addScalar("contagem", StandardBasicTypes.LONG);

		quantidade = (Long) query.uniqueResult();

		return quantidade;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CobrancaVO> obterCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro) {
		
		StringBuilder hql = this.obterConsultaCobrancasPorCota(filtro);

		if (filtro.getOrdenacaoColuna() != null) {
			
			hql.append(" ORDER BY ");
			hql.append(filtro.getOrdenacaoColuna().toString());
			hql.append(" ");
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}
		}

		Query query = this.createQueryCobrancasPorCota(filtro, hql.toString()); 
				
		if (filtro.getPaginacao() != null) {
        	
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		((SQLQuery) query).addScalar("codigo", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery) query).addScalar("numeroCota");
		((SQLQuery) query).addScalar("nome");
		((SQLQuery) query).addScalar("dataEmissao", StandardBasicTypes.DATE);
        ((SQLQuery) query).addScalar("dataVencimento", StandardBasicTypes.DATE);
		((SQLQuery) query).addScalar("valor", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery) query).addScalar("boletoAntecipado", StandardBasicTypes.BOOLEAN);
		((SQLQuery) query).addScalar("nossoNumero", StandardBasicTypes.STRING);

        try {
        	
			query.setResultTransformer(new AliasToBeanConstructorResultTransformer(
				CobrancaVO.class.getConstructor(
					BigInteger.class, Integer.class, String.class, Date.class, Date.class, BigDecimal.class, boolean.class, String.class)
				)
			);

		} catch (NoSuchMethodException e) {
			
			throw new IllegalArgumentException("Construtor inválido");
			
		} catch (SecurityException e) {

			throw new IllegalArgumentException("Construtor inválido");
		}
		
		return ((SQLQuery) query).list();
	}
	
	private StringBuilder obterConsultaCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ct.NUMERO_COTA as numeroCota, c.ID as codigo, ");
		hql.append(" COALESCE(p.NOME, p.RAZAO_SOCIAL) as nome, c.DT_EMISSAO as dataEmissao, "); 
		hql.append(" c.DT_VENCIMENTO as dataVencimento, c.VALOR as valor, false as boletoAntecipado, ");
		hql.append(" c.NOSSO_NUMERO as nossoNumero ");
		hql.append(" FROM cobranca c ");
		hql.append(" LEFT JOIN BAIXA_COBRANCA bc on (c.ID = bc.COBRANCA_ID and bc.STATUS_APROVACAO is null) ");
		hql.append(" LEFT JOIN DIVIDA d on d.ID = (c.DIVIDA_ID and d.STATUS != :statusPendenteInadimplencia) ");
		hql.append(" INNER JOIN COTA ct on ct.ID = c.COTA_ID ");
		hql.append(" INNER JOIN PESSOA p on p.ID = ct.PESSOA_ID ");
		hql.append(" WHERE ct.NUMERO_COTA = :ncota ");
		hql.append(" AND c.DT_PAGAMENTO IS NULL ");
		
		if (filtro.getDataVencimento()!=null){
		    hql.append(" AND c.DT_VENCIMENTO <= :vcto ");
		}
		
		if (filtro.getStatusCobranca()!=null){
			hql.append(" AND c.STATUS_COBRANCA = :status ");
		}
		
		if (filtro.isAcumulaDivida()){
		    hql.append(" AND ( (d.ACUMULADA = :acumulada) OR (d.DATA = :data) )");
		}

		hql.append(" UNION ALL ");
		
		hql.append(" SELECT ct.NUMERO_COTA as numeroCota, null as codigo, ");
		hql.append(" COALESCE(p.NOME, p.RAZAO_SOCIAL) as nome, ba.DATA as dataEmissao, "); 
		hql.append(" ba.DATA_VENCIMENTO as dataVencimento, ba.VALOR as valor, true as boletoAntecipado, ");
		hql.append(" ba.NOSSO_NUMERO as nossoNumero ");
		hql.append(" FROM boleto_antecipado ba ");
		hql.append(" INNER join chamada_encalhe_cota ce on ba.CHAMADA_ENCALHE_COTA_ID = ce.ID ");
		hql.append(" INNER join cota ct on ct.ID = ce.COTA_ID ");
		hql.append(" INNER join pessoa p on p.ID = ct.PESSOA_ID ");
		hql.append(" WHERE ct.NUMERO_COTA = :ncota ");
		hql.append(" AND ba.STATUS != :quitada ");
		
		return hql;
	}
	
	private Query createQueryCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro, String consulta) {
	
		Query query = super.getSession().createSQLQuery(consulta);

		query.setParameter("ncota", filtro.getNumeroCota());
		query.setParameter("statusPendenteInadimplencia", StatusDivida.PENDENTE_INADIMPLENCIA.name());
		query.setParameter("quitada", StatusDivida.QUITADA.name());

		if (filtro.getDataVencimento()!=null){
		    query.setDate("vcto", filtro.getDataVencimento());
		}
		
		if (filtro.getStatusCobranca()!=null){
		    query.setParameter("status", filtro.getStatusCobranca().name());
		}
		
		if (filtro.isAcumulaDivida()){
			query.setParameter("acumulada", filtro.isAcumulaDivida());
			query.setParameter("data", filtro.getDataVencimento());
		}

		return query;
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
		hql.append(" join cob.divida.consolidados cons ")
		   .append(" join cons.movimentos mov ")
		   .append(" where mov.id = :idMovimentoFinanceiro");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idMovimentoFinanceiro", idMovimentoFinanceiro);
		
		return (String) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NegociacaoDividaDetalheVO> obterDetalhesCobranca(Long idCobranca) {
		

		StringBuilder hql = new StringBuilder("select ");
		hql.append(" m.valor as valor, c.dataEmissao as data, ")
		   .append(" m.tipoMovimento.descricao || ")
		   .append(" (case when m.observacao is null then '' else (' - ' || m.observacao) end) as observacao, ")
		   .append(" m.tipoMovimento as tipoMovimentoFinanceiro ")
		   .append(" from Cobranca c ")
		   .append(" join c.divida.consolidados consolidados")
		   .append(" join consolidados.movimentos m ")
		   .append(" where c.id = :idCobranca ")
		   .append(" order by m.dataCriacao desc ");
		
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

    @SuppressWarnings("unchecked")
    @Override
    public List<Cobranca> obterCobrancasDataEmissaoMaiorQue(Date dataPagamento, List<Long> idCobrancas) {
        Criteria criteria = this.getSession().createCriteria(Cobranca.class, "cobranca");
        criteria.add(Restrictions.in("cobranca.id", idCobrancas.toArray()));
        criteria.add(Restrictions.gt("cobranca.dataEmissao", dataPagamento));
        return criteria.list();
    }
}
