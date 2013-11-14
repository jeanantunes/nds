package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author InfoA2
 */
public class ConsultaInterfacesDTO implements Serializable {

	private static final long serialVersionUID = -6408390586153149553L;

	//private StatusExecucaoEnum status;
	private String status;
	private String nome;
	private Long id;
	private Long idLogExecucao;
	private String descricao;
	private String nomeArquivo;
	private String extensaoArquivo;
	private Date dataInicio;
	private Long idInterface;
	
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getExtensaoArquivo() {
		return extensaoArquivo;
	}

	public void setExtensaoArquivo(String extensaoArquivo) {
		this.extensaoArquivo = extensaoArquivo;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getIdLogExecucao() {
		return idLogExecucao;
	}

	public void setIdLogExecucao(Long idLogExecucao) {
		this.idLogExecucao = idLogExecucao;
	}

	public Long getIdInterface() {
		return idInterface;
	}

	public void setIdInterface(Long idInterface) {
		this.idInterface = idInterface;
	}
	
}
