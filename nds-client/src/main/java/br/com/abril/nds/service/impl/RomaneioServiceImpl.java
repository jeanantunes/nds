package br.com.abril.nds.service.impl;

import java.io.ByteArrayOutputStream;
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
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.RomaneioRepository;
import br.com.abril.nds.service.RomaneioService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class RomaneioServiceImpl implements RomaneioService {
	
	@Autowired
	private RomaneioRepository romaneioRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
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
	public byte[] gerarRelatorio(FiltroRomaneioDTO filtro, String limitar, FileType fileType) throws URISyntaxException, JRException{
		
		if (filtro != null){
			
			List<RomaneioDTO> listaDTOParaExportacao = this.buscarRomaneio(filtro, false);
			
			if (!listaDTOParaExportacao.isEmpty()){
			
				Long idRota = null;
				Long qtdCotas = 0L;
				for (RomaneioDTO romaeio : listaDTOParaExportacao){
					
					qtdCotas++;
					
					if (romaeio.getIdRota() != null && romaeio.getIdRota().equals(idRota)){
						
						romaeio.setIdRota(null);
					} else {
						
						idRota = romaeio.getIdRota();
						romaeio.setQtdCotas(qtdCotas);
						qtdCotas = 0L;
					}
				}
				
				listaDTOParaExportacao.get(listaDTOParaExportacao.size() - 1).setQtdCotas(qtdCotas);
			}
			
			RomaneioModelo01DTO dto = new RomaneioModelo01DTO();
			dto.setDataGeracao(filtro.getData());
			dto.setEntregaBox(filtro.getBox());
			dto.setRota(filtro.getRota());
			dto.setRoteiro(filtro.getRoteiro());
			dto.setItens(listaDTOParaExportacao);
			
			List<RomaneioModelo01DTO> lista = new ArrayList<RomaneioModelo01DTO>();
			lista.add(dto);
			
			JRDataSource jrDataSource = new JRBeanCollectionDataSource(lista);
			
			URL diretorioReports = Thread.currentThread().getContextClassLoader().getResource("reports/");
			
			String path = "";
			
			if (filtro.getProdutos() == null || 
					filtro.getProdutos().isEmpty()){
				
				path = diretorioReports.toURI().getPath() + "/romaneio_modelo01.jasper";
			} else if (filtro.getProdutos().size() == 1) {
				
				Long idProdutoEdicao = filtro.getProdutos().get(0);
				
				ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
				
				dto.setCodigoProduto(produtoEdicao.getCodigo());
				dto.setNomeProduto(produtoEdicao.getNomeComercial());
				dto.setEdicao(produtoEdicao.getNumeroEdicao());
				dto.setPacotePadrao(new Long(produtoEdicao.getPacotePadrao()));
				
				path = diretorioReports.toURI().getPath() + "/romaneio_modelo02.jasper";
			} else {
				
				if (filtro.getProdutos().size() > 0){
					
					dto.setNomeProduto0(this.produtoEdicaoRepository.buscarNomeComercial(filtro.getProdutos().get(0)));
				}
				
				if (filtro.getProdutos().size() > 1){
					
					dto.setNomeProduto1(this.produtoEdicaoRepository.buscarNomeComercial(filtro.getProdutos().get(1)));
				}

				if (filtro.getProdutos().size() > 2){
					
					dto.setNomeProduto2(this.produtoEdicaoRepository.buscarNomeComercial(filtro.getProdutos().get(2)));
				}
				
				if (filtro.getProdutos().size() > 3){
					
					dto.setNomeProduto3(this.produtoEdicaoRepository.buscarNomeComercial(filtro.getProdutos().get(3)));
				}
				
				if (filtro.getProdutos().size() > 4){
					
					dto.setNomeProduto4(this.produtoEdicaoRepository.buscarNomeComercial(filtro.getProdutos().get(4)));
				}
				
				if (filtro.getProdutos().size() > 5){
					
					dto.setNomeProduto5(this.produtoEdicaoRepository.buscarNomeComercial(filtro.getProdutos().get(5)));
				}
				
				path = diretorioReports.toURI().getPath() + "/romaneio_modelo03.jasper";
			}
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
			
			if (FileType.PDF == fileType){
			
				return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
			} else if (FileType.XLS == fileType){
				
				JasperPrint jasperPrint = JasperFillManager.fillReport(path,
						parameters, jrDataSource);

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
	
	@Override
	@Transactional
	public Integer buscarTotalDeCotas(FiltroRomaneioDTO filtro) {		
		
		if(filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		}
		
		return this.romaneioRepository.buscarTotal(filtro, true);
	}
}