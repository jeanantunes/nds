package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;



public class SlipDTO {

	private Integer numeroCota;
	
	private String nomeCota;
	
	private BigDecimal valorTotalPagar;
	
	private BigDecimal valorTotalEncalhe;
	
	private BigInteger totalProdutos;
	
	private BigDecimal valorSlip;
	
	private BigDecimal valorDevido;
	
	private BigDecimal valorEncalheDia;
	
	private BigInteger totalProdutoDia;
	
	private Long ceJornaleiro;
	
	private Date dataConferencia;
	
	private String codigoBox;
	
	private Long numSlip;
	
	private String nomeUsuario;

	private List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlipDTO;
	
	
	
	/**
	 * Obtém numeroCota
	 *
	 * @return Integer
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * Atribuí numeroCota
	 * @param numeroCota 
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * Obtém nomeCota
	 *
	 * @return String
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * Atribuí nomeCota
	 * @param nomeCota 
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * Obtém valorTotalPagar
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorTotalPagar() {
		return valorTotalPagar;
	}

	/**
	 * Atribuí valorTotalPagar
	 * @param valorTotalPagar 
	 */
	public void setValorTotalPagar(BigDecimal valorTotalPagar) {
		this.valorTotalPagar = valorTotalPagar;
	}

	/**
	 * Obtém valorTotalEncalhe
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorTotalEncalhe() {
		return valorTotalEncalhe;
	}

	/**
	 * Atribuí valorTotalEncalhe
	 * @param valorTotalEncalhe 
	 */
	public void setValorTotalEncalhe(BigDecimal valorTotalEncalhe) {
		this.valorTotalEncalhe = valorTotalEncalhe;
	}

	/**
	 * Obtém totalProdutos
	 *
	 * @return BigInteger
	 */
	public BigInteger getTotalProdutos() {
		return totalProdutos;
	}

	/**
	 * Atribuí totalProdutos
	 * @param totalProdutos 
	 */
	public void setTotalProdutos(BigInteger totalProdutos) {
		this.totalProdutos = totalProdutos;
	}

	/**
	 * Obtém valorSlip
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorSlip() {
		return valorSlip;
	}

	/**
	 * Atribuí valorSlip
	 * @param valorSlip 
	 */
	public void setValorSlip(BigDecimal valorSlip) {
		this.valorSlip = valorSlip;
	}

	/**
	 * Obtém valorDevido
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorDevido() {
		return valorDevido;
	}

	/**
	 * Atribuí valorDevido
	 * @param valorDevido 
	 */
	public void setValorDevido(BigDecimal valorDevido) {
		this.valorDevido = valorDevido;
	}

	/**
	 * Obtém valorEncalheDia
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorEncalheDia() {
		return valorEncalheDia;
	}

	/**
	 * Atribuí valorEncalheDia
	 * @param valorEncalheDia 
	 */
	public void setValorEncalheDia(BigDecimal valorEncalheDia) {
		this.valorEncalheDia = valorEncalheDia;
	}

	/**
	 * Obtém totalProdutoDia
	 *
	 * @return BigInteger
	 */
	public BigInteger getTotalProdutoDia() {
		return totalProdutoDia;
	}

	/**
	 * Atribuí totalProdutoDia
	 * @param totalProdutoDia 
	 */
	public void setTotalProdutoDia(BigInteger totalProdutoDia) {
		this.totalProdutoDia = totalProdutoDia;
	}

	/**
	 * Obtém ceJornaleiro
	 *
	 * @return Long
	 */
	public Long getCeJornaleiro() {
		return ceJornaleiro;
	}

	/**
	 * Atribuí ceJornaleiro
	 * @param ceJornaleiro 
	 */
	public void setCeJornaleiro(Long ceJornaleiro) {
		this.ceJornaleiro = ceJornaleiro;
	}

	/**
	 * Obtém dataConferencia
	 *
	 * @return Date
	 */
	public Date getDataConferencia() {
		return dataConferencia;
	}

	/**
	 * Atribuí dataConferencia
	 * @param dataConferencia 
	 */
	public void setDataConferencia(Date dataConferencia) {
		this.dataConferencia = dataConferencia;
	}

	/**
	 * Obtém codigoBox
	 *
	 * @return String
	 */
	public String getCodigoBox() {
		return codigoBox;
	}

	/**
	 * Atribuí codigoBox
	 * @param codigoBox 
	 */
	public void setCodigoBox(String codigoBox) {
		this.codigoBox = codigoBox;
	}

	/**
	 * Obtém numSlip
	 *
	 * @return Long
	 */
	public Long getNumSlip() {
		return numSlip;
	}

	/**
	 * Atribuí numSlip
	 * @param numSlip 
	 */
	public void setNumSlip(Long numSlip) {
		this.numSlip = numSlip;
	}
	
	
	/**
	 * Obtém nomeUsuario
	 *
	 * @return String
	 */
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	
	/**
	 * Atribuí nomeUsuario
	 * @param nomeUsuario
	 */
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	
	/**
	 * Obtém listaProdutoEdicaoSlipDTO
	 *
	 * @return List<ProdutoEdicaoSlipDTO>
	 */
	public List<ProdutoEdicaoSlipDTO> getListaProdutoEdicaoSlipDTO() {
		return listaProdutoEdicaoSlipDTO;
	}

	/**
	 * Atribuí listaProdutoEdicaoSlipDTO
	 * @param listaProdutoEdicaoSlipDTO 
	 */
	public void setListaProdutoEdicaoSlipDTO(
			List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlipDTO) {
		this.listaProdutoEdicaoSlipDTO = listaProdutoEdicaoSlipDTO;
	}
	
}
