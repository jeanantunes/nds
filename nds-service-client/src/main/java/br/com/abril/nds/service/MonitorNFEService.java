package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.NfeVO;
import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;

public interface MonitorNFEService {

	public InfoNfeDTO pesquisarNFe(FiltroMonitorNfeDTO filtro);
	
	public byte[] obterDanfes(List<NfeVO> listaNfeImpressaoDanfe, boolean indEmissaoDepec);
	
	public void validarEmissaoDanfe(Long idNotaFiscal, boolean indEmissaoDepec);

}
