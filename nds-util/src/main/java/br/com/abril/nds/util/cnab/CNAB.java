package br.com.abril.nds.util.cnab;

import br.com.abril.nds.util.cnab.UtilitarioCNAB.PadraoCNAB;


public class CNAB {

	private CNAB(PadraoCNAB padraoCNAB){
		this.padraoCNAB = padraoCNAB;
	}
	
	private final PadraoCNAB padraoCNAB;
	
	public int indiceDataPagamentoInicio;
	public int indiceDataPagamentoFim;
	
	public int indiceNossoNumeroInicio;
	public int indiceNossoNumeroFim;
	
	public int indiceValorPagamentoInicio;
	public int indiceValorPagamentoFim;
	
	public int indiceNumeroRegistroInicio;
	public int indiceNumeroRegistroFim;

	public int indiceNumeroAgenciaInicio;
	public int indiceNumeroAgenciaFim;
	
	public int indiceNumeroContaInicio;
	public int indiceNumeroContaFim;
	
	public String segmentoDataPagamento;
	public String segmentoNossoNumero;
	public String segmentoValorPagamento;
	public String segmentoNumeroRegistro;
	public String segmentoNumeroAgencia;
	public String segmentoNumeroConta;
	
	
	public String obterDataPagamento(String linha) {
		return linha.substring(indiceDataPagamentoInicio, indiceDataPagamentoFim);
	}
	
	public String obterNossoNumero(String linha) {
		return linha.substring(indiceNossoNumeroInicio, indiceNossoNumeroFim);
	}
	
	public String obterValorPagamento(String linha) {
		return linha.substring(indiceValorPagamentoInicio, indiceValorPagamentoFim);
	}
	
	public String obterNumeroRegistro(String linha) {
		return linha.substring(indiceNumeroRegistroInicio, indiceNumeroRegistroFim);
	}
	
	public String obterNumeroAgencia(String linha) {
		return linha.substring(indiceNumeroAgenciaInicio, indiceNumeroAgenciaFim); 
	}
	
	public String obterNumeroConta(String linha) {
		return linha.substring(indiceNumeroContaInicio, indiceNumeroContaFim); 
	}

	public static CNAB obterCNAB(PadraoCNAB padraoCNAB, String codigoBanco){
		
		if(PadraoCNAB.CNAB240.equals(padraoCNAB)) {

	    	switch (codigoBanco) {
			
				case UtilitarioCNAB.BANCO_HSBC:
					return CNAB.newInstanceCnab240HSBC();
				case UtilitarioCNAB.BANCO_BRADESCO:
					return CNAB.newInstanceCnab240Bradesco();
				case UtilitarioCNAB.BANCO_ITAU:
					return CNAB.newInstanceCnab240Itau();
				case UtilitarioCNAB.BANCO_DO_BRASIL:
					return CNAB.newInstanceCnab240BancoDoBrasil();
				case UtilitarioCNAB.BANCO_CAIXA_ECONOMICA_FEDERAL:
					return CNAB.newInstanceCnab240CaixaEconomicaFederal();
				case UtilitarioCNAB.BANCO_SANTANDER:
					return CNAB.newInstanceCnab240Santander();
				default:
					throw new IllegalStateException("Leitura desse padrao ainda nao implementada");
			}

			
		} else if(PadraoCNAB.CNAB400.equals(padraoCNAB)) {

	    	switch (codigoBanco) {
			
				case UtilitarioCNAB.BANCO_HSBC:
					return CNAB.newInstanceCnab400Hsbc();
				case UtilitarioCNAB.BANCO_BRADESCO:
					return CNAB.newInstanceCnab400Bradesco();
				case UtilitarioCNAB.BANCO_ITAU:
					return CNAB.newInstanceCnab400Itau();
				case UtilitarioCNAB.BANCO_DO_BRASIL:
					return CNAB.newInstanceCnab400BancoDoBrasil();
				case UtilitarioCNAB.BANCO_CAIXA_ECONOMICA_FEDERAL:
					return CNAB.newInstanceCnab400CaixaEconomicaFederal();
				case UtilitarioCNAB.BANCO_SANTANDER:
					return CNAB.newInstanceCnab400Santander();
				default:
					throw new IllegalStateException("Leitura desse padrao ainda nao implementada");
					
			}
			
		} else {
			
			throw new IllegalStateException("Leitura desse padrao ainda nao implementada");
			
		}
		
    }
	
