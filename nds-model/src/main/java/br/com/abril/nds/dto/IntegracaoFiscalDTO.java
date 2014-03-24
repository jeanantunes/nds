package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@SuppressWarnings("serial")
@Exportable
public class IntegracaoFiscalDTO implements Serializable{
	
	@Export(label ="codEmpresa",exhibitionOrder=1)
	private String codEmpresa;
	
	@Export(label ="codFilial", exhibitionOrder=2)
	private String codFilial;
	
	@Export(label ="Natureza Estoque",exhibitionOrder=3)
	private String naturezaEstoque;
	
	@Export(label ="Centro Custo",exhibitionOrder=4)
	private String centroCusto;
	
	@Export(label ="dt. Inventario",exhibitionOrder=5)
	private String dataInventario;
	
	@Export(label ="Cod. Material",exhibitionOrder=6)
	private String codMaterial;
	
	@Export(label ="qtde ed",exhibitionOrder=7)
	private String qtdeEdicoes;
	
	@Export(label ="Classif. Fiscal",exhibitionOrder=8)
	private String classificacaoFiscal;
	
	@Export(label ="Local Estoque",exhibitionOrder=9)
	private String localEstoque;
	
	@Export(label ="Conta Estoque",exhibitionOrder=10)
	private String contaEstoque;
	
	@Export(label ="Qtde.",exhibitionOrder=11)
	private Integer quantidade;
	
	@Export(label ="Unidade Medida",exhibitionOrder=12)
	private String unidadeMedida;
	
	@Export(label ="Custo Unitário",exhibitionOrder=13)
	private String custoUnitario;


	public String getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(String codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	public String getCodFilial() {
		return codFilial;
	}

	public void setCodFilial(String codFilial) {
		this.codFilial = codFilial;
	}

	public String getNaturezaEstoque() {
		return naturezaEstoque;
	}

	public void setNaturezaEstoque(String naturezaEstoque) {
		this.naturezaEstoque = naturezaEstoque;
	}

	public String getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(String centroCusto) {
		this.centroCusto = centroCusto;
	}

	public String getDataInventario() {
		return dataInventario;
	}

	public void setDataInventario(String dataInventario) {
		this.dataInventario = dataInventario;
	}

	public String getCodMaterial() {
		return codMaterial;
	}

	public void setCodMaterial(String codMaterial) {
		this.codMaterial = codMaterial;
	}

	public String getQtdeEdicoes() {
		return qtdeEdicoes;
	}

	public void setQtdeEdicoes(String qtdeEdicoes) {
		this.qtdeEdicoes = qtdeEdicoes;
	}

	public String getClassificacaoFiscal() {
		return classificacaoFiscal;
	}

	public void setClassificacaoFiscal(String classificacaoFiscal) {
		this.classificacaoFiscal = classificacaoFiscal;
	}

	public String getLocalEstoque() {
		return localEstoque;
	}

	public void setLocalEstoque(String localEstoque) {
		this.localEstoque = localEstoque;
	}

	public String getContaEstoque() {
		return contaEstoque;
	}

	public void setContaEstoque(String contaEstoque) {
		this.contaEstoque = contaEstoque;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public String getCustoUnitario() {
		return custoUnitario;
	}

	public void setCustoUnitario(String custoUnitario) {
		this.custoUnitario = custoUnitario;
	}

}
