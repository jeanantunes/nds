package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroExtratoEdicaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.vo.PaginacaoVO;

public class MovimentoEstoqueRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private MovimentoEstoqueRepositoryImpl movimentoEstoqueRepositoryImpl;
	
	@Test
	public void testarChamada() {
		FiltroExtratoEdicaoDTO filtro = new FiltroExtratoEdicaoDTO();
		
		List<ExtratoEdicaoDTO> lista = movimentoEstoqueRepositoryImpl.obterListaExtratoEdicao(filtro, null);
		
		Assert.assertNotNull(lista);
	}

	@Test
	public void testarChamadaPorStatusAprovacao() {
		FiltroExtratoEdicaoDTO filtro = new FiltroExtratoEdicaoDTO();
		StatusAprovacao statusAprovacao = StatusAprovacao.APROVADO;
		
		List<ExtratoEdicaoDTO> lista = movimentoEstoqueRepositoryImpl.obterListaExtratoEdicao(filtro, statusAprovacao);
		
		Assert.assertNotNull(lista);
	}

	@Test
	public void testarChamadaComPaginacao() {
		FiltroExtratoEdicaoDTO filtro = new FiltroExtratoEdicaoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		filtro.getPaginacao().setPaginaAtual(1);
		
		List<ExtratoEdicaoDTO> lista = movimentoEstoqueRepositoryImpl.obterListaExtratoEdicao(filtro, null);
		
		Assert.assertNotNull(lista);
	}

}
