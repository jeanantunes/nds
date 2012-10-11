package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroCadastroTipoNotaDTO;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;

public interface TipoNotaFiscalRepository extends Repository<TipoNotaFiscal, Long> {

	List<TipoNotaFiscal> obterTiposNotasFiscais();
	
	List<TipoNotaFiscal> obterTiposNotasFiscais(TipoOperacao tipoOperacao);

	TipoNotaFiscal obterTipoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal);

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
}
