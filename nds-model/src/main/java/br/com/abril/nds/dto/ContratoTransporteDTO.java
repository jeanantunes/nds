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

}
