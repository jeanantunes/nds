package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChamadaEncalheImpressaoWrapper implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<CotaEmissaoDTO> emissaoCEImpressao;
	
	public ChamadaEncalheImpressaoWrapper() {}

	public ChamadaEncalheImpressaoWrapper(CotaEmissaoDTO cotaEmissao) {
		
		emissaoCEImpressao = new ArrayList<CotaEmissaoDTO>();
		
		emissaoCEImpressao.add(cotaEmissao);
		
	}

	public List<CotaEmissaoDTO> getEmissaoCEImpressao() {
		return emissaoCEImpressao;
	}

}
