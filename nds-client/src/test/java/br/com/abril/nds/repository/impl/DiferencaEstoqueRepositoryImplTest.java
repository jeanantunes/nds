package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO.OrdenacaoColunaConsulta;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO.OrdenacaoColunaLancamento;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class DiferencaEstoqueRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private DiferencaEstoqueRepositoryImpl diferencaEstoqueRepository;
	
	private Date dataMovimento;
	
	private BigInteger quantidadeDiferenca;
	
	private TipoDiferenca tipoDiferenca;
	
	private long qtdeMovimentosLancamento;
	
	private long qtdeMovimentosConsulta;
	
	private int qtdResultadoPorPagina;

	private TipoFornecedor tipoFornecedorPublicacao;
	
	private Editor abril;

	private Fornecedor fornecedor;
	
	private Date datalancamentoDiferenca = new Date();
	
	private Date datalancamentoDiferencaRateio;

	private ProdutoEdicao produtoEdicao;
	
	private Cota cotaManoel;
	
	@Before
	public void setup() {
		abril = Fixture.editoraAbril();
		save(abril);
		
		
		this.dataMovimento = Fixture.criarData(01, 2, 2012);
		
		this.quantidadeDiferenca = BigInteger.ONE;
		
		this.qtdeMovimentosLancamento = 30;
		
		this.qtdeMovimentosConsulta = 30;
		
		this.qtdResultadoPorPagina = 15;
		
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);

		produtoEdicao = this.criarProdutoEdicao();
		
		TipoMovimentoEstoque tipoMovimento = Fixture.tipoMovimentoFaltaEm();
		
		save(tipoMovimento);
		
		Usuario usuario = Fixture.usuarioJoao();
		
		save(usuario);
		
		this.tipoDiferenca = TipoDiferenca.FALTA_EM;
		
		StatusConfirmacao statusConfirmado = StatusConfirmacao.CONFIRMADO;
		
		StatusConfirmacao statusPendente = StatusConfirmacao.PENDENTE;
		
		CFOP cfop = Fixture.cfop5102();
		save(cfop);
		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento(cfop);
		
		save(tipoNotaFiscal);
		
		fornecedor = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedor);
		
		NotaFiscalEntradaFornecedor notaFiscalFornecedor = 
			Fixture.notaFiscalEntradaFornecedor(cfop, fornecedor, tipoNotaFiscal,
										 usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		
		save(notaFiscalFornecedor);

		ItemNotaFiscalEntrada itemNotaFiscal = 
			Fixture.itemNotaFiscal(produtoEdicao, usuario, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		
		save(itemNotaFiscal);

		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
			notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
		
		save(recebimentoFisico);
		
		ItemRecebimentoFisico itemRecebimentoFisico =  
			Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, BigInteger.valueOf(1));
		
		save(itemRecebimentoFisico);
		
		EstoqueProduto estoqueProduto = Fixture.estoqueProduto(produtoEdicao, quantidadeDiferenca);
		
		save(estoqueProduto);
		
		Lancamento lancamentoVeja =
			Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao,
							   DateUtil.adicionarDias(new Date(), 1), DateUtil.adicionarDias(new Date(),
							   produtoEdicao.getPeb()), new Date(),
							   new Date(), BigInteger.TEN,  StatusLancamento.CONFIRMADO,
							   itemRecebimentoFisico, 1);
		save(lancamentoVeja);
		
		for (int i = 0; i < this.qtdeMovimentosLancamento; i++) {
			
			MovimentoEstoque movimentoEstoque =
				Fixture.movimentoEstoque(
					itemRecebimentoFisico, produtoEdicao, tipoMovimento, usuario, 
						estoqueProduto, dataMovimento, 
							quantidadeDiferenca.multiply(BigInteger.valueOf(i)), StatusAprovacao.APROVADO, null);
			
			save(movimentoEstoque);
			
			LancamentoDiferenca lancamento = new LancamentoDiferenca();
			lancamento.setMovimentoEstoque(movimentoEstoque);
			lancamento.setDataProcessamento(new Date());
			
			save(lancamento);

			Diferenca diferenca = 
				Fixture.diferenca(quantidadeDiferenca, usuario, produtoEdicao,
								  tipoDiferenca, statusConfirmado, null,true, TipoEstoque.LANCAMENTO,TipoDirecionamentoDiferenca.COTA, datalancamentoDiferenca);
			
			diferenca.setItemRecebimentoFisico(itemRecebimentoFisico);
			diferenca.setLancamentoDiferenca(lancamento);
			
			save(diferenca);
			
		}
		
		for (int i = 0; i < this.qtdeMovimentosConsulta; i++) {
			
			MovimentoEstoque movimentoEstoque =
				Fixture.movimentoEstoque(
					itemRecebimentoFisico, produtoEdicao, tipoMovimento, usuario, 
						estoqueProduto, dataMovimento, 
							quantidadeDiferenca.multiply(BigInteger.valueOf(i)), StatusAprovacao.APROVADO, null);
			
			save(movimentoEstoque);
			
			LancamentoDiferenca lancamento = new LancamentoDiferenca();
			lancamento.setMovimentoEstoque(movimentoEstoque);
			lancamento.setDataProcessamento(new Date());
			
			save(lancamento);

			Diferenca diferenca = 
				Fixture.diferenca(quantidadeDiferenca, usuario, produtoEdicao,
								  tipoDiferenca, statusPendente, null, true, TipoEstoque.LANCAMENTO,TipoDirecionamentoDiferenca.COTA, datalancamentoDiferenca);
			
			diferenca.setItemRecebimentoFisico(itemRecebimentoFisico);
			diferenca.setLancamentoDiferenca(lancamento);
			
			save(diferenca);
			
		}
		
		datalancamentoDiferencaRateio = DateUtil.adicionarDias(datalancamentoDiferenca, 1);
		
		PessoaFisica manoel =
			Fixture.pessoaFisica("123.456.789-00", "sys.discover@gmail.com", "Manoel da Silva");
		
		save(manoel);
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		
		save(box1);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		
		save(cotaManoel);
		
		Diferenca diferenca = 
			Fixture.diferenca(
				quantidadeDiferenca, usuario, produtoEdicao, tipoDiferenca,
				statusPendente, null, true, TipoEstoque.LANCAMENTO,
				TipoDirecionamentoDiferenca.NOTA, datalancamentoDiferencaRateio);
		
		save(diferenca);
		
		RateioDiferenca rateioDiferenca =
			Fixture.rateioDiferenca(
				quantidadeDiferenca, cotaManoel, diferenca, null, datalancamentoDiferencaRateio);
		
		save(rateioDiferenca);
	}
	
	@Test
	public void obterDiferencasLancamento() {
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		
		filtro.setDataMovimento(datalancamentoDiferenca);
		filtro.setTipoDiferenca(this.tipoDiferenca);

		filtro.setOrdenacaoColuna(OrdenacaoColunaLancamento.VALOR_TOTAL_DIFERENCA);
		
		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setOrdenacao(Ordenacao.DESC);
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(this.qtdResultadoPorPagina);
		
		filtro.setPaginacao(paginacao);
		
		List<Diferenca> listaDiferencas = 
			this.diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(listaDiferencas);
		
		Assert.assertEquals(this.qtdResultadoPorPagina, listaDiferencas.size());
		
		for (Diferenca diferenca : listaDiferencas) {
			
			Assert.assertEquals(DateUtil.formatarDataPTBR(datalancamentoDiferenca),DateUtil.formatarDataPTBR(diferenca.getDataMovimento()));
			
			Assert.assertEquals(this.tipoDiferenca, diferenca.getTipoDiferenca());
		}
	}
	
	@Test
	public void obterTotalDiferencasLancamento() {
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		
		filtro.setDataMovimento(datalancamentoDiferenca);
		filtro.setTipoDiferenca(this.tipoDiferenca);
		
		Long quantidadeTotal = this.diferencaEstoqueRepository.obterTotalDiferencasLancamento(filtro);
		
		Assert.assertNotNull(quantidadeTotal);
		
		Assert.assertEquals(this.qtdeMovimentosLancamento, quantidadeTotal.longValue());
	}
	
	@Test
	public void obterDiferencas() {
		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setOrdenacao(Ordenacao.DESC);
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(10);
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		
		//filtro.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
		
		filtro.setTipoDiferenca(this.tipoDiferenca);
		
		filtro.setPaginacao(paginacao);
		
		List<Diferenca> lista = diferencaEstoqueRepository.obterDiferencas(filtro, null);
		
		Assert.assertNotNull(lista);
		
		Assert.assertTrue(!lista.isEmpty());
	}
	
	@Test
	public void obterTotalDiferencas() {
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		
		filtro.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
		
		filtro.setTipoDiferenca(this.tipoDiferenca);
		
		Long quantidadeTotal = diferencaEstoqueRepository.obterTotalDiferencas(filtro, null);
		
		Assert.assertNotNull(quantidadeTotal);
		
		Assert.assertEquals(this.qtdeMovimentosConsulta, quantidadeTotal.longValue());
	}
	
	private ProdutoEdicao criarProdutoEdicao() {
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProduto = Fixture.tipoRevista(ncmRevistas);
		
		save(tipoProduto);
		
		Produto produto = Fixture.produtoVeja(tipoProduto);
		produto.setEditor(abril);
		save(produto);
		
		ProdutoEdicao produtoEdicao = 
			Fixture.produtoEdicao(1L, 1, 1, new Long(10000), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOP", produto, null, false);
		
		save(produtoEdicao);
		
		return produtoEdicao;
	}
	
	@Test
	public void buscarStatusDiferencaLancadaAutomaticamente(){
		Boolean flag = null;
		
		flag = this.diferencaEstoqueRepository.buscarStatusDiferencaLancadaAutomaticamente(1L);
		
		Assert.assertNotNull(flag);
	}
	
	@Test
	public void obterQuantidadeTotalDiferencas() {
		
		BigInteger quantidadeTotalDiferencas =
			this.diferencaEstoqueRepository.obterQuantidadeTotalDiferencas(
				produtoEdicao.getProduto().getCodigo(), produtoEdicao.getNumeroEdicao(),
				TipoEstoque.LANCAMENTO, datalancamentoDiferenca);
		
		Assert.assertNotNull(quantidadeTotalDiferencas);
	}
	
	@Test
	public void obterDiferenca() {
		
		boolean existeDiferencaPorNota =
			this.diferencaEstoqueRepository.existeDiferencaPorNota(
				produtoEdicao.getId(), datalancamentoDiferencaRateio, cotaManoel.getNumeroCota());
				
		Assert.assertTrue(existeDiferencaPorNota);
	}
	
//	TESTES SEM USO DE MASSA
	
	@Test
	public void testarObterDiferencasLancamento() {
		
		List<Diferenca> diferencasLancamento;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		
		
		diferencasLancamento = diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(diferencasLancamento);
		
	}
	
//	OrdenacaoColuna CODIGO_PRODUTO
	@Test
	public void testarObterDiferencasLancamentoOrdenacaoColunaCODIGOPRODUTO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencasLancamento;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaLancamento.CODIGO_PRODUTO);
		
		diferencasLancamento = diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(diferencasLancamento);
		
	}
	
