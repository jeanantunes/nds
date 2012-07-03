package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.TipoProduto;
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
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.Origem;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.model.fiscal.nota.ValoresRetencoesTributos;
import br.com.abril.nds.model.fiscal.nota.Veiculo;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.NotaFiscalService;


public class NotaFiscalServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private NotaFiscalService notaFiscalService;
	
	private HashMap<StatusProcessamentoInterno, NotaFiscal> listaNotasFiscais = new HashMap<StatusProcessamentoInterno, NotaFiscal>();
	
	private List<NotaFiscal> notasParaTesteArquivo = new ArrayList<NotaFiscal>();
	
	@Before
	public void setup() {
		
		save(Fixture.parametroSistema(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO, "C:\\notas\\"));
		for (int i = 0; i < 5 ; i++) {
			NotaFiscal notaTesteArquivo = this.gerarNFE(
					"33111102737654003496550550000483081131621856",
					"87416762464", StatusProcessamentoInterno.GERADA,
					Status.SERVICO_EM_OPERACAO);
			notaTesteArquivo.setId((long)i);
			notaTesteArquivo.setProdutosServicos(this
					.gerarListaProdutoServico((long)i));

			this.notasParaTesteArquivo.add(notaTesteArquivo);
		}
	}
	
	@Test
	public void testExportarNotasFiscais() {
		try {
			
//			this.notaFiscalService.exportarNotasFiscais();
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
		
		notaCanceladaRetornoNFE.setChaveAcesso("33111102737654003496550550000483081131621856");
		notaCanceladaRetornoNFE.setCpfCnpj("37712543534");
		notaCanceladaRetornoNFE.setDataRecebimento(new Date());
		notaCanceladaRetornoNFE.setIdNotaFiscal(listaNotasFiscais.get(StatusProcessamentoInterno.RETORNADA).getId());
		notaCanceladaRetornoNFE.setProtocolo(null);
		notaCanceladaRetornoNFE.setStatus(Status.CANCELAMENTO_HOMOLOGADO);
		notaCanceladaRetornoNFE.setMotivo("motivo de cancelamento da nota");
		
		listaDadosRetornoNFE.add(notaCanceladaRetornoNFE);
		
		RetornoNFEDTO notaAutorizadaRetornoNFE = new RetornoNFEDTO();
		
		notaAutorizadaRetornoNFE.setChaveAcesso("33111102737654003496550550000483081131621856");
		notaAutorizadaRetornoNFE.setCpfCnpj("37712543534");
		notaAutorizadaRetornoNFE.setDataRecebimento(new Date());
		notaAutorizadaRetornoNFE.setIdNotaFiscal(listaNotasFiscais.get(StatusProcessamentoInterno.ENVIADA).getId());
		notaAutorizadaRetornoNFE.setProtocolo(null);
		notaAutorizadaRetornoNFE.setStatus(Status.AUTORIZADO);
		notaAutorizadaRetornoNFE.setMotivo("motivo de cancelamento da nota");
		
		listaDadosRetornoNFE.add(notaAutorizadaRetornoNFE);
		
		
		listaDadosRetornoNFE = this.notaFiscalService.processarRetornoNotaFiscal(listaDadosRetornoNFE);
		
		Assert.assertTrue(listaDadosRetornoNFE.size() > 0);
	}
	
	
	private NotaFiscal gerarNFE(String chaveAcesso, String documento, StatusProcessamentoInterno statusInterno, Status status) {
		
		Endereco endereco = Fixture.criarEndereco(TipoEndereco.COMERCIAL, 
				"13720000", "logradouro", 123, "bairro", "cidade", "uf");
		
		
		Telefone telefone = Fixture.telefone("ddd", "numero", "ramal");
		
		
		Veiculo veiculo = new Veiculo();
		veiculo.setPlaca("AAA1234");
		veiculo.setRegistroTransCarga("RN");
		veiculo.setUf("SP");
		
		Identificacao identificacao = Fixture.identificacao(
				new Date(), new Date(), new Date(), "", 001, Identificacao.FormaPagamento.A_PRAZO, 
				new Date(), "", null, 321L, 123, TipoOperacao.SAIDA);
		
		IdentificacaoDestinatario identificacaoDestinatario = Fixture.identificacaoDestinatario(
				documento, "teste@email.com", endereco, "inscricao", "Suframa", "nome", 
				"nomeFantasia", null, telefone);
		
		IdentificacaoEmitente identificacaoEmitente = Fixture.identificacaoEmitente(
				"c", documento, endereco, "IEstd", "inscricao", 
				"IMunc", "nome", "nomeFantasia", null, null, 
				telefone);
		
		InformacaoAdicional informacaoAdicional = Fixture.informacaoAdicional("informacoesComplementares");
		
		Endereco enderecoTransporte = Fixture.criarEndereco(TipoEndereco.COMERCIAL, "10500250", "Rua Nova", 1000, "Bairro Novo", "Olimpia", "SP");
		
		InformacaoTransporte informacaoTransporte = Fixture.informacaoTransporte(
				"88416646000103", enderecoTransporte , "IEstd", 132, "municipio", "nome", null, "SP", veiculo);
	
		ValoresRetencoesTributos valoreRetencoesTributos = new ValoresRetencoesTributos();
		
		valoreRetencoesTributos.setValorBaseCalculoIRRF(new BigDecimal(13212));
		
		InformacaoValoresTotais informacaoValoresTotais = Fixture.informacaoValoresTotais(
				null, null , new BigDecimal(999999), new BigDecimal(999999), new BigDecimal(999999), 
				new BigDecimal(999999), new BigDecimal(999999), new BigDecimal(999999), new BigDecimal(999999), 
				new BigDecimal(999999), new BigDecimal(999999), new BigDecimal(999999), new BigDecimal(999999), 
				new BigDecimal(999999), new BigDecimal(999999));
		
		RetornoComunicacaoEletronica retornoComunicacaoEletronica = Fixture.retornoComunicacaoEletronica(
				new Date(), "", 4312L, status);
		
		InformacaoEletronica informacaoEletronica = Fixture.informacaoEletronica(chaveAcesso, retornoComunicacaoEletronica);
		
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
		for (int i = 0 ; i < 5 ; i++) {
			
			Long numero = (i+2)*idNota+2;
			
			TipoProduto tipo = Fixture.tipoProduto("0"+numero+"descricao",
					GrupoProduto.REVISTA, 8888L, "codigoNBM", 4+123*numero);
			

			Produto produto = Fixture.produto("0"+numero+"codigo", "descricao", "0"+numero+"nome",
					PeriodicidadeProduto.ANUAL, tipo, 123, 123, new BigDecimal(
							123));
			

			ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(
					"codigoProdutoEdicao", 999L, 1111, 222, new BigDecimal(
							99999), new BigDecimal(99999),
					new BigDecimal(99999), "codigoDeBarras", 4321L, produto,
					new BigDecimal(99999), false);
			
			
			ICMS icms = new ICMS();
			
			icms.setCst("00");
			icms.setOrigem(Origem.NACIONAL);
			EncargoFinanceiroProduto encargo = new EncargoFinanceiroProduto();
			
			encargo.setIcms(icms);
			
			ProdutoServico produtoServico =
			Fixture.produtoServico(i+1, 111, 1111L,
					"codigoProduto", "descricaoProduto", encargo, 111L,
					111L, nota, produtoEdicao, BigDecimal.ONE, "uni",
					new BigDecimal(4312), new BigDecimal(4312), new BigDecimal(4312), new BigDecimal(4312),
					new BigDecimal(4312), new BigDecimal(12344));
			
			listaProdutoServico.add(produtoServico);
			
		}
		
		return listaProdutoServico;
	}
}
