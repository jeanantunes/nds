package br.com.abril.nds.model.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Cota;

/**
 * @author Luis Gustavo Maschietto
 * @version 1.0
 * @created 16-mar-2012 14:00:00
 */
@Entity
@Table(name = "CONSOLIDADO_FINANCEIRO_COTA")
@SequenceGenerator(name="CONSOLIDADO_SEQ", initialValue = 1, allocationSize = 1)
public class ConsolidadoFinanceiroCota implements Serializable {
	
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(generator = "CONSOLIDADO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_CONSOLIDADO", nullable = false)
	private Date dataConsolidado;
	
	@Column(name = "VALOR_POSTERGADO", precision=18, scale=4)
	private BigDecimal valorPostergado;
	
	@Column(name = "CONSIGNADO", precision=18, scale=4)
	private BigDecimal consignado;

	@Column(name = "ENCALHE", precision=18, scale=4)
	private BigDecimal encalhe;
	
	@Column(name = "VENDA_ENCALHE", precision=18, scale=4)
	private BigDecimal vendaEncalhe;
	
	@Column(name = "DEBITO_CREDITO", precision=18, scale=4)
	private BigDecimal debitoCredito;
	
	@Column(name = "ENCARGOS", precision=18, scale=4)
	private BigDecimal encargos;
	
	@Column(name = "PENDENTE", precision=18, scale=4)
	private BigDecimal pendente;
	
	@Column(name = "TOTAL", nullable = false, precision=18, scale=4)
	private BigDecimal total;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "CONSOLIDADO_MVTO_FINANCEIRO_COTA",
			   joinColumns = {@JoinColumn(name = "CONSOLIDADO_FINANCEIRO_ID")}, 
			   inverseJoinColumns = {@JoinColumn(name = "MVTO_FINANCEIRO_COTA_ID", unique = true)})
	private List<MovimentoFinanceiroCota> movimentos = new ArrayList<MovimentoFinanceiroCota>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataConsolidado() {
		return dataConsolidado;
	}

	public void setDataConsolidado(Date dataConsolidado) {
		this.dataConsolidado = dataConsolidado;
	}

	public BigDecimal getValorPostergado() {
		return valorPostergado;
	}

	public void setValorPostergado(BigDecimal valorPostergado) {
		this.valorPostergado = valorPostergado;
	}

	public BigDecimal getConsignado() {
		return consignado;
	}

	public void setConsignado(BigDecimal consignado) {
		this.consignado = consignado;
	}

	public BigDecimal getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe;
	}

	public BigDecimal getVendaEncalhe() {
		return vendaEncalhe;
	}

	public void setVendaEncalhe(BigDecimal vendaEncalhe) {
		this.vendaEncalhe = vendaEncalhe;
	}

	public BigDecimal getDebitoCredito() {
		return debitoCredito;
	}

	public void setDebitoCredito(BigDecimal debitoCredito) {
		this.debitoCredito = debitoCredito;
	}

	public BigDecimal getEncargos() {
		return encargos;
	}

	public void setEncargos(BigDecimal encargos) {
		this.encargos = encargos;
	}

	public BigDecimal getPendente() {
		return pendente;
	}

	public void setPendente(BigDecimal pendente) {
		this.pendente = pendente;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public List<MovimentoFinanceiroCota> getMovimentos() {
		return movimentos;
	}
	
	public void setMovimentos(List<MovimentoFinanceiroCota> movimentos) {
		this.movimentos = movimentos;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cota == null) ? 0 : cota.hashCode());
        result = prime * result
                + ((dataConsolidado == null) ? 0 : dataConsolidado.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConsolidadoFinanceiroCota other = (ConsolidadoFinanceiroCota) obj;
        if (cota == null) {
            if (other.cota != null) {
                return false;
            }
        } else if (!cota.equals(other.cota)) {
            return false;
        }
        if (dataConsolidado == null) {
            if (other.dataConsolidado != null) {
                return false;
            }
        } else if (!dataConsolidado.equals(other.dataConsolidado)) {
            return false;
        }
        return true;
    }
	
	
	
}