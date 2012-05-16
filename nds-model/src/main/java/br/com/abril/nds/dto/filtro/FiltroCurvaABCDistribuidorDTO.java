package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroCurvaABCDistribuidorDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8369999015893950135L;

	@Export(label = "Período de")
	private Date dataDe;
	
	@Export(label = "Período Até")
	private Date dataAte;
	
	@Export(label = "Fornecedor")
	private String codigoFornecedor;
	
	@Export(label = "Código")
	private String codigoProduto;
	
	@Export(label = "Produto")
	private String nomeProduto;
	
	@Export(label = "Edição")
	private String edicaoProduto;
	
	@Export(label = "Editor")
	private String codigoEditor;
	
	@Export(label = "Cota")
	private String codigoCota;
	
	@Export(label = "Nome")
	private String nomeCota;
	
	@Export(label = "Municipio")
	private String municipio;

	private PaginacaoVO paginacao;
	
	public Date getDataDe() {
		return dataDe;
	}

	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}

	public Date getDataAte() {
		return dataAte;
	}

	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}

	public String getCodigoFornecedor() {
		return codigoFornecedor;
	}

	public void setCodigoFornecedor(String codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getEdicaoProduto() {
		return edicaoProduto;
	}

	public void setEdicaoProduto(String edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}

	public String getCodigoEditor() {
		return codigoEditor;
	}

	public void setCodigoEditor(String codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

	public String getCodigoCota() {
		return codigoCota;
	}

	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public FiltroCurvaABCDistribuidorDTO(Date dataDe, Date dataAte,
			String codigoFornecedor, String codigoProduto, String nomeProduto,
			String edicaoProduto, String codigoEditor, String codigoCota,
			String nomeCota, String municipio) {
		super();
		this.dataDe = dataDe;
		this.dataAte = dataAte;
		this.codigoFornecedor = codigoFornecedor;
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.edicaoProduto = edicaoProduto;
		this.codigoEditor = codigoEditor;
		this.codigoCota = codigoCota;
		this.nomeCota = nomeCota;
		this.municipio = municipio;
	}	

}
