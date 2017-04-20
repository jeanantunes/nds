package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class AnaliticoEncalheVO implements Serializable {

	private static final long serialVersionUID = -6737419809956273600L;
	
	@Export(label="Cota", alignment=Export.Alignment.LEFT, exhibitionOrder=1)
	private String numeroCota;
	
	@Export(label="Nome", alignment=Export.Alignment.LEFT, exhibitionOrder=2)
	private String nomeCota;
	
	@Export(label="Box Encalhe", alignment=Export.Alignment.LEFT, exhibitionOrder=3)
	private String boxEncalhe;
	
	@Export(label="Total R$", alignment=Export.Alignment.RIGHT, exhibitionOrder=4)
	private String total;
	
	@Export(label="Cobrança", alignment=Export.Alignment.LEFT, exhibitionOrder=5)
	private String statusCobranca;
	
	@Export(label="usuário", alignment=Export.Alignment.LEFT, exhibitionOrder=6)
	private String usuario;
	
	@Export(label="Início", alignment=Export.Alignment.LEFT, exhibitionOrder=7)
	private String inicio;
	
	@Export(label="Fim", alignment=Export.Alignment.LEFT, exhibitionOrder=8)
	private String fim;
	
	private Long id;
	
	private Long quantidade;
	
	public AnaliticoEncalheVO(AnaliticoEncalheVO dto) {}
	
	
	public AnaliticoEncalheVO(AnaliticoEncalheDTO dto) {
		this.setNumeroCota(dto.getNumeroCota().toString());
		this.setNomeCota(dto.getNomeCota());
		this.setBoxEncalhe(dto.getBoxEncalhe());
		this.setTotal(CurrencyUtil.formatarValor(MathUtil.round(dto.getTotal(), 2)));
		this.setStatusCobranca(dto.getStatusCobranca().toString());
		this.setUsuario(dto.getUsuario());
		this.setInicio(dto.getIncio());
		this.setFim(dto.getFim());
		this.setId(dto.getId());
		this.setQuantidade(dto.getQuantidade());
	}


	public String getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(String numeroCota) {
		this.numeroCota = numeroCota;
	}


	public String getNomeCota() {
		return nomeCota;
	}
	
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getBoxEncalhe() {
		return boxEncalhe;
	}

	public void setBoxEncalhe(String boxEncalhe) {
		this.boxEncalhe = boxEncalhe;
	}
	
	public String getTotal() {
		return total;
	}


	public void setTotal(String total) {
		this.total = total;
	}


	public String getStatusCobranca() {
		return statusCobranca;
	}


	public void setStatusCobranca(String statusCobranca) {
		this.statusCobranca = statusCobranca;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getInicio() {
		return inicio;
	}
	
	public void setInicio(Date inicio) {
		this.inicio = DateUtil.formatarHoraMinuto(inicio);
	}
	
	public String getFim() {
		return fim;
	}
	
	public void setFim(Date fim) {
		this.fim = DateUtil.formatarHoraMinuto(fim);
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
	
}