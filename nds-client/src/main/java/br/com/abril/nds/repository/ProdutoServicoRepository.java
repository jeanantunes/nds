package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.pk.ProdutoServicoPK;

public interface ProdutoServicoRepository  extends Repository<ProdutoServico, ProdutoServicoPK> {
	
	/**
	 * Atualiza tabela de amarração que informa se o produto de um movimento possui nota fiscal.
	 * 
	 * @param listaProdutoServico 
	 * @param listItemNotaFiscal
	 */
	public abstract void atualizarProdutosQuePossuemNota(List<ProdutoServico> listaProdutoServico, 
			List<ItemNotaFiscal> listItemNotaFiscal);
	
}
