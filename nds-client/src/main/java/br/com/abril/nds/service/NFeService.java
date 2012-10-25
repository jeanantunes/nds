package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.NfeVO;
import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;

public interface NFeService {

	public InfoNfeDTO pesquisarNFe(FiltroMonitorNfeDTO filtro);
	
	public byte[] obterDanfesPDF(List<NfeVO> listaNfeImpressaoDanfe, boolean indEmissaoDepec);
	
	public byte[] obterNEsPDF(List<NfeVO> listaNfeImpressaoNE);
	
	public byte[] obterNECAsPDF(List<NfeVO> listaNfeImpressaoNECA);
	
}
