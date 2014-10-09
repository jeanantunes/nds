package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class RegiaoCotaDTO extends FiltroDTO  implements Serializable {

	private static final long serialVersionUID = 913156078576193397L;
	
	private Long cotaId;
	private Long registroCotaRegiaoId;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeCota;
	
	@Export(label = "Tipo PDV", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String tipoPDV = "";
	
	@Export(label = "Status", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private SituacaoCadastro tipoStatus;
	
	@Export(label = "Bairro", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private String bairro = " ";
	
	@Export(label = "Cidade", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private String cidade = "";
	
	@Export(label = "Faturamento", alignment=Alignment.LEFT, exhibitionOrder = 7)
	private String faturamento;
	
	@Export(label = "Usuário", alignment=Alignment.LEFT, exhibitionOrder = 8)
	private String nomeUsuario;
	
	@Export(label = "Data", alignment=Alignment.LEFT, exhibitionOrder = 9)
	private String data;
	
	@Export(label = "Hora", alignment=Alignment.LEFT, exhibitionOrder = 10)
	private String hora;
	
	private Pessoa pessoa;
	private TipoDistribuicaoCota tipoDistribuicaoCota;
	
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

	public String getTipoPDV() {
		return tipoPDV;
	}
	
	public void setTipoPDV(String tipoPDV) {
		if (tipoPDV == null){
			this.tipoPDV = " ";
		}else{
			this.tipoPDV = tipoPDV;
		}
	}
	
	public SituacaoCadastro getTipoStatus() {
		return tipoStatus;
	}

	public void setTipoStatus(SituacaoCadastro tipoStatus) {
		this.tipoStatus = tipoStatus;
	}

	public String getBairro() {
		return bairro;
	}
	
	public void setBairro(String bairro) {
		if (bairro == null){
			this.bairro = " ";
		}else{
			this.bairro = bairro;
		}
	}
	
	public String getCidade() {
		return cidade;
	}
	
	public void setCidade(String cidade) {
	this.cidade = cidade;
	}
	
	public String getFaturamento() {
		return faturamento;
	}
	
	public void setFaturamento(BigDecimal faturamento) {
		this.faturamento = CurrencyUtil.formatarValor(faturamento);
	}
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = DateUtil.formatarDataPTBR(data);
		this.hora = DateUtil.formatarHoraMinuto(data);
	}
	
	public String getHora() {
		return hora;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Long getRegistroCotaRegiaoId() {
		return registroCotaRegiaoId;
	}

	public void setRegistroCotaRegiaoId(Long registroCotaRegiaoId) {
		this.registroCotaRegiaoId = registroCotaRegiaoId;
	}

	public Long getCotaId() {
		return cotaId;
	}

	public void setCotaId(Long cotaId) {
		this.cotaId = cotaId;
	}

	public TipoDistribuicaoCota getTipoDistribuicaoCota() {
		return tipoDistribuicaoCota;
	}

	public void setTipoDistribuicaoCota(TipoDistribuicaoCota tipoDistribuicaoCota) {
		this.tipoDistribuicaoCota = tipoDistribuicaoCota;
		this.tipoPDV = tipoDistribuicaoCota.toString();
	}
	
}
