package br.com.abril.nds.util.cnab;


public class CNAB {

	private CNAB(){}
	
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

	
	
	public static CNAB newInstanceCnab240CaixaEconomicaFederal(){
		
		//TODO: documentacao cnab 240 da caixa
		// não possui o segmento de layout
		// apresentado nos bancos:  
		// - bradesco, 
		// - banco do brasil
		// - hsbc 
		// - e itau...
    	CNAB cnab = new CNAB();
 		
//		cnab.indiceDataPagamentoInicio = ;
//		cnab.indiceDataPagamentoFim = ;
//		
//		cnab.indiceNossoNumeroInicio = ;
//		cnab.indiceNossoNumeroFim = ;
//		
//		cnab.indiceValorPagamentoInicio = ;
//		cnab.indiceValorPagamentoFim = ;
//		
//		cnab.indiceNumeroRegistroInicio = ;
//		cnab.indiceNumeroRegistroFim = ;
//		
//		
//		//Informações abaixo no header de arquivo
//		//Dígito Verificador da Ag/Conta 71 72 
//		cnab.indiceNumeroAgenciaInicio = 52;
//		cnab.indiceNumeroAgenciaFim = 58;
//		
//		cnab.indiceNumeroContaInicio = 58;
//		cnab.indiceNumeroContaFim = 71;
		
		return cnab;    	
    	
    }	
    public static CNAB newInstanceCnab240Santander(){

    	CNAB cnab = new CNAB();
    	
    	//TODO : Não encontramos doc cnab 240 oficial
    	// do 
// 		
//		cnab.indiceDataPagamentoInicio = ;
//		cnab.indiceDataPagamentoFim = ;
//		
//		cnab.indiceNossoNumeroInicio = ;
//		cnab.indiceNossoNumeroFim = ;
//		
//		cnab.indiceValorPagamentoInicio = ;
//		cnab.indiceValorPagamentoFim = ;
//		
//		cnab.indiceNumeroRegistroInicio = ;
//		cnab.indiceNumeroRegistroFim = ;
//		
//		
//		//Informacoes abaixo no header de arquivo
//		cnab.indiceNumeroAgenciaInicio = ;
//		cnab.indiceNumeroAgenciaFim = ;
//		
//		cnab.indiceNumeroContaInicio = ;
//		cnab.indiceNumeroContaFim = ;
		
		return cnab;    	
    	
    }
	
	
    public static CNAB newInstanceCnab240Bradesco(){
    	
    	CNAB cnab = new CNAB();
 		
		cnab.indiceDataPagamentoInicio = 144;
		cnab.indiceDataPagamentoFim = 152;
		
		cnab.indiceNossoNumeroInicio = 202;
		cnab.indiceNossoNumeroFim = 222;
		
		cnab.indiceValorPagamentoInicio = 152;
		cnab.indiceValorPagamentoFim = 167;
		
		cnab.indiceNumeroRegistroInicio = 8;
		cnab.indiceNumeroRegistroFim = 13;
		
		
		//Informacoes abaixo no header de arquivo
		//Dígito Verificador da Ag/Conta 71 72 
		cnab.indiceNumeroAgenciaInicio = 52;
		cnab.indiceNumeroAgenciaFim = 58;
		
		cnab.indiceNumeroContaInicio = 58;
		cnab.indiceNumeroContaFim = 71;
		
		return cnab;    	
    }
	
	
    public static CNAB newInstanceCnab240BancoDoBrasil(){
    	
    	CNAB cnab = new CNAB();
 		
		cnab.indiceDataPagamentoInicio = 144;
		cnab.indiceDataPagamentoFim = 152;
		
		cnab.indiceNossoNumeroInicio = 202;
		cnab.indiceNossoNumeroFim = 222;
		
		cnab.indiceValorPagamentoInicio = 152;
		cnab.indiceValorPagamentoFim = 167;
		
		cnab.indiceNumeroRegistroInicio = 8;
		cnab.indiceNumeroRegistroFim = 13;
		
		
		//Informacoes abaixo no header de arquivo
		//Dígito Verificador da Ag/Conta 71 72 
		cnab.indiceNumeroAgenciaInicio = 52;
		cnab.indiceNumeroAgenciaFim = 58;
		
		cnab.indiceNumeroContaInicio = 58;
		cnab.indiceNumeroContaFim = 71;
		
		return cnab;
	}
	
    public static CNAB newInstanceCnab240HSBC(){
    	
    	CNAB cnab = new CNAB();

    	cnab.indiceDataPagamentoInicio = 144;
    	cnab.indiceDataPagamentoFim = 152;
		
    	cnab.indiceNossoNumeroInicio = 202;
    	cnab.indiceNossoNumeroFim = 218;
		
    	cnab.indiceValorPagamentoInicio = 154; 
    	cnab.indiceValorPagamentoFim = 167;
		
    	cnab.indiceNumeroRegistroInicio = 8; 
		cnab.indiceNumeroRegistroFim = 13;

		//Informacoes abaixo no header de arquivo
		//Dig. Verif. da Ag. Conta 71 72
		//Informacoes abaixo no header de arquivo
		cnab.indiceNumeroAgenciaInicio = 52;
		cnab.indiceNumeroAgenciaFim = 57;
		
		
		//Dig. Verif. da Conta 70 71
		cnab.indiceNumeroContaInicio = 58;
		cnab.indiceNumeroContaFim = 70;
		
		return cnab;
	}

	
    public static CNAB newInstanceCnab240Itau(){
    	
    	CNAB cnab = new CNAB();

    	cnab.indiceDataPagamentoInicio = 144;
    	cnab.indiceDataPagamentoFim = 152;
		
    	cnab.indiceNossoNumeroInicio = 215;
    	cnab.indiceNossoNumeroFim = 230;
		
    	cnab.indiceValorPagamentoInicio = 152; 
    	cnab.indiceValorPagamentoFim = 167;
		
    	cnab.indiceNumeroRegistroInicio = 8; 
		cnab.indiceNumeroRegistroFim = 13;

		//Informacoes abaixo no header de arquivo
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
    	
    	CNAB cnab = new CNAB();
    	
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
    	
    	CNAB cnab = new CNAB();
    	
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
	 * Retorna Cnab configurado para o banco Bradesco
	 * @param cnab
	 */
    public static CNAB newInstanceCnab400Bradesco(){
    	
    	CNAB cnab = new CNAB();

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
    	
    	CNAB cnab = new CNAB();

    	cnab.indiceDataPagamentoInicio = 366;
    	cnab.indiceDataPagamentoFim = 391;
		
    	cnab.indiceNossoNumeroInicio = 63;
    	cnab.indiceNossoNumeroFim = 70;
		
    	cnab.indiceValorPagamentoInicio = 127;
    	cnab.indiceValorPagamentoFim = 139;
		
    	cnab.indiceNumeroRegistroInicio = 395;
		cnab.indiceNumeroRegistroFim = 400;

		cnab.indiceNumeroAgenciaInicio = 143;
		cnab.indiceNumeroAgenciaFim = 147;
		
		cnab.indiceNumeroContaInicio = 24;
		cnab.indiceNumeroContaFim = 28;
		
		return cnab;
	}
    
    /**
	 * Retorna Cnab configurado para o banco BB
	 * @param cnab
	 */
    public static CNAB newInstanceCnab400BancoDoBrasil(){
    	
    	CNAB cnab = new CNAB();
 		
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
	
}
