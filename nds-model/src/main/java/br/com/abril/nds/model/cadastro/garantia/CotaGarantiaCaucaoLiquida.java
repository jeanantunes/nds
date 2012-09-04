/**
 * 
 */
package br.com.abril.nds.model.cadastro.garantia;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.ContaDepositoCaucaoLiquida;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoCaucaoLiquida;

/**
 * @author Diego Fernandes
 *
 */
@Entity
public class CotaGarantiaCaucaoLiquida extends CotaGarantia {

	private static final long serialVersionUID = 2374130596840933128L;
	
	@OneToMany(cascade={CascadeType.ALL},orphanRemoval=true)
	@OrderBy("atualizacao DESC")
	@JoinColumn(name="COTA_GARANTIA_CAUCAO_LIQUIDA_ID")
	private List<CaucaoLiquida> caucaoLiquidas;

	@Embedded
	private ContaDepositoCaucaoLiquida contaDepositoCaucaoLiquida;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_COBRANCA", nullable = false)
	private TipoCobranca tipoCobranca;	
	
	@OneToOne(cascade={CascadeType.ALL},orphanRemoval=true)
	@JoinColumn(name="PAGAMENTO_CAUCAO_LIQUIDA_ID")
	private PagamentoCaucaoLiquida formaPagamento;
	
	/**
	 * @return the caucaoLiquidas
	 */
	public List<CaucaoLiquida> getCaucaoLiquidas() {
		return caucaoLiquidas;
	}


	/**
	 * @param caucaoLiquidas the caucaoLiquidas to set
	 */
	public void setCaucaoLiquidas(List<CaucaoLiquida> caucaoLiquidas) {
		this.caucaoLiquidas = caucaoLiquidas;
	}


	/**
	 * @return the contaDepositoCaucaoLiquida
	 */
	public ContaDepositoCaucaoLiquida getContaDepositoCaucaoLiquida() {
		return contaDepositoCaucaoLiquida;
	}


	/**
	 * @param contaDepositoCaucaoLiquida the contaDepositoCaucaoLiquida to set
	 */
	public void setContaDepositoCaucaoLiquida(
			ContaDepositoCaucaoLiquida contaDepositoCaucaoLiquida) {
		this.contaDepositoCaucaoLiquida = contaDepositoCaucaoLiquida;
	}


	/**
	 * @return the formaPagamento
	 */
	public PagamentoCaucaoLiquida getFormaPagamento() {
		return formaPagamento;
	}


	/**
	 * @param formaPagamento the formaPagamento to set
	 */
	public void setFormaPagamento(PagamentoCaucaoLiquida formaPagamento) {
		this.formaPagamento = formaPagamento;
	}


	/**
	 * @return the tipoCobranca
	 */
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}


	/**
	 * @param tipoCobranca the tipoCobranca to set
	 */
	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

}
