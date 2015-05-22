package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.RomaneioModelo01DTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.RomaneioRepository;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.RomaneioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.JasperUtil;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class RomaneioServiceImpl implements RomaneioService {
	
	@Autowired
	private RomaneioRepository romaneioRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	protected ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	protected DistribuidorService distribuidorService;
	
	@Override
	@Transactional
	public List<RomaneioDTO> buscarRomaneio(FiltroRomaneioDTO filtro, boolean limitar) {
		
		if(filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		}
		
		return this.romaneioRepository.buscarRomaneios(filtro, limitar);
	}

	@Override
	@Transactional
	public Integer buscarTotalDeRomaneios(FiltroRomaneioDTO filtro) {
		
		if(filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		}
		
		return this.romaneioRepository.buscarTotal(filtro, false);
	}
	
	@Override
	@Transactional
	public List<ProdutoEdicao> buscarProdutosLancadosData(Date data){
		
		if (data == null){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Data é obrigatório."));
		}
		
		return this.produtoEdicaoRepository.buscarProdutosLancadosData(data);
	}
	
	
	@Transactional
	@Override
	public byte[] gerarRelatorio(FiltroRomaneioDTO filtro, String limitar, FileType fileType) throws URISyntaxException, JRException {
		
		if (filtro != null) {
			
			List<RomaneioDTO> lstRomaneioDTO = romaneioRepository.buscarRomaneiosParaExportacao(filtro);
			
			List<RomaneioModelo01DTO> lstRelatorio = new ArrayList<RomaneioModelo01DTO>();
			
			// Formata os romaneios para o relatório:
			if (lstRomaneioDTO != null && !lstRomaneioDTO.isEmpty()) {
				
				Long idRota = null;
				Long idRoteiro = null;
				Long idBox = null;
				
				RomaneioModelo01DTO dto = null;
				for (RomaneioDTO romaneio : lstRomaneioDTO) {
					
					if (idBox != null && idBox.equals(romaneio.getIdBox())
							&& idRoteiro.equals(romaneio.getIdRoteiro())
							&& idRota.equals(romaneio.getIdRota())) {

						dto.getItens().add(romaneio);

					} else {
						
						// Novo RomaneioModelo01DTO:
						idRota = romaneio.getIdRota();
						idRoteiro = romaneio.getIdRoteiro();
						idBox = romaneio.getIdBox();

						dto = new RomaneioModelo01DTO();
						dto.setDataGeracao(filtro.getData());
						dto.setEntregaBox(romaneio.getNomeBox());
						dto.setRota(romaneio.getNomeRota());
						dto.setRoteiro(romaneio.getNomeRoteiro());
						dto.setEntregaBox(idBox + " - " + romaneio.getNomeBox());
						
						dto.setItens(new ArrayList<RomaneioDTO>());
						dto.getItens().add(romaneio);
						
						lstRelatorio.add(dto);
					}
				}
			}
			
			URL diretorioReports = Thread.currentThread().getContextClassLoader().getResource("reports/");
			StringBuilder path = new StringBuilder();
			path.append(diretorioReports.toURI().getPath());
			
			final int qtdProdutos = (filtro.getProdutos() == null ? 0 : filtro.getProdutos().size()); 
			
			int qtdColunasProduto = 0;
			
			switch (qtdProdutos) {
				
				// nenhum produto a exibir:
				case 0:
					
					path.append("/romaneio_modelo01.jasper");
					break;
	
				// apenas um produto a exibir:
				case 1:
					
					Long idProdutoEdicao = filtro.getProdutos().get(0);
					ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
					for (RomaneioModelo01DTO dto : lstRelatorio) {
						
						dto.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
						dto.setNomeProduto(this.tratarNomeProdutoEdicao(produtoEdicao));
						dto.setEdicao(produtoEdicao.getNumeroEdicao());
						dto.setPacotePadrao(Long.valueOf(produtoEdicao.getPacotePadrao()));
					}
					
					path.append("/romaneio_modelo02.jasper");
					break;
				
				// vários produtos a exibir:
				default:
					
					List<String> nomesProduto = new ArrayList<>();
					for (Long idEdicao : filtro.getProdutos()) {
						
						nomesProduto.add(this.tratarNomeProdutoEdicao(this.produtoEdicaoRepository.buscarPorId(idEdicao)));
					}
					
					qtdColunasProduto = nomesProduto.size();
					for (RomaneioModelo01DTO dto : lstRelatorio) {
						
						switch (qtdColunasProduto) {
						case 6:
							dto.setNomeProduto5(nomesProduto.get(5));
						case 5:
							dto.setNomeProduto4(nomesProduto.get(4));
						case 4:
							dto.setNomeProduto3(nomesProduto.get(3));
						case 3:
							dto.setNomeProduto2(nomesProduto.get(2));
						case 2:
						default:
							dto.setNomeProduto1(nomesProduto.get(1));
							dto.setNomeProduto0(nomesProduto.get(0));
							break;
						}
					}
					
					path.append("/romaneio_modelo03.jasper");

					break;
			}
			
			
			InputStream inputStream = this.parametrosDistribuidorService.getLogotipoDistribuidor();
			
			if(inputStream == null) {
				inputStream = new ByteArrayInputStream(new byte[0]);
			}
			
			JRDataSource jrDataSource = new JRBeanCollectionDataSource(lstRelatorio);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
			parameters.put("QTD_COLUNAS_PRODUTO", qtdColunasProduto);
			parameters.put("LOGO_DISTRIBUIDOR", JasperUtil.getImagemRelatorio(inputStream));
			parameters.put("RAZAO_SOCIAL_DISTRIBUIDOR", this.distribuidorService.obterRazaoSocialDistribuidor());
			
			if (FileType.PDF == fileType) {
			
				return JasperRunManager.runReportToPdf(path.toString(),
						parameters, jrDataSource);
			} else if (FileType.XLS == fileType) {
				
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						path.toString(), parameters, jrDataSource);

				JRXlsExporter exporter = new JRXlsExporter();

				ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();

				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "romaneio");

				exporter.exportReport();

				return xlsReport.toByteArray();
			} else {
				
				throw new ValidacaoException(TipoMensagem.ERROR, "Parâmetro tipo de arquivo inválido.");
			}
		
		} else {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro inválido.");
		}
	}
		
	/**
	 * Para obter e tratar o nome comercial do produto.
	 * 
	 * @param pEdicao
	 * @return
	 */
	private String tratarNomeProdutoEdicao(ProdutoEdicao pEdicao) {
		
		String nome = "";
		if (pEdicao != null 
				&& pEdicao.getProduto() != null 
				&& pEdicao.getProduto().getNome() != null) {
			
			nome = pEdicao.getProduto().getNome();
		}
		
		return nome;
	}
	
	
	@Override
	@Transactional
	public Integer buscarTotalDeCotas(FiltroRomaneioDTO filtro) {		
		
		if(filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		}
		
		return this.romaneioRepository.buscarTotal(filtro, true);
	}
}