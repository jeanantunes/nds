package br.com.abril.nds.enums;

public enum ModalidadeFreteEnum {

	POR_CONTA_EMITENTE(0),
	POR_CONTA_DESTINATARIO_REMETENTE(1),
	POR_CONTA_TERCEIROS(2),
	SEM_FRETE_v2_0(9);

	private Integer descricao;

	ModalidadeFreteEnum(Integer descricao) {
		this.descricao = descricao;
	}

	public Integer getDescricao() {
		return descricao;
	}
	
}