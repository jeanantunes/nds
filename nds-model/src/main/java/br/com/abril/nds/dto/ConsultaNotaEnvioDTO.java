package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaNotaEnvioDTO implements Serializable {

	private static final long serialVersionUID = -4249511518029888493L;

	private BigInteger box;
	
	private Long idCota;
	@Export(label="Cota", alignment=Alignment.LEFT)
	private Integer numeroCota;
	@Export(label="Nome", alignment=Alignment.LEFT)
	private String nomeCota;
	@Export(label="Total Exemplares", alignment=Alignment.CENTER)
	private Long exemplares;
	
	@Export(label="Total R$", alignment=Alignment.RIGHT)
	private String total;
	
	private BigDecimal totalDesconto;
	
	private boolean notaImpressa;
	
	private boolean cotaSuspensa;
	
	private StatusLancamento status;
	
	private SituacaoCadastro situacaoCadastro;
	
	private Integer ordemRoteiro;
	
	private Integer ordemRota;
	
	private Integer ordemRotaPdv;

	public BigInteger getBox() {
		return box;
	}

	public void setBox(BigInteger box) {
		this.box = box;
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
	public void setIdCota(BigInteger idCota) {
		this.idCota = idCota.longValue();
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
	 * @return the exemplares
	 */
	public Long getExemplares() {
		return exemplares;
	}

	/**
	 * @param exemplares the exemplares to set
	 */
	public void setExemplares(BigDecimal exemplares) {
		this.exemplares = exemplares == null ? 0L : exemplares.longValue();
	}

	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total =  CurrencyUtil.formatarValor(total);
	}

	/**
	 * @return the totalDesconto
	 */
	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}

	/**
	 * @param totalDesconto the totalDesconto to set
	 */
	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	/**
	 * @return the notaImpressa
	 */
	public boolean isNotaImpressa() {
		return notaImpressa;
	}

	/**
	 * @param notaImpressa the notaImpressa to set
	 */
	public void setNotaImpressa(BigInteger notaImpressa) {
		this.notaImpressa = (notaImpressa.equals(BigInteger.ONE));
	}

	public boolean isCotaSuspensa() {
		return cotaSuspensa;
	}

	public void setCotaSuspensa(boolean cotaSuspensa) {
		this.cotaSuspensa = cotaSuspensa;
	}

	public SituacaoCadastro getSituacaoCadastro() {
		return situacaoCadastro;
	}

	public void setSituacaoCadastro(String situacaoCadastro) {
		this.situacaoCadastro = SituacaoCadastro.valueOf(situacaoCadastro);
	}

	public StatusLancamento getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if(status==null)
			return;
		this.status = StatusLancamento.valueOf(status);
	}

	/**
	 * @return the ordemRoteiro
	 */
	public Integer getOrdemRoteiro() {
		return ordemRoteiro;
	}

	/**
	 * @param ordemRoteiro the ordemRoteiro to set
	 */
	public void setOrdemRoteiro(Integer ordemRoteiro) {
		this.ordemRoteiro = ordemRoteiro;
	}

	/**
	 * @return the ordemRota
	 */
	public Integer getOrdemRota() {
		return ordemRota;
	}

	/**
	 * @param ordemRota the ordemRota to set
	 */
	public void setOrdemRota(Integer ordemRota) {
		this.ordemRota = ordemRota;
	}

	/**
	 * @return the ordemRotaPdv
	 */
	public Integer getOrdemRotaPdv() {
		return ordemRotaPdv;
	}

	/**
	 * @param ordemRotaPdv the ordemRotaPdv to set
	 */
	public void setOrdemRotaPdv(Integer ordemRotaPdv) {
		this.ordemRotaPdv = ordemRotaPdv;
	}
	
}
