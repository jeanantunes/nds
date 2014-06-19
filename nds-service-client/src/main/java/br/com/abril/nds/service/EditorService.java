package br.com.abril.nds.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.abril.nds.model.cadastro.Editor;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Editor}
 * 
 * @author InfoA2
 */
@Service
public interface EditorService {
	
	public Editor obterEditorPorId(Long idEditor);
	
	public Editor obterEditorPorCodigo(Long codigoEditor);

	public List<Editor> obterEditoresDesc();

    public Editor obterEditorPorFornecedor(Long idFornecedor);

    public Long criarEditorFornecedor(Long codigoFornecedor);

	public List<Editor> obterEditoresPorNomePessoa(String nomeEditor);
	
}