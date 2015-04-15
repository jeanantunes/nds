package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.util.CurrencyUtil;

public class ProdutoCadastroVO implements Serializable {

	private static final long serialVersionUID = 2062099010735397771L;

	private Long id;
	
	private String codigo;
	
	private String codigoICD;
	
	private String nome;
	
	private Long codigoFornecedor;
	
	private Long codigoEditor;
	
	private String slogan;
	
	private Long codigoTipoProduto;
	
	private String formaComercializacao;
	
	private Integer peb;
	
	private Integer pacotePadrao;
	
	private Long idDesconto;
	
	private String periodicidade;
	
	private String tributacaoFiscal;
	
	private String classeSocial;
	
	private String sexo;
	
	private String faixaEtaria;
	
	private String formatoProduto;
	
//	private String temaPrincipal;
	
	private Origem origem;
	
	private String desconto;
	
	private String descricaoDescontoManual;
	
	private String formaFisica;
	
	private Boolean isGeracaoAutomatica;
	
	private Boolean isRemessaDistribuicao;
	
	private Long idTipoSegmentoProduto;
	
	private Long idTipoClassifProduto;
	
	public ProdutoCadastroVO() {
		
	}
	
	public ProdutoCadastroVO(Long id, String codigo, String codigoICD, String nome,
			Long codigoFornecedor, Long codigoEditor, String slogan,
			Long codigoTipoProduto, String formaComercializacao, Integer peb,
			Integer pacotePadrao, Long idDesconto, String periodicidade,
			String tributacaoFiscal, String classeSocial, String sexo, 
			String faixaEtaria, String formatoProduto, 
			String formaFisica, 
			Long idTipoSegmentoProduto, 
			Origem origem, 
			Boolean isGeracaoAutomatica, Boolean isRemessaDistribuicao) {
		
		this.id = id;
		this.codigo = codigo;
		this.codigoICD = codigoICD;
		this.nome = nome;
		this.codigoFornecedor = codigoFornecedor;
		this.codigoEditor = codigoEditor;
		this.slogan = slogan;
		this.codigoTipoProduto = codigoTipoProduto;
		this.formaComercializacao = formaComercializacao;
		this.peb = peb;
		this.pacotePadrao = pacotePadrao;
		this.idDesconto = idDesconto;
		this.periodicidade = periodicidade;
		this.tributacaoFiscal = tributacaoFiscal;
		this.classeSocial = classeSocial;
		this.sexo = sexo;
		this.faixaEtaria = faixaEtaria;
		this.formatoProduto = formatoProduto;
		this.formaFisica=formaFisica;
		this.idTipoSegmentoProduto = idTipoSegmentoProduto;
		this.origem = origem;
		this.isGeracaoAutomatica = isGeracaoAutomatica;
		this.isRemessaDistribuicao = isRemessaDistribuicao;
	}

