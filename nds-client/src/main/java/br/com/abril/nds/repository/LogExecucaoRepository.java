package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * @author InfoA2
 */
public interface LogExecucaoRepository extends Repository<LogExecucao, Long> {

	public List<LogExecucao> obterInterfaces();

	public List<LogExecucaoMensagem> obterMensagensLogInterface(Long codigoLogExecucao, String orderBy, Ordenacao ordenacao, int initialResult, int maxResults);

	public Long quantidadeMensagensLogInterface(Long codigoLogExecucao);
	
}
