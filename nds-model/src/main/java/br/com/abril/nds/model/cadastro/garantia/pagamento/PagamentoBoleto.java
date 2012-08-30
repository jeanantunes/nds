package br.com.abril.nds.model.cadastro.garantia.pagamento;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Entity
@DiscriminatorValue(value="Boleto")
public class PagamentoBoleto extends PagamentoCaucaoLiquida {
	
	private static final long serialVersionUID = 2519203333658295852L;

	@Column(name="QNT_PARCELAS")
	private Integer quantidadeParcelas;
	
	@Column(name="VALOR_PARECELA")
	private BigDecimal valorParcela;
	
	@Column(name="PERIODO_COBRANCA",nullable=true)
	@Enumerated(EnumType.STRING)
	private PeriodoCobranca periodoCobranca;

	/**
	 * @return the quantidadeParcelas
	 */
	public Integer getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	/**
	 * @param quantidadeParcelas the quantidadeParcelas to set
	 */
	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	/**
	 * @return the valorParcela
	 */
	public BigDecimal getValorParcela() {
		return valorParcela;
	}

	/**
	 * @param valorParcela the valorParcela to set
	 */
	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	/**
	 * @return the periodoCobranca
	 */
	public PeriodoCobranca getPeriodoCobranca() {
		return periodoCobranca;
	}

	/**
	 * @param periodoCobranca the periodoCobranca to set
	 */
	public void setPeriodoCobranca(PeriodoCobranca periodoCobranca) {
		this.periodoCobranca = periodoCobranca;
	}
	
	
}
