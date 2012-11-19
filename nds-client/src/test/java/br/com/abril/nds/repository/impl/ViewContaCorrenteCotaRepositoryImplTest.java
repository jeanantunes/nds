package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO.ColunaOrdenacao;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.financeiro.ViewContaCorrenteCota;
import br.com.abril.nds.repository.ViewContaCorrenteCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class ViewContaCorrenteCotaRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private ViewContaCorrenteCotaRepository viewContaCorrenteCotaRepository;

	@SuppressWarnings("unused")
	@Test
	public void getQuantidadeViewContaCorrenteCota() {
		FiltroViewContaCorrenteCotaDTO filtro = new FiltroViewContaCorrenteCotaDTO();
		filtro.setInicioPeriodo(null);
		filtro.setFimPeriodo(null);

		Long quantidade = viewContaCorrenteCotaRepository
				.getQuantidadeViewContaCorrenteCota(filtro);

	}
	
	@SuppressWarnings("unused")
	@Test
	public void getQuantidadeViewContaCorrenteCotaPorPeriodo() {
		FiltroViewContaCorrenteCotaDTO filtro = new FiltroViewContaCorrenteCotaDTO();
		filtro.setInicioPeriodo(Fixture.criarData(15, Calendar.DECEMBER, 2011));
		filtro.setFimPeriodo(Fixture.criarData(15, Calendar.DECEMBER, 2012));

		Long quantidade = viewContaCorrenteCotaRepository
				.getQuantidadeViewContaCorrenteCota(filtro);

	}
	
	@Test
	public void getListaViewContaCorrenteCota() {
		FiltroViewContaCorrenteCotaDTO filtro = new FiltroViewContaCorrenteCotaDTO();
		filtro.setPaginacao(null);
		filtro.setColunaOrdenacao(null);

		List<ViewContaCorrenteCota> lista = viewContaCorrenteCotaRepository
				.getListaViewContaCorrenteCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void getListaViewContaCorrenteCotaPaginacao() {
		FiltroViewContaCorrenteCotaDTO filtro = new FiltroViewContaCorrenteCotaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(10);
		filtro.setColunaOrdenacao(null);

		List<ViewContaCorrenteCota> lista = viewContaCorrenteCotaRepository
				.getListaViewContaCorrenteCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void getListaViewContaCorrenteCotaPorColunaOrdenacao() {
		FiltroViewContaCorrenteCotaDTO filtro = new FiltroViewContaCorrenteCotaDTO();
		filtro.setPaginacao(null);
		filtro.setColunaOrdenacao("numeroCota");

		List<ViewContaCorrenteCota> lista = viewContaCorrenteCotaRepository
				.getListaViewContaCorrenteCota(filtro);
		
		Assert.assertNotNull(lista);
	}

	@Test
	public void getListaViewContaCorrenteCotaPorColunaOrdenacaoEPaginacao() {
		FiltroViewContaCorrenteCotaDTO filtro = new FiltroViewContaCorrenteCotaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(10);
		filtro.getPaginacao().setOrdenacao(Ordenacao.DESC);
		filtro.setColunaOrdenacao("numeroCota");
		

		List<ViewContaCorrenteCota> lista = viewContaCorrenteCotaRepository
				.getListaViewContaCorrenteCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
}
