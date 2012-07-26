package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class ChamadaEncalheAntecipadaVO implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long numeroEdicao;
	private String codigoProduto;
	private Long codigoChamdaEncalhe;
	
	@Export(label = "Box", exhibitionOrder = 1)
	private String box;
	
	@Export(label = "Cota",alignment = Alignment.CENTER, exhibitionOrder = 2)
	private String numeroCota;
	
	@Export(label = "Nome", exhibitionOrder = 3)
	private String nomeCota;
	
	@Export(label = "Qtde.Exemplares",alignment = Alignment.CENTER, exhibitionOrder = 4)
	private String qntExemplares;
	
	/**
	 * @return the codigoChamdaEncalhe
	 */
	public Long getCodigoChamdaEncalhe() {
		return codigoChamdaEncalhe;
	}
	/**
	 * @param codigoChamdaEncalhe the codigoChamdaEncalhe to set
	 */
	public void setCodigoChamdaEncalhe(Long codigoChamdaEncalhe) {
		this.codigoChamdaEncalhe = codigoChamdaEncalhe;
	}
	/**
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}
	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the box
	 */
	public String getBox() {
		return box;
	}
	/**
	 * @param box the box to set
	 */
	public void setBox(String box) {
		this.box = box;
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
	/**
	 * @return the qntExemplares
	 */
	public String getQntExemplares() {
		return qntExemplares;
	}
	/**
	 * @param qntExemplares the qntExemplares to set
	 */
	public void setQntExemplares(String qntExemplares) {
		this.qntExemplares = qntExemplares;
	}
	
	
	
}
