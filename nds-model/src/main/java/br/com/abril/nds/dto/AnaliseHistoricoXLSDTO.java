package br.com.abril.nds.dto;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class AnaliseHistoricoXLSDTO {

	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota = 0;
	
	@Export(label = "Status", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private SituacaoCadastro statusCota;
	
	private String statusCotaFormatado = "";
	
	@Export(label = "Tipo Distrib.", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String tipoDistribuicaoCota = "";
	
	@Export(label = "Tipo", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String tipoPDVPrincipal = "";
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private String nomePessoa = "";
	
	@Export(label = "Tipo Logradouro", alignment = Alignment.LEFT, exhibitionOrder = 6)
    private String tipoLogradouro;
    
    @Export(label = "Logradouro", alignment = Alignment.LEFT, exhibitionOrder = 7)
    private String logradouro;
    
    @Export(label = "Número", alignment = Alignment.LEFT, exhibitionOrder = 8)
    private String numeroEndereco;
    
    @Export(label = "Complemento", alignment = Alignment.LEFT, exhibitionOrder = 9)
    private String complemento;
    
    @Export(label = "Bairro", alignment = Alignment.LEFT, exhibitionOrder = 10)
    private String bairro;
    
    @Export(label = "Cidade", alignment = Alignment.LEFT, exhibitionOrder = 11)
    private String cidade;
    
    @Export(label = "CEP", alignment = Alignment.LEFT, exhibitionOrder = 12)
    private String cep;
    
    @Export(label = "Telefone", alignment = Alignment.LEFT, exhibitionOrder = 13)
    private String telefone;
	
	@Export(label = "NPDV", alignment=Alignment.LEFT, exhibitionOrder = 14)
	private Long qtdPdv = 0l;
	
	private Double reparteMedio;
	
	@Export(label = "REP Cota", alignment=Alignment.LEFT, exhibitionOrder = 15)
	private String reparteMedioFormat;
	
	private Double vendaMedia;
	
	@Export(label = "VDA Cota", alignment=Alignment.LEFT, exhibitionOrder = 16)
	private String vendaMediaFormat;

	// Edição 1
	@Export(label = "Produto 01", alignment=Alignment.LEFT, exhibitionOrder = 17)
	private String produto01;
	
	@Export(label = "REP 1", alignment=Alignment.LEFT, exhibitionOrder = 18)
	private Integer ed1Reparte;
	
	@Export(label = "VDA 1", alignment=Alignment.LEFT, exhibitionOrder = 19)
	private Integer ed1Venda;

	// Edição 2
	@Export(label = "Produto 02", alignment=Alignment.LEFT, exhibitionOrder = 20)
	private String produto02;
	
	@Export(label = "REP 2", alignment=Alignment.LEFT, exhibitionOrder = 21)
	private Integer ed2Reparte;
	
	@Export(label = "VDA 2", alignment=Alignment.LEFT, exhibitionOrder = 22)
	private Integer ed2Venda;

	// Edição 3
	@Export(label = "Produto 03", alignment=Alignment.LEFT, exhibitionOrder = 23)
	private String produto03;
	
	@Export(label = "REP 3", alignment=Alignment.LEFT, exhibitionOrder = 24)
	private Integer ed3Reparte;
	
	@Export(label = "VDA 3", alignment=Alignment.LEFT, exhibitionOrder = 25)
	private Integer ed3Venda;

	// Edição 4
	@Export(label = "Produto 04", alignment=Alignment.LEFT, exhibitionOrder = 26)
	private String produto04;
	
	@Export(label = "REP 4", alignment=Alignment.LEFT, exhibitionOrder = 27)
	private Integer ed4Reparte;
	
	@Export(label = "VDA 4", alignment=Alignment.LEFT, exhibitionOrder = 28)
	private Integer ed4Venda;

	// Edição 5
	@Export(label = "Produto 05", alignment=Alignment.LEFT, exhibitionOrder = 29)
	private String produto05;
	
	@Export(label = "REP 5", alignment=Alignment.LEFT, exhibitionOrder = 30)
	private Integer ed5Reparte;
	
	@Export(label = "VDA 5", alignment=Alignment.LEFT, exhibitionOrder = 31)
	private Integer ed5Venda;

	// Edição 6
	@Export(label = "Produto 06", alignment=Alignment.LEFT, exhibitionOrder = 32)
	private String produto06;
	
	@Export(label = "REP 6", alignment=Alignment.LEFT, exhibitionOrder = 33)
	private Integer ed6Reparte;
	
	@Export(label = "VDA 6", alignment=Alignment.LEFT, exhibitionOrder = 34)
	private Integer ed6Venda;
	
	private String codigoProduto;
	
	private Long numeroEdicao;

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public SituacaoCadastro getStatusCota() {
		return statusCota;
	}

	public void setStatusCota(SituacaoCadastro statusCota) {
		this.statusCota = statusCota;
	}

	public String getStatusCotaFormatado() {
		return statusCotaFormatado;
	}

	public void setStatusCotaFormatado(String statusCotaFormatado) {
		this.statusCotaFormatado = statusCotaFormatado;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public Long getQtdPdv() {
		return qtdPdv;
	}

	public void setQtdPdv(Long qtdPdv) {
		this.qtdPdv = qtdPdv;
	}

	public void setReparteMedio(Double reparteMedio) {
		this.reparteMedio = reparteMedio;
		this.reparteMedioFormat = CurrencyUtil.formatarValor(reparteMedio);
	}
	
	public Double getReparteMedio(){
		return this.reparteMedio;
	}

	public void setVendaMedia(Double vendaMedia) {
		
		this.vendaMedia = vendaMedia;
		this.vendaMediaFormat = CurrencyUtil.formatarValor(vendaMedia);
	}
	
	public Double getVendaMedia(){
		return this.vendaMedia;
	}

	

	public String getReparteMedioFormat() {
		return reparteMedioFormat;
	}
	
	public void setReparteMedioFormat(String reparteMedioFormatado) {
		this.reparteMedioFormat = reparteMedioFormatado;
	}

	public String getVendaMediaFormat() {
		return vendaMediaFormat;
	}
	
	public void setVendaMediaFormat(String vendaMediaFormatado) {
		this.vendaMediaFormat = vendaMediaFormatado;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public void setEd6Venda(int ed6Venda) {
		this.ed6Venda = ed6Venda;
	}

	public Integer getEd1Reparte() {
		return ed1Reparte;
	}

	public void setEd1Reparte(Integer ed1Reparte) {
		this.ed1Reparte = ed1Reparte;
	}

	public Integer getEd1Venda() {
		return ed1Venda;
	}

	public void setEd1Venda(Integer ed1Venda) {
		this.ed1Venda = ed1Venda;
	}

	public Integer getEd2Reparte() {
		return ed2Reparte;
	}

	public void setEd2Reparte(Integer ed2Reparte) {
		this.ed2Reparte = ed2Reparte;
	}

	public Integer getEd2Venda() {
		return ed2Venda;
	}

	public void setEd2Venda(Integer ed2Venda) {
		this.ed2Venda = ed2Venda;
	}

	public Integer getEd3Reparte() {
		return ed3Reparte;
	}

	public void setEd3Reparte(Integer ed3Reparte) {
		this.ed3Reparte = ed3Reparte;
	}

	public Integer getEd3Venda() {
		return ed3Venda;
	}

	public void setEd3Venda(Integer ed3Venda) {
		this.ed3Venda = ed3Venda;
	}

	public Integer getEd4Reparte() {
		return ed4Reparte;
	}

	public void setEd4Reparte(Integer ed4Reparte) {
		this.ed4Reparte = ed4Reparte;
	}

	public Integer getEd4Venda() {
		return ed4Venda;
	}

	public void setEd4Venda(Integer ed4Venda) {
		this.ed4Venda = ed4Venda;
	}

	public Integer getEd5Reparte() {
		return ed5Reparte;
	}

	public void setEd5Reparte(Integer ed5Reparte) {
		this.ed5Reparte = ed5Reparte;
	}

	public Integer getEd5Venda() {
		return ed5Venda;
	}

	public void setEd5Venda(Integer ed5Venda) {
		this.ed5Venda = ed5Venda;
	}

	public Integer getEd6Reparte() {
		return ed6Reparte;
	}

	public void setEd6Reparte(Integer ed6Reparte) {
		this.ed6Reparte = ed6Reparte;
	}

	public Integer getEd6Venda() {
		return ed6Venda;
	}

	public String getTipoDistribuicaoCota() {
		return tipoDistribuicaoCota;
	}

	public void setTipoDistribuicaoCota(String tipoDistribuicaoCota) {
		this.tipoDistribuicaoCota = tipoDistribuicaoCota;
	}

	public String getTipoPDVPrincipal() {
		return tipoPDVPrincipal;
	}

	public void setTipoPDVPrincipal(String tipoPDVPrincipal) {
		this.tipoPDVPrincipal = tipoPDVPrincipal;
	}

	public String getProduto01() {
		return produto01;
	}

	public void setProduto01(String produto01) {
		this.produto01 = produto01;
	}

	public String getProduto02() {
		return produto02;
	}

	public void setProduto02(String produto02) {
		this.produto02 = produto02;
	}

	public String getProduto03() {
		return produto03;
	}

	public void setProduto03(String produto03) {
		this.produto03 = produto03;
	}

	public String getProduto04() {
		return produto04;
	}

	public void setProduto04(String produto04) {
		this.produto04 = produto04;
	}

	public String getProduto05() {
		return produto05;
	}

	public void setProduto05(String produto05) {
		this.produto05 = produto05;
	}

	public String getProduto06() {
		return produto06;
	}

	public void setProduto06(String produto06) {
		this.produto06 = produto06;
	}
	
}
