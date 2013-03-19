<<<<<<< HEAD
package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class AnaliseHistogramaDTO implements Serializable {

	@Export(label="Faixa de Venda", alignment=Alignment.CENTER)
	private String faixaVenda=StringUtils.EMPTY;
	
	@Export(label="Rep. Total", alignment=Alignment.CENTER)
	private BigDecimal repTotal=BigDecimal.ZERO;
	
	@Export(label="Rep. Médio", alignment=Alignment.CENTER)
	private BigDecimal repMedio=BigDecimal.ZERO;
	
	@Export(label="Vda Média", alignment=Alignment.CENTER)
	private BigDecimal vdaMedio=BigDecimal.ZERO; 
	
	@Export(label="Vda Nominal", alignment=Alignment.CENTER)
	private BigDecimal vdaTotal=BigDecimal.ZERO;
	
	@Export(label="% Vda", alignment=Alignment.CENTER)
	private BigDecimal percVenda=BigDecimal.ZERO;
	
	@Export(label="Enc. Médio", alignment=Alignment.CENTER)
	private BigDecimal encalheMedio=BigDecimal.ZERO;
	
	@Export(label="Part. Reparte", alignment=Alignment.CENTER)
	private BigDecimal partReparte=BigDecimal.ZERO;
	
	@Export(label="Part. Venda", alignment=Alignment.CENTER)
	private BigDecimal partVenda=BigDecimal.ZERO;
	
	@Export(label="Qtde. Cotas", alignment=Alignment.CENTER)
	private BigInteger qtdeCotas=BigInteger.ZERO;
	
	@Export(label="Cotas Esmag.", alignment=Alignment.CENTER)
	private BigDecimal cotasEsmagadas=BigDecimal.ZERO;
	
	@Export(label="Vda Esmag.", alignment=Alignment.CENTER)
	private BigDecimal vendaEsmagadas=BigDecimal.ZERO;
	
	private BigDecimal qtdeCotasAtivas=BigDecimal.ZERO;
	
	private BigDecimal qtdeCotasSemVenda=BigDecimal.ZERO;
	
	private BigInteger reparteDistribuido=BigInteger.ZERO;
	
	private Long qtdeTotalCotasAtivas=0l;
	
	private String idCotaStr;
	
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
	public BigDecimal getCotasEsmagadas() {
		return cotasEsmagadas;
	}
	public void setCotasEsmagadas(BigDecimal cotasEsmagadas) {
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
	
	public void executeScaleValues(){
		
		repTotal = (repTotal==null)?BigDecimal.ZERO:repTotal.setScale(2,BigDecimal.ROUND_FLOOR);
		repMedio = (repMedio==null)?BigDecimal.ZERO: repMedio.setScale(2,BigDecimal.ROUND_FLOOR);
		vdaMedio = (vdaMedio==null)?BigDecimal.ZERO: vdaMedio.setScale(2,BigDecimal.ROUND_FLOOR);
		vdaTotal = (vdaTotal==null)?BigDecimal.ZERO: vdaTotal.setScale(2,BigDecimal.ROUND_FLOOR);
		percVenda =	(percVenda==null)?BigDecimal.ZERO: percVenda.setScale(2,BigDecimal.ROUND_FLOOR);
		encalheMedio = (encalheMedio==null)?BigDecimal.ZERO: encalheMedio.setScale(2,BigDecimal.ROUND_FLOOR);
		partReparte = (partReparte==null)?BigDecimal.ZERO: partReparte.setScale(2,BigDecimal.ROUND_FLOOR);
		partVenda =	(partVenda==null)?BigDecimal.ZERO: partVenda.setScale(2,BigDecimal.ROUND_FLOOR);
		cotasEsmagadas = (cotasEsmagadas==null)?BigDecimal.ZERO: cotasEsmagadas.setScale(2,BigDecimal.ROUND_FLOOR);
		vendaEsmagadas = (vendaEsmagadas==null)?BigDecimal.ZERO: vendaEsmagadas.setScale(2,BigDecimal.ROUND_FLOOR);
		qtdeCotasAtivas = (qtdeCotasAtivas==null)?BigDecimal.ZERO: qtdeCotasAtivas.setScale(2,BigDecimal.ROUND_FLOOR);
//		reparteDistribuido = reparteDistribuido.setScale(2,BigDecimal.ROUND_FLOOR);
		
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
		this.idCotaStr = idCotaStr;
	}
	

	
}
