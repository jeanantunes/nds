package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author InfoA2
 */
public class ConsultaInterfacesDTO implements Serializable {

	private String status;
	private String nome;
	private Long id;
	private String nomeArquivo;
	private Date dataInicio;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

}
