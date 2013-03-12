package br.com.abril.nds.model.distribuicao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "RANKING_FATURAMENTO_GERADOS")
@SequenceGenerator(name = "RANKING_FATURAMENTO_GERADO_SEQ", initialValue = 1, allocationSize = 1)
public class RankingFaturamentoGerado {

	@Id
	@GeneratedValue(generator = "RANKING_FATURAMENTO_GERADO_SEQ")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_GERACAO_RANK")
	private Date dataGeracao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataGeracao() {
		return dataGeracao;
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}
	
}
