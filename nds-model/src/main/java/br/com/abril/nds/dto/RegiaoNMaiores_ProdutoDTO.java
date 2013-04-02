package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.util.DateUtil;

public class RegiaoNMaiores_ProdutoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -8665752560777384228L;
	
	private String codProduto;
	private Long numeroEdicao;
	private String dataLcto;
	private TipoLancamento status;
	private Long classificacao;
	private String descricaoClassificacao;

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
	public TipoLancamento getStatus() {
		return status;
	}
	public void setStatus(TipoLancamento status) {
		this.status = status;
	}
	public String getDataLcto() {
		return dataLcto;
	}
	public void setDataLcto(Date dataLcto) {
		this.dataLcto = DateUtil.formatarDataPTBR(dataLcto);
	}
	public Long getClassificacao() {
		return classificacao;
	}
	public void setClassificacao(Long classificacao) {
		this.classificacao = classificacao;
	}
}