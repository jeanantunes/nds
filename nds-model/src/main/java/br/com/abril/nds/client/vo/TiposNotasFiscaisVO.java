package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class TiposNotasFiscaisVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2363370368181215212L;

	private Long notaFiscalEnvioCota;
	
	private Long notaFiscalDevolucaoCota;
	
	private Long notaFiscalVenda;
	
	private Long notaFiscalDevolucaoFornecedor;
	
	private Long notaFiscalSimbolicaVendaFornecedor;

	public Long getNotaFiscalEnvioCota() {
		return notaFiscalEnvioCota;
	}

	public void setNotaFiscalEnvioCota(Long notaFiscalEnvioCota) {
		this.notaFiscalEnvioCota = notaFiscalEnvioCota;
	}

	public Long getNotaFiscalDevolucaoCota() {
		return notaFiscalDevolucaoCota;
	}

	public void setNotaFiscalDevolucaoCota(Long notaFiscalDevolucaoCota) {
		this.notaFiscalDevolucaoCota = notaFiscalDevolucaoCota;
	}

	public Long getNotaFiscalVenda() {
		return notaFiscalVenda;
	}

	public void setNotaFiscalVenda(Long notaFiscalVenda) {
		this.notaFiscalVenda = notaFiscalVenda;
	}

	public Long getNotaFiscalDevolucaoFornecedor() {
		return notaFiscalDevolucaoFornecedor;
	}

	public void setNotaFiscalDevolucaoFornecedor(Long notaFiscalDevolucaoFornecedor) {
		this.notaFiscalDevolucaoFornecedor = notaFiscalDevolucaoFornecedor;
	}

	public Long getNotaFiscalSimbolicaVendaFornecedor() {
		return notaFiscalSimbolicaVendaFornecedor;
	}

	public void setNotaFiscalSimbolicaVendaFornecedor(
			Long notaFiscalSimbolicaVendaFornecedor) {
		this.notaFiscalSimbolicaVendaFornecedor = notaFiscalSimbolicaVendaFornecedor;
	}

}