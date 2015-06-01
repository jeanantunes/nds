package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;

public interface RecebimentoFisicoRepository extends Repository<RecebimentoFisico, Long> {	
		
   /**
	* Obtem lista com dados de itemRecebimento relativos ao id de uma nota fiscal.
	* 
	* @param idNotaFiscal
	* 
	* @return List - RecebimentoFisicoDTO
	* 
	*/
	List<RecebimentoFisicoDTO> obterListaItemRecebimentoFisico(Long idNotaFiscal);
	
	RecebimentoFisico obterRecebimentoFisicoPorNotaFiscal(Long idNotaFiscal);
	
	List<ItemRecebimentoFisico> obterItensRecebimentoFisicoDoProduto(Long idProdutoEdicao);

	boolean existeNotaFiscal(Long numero, Long serie, String cnpj, Long numeroNotaEnvio);

	/**
	 * Verifica se produto possui recebimento físico
	 * @param idProduto
	 * @return boolean
	 */
	boolean produtoPossuiRecebimentoFisico(Long idProduto);
	 
}