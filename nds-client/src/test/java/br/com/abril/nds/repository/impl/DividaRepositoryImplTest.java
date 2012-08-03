package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO.ColunaOrdenacao;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Moeda;
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
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.CobrancaDinheiro;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.HistoricoAcumuloDivida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class DividaRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private DividaRepository dividaRepository;
	
	ConsolidadoFinanceiroCota consolidado;
	
	@Before
	public void setUp() {
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		Carteira carteiraRegistrada = Fixture.carteira(30, TipoRegistroCobranca.REGISTRADA);
	    
		save(carteiraRegistrada);
		
		Banco bancoHSBC = Fixture.banco(10L, true, carteiraRegistrada, "1010",
				  123456L, "1", "1", "Instrucoes.", "HSBC","BANCO HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		
		save(bancoHSBC);
		
		PessoaFisica manoel = Fixture.pessoaFisica("319.435.088-95",
				"sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56.003.315/0001-47", "333333333333", "distrib_acme@mail.com", "99.999-9");
		
		save(juridicaDistrib);
		
		FormaCobranca formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
				  												BigDecimal.ONE, BigDecimal.ONE,null);
		save(formaBoleto);
		
		Distribuidor distribuidor = null; 
		
		PoliticaCobranca politicaCobranca =
				Fixture.criarPoliticaCobranca(distribuidor, formaBoleto, true, true, true, 1,"Assunto","Mansagem",true,FormaEmissao.INDIVIDUAL_BOX);
		
		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);
		
		distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), politicasCobranca);
		
		save(distribuidor);
		
		politicaCobranca.setDistribuidor(distribuidor);
		save(politicaCobranca);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
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
		
		Roteiro roteiro = Fixture.criarRoteiro("Pinheiros",box1,TipoRoteiro.NORMAL);
		save(roteiro);

		Rota rota = Fixture.rota("005", "Rota 005");
		rota.setRoteiro(roteiro);
		save(rota);
		
		Roteirizacao roteirizacao = Fixture.criarRoteirizacao(pdv, rota,1);
		save(roteirizacao);
		
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
				
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao("1", 1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", 1L,
				produtoVeja, null, false);
		save(produtoEdicaoVeja1);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				new BigDecimal(100.56), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinenceiroRecebimentoReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec), StatusAprovacao.APROVADO, new Date(), true);
		save(movimentoFinanceiroCota);
		
		consolidado = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota), cotaManoel,
						new Date(), new BigDecimal(200), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado);
		
		Divida divida = Fixture.divida(consolidado, cotaManoel, new Date(),
				        usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);
		divida.setAcumulada(false);
		save(divida);
		
		
		
		//CRIA UM OBJETO BOLETO NA SESSAO PARA TESTES

		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		ConsolidadoFinanceiroCota consolidado1 = Fixture.consolidadoFinanceiroCota(null, cotaManoel, new Date(), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado1);
		
		Divida divida1 = Fixture.divida(consolidado1, cotaManoel, new Date(), usuario, StatusDivida.EM_ABERTO, new BigDecimal(10),false);
		divida1.setAcumulada(false);
		save(divida1);
		
	    Boleto boleto = Fixture.boleto("5", "5", "5",
                					   new Date(), 
                					   new Date(), 
                					   new Date(), 
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
						Fixture.criarData(2, 2, 2010), new BigDecimal(210), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		
		Divida dividaAcumuladaGuilherme2 = Fixture.divida(consolidadoAcumuloGuilherme2, cotaManoel, Fixture.criarData(2, 2, 2010),
				usuarioJoao, StatusDivida.QUITADA, new BigDecimal(210),false);
		
		
		acumDividaGuilherme2 = Fixture.criarHistoricoAcumuloDivida(
				dividaAcumuladaGuilherme2, 
				Fixture.criarData(1, 1, 2010), usuarioJoao, StatusInadimplencia.QUITADA);
		
		
		
		
		cobrancaAcumuloGuilherme2 = Fixture.criarCobrancaDinheiro("3234567890124", 
				new Date(),Fixture.criarData(1, 1, 2010),  Fixture.criarData(2, 2, 2010),
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
	public void consultaQuantidadeDividasGeradas(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		
		Long quantidade = dividaRepository.obterQuantidadeRegistroDividasGeradas(filtro);
		
		Assert.assertNotNull(quantidade);
		
		Assert.assertTrue(quantidade > 0);
	}

	@Test
	public void consultaQuantidadeDividasGeradasPorData(){
		
		Long quantidade = dividaRepository.obterQunatidadeDividaGeradas(new Date());
		
		Assert.assertNotNull(quantidade);
		
		Assert.assertTrue(quantidade > 0);
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
	public void obterTotalInadimplencias() {
		
		List<StatusDividaDTO> lista = dividaRepository.obterInadimplenciasCota(new FiltroCotaInadimplenteDTO());
		
		Assert.assertTrue(lista.size()==1L);					
	}
	
	@Test
	public void obterTotalCotasInadimplencias() {
		
		Long lista = dividaRepository.obterTotalCotasInadimplencias(new FiltroCotaInadimplenteDTO());
		
		Assert.assertTrue(lista==1L);					
	}
	
	@Test
	public void obterSomaDividas() {
		
		Double valor = dividaRepository.obterSomaDividas(new FiltroCotaInadimplenteDTO());
		
		Assert.assertTrue(valor==210.0);					
	}
	
	@Test
	public void obterDividaPorIdConsolidado(){
		
		Assert.assertNotNull(this.dividaRepository.obterDividaPorIdConsolidado(consolidado.getId()));
	}
}
