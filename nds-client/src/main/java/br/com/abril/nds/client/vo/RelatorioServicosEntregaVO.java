package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.RelatorioServicosEntregaDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RelatorioServicosEntregaVO {
	
	private String transportadorId;

	@Export(label="Transportador", alignment=Alignment.LEFT, exhibitionOrder=1)
	private String nomeTransportador;
	
	@Export(label="Roteiro", alignment=Alignment.LEFT, exhibitionOrder=2)
	private String descricaoRoteiro;
	
	@Export(label="Rota", alignment=Alignment.LEFT, exhibitionOrder=3)
	private String descricaoRota;
	
	@Export(label="Cota", alignment=Alignment.LEFT, exhibitionOrder=4)
	private String numeroCota;
	
	@Export(label="Nome", alignment=Alignment.LEFT, exhibitionOrder=5)
	private String nomeJornaleiro;
	
	@Export(label="Valor R$", alignment=Alignment.RIGHT, exhibitionOrder=6)
	private String valor;
	
	
	public RelatorioServicosEntregaVO()
	{}
	
	
	public RelatorioServicosEntregaVO(RelatorioServicosEntregaDTO dto)
	{
		this.transportadorId = dto.getTransportadorId().toString();
		this.nomeTransportador = dto.getNomeTransportador();
		this.descricaoRoteiro = dto.getDescricaoRoteiro();
		this.descricaoRota = dto.getDescricaoRota();
		this.numeroCota = dto.getNumeroCota().toString();
		this.nomeJornaleiro = dto.getNomeJornaleiro();
		this.valor = CurrencyUtil.formatarValor(dto.getValor());
	}
	
	
	
	public String getTransportadorId() {
		return transportadorId;
	}
	public void setTransportadorId(String transportadorId) {
		this.transportadorId = transportadorId;
	}
	public String getNomeTransportador() {
		return nomeTransportador;
	}
	public void setNomeTransportador(String nomeTransportador) {
		this.nomeTransportador = nomeTransportador;
	}
	public String getDescricaoRoteiro() {
		return descricaoRoteiro;
	}
	public void setDescricaoRoteiro(String descricaoRoteiro) {
		this.descricaoRoteiro = descricaoRoteiro;
	}
	public String getDescricaoRota() {
		return descricaoRota;
	}
	public void setDescricaoRota(String descricaoRota) {
		this.descricaoRota = descricaoRota;
	}
	public String getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(String numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getNomeJornaleiro() {
		return nomeJornaleiro;
	}
	public void setNomeJornaleiro(String nomeJornaleiro) {
		this.nomeJornaleiro = nomeJornaleiro;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
}
