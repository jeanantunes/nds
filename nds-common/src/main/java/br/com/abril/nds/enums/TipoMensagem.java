package br.com.abril.nds.enums;

/**
 * Enum que representa os tipos de mensagem de validação.
 * 
 * @author Discover Technology
 *
 */
public enum TipoMensagem {
	
	NONE("none"),
	WARNING("warning"),
	ERROR("error"),
	SUCCESS("success");
	
	private String descricao;
	
	private TipoMensagem(String descricao) {

		this.descricao = descricao;
	}

	@Override
	public String toString() {

		return this.descricao;
	}
}