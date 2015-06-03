package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.vo.PaginacaoVO;



/**
 * Interface de serviços referentes a serviços de chamadade encalhe. 
 *   
 * 
 * @author Discover Technology
 */
public interface EmissaoBandeirasService {

	Long countObterBandeirasDaSemana(Integer semana, Long fornecedor);

	List<BandeirasDTO> obterBandeirasDaSemana(Integer semana, Long fornecedor, PaginacaoVO paginacaoVO);
	
	byte[] imprimirBandeira(Integer semana, Long forncedor, Date[] dataEnvio, Integer[] numeroPallets) throws Exception;
	
	byte[] imprimirBandeiraManual(String semana, Integer numeroPallets,String fornecedor,
			String praca, String canal, String dataEnvio, String titulo) throws Exception;
		
}