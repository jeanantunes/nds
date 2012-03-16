package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;

public class MovimentoEstoqueCotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Test
	public void testarChamada() {
		
		List<Long> listaIdProdutoDosFornecedores = new LinkedList<Long>();
		
		listaIdProdutoDosFornecedores.add(1L);
		listaIdProdutoDosFornecedores.add(2L);
		listaIdProdutoDosFornecedores.add(3L);
		
		TipoMovimentoEstoque tipoMovimento = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		movimentoEstoqueCotaRepository.obterListaContagemDevolucao(new Date(), tipoMovimento, listaIdProdutoDosFornecedores);
	}

}
