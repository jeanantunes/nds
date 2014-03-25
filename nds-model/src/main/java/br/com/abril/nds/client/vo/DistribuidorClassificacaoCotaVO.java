package br.com.abril.nds.client.vo;

import java.math.BigDecimal;

import br.com.abril.nds.model.ClassificacaoCotaDistribuidorEnum;

public class DistribuidorClassificacaoCotaVO {

	private Long id;
	private ClassificacaoCotaDistribuidorEnum codigoClassificacaoCota;
	private BigDecimal valorDe;
	private BigDecimal valorAte;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
