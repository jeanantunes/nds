package br.com.abril.nds.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.filtro.FiltroViewNotaFiscalDTO;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.nota.Condicao;
import br.com.abril.nds.util.Intervalo;

public interface GeracaoNFeService {
	
	/**
	 * Gera nota fiscal
	 * 
	 * @param intervaloBox
	 * @param intervalorCota
	 * @param intervaloDateMovimento
	 * @param listIdFornecedor
	 * @param listIdProduto
	 * @param idTipoNotaFiscal
	 * @param dataEmissao
	 * @param idCotasSuspensas cotas suspensas que vao emitir nota
	 * @return 
	 *  
	 */
	public abstract void gerarNotaFiscal(FiltroViewNotaFiscalDTO filtro, List<Long> idCotasSuspensas, Condicao condicao) throws FileNotFoundException, IOException;


	List<CotaExemplaresDTO> busca(Intervalo<Integer> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, Long idTipoNotaFiscal, Long idRoteiro,
			Long idRota, String sortname, String sortorder,
			Integer resultsPage, Integer page, SituacaoCadastro situacaoCadastro);

	
	public abstract List<CotaExemplaresDTO> consultaCotaExemplareSumarizado(FiltroViewNotaFiscalDTO filtro);


	public abstract Long consultaCotaExemplareSumarizadoQtd(FiltroViewNotaFiscalDTO filtro);


	public abstract List<FornecedorExemplaresDTO> consultaFornecedorExemplarSumarizado(FiltroViewNotaFiscalDTO filtro);

}
