package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO.ColunaOrdenacao;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.repository.DividaRepository;

@Repository
public class DividaRepositoryImpl extends AbstractRepository<Divida, Long> implements
		DividaRepository {

	public DividaRepositoryImpl() {
		super(Divida.class);
	}
	
	public Divida obterUltimaDividaPorCota(Long idCota){
		Criteria criteria = this.getSession().createCriteria(Divida.class);
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.setProjection(Projections.max("data"));
		
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
		
		hql.append(getSqldividas(true,filtro));
		
		Query query = super.getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosConsultaDividas(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		return (Long) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GeraDividaDTO> obterDividasGeradas(FiltroDividaGeradaDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(getSqldividas(false,filtro));
		
		hql.append(getOrdenacaoDivida(filtro));
		
		Query query = super.getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = getParametrosConsultaDividas(filtro);
		
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
	private HashMap<String,Object> getParametrosConsultaDividas(FiltroDividaGeradaDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		param.put("data",filtro.getDataMovimento());
		
		if(filtro.getNumeroCota()!= null && filtro.getNumeroCota() > 0 ){
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
	private String getSqldividas(boolean count,FiltroDividaGeradaDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		if(count){
			hql.append(" SELECT COUNT (divida.id)");
		}else{
			hql.append(" SELECT new ").append(GeraDividaDTO.class.getCanonicalName())
			.append("(")
				.append(" box.codigo,")
				.append(" rota.codigoRota,")
				.append(" roteiro.descricaoRoteiro,")
				.append(" cota.numeroCota,")
				.append(" cota.pessoa.nome,")
				.append(" cobranca.dataVencimento,")
				.append(" cobranca.dataEmissao,")
				.append(" divida.valor,")
				.append(" cobranca.tipoCobranca,")
				.append(" cobranca.vias, ")
				.append(" cobranca.nossoNumero, ")
				.append(" cota.parametroCobranca.recebeCobrancaEmail ")
			.append(")");
		}
		
		hql.append(" FROM ")
		.append(" Divida divida ")
		.append(" JOIN divida.cobranca cobranca ")
		.append(" JOIN cobranca.cota cota ")
		.append(" JOIN cota.box box")
		.append(" LEFT JOIN cota.rotaRoteiroOperacao rotaRoteiroOperacao ")
		.append(" LEFT JOIN rotaRoteiroOperacao.rota rota ")
		.append(" LEFT JOIN rotaRoteiroOperacao.roteiro roteiro ")
		
		.append(" WHERE ")
		
		.append(" divida.data =:data ");
		
		if(filtro.getNumeroCota()!= null && filtro.getNumeroCota() > 0 ){
			hql.append(" AND cota.numeroCota =:numeroCota ");
		}	
		
		if(filtro.getTipoCobranca()!= null){
			hql.append(" AND cobranca.tipoCobranca =:tipoCobranca ");
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
						orderByColumn += " divida.valor ";
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
}