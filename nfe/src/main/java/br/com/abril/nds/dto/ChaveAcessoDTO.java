package br.com.abril.nds.dto;

import br.com.abril.nds.ftfutil.FTFfield;

public class ChaveAcessoDTO {
	
	private String cUF;  
    private String dataAAMM;  
    private String cnpjCpf;  
    private String mod;  
    private String serie;  
    private String nNF;  
    private String tpEmis;  
    private String cNF;
	
	@FTFfield(tamanho=9, tipo="char", ordem = 1)
	private String codEmpresa;
	
	@FTFfield(tamanho=9, tipo="char", ordem = 2)
	private String codFilial;
	
	@FTFfield(tamanho=5, tipo="char", ordem = 3)
	private String naturezaEstoque;
	
	@FTFfield(tamanho=10, tipo="char", ordem = 4)
	private String dataLancamento;
	
	@FTFfield(tamanho=20, tipo="char", ordem = 5)
	private String codMaterial;
	
	@FTFfield(tamanho=6, tipo="char", ordem = 6)
	private String tipoOperacao;
	
	@FTFfield(tamanho=21, tipo="numeric", ordem = 7)
	private String quantidade;
	
	@FTFfield(tamanho=1, tipo="char", ordem = 8)
	private String indLancamento;
	
	@FTFfield(tamanho=40, tipo="char", ordem = 9)
	private String numArquivamento;
	
	@FTFfield(tamanho=3, tipo="char", ordem = 10)
	private String tipoDocumento;
	
	@FTFfield(tamanho=15, tipo="char", ordem = 11)
	private String numDocumento;
	
	@FTFfield(tamanho=5, tipo="char", ordem = 12)
	private String serDocumento;
	
	@FTFfield(tamanho=15, tipo="char", ordem = 13)
	private String numSequencialItem;
	
	@FTFfield(tamanho=12, tipo="char", ordem = 14)
	private String numSerieMaterial;
	
	@FTFfield(tamanho=2, tipo="char", ordem = 15)
	private String categoriaPfPj;
	
	@FTFfield(tamanho=16, tipo="char", ordem = 16)
	private String codigoPfPj;
	
	@FTFfield(tamanho=3, tipo="char", ordem = 17)
	private String localizacao;
	
	@FTFfield(tamanho=22, tipo="numeric", ordem = 18)
	private String vlrUnitario;
	
	@FTFfield(tamanho=20, tipo="numeric", ordem = 19)
	private String vlrTotal;
	
	@FTFfield(tamanho=22, tipo="numeric", ordem = 20)
	private String custoUnitario;
	
	@FTFfield(tamanho=20, tipo="numeric", ordem = 21)
	private String custoTotal;
	
	@FTFfield(tamanho=28, tipo="char", ordem = 22)
	private String contaEstoque;
	
	@FTFfield(tamanho=28, tipo="char", ordem = 23)
	private String contraPartida;
	
	@FTFfield(tamanho=14, tipo="char", ordem = 24)
	private String contratoServico;
	
	@FTFfield(tamanho=28, tipo="char", ordem = 25)
	private String centroCusto;
	
	@FTFfield(tamanho=6, tipo="char", ordem = 26)
	private String cfop;
	
	@FTFfield(tamanho=20, tipo="numeric", ordem = 27)
	private String vlrIpi;
	
	@FTFfield(tamanho=10, tipo="numeric", ordem = 28)
	private String pagLivroFiscal;
	
	@FTFfield(tamanho=10, tipo="char", ordem = 29)
	private String numLote;
	
	@FTFfield(tamanho=10, tipo="char", ordem = 30)
	private String docEstornado;
	
	@FTFfield(tamanho=4, tipo="char", ordem = 31)
	private String itemEstornado;
	
	@FTFfield(tamanho=4, tipo="char", ordem = 32)
	private String divisao;
	
	@FTFfield(tamanho=4, tipo="char", ordem = 33)
	private String anoDocumento;
	
	@FTFfield(tamanho=150, tipo="char", ordem = 34)
	private String observacao;
	
	@FTFfield(tamanho=150, tipo="char", ordem = 35)
	private String openflex01;
	
	@FTFfield(tamanho=150, tipo="char", ordem = 36)
	private String openflex02;
	
	@FTFfield(tamanho=150, tipo="char", ordem = 37)
	private String openflex03;
	
	@FTFfield(tamanho=150, tipo="char", ordem = 38)
	private String openflex04;
	
