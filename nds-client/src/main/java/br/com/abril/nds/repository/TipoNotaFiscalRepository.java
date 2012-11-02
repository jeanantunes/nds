package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroCadastroTipoNotaDTO;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;

public interface TipoNotaFiscalRepository extends Repository<TipoNotaFiscal, Long> {

	List<TipoNotaFiscal> obterTiposNotasFiscais();
	
	/**
	 * Retorna os tipos de notas fiscais baseado no tipo de operação e no tipo de destinatario
	 * @param tipoOperacao
	 * @return
	 */
	List<TipoNotaFiscal> obterTiposNotasFiscais(TipoOperacao tipoOperacao);

	TipoNotaFiscal obterTipoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal);
	
	List<TipoNotaFiscal> obterTiposNotasFiscais(TipoOperacao tipoOperacao, TipoUsuarioNotaFiscal tipoDestinatario, TipoUsuarioNotaFiscal tipoEmitente, GrupoNotaFiscal[] grupoNotaFiscal);

	public Integer obterQuantidadeTiposNotasFiscais(FiltroCadastroTipoNotaDTO filtro);
	
	List<TipoNotaFiscal> obterTiposNotasFiscaisPorTipoAtividadeDistribuidor(TipoAtividade tipoAtividade);
	/**
	 * Obtem tipos de notas fiscais de cotas nao contribuintes por tipo de atividade parametrizdo.
	 * 
	 * @param tipoAtividade Mercantil ou Prestador de Servicos
	 * @return lista tipo nota fiscal
	 */
	public List<TipoNotaFiscal> obterTiposNotasFiscaisCotasNaoContribuintesPor(TipoAtividade tipoAtividade);
	
	List<TipoNotaFiscal> consultarTipoNotaFiscal(FiltroCadastroTipoNotaDTO filtro);
	
	

	List<TipoNotaFiscal> obterTiposNotaFiscal(GrupoNotaFiscal grupoNotaFiscal);
	/**
	 * Obtem tipo de nota fiscal.
	 * 
	 * @param grupoNotaFiscal
	 * @param tipoAtividade
	 * @param isContribuinte
	 * @return TipoNotaFiscal
	 */
	TipoNotaFiscal obterTipoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal, TipoAtividade tipoAtividade, boolean isContribuinte);
	

}
