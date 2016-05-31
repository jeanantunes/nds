package br.com.abril.nds.dto;

public class SubHeaderPickingDTO {
	
	private String identificadorLinha;
	
	private String codigoCota;
	
	private String precoTotal;
	
	private String precoTotalDesconto;
	
	private String debito;
	
	private String credito;
	
	private String dataLancamento;
	
	private String consignado;

	public String getCodigoCota() {
		return codigoCota;
	}

	public String getPrecoTotal() {
		return precoTotal;
	}

	public String getPrecoTotalDesconto() {
		return precoTotalDesconto;
	}

	public String getDebito() {
		return debito;
	}

	public String getCredito() {
		return credito;
	}

	public String getDataLancamento() {
		return dataLancamento;
	}

	public String getConsignado() {
		return consignado;
	}

	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}

	public void setPrecoTotal(String precoTotal) {
		this.precoTotal = precoTotal;
	}

	public void setPrecoTotalDesconto(String precoTotalDesconto) {
		this.precoTotalDesconto = precoTotalDesconto;
	}

	public void setDebito(String debito) {
		this.debito = debito;
	}

	public void setCredito(String credito) {
		this.credito = credito;
	}

	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public void setConsignado(String consignado) {
		this.consignado = consignado;
	}

	public String getIdentificadorLinha() {
		return identificadorLinha;
	}

	public void setIdentificadorLinha(String identificadorLinha) {
		this.identificadorLinha = identificadorLinha;
	}
	
}
