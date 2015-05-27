package br.com.abril.nds.model.integracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "LOG_EXECUCAO_ARQUIVO")
@SequenceGenerator(name="LOG_EXECUCAO_ARQUIVO_SEQ", initialValue = 1, allocationSize = 1)
public class LogExecucaoArquivo implements Serializable {

	private static final long serialVersionUID = 1926815327934975236L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "LOG_EXECUCAO_ARQUIVO_SEQ")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "LOG_EXECUCAO_ID")
	private LogExecucao logExecucao;
	
	@Column(name = "DISTRIBUIDOR_ID", nullable = false, length = 7)
	private Integer distribuidor;
	
	@Type(type = "br.com.abril.nds.integracao.persistence.GenericEnumUserType", 
		parameters = {
			@Parameter( name="enumClass", value="br.com.abril.nds.model.integracao.StatusExecucaoEnum" )
		})
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 1)
	private StatusExecucaoEnum status;
	
	@Column(name = "CAMINHO_ARQUIVO", length = 350)
	private String caminhoArquivo;
	
	@Column(name = "MENSAGEM", length = 500)
	private String mensagem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LogExecucao getLogExecucao() {
		return logExecucao;
	}

	public void setLogExecucao(LogExecucao logExecucao) {
		this.logExecucao = logExecucao;
	}
	
	public Integer getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Integer distribuidor) {
		this.distribuidor = distribuidor;
	}

	public StatusExecucaoEnum getStatus() {
		return status;
	}

	public void setStatus(StatusExecucaoEnum status) {
		this.status = status;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
