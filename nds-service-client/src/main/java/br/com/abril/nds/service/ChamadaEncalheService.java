package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.DadosImpressaoEmissaoChamadaEncalhe;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.vo.PaginacaoVO;


/**
 * Interface de serviços referentes a serviços de chamadade encalhe. 
 *   
 * 
 * @author Discover Technology
 */
public interface ChamadaEncalheService {
	
	List<CotaEmissaoDTO> obterDadosEmissaoChamadasEncalhe(FiltroEmissaoCE filtro);

	DadosImpressaoEmissaoChamadaEncalhe obterDadosImpressaoEmissaoChamadasEncalhe(
			FiltroEmissaoCE filtro);

	

	public List<Integer> obterCotasComOperacaoDiferenciada(FiltroEmissaoCE filtro);


	public List<CotaEmissaoDTO> obterCotasSemOperacaoDiferenciada(FiltroEmissaoCE filtro);
	
	
	/**
	 * Obtem Produtos com Recolhimento na semana informada
	 * 
	 * @param semana número da semana
	 * @param fornecedor
	 * @return
	 */
	List<BandeirasDTO> obterBandeirasDaSemana(Integer semana, Long forncedor, PaginacaoVO paginacao);
	
	/**
	 * Obtém Dados dos Fornecedores para impressão de Bandeira
	 * 
	 * @param semana - Número da semana
	 * @param fornecedor TODO
	 * @return
	 */
	List<FornecedorDTO> obterDadosFornecedoresParaImpressaoBandeira(Integer semana, Long fornecedor);

	/**
	 * Obter Count de "obterBandeirasDaSemana"
	 * 
	 * @param semana
	 * @param fornecedor TODO
	 * @return
	 */
	Long countObterBandeirasDaSemana(Integer semana, Long fornecedor);

//	byte[] gerarEmissaoCE(FiltroEmissaoCE filtro) throws JRException, URISyntaxException;
	
}
