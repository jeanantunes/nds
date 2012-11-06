package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
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
@Table(name = "FECHAMENTO_DIARIO_CONSOLIDADO_COTA")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_CONSOLIDADO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiarioConsolidadoCota implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_CONSOLIDADO_COTA_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_ID")
	private FechamentoDiario fechamentoDiario;
	
	@Column(name="QNT_TOTAL")
	private Integer quantidadeTotal;
	
	@Column(name="QNT_ATIVOS")
	private Integer quantidadeAtivos;
	
	@Column(name="QNT_AUSENTE_REPARTE")
	private Integer quantidadeAusenteReparte;
	
	@Column(name="QNT_AUSENTE_ENCALHE")
	private Integer quantidadeAusenteEncalhe;
	
	@Column(name="QNT_NOVOS")
	private Integer quantidadeNovos;
	
	@Column(name="QNT_INATIVAS")
	private Integer quantidadeInativas;
	
	@OneToMany(mappedBy = "fechamentoDiarioConsolidadoCota")
	private List<FechamentoDiarioCota> fechamentoDiarioCotas;
	
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

	public Integer getQuantidadeTotal() {
		return quantidadeTotal;
	}

	public void setQuantidadeTotal(Integer quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}

	public Integer getQuantidadeAtivos() {
		return quantidadeAtivos;
	}

	public void setQuantidadeAtivos(Integer quantidadeAtivos) {
		this.quantidadeAtivos = quantidadeAtivos;
	}

	public Integer getQuantidadeAusenteReparte() {
		return quantidadeAusenteReparte;
	}

	public void setQuantidadeAusenteReparte(Integer quantidadeAusenteReparte) {
		this.quantidadeAusenteReparte = quantidadeAusenteReparte;
	}

	public Integer getQuantidadeAusenteEncalhe() {
		return quantidadeAusenteEncalhe;
	}

	public void setQuantidadeAusenteEncalhe(Integer quantidadeAusenteEncalhe) {
		this.quantidadeAusenteEncalhe = quantidadeAusenteEncalhe;
	}

	public Integer getQuantidadeNovos() {
		return quantidadeNovos;
	}

	public void setQuantidadeNovos(Integer quantidadeNovos) {
		this.quantidadeNovos = quantidadeNovos;
	}

	public Integer getQuantidadeInativas() {
		return quantidadeInativas;
	}

	public void setQuantidadeInativas(Integer quantidadeInativas) {
		this.quantidadeInativas = quantidadeInativas;
	}

	public List<FechamentoDiarioCota> getFechamentoDiarioCotas() {
		return fechamentoDiarioCotas;
	}

	public void setFechamentoDiarioCotas(
			List<FechamentoDiarioCota> fechamentoDiarioCotas) {
		this.fechamentoDiarioCotas = fechamentoDiarioCotas;
	}
}
