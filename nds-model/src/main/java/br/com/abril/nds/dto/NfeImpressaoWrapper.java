package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NfeImpressaoWrapper implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<NfeImpressaoDTO> nfesImpressao;
	
	public NfeImpressaoWrapper() {}

	public NfeImpressaoWrapper(NfeImpressaoDTO nfeImpressao) {
		
		nfesImpressao = new ArrayList<NfeImpressaoDTO>();
		
		nfesImpressao.add(nfeImpressao);
		
	}

	public List<NfeImpressaoDTO> getNfesImpressao() {
		return nfesImpressao;
	}

}
