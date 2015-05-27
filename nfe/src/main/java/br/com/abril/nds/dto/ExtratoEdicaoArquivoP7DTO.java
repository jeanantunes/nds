package br.com.abril.nds.dto;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;
import br.com.abril.nds.util.export.Delimiter;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
@Delimiter("|")
public class ExtratoEdicaoArquivoP7DTO extends FTFBaseDTO {
	
	@FTFfield(tamanho=9, tipo="char", ordem = 1)
	@Export(label = "Codigo Empresa", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String cod_empresa="";
	
	@FTFfield(tamanho=9, tipo="char", ordem = 2)
	@Export(label = "Cod Filial", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String cod_filial="";
	
	@FTFfield(tamanho=5, tipo="char", ordem = 3)
	@Export(label = "Natureza do Estoque", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String nat_estoque="180";
	
	@FTFfield(tamanho=28, tipo="char", ordem = 4)
	@Export(label = "Centro de Custo", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String centro_custo="@";
	
	@FTFfield(tamanho=10, tipo="char", ordem = 5)
	@Export(label = "Data do Inventário", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private String dat_inventario="";
	
	@FTFfield(tamanho=20, tipo="char", ordem = 6)
	@Export(label = "Codigo do Material", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private String cod_material="";
	
	@FTFfield(tamanho=10, tipo="char", ordem = 7)
	@Export(label = "Classificação Fiscal", alignment=Alignment.LEFT, exhibitionOrder = 7)
	private String classif_fiscal="";
	
	@FTFfield(tamanho=3, tipo="char", ordem = 8)
	@Export(label = "Localização do Estoque", alignment=Alignment.LEFT, exhibitionOrder = 8)
	private String local_estoque="1";
	
	@FTFfield(tamanho=28, tipo="char", ordem = 9)
	@Export(label = "Conta de estoque", alignment=Alignment.LEFT, exhibitionOrder = 9)
	private String cta_estoque="1140301004";

	@FTFfield(tamanho=17, tipo="numeric", ordem = 10)
	@Export(label = "Quantidade", alignment=Alignment.LEFT, exhibitionOrder = 10)
	private String quantidade="";
	
	@FTFfield(tamanho=3, tipo="char", ordem = 11)
	@Export(label = "Unidade de Medida", alignment=Alignment.LEFT, exhibitionOrder = 11)
	private String unidade_medida="UN";
	
	@FTFfield(tamanho=21, tipo="numeric", ordem = 12)
	@Export(label = "Custo Unitário", alignment=Alignment.LEFT, exhibitionOrder = 12)
	private String custo_unitario="";
	
	@FTFfield(tamanho=19, tipo="numeric", ordem = 13)
	@Export(label = "Custo Total", alignment=Alignment.LEFT, exhibitionOrder = 13)
	private String custo_total="";
	
	@FTFfield(tamanho=4, tipo="char", ordem = 14)
	@Export(label = "Divisão", alignment=Alignment.LEFT, exhibitionOrder = 14)
	private String divisao="";
	
	@FTFfield(tamanho=150, tipo="char", ordem = 15)
	@Export(label = "Observação", alignment=Alignment.LEFT, exhibitionOrder = 15)
	private String observacao="";
	
	@FTFfield(tamanho=150, tipo="char", ordem = 16)
	@Export(label = "openflex01", alignment=Alignment.LEFT, exhibitionOrder = 16)
	private String openflex01="NDS";
	
	@FTFfield(tamanho=150, tipo="char", ordem = 17)
	@Export(label = "openflex02", alignment=Alignment.LEFT, exhibitionOrder = 17)
	private String openflex02="";
	
	@FTFfield(tamanho=150, tipo="char", ordem = 18)
	@Export(label = "openflex03", alignment=Alignment.LEFT, exhibitionOrder = 18)
	private String openflex03="";
	
	@FTFfield(tamanho=150, tipo="char", ordem = 19)
	@Export(label = "openflex04", alignment=Alignment.LEFT, exhibitionOrder = 19)
	private String openflex04="NDS";
	
	@FTFfield(tamanho=150, tipo="char", ordem = 20)
	@Export(label = "openflex05", alignment=Alignment.LEFT, exhibitionOrder = 20)
	private String openflex05="";
	
	@FTFfield(tamanho=17, tipo="char", ordem = 21)
	@Export(label = "openflex06", alignment=Alignment.LEFT, exhibitionOrder = 21)
	private String openflex06="";
	
	@FTFfield(tamanho=17, tipo="char", ordem = 22)
	@Export(label = "openflex07", alignment=Alignment.LEFT, exhibitionOrder = 22)
	private String openflex07="";
	
	@FTFfield(tamanho=17, tipo="char", ordem = 23)
	@Export(label = "openflex08", alignment=Alignment.LEFT, exhibitionOrder = 23)
	private String openflex08="";
	

	public String getCod_empresa() {
		return cod_empresa;
	}

	public void setCod_empresa(String cod_empresa) {
		this.cod_empresa = cod_empresa;
	}

	public String getCod_filial() {
		return cod_filial;
	}

	public void setCod_filial(String cod_filial) {
		this.cod_filial = cod_filial;
	}

	public String getNat_estoque() {
		return nat_estoque;
	}

	public void setNat_estoque(String nat_estoque) {
		this.nat_estoque = nat_estoque;
	}

	public String getCentro_custo() {
		return centro_custo;
	}

	public void setCentro_custo(String centro_custo) {
		this.centro_custo = centro_custo;
	}

	public String getClassif_fiscal() {
		return classif_fiscal;
	}

	public void setClassif_fiscal(String classif_fiscal) {
		this.classif_fiscal = classif_fiscal;
	}

	public String getLocal_estoque() {
		return local_estoque;
	}

	public void setLocal_estoque(String local_estoque) {
		this.local_estoque = local_estoque;
	}

	public String getCta_estoque() {
		return cta_estoque;
	}

	public void setCta_estoque(String cta_estoque) {
		this.cta_estoque = cta_estoque;
	}

	public String getUnidade_medida() {
		return unidade_medida;
	}

	public void setUnidade_medida(String unidade_medida) {
		this.unidade_medida = unidade_medida;
	}

	public String getDivisao() {
		return divisao;
	}

	public void setDivisao(String divisao) {
		this.divisao = divisao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getOpenflex01() {
		return openflex01;
	}

	public void setOpenflex01(String openflex01) {
		this.openflex01 = openflex01;
	}

	public String getOpenflex02() {
		return openflex02;
	}

	public void setOpenflex02(String openflex02) {
		this.openflex02 = openflex02;
	}

	public String getOpenflex03() {
		return openflex03;
	}

	public void setOpenflex03(String openflex03) {
		this.openflex03 = openflex03;
	}

	public String getOpenflex04() {
		return openflex04;
	}

	public void setOpenflex04(String openflex04) {
		this.openflex04 = openflex04;
	}

	public String getOpenflex05() {
		return openflex05;
	}

	public void setOpenflex05(String openflex05) {
		this.openflex05 = openflex05;
	}

	public String getOpenflex06() {
		return openflex06;
	}

	public void setOpenflex06(String openflex06) {
		this.openflex06 = openflex06;
	}

	public String getOpenflex07() {
		return openflex07;
	}

	public void setOpenflex07(String openflex07) {
		this.openflex07 = openflex07;
	}

	public String getOpenflex08() {
		return openflex08;
	}

	public void setOpenflex08(String openflex08) {
		this.openflex08 = openflex08;
	}

	public String getCod_material() {
		return cod_material;
	}

	public void setCod_material(String cod_material) {
		this.cod_material = cod_material;
	}

	public String getCusto_unitario() {
		return custo_unitario;
	}

	public void setCusto_unitario(String custo_unitario) {
		this.custo_unitario = custo_unitario;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public String getCusto_total() {
		return custo_total;
	}

	public void setCusto_total(String custo_total) {
		this.custo_total = custo_total;
	}

	public String getDat_inventario() {
		return dat_inventario;
	}

	public void setDat_inventario(String dat_inventario) {
		this.dat_inventario = dat_inventario;
	}
	
	
}
