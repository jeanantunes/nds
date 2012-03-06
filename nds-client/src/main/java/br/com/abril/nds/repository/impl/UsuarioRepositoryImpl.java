package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.UsuarioRepository;

@Repository
public class UsuarioRepositoryImpl extends AbstractRepository<Usuario, Long> implements UsuarioRepository{

	public UsuarioRepositoryImpl() {
		super(Usuario.class);
	}

}
