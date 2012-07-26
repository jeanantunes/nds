package br.com.abril.nds.repository;

import br.com.abril.nds.model.seguranca.Usuario;

public interface UsuarioRepository extends Repository<Usuario, Long> {

	Usuario getUsuarioImportacao();
}
