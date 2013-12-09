package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ExtratoEdicaoDTO implements Serializable {
	
	/**
	 * Version UID
	 */
	private static final long serialVersionUID = 1L;

	private Long idMovimento;
	
	private Long idTipoMovimento;
	
	private Long idLancamentoDiferenca;
	
	@Export(label = "Data", alignment = Alignment.CENTER)
	private Date dataMovimento;
	
	@Export(label = "Movimento")
	private String descMovimento;
	
	@Export(label = "Entrada", alignment = Alignment.CENTER)
	private BigInteger qtdEdicaoEntrada = BigInteger.ZERO;
	
	@Export(label = "Saída", alignment = Alignment.CENTER)
	private BigInteger qtdEdicaoSaida = BigInteger.ZERO;
	
	@Export(label = "Parcial", alignment = Alignment.CENTER)
	private BigInteger qtdParcial = BigInteger.ZERO;
	
	public ExtratoEdicaoDTO(Long idMovimento, Long idTipoMovimento, Date dataMovimento,
			String descMovimento, Long idLancamentoDiferenca, 
			BigInteger qtdEdicaoEntrada, BigInteger qtdEdicaoSaida) {
		
		this.idMovimento = idMovimento;
		this.idTipoMovimento = idTipoMovimento;
		this.dataMovimento = dataMovimento;
		this.descMovimento = descMovimento;
		this.qtdEdicaoEntrada = qtdEdicaoEntrada;
		this.qtdEdicaoSaida = qtdEdicaoSaida;
		this.qtdParcial = BigInteger.ZERO;
		this.idLancamentoDiferenca = idLancamentoDiferenca;
	}

	public ExtratoEdicaoDTO(){
		
	}
	
	
	
	/**
	 * Obtém qtdEdicaoEntrada
	 *
	 * @return BigDecimal
	 */
	public BigInteger getQtdEdicaoEntrada() {
		return qtdEdicaoEntrada;
	}

	/**
	 * Atribuí qtdEdicaoEntrada
	 * @param qtdEdicaoEntrada 
	 */
	public void setQtdEdicaoEntrada(BigInteger qtdEdicaoEntrada) {
		this.qtdEdicaoEntrada = qtdEdicaoEntrada;
	}

	/**
	 * Obtém qtdEdicaoSaida
	 *
	 * @return BigDecimal
	 */
	public BigInteger getQtdEdicaoSaida() {
		return qtdEdicaoSaida;
	}

	/**
	 * Atribuí qtdEdicaoSaida
	 * @param qtdEdicaoSaida 
	 */
	public void setQtdEdicaoSaida(BigInteger qtdEdicaoSaida) {
		this.qtdEdicaoSaida = qtdEdicaoSaida;
	}

	/**
	 * Obtém qtdParcial
	 *
	 * @return BigDecimal
	 */
	public BigInteger getQtdParcial() {
		return qtdParcial;
	}

	/**
	 * Atribuí qtdParcial
	 * @param qtdParcial 
	 */
	public void setQtdParcial(BigInteger qtdParcial) {
		this.qtdParcial = qtdParcial;
	}

	/**
	 * Obtém dataMovimento
	 *
	 * @return Date
	 */
	public Date getDataMovimento() {
		return dataMovimento;
	}

	/**
	 * Atribuí dataMovimento
	 * @param dataMovimento 
	 */
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	/**
	 * Obtém descMovimento
	 *
	 * @return String
	 */
	public String getDescMovimento() {
		return descMovimento;
	}

	/**
	 * Atribuí descMovimento
	 * @param descMovimento 
	 */
	public void setDescMovimento(String descMovimento) {
		this.descMovimento = descMovimento;
	}
	

	/**
	 * Obtém idMovimento
	 *
	 * @return Long
	 */
	public Long getIdMovimento() {
		return idMovimento;
	}

	/**
	 * Atribuí idMovimento
	 * @param idMovimento 
	 */
	public void setIdMovimento(Long idMovimento) {
		this.idMovimento = idMovimento;
	}

	public Long getIdTipoMovimento() {
		return idTipoMovimento;
	}

	public void setIdTipoMovimento(Long idTipoMovimento) {
		this.idTipoMovimento = idTipoMovimento;
	}

	public Long getIdLancamentoDiferenca() {
		return idLancamentoDiferenca;
	}

	public void setIdLancamentoDiferenca(Long idLancamentoDiferenca) {
		this.idLancamentoDiferenca = idLancamentoDiferenca;
	}
	
}