	public static CNAB newInstanceCnab240CaixaEconomicaFederal(){
		
    	CNAB cnab = new CNAB(PadraoCNAB.CNAB240);
		
    	cnab.segmentoDataPagamento 	= UtilitarioCNAB.SEGMENTO_U;
		cnab.segmentoNossoNumero 	= UtilitarioCNAB.SEGMENTO_T;
		cnab.segmentoValorPagamento = UtilitarioCNAB.SEGMENTO_U;
		cnab.segmentoNumeroRegistro = UtilitarioCNAB.SEGMENTO_U;

		cnab.indiceDataPagamentoInicio = 137;
		cnab.indiceDataPagamentoFim = 145;
		
		cnab.indiceNossoNumeroInicio = 46;
		cnab.indiceNossoNumeroFim = 57;
		
		cnab.indiceValorPagamentoInicio = 77;
		cnab.indiceValorPagamentoFim = 92;
		
		cnab.indiceNumeroRegistroInicio = 8;
		cnab.indiceNumeroRegistroFim = 13;
		
		//INFORMAÇÕES ABAIXO NO HEADER
		cnab.indiceNumeroAgenciaInicio = 52;
		cnab.indiceNumeroAgenciaFim = 58;
		
		cnab.indiceNumeroContaInicio = 58;
		cnab.indiceNumeroContaFim = 71;
		
		return cnab;    	
    	
    }	
    public static CNAB newInstanceCnab240Santander(){

    	CNAB cnab = new CNAB(PadraoCNAB.CNAB240);
    	
    	cnab.segmentoDataPagamento 	= UtilitarioCNAB.SEGMENTO_U;
		cnab.segmentoNossoNumero 	= UtilitarioCNAB.SEGMENTO_T;
		cnab.segmentoValorPagamento = UtilitarioCNAB.SEGMENTO_U;
		cnab.segmentoNumeroRegistro = UtilitarioCNAB.SEGMENTO_U;

		cnab.indiceDataPagamentoInicio = 157;
		cnab.indiceDataPagamentoFim = 165;
		
		cnab.indiceNossoNumeroInicio = 40;
		cnab.indiceNossoNumeroFim = 53;
		
		cnab.indiceValorPagamentoInicio = 77;
		cnab.indiceValorPagamentoFim = 92;
		
		cnab.indiceNumeroRegistroInicio = 8;
		cnab.indiceNumeroRegistroFim = 13;
		
		
		//INFORMAÇÕES ABAIXO NO HEADER
		cnab.indiceNumeroAgenciaInicio = 32;
		cnab.indiceNumeroAgenciaFim = 37;
		
		cnab.indiceNumeroContaInicio = 37;
		cnab.indiceNumeroContaFim = 47;
		
		return cnab;    	
    	
    }
	
	
    public static CNAB newInstanceCnab240Bradesco(){
    	
    	CNAB cnab = new CNAB(PadraoCNAB.CNAB240);
 		
    	cnab.segmentoDataPagamento 	= UtilitarioCNAB.SEGMENTO_U;
		cnab.segmentoNossoNumero 	= UtilitarioCNAB.SEGMENTO_T;
		cnab.segmentoValorPagamento = UtilitarioCNAB.SEGMENTO_U;
		cnab.segmentoNumeroRegistro = UtilitarioCNAB.SEGMENTO_U;
    	
		cnab.indiceDataPagamentoInicio = 157;
		cnab.indiceDataPagamentoFim = 165;
		
		cnab.indiceNossoNumeroInicio = 37;
		cnab.indiceNossoNumeroFim = 57;
		
		cnab.indiceValorPagamentoInicio = 77;
		cnab.indiceValorPagamentoFim = 92;
		
		cnab.indiceNumeroRegistroInicio = 8;
		cnab.indiceNumeroRegistroFim = 13;
		
		//INFORMAÇÕES ABAIXO NO HEADER
		cnab.indiceNumeroAgenciaInicio = 52;
		cnab.indiceNumeroAgenciaFim = 58;
		
		cnab.indiceNumeroContaInicio = 58;
		cnab.indiceNumeroContaFim = 71;
		
		return cnab;    	
    }
	
	
    public static CNAB newInstanceCnab240BancoDoBrasil(){
    	
    	CNAB cnab = new CNAB(PadraoCNAB.CNAB240);
 		
    	cnab.segmentoDataPagamento 	= UtilitarioCNAB.SEGMENTO_J;
		cnab.segmentoNossoNumero 	= UtilitarioCNAB.SEGMENTO_J;
		cnab.segmentoValorPagamento = UtilitarioCNAB.SEGMENTO_J;
		cnab.segmentoNumeroRegistro = UtilitarioCNAB.SEGMENTO_J;
    	
		cnab.indiceDataPagamentoInicio = 144;
		cnab.indiceDataPagamentoFim = 152;
		
		cnab.indiceNossoNumeroInicio = 202;
		cnab.indiceNossoNumeroFim = 222;
		
		cnab.indiceValorPagamentoInicio = 152;
		cnab.indiceValorPagamentoFim = 167;
		
		cnab.indiceNumeroRegistroInicio = 8;
		cnab.indiceNumeroRegistroFim = 13;
		
		
		//INFORMAÇÕES ABAIXO NO HEADER
		cnab.indiceNumeroAgenciaInicio = 52;
		cnab.indiceNumeroAgenciaFim = 58;
		
		cnab.indiceNumeroContaInicio = 58;
		cnab.indiceNumeroContaFim = 71;
		
		return cnab;
	}
	
