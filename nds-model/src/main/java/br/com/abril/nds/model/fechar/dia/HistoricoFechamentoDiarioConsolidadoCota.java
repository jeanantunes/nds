package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_COTA")
@SequenceGenerator(name = "HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoFechamentoDiarioConsolidadoCota implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_COTA_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_ID")
	private FechamentoDiario fechamentoDiario;
	
	@Column(name="QNT_TOTAL")
	private BigInteger quantidadeTotal;
	
	@Column(name="QNT_ATIVOS")
	private BigInteger quantidadeAtivos;
	
	@Column(name="QNT_AUSENTE_REPARTE")
	private BigInteger quantidadeAusenteReparte;
	
	@Column(name="QNT_AUSENTE_ENCALHE")
	private BigInteger quantidadeAusenteEncalhe;
	
	@Column(name="QNT_NOVOS")
	private BigInteger quantidadeNovos;
	
	@Column(name="QNT_INATIVAS")
	private BigInteger quantidadeInativas;
	
	@OneToMany(mappedBy = "historicoConsolidadoCota")
	private List<HistoricoFechamentoDiarioCota> historicoFechamentoDiarioCotas;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public FechamentoDiario getFechamentoDiario() {
		return fechamentoDiario;
	}

	public void setFechamentoDiario(FechamentoDiario fechamentoDiario) {
		this.fechamentoDiario = fechamentoDiario;
	}

	public BigInteger getQuantidadeTotal() {
		return quantidadeTotal;
	}

	public void setQuantidadeTotal(BigInteger quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}

	public BigInteger getQuantidadeAtivos() {
		return quantidadeAtivos;
	}

	public void setQuantidadeAtivos(BigInteger quantidadeAtivos) {
		this.quantidadeAtivos = quantidadeAtivos;
	}

	public BigInteger getQuantidadeAusenteReparte() {
		return quantidadeAusenteReparte;
	}

	public void setQuantidadeAusenteReparte(BigInteger quantidadeAusenteReparte) {
		this.quantidadeAusenteReparte = quantidadeAusenteReparte;
	}

	public BigInteger getQuantidadeAusenteEncalhe() {
		return quantidadeAusenteEncalhe;
	}

	public void setQuantidadeAusenteEncalhe(BigInteger quantidadeAusenteEncalhe) {
		this.quantidadeAusenteEncalhe = quantidadeAusenteEncalhe;
	}

	public BigInteger getQuantidadeNovos() {
		return quantidadeNovos;
	}

	public void setQuantidadeNovos(BigInteger quantidadeNovos) {
		this.quantidadeNovos = quantidadeNovos;
	}

	public BigInteger getQuantidadeInativas() {
		return quantidadeInativas;
	}

	public void setQuantidadeInativas(BigInteger quantidadeInativas) {
		this.quantidadeInativas = quantidadeInativas;
	}

	public List<HistoricoFechamentoDiarioCota> getHistoricoFechamentoDiarioCotas() {
		return historicoFechamentoDiarioCotas;
	}

	public void setHistoricoFechamentoDiarioCotas(
			List<HistoricoFechamentoDiarioCota> historicoFechamentoDiarioCotas) {
		this.historicoFechamentoDiarioCotas = historicoFechamentoDiarioCotas;
	}
}
