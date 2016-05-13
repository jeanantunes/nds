package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "INFORMACOES_REPARTE_COMPLEMENTAR_ESTUDO")
@SequenceGenerator(name="INFORMACOES_REPARTE_COMPLEMENTAR_ESTUDO_SEQ", initialValue = 1, allocationSize = 1)
public class InformacoesReparteComplementarEstudo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(generator = "INFORMACOES_REPARTE_COMPLEMENTAR_ESTUDO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "ID_ESTUDO")
	private Long idEstudo;
	
	@Column(name = "TOTAL_REPARTE_A_DISTRIBUIR")
	private BigInteger totalReparteDistribuir;
	
	@Column(name = "TOTAL_REPARTE_COMPLEMENTAR")
	private BigInteger totalReparteDistribuidoPorComplementar;
	
	@Column(name = "TOTAL_REPARTE_DISTRIBUIDO")
	private BigInteger totalReparteDistribuidoSemComplementar;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdEstudo() {
		return idEstudo;
	}

	public void setIdEstudo(Long idEstudo) {
		this.idEstudo = idEstudo;
	}

	public BigInteger getTotalReparteDistribuir() {
		return totalReparteDistribuir;
	}

	public void setTotalReparteDistribuir(BigInteger totalReparteDistribuir) {
		this.totalReparteDistribuir = totalReparteDistribuir;
	}

	public BigInteger getTotalReparteDistribuidoPorComplementar() {
		return totalReparteDistribuidoPorComplementar;
	}

	public void setTotalReparteDistribuidoPorComplementar(BigInteger totalReparteDistribuidoPorComplementar) {
		this.totalReparteDistribuidoPorComplementar = totalReparteDistribuidoPorComplementar;
	}

	public BigInteger getTotalReparteDistribuidoSemComplementar() {
		return totalReparteDistribuidoSemComplementar;
	}

	public void setTotalReparteDistribuidoSemComplementar(BigInteger totalReparteDistribuidoSemComplementar) {
		this.totalReparteDistribuidoSemComplementar = totalReparteDistribuidoSemComplementar;
	}
	
}
