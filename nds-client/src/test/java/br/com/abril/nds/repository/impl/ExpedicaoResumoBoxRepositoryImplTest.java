package br.com.abril.nds.repository.impl;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ExpedicaoRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class ExpedicaoResumoBoxRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ExpedicaoRepository expedicaoRepository;
	
	private Date dataLancamento = new Date();

	private Box box1;
	
	@Before
	public void setup() {
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		Carteira carteira = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		save(carteira);
		
		Banco banco = Fixture.hsbc(carteira); 
		save(banco);
		
		
		ParametroCobrancaCota parametroCobranca = 
				Fixture.parametroCobrancaCota(null, 2, BigDecimal.TEN, null, 1, 
											  true, BigDecimal.TEN, null);
  		save(parametroCobranca);
		
		FormaCobranca formaBoleto =
			Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, banco,
										BigDecimal.ONE, BigDecimal.ONE,parametroCobranca);
		save(formaBoleto);
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1,"Assunto","Mensagem",true,FormaEmissao.INDIVIDUAL_BOX);
		
		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"33.333.333/0001-33", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);

		Distribuidor distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), politicasCobranca);
		save(distribuidor);
		
		politicaCobranca.setDistribuidor(distribuidor);
		save(politicaCobranca);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorDinap);
		
		box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		Box box2 = Fixture.criarBox(2, "BX-002", TipoBox.LANCAMENTO);
		save(box2);
		
		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
		save(cotaManoel);
		
		PessoaFisica jose = Fixture.pessoaFisica("123.456.789-01",
				"jose@mail.com", "Jose da Silva");
		save(jose);
		
		Cota cotaJose = Fixture.cota(1234, jose, SituacaoCadastro.ATIVO,box1);
		save(cotaJose);
		
		PessoaFisica ze = Fixture.pessoaFisica("123.456.789-02",
				"ze@mail.com", "Ze da Silva");
		save(ze);
		
		Cota cotaZe = Fixture.cota(12345, ze, SituacaoCadastro.ATIVO,box2);
		save(cotaZe);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.addFornecedor(fornecedorDinap);
		produtoVeja.setEditor(abril);
		save(produtoVeja);

		Produto produtoSuper = Fixture.produtoSuperInteressante(tipoProdutoRevista);
		produtoSuper.addFornecedor(fornecedorDinap);
		produtoSuper.setEditor(abril);
		save(produtoSuper);
		
		Produto produtoCapricho = Fixture.produtoCapricho(tipoProdutoRevista);
		produtoCapricho.addFornecedor(fornecedorDinap);
		produtoCapricho.setEditor(abril);
		save(produtoCapricho);
		
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao("1", 1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", 1L,
				produtoVeja, null, false);
		save(produtoEdicaoVeja1);
		
		ProdutoEdicao produtoEdicaoVeja2 = Fixture.produtoEdicao("1", 2L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPA", 2L,
				produtoVeja, null, false);
		save(produtoEdicaoVeja2);
		
		ProdutoEdicao produtoEdicaoSuper1 = Fixture.produtoEdicao("1", 1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPB", 3L,
				produtoSuper, null, false);
		save(produtoEdicaoSuper1);
		
		ProdutoEdicao produtoEdicaoCapricho1 = Fixture.produtoEdicao("1", 1L, 9, 14,
				new BigDecimal(0.15), new BigDecimal(9), new BigDecimal(13.5), "ABCDEFGHIJKLMNOPC", 4L,
				produtoCapricho, null, false);
		save(produtoEdicaoCapricho1);
		
		CFOP cfop5102 = Fixture.cfop5102();
		save(cfop5102);
		
		TipoMovimentoEstoque tipoMovimentoFaltaEm = Fixture.tipoMovimentoFaltaEm();
		TipoMovimentoEstoque tipoMovimentoFaltaDe = Fixture.tipoMovimentoFaltaDe();
		TipoMovimentoEstoque tipoMovimentoSobraEm = Fixture.tipoMovimentoSobraEm();
		TipoMovimentoEstoque tipoMovimentoSobraDe = Fixture.tipoMovimentoSobraDe();
		TipoMovimentoEstoque tipoMovimentoRecFisico = Fixture.tipoMovimentoRecebimentoFisico();
		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoFaltaEm, tipoMovimentoFaltaDe,
				tipoMovimentoSobraEm, tipoMovimentoSobraDe,
				tipoMovimentoRecFisico, tipoMovimentoRecReparte);
		
		TipoNotaFiscal tipoNotaFiscalRecebimento = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscalRecebimento);
		
		NotaFiscalEntradaFornecedor notaFiscalFornecedor = Fixture
				.notaFiscalEntradaFornecedor(cfop5102, fornecedorDinap.getJuridica(), fornecedorDinap, tipoNotaFiscalRecebimento,
						usuarioJoao, new BigDecimal(15), new BigDecimal(5), BigDecimal.TEN);
		save(notaFiscalFornecedor);

		ItemNotaFiscalEntrada itemNotaFiscalFornecedor = Fixture.itemNotaFiscal(produtoEdicaoVeja1,
				usuarioJoao, notaFiscalFornecedor, new Date(),new Date(),TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		save(itemNotaFiscalFornecedor);
		
		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
				notaFiscalFornecedor, usuarioJoao, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico);
			
		ItemRecebimentoFisico itemRecebimentoFisico = Fixture.itemRecebimentoFisico(itemNotaFiscalFornecedor, recebimentoFisico, BigDecimal.TEN);
		save(itemRecebimentoFisico);
		
		EstoqueProduto estoqueProdutoVeja1 = Fixture.estoqueProduto(produtoEdicaoVeja1, BigDecimal.ZERO);
		save(estoqueProdutoVeja1);
		
		MovimentoEstoque movimentoRecFisicoVeja1 =
			Fixture.movimentoEstoque(itemRecebimentoFisico, produtoEdicaoVeja1, tipoMovimentoRecFisico, usuarioJoao,
				estoqueProdutoVeja1, new Date(), new BigDecimal(1),
				StatusAprovacao.APROVADO, "Aprovado");

		save(movimentoRecFisicoVeja1);
		update(estoqueProdutoVeja1);
		
		
		Expedicao expedicao1 = Fixture.expedicao(usuarioJoao,Fixture.criarData(1, 3, 2010));
		save(expedicao1);
		
		Lancamento lancamentoVeja1 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja1.getPeb()), new Date(),
						new Date(), BigDecimal.TEN, StatusLancamento.EXPEDIDO,
						itemRecebimentoFisico,expedicao1, 1);

		save(lancamentoVeja1);
		
		Expedicao expedicao2 = Fixture.expedicao(usuarioJoao,Fixture.criarData(1, 3, 2010));
		save(expedicao2);
		
		Lancamento lancamentoVeja2 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja2,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja2.getPeb()), new Date(),
						new Date(), BigDecimal.TEN, StatusLancamento.EXPEDIDO,
						null,expedicao2, 1);
		save(lancamentoVeja2);

		Expedicao expedicao3 = Fixture.expedicao(usuarioJoao,Fixture.criarData(1, 3, 2010));
		save(expedicao3);
		
		Lancamento lancamentoSuper1 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoSuper1.getPeb()), new Date(),
								new Date(), new BigDecimal(100), StatusLancamento.EXPEDIDO,
								null,expedicao3, 1);

		save(lancamentoSuper1);
		
		Expedicao expedicao4 = Fixture.expedicao(usuarioJoao,Fixture.criarData(1, 3, 2010));
		save(expedicao4);
		
		Lancamento lancamentoCapricho1 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCapricho1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCapricho1.getPeb()), new Date(),
								new Date(), new BigDecimal(1000), StatusLancamento.EXPEDIDO,
								null,expedicao4, 1);

		save(lancamentoCapricho1);
		
		Estudo estudoVeja1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoVeja1.getDataLancamentoDistribuidor(), produtoEdicaoVeja1);
		save(estudoVeja1);
		
		Estudo estudoVeja2 = Fixture
				.estudo(BigDecimal.TEN, lancamentoVeja2.getDataLancamentoDistribuidor(), produtoEdicaoVeja2);
		save(estudoVeja2);
		
		Estudo estudoSuper1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoSuper1.getDataLancamentoDistribuidor(), produtoEdicaoSuper1);
		save(estudoSuper1);
		
		Estudo estudoCapricho1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoCapricho1.getDataLancamentoDistribuidor(), produtoEdicaoCapricho1);
		save(estudoCapricho1);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		
		EstudoCota estudoCotaVeja1Joao = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja1, cotaJose);
		save(estudoCotaVeja1Joao);
		
		EstudoCota estudoCotaVeja2Joao = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja2, cotaJose);
		save(estudoCotaVeja2Joao);
		
		EstudoCota estudoCotaCaprichoZe = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoCapricho1, cotaZe);
		save(estudoCotaCaprichoZe);
		
		EstudoCota estudoCotaSuper1Manoel = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoSuper1, cotaManoel);
		save(estudoCotaSuper1Manoel);

		
		MovimentoEstoque movimentoEstoqueDiferenca =
				Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoRecFisico, usuarioJoao,
										 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
										 StatusAprovacao.APROVADO, "motivo");	
		save(movimentoEstoqueDiferenca);
		
		MovimentoEstoque movimentoEstoqueDiferenca2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(2),
									 StatusAprovacao.APROVADO, "motivo");
		save(movimentoEstoqueDiferenca2);
		
		MovimentoEstoque movimentoEstoqueDiferenca3 =
			Fixture.movimentoEstoque(null, produtoEdicaoSuper1, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(3),
									 StatusAprovacao.APROVADO, "motivo");
		save(movimentoEstoqueDiferenca3);
		
		MovimentoEstoque movimentoEstoqueDiferenca4 =
			Fixture.movimentoEstoque(null, produtoEdicaoCapricho1, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(4),
									 StatusAprovacao.APROVADO, "motivo");
		save(movimentoEstoqueDiferenca4);
		
		
		

		Diferenca diferenca =
			Fixture.diferenca(new BigDecimal(1), usuarioJoao, produtoEdicaoVeja1, TipoDiferenca.FALTA_EM,
							  StatusConfirmacao.CONFIRMADO, null, movimentoEstoqueDiferenca, true);
		save(diferenca);
		
		Diferenca diferenca2 =
			Fixture.diferenca(new BigDecimal(2), usuarioJoao, produtoEdicaoVeja2, TipoDiferenca.FALTA_DE,
							  StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico, movimentoEstoqueDiferenca2, true);
		save(diferenca2);
		
		Diferenca diferenca3 =
			Fixture.diferenca(new BigDecimal(3), usuarioJoao, produtoEdicaoSuper1, TipoDiferenca.SOBRA_EM,
							  StatusConfirmacao.CONFIRMADO, null, movimentoEstoqueDiferenca3, true);
		save(diferenca3);
		
		Diferenca diferenca4 =
			Fixture.diferenca(new BigDecimal(4), usuarioJoao, produtoEdicaoCapricho1, TipoDiferenca.SOBRA_DE,
					          StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico, movimentoEstoqueDiferenca4, true);
		save(diferenca4);
						
		gerarCargaDiferencaEstoque(
			50, produtoEdicaoVeja1, tipoMovimentoFaltaEm, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.FALTA_EM);
		
		gerarCargaDiferencaEstoque(
			 50, produtoEdicaoVeja2, tipoMovimentoFaltaDe, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.FALTA_DE);
		
		gerarCargaDiferencaEstoque(
			 50, produtoEdicaoSuper1, tipoMovimentoSobraDe, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.SOBRA_DE);
		
		gerarCargaDiferencaEstoque(
			 50, produtoEdicaoCapricho1, tipoMovimentoSobraEm, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.SOBRA_EM);
		
		
		RateioDiferenca rateioDiferencaCotaManoel = Fixture.rateioDiferenca(new BigDecimal(10), cotaManoel, diferenca3, estudoCotaSuper1Manoel, new Date());
		save(rateioDiferencaCotaManoel);
		
		RateioDiferenca rateioDiferencaJose = Fixture.rateioDiferenca(new BigDecimal(10),cotaJose, diferenca, estudoCotaVeja1Joao, new Date());
		save(rateioDiferencaJose);
	}

	
	@Test
	public void consultarResumoExpedicaoPorBox(){
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setDataLancamento(dataLancamento);
		filtro.setPaginacao(getPaginacaoVO(1, 10, Ordenacao.DESC));
		
		List<ExpedicaoDTO> lista = expedicaoRepository.obterResumoExpedicaoPorBox(filtro);
		
		Assert.assertNotNull(lista);
		
		Assert.assertTrue(!lista.isEmpty());
	
	}
	
	@Test
	public void consultarQuantidadeResumoExpedicaoPorBox() {
		Long quantidade = expedicaoRepository
				.obterQuantidadeResumoExpedicaoPorBox(box1.getId(),
						dataLancamento);

		Assert.assertNotNull(quantidade);

		Assert.assertTrue(quantidade != 0);
	}
	
	/**
	 * Retorna um objeto com  valores de paginação.
	 * @param paginaAtual
	 * @param resultadoPorPagina
	 * @param ordenacao
	 * @return PaginacaoVO
	 */
	private PaginacaoVO getPaginacaoVO(int paginaAtual, int resultadoPorPagina, Ordenacao ordenacao){
		
		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setOrdenacao(ordenacao);
		paginacao.setPaginaAtual(paginaAtual);
		paginacao.setQtdResultadosPorPagina(resultadoPorPagina);
		
		return paginacao;
	}
	
	/**
	 * Gera carga de diferença de estoque
	 * @param quantidadeRegistros
	 * @param produtoEdicao
	 * @param tipoMovimento
	 * @param usuario
	 * @param estoqueProduto
	 * @param tipoDiferenca
	 */
	private void gerarCargaDiferencaEstoque(int quantidadeRegistros,
											ProdutoEdicao produtoEdicao, 
											TipoMovimentoEstoque tipoMovimento, 
											Usuario usuario,
											EstoqueProduto estoqueProduto,
											TipoDiferenca tipoDiferenca) {

		for (int i = 1; i <= quantidadeRegistros; i++) {
		
			MovimentoEstoque movimentoEstoqueDiferenca = 
			Fixture.movimentoEstoque(
			null, produtoEdicao, tipoMovimento, usuario, estoqueProduto, new Date(), new BigDecimal(i), StatusAprovacao.APROVADO, "motivo");
			
			save(movimentoEstoqueDiferenca);
			
			Diferenca diferenca = 
			Fixture.diferenca(
			new BigDecimal(i), usuario, produtoEdicao, tipoDiferenca, 
			StatusConfirmacao.CONFIRMADO, null, movimentoEstoqueDiferenca, true);
			
			save(diferenca);
		}
	}
	
	//-----------------------------------------------------
	
	
	@Test 
	public void consultarResumoExpedicaoProdutosDoBox(){
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setDataLancamento(dataLancamento);
		filtro.setCodigoBox(1);
		filtro.setPaginacao(getPaginacaoVO(1, 10, Ordenacao.DESC));
		
		List<ExpedicaoDTO> lista = expedicaoRepository.obterResumoExpedicaoProdutosDoBox(filtro);
		
		Assert.assertNotNull(lista);
		
		Assert.assertTrue(!lista.isEmpty());
	
	}
		
	@Test
	public void consultarQuantidadeResumoExpedicaoProdutosDoBox(){
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		filtro.setDataLancamento(dataLancamento);
		filtro.setCodigoBox(1);
		filtro.setPaginacao(getPaginacaoVO(1, 10, Ordenacao.DESC));
		
		Long quantidade =  expedicaoRepository.obterQuantidadeResumoExpedicaoProdutosDoBox(filtro);
		
		Assert.assertNotNull(quantidade);
		
		Assert.assertTrue(quantidade != 0);
	}
	
	
	
}
