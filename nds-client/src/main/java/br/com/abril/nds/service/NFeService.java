package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public interface NFeService {

	public InfoNfeDTO pesquisarNFe(FiltroMonitorNfeDTO filtro);
	
	public byte[] obterDanfesPDF(List<NotaFiscal> listaNfeImpressaoDanfe, boolean indEmissaoDepec);
	
	public byte[] obterNEsPDF(List<NotaEnvio> listaNfeImpressaoNE, boolean isNECA);
	
}
