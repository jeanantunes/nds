package br.com.abril.nds.util.xls;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.util.upload.XlsMapper;

public class CotaTemplateDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4639121021150316634L;
	
	@XlsMapper(value="codigoProduto")
	private String codigoProduto;
	
	@XlsMapper(value = "numeroCota")
	private Integer numeroCota;
	
	@XlsMapper(value = "reparteMinimo")
	private BigInteger reparteMinimo;
	
	@XlsMapper(value = "reparteMaximo")
	private BigInteger reparteMaximo;
	
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	public BigInteger getReparteMinimo() {
		return reparteMinimo;
	}
	public void setReparteMinimo(BigInteger reparteMinimo) {
		this.reparteMinimo = reparteMinimo;
	}
	public BigInteger getReparteMaximo() {
		return reparteMaximo;
	}
	public void setReparteMaximo(BigInteger reparteMaximo) {
		this.reparteMaximo = reparteMaximo;
	}
}
