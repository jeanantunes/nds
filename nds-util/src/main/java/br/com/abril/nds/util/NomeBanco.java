package br.com.abril.nds.util;

public enum NomeBanco {
	
	BANCO_DO_BRASIL("001"),
	BANCO_DO_NORDESTE_DO_BRASIL("004"),
	BANCO_DO_ESTADO_DO_ESPIRITO_SANTO("021"),
	BANCO_SANTANDER("033"),
	BANCO_DO_ESTADO_DO_RIO_GRANDE_DO_SUL("041"),
	BANCO_INTEMEDIUM("077"),
	CAIXA_ECONOMICA_FEDERAL("104"),
	NOSSA_CAIXA("151"),
	BANCO_BRADESCO("237"),
	BANCO_ITAU("341"),
	BANCO_ABN_AMRO_REAL("356"),
	MERCANTIL_DO_BRASIL("389"),
	HSBC("399"),
	UNIBANCO("409"),
	BANCO_SAFRA("422"),
	BANCO_RURAL("453"),
	BANCO_SICREDI("748"),
	BANCOOB("756"),
	CREDCOMIM("085");
	
	private String numeroBanco;
	
	private NomeBanco(String numeroBanco){
		this.numeroBanco = numeroBanco;
	}

	public String getNumeroBanco() {
		return numeroBanco;
	}
	
	public static NomeBanco getByNumeroBanco(String numeroBanco){
		
		for (NomeBanco nomeBanco : NomeBanco.values()){
			if (nomeBanco.getNumeroBanco().equals(numeroBanco)){
				return nomeBanco;
			}
		}
		
		return null;
	}
}