	@FTFfield(tamanho=150, tipo="char", ordem = 39)
	private String openflex05;
	
	@FTFfield(tamanho=150, tipo="char", ordem = 40)
	private String openflex06;
	
	@FTFfield(tamanho=150, tipo="char", ordem = 41)
	private String openflex07;
	
	@FTFfield(tamanho=150, tipo="char", ordem = 42)
	private String openflex08;

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

	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getCodMaterial() {
		return codMaterial;
	}

	public void setCodMaterial(String codMaterial) {
		this.codMaterial = codMaterial;
	}

	public String getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public String getIndLancamento() {
		return indLancamento;
	}

	public void setIndLancamento(String indLancamento) {
		this.indLancamento = indLancamento;
	}

	public String getNumArquivamento() {
		return numArquivamento;
	}

	public void setNumArquivamento(String numArquivamento) {
		this.numArquivamento = numArquivamento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public String getSerDocumento() {
		return serDocumento;
	}

	public void setSerDocumento(String serDocumento) {
		this.serDocumento = serDocumento;
	}

	public String getNumSequencialItem() {
		return numSequencialItem;
	}

	public void setNumSequencialItem(String numSequencialItem) {
		this.numSequencialItem = numSequencialItem;
	}

	public String getNumSerieMaterial() {
		return numSerieMaterial;
	}

	public void setNumSerieMaterial(String numSerieMaterial) {
		this.numSerieMaterial = numSerieMaterial;
	}

	public String getCategoriaPfPj() {
		return categoriaPfPj;
	}

	public void setCategoriaPfPj(String categoriaPfPj) {
		this.categoriaPfPj = categoriaPfPj;
	}

	public String getCodigoPfPj() {
		return codigoPfPj;
	}

	public void setCodigoPfPj(String codigoPfPj) {
		this.codigoPfPj = codigoPfPj;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public String getVlrUnitario() {
		return vlrUnitario;
	}

	public void setVlrUnitario(String vlrUnitario) {
		this.vlrUnitario = vlrUnitario;
	}

	public String getVlrTotal() {
		return vlrTotal;
	}

	public void setVlrTotal(String vlrTotal) {
		this.vlrTotal = vlrTotal;
	}

	public String getCustoUnitario() {
		return custoUnitario;
	}

	public void setCustoUnitario(String custoUnitario) {
		this.custoUnitario = custoUnitario;
	}

	public String getCustoTotal() {
		return custoTotal;
	}

	public void setCustoTotal(String custoTotal) {
		this.custoTotal = custoTotal;
	}

	public String getContaEstoque() {
		return contaEstoque;
	}

	public void setContaEstoque(String contaEstoque) {
		this.contaEstoque = contaEstoque;
	}

	public String getContraPartida() {
		return contraPartida;
	}

	public void setContraPartida(String contraPartida) {
		this.contraPartida = contraPartida;
	}

	public String getContratoServico() {
		return contratoServico;
	}

	public void setContratoServico(String contratoServico) {
		this.contratoServico = contratoServico;
	}

	public String getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(String centroCusto) {
		this.centroCusto = centroCusto;
	}

	public String getCfop() {
		return cfop;
	}

	public void setCfop(String cfop) {
		this.cfop = cfop;
	}

	public String getVlrIpi() {
		return vlrIpi;
	}

	public void setVlrIpi(String vlrIpi) {
		this.vlrIpi = vlrIpi;
	}
	
	public String getPagLivroFiscal() {
		return pagLivroFiscal;
	}

	public void setPagLivroFiscal(String pagLivroFiscal) {
		this.pagLivroFiscal = pagLivroFiscal;
	}

	public String getNumLote() {
		return numLote;
	}

	public void setNumLote(String numLote) {
		this.numLote = numLote;
	}

	public String getDocEstornado() {
		return docEstornado;
	}

	public void setDocEstornado(String docEstornado) {
		this.docEstornado = docEstornado;
	}

	public String getItemEstornado() {
		return itemEstornado;
	}

	public void setItemEstornado(String itemEstornado) {
		this.itemEstornado = itemEstornado;
	}

	public String getDivisao() {
		return divisao;
	}

	public void setDivisao(String divisao) {
		this.divisao = divisao;
	}

	public String getAnoDocumento() {
		return anoDocumento;
	}

	public void setAnoDocumento(String anoDocumento) {
		this.anoDocumento = anoDocumento;
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

}
