package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheVendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.repository.VendaProdutoRepository;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.VendaProdutoService;

@Service
public class VendaProdutoServiceImpl implements VendaProdutoService {
	
	@Autowired
	private VendaProdutoRepository vendaProdutoRepository;
	
	@Autowired
	private TipoClassificacaoProdutoRepository tipoClassificacaoProduto;
	
	@Autowired
	private EstoqueProdutoService estoqueProdutoService;

	@Override
	@Transactional
	public List<VendaProdutoDTO> buscaVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		
		if(filtro == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		}
		
		List<VendaProdutoDTO> listVendaProduto = vendaProdutoRepository.buscarVendaPorProduto(filtro);
				
		for (VendaProdutoDTO vendaProdutoDTO : listVendaProduto) {
			if(vendaProdutoDTO.getStatusLancamento().equalsIgnoreCase("FECHADO") 
					&& (vendaProdutoDTO.getVenda() == null || vendaProdutoDTO.getVenda().compareTo(BigInteger.ZERO) <= 0)){
				vendaProdutoDTO.setVenda(estoqueProdutoService.obterVendaBaseadoNoEstoque(vendaProdutoDTO.getIdProdutoEdicao().longValue()).toBigInteger());
			}
		}
		
		return listVendaProduto;
		 
	}

	@Override
	@Transactional
	public List<LancamentoPorEdicaoDTO> buscaLancamentoPorEdicao(FiltroDetalheVendaProdutoDTO filtro) {
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		 
		List<LancamentoPorEdicaoDTO> listaLancamentoPorEdicao =
			this.vendaProdutoRepository.buscarLancamentoPorEdicao(filtro);
		
		int numPeriodo = 1;
		
		BigInteger vendaAcumulada = BigInteger.ZERO;
		
		for (LancamentoPorEdicaoDTO lancamentoPorEdicao : listaLancamentoPorEdicao) {
			
			vendaAcumulada = vendaAcumulada.add(lancamentoPorEdicao.getVenda());
			
			lancamentoPorEdicao.setPeriodo(numPeriodo++ + "º");
			lancamentoPorEdicao.setVendaAcumulada(vendaAcumulada);
		}
		
		return listaLancamentoPorEdicao;
	}
	
	@Override
	@Transactional
	public List<TipoClassificacaoProduto> buscarClassificacaoProduto() {
		return tipoClassificacaoProduto.buscarTodos();
	}
}
