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
	
	List<EstudoCota> obterEstudoCota(Long isCota, Date dataDe, Date dataAte);
	
	List<EstudoCotaDTO> obterEstudoCotaPorDataProdutoEdicao(Long idLancamento, Long idProdutoEdicao);
	
	EstudoCota obterEstudoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota);
	
	EstudoCota obterEstudoCotaDeLancamentoComEstudoFechado(Date dataLancamentoDistribuidor, Long idProdutoEdicao, Integer numeroCota);
	
	List<EstudoCota> obterEstudosCotaParaNotaEnvio(List<Long> listaIdCotas, 
												   Intervalo<Date> periodo, 
												   List<Long> listaIdsFornecedores,
												   String exibirNotasEnvio);
	
	List<EstudoCota> obterEstudosCota(Long idEstudo);
	
	void removerEstudosCotaPorEstudos(List<Long> listIdEstudos);

}
