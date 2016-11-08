package br.com.abril.nds.util.cnab;

public class UtilitarioCNAB {

	public static final String BANCO_HSBC = "399";
	
	public static final String BANCO_BRADESCO = "237";
	
	public static final String BANCO_ITAU = "341";
	
	public static final String BANCO_DO_BRASIL = "001";
	
	public static final String BANCO_CAIXA_ECONOMICA_FEDERAL = "104";
	
	public static final String BANCO_SANTANDER = "033";
	
	public static final String CREDCOMIM = "085";
	
	public static final String SEGMENTO_J = "J";
	public static final String SEGMENTO_J52 = "J52";
	public static final String SEGMENTO_T = "T";
	public static final String SEGMENTO_U = "U";
	
	/**
	 * Contém os códigos dos tipos de registros 
	 * existentes nos padrões CNAB 240 e CNAB 400
	 */
	public enum TipoRegistroCNAB {
		
		TipoRegistroCNAB240("0", "1", "2", "3", "4", "5", "9"),
		TipoRegistroCNAB400("0", "1", "9");

		private String header;
		private String headerLote;
		private String inicialLote;
		private String detalhe;
		private String finalLote;
		private String trailerLote;
		private String trailer;
		
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
				String trailer) {
			
			this.header = header;
			this.headerLote = headerLote;
			this.inicialLote = inicialLote;
			this.detalhe = detalhe;
			this.finalLote = finalLote;
			this.trailerLote = trailerLote;
			this.trailer = trailer;
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
		
	}
	
	public enum IndiceCNAB {
		
		IndiceCNAB240(7,8, 0,3, 13,14),
		IndiceCNAB400(0,1, 76,79);

		private int indiceInicioTipoRegistro;
		private int indiceFimTipoRegistro;

		private int indiceInicioBanco;
		private int indiceFimBanco;
		
		private int indiceInicioTipoSegmento;
		private int indiceFimTipoSegmento;

		
		IndiceCNAB(	int indiceInicioTipoRegistro, int indiceFimTipoRegistro,
					int indiceInicioBanco, int indiceFimBanco) {
			
			this.indiceInicioTipoRegistro = indiceInicioTipoRegistro;
			this.indiceFimTipoRegistro = indiceFimTipoRegistro;
			
			this.indiceInicioBanco = indiceInicioBanco;
			this.indiceFimBanco = indiceFimBanco;
		}

		IndiceCNAB(	int indiceInicioTipoRegistro, int indiceFimTipoRegistro,
				int indiceInicioBanco, int indiceFimBanco, 
				int indiceInicioTipoSegmento, int indiceFimTipoSegmento) {
		
		this.indiceInicioTipoRegistro = indiceInicioTipoRegistro;
		this.indiceFimTipoRegistro = indiceFimTipoRegistro;
		
		this.indiceInicioBanco = indiceInicioBanco;
		this.indiceFimBanco = indiceFimBanco;
		
		this.indiceInicioTipoSegmento = indiceInicioTipoSegmento;
		this.indiceFimTipoSegmento = indiceFimTipoSegmento;
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

		public int getIndiceInicioTipoSegmento() {
			return indiceInicioTipoSegmento;
		}

		public int getIndiceFimTipoSegmento() {
			return indiceFimTipoSegmento;
		}
		
	}
	
	
	public enum PadraoCNAB {
		
		CNAB240(TipoRegistroCNAB.TipoRegistroCNAB240, IndiceCNAB.IndiceCNAB240, "CNAB 240", 240, 5, "ddMMyyyy"),
		CNAB400(TipoRegistroCNAB.TipoRegistroCNAB400, IndiceCNAB.IndiceCNAB400, "CNAB 400", 400, 3, "ddMMyy");
		
		private TipoRegistroCNAB tipoRegistroCNAB;
		private IndiceCNAB indiceCNAB;
		private String descricao;
		private int size;
		private int qtdMinimaLinhas;
		private String formatoDataArquivoCNAB;
		
		PadraoCNAB(
				TipoRegistroCNAB tipoRegistroCNAB,
			    IndiceCNAB indiceCNAB,
				String descricao, 
				int size,
				int qtdMinimaLinhas, 
				String formatoDataArquivoCNAB) {
			
			this.tipoRegistroCNAB = tipoRegistroCNAB;
			this.indiceCNAB = indiceCNAB;
			this.descricao = descricao;
			this.size = size;
			this.formatoDataArquivoCNAB = formatoDataArquivoCNAB;
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
		
		public String obterTipoSegmento(String line) {
			
			if(isDetalhe(line)) {
				return line.substring(indiceCNAB.getIndiceInicioTipoSegmento(), indiceCNAB.getIndiceFimTipoSegmento());
			} else {
				return "-1";
			}
			
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

		public String getFormatoDataArquivoCNAB() {
			return formatoDataArquivoCNAB;
		}
		
	}	
}