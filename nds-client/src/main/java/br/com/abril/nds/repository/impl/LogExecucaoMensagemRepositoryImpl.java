package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.repository.LogExecucaoMensagemRepository;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.integracao.LogExecucao }  
 * @author InfoA2
 */
@Repository
public class LogExecucaoMensagemRepositoryImpl extends AbstractRepository<LogExecucaoMensagem,Long> implements LogExecucaoMensagemRepository {

	@Autowired
	DistribuidorService distribuidorService;
	
	/**
	 * Construtor padrão
	 */
	public LogExecucaoMensagemRepositoryImpl() {
		super(LogExecucaoMensagem.class);
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
		Criteria criteria = getSession().createCriteria(LogExecucaoMensagem.class, "logExecucaoMensagem");

		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		
		criteria.createCriteria("logExecucao", "logExecucao", Criteria.LEFT_JOIN);

		ProjectionList projList = Projections.projectionList();  
	    projList.add(Projections.max("logExecucao.dataInicio"));
	    
	    criteria.setProjection(projList);
		
	    // Caso a data de fim seja vazia, mostra como não processado
	    
		// Restrição de datas
		criteria.add(Restrictions.ge("logExecucao.dataInicio", dataOperacao));
		criteria.add(Restrictions.le("logExecucao.dataFim",    dataOperacao));
		
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
