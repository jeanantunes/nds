package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class BoxDTO implements Serializable {

	private static final long serialVersionUID = -6593247184450400133L;

	@Export(label="Box")
	private Integer codigo;
	
	@Export(label="Nome")
	private String nome;
	
	@Export(label="Tipo de Box")
	private TipoBox tipoBox;

	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the tipoBox
	 */
	public TipoBox getTipoBox() {
		return tipoBox;
	}

	/**
	 * @param tipoBox the tipoBox to set
	 */
	public void setTipoBox(TipoBox tipoBox) {
		this.tipoBox = tipoBox;
	}
}
