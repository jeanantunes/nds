package br.com.abril.nds.model.cadastro.garantia.pagamento;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value="Desconto Cota")
public class PagamentoDescontoCota extends PagamentoCaucaoLiquida {

	private static final long serialVersionUID = 7009749644620020575L;
	
	@Column(name="DESCONTO_ATUAL", nullable=true)
	private BigDecimal descontoAtual;
	
	@Column(name="DESCONTO_COTA",nullable=true)
	private BigDecimal descontoCota;
	
	@Column(name="VALOR_UTILIZADO",nullable=true)
	private BigDecimal valorUtilizado;

	/**
	 * @return the descontoAtual
	 */
	public BigDecimal getDescontoAtual() {
		return descontoAtual;
	}

	/**
	 * @param descontoAtual the descontoAtual to set
	 */
	public void setDescontoAtual(BigDecimal descontoAtual) {
		this.descontoAtual = descontoAtual;
	}

	/**
	 * @return the descontoCota
	 */
	public BigDecimal getDescontoCota() {
		return descontoCota;
	}

	/**
	 * @param descontoCota the descontoCota to set
	 */
	public void setDescontoCota(BigDecimal descontoCota) {
		this.descontoCota = descontoCota;
	}

	/**
	 * @return the valorUtilizado
	 */
	public BigDecimal getValorUtilizado() {
		return valorUtilizado;
	}

	/**
	 * @param valorUtilizado the valorUtilizado to set
	 */
	public void setValorUtilizado(BigDecimal valorUtilizado) {
		this.valorUtilizado = valorUtilizado;
	}
	
	
}
