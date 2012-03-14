package br.com.abril.nds.controllers.financeiro;

import java.io.File;
import java.io.IOException;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;

import br.com.abril.nds.dto.BoletoDTO;

public class UtilBoleto {
	
	private Boleto montar(BoletoDTO b){
		
		 /*
         * INFORMANDO DADOS SOBRE O CEDENTE.
         */
        Cedente cedente = new Cedente(b.getCedenteNome(), b.getCedenteDocumento());


        /*
         * INFORMANDO DADOS SOBRE O SACADO.
         */
        Sacado sacado = new Sacado(b.getSacadoNome(), b.getSacadoDocumento());

        // Informando o endereço do sacado.
        Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(UnidadeFederativa.RN);//!!!
        enderecoSac.setLocalidade(b.getEnderecoSacadoLocalidade());
        enderecoSac.setCep(new CEP(b.getEnderecoSacadoCep()));
        enderecoSac.setBairro(b.getEnderecoSacadoBairro());
        enderecoSac.setLogradouro(b.getEnderecoSacadoLogradouro());
        enderecoSac.setNumero(b.getEnderecoSacadoNumero());
        sacado.addEndereco(enderecoSac);

        /*
         * INFORMANDO DADOS SOBRE O SACADOR AVALISTA.
         */
        SacadorAvalista sacadorAvalista = new SacadorAvalista(b.getSacadorAvalistaNome(), b.getSacadorAvalistaDocumento());

        // Informando o endereço do sacador avalista.
        Endereco enderecoSacAval = new Endereco();
        enderecoSacAval.setUF(UnidadeFederativa.DF);//!!!
        enderecoSacAval.setLocalidade(b.getEnderecoSacadorAvalistaLocalidade());
        enderecoSacAval.setCep(new CEP(b.getEnderecoSacadorAvalistaCep()));
        enderecoSacAval.setBairro(b.getEnderecoSacadorAvalistaBairro());
        enderecoSacAval.setLogradouro(b.getEnderecoSacadorAvalistaLogradouro());
        enderecoSacAval.setNumero(b.getEnderecoSacadorAvalistaNumero());
        sacadorAvalista.addEndereco(enderecoSacAval);

        /*
         * INFORMANDO OS DADOS SOBRE O TÍTULO.
         */
        
        // Informando dados sobre a conta bancária do título.
        ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_BRADESCO.create());//!!!
        contaBancaria.setNumeroDaConta(new NumeroDaConta(b.getContaNumero(), "0"));
        contaBancaria.setCarteira(new Carteira(b.getContaCarteira()));
        contaBancaria.setAgencia(new Agencia(b.getContaAgencia(), "1"));
        
        Titulo titulo = new Titulo(contaBancaria, sacado, cedente, sacadorAvalista);
        titulo.setNumeroDoDocumento(b.getTituloNumeroDoDocumento());
        titulo.setNossoNumero(b.getTituloNossoNumero());
        titulo.setDigitoDoNossoNumero(b.getTituloDigitoDoNossoNumero());
        titulo.setValor(b.getTituloValor());
        titulo.setDataDoDocumento(b.getTituloDataDoDocumento());
        titulo.setDataDoVencimento(b.getTituloDataDoVencimento());
        titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);//!!!
        titulo.setAceite(Aceite.A);//!!!
        titulo.setDesconto(b.getTituloDesconto());
        titulo.setDeducao(b.getTituloDeducao());
        titulo.setMora(b.getTituloMora());
        titulo.setAcrecimo(b.getTituloAcrecimo());
        titulo.setValorCobrado(b.getTituloValorCobrado());

        /*
         * INFORMANDO OS DADOS SOBRE O BOLETO.
         */
        Boleto boleto = new Boleto(titulo);
        boleto.setLocalPagamento(b.getBoletoLocalPagamento());
        boleto.setInstrucaoAoSacado(b.getBoletoInstrucaoAoSacado());
        boleto.setInstrucao1(b.getBoletoInstrucao1());
        boleto.setInstrucao2(b.getBoletoInstrucao2());
        boleto.setInstrucao3(b.getBoletoInstrucao3());
        boleto.setInstrucao4(b.getBoletoInstrucao4());
        boleto.setInstrucao5(b.getBoletoInstrucao5());
        boleto.setInstrucao6(b.getBoletoInstrucao6());
        boleto.setInstrucao7(b.getBoletoInstrucao7());
        boleto.setInstrucao8(b.getBoletoInstrucao8());
        
        return boleto;
	}


	 /**
	  * Gera o arquivo PDF.
	  * 
	  * @param arquivoBoleto
	  */
	 public File gerar(BoletoDTO b,String path) {
		 
	    //MONTA BOLETO COM PARAMETROS PASSADOS
	    Boleto boleto = this.montar(b);
	 
	    /*
         * GERANDO O BOLETO BANCÁRIO.
         */
        // Instanciando um objeto "BoletoViewer", classe responsável pela
        // geração do boleto bancário.
	    BoletoViewer boletoViewer = new BoletoViewer(boleto);
	    
	    // Gerando o arquivo. No caso o arquivo mencionado será salvo na mesma
        // pasta do projeto. Outros exemplos:
        // WINDOWS: boletoViewer.getAsPDF("C:/Temp/MeuBoleto.pdf");
        // LINUX: boletoViewer.getAsPDF("/home/temp/MeuBoleto.pdf");
	    File arquivoPdf = boletoViewer.getPdfAsFile(path);
	    
	    return arquivoPdf;
	 }
	 
	 
	 /**
	  * Envia arquivo gerado por email.
	  * 
	  * @param arquivoBoleto
	  */
	 @SuppressWarnings("unused")
	 public void email(BoletoDTO b, String to, String from, String mensagem, String pathAnexo) {
	     File anexo = this.gerar(b, pathAnexo);
		
		
		 //AQUI ADICIONAR ENVIO POR EMAIL
		
		
	  }
	 
	 
	 /**
	  * Exibe o arquivo na tela.
	  * 
	  * @param arquivoBoleto
	  */
	 @SuppressWarnings("unused")
	 private void visualizar(BoletoDTO b, String pathAnexo) {
		 java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
		 File arquivoBoleto = this.gerar(b, pathAnexo);
         try {
             desktop.open(arquivoBoleto);
         } catch (IOException e) {
             e.printStackTrace();
         }    
	 }
	 
	 
}
