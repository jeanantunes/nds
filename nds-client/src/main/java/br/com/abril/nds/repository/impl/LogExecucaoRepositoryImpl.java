package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.repository.LogExecucaoRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.integracao.LogExecucao }  
 * @author InfoA2
 */
@Repository
public class LogExecucaoRepositoryImpl extends AbstractRepository<LogExecucao,Long> implements LogExecucaoRepository {

	/**
	 * Construtor padrão
	 */
	public LogExecucaoRepositoryImpl() {
		super(LogExecucao.class);
	}

	/**
	 * Busca os LogExecucao respeitando as restricoes parametrizadas.
	 * @param orderBy
	 * @param ordenacao
	 * @param initialResult
	 * @param maxResults
	 * @return List<LogExecucao>
	 */
	@Override
	public List<LogExecucao> buscaPaginada(String orderBy, Ordenacao ordenacao, int initialResult, int maxResults) {
		Criteria criteria = getSession().createCriteria(LogExecucao.class);
		
		if(Ordenacao.ASC ==  ordenacao){
			criteria.addOrder(Order.asc(orderBy));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(orderBy));
		}
		
		criteria.setMaxResults(maxResults);
		criteria.setFirstResult(initialResult);
		
		return criteria.list();	
	}

}
