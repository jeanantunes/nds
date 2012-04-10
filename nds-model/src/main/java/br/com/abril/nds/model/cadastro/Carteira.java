package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "CARTEIRA")
@SequenceGenerator(name="CARTEIRA_SEQ", initialValue = 1, allocationSize = 1)
public class Carteira implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -863137343136339177L;
	
	@Id
	@GeneratedValue(generator = "CARTEIRA_SEQ")
	private Long id;
	@Column(name = "CODIGO", nullable = false)
	private int codigo;
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_REGISTRO_COBRANCA")
	private TipoRegistroCobranca tipoRegistroCobranca;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public TipoRegistroCobranca getTipoRegistroCobranca() {
		return tipoRegistroCobranca;
	}
	
	public void setTipoRegistroCobranca(TipoRegistroCobranca tipoRegistroCobranca) {
		this.tipoRegistroCobranca = tipoRegistroCobranca;
	}

}
