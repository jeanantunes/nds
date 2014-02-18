package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class AnaliseHistogramaDTO implements Serializable {

	private static final long serialVersionUID = 23157883037782978L;

	@Export(label="Faixa de Venda", alignment=Alignment.CENTER, exhibitionOrder = 1)
	private String faixaVenda=StringUtils.EMPTY;
	
	@Export(label="Rep. Total", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private BigDecimal repTotal=BigDecimal.ZERO;
	
	@Export(label="Rep. Médio", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private BigDecimal repMedio=BigDecimal.ZERO;
	
	@Export(label="Vda Média", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private BigDecimal vdaMedio=BigDecimal.ZERO; 
	
	@Export(label="Vda Nominal", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private BigDecimal vdaTotal=BigDecimal.ZERO;
	
	@Export(label="% Vda", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private BigDecimal percVenda=BigDecimal.ZERO;
	
	@Export(label="Enc. Médio", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private BigDecimal encalheMedio=BigDecimal.ZERO;
	
	@Export(label="Part. Reparte", alignment=Alignment.CENTER, exhibitionOrder = 8)
	private BigDecimal partReparte=BigDecimal.ZERO;
	
	@Export(label="Part. Venda", alignment=Alignment.CENTER, exhibitionOrder = 9)
	private BigDecimal partVenda=BigDecimal.ZERO;
	
	@Export(label="Qtde. Cotas", alignment=Alignment.CENTER, exhibitionOrder = 10)
	private BigInteger qtdeCotas=BigInteger.ZERO;
	
	@Export(label="Cotas Esmag.", alignment=Alignment.CENTER, exhibitionOrder = 11)
	private BigInteger cotasEsmagadas=BigInteger.ZERO;
	
	@Export(label="Vda Esmag.", alignment=Alignment.CENTER , exhibitionOrder = 12)
	private BigDecimal vendaEsmagadas=BigDecimal.ZERO;
	
	private BigDecimal qtdeCotasAtivas=BigDecimal.ZERO;
	
	private BigDecimal qtdeCotasSemVenda=BigDecimal.ZERO;
	
	private BigInteger reparteDistribuido=BigInteger.ZERO;
	
	private Long qtdeTotalCotasAtivas=0l;
	
	private String idCotaStr = "";
	private String idCotasEsmagadas = "";
	private int reparteTotalDistribuidor = 0;
	
	private Integer faixaDe;
	
	private Integer faixaAte;
	
	private BigDecimal mediaEdPresente;
	
	public String getFaixaVenda() {
		return faixaVenda;
	}
	public void setFaixaVenda(String faixaVenda) {
		this.faixaVenda = faixaVenda;
	}
	public BigDecimal getRepTotal() {
		return repTotal;
	}
	public void setRepTotal(BigDecimal reparteTotal) {
		this.repTotal = reparteTotal;
	}
	public BigDecimal getRepMedio() {
		return repMedio;
	}
	public void setRepMedio(BigDecimal repMedio) {
		this.repMedio = repMedio;
	}
	public BigDecimal getVdaTotal() {
		return vdaTotal;
	}
	public void setVdaTotal(BigDecimal vdaTotal) {
		this.vdaTotal = vdaTotal;
	}
	public BigDecimal getPercVenda() {
		return percVenda;
	}
	public void setPercVenda(BigDecimal percVenda) {
		this.percVenda = percVenda;
	}
	public BigDecimal getEncalheMedio() {
		return encalheMedio;
	}
	public void setEncalheMedio(BigDecimal encalheMedio) {
		this.encalheMedio = encalheMedio;
	}
	public BigDecimal getPartReparte() {
		return partReparte;
	}
	public void setPartReparte(BigDecimal partReparte) {
		this.partReparte = partReparte;
	}
	public BigDecimal getPartVenda() {
		return partVenda;
	}
	public void setPartVenda(BigDecimal partVenda) {
		this.partVenda = partVenda;
	}
	public BigInteger getQtdeCotas() {
		return qtdeCotas;
	}
	public void setQtdeCotas(BigInteger qtdeCotas) {
		this.qtdeCotas = qtdeCotas;
	}
	public BigInteger getCotasEsmagadas() {
		return cotasEsmagadas;
	}
	public void setCotasEsmagadas(BigInteger cotasEsmagadas) {
		this.cotasEsmagadas = cotasEsmagadas;
	}
	public BigDecimal getVendaEsmagadas() {
		return vendaEsmagadas;
	}
	public void setVendaEsmagadas(BigDecimal vendaEsmagadas) {
		this.vendaEsmagadas = vendaEsmagadas;
	}
	public BigDecimal getVdaMedio() {
		return vdaMedio;
	}
	public void setVdaMedio(BigDecimal vdaMedio) {
		this.vdaMedio = vdaMedio;
	}

	public BigDecimal getQtdeCotasAtivas() {
		return qtdeCotasAtivas;
	}
	public void setQtdeCotasAtivas(BigDecimal qtdeCotasAtivas) {
		this.qtdeCotasAtivas = qtdeCotasAtivas;
	}
	
	public BigDecimal getQtdeCotasSemVenda() {
		return qtdeCotasSemVenda;
	}
	public void setQtdeCotasSemVenda(BigDecimal qtdeCotasSemVenda) {
		this.qtdeCotasSemVenda = qtdeCotasSemVenda;
	}
	
	public Long getQtdeTotalCotasAtivas() {
		return qtdeTotalCotasAtivas;
	}
	public void setQtdeTotalCotasAtivas(Long qtdeTotalCotasAtivas) {
		this.qtdeTotalCotasAtivas = qtdeTotalCotasAtivas;
	}
	
	public void executeScaleValues(int qtdEdicoes){
		
		repTotal = (repTotal==null)?BigDecimal.ZERO: repTotal.setScale(2,BigDecimal.ROUND_FLOOR);//.divide(new BigDecimal(qtdEdicoes));
		repMedio = (repMedio==null)?BigDecimal.ZERO: repMedio.setScale(2,BigDecimal.ROUND_FLOOR);//.divide(new BigDecimal(qtdEdicoes));
		vdaMedio = (vdaMedio==null)?BigDecimal.ZERO: vdaMedio.setScale(2,BigDecimal.ROUND_FLOOR);//.divide(new BigDecimal(qtdEdicoes));
		vdaTotal = (vdaTotal==null)?BigDecimal.ZERO: vdaTotal.setScale(2,BigDecimal.ROUND_FLOOR);//.divide(new BigDecimal(qtdEdicoes));
		percVenda =	(percVenda==null)?BigDecimal.ZERO: percVenda.setScale(2,BigDecimal.ROUND_FLOOR);
		
		BigDecimal encalhe = repTotal.subtract(vdaTotal);
		int qtdCotas = Integer.parseInt(this.qtdeCotas.toString());
		BigDecimal qtdCotasDecimal = new BigDecimal(qtdCotas).setScale(2,BigDecimal.ROUND_FLOOR);
		BigDecimal encalheMedioCalc = null;
		
		if (qtdCotas != 0) {
			encalheMedioCalc = encalhe.divide(qtdCotasDecimal,2, RoundingMode.HALF_UP);
		}
		
		encalheMedio = (encalheMedioCalc==null)?BigDecimal.ZERO: encalheMedioCalc;
		partReparte = (partReparte==null)?BigDecimal.ZERO: partReparte.setScale(2,BigDecimal.ROUND_FLOOR);
		partVenda =	(partVenda==null)?BigDecimal.ZERO: partVenda.setScale(2,BigDecimal.ROUND_FLOOR);
		cotasEsmagadas = (cotasEsmagadas==null)?BigInteger.ZERO: cotasEsmagadas;
		vendaEsmagadas = (vendaEsmagadas==null)?BigDecimal.ZERO: vendaEsmagadas.setScale(2,BigDecimal.ROUND_FLOOR);//.divide(new BigDecimal(qtdEdicoes));
		qtdeCotasAtivas = (qtdeCotasAtivas==null)?BigDecimal.ZERO: qtdeCotasAtivas.setScale(2,BigDecimal.ROUND_FLOOR);
	}
	public BigInteger getReparteDistribuido() {
		return reparteDistribuido;
	}
	public void setReparteDistribuido(BigInteger reparteDistribuido) {
		this.reparteDistribuido = reparteDistribuido;
	}
	public String getIdCotaStr() {
		return idCotaStr;
	}
	public void setIdCotaStr(String idCotaStr) {
		this.idCotaStr = (idCotaStr == null ? "" : idCotaStr);
	}
	public String getIdCotasEsmagadas() {
		return idCotasEsmagadas;
	}
	public void setIdCotasEsmagadas(String idCotasEsmagadas) {
		this.idCotasEsmagadas = (idCotasEsmagadas == null ? "" : idCotasEsmagadas);
	}
	public Integer getReparteTotalDistribuidor() {
		return reparteTotalDistribuidor;
	}
	public void setReparteTotalDistribuidor(int reparteTotalDistribuidor) {
		this.reparteTotalDistribuidor = reparteTotalDistribuidor;
	}
	public Integer getFaixaDe() {
		return faixaDe;
	}
	public void setFaixaDe(BigInteger faixaDe) {
		this.faixaDe = faixaDe.intValue();
	}
	public Integer getFaixaAte() {
		return faixaAte;
	}
	public void setFaixaAte(BigInteger faixaAte) {
		this.faixaAte = faixaAte.intValue();
	}
	public BigDecimal getMediaEdPresente() {
		return mediaEdPresente;
	}
	public void setMediaEdPresente(BigDecimal mediaEdPresente) {
		this.mediaEdPresente = mediaEdPresente;
	}
}
