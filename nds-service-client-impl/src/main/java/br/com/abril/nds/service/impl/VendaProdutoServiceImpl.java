package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.repository.VendaProdutoRepository;
import br.com.abril.nds.service.VendaProdutoService;

@Service
public class VendaProdutoServiceImpl implements VendaProdutoService {
	
	@Autowired
	private VendaProdutoRepository vendaProdutoRepository;

	@Override
	@Transactional
	public List<VendaProdutoDTO> buscaVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
				
		return vendaProdutoRepository.buscarVendaPorProduto(filtro);
		 
	}

	@Override
	@Transactional
	public List<LancamentoPorEdicaoDTO> buscaLancamentoPorEdicao(FiltroVendaProdutoDTO filtro) {
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		 
		return vendaProdutoRepository.buscarLancamentoPorEdicao(filtro);
	}
}
