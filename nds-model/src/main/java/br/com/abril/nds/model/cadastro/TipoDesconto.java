package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TIPO_DESCONTO")
@SequenceGenerator(name="TIPO_DESCONTO_SEQ", initialValue = 1, allocationSize = 1)
public class TipoDesconto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "TIPO_DESCONTO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@Column(name="CODIGO")
	private String codigo;
	
	@Column(name="PORCENTAGEM")
	private BigDecimal porcentagem;

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
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the porcentagem
	 */
	public BigDecimal getPorcentagem() {
		return porcentagem;
	}

	/**
	 * @param porcentagem the porcentagem to set
	 */
	public void setPorcentagem(BigDecimal porcentagem) {
		this.porcentagem = porcentagem;
	}
	
	
}
