package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ConsolidadoFinanceiroCotaDTO implements Serializable {
	
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
	
	private int lineId;
	
	public ConsolidadoFinanceiroCotaDTO(
			Date dataConsolidado,
			BigDecimal numeroAtrasado,
			BigDecimal consignado,
			BigDecimal encalhe,
			BigDecimal vendaEncalhe,
			BigDecimal debCred,
			BigDecimal encargos,
			BigDecimal pendente,
			BigDecimal total) {
		this.dataConsolidado = dataConsolidado;
		this.numeroAtradao = numeroAtrasado;
		this.consignado = consignado;
		this.encalhe = encalhe;
		this.vendaEncalhe = vendaEncalhe;
		this.debCred = debCred;
		this.encargos = encargos;
		this.pendente = pendente;
		this.total = total;
		
		
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
		if (!(obj instanceof ConsolidadoFinanceiroCotaDTO)) {
			return false;
		}
		ConsolidadoFinanceiroCotaDTO other = (ConsolidadoFinanceiroCotaDTO) obj;
		if (lineId != other.lineId) {
			return false;
		}
		return true;
	}
	
	

}