    public static CNAB newInstanceCnab240HSBC(){
    	
    	CNAB cnab = new CNAB(PadraoCNAB.CNAB240);

    	cnab.segmentoDataPagamento 	= UtilitarioCNAB.SEGMENTO_J;
		cnab.segmentoNossoNumero 	= UtilitarioCNAB.SEGMENTO_J;
		cnab.segmentoValorPagamento = UtilitarioCNAB.SEGMENTO_J;
		cnab.segmentoNumeroRegistro = UtilitarioCNAB.SEGMENTO_J;

    	
    	cnab.indiceDataPagamentoInicio = 144;
    	cnab.indiceDataPagamentoFim = 152;
		
    	cnab.indiceNossoNumeroInicio = 202;
    	cnab.indiceNossoNumeroFim = 218;
		
    	cnab.indiceValorPagamentoInicio = 154; 
    	cnab.indiceValorPagamentoFim = 167;
		
    	cnab.indiceNumeroRegistroInicio = 8; 
		cnab.indiceNumeroRegistroFim = 13;

		//INFORMAÇÕES ABAIXO NO HEADER
		cnab.indiceNumeroAgenciaInicio = 52;
		cnab.indiceNumeroAgenciaFim = 57;
		
		cnab.indiceNumeroContaInicio = 58;
		cnab.indiceNumeroContaFim = 70;
		
		return cnab;
	}

	
    public static CNAB newInstanceCnab240Itau(){
    	
    	CNAB cnab = new CNAB(PadraoCNAB.CNAB240);

    	cnab.segmentoDataPagamento 	= UtilitarioCNAB.SEGMENTO_J;
		cnab.segmentoNossoNumero 	= UtilitarioCNAB.SEGMENTO_J;
		cnab.segmentoValorPagamento = UtilitarioCNAB.SEGMENTO_J;
		cnab.segmentoNumeroRegistro = UtilitarioCNAB.SEGMENTO_J;

    	
    	cnab.indiceDataPagamentoInicio = 144;
    	cnab.indiceDataPagamentoFim = 152;
		
    	cnab.indiceNossoNumeroInicio = 215;
    	cnab.indiceNossoNumeroFim = 230;
		
    	cnab.indiceValorPagamentoInicio = 152; 
    	cnab.indiceValorPagamentoFim = 167;
		
    	cnab.indiceNumeroRegistroInicio = 8; 
		cnab.indiceNumeroRegistroFim = 13;

		//INFORMAÇÕES ABAIXO NO HEADER
		cnab.indiceNumeroAgenciaInicio = 52;
		cnab.indiceNumeroAgenciaFim = 57;
		
		cnab.indiceNumeroContaInicio = 58;
		cnab.indiceNumeroContaFim = 70;
		
		return cnab;
	}
    
