package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.GeracaoNotaEnvioService;

public class GeracaoNotaEnvioServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private GeracaoNotaEnvioService geracaoNotaEnvioService;
	private Cota cotaManoel;
	private Cota cotaComDesconto;
	private ProdutoEdicao produtoEdicaoVeja;
	private ProdutoEdicao produtoEdicaoComDesconto;
	private TipoNotaFiscal tipoNotaFiscalDevolucao;
	private Banco bancoHSBC;
	private FormaCobranca formaBoleto;
	private Distribuidor distribuidor;
	
	
	
	@Before
	public void setUp(){
		
		tipoNotaFiscalDevolucao = Fixture.tipoNotaFiscalDevolucao();
		CFOP cfop1209 =Fixture.cfop1209();
		CFOP cfop1210 =Fixture.cfop1210();
		tipoNotaFiscalDevolucao.setCfopEstado(cfop1209);
		tipoNotaFiscalDevolucao.setCfopOutrosEstados(cfop1210);
		save(cfop1209,cfop1210,tipoNotaFiscalDevolucao);
		produtoEdicaoSetup();
	}

	/**
	 * 
	 */
	private void produtoEdicaoSetup() {
		
		bancoHSBC = Fixture.banco(10L, true, 30, "1010",
				123456L, "1", "1", "Instrucoes.", "HSBC","BANCO HSBC", "399",
				BigDecimal.ZERO, BigDecimal.ZERO);

		save(bancoHSBC);

		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica(
				"Distribuidor Acme", "590033123647", "333333333333",
				"distrib_acme@mail.com", "999999");
		save(juridicaDistrib);

		formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200),
				true, bancoHSBC, BigDecimal.ONE, BigDecimal.ONE, null);

		save(formaBoleto);


		distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(),
				null);
		

		distribuidor.setPoliticaSuspensao(null);

		ParametroContratoCota parametroContrato = Fixture
				.criarParametroContratoCota(
						"<font color='blue'><b>CONSIDERANDO QUE:</b></font><br>"
								+ "<br>"
								+ "<b>(i)</b>	A Contratante contempla, dentro de seu objeto social, a atividade de distribuição de livros, jornais, revistas, impressos e publicações em geral e, portanto, necessita de serviços de transporte de revistas;"
								+ "<br>"
								+ "<b>(ii)</b>	A Contratada é empresa especializada e, por isso, capaz de prestar serviços de transportes, bem como declara que possui qualificação técnica e documentação necessária para a prestação dos serviços citados acima;"
								+ "<br>"
								+ "<b>(iii)</b>	A Contratante deseja contratar a Contratada para a prestação dos serviços de transporte de revistas;"
								+ "<br>"
								+ "RESOLVEM, mútua e reciprocamente, celebrar o presente Contrato de Prestação de Serviços de Transporte de Revistas (“Contrato”), que se obrigam a cumprir, por si e seus eventuais sucessores a qualquer título, em conformidade com os termos e condições a seguir:"
								+ "<br><br>"
								+ "<font color='blue'><b>1.	OBJETO DO CONTRATO</b><br></font>"
								+ "<br>"
								+ "<b>1.1.</b>	O presente contrato tem por objeto a prestação dos serviços pela Contratada de transporte de revistas, sob sua exclusiva responsabilidade, sem qualquer relação de subordinação com a Contratante e dentro da melhor técnica, diligência, zelo e probidade, consistindo na disponibilização de veículos e motoristas que atendam a demanda da Contratante.",
						"neste ato, por seus representantes infra-assinados, doravante denominada simplesmente CONTRATADA.",
						30, 30);
		save(parametroContrato);

		distribuidor.setParametroContratoCota(parametroContrato);

		distribuidor.setFatorDesconto(BigDecimal.TEN);

		save(distribuidor);
		
		Endereco endereco = Fixture.criarEndereco(
				TipoEndereco.COBRANCA, "13222-020", "Rua João de Souza", "51", "Centro", "São Paulo", "SP",1);
		save(endereco);
		EnderecoDistribuidor enderecoDistribuidor = Fixture.enderecoDistribuidor(distribuidor, endereco, true, TipoEndereco.COBRANCA);
		
		save(enderecoDistribuidor);
		
		Telefone telefone = Fixture.telefone("019", "259633", "012");

		TelefoneDistribuidor teDistribuidor = Fixture.telefoneDistribuidor(distribuidor, true, telefone, TipoTelefone.COMERCIAL);

		save(telefone,teDistribuidor);

		// ////////////

		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);

		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);

		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		
		Endereco enderecoCotaManotel = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "13730-000", "Rua Marechal Deodoro", "50", "Centro", "Mococa", "SP",1);

		EnderecoCota enderecoCota = new EnderecoCota();
		enderecoCota.setCota(cotaManoel);
		enderecoCota.setEndereco(enderecoCotaManotel);
		enderecoCota.setPrincipal(true);
		enderecoCota.setTipoEndereco(TipoEndereco.COBRANCA);
		save(enderecoCotaManotel,enderecoCota);
		
		Telefone telefoneManoel = Fixture.telefone("19", "12345678", null);
		TelefoneCota telefoneCota = new TelefoneCota();
		
		telefoneCota.setPrincipal(true);
		telefoneCota.setTelefone(telefoneManoel);
		telefoneCota.setTipoTelefone(TipoTelefone.COMERCIAL);
		telefoneCota.setCota(cotaManoel);
		
		save(telefoneManoel,telefoneCota);

		cotaComDesconto = Fixture.cota(456, manoel, SituacaoCadastro.ATIVO,
				box1);
		save(cotaComDesconto);

		Editor abril = Fixture.editoraAbril();
		save(abril);

		TipoFornecedor tipoFornecedorPublicacao = Fixture
				.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);

		Fornecedor dinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(dinap);

		NCM ncmRevistas = Fixture.ncm(49029000l, "REVISTAS", "KG");
		save(ncmRevistas);

		TipoProduto tipoProduto = Fixture.tipoRevista(ncmRevistas);
		save(tipoProduto);

		// ////
		Produto produto = Fixture.produtoVeja(tipoProduto);
		produto.addFornecedor(dinap);
		produto.setEditor(abril);
		save(produto);

		produtoEdicaoVeja = Fixture.produtoEdicao("1", 1L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"798765431", 1L, produto, null, false);
		save(produtoEdicaoVeja);
		// ////

		// ////
		Produto produtoComDesconto = Fixture.produto("8001", "Novo", "Novo",
				PeriodicidadeProduto.ANUAL, tipoProduto, 5, 5, new Long(100), TributacaoFiscal.TRIBUTADO);
		produtoComDesconto.addFornecedor(dinap);
		produtoComDesconto.setEditor(abril);
		save(produtoComDesconto);

		produtoEdicaoComDesconto = Fixture.produtoEdicao("1", 2L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"798765431", 2L, produtoComDesconto, null, false);
		save(produtoEdicaoComDesconto);
		// ////

		Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO,
				produtoEdicaoVeja, new Date(), new Date(), new Date(),
				new Date(), BigInteger.TEN, StatusLancamento.CONFIRMADO, null,
				1);
		save(lancamento);

		Estudo estudo = Fixture.estudo(BigInteger.TEN, new Date(),
				produtoEdicaoVeja);
		save(estudo);
	}
	
	
	@Test
	public void buscaTest(){
		List<CotaExemplaresDTO>  lista = geracaoNotaEnvioService.busca(null, null, null, null, null, null, null, null, null, null, null);
		System.out.println(lista);
	}
	

}
