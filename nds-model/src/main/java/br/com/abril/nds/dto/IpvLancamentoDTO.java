package br.com.abril.nds.dto;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;

public class IpvLancamentoDTO extends FTFBaseDTO{
	
	@FTFfield(tamanho=2, tipo="numeric", ordem=1)
	private String versao = "01";
	
	@FTFfield(tamanho=1, tipo="char", ordem=2)
	private String tipoArquivo = "L";
	
	@FTFfield(tamanho=7, tipo="numeric", ordem=3)
	private String codDistribuidor;
	
	@FTFfield(tamanho=7, tipo="numeric", ordem=4)
	private String codJornaleiro;
	
	@FTFfield(tamanho=5, tipo="numeric", ordem=5)
	private String codCota;
	
	@FTFfield(tamanho=5, tipo="numeric", ordem=6)
	private String codPDV;
	
	@FTFfield(tamanho=8, tipo="char", ordem=7)
	private String dataMovimento;
	
	@FTFfield(tamanho=8, tipo="numeric", ordem=8)
	private String codProduto;
	
	@FTFfield(tamanho=4, tipo="numeric", ordem=9)
	private String numEdicao;
	
	@FTFfield(tamanho=18, tipo="numeric", ordem=10)
	private String codBarras;
	
	@FTFfield(tamanho=30, tipo="char", ordem=11)
	private String nomeProduto;
	
	@FTFfield(tamanho=8, tipo="numeric", ordem=12)
	private String reparte;
	
	@FTFfield(tamanho=35, tipo="char", ordem=13 )
	private String nomeEditora;
	
	@FTFfield(tamanho=10, tipo="numeric", ordem=14)
	private String precoCapa;
	
	@FTFfield(tamanho=10, tipo="numeric", ordem=15)
	private String precoCusto;
	
	@FTFfield(tamanho=30, tipo="char", ordem=16)
	private String chamadaCapa;
	
	@FTFfield(tamanho=8, tipo="char", ordem=17)
	private String dataLancamento;
	
	@FTFfield(tamanho=8, tipo="char", ordem=18)
	private String dataPrimeiroLancamentoParcial;

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}

	public String getCodDistribuidor() {
		return codDistribuidor;
	}

	public void setCodDistribuidor(String codDistribuidor) {
		this.codDistribuidor = codDistribuidor;
	}

	public String getCodJornaleiro() {
		return codJornaleiro;
	}

	public void setCodJornaleiro(String codJornaleiro) {
		this.codJornaleiro = codJornaleiro;
	}

	public String getCodCota() {
		return codCota;
	}

	public void setCodCota(String codCota) {
		this.codCota = codCota;
	}

	public String getCodPDV() {
		return codPDV;
	}

	public void setCodPDV(String codPDV) {
		this.codPDV = codPDV;
	}

	public String getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(String dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	public String getCodProduto() {
		return codProduto;
	}

	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}

	public String getNumEdicao() {
		return numEdicao;
	}

	public void setNumEdicao(String numEdicao) {
		this.numEdicao = numEdicao;
	}

	public String getCodBarras() {
		return codBarras;
	}

	public void setCodBarras(String codBarras) {
		this.codBarras = codBarras;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getReparte() {
		return reparte;
	}

	public void setReparte(String reparte) {
		this.reparte = reparte;
	}

	public String getNomeEditora() {
		return nomeEditora;
	}

	public void setNomeEditora(String nomeEditora) {
		this.nomeEditora = nomeEditora;
	}

	public String getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(String precoCapa) {
		this.precoCapa = precoCapa;
	}

	public String getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(String precoCusto) {
		this.precoCusto = precoCusto;
	}

	public String getChamadaCapa() {
		return chamadaCapa;
	}

	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = chamadaCapa;
	}

	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getDataPrimeiroLancamentoParcial() {
		return dataPrimeiroLancamentoParcial;
	}

	public void setDataPrimeiroLancamentoParcial(
			String dataPrimeiroLancamentoParcial) {
		this.dataPrimeiroLancamentoParcial = dataPrimeiroLancamentoParcial;
	}
	
}
