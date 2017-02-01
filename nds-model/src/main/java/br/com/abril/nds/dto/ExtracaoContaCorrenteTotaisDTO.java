package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class ExtracaoContaCorrenteTotaisDTO implements Serializable {

	private static final long serialVersionUID = 5764706436811529282L;

	private BigInteger totalReparte = BigInteger.ZERO;
	private BigInteger totalVendaEnc = BigInteger.ZERO;
	private BigInteger totalEncalhe = BigInteger.ZERO;
	private BigInteger totalVenda = BigInteger.ZERO;
	private BigDecimal totalVendaTotal = BigDecimal.ZERO;

	private BigInteger qtdRemessa = BigInteger.ZERO;
	private BigInteger qtdDevolucao = BigInteger.ZERO;
	private BigInteger qtdVenda = BigInteger.ZERO;
	private BigDecimal brutoRemessa = BigDecimal.ZERO;
	private BigDecimal brutoDevolucao = BigDecimal.ZERO;
	private BigDecimal brutoVenda = BigDecimal.ZERO;
	private BigDecimal descontoCotaRemessa = BigDecimal.ZERO;
	private BigDecimal descontoCotaDevolucao = BigDecimal.ZERO;
	private BigDecimal descontoCotaVenda = BigDecimal.ZERO;
	private BigDecimal descontoDistribRemessa = BigDecimal.ZERO;
	private BigDecimal descontoDistribDevolucao = BigDecimal.ZERO;
	private BigDecimal descontoDistribVenda = BigDecimal.ZERO;
	private BigDecimal liquidoCotaRemessa = BigDecimal.ZERO;
	private BigDecimal liquidoCotaDevolucao = BigDecimal.ZERO;
	private BigDecimal liquidoCotaVenda = BigDecimal.ZERO;
	private BigDecimal liquidoDistribRemessa = BigDecimal.ZERO;
	private BigDecimal liquidoDistribDevolucao = BigDecimal.ZERO;
	private BigDecimal liquidoDistribVenda = BigDecimal.ZERO;
	
	
	public static ExtracaoContaCorrenteTotaisDTO processarTotais(List<ExtracaoContaCorrenteDTO> listExtracoes){
		
		ExtracaoContaCorrenteTotaisDTO totaisReturn = new ExtracaoContaCorrenteTotaisDTO();
		
		for (ExtracaoContaCorrenteDTO dto : listExtracoes) {

			totaisReturn.setQtdRemessa(totaisReturn.getQtdRemessa().add(dto.getReparte()));
			totaisReturn.setQtdDevolucao(totaisReturn.getQtdDevolucao().add(dto.getEncalhe()));
			totaisReturn.setBrutoRemessa(totaisReturn.getBrutoRemessa().add(dto.getPrecoCapa().multiply(new BigDecimal(dto.getReparte()))));
			totaisReturn.setBrutoDevolucao(totaisReturn.getBrutoDevolucao().add(dto.getPrecoCapa().multiply(new BigDecimal(dto.getEncalhe()))));
			totaisReturn.setDescontoCotaRemessa(totaisReturn.getDescontoCotaRemessa().add(dto.getDescCota().multiply(new BigDecimal(dto.getReparte()))));
			totaisReturn.setDescontoCotaDevolucao(totaisReturn.getDescontoCotaDevolucao().add(dto.getDescCota().multiply(new BigDecimal(dto.getEncalhe()))));
			totaisReturn.setDescontoDistribRemessa(totaisReturn.getDescontoDistribRemessa().add(dto.getDescLogistica().multiply(new BigDecimal(dto.getReparte()))));
			totaisReturn.setDescontoDistribDevolucao(totaisReturn.getDescontoDistribDevolucao().add(dto.getDescLogistica().multiply(new BigDecimal(dto.getEncalhe()))));
			
			totaisReturn.setTotalReparte(totaisReturn.getTotalReparte().add(dto.getReparte()));
			totaisReturn.setTotalVendaEnc(totaisReturn.getTotalVendaEnc().add(dto.getVendaEncalhe()));
			totaisReturn.setTotalEncalhe(totaisReturn.getTotalEncalhe().add(dto.getEncalhe()));
			totaisReturn.setTotalVenda(totaisReturn.getTotalVenda().add(dto.getVenda()));
			totaisReturn.setTotalVendaTotal(totaisReturn.getTotalVendaTotal().add(dto.getVendaTotal()));
			
		}
		
		totaisReturn.setQtdVenda(totaisReturn.getQtdRemessa().subtract(totaisReturn.getQtdDevolucao()));
		totaisReturn.setBrutoVenda(totaisReturn.getBrutoRemessa().subtract(totaisReturn.getBrutoDevolucao()));
		totaisReturn.setDescontoCotaVenda(totaisReturn.getDescontoCotaRemessa().subtract(totaisReturn.getDescontoCotaDevolucao()));
		totaisReturn.setDescontoDistribVenda(totaisReturn.getDescontoDistribRemessa().subtract(totaisReturn.getDescontoDistribDevolucao()));
		
		totaisReturn.setLiquidoCotaRemessa(totaisReturn.getBrutoRemessa().subtract(totaisReturn.getDescontoCotaRemessa()));
		totaisReturn.setLiquidoCotaDevolucao(totaisReturn.getBrutoDevolucao().subtract(totaisReturn.getDescontoCotaDevolucao()));
		totaisReturn.setLiquidoCotaVenda(totaisReturn.getBrutoVenda().subtract(totaisReturn.getDescontoCotaVenda()));
		
		totaisReturn.setLiquidoDistribRemessa(totaisReturn.getBrutoRemessa().subtract(totaisReturn.getDescontoDistribRemessa()));
		totaisReturn.setLiquidoDistribDevolucao(totaisReturn.getBrutoDevolucao().subtract(totaisReturn.getDescontoDistribDevolucao()));
		totaisReturn.setLiquidoDistribVenda(totaisReturn.getBrutoVenda().subtract(totaisReturn.getDescontoDistribVenda()));
		
		return totaisReturn;
	}
	
	public BigInteger getTotalReparte() {
		return totalReparte;
	}
	public void setTotalReparte(BigInteger totalReparte) {
		this.totalReparte = totalReparte;
	}
	public BigInteger getTotalVendaEnc() {
		return totalVendaEnc;
	}
	public void setTotalVendaEnc(BigInteger totalVendaEnc) {
		this.totalVendaEnc = totalVendaEnc;
	}
	public BigInteger getTotalEncalhe() {
		return totalEncalhe;
	}
	public void setTotalEncalhe(BigInteger totalEncalhe) {
		this.totalEncalhe = totalEncalhe;
	}
	public BigInteger getTotalVenda() {
		return totalVenda;
	}
	public void setTotalVenda(BigInteger totalVenda) {
		this.totalVenda = totalVenda;
	}
	public BigDecimal getTotalVendaTotal() {
		return totalVendaTotal;
	}
	public void setTotalVendaTotal(BigDecimal totalVendaTotal) {
		this.totalVendaTotal = totalVendaTotal;
	}
	public BigInteger getQtdRemessa() {
		return qtdRemessa;
	}
	public void setQtdRemessa(BigInteger qtdRemessa) {
		this.qtdRemessa = qtdRemessa;
	}
	public BigInteger getQtdDevolucao() {
		return qtdDevolucao;
	}
	public void setQtdDevolucao(BigInteger qtdDevolucao) {
		this.qtdDevolucao = qtdDevolucao;
	}
	public BigInteger getQtdVenda() {
		return qtdVenda;
	}
	public void setQtdVenda(BigInteger qtdVenda) {
		this.qtdVenda = qtdVenda;
	}
	public BigDecimal getBrutoRemessa() {
		return brutoRemessa;
	}
	public void setBrutoRemessa(BigDecimal brutoRemessa) {
		this.brutoRemessa = brutoRemessa;
	}
	public BigDecimal getBrutoDevolucao() {
		return brutoDevolucao;
	}
	public void setBrutoDevolucao(BigDecimal brutoDevolucao) {
		this.brutoDevolucao = brutoDevolucao;
	}
	public BigDecimal getBrutoVenda() {
		return brutoVenda;
	}
	public void setBrutoVenda(BigDecimal brutoVenda) {
		this.brutoVenda = brutoVenda;
	}
	public BigDecimal getDescontoCotaRemessa() {
		return descontoCotaRemessa;
	}
	public void setDescontoCotaRemessa(BigDecimal descontoCotaRemessa) {
		this.descontoCotaRemessa = descontoCotaRemessa;
	}
	public BigDecimal getDescontoCotaDevolucao() {
		return descontoCotaDevolucao;
	}
	public void setDescontoCotaDevolucao(BigDecimal descontoCotaDevolucao) {
		this.descontoCotaDevolucao = descontoCotaDevolucao;
	}
	public BigDecimal getDescontoCotaVenda() {
		return descontoCotaVenda;
	}
	public void setDescontoCotaVenda(BigDecimal descontoCotaVenda) {
		this.descontoCotaVenda = descontoCotaVenda;
	}
	public BigDecimal getDescontoDistribRemessa() {
		return descontoDistribRemessa;
	}
	public void setDescontoDistribRemessa(BigDecimal descontoDistribRemessa) {
		this.descontoDistribRemessa = descontoDistribRemessa;
	}
	public BigDecimal getDescontoDistribDevolucao() {
		return descontoDistribDevolucao;
	}
	public void setDescontoDistribDevolucao(BigDecimal descontoDistribDevolucao) {
		this.descontoDistribDevolucao = descontoDistribDevolucao;
	}
	public BigDecimal getDescontoDistribVenda() {
		return descontoDistribVenda;
	}
	public void setDescontoDistribVenda(BigDecimal descontoDistribVenda) {
		this.descontoDistribVenda = descontoDistribVenda;
	}
	public BigDecimal getLiquidoCotaRemessa() {
		return liquidoCotaRemessa;
	}
	public void setLiquidoCotaRemessa(BigDecimal liquidoCotaRemessa) {
		this.liquidoCotaRemessa = liquidoCotaRemessa;
	}
	public BigDecimal getLiquidoCotaDevolucao() {
		return liquidoCotaDevolucao;
	}
	public void setLiquidoCotaDevolucao(BigDecimal liquidoCotaDevolucao) {
		this.liquidoCotaDevolucao = liquidoCotaDevolucao;
	}
	public BigDecimal getLiquidoCotaVenda() {
		return liquidoCotaVenda;
	}
	public void setLiquidoCotaVenda(BigDecimal liquidoCotaVenda) {
		this.liquidoCotaVenda = liquidoCotaVenda;
	}
	public BigDecimal getLiquidoDistribRemessa() {
		return liquidoDistribRemessa;
	}
	public void setLiquidoDistribRemessa(BigDecimal liquidoDistribRemessa) {
		this.liquidoDistribRemessa = liquidoDistribRemessa;
	}
	public BigDecimal getLiquidoDistribDevolucao() {
		return liquidoDistribDevolucao;
	}
	public void setLiquidoDistribDevolucao(BigDecimal liquidoDistribDevolucao) {
		this.liquidoDistribDevolucao = liquidoDistribDevolucao;
	}
	public BigDecimal getLiquidoDistribVenda() {
		return liquidoDistribVenda;
	}
	public void setLiquidoDistribVenda(BigDecimal liquidoDistribVenda) {
		this.liquidoDistribVenda = liquidoDistribVenda;
	}
	
	
}
