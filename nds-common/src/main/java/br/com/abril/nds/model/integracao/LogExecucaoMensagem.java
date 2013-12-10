package br.com.abril.nds.model.integracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "LOG_EXECUCAO_MENSAGEM")
@SequenceGenerator(name="LOG_EXECUCAO_MENSAGEM_SEQ", initialValue = 1, allocationSize = 1)
public class LogExecucaoMensagem implements Serializable {

	private static final long serialVersionUID = -4902886710115803472L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "LOG_EXECUCAO_MENSAGEM_SEQ")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "LOG_EXECUCAO_ID")
	private LogExecucao logExecucao;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "EVENTO_EXECUCAO_ID", nullable = true)
	private EventoExecucao eventoExecucao;
	
	@Column(name = "NOME_ARQUIVO", length = 50, nullable = false)
	private String nomeArquivo;
	
	@Column(name = "NUMERO_LINHA", nullable = true)
	private Integer numeroLinha;
	
	@Column(name = "MENSAGEM", length = 500, nullable = true)
	private String mensagem;
	
	@Column(name = "MENSAGEM_INFO", length = 500, nullable = true)
	private String mensagemInfo;
	
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

	public EventoExecucao getEventoExecucao() {
		return eventoExecucao;
	}

	public void setEventoExecucao(EventoExecucao eventoExecucao) {
		this.eventoExecucao = eventoExecucao;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Integer getNumeroLinha() {
		return numeroLinha;
	}

	public void setNumeroLinha(Integer numeroLinha) {
		this.numeroLinha = numeroLinha;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getMensagemInfo() {
		return mensagemInfo;
	}

	public void setMensagemInfo(String mensagemInfo) {
		this.mensagemInfo = mensagemInfo;
	}
	
}