package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResumoReparteFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = -4042843270342484205L;
	
	private BigDecimal totalReparte;
	
	private BigDecimal totalSobras;
	
	private BigDecimal totalFaltas;
	
	private BigDecimal totalTranferencia;
	
	private BigDecimal totalADistribuir;
	
	private BigDecimal totalDistribuido;
	
	private BigDecimal sobraDistribuido;
	
	private BigDecimal diferenca;

	public BigDecimal getTotalReparte() {
		return totalReparte;
	}

	public void setTotalReparte(BigDecimal totalReparte) {
		this.totalReparte = totalReparte;
	}

	public BigDecimal getTotalSobras() {
		return totalSobras;
	}

	public void setTotalSobras(BigDecimal totalSobras) {
		this.totalSobras = totalSobras;
	}

	public BigDecimal getTotalFaltas() {
		return totalFaltas;
	}

	public void setTotalFaltas(BigDecimal totalFaltas) {
		this.totalFaltas = totalFaltas;
	}

	public BigDecimal getTotalTranferencia() {
		return totalTranferencia;
	}

	public void setTotalTranferencia(BigDecimal totalTranferencia) {
		this.totalTranferencia = totalTranferencia;
	}

	public BigDecimal getTotalADistribuir() {
		return totalADistribuir;
	}

	public void setTotalADistribuir(BigDecimal totalADistribuir) {
		this.totalADistribuir = totalADistribuir;
	}

	public BigDecimal getTotalDistribuido() {
		return totalDistribuido;
	}

	public void setTotalDistribuido(BigDecimal totalDistribuido) {
		this.totalDistribuido = totalDistribuido;
	}

	public BigDecimal getSobraDistribuido() {
		return sobraDistribuido;
	}

	public void setSobraDistribuido(BigDecimal sobraDistribuido) {
		this.sobraDistribuido = sobraDistribuido;
	}

	public BigDecimal getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(BigDecimal diferenca) {
		this.diferenca = diferenca;
	}
	
	

}
