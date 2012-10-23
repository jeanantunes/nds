package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.CapaDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.ProdutoEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
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
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.util.Intervalo;

public class ChamadaEncalheRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private ChamadaEncalheRepository chamadaEncalheRepository;

	private ChamadaEncalhe chamadaEncalheVeja;
	private ChamadaEncalhe chamadaEncalheQuatroRodas;

	private ProdutoEdicao veja1;

	@Before
	public void setUpGeral() {
		TipoFornecedor tipoFornecedorPublicacao = Fixture
				.tipoFornecedorPublicacao();
		Fornecedor fornecedorFC = Fixture
				.fornecedorFC(tipoFornecedorPublicacao);
		Fornecedor fornecedorDinap = Fixture
				.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorFC, fornecedorDinap);

		NCM ncmRevistas = Fixture.ncm(49029000l, "REVISTAS", "KG");
		save(ncmRevistas);

		TipoProduto tipoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoRevista);

		Produto veja = Fixture.produtoVeja(tipoRevista);
		veja.addFornecedor(fornecedorDinap);

		Produto quatroRodas = Fixture.produtoQuatroRodas(tipoRevista);
		quatroRodas.addFornecedor(fornecedorDinap);

		save(veja, quatroRodas);

		veja1 = Fixture.produtoEdicao(1L, 10, 7, new Long(100),
				BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQ", 
				veja, null, false);

		ProdutoEdicao quatroRoda2 = Fixture.produtoEdicao(2L, 15, 30,
				new Long(100), BigDecimal.TEN, BigDecimal.TEN,
				"ABCDEFGHIJKNOPQ", quatroRodas, null, false);


		save(veja1, quatroRoda2);

		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);

		CFOP cfop = Fixture.cfop5102();
		save(cfop);

		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);

		NotaFiscalEntradaFornecedor notaFiscal1Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC,
						tipoNotaFiscal, usuario, BigDecimal.TEN,
						BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal1Veja);

		ItemNotaFiscalEntrada itemNotaFiscal1Veja = Fixture.itemNotaFiscal(
				veja1, usuario, notaFiscal1Veja,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				TipoLancamento.LANCAMENTO, BigInteger.valueOf(50));
		save(itemNotaFiscal1Veja);

		Date dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuario, dataRecebimento, dataRecebimento,
				StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico1Veja);

		ItemRecebimentoFisico itemRecebimentoFisico1Veja = Fixture
				.itemRecebimentoFisico(itemNotaFiscal1Veja,
						recebimentoFisico1Veja, BigInteger.valueOf(50));
		save(itemRecebimentoFisico1Veja);

		NotaFiscalEntradaFornecedor notaFiscal2Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC,
						tipoNotaFiscal, usuario, BigDecimal.TEN,
						BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal2Veja);

		ItemNotaFiscalEntrada itemNotaFiscal2Veja = Fixture.itemNotaFiscal(
				veja1, usuario, notaFiscal2Veja,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				TipoLancamento.LANCAMENTO, BigInteger.valueOf(50));

		save(itemNotaFiscal2Veja);

		RecebimentoFisico recebimentoFisico2Veja = Fixture.recebimentoFisico(
				notaFiscal2Veja, usuario, dataRecebimento, dataRecebimento,
				StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico2Veja);

		ItemRecebimentoFisico itemRecebimentoFisico2Veja = Fixture
				.itemRecebimentoFisico(itemNotaFiscal2Veja,
						recebimentoFisico2Veja, BigInteger.valueOf(50));
		save(itemRecebimentoFisico2Veja);

		Lancamento lancamentoVeja = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), new Date(),
				new Date(), BigInteger.valueOf(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO,
				itemRecebimentoFisico1Veja, 1);

		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);

		Estudo estudo = Fixture.estudo(BigInteger.valueOf(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), veja1);

		save(lancamentoVeja, estudo);

		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);

		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);

		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,
				box1);
		save(cotaManoel);

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				veja1, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);

		estoqueProdutoCota = Fixture.estoqueProdutoCota(quatroRoda2,
				cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);

		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);

		TipoMovimentoEstoque tipoMovimentoEnvioEncalhe = Fixture
				.tipoMovimentoEnvioEncalhe();
		save(tipoMovimentoEnvioEncalhe);

		chamadaEncalheVeja = Fixture.chamadaEncalhe(new Date(), veja1,
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		save(chamadaEncalheVeja);

		chamadaEncalheQuatroRodas = Fixture.chamadaEncalhe(new Date(),
				quatroRoda2, TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		save(chamadaEncalheQuatroRodas);
	}

	@SuppressWarnings("unused")
	@Test
	public void testObterPorNumeroEdicaoEDataRecolhimento() {
		
		Date dataDeRecolhimento = Fixture.criarData(19, Calendar.NOVEMBER, 2012);
		
		ChamadaEncalhe chamadaEncalhe = chamadaEncalheRepository
				.obterPorNumeroEdicaoEDataRecolhimento(veja1, dataDeRecolhimento, TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

	}
	
	@Test
	public void testObterChamadasEncalhePor() {

		Calendar dataOperacao = Calendar.getInstance();

		dataOperacao.set(2012, 5, 20);

		Long idCota = 1L;

		List<ChamadaEncalhe> listaChamadaEncalhes = this.chamadaEncalheRepository
				.obterChamadasEncalhePor(dataOperacao.getTime(), idCota);

		Assert.assertNotNull(listaChamadaEncalhes);
	}

	@Test
	public void testObterPorNumeroEdicaoEMaiorDataRecolhimento() {

		ChamadaEncalhe chamadaEncalhe = chamadaEncalheRepository
				.obterPorNumeroEdicaoEMaiorDataRecolhimento(veja1,
						TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		Assert.assertNotNull(chamadaEncalhe);
	}
		

	@Test
	public void obterDadosEmissaoChamadasEncalheFiltroNulo() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		filtro.setColunaOrdenacao("numCota");
		filtro.setOrdenacao("asc");
		
		List<CotaEmissaoDTO> lista = chamadaEncalheRepository
				.obterDadosEmissaoChamadasEncalhe(filtro);

		Assert.assertNotNull(lista);

	}
	
	@Test
	public void obterDadosEmissaoChamadasEncalheFiltroFornecedores() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		filtro.setColunaOrdenacao("numCota");
		filtro.setOrdenacao("asc");
		List<Long> fornecedores = new ArrayList<Long>();
		fornecedores.add(1L);
		filtro.setFornecedores(fornecedores);

		List<CotaEmissaoDTO> lista = chamadaEncalheRepository
				.obterDadosEmissaoChamadasEncalhe(filtro);

		Assert.assertNotNull(lista);

	}

	@Test
	public void obterDadosEmissaoChamadasEncalheIdBoxAte() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		filtro.setColunaOrdenacao("numCota");
		filtro.setOrdenacao("asc");
		filtro.setIdBoxAte(1);

		List<CotaEmissaoDTO> lista = chamadaEncalheRepository
				.obterDadosEmissaoChamadasEncalhe(filtro);

		Assert.assertNotNull(lista);

	}

	@Test
	public void obterDadosEmissaoChamadasEncalheIdBoxDe() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		filtro.setColunaOrdenacao("numCota");
		filtro.setOrdenacao("asc");
		filtro.setIdBoxDe(2);

		List<CotaEmissaoDTO> lista = chamadaEncalheRepository
				.obterDadosEmissaoChamadasEncalhe(filtro);

		Assert.assertNotNull(lista);

	}

	@Test
	public void obterDadosEmissaoChamadasEncalheIdRota() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		filtro.setColunaOrdenacao("numCota");
		filtro.setOrdenacao("asc");
		filtro.setIdRota(1L);

		List<CotaEmissaoDTO> lista = chamadaEncalheRepository
				.obterDadosEmissaoChamadasEncalhe(filtro);

		Assert.assertNotNull(lista);

	}

	@Test
	public void obterDadosEmissaoChamadasEncalheIdRoteiro() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		filtro.setColunaOrdenacao("numCota");
		filtro.setOrdenacao("asc");
		filtro.setIdRoteiro(1L);

		List<CotaEmissaoDTO> lista = chamadaEncalheRepository
				.obterDadosEmissaoChamadasEncalhe(filtro);

		Assert.assertNotNull(lista);

	}

	@Test
	public void obterDadosEmissaoChamadasEncalheNumCotaAte() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		filtro.setColunaOrdenacao("numCota");
		filtro.setOrdenacao("asc");
		filtro.setNumCotaAte(1);

		List<CotaEmissaoDTO> lista = chamadaEncalheRepository
				.obterDadosEmissaoChamadasEncalhe(filtro);

		Assert.assertNotNull(lista);

	}
	
	@Test
	public void obterDadosEmissaoChamadasEncalheNumCotaDe() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		filtro.setColunaOrdenacao("numCota");
		filtro.setOrdenacao("asc");
		filtro.setNumCotaAte(1);

		List<CotaEmissaoDTO> lista = chamadaEncalheRepository
				.obterDadosEmissaoChamadasEncalhe(filtro);

		Assert.assertNotNull(lista);

	}
	
	@Test
	public void obterDadosEmissaoChamadasEncalheDtRecolhimentoAte() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		filtro.setColunaOrdenacao("numCota");
		filtro.setOrdenacao("asc");
		filtro.setDtRecolhimentoAte(Fixture.criarData(19, Calendar.NOVEMBER, 2012));

		List<CotaEmissaoDTO> lista = chamadaEncalheRepository
				.obterDadosEmissaoChamadasEncalhe(filtro);

		Assert.assertNotNull(lista);

	}
	
	@Test
	public void obterDadosEmissaoChamadasEncalheDtRecolhimentoDe() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		filtro.setColunaOrdenacao("numCota");
		filtro.setOrdenacao("asc");
		filtro.setDtRecolhimentoAte(Fixture.criarData(9, Calendar.NOVEMBER, 2012));

		List<CotaEmissaoDTO> lista = chamadaEncalheRepository
				.obterDadosEmissaoChamadasEncalhe(filtro);

		Assert.assertNotNull(lista);

	}
	
	@Test
	public void obterDadosEmissaoImpressaoChamadasEncalheParametroLista() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		filtro.setColunaOrdenacao("numCota");
		filtro.setOrdenacao("asc");
		List<Long> fornecedores = new ArrayList<Long>();
		fornecedores.add(1L);
		filtro.setFornecedores(fornecedores);

		List<CotaEmissaoDTO> lista = chamadaEncalheRepository
				.obterDadosEmissaoImpressaoChamadasEncalhe(filtro);

		Assert.assertNotNull(lista);

	}
	
	@Test
	public void obterDadosEmissaoImpressaoChamadasEncalheParametroOutro() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		filtro.setColunaOrdenacao("numCota");
		filtro.setOrdenacao("asc");
		filtro.setNumCotaAte(1);

		List<CotaEmissaoDTO> lista = chamadaEncalheRepository
				.obterDadosEmissaoImpressaoChamadasEncalhe(filtro);

		Assert.assertNotNull(lista);

	}
	
	@Test
	public void obterIdsCapasChamadaEncalheDataDe() {
		
		Date dataDe = Fixture.criarData(19, Calendar.NOVEMBER, 2012);
			

		List<CapaDTO> lista = chamadaEncalheRepository
				.obterIdsCapasChamadaEncalhe(dataDe, null);

		Assert.assertNotNull(lista);
		
	}
	
	@Test
	public void obterIdsCapasChamadaEncalheDataAte() {
		
		Date dataAte = Fixture.criarData(29, Calendar.NOVEMBER, 2012);
			
		List<CapaDTO> lista = chamadaEncalheRepository
				.obterIdsCapasChamadaEncalhe(null, dataAte);

		Assert.assertNotNull(lista);
		
	}
	
	@Test
	public void obterProdutosEmissaoCEDtRecolhimentoDe() {

		FiltroEmissaoCE filtro = new FiltroEmissaoCE();
		
		filtro.setDtRecolhimentoDe(Fixture.criarData(19, Calendar.NOVEMBER, 2012));
		Long idcota = 1L; 
		

		List<ProdutoEmissaoDTO> lista = chamadaEncalheRepository
				.obterProdutosEmissaoCE(filtro, idcota);

		Assert.assertNotNull(lista);

	}
	
	@Test
	public void obterProximaDataEncalhe() {

		Date dataBase = Fixture.criarData(22, Calendar.OCTOBER, 2012);
		

		@SuppressWarnings("unused")
		Date dataEncalhe = chamadaEncalheRepository
				.obterProximaDataEncalhe(dataBase);


	}
}
