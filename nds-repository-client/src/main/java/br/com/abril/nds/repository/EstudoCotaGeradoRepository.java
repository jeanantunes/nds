package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.util.Intervalo;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.EstudoCota}  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoCotaGeradoRepository extends Repository<EstudoCotaGerado, Long> {

	/**
	 * Obtém o estudo da cota de acordo com o
	 * número da cota e a data de referência do lançamento.
	 * 
	 * @param numeroCota - número da cota
	 * @param dataReferencia - data de referência
	 * 
	 * @return {@link EstudoCota}
	 */
	EstudoCotaGerado obterEstudoCota(Integer numeroCota, Date dataReferencia);
	
	List<EstudoCotaGerado> obterEstudoCota(Long isCota, Date dataDe, Date dataAte);
	
	List<EstudoCotaDTO> obterEstudoCotaPorDataProdutoEdicao(Date dataLancamento, Long idProdutoEdicao);
	
	EstudoCotaGerado obterEstudoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota);
	
	EstudoCotaGerado obterEstudoCotaDeLancamentoComEstudoFechado(Date dataLancamentoDistribuidor, Long idProdutoEdicao, Integer numeroCota);
	
	List<EstudoCotaGerado> obterEstudosCotaParaNotaEnvio(List<Long> listaIdCotas, 
												   Intervalo<Date> periodo, 
												   List<Long> listaIdsFornecedores,
												   String exibirNotasEnvio);

	/**
	 * 
	 * @param idEstudo
	 */
	public abstract void removerEstudoCotaPorEstudo(Long idEstudo);

	List<EstudoCotaGerado> obterEstudosCota(Long idEstudo);
	
	List<EstudoCotaGerado> obterEstudoCotaPorEstudo(EstudoGerado estudo);

	void inserirProdutoBase(EstudoGerado estudo);
	
	void removerEstudosCotaPorEstudos(List<Long> listIdEstudos);

	EstudoCotaGerado obterEstudoCotaGerado(Long cotaId, Long estudoId);
	
	void removerEstudoCotaGerado(Long idEstudoCotaGerado);
}
