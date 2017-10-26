package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.dto.RegistroCurvaABCDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * Classe responsável por armazenar os valores referente aos registros da
 * pesquisa de registra de curva ABC do distribuidor.
 * @author InfoA2
 */
@Exportable
public class RegistroCurvaABCDistribuidorVO extends RegistroCurvaABCDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3849170873913801404L;

	private Integer numeroCota;

	@Export(label = "Cota", exhibitionOrder = 4)
	private String nomeCota;

	private Long quantidadePdvs;

	@Export(label = "Municipio", exhibitionOrder = 5)
	private String municipio;

	private BigInteger vendaExemplares;

	private BigDecimal faturamentoCapa;

	@Export(label = "Venda de Exemplares", exhibitionOrder = 7, columnType = ColumnType.NUMBER)
	private String vendaExemplaresFormatado;
	
	@Export(label = "Faturamento da Capa", exhibitionOrder = 8, columnType = ColumnType.DECIMAL)
	private String faturamentoCapaFormatado;
	
	@Export(label = "RK.Produto", exhibitionOrder = 1, columnType = ColumnType.NUMBER)
	private Long rkProduto;
	
	@Export(label = "RK. Cota", exhibitionOrder = 2,  columnType = ColumnType.NUMBER)
	private Long rkCota;
	
	private Long idProduto;
	
	private Long idCota;
	

	@Export(label = "Número", exhibitionOrder = 3, columnType = ColumnType.NUMBER)
	public String getNumeroCotaString() {
		return this.getNumeroCota().toString();
	}
	
	@Export(label = "segmento", exhibitionOrder = 11,   columnType = ColumnType.STRING)
	private String segmento;
	
	@Export(label = "produtoCodigo", exhibitionOrder = 12,  columnType = ColumnType.STRING)
	private String produtoCodigo;
	
	@Export(label = "produtoNome", exhibitionOrder = 13,  columnType = ColumnType.STRING)
	private String produtoNome;
	
	@Export(label = "editorCodigo", exhibitionOrder = 14,  columnType = ColumnType.STRING)
	private String editorCodigo;
	
	@Export(label = "codigoBarra", exhibitionOrder = 15, columnType = ColumnType.STRING)
	private String codigoBarra;
	
	@Export(label = "peb", exhibitionOrder = 16,  columnType = ColumnType.STRING)
	private String peb;
	
	@Export(label = "numeroEdicao", exhibitionOrder = 17,  columnType = ColumnType.STRING)
	private String numeroEdicao;
	
	@Export(label = "dataLancamento", exhibitionOrder = 18, columnType = ColumnType.STRING)
	private String dataLancamento;
	
	@Export(label = "dataRecolhimento", exhibitionOrder = 19,  columnType = ColumnType.STRING)
	private String dataRecolhimento;
	
	@Export(label = "classificacao", exhibitionOrder = 20,  columnType = ColumnType.STRING)
	private String classificacao;
	
	@Export(label = "venda", exhibitionOrder = 21,  columnType = ColumnType.STRING)
	private String venda;
	
	@Export(label = "preco", exhibitionOrder = 22,   columnType = ColumnType.STRING)
	private String preco;
	
	private BigDecimal reparte;
	
	private String reparteFormatado;
	
	private String tipoPDV;
	
	@Export(label = "Quantidade de Pdvs", exhibitionOrder = 6, columnType = ColumnType.NUMBER)
	public String getQuantidadePdvsString() {
		return this.getQuantidadePdvs().toString();
	}

	@Export(label = "% Participação", exhibitionOrder = 9, columnType = ColumnType.DECIMAL)
	public String getParticipacaoString() {
		return getParticipacaoFormatado();
	}

	@Export(label = "% Participação Acumulada", exhibitionOrder = 10, columnType = ColumnType.DECIMAL)
	public String getParticipacaoAcumuladaString() {
		return getParticipacaoAcumuladaFormatado();
	}
	
	public RegistroCurvaABCDistribuidorVO() {
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public Long getRkProduto() {
		return rkProduto;
	}

	public void setRkProduto(Long rkProduto) {
		this.rkProduto = rkProduto;
	}

	public Long getRkCota() {
		return rkCota;
	}

	public void setRkCota(Long rkCota) {
		this.rkCota = rkCota;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public Long getQuantidadePdvs() {
		return quantidadePdvs;
	}

	public void setQuantidadePdvs(Long quantidadePdvs) {
		this.quantidadePdvs = quantidadePdvs;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public BigInteger getVendaExemplares() {
		return vendaExemplares;
	}

	public void setVendaExemplares(BigInteger vendaExemplares) {
		this.vendaExemplares = vendaExemplares == null ? BigInteger.ZERO : vendaExemplares;
		this.vendaExemplaresFormatado = String.valueOf(this.vendaExemplares);
	}

	public BigDecimal getFaturamentoCapa() {
		return faturamentoCapa;
		
	}

	public void setFaturamentoCapa(BigDecimal faturamentoCapa) {
		this.faturamentoCapa = faturamentoCapa == null ? BigDecimal.ZERO : faturamentoCapa;
		this.faturamentoCapaFormatado = CurrencyUtil.formatarValor(faturamentoCapa);
	}
	public String getVendaExemplaresFormatado() {
		return vendaExemplaresFormatado;
	}

	public void setVendaExemplaresFormatado(String vendaExemplaresFormatado) {
		this.vendaExemplaresFormatado = vendaExemplaresFormatado;
	}

	public String getFaturamentoCapaFormatado() {
		return faturamentoCapaFormatado;
	}

	public void setFaturamentoCapaFormatado(String faturamentoCapaFormatado) {
		this.faturamentoCapaFormatado = faturamentoCapaFormatado;
	}

	public String getSegmento() {
		return segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	public String getProdutoCodigo() {
		return produtoCodigo;
	}

	public void setProdutoCodigo(String produtoCodigo) {
		this.produtoCodigo = produtoCodigo;
	}

	public String getProdutoNome() {
		return produtoNome;
	}

	public void setProdutoNome(String produtoNome) {
		this.produtoNome = produtoNome;
	}

	public String getEditorCodigo() {
		return editorCodigo;
	}

	public void setEditorCodigo(String editorCodigo) {
		this.editorCodigo = editorCodigo;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getPeb() {
		return peb;
	}

	public void setPeb(String peb) {
		this.peb = peb;
	}

	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public String getVenda() {
		return venda;
	}

	public void setVenda(String venda) {
		this.venda = venda;
	}

	public String getPreco() {
		return preco;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}

	public String getTipoPDV() {
		return tipoPDV;
	}

	public void setTipoPDV(String tipoPDV) {
		this.tipoPDV = tipoPDV;
	}

	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte == null ? BigDecimal.ZERO : reparte;
		this.reparteFormatado = String.valueOf(this.reparte);
	}

	public String getReparteFormatado() {
		return reparteFormatado;
	}

	public void setReparteFormatado(String reparteFormatado) {
		this.reparteFormatado = reparteFormatado;
	}
	
}
