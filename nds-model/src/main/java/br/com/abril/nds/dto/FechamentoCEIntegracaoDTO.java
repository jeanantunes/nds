package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class FechamentoCEIntegracaoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<ItemFechamentoCEIntegracaoDTO> itensFechamentoCE;
	
	private Integer qntItensCE;
	
	private boolean semanaFechada;
	
	private FechamentoCEIntegracaoConsolidadoDTO consolidado;

	public List<ItemFechamentoCEIntegracaoDTO> getItensFechamentoCE() {
		return itensFechamentoCE;
	}

	public void setItensFechamentoCE(
			List<ItemFechamentoCEIntegracaoDTO> itensFechamentoCE) {
		this.itensFechamentoCE = itensFechamentoCE;
	}

	public Integer getQntItensCE() {
		return qntItensCE;
	}

	public void setQntItensCE(Integer qntItensCE) {
		this.qntItensCE = qntItensCE;
	}

	public FechamentoCEIntegracaoConsolidadoDTO getConsolidado() {
		return consolidado;
	}

	public void setConsolidado(FechamentoCEIntegracaoConsolidadoDTO consolidado) {
		this.consolidado = consolidado;
	}

	public boolean isSemanaFechada() {
		return semanaFechada;
	}

	public void setSemanaFechada(boolean semanaFechada) {
		this.semanaFechada = semanaFechada;
	}

}
