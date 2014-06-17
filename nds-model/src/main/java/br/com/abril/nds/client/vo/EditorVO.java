package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

/**
 * Value Object para editor.
 * 
 */
@Exportable
public class EditorVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6179688578868744836L;

	private Long idEditor;
	
	private Long codigo;
	
	private String nome;

	public EditorVO(Long idEditor, Long codigo, String nome) {
		this.idEditor = idEditor;
		this.codigo = codigo;
		this.nome = nome;
	}
	
	public Long getIdEditor() {
		return idEditor;
	}

	public Long getCodigo() {
		return codigo;
	}

	public String getNome() {
		return nome;
	}

}