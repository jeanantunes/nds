package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaNFENotasPendentesDTO;
import br.com.abril.nds.dto.ConsultaNFENotasRecebidasDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNFEEncalheTratamento;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.repository.ConsultaNFEEncalheTratamentoNotasRecebidasRepository;

@Repository
public class ConsultaNFEEncalheTratamentoNotasRecebidasRepositoryImpl extends AbstractRepository<NotaFiscalEntrada, Long> implements
		ConsultaNFEEncalheTratamentoNotasRecebidasRepository {

	public ConsultaNFEEncalheTratamentoNotasRecebidasRepositoryImpl() {
		super(NotaFiscalEntrada.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaNFENotasRecebidasDTO> buscarNFNotasRecebidas(FiltroConsultaNFEEncalheTratamento filtro, String limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nome, ");		
		hql.append("notaCota.numero as numeroNfe, ");
		hql.append("notaCota.serie as serie, ");
		hql.append("notaCota.chaveAcesso as chaveAcesso ");
		
		hql.append(getSqlFromEWhereNotaEntrada(filtro));
		
		hql.append(getOrderBy(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametros(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaNFENotasRecebidasDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar.equals("limitar")) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		 
		return query.list();
	}
	
	private String getSqlFromEWhereNotaEntrada(FiltroConsultaNFEEncalheTratamento filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" from NotaFiscalEntradaCota as notaCota ");
		hql.append(" LEFT JOIN notaCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		boolean usarAnd = false;
		
		if(filtro.getCodigoCota() != null && !filtro.getCodigoCota().equals("") ) {			
			hql.append( (usarAnd ? " and ":" where ") +" cota.numeroCota = :numeroCota ");
			usarAnd = true;
		}
		if(filtro.getData() != null){
			hql.append( (usarAnd ? " and ":" where ") +" date(notaCota.dataEmissao) = :data ");
			usarAnd = true;			
		}

		return hql.toString();
	}
	
	private String getOrderBy(FiltroConsultaNFEEncalheTratamento filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		hql.append(" order by cota.numeroCota ");
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	private HashMap<String,Object> buscarParametros(FiltroConsultaNFEEncalheTratamento filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		if(filtro.getCodigoCota() != null && !filtro.getCodigoCota().equals("")) { 
			param.put("numeroCota", filtro.getCodigoCota());
		}
		if(filtro.getData() != null){
			param.put("data", filtro.getData());
		}		
		return param;
	}

	@Override
	public Integer buscarTotalNotasRecebidas(
			FiltroConsultaNFEEncalheTratamento filtro) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(cota) ");
		
		if(filtro.getStatusNotaFiscalEntrada().name().equals("RECEBIDA")){
			hql.append(getSqlFromEWhereNotaEntrada(filtro));			
		}else{
			hql.append(getSqlFromEWhereNotaPendente(filtro));
		}
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametros(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}	
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaNFENotasPendentesDTO> buscarNFNotasPendentes(
			FiltroConsultaNFEEncalheTratamento filtro, String limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nome, ");		
		hql.append("conf.data as dataEncalhe, ");		
		hql.append("tipo.tipoOperacao as tipoNota, ");
		hql.append("(conf.qtdeInformada * conf.precoCapaInformado ) as vlrNota, ");
		hql.append("(conf.qtde * conf.precoCapaInformado ) as vlrReal, ");
		hql.append("((conf.qtdeInformada * conf.precoCapaInformado) -  (conf.qtde * conf.precoCapaInformado)) as diferenca, ");
		hql.append(" notaCota.numero as numeroNfe, ");
		hql.append(" notaCota.serie as serie, ");
		hql.append(" notaCota.chaveAcesso as chaveAcesso ");
		
		hql.append(getSqlFromEWhereNotaPendente(filtro));
		
		hql.append(getOrderByNotasPendentes(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametros(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaNFENotasPendentesDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar.equals("limitar")) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		 
		return query.list();
		 
	}
	
	private String getSqlFromEWhereNotaPendente(FiltroConsultaNFEEncalheTratamento filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" from ControleConferenciaEncalheCota as controleCota ");
		hql.append(" LEFT JOIN controleCota.notaFiscalEntradaCota as notaCota ");
		hql.append(" LEFT JOIN notaCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN notaCota.tipoNotaFiscal as tipo ");
		hql.append(" LEFT JOIN controleCota.conferenciasEncalhe as conf");
		
		
		hql.append(" where tipo.tipoOperacao = 'ENTRADA' ");
		
		if(filtro.getStatusNotaFiscalEntrada().name().equals("PENDENTE_RECEBIMENTO")){
			hql.append(" and ((conf.qtdeInformada * conf.precoCapaInformado) -  (conf.qtde * conf.precoCapaInformado)) > 0 ");
			
		}else if(filtro.getStatusNotaFiscalEntrada().name().equals("PENDENTE_EMISAO")){
			hql.append(" and ((conf.qtdeInformada * conf.precoCapaInformado) -  (conf.qtde * conf.precoCapaInformado)) < 0 ");
		}
		
		if(filtro.getCodigoCota() != null && !filtro.getCodigoCota().equals("") ) {			
			hql.append( " and cota.numeroCota = :numeroCota ");
		}
		if(filtro.getData() != null){
			hql.append( " and date(notaCota.dataEmissao) = :data ");
		}

		return hql.toString();
	}
	
	private String getOrderByNotasPendentes(FiltroConsultaNFEEncalheTratamento filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		hql.append(" order by cota.numeroCota ");
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}	
	
}
