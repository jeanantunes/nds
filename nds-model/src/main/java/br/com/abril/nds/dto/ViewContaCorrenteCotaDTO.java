package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ViewContaCorrenteCotaDTO implements Serializable {
	
	private static final long serialVersionUID = -580201558784688016L;

	private Date dataConsolidado;
	
	private BigDecimal valorPostergado;
	
	private BigDecimal numeroAtradao;
	
	private BigDecimal consignado;
	
	private BigDecimal encalhe;
	
	private BigDecimal vendaEncalhe;
	
	private BigDecimal debCred;
	
	private BigDecimal encargos;
	
	private BigDecimal pendente;
	
	private BigDecimal total;
	
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private int lineId;
	
	public ViewContaCorrenteCotaDTO(){
		
	}
	
	public Date getDataConsolidado() {
		return dataConsolidado;
	}



	public void setDataConsolidado(Date dataConsolidado) {
		this.dataConsolidado = dataConsolidado;
	}



	public BigDecimal getValorPostergado() {
		return valorPostergado;
	}



	public void setValorPostergado(BigDecimal valorPostergado) {
		this.valorPostergado = valorPostergado;
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
	
	public BigDecimal getNumeroAtradao() {
		return numeroAtradao;
	}

	public void setNumeroAtradao(BigDecimal numeroAtradao) {
		this.numeroAtradao = numeroAtradao;
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
		if (!(obj instanceof ViewContaCorrenteCotaDTO)) {
			return false;
		}
		ViewContaCorrenteCotaDTO other = (ViewContaCorrenteCotaDTO) obj;
		if (lineId != other.lineId) {
			return false;
		}
		return true;
	}
	
	

}
