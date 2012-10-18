package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;


@Exportable
public class ContasAPagarParcialVO {

	@Export(label="Lcto", alignment=Alignment.CENTER, exhibitionOrder = 1)
	private String lcto;
	
	@Export(label="Rctl", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String rclt;
	
	@Export(label="Reparte", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String reparte;
	
	@Export(label="Suplementação", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private String suplementacao;
	
	@Export(label="Encalhe", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private String encalhe;
	
	@Export(label="Venda", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private String venda;
	
	@Export(label="% Venda", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private String pctVenda;
	
	@Export(label="Venda CE", alignment=Alignment.CENTER, exhibitionOrder = 8)
	private String vendaCe;
	
	@Export(label="Reparte Acum.", alignment=Alignment.CENTER, exhibitionOrder = 9)
	private String reparteAcum;
	
	@Export(label="Venda Acum.", alignment=Alignment.CENTER, exhibitionOrder = 10)
	private String vendaAcum;
	
	@Export(label="% Venda Acum.", alignment=Alignment.CENTER, exhibitionOrder = 11)
	private String pctVendaAcum;
	
	@Export(label="N° NF-e", alignment=Alignment.CENTER, exhibitionOrder = 12)
	private String nfe;
	
	public ContasAPagarParcialVO() 
	{}
	
	public ContasAPagarParcialVO(ContasAPagarParcialDTO dto) {
		
		this.lcto = DateUtil.formatarDataPTBR(dto.getLcto());
		this.rclt = DateUtil.formatarDataPTBR(dto.getRclt());
		this.reparte = dto.getReparte().toString();
		this.suplementacao = dto.getSuplementacao().toString();
		this.encalhe = dto.getEncalhe().toString();
		this.venda = dto.getVenda().toString();
		this.pctVenda = CurrencyUtil.formatarValor(dto.getPctVenda());
		this.vendaCe = dto.getVendaCe().toString();
		this.reparteAcum = dto.getReparteAcum().toString();
		this.vendaAcum = dto.getVendaAcum().toString();
		this.pctVendaAcum = CurrencyUtil.formatarValor(dto.getPctVendaAcum());
		this.nfe = dto.getNfe();
	}
	
	
	public String getLcto() {
		return lcto;
	}
	public void setLcto(String lcto) {
		this.lcto = lcto;
	}
	public String getRclt() {
		return rclt;
	}
	public void setRclt(String rclt) {
		this.rclt = rclt;
	}
	public String getReparte() {
		return reparte;
	}
	public void setReparte(String reparte) {
		this.reparte = reparte;
	}
	public String getSuplementacao() {
		return suplementacao;
	}
	public void setSuplementacao(String suplementacao) {
		this.suplementacao = suplementacao;
	}
	public String getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(String encalhe) {
		this.encalhe = encalhe;
	}
	public String getVenda() {
		return venda;
	}
	public void setVenda(String venda) {
		this.venda = venda;
	}
	public String getPctVenda() {
		return pctVenda;
	}
	public void setPctVenda(String pctVenda) {
		this.pctVenda = pctVenda;
	}
	public String getVendaCe() {
		return vendaCe;
	}
	public void setVendaCe(String vendaCe) {
		this.vendaCe = vendaCe;
	}
	public String getReparteAcum() {
		return reparteAcum;
	}
	public void setReparteAcum(String reparteAcum) {
		this.reparteAcum = reparteAcum;
	}
	public String getVendaAcum() {
		return vendaAcum;
	}
	public void setVendaAcum(String vendaAcum) {
		this.vendaAcum = vendaAcum;
	}
	public String getPctVendaAcum() {
		return pctVendaAcum;
	}
	public void setPctVendaAcum(String pctVendaAcum) {
		this.pctVendaAcum = pctVendaAcum;
	}
	public String getNfe() {
		return nfe;
	}
	public void setNfe(String nfe) {
		this.nfe = nfe;
	}
}
