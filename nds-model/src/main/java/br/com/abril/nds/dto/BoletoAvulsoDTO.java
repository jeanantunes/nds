package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.common.base.Strings;

import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.upload.XlsMapper;

public class BoletoAvulsoDTO implements Serializable {

	private static final long serialVersionUID = 1687109964774922384L;
	
	private Long id;
	
	private String dataLancamento;
	
	@XlsMapper(value="dataVencimento")
	private String dataVencimento;
	
	@XlsMapper(value="numeroCota")
	private Integer numeroCota;
	
	private String nomeCota;
	
	private TipoMovimentoFinanceiro tipoMovimentoFinanceiro;
	
	private Long idUsuario;
	
	@XlsMapper(value="valor")
	private String valor;
	
	@XlsMapper(value="observ")
	private String observacao;

	private boolean permiteAlteracao;
	
	private Date dataCriacao;
	
	private Long idBanco;
	
	private List<ItemDTO<Integer, String>> bancos;
	
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
		
		if(Strings.isNullOrEmpty(valor))
            return;        
        
		BigDecimal valorRetornado = (BigDecimal.valueOf(Double.parseDouble(valor.replace(",", "."))));
		
		this.valor = CurrencyUtil.formatarValor(valorRetornado);
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

	public BoletoAvulsoDTO(){
		
	}
	
	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}
	
	public List<ItemDTO<Integer, String>> getBancos() {
		return bancos;
	}

	public void setBancos(List<ItemDTO<Integer, String>> bancos) {
		this.bancos = bancos;
	}
	
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	public BoletoAvulsoDTO(Long id, String dataLancamento,
			String dataVencimento, Integer numeroCota, String nomeCota,
			TipoMovimentoFinanceiro tipoMovimentoFinanceiro, Long idUsuario,
			String valor, String observacao, List<ItemDTO<Integer, String>> bancos) {
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
		this.bancos =bancos;
	}	
}
