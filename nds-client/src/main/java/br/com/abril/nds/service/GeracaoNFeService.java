package br.com.abril.nds.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.util.Intervalo;

public interface GeracaoNFeService {
	
	
	/**
	 * Busca a quantidade de exemplares relacionadas a cota para gerar NFe
	 * 
	 * @param intervaloBox Intervalo de Box(de ate)
	 * @param intervalorCota Intervalo de Cota(de ate)
	 * @param intervaloDateMovimento Intervalo de Data(de ate)
	 * @param listIdFornecedor lista de fornecedores
	 * @param tipoNotaFiscal Tipo de nota
	 * @param sortname
	 * @param sortorder
	 * @param rp
	 * @param page
	 * @return
	 */
	public abstract List<CotaExemplaresDTO> busca(Intervalo<Integer> intervaloBox,
	Intervalo<Integer> intervalorCota,
	Intervalo<Date> intervaloDateMovimento, List<Long> listIdFornecedor, List<Long> listIdProduto, Long idTipoNotaFiscal, String sortname,
	String sortorder, Integer rp, Integer page);


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
	 *  
	 */
	public abstract void gerarNotaFiscal(Intervalo<Integer> intervaloBox, Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento, List<Long> listIdFornecedor, List<Long> listIdProduto, 
			Long idTipoNotaFiscal, Date dataEmissao) throws FileNotFoundException, IOException;
}
