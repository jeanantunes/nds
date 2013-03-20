package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

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

import br.com.abril.nds.model.ClassificacaoCotaDistribuidorEnum;

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
	private ClassificacaoCotaDistribuidorEnum codigoClassificacaoCota;

	@Column(name = "VALOR_DE")
	private BigDecimal valorDe;

	@Column(name = "VALOR_ATE")
	private BigDecimal valorAte;

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

	public ClassificacaoCotaDistribuidorEnum getCodigoClassificacaoCota() {
		return codigoClassificacaoCota;
	}

	public void setCodigoClassificacaoCota(
			ClassificacaoCotaDistribuidorEnum codigoClassificacaoCota) {
		this.codigoClassificacaoCota = codigoClassificacaoCota;
	}

	public BigDecimal getValorDe() {
		return valorDe;
	}

	public void setValorDe(BigDecimal valorDe) {
		this.valorDe = valorDe;
	}

	public BigDecimal getValorAte() {
		return valorAte;
	}

	public void setValorAte(BigDecimal valorAte) {
		this.valorAte = valorAte;
	}

}
