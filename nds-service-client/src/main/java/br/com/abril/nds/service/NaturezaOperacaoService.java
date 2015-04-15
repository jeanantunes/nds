package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroNaturezaOperacaoDTO;
import br.com.abril.nds.model.cadastro.NotaFiscalTipoEmissao.NotaFiscalTipoEmissaoEnum;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.TipoEmitente;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;

public interface NaturezaOperacaoService {

	NaturezaOperacao obterPorId(Long id);

	Integer obterQuantidadeNaturezasOperacoes(FiltroNaturezaOperacaoDTO filtro);
	
	/**
	 * Obtem o proximo numero do Documento fiscal com base na serie
	 * @param serie
	 * @return
	 */
	Long proximoNumeroDocumentoFiscal(int serie);
	
	/**
	 * Carrega combo tipo notas ficais de acordo com o tipo de atividade do distribuidor
	 * 
	 * @return lista de itens para combo
	 */
	List<ItemDTO<Long, String>> carregarComboNaturezasOperacoes(TipoAtividade tipoAtividade);
	
	/**
	 * Carrega combo tipo notas ficais de acordo com o tipo de operação
	 * @param tipoOperacao
	 * @return
	 */
	List<ItemDTO<Long, String>> carregarComboNaturezasOperacoes(TipoOperacao tipoOperacao);
	
	/**
	 * Carrega combo tipo notas ficais de acordo com o tipo de operação, tipo de destinatário, tipo de emitente e grupo denota fiscal
	 * 
	 * @param tipoOperacao
	 * @param tipoDestinatario
	 * @param tipoEmitente
	 * @param grupoNotaFiscal
	 * @return
	 */
	List<ItemDTO<Long, String>> carregarComboNaturezasOperacoes(TipoOperacao tipoOperacao, TipoUsuarioNotaFiscal tipoDestinatario, TipoUsuarioNotaFiscal tipoEmitente, GrupoNotaFiscal[] grupoNotaFiscal);
	
	List<NaturezaOperacao> obterNaturezasOperacoesPorTipoAtividadeDistribuidor();
	
	List<NaturezaOperacao> consultarNaturezasOperacoes(FiltroNaturezaOperacaoDTO filtro);

	List<NaturezaOperacao> obterNaturezasOperacoes(TipoAtividade tipoAtividade);
	
	NaturezaOperacao obterNaturezaOperacaoPorId(Long idNaturezaOperacao);

	NaturezaOperacao obterNaturezaOperacao(TipoAtividade tipoAtividade, TipoEmitente tipoEmitente, TipoDestinatario tipoDestinatario, TipoOperacao tipoOperacao);

	/**
	 * Metodo especifico para obter a natureza de operacao de devolucao simbolica por destinatario
	 * pois nao pode haver mais de uma
	 * 
	 * @param tipoDestinatario
	 * @return
	 */
	NaturezaOperacao obterNaturezaOperacaoDevolucaoSimbolica(TipoDestinatario tipoDestinatario);
	
	/**
	 * Metodo especifico para obter a natureza de operacao de venda de consignado por destinatario
	 * pois nao pode haver mais de uma
	 * 
	 * @param tipoDestinatario
	 * @return
	 */
	NaturezaOperacao obterNaturezaOperacaoVendaConsignado(TipoDestinatario tipoDestinatario);

	List<ItemDTO<Long, String>> obterNaturezasOperacoesPorEmitenteDestinatario(TipoEmitente tipoEmitente, TipoDestinatario tipoDestinatario, boolean vendaConsignado, Boolean gerarCotaNaoExigeNFe);

	NotaFiscalTipoEmissaoEnum verificarRegimeEspecialNaturezaOperacao(Long naturezaOperacaoId);
	
}