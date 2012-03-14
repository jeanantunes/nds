package br.com.abril.nds.controllers.financeiro;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.dto.BoletoDTO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

/**
 * Controller responsável pela geração de documentos financeiros. 
 * 
 * @author Discover Technology
 *
 */

@Resource
@Path("/financeiro/documentos")
public class GeracaoDocumentosController {
	
	@Post
	@Path("/gerarBoleto")
	public void gerarBoleto(){
		
		BoletoDTO boleto = new BoletoDTO();
		
		boleto.setCedenteNome("PROJETO JRimum");
		boleto.setCedenteDocumento("00.000.208/0001-00");

		boleto.setSacadoNome("PROJETO JRimum");
		boleto.setSacadoDocumento("00.000.208/0001-00");

        boleto.setEnderecoSacadoUf("RN");//!!
        boleto.setEnderecoSacadoLocalidade("Natal");
        boleto.setEnderecoSacadoCep("59064-120");
        boleto.setEnderecoSacadoBairro("Grande Centro");
        boleto.setEnderecoSacadoLogradouro("Rua poeta dos programas");
        boleto.setEnderecoSacadoNumero("1");
 
        boleto.setSacadorAvalistaNome("PROJETO JRimum");
		boleto.setSacadorAvalistaDocumento("00.000.208/0001-00");
		
		boleto.setEnderecoSacadorAvalistaUf("RN");//!!
        boleto.setEnderecoSacadorAvalistaLocalidade("Natal");
        boleto.setEnderecoSacadorAvalistaCep("59064-120");
        boleto.setEnderecoSacadorAvalistaBairro("Grande Centro");
        boleto.setEnderecoSacadorAvalistaLogradouro("Rua poeta dos programas");
        boleto.setEnderecoSacadorAvalistaNumero("1");

        boleto.setContaNumero(123456);
        boleto.setContaCarteira(30);
        boleto.setContaAgencia(1234);
        
        boleto.setTituloNumeroDoDocumento("123456");
        boleto.setTituloNossoNumero("99345678912");
        boleto.setTituloDigitoDoNossoNumero("5");
        boleto.setTituloValor(new BigDecimal(0.23));
        boleto.setTituloDataDoDocumento(new Date());
        boleto.setTituloDataDoVencimento(new Date());
        boleto.setTituloTipoDeDocumento("DM_DUPLICATA_MERCANTIL");//!!!
        boleto.setTituloAceite("A");//!!!
        boleto.setTituloDesconto(new BigDecimal(0.05));
        boleto.setTituloDeducao(BigDecimal.ZERO);
        boleto.setTituloMora(BigDecimal.ZERO);
        boleto.setTituloAcrecimo(BigDecimal.ZERO);
        boleto.setTituloValorCobrado(BigDecimal.ZERO);

        boleto.setBoletoLocalPagamento("Pagável preferencialmente na Rede X ou em " +
                        "qualquer Banco até o Vencimento.");
        boleto.setBoletoInstrucaoAoSacado("Senhor sacado, sabemos sim que o valor " +
                        "cobrado não é o esperado, aproveite o DESCONTÃO!");
        boleto.setBoletoInstrucao1("PARA PAGAMENTO 1 até Hoje não cobrar nada!");
        boleto.setBoletoInstrucao2("PARA PAGAMENTO 2 até Amanhã Não cobre!");
        boleto.setBoletoInstrucao3("PARA PAGAMENTO 3 até Depois de amanhã, OK, não cobre.");
        boleto.setBoletoInstrucao4("PARA PAGAMENTO 4 até 04/xx/xxxx de 4 dias atrás COBRAR O VALOR DE: R$ 01,00");
        boleto.setBoletoInstrucao5("PARA PAGAMENTO 5 até 05/xx/xxxx COBRAR O VALOR DE: R$ 02,00");
        boleto.setBoletoInstrucao6("PARA PAGAMENTO 6 até 06/xx/xxxx COBRAR O VALOR DE: R$ 03,00");
        boleto.setBoletoInstrucao7("PARA PAGAMENTO 7 até xx/xx/xxxx COBRAR O VALOR QUE VOCÊ QUISER!");
        boleto.setBoletoInstrucao8("APÓS o Vencimento, Pagável Somente na Rede X.");
		
        UtilBoleto utilBoleto = new UtilBoleto();
        utilBoleto.gerar(boleto, "D:/Boleto.pdf");
        
	}
	

}
