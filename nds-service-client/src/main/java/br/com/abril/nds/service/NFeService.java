package br.com.abril.nds.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.BoletoAvulsoDTO;
import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.util.Intervalo;

public interface NFeService {

	public byte[] obterDanfesPDF(List<NotaFiscal> listaNfeImpressaoDanfe, boolean indEmissaoDepec);
	
	public byte[] obterNEsPDF(List<NotaEnvio> listaNfeImpressaoNE, boolean isNECA, Intervalo<Date> intervalo) throws Exception;
	
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

	
	public abstract List<CotaExemplaresDTO> consultaCotaExemplaresSumarizados(FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao);


	public abstract Long consultaCotaExemplareSumarizadoQtd(FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao);


	public abstract List<FornecedorExemplaresDTO> consultaFornecedorExemplarSumarizado(FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao);

	public abstract Long consultaFornecedorExemplaresSumarizadosQtd(FiltroNFeDTO filtro, NaturezaOperacao naturezaOperacao);

	public void gerarNotasFiscaisCotasEncalhe(final List<NotaFiscal> notasFiscais, final Distribuidor distribuidor, final NaturezaOperacao naturezaOperacao, 
			final Map<String, ParametroSistema> parametrosSistema, final List<Cota> cotas);

	void gerarNotasFiscaisCotas(FiltroNFeDTO filtro,
			List<NotaFiscal> notasFiscais, Distribuidor distribuidor,
			NaturezaOperacao naturezaOperacao,
			Map<String, ParametroSistema> parametrosSistema, 
			List<Cota> cotas);

	void gerarNotasFiscaisFornecedor(FiltroNFeDTO filtro,
			List<NotaFiscal> notasFiscais, Distribuidor distribuidor,
			NaturezaOperacao naturezaOperacao,
			Map<String, ParametroSistema> parametrosSistema, 
			List<Fornecedor> fornecedores);
	
	public NaturezaOperacao regimeEspecialParaCota(Cota cota);
	
	public  List<DebitoCreditoDTO> listaBoletoNFE(Date dataBoleto);
	
	NotaFiscal obterNFEPorID(Long idNota);
	
	boolean existeNotaNaData(Date dataReferencia);
	
}