package br.com.abril.nds.model.seguranca;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "CONFERENCIA_ENCALHE_COTA_USUARIO")
public class ConferenciaEncalheCotaUsuario implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7262170994750001146L;

	@Id
	@Column(name="NUMERO_COTA", nullable = false, unique = true)
	private Integer numeroCota;
	
	@Column(name="LOGIN", nullable = false, unique = true)
	private String login;

	@Column(name="NOME_USUARIO")
	private String nomeUsuario;
	
	@Column(name="SESSION_ID", nullable = false, unique = true)
	private String sessionId;
	
	public ConferenciaEncalheCotaUsuario() {
		super();
	}
	
	public ConferenciaEncalheCotaUsuario(String login, String nomeUsuario, String sessionId, Integer numeroCota) throws IllegalArgumentException {
		this.login = login;
		this.nomeUsuario = nomeUsuario;
		this.sessionId = sessionId;
		this.numeroCota = numeroCota;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
}