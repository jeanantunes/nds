package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.EntradaNFETerceirosRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class EntradaNFETerceirosRepositoryImplTest
		extends AbstractRepositoryImplTest {

	@Autowired
	private EntradaNFETerceirosRepository entradaNFETerceirosRepositoryRepository;
	
	@Before
	public void setUp() {

		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);

		Fornecedor fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		fornecedorDinap.setCodigoInterface(9999999);
		fornecedorDinap.setResponsavel("Maria");
		fornecedorDinap.setOrigem(Origem.MANUAL);
		fornecedorDinap.setEmailNfe("maria@email.com");

		save(fornecedorDinap);

		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);

		PessoaFisica johnyDasNotas = Fixture.pessoaFisica(
				"65485291673",
				"johny@discover.com.br", "Johny da Silva");

		save(johnyDasNotas);

		Cota cotaJohnyConsultaEncalhe = Fixture.cota(2700, johnyDasNotas, SituacaoCadastro.ATIVO, box1);

		save(cotaJohnyConsultaEncalhe);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);

		CFOP cfop5102 = Fixture.cfop5102();
		
		save(cfop5102);
		
		TipoNotaFiscal tipoNotaFiscalRecebimento = Fixture.tipoNotaFiscalRecebimento();

		save(tipoNotaFiscalRecebimento);

		String naturezaOperacao = "00001";
		String formaPagamento = "00002";
		String horaSaida = "12:00";
		String ambiente = "000005";
		String protocolo = "321321";
		String versao = "0000000885";
		String emissorInscricaoEstadualSubstituto = "111.565.365.25";
		String emissorInscricaoMunicipal = "854.632.002.54";
		BigDecimal valorBaseICMS = BigDecimal.TEN;
		BigDecimal valorICMS = BigDecimal.TEN;
		BigDecimal valorBaseICMSSubstituto = BigDecimal.TEN;
		BigDecimal valorICMSSubstituto = BigDecimal.TEN;
		BigDecimal valorProdutos = BigDecimal.TEN;
		BigDecimal valorFrete = BigDecimal.TEN;
		BigDecimal valorSeguro = BigDecimal.TEN;
		BigDecimal valorOutro = BigDecimal.TEN;
		BigDecimal valorIPI = BigDecimal.TEN;
		BigDecimal valorNF = BigDecimal.TEN;
		Integer frete = 0;
		String transportadoraCNPJ = "123.235.284.663-52";
		String transportadoraNome = "TRANS. ABRIL LTDA";
		String transportadoraInscricaoEstadual = "465.456.878.34-785";
		String transportadoraEndereco = "Rua Laranjeiras";
		String transportadoraMunicipio = "Cachoeira do Itapemirim";
		String transportadoraUF = "SP";
		String transportadoraQuantidade = "100";
		String transportadoraEspecie = "0544400";
		String transportadoraMarca = "NOVA TRANSP.";
		String transportadoraNumeracao = "1111111111222";
		BigDecimal transportadoraPesoBruto = BigDecimal.TEN;
		BigDecimal transportadoraPesoLiquido = BigDecimal.TEN;
		String transportadoraANTT = "3454-4454545-345345-54";
		String transportadoraPlacaVeiculo = "BYS9012";
		String transportadoraPlacaVeiculoUF = "SP";
		BigDecimal ISSQNTotal = BigDecimal.TEN;
		BigDecimal ISSQNBase = BigDecimal.TEN;
		BigDecimal ISSQNValor = BigDecimal.TEN;
		String informacoesComplementares = "NENHUMA INFO A COMPLEMENTAR";
		String numeroFatura = "222222233354";
		BigDecimal valorFatura = BigDecimal.TEN;
		
		NotaFiscalEntradaCota notaFiscalEntradaCota = Fixture.notaFiscalEntradaCotaNFE(
				cfop5102,
				222220000202L,
				"220202022220",
				"2000022",
				cotaJohnyConsultaEncalhe,
				StatusEmissaoNfe.NFE_AUTORIZADA,
				TipoEmissaoNfe.CONTINGENCIA_DPEC,
				tipoNotaFiscalRecebimento,
				usuarioJoao,
				BigDecimal.TEN,
				BigDecimal.ZERO,
				BigDecimal.TEN,
				true,
				naturezaOperacao,
				formaPagamento,
				horaSaida,
				ambiente,
				protocolo,
				versao,
				emissorInscricaoEstadualSubstituto,
				emissorInscricaoMunicipal,
				valorBaseICMS,
				valorICMS,
				valorBaseICMSSubstituto,
				valorICMSSubstituto,
				valorProdutos,
				valorFrete,
				valorSeguro,
				valorOutro,
				valorIPI,
				valorNF,
				frete,
				transportadoraCNPJ,
				transportadoraNome,
				transportadoraInscricaoEstadual,
				transportadoraEndereco,
				transportadoraMunicipio,
				transportadoraUF,
				transportadoraQuantidade,
				transportadoraEspecie,
				transportadoraMarca,
				transportadoraNumeracao,
				transportadoraPesoBruto,
				transportadoraPesoLiquido,
				transportadoraANTT,
				transportadoraPlacaVeiculo,
				transportadoraPlacaVeiculoUF,
				ISSQNTotal,
				ISSQNBase,
				ISSQNValor,
				informacoesComplementares,
				numeroFatura,
				valorFatura);

		save(notaFiscalEntradaCota);

		NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedor = Fixture.notaFiscalEntradaFornecedorNFE(
				cfop5102,
				11011110L,
				"11111000",
				"11101011101",
				fornecedorDinap,
				StatusEmissaoNfe.NFE_AUTORIZADA,
				TipoEmissaoNfe.CONTINGENCIA_DPEC,
				tipoNotaFiscalRecebimento,
				usuarioJoao,
				BigDecimal.TEN,
				BigDecimal.ZERO,
				BigDecimal.TEN,
				true,
				naturezaOperacao,
				formaPagamento,
				horaSaida,
				ambiente,
				protocolo,
				versao,
				emissorInscricaoEstadualSubstituto,
				emissorInscricaoMunicipal,
				valorBaseICMS,
				valorICMS,
				valorBaseICMSSubstituto,
				valorICMSSubstituto,
				valorProdutos,
				valorFrete,
				valorSeguro,
				valorOutro,
				valorIPI,
				valorNF,
				frete,
				transportadoraCNPJ,
				transportadoraNome,
				transportadoraInscricaoEstadual,
				transportadoraEndereco,
				transportadoraMunicipio,
				transportadoraUF,
				transportadoraQuantidade,
				transportadoraEspecie,
				transportadoraMarca,
				transportadoraNumeracao,
				transportadoraPesoBruto,
				transportadoraPesoLiquido,
				transportadoraANTT,
				transportadoraPlacaVeiculo,
				transportadoraPlacaVeiculoUF,
				ISSQNTotal,
				ISSQNBase,
				ISSQNValor,
				informacoesComplementares,
				numeroFatura,
				valorFatura);

		save(notaFiscalEntradaFornecedor);
		
		ControleConferenciaEncalhe controleConferenciaEncalhe = 
				Fixture.controleConferenciaEncalhe(StatusOperacao.EM_ANDAMENTO, Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		save(controleConferenciaEncalhe);
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = Fixture.controleConferenciaEncalheCota(
				controleConferenciaEncalhe,
				cotaJohnyConsultaEncalhe,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				StatusOperacao.CONCLUIDO,
				usuarioJoao,
				box1);
		controleConferenciaEncalheCota.setNotaFiscalEntradaCota(notaFiscalEntradaCota);

		save(controleConferenciaEncalheCota);

	}
	
	@Test
	@Ignore
	public void buscarItensPorNota(){
		
		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
//		filtro.setCodigoNota(1L);
		filtro.setPaginacao(new PaginacaoVO());
		
		List<ItemNotaFiscalPendenteDTO> lista = 
				this.entradaNFETerceirosRepositoryRepository.buscarItensPorNota(
						filtro);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void buscarNFNotasRecebidas(){
		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(new PaginacaoVO());
		
		List<ConsultaEntradaNFETerceirosRecebidasDTO> lista = entradaNFETerceirosRepositoryRepository.buscarNFNotasRecebidas(filtro, false);

		Assert.assertNotNull(lista);

		int tamanhoEsperado = 2;
		
		Assert.assertEquals(tamanhoEsperado, lista.size());
		
	}
	
	@Test
	public void buscarNFNotasPendentes(){
		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(new PaginacaoVO());
		
		List<ConsultaEntradaNFETerceirosPendentesDTO> lista = entradaNFETerceirosRepositoryRepository.buscarNFNotasPendentes(filtro, false);

		Assert.assertNotNull(lista);

		int tamanhoEsperado = 1;
		
		Assert.assertEquals(tamanhoEsperado, lista.size());
		
	}
	
}
