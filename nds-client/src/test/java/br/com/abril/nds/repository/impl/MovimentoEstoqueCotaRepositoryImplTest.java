package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.ControleContagemDevolucao;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
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
	
	
	private Lancamento lancamentoVeja;
	
    private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;
	private TipoProduto tipoCromo;
	private TipoFornecedor tipoFornecedorPublicacao;

	@Before
	public void setUp() {
		
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorFC, fornecedorDinap);

		TipoProduto tipoRevista = Fixture.tipoRevista();
		tipoCromo = Fixture.tipoCromo();
		save(tipoRevista, tipoCromo);
		
		Produto veja = Fixture.produtoVeja(tipoRevista);
		veja.addFornecedor(fornecedorDinap);

		Produto quatroRodas = Fixture.produtoQuatroRodas(tipoRevista);
		quatroRodas.addFornecedor(fornecedorDinap);

		Produto infoExame = Fixture.produtoInfoExame(tipoRevista);
		infoExame.addFornecedor(fornecedorDinap);

		Produto capricho = Fixture.produtoCapricho(tipoRevista);
		capricho.addFornecedor(fornecedorDinap);
		save(veja, quatroRodas, infoExame, capricho);
		
		Produto cromoReiLeao = Fixture.produtoCromoReiLeao(tipoCromo);
		cromoReiLeao.addFornecedor(fornecedorDinap);
		save(cromoReiLeao);

		ProdutoEdicao veja1 = Fixture.produtoEdicao(1L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), veja);

		ProdutoEdicao quatroRoda2 = Fixture.produtoEdicao(2L, 15, 30,
				new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN,
				quatroRodas);

		ProdutoEdicao infoExame3 = Fixture.produtoEdicao(3L, 5, 30,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(12), infoExame);

		ProdutoEdicao capricho1 = Fixture.produtoEdicao(1L, 10, 15,
				new BigDecimal(0.12), BigDecimal.TEN, BigDecimal.TEN, capricho);
		
		ProdutoEdicao cromoReiLeao1 = Fixture.produtoEdicao(1L, 100, 60,
				new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), cromoReiLeao);
		
		save(veja1, quatroRoda2, infoExame3, capricho1, cromoReiLeao1);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		CFOP cfop = Fixture.cfop5102();
		save(cfop);
		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
		
		NotaFiscalEntradaFornecedor notaFiscal1Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal1Veja);

		ItemNotaFiscalEntrada itemNotaFiscal1Veja = Fixture.itemNotaFiscal(veja1, usuario,
				notaFiscal1Veja, 
				Fixture.criarData(22, Calendar.FEBRUARY,2012),
				Fixture.criarData(22, Calendar.FEBRUARY,2012),
				TipoLancamento.LANCAMENTO,
						new BigDecimal(50));
		save(itemNotaFiscal1Veja);
		
		Date dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico1Veja);
			
		ItemRecebimentoFisico itemRecebimentoFisico1Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal1Veja, recebimentoFisico1Veja, new BigDecimal(50));
		save(itemRecebimentoFisico1Veja);
		
		
		NotaFiscalEntradaFornecedor notaFiscal2Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal2Veja);

		ItemNotaFiscalEntrada itemNotaFiscal2Veja = Fixture.itemNotaFiscal(
				veja1, 
				usuario,
				notaFiscal2Veja, 
				Fixture.criarData(22, Calendar.FEBRUARY,2012), 
				Fixture.criarData(22, Calendar.FEBRUARY,2012),
				TipoLancamento.LANCAMENTO,
				new BigDecimal(50));
		
		save(itemNotaFiscal2Veja);

		RecebimentoFisico recebimentoFisico2Veja = Fixture.recebimentoFisico(
				notaFiscal2Veja, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico2Veja);
			
		ItemRecebimentoFisico itemRecebimentoFisico2Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal2Veja, recebimentoFisico2Veja, new BigDecimal(50));
		save(itemRecebimentoFisico2Veja);
		
		
		NotaFiscalEntradaFornecedor notaFiscal4Rodas= Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal4Rodas);

		ItemNotaFiscalEntrada itemNotaFiscal4Rodas = 
		
				Fixture.itemNotaFiscal(
						quatroRoda2, 
						usuario,
						notaFiscal4Rodas, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012), 
						Fixture.criarData(22, Calendar.FEBRUARY,2012), 
						TipoLancamento.LANCAMENTO,
						new BigDecimal(25));
		
		save(itemNotaFiscal4Rodas);
		
		RecebimentoFisico recebimentoFisico4Rodas = Fixture.recebimentoFisico(
				notaFiscal4Rodas, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico4Rodas);
			
		ItemRecebimentoFisico itemRecebimentoFisico4Rodas = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal4Rodas, recebimentoFisico4Rodas, new BigDecimal(25));
		save(itemRecebimentoFisico4Rodas);
		
		lancamentoVeja = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, 
				veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.PLANEJADO, itemRecebimentoFisico1Veja);
		
		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);
		
		
		Estudo estudo = Fixture.estudo(new BigDecimal(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), veja1);

		save(lancamentoVeja, estudo);

		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);
		
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(box1);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				veja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(estoqueProdutoCota);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		TipoMovimentoEstoque tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
		save(tipoMovimentoEnvioEncalhe);

		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				veja1,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCota,
				new BigDecimal(12), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mec);
		

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				new BigDecimal(25), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mec);
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				new BigDecimal(14), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mec);
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				new BigDecimal(19), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mec);
		
		ControleContagemDevolucao controleContagemDevolucao = Fixture.controleContagemDevolucao(
				StatusOperacao.EM_ANDAMENTO, 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012));

		save(controleContagemDevolucao);
		
	}
	

	@Test
	public void testarTudo() {
		
		testarObterListaContagemDevolucaoComQtdMovimentoParcial();
		
		testarObterQuantidadeContagemDevolucao();
		
		testarObterListaContagemDevolucao();
		
		testarObterValorTotal();
		
	}

	
	
	private void testarObterListaContagemDevolucaoComQtdMovimentoParcial() {
		
		List<ContagemDevolucaoDTO> retorno = 
				
				movimentoEstoqueCotaRepository.obterListaContagemDevolucao(
				obterFiltro(), 
				obterTipoMovimento(),
				true);
		
		
		Assert.assertTrue(retorno.size() == 1);
		
		ContagemDevolucaoDTO contagem = retorno.get(0);
		
		Assert.assertTrue(contagem.getQtdDevolucao().intValue() == 70);
		
	}
	
	private void testarObterValorTotal() {
		
		
		@SuppressWarnings("unused")
		BigDecimal total = movimentoEstoqueCotaRepository.obterValorTotalGeralContagemDevolucao(
				obterFiltro(), 
				obterTipoMovimento());
		
		Assert.assertTrue(total.intValue() == 1050);
		
	}
	
	private void testarObterListaContagemDevolucao() {
		
		List<ContagemDevolucaoDTO> listaContagemDevolucao = movimentoEstoqueCotaRepository.obterListaContagemDevolucao(
				obterFiltro(), 
				obterTipoMovimento(),
				false);
	}

	private void testarObterQuantidadeContagemDevolucao() {
		
		movimentoEstoqueCotaRepository.obterQuantidadeContagemDevolucao(
				obterFiltro());
	}
	
	private FiltroDigitacaoContagemDevolucaoDTO obterFiltro() {
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		
		PaginacaoVO paginacao = new PaginacaoVO();

		paginacao.setOrdenacao(Ordenacao.ASC);
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(500);

		filtro.setPaginacao(paginacao);

		Date dataInicial = Fixture.criarData(27, Calendar.FEBRUARY, 2012);
		Date dataFinal = Fixture.criarData(1, Calendar.MARCH, 2012);
		
	
		PeriodoVO periodo = new PeriodoVO();
		periodo.setDataInicial(dataInicial);
		periodo.setDataFinal(dataFinal);
		filtro.setPeriodo(periodo);
		
		filtro.setOrdenacaoColuna(OrdenacaoColuna.CODIGO_PRODUTO);
		
		filtro.setIdFornecedor(fornecedorDinap.getId());
		
		return filtro;
		
	}
	
	
	private TipoMovimentoEstoque obterTipoMovimento() {
		
		TipoMovimentoEstoque tipoMovimentoEstoque = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		return tipoMovimentoEstoque;
		
	}
	
	
}
