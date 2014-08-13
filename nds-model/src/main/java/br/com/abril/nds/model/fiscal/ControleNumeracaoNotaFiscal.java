package br.com.abril.nds.model.fiscal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CONTROLE_NUMERACAO_NF")
@SequenceGenerator(name="CONTROLE_NUMERACAO_NF_SEQ", initialValue = 1, allocationSize = 1)
public class ControleNumeracaoNotaFiscal {
	
	@Id
	@GeneratedValue(generator = "CONTROLE_NUMERACAO_NF_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "SERIE_NF", nullable = false, unique = true)
	private String serieNF;
	
	@Column(name = "PROXIMO_NUMERO_NF", nullable = false)
	private Long proximoNumeroNF;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getSerieNF() {
		return serieNF;
	}
	
	public void setSerieNF(String serieNF) {
		this.serieNF = serieNF;
	}
	
	public Long getProximoNumeroNF() {
		return proximoNumeroNF;
	}
	
	public void setProximoNumeroNF(Long proximoNumeroNF) {
		this.proximoNumeroNF = proximoNumeroNF;
	}

}