//	OrdenacaoColuna DESCRICAO_PRODUTO
	@Test
	public void testarObterDiferencasLancamentoOrdenacaoColunaDESCRICAOPRODUTO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencasLancamento;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaLancamento.DESCRICAO_PRODUTO);
		
		diferencasLancamento = diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(diferencasLancamento);
		
	}	
	
//	OrdenacaoColuna QUANTIDADE
	@Test
	public void testarObterDiferencasLancamentoOrdenacaoColunaQUANTIDADE() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencasLancamento;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaLancamento.QUANTIDADE);
		
		diferencasLancamento = diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(diferencasLancamento);
		
	}
	
//	OrdenacaoColuna NUMERO_EDICAO
	@Test
	public void testarObterDiferencasLancamentoOrdenacaoColunaNUMEROEDICAO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencasLancamento;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaLancamento.NUMERO_EDICAO);
		
		diferencasLancamento = diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(diferencasLancamento);
		
	}
	
//	OrdenacaoColuna PACOTE_PADRAO
	@Test
	public void testarObterDiferencasLancamentoOrdenacaoColunaPACOTEPADRAO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencasLancamento;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaLancamento.PACOTE_PADRAO);
		
		diferencasLancamento = diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(diferencasLancamento);
		
	}
	
//	OrdenacaoColuna PRECO_VENDA
	@Test
	public void testarObterDiferencasLancamentoOrdenacaoColunaPRECOVENDA() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencasLancamento;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaLancamento.PRECO_VENDA);
		
		diferencasLancamento = diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(diferencasLancamento);
		
	}
	
