package br.com.abril.nds.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.ParametrosBancariosMap;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeCobranca;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;
import org.jrimum.domkee.financeiro.banco.hsbc.TipoIdentificadorCNR;

public class GeradorBoleto {
	
	private CorpoBoleto corpoBoleto;
	
	private List<CorpoBoleto> corpoBoletos;
	
	public GeradorBoleto(CorpoBoleto corpoBoleto) {
		this.corpoBoleto = corpoBoleto;
	}
	
	public GeradorBoleto(List<CorpoBoleto> corpoBoletos) {
		this.corpoBoletos = corpoBoletos;
	}
	
	/**
	 * Monta o boleto para o formato "Bopepo"
	 * @param b : Dados do boleto
	 * @return
	 */
	private Boleto createBoleto(CorpoBoleto corpoBoleto){
		
        //CEDENTE
        Cedente cedente = new Cedente(corpoBoleto.getCedenteNome(), corpoBoleto.getCedenteDocumento());
        
        //SACADO
        Sacado sacado = new Sacado(corpoBoleto.getSacadoNome(), corpoBoleto.getSacadoDocumento());
        //ENDERECO DO SACADO
        
        Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(UnidadeFederativa.valueOf(corpoBoleto.getEnderecoSacadoUf()));
        enderecoSac.setLocalidade(corpoBoleto.getEnderecoSacadoBairro());
        enderecoSac.setCep(new CEP(corpoBoleto.getEnderecoSacadoCep()));
        enderecoSac.setBairro(corpoBoleto.getEnderecoSacadoLogradouro());
        enderecoSac.setLogradouro(corpoBoleto.getEnderecoSacadoLocalidade());
        enderecoSac.setNumero(corpoBoleto.getEnderecoSacadoNumero());
        sacado.addEndereco(enderecoSac);
        
        SacadorAvalista sacadorAvalista = null;
        if ( (corpoBoleto.getSacadorAvalistaNome()!=null) && !("".equals(corpoBoleto.getSacadorAvalistaNome())) ){
	        //SACADOR AVALISTA
	        sacadorAvalista = new SacadorAvalista(corpoBoleto.getSacadorAvalistaNome(), corpoBoleto.getSacadorAvalistaDocumento());
	        //ENDERECO DO SACADO AVALISTA
	        Endereco enderecoSacAval = new Endereco();
	        enderecoSacAval.setUF(UnidadeFederativa.valueOf(corpoBoleto.getEnderecoSacadorAvalistaUf()));
	        enderecoSacAval.setLocalidade(corpoBoleto.getEnderecoSacadorAvalistaLocalidade());
	        enderecoSacAval.setCep(new CEP(corpoBoleto.getEnderecoSacadorAvalistaCep()));
	        enderecoSacAval.setBairro(corpoBoleto.getEnderecoSacadorAvalistaBairro());
	        enderecoSacAval.setLogradouro(corpoBoleto.getEnderecoSacadorAvalistaLogradouro());
	        enderecoSacAval.setNumero(corpoBoleto.getEnderecoSacadorAvalistaNumero());
	        sacadorAvalista.addEndereco(enderecoSacAval);
        }
        
        //CONTA BANCARIA
        ContaBancaria contaBancaria = new ContaBancaria(getBancoByNumero(corpoBoleto.getContaNumeroBanco()).create());
        contaBancaria.setNumeroDaConta(new NumeroDaConta(corpoBoleto.getCodigoCedente(), null));
        //CARTEIRA DA CONTA BANCARIA  
        Carteira carteira = new Carteira(corpoBoleto.getContaCarteira());
        //TIPO DE COBRANCA DA CARTEIRA DA CONTA BANCARIA  
        carteira.setTipoCobranca(TipoDeCobranca.valueOf(corpoBoleto.getContaTipoDeCobranca())); 
        contaBancaria.setCarteira(carteira);
        contaBancaria.setAgencia(new Agencia(corpoBoleto.getContaAgencia(), corpoBoleto.getDigitoAgencia()));
        
        //TITULO
        Titulo titulo;
        if (sacadorAvalista!=null){
            titulo = new Titulo(contaBancaria, sacado, cedente, sacadorAvalista);
        }
        else{
        	titulo = new Titulo(contaBancaria, sacado, cedente);
        }
        //PARAMETROS BANCARIOS DO TITULO
        ParametrosBancariosMap parametrosBancarios = new
        ParametrosBancariosMap();
        parametrosBancarios.adicione(TipoIdentificadorCNR.class.getName(),
                                     TipoIdentificadorCNR.valueOf(corpoBoleto.getTituloTipoIdentificadorCNR()));
        titulo.setParametrosBancarios(parametrosBancarios);
        titulo.setNumeroDoDocumento(corpoBoleto.getTituloNumeroDoDocumento());
        titulo.setNossoNumero(corpoBoleto.getTituloNossoNumero());
        titulo.setDigitoDoNossoNumero(corpoBoleto.getTituloDigitoDoNossoNumero());
        titulo.setValor(corpoBoleto.getTituloValor());
        titulo.setDataDoDocumento(corpoBoleto.getTituloDataDoDocumento());
        titulo.setDataDoVencimento(corpoBoleto.getTituloDataDoVencimento());
        titulo.setTipoDeDocumento(TipoDeTitulo.valueOf(corpoBoleto.getTituloTipoDeDocumento()));
        titulo.setAceite(Aceite.valueOf(corpoBoleto.getTituloAceite()));
        titulo.setDesconto(corpoBoleto.getTituloDesconto());
        titulo.setDeducao(corpoBoleto.getTituloDeducao());
        titulo.setMora(corpoBoleto.getTituloMora());
        titulo.setAcrecimo(corpoBoleto.getTituloAcrecimo());
        titulo.setValorCobrado(corpoBoleto.getTituloValorCobrado());

        //BOLETO
        Boleto boleto = new Boleto(titulo);
        boleto.setLocalPagamento(corpoBoleto.getBoletoLocalPagamento());
        boleto.setInstrucaoAoSacado(corpoBoleto.getBoletoInstrucaoAoSacado());
        boleto.setInstrucao1(corpoBoleto.getBoletoInstrucao1());
        boleto.setInstrucao2(corpoBoleto.getBoletoInstrucao2());
        boleto.setInstrucao3(corpoBoleto.getBoletoInstrucao3());
        boleto.setInstrucao4(corpoBoleto.getBoletoInstrucao4());
        boleto.setInstrucao5(corpoBoleto.getBoletoInstrucao5());
        boleto.setInstrucao6(corpoBoleto.getBoletoInstrucao6());
        boleto.setInstrucao7(corpoBoleto.getBoletoInstrucao7());
        boleto.setInstrucao8(corpoBoleto.getBoletoInstrucao8());
        
        //BOLETO COM CAMPOS DE VALOR EM BRANCO
        if (corpoBoleto.isBoletoSemValor()){
        	
	        boleto.addTextosExtras("txtRsValorCobrado"," ");
	        boleto.addTextosExtras("txtRsValorDocumento"," ");
	        
	        boleto.addTextosExtras("txtFcValor"," ");
	        boleto.addTextosExtras("txtFcValorCobrado"," ");
	        boleto.addTextosExtras("txtFcValorDocumento"," ");
        }

        return boleto;

	}
	
	
	private List<Boleto> createBoletos(){
		
		List<Boleto> listaBoletos = new ArrayList<Boleto>();
		
		if(corpoBoletos!= null && !corpoBoletos.isEmpty() ){
			
			for(CorpoBoleto corpo  : corpoBoletos){
				listaBoletos.add(createBoleto(corpo));
			}
		}
		return listaBoletos;
	}

