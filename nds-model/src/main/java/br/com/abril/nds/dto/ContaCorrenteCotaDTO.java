package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ContaCorrenteCotaDTO implements Serializable {
	
	private static final long serialVersionUID = -580201558784688016L;

	private Date data;
	
	private BigDecimal valorPostergado;
	
	private BigDecimal NA;
	
	private BigDecimal consignado;
	
	private BigDecimal encalhe;
	
	private BigDecimal vendaEncalhe;
	
	private BigDecimal debCred;
	
	private BigDecimal encargos;
	
	private BigDecimal pendente;
	
	private BigDecimal total;
	
	private int lineId;
	
	public ContaCorrenteCotaDTO() {
		
	}
	
	public Date getData() {
		return data;
	}



	public void setData(Date data) {
		this.data = data;
	}



	public BigDecimal getValorPostergado() {
		return valorPostergado;
	}



	public void setValorPostergado(BigDecimal valorPostergado) {
		this.valorPostergado = valorPostergado;
	}



	public BigDecimal getNA() {
		return NA;
	}



	public void setNA(BigDecimal nA) {
		NA = nA;
	}



	public BigDecimal getConsignado() {
		return consignado;
	}



	public void setConsignado(BigDecimal consignado) {
		this.consignado = consignado;
	}



	public BigDecimal getEncalhe() {
		return encalhe;
	}



	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe;
	}



	public BigDecimal getVendaEncalhe() {
		return vendaEncalhe;
	}



	public void setVendaEncalhe(BigDecimal vendaEncalhe) {
		this.vendaEncalhe = vendaEncalhe;
	}



	public BigDecimal getDebCred() {
		return debCred;
	}



	public void setDebCred(BigDecimal debCred) {
		this.debCred = debCred;
	}



	public BigDecimal getEncargos() {
		return encargos;
	}



	public void setEncargos(BigDecimal encargos) {
		this.encargos = encargos;
	}



	public BigDecimal getPendente() {
		return pendente;
	}



	public void setPendente(BigDecimal pendente) {
		this.pendente = pendente;
	}



	public BigDecimal getTotal() {
		return total;
	}



	public void setTotal(BigDecimal total) {
		this.total = total;
	}



	public int getLineId() {
		return lineId;
	}



	public void setLineId(int lineId) {
		this.lineId = lineId;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lineId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ContaCorrenteCotaDTO)) {
			return false;
		}
		ContaCorrenteCotaDTO other = (ContaCorrenteCotaDTO) obj;
		if (lineId != other.lineId) {
			return false;
		}
		return true;
	}
	
	

}
