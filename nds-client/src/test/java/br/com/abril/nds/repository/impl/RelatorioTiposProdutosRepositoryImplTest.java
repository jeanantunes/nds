package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.RelatorioTiposProdutosDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioTiposProdutos;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.repository.RelatorioTiposProdutosRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class RelatorioTiposProdutosRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private RelatorioTiposProdutosRepository relatorioTiposProdutosRepository;
	
	@Test
	public void gerarRelatorio(){
		FiltroRelatorioTiposProdutos filtro = new FiltroRelatorioTiposProdutos();
		filtro.setTipoProduto(1L);
		filtro.setDataLancamentoDe(Fixture.criarData(11, Calendar.APRIL, 2011));
		filtro.setDataLancamentoAte(Fixture.criarData(20, Calendar.APRIL, 2011));		
		filtro.setDataRecolhimentoDe(Fixture.criarData(22, Calendar.APRIL, 2011));
		filtro.setDataRecolhimentoAte(Fixture.criarData(29, Calendar.APRIL, 2011));
		
		
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setSortOrder("ASC");
		filtro.getPaginacaoVO().setSortColumn("codigo");
		filtro.getPaginacaoVO().setPaginaAtual(2);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(10);
		
		List<RelatorioTiposProdutosDTO> lista = relatorioTiposProdutosRepository.gerarRelatorio(filtro);
		
		Assert.assertNotNull(lista);
	}

	@Test
	public void gerarRelatorioPorTipoProduto(){
		FiltroRelatorioTiposProdutos filtro = new FiltroRelatorioTiposProdutos();
		filtro.setTipoProduto(1L);
			
		
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setSortOrder("ASC");
		filtro.getPaginacaoVO().setSortColumn("codigo");
		filtro.getPaginacaoVO().setPaginaAtual(2);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(10);
		
		
		List<RelatorioTiposProdutosDTO> lista = relatorioTiposProdutosRepository.gerarRelatorio(filtro);
		
		Assert.assertNotNull(lista);
	}

	@Test
	public void gerarRelatorioPorDataLancamentoDe(){
		FiltroRelatorioTiposProdutos filtro = new FiltroRelatorioTiposProdutos();
		filtro.setDataLancamentoDe(Fixture.criarData(11, Calendar.APRIL, 2011));
		
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setSortOrder("ASC");
		filtro.getPaginacaoVO().setSortColumn("codigo");
		filtro.getPaginacaoVO().setPaginaAtual(2);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(10);
		

		List<RelatorioTiposProdutosDTO> lista = relatorioTiposProdutosRepository.gerarRelatorio(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void gerarRelatorioPorDataLancamentoAte(){
		FiltroRelatorioTiposProdutos filtro = new FiltroRelatorioTiposProdutos();
		filtro.setDataLancamentoAte(Fixture.criarData(20, Calendar.APRIL, 2011));		
		
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setSortOrder("ASC");
		filtro.getPaginacaoVO().setSortColumn("codigo");
		filtro.getPaginacaoVO().setPaginaAtual(2);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(10);
		
		List<RelatorioTiposProdutosDTO> lista = relatorioTiposProdutosRepository.gerarRelatorio(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void gerarRelatorioPorDataRecolhimentoDe(){
		FiltroRelatorioTiposProdutos filtro = new FiltroRelatorioTiposProdutos();
		filtro.setDataRecolhimentoDe(Fixture.criarData(22, Calendar.APRIL, 2011));		
		
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setSortOrder("ASC");
		filtro.getPaginacaoVO().setSortColumn("codigo");
		filtro.getPaginacaoVO().setPaginaAtual(2);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(10);
		
		List<RelatorioTiposProdutosDTO> lista = relatorioTiposProdutosRepository.gerarRelatorio(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void gerarRelatorioPorDataRecolhimentoAte(){
		FiltroRelatorioTiposProdutos filtro = new FiltroRelatorioTiposProdutos();
		filtro.setDataRecolhimentoAte(Fixture.criarData(29, Calendar.APRIL, 2011));		
		
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setSortOrder("ASC");
		filtro.getPaginacaoVO().setSortColumn("codigo");
		filtro.getPaginacaoVO().setPaginaAtual(2);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(10);
		
		List<RelatorioTiposProdutosDTO> lista = relatorioTiposProdutosRepository.gerarRelatorio(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	
	@Test
	public void gerarRelatorioNulo(){
		FiltroRelatorioTiposProdutos filtro = new FiltroRelatorioTiposProdutos();
				
		filtro.setPaginacaoVO(new PaginacaoVO());
		filtro.getPaginacaoVO().setSortOrder("ASC");
		filtro.getPaginacaoVO().setSortColumn("codigo");
		filtro.getPaginacaoVO().setPaginaAtual(2);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(10);
		

		List<RelatorioTiposProdutosDTO> lista = relatorioTiposProdutosRepository.gerarRelatorio(filtro);
		
		Assert.assertNotNull(lista);
	}
	
}
