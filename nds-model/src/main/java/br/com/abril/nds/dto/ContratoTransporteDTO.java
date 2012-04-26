package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;



/**
 * DTO Contrato de prestação de serviços de transporte de revistas.
 * @author Discover
 *
 */
public class ContratoTransporteDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2646577588436107858L;
	
	private PessoaContratoDTO contratante;
	
	private PessoaContratoDTO contratada;
	
	private Date inicio;
	private Date termino;
	private String prazo;
	private String avisoPrevio;
	
	public String getPrazo() {
		return prazo;
	}
	public void setPrazo(String prazo) {
		this.prazo = prazo;
	}
	public String getAvisoPrevio() {
		return avisoPrevio;
	}
	public void setAvisoPrevio(String avisoPrevio) {
		this.avisoPrevio = avisoPrevio;
	}
	public PessoaContratoDTO getContratante() {
		return contratante;
	}
	public void setContratante(PessoaContratoDTO contratante) {
		this.contratante = contratante;
	}
	public PessoaContratoDTO getContratada() {
		return contratada;
	}
	public void setContratada(PessoaContratoDTO contratada) {
		this.contratada = contratada;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getTermino() {
		return termino;
	}
	public void setTermino(Date termino) {
		this.termino = termino;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((avisoPrevio == null) ? 0 : avisoPrevio.hashCode());
		result = prime * result
				+ ((contratada == null) ? 0 : contratada.hashCode());
		result = prime * result
				+ ((contratante == null) ? 0 : contratante.hashCode());
		result = prime * result + ((inicio == null) ? 0 : inicio.hashCode());
		result = prime * result + ((prazo == null) ? 0 : prazo.hashCode());
		result = prime * result + ((termino == null) ? 0 : termino.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContratoTransporteDTO other = (ContratoTransporteDTO) obj;
		if (avisoPrevio == null) {
			if (other.avisoPrevio != null)
				return false;
		} else if (!avisoPrevio.equals(other.avisoPrevio))
			return false;
		if (contratada == null) {
			if (other.contratada != null)
				return false;
		} else if (!contratada.equals(other.contratada))
			return false;
		if (contratante == null) {
			if (other.contratante != null)
				return false;
		} else if (!contratante.equals(other.contratante))
			return false;
		if (inicio == null) {
			if (other.inicio != null)
				return false;
		} else if (!inicio.equals(other.inicio))
			return false;
		if (prazo == null) {
			if (other.prazo != null)
				return false;
		} else if (!prazo.equals(other.prazo))
			return false;
		if (termino == null) {
			if (other.termino != null)
				return false;
		} else if (!termino.equals(other.termino))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ContratoTransporteDTO [contratante=" + contratante
				+ ", contratada=" + contratada + ", inicio=" + inicio
				+ ", termino=" + termino + ", prazo=" + prazo
				+ ", avisoPrevio=" + avisoPrevio + "]";
	}
	
	

}
