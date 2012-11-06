package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO.ColunaOrdenacao;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.BaixaManual;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.CobrancaDeposito;
import br.com.abril.nds.model.financeiro.CobrancaDinheiro;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.HistoricoAcumuloDivida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.util.TipoBaixaCobranca;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class DividaRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private DividaRepository dividaRepository;
	
	ConsolidadoFinanceiroCota consolidado;

    private FormaCobranca formaBoleto;

    private FormaCobranca formaDinheiro;

    private FormaCobranca formaDeposito;

    private Distribuidor distribuidor;

    private Cota cotaManoel;

    private Box box1;

    private Cota cotaJose;

    private Cota cotaMaria;

    private TipoMovimentoFinanceiro tipoMovimentoFinanceitoDebito;

    private Usuario usuarioJoao;

    private Banco bancoHSBC;

    private Cota cotaOrlando;

    private Cota cotaLuis;

    private Cota cotaMariana;
	
	@Before
	public void setUp() {
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		bancoHSBC = Fixture.banco(10L, true, 30, "1010",
				  123456L, "1", "1", "Instrucoes.", "HSBC","BANCO HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		save(bancoHSBC);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56.003.315/0001-47", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);

		distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), null);
		save(distribuidor);
		
		formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
				  												BigDecimal.ONE, BigDecimal.ONE,null);

		formaDinheiro = Fixture.formaCobrancaDinheiro(true, BigDecimal.ZERO, true, null, BigDecimal.ONE, BigDecimal.TEN, null);
		
		formaDeposito = Fixture.formaCobrancaDeposito(true, BigDecimal.ZERO, true, bancoHSBC, BigDecimal.ONE, BigDecimal.TEN, null);
		
		save(formaBoleto, formaDinheiro, formaDeposito);

		PoliticaCobranca politicaCobrancaBoleto =
				Fixture.criarPoliticaCobranca(distribuidor, formaBoleto, true, true, true, 1,"Assunto","Mensagem",true,FormaEmissao.INDIVIDUAL_BOX);
		
		save(politicaCobrancaBoleto);
		
		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobrancaBoleto);
		politicaCobrancaBoleto.setDistribuidor(distribuidor);
		
		distribuidor.setPoliticasCobranca(politicasCobranca);
		
		usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		PessoaFisica manoel = Fixture.pessoaFisica("319.435.088-95",
		        "sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);

		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
		save(cotaManoel);
		
		PDV pdv = Fixture.criarPDVPrincipal("Manoel", cotaManoel);
		save(pdv);
		
		Set<FormaCobranca> formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaBoleto);
		ParametroCobrancaCota parametroCobrancaConta = 
				Fixture.parametroCobrancaCota(formasCobranca, 1, BigDecimal.TEN, cotaManoel, 1, 
											  true, BigDecimal.TEN, null);
		formaBoleto.setParametroCobrancaCota(parametroCobrancaConta);
		formaBoleto.setPrincipal(true);
		
		save(parametroCobrancaConta);
		
		Roteirizacao roteirizacao = Fixture.criarRoteirizacao(box1);
		save(roteirizacao);
		
		Roteiro roteiro = Fixture.criarRoteiro("Pinheiros",roteirizacao,TipoRoteiro.NORMAL);
		save(roteiro);

		Rota rota = Fixture.rota("Rota 005",roteiro);
		rota.addPDV(pdv, 1, box1);
		rota.setRoteiro(roteiro);
		save(rota);
		
		tipoMovimentoFinanceitoDebito = Fixture.tipoMovimentoFinanceiroDebito();
		save(tipoMovimentoFinanceitoDebito);
		
		TipoMovimentoFinanceiro tipoMovimentoFinenceiroRecebimentoReparte =
			Fixture.tipoMovimentoFinanceiroRecebimentoReparte();
		
		save(tipoMovimentoFinenceiroRecebimentoReparte);
		
		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoRecReparte);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.setEditor(abril);
		save(produtoVeja);		
				
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 14, new Long(100),
				BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", produtoVeja, 
				null, false);
		save(produtoEdicaoVeja1);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(101), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinenceiroRecebimentoReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec), StatusAprovacao.APROVADO, new Date(), true);
		save(movimentoFinanceiroCota);
		
		consolidado = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota), cotaManoel,
						new Date(), new BigDecimal(200), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado);
		
		Divida divida = Fixture.divida(consolidado, cotaManoel, new Date(),
				        usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);
		divida.setAcumulada(false);
		save(divida);
		
		
		
		//CRIA UM OBJETO BOLETO NA SESSAO PARA TESTES

		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		ConsolidadoFinanceiroCota consolidado1 = Fixture.consolidadoFinanceiroCota(null, cotaManoel, new Date(), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado1);
		
		Divida divida1 = Fixture.divida(consolidado1, cotaManoel, new Date(), usuario, StatusDivida.EM_ABERTO, new BigDecimal(10),false);
		divida1.setAcumulada(false);
		save(divida1);
		
	    Boleto boleto = Fixture.boleto("5557884985445", "5", "5",
	    		                       Fixture.criarData(2, 2, 2010), 
	    		                       Fixture.criarData(2, Calendar.MAY, 2010), 
                					   null, 
                					   BigDecimal.ZERO, 
                					   new BigDecimal(100.00), 
                					   "1", 
                					   "1",
                					   StatusCobranca.NAO_PAGO,
                					   cotaManoel,
                					   bancoHSBC,
                					   divida,0);
		save(boleto);		
		
		HistoricoAcumuloDivida acumDividaGuilherme2;
		CobrancaDinheiro cobrancaAcumuloGuilherme2;
		
		ConsolidadoFinanceiroCota consolidadoAcumuloGuilherme2 = Fixture
				.consolidadoFinanceiroCota(null, cotaManoel,
						Fixture.criarData(2, 2, 2010), new BigDecimal(210), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		
		Divida dividaAcumuladaGuilherme2 = Fixture.divida(consolidadoAcumuloGuilherme2, cotaManoel, Fixture.criarData(2, 2, 2010),
				usuarioJoao, StatusDivida.QUITADA, new BigDecimal(210),false);
		
		
		acumDividaGuilherme2 = Fixture.criarHistoricoAcumuloDivida(
				dividaAcumuladaGuilherme2, 
				Fixture.criarData(1, 1, 2010), usuarioJoao, StatusInadimplencia.QUITADA);
		
		
		
		
		cobrancaAcumuloGuilherme2 = Fixture.criarCobrancaDinheiro("3234567890124", 
				new Date(), Fixture.criarData(2, Calendar.MAY, 2010), null,
                BigDecimal.ZERO, new BigDecimal(210),
				"TIPO_BAIXA", "ACAO", StatusCobranca.PAGO,
				cotaManoel, bancoHSBC, dividaAcumuladaGuilherme2,1);
		
		
		dividaAcumuladaGuilherme2.getAcumulado().add(divida);
		
		save(consolidadoAcumuloGuilherme2,dividaAcumuladaGuilherme2,acumDividaGuilherme2,cobrancaAcumuloGuilherme2);
		
		
	}
	
	@Test
	public void consultaDividasGeradas(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		
		List<GeraDividaDTO> lista = dividaRepository.obterDividasGeradas(filtro);
		
		Assert.assertNotNull(lista);
		
		Assert.assertTrue(!lista.isEmpty());
	}
	
	
	@Test
	public void consultaQuantidadeDividasGeradasPorData(){
		
		Long quantidade = dividaRepository.obterQunatidadeDividaGeradas(new Date());
		
		Assert.assertNotNull(quantidade);
		
		Assert.assertTrue(quantidade > 0);
	}
	
	@Test
	public void consultaQuantidadeDividasGeradas(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		
		Long quantidade = dividaRepository.obterQuantidadeRegistroDividasGeradas(filtro);
		
		Assert.assertNotNull(quantidade);
		
		Assert.assertTrue(quantidade > 0);
	}
	
	@Test
	public void consultaQuantidadeDividasGeradasNumCota(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setNumeroCota(1);
		
		Long quantidade = dividaRepository.obterQuantidadeRegistroDividasGeradas(filtro);
		
		}
	
	@Test
	public void consultaQuantidadeDividasGeradasIdBox(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setIdBox(1L);
		
		Long quantidade = dividaRepository.obterQuantidadeRegistroDividasGeradas(filtro);
		
		}
	
	@Test
	public void consultaQuantidadeDividasGeradasIdRota(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setIdRota(1L);
		
		Long quantidade = dividaRepository.obterQuantidadeRegistroDividasGeradas(filtro);
		
		}
	

	@Test
	public void consultaQuantidadeDividasGeradasIdRoteiro(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setIdRoteiro(1L);
		
		Long quantidade = dividaRepository.obterQuantidadeRegistroDividasGeradas(filtro);
		
		}
	
	@Test
	public void obterDividasGeradasSemBoleto(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		
		List<GeraDividaDTO> listaGeraDividaDTOs = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		
		Assert.assertNotNull(listaGeraDividaDTOs);
	}
	
	@Test
	public void obterDividasGeradasSemBoletoOrdenacaoBox(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setListaColunaOrdenacao(new ArrayList<FiltroDividaGeradaDTO.ColunaOrdenacao>());
		filtro.getListaColunaOrdenacao().add(FiltroDividaGeradaDTO.ColunaOrdenacao.BOX);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		
		List<GeraDividaDTO> listaGeraDividaDTOs = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		
		Assert.assertNotNull(listaGeraDividaDTOs);
}
	@Test
	public void obterDividasGeradasSemBoletoOrdenacaoDataEmissao(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setListaColunaOrdenacao(new ArrayList<FiltroDividaGeradaDTO.ColunaOrdenacao>());
		filtro.getListaColunaOrdenacao().add(FiltroDividaGeradaDTO.ColunaOrdenacao.DATA_EMISSAO);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		
		List<GeraDividaDTO> listaGeraDividaDTOs = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		
		Assert.assertNotNull(listaGeraDividaDTOs);
}
	
	@Test
	public void obterDividasGeradasSemBoletoOrdenacaoData(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setListaColunaOrdenacao(new ArrayList<FiltroDividaGeradaDTO.ColunaOrdenacao>());
		filtro.getListaColunaOrdenacao().add(FiltroDividaGeradaDTO.ColunaOrdenacao.DATA_VENCIMENTO);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		
		List<GeraDividaDTO> listaGeraDividaDTOs = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		
		Assert.assertNotNull(listaGeraDividaDTOs);
}
	@Test
	public void obterDividasGeradasSemBoletoOrdenacaoNomeCota(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setListaColunaOrdenacao(new ArrayList<FiltroDividaGeradaDTO.ColunaOrdenacao>());
		filtro.getListaColunaOrdenacao().add(FiltroDividaGeradaDTO.ColunaOrdenacao.NOME_COTA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		
		List<GeraDividaDTO> listaGeraDividaDTOs = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		
		Assert.assertNotNull(listaGeraDividaDTOs);
}
	
	@Test
	public void obterDividasGeradasSemBoletoOrdenacaoNumeroCota(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setListaColunaOrdenacao(new ArrayList<FiltroDividaGeradaDTO.ColunaOrdenacao>());
		filtro.getListaColunaOrdenacao().add(FiltroDividaGeradaDTO.ColunaOrdenacao.NUMERO_COTA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		
		List<GeraDividaDTO> listaGeraDividaDTOs = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		
		Assert.assertNotNull(listaGeraDividaDTOs);
}	
	
	@Test
	public void obterDividasGeradasSemBoletoOrdenacaoRota(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setListaColunaOrdenacao(new ArrayList<FiltroDividaGeradaDTO.ColunaOrdenacao>());
		filtro.getListaColunaOrdenacao().add(FiltroDividaGeradaDTO.ColunaOrdenacao.ROTA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		
		List<GeraDividaDTO> listaGeraDividaDTOs = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		
		Assert.assertNotNull(listaGeraDividaDTOs);
}	
	
	@Test
	public void obterDividasGeradasSemBoletoOrdenacaoRoteiro(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setListaColunaOrdenacao(new ArrayList<FiltroDividaGeradaDTO.ColunaOrdenacao>());
		filtro.getListaColunaOrdenacao().add(FiltroDividaGeradaDTO.ColunaOrdenacao.ROTEIRO);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		
		List<GeraDividaDTO> listaGeraDividaDTOs = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		
		Assert.assertNotNull(listaGeraDividaDTOs);
}	
	
	@Test
	public void obterDividasGeradasSemBoletoOrdenacaoTipoCobranca(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setListaColunaOrdenacao(new ArrayList<FiltroDividaGeradaDTO.ColunaOrdenacao>());
		filtro.getListaColunaOrdenacao().add(FiltroDividaGeradaDTO.ColunaOrdenacao.TIPO_COBRANCA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		
		List<GeraDividaDTO> listaGeraDividaDTOs = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		
		Assert.assertNotNull(listaGeraDividaDTOs);
}	
	@Test
	public void obterDividasGeradasSemBoletoOrdenacaoValor(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setListaColunaOrdenacao(new ArrayList<FiltroDividaGeradaDTO.ColunaOrdenacao>());
		filtro.getListaColunaOrdenacao().add(FiltroDividaGeradaDTO.ColunaOrdenacao.VALOR);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		
		List<GeraDividaDTO> listaGeraDividaDTOs = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		
		Assert.assertNotNull(listaGeraDividaDTOs);
}	
	@Test
	public void obterDividasGeradasSemBoletoOrdenacaoVia(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		filtro.setListaColunaOrdenacao(new ArrayList<FiltroDividaGeradaDTO.ColunaOrdenacao>());
		filtro.getListaColunaOrdenacao().add(FiltroDividaGeradaDTO.ColunaOrdenacao.VIA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		
		List<GeraDividaDTO> listaGeraDividaDTOs = dividaRepository.obterDividasGeradasSemBoleto(filtro);
		
		Assert.assertNotNull(listaGeraDividaDTOs);
}	
	
	
	@Test
	public void consultaQuantidadeDividasGeradasTipoCobranca(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setTipoCobranca(TipoCobranca.BOLETO);
		
		Long quantidade = dividaRepository.obterQuantidadeRegistroDividasGeradas(filtro);
		
		}
	
	
	
	
	@Test
	public void obterInadimplencias(){
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.NOME);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(true);
		filtro.setSituacaoNegociada(false);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(filtro);
		Assert.assertEquals(lista.size(),1);	
		StatusDividaDTO divida = lista.get(0);

		Assert.assertEquals(divida.getDataPagamento(), "02/03/2010" );
		Assert.assertEquals(divida.getDataVencimento(), "01/02/2010");
		Assert.assertEquals(divida.getNome(), "Manoel da Silva");
		Assert.assertEquals(divida.getSituacao(),"Quitada");
		Assert.assertTrue(divida.getDiasAtraso() == 29);
		Assert.assertTrue(divida.getNumCota() == 123);
		Assert.assertEquals(divida.getStatus(),"Ativo");
	}
	
	@Test
	public void obterInadimplenciasSituacaoNegociada(){
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.NOME);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(false);
		filtro.setSituacaoNegociada(true);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterInadimplenciasColunaOrdenacaoNulo(){
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(null);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(false);
		filtro.setSituacaoNegociada(true);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterInadimplenciasPaginacaoNulo(){
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.NOME);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(false);
		filtro.setSituacaoNegociada(true);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(null);
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterInadimplenciasOrderByNumCota(){
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.NUM_COTA);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(true);
		filtro.setSituacaoNegociada(false);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterInadimplenciasOrderByStatus(){
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.STATUS);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(true);
		filtro.setSituacaoNegociada(false);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterInadimplenciasOrderByConsignado(){
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.CONSIGNADO);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(true);
		filtro.setSituacaoNegociada(false);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterInadimplenciasOrderByDataVencimento(){
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.DATA_VENCIMENTO);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(true);
		filtro.setSituacaoNegociada(false);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	
	
	@Test
	public void obterInadimplenciasOrderByDataPagamento(){
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.DATA_PAGAMENTO);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(true);
		filtro.setSituacaoNegociada(false);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterInadimplenciasOrderByDataSituacao(){
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.SITUACAO);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(true);
		filtro.setSituacaoNegociada(false);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterInadimplenciasOrderByDividaAcumulada(){
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.DIVIDA_ACUMULADA);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(true);
		filtro.setSituacaoNegociada(false);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterInadimplenciasOrderByDiasAtraso(){
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.ATRASO);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(true);
		filtro.setSituacaoNegociada(false);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterTotalInadimplenciasCota() {
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.ATRASO);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(true);
		filtro.setSituacaoNegociada(false);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		
		dividaRepository.obterTotalInadimplenciasCota(filtro);
		
	}
	
	@Test
	public void obterTotalCotasInadimplencias() {
		
		Long lista = dividaRepository.obterTotalCotasInadimplencias(new FiltroCotaInadimplenteDTO());
		
		Assert.assertTrue(lista==1L);					
	}
	
	@Test
	public void obterTotalCotasInadimplenciasFiltro() {
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.ATRASO);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(true);
		filtro.setSituacaoNegociada(false);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		
		dividaRepository.obterTotalCotasInadimplencias(filtro);
		
	}
	
	@Test
	public void obterSomaDividas() {
		
		Double valor = dividaRepository.obterSomaDividas(new FiltroCotaInadimplenteDTO());
		
		Assert.assertTrue(valor==210.0);					
	}
	
	@Test
	public void obterSomaDividasFiltro() {
		
		FiltroCotaInadimplenteDTO filtro = new FiltroCotaInadimplenteDTO();
		filtro.setColunaOrdenacao(FiltroCotaInadimplenteDTO.ColunaOrdenacao.ATRASO);
		filtro.setNomeCota("Manoel da Silva");
		filtro.setNumCota(123);
		filtro.setPeriodoDe("02/03/2009");
		filtro.setPeriodoAte("02/03/2012");
		filtro.setSituacaoEmAberto(true);
		filtro.setSituacaoPaga(true);
		filtro.setSituacaoNegociada(false);
		filtro.setStatusCota("Ativo");
		filtro.setPaginacao(new PaginacaoVO(1, 5, "ASC",ColunaOrdenacao.NOME.toString()));
		
		
		dividaRepository.obterSomaDividas(filtro);
		
	}
	
	@Test
	public void obterDividaPorIdConsolidado(){
		
		Assert.assertNotNull(this.dividaRepository.obterDividaPorIdConsolidado(consolidado.getId()));
	}
	
	
    @Test
    public void testSumarizacaoDividasReceberEm() {
        Date verificacao = Fixture.criarData(6, Calendar.NOVEMBER, 2012);
        Date vencimento = Fixture.criarData(6, Calendar.NOVEMBER, 2012);
        criarDadosSumarizacaoDividas(vencimento);

        Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacao = dividaRepository.sumarizacaoDividasReceberEm(verificacao);

        Assert.assertNotNull(sumarizacao);
        Assert.assertEquals(3, sumarizacao.size());
        
        SumarizacaoDividasDTO sumarizacaoBoleto = sumarizacao.get(TipoCobranca.BOLETO);
        Assert.assertNotNull(sumarizacaoBoleto);
        Assert.assertEquals(TipoCobranca.BOLETO, sumarizacaoBoleto.getTipoCobranca());
        Assert.assertEquals(vencimento, sumarizacaoBoleto.getData());
        Assert.assertEquals(TipoDivida.DIVIDA_A_RECEBER, sumarizacaoBoleto.getTipoSumarizacao());
        Assert.assertEquals(BigDecimal.valueOf(1375.5).setScale(2), sumarizacaoBoleto.getTotal());
        Assert.assertEquals(BigDecimal.valueOf(923.5).setScale(2), sumarizacaoBoleto.getValorPago());
        Assert.assertEquals(BigDecimal.valueOf(452).setScale(2), sumarizacaoBoleto.getInadimplencia());
        
        SumarizacaoDividasDTO sumarizacaoDeposito = sumarizacao.get(TipoCobranca.DEPOSITO);
        Assert.assertNotNull(sumarizacaoDeposito);
        Assert.assertEquals(TipoCobranca.DEPOSITO, sumarizacaoDeposito.getTipoCobranca());
        Assert.assertEquals(vencimento, sumarizacaoDeposito.getData());
        Assert.assertEquals(TipoDivida.DIVIDA_A_RECEBER, sumarizacaoDeposito.getTipoSumarizacao());
        Assert.assertEquals(BigDecimal.valueOf(1371.56).setScale(2), sumarizacaoDeposito.getTotal());
        Assert.assertEquals(BigDecimal.valueOf(621.56).setScale(2), sumarizacaoDeposito.getValorPago());
        Assert.assertEquals(BigDecimal.valueOf(750).setScale(2), sumarizacaoDeposito.getInadimplencia());
        
        SumarizacaoDividasDTO sumarizacaoDinheiro = sumarizacao.get(TipoCobranca.DINHEIRO);
        Assert.assertNotNull(sumarizacaoDinheiro);
        Assert.assertEquals(TipoCobranca.DINHEIRO, sumarizacaoDinheiro.getTipoCobranca());
        Assert.assertEquals(vencimento, sumarizacaoDinheiro.getData());
        Assert.assertEquals(TipoDivida.DIVIDA_A_RECEBER, sumarizacaoDinheiro.getTipoSumarizacao());
        Assert.assertEquals(BigDecimal.valueOf(989.32).setScale(2), sumarizacaoDinheiro.getTotal());
        Assert.assertEquals(BigDecimal.valueOf(489.32).setScale(2), sumarizacaoDinheiro.getValorPago());
        Assert.assertEquals(BigDecimal.valueOf(500).setScale(2), sumarizacaoDinheiro.getInadimplencia());
    }
    
    @Test
    public void testSumarizacaoDividasReceberEmSemDividas() {
        Date verificacao = Fixture.criarData(6, Calendar.NOVEMBER, 2012);
        Date vencimento = Fixture.criarData(5, Calendar.NOVEMBER, 2012);
        criarDadosSumarizacaoDividas(vencimento);

        Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacao = dividaRepository.sumarizacaoDividasReceberEm(verificacao);

        Assert.assertNotNull(sumarizacao);
        Assert.assertTrue(sumarizacao.isEmpty());
    }
    
    @Test
    public void testSumarizacaoDividasVencerApos() {
        Date verificacao = Fixture.criarData(6, Calendar.NOVEMBER, 2012);
        Date vencimento = Fixture.criarData(8, Calendar.NOVEMBER, 2012);
        criarDadosSumarizacaoDividas(vencimento);
        
        Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacao = dividaRepository.sumarizacaoDividasVencerApos(verificacao);
        Assert.assertNotNull(sumarizacao);
        Assert.assertEquals(3, sumarizacao.size());
        
        SumarizacaoDividasDTO sumarizacaoBoleto = sumarizacao.get(TipoCobranca.BOLETO);
        Assert.assertNotNull(sumarizacaoBoleto);
        Assert.assertEquals(TipoCobranca.BOLETO, sumarizacaoBoleto.getTipoCobranca());
        Assert.assertEquals(verificacao, sumarizacaoBoleto.getData());
        Assert.assertEquals(TipoDivida.DIVIDA_A_VENCER, sumarizacaoBoleto.getTipoSumarizacao());
        Assert.assertEquals(BigDecimal.valueOf(1375.5).setScale(2), sumarizacaoBoleto.getTotal());
        Assert.assertEquals(BigDecimal.valueOf(923.5).setScale(2), sumarizacaoBoleto.getValorPago());
        Assert.assertEquals(BigDecimal.valueOf(452).setScale(2), sumarizacaoBoleto.getInadimplencia());
        
        SumarizacaoDividasDTO sumarizacaoDeposito = sumarizacao.get(TipoCobranca.DEPOSITO);
        Assert.assertNotNull(sumarizacaoDeposito);
        Assert.assertEquals(TipoCobranca.DEPOSITO, sumarizacaoDeposito.getTipoCobranca());
        Assert.assertEquals(verificacao, sumarizacaoDeposito.getData());
        Assert.assertEquals(TipoDivida.DIVIDA_A_VENCER, sumarizacaoDeposito.getTipoSumarizacao());
        Assert.assertEquals(BigDecimal.valueOf(1371.56).setScale(2), sumarizacaoDeposito.getTotal());
        Assert.assertEquals(BigDecimal.valueOf(621.56).setScale(2), sumarizacaoDeposito.getValorPago());
        Assert.assertEquals(BigDecimal.valueOf(750).setScale(2), sumarizacaoDeposito.getInadimplencia());
        
        SumarizacaoDividasDTO sumarizacaoDinheiro = sumarizacao.get(TipoCobranca.DINHEIRO);
        Assert.assertNotNull(sumarizacaoDinheiro);
        Assert.assertEquals(TipoCobranca.DINHEIRO, sumarizacaoDinheiro.getTipoCobranca());
        Assert.assertEquals(verificacao, sumarizacaoDinheiro.getData());
        Assert.assertEquals(TipoDivida.DIVIDA_A_VENCER, sumarizacaoDinheiro.getTipoSumarizacao());
        Assert.assertEquals(BigDecimal.valueOf(989.32).setScale(2), sumarizacaoDinheiro.getTotal());
        Assert.assertEquals(BigDecimal.valueOf(489.32).setScale(2), sumarizacaoDinheiro.getValorPago());
        Assert.assertEquals(BigDecimal.valueOf(500).setScale(2), sumarizacaoDinheiro.getInadimplencia());
    }
    
    @Test
    public void testSumarizacaoDividasVencerAposSemDividas() {
        Date verificacao = Fixture.criarData(6, Calendar.NOVEMBER, 2012);
        Date vencimento = Fixture.criarData(6, Calendar.NOVEMBER, 2012);
        criarDadosSumarizacaoDividas(vencimento);
        
        Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacao = dividaRepository.sumarizacaoDividasVencerApos(verificacao);
        Assert.assertNotNull(sumarizacao);
        Assert.assertTrue(sumarizacao.isEmpty());
    }



	@Test
	public void obterTotalDividasAbertoCota() {
		Long idCota = 1L;
		
		dividaRepository.obterTotalDividasAbertoCota(idCota);
		
	}
	
	@Test
	public void obterDividaParaAcumuloPorCotaIdCota() {
		Long idCota = 1L;
		
		Divida divida =  dividaRepository.obterDividaParaAcumuloPorCota(idCota, null);
		
	}
	
	@Test
	public void obterDividaParaAcumuloPorCotaData() {
		Date diaDivida = Fixture.criarData(24, Calendar.OCTOBER, 2012);
		
		Divida divida =  dividaRepository.obterDividaParaAcumuloPorCota(null, diaDivida);
		
	}
	
	@Test
	public void obterValorDividasDataOperacaoDividaVencendo() {
		
		Boolean dividaVencendo = true;
		
		dividaRepository.obterValorDividasDataOperacao(dividaVencendo, false);
		
	}
	
	@Test
	public void obterValorDividasDataOperacaoDividaAcululada() {
		
		Boolean dividaAcumulada = true;
		
		dividaRepository.obterValorDividasDataOperacao(false, dividaAcumulada);
		
	}
		
	@Test
	public void obterValoresDividasGeradasDataOperacaoPostergadaTrue() {
		
		Boolean postergada = true;
		
		dividaRepository.obterValoresDividasGeradasDataOperacao(postergada);
		
	}
	
	@Test
	public void obterValoresDividasGeradasDataOperacaoPostergadaFalse() {
		
		Boolean postergada = false;
		
		dividaRepository.obterValoresDividasGeradasDataOperacao(postergada);
		
	}

    /**
     * Gera os dados para testes de sumarização de dívidas à receber e dívidas à
     * vencer e consulta de dívidas que compõe a sumarização
     * 
     * @param vencimento
     *            data de vencimento das dívidas a serem geradas
     */
    private void criarDadosSumarizacaoDividas(Date vencimento) {
        PoliticaCobranca politicaCobrancaDinheiro = Fixture.criarPoliticaCobranca(distribuidor, formaDinheiro, true,
                        false, true, 3, "Assunto", "Mensagem", true, FormaEmissao.INDIVIDUAL_BOX);
        distribuidor.getPoliticasCobranca().add(politicaCobrancaDinheiro);
        save(politicaCobrancaDinheiro);

        PoliticaCobranca politicaCobrancaDeposito = Fixture.criarPoliticaCobranca(distribuidor, formaDeposito, true,
                        false, true, 3, "Assunto", "Mensagem", true, FormaEmissao.INDIVIDUAL_BOX);
        distribuidor.getPoliticasCobranca().add(politicaCobrancaDeposito);
        save(politicaCobrancaDeposito);

        PessoaFisica jose = Fixture.pessoaFisica("12345678901", "sys.discover@gmail.com", "Jose da Silva");
        cotaJose = Fixture.cota(456, jose, SituacaoCadastro.ATIVO, box1);
        save(jose, cotaJose);

        ParametroCobrancaCota parametroCobrancaJose = Fixture.parametroCobrancaCota(Collections.singleton(formaDinheiro), 3,
                        null, cotaJose, 1, false, BigDecimal.valueOf(100), TipoCota.CONSIGNADO);
        cotaJose.setParametroCobranca(parametroCobrancaJose);
        save(parametroCobrancaJose);

        PessoaFisica maria = Fixture.pessoaFisica("12345678902", "sys.discover@gmail.com", "Maria da Silva");
        cotaMaria = Fixture.cota(789, maria, SituacaoCadastro.ATIVO, box1);
        save(maria, cotaMaria);

        ParametroCobrancaCota parametroCobrancaMaria = Fixture.parametroCobrancaCota(Collections.singleton(formaDeposito), 3,
                        null, cotaMaria, 1, false, BigDecimal.valueOf(100), TipoCota.CONSIGNADO);
        cotaMaria.setParametroCobranca(parametroCobrancaMaria);
        save(parametroCobrancaMaria);

        PessoaFisica orlando = Fixture.pessoaFisica("99933355544", "sys.discover@gmail.com", "Orlando da Silva");
        cotaOrlando = Fixture.cota(1011, orlando, SituacaoCadastro.ATIVO, box1);
        save(orlando, cotaOrlando);

        ParametroCobrancaCota parametroCobrancaOrlando = Fixture.parametroCobrancaCota(Collections.singleton(formaBoleto), 3,
                        null, cotaOrlando, 1, false, BigDecimal.valueOf(100), TipoCota.CONSIGNADO);
        cotaOrlando.setParametroCobranca(parametroCobrancaOrlando);
        save(parametroCobrancaOrlando);

        PessoaFisica luis = Fixture.pessoaFisica("12345678903", "sys.discover@gmail.com", "Luis Silva");
        cotaLuis = Fixture.cota(1213, luis, SituacaoCadastro.ATIVO, box1);
        save(luis, cotaLuis);

        ParametroCobrancaCota parametroCobrancaLuis = Fixture.parametroCobrancaCota(Collections.singleton(formaDinheiro), 3,
                        null, cotaLuis, 1, false, BigDecimal.valueOf(100), TipoCota.CONSIGNADO);
        cotaLuis.setParametroCobranca(parametroCobrancaLuis);
        save(parametroCobrancaLuis);

        PessoaFisica mariana = Fixture.pessoaFisica("99933355533", "sys.discover@gmail.com", "Mariana da Silva");
        cotaMariana = Fixture.cota(1415, mariana, SituacaoCadastro.ATIVO, box1);
        save(mariana, cotaMariana);

        ParametroCobrancaCota parametroCobrancaMariana = Fixture.parametroCobrancaCota(Collections.singleton(formaDeposito), 3,
                        null, cotaMariana, 1, false, BigDecimal.valueOf(100), TipoCota.CONSIGNADO);
        cotaMariana.setParametroCobranca(parametroCobrancaMariana);
        save(parametroCobrancaMariana);

        MovimentoFinanceiroCota mfcManoel = Fixture.movimentoFinanceiroCota(cotaManoel, tipoMovimentoFinanceitoDebito, usuarioJoao,
                BigDecimal.valueOf(452), null, StatusAprovacao.APROVADO, Fixture.criarData(5, Calendar.NOVEMBER, 2012), true);
        save(mfcManoel);

        ConsolidadoFinanceiroCota consolidadoManoel = Fixture.consolidadoFinanceiroCota(Arrays.asList(mfcManoel),
                        cotaManoel, Fixture.criarData(5, Calendar.NOVEMBER, 2012), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.valueOf(452), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        save(consolidadoManoel);

        Divida dividaManoel = Fixture.divida(consolidadoManoel, cotaManoel, Fixture.criarData(5, Calendar.NOVEMBER, 2012), usuarioJoao,
                StatusDivida.EM_ABERTO, BigDecimal.valueOf(452), false);
        save(dividaManoel);

        Boleto boletoManoel = Fixture.boleto("5557884985446", "7", "55578849854467", Fixture.criarData(5, Calendar.NOVEMBER, 2012),
                        vencimento, null, BigDecimal.ZERO, BigDecimal.valueOf(452), "1", "1", StatusCobranca.NAO_PAGO, cotaManoel, bancoHSBC,
                        dividaManoel, 0);
        save(boletoManoel);

        MovimentoFinanceiroCota mfcJose = Fixture.movimentoFinanceiroCota(cotaJose, tipoMovimentoFinanceitoDebito, usuarioJoao,
                BigDecimal.valueOf(500), null, StatusAprovacao.APROVADO, Fixture.criarData(5, Calendar.NOVEMBER, 2012), true);
        save(mfcJose);

        ConsolidadoFinanceiroCota consolidadoJose = Fixture.consolidadoFinanceiroCota(Arrays.asList(mfcJose), cotaJose,
                        Fixture.criarData(5, Calendar.NOVEMBER, 2012), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.valueOf(500), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        save(consolidadoJose);

        Divida dividaJose = Fixture.divida(consolidadoJose, cotaJose, Fixture.criarData(5, Calendar.NOVEMBER, 2012), usuarioJoao,
                StatusDivida.EM_ABERTO, BigDecimal.valueOf(500), false);
        save(dividaJose);

        CobrancaDinheiro cobrancaJose = Fixture.cobrancaDinheiro("5557884985447", Fixture.criarData(5, Calendar.NOVEMBER, 2012),
                vencimento, null, BigDecimal.ZERO, BigDecimal.valueOf(500), TipoBaixaCobranca.MANUAL.name(), null, StatusCobranca.NAO_PAGO,
                cotaJose, null, dividaJose, 0);
        save(cobrancaJose);

        MovimentoFinanceiroCota mfcMaria = Fixture.movimentoFinanceiroCota(cotaMaria, tipoMovimentoFinanceitoDebito, usuarioJoao,
                BigDecimal.valueOf(750), null, StatusAprovacao.APROVADO, Fixture.criarData(5, Calendar.NOVEMBER, 2012), true);
        save(mfcMaria);

        ConsolidadoFinanceiroCota consolidadoMaria = Fixture.consolidadoFinanceiroCota(Arrays.asList(mfcMaria), cotaMaria,
                        Fixture.criarData(5, Calendar.NOVEMBER, 2012), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.valueOf(750), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        save(consolidadoMaria);

        Divida dividaMaria = Fixture.divida(consolidadoMaria, cotaMaria, Fixture.criarData(5, Calendar.NOVEMBER, 2012), usuarioJoao,
                StatusDivida.EM_ABERTO, BigDecimal.valueOf(750), false);
        save(dividaMaria);

        CobrancaDeposito cobrancaMaria = Fixture.cobrancaDeposito("5557884985448", Fixture.criarData(5, Calendar.NOVEMBER, 2012),
                vencimento, null, BigDecimal.ZERO, BigDecimal.valueOf(750), TipoBaixaCobranca.MANUAL.name(), null, StatusCobranca.NAO_PAGO,
                cotaMaria, bancoHSBC, dividaMaria, 0);
        save(cobrancaMaria);

        MovimentoFinanceiroCota mfcOrlando = Fixture.movimentoFinanceiroCota(cotaOrlando, tipoMovimentoFinanceitoDebito, usuarioJoao,
                BigDecimal.valueOf(923.5), null, StatusAprovacao.APROVADO, Fixture.criarData(5, Calendar.NOVEMBER, 2012), true);
        save(mfcOrlando);

        ConsolidadoFinanceiroCota consolidadoOrlando = Fixture.consolidadoFinanceiroCota(Arrays.asList(mfcOrlando), cotaOrlando,
                        Fixture.criarData(5, Calendar.NOVEMBER, 2012), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.valueOf(923.5), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        save(consolidadoOrlando);

        Divida dividaOrlando = Fixture.divida(consolidadoOrlando, cotaOrlando, Fixture.criarData(5, Calendar.NOVEMBER, 2012), usuarioJoao,
                StatusDivida.QUITADA, BigDecimal.valueOf(923.5), false);
        save(dividaOrlando);

        Boleto boletoOrlando = Fixture.boleto("5557884985452", "8", "55578849854528", Fixture.criarData(5, Calendar.NOVEMBER, 2012), vencimento,
                vencimento, BigDecimal.ZERO, BigDecimal.valueOf(923.5), "1", "1", StatusCobranca.PAGO, cotaOrlando, bancoHSBC, dividaOrlando, 0);
        save(boletoOrlando);
        
        BaixaAutomatica baixaOrlando = Fixture.baixaAutomatica(boletoOrlando, vencimento, "arquivo.txt", "55578849854528", 100, StatusBaixa.PAGO, BigDecimal.valueOf(923.5), bancoHSBC);
        boletoOrlando.getBaixasCobranca().add(baixaOrlando);
        save(baixaOrlando);

        MovimentoFinanceiroCota mfcMariana = Fixture.movimentoFinanceiroCota(cotaMariana, tipoMovimentoFinanceitoDebito, usuarioJoao, 
                BigDecimal.valueOf(621.56), null, StatusAprovacao.APROVADO, Fixture.criarData(5, Calendar.NOVEMBER, 2012), true);
        save(mfcMariana);

        ConsolidadoFinanceiroCota consolidadoMariana = Fixture.consolidadoFinanceiroCota(Arrays.asList(mfcMariana), cotaMariana,
                        Fixture.criarData(5, Calendar.NOVEMBER, 2012), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.valueOf(621.56), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        save(consolidadoMariana);

        Divida dividaMariana = Fixture.divida(consolidadoMariana, cotaMariana, Fixture.criarData(5, Calendar.NOVEMBER, 2012), usuarioJoao, 
                StatusDivida.QUITADA, BigDecimal.valueOf(621.56), false);
        save(dividaMariana);

        CobrancaDeposito cobrancaMariana = Fixture.cobrancaDeposito("5557884985472", Fixture.criarData(5, Calendar.NOVEMBER, 2012), vencimento, 
                vencimento, BigDecimal.ZERO, BigDecimal.valueOf(621.56), TipoBaixaCobranca.MANUAL.name(),null, StatusCobranca.PAGO, cotaMariana, bancoHSBC,
                dividaMariana, 0);
        save(cobrancaMariana);

        BaixaManual baixaMariana = Fixture.baixaManual(cobrancaMariana, vencimento, StatusBaixa.PAGO, BigDecimal.valueOf(621.56), null, StatusAprovacao.APROVADO);
        cobrancaMariana.getBaixasCobranca().add(baixaMariana);
        save(baixaMariana);

        MovimentoFinanceiroCota mfcLuis = Fixture.movimentoFinanceiroCota(cotaLuis, tipoMovimentoFinanceitoDebito, usuarioJoao, BigDecimal.valueOf(489.32), null, StatusAprovacao.APROVADO,
                Fixture.criarData(5, Calendar.NOVEMBER, 2012), true);
        save(mfcLuis);

        ConsolidadoFinanceiroCota consolidadoLuis = Fixture.consolidadoFinanceiroCota(Arrays.asList(mfcLuis), cotaLuis, Fixture.criarData(5, Calendar.NOVEMBER, 2012),
                        BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(489.32), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        save(consolidadoLuis);

        Divida dividaLuis = Fixture.divida(consolidadoLuis, cotaLuis, Fixture.criarData(5, Calendar.NOVEMBER, 2012), usuarioJoao,StatusDivida.QUITADA, BigDecimal.valueOf(489.32), false);
        save(dividaLuis);

        CobrancaDinheiro cobrancaLuis = Fixture.cobrancaDinheiro("5557884985439", Fixture.criarData(5, Calendar.NOVEMBER, 2012), vencimento, vencimento, BigDecimal.ZERO,
                BigDecimal.valueOf(489.32), TipoBaixaCobranca.MANUAL.name(), null, StatusCobranca.PAGO, cotaLuis, null, dividaLuis, 0);
        save(cobrancaLuis);

        BaixaManual baixaLuis = Fixture.baixaManual(cobrancaLuis, vencimento, StatusBaixa.PAGO, BigDecimal.valueOf(489.32), null, StatusAprovacao.APROVADO);
        cobrancaLuis.getBaixasCobranca().add(baixaLuis);
        save(baixaLuis);
    }

}
