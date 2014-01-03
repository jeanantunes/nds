package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.Comparator;

public class RankingDTO implements Comparable<RankingDTO> {
	
	//atributo relativo a posição no ranking 
	private Long ranking;
	
	
	private BigDecimal valor;
	
	private BigDecimal valorAcumulado;

	public RankingDTO(){}
	
	
	
	@Override
	public int compareTo(RankingDTO o) {
		
		return this.getRanking().compareTo(o.getRanking());
		
	}



	public RankingDTO(
			Long ranking, 
			BigDecimal valor,
			BigDecimal valorAcumulado
			) {
		super();
		this.ranking = ranking;
		this.valor = valor;
		this.valorAcumulado = valorAcumulado;
	}

	public Long getRanking() {
		return ranking;
	}

	public void setRanking(Long ranking) {
		this.ranking = ranking;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValorAcumulado() {
		return valorAcumulado;
	}

	public void setValorAcumulado(BigDecimal valorAcumulado) {
		this.valorAcumulado = valorAcumulado;
	}
	
	
	
}
