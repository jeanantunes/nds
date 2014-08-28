package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class DadosImpressaoEmissaoChamadaEncalhe implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<CotaEmissaoDTO> cotasEmissao;
	
	private List<List<CapaDTO>> capasPaginadas;

	public List<CotaEmissaoDTO> getCotasEmissao() {
		return cotasEmissao;
	}

	public void setCotasEmissao(List<CotaEmissaoDTO> cotasEmissao) {
		this.cotasEmissao = cotasEmissao;
	}

	public List<List<CapaDTO>> getCapasPaginadas() {
		return capasPaginadas;
	}

	public void setCapasPaginadas(List<List<CapaDTO>> capasPaginadas) {
		this.capasPaginadas = capasPaginadas;
	}
	
}
