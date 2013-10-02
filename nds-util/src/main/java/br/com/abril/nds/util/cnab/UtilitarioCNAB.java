package br.com.abril.nds.util.cnab;




public class UtilitarioCNAB {

	public static final String BANCO_HSBC = "399";
	
	public static final String BANCO_BRADESCO = "237";
	
	public static final String BANCO_ITAU = "341";
	
	public static final String BANCO_DO_BRASIL = "001";
	
	public static final String BANCO_CAIXA_ECONOMICA_FEDERAL = "104";
	
	public static final String BANCO_SANTANDER = "033";
	
	/**
	 * Contém os códigos dos tipos de registros 
	 * existentes nos padrões CNAB 240 e CNAB 400
	 */
	public enum TipoRegistroCNAB {
		
		TipoRegistroCNAB240("0", "1", "2", "3", "4", "5", "9", "J", "52"),
		TipoRegistroCNAB400("0", "1", "9");

		private String header;
		private String headerLote;
		private String inicialLote;
		private String detalhe;
		private String finalLote;
		private String trailerLote;
		private String trailer;
		private String segmentoJ;
		private String segmentoJ52;
		
		TipoRegistroCNAB(String header, String detalhe, String trailer) {
			
			this.header = header;
			this.detalhe = detalhe;
			this.trailer = trailer;
			
		}
		
		TipoRegistroCNAB(
				String header,
				String headerLote,
				String inicialLote,
				String detalhe,
				String finalLote,
				String trailerLote,
				String trailer,
				String segmentoJ,
				String segmentoJ52) {
			
			this.header = header;
			this.headerLote = headerLote;
			this.inicialLote = inicialLote;
			this.detalhe = detalhe;
			this.finalLote = finalLote;
			this.trailerLote = trailerLote;
			this.trailer = trailer;
			this.segmentoJ = segmentoJ;
			this.segmentoJ52 = segmentoJ52;
		}

		public String getHeader() {
			return header;
		}

		public String getHeaderLote() {
			return headerLote;
		}

		public String getInicialLote() {
			return inicialLote;
		}

		public String getDetalhe() {
			return detalhe;
		}

		public String getFinalLote() {
			return finalLote;
		}

		public String getTrailerLote() {
			return trailerLote;
		}

		public String getTrailer() {
			return trailer;
		}

		public String getSegmentoJ() {
			return segmentoJ;
		}

		public String getSegmentoJ52() {
			return segmentoJ52;
		}
		
	}
	
	public enum IndiceCNAB {
		
		IndiceCNAB240(7,8,0,3),
		IndiceCNAB400(0,1,76,79);

		private int indiceInicioTipoRegistro;
		private int indiceFimTipoRegistro;

		private int indiceInicioBanco;
		private int indiceFimBanco;

		
		IndiceCNAB(	int indiceInicioTipoRegistro, int indiceFimTipoRegistro,
					int indiceInicioBanco, int indiceFimBanco) {
			
			this.indiceInicioTipoRegistro = indiceInicioTipoRegistro;
			this.indiceFimTipoRegistro = indiceFimTipoRegistro;
			
			this.indiceInicioBanco = indiceInicioBanco;
			this.indiceFimBanco = indiceFimBanco;
		}


		public int getIndiceInicioTipoRegistro() {
			return indiceInicioTipoRegistro;
		}

		public int getIndiceFimTipoRegistro() {
			return indiceFimTipoRegistro;
		}

		public int getIndiceInicioBanco() {
			return indiceInicioBanco;
		}

		public int getIndiceFimBanco() {
			return indiceFimBanco;
		}
		
	}
	
	
	public enum PadraoCNAB {
		
		CNAB240(TipoRegistroCNAB.TipoRegistroCNAB240, IndiceCNAB.IndiceCNAB240, "CNAB 240", 240, 5),
		CNAB400(TipoRegistroCNAB.TipoRegistroCNAB400, IndiceCNAB.IndiceCNAB400, "CNAB 400", 400, 3);
		
		private TipoRegistroCNAB tipoRegistroCNAB;
		private IndiceCNAB indiceCNAB;
		private String descricao;
		private int size;
		private int qtdMinimaLinhas;
		
		PadraoCNAB(
				TipoRegistroCNAB tipoRegistroCNAB,
			    IndiceCNAB indiceCNAB,
				String descricao, 
				int size,
				int qtdMinimaLinhas) {
			
			this.tipoRegistroCNAB = tipoRegistroCNAB;
			this.indiceCNAB = indiceCNAB;
			this.descricao = descricao;
			this.size = size;
		}
		
		public int getSize(){
			return this.size;
		}
		
		public String getDescricao(){
			return this.descricao;
		}
		
		public String obterCodigoBanco(String headerArquivo) {
			return headerArquivo.substring(indiceCNAB.getIndiceInicioBanco(), indiceCNAB.getIndiceFimBanco());
		}
		
		public String obterTipoRegistro(String line) {
			return line.substring(indiceCNAB.getIndiceInicioTipoRegistro(), indiceCNAB.getIndiceFimTipoRegistro());
		}
		
		public TipoRegistroCNAB getTipoRegistroCNAB() {
			return tipoRegistroCNAB;
		}

		public boolean isHeader(String line) {
		  return  tipoRegistroCNAB.getHeader().equals(obterTipoRegistro(line));
		}
		
		public boolean isDetalhe(String line) {
		  return  tipoRegistroCNAB.getDetalhe().equals(obterTipoRegistro(line));
		}
		
		public boolean isTrailer(String line) {
			  return  tipoRegistroCNAB.getTrailer().equals(obterTipoRegistro(line));
		}
		
		
		public boolean possuiQuantidadeMinimaLinhas(int qtdLinhas){
			return qtdLinhas >= qtdMinimaLinhas;
		}
		
		
	}
	
	public static CNAB obterCNAB(PadraoCNAB padraoCNAB, String codigoBanco){
		
		if(PadraoCNAB.CNAB240.equals(padraoCNAB)) {

	    	switch (codigoBanco) {
			
				case BANCO_HSBC:
					throw new IllegalStateException("Leitura desse padrao ainda nao implementada");
				case BANCO_BRADESCO:
					throw new IllegalStateException("Leitura desse padrao ainda nao implementada");
				case BANCO_ITAU:
					throw new IllegalStateException("Leitura desse padrao ainda nao implementada");
				case BANCO_DO_BRASIL:
					throw new IllegalStateException("Leitura desse padrao ainda nao implementada");
				default:
					throw new IllegalStateException("Leitura desse padrao ainda nao implementada");
			}

			
		} else if(PadraoCNAB.CNAB400.equals(padraoCNAB)) {

	    	switch (codigoBanco) {
			
				case BANCO_HSBC:
					return CNAB.newInstanceCnab400Hsbc();
				case BANCO_BRADESCO:
					return CNAB.newInstanceCnab400Bradesco();
				case BANCO_ITAU:
					return CNAB.newInstanceCnab400Itau();
				case BANCO_DO_BRASIL:
					return CNAB.newInstanceCnab400BancoDoBrasil();
				default:
					return null;
			}
			
		} else {
			
			return null;
			
		}
		
    }

   
    

	
}
