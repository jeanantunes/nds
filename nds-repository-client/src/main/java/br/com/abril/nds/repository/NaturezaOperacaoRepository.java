package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroNaturezaOperacaoDTO;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.TipoEmitente;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;

public interface NaturezaOperacaoRepository extends Repository<NaturezaOperacao, Long> {

	List<NaturezaOperacao> obterNaturezasOperacoes();
	
	/**
	 * Retorna os tipos de notas fiscais baseado no tipo de operação e no tipo de destinatario
	 * @param tipoOperacao
	 * @return
	 */
	List<NaturezaOperacao> obterNaturezasOperacoes(TipoOperacao tipoOperacao);

	NaturezaOperacao obterNaturezaOperacao(GrupoNotaFiscal grupoNotaFiscal);
	
	List<NaturezaOperacao> obterNaturezasOperacoes(TipoOperacao tipoOperacao, TipoUsuarioNotaFiscal tipoDestinatario, TipoUsuarioNotaFiscal tipoEmitente, GrupoNotaFiscal[] grupoNotaFiscal);

	public Integer obterQuantidadeTiposNotasFiscais(FiltroNaturezaOperacaoDTO filtro);
	
	List<NaturezaOperacao> obterNaturezasOperacoesPorTipoAtividadeDistribuidor(TipoAtividade tipoAtividade);
	/**
	 * Obtem tipos de notas fiscais de cotas nao contribuintes por tipo de atividade parametrizdo.
	 * 
	 * @param tipoAtividade Mercantil ou Prestador de Servicos
	 * @return lista tipo nota fiscal
	 */
	public List<NaturezaOperacao> obterNaturezasOperacoesCotasNaoContribuintesPor(TipoAtividade tipoAtividade);
	
	List<NaturezaOperacao> consultarNaturezaOperacao(FiltroNaturezaOperacaoDTO filtro);

	List<NaturezaOperacao> obterTiposNotaFiscal(GrupoNotaFiscal grupoNotaFiscal);
	
	NaturezaOperacao obterNaturezaOperacao(Long idNaturezaOperacao);
	
	NaturezaOperacao obterNaturezaOperacao(TipoAtividade tipoAtividade, TipoEmitente tipoEmitente, TipoDestinatario tipoDestinatario, TipoOperacao tipoOperacao, boolean devolucaoSimbolica, boolean vendaConsignado, Boolean gerarCotaNaoExigeNFe);

	NaturezaOperacao obterNaturezaOperacaoDevolucaoSimbolica(TipoAtividade tipoAtividade, TipoDestinatario tipoDestinatario);

	NaturezaOperacao obterNaturezaOperacaoVendaConsignado(TipoAtividade tipoAtividade, TipoDestinatario tipoDestinatario);

	List<ItemDTO<Long, String>> obterNaturezasOperacoesPorEmitenteDestinatario(TipoEmitente tipoEmitente, TipoDestinatario tipoDestinatario, boolean vendaConsignado, Boolean gerarCotaNaoExigeNFe);
	
	List<NaturezaOperacao> obterNaturezaOperacaoAtividadeDestinatarioEmitente(TipoAtividade tipoAtividade, TipoDestinatario tipoDestinatario, TipoEmitente tipoEmitente, TipoOperacao tipoOperacao);

}