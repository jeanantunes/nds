package br.com.abril.nds.dto.filtro;

import java.util.Date;

public class FiltroRelatorioServicosEntregaDTO extends FiltroDTO {
	
	private static final long serialVersionUID = -7026602786415557640L;
	
	private Date entregaDataInicio;
	private Date entregaDataFim;
	private Long idTransportador;
	private Long idCota;
	
	public Date getEntregaDataInicio() {
		return entregaDataInicio;
	}
	public void setEntregaDataInicio(Date entregaDataInicio) {
		this.entregaDataInicio = entregaDataInicio;
	}
	public Date getEntregaDataFim() {
		return entregaDataFim;
	}
	public void setEntregaDataFim(Date entregaDataFim) {
		this.entregaDataFim = entregaDataFim;
	}
	public Long getIdTransportador() {
		return idTransportador;
	}
	public void setIdTransportador(Long idTransportador) {
		this.idTransportador = idTransportador;
	}
	public Long getIdCota() {
		return idCota;
	}
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
}
