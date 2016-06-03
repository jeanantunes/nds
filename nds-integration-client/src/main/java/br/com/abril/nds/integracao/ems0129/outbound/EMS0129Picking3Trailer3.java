package br.com.abril.nds.integracao.ems0129.outbound;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0129Picking3Trailer3 implements Serializable {

	private static final long serialVersionUID = 2239024763455443032L;
	
	private String identificadorLinha;
	
	private String codigoCota;
	
	private String precoTotal;
	
	private String precoTotalDesconto;
	
	private String debito;
	
	private String credito;
	
	private String dataLancamento;
	
	private String consignado;
	
	

	public EMS0129Picking3Trailer3(String identificadorLinha, String codigoCota, String precoTotal, String precoTotalDesconto, String debito,
			String credito, String dataLancamento, String consignado) {
		this.identificadorLinha = identificadorLinha;
		this.codigoCota = codigoCota;
		this.precoTotal = precoTotal;
		this.precoTotalDesconto = precoTotalDesconto;
		this.debito = debito;
		this.credito = credito;
		this.dataLancamento = dataLancamento;
		this.consignado = consignado;
	}
	
	@Field(offset = 1, length = 2)
	public String getIdentificadorLinha() {
		return identificadorLinha;
	}

	@Field(offset = 3, length = 5)
	public String getCodigoCota() {
		return codigoCota;
	}

	@Field(offset = 8, length = 11)
	public String getPrecoTotal() {
		return precoTotal;
	}

	@Field(offset = 19, length = 11)
	public String getPrecoTotalDesconto() {
		return precoTotalDesconto;
	}

	@Field(offset = 30, length = 11)
	public String getDebito() {
		return debito;
	}

	@Field(offset = 41, length = 11)
	public String getCredito() {
		return credito;
	}

	@Field(offset = 52, length = 9)
	public String getDataLancamento() {
		return dataLancamento;
	}

	@Field(offset = 61, length = 11)
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

	public void setIdentificadorLinha(String identificadorLinha) {
		this.identificadorLinha = identificadorLinha;
	}
	
	
	
}
