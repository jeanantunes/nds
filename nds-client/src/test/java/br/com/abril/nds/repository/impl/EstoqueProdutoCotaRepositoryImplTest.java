package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
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
import br.com.abril.nds.model.cadastro.FormaEmissao;
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
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
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
	
	private Cota cotaManoel1;
	private Cota cotaManoel2;
	
	private Lancamento lancamentoVeja1;
	private Lancamento lancamentoCaras1;
	
	@Before
	public void setup() {
		
		carteiraSemRegistro = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		
		save(carteiraSemRegistro);
		
		bancoHSBC = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
				  123456L, "1", "1", "Instrucoes.", "HSBC","BANCO HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		
		save(bancoHSBC);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"590033123647", "333", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);
		
		formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
												  BigDecimal.ONE, BigDecimal.ONE, null);

		save(formaBoleto);

		distribuidor = null;

		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		politicaSuspensao.setValor(new BigDecimal(0));

		distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), null);

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
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		save(produtoVeja);
		
		Produto produtoCaras = Fixture.produtoCaras(tipoProdutoRevista);
		save(produtoCaras);
		
		produtoEdicaoVeja1 =
			Fixture.produtoEdicao("1", 1L, 10, 14, new BigDecimal(0.1),
								  BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", 1L, produtoVeja, null, false);
		produtoEdicaoVeja1.setDesconto(null);
		save(produtoEdicaoVeja1);
		
		
		produtoEdicaoCaras1 = Fixture.produtoEdicao("1", 2L, 10, 14, new BigDecimal(0.1),
				  BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPA", 2L, produtoCaras, null, false);
		
		produtoEdicaoCaras1.setDesconto(new BigDecimal(8));
		save(produtoEdicaoCaras1);
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		PessoaFisica manoel =
			Fixture.pessoaFisica("123.456.789-00", "sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		cotaManoel1 = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel1);
		
		cotaManoel2 = Fixture.cota(1234, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel2);
		
		EstoqueProdutoCota estoqueProdutoCotaVeja1 =
			Fixture.estoqueProdutoCota(produtoEdicaoVeja1, cotaManoel1,
									   BigDecimal.TEN, BigDecimal.ONE);
		save(estoqueProdutoCotaVeja1);
		
		EstoqueProdutoCota estoqueProdutoCotaCaras1 =
				Fixture.estoqueProdutoCota(produtoEdicaoCaras1, cotaManoel1,
										   BigDecimal.TEN, BigDecimal.ONE);
		save(estoqueProdutoCotaCaras1);
		
		EstoqueProdutoCota estoqueProdutoCota2Veja1 =
			Fixture.estoqueProdutoCota(produtoEdicaoVeja1, cotaManoel2,
									   BigDecimal.TEN, BigDecimal.ONE);
		save(estoqueProdutoCota2Veja1);
		
		lancamentoVeja1 =
			Fixture.lancamentoExpedidos(TipoLancamento.LANCAMENTO, produtoEdicaoVeja1, new Date(),
										DateUtil.adicionarDias(new Date(), 1), new Date(), new Date(),
										BigDecimal.TEN, StatusLancamento.EXPEDIDO, null, null, 1);
		save(lancamentoVeja1);
		
		
		lancamentoCaras1 =
				Fixture.lancamentoExpedidos(TipoLancamento.LANCAMENTO, produtoEdicaoCaras1, new Date(),
											DateUtil.adicionarDias(new Date(), 1), new Date(), new Date(),
											BigDecimal.TEN, StatusLancamento.EXPEDIDO, null, null, 1);
		
		save(lancamentoCaras1);
		
		Estudo estudoVeja =
			Fixture.estudo(BigDecimal.ZERO, lancamentoVeja1.getDataLancamentoPrevista(),
						   produtoEdicaoVeja1);
		
		Estudo estudoCaras =
				Fixture.estudo(BigDecimal.ZERO, lancamentoCaras1.getDataLancamentoPrevista(),
							   produtoEdicaoCaras1);
		
		save(estudoVeja, estudoCaras);
		
		EstudoCota estudoCotaManoel1Veja =
			Fixture.estudoCota(BigDecimal.ZERO, BigDecimal.ZERO, estudoVeja, cotaManoel1);
		
		EstudoCota estudoCotaManoel2Veja =
			Fixture.estudoCota(BigDecimal.ZERO, BigDecimal.ZERO, estudoVeja, cotaManoel2);
		
		EstudoCota estudoCotaManoel1Caras =
				Fixture.estudoCota(BigDecimal.ZERO, BigDecimal.ZERO, estudoCaras, cotaManoel1);
		
		save(estudoCotaManoel1Veja, estudoCotaManoel2Veja, estudoCotaManoel1Caras);
	}
	
	public void criarDistribuidor() {
		
		carteiraSemRegistro = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		
		save(carteiraSemRegistro);
		
		bancoHSBC = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
				  123456L, "1", "1", "Instrucoes.", "HSBC","BANCO HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		
		save(bancoHSBC);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000147", "333.333.333.333", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);
		
		formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
												  BigDecimal.ONE, BigDecimal.ONE, null);

		save(formaBoleto);

		distribuidor = null;
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(distribuidor, formaBoleto, true, true, true, 1,"Assunto","Mansagem",true,FormaEmissao.INDIVIDUAL_BOX);

		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);
		
		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		politicaSuspensao.setValor(new BigDecimal(0));

		distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), politicasCobranca);

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
		
		politicaCobranca.setDistribuidor(distribuidor);
		save(politicaCobranca);
		
	}
	
	@Test
	public void buscarEstoqueProdutoCotaPorIdProdutEdicao() {
		
		Set<Long> idsLancamento = new TreeSet<Long>();
		
		idsLancamento.add(lancamentoVeja1.getId());
		idsLancamento.add(lancamentoCaras1.getId());
		
		List<EstoqueProdutoCota> listaEstoqueProdutoCota = 
			this.estoqueProdutoCotaRepository.buscarListaEstoqueProdutoCota(
				idsLancamento);
		
		Assert.assertNotNull(listaEstoqueProdutoCota);
		
		Assert.assertTrue(listaEstoqueProdutoCota.size() > 0);
	}
	
	@Test
	public void testObterValorTotalReparteCota() {
		
		List<Long> listaIdProdutoEdicao = new LinkedList<Long>();
		
		listaIdProdutoEdicao.add(produtoEdicaoVeja1.getId());
		
		listaIdProdutoEdicao.add(produtoEdicaoCaras1.getId());
		
		Long idDistribuidor = distribuidor.getId();
		
		Integer numeroCota = cotaManoel1.getNumeroCota();
		
		BigDecimal valorTotalReparteCota = 
				estoqueProdutoCotaRepository.obterValorTotalReparteCota(numeroCota, listaIdProdutoEdicao, idDistribuidor);
		
		Assert.assertEquals(364, valorTotalReparteCota.intValue());
		
	}
	
	@Test
	public void testObterFaturamentoCota() {
		Double faturamento = null;
		
		faturamento  = estoqueProdutoCotaRepository.obterFaturamentoCota(cotaManoel1.getId());
		
		Assert.assertTrue(faturamento.equals(108.0));
		
	}
}
