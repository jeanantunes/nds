package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.ClassificacaoCotaDistribuidor;

@Entity
@Table(name = "DISTRIBUIDOR_CLASSIFICACAO_COTA")
@SequenceGenerator(name = "DISTRIBUIDOR_CLASSIFICACAO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class DistribuidorClassificacaoCota implements Serializable {

	private static final long serialVersionUID = -2931366569492761550L;

	@Id
	@GeneratedValue(generator = "DISTRIBUIDOR_CLASSIFICACAO_COTA_SEQ")
	@Column(name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;

	@Enumerated(EnumType.STRING)
	@Column(name = "COD_CLASSIFICACAO")
	private ClassificacaoCotaDistribuidor codigoClassificacaoCota;

	@Column(name = "VALOR_DE")
	private Integer valorDe;

	@Column(name = "VALOR_ATE")
	private Integer valorAte;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}

	public ClassificacaoCotaDistribuidor getCodigoClassificacaoCota() {
		return codigoClassificacaoCota;
	}

	public void setCodigoClassificacaoCota(
			ClassificacaoCotaDistribuidor codigoClassificacaoCota) {
		this.codigoClassificacaoCota = codigoClassificacaoCota;
	}

	public Integer getValorDe() {
		return valorDe;
	}

	public void setValorDe(Integer valorDe) {
		this.valorDe = valorDe;
	}

	public Integer getValorAte() {
		return valorAte;
	}

	public void setValorAte(Integer valorAte) {
		this.valorAte = valorAte;
	}

}