//	OrdenacaoColuna PRECO_DESCONTO
	@Test
	public void testarObterDiferencasLancamentoOrdenacaoColunaPRECODESCONTO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencasLancamento;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaLancamento.PRECO_DESCONTO);
		
		diferencasLancamento = diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(diferencasLancamento);
		
	}
	
//	OrdenacaoColuna TIPO_DIFERENCA
	@Test
	public void testarObterDiferencasLancamentoOrdenacaoColunaTIPODIFERENCA() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencasLancamento;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaLancamento.TIPO_DIFERENCA);
		
		diferencasLancamento = diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(diferencasLancamento);
		
	}
	
//	OrdenacaoColuna VALOR_TOTAL_DIFERENCA
	@Test
	public void testarObterDiferencasLancamentoOrdenacaoColunaVALORTOTALDIFERENCA() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencasLancamento;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaLancamento.VALOR_TOTAL_DIFERENCA);
		
		diferencasLancamento = diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(diferencasLancamento);
		
	}
	
//	DataMovimento
	@Test
	public void testarObterDiferencasLancamentoDataMovimento() {
		
		List<Diferenca> diferencasLancamento;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		Calendar dataMovimento = Calendar.getInstance();
		filtro.setDataMovimento(dataMovimento.getTime());
		
		diferencasLancamento = diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(diferencasLancamento);
		
	}
	
