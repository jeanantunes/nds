package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.repository.EditorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.RankingRepository;
import br.com.abril.nds.service.EditorService;

import com.google.common.collect.Sets;

@Service
public class EditorServiceImpl implements EditorService {

	@Autowired
	private EditorRepository editorRepository;
	
	@Autowired
	private RankingRepository rankingRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Override
	@Transactional(readOnly=true)
	public Editor obterEditorPorId(Long idEditor) {
		return editorRepository.buscarPorId(idEditor);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Editor> obterEditoresDesc() {
		
		return this.editorRepository.obterEditoresDesc();
	}

    @Override
    public Editor obterEditorPorFornecedor(Long idFornecedor) {
        
        return this.editorRepository.obterEditorPorFornecedor(idFornecedor);
    }

    @Override
    @Transactional
    public Long criarEditorFornecedor(Long codigoFornecedor) {
        
        Fornecedor fornecedor = fornecedorRepository.buscarPorId(codigoFornecedor);
        
        Editor editor = new Editor();
        editor.setAtivo(true);
        editor.setFornecedores(Sets.newHashSet(fornecedor));
        editor.setOrigemInterface(false);
        editor.setPessoaJuridica(fornecedor.getJuridica());
        editor.setNomeContato(fornecedor.getJuridica().getNome());
        editor.setCodigo(fornecedor.getId());
        
        return editorRepository.merge(editor).getId();
    }

	@Override
	@Transactional
	public List<Editor> obterEditoresPorNomePessoa(String nomeEditor) {
		
		return editorRepository.obterEditoresPorNomePessoa(nomeEditor);
	}

	@Override
	@Transactional
	public Editor obterEditorPorCodigo(Long codigoEditor) {
		
		return editorRepository.obterPorCodigo(codigoEditor);
	}

}