package br.com.abril.nds.client.vo;

import java.io.File;
import java.util.Date;

public class ContratoVO {

	private File tempFile;
	
	private Date dataInicio;
	
	private Date dataTermino;
	
	private boolean recebido;

	private Long idCota;
	
	public ContratoVO() {}
	
	public ContratoVO(Date dataInicio, Date dataTermino, boolean recebido,
			Long idCota) {
		super();
		this.dataInicio = dataInicio;
		this.dataTermino = dataTermino;
		this.recebido = recebido;
		this.idCota = idCota;
	}



	public File getTempFile() {
		return tempFile;
	}

	public void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public boolean isRecebido() {
		return recebido;
	}

	public void setRecebido(boolean recebido) {
		this.recebido = recebido;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
	
}
