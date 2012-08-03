package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
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
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoBaixaCobranca;

public class BoletoServiceImplTest  extends AbstractRepositoryImplTest {
	
	@Autowired
	private BoletoServiceImpl boletoServiceImpl;
	
	@Mock
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	private Usuario usuarioJoao;
	private Distribuidor distribuidor;
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		
		Carteira carteiraSemRegistro = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		save(carteiraSemRegistro);
		
		Banco bancoHSBC = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
			  							123456L, "1", "1", "Instruções.", "HSBC","BANCO HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		save(bancoHSBC);
		
		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("LH", "01.001.001/001-00", "000.000.000.00", "lh@mail.com", "99.999-9");
		save(pessoaJuridica);
		
		ParametroCobrancaCota parametroCobranca = 
				Fixture.parametroCobrancaCota(null, 2, BigDecimal.TEN, null, 1, 
											  true, BigDecimal.TEN);
  		save(parametroCobranca);
		
		FormaCobranca formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
			  BigDecimal.ONE, BigDecimal.ONE,parametroCobranca);
		save(formaBoleto);
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1,"Assunto","Mansagem",true,FormaEmissao.INDIVIDUAL_BOX);
		save(politicaCobranca);
		
		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);
		
		distribuidor = Fixture.distribuidor(1, pessoaJuridica, new Date(), politicasCobranca);
		save(distribuidor);
		
		politicaCobranca.setDistribuidor(distribuidor);
		save(politicaCobranca);
		
		Box box = Fixture.criarBox(300, "Box 300", TipoBox.LANCAMENTO);
		save(box);
		
		Cota cota = Fixture.cota(1000, pessoaJuridica, SituacaoCadastro.ATIVO,box);
		save(cota);
		
		//AMARRAÇAO DIVIDA X BOLETO
		usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		TipoMovimentoFinanceiro tipoMovimentoFinenceiroRecebimentoReparte =
			Fixture.tipoMovimentoFinanceiroRecebimentoReparte();
		save(tipoMovimentoFinenceiroRecebimentoReparte);
		
		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoRecReparte);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.setEditor(abril);
		save(produtoVeja);		
				
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao("1", 1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQRSTU", 1L,
				produtoVeja, null, false);
		save(produtoEdicaoVeja1);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cota, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				new BigInteger("101"), cota, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
				cota, tipoMovimentoFinenceiroRecebimentoReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec), StatusAprovacao.APROVADO, new Date(), true);
		save(movimentoFinanceiroCota);
		
		ConsolidadoFinanceiroCota consolidado1 =
			Fixture.consolidadoFinanceiroCota(Arrays.asList(movimentoFinanceiroCota),
										      cota, new Date(), new BigDecimal(200),
										      new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado1);
		
		ConsolidadoFinanceiroCota consolidado2 =
			Fixture.consolidadoFinanceiroCota(null, cota, new Date(), new BigDecimal(200),
				      new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado2);
		
		ConsolidadoFinanceiroCota consolidado3 =
				Fixture.consolidadoFinanceiroCota(null, cota, new Date(), new BigDecimal(200),
					      new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
			save(consolidado3);
		
		ConsolidadoFinanceiroCota consolidado4 =
				Fixture.consolidadoFinanceiroCota(null, cota, new Date(), new BigDecimal(200),
					      new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado4);
		
		ConsolidadoFinanceiroCota consolidado5 =
				Fixture.consolidadoFinanceiroCota(null, cota, new Date(), new BigDecimal(200),
					      new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado5);
			
		Divida divida1 = Fixture.divida(consolidado1, cota, new Date(),
										usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);
		save(divida1);

		Divida divida2 = Fixture.divida(consolidado2, cota, new Date(),
		        						usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);
		save(divida2);
		
		Divida divida3 = Fixture.divida(consolidado3, cota, new Date(),
		        						usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);
		save(divida3);
		
		Divida divida4 = Fixture.divida(consolidado4, cota, new Date(),
										usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);
		save(divida4);
		
		Divida divida5 = Fixture.divida(consolidado5, cota, new Date(),
										usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200),false);
		save(divida5);
		
		Boleto boleto1 = Fixture.boleto("1234567890123", "456", "1234567890123456", new Date(),
									    new Date(), new Date(), BigDecimal.ZERO, 
                					    new BigDecimal(100), "1", "1", StatusCobranca.PAGO,
                					    cota, bancoHSBC, divida1, 0);
		save(boleto1);
		
		Boleto boleto2 = Fixture.boleto("1234567890124", "456", "1234567890124456", new Date(),
										new Date(), new Date(), BigDecimal.ZERO, 
				   						new BigDecimal(100), "1", "1", StatusCobranca.NAO_PAGO,
				   						cota, bancoHSBC, divida2, 0);
		save(boleto2);
		
		Boleto boleto3 = Fixture.boleto("1234567890125", "456", "1234567890125456", new Date(),
										new Date(), new Date(), BigDecimal.ZERO, 
										new BigDecimal(100), "1", "1", StatusCobranca.NAO_PAGO,
										cota, bancoHSBC, divida3, 0);
		save(boleto3);
		
		Boleto boleto4 = Fixture.boleto("1234567890126", "456", "1234567890126456", new Date(),
										new Date(), new Date(), BigDecimal.ZERO, 
										new BigDecimal(100.00), "1", "1", StatusCobranca.NAO_PAGO,
										cota, bancoHSBC, divida4, 0);
		save(boleto4);
		
		Boleto boleto5 = Fixture.boleto("1234567890127", "456", "1234567890127456", new Date(),
										new Date(), new Date(), BigDecimal.ZERO, 
										new BigDecimal(100.00), "1", "1", StatusCobranca.NAO_PAGO,
										cota, bancoHSBC, divida5, 0);
		save(boleto5);
	}
	
	@Test
	public void testeBaixaAutomaticaPermiteDivergencia() {
		
		PoliticaCobranca politicaCobranca =
				Fixture.criarPoliticaCobranca(null, null, true, true, true, 1, null, null,true,FormaEmissao.INDIVIDUAL_BOX);
		
		Mockito.when(politicaCobrancaRepository.obterPorTipoCobranca(TipoCobranca.BOLETO))
			.thenReturn(politicaCobranca);
		
		ArquivoPagamentoBancoDTO arquivo = criarArquivoPagamentoBanco();
		
		ResumoBaixaBoletosDTO resumo = 
			boletoServiceImpl.baixarBoletosAutomatico(arquivo, new BigDecimal(200), usuarioJoao);
		
		Assert.assertTrue(resumo.getQuantidadeLidos() == 6);
		
		Assert.assertTrue(resumo.getQuantidadeBaixados() == 2);
		
		Assert.assertTrue(resumo.getQuantidadeRejeitados() == 1);
		
		Assert.assertTrue(resumo.getQuantidadeBaixadosComDivergencia() == 3);
	}
	
	@Test
	public void testeBaixaAutomaticaNaoPermiteDivergencia() {
		
		PoliticaCobranca politicaCobranca =
				Fixture.criarPoliticaCobranca(null, null, false, false, false, 1, null, null,true,FormaEmissao.INDIVIDUAL_BOX);
		
		Mockito.when(politicaCobrancaRepository.obterPorTipoCobranca(TipoCobranca.BOLETO))
			.thenReturn(politicaCobranca);
		
		ArquivoPagamentoBancoDTO arquivo = criarArquivoPagamentoBanco();
		
		boletoServiceImpl.politicaCobrancaRepository = politicaCobrancaRepository;
		
		ResumoBaixaBoletosDTO resumo = 
			boletoServiceImpl.baixarBoletosAutomatico(arquivo, new BigDecimal(200), usuarioJoao);
		
		Assert.assertTrue(resumo.getQuantidadeLidos() == 6);
		
		Assert.assertTrue(resumo.getQuantidadeBaixados() == 2);
		
		Assert.assertTrue(resumo.getQuantidadeRejeitados() == 4);
		
		Assert.assertTrue(resumo.getQuantidadeBaixadosComDivergencia() == 0);
	}
	
	@Test
	public void testeBaixaManual() {
		
		PagamentoDTO pagamento = new PagamentoDTO();
		
		pagamento.setDataPagamento(DateUtil.adicionarDias(new Date(), 1));
		pagamento.setNossoNumero("1234567890127");
		pagamento.setNumeroRegistro(1);
		pagamento.setValorPagamento(new BigDecimal(100.00));
		
		PoliticaCobranca politicaPrincipal = this.politicaCobrancaRepository.buscarPoliticaCobrancaPrincipal();
		
		boletoServiceImpl.baixarBoleto(TipoBaixaCobranca.MANUAL, pagamento, usuarioJoao,
									   null, politicaPrincipal, distribuidor,
									   DateUtil.adicionarDias(new Date(), 1), null);
	}
	
	private ArquivoPagamentoBancoDTO criarArquivoPagamentoBanco() {
		
		List<PagamentoDTO> listaPagemento = new ArrayList<PagamentoDTO>();
		
		// Boleto já foi pago
		PagamentoDTO pagamento = new PagamentoDTO();
		pagamento.setDataPagamento(new Date());
		pagamento.setNossoNumero("1234567890123456");
		pagamento.setNumeroRegistro(1);
		pagamento.setValorPagamento(new BigDecimal(10.0));
		
		listaPagemento.add(pagamento);
		
		// Valor correto
		pagamento = new PagamentoDTO();
		pagamento.setDataPagamento(new Date());
		pagamento.setNossoNumero("1234567890124456");
		pagamento.setNumeroRegistro(1);
		pagamento.setValorPagamento(new BigDecimal(100.00));
		
		listaPagemento.add(pagamento);
		
		// Valor acima
		pagamento = new PagamentoDTO();
		pagamento.setDataPagamento(new Date());
		pagamento.setNossoNumero("1234567890125456");
		pagamento.setNumeroRegistro(1);
		pagamento.setValorPagamento(new BigDecimal(200.00));
		
		listaPagemento.add(pagamento);
		
		// Valor abaixo
		pagamento = new PagamentoDTO();
		pagamento.setDataPagamento(new Date());
		pagamento.setNossoNumero("1234567890126456");
		pagamento.setNumeroRegistro(1);
		pagamento.setValorPagamento(new BigDecimal(10.00));
		
		listaPagemento.add(pagamento);
		
		// Valor abaixo
		pagamento = new PagamentoDTO();
		pagamento.setDataPagamento(DateUtil.adicionarDias(new Date(), 1));
		pagamento.setNossoNumero("1234567890127456");
		pagamento.setNumeroRegistro(1);
		pagamento.setValorPagamento(new BigDecimal(10.00));
		
		listaPagemento.add(pagamento);
		
		// Boleto não existe na base
		pagamento = new PagamentoDTO();
		pagamento.setDataPagamento(new Date());
		pagamento.setNossoNumero("1111111111111111");
		pagamento.setNumeroRegistro(1);
		pagamento.setValorPagamento(new BigDecimal(10.0));
		
		listaPagemento.add(pagamento);
		
		ArquivoPagamentoBancoDTO arquivo = new ArquivoPagamentoBancoDTO();
		
		arquivo.setSomaPagamentos(new BigDecimal(200));
		arquivo.setNomeArquivo("arquivo.dat");
		arquivo.setListaPagemento(listaPagemento);
		
		return arquivo;
	}
	
}
