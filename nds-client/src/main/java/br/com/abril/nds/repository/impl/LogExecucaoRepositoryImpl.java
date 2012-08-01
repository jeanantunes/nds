package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaInterfacesDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.repository.LogExecucaoRepository;

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
	public List<ConsultaInterfacesDTO> obterInterfaces() {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Criteria criteria =  getSession().createCriteria(LogExecucaoMensagem.class, "logExecucaoMensagem");	
		
		criteria.createCriteria("logExecucao", "logExecucao", Criteria.LEFT_JOIN);
		criteria.createCriteria("logExecucao.interfaceExecucao", "interfaceExecucao", Criteria.LEFT_JOIN);
		
		criteria.setProjection( Projections.projectionList()
			    .add(Projections.groupProperty("logExecucao.status"), "status")  
			    .add(Projections.groupProperty("interfaceExecucao.nome"), "nome")  
			    .add(Projections.groupProperty("interfaceExecucao.id"), "id")  
			    .add(Projections.groupProperty("logExecucao.dataInicio"), "dataInicio")
			    .add(Projections.max("logExecucaoMensagem.nomeArquivo"), "nomeArquivo"))
			    .setResultTransformer(Transformers.aliasToBean(ConsultaInterfacesDTO.class)); 

		criteria.add( Restrictions.eq("logExecucao.dataInicio", distribuidor.getDataOperacao()) );

		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.LogExecucaoRepository#obterMensagensLogInterface(java.lang.Long, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public List<LogExecucaoMensagem> obterMensagensLogInterface(Long codigoLogExecucao) {

		Criteria criteria = addMensagensLogInterfaceRestrictions(codigoLogExecucao);
		
		return criteria.list();
	}

	/**
	 * Adiciona as restrições á busca de mensagens de log de interface
	 * @param codigoLogExecucao
	 * @return
	 */
	private Criteria addMensagensLogInterfaceRestrictions(Long codigoLogExecucao) {
		Criteria criteria = getSession().createCriteria(LogExecucaoMensagem.class);
		criteria.createCriteria("logExecucao", "logExecucao", Criteria.LEFT_JOIN);
		criteria.createCriteria("logExecucao.interfaceExecucao", "interfaceExecucao", Criteria.LEFT_JOIN);
		criteria.add(Restrictions.eq("interfaceExecucao.id", codigoLogExecucao));
		return criteria;
	}

	@Override
	public List<LogExecucaoMensagem> obterMensagensErroLogInterface(Long codigoLogExecucao) {
		Criteria criteria = addMensagensLogInterfaceRestrictions(codigoLogExecucao);
		
		criteria.add( Restrictions.not(Restrictions.eq("logExecucao.status", StatusExecucaoEnum.SUCESSO)) );

		return criteria.list();
	}
	
}
