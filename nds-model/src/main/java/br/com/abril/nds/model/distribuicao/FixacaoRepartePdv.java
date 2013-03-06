package br.com.abril.nds.model.distribuicao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.pdv.PDV;

@Entity
@Table(name = "FIXACAO_REPARTE_PDV")
@SequenceGenerator(name="FIXREPPDV_SEQ", initialValue = 1, allocationSize = 1)
public class FixacaoRepartePdv {
	
	@Id
	@GeneratedValue(generator = "FIXREPPDV_SEQ")
	@Column(name = "ID",nullable=false)
	private Long id;
	
	@Column(name="REPARTE")
	private Integer repartePdv;
	
	@ManyToOne
	@JoinColumn(name = "ID_FIXACAO_REPARTE",nullable=false)
	private  FixacaoReparte fixacaoReparte;
	
	@ManyToOne
	@JoinColumn(name = "ID_PDV",nullable=false)
	private PDV pdv;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRepartePdv() {
		return repartePdv;
	}

	public void setRepartePdv(Integer repartePdv) {
		this.repartePdv = repartePdv;
	}

	public FixacaoReparte getFixacaoReparte() {
		return fixacaoReparte;
	}

	public void setFixacaoReparte(FixacaoReparte fixacaoReparte) {
		this.fixacaoReparte = fixacaoReparte;
	}

	public PDV getPdv() {
		return pdv;
	}

	public void setPdv(PDV pdv) {
		this.pdv = pdv;
	}
	
	


	

}

