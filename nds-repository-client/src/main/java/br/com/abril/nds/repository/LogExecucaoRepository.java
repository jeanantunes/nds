package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ConsultaInterfacesDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheProcessamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroInterfacesDTO;
import br.com.abril.nds.model.integracao.InterfaceExecucao;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * @author InfoA2
 */
public interface LogExecucaoRepository extends Repository<LogExecucao, Long> {

	List<LogExecucaoMensagem> obterMensagensLogInterface(Long codigoLogExecucao);

	List<LogExecucaoMensagem> obterMensagensErroLogInterface(FiltroDetalheProcessamentoDTO filtro);

	List<ConsultaInterfacesDTO> obterInterfaces(FiltroInterfacesDTO filtro);

	LogExecucao inserir(LogExecucao logExecucao);

	void atualizar(LogExecucao logExecucao);

	Long obterTotalMensagensErroLogInterface(FiltroDetalheProcessamentoDTO filtro);

	BigInteger obterTotalInterfaces(FiltroInterfacesDTO filtro);
	
	List<ConsultaInterfacesDTO> obterInterfacesExecucaoMicroDistribuicao();
	
	InterfaceExecucao findByID(Long id);
	
	InterfaceExecucao findByNome(String nome);

	Date buscarDataUltimaExecucao(String nome);
	
}