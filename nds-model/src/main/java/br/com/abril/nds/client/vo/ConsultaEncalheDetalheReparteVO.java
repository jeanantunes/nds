package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;





@Exportable
public class ConsultaEncalheDetalheReparteVO {


	@Export(label="Cota")
	private String numeroCota;
	
	@Export(label="Nome Cota")
	private String nomeCota;
	
	@Export(label="Reparte")
	private Long  reparte;
	
	@Export(label="Encalhe")
	private Long encalhe;
	
	@Export(label="Box")
	private Long idBox;
	
	@Export(label="Nome Box")
	private String nomeBox;
	
	
	public Long getReparte() {
		return reparte;
	}

	public void setReparte(Long reparte) {
		this.reparte = reparte;
	}

	


	

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
