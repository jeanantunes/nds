package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ArquivoRotDTO;
import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.model.cadastro.Box;

public interface RomaneioRepository extends Repository<Box, Long> {
	
	List<RomaneioDTO> buscarRomaneios(FiltroRomaneioDTO filtro, boolean limitar);

	Integer buscarTotal(FiltroRomaneioDTO filtro, boolean countCotas);
	
	/**
	 * Busca os Romaneios para serem exportados em outros formatos além da
	 * tela do usuário.
	 * 
	 * @param filtro
	 * @return
	 */
	List<RomaneioDTO> buscarRomaneiosParaExportacao(FiltroRomaneioDTO filtro);

	List<ArquivoRotDTO> obterInformacoesParaArquivoRot(FiltroRomaneioDTO filtro);
	
}
