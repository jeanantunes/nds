package br.com.abril.nds.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABCEditor;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
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

	public List<Editor> obterEditores();
	
	public Editor obterEditorPorId(Long idEditor);
	

	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO);
	public ResultadoCurvaABCEditor obterCurvaABCEditorTotal(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO);
	public List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroPesquisarHistoricoEditorDTO filtro);
	
}
