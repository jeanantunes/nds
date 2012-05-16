package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.util.DateUtil;

public class EstoqueProdutoCotaRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	private static ProdutoEdicao produtoEdicaoVeja1;

	private static ProdutoEdicao produtoEdicaoCaras1;
	
	private static FormaCobranca formaBoleto;

	private static Carteira carteiraSemRegistro;
	
	private static Distribuidor distribuidor;
	
	private static Banco bancoHSBC;
	
	private Cota cotaManoel;
	
	@Before
	public void setup() {
		
		criarDistribuidor();
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista();
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		save(produtoVeja);
		
		Produto produtoCaras = Fixture.produtoCaras(tipoProdutoRevista);
		save(produtoCaras);
		
		produtoEdicaoVeja1 =
			Fixture.produtoEdicao(1L, 10, 14, new BigDecimal(0.1),
								  BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQRSTU", 1L, produtoVeja);
		produtoEdicaoVeja1.setDesconto(null);
		save(produtoEdicaoVeja1);
		
		
		produtoEdicaoCaras1 = Fixture.produtoEdicao(2L, 10, 14, new BigDecimal(0.1),
				  BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQRST", 2L, produtoCaras);
		
		produtoEdicaoCaras1.setDesconto(new BigDecimal(8));
		save(produtoEdicaoCaras1);
		
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		PessoaFisica manoel =
			Fixture.pessoaFisica("123.456.789-00", "sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		EstoqueProdutoCota estoqueProdutoCotaVeja1 =
			Fixture.estoqueProdutoCota(produtoEdicaoVeja1, cotaManoel,
									   BigDecimal.TEN, BigDecimal.ONE);
		save(estoqueProdutoCotaVeja1);
		
		EstoqueProdutoCota estoqueProdutoCotaCaras1 =
				Fixture.estoqueProdutoCota(produtoEdicaoCaras1, cotaManoel,
										   BigDecimal.TEN, BigDecimal.ONE);
			save(estoqueProdutoCotaCaras1);
		
		Lancamento lancamentoVeja1 =
			Fixture.lancamentoExpedidos(TipoLancamento.LANCAMENTO, produtoEdicaoVeja1, new Date(),
										DateUtil.adicionarDias(new Date(), 1), new Date(), new Date(),
										BigDecimal.TEN, StatusLancamento.EXPEDIDO, null, null, 1);
		save(lancamentoVeja1);
		
		
		Lancamento lancamentoCaras1 =
				Fixture.lancamentoExpedidos(TipoLancamento.LANCAMENTO, produtoEdicaoCaras1, new Date(),
											DateUtil.adicionarDias(new Date(), 1), new Date(), new Date(),
											BigDecimal.TEN, StatusLancamento.EXPEDIDO, null, null, 1);
		save(lancamentoCaras1);
		
		
	}
	
	public void criarDistribuidor() {
		
		carteiraSemRegistro = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		
		save(carteiraSemRegistro);
		
		bancoHSBC = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
				  123456L, "1", "1", "Instrucoes.", Moeda.REAL, "HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		
		save(bancoHSBC);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000147", "333.333.333.333", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);
		
		formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
												  BigDecimal.ONE, BigDecimal.ONE, null);

		save(formaBoleto);

		distribuidor = null;
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(distribuidor, formaBoleto, true, true, true, 1,"Assunto","Mansagem");

		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		politicaSuspensao.setValor(new BigDecimal(0));

		distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), politicaCobranca);
		distribuidor.getFormasCobranca().add(formaBoleto);

		distribuidor.setPoliticaSuspensao(politicaSuspensao);
		
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
		
	}
	
	@Test
	public void buscarEstoqueProdutoCotaPorIdProdutEdicao() {
		
		Set<Long> idsProdutoEdicao = new TreeSet<Long>();
		
		idsProdutoEdicao.add(produtoEdicaoVeja1.getId());
		
		List<EstoqueProdutoCota> listaEstoqueProdutoCota = 
			this.estoqueProdutoCotaRepository.buscarEstoquesProdutoCotaPorIdProdutEdicao(
				idsProdutoEdicao);
		
		Assert.assertNotNull(listaEstoqueProdutoCota);
		
		Assert.assertTrue(!listaEstoqueProdutoCota.isEmpty());
	}
	
	@Test
	public void testObterValorTotalReparteCota() {
		
		List<Long> listaIdProdutoEdicao = new LinkedList<Long>();
		
		listaIdProdutoEdicao.add(produtoEdicaoVeja1.getId());
		
		listaIdProdutoEdicao.add(produtoEdicaoCaras1.getId());
		
		Long idDistribuidor = distribuidor.getId();
		
		Integer numeroCota = cotaManoel.getNumeroCota();
		
		BigDecimal valorTotalReparteCota = 
				estoqueProdutoCotaRepository.obterValorTotalReparteCota(numeroCota, listaIdProdutoEdicao, idDistribuidor);
		
		Assert.assertEquals(364, valorTotalReparteCota.intValue());
		
	}
	
}
