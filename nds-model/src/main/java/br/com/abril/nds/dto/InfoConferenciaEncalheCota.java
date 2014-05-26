package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;

public class InfoConferenciaEncalheCota implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Id de uma conferencia que esta em processo de reabertura.
	 */
	private Long idControleConferenciaEncalheCota;
	
	private Cota cota;
	
	private List<ConferenciaEncalheDTO> listaConferenciaEncalhe;
	
	private List<DebitoCreditoCotaDTO> listaDebitoCreditoCota;
	
	private NotaFiscalEntradaCota notaFiscalEntradaCota;
	
	private BigDecimal reparte;
	
	private BigDecimal encalhe;
	
	private BigDecimal valorVendaDia;
	
	private BigDecimal totalDebitoCreditoCota;
	
	private BigDecimal valorPagar;
	
	private boolean distribuidorAceitaJuramentado;
	
	private boolean processoUtilizaNfe;
	
	
	public boolean isDistribuidorAceitaJuramentado() {
		return distribuidorAceitaJuramentado;
	}

	public void setDistribuidorAceitaJuramentado(
			boolean distribuidorAceitaJuramentado) {
		this.distribuidorAceitaJuramentado = distribuidorAceitaJuramentado;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public BigDecimal getValorVendaDia() {
		return valorVendaDia;
	}

	public void setValorVendaDia(BigDecimal valorVendaDia) {
		this.valorVendaDia = valorVendaDia;
	}

	public BigDecimal getTotalDebitoCreditoCota() {
		return totalDebitoCreditoCota;
	}

	public void setTotalDebitoCreditoCota(BigDecimal totalDebitoCreditoCota) {
		this.totalDebitoCreditoCota = totalDebitoCreditoCota;
	}

	public BigDecimal getValorPagar() {
		return valorPagar;
	}

	public void setValorPagar(BigDecimal valorPagar) {
		this.valorPagar = valorPagar;
	}

	public List<ConferenciaEncalheDTO> getListaConferenciaEncalhe() {
		return listaConferenciaEncalhe;
	}

	public void setListaConferenciaEncalhe(
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		this.listaConferenciaEncalhe = listaConferenciaEncalhe;
	}

	public List<DebitoCreditoCotaDTO> getListaDebitoCreditoCota() {
		return listaDebitoCreditoCota;
	}

	public void setListaDebitoCreditoCota(
			List<DebitoCreditoCotaDTO> listaDebitoCreditoCota) {
		this.listaDebitoCreditoCota = listaDebitoCreditoCota;
	}

	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}

	public BigDecimal getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe;
	}

	public Long getIdControleConferenciaEncalheCota() {
		return idControleConferenciaEncalheCota;
	}

	public void setIdControleConferenciaEncalheCota(
			Long idControleConferenciaEncalheCota) {
		this.idControleConferenciaEncalheCota = idControleConferenciaEncalheCota;
	}

	public NotaFiscalEntradaCota getNotaFiscalEntradaCota() {
		return notaFiscalEntradaCota;
	}

	public void setNotaFiscalEntradaCota(NotaFiscalEntradaCota notaFiscalEntradaCota) {
		this.notaFiscalEntradaCota = notaFiscalEntradaCota;
	}

	public boolean isProcessoUtilizaNfe() {
		return processoUtilizaNfe;
	}

	public void setProcessoUtilizaNfe(boolean processoUtilizaNfe) {
		this.processoUtilizaNfe = processoUtilizaNfe;
	}
	
}