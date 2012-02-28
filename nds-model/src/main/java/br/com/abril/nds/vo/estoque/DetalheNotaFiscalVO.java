package br.com.abril.nds.vo.estoque;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.model.estoque.TipoDiferenca;

public class DetalheNotaFiscalVO implements Serializable {

	private static final long serialVersionUID = -3303109650463467609L;

	private Long codigoItem;
	
	private String codigoProduto;

	private String nomeProduto;
	
	private Long numeroEdicao;
	
	private BigDecimal quantidadeExemplares;
	
	private BigDecimal sobrasFaltas;

	private TipoDiferenca tipoDiferenca;
	
	public DetalheNotaFiscalVO() { }

	/**
	 * @return the codigoItem
	 */
	public Long getCodigoItem() {
		return codigoItem;
	}

	/**
	 * @param codigoItem the codigoItem to set
	 */
	public void setCodigoItem(Long codigoItem) {
		this.codigoItem = codigoItem;
	}

	/**
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	/**
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the quantidadeExemplares
	 */
	public BigDecimal getQuantidadeExemplares() {
		return quantidadeExemplares;
	}

	/**
	 * @param quantidadeExemplares the quantidadeExemplares to set
	 */
	public void setQuantidadeExemplares(BigDecimal quantidadeExemplares) {
		this.quantidadeExemplares = quantidadeExemplares;
	}

	/**
	 * @return the sobrasFaltas
	 */
	public BigDecimal getSobrasFaltas() {
		return sobrasFaltas;
	}

	/**
	 * @param sobrasFaltas the sobrasFaltas to set
	 */
	public void setSobrasFaltas(BigDecimal sobrasFaltas) {
		this.sobrasFaltas = sobrasFaltas;
	}

	/**
	 * @return the tipoDiferenca
	 */
	public TipoDiferenca getTipoDiferenca() {
		return tipoDiferenca;
	}

	/**
	 * @param tipoDiferenca the tipoDiferenca to set
	 */
	public void setTipoDiferenca(TipoDiferenca tipoDiferenca) {
		this.tipoDiferenca = tipoDiferenca;
	}

	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
}
