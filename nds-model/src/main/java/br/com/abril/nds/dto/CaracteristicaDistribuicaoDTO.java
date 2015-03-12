package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
@Exportable
public class CaracteristicaDistribuicaoDTO implements Serializable{

	private static final long serialVersionUID = -7289100234414430049L;

	@Export(label="Codigo",exhibitionOrder=1)
	private String codigoProduto;
	
	@Export(label="Classificação",exhibitionOrder=2)
	private String classificacao;
	
	@Export(label="Segmento",exhibitionOrder=3)
	private String segmento;
	
	@Export(label="Produto",exhibitionOrder=4)
	private String nomeProduto;
	
	@Export(label="Editor",exhibitionOrder=5)
	private String nomeEditor;
	
	@Export(label="Edição",exhibitionOrder=6)
	private BigInteger numeroEdicao;
	
	@Export(label="Chamada de Capa",exhibitionOrder=7)
	private String chamadaCapa;
	
	@Export(label="Preço de Capa",exhibitionOrder=8)
	private BigDecimal precoCapa;
	
	
	@Export(label="Lançamento",exhibitionOrder=9)
	private String dataLancamentoString;
	
	@Export(label="Recolhimento",exhibitionOrder=10)
	private String dataRecolhimentoString;
	
	@Export(label="Reparte",exhibitionOrder=11)
	private String reparteString;
	
	@Export(label="Venda",exhibitionOrder=12)
	private String vendaString;
	
	private BigInteger reparte;
	private BigInteger venda;
	private Date dataLancamento;
	private Date dataRecolhimento;
	private BigInteger idProdEd;
	private BigInteger idProd;
	
	private String statusLancamento;
	
	public BigInteger getIdProdEd() {
		return idProdEd;
	}
	public void setIdCapa(BigInteger idCapa) {
		this.idProdEd = idCapa;
	}
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getClassificacao() {
		return classificacao;
	}
	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}
	public String getSegmento() {
		return segmento;
	}
	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public String getNomeEditor() {
		return nomeEditor;
	}
	public void setNomeEditor(String nomeEditor) {
		if(nomeEditor==null){
			this.nomeEditor="";
		}else{
			this.nomeEditor = nomeEditor;
		}
	}
	public String getChamadaCapa() {
		return chamadaCapa;
	}
	public void setChamadaCapa(String chamadaCapa) {
		if(chamadaCapa==null){
			this.chamadaCapa="";
		}else{
			this.chamadaCapa = chamadaCapa;
		}
	}
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoDeCapa) {
		this.precoCapa = precoDeCapa;
	}
	public Date getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
		this.dataLancamentoString =DateUtil.formatarData(dataLancamento, "dd/MM/yyyy");
	}
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
		this.dataRecolhimentoString =DateUtil.formatarData(dataRecolhimento, "dd/MM/yyyy");
	}
	
	public void setReparte(BigInteger repartePraca) {
		this.reparte = repartePraca;
		if(repartePraca!=null){
			this.reparteString=repartePraca.toString();
		}else{
			this.reparteString="";
		}
	}
	public void setVenda(BigInteger vendaPraca) {
		this.venda = vendaPraca;
		if(vendaPraca!=null){
			this.vendaString = vendaPraca.toString();
		}else{
			this.vendaString="";
		}
	}
	public BigInteger getReparte() {
		return reparte;
	}
	public BigInteger getVenda() {
		return venda;
	}
	public BigInteger getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(BigInteger numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	public String getReparteString() {
		return reparteString;
	}
	public void setReparteString(String reparteString) {
		this.reparteString = reparteString;
	}
	public String getVendaString() {
		return vendaString;
	}
	public void setVendaString(String vendaString) {
		this.vendaString = vendaString;
	}
	public String getDataLancamentoString() {
		return dataLancamentoString;
	}
	public void setDataLancamentoString(String dataLancamentoString) {
		this.dataLancamentoString = dataLancamentoString;
	}
	public String getDataRecolhimentoString() {
		return dataRecolhimentoString;
	}
	public void setDataRecolhimentoString(String dataRecolhimentoString) {
		this.dataRecolhimentoString = dataRecolhimentoString;
	}
	public BigInteger getIdProd() {
		return idProd;
	}
	public void setIdProd(BigInteger idProd) {
		this.idProd = idProd;
	}
	public String getStatusLancamento() {
		return statusLancamento;
	}
	public void setStatusLancamento(String statusLancamento) {
		this.statusLancamento = statusLancamento;
	}
}
