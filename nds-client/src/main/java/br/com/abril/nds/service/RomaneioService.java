package br.com.abril.nds.service;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.util.export.FileExporter.FileType;

public interface RomaneioService {
	
	List<RomaneioDTO> buscarRomaneio(FiltroRomaneioDTO filtro, String limitar);

	Integer buscarTotalDeRomaneios(FiltroRomaneioDTO filtro);

	List<ProdutoEdicao> buscarProdutosLancadosData(Date data);

	byte[] gerarRelatorio(FiltroRomaneioDTO filtro, String limitar, FileType fileType)
			throws URISyntaxException, JRException;
}