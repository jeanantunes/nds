package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.util.DateUtil;

public class RegiaoNMaiores_ProdutoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -8665752560777384228L;
	
	private String codProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private String dataLcto;
	private String dataRcto;
	private StatusLancamento status;
	private Long classificacao;
	private String descricaoClassificacao;
	private String codigo_icd;
	private BigInteger reparte;
	private BigInteger venda;

	public String getCodProduto() {
		return codProduto;
	}
	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}
	public String getDescricaoClassificacao() {
		return descricaoClassificacao;
	}
	public void setDescricaoClassificacao(String descricaoClassificacao) {
		this.descricaoClassificacao = descricaoClassificacao;
	}
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	public StatusLancamento getStatus() {
		return status;
	}
	public void setStatus(StatusLancamento status) {
		this.status = status;
	}
	public String getDataLcto() {
		return dataLcto;
	}
	public void setDataLcto(Date dataLcto) {
		this.dataLcto = DateUtil.formatarDataPTBR(dataLcto);
	}
	public String getDataRcto() {
		return dataRcto;
	}
	public void setDataRcto(Date dataRcto) {
		this.dataRcto = DateUtil.formatarDataPTBR(dataRcto);
	}
	public Long getClassificacao() {
		return classificacao;
	}
	public void setClassificacao(Long classificacao) {
		this.classificacao = classificacao;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public String getCodigo_icd() {
		return codigo_icd;
	}
	public void setCodigo_icd(String codigo_icd) {
		this.codigo_icd = codigo_icd;
	}
	public BigInteger getReparte() {
		return reparte;
	}
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	public BigInteger getVenda() {
		return venda;
	}
	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}
	
}