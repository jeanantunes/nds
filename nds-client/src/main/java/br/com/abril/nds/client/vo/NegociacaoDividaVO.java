package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.sql.Date;

import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class NegociacaoDividaVO implements Serializable {

	private static final long serialVersionUID = -5556381641079808947L;
	
	public NegociacaoDividaVO (NegociacaoDividaDTO negociacao){
		setDtEmissao(negociacao.getDtEmissao());
		setDtVencimento(negociacao.getDtVencimento());
		setPrazo(negociacao.getPrazo());
		setVlDivida(CurrencyUtil.formatarValor(negociacao.getVlDivida()));
		setEncargos(CurrencyUtil.formatarValor(negociacao.getEncargos()));	
		setTotal(CurrencyUtil.formatarValor(negociacao.getTotal()));
		setIdDivida(negociacao.getIdDivida());
		setIdCobranca(negociacao.getIdCobranca());
	}
	
	@Export(label = "Data Emissão", alignment=Alignment.CENTER, exhibitionOrder = 1)
	private Date dtEmissao;
	
	@Export(label = "Data Vencimento", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private Date dtVencimento;
	
	@Export(label = "Prazo", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private Integer prazo;
	
	@Export(label = "Valor Dívida R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private String vlDivida;
	
	@Export(label = "Encargos", alignment=Alignment.RIGHT, exhibitionOrder = 5)
	private String encargos;
	
	@Export(label = "Total R$", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private String total;
	
	private String detalhes = "";
	
	private String acao = "";
	
	private Long idDivida;
	
	private Long idCobranca;

	public Date getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public Date getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public Integer getPrazo() {
		return prazo;
	}

	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}

	public String getVlDivida() {
		return vlDivida;
	}

	public void setVlDivida(String vlDivida) {
		this.vlDivida = vlDivida;
	}

	public String getEncargos() {
		return encargos;
	}

	public void setEncargos(String encargos) {
		this.encargos = encargos;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public Long getIdDivida() {
		return idDivida;
	}

	public void setIdDivida(Long idDivida) {
		this.idDivida = idDivida;
	}

	public Long getIdCobranca() {
		return idCobranca;
	}

	public void setIdCobranca(Long idCobranca) {
		this.idCobranca = idCobranca;
	}
	
	
	
	
}
