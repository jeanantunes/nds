package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TIPO_DESCONTO_COTA")
@SequenceGenerator(name="TIPO_DESCONTO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class TipoDescontoCota implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7293231301319891635L;
	
	@Id
	@GeneratedValue(generator = "TIPO_DESCONTO_COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "DESCONTO")
	private BigDecimal desconto;
	
	@Column(name="DATA_ALTERACAO")
	@Temporal(TemporalType.DATE)
	private Date dataAlteracao;
	
	@Column(name = "USUARIO")
	private String usuario;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ESPECIFICAO_DESCONTO", nullable = false)
	private EspecificacaoDesconto especificacaoDesconto;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public EspecificacaoDesconto getEspecificacaoDesconto() {
		return especificacaoDesconto;
	}

	public void setEspecificacaoDesconto(EspecificacaoDesconto especificacaoDesconto) {
		this.especificacaoDesconto = especificacaoDesconto;
	}
}
