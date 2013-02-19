package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ConsultaInterfacesDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheProcessamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroInterfacesDTO;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * @author InfoA2
 */
public interface LogExecucaoRepository extends Repository<LogExecucao, Long> {

	public List<LogExecucaoMensagem> obterMensagensLogInterface(Long codigoLogExecucao);

	public List<LogExecucaoMensagem> obterMensagensErroLogInterface(Long codigoLogExecucao, Date dataOperacao, FiltroDetalheProcessamentoDTO filtro);

	public Long obterTotalMensagensErroLogInterface(long codigoLogExecucao, Date obterDataOperacaoDistribuidor, FiltroDetalheProcessamentoDTO filtro);

	public List<ConsultaInterfacesDTO> obterInterfaces(FiltroInterfacesDTO filtro);

	public BigInteger obterTotalInterfaces(FiltroInterfacesDTO filtro);

}
