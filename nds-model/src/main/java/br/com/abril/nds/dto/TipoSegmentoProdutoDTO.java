package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class TipoSegmentoProdutoDTO implements Serializable {
	private static final long serialVersionUID = -2778240517105998718L;
	
	private Long idSegmento;
	private String descricao;

	public Long getIdSegmento() {
		return idSegmento;
	}

	public void setIdSegmento(Long idSegmento) {
		this.idSegmento = idSegmento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
