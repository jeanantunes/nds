package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.UsuarioRepository;

@Repository
public class UsuarioRepositoryImpl extends AbstractRepositoryModel<Usuario, Long> implements UsuarioRepository{

	public UsuarioRepositoryImpl() {
		super(Usuario.class);
	}
	

	//TODO Definição de Usuario de Importação
	@Transactional
	public Usuario getUsuarioImportacao() {
				
		Usuario usuario = new Usuario();
		usuario.setLogin("usuarioImportacao");
		usuario.setNome("Usuário de Importação");
		usuario.setSenha("usuarioImportacao");
		
		return merge(usuario);
	}
	

}
