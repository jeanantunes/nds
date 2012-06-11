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
		
		//TODO definir os valores de cada opção do Enum
		
		DATA(""),
		NUMERO_COTA(""),
		NOME_COTA(""),
		CODIGO_PRODUTO(""),
		NOME_PRODUTO(""),
		NUMERO_EDICAO(""),
		PRECO_CAPA(""),
		PRECO_DESCONTO(""),
		QNT_PRODUTO(""),
		TOTAL_VENDA(""),
		TIPO_VENDA("");
		
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
	
	
}
