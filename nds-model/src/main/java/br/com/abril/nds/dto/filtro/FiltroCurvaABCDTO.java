package br.com.abril.nds.dto.filtro;

import java.util.Date;

import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public abstract class FiltroCurvaABCDTO extends FiltroDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6433750773025148881L;

	private Date dataDe;
	
	private Date dataAte;
	
	private String codigoFornecedor;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private String edicaoProduto;
	
	private String codigoEditor;
	
	private String codigoCota;
	
	private String nomeCota;
	
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

	public String getEdicaoProduto() {
		return edicaoProduto;
	}

	public void setEdicaoProduto(String edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}

	public String getCodigoFornecedor() {
		return codigoFornecedor;
	}

	public void setCodigoFornecedor(String codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}

	public String getCodigoEditor() {
		return codigoEditor;
	}

	public void setCodigoEditor(String codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

}
