package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroMapaAbastecimentoDTO  implements Serializable {
	
	private static final long serialVersionUID = 5563066721323982905L;
	
	private String dataLancamento;
	private Date dataDate;
	private TipoConsulta tipoConsulta;
	private Long box;
	private Long rota;
	private Long roteiro;
	private List<String> codigosProduto;
	private String nomeProduto;
	private Integer codigoCota; 
	private String nomeCota;
	private boolean quebraPorCota;
	private boolean useSM;
	private Long idEntregador;
	private boolean excluirProdutoSemReparte;
	private boolean porRepartePromocional;
	private boolean produtoEspecifico;
			
	private PaginacaoVO paginacao;
	
	private PaginacaoVO paginacaoDetalhes;
	
	private List<Long> numerosEdicao;
		
	public enum TipoConsulta {
		
		BOX,
		ROTA,
		COTA,
		PRODUTO,
		PRODUTO_ESPECIFICO,
		PRODUTO_X_COTA,
		ENTREGADOR,
		PROMOCIONAL,
		BOX_X_COTA;
	}
	
	public enum ColunaOrdenacao {
		BOX("box"),
		TOTAL_PRODUTO("totalProduto"),
		TOTAL_REPARTE("totalReparte"),
		TOTAL_BOX("totalBox"),
		CODIGO_ROTA("codigoRota"),
		NOME_COTA("nomeCota"),
		REPARTE("reparte"),
		PROMOCIONAL("materialPromocional"),
		CODIGO_BOX("codigoBox"),
		PRECO_CAPA("precoCapa"),
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODTO("nomeProduto"),
		NUMERO_EDICAO("numeroEdicao"),
		TOTAL("total"),
		CODIGO_COTA("codigoCota"),
		NOME_EDICAO("nomeEdicao"),	
		PRODUTO_COTA("produtoCota"),
		SEQUENCIA_MATRIZ("sequenciaMatriz");
		
		private String nomeColuna;
		
		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacao getPorDescricao(String descricao) {
			if(descricao == null) {
				return null;
			}
			
			if (descricao.equalsIgnoreCase("")) {
				return null;
			}
			
			for(ColunaOrdenacao coluna: ColunaOrdenacao.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}
	
	public enum ColunaOrdenacaoEntregador {
		
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODTO("nomeProduto"),
		EDICAO_PRODUTO("numeroEdicao"),
		NUMERO_EDICAO("numeroEdicao"),
		CODIGO_BARRA("codigoBarra"),
		PACOTE_PADRAO("pacotePadrao"),
		REPARTE("reparte"),
		PRECO_CAPA("precoCapa"),
		CODIGO_COTA("codigoCota"),
		NOME_COTA("nomeCota"),
		QTDE_EXEMPLARES("qtdeExms");
		
		private String nomeColuna;
		
		private ColunaOrdenacaoEntregador(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacaoEntregador getPorDescricao(String descricao) {
			for(ColunaOrdenacaoEntregador coluna: ColunaOrdenacaoEntregador.values()) {
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
		TOTAL("total"),
		CODIGO_BOX("codigoBox"),	
		NOME_EDICAO("nomeEdicao");
		
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
		return rota;
	}

	/**
	 * @param rota the rota to set
	 */
	public void setRota(Long rota) {
		this.rota = rota;
	}

	/**
	 * @return the codigosProduto
	 */
	public List<String> getCodigosProduto() {
		return codigosProduto;
	}

	/**
	 * @param codigosProduto the codigosProduto to set
	 */
	public void setCodigosProduto(List<String> codigosProduto) {
		this.codigosProduto = codigosProduto;
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
	public boolean getQuebraPorCota() {
		return quebraPorCota;
	}

	/**
	 * @param quebraPorCota the quebraPorCota to set
	 */
	public void setQuebraPorCota(boolean quebraPorCota) {
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

	public boolean getUseSM() {
		return useSM;
	}

	public void setUseSM(boolean useSM) {
		this.useSM = useSM;
	}

	/**
	 * @return the excluirProdutoSemReparte
	 */
	public boolean getExcluirProdutoSemReparte() {
		return excluirProdutoSemReparte;
	}

	/**
	 * @param excluirProdutoSemReparte the excluirProdutoSemReparte to set
	 */
	public void setExcluirProdutoSemReparte(boolean excluirProdutoSemReparte) {
		this.excluirProdutoSemReparte = excluirProdutoSemReparte;
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
	 * @return the idEntregador
	 */
	public Long getIdEntregador() {
		return idEntregador;
	}

	/**
	 * @param idEntregador the idEntregador to set
	 */
	public void setIdEntregador(Long idEntregador) {
		this.idEntregador = idEntregador;
	}

	public boolean isPorRepartePromocional() {
		return porRepartePromocional;
	}

	public void setPorRepartePromocional(boolean porRepartePromocional) {
		this.porRepartePromocional = porRepartePromocional;
	}

	public boolean isProdutoEspecifico() {
		return produtoEspecifico;
	}

	public void setProdutoEspecifico(boolean produtoEspecifico) {
		this.produtoEspecifico = produtoEspecifico;
	}

    
    public List<Long> getNumerosEdicao() {
        return numerosEdicao;
    }

    
    public void setNumerosEdicao(List<Long> numerosEdicao) {
        this.numerosEdicao = numerosEdicao;
    }
	
	
}
