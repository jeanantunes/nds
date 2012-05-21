package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroParciaisDTO implements Serializable  {

	private static final long serialVersionUID = -4620933960876071792L;
	
	@Export(label = "Código")
	private String codigoProduto;	
	@Export(label = "Produto")
	private String nomeProduto;
	@Export(label = "Edição")
	private Long edicaoProduto;
	@Export(label = "Fornecedor")
	private String nomeFornecedor;
	private Long idFornecedor;
	@Export(label = "Período")
	private String dataInicial;
	@Export(label = "Até")
	private String dataFinal;
	@Export(label = "Status")
	private String status;
	
	
	private PaginacaoVO paginacao;
			
	public enum ColunaOrdenacao {

		DATA_LANCAMENTO("dataLancamento"),
		DATA_RECOLHIMENTO("dataRecolhimento"),
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		NUM_EDICAO("numEdicao"),
		NOME_FORNECEDOR("nomeFornecedor"),
		STATUS_PARCIAL("statusParcial");
		
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
	
	public enum ColunaOrdenacaoPeriodo {

		DATA_LANCAMENTO("dataLancamento"),
		DATA_RECOLHIMENTO("dataRecolhimento"),
		PERIODO("periodo"),
		REPARTE("reparte"),
		ENCALHE("encalhe"),
		VENDAS("vendas"),
		VENDA_ACUMULADA("vendaAcumulada"),
		PERC_VENDA("percVenda");
		
		private String nomeColuna;
		
		private ColunaOrdenacaoPeriodo(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacaoPeriodo getPorDescricao(String descricao) {
			for(ColunaOrdenacaoPeriodo coluna: ColunaOrdenacaoPeriodo.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}
	
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		if(codigoProduto.trim().isEmpty())
			this.codigoProduto = null;
		else
			this.codigoProduto = codigoProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		if(nomeProduto.trim().isEmpty())
			this.nomeProduto = null;
		else
			this.nomeProduto = nomeProduto;
	}
	public Long getEdicaoProduto() {
		return edicaoProduto;
	}
	public void setEdicaoProduto(Long edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}
	public Long getIdFornecedor() {
		return idFornecedor;
	}
	public void setIdFornecedor(Long idFornecedor) {
		if(idFornecedor == null || idFornecedor<0)
			this.idFornecedor = null;
		else
			this.idFornecedor = idFornecedor;
	}
	public String getDataInicial() {
		return dataInicial;
	}
	
	public Date getDataInicialDate() {
		if(DateUtil.isValidDatePTBR(dataInicial))
			return DateUtil.parseData(dataInicial, Constantes.DATE_PATTERN_PT_BR);
		
		return null;
	}
	
	public Date getDataFinalDate() {
		if(DateUtil.isValidDatePTBR(dataFinal))
			return DateUtil.parseData(dataFinal, Constantes.DATE_PATTERN_PT_BR);
		
		return null;
	}
	
	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}
	public String getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		if(status== null || status.trim().isEmpty())
			status = null;
		else
			this.status = status;
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
	public StatusLancamentoParcial getStatusEnum() {
		return StatusLancamentoParcial.valueOf(status);
	}
	/**
	 * @return the nomeFornecedor
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	/**
	 * @param nomeFornecedor the nomeFornecedor to set
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
}
