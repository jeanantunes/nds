package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class LancamentoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	private Lancamento lancamentoVeja;
	private Lancamento lancamentoQuatroRodas;
	private Lancamento lancamentoInfoExame;
	private Lancamento lancamentoCapricho;
	private Lancamento lancamentoCromoReiLeao;
	
	private ProdutoEdicao veja1;
	
    private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;
	private TipoProduto tipoCromo;
	private TipoFornecedor tipoFornecedorPublicacao;
	
	private ChamadaEncalhe chamadaEncalhe;
	private ChamadaEncalhe chamadaEncalhe1;
	private ChamadaEncalhe chamadaEncalhe2;

	@Before
	public void setUp() {
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorFC, fornecedorDinap);

		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		NCM ncmCromo = Fixture.ncm(48205000l,"CROMO","KG");
		save(ncmCromo);
		
		TipoProduto tipoRevista = Fixture.tipoRevista(ncmRevistas);
		tipoCromo = Fixture.tipoCromo(ncmCromo);
		save(tipoRevista, tipoCromo);
		
		Produto veja = Fixture.produtoVeja(tipoRevista);
		veja.setEditor(abril);
		veja.addFornecedor(fornecedorDinap);

		Produto quatroRodas = Fixture.produtoQuatroRodas(tipoRevista);
		quatroRodas.setEditor(abril);
		quatroRodas.addFornecedor(fornecedorDinap);

		Produto infoExame = Fixture.produtoInfoExame(tipoRevista);
		infoExame.setEditor(abril);
		infoExame.addFornecedor(fornecedorDinap);

		Produto capricho = Fixture.produtoCapricho(tipoRevista);
		capricho.setEditor(abril);
		capricho.addFornecedor(fornecedorDinap);
		save(veja, quatroRodas, infoExame, capricho);
		
		Produto cromoReiLeao = Fixture.produtoCromoReiLeao(tipoCromo);
		cromoReiLeao.setEditor(abril);
		cromoReiLeao.addFornecedor(fornecedorDinap);
		save(cromoReiLeao);

		veja1 = Fixture.produtoEdicao("1", 1L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQ", 1L, veja, null, false);

		ProdutoEdicao quatroRoda2 = Fixture.produtoEdicao("1", 2L, 15, 30,
				new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPA", 2L,
				quatroRodas, null, false);

		ProdutoEdicao infoExame3 = Fixture.produtoEdicao("1", 3L, 5, 30,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(12), "ABCDEFGHIJKLMNOPC", 3L, infoExame, null, false);

		ProdutoEdicao capricho1 = Fixture.produtoEdicao("1", 1L, 10, 15,
				new BigDecimal(0.12), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPD", 4L, capricho, null, false);
		
		ProdutoEdicao cromoReiLeao1 = Fixture.produtoEdicao("1", 1L, 100, 60,
				new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), "ABCDEFGHIJKLMNOPE", 5L, cromoReiLeao, null, false);
		
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
		
		lancamentoVeja = Fixture.lancamento(TipoLancamento.SUPLEMENTAR, veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisico1Veja, 1);
		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);
		
		lancamentoQuatroRodas = Fixture.lancamento(TipoLancamento.LANCAMENTO, quatroRoda2,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.MARCH, 2012),
				new Date(),
				new Date(),
				new BigDecimal(25),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisico4Rodas, 1);
		
		lancamentoInfoExame = Fixture.lancamento(TipoLancamento.LANCAMENTO, infoExame3,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(23, Calendar.MARCH, 2012), 
				new Date(),
				new Date(),
				new BigDecimal(40),
				StatusLancamento.CONFIRMADO, null, 1);
		
		lancamentoCapricho = Fixture.lancamento(TipoLancamento.LANCAMENTO, capricho1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(12, Calendar.MARCH, 2012),
				new Date(),
				new Date(),
				BigDecimal.TEN,
				StatusLancamento.CONFIRMADO, null, 1);
		
		lancamentoCromoReiLeao = Fixture.lancamento(TipoLancamento.LANCAMENTO, cromoReiLeao1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(23, Calendar.APRIL, 2012),
				new Date(),
				new Date(),
				new BigDecimal(10000),
				StatusLancamento.CONFIRMADO, null, 1);
		
		Estudo estudo = Fixture.estudo(new BigDecimal(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), veja1);
		
		save(lancamentoVeja, lancamentoQuatroRodas, lancamentoInfoExame,
				lancamentoCapricho, lancamentoCromoReiLeao, estudo);
		
		Calendar calendar = Calendar.getInstance();
		
		chamadaEncalhe = new ChamadaEncalhe();
		
		chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		chamadaEncalhe.setDataRecolhimento(calendar.getTime());
		
		chamadaEncalhe.setProdutoEdicao(veja1);
		
		save(chamadaEncalhe);

		chamadaEncalhe1 = new ChamadaEncalhe();
		
		chamadaEncalhe1.setTipoChamadaEncalhe(TipoChamadaEncalhe.ANTECIPADA);
		
		calendar.add(Calendar.DAY_OF_MONTH, 5);
		
		chamadaEncalhe1.setDataRecolhimento(calendar.getTime());
		
		chamadaEncalhe1.setProdutoEdicao(capricho1);
		
		save(chamadaEncalhe1);

		chamadaEncalhe2 = new ChamadaEncalhe();
		
		chamadaEncalhe2.setTipoChamadaEncalhe(TipoChamadaEncalhe.CHAMADAO);
		
		calendar.add(Calendar.DAY_OF_MONTH, 5);
		
		chamadaEncalhe2.setDataRecolhimento(calendar.getTime());
		
		chamadaEncalhe2.setProdutoEdicao(veja1);
		
		save(chamadaEncalhe2);
		
		Box box = Fixture.boxReparte300();
		
		Cota cota = Fixture.cota(50, fornecedorDinap.getJuridica(), SituacaoCadastro.ATIVO, box);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(capricho1, BigDecimal.TEN, cota, null);
		
		save(box, cota, estudo, estoqueProdutoCota);

		LancamentoParcial lancamentoParcial = Fixture.criarLancamentoParcial(capricho1,
																			 lancamentoCapricho.getDataLancamentoPrevista(), 
																			 lancamentoCapricho.getDataRecolhimentoPrevista(),
																			 StatusLancamentoParcial.PROJETADO);

		PeriodoLancamentoParcial parcial = Fixture.criarPeriodoLancamentoParcial(
				lancamentoCapricho, 
				lancamentoParcial,  
				StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.FINAL);

		EstudoCota estudoCota = Fixture.estudoCota(BigDecimal.TEN, BigDecimal.ONE, estudo, cota);

		save(lancamentoParcial, parcial, estudoCota);
		
		Cota cota2 = Fixture.cota(55, fornecedorFC.getJuridica(), SituacaoCadastro.ATIVO, box);
		
		EstoqueProdutoCota estoqueProdutoCota2 = Fixture.estoqueProdutoCota(infoExame3, BigDecimal.TEN, cota2, null);
		
		save(cota2, estoqueProdutoCota2);
		
		LancamentoParcial lancamentoParcial2 = Fixture.criarLancamentoParcial(infoExame3,
																			  lancamentoInfoExame.getDataLancamentoPrevista(), 
																			  lancamentoInfoExame.getDataRecolhimentoPrevista(),
																			  StatusLancamentoParcial.PROJETADO);

		PeriodoLancamentoParcial parcial2 = Fixture.criarPeriodoLancamentoParcial(
				lancamentoInfoExame, 
				lancamentoParcial2, 
				StatusLancamentoParcial.RECOLHIDO, TipoLancamentoParcial.PARCIAL);
		
		Estudo estudo2 = Fixture.estudo(new BigDecimal(180),
				Fixture.criarData(12, Calendar.MARCH, 2012), infoExame3);
		
		EstudoCota estudoCota2 = Fixture.estudoCota(BigDecimal.TEN, BigDecimal.ONE, estudo2, cota2);
		
		save(estudo2, lancamentoParcial2, parcial2, estudoCota2);
	}

	@Test
	public void totalLancamentosBalanceamentoMatriz() {
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		SumarioLancamentosDTO sumario = lancamentoRepository
				.sumarioBalanceamentoMatrizLancamentos(data,
						Collections.singletonList(fornecedorDinap.getId()));
		Assert.assertNotNull(sumario);
		Assert.assertEquals(Long.valueOf(3), sumario.getTotalLancamentos());
		Assert.assertEquals(CurrencyUtil.formatarValor(new BigDecimal(2230)),
				CurrencyUtil.formatarValor(sumario.getValorTotalLancamentos()));
	}
	
	@Test
	public void totalLancamentosBalanceamentoMatrizNenhum() {
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		SumarioLancamentosDTO sumario = lancamentoRepository
				.sumarioBalanceamentoMatrizLancamentos(data,
						Collections.singletonList(fornecedorFC.getId()));
		Assert.assertNotNull(sumario);
		Assert.assertEquals(Long.valueOf(0), sumario.getTotalLancamentos());
		Assert.assertNull(sumario.getValorTotalLancamentos());
	}
	
	@Test
	public void buscarResumosPeriodo() {
		
		Date data22022012 = Fixture.criarData(22,
				Calendar.FEBRUARY, 2012);
		Date data23022012 = Fixture.criarData(23,
				Calendar.FEBRUARY, 2012);
		List<Date> datas = Arrays.asList(data22022012, data23022012);
		List<ResumoPeriodoBalanceamentoDTO> resumos = lancamentoRepository
				.buscarResumosPeriodo(datas,
						Collections.singletonList(fornecedorDinap.getId()), GrupoProduto.CROMO);
		Assert.assertEquals(2, resumos.size());
		
		ResumoPeriodoBalanceamentoDTO resumo2202 = resumos.get(0);
		Assert.assertNotNull(resumo2202);
		Assert.assertEquals(data22022012, resumo2202.getData());
		Assert.assertEquals(Long.valueOf(3), resumo2202.getQtdeTitulos());
		Assert.assertEquals(CurrencyUtil.formatarValor(new BigDecimal(165.00)),
				CurrencyUtil.formatarValor(resumo2202.getQtdeExemplares()));
		Assert.assertEquals(CurrencyUtil.formatarValor(new BigDecimal(16.5)),
				CurrencyUtil.formatarValor(resumo2202.getPesoTotal()));
		
		ResumoPeriodoBalanceamentoDTO resumo2302 = resumos.get(1);
		Assert.assertNotNull(resumo2302);
		Assert.assertEquals(data23022012, resumo2302.getData());
		Assert.assertEquals(Long.valueOf(2), resumo2302.getQtdeTitulos());
		Assert.assertEquals(CurrencyUtil.formatarValor(new BigDecimal(110.00)),
				CurrencyUtil.formatarValor(resumo2302.getQtdeExemplares()));
		Assert.assertEquals(CurrencyUtil.formatarValor(new BigDecimal(101.20)),
				CurrencyUtil.formatarValor(resumo2302.getPesoTotal()));
	}
	
	@Test
	public void obterUltimoLancamentoDaEdicao() {
	
		Lancamento lancamento = lancamentoRepository.obterUltimoLancamentoDaEdicao(veja1.getId());
		
		Assert.assertEquals(lancamento.getId(),lancamentoVeja.getId());
		Assert.assertEquals(lancamento.getDataLancamentoDistribuidor(),lancamentoVeja.getDataLancamentoDistribuidor());
	}
	
	@Test
	public void obterLancamentosPorId() {
		
		Set<Long> idsLancamento = new TreeSet<Long>();
		
		idsLancamento.add(lancamentoVeja.getId());
		idsLancamento.add(lancamentoInfoExame.getId());
		idsLancamento.add(lancamentoQuatroRodas.getId());
		
		List<Lancamento> listaLancamento =
			lancamentoRepository.obterLancamentosPorIdOrdenados(idsLancamento);
		
		Assert.assertNotNull(listaLancamento);
		
		Assert.assertTrue(listaLancamento.size() == idsLancamento.size());
	}

	@Test
	public void quantidadeLancamentoInformeRecolhimento() {
		Long quantidade = lancamentoRepository.quantidadeLancamentoInformeRecolhimento(
				null, Calendar.getInstance(), Calendar.getInstance());
	}

	@Test
	public void obterLancamentoInformeRecolhimento() {
		List<InformeEncalheDTO>  result =  lancamentoRepository.obterLancamentoInformeRecolhimento(
				1L, Calendar.getInstance(), Calendar.getInstance(),
				"precoDesconto", Ordenacao.ASC, 0, 15);
	}
	
	
	
	
}
