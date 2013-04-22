package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class AnaliseParcialDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private int cota;
	
	@Export(label = "Classificacao", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String classificacao;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String nome;
	
	@Export(label = "Quantidade PDVs", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private BigInteger npdv;
	
	@Export(label = "Reparte Sugerido", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private BigInteger reparteSugerido;
	
	@Export(label = "Legenda", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private String leg;
	
	@Export(label = "Juramento", alignment=Alignment.LEFT, exhibitionOrder = 7)
	private BigInteger juramento;
	
	@Export(label = "Media Venda", alignment=Alignment.LEFT, exhibitionOrder = 8)
	private BigDecimal mediaVenda;
	
	@Export(label = "Último Reparte", alignment=Alignment.LEFT, exhibitionOrder = 9)
	private BigInteger ultimoReparte;
	
	@Export(label = "Reparte 1", alignment=Alignment.LEFT, exhibitionOrder = 10)
	private BigInteger reparte1;
	
	@Export(label = "Venda 1", alignment=Alignment.LEFT, exhibitionOrder = 11)
	private BigDecimal venda1;
	
	@Export(label = "Reparte 2", alignment=Alignment.LEFT, exhibitionOrder = 12)
	private BigInteger reparte2;
	
	@Export(label = "Venda 2", alignment=Alignment.LEFT, exhibitionOrder = 13)
	private BigDecimal venda2;
	
	@Export(label = "Reparte 3", alignment=Alignment.LEFT, exhibitionOrder = 14)
	private BigInteger reparte3;
	
	@Export(label = "Venda 3", alignment=Alignment.LEFT, exhibitionOrder = 15)
	private BigDecimal venda3;
	
	@Export(label = "Reparte 4", alignment=Alignment.LEFT, exhibitionOrder = 16)
	private BigInteger reparte4;
	
	@Export(label = "Venda 4", alignment=Alignment.LEFT, exhibitionOrder = 17)
	private BigDecimal venda4;
	
	public int getCota() {
		return cota;
	}
	public void setCota(int cota) {
		this.cota = cota;
	}
	public String getClassificacao() {
		return classificacao;
	}
	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public long getNpdv() {
		return npdv.intValue();
	}
	public void setNpdv(BigInteger npdv) {
		this.npdv = npdv;
	}
	public double getReparteSugerido() {
		return reparteSugerido.doubleValue();
	}
	public void setReparteSugerido(BigInteger reparteSugerido) {
		this.reparteSugerido = reparteSugerido;
	}
	public String getLeg() {
		return leg;
	}
	public void setLeg(String leg) {
		this.leg = leg;
	}
	public long getJuramento() {
		return juramento.longValue();
	}
	public void setJuramento(BigInteger juramento) {
		this.juramento = juramento;
	}
	public double getMediaVenda() {
		return mediaVenda.doubleValue();
	}
	public void setMediaVenda(BigDecimal mediaVenda) {
		this.mediaVenda = mediaVenda;
	}
	public double getUltimoReparte() {
		return ultimoReparte.intValue();
	}
	public void setUltimoReparte(BigInteger ultimoReparte) {
		this.ultimoReparte = ultimoReparte;
	}
	public double getReparte1() {
		return reparte1.doubleValue();
	}
	public void setReparte1(BigInteger reparte1) {
		this.reparte1 = reparte1;
	}
	public double getVenda1() {
		return venda1.doubleValue();
	}
	public void setVenda1(BigDecimal venda1) {
		this.venda1 = venda1;
	}
	public double getReparte2() {
		return reparte2.doubleValue();
	}
	public void setReparte2(BigInteger reparte2) {
		this.reparte2 = reparte2;
	}
	public double getVenda2() {
		return venda2.doubleValue();
	}
	public void setVenda2(BigDecimal venda2) {
		this.venda2 = venda2;
	}
	public double getReparte3() {
		return reparte3.doubleValue();
	}
	public void setReparte3(BigInteger reparte3) {
		this.reparte3 = reparte3;
	}
	public double getVenda3() {
		return venda3.doubleValue();
	}
	public void setVenda3(BigDecimal venda3) {
		this.venda3 = venda3;
	}
	public double getReparte4() {
		return reparte4.doubleValue();
	}
	public void setReparte4(BigInteger reparte4) {
		this.reparte4 = reparte4;
	}
	public double getVenda4() {
		return venda4.doubleValue();
	}
	public void setVenda4(BigDecimal venda4) {
		this.venda4 = venda4;
	}
	
	
}
