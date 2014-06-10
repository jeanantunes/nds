package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;

/**
 * @author Discover Technology
 *
 */
public class CotaOperacaoDiferenciadaDTO {

	private Long idLancamento;
	
	private Long idCota;
	
	private BigDecimal expectativaEncalhe;
	
	private List<Date> datasRecolhimento;
	
	private Long peso;
	
	private BigDecimal valorTotal;
	
	private Long idProdutoEdicao;
	
	private TipoLancamentoParcial parcial;

	/**
	 * @return the idLancamento
	 */
	public Long getIdLancamento() {
		return idLancamento;
	}

	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}

	/**
	 * @return the idCota
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * @param idCota the idCota to set
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	/**
	 * @return the expectativaEncalhe
	 */
	public BigDecimal getExpectativaEncalhe() {
		return expectativaEncalhe;
	}

	/**
	 * @param expectativaEncalhe the expectativaEncalhe to set
	 */
	public void setExpectativaEncalhe(BigDecimal expectativaEncalhe) {
		this.expectativaEncalhe = expectativaEncalhe;
	}

	/**
	 * @return the datasRecolhimento
	 */
	public List<Date> getDatasRecolhimento() {
		return datasRecolhimento;
	}

	/**
	 * @param datasRecolhimento the datasRecolhimento to set
	 */
	public void setDatasRecolhimento(List<Date> datasRecolhimento) {
		this.datasRecolhimento = datasRecolhimento;
	}

	/**
	 * @return the peso
	 */
	public Long getPeso() {
		return peso;
	}

	/**
	 * @param peso the peso to set
	 */
	public void setPeso(Long peso) {
		this.peso = peso;
	}

	/**
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @return the valorTotal
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/**
	 * @return the parcial
	 */
	public TipoLancamentoParcial getParcial() {
		return parcial;
	}

	/**
	 * @param parcial the parcial to set
	 */
	public void setParcial(TipoLancamentoParcial parcial) {
		this.parcial = parcial;
	}

}
