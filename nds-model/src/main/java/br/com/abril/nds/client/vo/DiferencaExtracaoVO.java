package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class DiferencaExtracaoVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6329780427551941318L;
	
	@Export(label = "SEQ", alignment = Alignment.CENTER, exhibitionOrder = 1, fontSize = 9, widthPercent = 7)
	private Integer seqChamadaEncalhe;
	
	@Export(label = "Código", alignment = Alignment.CENTER, exhibitionOrder = 2, fontSize = 9, widthPercent = 7)
	private String codigoProduto;

	@Export(label = "Produto", alignment = Alignment.CENTER, exhibitionOrder = 3, fontSize = 9, widthPercent = 18)
	private String descricaoProduto;

	@Export(label = "Edição", alignment = Alignment.CENTER, exhibitionOrder = 4, fontSize = 9, widthPercent = 6)
	private BigInteger numeroEdicao;

	@Export(label = "Tipo", alignment = Alignment.CENTER, exhibitionOrder = 5, fontSize = 9, widthPercent = 6)
	private String tipo;

	@Export(label = "Reparte", alignment = Alignment.CENTER, exhibitionOrder = 6, fontSize = 9, widthPercent = 6)
	private BigInteger reparte;
	
	@Export(label = "Sobra", alignment = Alignment.CENTER, exhibitionOrder = 7, fontSize = 9, widthPercent = 7, columnType = ColumnType.INTEGER)
	private Number sobra;
	
	@Export(label = "Falta", alignment = Alignment.CENTER, exhibitionOrder = 8, fontSize = 9, widthPercent = 5, columnType = ColumnType.INTEGER)
	private Number falta;
	
	@Export(label = "Ganho", alignment = Alignment.CENTER, exhibitionOrder = 9, fontSize = 9, widthPercent = 6, columnType = ColumnType.INTEGER)
	private Number ganho;
	
	@Export(label = "Perda", alignment = Alignment.CENTER, exhibitionOrder = 10, fontSize = 9, widthPercent = 7, columnType = ColumnType.INTEGER)
	private Number perda;
	
	@Export(label = "Saldo", alignment = Alignment.CENTER, exhibitionOrder = 11, fontSize = 9, widthPercent = 7, columnType = ColumnType.INTEGER)
	private Number saldo;
	
	private BigDecimal qtde;
	
	public DiferencaExtracaoVO() {
		
	}
	
	public Integer getSeqChamadaEncalhe() {
		return seqChamadaEncalhe;
	}
	
	public void setSeqChamadaEncalhe(Integer seqChamadaEncalhe) {
		this.seqChamadaEncalhe = seqChamadaEncalhe;
	}
	
	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public BigInteger getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(BigInteger numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public Number getSobra() {
		return sobra;
	}

	public void setSobra(Number sobra) {
		this.sobra = sobra == null ? BigDecimal.ZERO : sobra.intValue();
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public BigDecimal getQtde() {
		return qtde;
	}

	public void setQtde(BigDecimal qtde) {
		this.qtde = qtde;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public Number getFalta() {
		return falta;
	}

	public void setFalta(Number falta) {
		this.falta = falta == null ? BigDecimal.ZERO : falta.intValue();
	}

	public Number getGanho() {
		return ganho;
	}

	public void setGanho(Number ganho) {
		this.ganho = ganho == null ? 0 : ganho.intValue();
	}

	public Number getPerda() {
		return perda;
	}

	public void setPerda(Number perda) {
		this.perda = perda == null ? 0 : perda.intValue();
	}

	public Number getSaldo() {
		return saldo;
	}

	public void setSaldo(Number saldo) {
		this.saldo = (reparte.intValue() + sobra.intValue() - falta.intValue()) ;
	}
}