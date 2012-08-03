package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.seguranca.GrupoPermissao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.GrupoPermissaoRepository;
import br.com.abril.nds.service.GrupoPermissaoService;

/**
 * @author InfoA2
 */
@Service
public class GrupoPermissaoServiceImpl implements GrupoPermissaoService {

	@Autowired
	GrupoPermissaoRepository grupoPermissaoRepository;
	
	@Transactional(readOnly = true)
	@Override
	public List<Permissao> buscarPermissoesGrupo(Long codigo) {
		return new ArrayList<Permissao>(grupoPermissaoRepository.buscarPorId(codigo).getPermissoes());
	}

	@Override
	public void salvar(GrupoPermissao grupoPermissao) {
		if (grupoPermissao.getId() == null || grupoPermissao.getId() == 0) {
			grupoPermissaoRepository.adicionar(grupoPermissao);
		}
		grupoPermissaoRepository.alterar(grupoPermissao);
	}

}
