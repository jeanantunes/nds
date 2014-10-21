package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoEditor;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do editor
 * 
 * @author Discover Technology
 */
public interface HistoricoDescontoEditorRepository extends Repository<HistoricoDescontoEditor, Long> {

	/**
	 * Retorna os descontos do editor e particulares dados aos editores
	 * @param filtro - filtro de cosnulta
	 * @return List<DescontoDistribuidor> 
	 */
	List<TipoDescontoDTO> buscarDescontos(FiltroTipoDescontoDTO filtro);
	
	/**
	 * Retorna o ultimo desconto valido do distribuidor a um editor
	 * 
	 * @param idUltimoDesconto - id do desconto a ser excluido
	 * @param editor - editor
	 * 
	 * @return HistoricoDescontoEditor
	 */
	HistoricoDescontoEditor buscarUltimoDescontoValido(Editor editor);
	
	/**
	 * Retorna o desconto do editor
	 * 
	 * @param desconto
	 * @param editor
	 * @return
	 */
	HistoricoDescontoEditor buscarHistoricoDescontoEditorPor(Desconto desconto, Editor editor);
	
}