    /**
	 * Retorna Cnab configurado para o banco HSBC 
	 * @param cnab
	 */
    public static CNAB newInstanceCnab400CaixaEconomicaFederal(){
    	
    	CNAB cnab = new CNAB(PadraoCNAB.CNAB400);
    	
		cnab.indiceDataPagamentoInicio = 110;
		cnab.indiceDataPagamentoFim = 116;
		
		cnab.indiceNossoNumeroInicio = 37;
		cnab.indiceNossoNumeroFim = 53;
		
		cnab.indiceValorPagamentoInicio = 253;
		cnab.indiceValorPagamentoFim = 266;
		
		cnab.indiceNumeroRegistroInicio = 394;
		cnab.indiceNumeroRegistroFim = 400;
		
//		Código Agência
		
//		AAAAOOOCCCCCCCCD, onde
//		AAAA = Código da Agência CAIXA que o cliente opera
//		OOO = Operação
//		CCCCCCCC = Número da Conta
//		D = Dígito Verificador
		
		cnab.indiceNumeroAgenciaInicio = 17;
		cnab.indiceNumeroAgenciaFim = 21;
		
		cnab.indiceNumeroContaInicio = 24;
		cnab.indiceNumeroContaFim = 33;
		
		return cnab;
	}
    
	
	/**
	 * Retorna Cnab configurado para o banco HSBC 
	 * @param cnab
	 */
    public static CNAB newInstanceCnab400Hsbc(){
    	
    	CNAB cnab = new CNAB(PadraoCNAB.CNAB400);
    	
		cnab.indiceDataPagamentoInicio = 110;
		cnab.indiceDataPagamentoFim = 116;
		
		cnab.indiceNossoNumeroInicio = 37;
		cnab.indiceNossoNumeroFim = 62;
		
		cnab.indiceValorPagamentoInicio = 253;
		cnab.indiceValorPagamentoFim = 266;
		
		cnab.indiceNumeroRegistroInicio = 394;
		cnab.indiceNumeroRegistroFim = 400;
		
		cnab.indiceNumeroAgenciaInicio = 28;
		cnab.indiceNumeroAgenciaFim = 31;
		
		cnab.indiceNumeroContaInicio = 34;
		cnab.indiceNumeroContaFim = 44;
		
		return cnab;
	}
	
    /**
	 * Retorna Cnab configurado para o banco Bradesco
	 * @param cnab
	 */
    public static CNAB newInstanceCnab400Bradesco(){
    	
    	CNAB cnab = new CNAB(PadraoCNAB.CNAB400);

    	cnab.indiceDataPagamentoInicio = 110;
    	cnab.indiceDataPagamentoFim = 116;
		
    	cnab.indiceNossoNumeroInicio = 70;
    	cnab.indiceNossoNumeroFim = 81;
		
    	cnab.indiceValorPagamentoInicio = 256;
    	cnab.indiceValorPagamentoFim = 266;
		
    	cnab.indiceNumeroRegistroInicio = 395;
    	cnab.indiceNumeroRegistroFim = 400;
		
    	cnab.indiceNumeroAgenciaInicio = 26;
    	cnab.indiceNumeroAgenciaFim = 29;
		
    	cnab.indiceNumeroContaInicio = 31;
    	cnab.indiceNumeroContaFim = 37;
    	
    	return cnab;
	}
    
    /**
	 * Retorna Cnab configurado para o banco Itau
	 * @param cnab
	 */
    public static CNAB newInstanceCnab400Itau(){
    	
    	CNAB cnab = new CNAB(PadraoCNAB.CNAB400);

    	cnab.indiceDataPagamentoInicio = 295;
    	cnab.indiceDataPagamentoFim = 301;
		
    	cnab.indiceNossoNumeroInicio = 62;
    	cnab.indiceNossoNumeroFim = 70;
		
    	cnab.indiceValorPagamentoInicio = 254;
    	cnab.indiceValorPagamentoFim = 266;
		
    	cnab.indiceNumeroRegistroInicio = 395;
		cnab.indiceNumeroRegistroFim = 400;

		cnab.indiceNumeroAgenciaInicio = 26;
		cnab.indiceNumeroAgenciaFim = 30;
		
		cnab.indiceNumeroContaInicio = 33;
		cnab.indiceNumeroContaFim = 38;
		
		return cnab;
	}
    
