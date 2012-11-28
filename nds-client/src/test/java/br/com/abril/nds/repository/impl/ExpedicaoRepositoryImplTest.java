package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO.OrdenacaoColunaProduto;
import br.com.abril.nds.repository.ExpedicaoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class ExpedicaoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private ExpedicaoRepository expedicaoRepository;

	private FiltroResumoExpedicaoDTO filtro;

	@Before
	public void setUp() {

		this.filtro = new FiltroResumoExpedicaoDTO();
		this.filtro.setCodigoBox(1);
		this.filtro.setDataLancamento(new Date());
	}

	@Test
	public void obterQuantidadeResumoExpedicaoProdutosDoBox() {

		this.expedicaoRepository
				.obterQuantidadeResumoExpedicaoProdutosDoBox(this.filtro);
	}

	@Test
	public void obterResumoExpedicaoProdutosDoBox() {

		this.expedicaoRepository.obterResumoExpedicaoProdutosDoBox(this.filtro);
	}

	// TESTES SEM USO DE MASSA
	
	@Test
	public void testarObterQuantidadeResumoExpedicaoProdutosDoBox() {
		
		Long quantidadeResumo;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		
		quantidadeResumo = expedicaoRepository.obterQuantidadeResumoExpedicaoProdutosDoBox(filtro);
		
		Assert.assertNotNull(quantidadeResumo);
		
	}
	
	@Test
	 public void testarObterResumoExpedicaoProdutosDoBox() {
		
		List<ExpedicaoDTO> resumoExpedicao;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		
		resumoExpedicao = expedicaoRepository.obterResumoExpedicaoProdutosDoBox(filtro);
		
		Assert.assertNotNull(resumoExpedicao);
		
	}
	
//	getPaginacao
	@Test
	 public void testarObterResumoExpedicaoProdutosDoBoxGetPaginacao() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<ExpedicaoDTO> resumoExpedicao;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setPaginacao(paginacao);
		
		resumoExpedicao = expedicaoRepository.obterResumoExpedicaoProdutosDoBox(filtro);
		
		Assert.assertNotNull(resumoExpedicao);
		
	}
	
	@Test
	public void testarObterResumoExpedicaoPorProduto() {
		
		List<ExpedicaoDTO> resumoExpedicao;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		
		resumoExpedicao = expedicaoRepository.obterResumoExpedicaoPorProduto(filtro);
		
		Assert.assertNotNull(resumoExpedicao);
		
	}
	
//	getPaginacao
	@Test
	 public void testarObterResumoExpedicaoExpedicaoPorProdutoGetPaginacao() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<ExpedicaoDTO> resumoExpedicao;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setPaginacao(paginacao);
		
		resumoExpedicao = expedicaoRepository.obterResumoExpedicaoPorProduto(filtro);
		
		Assert.assertNotNull(resumoExpedicao);
		
	}
	
//	getOrderBy() por obterResumoExpedicaoPorProduto()
//	CODIGO_PRODUTO
	@Test
	 public void testarGetOrderByCODIGOPRODUTO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<ExpedicaoDTO> resumoExpedicao;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColunaProduto(OrdenacaoColunaProduto.CODIGO_PRODUTO);
		
		resumoExpedicao = expedicaoRepository.obterResumoExpedicaoPorProduto(filtro);
		
		Assert.assertNotNull(resumoExpedicao);
		
	}
	
//	DESCRICAO_PRODUTO
	@Test
	 public void testarGetOrderByDESCRICAOPRODUTO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<ExpedicaoDTO> resumoExpedicao;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColunaProduto(OrdenacaoColunaProduto.DESCRICAO_PRODUTO);
		
		resumoExpedicao = expedicaoRepository.obterResumoExpedicaoPorProduto(filtro);
		
		Assert.assertNotNull(resumoExpedicao);
		
	}
	
