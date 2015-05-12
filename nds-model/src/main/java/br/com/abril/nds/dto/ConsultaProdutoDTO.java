package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaProdutoDTO implements Serializable {

	private static final long serialVersionUID = -8072285478313345444L;

	private Long id;
	
	@Export(label="Código", alignment=Alignment.CENTER, widthPercent=8)
	private String codigo;

	@Export(label="Produto", alignment=Alignment.LEFT)
	private String produtoDescricao;
	
	@Export(label="Tipo Produto", alignment=Alignment.LEFT, widthPercent=10)
	private String tipoProdutoDescricao;
	
	@Export(label="Editor", alignment=Alignment.LEFT)
	private String nomeEditor;
	
	@Export(label="Fornecedor", alignment=Alignment.LEFT, widthPercent=12)
	private String tipoContratoFornecedor;
	
	@Export(label="PEB", alignment=Alignment.CENTER, widthPercent=6)
	private Integer peb;
	
	@Export(label="Pcte. Padrão" , alignment=Alignment.CENTER, widthPercent=6)
	private Integer pacotePadrao;
	
	@Export(label="Desconto %", alignment=Alignment.RIGHT, widthPercent=8)
	private BigDecimal percentualDesconto;

	@Export(label="Periodicidade", alignment=Alignment.CENTER, widthPercent=10)
	private PeriodicidadeProduto periodicidade;
	
	private boolean isSemCeIntegracao;
	
	/**
	 * Construtor Padrão
	 */
	public ConsultaProdutoDTO() {
		
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the produtoDescricao
	 */
	public String getProdutoDescricao() {
		return produtoDescricao;
	}

	/**
	 * @param produtoDescricao the produtoDescricao to set
	 */
	public void setProdutoDescricao(String produtoDescricao) {
		this.produtoDescricao = produtoDescricao;
	}

	/**
	 * @return the tipoProdutoDescricao
	 */
	public String getTipoProdutoDescricao() {
		return tipoProdutoDescricao;
	}

	/**
	 * @param tipoProdutoDescricao the tipoProdutoDescricao to set
	 */
	public void setTipoProdutoDescricao(String tipoProdutoDescricao) {
		this.tipoProdutoDescricao = tipoProdutoDescricao;
	}

	/**
	 * @return the nomeEditor
	 */
	public String getNomeEditor() {
		return nomeEditor;
	}

	/**
	 * @param nomeEditor the nomeEditor to set
	 */
	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor == null ? "" : nomeEditor;
	}

	/**
	 * @return the tipoContratoFornecedor
	 */
	public String getTipoContratoFornecedor() {
		return tipoContratoFornecedor;
	}

	/**
	 * @param tipoContratoFornecedor the tipoContratoFornecedor to set
	 */
	public void setTipoContratoFornecedor(String tipoContratoFornecedor) {
		this.tipoContratoFornecedor = tipoContratoFornecedor;
	}

	/**
	 * @return the peb
	 */
	public Integer getPeb() {
		return peb;
	}

	/**
	 * @param peb the peb to set
	 */
	public void setPeb(Integer peb) {
		this.peb = peb;
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
	 * @return the pacotePadrao
	 */
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}

	/**
	 * @param pacotePadrao the pacotePadrao to set
	 */
	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	/**
	 * @return the percentualDesconto
	 */
	public BigDecimal getPercentualDesconto() {
		return percentualDesconto;
	}

	/**
	 * @param percentualDesconto the percentualDesconto to set
	 */
	public void setPercentualDesconto(BigDecimal percentualDesconto) {
		this.percentualDesconto = percentualDesconto != null 
				? percentualDesconto.setScale(2, RoundingMode.HALF_EVEN) 
				: null;
	}

	/**
	 * @return the periodicidade
	 */
	public String getPeriodicidade() {
		return periodicidade.toString();
	}

	/**
	 * @param periodicidade the periodicidade to set
	 */
	public void setPeriodicidade(PeriodicidadeProduto periodicidade) {
		this.periodicidade = periodicidade;
	}

	public boolean isSemCeIntegracao() {
		return isSemCeIntegracao;
	}

	public void setSemCeIntegracao(boolean isSemCeIntegracao) {
		this.isSemCeIntegracao = isSemCeIntegracao;
	}
}