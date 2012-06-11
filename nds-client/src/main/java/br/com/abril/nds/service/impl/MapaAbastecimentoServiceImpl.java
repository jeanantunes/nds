package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoMapaDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.service.MapaAbastecimentoService;

@Service
public class MapaAbastecimentoServiceImpl implements MapaAbastecimentoService{

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Override
	@Transactional
	public List<AbastecimentoDTO> obterDadosAbastecimento(
			FiltroMapaAbastecimentoDTO filtro) {
		return movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);
	}

	@Override
	@Transactional
	public Long countObterDadosAbastecimento(FiltroMapaAbastecimentoDTO filtro) {
		return movimentoEstoqueCotaRepository.countObterDadosAbastecimento(filtro);
	}

	@Override
	@Transactional
	public List<ProdutoAbastecimentoDTO> obterDetlhesDadosAbastecimento(Long idBox, FiltroMapaAbastecimentoDTO filtro) {
		return movimentoEstoqueCotaRepository.obterDetlhesDadosAbastecimento(idBox,filtro);
	}



	/**
	 * Gera Mapa para Impressão por box
	 */
	public HashMap<String, ProdutoMapaDTO> obterMapaDeImpressaoPorBox(
			FiltroMapaAbastecimentoDTO filtro) {
		
		List<String> boxes =  new ArrayList<String>();
		
		HashMap<String, ProdutoMapaDTO> produtoMapa = new HashMap<String, ProdutoMapaDTO>();
		
		List<ProdutoAbastecimentoDTO> produtosPorBox = movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBox(filtro);
		
		for(ProdutoAbastecimentoDTO produtoPorBox : produtosPorBox ) {
			
			String keyProduto = produtoPorBox.getCodigoProduto();
			
			if(!boxes.contains(produtoPorBox.getCodigoBox()))
				boxes.add(produtoPorBox.getCodigoBox());
			
			if(produtoMapa.containsKey(keyProduto)) {
				
				adicionarReparteProduto(
						produtoMapa.get(keyProduto),
						produtoPorBox.getCodigoBox(),
						produtoPorBox.getReparte());
				
			} else {
				produtoMapa.put(keyProduto, inicializarNovoProdutoMapaDTO(produtoPorBox));
			}
		}
		
		preencheBoxNaoUtilizado(boxes, produtoMapa);		
		
		return produtoMapa;
	}
	
	/**
	 * Gera novo ProdutoMapaDTO preenchido com dados  de um ProdutoAbastecimentoDTO
	 *  
	 * @param produtoPorBox
	 * @return - ProdutoMapaDTO
	 */
	public ProdutoMapaDTO inicializarNovoProdutoMapaDTO(ProdutoAbastecimentoDTO produtoPorBox) {
		
		ProdutoMapaDTO dto =  new ProdutoMapaDTO(
				produtoPorBox.getCodigoProduto(), 
				produtoPorBox.getNomeProduto(), 
				produtoPorBox.getNumeroEdicao(), 
				produtoPorBox.getPrecoCapa(), 
				produtoPorBox.getReparte(), null);
		
		dto.setBoxQtde(new HashMap<String, Integer>());
		dto.getBoxQtde().put(produtoPorBox.getCodigoBox(), produtoPorBox.getReparte());
		
		return dto;
	}
	
	/**
	 * Adiciona reparte a um box do ProdutoMapaDTO passado como parâmeto
	 * 
	 * @param produtoMapa
	 * @param keyBox
	 * @param reparte
	 */
	private void adicionarReparteProduto(ProdutoMapaDTO produtoMapa, String keyBox, Integer reparte)  {
		
		if(!produtoMapa.getBoxQtde().containsKey(keyBox)) {
			produtoMapa.getBoxQtde().put(keyBox, reparte);
		} else {
			Integer novaQtde = reparte + produtoMapa.getBoxQtde().get(keyBox);
			produtoMapa.getBoxQtde().put(keyBox,novaQtde);
		}
	}
	
	/**
	 * Adiciona Boxes não existentes aos HashMaps de box de ProdutoMapaDTO
	 * 
	 * @param boxes
	 * @param produtos
	 */
	private void preencheBoxNaoUtilizado(List<String> boxes, HashMap<String, ProdutoMapaDTO> produtos) {
		for(String keyBox : boxes) {
			
			for(String keyProduto : produtos.keySet()) {
			
				if(!produtos.get(keyProduto).getBoxQtde().containsKey(keyBox))
					produtos.get(keyProduto).getBoxQtde().put(keyBox, 0);
			}
		}
	}
}
