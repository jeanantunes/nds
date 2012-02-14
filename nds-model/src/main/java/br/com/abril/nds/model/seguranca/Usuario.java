package br.com.abril.nds.model.seguranca;

import java.util.List;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public class Usuario {

	private Long id;
	private String nome;
	public List<PerfilUsuario> perfilUsuario;

	public Usuario(){

	}

}