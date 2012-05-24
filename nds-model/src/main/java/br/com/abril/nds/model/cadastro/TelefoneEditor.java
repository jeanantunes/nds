package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TELEFONE_EDITOR")
@SequenceGenerator(name="TELEFONE_EDITOR_SEQ", initialValue = 1, allocationSize = 1)
public class TelefoneEditor extends AssociacaoTelefone implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8113593637472396104L;

	@Id
	@GeneratedValue(generator = "TELEFONE_EDITOR_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "EDITOR_ID")
	private Editor editor;
	
	/**
	 * Construtor padrão.
	 */
	public TelefoneEditor() {
		
		
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the editor
	 */
	public Editor getEditor() {
		return editor;
	}

	/**
	 * @param editor the editor to set
	 */
	public void setEditor(Editor editor) {
		this.editor = editor;
	}

}
