package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroEmissaoCE implements Serializable {

	private static final long serialVersionUID = -701275770998729277L;

	private List<Integer> cotasOperacaoDiferenciada;
	
	private TipoImpressaoCE tipoImpressao;
	
	@Export(label="Dt. Recolhimento:")
	private Date dtRecolhimentoDe;
	@Export(label="Até:")
	private Date dtRecolhimentoAte;
	@Export(label=" Intervalor Box:")
	private Integer codigoBoxDe;
	@Export(label="Até:")
	private Integer codigoBoxAte;
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
	
	/**
	 * Quantidade de produtos por página
	 */
	private int qtdProdutosPorPagina;
			
	/**
	 * Quantidade de capas por página		
	 */
	private int qtdCapasPorPagina;
	
	/**
	 * Quantidade máxima de produtos em uma pagina que ira comportar 
	 * tambem a grid com totalização
	 */
	private int qtdMaximaProdutosComTotalizacao;
	
	
	
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

	public Integer getCodigoBoxDe() {
		return codigoBoxDe;
	}
	
	public void setCodigoBoxDe(Integer codigoBoxDe) {
		this.codigoBoxDe = codigoBoxDe;
	}
	
	public Integer getCodigoBoxAte() {
		return codigoBoxAte;
	}
	
	public void setCodigoBoxAte(Integer codigoBoxAte) {
		this.codigoBoxAte = codigoBoxAte;
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
	
	/**
	 * @return the qtdProdutosPorPagina
	 */
	public int getQtdProdutosPorPagina() {
		return qtdProdutosPorPagina;
	}
	
	/**
	 * @param qtdProdutosPorPagina the qtdProdutosPorPagina to set
	 */
	public void setQtdProdutosPorPagina(int qtdProdutosPorPagina) {
		this.qtdProdutosPorPagina = qtdProdutosPorPagina;
	}
	
	/**
	 * @return the qtdCapasPorPagina
	 */
	public int getQtdCapasPorPagina() {
		return qtdCapasPorPagina;
	}
	
	/**
	 * @param qtdCapasPorPagina the qtdCapasPorPagina to set
	 */
	public void setQtdCapasPorPagina(int qtdCapasPorPagina) {
		this.qtdCapasPorPagina = qtdCapasPorPagina;
	}
	
	/**
	 * @return the qtdMaximaProdutosComTotalizacao
	 */
	public int getQtdMaximaProdutosComTotalizacao() {
		return qtdMaximaProdutosComTotalizacao;
	}
	
	/**
	 * @param qtdMaximaProdutosComTotalizacao the qtdMaximaProdutosComTotalizacao to set
	 */
	public void setQtdMaximaProdutosComTotalizacao(
			int qtdMaximaProdutosComTotalizacao) {
		this.qtdMaximaProdutosComTotalizacao = qtdMaximaProdutosComTotalizacao;
	}
	
	public TipoImpressaoCE getTipoImpressao() {
		return tipoImpressao;
	}
	
	public void setTipoImpressao(TipoImpressaoCE tipoImpressao) {
		this.tipoImpressao = tipoImpressao;
	}
	
	public List<Integer> getCotasOperacaoDiferenciada() {
		return cotasOperacaoDiferenciada;
	}
	public void setCotasOperacaoDiferenciada(List<Integer> cotasOperacaoDiferenciada) {
		this.cotasOperacaoDiferenciada = cotasOperacaoDiferenciada;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capa == null) ? 0 : capa.hashCode());
		result = prime * result
				+ ((codigoBoxAte == null) ? 0 : codigoBoxAte.hashCode());
		result = prime * result
				+ ((codigoBoxDe == null) ? 0 : codigoBoxDe.hashCode());
		result = prime * result
				+ ((colunaOrdenacao == null) ? 0 : colunaOrdenacao.hashCode());
		result = prime
				* result
				+ ((dtRecolhimentoAte == null) ? 0 : dtRecolhimentoAte
						.hashCode());
		result = prime
				* result
				+ ((dtRecolhimentoDe == null) ? 0 : dtRecolhimentoDe.hashCode());
		result = prime * result
				+ ((fornecedores == null) ? 0 : fornecedores.hashCode());
		result = prime * result + ((idRota == null) ? 0 : idRota.hashCode());
		result = prime * result
				+ ((idRoteiro == null) ? 0 : idRoteiro.hashCode());
		result = prime * result
				+ ((numCotaAte == null) ? 0 : numCotaAte.hashCode());
		result = prime * result
				+ ((numCotaDe == null) ? 0 : numCotaDe.hashCode());
		result = prime * result
				+ ((ordenacao == null) ? 0 : ordenacao.hashCode());
		result = prime * result
				+ ((personalizada == null) ? 0 : personalizada.hashCode());
		result = prime * result + qtdCapasPorPagina;
		result = prime * result + qtdMaximaProdutosComTotalizacao;
		result = prime * result + qtdProdutosPorPagina;
		result = prime * result
				+ ((tipoImpressao == null) ? 0 : tipoImpressao.hashCode());
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
		FiltroEmissaoCE other = (FiltroEmissaoCE) obj;
		if (capa == null) {
			if (other.capa != null)
				return false;
		} else if (!capa.equals(other.capa))
			return false;
		if (codigoBoxAte == null) {
			if (other.codigoBoxAte != null)
				return false;
		} else if (!codigoBoxAte.equals(other.codigoBoxAte))
			return false;
		if (codigoBoxDe == null) {
			if (other.codigoBoxDe != null)
				return false;
		} else if (!codigoBoxDe.equals(other.codigoBoxDe))
			return false;
		if (colunaOrdenacao == null) {
			if (other.colunaOrdenacao != null)
				return false;
		} else if (!colunaOrdenacao.equals(other.colunaOrdenacao))
			return false;
		if (dtRecolhimentoAte == null) {
			if (other.dtRecolhimentoAte != null)
				return false;
		} else if (!dtRecolhimentoAte.equals(other.dtRecolhimentoAte))
			return false;
		if (dtRecolhimentoDe == null) {
			if (other.dtRecolhimentoDe != null)
				return false;
		} else if (!dtRecolhimentoDe.equals(other.dtRecolhimentoDe))
			return false;
		if (fornecedores == null) {
			if (other.fornecedores != null)
				return false;
		} else if (!fornecedores.equals(other.fornecedores))
			return false;
		if (idRota == null) {
			if (other.idRota != null)
				return false;
		} else if (!idRota.equals(other.idRota))
			return false;
		if (idRoteiro == null) {
			if (other.idRoteiro != null)
				return false;
		} else if (!idRoteiro.equals(other.idRoteiro))
			return false;
		if (numCotaAte == null) {
			if (other.numCotaAte != null)
				return false;
		} else if (!numCotaAte.equals(other.numCotaAte))
			return false;
		if (numCotaDe == null) {
			if (other.numCotaDe != null)
				return false;
		} else if (!numCotaDe.equals(other.numCotaDe))
			return false;
		if (ordenacao == null) {
			if (other.ordenacao != null)
				return false;
		} else if (!ordenacao.equals(other.ordenacao))
			return false;
		if (personalizada == null) {
			if (other.personalizada != null)
				return false;
		} else if (!personalizada.equals(other.personalizada))
			return false;
		if (qtdCapasPorPagina != other.qtdCapasPorPagina)
			return false;
		if (qtdMaximaProdutosComTotalizacao != other.qtdMaximaProdutosComTotalizacao)
			return false;
		if (qtdProdutosPorPagina != other.qtdProdutosPorPagina)
			return false;
		if (tipoImpressao != other.tipoImpressao)
			return false;
		return true;
	}
}
