package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0137InputItem extends IntegracaoDocumentDetail implements Serializable {
	

	/**
	 * reenvio
	 */
	private static final long serialVersionUID = 1L;

	private ItemCEPKInput ceItemPK;
	
	private IcdLancamentoEdicaoPublicacaoInput lancamentoEdicaoPublicacao;
	
	private Date dataRecolhimento;
	
	private Long numeroDocumento;
	
	private Long codigoLancamentoEdicao;

	private Long codigoValeDesconto;

	private Long quantidadeEnviada;

	private Long codigoFormaDevolucao;
	
	private Integer numeroControle;
	
	private String codigoRegimeRecolhimento;
	
	private BigDecimal valorPrecoUnitario;
	
	private Long quantidadeDevolucaoApurada;
	
	private Long quantidadeVendaApurada;
	
	private BigDecimal valorVendaApurada;

	private Long quantidadeDevolucaoInformada;
	
	private Long quantidadeVendaInformada;
	
	private BigDecimal valorVendaInformada;

	private Long numeroNotaEnvio;
	
	private String codigoNotaEnvioMultipla;
	
	private String tipoProduto;
	
	private String codigoPreenchimento;
	
	private String tipoStatus;
	
	private Long quantidadeDevolucaoParcial;
	
	private Long numeroEdicaoValeDesconto;
	
	private BigDecimal valorMargemInformado;

	private BigDecimal valorMargemApurado;
	
	private String numeroAcessoNotaEnvio;
	
	private Date dataEmissaoNotaEnvio;
	
	private Long tipoModeloNotaEnvio;
	
	private Long codigoSerieNotaEnvio;

	public ItemCEPKInput getCeItemPK() {
		return ceItemPK;
	}

	public void setCeItemPK(ItemCEPKInput ceItemPK) {
		this.ceItemPK = ceItemPK;
	}

	public IcdLancamentoEdicaoPublicacaoInput getLancamentoEdicaoPublicacao() {
		return lancamentoEdicaoPublicacao;
	}

	public void setLancamentoEdicaoPublicacao(IcdLancamentoEdicaoPublicacaoInput lancamentoEdicaoPublicacao) {
		this.lancamentoEdicaoPublicacao = lancamentoEdicaoPublicacao;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	public Long getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(Long numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public Long getCodigoLancamentoEdicao() {
		return codigoLancamentoEdicao;
	}

	public void setCodigoLancamentoEdicao(Long codigoLancamentoEdicao) {
		this.codigoLancamentoEdicao = codigoLancamentoEdicao;
	}

	public Long getCodigoValeDesconto() {
		return codigoValeDesconto;
	}

	public void setCodigoValeDesconto(Long codigoValeDesconto) {
		this.codigoValeDesconto = codigoValeDesconto;
	}

	public Long getQuantidadeEnviada() {
		return quantidadeEnviada;
	}

	public void setQuantidadeEnviada(Long quantidadeEnviada) {
		this.quantidadeEnviada = quantidadeEnviada;
	}

	public Long getCodigoFormaDevolucao() {
		return codigoFormaDevolucao;
	}

	public void setCodigoFormaDevolucao(Long codigoFormaDevolucao) {
		this.codigoFormaDevolucao = codigoFormaDevolucao;
	}

	public Integer getNumeroControle() {
		return numeroControle;
	}

	public void setNumeroControle(Integer numeroControle) {
		this.numeroControle = numeroControle;
	}

	public String getCodigoRegimeRecolhimento() {
		return codigoRegimeRecolhimento;
	}

	public void setCodigoRegimeRecolhimento(String codigoRegimeRecolhimento) {
		this.codigoRegimeRecolhimento = codigoRegimeRecolhimento;
	}

	public BigDecimal getValorPrecoUnitario() {
		return valorPrecoUnitario;
	}

	public void setValorPrecoUnitario(BigDecimal valorPrecoUnitario) {
		this.valorPrecoUnitario = valorPrecoUnitario;
	}

	public Long getQuantidadeDevolucaoApurada() {
		return quantidadeDevolucaoApurada;
	}

	public void setQuantidadeDevolucaoApurada(Long quantidadeDevolucaoApurada) {
		this.quantidadeDevolucaoApurada = quantidadeDevolucaoApurada;
	}

	public Long getQuantidadeVendaApurada() {
		return quantidadeVendaApurada;
	}

	public void setQuantidadeVendaApurada(Long quantidadeVendaApurada) {
		this.quantidadeVendaApurada = quantidadeVendaApurada;
	}

	public BigDecimal getValorVendaApurada() {
		return valorVendaApurada;
	}

	public void setValorVendaApurada(BigDecimal valorVendaApurada) {
		this.valorVendaApurada = valorVendaApurada;
	}

	public Long getQuantidadeDevolucaoInformada() {
		return quantidadeDevolucaoInformada;
	}

	public void setQuantidadeDevolucaoInformada(Long quantidadeDevolucaoInformada) {
		this.quantidadeDevolucaoInformada = quantidadeDevolucaoInformada;
	}

	public Long getQuantidadeVendaInformada() {
		return quantidadeVendaInformada;
	}

	public void setQuantidadeVendaInformada(Long quantidadeVendaInformada) {
		this.quantidadeVendaInformada = quantidadeVendaInformada;
	}

	public BigDecimal getValorVendaInformada() {
		return valorVendaInformada;
	}

	public void setValorVendaInformada(BigDecimal valorVendaInformada) {
		this.valorVendaInformada = valorVendaInformada;
	}

	public Long getNumeroNotaEnvio() {
		return numeroNotaEnvio;
	}

	public void setNumeroNotaEnvio(Long numeroNotaEnvio) {
		this.numeroNotaEnvio = numeroNotaEnvio;
	}

	public String getCodigoNotaEnvioMultipla() {
		return codigoNotaEnvioMultipla;
	}

	public void setCodigoNotaEnvioMultipla(String codigoNotaEnvioMultipla) {
		this.codigoNotaEnvioMultipla = codigoNotaEnvioMultipla;
	}

	public String getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(String tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public String getCodigoPreenchimento() {
		return codigoPreenchimento;
	}

	public void setCodigoPreenchimento(String codigoPreenchimento) {
		this.codigoPreenchimento = codigoPreenchimento;
	}

	public String getTipoStatus() {
		return tipoStatus;
	}

	public void setTipoStatus(String tipoStatus) {
		this.tipoStatus = tipoStatus;
	}

	public Long getQuantidadeDevolucaoParcial() {
		return quantidadeDevolucaoParcial;
	}

	public void setQuantidadeDevolucaoParcial(Long quantidadeDevolucaoParcial) {
		this.quantidadeDevolucaoParcial = quantidadeDevolucaoParcial;
	}

	public Long getNumeroEdicaoValeDesconto() {
		return numeroEdicaoValeDesconto;
	}

	public void setNumeroEdicaoValeDesconto(Long numeroEdicaoValeDesconto) {
		this.numeroEdicaoValeDesconto = numeroEdicaoValeDesconto;
	}

	public BigDecimal getValorMargemInformado() {
		return valorMargemInformado;
	}

	public void setValorMargemInformado(BigDecimal valorMargemInformado) {
		this.valorMargemInformado = valorMargemInformado;
	}

	public BigDecimal getValorMargemApurado() {
		return valorMargemApurado;
	}

	public void setValorMargemApurado(BigDecimal valorMargemApurado) {
		this.valorMargemApurado = valorMargemApurado;
	}

	public String getNumeroAcessoNotaEnvio() {
		return numeroAcessoNotaEnvio;
	}

	public void setNumeroAcessoNotaEnvio(String numeroAcessoNotaEnvio) {
		this.numeroAcessoNotaEnvio = numeroAcessoNotaEnvio;
	}

	public Date getDataEmissaoNotaEnvio() {
		return dataEmissaoNotaEnvio;
	}

	public void setDataEmissaoNotaEnvio(Date dataEmissaoNotaEnvio) {
		this.dataEmissaoNotaEnvio = dataEmissaoNotaEnvio;
	}

	public Long getTipoModeloNotaEnvio() {
		return tipoModeloNotaEnvio;
	}

	public void setTipoModeloNotaEnvio(Long tipoModeloNotaEnvio) {
		this.tipoModeloNotaEnvio = tipoModeloNotaEnvio;
	}

	public Long getCodigoSerieNotaEnvio() {
		return codigoSerieNotaEnvio;
	}

	public void setCodigoSerieNotaEnvio(Long codigoSerieNotaEnvio) {
		this.codigoSerieNotaEnvio = codigoSerieNotaEnvio;
	}
	
	public class ItemCEPKInput {
		
		private Long numeroChamadaEncalhe;
		
		private Integer numeroItem;

		public Long getNumeroChamadaEncalhe() {
			return numeroChamadaEncalhe;
		}

		public void setNumeroChamadaEncalhe(Long numeroChamadaEncalhe) {
			this.numeroChamadaEncalhe = numeroChamadaEncalhe;
		}

		public Integer getNumeroItem() {
			return numeroItem;
		}

		public void setNumeroItem(Integer numeroItem) {
			this.numeroItem = numeroItem;
		}
		
	}
	
	public class IcdLancamentoEdicaoPublicacaoInput {
		
		private Long codigoLancamentoEdicao;
		
		private String codigoPublicacao;
		
		private Integer numeroEdicao;

		public Long getCodigoLancamentoEdicao() {
			return codigoLancamentoEdicao;
		}

		public void setCodigoLancamentoEdicao(Long codigoLancamentoEdicao) {
			this.codigoLancamentoEdicao = codigoLancamentoEdicao;
		}

		public String getCodigoPublicacao() {
			return codigoPublicacao;
		}

		public void setCodigoPublicacao(String codigoPublicacao) {
			this.codigoPublicacao = codigoPublicacao;
		}

		public Integer getNumeroEdicao() {
			return numeroEdicao;
		}

		public void setNumeroEdicao(Integer numeroEdicao) {
			this.numeroEdicao = numeroEdicao;
		}
		
	}
	
}