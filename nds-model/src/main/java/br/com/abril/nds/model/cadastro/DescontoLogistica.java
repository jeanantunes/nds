package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "DESCONTO_LOGISTICA")
@SuppressWarnings("serial")
@SequenceGenerator(name="DESCONTO_LOGISTICA_SEQ", initialValue = 1, allocationSize = 1)
public class DescontoLogistica implements Serializable {
	
	@Id
	@GeneratedValue(generator = "DESCONTO_LOGISTICA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "TIPO_DESCONTO", nullable = false)
	private Integer tipoDesconto;
	
	@Column(name = "PERCENTUAL_DESCONTO", nullable = false, precision=18, scale=4)
	private BigDecimal percentualDesconto;
	
	@Column(name = "PERCENTUAL_PRESTACAO_SERVICO", nullable = false, precision=18, scale=4)
	private BigDecimal percentualPrestacaoServico;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INICIO_VIGENCIA", nullable = false)
	private Date dataInicioVigencia;
	
	@OneToMany(mappedBy="descontoLogistica")
	private Set<Produto> produtos = new HashSet<Produto>();
	
	@ManyToOne
	@JoinColumn(name = "FORNECEDOR_ID")
	private Fornecedor fornecedor;
	
	/**
	 * Construtor padrão.
	 */
	public DescontoLogistica() {
		
		
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
	 * @return the tipoDesconto
	 */
	public Integer getTipoDesconto() {
		return tipoDesconto;
	}

	/**
	 * @param tipoDesconto the tipoDesconto to set
	 */
	public void setTipoDesconto(Integer tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	/**
	 * @return the percentualDesconto
	 */
	public BigDecimal getPercentualDesconto() {
		return percentualDesconto;
	}

	/**
	 * @param percentualDesconto the percentualDesconto to set
	 */
	public void setPercentualDesconto(BigDecimal percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}

	/**
	 * @return the percentualPrestacaoServico
	 */
	public BigDecimal getPercentualPrestacaoServico() {
		return percentualPrestacaoServico;
	}

	/**
	 * @param percentualPrestacaoServico the percentualPrestacaoServico to set
	 */
	public void setPercentualPrestacaoServico(BigDecimal percentualPrestacaoServico) {
		this.percentualPrestacaoServico = percentualPrestacaoServico;
	}

	/**
	 * @return the dataInicioVigencia
	 */
	public Date getDataInicioVigencia() {
		return dataInicioVigencia;
	}

	/**
	 * @param dataInicioVigencia the dataInicioVigencia to set
	 */
	public void setDataInicioVigencia(Date dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	public Set<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(Set<Produto> produtos) {
		this.produtos = produtos;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}	
	
    public Fornecedor getFornecedor() {
        return fornecedor;
    }
    
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DescontoLogistica other = (DescontoLogistica) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		try {
		return "DescontoLogistica [ descricao=" + descricao
				+ ", tipoDesconto=" + tipoDesconto + ", percentualDesconto="
				+ percentualDesconto + ", percentualPrestacaoServico="
				+ percentualPrestacaoServico + ", dataInicioVigencia="
				+ dataInicioVigencia 
				+ ", fornecedor=" + fornecedor + "]";
		} catch ( Exception e ) {
		   return ""+this.id;
		}
	}

	
	
}
