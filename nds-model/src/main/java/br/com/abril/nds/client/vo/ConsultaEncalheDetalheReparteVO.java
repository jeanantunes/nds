package br.com.abril.nds.client.vo;

public class ConsultaEncalheDetalheReparteVO {

	private String numeroCota;
	
	private String nomeCota;
	
	private Long  reparte;
	
	private Long encalhe;
	
	
	public Long getReparte() {
		return reparte;
	}

	public void setReparte(Long reparte) {
		this.reparte = reparte;
	}

	private Long idBox;
	
	private String nomeBox;


	

	public Long getIdBox() {
		return idBox;
	}

	public void setIdBox(Long idBox) {
		this.idBox = idBox;
	}

	/**
	 * @return the numeroCota
	 */
	public String getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(String numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getNomeBox() {
		return nomeBox;
	}

	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

	public Long getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(Long encalhe) {
		this.encalhe = encalhe;
	}

	
}
