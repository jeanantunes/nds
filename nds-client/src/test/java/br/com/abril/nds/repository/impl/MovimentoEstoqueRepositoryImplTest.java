package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;

public class MovimentoEstoqueRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private MovimentoEstoqueRepositoryImpl movimentoEstoqueRepositoryImpl;
	
	@Test
	public void testarChamada() {
		List<ExtratoEdicaoDTO> lista = movimentoEstoqueRepositoryImpl.obterListaExtratoEdicao(null, StatusAprovacao.APROVADO);
	}

}
