package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.controllers.financeiro.InadimplenciaController;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO.ColunaOrdenacao;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.repository.DividaRepository;

@Repository
public class DividaRepositoryImpl extends AbstractRepository<Divida, Long> implements
		DividaRepository {

	@Value("#{queries.inadimplenciasCota}")
	protected String queryInadimplenciasCota;
	
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
		
		if(filtro.getCodigoBox()!= null && !filtro.getCodigoBox().isEmpty()){
			param.put("box",filtro.getCodigoBox() + "%");
		}
		
		if(filtro.getRota()!= null && !filtro.getRota().isEmpty()){
			param.put("rota",filtro.getRota() +"%");
		}
		
		if(filtro.getRoteiro()!= null && !filtro.getRoteiro().isEmpty()){
			param.put("roteiro",filtro.getRoteiro() + "%" );
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
				.append(" box.codigo,")
				.append(" rota.codigoRota,")
				.append(" roteiro.descricaoRoteiro,")
				.append(" cota.numeroCota,")
				.append(" pessoa.nome,")
				.append(" cobranca.dataVencimento,")
				.append(" cobranca.dataEmissao,")
				.append(" cobranca.valor,")
				.append(" cobranca.tipoCobranca,")
				.append(" cobranca.vias, ")
				.append(" cobranca.nossoNumero, ")
				.append(" parametroCobranca.recebeCobrancaEmail ")
			.append(")");
		}
		
		hql.append(" FROM ")
		.append(" Divida divida ")
		.append(" JOIN divida.cobranca cobranca ")
		.append(" JOIN cobranca.cota cota ")
		.append(" JOIN cota.pessoa pessoa ")
		.append(" JOIN cota.parametroCobranca parametroCobranca ")
		.append(" JOIN cota.box box")
		.append(" LEFT JOIN cota.rotaRoteiroOperacao rotaRoteiroOperacao ")
		.append(" LEFT JOIN rotaRoteiroOperacao.rota rota ")
		.append(" LEFT JOIN rotaRoteiroOperacao.roteiro roteiro ")
		
		.append(" WHERE ")
		
		.append(" divida.data =:data ")
		.append(" AND divida.acumulada =:acumulaDivida ")
		.append(" AND cobranca.statusCobranca=:statusCobranca");
		
		
		if(filtro.getNumeroCota()!= null  ){
			hql.append(" AND cota.numeroCota =:numeroCota ");
		}	
		
		if(filtro.getTipoCobranca()!= null){
			
			hql.append(" AND cobranca.tipoCobranca =:tipoCobranca  ");
		}
		
		if(!isBoleto){
			hql.append(" AND cobranca.tipoCobranca not in (:tipoCobrancaBoleto ) ");
		}
		
		if(filtro.getCodigoBox()!= null && !filtro.getCodigoBox().isEmpty()){
			hql.append(" AND upper(box.codigo) like upper(:box) ");
		}
		
		if(filtro.getRota()!= null && !filtro.getRota().isEmpty()){
			hql.append(" AND upper(rota.codigoRota) like upper(:rota)");
		}
		
		if(filtro.getRoteiro()!= null && !filtro.getRoteiro().isEmpty()){
			hql.append(" AND upper(roteiro.descricaoRoteiro) like (:roteiro)");
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
						orderByColumn += " rota.codigoRota ";
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
		
		sql.append(obterOrderByInadimplenciasCota(filtro));
		
		//TODO
		//sql.append(obterFiltrosInadimplenciasCota(filtro));
		
		if(filtro.getPaginacao().getPosicaoInicial()!= null &&  filtro.getPaginacao().getQtdResultadosPorPagina()!= null) {
			sql.append(" LIMIT :inicio,:qtdeResult");
		}
		
		Query query = getSession().createSQLQuery(sql.toString())
				.addScalar("numCota")
				.addScalar("nome")
				.addScalar("status")
				.addScalar("consignado")
				.addScalar("dataVencimento")
				.addScalar("valor")
				.addScalar("situacao")
				.addScalar("dividaAcumulada")
				.addScalar("diasAtraso");
		
		if(filtro.getPaginacao().getPosicaoInicial() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			
			query.setInteger("inicio", filtro.getPaginacao().getPosicaoInicial());
			query.setInteger("qtdeResult", filtro.getPaginacao().getQtdResultadosPorPagina() );
		}
			
		query.setResultTransformer(Transformers.aliasToBean(StatusDividaDTO.class));
				
		return query.list();
	}	
	
	/*private HashMap<String, Object> obterFiltrosInadimplenciasCota(FiltroCotaInadimplenteDTO filtro) {
		
		//TODO get correct params
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		param.put("data",filtro.getDataMovimento());
		
		if(!isBoleto){
			param.put("tipoCobrancaBoleto",TipoCobranca.BOLETO);
		}
	
		
		return param;
	}*/
	
	private String obterOrderByInadimplenciasCota(FiltroCotaInadimplenteDTO filtro) {
		
		String sortColumn = filtro.getColunaOrdenacao().name();
		String sortOrder = filtro.getPaginacao().getOrdenacao().name();
		
		
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
		} else if(sortColumn.equalsIgnoreCase("valor")) {
			sql += "valor";
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
}