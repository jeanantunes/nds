package br.com.abril.nds.model.distribuicao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "RANKING_SEGMENTO_GERADOS")
@SequenceGenerator(name = "RANKING_SEGMENTO_GERADOS_SEQ", initialValue = 1, allocationSize = 1)
public class RankingSegmentosGerados {

	@Id
	@GeneratedValue(generator = "RANKING_SEGMENTO_GERADOS_SEQ")
	private Long id;
	
	@Column(name = "DATA_GERACAO_RANK")
	private Date dataGeracaoRanking;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataGeracaoRanking() {
		return dataGeracaoRanking;
	}

	public void setDataGeracaoRanking(Date dataGeracaoRanking) {
		this.dataGeracaoRanking = dataGeracaoRanking;
	}
}
