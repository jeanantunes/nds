package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.service.DigitacaoContagemDevolucaoService;

public class DigitacaoContagemDevolucaoServiceImpl implements DigitacaoContagemDevolucaoService {

	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;  
	
	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao() {
		
		
	//	movimentoEstoqueRepository.obterListaExtratoEdicao(codigoProduto, numeroEdicao, statusAprovacao)
		return null;
		
	}
	
}
