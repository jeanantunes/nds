package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.service.ExtratoEdicaoService;


@Service
public class ExtratoEdicaoServiceImpl implements ExtratoEdicaoService {

	@Autowired
	MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Override
	public List<ExtratoEdicaoDTO> obterListaExtratoEdicao(){
		
		List<MovimentoEstoque> lista = movimentoEstoqueRepository.obterListaMovimentoEstoque();
		
		return null;
		
	}
	
	
}
