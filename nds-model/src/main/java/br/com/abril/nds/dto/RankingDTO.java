package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RankingDTO implements Comparable<RankingDTO> {
	
	private Long chave;
	
	//atributo relativo a posição no ranking 
	private Long ranking;
	
	private BigDecimal valor;
	
	private BigDecimal valorAcumulado;

	private BigInteger reparte;
	
	private BigDecimal faturamentoCapa;
	
	private BigInteger vendaExemplares;
	
	private BigDecimal porcentagemVendaExemplares;
	
	private BigDecimal valorMargemDistribuidor;
			
	private BigDecimal porcentagemMargemDistribuidor; 	
	
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

	public Long getChave() {
		return chave;
	}

	public void setChave(Long chave) {
		this.chave = chave;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public BigDecimal getFaturamentoCapa() {
		return faturamentoCapa;
	}

	public void setFaturamentoCapa(BigDecimal faturamentoCapa) {
		this.faturamentoCapa = faturamentoCapa;
	}

	public BigInteger getVendaExemplares() {
		return vendaExemplares;
	}

	public void setVendaExemplares(BigInteger vendaExemplares) {
		this.vendaExemplares = vendaExemplares;
	}

	public BigDecimal getPorcentagemVendaExemplares() {
		return porcentagemVendaExemplares;
	}

	public void setPorcentagemVendaExemplares(BigDecimal porcentagemVendaExemplares) {
		this.porcentagemVendaExemplares = porcentagemVendaExemplares;
	}

	public BigDecimal getValorMargemDistribuidor() {
		return valorMargemDistribuidor;
	}

	public void setValorMargemDistribuidor(BigDecimal valorMargemDistribuidor) {
		this.valorMargemDistribuidor = valorMargemDistribuidor;
	}

	public BigDecimal getPorcentagemMargemDistribuidor() {
		return porcentagemMargemDistribuidor;
	}

	public void setPorcentagemMargemDistribuidor(
			BigDecimal porcentagemMargemDistribuidor) {
		this.porcentagemMargemDistribuidor = porcentagemMargemDistribuidor;
	}
	
	
}
