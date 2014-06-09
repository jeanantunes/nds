package br.com.abril.nds.dto.chamadaencalhe.integracao;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO com as informações do Item da Chamada de Encalhe do Fornecedor para
 * recolhimento dos produtos do Distribuidor
 * 
 * @author francisco.garcia
 * 
 */
public class ChamadaEncalheFornecedorIntegracaoItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long numeroChamadaEncalhe;
    
    private Integer numeroItem;
    
    private Long quantidadeEnviada;
    
    private Long quantidadeDevolucaoInformada;
    
    private Long quantidadeVendaInformada;
    
    private String numeroAcessoNotaEnvio;
    
    private Date dataEmissaoNotaEnvio;
    
    private Integer tipoModeloNotaEnvio;
    
    private Integer serieNotaEnvio;

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

	public Long getQuantidadeEnviada() {
		return quantidadeEnviada;
	}

	public void setQuantidadeEnviada(Long quantidadeEnviada) {
		this.quantidadeEnviada = quantidadeEnviada;
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

	public String getNumeroAcessoNotaEnvio() {
		return numeroAcessoNotaEnvio;
	}

	public void setNumeroAcessoNotaEnvio(String numeroAcessoNotaEnvio) {
		this.numeroAcessoNotaEnvio = numeroAcessoNotaEnvio;
	}

	public Date getDataEmissaoNotaEnvio() {
		return dataEmissaoNotaEnvio;
	}

	public void setDataEmissaoNotaEnvio(Date dataEmissaoNotaenvio) {
		this.dataEmissaoNotaEnvio = dataEmissaoNotaenvio;
	}

	public Integer getTipoModeloNotaEnvio() {
		return tipoModeloNotaEnvio;
	}

	public void setTipoModeloNotaEnvio(Integer tipoModeloNotaEnvio) {
		this.tipoModeloNotaEnvio = tipoModeloNotaEnvio;
	}

	public Integer getSerieNotaEnvio() {
		return serieNotaEnvio;
	}

	public void setSerieNotaEnvio(Integer serieNotaEnvio) {
		this.serieNotaEnvio = serieNotaEnvio;
	}
    
}
