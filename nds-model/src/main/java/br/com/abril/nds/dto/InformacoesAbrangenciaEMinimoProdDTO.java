package br.com.abril.nds.dto;

import java.io.Serializable;

/*
 * Classe utilizada no detalhamento do produto. 
 * 
 * Funcionalidade: % Abrangência e Mínimo do Produto.
 * 
 */

public class InformacoesAbrangenciaEMinimoProdDTO implements Serializable {

	private static final long serialVersionUID = 2847196441994562180L;
	
	private Long abrangenciaSugerida;
	private Long abrangenciaApurada;
	private Long minimoSugerido;
	private Long minimoEstudoId;

	
	public Long getAbrangenciaSugerida() {
		return abrangenciaSugerida;
	}
	public void setAbrangenciaSugerida(Long abrangenciaSugerida) {
		this.abrangenciaSugerida = abrangenciaSugerida;
	}
	public Long getAbrangenciaApurada() {
		return abrangenciaApurada;
	}
	public void setAbrangenciaApurada(Long abrangenciaApurada) {
		this.abrangenciaApurada = abrangenciaApurada;
	}
	public Long getMinimoSugerido() {
		return minimoSugerido;
	}
	public void setMinimoSugerido(Long minimoSugerido) {
		this.minimoSugerido = minimoSugerido;
	}
	public Long getMinimoEstudoId() {
		return minimoEstudoId;
	}
	public void setMinimoEstudoId(Long minimoEstudoId) {
		this.minimoEstudoId = minimoEstudoId;
	}
}
