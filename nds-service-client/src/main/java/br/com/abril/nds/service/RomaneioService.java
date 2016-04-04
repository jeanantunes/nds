package br.com.abril.nds.service;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.util.export.FileExporter.FileType;
import net.sf.jasperreports.engine.JRException;

public interface RomaneioService {
	
	List<RomaneioDTO> buscarRomaneio(FiltroRomaneioDTO filtro, boolean limitar);

	Integer buscarTotalDeRomaneios(FiltroRomaneioDTO filtro);

	List<ProdutoEdicao> buscarProdutosLancadosData(Date data);

	byte[] gerarRelatorio(FiltroRomaneioDTO filtro, String limitar, FileType fileType)
			throws URISyntaxException, JRException;

	Integer buscarTotalDeCotas(FiltroRomaneioDTO filtro);

	byte[] gerarArquivoRot(FiltroRomaneioDTO filtro, FileType fileType);
	
}