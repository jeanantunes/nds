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
	
	@Export(label="Interface")
	private String nome;
	
	@Export(label="Arquivo")
	private String nomeArquivo;
	
	@Export(label="Extensao")
	private String extensaoArquivo;
	
	@Export(label="Descricao")
	private String descricaoInterface;
	
	@Export(label="Status")
	private String status;
	
	@Export(label="Data Processamento")
	private String dataProcessmento;
	
	@Export(label="Hora Processamento")
	private String horaProcessamento;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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