package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroCadastroTipoNotaDTO;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
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
	
	List<NaturezaOperacao> obterTiposNotasFiscais(TipoOperacao tipoOperacao, TipoUsuarioNotaFiscal tipoDestinatario, TipoUsuarioNotaFiscal tipoEmitente, GrupoNotaFiscal[] grupoNotaFiscal);

	public Integer obterQuantidadeTiposNotasFiscais(FiltroCadastroTipoNotaDTO filtro);
	
	List<NaturezaOperacao> obterTiposNotasFiscaisPorTipoAtividadeDistribuidor(TipoAtividade tipoAtividade);
	/**
	 * Obtem tipos de notas fiscais de cotas nao contribuintes por tipo de atividade parametrizdo.
	 * 
	 * @param tipoAtividade Mercantil ou Prestador de Servicos
	 * @return lista tipo nota fiscal
	 */
	public List<NaturezaOperacao> obterTiposNotasFiscaisCotasNaoContribuintesPor(TipoAtividade tipoAtividade);
	
	List<NaturezaOperacao> consultarTipoNotaFiscal(FiltroCadastroTipoNotaDTO filtro);
	
	

	List<NaturezaOperacao> obterTiposNotaFiscal(GrupoNotaFiscal grupoNotaFiscal);
	/**
	 * Obtem tipo de nota fiscal.
	 * 
	 * @param grupoNotaFiscal
	 * @param tipoAtividade
	 * @param isContribuinte
	 * @return TipoNotaFiscal
	 */
	NaturezaOperacao obterNaturezaOperacao(GrupoNotaFiscal grupoNotaFiscal, TipoAtividade tipoAtividade, boolean isContribuinte);
	
	NaturezaOperacao obterNaturezaOperacao(Long idNaturezaOperacao);
}
