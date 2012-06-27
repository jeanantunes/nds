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
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiroProduto;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.model.fiscal.nota.ValoresRetencoesTributos;
import br.com.abril.nds.model.fiscal.nota.Veiculo;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.NotaFiscalService;

@Ignore
public class NotaFiscalServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private NotaFiscalService notaFiscalService;
	
	private HashMap<StatusProcessamentoInterno, NotaFiscal> listaNotasFiscais = new HashMap<StatusProcessamentoInterno, NotaFiscal>();
	
	private List<NotaFiscal> notasParaTesteArquivo = new ArrayList<NotaFiscal>();
	
	@Before
	public void setup() {
		
		//Dados para teste de arquivo
//		for ( int i = 0; i < 5 ; i++ ) {
			NotaFiscal notaTesteArquivo = this.gerarNFE("33111102737654003496550550000483081131621856", 
					"37712543534", StatusProcessamentoInterno.GERADA, Status.SERVICO_EM_OPERACAO);
			save(notaTesteArquivo);
		//	this.notasParaTesteArquivo.add(notaTesteArquivo);
//		}
	}
	
	
	@Test
	public void testExportarNotasFiscais() {
		
		try {
			this.notaFiscalService.exportarNotasFiscais();
		} catch (Exception e) {
			
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
		save(endereco);
		
		Telefone telefone = Fixture.telefone("ddd", "numero", "ramal");
		save(telefone);
		
		Veiculo veiculo = new Veiculo();
		veiculo.setPlaca("1234");
		veiculo.setRegistroTransCarga("RT");
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
		
		InformacaoTransporte informacaoTransporte = Fixture.informacaoTransporte(
				"18130646000159", "", "enderecoCompleto", "IEstd", 132, "municipio", "nome", null, "SP", veiculo);
	
		ValoresRetencoesTributos valoreRetencoesTributos = new ValoresRetencoesTributos();
		
		valoreRetencoesTributos.setValorBaseCalculoIRRF(new BigDecimal(13212));
		
		//valoreRetencoesTributos.setNotaFiscal(nota);
		
		//save(valoreRetencoesTributos);
		
//		ValoresTotaisISSQN valoreTotaisISSQN= new ValoresTotaisISSQN();
//		
//		
//		save(valoreTotaisISSQN);
		
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

	private List<ProdutoServico> gerarListaProdutoServico(NotaFiscal nota) {
		
		List<ProdutoServico> listaProdutoServico = new ArrayList<ProdutoServico>();
		
		TipoProduto tipo = Fixture.tipoProduto("descricao", GrupoProduto.REVISTA, 8888L, "codigoNBM", 1234L);
		
		Produto produto = Fixture.produto("codigo", "descricao", "nome", PeriodicidadeProduto.ANUAL, tipo, 
				123, 123, new BigDecimal(123));
		
		ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(
				"codigoProdutoEdicao", 999L, 1111, 
				222, new BigDecimal(99999),  new BigDecimal(99999),  new BigDecimal(99999), "codigoDeBarras", 
				4321L, produto, new BigDecimal(99999), false);
		
		
		for (int i = 0 ; i < 5 ; i++) {
			
			listaProdutoServico.add(Fixture.produtoServico(111, 1111L,
					"codigoProduto", "descricaoProduto", new EncargoFinanceiroProduto(), 111L,
					111L, nota, produtoEdicao, 9L, "variavel",
					new BigDecimal(4312), new BigDecimal(4312), new BigDecimal(4312), new BigDecimal(4312),
					new BigDecimal(4312), new BigDecimal(12344)));
		}
		
		return listaProdutoServico;
	}
}
