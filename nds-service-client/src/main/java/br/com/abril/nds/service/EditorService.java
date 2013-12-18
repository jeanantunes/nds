package br.com.abril.nds.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
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
	
	public List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroPesquisarHistoricoEditorDTO filtro);

	public List<Editor> obterEditoresDesc();
}