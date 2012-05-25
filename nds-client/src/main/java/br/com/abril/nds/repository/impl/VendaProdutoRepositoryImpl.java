package br.com.abril.nds.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.repository.VendaProdutoRepository;

@Repository
public class VendaProdutoRepositoryImpl extends AbstractRepository<MovimentoEstoque, Long> implements VendaProdutoRepository {

	public VendaProdutoRepositoryImpl(Class<MovimentoEstoque> clazz) {
		super(MovimentoEstoque.class);
	}

	@Override
	public List<VendaProdutoDTO> buscarLancamentosParciais(
			FiltroVendaProdutoDTO filtro) {
		 
		return null;
	}

	
}
