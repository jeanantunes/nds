package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.repository.VendaProdutoRepository;
import br.com.abril.nds.service.VendaProdutoService;
import br.com.abril.nds.util.TipoMensagem;

public class VendaProdutoServiceImpl implements VendaProdutoService {
	
	@Autowired
	private VendaProdutoRepository vendaProdutoRepository;

	@Override
	public List<VendaProdutoDTO> buscaVendaporProduto(FiltroVendaProdutoDTO filtro) {
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
				
		return vendaProdutoRepository.buscarLancamentosParciais(filtro);
		 
	}

}
