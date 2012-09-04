package br.com.abril.nds.model.cadastro.garantia.pagamento;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import br.com.abril.nds.model.cadastro.FormaCobrancaCaucaoLiquida;


@Entity
@DiscriminatorValue(value="Boleto")
public class PagamentoBoleto extends PagamentoCaucaoLiquida {

	private static final long serialVersionUID = 2519203333658295852L;
	
	@Column(name="QNT_PARCELAS")
	private Integer quantidadeParcelas;

	@Column(name="VALOR_PARCELA")
	private BigDecimal valorParcela;

	@OneToOne(cascade={CascadeType.ALL},orphanRemoval=true)
	@JoinColumn(name="FORMA_COBRANCA_CAUCAO_LIQUIDA_ID")
	private FormaCobrancaCaucaoLiquida formaCobrancaCaucaoLiquida;
	

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
	 * @return the formaCobrancaCaucaoLiquida
	 */
	public FormaCobrancaCaucaoLiquida getFormaCobrancaCaucaoLiquida() {
		return formaCobrancaCaucaoLiquida;
	}

	/**
	 * @param formaCobrancaCaucaoLiquida the formaCobrancaCaucaoLiquida to set
	 */
	public void setFormaCobrancaCaucaoLiquida(
			FormaCobrancaCaucaoLiquida formaCobrancaCaucaoLiquida) {
		this.formaCobrancaCaucaoLiquida = formaCobrancaCaucaoLiquida;
	}

}
