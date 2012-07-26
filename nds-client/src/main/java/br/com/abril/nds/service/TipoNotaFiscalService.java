package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface TipoNotaFiscalService {

	List<TipoNotaFiscal> obterTiposNotasFiscais();

	TipoNotaFiscal obterPorId(Long id);

	public List<TipoNotaFiscal> obterTiposNotasFiscais(String cfop, String tipoNota, TipoAtividade tipoAtividade, String  orderBy, Ordenacao ordenacao, int initialResult, int maxResults);

	public List<TipoNotaFiscal> obterTiposNotasFiscais(String cfop, String tipoNota, TipoAtividade tipoAtividade);

	public Long obterQuantidadeTiposNotasFiscais(String cfop, String tipoNota, TipoAtividade tipoAtividade);
	
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

	
	
	List<TipoNotaFiscal> obterTiposNotasFiscaisPorTipoAtividadeDistribuidor(Long idDistribuidor);
}
