package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroCadastroTipoNotaDTO;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;

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
	
	/**
	 * Carrega combo tipo notas ficais de acordo com o tipo de operação, tipo de destinatário, tipo de emitente e grupo denota fiscal
	 * 
	 * @param tipoOperacao
	 * @param tipoDestinatario
	 * @param tipoEmitente
	 * @param grupoNotaFiscal
	 * @return
	 */
	List<ItemDTO<Long, String>> carregarComboTiposNotasFiscais(TipoOperacao tipoOperacao, TipoUsuarioNotaFiscal tipoDestinatario, TipoUsuarioNotaFiscal tipoEmitente, GrupoNotaFiscal[] grupoNotaFiscal);
	
	List<TipoNotaFiscal> obterTiposNotasFiscaisPorTipoAtividadeDistribuidor(Long idDistribuidor);
	
	List<TipoNotaFiscal> consultarTipoNotaFiscal(FiltroCadastroTipoNotaDTO filtro);
}
