package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
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
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiroProduto;
import br.com.abril.nds.model.fiscal.nota.ICMS;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.Origem;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.model.fiscal.nota.ValoresRetencoesTributos;
import br.com.abril.nds.model.fiscal.nota.Veiculo;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.NotaFiscalService;

public class NotaFiscalServiceImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private NotaFiscalService notaFiscalService;

	private HashMap<StatusProcessamentoInterno, NotaFiscal> listaNotasFiscais = new HashMap<StatusProcessamentoInterno, NotaFiscal>();

	private List<NotaFiscal> notasParaTesteArquivo = new ArrayList<NotaFiscal>();
	private FormaCobranca formaBoleto;

	private Distribuidor distribuidor;

	private Banco bancoHSBC;

	private Cota cotaManoel;

	private Cota cotaComDesconto;

	private ProdutoEdicao produtoEdicaoVeja;

	private ProdutoEdicao produtoEdicaoComDesconto;

	private TipoNotaFiscal tipoNotaFiscalDevolucao;

	@Before
	public void setup() {

		save(Fixture.parametroSistema(
				TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO,
				"C:\\notas\\"));
//		for (int i = 0; i < 5; i++) {
//			NotaFiscal notaTesteArquivo = this.gerarNFE(
//					"33111102737654003496550550000483081131621856",
//					"87416762464", StatusProcessamentoInterno.GERADA,
//					Status.SERVICO_EM_OPERACAO);
//			notaTesteArquivo.setId((long) i);
//			notaTesteArquivo.setProdutosServicos(this
//					.gerarListaProdutoServico((long) i));
//
//			this.notasParaTesteArquivo.add(notaTesteArquivo);
//		}
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

		distribuidor = null;

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
	public void testExportarNotasFiscais() {
		try {

			this.notaFiscalService
					.exportarNotasFiscais(new ArrayList<NotaFiscal>());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	@Ignore
	public void testSumarizarNotasFiscais() {

		List<RetornoNFEDTO> listaDadosRetornoNFE = new ArrayList<RetornoNFEDTO>();

		RetornoNFEDTO notaCanceladaRetornoNFE = new RetornoNFEDTO();

		notaCanceladaRetornoNFE
				.setChaveAcesso("33111102737654003496550550000483081131621856");
		notaCanceladaRetornoNFE.setCpfCnpj("37712543534");
		notaCanceladaRetornoNFE.setDataRecebimento(new Date());
		notaCanceladaRetornoNFE.setIdNotaFiscal(listaNotasFiscais.get(
				StatusProcessamentoInterno.RETORNADA).getId());
		notaCanceladaRetornoNFE.setProtocolo(null);
		notaCanceladaRetornoNFE.setStatus(Status.CANCELAMENTO_HOMOLOGADO);
		notaCanceladaRetornoNFE.setMotivo("motivo de cancelamento da nota");

		listaDadosRetornoNFE.add(notaCanceladaRetornoNFE);

		RetornoNFEDTO notaAutorizadaRetornoNFE = new RetornoNFEDTO();

		notaAutorizadaRetornoNFE
				.setChaveAcesso("33111102737654003496550550000483081131621856");
		notaAutorizadaRetornoNFE.setCpfCnpj("37712543534");
		notaAutorizadaRetornoNFE.setDataRecebimento(new Date());
		notaAutorizadaRetornoNFE.setIdNotaFiscal(listaNotasFiscais.get(
				StatusProcessamentoInterno.ENVIADA).getId());
		notaAutorizadaRetornoNFE.setProtocolo(null);
		notaAutorizadaRetornoNFE.setStatus(Status.AUTORIZADO);
		notaAutorizadaRetornoNFE.setMotivo("motivo de cancelamento da nota");

		listaDadosRetornoNFE.add(notaAutorizadaRetornoNFE);

		listaDadosRetornoNFE = this.notaFiscalService
				.processarRetornoNotaFiscal(listaDadosRetornoNFE);

		Assert.assertTrue(listaDadosRetornoNFE.size() > 0);
	}

	private NotaFiscal gerarNFE(String chaveAcesso, String documento,
			StatusProcessamentoInterno statusInterno, Status status) {

		Endereco endereco = Fixture.criarEndereco(TipoEndereco.COMERCIAL,
				"13720000", "logradouro", "123", "bairro", "cidade", "uf",1);

		Telefone telefone = Fixture.telefone("ddd", "numero", "ramal");

		Veiculo veiculo = new Veiculo();
		veiculo.setPlaca("AAA1234");
		veiculo.setRegistroTransCarga("RN");
		veiculo.setUf("SP");

		Identificacao identificacao = Fixture.identificacao(new Date(),
				new Date(), new Date(), "", 001,
				Identificacao.FormaPagamento.A_PRAZO, new Date(), "", null,
				321L, 123, TipoOperacao.SAIDA);

		IdentificacaoDestinatario identificacaoDestinatario = Fixture
				.identificacaoDestinatario(documento, "teste@email.com",
						endereco, "inscricao", "Suframa", "nome",
						"nomeFantasia", null, telefone);

		IdentificacaoEmitente identificacaoEmitente = Fixture
				.identificacaoEmitente("c", documento, endereco, "IEstd",
						"inscricao", "IMunc", "nome", "nomeFantasia", null,
						null, telefone);

		InformacaoAdicional informacaoAdicional = Fixture
				.informacaoAdicional("informacoesComplementares");

		Endereco enderecoTransporte = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "10500250", "Rua Nova", "1000",
				"Bairro Novo", "Olimpia", "SP",1);

		InformacaoTransporte informacaoTransporte = Fixture
				.informacaoTransporte("88416646000103", enderecoTransporte,
						"IEstd", 132, "municipio", "nome", null, "SP", veiculo);

		ValoresRetencoesTributos valoreRetencoesTributos = new ValoresRetencoesTributos();

		valoreRetencoesTributos.setValorBaseCalculoIRRF(new BigDecimal(13212));

		InformacaoValoresTotais informacaoValoresTotais = Fixture
				.informacaoValoresTotais(null, null, new BigDecimal(999999),
						new BigDecimal(999999), new BigDecimal(999999),
						new BigDecimal(999999), new BigDecimal(999999),
						new BigDecimal(999999), new BigDecimal(999999),
						new BigDecimal(999999), new BigDecimal(999999),
						new BigDecimal(999999), new BigDecimal(999999),
						new BigDecimal(999999), new BigDecimal(999999));

		RetornoComunicacaoEletronica retornoComunicacaoEletronica = Fixture
				.retornoComunicacaoEletronica(new Date(), "", 4312L, status);

		InformacaoEletronica informacaoEletronica = Fixture
				.informacaoEletronica(chaveAcesso, retornoComunicacaoEletronica);

		NotaFiscal nota = new NotaFiscal();
		nota.setIdentificacaoDestinatario(identificacaoDestinatario);
		nota.setIdentificacaoEmitente(identificacaoEmitente);
		nota.setInformacaoAdicional(informacaoAdicional);
		nota.setInformacaoEletronica(informacaoEletronica);
		nota.setInformacaoTransporte(informacaoTransporte);
		nota.setInformacaoValoresTotais(informacaoValoresTotais);
		nota.setStatusProcessamentoInterno(statusInterno);
		nota.setIdentificacao(identificacao);

		return nota;
	}

	private List<ProdutoServico> gerarListaProdutoServico(Long idNota) {

		List<ProdutoServico> listaProdutoServico = new ArrayList<ProdutoServico>();
		NotaFiscal nota = new NotaFiscal();
		nota.setId(idNota);
		for (int i = 0; i < 5; i++) {

			Long numero = (i + 2) * idNota + 2;

			NCM ncmRevistas = Fixture.ncm(8888l, "REVISTAS", "KG");
			save(ncmRevistas);

			TipoProduto tipo = Fixture.tipoProduto("0" + numero + "descricao",
					GrupoProduto.REVISTA, ncmRevistas, "codigoNBM",
					4 + 123 * numero);

			Produto produto = Fixture.produto("0" + numero + "codigo",
					"descricao", "0" + numero + "nome",
					PeriodicidadeProduto.ANUAL, tipo, 123, 123, new Long(100), TributacaoFiscal. TRIBUTADO);

			ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(
					"codigoProdutoEdicao", 999L, 1111, 222, new Long(1000), new BigDecimal(99999),
					new BigDecimal(99999), "codigoDeBarras", 4321L, produto,
					new BigDecimal(99999), false);

			ICMS icms = new ICMS();

			icms.setCst("00");
			icms.setOrigem(Origem.NACIONAL);
			EncargoFinanceiroProduto encargo = new EncargoFinanceiroProduto();

			encargo.setIcms(icms);

			ProdutoServico produtoServico = Fixture.produtoServico(i + 1, 111,
					1111L, "codigoProduto", "descricaoProduto", encargo, 111L,
					111L, nota, produtoEdicao, BigInteger.ONE, "uni",
					new BigDecimal(4312), new BigDecimal(4312), new BigDecimal(
							4312), new BigDecimal(4312), new BigDecimal(4312),
					new BigDecimal(12344));

			listaProdutoServico.add(produtoServico);

		}

		return listaProdutoServico;
	}
	
	@Test
	public void emitiNotaFiscal(){
		List<ItemNotaFiscal> listItemNotaFiscal = new ArrayList<ItemNotaFiscal>();
		
		
		listItemNotaFiscal.add(new ItemNotaFiscal(produtoEdicaoComDesconto.getId(), BigInteger.TEN, BigDecimal.TEN, "091"));
		
		Endereco enderecoTransporte = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "10500250", "Rua Nova", "1000",
				"Bairro Novo", "Olimpia", "SP",1);
		save(enderecoTransporte);
		InformacaoTransporte informacaoTransporte = Fixture
				.informacaoTransporte("88416646000103", enderecoTransporte,
						"IEstd", 132, "municipio", "nome", null, "SP", null);
		InformacaoAdicional informacaoAdicional = new InformacaoAdicional();

		Set<Processo> processos = new HashSet<Processo>();
		processos.add(Processo.GERACAO_NF_E);

		notaFiscalService.emitiNotaFiscal(tipoNotaFiscalDevolucao.getId(), new Date(), cotaManoel.getId(), listItemNotaFiscal, informacaoTransporte, informacaoAdicional, null, processos);
	}
	
	@Test
	public void buscarNFeParaImpressao() {
		FiltroImpressaoNFEDTO filtro = new FiltroImpressaoNFEDTO();
		notaFiscalService.buscarNFeParaImpressao(filtro);
	}
	
	@Test
	public void buscarNFeParaImpressaoQtd() {
		FiltroImpressaoNFEDTO filtro = new FiltroImpressaoNFEDTO();
		notaFiscalService.buscarNFeParaImpressaoQtd(filtro);
	}
}