//	NUMERO_EDICAO
	@Test
	 public void testarGetOrderByNUMEROEDICAO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<ExpedicaoDTO> resumoExpedicao;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColunaProduto(OrdenacaoColunaProduto.NUMERO_EDICAO);
		
		resumoExpedicao = expedicaoRepository.obterResumoExpedicaoPorProduto(filtro);
		
		Assert.assertNotNull(resumoExpedicao);
		
	}
	
//	PRECO_CAPA
	@Test
	 public void testarGetOrderByPRECOCAPA() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<ExpedicaoDTO> resumoExpedicao;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColunaProduto(OrdenacaoColunaProduto.PRECO_CAPA);
		
		resumoExpedicao = expedicaoRepository.obterResumoExpedicaoPorProduto(filtro);
		
		Assert.assertNotNull(resumoExpedicao);
		
	}
	
//	REPARTE
	@Test
	 public void testarGetOrderByREPARTE() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<ExpedicaoDTO> resumoExpedicao;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColunaProduto(OrdenacaoColunaProduto.REPARTE);
		
		resumoExpedicao = expedicaoRepository.obterResumoExpedicaoPorProduto(filtro);
		
		Assert.assertNotNull(resumoExpedicao);
		
	}
	
//	DIFERENCA
	@Test
	 public void testarGetOrderByDIFERENCA() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<ExpedicaoDTO> resumoExpedicao;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColunaProduto(OrdenacaoColunaProduto.DIFERENCA);
		
		resumoExpedicao = expedicaoRepository.obterResumoExpedicaoPorProduto(filtro);
		
		Assert.assertNotNull(resumoExpedicao);
		
	}
	
//	VALOR_FATURADO
	@Test
	 public void testarGetOrderByVALORFATURADO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<ExpedicaoDTO> resumoExpedicao;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColunaProduto(OrdenacaoColunaProduto.VALOR_FATURADO);
		
		resumoExpedicao = expedicaoRepository.obterResumoExpedicaoPorProduto(filtro);
		
		Assert.assertNotNull(resumoExpedicao);
		
	}
	
	@Test
	public void testarObterResumoExpedicaoPorBox() {
		
		List<ExpedicaoDTO> listaResumoExpedicao;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		
		listaResumoExpedicao = expedicaoRepository.obterResumoExpedicaoPorBox(filtro);
		
		Assert.assertNotNull(listaResumoExpedicao);
		
	}
	
	@Test
	public void testarObterQuantidadeResumoExpedicaoPorProduto() {
		
		Long quantidadeResumo;
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		
		quantidadeResumo = expedicaoRepository.obterQuantidadeResumoExpedicaoPorProduto(filtro);
		
		Assert.assertNotNull(quantidadeResumo);	
		
	}
	
	@Test
	public void testarObterQuantidadeResumoExpedicaoPorBox() {
		
		Long quantidadeResumo;
		
		Long idBox = 1L;
		
		Calendar d = Calendar.getInstance();
		Date dataLancamento = d.getTime();
		
		quantidadeResumo = expedicaoRepository.obterQuantidadeResumoExpedicaoPorBox(idBox, dataLancamento);
		
		Assert.assertNotNull(quantidadeResumo);
		
	}
	
	@Test
	public void testarObterUltimaExpedicaoDia() {
		
		Date ultimaExpedicaoDia;
		
		Calendar d = Calendar.getInstance();
		Date dataOperacao = d.getTime();
		
		ultimaExpedicaoDia = expedicaoRepository.obterUltimaExpedicaoDia(dataOperacao);
		
//		Assert.assertNull(ultimaExpedicaoDia);
		
	}
	
	@Test
	public void testarObterDataUltimaExpedicao() {
		
		Date dataUltimaExpedicao;
		
		dataUltimaExpedicao = expedicaoRepository.obterDataUltimaExpedicao();
		
		Assert.assertNull(dataUltimaExpedicao);
		
	}



}
