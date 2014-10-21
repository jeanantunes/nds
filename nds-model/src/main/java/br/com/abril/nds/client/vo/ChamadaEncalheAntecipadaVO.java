package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ChamadaEncalheAntecipadaVO implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long numeroEdicao;
	private String codigoProduto;
	private Long codigoChamadaEncalhe;
	private Long idLancamento;
	private Integer codBox;
	
	@Export(label = "Box", exhibitionOrder = 1)
	private String box;
	
	@Export(label = "Cota",alignment = Alignment.CENTER, exhibitionOrder = 2)
	private String numeroCota;
	
	@Export(label = "Nome", exhibitionOrder = 3)
	private String nomeCota;
	
	@Export(label = "Qtde.Exemplares",alignment = Alignment.CENTER, exhibitionOrder = 4)
	private BigInteger qntExemplares;
	
	private boolean recolhimentoFinal;
	
	/**
	 * @return the idLancamento
	 */
	public Long getIdLancamento() {
		return idLancamento;
	}
	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}
	/**
	 * @return the codigoChamdaEncalhe
	 */
	public Long getCodigoChamadaEncalhe() {
		return codigoChamadaEncalhe;
	}
	/**
	 * @param codigoChamdaEncalhe the codigoChamdaEncalhe to set
	 */
	public void setCodigoChamadaEncalhe(Long codigoChamadaEncalhe) {
		this.codigoChamadaEncalhe = codigoChamadaEncalhe;
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
	public BigInteger getQntExemplares() {
		return qntExemplares;
	}
	
	/**
	 * @param qntExemplares the qntExemplares to set
	 */
	public void setQntExemplares(BigInteger qntExemplares) {
		this.qntExemplares = qntExemplares;
	}
	
	public Integer getCodBox() {
		return codBox;
	}
	
	public void setCodBox(Integer codBox) {
		this.codBox = codBox;
	}
	
	public boolean isRecolhimentoFinal() {
		return recolhimentoFinal;
	}
	
	public void setRecolhimentoFinal(boolean recolhimentoFinal) {
		this.recolhimentoFinal = recolhimentoFinal;
	}
}