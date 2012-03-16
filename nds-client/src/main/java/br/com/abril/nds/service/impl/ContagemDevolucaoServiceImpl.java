package br.com.abril.nds.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.service.ContagemDevolucaoService;

public class ContagemDevolucaoServiceImpl implements ContagemDevolucaoService {

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;  
	
	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao() {
		
		movimentoEstoqueCotaRepository.obterListaContagemDevolucao(null, null, null);
		
		
		return null;
	
	}
	
	private List<ContagemDevolucaoDTO> getFromBD() {
		
		List<ContagemDevolucaoDTO> lista = new LinkedList<ContagemDevolucaoDTO>();
		
		Long contador = 0L;
		
		Long max = 10L;
		
		ContagemDevolucaoDTO contagem = null;
		
		while(contador < max) {

			contagem = new ContagemDevolucaoDTO();
			contagem.setCodigoProduto(contador.toString());
			contagem.setNomeProduto("PRODUTO_"+contador);
			contagem.setNumeroEdicao(contador);
			//contagem.setQtdDevolucao(new BigDecimal(val));
			
			contagem = new ContagemDevolucaoDTO();
			
			contador++;

		}
		
		return lista;
		
	}
	
}
