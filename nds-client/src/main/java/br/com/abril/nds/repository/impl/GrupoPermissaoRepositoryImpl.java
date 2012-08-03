package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.seguranca.GrupoPermissao;
import br.com.abril.nds.repository.GrupoPermissaoRepository;

@Repository
public class GrupoPermissaoRepositoryImpl extends AbstractRepositoryModel<GrupoPermissao,Long> implements GrupoPermissaoRepository {

	/**
	 * Construtor padrão
	 */
	public GrupoPermissaoRepositoryImpl() {
		super(GrupoPermissao.class);
	}

}
