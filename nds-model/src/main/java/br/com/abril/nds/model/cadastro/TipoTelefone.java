package br.com.abril.nds.model.cadastro;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoTelefone {
	COMERCIAL,
	CELULAR,
	FAX,
	RESIDENCIAL,
	RADIO;
	
	public static String getDescricao(TipoTelefone tipoTelefone){
		switch(tipoTelefone){
			case COMERCIAL:
				return "Comercial";
			case CELULAR:
				return "Celular";
			case FAX:
				return "Fax";
			case RESIDENCIAL:
				return "Residencial";
			case RADIO:
				return "Rádio";
			default:
				return "";
		}
	}
}