package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Interface de serviços referentes a serviços de chamadade encalhe. 
 *   
 * 
 * @author Discover Technology
 */
public interface EmissaoBandeirasService {

	Long countObterBandeirasDaSemana(Date dataEmissao, Long fornecedor);

	List<BandeirasDTO> obterBandeirasDaSemana(FiltroImpressaoNFEDTO filtroNFE);
	
	List<BandeirasDTO> obterBandeirasDaSemana(Date dataEmissao, Long fornecedor, String numeroNotaDe, String numeroNotaAte, PaginacaoVO paginacaoVO, boolean bandeiraGerada);
	
	byte[] imprimirBandeira(Date dataEmissao, Long forncedor, Date[] dataEnvio, Integer[] numeroPallets,Integer[] numero,Integer[] serie, String numeroNotaDe, String numeroNotaAte) throws Exception;
	
	byte[] imprimirBandeiraManual(String semana, Integer numeroPallets,String fornecedor,
			String praca, String canal, String dataEnvio, String titulo) throws Exception;
		
}