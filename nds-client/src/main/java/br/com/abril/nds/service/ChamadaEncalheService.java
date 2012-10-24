package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.CapaDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.FornecedoresBandeiraDTO;
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

	List<CotaEmissaoDTO> obterDadosImpressaoEmissaoChamadasEncalhe(
			FiltroEmissaoCE filtro);

	List<CapaDTO> obterIdsCapasChamadaEncalhe(Date dtRecolhimentoDe,
			Date dtRecolhimentoAte);
	
	/**
	 * Obtem Produtos com Recolhimento na semana informada
	 * 
	 * @param semana número da semana
	 * @return
	 */
	List<BandeirasDTO> obterBandeirasDaSemana(Integer semana, PaginacaoVO paginacao);
	
	/**
	 * Obtém Dados dos Fornecedores para impressão de Bandeira
	 * 
	 * @param semana - Número da semana
	 * @return
	 */
	List<FornecedoresBandeiraDTO> obterDadosFornecedoresParaImpressaoBandeira(Integer semana);
}
