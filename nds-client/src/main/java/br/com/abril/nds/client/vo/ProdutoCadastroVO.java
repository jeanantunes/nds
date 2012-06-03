package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;

public class ProdutoCadastroVO implements Serializable {

	private static final long serialVersionUID = 2062099010735397771L;

	private Long id;
	
	private String codigo;
	
	private String nome;
	
	private Long codigoFornecedor;
	
	private Long codigoEditor;
	
	private String slogan;
	
	private Long codigoTipoProduto;
	
	private String formaComercializacao;
	
	private Integer peb;
	
	private Integer pacotePadrao;
	
	private Long tipoDesconto;
	
	private String periodicidade;
	
	private String grupoEditorial;
	
	private String subGrupoEditorial;
	
	private String tributacaoFiscal;
	
	/**
	 * 
	 */
	public ProdutoCadastroVO() {
		
	}
	
	public ProdutoCadastroVO(Long id, String codigo, String nome,
			Long codigoFornecedor, Long codigoEditor, String slogan,
			Long codigoTipoProduto, String formaComercializacao, Integer peb,
			Integer pacotePadrao, Long tipoDesconto, String periodicidade,
			String grupoEditorial, String subGrupoEditorial,
			String tributacaoFiscal) {
		this.id = id;
		this.codigo = codigo;
		this.nome = nome;
		this.codigoFornecedor = codigoFornecedor;
		this.codigoEditor = codigoEditor;
		this.slogan = slogan;
		this.codigoTipoProduto = codigoTipoProduto;
		this.formaComercializacao = formaComercializacao;
		this.peb = peb;
		this.pacotePadrao = pacotePadrao;
		this.tipoDesconto = tipoDesconto;
		this.periodicidade = periodicidade;
		this.grupoEditorial = grupoEditorial;
		this.subGrupoEditorial = subGrupoEditorial;
		this.tributacaoFiscal = tributacaoFiscal;
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
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the codigoFornecedor
	 */
	public Long getCodigoFornecedor() {
		return codigoFornecedor;
	}

	/**
	 * @param codigoFornecedor the codigoFornecedor to set
	 */
	public void setCodigoFornecedor(Long codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}

	/**
	 * @return the codigoEditor
	 */
	public Long getCodigoEditor() {
		return codigoEditor;
	}

	/**
	 * @param codigoEditor the codigoEditor to set
	 */
	public void setCodigoEditor(Long codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

	/**
	 * @return the slogan
	 */
	public String getSlogan() {
		return slogan;
	}

	/**
	 * @param slogan the slogan to set
	 */
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	/**
	 * @return the codigoTipoProduto
	 */
	public Long getCodigoTipoProduto() {
		return codigoTipoProduto;
	}

	/**
	 * @param codigoTipoProduto the codigoTipoProduto to set
	 */
	public void setCodigoTipoProduto(Long codigoTipoProduto) {
		this.codigoTipoProduto = codigoTipoProduto;
	}

	/**
	 * @return the formaComercializacao
	 */
	public String getFormaComercializacao() {
		return formaComercializacao;
	}

	/**
	 * @param formaComercializacao the formaComercializacao to set
	 */
	public void setFormaComercializacao(String formaComercializacao) {
		this.formaComercializacao = formaComercializacao;
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
	 * @return the tipoDesconto
	 */
	public Long getTipoDesconto() {
		return tipoDesconto;
	}

	/**
	 * @param tipoDesconto the tipoDesconto to set
	 */
	public void setTipoDesconto(Long tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	/**
	 * @return the periodicidade
	 */
	public String getPeriodicidade() {
		return periodicidade;
	}

	/**
	 * @param periodicidade the periodicidade to set
	 */
	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}

	/**
	 * @return the grupoEditorial
	 */
	public String getGrupoEditorial() {
		return grupoEditorial;
	}

	/**
	 * @param grupoEditorial the grupoEditorial to set
	 */
	public void setGrupoEditorial(String grupoEditorial) {
		this.grupoEditorial = grupoEditorial;
	}

	/**
	 * @return the subGrupoEditorial
	 */
	public String getSubGrupoEditorial() {
		return subGrupoEditorial;
	}

	/**
	 * @param subGrupoEditorial the subGrupoEditorial to set
	 */
	public void setSubGrupoEditorial(String subGrupoEditorial) {
		this.subGrupoEditorial = subGrupoEditorial;
	}

	/**
	 * @return the tributacaoFiscal
	 */
	public String getTributacaoFiscal() {
		return tributacaoFiscal;
	}

	/**
	 * @param tributacaoFiscal the tributacaoFiscal to set
	 */
	public void setTributacaoFiscal(String tributacaoFiscal) {
		this.tributacaoFiscal = tributacaoFiscal;
	}

	public static ProdutoCadastroVO parseProdutoToProdutoCadastroVO(Produto produto) {

		if (produto == null) {
			throw new RuntimeException("Produto não pode ser nulo!");
		}
		
		FormaComercializacao formaComercializacao = produto.getFormaComercializacao();
		
		PeriodicidadeProduto periodicidade = produto.getPeriodicidade();
		
		TributacaoFiscal tributacaoFiscal = produto.getTributacaoFiscal();
		
		long codigoTipoDesconto = produto.getDescontoLogistica() != null ? 
				produto.getDescontoLogistica().getTipoDesconto().longValue() : 0L;
		
		ProdutoCadastroVO produtoCadastroVO = new ProdutoCadastroVO(
			produto.getId(), produto.getCodigo(), produto.getNome(), 
			produto.getFornecedor().getId(), produto.getEditor().getId(), 
			produto.getSlogan(), produto.getTipoProduto().getId(), 
			formaComercializacao != null ? formaComercializacao.toString() : "", produto.getPeb(), 
			produto.getPacotePadrao(), codigoTipoDesconto, periodicidade != null ? periodicidade.toString() : "", 
			produto.getGrupoEditorial(), produto.getSubGrupoEditorial(), tributacaoFiscal != null ? tributacaoFiscal.toString() : "");
		
		return produtoCadastroVO;
	}
	
}