//	TipoDiferenca
	@Test
	public void testarObterDiferencasLancamentoTipoDiferenca() {
		
		List<Diferenca> diferencasLancamento;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		filtro.setTipoDiferenca(TipoDiferenca.FALTA_EM);
		
		diferencasLancamento = diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(diferencasLancamento);
		
	}
	
	@Test
	public void testarObterTotalDiferencasLancamento() {
		
		Long totalDiferencas;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		
		totalDiferencas = diferencaEstoqueRepository.obterTotalDiferencasLancamento(filtro);
		
		Assert.assertNotNull(totalDiferencas);
		
	}
	
//	DataMovimento
	@Test
	public void testarObterTotalDiferencasLancamentoDataMovimento() {
		
		Long totalDiferencas;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		Calendar dataMovimento = Calendar.getInstance();
		filtro.setDataMovimento(dataMovimento.getTime());
		
		totalDiferencas = diferencaEstoqueRepository.obterTotalDiferencasLancamento(filtro);
		
		Assert.assertNotNull(totalDiferencas);
		
	}
	
//	TipoDiferenca
	@Test
	public void testarObterTotalDiferencasLancamentoTipoDiferenca() {
		
		Long totalDiferencas;
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		filtro.setTipoDiferenca(TipoDiferenca.FALTA_EM);
		
		totalDiferencas = diferencaEstoqueRepository.obterTotalDiferencasLancamento(filtro);
		
		Assert.assertNotNull(totalDiferencas);
		
	}	
	
	@Test
	public void testarObterDiferencas() {
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
//	 OrdenacaoColuna DATA_LANCAMENTO_NUMERO_EDICAO
	@Test
	public void testarObterDiferencasOrdenacaoColunaDATALANCAMENTONUMEROEDICAO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.DATA_LANCAMENTO_NUMERO_EDICAO);
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
//	 OrdenacaoColuna DATA_LANCAMENTO
	@Test
	public void testarObterDiferencasOrdenacaoColunaDATALANCAMENTO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.DATA_LANCAMENTO);
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
//	 OrdenacaoColuna CODIGO_PRODUTO
	@Test
	public void testarObterDiferencasOrdenacaoColunaCODIGOPRODUTO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.CODIGO_PRODUTO);
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
//	 OrdenacaoColuna DESCRICAO_PRODUTO
	@Test
	public void testarObterDiferencasOrdenacaoColunaDESCRICAOPRODUTO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.DESCRICAO_PRODUTO);
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
//	 OrdenacaoColuna NUMERO_EDICAO
	@Test
	public void testarObterDiferencasOrdenacaoColunaNUMEROEDICAO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.NUMERO_EDICAO);
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
//	 OrdenacaoColuna PRECO_VENDA
	@Test
	public void testarObterDiferencasOrdenacaoColunaPRECOVENDA() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.PRECO_VENDA);
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
//	 OrdenacaoColuna PRECO_DESCONTO
	@Test
	public void testarObterDiferencasOrdenacaoColunaPRECODESCONTO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.PRECO_DESCONTO);
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
//	 OrdenacaoColuna TIPO_DIFERENCA
	@Test
	public void testarObterDiferencasOrdenacaoColunaTIPODIFERENCA() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.TIPO_DIFERENCA);
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
//	 OrdenacaoColuna NUMERO_NOTA_FISCAO
	@Test
	public void testarObterDiferencasOrdenacaoColunaNUMERONOTAFISCAL() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.NUMERO_NOTA_FISCAL);
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
//	 OrdenacaoColuna QUANTIDADE
	@Test
	public void testarObterDiferencasOrdenacaoColunaQUANTIDADE() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.QUANTIDADE);
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
//	 OrdenacaoColuna STATUS_APROVACAO
	@Test
	public void testarObterDiferencasOrdenacaoColunaSTATUSAPROVACAO() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.STATUS_APROVACAO);
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
//	 OrdenacaoColuna VALOR_TOTAL_DIFERENCA
	@Test
	public void testarObterDiferencasOrdenacaoColunaVALORTOTALDIFERENCA() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1,1,"asc");
		
		List<Diferenca> diferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.VALOR_TOTAL_DIFERENCA);
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		diferencas = diferencaEstoqueRepository.obterDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(diferencas);
		
	}
	
	@Test
	public void testarObterTotalDiferencas() {
		
		Long totalDiferencas;
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		
		Calendar d = Calendar.getInstance();
		Date dataLimiteLancamentoPesquisa = d.getTime();
		
		totalDiferencas = diferencaEstoqueRepository.obterTotalDiferencas(filtro, dataLimiteLancamentoPesquisa);
		
		Assert.assertNotNull(totalDiferencas);
		
	}
	
	@Test
	public void testarBuscarStatusDiferencaLancadaAutomaticamente() {
		
		boolean statusDiferencaLancada;
		
		Long idDiferenca = 1L;
		
		statusDiferencaLancada = diferencaEstoqueRepository.buscarStatusDiferencaLancadaAutomaticamente(idDiferenca);
		
		Assert.assertFalse(statusDiferencaLancada);
		
	}
	
	@Test
	public void testarObterValorFinanceiroPorTipoDiferenca() {
		
		BigDecimal valorFinanceiro;
		
		valorFinanceiro = diferencaEstoqueRepository.obterValorFinanceiroPorTipoDiferenca(TipoDiferenca.FALTA_EM);
		
	}
	
	@Test
	public void testarObterQuantidadeTotalDiferencas() {
		
		BigInteger quantidadeTotal;
		
		String codigoProduto = "123";
		Long numeroEdicao = 1L;
		
		Calendar d = Calendar.getInstance();
		Date dataMovimento = d.getTime();
		
		quantidadeTotal = diferencaEstoqueRepository.obterQuantidadeTotalDiferencas(codigoProduto, numeroEdicao, TipoEstoque.DANIFICADO, dataMovimento);
		
//		Assert.assertNull(quantidadeTotal);
		
	}
	
	@Test
	public void testarExisteDiferencaPorNota() {
		
		boolean diferencaPorNota;
		
		Long idProdutoEdicao = 1L;
		
		Calendar d = Calendar.getInstance();
		Date dataNotaEnvio = d.getTime();
		
		Integer numeroCota = 1;
		
		diferencaPorNota = diferencaEstoqueRepository.existeDiferencaPorNota(idProdutoEdicao, dataNotaEnvio, numeroCota);
		
		Assert.assertFalse(diferencaPorNota);
		
	}
	
	@Test
	public void testObterDiferencas() {
	    List<Diferenca> diferencas = diferencaEstoqueRepository.obterDiferencas(new Date());
	    Assert.assertNotNull(diferencas);
	    
	    Assert.assertEquals(qtdeMovimentosConsulta + qtdeMovimentosLancamento, diferencas.size());
	}
	
	@Test
    public void testObterDiferencasVazio() {
        List<Diferenca> diferencas = diferencaEstoqueRepository.obterDiferencas(DateUtil.adicionarDias(new Date(), -10));
        Assert.assertNotNull(diferencas);
        
        Assert.assertTrue(diferencas.isEmpty());
    }
	
	
	
	
	
}
