package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.model.estoque.MovimentoEstoque;

public interface VendaProdutoRepository extends Repository<MovimentoEstoque, Long>{

	List<VendaProdutoDTO> buscarVendaPorProduto(FiltroVendaProdutoDTO filtro);
	
}
