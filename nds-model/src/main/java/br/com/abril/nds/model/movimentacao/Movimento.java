package br.com.abril.nds.model.movimentacao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.abril.nds.model.aprovacao.Aprovacao;
import br.com.abril.nds.model.integracao.StatusIntegracao;
import br.com.abril.nds.model.seguranca.Usuario;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@TableGenerator(name="SEQ_GENERATOR", table="SEQ_GENERATOR", initialValue = 1, allocationSize = 1)
public abstract class Movimento extends Aprovacao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SEQ_GENERATOR")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", nullable = false)
	private Date data;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CRIACAO", nullable = false)
	private Date dataCriacao;

	@ManyToOne(optional = false)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "TIPO_MOVIMENTO_ID")
	private TipoMovimento tipoMovimento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_INTEGRACAO")
	private StatusIntegracao statusIntegracao = StatusIntegracao.NAO_INTEGRADO;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INTEGRACAO", nullable = true)
	private Date dataIntegracao;
	
	public Movimento() {
        this.dataCriacao = new Date();
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
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @return the dataCriacao
	 */
	public Date getDataCriacao() {
		return dataCriacao;
	}

	/**
	 * @param dataCriacao the dataCriacao to set
	 */
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	/**
	 * @return the tipoMovimento
	 */
	public TipoMovimento getTipoMovimento() {
		return tipoMovimento;
	}

	/**
	 * @param tipoMovimento the tipoMovimento to set
	 */
	public void setTipoMovimento(TipoMovimento tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	/**
	 * @return the statusIntegracao
	 */
	public StatusIntegracao getStatusIntegracao() {
		return statusIntegracao;
	}

	/**
	 * @param statusIntegracao the statusIntegracao to set
	 */
	public void setStatusIntegracao(StatusIntegracao statusIntegracao) {
		this.statusIntegracao = statusIntegracao;
	}

	/**
	 * @return the dataIntegracao
	 */
	public Date getDataIntegracao() {
		return dataIntegracao;
	}

	/**
	 * @param dataIntegracao the dataIntegracao to set
	 */
	public void setDataIntegracao(Date dataIntegracao) {
		this.dataIntegracao = dataIntegracao;
	}

}