	 /**
	  * Gera o arquivo PDF local.
	  * @param path
	  */
	 public File gerar(String path) {
	    Boleto boleto = this.createBoleto(corpoBoleto);
	    BoletoViewer boletoViewer = new BoletoViewer(boleto);
	    File arquivoPdf = boletoViewer.getPdfAsFile(path);
	    return arquivoPdf;
	 }
	  
	 /**
	  * Retorna um Array de Bytes do Boleto PDF.
	  * 
	  */
	 public byte[] getBytePdf()  {
	    Boleto boleto = this.createBoleto(corpoBoleto);
	    BoletoViewer boletoViewer = new BoletoViewer(boleto);
	    byte[] byteArrayPdf = boletoViewer.getPdfAsByteArray();
	    return byteArrayPdf;
	 }
	 
	 /**
	  * Retorna um Array de Bytes do Boleto PDF.
	  * 
	  */
	 public byte[] getByteGroupPdf()  {
	    
		List<Boleto> list = createBoletos();
		
		if(!list.isEmpty()){
			byte[] byteArrayPdf = BoletoViewer.groupInOnePDF(list);
		    return byteArrayPdf;
		}
	    return null;
	 }
	  
	 /**
	  * Retorna um File do Boleto PDF.
	  * 
	  */
	 public File getFilePdf(){
 		Boleto boleto = this.createBoleto(corpoBoleto);
 		BoletoViewer viewer = new BoletoViewer(boleto);
 		File file = new File("boleto"+boleto.getTitulo().getNossoNumero()+".pdf");
 		viewer.getPdfAsFile(file);
 		return file;
     }
	 
	 
	 /**
	  * Recupera o banco suportado pelo número
	  * @param número do banco
	  * @return banco suportado que corresponde ao número do banco, ou null caso
	  * o número do banco nao sera valido
	  */
	 private static BancosSuportados getBancoByNumero(String numero) {
		 for (BancosSuportados banco : BancosSuportados.values()) {
			 if (banco.getCodigoDeCompensacao().equals(numero)) {
				 return banco;
			 }
		 }
		 return null;
	 }
	 
}
