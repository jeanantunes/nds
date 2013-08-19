package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;

public class DebitoCreditoDTO implements Serializable {

	private static final long serialVersionUID = 1687109964774922384L;
	
	private Long id;

	private String dataLancamento;
	
	private String dataVencimento;
	
	private Integer numeroCota;
	
	private String nomeCota;
	
	private TipoMovimentoFinanceiro tipoMovimentoFinanceiro;
	
	private Long idUsuario;
	
	private String valor;
	
	private String observacao;

	private boolean permiteAlteracao;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the dataLancamento
	 */
	public String getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	/**
	 * @return the dataVencimento
	 */
	public String getDataVencimento() {
		return dataVencimento;
	}

	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * @return the tipoMovimentoFinanceiro
	 */
	public TipoMovimentoFinanceiro getTipoMovimentoFinanceiro() {
		return tipoMovimentoFinanceiro;
	}

	/**
	 * @param tipoMovimentoFinanceiro the tipoMovimentoFinanceiro to set
	 */
	public void setTipoMovimentoFinanceiro(
			TipoMovimentoFinanceiro tipoMovimentoFinanceiro) {
		this.tipoMovimentoFinanceiro = tipoMovimentoFinanceiro;
	}
	
	/**
	 * @return the idUsuario
	 */
	public Long getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the observacao
	 */
	public String getObservacao() {
		return observacao;
	}

	/**
	 * @param observacao the observacao to set
	 */
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public boolean isPermiteAlteracao() {
		return permiteAlteracao;
	}

	public void setPermiteAlteracao(boolean permiteAlteracao) {
		this.permiteAlteracao = permiteAlteracao;
	}

	public DebitoCreditoDTO(){
		
	}
			
	public DebitoCreditoDTO(Long id, String dataLancamento,
			String dataVencimento, Integer numeroCota, String nomeCota,
			TipoMovimentoFinanceiro tipoMovimentoFinanceiro, Long idUsuario,
			String valor, String observacao) {
		super();
		this.id = id;
		this.dataLancamento = dataLancamento;
		this.dataVencimento = dataVencimento;
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.tipoMovimentoFinanceiro = tipoMovimentoFinanceiro;
		this.idUsuario = idUsuario;
		this.valor = valor;
		this.observacao = observacao;
	}
	
	
}
