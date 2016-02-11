package br.com.abril.nds.dto.filtro;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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
	
	private List<Long> edicaoProduto;
	
	private String codigoEditor;
	
	private Integer codigoCota;
	
	private String nomeCota;
	
	private String municipio;
	
	private Long regiaoID;
	
	private List<Integer> numCotasDentroDaRegiao;

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
		this.codigoProduto = (codigoProduto != null && !"".equals(codigoProduto)) ? StringUtils.leftPad(codigoProduto, 8, '0') : null;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Integer getCodigoCota() {
		return codigoCota;
	}

	public void setCodigoCota(Integer codigoCota) {
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

	public List<Long> getEdicaoProduto() {
		return edicaoProduto;
	}

	public void setEdicaoProduto(List<Long> edicaoProduto) {
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

	public Long getRegiaoID() {
		return regiaoID;
	}

	public void setRegiaoID(Long regiaoID) {
		this.regiaoID = regiaoID;
	}

	public List<Integer> getNumCotasDentroDaRegiao() {
		return numCotasDentroDaRegiao;
	}

	public void setNumCotasDentroDaRegiao(List<Integer> numCotasDentroDaRegiao) {
		this.numCotasDentroDaRegiao = numCotasDentroDaRegiao;
	}
	
}