package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroCadastroTipoNotaDTO;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;

public interface TipoNotaFiscalService {

	TipoNotaFiscal obterPorId(Long id);

	public Integer obterQuantidadeTiposNotasFiscais(FiltroCadastroTipoNotaDTO filtro);
	
	/**
	 * Obtem o proximo numero do Documento fiscal com base na serie
	 * @param serie
	 * @return
	 */
	public Long proximoNumeroDocumentoFiscal(int serie);
	
	/**
	 * Carrega combo tipo notas ficais de acordo com o tipo de atividade do distribuidor
	 * 
	 * @return lista de itens para combo
	 */
	public abstract List<ItemDTO<Long, String>> carregarComboTiposNotasFiscais(TipoAtividade tipoAtividade);
	
	/**
	 * Carrega combo tipo notas ficais de acordo com o tipo de operação
	 * @param tipoOperacao
	 * @return
	 */
	List<ItemDTO<Long, String>> carregarComboTiposNotasFiscais(TipoOperacao tipoOperacao);
	
	List<TipoNotaFiscal> obterTiposNotasFiscaisPorTipoAtividadeDistribuidor(Long idDistribuidor);
	
	List<TipoNotaFiscal> consultarTipoNotaFiscal(FiltroCadastroTipoNotaDTO filtro);
}
