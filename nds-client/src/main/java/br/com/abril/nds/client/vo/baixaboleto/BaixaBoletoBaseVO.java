package br.com.abril.nds.client.vo.baixaboleto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * VO que representa informações sobre Baixas de Boletos.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class BaixaBoletoBaseVO implements Serializable {

	private static final long serialVersionUID = 2743160908272450578L;
	
	@Export(label = "Banco", exhibitionOrder=0)
	private String nomeBanco;
	
	@Export(label = "Conta-corrente", exhibitionOrder=0)
	private String numeroConta;
	
	@Export(label = "Valor R$", exhibitionOrder=0)
	private BigDecimal valorBoleto;

	private TipoBaixaBoleto tipoBaixaBoleto;

	/**
	 * Enum que define o tipo de baixa que será usado na exportação
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum TipoBaixaBoleto {

		PREVISTOS,
		BAIXADOS,
		REJEITADOS,
		DIVERGENTES,
		INADIMPLENTES,
		TOTAL_BANCARIO;
	}
	
	/**
	 * @return the nomeBanco
	 */
	public String getNomeBanco() {
		return nomeBanco;
	}

	/**
	 * @param nomeBanco the nomeBanco to set
	 */
	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	/**
	 * @return the numeroConta
	 */
	public String getNumeroConta() {
		return numeroConta;
	}

	/**
	 * @param numeroConta the numeroConta to set
	 */
	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	/**
	 * @return the valorBoleto
	 */
	public BigDecimal getValorBoleto() {
		return valorBoleto;
	}

	/**
	 * @param valorBoleto the valorBoleto to set
	 */
	public void setValorBoleto(BigDecimal valorBoleto) {
		this.valorBoleto = valorBoleto;
	}

	/**
	 * @return the tipoBaixaBoleto
	 */
	public TipoBaixaBoleto getTipoBaixaBoleto() {
		return tipoBaixaBoleto;
	}

	/**
	 * @param tipoBaixaBoleto the tipoBaixaBoleto to set
	 */
	public void setTipoBaixaBoleto(TipoBaixaBoleto tipoBaixaBoleto) {
		this.tipoBaixaBoleto = tipoBaixaBoleto;
	}
}
