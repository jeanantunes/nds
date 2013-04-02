<<<<<<< HEAD
package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO.ColunaOrdenacao;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.vo.PaginacaoVO;


@Repository
public class DividaRepositoryImpl extends AbstractRepositoryModel<Divida, Long> implements
		DividaRepository {
	
	public DividaRepositoryImpl() {
		super(Divida.class);
	}
	
	public Divida obterDividaParaAcumuloPorCota(Long idCota, Date diaDivida){
		Criteria criteria = this.getSession().createCriteria(Divida.class);
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("data", diaDivida));
		
		return (Divida) criteria.uniqueResult();
	}
	
	@Override
	public Long obterQunatidadeDividaGeradas(Date dataMovimento){
		
		String hql  = "select count (divida.id) from Divida divida where divida.data =:data";
		
		Query query  = super.getSession().createQuery(hql);
		query.setParameter("data", dataMovimento);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeRegistroDividasGeradas(FiltroDividaGeradaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getSqldividas(true,filtro,true));
		
		Query query = super.getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosConsultaDividas(filtro,true);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		return  (Long) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GeraDividaDTO> obterDividasGeradasSemBoleto(FiltroDividaGeradaDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getSqldividas(false,filtro,false));
		
		hql.append(getOrdenacaoDivida(filtro));
		
		Query query = super.getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosConsultaDividas(filtro,false);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
	
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GeraDividaDTO> obterDividasGeradas(FiltroDividaGeradaDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getSqldividas(false,filtro, true));
		
		hql.append(getOrdenacaoDivida(filtro));
		
		Query query = super.getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosConsultaDividas(filtro,true);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		return query.list();
	}
	
	/**
	 * Retorna os parametros da consulta de dividas.
	 * @param filtro
	 * @return HashMap<String,Object>
	 */
	private HashMap<String,Object> getParametrosConsultaDividas(FiltroDividaGeradaDTO filtro,boolean isBoleto){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		param.put("data",filtro.getDataMovimento());
		param.put("acumulaDivida", Boolean.FALSE);
		param.put("statusCobranca",StatusCobranca.NAO_PAGO);
		
		if(!isBoleto){
			param.put("tipoCobrancaBoleto",TipoCobranca.BOLETO);
		}
		
		if(filtro.getNumeroCota()!= null ){
			 param.put("numeroCota",filtro.getNumeroCota());
		}
		
		if(filtro.getTipoCobranca()!= null){
			param.put("tipoCobranca",filtro.getTipoCobranca());
		}
		
		if(filtro.getIdBox()!= null ){
			param.put("box",filtro.getIdBox());
		}
		
		if(filtro.getIdRota()!= null){
			param.put("rota",filtro.getIdRota());
		}
		
		if(filtro.getIdRoteiro()!= null){
			param.put("roteiro",filtro.getIdRoteiro());
		}
		
		return param;
	}
	
	/**
	 * Retorna o hql da consulta de dividas.
	 * @param count
	 * @param filtro 
	 * @return String
	 */
	private String getSqldividas(boolean count,FiltroDividaGeradaDTO filtro, boolean isBoleto){
		
		StringBuilder hql = new StringBuilder();
		
		if(count){
			hql.append(" SELECT count(distinct divida.id )");
		}else{
			hql.append(" SELECT distinct new ").append(GeraDividaDTO.class.getCanonicalName())
			.append("(")
				.append(" box.codigo || '-'|| box.nome,")
				.append(" rota.descricaoRota,")
				.append(" roteiro.descricaoRoteiro,")
				.append(" cota.numeroCota,")
				.append(" coalesce(pessoa.nome, pessoa.razaoSocial),")
				.append(" cobranca.dataVencimento,")
				.append(" cobranca.dataEmissao,")
				.append(" cobranca.valor,")
				.append(" cobranca.tipoCobranca,")
				.append(" cobranca.vias, ")
				.append(" cobranca.nossoNumero, ")

				.append(" case when ("+
				        "           select count(f.recebeCobrancaEmail) " +
						"           from PoliticaCobranca p " +
						"           join p.formaCobranca f " +
						"           where f.recebeCobrancaEmail = true"+
						"           and p.principal=true " +//TODO
						"           and p.ativo=true " +
				        "           ) > 0 " +
						" then " + 
				
				        " (case when ("+
				        "             select count(f.parametroCobrancaCota) " +
						"             from FormaCobranca f " +
						"             join f.parametroCobrancaCota p " +
						"             join p.cota c " +
						"             where f.recebeCobrancaEmail = true " +
						"             and c.id = cota.id"+
						"             and f.principal=true " +//TODO
						"             and f.ativa=true "+
				        "             ) > 0 then true else false end)" +
						" else false end ")
						
			.append(")");
			
		}
		
		hql.append(" FROM ")
		.append(" Divida divida ")
		.append(" JOIN divida.cobranca cobranca ")
		.append(" JOIN divida.consolidado consolidado ")
		.append(" JOIN cobranca.cota cota ")
		.append(" JOIN cota.box box ")
		.append(" JOIN cota.pdvs pdv ")
		.append(" JOIN cota.pessoa pessoa ")
		.append(" JOIN cota.parametroCobranca parametroCobranca ")
		.append(" JOIN pdv.rotas rotaPdv  ")
		.append(" JOIN rotaPdv.rota rota  ")
		.append(" JOIN rota.roteiro roteiro ")
		
		.append(" WHERE ")
		
		.append(" divida.data =:data ")
		.append(" AND divida.acumulada =:acumulaDivida ")
		.append(" AND cobranca.statusCobranca=:statusCobranca ");
		
		if(filtro.getNumeroCota()!= null  ){
			hql.append(" AND cota.numeroCota =:numeroCota ");
		}	
		
		if(filtro.getTipoCobranca()!= null){
			
			hql.append(" AND cobranca.tipoCobranca =:tipoCobranca  ");
		}
		
		if(!isBoleto){
			hql.append(" AND cobranca.tipoCobranca not in (:tipoCobrancaBoleto ) ");
		}
		
		if(filtro.getIdBox()!= null ){
			hql.append(" AND box.id =:box ");
		}
		
		if(filtro.getIdRota()!= null){
			hql.append(" AND rota.id =:rota ");
		}
		
		if(filtro.getIdRoteiro()!= null ){
			hql.append(" AND roteiro.id =:roteiro ");
		}
		
		return hql.toString();
	}
	
	
	/**
	 * Retorna a string hql com a oredenação da consulta
	 * @param filtro
	 * @return String
	 */
	private String getOrdenacaoDivida(FiltroDividaGeradaDTO filtro){
		
		if( !(filtro.getListaColunaOrdenacao()!= null && !filtro.getListaColunaOrdenacao().isEmpty())){
			return "";
		}
		
		String orderByColumn = "";
		
		for( ColunaOrdenacao ordenacao : filtro.getListaColunaOrdenacao()){

			switch (ordenacao) {
				case BOX:	
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " box.codigo ";
					break;
				case DATA_EMISSAO:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cobranca.dataVencimento ";
					break;
				case DATA_VENCIMENTO:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cobranca.dataEmissao ";
					break;
				case NOME_COTA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cota.pessoa.nome ";
					break;
				case NUMERO_COTA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cota.numeroCota ";
					break;
				case ROTA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " rota.descricaoRota ";
					break;
				case ROTEIRO:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " roteiro.descricaoRoteiro ";
					break;
				case TIPO_COBRANCA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cobranca.tipoCobranca ";
					break;
				case VALOR:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cobranca.valor ";
					break;
				case VIA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cobranca.vias ";
					break;
			}
		}
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" ORDER BY ").append(orderByColumn);
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return  hql.toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<StatusDividaDTO> obterInadimplenciasCota(FiltroCotaInadimplenteDTO filtro) {
	
		StringBuilder sql =
			new StringBuilder(
				this.getSqlInadimplenciaClausulaSelect() + this.getSqlInadimplenciaClausulaFrom());
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		tratarFiltro(sql,params,filtro);
		
		sql.append(obterOrderByInadimplenciasCota(filtro));
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			sql.append(" LIMIT :inicio,:qtdeResult");
			params.put("inicio", filtro.getPaginacao().getPosicaoInicial());
			params.put("qtdeResult", filtro.getPaginacao().getQtdResultadosPorPagina());
		}
		
		Query query = getSession().createSQLQuery(sql.toString())
				.addScalar("idDivida", StandardBasicTypes.LONG)
				.addScalar("idCota", StandardBasicTypes.LONG)
				.addScalar("idCobranca", StandardBasicTypes.LONG)
				.addScalar("idNegociacao", StandardBasicTypes.LONG)
				.addScalar("comissaoSaldoDivida")
				.addScalar("numCota")
				.addScalar("nome")
				.addScalar("status")
				.addScalar("consignado")
				.addScalar("dataVencimento")
				.addScalar("dataPagamento")
				.addScalar("situacao")
				.addScalar("dividaAcumulada")
				.addScalar("diasAtraso", StandardBasicTypes.LONG);
		
		for(String key : params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		query.setResultTransformer(Transformers.aliasToBean(StatusDividaDTO.class));
				
		return query.list();
	}	
		
	private void tratarFiltro(StringBuilder sql, HashMap<String, Object> params, FiltroCotaInadimplenteDTO filtro) {
		
		boolean whereUtilizado = false;
		
		if(filtro.getPeriodoDe() != null) {
			
			sql.append(whereUtilizado ? " AND " : " WHERE ");
			whereUtilizado = true;
			
			sql.append(" COBRANCA_.DT_VENCIMENTO >= :periodoDe ");
			params.put("periodoDe", filtro.getPeriodoDe());
		}
		
		if(filtro.getPeriodoAte() != null) {
			
			sql.append(whereUtilizado ? " AND " : " WHERE ");
			whereUtilizado = true;
			
			sql.append(" COBRANCA_.DT_VENCIMENTO <= :periodoAte ");
			params.put("periodoAte",filtro.getPeriodoAte());
		}
		
		if(filtro.getNumCota() != null) {
			
			sql.append(whereUtilizado ? " AND " : " WHERE ");
			whereUtilizado = true;
			
			sql.append(" COTA_.NUMERO_COTA = :numCota ");
			params.put("numCota",filtro.getNumCota());
		}
		
		if(filtro.getNomeCota() != null) {
			
			sql.append(whereUtilizado ? " AND " : " WHERE ");
			whereUtilizado = true;
			
			sql.append("  (upper(PESSOA_.NOME) like :nomeCota or upper(PESSOA_.RAZAO_SOCIAL) like :nomeCota)");
			params.put("nomeCota", "%" + filtro.getNomeCota().toUpperCase() + "%");
		}
		
		if(filtro.getStatusCota() != null) {
			
			sql.append(whereUtilizado ? " AND " : " WHERE ");
			whereUtilizado = true;
			
			sql.append(" COTA_.SITUACAO_CADASTRO = :statusCota ");
			params.put("statusCota",filtro.getStatusCota());
		}
		
		if (filtro.getSituacaoEmAberto() || filtro.getSituacaoNegociada() || filtro.getSituacaoPaga()) {
		
			sql.append(" AND ( ");
			
			boolean utilizarOr = false;
			
			if (filtro.getSituacaoEmAberto()) {
				
				sql.append(" (COBRANCA_.DT_VENCIMENTO <= :dataAtual ");
				sql.append(" AND DIVIDA_.STATUS = :statusDividaAberto) ");
				
				utilizarOr = true;
				
				params.put("dataAtual", new Date());
				params.put("statusDividaAberto", StatusDivida.EM_ABERTO.name());
			}
			
			if (filtro.getSituacaoNegociada()) {
				
				sql.append(utilizarOr ? " OR " : "");
				
				sql.append(" (COBRANCA_.DT_PAGAMENTO > COBRANCA_.DT_VENCIMENTO ");
				sql.append(" AND DIVIDA_.STATUS = :statusDividaNegogiada) ");
				
				utilizarOr = true;
				
				params.put("statusDividaNegogiada", StatusDivida.NEGOCIADA.name());
			}
			
			if (filtro.getSituacaoPaga()) {
				
				sql.append(utilizarOr ? " OR " : "");
				
				sql.append(" (COBRANCA_.DT_PAGAMENTO > COBRANCA_.DT_VENCIMENTO ");
				sql.append(" AND DIVIDA_.STATUS = :statusDividaQuitada) ");
				
				params.put("statusDividaQuitada", StatusDivida.QUITADA.name());
			}
			
			sql.append(" ) ");
		}
	}

	private String obterOrderByInadimplenciasCota(FiltroCotaInadimplenteDTO filtro) {
		
		if(filtro.getColunaOrdenacao() == null || filtro.getPaginacao() == null) {
			return "";
		}
		
		String sortColumn = filtro.getColunaOrdenacao().toString();
		String sortOrder = filtro.getPaginacao().getSortOrder();
		
		
		String sql = "";
		
		if(sortColumn == null || sortOrder == null) {
			return sql;
		}
		
		sql += " ORDER BY ";
		
		if(sortColumn.equalsIgnoreCase("numCota")) {
			sql += "numCota";
		} else if(sortColumn.equalsIgnoreCase("nome")) {
			sql += "nome";
		} else if(sortColumn.equalsIgnoreCase("status")) {
			sql += "status";
		} else if(sortColumn.equalsIgnoreCase("consignado")) {
			sql += "consignado";
		} else if(sortColumn.equalsIgnoreCase("dataVencimento")) {
			sql += "dataVencimento";
		} else if(sortColumn.equalsIgnoreCase("dataPagamento")) {
			sql += "dataPagamento";
		}  else if(sortColumn.equalsIgnoreCase("situacao")) {
			sql += "situacao";
		} else if(sortColumn.equalsIgnoreCase("dividaAcumulada")) {
			sql += "dividaAcumulada";
		} else if(sortColumn.equalsIgnoreCase("diasAtraso")) {
			sql += "diasAtraso";
		} else {
			return "";
		}
		
		sql += sortOrder.equalsIgnoreCase("asc") ?  " ASC " : " DESC ";		
		
		return sql;
	}
	
	public Long obterTotalInadimplenciasCota(FiltroCotaInadimplenteDTO filtro) {
		
		StringBuilder sql =
			new StringBuilder(
				this.getSqlCountInadimplenciaClausulaSelect() + this.getSqlInadimplenciaClausulaFrom());	
		
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		tratarFiltro(sql,params,filtro);
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		for(String key : params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		return ((BigInteger) query.uniqueResult()).longValue();
	}
	
	public Long obterTotalCotasInadimplencias(FiltroCotaInadimplenteDTO filtro) {
		
		StringBuilder sql =
			new StringBuilder(
				this.getSqlCountCotasInadimplentesClausulaSelect() + this.getSqlInadimplenciaClausulaFrom());	
		
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		tratarFiltro(sql,params,filtro);
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		for(String key : params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		return ((BigInteger) query.uniqueResult()).longValue();
	}

	@Override
	public Double obterSomaDividas(FiltroCotaInadimplenteDTO filtro) {
		
		StringBuilder sql =
			new StringBuilder(
				this.getSqlSumValorInadimplenciaClausulaSelect() + this.getSqlInadimplenciaClausulaFrom());	
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		tratarFiltro(sql,params,filtro);
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		for(String key : params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		if (query.uniqueResult() == null)
			return 0.0;
		
		return ((BigDecimal) query.uniqueResult()).doubleValue();
	}

	@Override
	public Divida obterDividaPorIdConsolidado(Long idConsolidado) {
		
		StringBuilder hql = new StringBuilder("select d ");
		hql.append(" from Divida d ")
		   .append(" join d.consolidado cons ")
		   .append(" join d.cobranca cob ")
		   .append(" where cons.id = :idConsolidado ")
		   .append(" and cob.id not in ( ")
		   .append("     select c.id ")
		   .append("     from Negociacao neg")
		   .append("     join neg.cobrancasOriginarias c) ")
		   .append(" and d.origemNegociacao = true ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idConsolidado", idConsolidado);
		
		return (Divida) query.uniqueResult();
	}

	@Override
	public BigDecimal obterTotalDividasAbertoCota(Long idCota) {
        
		String hql  = "select sum(COALESCE(divida.valor,0)) from Divida divida where divida.cota.id = :idCota and status = :status ";
		Query query  = super.getSession().createQuery(hql);
		query.setParameter("idCota", idCota);
		query.setParameter("status", StatusDivida.EM_ABERTO);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterValorDividasDataOperacao(boolean dividaVencendo, boolean dividaAcumulada){
		
		StringBuilder hql = new StringBuilder("select sum(divida.valor) ");
		hql.append(" from Divida divida, Distribuidor dist ")
		   .append(" where ");
		
		if (dividaVencendo){
			
			hql.append(" divida.cobranca.dataVencimento = dist.dataOperacao ");
		} else {
			
			hql.append(" divida.cobranca.dataVencimento < dist.dataOperacao ");
		}
		
		if (dividaAcumulada){
			
			hql.append(" and divida.acumulada = :acumulada ");
		}
		
		hql.append(" and divida.status != :quitada ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (dividaAcumulada){
			
			query.setParameter("acumulada", true);
		}
		
		query.setParameter("quitada", StatusDivida.QUITADA);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterValoresDividasGeradasDataOperacao(boolean postergada){
		
		StringBuilder hql = new StringBuilder("select sum(divida.valor) ");
		hql.append(" from Divida divida, Distribuidor dist ")
		   .append(" where divida.data = dist.dataOperacao ");
		
		if (postergada){
			
			hql.append(" and divida.status = :indPostergada ");
		} else {
			
			hql.append(" and divida.status != :indPostergada ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("indPostergada", StatusDivida.POSTERGADA);
		
		return (BigDecimal) query.uniqueResult();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacaoDividasReceberEm(Date data) {
	    Objects.requireNonNull(data, "Data para sumarização das dívidas a receber EM não pode ser nula!");
	    return sumarizarDividas(data, TipoDivida.DIVIDA_A_RECEBER);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacaoDividasVencerApos(Date data) {
	    Objects.requireNonNull(data, "Data para sumarização das dívidas à vencer APÓS não pode ser nula!");
        return sumarizarDividas(data, TipoDivida.DIVIDA_A_VENCER);
    }

    /**
     * Sumariza as dívidas de acordo com o tipo de dívida, para dívidas à
     * receber são consideradas as dívidas com vencimento na data base, no
     * caso de dívidas à vencer são consideradas as dívidas com vencimento após
     * a data base
     * 
     * @param data
     *            data base para sumarização das dívidas
     * @param tipoDivida
     *            tipo da dívida para sumarização
     * @return Mapa com as dívidas sumarizadas pelo tipo de cobrança
     */
	@SuppressWarnings("unchecked")
	private Map<TipoCobranca, SumarizacaoDividasDTO> sumarizarDividas(Date data, TipoDivida tipoDivida) {
	    boolean isDividaReceber = TipoDivida.DIVIDA_A_RECEBER.equals(tipoDivida);
	    
	    Map<String, Object> parametros = new HashMap<String, Object>();
	    parametros.put("data", data);
	    parametros.put("statusCobrancaPago", StatusCobranca.PAGO);
	    
	    StringBuilder hql = new StringBuilder("select new map(cobranca.tipoCobranca as tipoCobranca, ");
	    hql.append("sum(divida.valor) as total, ");
	    hql.append("sum(case when cobranca.statusCobranca = :statusCobrancaPago then divida.valor else 0 end) as pago, ");
	    if (isDividaReceber) {
	        hql.append("sum(case when cobranca.statusCobranca = :statusCobrancaNaoPago then divida.valor else 0 end) as inadimplencia) ");
	        parametros.put("statusCobrancaNaoPago", StatusCobranca.NAO_PAGO);
	    } else {
	        //Dívidas à vencer, sem possibilidade de verificação de inadimplência 
	        hql.append("cast(0 as big_decimal) as inadimplencia) ");
	    }
	    hql.append("from Cobranca cobranca ");
	    hql.append("where cobranca.dataVencimento ");
        hql.append(isDividaReceber ? " = " : " > ");
	    hql.append(" :data ");
	    hql.append("group by cobranca.tipoCobranca");
	    
	    Query query = getSession().createQuery(hql.toString());
	    for (Entry<String, Object> entry : parametros.entrySet()) {
	        query.setParameter(entry.getKey(), entry.getValue());
	    }
	    
	    List<Map<String, Object>> maps = query.list();
	    Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacoes = new EnumMap<TipoCobranca, SumarizacaoDividasDTO>(TipoCobranca.class);
	    
	    for (Map<String, Object> map : maps) {
	        
	        TipoCobranca tipoCobranca = (TipoCobranca) map.get("tipoCobranca");
	        BigDecimal total = (BigDecimal) map.get("total");
	        BigDecimal pago = (BigDecimal) map.get("pago");
	        BigDecimal inadimplencia = (BigDecimal) map.get("inadimplencia");
	        
	        SumarizacaoDividasDTO sumarizacao = new SumarizacaoDividasDTO(data, tipoDivida, tipoCobranca, total, pago, inadimplencia);
	        sumarizacoes.put(tipoCobranca, sumarizacao);
	    }
	    
	    return sumarizacoes;
	}
    
	/**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
    @Override
    public List<Divida> obterDividasReceberEm(Date data, PaginacaoVO paginacao) {
	    Objects.requireNonNull(data, "Data para consulta das dívidas à receber EM não pode ser nula!");
	    Query query = queryDividas(data, TipoDivida.DIVIDA_A_RECEBER, paginacao, false);
	    return query.list();
    }


    /**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
    @Override
    public List<Divida> obterDividasVencerApos(Date data, PaginacaoVO paginacao) {
	    Objects.requireNonNull(data, "Data para consulta das dívidas à vencer APÓS não pode ser nula!");
	    Query query = queryDividas(data, TipoDivida.DIVIDA_A_VENCER, paginacao, false);
        return query.list();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public long contarDividasReceberEm(Date data) {
	    Objects.requireNonNull(data, "Data para contagem das dívidas à receber EM não pode ser nula!");
	    Query query = queryDividas(data, TipoDivida.DIVIDA_A_RECEBER, null, true);
        return (long) query.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public long contarDividasVencerApos(Date data) {
	    Objects.requireNonNull(data, "Data para contagem das dívidas à vencer APÓS não pode ser nula!");
	    Query query = queryDividas(data, TipoDivida.DIVIDA_A_VENCER, null, true);
        return (long) query.uniqueResult();
    }

	/**
	 * Cria a query para consulta / contagem de dívidas à receber / à vencer
	 * 
	 * @param data
	 *            data base para consulta ou contagem de dívidas
	 * @param tipoDivida  tipo da dívida para recuperação, dívidas a receber ou dívidas a 
	 * vencer
	 * @param paginacao
	 *            no caso de consulta de dívidas, contém os parâmetros de
	 *            paginação de dívidas, pode ser {@code null}
	 * @param count
	 *            flag indicando se a query é consulta das dívidas ou contagem
	 *            de dívidas
	 * @return query criada com os parâmetros recebidos
	 */
	private Query queryDividas(Date data, TipoDivida tipoDivida, PaginacaoVO paginacao, boolean count) {
	    boolean isDividaReceber = TipoDivida.DIVIDA_A_RECEBER.equals(tipoDivida);
	    
	    StringBuilder hql = new StringBuilder("select ");
	    hql.append(count ? "count(divida) " : "divida ");
	    hql.append("from Divida divida ");
	    hql.append("left join divida.cobranca cobranca ");
	    hql.append("where cobranca.dataVencimento ");
	    hql.append(isDividaReceber ? " = " : " > ");
	    hql.append(" :data ");
	    if (!count) {
	        hql.append("order by divida.cota.numeroCota ");
	    }
	    Query query = getSession().createQuery(hql.toString());
	    query.setParameter("data", data);
	    
	    if (!count && paginacao != null) {
	        query.setFirstResult(paginacao.getPosicaoInicial());
	        query.setMaxResults(paginacao.getQtdResultadosPorPagina());
	    }
	    return query;
	}
	
	private String getSqlInadimplenciaClausulaFrom() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" FROM DIVIDA AS DIVIDA_ ");
		sql.append(" JOIN COTA AS COTA_ ON(COTA_.ID=DIVIDA_.COTA_ID) ");
		sql.append(" JOIN PESSOA AS PESSOA_ ON (PESSOA_.ID=COTA_.PESSOA_ID) ");
		sql.append(" JOIN COBRANCA AS COBRANCA_ ON(COBRANCA_.DIVIDA_ID=DIVIDA_.ID) ");
		sql.append(" LEFT JOIN NEGOCIACAO_COBRANCA_ORIGINARIA AS NEGOCIACAO_COBRANCA_ORIGINARIA_ ");
		sql.append(" ON (NEGOCIACAO_COBRANCA_ORIGINARIA_.COBRANCA_ID = COBRANCA_.ID) ");
		sql.append(" LEFT JOIN NEGOCIACAO AS NEGOCIACAO_ ");
		sql.append(" ON (NEGOCIACAO_COBRANCA_ORIGINARIA_.NEGOCIACAO_ID=NEGOCIACAO_.ID) ");
		sql.append(" JOIN POLITICA_COBRANCA POLITICA_COBRANCA_ ON (POLITICA_COBRANCA_.PRINCIPAL IS TRUE) ");
		
		return sql.toString();
	}
	
	private String getSqlInadimplenciaClausulaSelect() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		sql.append(" CASE WHEN DIVIDA_.ACUMULADA=0 ");
		sql.append(" THEN DIVIDA_.VALOR ");
		sql.append(" ELSE (SELECT SUM(ACUMULADAS_.VALOR) FROM DIVIDA ACUMULADAS_ ");
		sql.append(" where ACUMULADAS_.DIVIDA_RAIZ_ID = divida_.DIVIDA_RAIZ_ID ");
		sql.append(" or ACUMULADAS_.id = DIVIDA_.DIVIDA_RAIZ_ID) ");
		sql.append(" END as dividaAcumulada, ");
		sql.append(" CASE WHEN DATEDIFF(CURRENT_DATE, COBRANCA_.DT_VENCIMENTO)<0 THEN 0 ");
		sql.append(" ELSE DATEDIFF(CURRENT_DATE, COBRANCA_.DT_VENCIMENTO) ");
		sql.append(" END as diasAtraso, ");
		sql.append(" DIVIDA_.ID as idDivida, ");
		sql.append(" COTA_.ID AS idCota, ");
		sql.append(" COBRANCA_.ID AS idCobranca, ");
		sql.append(" NEGOCIACAO_.ID as idNegociacao, ");
		sql.append(" NEGOCIACAO_.COMISSAO_PARA_SALDO_DIVIDA as comissaoSaldoDivida, ");
		sql.append(" COTA_.NUMERO_COTA AS numCota, ");
		sql.append(" COTA_.SITUACAO_CADASTRO as status, ");
		sql.append(" CASE WHEN PESSOA_.NOME IS NOT NULL ");
		sql.append(" THEN PESSOA_.NOME ");
		sql.append(" ELSE PESSOA_.RAZAO_SOCIAL END AS nome, ");
		sql.append(" (SELECT SUM(ESTOQUE.QTDE_RECEBIDA * PRODEDICAO.PRECO_CUSTO) AS TOTALPRODUTO ");
		sql.append(" FROM ESTOQUE_PRODUTO_COTA ESTOQUE ");
		sql.append(" JOIN PRODUTO_EDICAO PRODEDICAO ON (ESTOQUE.PRODUTO_EDICAO_ID=PRODEDICAO.ID) ");
		sql.append(" WHERE ESTOQUE.COTA_ID=COTA_.ID) AS consignado, ");
		sql.append(" COBRANCA_.DT_VENCIMENTO as dataVencimento, ");
		sql.append(" COBRANCA_.DT_PAGAMENTO as dataPagamento, ");
		sql.append(" DIVIDA_.STATUS as situacao ");
		
		return sql.toString();
	}
	
	private String getSqlCountInadimplenciaClausulaSelect() {
		
		return " SELECT COUNT(*) ";
	}
	
	private String getSqlCountCotasInadimplentesClausulaSelect() {
		
		return " SELECT COUNT(DISTINCT COTA_.ID) ";
	}
	
	private String getSqlSumValorInadimplenciaClausulaSelect() {
		
		return " SELECT SUM(DIVIDA_.VALOR) ";
	}
	
}
=======
package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO.ColunaOrdenacao;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.vo.PaginacaoVO;


@Repository
public class DividaRepositoryImpl extends AbstractRepositoryModel<Divida, Long> implements
		DividaRepository {
	
	public DividaRepositoryImpl() {
		super(Divida.class);
	}
	
	public Divida obterDividaParaAcumuloPorCota(Long idCota, Date diaDivida){
		Criteria criteria = this.getSession().createCriteria(Divida.class);
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("data", diaDivida));
		
		return (Divida) criteria.uniqueResult();
	}
	
	@Override
	public Long obterQunatidadeDividaGeradas(Date dataMovimento){
		
		String hql  = "select count (divida.id) from Divida divida where divida.data =:data";
		
		Query query  = super.getSession().createQuery(hql);
		query.setParameter("data", dataMovimento);
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeRegistroDividasGeradas(FiltroDividaGeradaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getSqldividas(true,filtro,true));
		
		Query query = super.getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosConsultaDividas(filtro,true);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		return  (Long) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GeraDividaDTO> obterDividasGeradasSemBoleto(FiltroDividaGeradaDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getSqldividas(false,filtro,false));
		
		hql.append(getOrdenacaoDivida(filtro));
		
		Query query = super.getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosConsultaDividas(filtro,false);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
	
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GeraDividaDTO> obterDividasGeradas(FiltroDividaGeradaDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getSqldividas(false,filtro, true));
		
		hql.append(getOrdenacaoDivida(filtro));
		
		Query query = super.getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosConsultaDividas(filtro,true);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		return query.list();
	}
	
	/**
	 * Retorna os parametros da consulta de dividas.
	 * @param filtro
	 * @return HashMap<String,Object>
	 */
	private HashMap<String,Object> getParametrosConsultaDividas(FiltroDividaGeradaDTO filtro,boolean isBoleto){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		param.put("data",filtro.getDataMovimento());
		param.put("acumulaDivida", Boolean.FALSE);
		param.put("statusCobranca",StatusCobranca.NAO_PAGO);
		
		if(!isBoleto){
			param.put("tipoCobrancaBoleto",TipoCobranca.BOLETO);
		}
		
		if(filtro.getNumeroCota()!= null ){
			 param.put("numeroCota",filtro.getNumeroCota());
		}
		
		if(filtro.getTipoCobranca()!= null){
			param.put("tipoCobranca",filtro.getTipoCobranca());
		}
		
		if(filtro.getIdBox()!= null ){
			param.put("box",filtro.getIdBox());
		}
		
		if(filtro.getIdRota()!= null){
			param.put("rota",filtro.getIdRota());
		}
		
		if(filtro.getIdRoteiro()!= null){
			param.put("roteiro",filtro.getIdRoteiro());
		}
		
		return param;
	}
	
	/**
	 * Retorna o hql da consulta de dividas.
	 * @param count
	 * @param filtro 
	 * @return String
	 */
	private String getSqldividas(boolean count,FiltroDividaGeradaDTO filtro, boolean isBoleto){
		
		StringBuilder hql = new StringBuilder();
		
		if(count){
			hql.append(" SELECT count(distinct divida.id )");
		}else{
			hql.append(" SELECT distinct new ").append(GeraDividaDTO.class.getCanonicalName())
			.append("(")
				.append(" box.codigo || '-'|| box.nome,")
				.append(" rota.descricaoRota,")
				.append(" roteiro.descricaoRoteiro,")
				.append(" cota.numeroCota,")
				.append(" coalesce(pessoa.nome, pessoa.razaoSocial),")
				.append(" cobranca.dataVencimento,")
				.append(" cobranca.dataEmissao,")
				.append(" cobranca.valor,")
				.append(" cobranca.tipoCobranca,")
				.append(" cobranca.vias, ")
				.append(" cobranca.nossoNumero, ")

				.append(" case when ("+
				        "           select count(f.recebeCobrancaEmail) " +
						"           from PoliticaCobranca p " +
						"           join p.formaCobranca f " +
						"           where f.recebeCobrancaEmail = true"+
						"           and p.principal=true " +//TODO
						"           and p.ativo=true " +
				        "           ) > 0 " +
						" then " + 
				
				        " (case when ("+
				        "             select count(f.parametroCobrancaCota) " +
						"             from FormaCobranca f " +
						"             join f.parametroCobrancaCota p " +
						"             join p.cota c " +
						"             where f.recebeCobrancaEmail = true " +
						"             and c.id = cota.id"+
						"             and f.principal=true " +//TODO
						"             and f.ativa=true "+
				        "             ) > 0 then true else false end)" +
						" else false end ")
						
			.append(")");
			
		}
		
		hql.append(" FROM ")
		.append(" Divida divida ")
		.append(" JOIN divida.cobranca cobranca ")
		.append(" JOIN divida.consolidado consolidado ")
		.append(" JOIN cobranca.cota cota ")
		.append(" JOIN cota.box box ")
		.append(" JOIN cota.pdvs pdv ")
		.append(" JOIN cota.pessoa pessoa ")
		.append(" JOIN cota.parametroCobranca parametroCobranca ")
		.append(" JOIN pdv.rotas rotaPdv  ")
		.append(" JOIN rotaPdv.rota rota  ")
		.append(" JOIN rota.roteiro roteiro ")
		
		.append(" WHERE ")
		
		.append(" divida.data =:data ")
		.append(" AND divida.acumulada =:acumulaDivida ")
		.append(" AND cobranca.statusCobranca=:statusCobranca ");
		
		if(filtro.getNumeroCota()!= null  ){
			hql.append(" AND cota.numeroCota =:numeroCota ");
		}	
		
		if(filtro.getTipoCobranca()!= null){
			
			hql.append(" AND cobranca.tipoCobranca =:tipoCobranca  ");
		}
		
		if(!isBoleto){
			hql.append(" AND cobranca.tipoCobranca not in (:tipoCobrancaBoleto ) ");
		}
		
		if(filtro.getIdBox()!= null ){
			hql.append(" AND box.id =:box ");
		}
		
		if(filtro.getIdRota()!= null){
			hql.append(" AND rota.id =:rota ");
		}
		
		if(filtro.getIdRoteiro()!= null ){
			hql.append(" AND roteiro.id =:roteiro ");
		}
		
		return hql.toString();
	}
	
	
	/**
	 * Retorna a string hql com a oredenação da consulta
	 * @param filtro
	 * @return String
	 */
	private String getOrdenacaoDivida(FiltroDividaGeradaDTO filtro){
		
		if( !(filtro.getListaColunaOrdenacao()!= null && !filtro.getListaColunaOrdenacao().isEmpty())){
			return "";
		}
		
		String orderByColumn = "";
		
		for( ColunaOrdenacao ordenacao : filtro.getListaColunaOrdenacao()){

			switch (ordenacao) {
				case BOX:	
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " box.codigo ";
					break;
				case DATA_EMISSAO:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cobranca.dataVencimento ";
					break;
				case DATA_VENCIMENTO:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cobranca.dataEmissao ";
					break;
				case NOME_COTA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cota.pessoa.nome ";
					break;
				case NUMERO_COTA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cota.numeroCota ";
					break;
				case ROTA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " rota.descricaoRota ";
					break;
				case ROTEIRO:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " roteiro.descricaoRoteiro ";
					break;
				case TIPO_COBRANCA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cobranca.tipoCobranca ";
					break;
				case VALOR:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cobranca.valor ";
					break;
				case VIA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " cobranca.vias ";
					break;
			}
		}
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" ORDER BY ").append(orderByColumn);
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return  hql.toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<StatusDividaDTO> obterInadimplenciasCota(FiltroCotaInadimplenteDTO filtro) {
	
		StringBuilder sql =
			new StringBuilder(
				this.getSqlInadimplenciaClausulaSelect() + this.getSqlInadimplenciaClausulaFrom());
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		tratarFiltro(sql,params,filtro);
		
		sql.append(obterOrderByInadimplenciasCota(filtro));
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			sql.append(" LIMIT :inicio,:qtdeResult");
			params.put("inicio", filtro.getPaginacao().getPosicaoInicial());
			params.put("qtdeResult", filtro.getPaginacao().getQtdResultadosPorPagina());
		}
		
		Query query = getSession().createSQLQuery(sql.toString())
				.addScalar("idDivida", StandardBasicTypes.LONG)
				.addScalar("idCota", StandardBasicTypes.LONG)
				.addScalar("idCobranca", StandardBasicTypes.LONG)
				.addScalar("idNegociacao", StandardBasicTypes.LONG)
				.addScalar("comissaoSaldoDivida")
				.addScalar("numCota")
				.addScalar("nome")
				.addScalar("status")
				.addScalar("consignado")
				.addScalar("dataVencimento")
				.addScalar("dataPagamento")
				.addScalar("situacao")
				.addScalar("dividaAcumulada")
				.addScalar("diasAtraso", StandardBasicTypes.LONG);
		
		for(String key : params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		query.setResultTransformer(Transformers.aliasToBean(StatusDividaDTO.class));
				
		return query.list();
	}	
		
	private void tratarFiltro(StringBuilder sql, HashMap<String, Object> params, FiltroCotaInadimplenteDTO filtro) {
		
		boolean whereUtilizado = false;
		
		if(filtro.getPeriodoDe() != null) {
			
			sql.append(whereUtilizado ? " AND " : " WHERE ");
			whereUtilizado = true;
			
			sql.append(" COBRANCA_.DT_VENCIMENTO >= :periodoDe ");
			params.put("periodoDe", filtro.getPeriodoDe());
		}
		
		if(filtro.getPeriodoAte() != null) {
			
			sql.append(whereUtilizado ? " AND " : " WHERE ");
			whereUtilizado = true;
			
			sql.append(" COBRANCA_.DT_VENCIMENTO <= :periodoAte ");
			params.put("periodoAte",filtro.getPeriodoAte());
		}
		
		if(filtro.getNumCota() != null) {
			
			sql.append(whereUtilizado ? " AND " : " WHERE ");
			whereUtilizado = true;
			
			sql.append(" COTA_.NUMERO_COTA = :numCota ");
			params.put("numCota",filtro.getNumCota());
		}
		
		if(filtro.getNomeCota() != null) {
			
			sql.append(whereUtilizado ? " AND " : " WHERE ");
			whereUtilizado = true;
			
			sql.append("  (upper(PESSOA_.NOME) like :nomeCota or upper(PESSOA_.RAZAO_SOCIAL) like :nomeCota)");
			params.put("nomeCota", "%" + filtro.getNomeCota().toUpperCase() + "%");
		}
		
		if(filtro.getStatusCota() != null) {
			
			sql.append(whereUtilizado ? " AND " : " WHERE ");
			whereUtilizado = true;
			
			sql.append(" COTA_.SITUACAO_CADASTRO = :statusCota ");
			params.put("statusCota",filtro.getStatusCota());
		}
		
		if (filtro.getSituacaoEmAberto() || filtro.getSituacaoNegociada() || filtro.getSituacaoPaga()) {
		
			sql.append(" AND ( ");
			
			boolean utilizarOr = false;
			
			if (filtro.getSituacaoEmAberto()) {
				
				sql.append(" (COBRANCA_.DT_VENCIMENTO <= :dataAtual ");
				sql.append(" AND DIVIDA_.STATUS = :statusDividaAberto) ");
				
				utilizarOr = true;
				
				params.put("dataAtual", new Date());
				params.put("statusDividaAberto", StatusDivida.EM_ABERTO.name());
			}
			
			if (filtro.getSituacaoNegociada()) {
				
				sql.append(utilizarOr ? " OR " : "");
				
				sql.append(" (COBRANCA_.DT_PAGAMENTO > COBRANCA_.DT_VENCIMENTO ");
				sql.append(" AND DIVIDA_.STATUS = :statusDividaNegogiada) ");
				
				utilizarOr = true;
				
				params.put("statusDividaNegogiada", StatusDivida.NEGOCIADA.name());
			}
			
			if (filtro.getSituacaoPaga()) {
				
				sql.append(utilizarOr ? " OR " : "");
				
				sql.append(" (COBRANCA_.DT_PAGAMENTO > COBRANCA_.DT_VENCIMENTO ");
				sql.append(" AND DIVIDA_.STATUS = :statusDividaQuitada) ");
				
				params.put("statusDividaQuitada", StatusDivida.QUITADA.name());
			}
			
			sql.append(" ) ");
		}
	}

	private String obterOrderByInadimplenciasCota(FiltroCotaInadimplenteDTO filtro) {
		
		if(filtro.getColunaOrdenacao() == null || filtro.getPaginacao() == null) {
			return "";
		}
		
		String sortColumn = filtro.getColunaOrdenacao().toString();
		String sortOrder = filtro.getPaginacao().getSortOrder();
		
		
		String sql = "";
		
		if(sortColumn == null || sortOrder == null) {
			return sql;
		}
		
		sql += " ORDER BY ";
		
		if(sortColumn.equalsIgnoreCase("numCota")) {
			sql += "numCota";
		} else if(sortColumn.equalsIgnoreCase("nome")) {
			sql += "nome";
		} else if(sortColumn.equalsIgnoreCase("status")) {
			sql += "status";
		} else if(sortColumn.equalsIgnoreCase("consignado")) {
			sql += "consignado";
		} else if(sortColumn.equalsIgnoreCase("dataVencimento")) {
			sql += "dataVencimento";
		} else if(sortColumn.equalsIgnoreCase("dataPagamento")) {
			sql += "dataPagamento";
		}  else if(sortColumn.equalsIgnoreCase("situacao")) {
			sql += "situacao";
		} else if(sortColumn.equalsIgnoreCase("dividaAcumulada")) {
			sql += "dividaAcumulada";
		} else if(sortColumn.equalsIgnoreCase("diasAtraso")) {
			sql += "diasAtraso";
		} else {
			return "";
		}
		
		sql += sortOrder.equalsIgnoreCase("asc") ?  " ASC " : " DESC ";		
		
		return sql;
	}
	
	public Long obterTotalInadimplenciasCota(FiltroCotaInadimplenteDTO filtro) {
		
		StringBuilder sql =
			new StringBuilder(
				this.getSqlCountInadimplenciaClausulaSelect() + this.getSqlInadimplenciaClausulaFrom());	
		
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		tratarFiltro(sql,params,filtro);
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		for(String key : params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		return ((BigInteger) query.uniqueResult()).longValue();
	}
	
	public Long obterTotalCotasInadimplencias(FiltroCotaInadimplenteDTO filtro) {
		
		StringBuilder sql =
			new StringBuilder(
				this.getSqlCountCotasInadimplentesClausulaSelect() + this.getSqlInadimplenciaClausulaFrom());	
		
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		tratarFiltro(sql,params,filtro);
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		for(String key : params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		return ((BigInteger) query.uniqueResult()).longValue();
	}

	@Override
	public Double obterSomaDividas(FiltroCotaInadimplenteDTO filtro) {
		
		StringBuilder sql =
			new StringBuilder(
				this.getSqlSumValorInadimplenciaClausulaSelect() + this.getSqlInadimplenciaClausulaFrom());	
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		tratarFiltro(sql,params,filtro);
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		for(String key : params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		if (query.uniqueResult() == null)
			return 0.0;
		
		return ((BigDecimal) query.uniqueResult()).doubleValue();
	}

	@Override
	public Divida obterDividaPorIdConsolidado(Long idConsolidado) {
		
		StringBuilder hql = new StringBuilder("select d ");
		hql.append(" from Divida d ")
		   .append(" join d.consolidado cons ")
		   .append(" join d.cobranca cob ")
		   .append(" where cons.id = :idConsolidado ")
		   .append(" and cob.id not in ( ")
		   .append("     select c.id ")
		   .append("     from Negociacao neg")
		   .append("     join neg.cobrancasOriginarias c) ")
		   .append(" and d.origemNegociacao = true ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idConsolidado", idConsolidado);
		
		return (Divida) query.uniqueResult();
	}

	@Override
	public BigDecimal obterTotalDividasAbertoCota(Long idCota) {
        
		String hql  = "select sum(COALESCE(divida.valor,0)) from Divida divida where divida.cota.id = :idCota and status = :status ";
		Query query  = super.getSession().createQuery(hql);
		query.setParameter("idCota", idCota);
		query.setParameter("status", StatusDivida.EM_ABERTO);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterValorDividasDataOperacao(boolean dividaVencendo, boolean dividaAcumulada){
		
		StringBuilder hql = new StringBuilder("select sum(divida.valor) ");
		hql.append(" from Divida divida, Distribuidor dist ")
		   .append(" where ");
		
		if (dividaVencendo){
			
			hql.append(" divida.cobranca.dataVencimento = dist.dataOperacao ");
		} else {
			
			hql.append(" divida.cobranca.dataVencimento < dist.dataOperacao ");
		}
		
		if (dividaAcumulada){
			
			hql.append(" and divida.acumulada = :acumulada ");
		}
		
		hql.append(" and divida.status != :quitada ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (dividaAcumulada){
			
			query.setParameter("acumulada", true);
		}
		
		query.setParameter("quitada", StatusDivida.QUITADA);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterValoresDividasGeradasDataOperacao(boolean postergada){
		
		StringBuilder hql = new StringBuilder("select sum(divida.valor) ");
		hql.append(" from Divida divida, Distribuidor dist ")
		   .append(" where divida.data = dist.dataOperacao ");
		
		if (postergada){
			
			hql.append(" and divida.status = :indPostergada ");
		} else {
			
			hql.append(" and divida.status != :indPostergada ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("indPostergada", StatusDivida.POSTERGADA);
		
		return (BigDecimal) query.uniqueResult();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacaoDividasReceberEm(Date data) {
	    Objects.requireNonNull(data, "Data para sumarização das dívidas a receber EM não pode ser nula!");
	    return sumarizarDividas(data, TipoDivida.DIVIDA_A_RECEBER);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacaoDividasVencerApos(Date data) {
	    Objects.requireNonNull(data, "Data para sumarização das dívidas à vencer APÓS não pode ser nula!");
        return sumarizarDividas(data, TipoDivida.DIVIDA_A_VENCER);
    }

    /**
     * Sumariza as dívidas de acordo com o tipo de dívida, para dívidas à
     * receber são consideradas as dívidas com vencimento na data base, no
     * caso de dívidas à vencer são consideradas as dívidas com vencimento após
     * a data base
     * 
     * @param data
     *            data base para sumarização das dívidas
     * @param tipoDivida
     *            tipo da dívida para sumarização
     * @return Mapa com as dívidas sumarizadas pelo tipo de cobrança
     */
	@SuppressWarnings("unchecked")
	private Map<TipoCobranca, SumarizacaoDividasDTO> sumarizarDividas(Date data, TipoDivida tipoDivida) {
	    boolean isDividaReceber = TipoDivida.DIVIDA_A_RECEBER.equals(tipoDivida);
	    
	    Map<String, Object> parametros = new HashMap<String, Object>();
	    parametros.put("data", data);
	    parametros.put("statusCobrancaPago", StatusCobranca.PAGO);
	    
	    StringBuilder hql = new StringBuilder("select new map(cobranca.tipoCobranca as tipoCobranca, ");
	    hql.append("sum(divida.valor) as total, ");
	    hql.append("sum(case when cobranca.statusCobranca = :statusCobrancaPago then divida.valor else 0 end) as pago, ");
	    if (isDividaReceber) {
	        hql.append("sum(case when cobranca.statusCobranca = :statusCobrancaNaoPago then divida.valor else 0 end) as inadimplencia) ");
	        parametros.put("statusCobrancaNaoPago", StatusCobranca.NAO_PAGO);
	    } else {
	        //Dívidas à vencer, sem possibilidade de verificação de inadimplência 
	        hql.append("cast(0 as big_decimal) as inadimplencia) ");
	    }
	    hql.append("from Cobranca cobranca ");
	    hql.append("where cobranca.dataVencimento ");
        hql.append(isDividaReceber ? " = " : " > ");
	    hql.append(" :data ");
	    hql.append("group by cobranca.tipoCobranca");
	    
	    Query query = getSession().createQuery(hql.toString());
	    for (Entry<String, Object> entry : parametros.entrySet()) {
	        query.setParameter(entry.getKey(), entry.getValue());
	    }
	    
	    List<Map<String, Object>> maps = query.list();
	    Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacoes = new EnumMap<TipoCobranca, SumarizacaoDividasDTO>(TipoCobranca.class);
	    
	    for (Map<String, Object> map : maps) {
	        
	        TipoCobranca tipoCobranca = (TipoCobranca) map.get("tipoCobranca");
	        BigDecimal total = (BigDecimal) map.get("total");
	        BigDecimal pago = (BigDecimal) map.get("pago");
	        BigDecimal inadimplencia = (BigDecimal) map.get("inadimplencia");
	        
	        SumarizacaoDividasDTO sumarizacao = new SumarizacaoDividasDTO(data, tipoDivida, tipoCobranca, total, pago, inadimplencia);
	        sumarizacoes.put(tipoCobranca, sumarizacao);
	    }
	    
	    return sumarizacoes;
	}
    
	/**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
    @Override
    public List<Divida> obterDividasReceberEm(Date data, PaginacaoVO paginacao) {
	    Objects.requireNonNull(data, "Data para consulta das dívidas à receber EM não pode ser nula!");
	    Query query = queryDividas(data, TipoDivida.DIVIDA_A_RECEBER, paginacao, false);
	    return query.list();
    }


    /**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
    @Override
    public List<Divida> obterDividasVencerApos(Date data, PaginacaoVO paginacao) {
	    Objects.requireNonNull(data, "Data para consulta das dívidas à vencer APÓS não pode ser nula!");
	    Query query = queryDividas(data, TipoDivida.DIVIDA_A_VENCER, paginacao, false);
        return query.list();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public long contarDividasReceberEm(Date data) {
	    Objects.requireNonNull(data, "Data para contagem das dívidas à receber EM não pode ser nula!");
	    Query query = queryDividas(data, TipoDivida.DIVIDA_A_RECEBER, null, true);
        return (long) query.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public long contarDividasVencerApos(Date data) {
	    Objects.requireNonNull(data, "Data para contagem das dívidas à vencer APÓS não pode ser nula!");
	    Query query = queryDividas(data, TipoDivida.DIVIDA_A_VENCER, null, true);
        return (long) query.uniqueResult();
    }

	/**
	 * Cria a query para consulta / contagem de dívidas à receber / à vencer
	 * 
	 * @param data
	 *            data base para consulta ou contagem de dívidas
	 * @param tipoDivida  tipo da dívida para recuperação, dívidas a receber ou dívidas a 
	 * vencer
	 * @param paginacao
	 *            no caso de consulta de dívidas, contém os parâmetros de
	 *            paginação de dívidas, pode ser {@code null}
	 * @param count
	 *            flag indicando se a query é consulta das dívidas ou contagem
	 *            de dívidas
	 * @return query criada com os parâmetros recebidos
	 */
	private Query queryDividas(Date data, TipoDivida tipoDivida, PaginacaoVO paginacao, boolean count) {
	    boolean isDividaReceber = TipoDivida.DIVIDA_A_RECEBER.equals(tipoDivida);
	    
	    StringBuilder hql = new StringBuilder("select ");
	    hql.append(count ? "count(divida) " : "divida ");
	    hql.append("from Divida divida ");
	    hql.append("left join divida.cobranca cobranca ");
	    hql.append("where cobranca.dataVencimento ");
	    hql.append(isDividaReceber ? " = " : " > ");
	    hql.append(" :data ");
	    if (!count) {
	        hql.append("order by divida.cota.numeroCota ");
	    }
	    Query query = getSession().createQuery(hql.toString());
	    query.setParameter("data", data);
	    
	    if (!count && paginacao != null) {
	        query.setFirstResult(paginacao.getPosicaoInicial());
	        query.setMaxResults(paginacao.getQtdResultadosPorPagina());
	    }
	    return query;
	}
	
	private String getSqlInadimplenciaClausulaFrom() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" FROM DIVIDA AS DIVIDA_ ");
		sql.append(" JOIN COTA AS COTA_ ON(COTA_.ID=DIVIDA_.COTA_ID) ");
		sql.append(" JOIN PESSOA AS PESSOA_ ON (PESSOA_.ID=COTA_.PESSOA_ID) ");
		sql.append(" JOIN COBRANCA AS COBRANCA_ ON(COBRANCA_.DIVIDA_ID=DIVIDA_.ID) ");
		sql.append(" LEFT JOIN NEGOCIACAO_COBRANCA_ORIGINARIA AS NEGOCIACAO_COBRANCA_ORIGINARIA_ ");
		sql.append(" ON (NEGOCIACAO_COBRANCA_ORIGINARIA_.COBRANCA_ID = COBRANCA_.ID) ");
		sql.append(" LEFT JOIN NEGOCIACAO AS NEGOCIACAO_ ");
		sql.append(" ON (NEGOCIACAO_COBRANCA_ORIGINARIA_.NEGOCIACAO_ID=NEGOCIACAO_.ID) ");
		sql.append(" JOIN POLITICA_COBRANCA POLITICA_COBRANCA_ ON (POLITICA_COBRANCA_.PRINCIPAL IS TRUE) ");
		
		return sql.toString();
	}
	
	private String getSqlInadimplenciaClausulaSelect() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		sql.append(" CASE WHEN DIVIDA_.ACUMULADA=0 ");
		sql.append(" THEN DIVIDA_.VALOR ");
		sql.append(" ELSE (SELECT SUM(ACUMULADAS_.VALOR) FROM DIVIDA ACUMULADAS_ ");
		sql.append(" where ACUMULADAS_.DIVIDA_RAIZ_ID = divida_.DIVIDA_RAIZ_ID ");
		sql.append(" or ACUMULADAS_.id = DIVIDA_.DIVIDA_RAIZ_ID) ");
		sql.append(" END as dividaAcumulada, ");
		sql.append(" CASE WHEN DATEDIFF(CURRENT_DATE, COBRANCA_.DT_VENCIMENTO)<0 THEN 0 ");
		sql.append(" ELSE DATEDIFF(CURRENT_DATE, COBRANCA_.DT_VENCIMENTO) ");
		sql.append(" END as diasAtraso, ");
		sql.append(" DIVIDA_.ID as idDivida, ");
		sql.append(" COTA_.ID AS idCota, ");
		sql.append(" COBRANCA_.ID AS idCobranca, ");
		sql.append(" NEGOCIACAO_.ID as idNegociacao, ");
		sql.append(" NEGOCIACAO_.COMISSAO_PARA_SALDO_DIVIDA as comissaoSaldoDivida, ");
		sql.append(" COTA_.NUMERO_COTA AS numCota, ");
		sql.append(" COTA_.SITUACAO_CADASTRO as status, ");
		sql.append(" CASE WHEN PESSOA_.NOME IS NOT NULL ");
		sql.append(" THEN PESSOA_.NOME ");
		sql.append(" ELSE PESSOA_.RAZAO_SOCIAL END AS nome, ");
		sql.append(" (SELECT SUM(ESTOQUE.QTDE_RECEBIDA * PRODEDICAO.PRECO_CUSTO) AS TOTALPRODUTO ");
		sql.append(" FROM ESTOQUE_PRODUTO_COTA ESTOQUE ");
		sql.append(" JOIN PRODUTO_EDICAO PRODEDICAO ON (ESTOQUE.PRODUTO_EDICAO_ID=PRODEDICAO.ID) ");
		sql.append(" WHERE ESTOQUE.COTA_ID=COTA_.ID) AS consignado, ");
		sql.append(" COBRANCA_.DT_VENCIMENTO as dataVencimento, ");
		sql.append(" COBRANCA_.DT_PAGAMENTO as dataPagamento, ");
		sql.append(" DIVIDA_.STATUS as situacao ");
		
		return sql.toString();
	}
	
	private String getSqlCountInadimplenciaClausulaSelect() {
		
		return " SELECT COUNT(*) ";
	}
	
	private String getSqlCountCotasInadimplentesClausulaSelect() {
		
		return " SELECT COUNT(DISTINCT COTA_.ID) ";
	}
	
	private String getSqlSumValorInadimplenciaClausulaSelect() {
		
		return " SELECT SUM(DIVIDA_.VALOR) ";
	}
	
}
>>>>>>> refs/remotes/DGBTi/fase2
