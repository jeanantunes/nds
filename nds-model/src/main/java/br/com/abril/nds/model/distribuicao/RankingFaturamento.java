package br.com.abril.nds.model.distribuicao;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;

@Entity
@Table(name = "RANKING_FATURAMENTO")
@SequenceGenerator(name = "RANKING_FATURAMENTO_SEQ", initialValue = 1, allocationSize = 1)
public class RankingFaturamento {

	@Id
	@GeneratedValue(generator = "RANKING_FATURAMENTO_SEQ")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Column
	private BigDecimal faturamento;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "RANKING_FATURAMENTO_GERADOS_ID")
	private RankingFaturamentoGerado rankingFaturamentoGerado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public BigDecimal getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(BigDecimal faturamento) {
		this.faturamento = faturamento;
	}

	public RankingFaturamentoGerado getRankingFaturamentoGerado() {
		return rankingFaturamentoGerado;
	}

	public void setRankingFaturamentoGerado(
			RankingFaturamentoGerado rankingFaturamentoGerado) {
		this.rankingFaturamentoGerado = rankingFaturamentoGerado;
	}


}