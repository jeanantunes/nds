package br.com.abril.nds.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.util.Intervalo;

public interface NFeService {

	public byte[] obterDanfesPDF(List<NotaFiscal> listaNfeImpressaoDanfe, boolean indEmissaoDepec);
	
	public byte[] obterNEsPDF(List<NotaEnvio> listaNfeImpressaoNE, boolean isNECA, Intervalo<Date> intervalo);
	
	public NotaFiscal obterNotaFiscalPorId(NotaFiscal notaFiscal);
	
	public NotaEnvio obterNotaEnvioPorId(NotaEnvio notaEnvio);
	
	public NotaFiscal mergeNotaFiscal(NotaFiscal notaFiscal);
	
	public NotaEnvio mergeNotaEnvio(NotaEnvio notaEnvio);
	
	// Geracao NFe Service
	List<NotaFiscal> gerarNotaFiscal(FiltroNFeDTO filtro) throws FileNotFoundException, IOException;

	List<CotaExemplaresDTO> busca(Intervalo<Integer> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, Long idTipoNotaFiscal, Long idRoteiro,
			Long idRota, String sortname, String sortorder,
			Integer resultsPage, Integer page, SituacaoCadastro situacaoCadastro);

	
	public abstract List<CotaExemplaresDTO> consultaCotaExemplaresSumarizados(FiltroNFeDTO filtro);


	public abstract Long consultaCotaExemplareSumarizadoQtd(FiltroNFeDTO filtro);


	public abstract List<FornecedorExemplaresDTO> consultaFornecedorExemplarSumarizado(FiltroNFeDTO filtro);

	public abstract Long consultaFornecedorExemplaresSumarizadosQtd(FiltroNFeDTO filtro);

	
}
