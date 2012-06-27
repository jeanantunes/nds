package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.repository.LogExecucaoRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class LogExecucaoRepositoryImpl extends AbstractRepositoryModel<LogExecucao, Long>  implements LogExecucaoRepository {

	@Autowired
	DistribuidorService distribuidorService;
	
	/**
	 * Construtor padrão.
	 */
	public LogExecucaoRepositoryImpl() {
		super(LogExecucao.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.LogExecucaoRepository#obterResultadoPaginado(java.lang.String, java.lang.String, int, int)
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public List<LogExecucao> obterInterfaces() {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Criteria criteria =  getSession().createCriteria(LogExecucao.class, "logExecucao");	
		
		criteria.add( Restrictions.eq("logExecucao.dataInicio", distribuidor.getDataOperacao()) );
		
		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.LogExecucaoRepository#obterMensagensLogInterface(java.lang.Long, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public List<LogExecucaoMensagem> obterMensagensLogInterface(Long codigoLogExecucao, String orderBy, Ordenacao ordenacao, int initialResult, int maxResults) {

		Criteria criteria = addMensagensLogInterfaceRestrictions(codigoLogExecucao);
		
		if(Ordenacao.ASC ==  ordenacao){
			criteria.addOrder(Order.asc(orderBy));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(orderBy));
		}
		
		criteria.setMaxResults(maxResults);
		criteria.setFirstResult(initialResult);

		return criteria.list();
	}

	/**
	 * Retorna a quantidade de registros total de mensagens de log de interface na base de dados
	 * @param codigoLogExecucao
	 * @return
	 */
	@Override
	public Long quantidadeMensagensLogInterface(Long codigoLogExecucao){
		Criteria criteria = addMensagensLogInterfaceRestrictions(codigoLogExecucao);
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.list().get(0);
	}

	/**
	 * Adiciona as restrições á busca de mensagens de log de interface
	 * @param codigoLogExecucao
	 * @return
	 */
	private Criteria addMensagensLogInterfaceRestrictions(Long codigoLogExecucao) {
		Criteria criteria = getSession().createCriteria(LogExecucaoMensagem.class);
		criteria.createCriteria("logExecucao", "logExecucao", Criteria.LEFT_JOIN);
		criteria.add(Restrictions.eq("logExecucao.id", codigoLogExecucao));
		return criteria;
	}
	
}
