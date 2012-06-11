package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.LancamentoPorEdicaoDTO;
import br.com.abril.nds.dto.VendaEncalheDTO;
import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.repository.VendaProdutoRepository;
import br.com.abril.nds.service.VendaProdutoService;
import br.com.abril.nds.util.TipoMensagem;

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

	@Override
	@Transactional(readOnly=true)
	public List<VendaEncalheDTO> buscarVendasProduto(FiltroVendaEncalheDTO filtro) {
		
		List<VendaEncalheDTO> lista = new ArrayList<VendaEncalheDTO>();
		
		for(int i=0;i<15;i++){
		
			VendaEncalheDTO vendaEncalheDTO = new VendaEncalheDTO();
			
			vendaEncalheDTO.setIdVenda(new Long(i));
			vendaEncalheDTO.setDataVenda(new Date());
			vendaEncalheDTO.setCodigoProduto("10" + i);
			vendaEncalheDTO.setNomeCota("Jose da Silva" + i);
			vendaEncalheDTO.setNomeProduto("Veja" + i);
			vendaEncalheDTO.setNumeroCota(123);
			vendaEncalheDTO.setNumeroEdicao(123);
			vendaEncalheDTO.setPrecoCapa(BigDecimal.TEN);
			vendaEncalheDTO.setPrecoDesconto(BigDecimal.TEN);
			vendaEncalheDTO.setQntProduto(10);
			vendaEncalheDTO.setTipoVendaEncalhe(TipoVendaEncalhe.ENCALHE);
			vendaEncalheDTO.setValoTotalProduto(BigDecimal.TEN);
			
			lista.add(vendaEncalheDTO);
		}
		return lista;
	}

	@Override
	@Transactional(readOnly=true)
	public Long buscarQntVendasProduto(FiltroVendaEncalheDTO filtro) {
		
		return 10L;
	}

}
