package br.com.abril.nds.model.financeiro;

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

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "DIVIDA")
@SequenceGenerator(name="DIVIDA_SEQ", initialValue = 1, allocationSize = 1)
public class Divida {
	
	@Id
	@GeneratedValue(generator = "DIVIDA_SEQ")
	@Column(name = "ID")
	private Long id;
	
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
	
	@OneToMany
	private Set<Divida> acumulado = new HashSet<Divida>();
	
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
}