package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface TipoNotaFiscalRepository extends Repository<TipoNotaFiscal, Long> {

	List<TipoNotaFiscal> obterTiposNotasFiscais();
	
	List<TipoNotaFiscal> obterTiposNotasFiscais(TipoOperacao tipoOperacao);

	TipoNotaFiscal obterTipoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal);

	public List<TipoNotaFiscal> obterTiposNotasFiscais(String cfop, String tipoNota, TipoAtividade tipoAtividade, String orderBy, Ordenacao ordenacao, Integer initialResult, Integer maxResults);

	public Long obterQuantidadeTiposNotasFiscais(String cfop, String tipoNota, TipoAtividade tipoAtividade);
	
}
