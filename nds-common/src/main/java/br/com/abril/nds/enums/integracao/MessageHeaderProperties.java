package br.com.abril.nds.enums.integracao;

/**
 * Enum com as chaves das propriedades que vão no header da mensagem
 * 
 * @author jonatas.junior
 */
public enum MessageHeaderProperties {

	URI("URI"),
	PAYLOAD("PAYLOAD"),
	FILE_NAME("FILE_NAME"),
	FILE_CREATION_DATE("FILE_CREATION_DATE,"),
	LINE_NUMBER("LINE_NUMBER"),
	USER_NAME("USER_NAME"),
	INTERFACE("INTERFACE"),
	CODIGO_DISTRIBUIDOR("CODIGO_DISTRIBUIDOR"),
	ERRO_PROCESSAMENTO("ERRO_PROCESSAMENTO"),
	OUTBOUND_FOLDER("OUTBOUND_FOLDER");
	
	private String value;
	
	private MessageHeaderProperties(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
