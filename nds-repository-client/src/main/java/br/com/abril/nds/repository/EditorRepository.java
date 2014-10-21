package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Editor;

public interface EditorRepository extends Repository<Editor, Long> {
	
	/**
	 * Obtém editor por código
	 * @param codigo
	 * @return Editor
	 */
	public Editor obterPorCodigo(Long codigo);

	public List<Editor> obterEditoresDesc();

    public Editor obterEditorPorFornecedor(Long idFornecedor);

	public List<Editor> obterEditoresPorNomePessoa(String nomeEditor);
	
}