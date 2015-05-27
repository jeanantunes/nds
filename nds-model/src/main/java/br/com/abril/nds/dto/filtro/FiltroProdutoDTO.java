package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroProdutoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Export(label="Código")
	private String codigo;
	
	@Export(label="Produto")
	private String nome;
	
	@Export(label="Fornecedor")
	private String fornecedor;
	
	@Export(label="Editor")
	private String editor;
	
	@Export(label="Tipo Produto")
	private String getDescricaoTipoProduto(){
		
		return (this.tipoProduto!= null)?tipoProduto.getDescricao():"";
	}
	
	private TipoProduto tipoProduto;
	
	private String sortOrder;
	
	private String sortName;

	private Boolean isGeracaoAutomatica;
	
	public FiltroProdutoDTO() {}
	
	public FiltroProdutoDTO(String codigo, String nome, String editor, String fornecedor, Long codigoTipoProduto, String sortOrder, String sortName, Boolean isGeracaAutomatica) {
		
		this.codigo = codigo;
		this.nome = nome;
		this.editor = editor;
		this.fornecedor = fornecedor;
		this.sortName = sortName;
		this.sortOrder = sortOrder;
		this.tipoProduto = new TipoProduto();
		this.tipoProduto.setCodigo(codigoTipoProduto);
		this.isGeracaoAutomatica = isGeracaAutomatica;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		//this.codigo = StringUtils.leftPad(codigo, 8, '0');
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public Boolean getIsGeracaoAutomatica() {
		return isGeracaoAutomatica;
	}

	public void setIsGeracaoAutomatica(Boolean isGeracaoAutomatica) {
		this.isGeracaoAutomatica = isGeracaoAutomatica;
	}
}