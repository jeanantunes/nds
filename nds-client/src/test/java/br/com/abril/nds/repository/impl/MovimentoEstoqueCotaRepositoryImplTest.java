package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.PeriodoVO;

public class MovimentoEstoqueCotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	private static final String FILTRO = "filtro";
	private static final String LISTA_ID_PRODUTO = "listaIdProduto";
	
	
	@Test
	public void testarObterListaContagemDevolucao() {
		
		Map params = new HashMap();
		
		setUpParamPesquisaContagemDevolucao(params);
		
		TipoMovimentoEstoque tipoMovimentoEstoque = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		movimentoEstoqueCotaRepository.obterListaContagemDevolucao(
				(FiltroDigitacaoContagemDevolucaoDTO)params.get(FILTRO), 
				tipoMovimentoEstoque, 
				(List<Long>)params.get(LISTA_ID_PRODUTO));
	}

	@Test
	public void testarObterQuantidadeContagemDevolucao() {
		
		Map params = new HashMap();
		
		setUpParamPesquisaContagemDevolucao(params);
		
		TipoMovimentoEstoque tipoMovimentoEstoque = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		movimentoEstoqueCotaRepository.obterQuantidadeContagemDevolucao(
				(FiltroDigitacaoContagemDevolucaoDTO)params.get(FILTRO), 
				tipoMovimentoEstoque, 
				(List<Long>)params.get(LISTA_ID_PRODUTO));
	}
	
	private void setUpParamPesquisaContagemDevolucao(Map params) {
		
		List<Long> listaIdProdutoDosFornecedores = new LinkedList<Long>();
		listaIdProdutoDosFornecedores.add(1L);
		listaIdProdutoDosFornecedores.add(2L);
		listaIdProdutoDosFornecedores.add(3L);
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		
		PaginacaoVO paginacao = new PaginacaoVO();

		paginacao.setOrdenacao(Ordenacao.ASC);
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(500);

		filtro.setPaginacao(paginacao);

		Calendar dataInicial = Calendar.getInstance();
		dataInicial.set(2011, 1, 1);

		Calendar dataFinal = Calendar.getInstance();
		dataFinal.set(2015, 1, 1);

		PeriodoVO periodo = new PeriodoVO();
		
		periodo.setDataInicial(dataInicial.getTime());
		
		periodo.setDataInicial(dataFinal.getTime());
		
		filtro.setPeriodo(periodo);
		
		filtro.setOrdenacaoColuna(OrdenacaoColuna.CODIGO_PRODUTO);
		
		params.put(FILTRO, filtro);
		
		params.put(LISTA_ID_PRODUTO, listaIdProdutoDosFornecedores);
		
	}
	
}
