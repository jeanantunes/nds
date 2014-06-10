package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.util.Intervalo;

public interface NFeService {

	public InfoNfeDTO pesquisarNFe(FiltroMonitorNfeDTO filtro);
	
	public byte[] obterDanfesPDF(List<NotaFiscal> listaNfeImpressaoDanfe, boolean indEmissaoDepec);
	
	public byte[] obterNEsPDF(List<NotaEnvio> listaNfeImpressaoNE, boolean isNECA, Intervalo<Date> intervalo);
	
	public NotaFiscal obterNotaFiscalPorId(NotaFiscal notaFiscal);
	
	public NotaEnvio obterNotaEnvioPorId(NotaEnvio notaEnvio);
	
	public NotaFiscal mergeNotaFiscal(NotaFiscal notaFiscal);
	
	public NotaEnvio mergeNotaEnvio(NotaEnvio notaEnvio);
	
}
