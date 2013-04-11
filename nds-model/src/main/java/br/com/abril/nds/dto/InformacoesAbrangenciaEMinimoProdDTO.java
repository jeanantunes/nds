package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/*
 * Classe utilizada no detalhamento do produto. 
 * 
 * Funcionalidade: % Abrangência e Mínimo do Produto.
 * 
 */

public class InformacoesAbrangenciaEMinimoProdDTO implements Serializable {

	private static final long serialVersionUID = 2847196441994562180L;
	
	private BigDecimal abrangenciaSugerida;
	private BigInteger minimoSugerido;
	private Long minimoEstudoId;
	private BigDecimal abrangenciaApurada;

	
	public BigDecimal getAbrangenciaApurada() {
		return abrangenciaApurada;
	}
	public void setAbrangenciaApurada(BigDecimal abrangenciaApurada) {
			this.abrangenciaApurada = abrangenciaApurada;
	}
	public BigDecimal getAbrangenciaSugerida() {
		return abrangenciaSugerida;
	}
	public void setAbrangenciaSugerida(BigDecimal abrangenciaSugerida) {
			this.abrangenciaSugerida = abrangenciaSugerida;
	}
	public Long getMinimoEstudoId() {
		return minimoEstudoId;
	}
	public void setMinimoEstudoId(Long minimoEstudoId) {
		this.minimoEstudoId = minimoEstudoId;
	}
	public BigInteger getMinimoSugerido() {
		return minimoSugerido;
	}
	public void setMinimoSugerido(BigInteger minimoSugerido) {
			this.minimoSugerido = minimoSugerido;
	}
}
