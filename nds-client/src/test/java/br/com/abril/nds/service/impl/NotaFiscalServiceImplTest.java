package br.com.abril.nds.service.impl;

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
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormaPagamento;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.NotaFiscalService;

@Ignore
public class NotaFiscalServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private NotaFiscalService notaFiscalService;
	
	private HashMap<StatusProcessamentoInterno, NotaFiscal> listaNotasFiscais = new HashMap<StatusProcessamentoInterno, NotaFiscal>();
	
	@Before
	public void setup() {
	
		Endereco endereco = Fixture.criarEndereco(TipoEndereco.COMERCIAL, "13720000", "Logradouro", 123, "Bairro", "Cidade", "SP");
		save(endereco);
		
		PessoaFisica pessoaFisicaEmitente = Fixture.pessoaFisica("37712543534", "email@email.com", "Joao");
		save(pessoaFisicaEmitente);				
						
		PessoaFisica pessoaFisicaDestinatario =  Fixture.pessoaFisica("10522837131", "email@email.com", "Jose");
		save(pessoaFisicaDestinatario);			
		
		NotaFiscal notaFiscalRetornadaAutorizada = 
				gerarNotaFiscal(Status.AUTORIZADO, "33111102737654003496550550000483081131621856", 
						StatusProcessamentoInterno.RETORNADA, pessoaFisicaDestinatario, pessoaFisicaEmitente, endereco);
		
		notaFiscalRetornadaAutorizada = merge(notaFiscalRetornadaAutorizada);
		
		
		listaNotasFiscais.put(notaFiscalRetornadaAutorizada.getStatusProcessamentoInterno(), 
																notaFiscalRetornadaAutorizada);
		
		NotaFiscal notaFiscalEnviadaAutorizada = 
				gerarNotaFiscal(Status.SERVICO_EM_OPERACAO, "33111102737654003496550550000483081131621856", 
						StatusProcessamentoInterno.ENVIADA, pessoaFisicaDestinatario, pessoaFisicaEmitente, endereco);
		
		notaFiscalEnviadaAutorizada = merge(notaFiscalEnviadaAutorizada);
		
		listaNotasFiscais.put(notaFiscalEnviadaAutorizada.getStatusProcessamentoInterno(), 
				notaFiscalEnviadaAutorizada);
	}
	
	@Test
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
	
	private NotaFiscal gerarNotaFiscal(Status status, String chaveAcesso, 
			StatusProcessamentoInterno statusProcessamentoInterno, Pessoa destinatario, Pessoa emitente, Endereco endereco) {
		
		IdentificacaoEmitente identificacaoEmitente = new IdentificacaoEmitente();
		identificacaoEmitente.setPessoaEmitenteReferencia(emitente);
		identificacaoEmitente.setDocumento("37712543534");
		identificacaoEmitente.setEndereco(endereco);
		identificacaoEmitente.setInscricaoEstual("InscricaoEstadua");
		identificacaoEmitente.setNome("NomeEmitente");
		
		IdentificacaoDestinatario identificacaoDestinatario = new IdentificacaoDestinatario();
		identificacaoDestinatario.setPessoaDestinatarioReferencia(destinatario);
		
		RetornoComunicacaoEletronica retornoComunicacaoEletronica = new RetornoComunicacaoEletronica();
		retornoComunicacaoEletronica.setDataRecebimento(new Date());
		retornoComunicacaoEletronica.setMotivo("");
		retornoComunicacaoEletronica.setProtocolo(123L);
		retornoComunicacaoEletronica.setStatus(status);
		
		InformacaoEletronica informacaoEletronica = new InformacaoEletronica();
		informacaoEletronica.setChaveAcesso(chaveAcesso);
		informacaoEletronica.setRetornoComunicacaoEletronica(retornoComunicacaoEletronica);
		
		Identificacao identificacao = new Identificacao();
		identificacao.setTipoOperacao(TipoOperacao.ENTRADA);
		identificacao.setDataEmissao(new Date());
		identificacao.setNumeroDocumentoFiscal(473129471L);
		identificacao.setSerie(43124);
		identificacao.setFormaPagamento(FormaPagamento.A_VISTA);
		identificacao.setDescricaoNaturezaOperacao("Natureza Operacao");
		
		NotaFiscal notaFiscal = new NotaFiscal();
		notaFiscal.setIdentificacaoEmitente(identificacaoEmitente);
		notaFiscal.setInformacaoEletronica(informacaoEletronica);
		notaFiscal.setStatusProcessamentoInterno(statusProcessamentoInterno);
		notaFiscal.setIdentificacao(identificacao);
		notaFiscal.setIdentificacaoDestinatario(identificacaoDestinatario);
		
		return notaFiscal;
	}
	
}
