package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
import br.com.abril.nds.util.DateUtil;

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
			
			if(vendaProdutoDTO.getPeriodoFormatado() == null){
				vendaProdutoDTO.setPeriodoFormatado(0);
			}
		}
		
		return listVendaProduto;
		 
	}

	@Override
	@Transactional
	public List<LancamentoPorEdicaoDTO> buscaLancamentoPorEdicao(FiltroDetalheVendaProdutoDTO filtro) {
		
		List<LancamentoPorEdicaoDTO> listaLancamentoPorEdicao = new ArrayList<>();
		
		if(filtro == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		}
		
		FiltroVendaProdutoDTO ftro = new FiltroVendaProdutoDTO();
		
		ftro.setCodigo(filtro.getCodigo());
		ftro.setEdicao(filtro.getEdicao());
		
		List<VendaProdutoDTO> listVendaProduto = vendaProdutoRepository.buscarVendaPorProduto(ftro);
		 
		for (VendaProdutoDTO vendas : listVendaProduto) {
			
			LancamentoPorEdicaoDTO lancamento = new LancamentoPorEdicaoDTO();
			
			lancamento.setDataLancamento(DateUtil.parseDataPTBR(vendas.getDataLancamento()));
			lancamento.setDataRecolhimento(DateUtil.parseDataPTBR(vendas.getDataRecolhimento()));
			lancamento.setReparte(vendas.getReparte());
			lancamento.setVenda(vendas.getVenda());
			lancamento.setEncalhe(vendas.getReparte().subtract(vendas.getVenda()));
			
			BigInteger percentual = new BigInteger("100");

			BigInteger percentualVendas = (vendas.getVenda().multiply(percentual).divide(vendas.getReparte())); 
			
			lancamento.setPercentualVenda(new BigDecimal(percentualVendas));
			
			listaLancamentoPorEdicao.add(lancamento);
		}
		
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
