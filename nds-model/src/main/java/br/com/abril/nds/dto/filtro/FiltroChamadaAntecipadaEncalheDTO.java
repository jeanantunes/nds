package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/** 
 * Data Transfer Object para filtro da pesquisa de chamada antecipada de encalhe.
 * 
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class FiltroChamadaAntecipadaEncalheDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1821183862532025765L;
	
	@Export(label = "Código")
	private String codigoProduto;
	
	@Export(label = "Produto")
	private String nomeProduto;
	
	@Export(label = "Edição")
	private Long numeroEdicao;
	
	@Export(label = "Data Programada")
	private String dataProgramada;
	
	private Long box;
	
	@Export(label = "Fornecedor")
	private String nomeFornecedor;

	private Long fornecedor;
	
	private Integer numeroCota;
	
	private Date dataAntecipacao;

	private PaginacaoVO paginacao;
	
	private OrdenacaoColuna ordenacaoColuna;
	
	private Long roteiro;
	
	private Long rota;
	
	private boolean programacaoCE;
	
	private Date dataOperacao;
	
	@Export(label = "Box")
	private String descBox;
	
	@Export(label = "Rota")
	private String descRota;
	
	@Export(label = "Roteiro")
	private String descRoteiro;
	
	@Export(label = "Município")
	private String descMunicipio;
	
	@Export(label = "Tipo de Ponto")
	private String descTipoPontoPDV;
	
	@Export(label = "Com CE")
	private String descComCE;
	
	private String codTipoPontoPDV;
	
	private boolean recolhimentoFinal;
	
	private List<ChamadaAntecipadaEncalheDTO> chamadasNaoSelecionadas;

	/**
	 * Construtor padrão.
	 */
	public FiltroChamadaAntecipadaEncalheDTO(String codigoProduto,Long numeroEdicao,
											Long box, Long fornecedor, Long roteiro, Long rota,
											boolean programacaoCE, String municipio, String tipoPontoPDV) {
		
		this.codigoProduto = codigoProduto;
		this.numeroEdicao = numeroEdicao;
		this.box = box;
		this.fornecedor = fornecedor;
		this.rota = rota;
		this.roteiro = roteiro;
		this.programacaoCE = programacaoCE;
		this.codTipoPontoPDV = tipoPontoPDV;
		this.descMunicipio = municipio;
	}
	
	/**
	 * Construtor padrão.
	 */
	public FiltroChamadaAntecipadaEncalheDTO(String codigoProduto,Long numeroEdicao,Long box, Long fornecedor,Integer numeroCota) {
		
		this.codigoProduto = codigoProduto;
		this.numeroEdicao = numeroEdicao;
		this.box = box;
		this.fornecedor = fornecedor;
		this.numeroCota = numeroCota;
	}

	public FiltroChamadaAntecipadaEncalheDTO() {
		
	}
	
	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColuna {
		
		BOX("box"),
		NUMERO_COTA("numeroCota"),
		NOME_COTA("nomeCota"),
		QNT_EXEMPLARES("qntExemplares");
		
		private String nomeColuna;
		
		private OrdenacaoColuna(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
	}
	
	/**
	 * @return the descComCE
	 */
	public String getDescComCE() {
		return descComCE;
	}

	/**
	 * @param descComCE the descComCE to set
	 */
	public void setDescComCE(String descComCE) {
		this.descComCE = descComCE;
	}

	/**
	 * @return the descBox
	 */
	public String getDescBox() {
		return descBox;
	}

	/**
	 * @param descBox the descBox to set
	 */
	public void setDescBox(String descBox) {
		this.descBox = descBox;
	}

	/**
	 * @return the descRota
	 */
	public String getDescRota() {
		return descRota;
	}

	/**
	 * @param descRota the descRota to set
	 */
	public void setDescRota(String descRota) {
		this.descRota = descRota;
	}

	/**
	 * @return the descRoteiro
	 */
	public String getDescRoteiro() {
		return descRoteiro;
	}

	/**
	 * @param descRoteiro the descRoteiro to set
	 */
	public void setDescRoteiro(String descRoteiro) {
		this.descRoteiro = descRoteiro;
	}

	/**
	 * @return the programacaoCE
	 */
	public boolean isProgramacaoCE() {
		return programacaoCE;
	}

	/**
	 * @param programacaoCE the programacaoCE to set
	 */
	public void setProgramacaoCE(boolean programacaoCE) {
		this.programacaoCE = programacaoCE;
	}

	/**
	 * @return the roteiro
	 */
	public Long getRoteiro() {
		return roteiro;
	}

	/**
	 * @param roteiro the roteiro to set
	 */
	public void setRoteiro(Long roteiro) {
		this.roteiro = roteiro;
	}

	/**
	 * @return the rota
	 */
	public Long getRota() {
		return rota;
	}

	/**
	 * @param rota the rota to set
	 */
	public void setRota(Long rota) {
		this.rota = rota;
	}

	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return StringUtils.leftPad(codigoProduto, 8, '0');
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = StringUtils.leftPad(codigoProduto, 8, '0');
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
	 * @return the box
	 */
	public Long getBox() {
		return box;
	}

	/**
	 * @param box the box to set
	 */
	public void setBox(Long box) {
		this.box = box;
	}

	/**
	 * @return the fornecedor
	 */
	public Long getFornecedor() {
		return fornecedor;
	}

	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(Long fornecedor) {
		this.fornecedor = fornecedor;
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

	/**
	 * @return the ordenacaoColuna
	 */
	public OrdenacaoColuna getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * @param ordenacaoColuna the ordenacaoColuna to set
	 */
	public void setOrdenacaoColuna(OrdenacaoColuna ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	public String getDataProgramada() {
		return dataProgramada;
	}

	public void setDataProgramada(String dataProgramada) {
		this.dataProgramada = dataProgramada;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	/**
	 * @return the dataAntecipacao
	 */
	public Date getDataAntecipacao() {
		return dataAntecipacao;
	}

	/**
	 * @param dataAntecipacao the dataAntecipacao to set
	 */
	public void setDataAntecipacao(Date dataAntecipacao) {
		this.dataAntecipacao = dataAntecipacao;
	}
	
	/**
	 * @return the dataOperacao
	 */
	public Date getDataOperacao() {
		return dataOperacao;
	}

	/**
	 * @param dataOperacao the dataOperacao to set
	 */
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
	
	
	/**
	 * @return the codTipoPontoPDV
	 */
	public String getCodTipoPontoPDV() {
		return codTipoPontoPDV;
	}

	/**
	 * @param codTipoPontoPDV the codTipoPontoPDV to set
	 */
	public void setCodTipoPontoPDV(String tipoPontoPDV) {
		this.codTipoPontoPDV = tipoPontoPDV;
	}

	/**
	 * @return the descMunicipio
	 */
	public String getDescMunicipio() {
		return descMunicipio;
	}

	/**
	 * @param descMunicipio the descMunicipio to set
	 */
	public void setDescMunicipio(String descMunicipio) {
		this.descMunicipio = descMunicipio;
	}

	/**
	 * @return the descTipoPontoPDV
	 */
	public String getDescTipoPontoPDV() {
		return descTipoPontoPDV;
	}

	/**
	 * @param descTipoPontoPDV the descTipoPontoPDV to set
	 */
	public void setDescTipoPontoPDV(String descTipoPontoPDV) {
		this.descTipoPontoPDV = descTipoPontoPDV;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((box == null) ? 0 : box.hashCode());
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime * result
				+ ((dataAntecipacao == null) ? 0 : dataAntecipacao.hashCode());
		result = prime * result
				+ ((dataProgramada == null) ? 0 : dataProgramada.hashCode());
		result = prime * result
				+ ((fornecedor == null) ? 0 : fornecedor.hashCode());
		result = prime * result
				+ ((nomeFornecedor == null) ? 0 : nomeFornecedor.hashCode());
		result = prime * result
				+ ((nomeProduto == null) ? 0 : nomeProduto.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroChamadaAntecipadaEncalheDTO other = (FiltroChamadaAntecipadaEncalheDTO) obj;
		if (box == null) {
			if (other.box != null)
				return false;
		} else if (!box.equals(other.box))
			return false;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (dataAntecipacao == null) {
			if (other.dataAntecipacao != null)
				return false;
		} else if (!dataAntecipacao.equals(other.dataAntecipacao))
			return false;
		if (dataProgramada == null) {
			if (other.dataProgramada != null)
				return false;
		} else if (!dataProgramada.equals(other.dataProgramada))
			return false;
		if (fornecedor == null) {
			if (other.fornecedor != null)
				return false;
		} else if (!fornecedor.equals(other.fornecedor))
			return false;
		if (nomeFornecedor == null) {
			if (other.nomeFornecedor != null)
				return false;
		} else if (!nomeFornecedor.equals(other.nomeFornecedor))
			return false;
		if (nomeProduto == null) {
			if (other.nomeProduto != null)
				return false;
		} else if (!nomeProduto.equals(other.nomeProduto))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		return true;
	}

	public boolean isRecolhimentoFinal() {
		return recolhimentoFinal;
	}

	public void setRecolhimentoFinal(boolean recolhimentoFinal) {
		this.recolhimentoFinal = recolhimentoFinal;
	}

	public List<ChamadaAntecipadaEncalheDTO> getChamadasNaoSelecionadas() {
		return chamadasNaoSelecionadas;
	}

	public void setChamadasNaoSelecionadas(
			List<ChamadaAntecipadaEncalheDTO> chamadasNaoSelecionadas) {
		this.chamadasNaoSelecionadas = chamadasNaoSelecionadas;
	}
}
