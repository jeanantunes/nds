package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Data Transfer Object para filtro da pesquisa de vendas de encalhe
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class FiltroVendaEncalheDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	@Export(label="Cota")
	private Integer numeroCota;
	
	@Export(label="Nome")
	private String nomeCota;
	
	@Export(label="Tipo de Venda")
	private TipoVendaEncalhe tipoVendaEncalhe;
	
	@Export(label="Período")
	private Date periodoInicial;
	
	@Export(label="Até")
	private Date periodoFinal;
	
	private Date horarioVenda;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColuna ordenacaoColuna;
	
	public FiltroVendaEncalheDTO() {}
	
	public FiltroVendaEncalheDTO(Integer numeroCota,
			TipoVendaEncalhe tipoVendaEncalhe, Date periodoInicial,
			Date periodoFinal) {
		this.numeroCota = numeroCota;
		this.tipoVendaEncalhe = tipoVendaEncalhe;
		this.periodoInicial = periodoInicial;
		this.periodoFinal = periodoFinal;
	}



	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum OrdenacaoColuna{
		
		DATA("dataVenda"),
		NUMERO_COTA("numeroCota"),
		NOME_COTA("nomeCota"),
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		NUMERO_EDICAO("numeroEdicao"),
		PRECO_CAPA("precoCapa"),
		PRECO_DESCONTO("precoDesconto"),
		QNT_PRODUTO("qntProduto"),
		TOTAL_VENDA("valoTotalProduto"),
		TIPO_VENDA("tipoVendaEncalhe");
		
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
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the tipoVendaEncalhe
	 */
	public TipoVendaEncalhe getTipoVendaEncalhe() {
		return tipoVendaEncalhe;
	}

	/**
	 * @param tipoVendaEncalhe the tipoVendaEncalhe to set
	 */
	public void setTipoVendaEncalhe(TipoVendaEncalhe tipoVendaEncalhe) {
		this.tipoVendaEncalhe = tipoVendaEncalhe;
	}

	/**
	 * @return the periodoInicial
	 */
	public Date getPeriodoInicial() {
		return periodoInicial;
	}

	/**
	 * @param periodoInicial the periodoInicial to set
	 */
	public void setPeriodoInicial(Date periodoInicial) {
		this.periodoInicial = periodoInicial;
	}

	/**
	 * @return the periodoFinal
	 */
	public Date getPeriodoFinal() {
		return periodoFinal;
	}

	/**
	 * @param periodoFinal the periodoFinal to set
	 */
	public void setPeriodoFinal(Date periodoFinal) {
		this.periodoFinal = periodoFinal;
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
	
	public Date getHorarioVenda() {
		return horarioVenda;
	}

	public void setHorarioVenda(Date horarioVenda) {
		this.horarioVenda = horarioVenda;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nomeCota == null) ? 0 : nomeCota.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime * result
				+ ((periodoFinal == null) ? 0 : periodoFinal.hashCode());
		result = prime * result
				+ ((periodoInicial == null) ? 0 : periodoInicial.hashCode());
		result = prime
				* result
				+ ((tipoVendaEncalhe == null) ? 0 : tipoVendaEncalhe.hashCode());
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
		FiltroVendaEncalheDTO other = (FiltroVendaEncalheDTO) obj;
		if (nomeCota == null) {
			if (other.nomeCota != null)
				return false;
		} else if (!nomeCota.equals(other.nomeCota))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		if (periodoFinal == null) {
			if (other.periodoFinal != null)
				return false;
		} else if (!periodoFinal.equals(other.periodoFinal))
			return false;
		if (periodoInicial == null) {
			if (other.periodoInicial != null)
				return false;
		} else if (!periodoInicial.equals(other.periodoInicial))
			return false;
		if (tipoVendaEncalhe != other.tipoVendaEncalhe)
			return false;
		return true;
	}
}
