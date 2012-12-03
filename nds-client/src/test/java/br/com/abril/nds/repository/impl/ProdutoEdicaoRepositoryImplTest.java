package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.util.Intervalo;

public class ProdutoEdicaoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ProdutoEdicaoRepositoryImpl produtoEdicaoRepository;

	private FormaCobranca formaBoleto;
	
	private Distribuidor distribuidor;
	
	private Banco bancoHSBC;
	
	private Cota cotaManoel;
	
	private Cota cotaComDesconto;
	
	private ProdutoEdicao produtoEdicaoVeja;
	
	private ProdutoEdicao produtoEdicaoComDesconto;
	
	
	@Before
	public void setUp() {
		
		bancoHSBC = Fixture.banco(10L, true, 30, "1010",
				  123456L, "1", "1", "Instrucoes.", "HSBC","BANCO HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		
		save(bancoHSBC);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"590033123647", "33333333333", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);
		
		formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
												  BigDecimal.ONE, BigDecimal.ONE, null);

		save(formaBoleto);

		distribuidor = null;

		distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), null);

		distribuidor.setPoliticaSuspensao(null);
		
		ParametroContratoCota parametroContrato = Fixture.criarParametroContratoCota("<font color='blue'><b>CONSIDERANDO QUE:</b></font><br>"+
																					 "<br>"+"<b>(i)</b>	A Contratante contempla, dentro de seu objeto social, a atividade de distribuição de livros, jornais, revistas, impressos e publicações em geral e, portanto, necessita de serviços de transporte de revistas;"+
																					 "<br>"+"<b>(ii)</b>	A Contratada é empresa especializada e, por isso, capaz de prestar serviços de transportes, bem como declara que possui qualificação técnica e documentação necessária para a prestação dos serviços citados acima;"+
																					 "<br>"+"<b>(iii)</b>	A Contratante deseja contratar a Contratada para a prestação dos serviços de transporte de revistas;"+
																					 "<br>"+"RESOLVEM, mútua e reciprocamente, celebrar o presente Contrato de Prestação de Serviços de Transporte de Revistas (“Contrato”), que se obrigam a cumprir, por si e seus eventuais sucessores a qualquer título, em conformidade com os termos e condições a seguir:"+
																					 "<br><br>"+"<font color='blue'><b>1.	OBJETO DO CONTRATO</b><br></font>"+
																					 "<br>"+"<b>1.1.</b>	O presente contrato tem por objeto a prestação dos serviços pela Contratada de transporte de revistas, sob sua exclusiva responsabilidade, sem qualquer relação de subordinação com a Contratante e dentro da melhor técnica, diligência, zelo e probidade, consistindo na disponibilização de veículos e motoristas que atendam a demanda da Contratante."
																					 , "neste ato, por seus representantes infra-assinados, doravante denominada simplesmente CONTRATADA.", 30, 30);
		save(parametroContrato);
		
		distribuidor.setParametroContratoCota(parametroContrato);

		distribuidor.setFatorDesconto(BigDecimal.TEN);
		
		save(distribuidor);			
		
		//////////////
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		PessoaFisica manoel =
			Fixture.pessoaFisica("123.456.789-00", "sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		cotaComDesconto = Fixture.cota(456, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaComDesconto);
		
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor dinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(dinap);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProduto = Fixture.tipoRevista(ncmRevistas);
		save(tipoProduto);

		//////
		Produto produto = Fixture.produtoVeja(tipoProduto);
		produto.addFornecedor(dinap);
		produto.setEditor(abril);
		save(produto);

		produtoEdicaoVeja = Fixture.produtoEdicao(1L, 10, 14, new Long(100), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", produto, null, false);
		save(produtoEdicaoVeja);
		//////
		
		//////
		Produto produtoComDesconto = Fixture.produto("8001", "Novo", "Novo", PeriodicidadeProduto.ANUAL, tipoProduto, 5, 5, new Long(100), TributacaoFiscal. TRIBUTADO);
		produtoComDesconto.addFornecedor(dinap);
		produtoComDesconto.setEditor(abril);
		save(produtoComDesconto);

		produtoEdicaoComDesconto = Fixture.produtoEdicao(2L, 10, 14, new Long(100), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", produtoComDesconto, null, false);
		save(produtoEdicaoComDesconto);
		//////
		
		
		Lancamento lancamento = 
				Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicaoVeja, 
						new Date(), new Date(), new Date(), new Date(), BigInteger.TEN, StatusLancamento.CONFIRMADO, null, 1);
		save(lancamento);
		
		Estudo estudo = 
				Fixture.estudo(BigInteger.TEN, new Date(), produtoEdicaoVeja);
		save(estudo);
	}
	
	@Test
	public void obterProdutoEdicaoPorNomeProduto() {
		List<ProdutoEdicao> listaProdutoEdicao = 
			produtoEdicaoRepository.obterProdutoEdicaoPorNomeProduto("Veja");
		
		Assert.assertTrue(!listaProdutoEdicao.isEmpty());
	}
	
	@Test
	public void obterProdutoEdicaoPorCodProdutoNumEdicao() {
		ProdutoEdicao produtoEdicao = 
			produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao("1", 1L);
		
		Assert.assertTrue(produtoEdicao != null);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterProdutoEdicaoPorCodigoEdicaoDataLancamento(){
		FuroProdutoDTO furoProdutoDTO = 
				produtoEdicaoRepository.obterProdutoEdicaoPorCodigoEdicaoDataLancamento("1", null, 1L, new Date(), true);
	}
	
	@Test
	public void obterListaProdutoEdicao() {
		
		Produto produto = new Produto();
		produto.setId(1L);
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setNumeroEdicao(1L);
		
		
		@SuppressWarnings("unused")
		List<ProdutoEdicao> listaProdutoEdicao = 
				produtoEdicaoRepository.obterListaProdutoEdicao(produto, produtoEdicao);
		
	}
	
	@Test
	public void obterProdutoEdicaoPorCodigoBarra() {
		
		String codigoBarra = "147a7a77a7s";
		
			ProdutoEdicao produtoEdicao =
						produtoEdicaoRepository.obterProdutoEdicaoPorCodigoBarra(codigoBarra);
		}
	
	@Test
	public void obterProdutosEdicaoPorCodigoProduto() {
		
		String codigoProduto = "55b";
		
			List<ProdutoEdicao> produtoEdicao =
						produtoEdicaoRepository.obterProdutosEdicaoPorCodigoProduto(codigoProduto);
			
			Assert.assertNotNull(produtoEdicao);
		}
	
	@Test
	public void obterProdutoPorCodigoNome() {
		
		String codigoNomeProduto = "produto55b";
		
			List<ProdutoEdicao> produtoEdicao =
						produtoEdicaoRepository.obterProdutoPorCodigoNome(codigoNomeProduto, 10);
			
			Assert.assertNotNull(produtoEdicao);
	}
	
	@Test
	public void obterProdutosEdicaoPorId() {
		
		Set<Long> idsProdutoEdicao = new TreeSet<Long>();
		
		idsProdutoEdicao.add(produtoEdicaoVeja.getId());
		idsProdutoEdicao.add(produtoEdicaoComDesconto.getId());
		
		List<ProdutoEdicao> listaProdutoEdicao =
			produtoEdicaoRepository.obterProdutosEdicaoPorId(idsProdutoEdicao);
		
		Assert.assertNotNull(listaProdutoEdicao);
		
		Assert.assertTrue(listaProdutoEdicao.size() == idsProdutoEdicao.size());
	}
	
	@Test
	public void obterProdutoEdicaoPorSequenciaMatriz() {
		
		Integer sequenciaMatriz = 1;
		
			ProdutoEdicao produtoEdicao =
						produtoEdicaoRepository.obterProdutoEdicaoPorSequenciaMatriz(sequenciaMatriz);
			
		}
	
	@Test
	public void obterCodigoMatrizPorProdutoEdicao() {
		
		Long idProdutoEdicao = 1L;
		
				produtoEdicaoRepository.obterCodigoMatrizPorProdutoEdicao(idProdutoEdicao);
			
		}
	
	@Test
	public void pesquisarEdicoesCodigoProduto() {
		
		String codigoProduto = "1B";
		
		List<ProdutoEdicaoDTO> produtoEdicaoDTOs =
			produtoEdicaoRepository.pesquisarEdicoes(codigoProduto, null, null, null, null, null, false, null, null, 0, 0);
		
		Assert.assertNotNull(produtoEdicaoDTOs);
			
		}
	
	@Test
	public void pesquisarEdicoesNome() {
		
		String nome = "nomeTest";
		
		List<ProdutoEdicaoDTO> produtoEdicaoDTOs =
			produtoEdicaoRepository.pesquisarEdicoes(null, nome, null, null, null, null, false, null, null, 0, 0);
		
		Assert.assertNotNull(produtoEdicaoDTOs);
			
	}
	
	@Test
	public void pesquisarEdicoesDataLancamento() {
		
		Intervalo<Date> dataLancamento = new Intervalo<Date>();
		dataLancamento.setDe(Fixture.criarData(13, Calendar.OCTOBER, 2012));
		dataLancamento.setAte(Fixture.criarData(13, Calendar.NOVEMBER, 2012));
		
		List<ProdutoEdicaoDTO> produtoEdicaoDTOs =
			produtoEdicaoRepository.pesquisarEdicoes(null, null, dataLancamento, null, null, null, false, null, null, 0, 0);
		
		Assert.assertNotNull(produtoEdicaoDTOs);
			
		}
	
	@Test
	public void pesquisarEdicoesPreco() {
		
		Intervalo<BigDecimal> preco = new Intervalo<BigDecimal>();
		preco.setDe(BigDecimal.ONE);
		preco.setAte(BigDecimal.TEN);
		
		List<ProdutoEdicaoDTO> produtoEdicaoDTOs =
			produtoEdicaoRepository.pesquisarEdicoes(null, null, null, preco, null, null, false, null, null, 0, 0);
		
		Assert.assertNotNull(produtoEdicaoDTOs);
			
		}
	
	@Test
	public void pesquisarEdicoesStatusLancamento() {
		
		StatusLancamento status = StatusLancamento.CALCULADO;
		
		List<ProdutoEdicaoDTO> produtoEdicaoDTOs =
			produtoEdicaoRepository.pesquisarEdicoes(null, null, null, null, status, null, false, null, null, 0, 0);
		
		Assert.assertNotNull(produtoEdicaoDTOs);
			
		}
	
	@Test
	public void pesquisarEdicoesCodigoDeBarras() {
		
		String codigoDeBarras = "4a4s4d5e";
		
		List<ProdutoEdicaoDTO> produtoEdicaoDTOs =
			produtoEdicaoRepository.pesquisarEdicoes(null, null, null, null, null, codigoDeBarras, false, null, null, 0, 0);
		
		Assert.assertNotNull(produtoEdicaoDTOs);
			
	}
	
	@Test
	public void pesquisarEdicoesBrinde() {
		
		Boolean brinde = true;
		
		List<ProdutoEdicaoDTO> produtoEdicaoDTOs =
			produtoEdicaoRepository.pesquisarEdicoes(null, null, null, null, null, null, brinde, null, null, 0, 0);
		
		Assert.assertNotNull(produtoEdicaoDTOs);
			
		}
	
	@Test
	public void pesquisarEdicoesSortOrderSortName() {
		
		String sortOrder = "Asc";
		String sortName = "nomeComercial";
		
		List<ProdutoEdicaoDTO> produtoEdicaoDTOs =
			produtoEdicaoRepository.pesquisarEdicoes(null, null, null, null, null, null, false, sortOrder, sortName, 0, 0);
		
		Assert.assertNotNull(produtoEdicaoDTOs);
			
		}
	
	@Test
	public void pesquisarEdicoesInitialResult() {
		
		int initialResult = 1;
		
		List<ProdutoEdicaoDTO> produtoEdicaoDTOs =
			produtoEdicaoRepository.pesquisarEdicoes(null, null, null, null, null, null, false, null, null, initialResult, 0);
		
		Assert.assertNotNull(produtoEdicaoDTOs);
			
		}
	
	@Test
	public void countPesquisarEdicoes() {
		
		String codigoProduto = "1a";
		
		produtoEdicaoRepository.countPesquisarEdicoes(codigoProduto, null, null, null, null, null, false);
					
	}
	
	@Test
	public void pesquisarUltimasEdicoes() {
		
		String codigoProduto = "1a";
		int qntEdicoes = 5;
		
		
		List<ProdutoEdicaoDTO> produtoEdicaoDTOs =
			produtoEdicaoRepository.pesquisarUltimasEdicoes(codigoProduto, qntEdicoes);
		
		Assert.assertNotNull(produtoEdicaoDTOs);
			
		}
	
	@Test
	public void hasProdutoEdicao() {
		
		Produto produto = new Produto();
		produto.setId(1L);
		
		Boolean produtoEdicao = 
			produtoEdicaoRepository.hasProdutoEdicao(produto);
		
		Assert.assertFalse(produtoEdicao);
			
		}
	
	@Test
	public void isProdutoEdicaoJaPublicada() {
		
		Long idProdutoEdicao = 1L;
		
		Boolean produtoEdicaoJaPublicada = 
			produtoEdicaoRepository.isProdutoEdicaoJaPublicada(idProdutoEdicao);
		
		Assert.assertFalse(produtoEdicaoJaPublicada);
			
		}
	
	@Test
	public void isNumeroEdicaoCadastrada() {
		
		String codigoProduto = "14s";
		Long numeroEdicao = 1L;
		Long idProdutoEdicao = 1L;
		
		Boolean produtoEdicaoCadastrada= 
			produtoEdicaoRepository.isNumeroEdicaoCadastrada(codigoProduto, numeroEdicao, idProdutoEdicao);
		
		Assert.assertFalse(produtoEdicaoCadastrada);
			
		}
	
	@Test
	public void obterProdutoEdicaoPorCodigoDeBarra() {
		
		String codigoBarras = "14s3rer45dg44";
		Long idProdutoEdicao = 1L;
		
		List<ProdutoEdicao> produtoEdicao = 
			produtoEdicaoRepository.obterProdutoEdicaoPorCodigoDeBarra(codigoBarras, idProdutoEdicao);
		
		Assert.assertNotNull(produtoEdicao);
			
		}
	
	@Test
	public void obterProdutoEdicaoPorProdutoEEdicaoOuNome() {
			
		Long idProduto = 1L;
		Long numeroEdicao = 1L;
		String nome = "produtoTest";
				
		ProdutoEdicao produtoEdicao = 
			produtoEdicaoRepository.obterProdutoEdicaoPorProdutoEEdicaoOuNome(idProduto, numeroEdicao, nome);
		
		}
	
	@Test
	public void obterQuantidadeEdicoesPorCodigoProduto() {
			
		String codigoProduto = "11d";
		
			produtoEdicaoRepository.obterQuantidadeEdicoesPorCodigoProduto(codigoProduto);
		
		}
	
	@Test
	public void obterProdutosEdicoesPorCodigoProdutoLimitado() {
			
		String codigoProduto = "11d";
		Integer limite = 3;
		
		List<ProdutoEdicao> produtoEdicao = 
				produtoEdicaoRepository.obterProdutosEdicoesPorCodigoProdutoLimitado(codigoProduto, limite);
		
		Assert.assertNotNull(produtoEdicao);
		
		}
	
	@Test
	public void obterProdutosEdicaoPorFornecedor() {
			
		Long idFornecedor = 1L;
		
		Set <ProdutoEdicao> produtoEdicao = 
				produtoEdicaoRepository.obterProdutosEdicaoPorFornecedor(idFornecedor);
		
		Assert.assertNotNull(produtoEdicao);
		
		}
	
	@Test
	public void buscarProdutosLancadosData() {
			
		Date data = Fixture.criarData(14, Calendar.OCTOBER, 2012);
		
		List <ProdutoEdicao> produtoEdicao = 
				produtoEdicaoRepository.buscarProdutosLancadosData(data);
		
		Assert.assertNotNull(produtoEdicao);
		
		}
	
	@Test
	public void buscarNome() {
			
		Long idProdutoEdicao = 1L;
		
						produtoEdicaoRepository.buscarNome(idProdutoEdicao);
				
		}
	
	@Test
	public void obterUltimoNumeroEdicao() {
			
		String codigoProduto = "84s";
		
						produtoEdicaoRepository.obterUltimoNumeroEdicao(codigoProduto);
				
		}
	
	@Test
	public void filtrarDescontoProdutoEdicaoPorDistribuidor() {
		
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setId(1L);
		
		Fornecedor fornecedor2 = new Fornecedor();
		fornecedor2.setId(2L);

		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>(); 
		fornecedores.add(fornecedor);
		fornecedores.add(fornecedor2);
		
		Set<ProdutoEdicao> produtoEdicao =  produtoEdicaoRepository.filtrarDescontoProdutoEdicaoPorDistribuidor(fornecedores);
		
		Assert.assertNotNull(produtoEdicao);
				
		}
	
	@Test
	public void filtrarDescontoProdutoEdicaoPorCota() {
		
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setId(1L);
		
		Fornecedor fornecedor2 = new Fornecedor();
		fornecedor2.setId(2L);
		
		Cota cota = new Cota();
		cota.setId(1L);
		
		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>(); 
		fornecedores.add(fornecedor);
		fornecedores.add(fornecedor2);
		
		Set<ProdutoEdicao> produtoEdicao =  produtoEdicaoRepository.filtrarDescontoProdutoEdicaoPorCota(cota, fornecedores);
		
		Assert.assertNotNull(produtoEdicao);
				
	}

	@SuppressWarnings("unused")
	@Test
	public void validarExpedicaoFisicaProdutoEdicao(){
		boolean validar = produtoEdicaoRepository.validarExpedicaoFisicaProdutoEdicao(produtoEdicaoVeja);
	}

}
