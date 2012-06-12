package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaNFEEncalheTratamentoDTO;
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
	public List<ConsultaNFEEncalheTratamentoDTO> buscarNFNotasRecebidas(FiltroConsultaNFEEncalheTratamento filtro, String limitar) {
		
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
				ConsultaNFEEncalheTratamentoDTO.class));
		
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
		

		//hql.append( " where notaCota.TIPO = 'COTA' ");
		
		if(filtro.getCodigoCota() != null && !filtro.getCodigoCota().equals("") ) { 
			hql.append( " and cota.numeroCota = :numeroCota ");
		}
		if(filtro.getData() != null){
			hql.append(" and date(notaCota.dataEmissao) = :data ");
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
		
		hql.append(getSqlFromEWhereNotaEntrada(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametros(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}	
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}


}
