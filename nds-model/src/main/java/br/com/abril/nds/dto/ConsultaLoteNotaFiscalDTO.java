package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Data Transfer Object que representa a consulta de um lote de Notas Fiscais.
 * 
 * @author Discover Technology
 *
 */
public class ConsultaLoteNotaFiscalDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8316849144436282133L;

	private Set<Long> idsCotasDestinatarias;
	
	private Intervalo<Date> periodoMovimento;
	
	private PaginacaoVO paginacao;
	
	/**
	 * Construtor padrão.
	 */
	public ConsultaLoteNotaFiscalDTO() {
		
	}

	/**
	 * @return the idsCotasDestinatarias
	 */
	public Set<Long> getIdsCotasDestinatarias() {
		return idsCotasDestinatarias;
	}

	/**
	 * @param idsCotasDestinatarias the idsCotasDestinatarias to set
	 */
	public void setIdsCotasDestinatarias(Set<Long> idsCotasDestinatarias) {
		this.idsCotasDestinatarias = idsCotasDestinatarias;
	}

	/**
	 * @return the periodoMovimento
	 */
	public Intervalo<Date> getPeriodoMovimento() {
		return periodoMovimento;
	}

	/**
	 * @param periodoMovimento the periodoMovimento to set
	 */
	public void setPeriodoMovimento(Intervalo<Date> periodoMovimento) {
		this.periodoMovimento = periodoMovimento;
	}

	/**
	 * @return the paginacao
	 */
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	/**
	 * @param paginacao the paginacao to set
	 */
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

}
