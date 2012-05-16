package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Moeda;
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
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;

public class ProdutoEdicaoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ProdutoEdicaoRepositoryImpl produtoEdicaoRepository;

	private FormaCobranca formaBoleto;
	
	private Carteira carteiraSemRegistro;
	
	private Distribuidor distribuidor;
	
	private Banco bancoHSBC;
	
	private Cota cotaManoel;
	
	private Cota cotaComDesconto;
	
	private ProdutoEdicao produtoEdicaoVeja;
	
	private ProdutoEdicao produtoEdicaoComDesconto;
	
	
	@Before
	public void setUp() {
		
		//////////////

		carteiraSemRegistro = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		
		save(carteiraSemRegistro);
		
		bancoHSBC = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
				  123456L, "1", "1", "Instrucoes.", Moeda.REAL, "HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		
		save(bancoHSBC);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"590033123647", "333.333.333.333", "distrib_acme@mail.com", "99.999-9");
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
		
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		PessoaFisica manoel =
			Fixture.pessoaFisica("123.456.789-00", "sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		cotaComDesconto = Fixture.cota(456, manoel, SituacaoCadastro.ATIVO, box1);
		cotaComDesconto.setFatorDesconto(new BigDecimal(25));
		save(cotaComDesconto);
		
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor dinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(dinap);
		
		TipoProduto tipoProduto = Fixture.tipoRevista();
		save(tipoProduto);

		//////
		Produto produto = Fixture.produtoVeja(tipoProduto);
		produto.addFornecedor(dinap);
		produto.setEditor(abril);
		save(produto);

		produtoEdicaoVeja = Fixture.produtoEdicao(1L, 10, 14, new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQRSTU", 1L, produto);
		produtoEdicaoVeja.setDesconto(null);
		save(produtoEdicaoVeja);
		//////
		
		//////
		Produto produtoComDesconto = Fixture.produto("8001", "Novo", "Novo", PeriodicidadeProduto.ANUAL, tipoProduto);
		produtoComDesconto.addFornecedor(dinap);
		produtoComDesconto.setEditor(abril);
		save(produtoComDesconto);

		produtoEdicaoComDesconto = Fixture.produtoEdicao(2L, 10, 14, new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQRST", 2L, produtoComDesconto);
		produtoEdicaoComDesconto.setDesconto(new BigDecimal(19));
		save(produtoEdicaoComDesconto);
		//////
		
		
		Lancamento lancamento = 
				Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicaoVeja, 
						new Date(), new Date(), new Date(), new Date(), BigDecimal.TEN, StatusLancamento.CONFIRMADO, null, 1);
		save(lancamento);
		
		Estudo estudo = 
				Fixture.estudo(BigDecimal.TEN, new Date(), produtoEdicaoVeja);
		save(estudo);
	}
	
	@Test
	public void testObterPercentualComissionamento() {

		Long 	idDistribuidor 	= distribuidor.getId();
		
		//////////////////////

		Long 	idProdutoEdicao = produtoEdicaoComDesconto.getId();
		Integer numeroCota 		= cotaComDesconto.getNumeroCota();

		BigDecimal fatorDesconto = produtoEdicaoRepository.obterFatorDesconto(idProdutoEdicao, numeroCota, idDistribuidor);
		Assert.assertEquals(19, fatorDesconto.intValue());

		//////////////////////
		
		idProdutoEdicao = produtoEdicaoVeja.getId();
		numeroCota = cotaComDesconto.getNumeroCota();
		
		fatorDesconto = produtoEdicaoRepository.obterFatorDesconto(idProdutoEdicao, numeroCota, idDistribuidor);
		Assert.assertEquals(25, fatorDesconto.intValue());
		
		//////////////////////
		
		idProdutoEdicao = produtoEdicaoVeja.getId();
		numeroCota = cotaManoel.getNumeroCota();
		
		fatorDesconto = produtoEdicaoRepository.obterFatorDesconto(idProdutoEdicao, numeroCota, idDistribuidor);
		Assert.assertEquals(10, fatorDesconto.intValue());

		//////////////////////

		
		
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
	
	@Test
	public void obterProdutoEdicaoPorCodigoEdicaoDataLancamento(){
		FuroProdutoDTO furoProdutoDTO = 
				produtoEdicaoRepository.obterProdutoEdicaoPorCodigoEdicaoDataLancamento("1", null, 1L, new Date());
		
		Assert.assertTrue(furoProdutoDTO != null);
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
	
}
