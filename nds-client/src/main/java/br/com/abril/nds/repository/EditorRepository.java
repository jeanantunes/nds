package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABC;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.model.cadastro.Editor;

public interface EditorRepository {

	public List<Editor> obterEditores();

	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO);
	public ResultadoCurvaABC obterCurvaABCEditorTotal(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO);
	public List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroPesquisarHistoricoEditorDTO filtro);
	
}
