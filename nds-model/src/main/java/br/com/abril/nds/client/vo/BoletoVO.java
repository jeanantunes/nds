package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * Value Object que representa dados do boleto.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class BoletoVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5988272381369010513L;
	
	@Export(label = "Nosso Número")
	private String nossoNumero;
	
	@Export(label = "Data Emissão", alignment = Alignment.CENTER)
	private Date dataEmissao;
	
	@Export(label = "Data Vencimento", alignment = Alignment.CENTER)
	private Date dataVencimento;
	
	@Export(label = "Data Pagamento", alignment = Alignment.CENTER)
	private Date dataPagamento;
	
	@Export(label = "Encargos", alignment = Alignment.RIGHT)
	private BigDecimal encargos;
	
	@Export(label = "Valor R$", alignment = Alignment.RIGHT)
	private BigDecimal valor;
	
	@Export(label = "Tipo Baixa")
	private String tipoBaixa;
	
	@Export(label = "Status")
	private StatusCobranca status;
	
	/**
	 * Construtor padrão.
	 */
	public BoletoVO() {
		
	}

	/**
	 * @return the nossoNumero
	 */
	public String getNossoNumero() {
		return nossoNumero;
	}

	/**
	 * @param nossoNumero the nossoNumero to set
	 */
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	/**
	 * @return the dataEmissao
	 */
	public Date getDataEmissao() {
		return dataEmissao;
	}

	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	/**
	 * @return the dataVencimento
	 */
	public Date getDataVencimento() {
		return dataVencimento;
	}

	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	/**
	 * @return the dataPagamento
	 */
	public Date getDataPagamento() {
		return dataPagamento;
	}

	/**
	 * @param dataPagamento the dataPagamento to set
	 */
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @return the encargos
	 */
	public BigDecimal getEncargos() {
		return encargos;
	}

	/**
	 * @param encargos the encargos to set
	 */
	public void setEncargos(BigDecimal encargos) {
		this.encargos = encargos;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the tipoBaixa
	 */
	public String getTipoBaixa() {
		return tipoBaixa;
	}

	/**
	 * @param tipoBaixa the tipoBaixa to set
	 */
	public void setTipoBaixa(String tipoBaixa) {
		this.tipoBaixa = tipoBaixa;
	}

	/**
	 * @return the status
	 */
	public StatusCobranca getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusCobranca status) {
		this.status = status;
	}

}
