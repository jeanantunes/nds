package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.repository.LogExecucaoMensagemRepository;
import br.com.abril.nds.service.LogExecucaoMensagemService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente ao serviço da entidade
 * {@link br.com.abril.nds.model.integracao.LogExecucao}
 * 
 * @author InfoA2
 */
@Service
public class LogExecucaoMensagemServiceImpl implements LogExecucaoMensagemService {

	@Autowired
	private LogExecucaoMensagemRepository LogExecucaoMensagemRepository;

	/**
	 * Busca os LogExecucao respeitando as restricoes parametrizadas.
	 * @param orderBy
	 * @param ordenacao
	 * @param initialResult
	 * @param maxResults
	 * @return List<LogExecucao>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<LogExecucao> buscaPaginada(String orderBy, Ordenacao ordenacao, int initialResult, int maxResults) {
		return LogExecucaoMensagemRepository.buscaPaginada(orderBy, ordenacao, initialResult, maxResults);
	}

}
