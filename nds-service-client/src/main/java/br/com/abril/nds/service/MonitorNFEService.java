package br.com.abril.nds.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import br.com.abril.nds.client.vo.NfeVO;
import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;

public interface MonitorNFEService {

	public InfoNfeDTO pesquisarNotaFiscal(FiltroMonitorNfeDTO filtro);
	
	public byte[] obterDanfes(List<NfeVO> listaNfeImpressaoDanfe, boolean indEmissaoDepec);
	
	public void validarEmissaoDanfe(Long idNotaFiscal, boolean indEmissaoDepec);

	public void cancelarNfe(FiltroMonitorNfeDTO filtro) throws FileNotFoundException, IOException;

}
