package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class InterfaceDTO implements Serializable {

	private static final long serialVersionUID = 7870636987847534600L;

	private String idInterface;
	
	private String idLogProcessamento;
	
	private Long idLogExecucao;
	
	private String nome;
	
	@Export(label="Arquivo", exhibitionOrder=2)
	private String nomeArquivo;
	
	@Export(label="Extensao", exhibitionOrder=3)
	private String extensaoArquivo;

	@Export(label="Interface", exhibitionOrder=1, widthPercent=40)
	private String descricaoInterface;
	
	private Status status;
	
	@Export(label="Data Processamento",exhibitionOrder=5)
	private String dataProcessmento;
	
	@Export(label="Hora Processamento",exhibitionOrder=6)
	private String horaProcessamento;
	
	public enum Status {
		S("Realizado com Sucesso"),
		A("Realizado com sucesso / Alerta"),
		F("Processado com erro"),
		V("Sem dados a processar"),
		E("Não processado");
		
		private String descricao;
		
		Status(String descricao) {
			this.descricao = descricao;
		}
		
		public String getDescricao() {
			return this.descricao;
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getExtensaoArquivo() {
		return extensaoArquivo;
	}

	public void setExtensaoArquivo(String extensaoArquivo) {
		this.extensaoArquivo = extensaoArquivo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Export(label="Status", exhibitionOrder=4, widthPercent=20)
	public String getStatusDescricao() {
		return this.status == null ? Status.V.getDescricao() : this.status.getDescricao();
	}

	public String getDataProcessmento() {
		return dataProcessmento;
	}

	public void setDataProcessmento(String dataProcessmento) {
		this.dataProcessmento = dataProcessmento;
	}

	public String getHoraProcessamento() {
		return horaProcessamento;
	}

	public void setHoraProcessamento(String horaProcessamento) {
		this.horaProcessamento = horaProcessamento;
	}

	public String getIdLogProcessamento() {
		return idLogProcessamento;
	}

	public void setIdLogProcessamento(String idLogProcessamento) {
		this.idLogProcessamento = idLogProcessamento;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getDescricaoInterface() {
		return descricaoInterface;
	}

	public void setDescricaoInterface(String descricaoInterface) {
		this.descricaoInterface = descricaoInterface;
	}

	public Long getIdLogExecucao() {
		return idLogExecucao;
	}

	public void setIdLogExecucao(Long idLogExecucao) {
		this.idLogExecucao = idLogExecucao;
	}

	public String getIdInterface() {
		return idInterface;
	}

	public void setIdInterface(String idInterface) {
		this.idInterface = idInterface;
	}

}