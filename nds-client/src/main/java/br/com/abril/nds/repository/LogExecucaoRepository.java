package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaInterfacesDTO;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * @author InfoA2
 */
public interface LogExecucaoRepository extends Repository<LogExecucao, Long> {

	public List<ConsultaInterfacesDTO> obterInterfaces();

	public List<LogExecucaoMensagem> obterMensagensLogInterface(Long codigoLogExecucao);

	public Long quantidadeMensagensLogInterface(Long codigoLogExecucao);

	public List<LogExecucaoMensagem> obterMensagensErroLogInterface(Long codigoLogExecucao);
	
}
