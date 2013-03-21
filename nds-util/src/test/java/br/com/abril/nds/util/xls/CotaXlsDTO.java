package br.com.abril.nds.util.xls;

import java.io.Serializable;

import br.com.abril.nds.util.upload.XlsMapper;

/**
 * @author Thiago
 * Bean de exemplo, com fields anotados, contemplando as colunas do XLS de exemplo.
 */
public class CotaXlsDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 558490138154709974L;

	@XlsMapper(value = "cotaID")
	private long idCota;
	
	@XlsMapper(value = "nomeCota")
	private String nomeCota;
	
	public long getIdCota() {
		return idCota;
	}
	public void setIdCota(long idCota) {
		this.idCota = idCota;
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
}
