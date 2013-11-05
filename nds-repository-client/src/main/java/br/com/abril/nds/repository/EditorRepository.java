package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.model.cadastro.Editor;

public interface EditorRepository extends Repository<Editor, Long> {

	public List<Editor> obterEditores();
	
	public List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroPesquisarHistoricoEditorDTO filtro);
	
	/**
	 * Obtém editor por código
	 * @param codigo
	 * @return Editor
	 */
	public Editor obterPorCodigo(Long codigo);
	
}
