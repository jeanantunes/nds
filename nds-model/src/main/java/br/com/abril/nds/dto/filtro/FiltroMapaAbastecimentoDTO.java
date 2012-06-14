package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroMapaAbastecimentoDTO  implements Serializable {
	
	private static final long serialVersionUID = 5563066721323982905L;
	
	private String dataLancamento;
	private Date dataDate;
	private TipoConsulta tipoConsulta;
	private Long box;
	private Long Rota;
	private String codigoProduto;
	private String nomeProduto;
	private Integer codigoCota;
	private Long edicaoProduto; 
	private String nomeCota;
	private Boolean quebraPorCota;
	private Boolean useSM;
			
	private PaginacaoVO paginacao;
	
	private PaginacaoVO paginacaoDetalhes;
		
	public enum TipoConsulta {
		
		BOX,
		ROTA,
		COTA,
		PRODUTO;		
	}
	
	public enum ColunaOrdenacao {
		BOX("box"),
		TOTAL_PRODUTO("totalProduto"),
		TOTAL_REPARTE("totalReparte"),
		TOTAL_BOX("totalBox");	
		
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
	
	public enum ColunaOrdenacaoDetalhes {
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODTO("nomeProduto"),
		NUMERO_EDICAO("numeroEdicao"),
		REPARTE("reparte"),
		PRECO_CAPA("precoCapa"),
		TOTAL("total");	
		
		private String nomeColuna;
		
		private ColunaOrdenacaoDetalhes(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacaoDetalhes getPorDescricao(String descricao) {
			for(ColunaOrdenacaoDetalhes coluna: ColunaOrdenacaoDetalhes.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}

	/**
	 * @return the dataLancamento
	 */
	public String getDataLancamento() {
		return dataLancamento;
	}
		
	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(String dataLancamento) {
		this.dataDate = DateUtil.parseDataPTBR(dataLancamento);
		this.dataLancamento = dataLancamento;
	}

	/**
	 * @return the tipoConsulta
	 */
	public TipoConsulta getTipoConsulta() {
		return tipoConsulta;
	}

	/**
	 * @param tipoConsulta the tipoConsulta to set
	 */
	public void setTipoConsulta(TipoConsulta tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
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
	 * @return the rota
	 */
	public Long getRota() {
		return Rota;
	}

	/**
	 * @param rota the rota to set
	 */
	public void setRota(Long rota) {
		Rota = rota;
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
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	/**
	 * @return the codigoCota
	 */
	public Integer getCodigoCota() {
		return codigoCota;
	}

	/**
	 * @param codigoCota the codigoCota to set
	 */
	public void setCodigoCota(Integer codigoCota) {
		this.codigoCota = codigoCota;
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
	 * @return the quebraPorCota
	 */
	public Boolean getQuebraPorCota() {
		return quebraPorCota;
	}

	/**
	 * @param quebraPorCota the quebraPorCota to set
	 */
	public void setQuebraPorCota(Boolean quebraPorCota) {
		this.quebraPorCota = quebraPorCota;
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

	public Date getDataDate() {
		return dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}
	

	public PaginacaoVO getPaginacaoDetalhes() {
		return paginacaoDetalhes;
	}

	public void setPaginacaoDetalhes(PaginacaoVO paginacaoDetalhes) {
		this.paginacaoDetalhes = paginacaoDetalhes;
	}

	public Long getEdicaoProduto() {
		return edicaoProduto;
	}

	public void setEdicaoProduto(Long edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}

	public Boolean getUseSM() {
		return useSM;
	}

	public void setUseSM(Boolean useSM) {
		this.useSM = useSM;
	}
	
	
}