    /**
	 * Retorna Cnab configurado para o banco BB
	 * @param cnab
	 */
    public static CNAB newInstanceCnab400BancoDoBrasil(){
    	
    	CNAB cnab = new CNAB(PadraoCNAB.CNAB400);
 		
		cnab.indiceDataPagamentoInicio = 110;
		cnab.indiceDataPagamentoFim = 116;
		
		cnab.indiceNossoNumeroInicio = 37;
		cnab.indiceNossoNumeroFim = 53;
		
		cnab.indiceValorPagamentoInicio = 253;
		cnab.indiceValorPagamentoFim = 266;
		
		cnab.indiceNumeroRegistroInicio = 394;
		cnab.indiceNumeroRegistroFim = 400;
		
		cnab.indiceNumeroAgenciaInicio = 26;
		cnab.indiceNumeroAgenciaFim = 31;
		
		cnab.indiceNumeroContaInicio = 33;
		cnab.indiceNumeroContaFim = 44;
		
		return cnab;
	}
    
    /**
   	 * Retorna Cnab configurado para o banco Santander
   	 * @param cnab
   	 */
    public static CNAB newInstanceCnab400Santander(){
       	
       	CNAB cnab = new CNAB(PadraoCNAB.CNAB400);
    		
		cnab.indiceDataPagamentoInicio = 110;
		cnab.indiceDataPagamentoFim = 116;
		
		cnab.indiceNossoNumeroInicio = 62;
		cnab.indiceNossoNumeroFim = 70;
		
		cnab.indiceValorPagamentoInicio = 253;
		cnab.indiceValorPagamentoFim = 266;
		
		cnab.indiceNumeroRegistroInicio = 394;
		cnab.indiceNumeroRegistroFim = 400;
		
		//INFORMAÇÕES ABAIXO NO HEADER
		cnab.indiceNumeroAgenciaInicio = 26;
		cnab.indiceNumeroAgenciaFim = 30;
		
		cnab.indiceNumeroContaInicio = 30;
		cnab.indiceNumeroContaFim = 38;
		
		return cnab;
   	}

    /**
     * Verifica se a linha em questão possui informações sobre a data de pagamento.
     * Para o padrão CNAB 240 esta informação se encontra em segmentos específicos
     * de acordo com o banco que emitiu o arquivo de retorno.
     * 
     * @param line
     * @return boolean
     */
	public boolean containsDataPagamento(String line) {
		if(PadraoCNAB.CNAB400.equals(this.padraoCNAB)) {
			return true;
		} else {
			
			String tipoSegmento = br.com.abril.nds.util.cnab.UtilitarioCNAB.PadraoCNAB.CNAB240.obterTipoSegmento(line);
			return this.segmentoDataPagamento.equals(tipoSegmento);
		}
	}

	/**
     * Verifica se a linha em questão possui informações sobre Nosso Numero.
     * Para o padrão CNAB 240 esta informação se encontra em segmentos específicos
     * de acordo com o banco que emitiu o arquivo de retorno.
     * 
     * @param line
     * @return boolean
     */
	public boolean containsNossoNumero(String line) {
		if(PadraoCNAB.CNAB400.equals(this.padraoCNAB)) {
			return true;
		} else {
			String tipoSegmento = br.com.abril.nds.util.cnab.UtilitarioCNAB.PadraoCNAB.CNAB240.obterTipoSegmento(line);
			return this.segmentoDataPagamento.equals(tipoSegmento);
		}
	}

    /**
     * Verifica se a linha em questão possui informações sobre o Valor Pagamento.
     * Para o padrão CNAB 240 esta informação se encontra em segmentos específicos
     * de acordo com o banco que emitiu o arquivo de retorno.
     * 
     * @param line
     * @return boolean
     */
	public boolean containsValorPagamento(String line) {
		if(PadraoCNAB.CNAB400.equals(this.padraoCNAB)) {
			return true;
		} else {
			String tipoSegmento = br.com.abril.nds.util.cnab.UtilitarioCNAB.PadraoCNAB.CNAB240.obterTipoSegmento(line);
			return this.segmentoDataPagamento.equals(tipoSegmento);
		}
	}
	
}
