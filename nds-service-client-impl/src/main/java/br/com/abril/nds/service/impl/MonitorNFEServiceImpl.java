package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.NfeVO;
import br.com.abril.nds.dto.DanfeDTO;
import br.com.abril.nds.dto.DanfeWrapper;
import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoEmissao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamento;
import br.com.abril.nds.model.fiscal.nota.StatusRetornado;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ItemNotaFiscalSaidaRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.MonitorNFEService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.builders.DanfeBuilder;

@Service
public class MonitorNFEServiceImpl implements MonitorNFEService {
	
	@Autowired
	protected NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	protected ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository;
	
	@Autowired
	protected ItemNotaFiscalSaidaRepository itemNotaFiscalSaidaRepository;
	
	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private NotaFiscalService notaFiscalService;
	
	
	@Transactional
	public InfoNfeDTO pesquisarNotaFiscal(FiltroMonitorNfeDTO filtro) {
		
		InfoNfeDTO info = new InfoNfeDTO();
		
		List<NfeDTO> listaNotaFisal = notaFiscalRepository.pesquisarNotaFiscal(filtro);
		
		Integer qtdeRegistros = notaFiscalRepository.obterQtdeRegistroNotaFiscal(filtro);

		info.setListaNfeDTO(listaNotaFisal);

		info.setQtdeRegistros(qtdeRegistros);
		
		return info;
		
	}
	/**
	 * Obtém os arquivos das DANFE relativas as NFes passadas como parâmetro. 
	 * 
	 * @param listaNfeImpressaoDanfe
	 * @param indEmissaoDepec
	 * 
	 * @return byte[] - Bytes das DANFES
	 */
	@Transactional
	public byte[] obterDanfes(List<NfeVO> listaNfeImpressaoDanfe, boolean indEmissaoDepec) {
		
		List<DanfeWrapper> listaDanfeWrapper = new ArrayList<DanfeWrapper>();
		
		for(NfeVO notaFiscal :  listaNfeImpressaoDanfe) {
			
			DanfeDTO danfe = obterDadosDANFE(notaFiscal);
			
			if(danfe!=null) {
				listaDanfeWrapper.add(new DanfeWrapper(danfe));
			}
			
		}
		
		try {
			
			return gerarDocumentoIreport(listaDanfeWrapper, indEmissaoDepec);
		
		} catch(Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração dos arquivos DANFE: " + e.getMessage());
		}
		
	}
	
	@Transactional
	public void validarEmissaoDanfe(Long idNota, boolean indEmissaoDepec) {
		
		NotaFiscal notaFiscal = notaFiscalRepository.buscarPorId(idNota);
		
		if(notaFiscal == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nota Fiscal não encontrada!");
			
		}
		
		if (notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica().getChaveAcesso() == null || 
				notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica().getRetornoComunicacaoEletronica() == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nota ainda não submetida ao SEFAZ");
		} else if (!notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica().getRetornoComunicacaoEletronica().getStatusRetornado().equals(
				StatusRetornado.AUTORIZADO) && !notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica().getRetornoComunicacaoEletronica().getStatusRetornado().equals(
						StatusRetornado.CANCELAMENTO_HOMOLOGADO) ) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nota não autorizada pelo SEFAZ");
		}
		
		if(indEmissaoDepec) {
			
			if(	StatusProcessamento.GERADA.equals(notaFiscal.getNotaFiscalInformacoes().getStatusProcessamento()) ||
				StatusProcessamento.EM_PROCESSAMENTO.equals(notaFiscal.getNotaFiscalInformacoes().getStatusProcessamento()) ) {
				
				notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setTipoEmissao(TipoEmissao.CONTINGENCIA);
				notaFiscalRepository.alterar(notaFiscal);
				
				return;
			}
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nota Fiscal não possui status correto para geração Depec");
			
		}
	}
	
	/**
	 * Carrega e retorna um objeto DANFE com os dados pertinentes a notaFiscal 
	 * passada como parâmetro.
	 * 
	 * @param nfe
	 * 
	 * @return DanfeDTO
	 */
	@Transactional
	private DanfeDTO obterDadosDANFE(NfeVO nfe) {
		
		DanfeDTO danfe = new DanfeDTO();
		
		if(nfe == null || nfe.getIdNotaFiscal() == null) {
			return null;
		}
		
		NotaFiscal notaFiscal = notaFiscalRepository.buscarPorId(nfe.getIdNotaFiscal()); 
		
		if(notaFiscal == null) {
			return null;
		}
		
		DanfeBuilder.carregarDanfeDadosPrincipais(danfe, notaFiscal);
		
		DanfeBuilder.carregarDanfeDadosEmissor(danfe, notaFiscal);

		DanfeBuilder.carregarDanfeDadosDestinatario(danfe, notaFiscal);
		
		DanfeBuilder.carregarDanfeDadosTributarios(danfe, notaFiscal);
		
		DanfeBuilder.carregarDanfeDadosTransportadora(danfe, notaFiscal);
		
		DanfeBuilder.carregarDadosItensDanfe(danfe, notaFiscal);
		
		DanfeBuilder.carregarDadosDuplicatas(danfe, notaFiscal);
		
		return danfe;
		
	}
	
	protected URL obterDiretorioReports() {
		
		URL urlDanfe = Thread.currentThread().getContextClassLoader().getResource("reports");
		
		return urlDanfe;
	}
	
	private byte[] gerarDocumentoIreport(List<DanfeWrapper> list, boolean indEmissaoDepec) throws JRException, URISyntaxException {

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		
		URL diretorioReports = obterDiretorioReports();
		
		String path = diretorioReports.toURI().getPath() + "/danfeWrapper.jasper";
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
		parameters.put("IND_EMISSAO_DEPEC", indEmissaoDepec);
		
		InputStream inputStream = this.parametrosDistribuidorService.getLogotipoDistribuidor();
		
		if(inputStream == null) {
			inputStream = new ByteArrayInputStream(new byte[0]);
		}
		
		parameters.put("LOGO_DISTRIBUIDOR", inputStream);
		
		return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}
	
	@Override
	@Transactional
	public void cancelarNfe(FiltroMonitorNfeDTO filtro) throws FileNotFoundException, IOException {
		
		
		List<NotaFiscal> notas = this.notaFiscalRepository.obterListaNotasFiscaisNumeroSerie(filtro);
		
		if(notas != null && ! notas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nota Fiscal não encontrada!");
		}
		
		this.notaFiscalService.exportarNotasFiscais(notas);
		
	}
	
}