	public static ProdutoCadastroVO parseProdutoToProdutoCadastroVO(Produto produto) {

		if (produto == null) {
            throw new RuntimeException("Produto não pode ser nulo!");
		}
		
		FormaComercializacao formaComercializacao = produto.getFormaComercializacao();
		
		PeriodicidadeProduto periodicidade = produto.getPeriodicidade();
		
		TributacaoFiscal tributacaoFiscal = produto.getTributacaoFiscal();
		
		long idDesconto = produto.getDescontoLogistica() != null ? 
				produto.getDescontoLogistica().getId() : 0L;
		ProdutoCadastroVO produtoCadastroVO = new ProdutoCadastroVO(
			produto.getId(), 
			produto.getCodigo(),
			produto.getCodigoICD(),
			produto.getNome(), 
			(produto.getFornecedor()!=null)?produto.getFornecedor().getId():null, 
			produto.getEditor()!=null?produto.getEditor().getId():0, 
			produto.getSlogan(), 
			produto.getTipoProduto().getId(), 
			formaComercializacao != null ? formaComercializacao.name() : "", 
			produto.getPeb(), 
			produto.getPacotePadrao(), idDesconto, periodicidade != null ? periodicidade.toString() : "", 
			tributacaoFiscal != null ? tributacaoFiscal.toString() : "",
			produto.getSegmentacao()!=null?(produto.getSegmentacao().getClasseSocial()!=null?produto.getSegmentacao().getClasseSocial().name():""):"",
			produto.getSegmentacao()!=null?(produto.getSegmentacao().getSexo()!=null?produto.getSegmentacao().getSexo().name():""):"",
			produto.getSegmentacao()!=null?(produto.getSegmentacao().getFaixaEtaria()!=null?produto.getSegmentacao().getFaixaEtaria().name():""):"",
			produto.getSegmentacao()!=null?(produto.getSegmentacao().getFormatoProduto()!=null?produto.getSegmentacao().getFormatoProduto().name():""):"",
			produto.getSegmentacao()!=null?(produto.getSegmentacao().getFormaFisica()!=null?produto.getSegmentacao().getFormaFisica().name():""):"",											
			(produto.getTipoSegmentoProduto()!=null)?produto.getTipoSegmentoProduto().getId():null,
			produto.getOrigem(),
			produto.getIsGeracaoAutomatica(),
			produto.getIsRemessaDistribuicao()
			);
		
		if(Origem.INTERFACE.equals(produto.getOrigem()) && produto.getDescontoLogistica()!= null){
			produtoCadastroVO.setDesconto(CurrencyUtil.formatarValor( produto.getDescontoLogistica().getPercentualDesconto()).replace(",","."));
			
		}else if(produto.getDesconto()!= null){
			produtoCadastroVO.setDesconto(CurrencyUtil.formatarValor(produto.getDesconto()).replace(",","."));
			produtoCadastroVO.setDescricaoDescontoManual(produto.getDescricaoDesconto());
		}
		
		return produtoCadastroVO;
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
	
	public String getCodigoICD() {
		return codigoICD;
	}

	public void setCodigoICD(String codigoICD) {
		this.codigoICD = codigoICD;
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
	 * @return the idDesconto
	 */
	public Long getIdDesconto() {
		return idDesconto;
	}

	/**
	 * @param idDesconto the idDesconto to set
	 */
	public void setIdDesconto(Long idDesconto) {
		this.idDesconto = idDesconto;
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

	/**
	 * @return the classeSocial
	 */
	public String getClasseSocial() {
		return classeSocial;
	}

	/**
	 * @param classeSocial the classeSocial to set
	 */
	public void setClasseSocial(String classeSocial) {
		this.classeSocial = classeSocial;
	}

	/**
	 * @return the sexo
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * @return the faixaEtaria
	 */
	public String getFaixaEtaria() {
		return faixaEtaria;
	}

	/**
	 * @param faixaEtaria the faixaEtaria to set
	 */
	public void setFaixaEtaria(String faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
	}

	/**
	 * @return the formatoProduto
	 */
	public String getFormatoProduto() {
		return formatoProduto;
	}

	/**
	 * @param formatoProduto the formatoProduto to set
	 */
	public void setFormatoProduto(String formatoProduto) {
		this.formatoProduto = formatoProduto;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}
	
	public String getDesconto() {
		return desconto;
	}

	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}

	public String getDescricaoDescontoManual() {
		return descricaoDescontoManual;
	}

	public void setDescricaoDescontoManual(String descricaoDescontoManual) {
		this.descricaoDescontoManual = descricaoDescontoManual;
	}

	public String getFormaFisica() {
		return formaFisica;
	}

	public void setFormaFisica(String formaFisica) {
		this.formaFisica = formaFisica;
	}

	public Boolean getIsGeracaoAutomatica() {
		return isGeracaoAutomatica;
	}

	public void setIsGeracaoAutomatica(Boolean isGeracaoAutomatica) {
		this.isGeracaoAutomatica = isGeracaoAutomatica;
	}

	public Boolean getIsRemessaDistribuicao() {
		return isRemessaDistribuicao;
	}

	public void setIsRemessaDistribuicao(Boolean isRemessaDistribuicao) {
		this.isRemessaDistribuicao = isRemessaDistribuicao;
	}

	public Long getIdTipoSegmentoProduto() {
		return idTipoSegmentoProduto;
	}

	public void setIdTipoSegmentoProduto(Long idTipoSegmentoProduto) {
		this.idTipoSegmentoProduto = idTipoSegmentoProduto;
	}

	public Long getIdTipoClassifProduto() {
		return idTipoClassifProduto;
	}

	public void setIdTipoClassifProduto(Long idTipoClassifProduto) {
		this.idTipoClassifProduto = idTipoClassifProduto;
	}
}
