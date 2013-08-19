package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ProcessoDTO implements Serializable {

	private static final long serialVersionUID = 1376104032774837151L;

	@Export(label="Processos")
	private String nome;

	@Export(label="Status")
	private String status;

	@Export(label="Data Processamento")
	private String dataProcessmento;
	
	@Export(label="Hora Processamento")
	private String horaProcessamento;

	private boolean isSistemaOperacional;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public boolean isSistemaOperacional() {
		return isSistemaOperacional;
	}

	public void setSistemaOperacional(boolean isSistemaOperacional) {
		this.isSistemaOperacional = isSistemaOperacional;
	}
	
}
