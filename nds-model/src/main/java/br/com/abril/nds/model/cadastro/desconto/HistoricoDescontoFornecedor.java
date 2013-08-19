package br.com.abril.nds.model.cadastro.desconto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "HISTORICO_DESCONTOS_FORNECEDORES")
@SequenceGenerator(name="HISTORICO_DESCONTO_FORNECEDORES_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoDescontoFornecedor implements Serializable {

	private static final long serialVersionUID = 3028451605686762672L;

	@Id
	@GeneratedValue(generator = "HISTORICO_DESCONTO_FORNECEDORES_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_ALTERACAO")
	private Date dataAlteracao;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@ManyToOne(optional=false)
	@JoinColumn(name = "DESCONTO_ID")
	private Desconto desconto;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FORNECEDOR_ID")
	private Fornecedor fornecedor;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;
	

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

	public Desconto getDesconto() {
		return desconto;
	}

	public void setDesconto(Desconto desconto) {
		this.desconto = desconto;
	}

	/**
	 * @return the dataAlteracao
	 */
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	/**
	 * @param dataAlteracao the dataAlteracao to set
	 */
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
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

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	/**
	 * @return the distribuidor
	 */
	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	/**
	 * @param distribuidor the distribuidor to set
	 */
	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}

}
