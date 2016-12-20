package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroProdutoEdicaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Export(label="Código")
	private String codigo;
	
	@Export(label="Produto")
	private String nome;
	
	@Export(label="Edição")
	private Long edicao;
	
	@Export(label="Fornecedor")
	private String fornecedor;
	
	@Export(label="Parcial")
	private String parcial;
	
	@Export(label="Tipo de Lançamento")
	private String tipoLancamento;
	
	@Export(label="Situação")
	private StatusLancamento statusLancamento;
	
	@Export(label="Brinde")
	private boolean brinde;
	
    private Date dataLancamentoDe;
	
	private Date dataLancamentoAte;
	
	private Double precoDe;
	
	private Double precoAte;
	
	private String codigoBarras;
	
	private String sortOrder;
	
	private String sortName;

	private int page;
	
	private int rp;
	
	
	public FiltroProdutoEdicaoDTO() {}
	
	public FiltroProdutoEdicaoDTO(String codigo, String nome, String fornecedor,String parcial,String tipoLancamento,StatusLancamento statusLancamento,boolean brinde,Date dataLancamentoDe,Date dataLancamentoAte,Double precoDe,Double precoAte,String codigoBarras, String sortOrder, String sortName, int page,int rp) {
		
		this.codigo = codigo;
		this.nome = nome;
		this.fornecedor = fornecedor;
		this.parcial = parcial;
		this.tipoLancamento = tipoLancamento;
		this.statusLancamento = statusLancamento;
		this.brinde = brinde;
		this.dataLancamentoDe = dataLancamentoDe;
		this.dataLancamentoAte = dataLancamentoAte;
		this.precoDe = precoDe;
		this.precoAte = precoAte;
		this.codigoBarras = codigoBarras;
		this.sortName = sortName;
		this.sortOrder = sortOrder;
		this.page = page;
		this.rp = rp;
		
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

	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}

	public String getParcial() {
		return parcial;
	}

	public void setParcial(String parcial) {
		this.parcial = parcial;
	}
	
	public String getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}
	
	public StatusLancamento getStatusLancamento() {
		return statusLancamento;
	}

	public void setStatusLancamento(StatusLancamento statusLancamento) {
		this.statusLancamento = statusLancamento;
	}
	
	public boolean getBrinde() {
		return brinde;
	}

	public void setBrinde(boolean brinde) {
		this.brinde = brinde;
	}

	public Date getDataLancamentoDe() {
		return dataLancamentoDe;
	}

	public void setDdataLancamentoDe(Date dataLancamentoDe) {
		this.dataLancamentoDe = dataLancamentoDe;
	}
	
	public Date getDataLancamentoAte() {
		return dataLancamentoAte;
	}

	public void setDdataLancamentoAte(Date dataLancamentoAte) {
		this.dataLancamentoAte = dataLancamentoAte;
	}
	
	public Double getPrecoDe() {
		return precoDe;
	}

	public void setPrecoDe(Double precoDe) {
		this.precoDe = precoDe;
	}
	
	public Double getPrecoAte() {
		return precoAte;
	}

	public void setPrecoAte(Double precoAte) {
		this.precoAte = precoAte;
	}
	
	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
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

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public int getRp() {
		return rp;
	}

	public void setRp(int rp) {
		this.rp = rp;
	}
	
}