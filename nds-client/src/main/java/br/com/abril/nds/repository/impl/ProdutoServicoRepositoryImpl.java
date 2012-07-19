package br.com.abril.nds.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.repository.ProdutoServicoRepository;

@Repository
public class ProdutoServicoRepositoryImpl extends AbstractRepositoryModel<ProdutoServico, Long> implements ProdutoServicoRepository {

	public ProdutoServicoRepositoryImpl() {
		super(ProdutoServico.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.ProdutoServicoRepository#atualizarProdutosQuePossuemNota(java.util.List, java.util.List)
	 */
	@Override
	public void atualizarProdutosQuePossuemNota(
			List<ProdutoServico> listaProdutoServico,
			List<ItemNotaFiscal> listItemNotaFiscal) {

		for (ProdutoServico produtoServico : listaProdutoServico) {
		
			for (ItemNotaFiscal itemNotaFiscal : listItemNotaFiscal) {
			
				produtoServico.setListaMovimentoEstoqueCota(itemNotaFiscal.getListaMovimentoEstoqueCota());
				merge(produtoServico);
			}
		}
		
	}

}
