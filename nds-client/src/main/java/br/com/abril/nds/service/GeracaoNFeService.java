package br.com.abril.nds.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.Intervalo;

public interface GeracaoNFeService {
	
	
	/**
	 * Busca a quantidade de exemplares relacionadas a cota para gerar NFe
	 * 
	 * @param intervaloBox Intervalo de Box(de ate)
	 * @param intervalorCota Intervalo de Cota(de ate)
	 * @param intervaloDateMovimento Intervalo de Data(de ate)
	 * @param listIdFornecedor lista de fornecedores
	 * @param sortname
	 * @param sortorder
	 * @param rp
	 * @param page
	 * @param situacaoCadastro 
	 * @param tipoNotaFiscal Tipo de nota
	 * @return
	 */
	public abstract List<CotaExemplaresDTO> busca(Intervalo<Integer> intervaloBox,
	Intervalo<Integer> intervalorCota,
	Intervalo<Date> intervaloDateMovimento, List<Long> listIdFornecedor, Long idTipoNotaFiscal, Long idRoteiro, Long idRota, 
	String sortname, String sortorder, Integer rp, Integer page, SituacaoCadastro situacaoCadastro);


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
	 *  
	 */
	public abstract void gerarNotaFiscal(Intervalo<Integer> intervaloBox, Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento, List<Long> listIdFornecedor, List<Long> listIdProduto, 
			Long idTipoNotaFiscal, Date dataEmissao, List<Long> idCotasSuspensas) throws FileNotFoundException, IOException;

}
