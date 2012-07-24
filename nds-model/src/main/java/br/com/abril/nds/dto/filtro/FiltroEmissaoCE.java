package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroEmissaoCE implements Serializable {

	private static final long serialVersionUID = -701275770998729277L;

	@Export(label="Dt. Recolhimento:")
	private Date dtRecolhimentoDe;
	@Export(label="Até:")
	private Date dtRecolhimentoAte;
	@Export(label=" Intervalor Box:")
	private Long idBoxDe;
	@Export(label="Até:")
	private Long idBoxAte;
	@Export(label="Cota:")
	private Integer numCotaDe;
	@Export(label="Até:")
	private Integer numCotaAte;
	@Export(label="Roteiro:")
	private Long idRoteiro;
	@Export(label="Rota:")
	private Long idRota; 
	private Boolean capa;
	private Boolean personalizada;
	private List<Long> fornecedores;
	private String colunaOrdenacao;
	private String ordenacao;
	
	public enum ColunaOrdenacao {

		COTA("numCota"),
		NOME("nomeCota"),
		EXEMPLARES("qtdeExemplares"),
		VALOR("vlrTotalCe");
		
		private String nomeColuna;
		
		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacao getPorDescricao(String descricao) {
			for(ColunaOrdenacao coluna: ColunaOrdenacao.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}
	
	
	/**
	 * @return the dtRecolhimentoDe
	 */
	public Date getDtRecolhimentoDe() {
		return dtRecolhimentoDe;
	}
	/**
	 * @param dtRecolhimentoDe the dtRecolhimentoDe to set
	 */
	public void setDtRecolhimentoDe(Date dtRecolhimentoDe) {
		this.dtRecolhimentoDe = dtRecolhimentoDe;
	}
	/**
	 * @return the dtRecolhimentoAte
	 */
	public Date getDtRecolhimentoAte() {
		return dtRecolhimentoAte;
	}
	/**
	 * @param dtRecolhimentoAte the dtRecolhimentoAte to set
	 */
	public void setDtRecolhimentoAte(Date dtRecolhimentoAte) {
		this.dtRecolhimentoAte = dtRecolhimentoAte;
	}
	/**
	 * @return the idBoxDe
	 */
	public Long getIdBoxDe() {
		return idBoxDe;
	}
	/**
	 * @param idBoxDe the idBoxDe to set
	 */
	public void setIdBoxDe(Long idBoxDe) {
		this.idBoxDe = idBoxDe;
	}
	/**
	 * @return the idBoxAte
	 */
	public Long getIdBoxAte() {
		return idBoxAte;
	}
	/**
	 * @param idBoxAte the idBoxAte to set
	 */
	public void setIdBoxAte(Long idBoxAte) {
		this.idBoxAte = idBoxAte;
	}
	/**
	 * @return the numCotaDe
	 */
	public Integer getNumCotaDe() {
		return numCotaDe;
	}
	/**
	 * @param numCotaDe the numCotaDe to set
	 */
	public void setNumCotaDe(Integer numCotaDe) {
		this.numCotaDe = numCotaDe;
	}
	/**
	 * @return the numCotaAte
	 */
	public Integer getNumCotaAte() {
		return numCotaAte;
	}
	/**
	 * @param numCotaAte the numCotaAte to set
	 */
	public void setNumCotaAte(Integer numCotaAte) {
		this.numCotaAte = numCotaAte;
	}
	/**
	 * @return the idRoteiro
	 */
	public Long getIdRoteiro() {
		return idRoteiro;
	}
	/**
	 * @param idRoteiro the idRoteiro to set
	 */
	public void setIdRoteiro(Long idRoteiro) {
		this.idRoteiro = idRoteiro;
	}
	/**
	 * @return the idRota
	 */
	public Long getIdRota() {
		return idRota;
	}
	/**
	 * @param idRota the idRota to set
	 */
	public void setIdRota(Long idRota) {
		this.idRota = idRota;
	}
	/**
	 * @return the capa
	 */
	public Boolean getCapa() {
		return capa;
	}
	/**
	 * @param capa the capa to set
	 */
	public void setCapa(Boolean capa) {
		this.capa = capa;
	}
	/**
	 * @return the personalizada
	 */
	public Boolean getPersonalizada() {
		return personalizada;
	}
	/**
	 * @param personalizada the personalizada to set
	 */
	public void setPersonalizada(Boolean personalizada) {
		this.personalizada = personalizada;
	}
	/**
	 * @return the fornecedores
	 */
	public List<Long> getFornecedores() {
		return fornecedores;
	}
	/**
	 * @param fornecedores the fornecedores to set
	 */
	public void setFornecedores(List<Long> fornecedores) {
		this.fornecedores = fornecedores;
	}
	/**
	 * @return the colunaOrdenacao
	 */
	public String getColunaOrdenacao() {
		return colunaOrdenacao;
	}
	/**
	 * @param colunaOrdenacao the colunaOrdenacao to set
	 */
	public void setColunaOrdenacao(String colunaOrdenacao) {
		this.colunaOrdenacao = colunaOrdenacao;
	}
	/**
	 * @return the ordenacao
	 */
	public String getOrdenacao() {
		return ordenacao;
	}
	/**
	 * @param ordenacao the ordenacao to set
	 */
	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}
	
	
	
}
