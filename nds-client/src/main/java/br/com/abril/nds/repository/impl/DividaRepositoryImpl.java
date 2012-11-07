package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Value;
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
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.vo.PaginacaoVO;


@Repository
public class DividaRepositoryImpl extends AbstractRepositoryModel<Divida, Long> implements
		DividaRepository {

	@Value("#{queries.inadimplenciasCota}")
	protected String queryInadimplenciasCota;
	
	@Value("#{queries.countInadimplenciasCota}")
	protected String queryCountInadimplenciasCota;
	
	@Value("#{queries.countCotaInadimplencias}")
	protected String queryCountCotaInadimplencias;
	
	@Value("#{queries.sumDividaCotas}")
	protected String querySumDividaCotas;
	
	
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
		param.put("tipoRoteiro", TipoRoteiro.NORMAL);
		
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
			hql.append(" SELECT count(divida.id )");
		}else{
			hql.append(" SELECT new ").append(GeraDividaDTO.class.getCanonicalName())
			.append("(")
				.append(" box.codigo || '-'|| box.nome,")
				.append(" rota.descricaoRota,")
				.append(" roteiro.descricaoRoteiro,")
				.append(" cota.numeroCota,")
				.append(" pessoa.nome,")
				.append(" cobranca.dataVencimento,")
				.append(" cobranca.dataEmissao,")
				.append(" cobranca.valor,")
				.append(" cobranca.tipoCobranca,")
				.append(" cobranca.vias, ")
				.append(" cobranca.nossoNumero, ")
				.append(" (select f.recebeCobrancaEmail " +
						"  from FormaCobranca f " +
						"  where f.parametroCobrancaCota=parametroCobranca " +
						"  and f.principal=true group by f.principal )")
			.append(")");
		}
		
		hql.append(" FROM ")
		.append(" Divida divida ")
		.append(" JOIN divida.cobranca cobranca ")
		.append(" JOIN cobranca.cota cota ")
		.append(" JOIN cota.pdvs pdv ")
		.append(" JOIN cota.pessoa pessoa ")
		.append(" JOIN cota.parametroCobranca parametroCobranca ")
		.append(" LEFT JOIN pdv.rotas rotaPdv  ")
		.append(" LEFT JOIN rotaPdv.rota rota  ")
		.append(" LEFT JOIN rota.roteiro roteiro ")
		.append(" LEFT JOIN roteiro.roteirizacao roteirizacao ")
		.append(" LEFT JOIN roteirizacao.box box")
		
		.append(" WHERE ")
		
		.append(" divida.data =:data ")
		.append(" AND divida.acumulada =:acumulaDivida ")
		.append(" AND cobranca.statusCobranca=:statusCobranca ")
		.append(" AND pdv.caracteristicas.pontoPrincipal = true ")
		.append(" AND (roteiro.tipoRoteiro is null OR roteiro.tipoRoteiro=:tipoRoteiro) ");
		
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
	
		StringBuilder sql = new StringBuilder(queryInadimplenciasCota);
		
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

		boolean utilizado = false;
		
		if( filtro.getSituacaoPaga() != null && filtro.getSituacaoEmAberto() != null && filtro.getSituacaoNegociada() != null
				&& (filtro.getSituacaoPaga() == true || filtro.getSituacaoEmAberto() == true || filtro.getSituacaoNegociada() == true) ) {
			
			sql.append(whereUtilizado ? " AND " : " WHERE ");
			whereUtilizado = true;			
			sql.append("( ");
			
			if( filtro.getSituacaoEmAberto() == true) {
				utilizado = true;
				
				sql.append(" DIVIDA_.STATUS = :situacaoEmAberto ");
				params.put("situacaoEmAberto",StatusDivida.EM_ABERTO.name());
			}
			if( filtro.getSituacaoNegociada() == true) {
				sql.append( utilizado ? " || " : "");
				utilizado = true;
				
				sql.append(" DIVIDA_.STATUS = :situacaoNegociada ");
				params.put("situacaoNegociada",StatusDivida.NEGOCIADA.name());
			} 
			if( filtro.getSituacaoPaga() == true ) {
				sql.append( utilizado ? " || " : "");
				utilizado = true;
				
				sql.append(" DIVIDA_.STATUS = :situacaoPaga ");
				params.put("situacaoPaga",StatusDivida.QUITADA.name());
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
		
		StringBuilder sql = new StringBuilder(queryCountInadimplenciasCota);	
		
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		tratarFiltro(sql,params,filtro);
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		for(String key : params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		return ((BigInteger) query.uniqueResult()).longValue();
	}
	
	public Long obterTotalCotasInadimplencias(FiltroCotaInadimplenteDTO filtro) {
		
		StringBuilder sql = new StringBuilder(queryCountCotaInadimplencias);	
		
		
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
		
		StringBuilder sql = new StringBuilder(querySumDividaCotas);	
		
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
		
		Criteria criteria = this.getSession().createCriteria(Divida.class);
		criteria.add(Restrictions.eq("consolidado.id", idConsolidado));
		
		return (Divida) criteria.uniqueResult();
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
	    
	    Map<String, Object> parametros = new HashMap<>();
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
	    Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacoes = new EnumMap<>(TipoCobranca.class);
	    
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
	    Query query = queryDividasReceberVencer(data, paginacao, false);
	    return query.list();
    }


    /**
     * {@inheritDoc}
     */
	@Override
    public List<Divida> obterDividasVencerApos(Date data, PaginacaoVO paginacao) {
        //TODO: implementar
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public long contarDividasReceberEm(Date data) {
	    Query query = queryDividasReceberVencer(data, null, true);
        return (long) query.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public long contarDividasVencerApos(Date data) {
	    //TODO: implementar
        return 0;
    }

	/**
	 * Cria a query para consulta / contagem de dívidas à receber / à vencer
	 * 
	 * @param data
	 *            data base para consulta ou contagem de dívidas
	 * @param paginacao
	 *            no caso de consulta de dívidas, contém os parâmetros de
	 *            paginação de dívidas, pode ser {@code null}
	 * @param count
	 *            flag indicando se a query é consulta das dívidas ou contagem
	 *            de dívidas
	 * @return query criada com os parâmetros recebidos
	 */
	private Query queryDividasReceberVencer(Date data, PaginacaoVO paginacao, boolean count) {
	    StringBuilder hql = new StringBuilder("select ");
	    hql.append(count ? "count(divida) " : "divida ");
	    hql.append("from Divida divida ");
	    hql.append("left join divida.cobranca cobranca ");
	    hql.append("where cobranca.dataVencimento = :data ");
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
	
}