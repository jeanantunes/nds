package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.util.Intervalo;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.EstudoCota}  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoCotaRepository extends Repository<EstudoCota, Long> {

	/**
	 * Obtém o estudo da cota de acordo com o
	 * número da cota e a data de referência do lançamento.
	 * 
	 * @param numeroCota - número da cota
	 * @param dataReferencia - data de referência
	 * 
	 * @return {@link EstudoCota}
	 */
	EstudoCota obterEstudoCota(Integer numeroCota, Date dataReferencia);
	
	List<EstudoCotaDTO> obterEstudoCotaPorDataProdutoEdicao(Date dataLancamento, Long idProdutoEdicao);
	
	EstudoCota obterEstudoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota);
	
	EstudoCota obterEstudoCotaDeLancamentoComEstudoFechado(Date dataLancamentoDistribuidor, Long idProdutoEdicao, Integer numeroCota);
	
	List<EstudoCota> obterEstudosCotaParaNotaEnvio(Long idCota, 
												   Intervalo<Date> periodo, 
												   List<Long> listaIdsFornecedores);

	/**
	 * 
	 * @param idEstudo
	 */
	public abstract void removerEstudoCotaPorEstudo(Long idEstudo);

	List<EstudoCota> obterEstudosCota(Long idEstudo);
}
