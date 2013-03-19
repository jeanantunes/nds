package br.com.abril.nds.model.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "DIVIDA")
@SequenceGenerator(name="DIVIDA_SEQ", initialValue = 1, allocationSize = 1)
public class Divida implements Serializable {
	
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(generator = "DIVIDA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", nullable = false)
	private Date data;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario responsavel;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Column(name = "VALOR", nullable = false)
	private BigDecimal valor;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusDivida status;
	
	@OneToOne(mappedBy = "divida")
	private Cobranca cobranca;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "CONSOLIDADO_ID")
	private ConsolidadoFinanceiroCota consolidado;	
	
	// Divida -> acumulados
	@OneToMany(mappedBy="dividaRaiz")
	private Set<Divida> acumulado = new HashSet<Divida>();
	
	// Acumulados -> Divida
	@ManyToOne(optional=true)
	@JoinColumn(name="DIVIDA_RAIZ_ID")
	private Divida dividaRaiz;
	
	@Column(name = "ACUMULADA")
	private boolean acumulada;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Usuario getResponsavel() {
		return responsavel;
	}
	
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public StatusDivida getStatus() {
		return status;
	}
	
	public void setStatus(StatusDivida status) {
		this.status = status;
	}
	
	public Cobranca getCobranca() {
		return cobranca;
	}
	
	public void setCobranca(Cobranca cobranca) {
		this.cobranca = cobranca;
	}
	
	public ConsolidadoFinanceiroCota getConsolidado() {
		return consolidado;
	}
	
	public void setConsolidado(ConsolidadoFinanceiroCota consolidado) {
		this.consolidado = consolidado;
	}
	
	public Set<Divida> getAcumulado() {
		return acumulado;
	}
	
	public void setAcumulado(Set<Divida> acumulado) {
		this.acumulado = acumulado;
	}

	public boolean isAcumulada() {
		return acumulada;
	}

	public void setAcumulada(boolean acumulada) {
		this.acumulada = acumulada;
	}

	/**
	 * @return the dividaRaiz
	 */
	public Divida getDividaRaiz() {
		return dividaRaiz;
	}

	/**
	 * @param dividaRaiz the dividaRaiz to set
	 */
	public void setDividaRaiz(Divida dividaRaiz) {
		this.dividaRaiz = dividaRaiz;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((consolidado == null) ? 0 : consolidado.hashCode());
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
        Divida other = (Divida) obj;
        if (consolidado == null) {
            if (other.consolidado != null) {
                return false;
            }
        } else if (!consolidado.equals(other.consolidado)) {
            return false;
        }
        return true;
    }
	
	
}