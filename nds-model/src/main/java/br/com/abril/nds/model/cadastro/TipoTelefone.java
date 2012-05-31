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
	RADIO,
	CONTATO;
	
	public static String getDescricao(TipoTelefone tipoTelefone){
		if (tipoTelefone == null){
			return "";
		}
		
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
			case CONTATO:
				return "Contato";
			default:
				return "";
		}
	}
	
	public String toString(){
		return this.name();
	}
}