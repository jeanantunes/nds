package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.FinanceiroVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
public interface FinanceiroService {

	
	
    /**
     * Obtém parametros de Cobranca da cota
     * @param idCota
     * @return Value Object com parametros de Cobranca da cota 
     */
    FinanceiroVO obterDadosCotaCobranca(Long idCota);
    
    
    /**
	 * Método responsável por obter bancos relacionados ao tipo de cobrança para preencher combo da camada view
	 * @param Tipo de cobrança
	 * @return comboBancos: bancos cadastrados relacionados ao tipo de cobrança
	 */
    List<ItemDTO<Integer, String>> getComboBancosTipoCobranca(TipoCobranca tipoCobranca);
    
    /**
	 * Método responsável por obter formas de cobrança para preencher combo da camada view
	 * @return comboTiposPagamento: formas de cobrança cadastrados
	 */
    List<ItemDTO<TipoCobranca, String>> getComboTiposCobranca();
    
    
    /**
     * Posta parametros de Cobranca da cota
     * @param Value Object com parametros de Cobranca da cota 
     */
    void postarDadosCotaCobranca(FinanceiroVO cotaCobranca);

	
    
    
    
    /**
     * !!!
     * @return
     */
    List<FormaCobranca> obterFormasCobrancaPorCota(/*FiltroConsultaFormasCobrancaCotaDTO filtro*/);

}
