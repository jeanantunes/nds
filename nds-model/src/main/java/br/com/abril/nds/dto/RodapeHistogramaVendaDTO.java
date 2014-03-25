package br.com.abril.nds.dto;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RodapeHistogramaVendaDTO {
	
	@Export(label = "Cotas Ativas:")
    private String cotasAtivas;
    
    @Export(label = "Cotas Produto:")
    private String cotasProduto;
    
    @Export(label = "Cotas Esmagadas:")
    private String cotasEsmagadas;
    
    @Export(label = "Venda Esmagada:")
    private String vendaEsmagada;
    
    @Export(label = "Reparte Total:")
    private String reparteTotal;
    
    @Export(label = "Reparte Distribuído:")
    private String reparteDistribuido;
    
    @Export(label = "Venda:")
    private String venda;
    
    @Export(label = "Eficiência de Venda:")
    private String eficienciaVenda;

    @Export(label = "Abrangência Distribuição:")
    private String abrangenciaDistribuicao;
    
    @Export(label = "Abrangência Venda:")
    private String abrangenciaVenda;
    
    @Export(label = "Reparte Médio:")        
    private String reparteMedio;
    
    @Export(label = "Venda Média:")
    private String vendaMedia;
    
    @Export(label = "Encalhe Médio:")
    private String encalheMedio;
    
    

	public RodapeHistogramaVendaDTO(String cotasAtivas, String cotasProduto,
			String cotasEsmagadas, String vendaEsmagada,
			String reparteTotal, String reparteDistribuido, String venda,
			String eficienciaVenda, 
			String abrangenciaDistribuicao,
			String abrangenciaVenda,
			String reparteMedio,
			String vendaMedia,
			String encalheMedio) {
		super();
		this.cotasAtivas = cotasAtivas;
		this.cotasProduto = cotasProduto;
		this.cotasEsmagadas = cotasEsmagadas;
		this.vendaEsmagada = vendaEsmagada;
		this.reparteTotal = reparteTotal;
		this.reparteDistribuido = reparteDistribuido;
		this.venda = venda;
		this.eficienciaVenda = eficienciaVenda;
		this.abrangenciaDistribuicao = abrangenciaDistribuicao;
		this.abrangenciaVenda = abrangenciaVenda;
		this.reparteMedio = reparteMedio;
		this.vendaMedia = vendaMedia;
		this.encalheMedio = encalheMedio;
	}

	public String getCotasAtivas() {
		return cotasAtivas;
	}

	public void setCotasAtivas(String cotasAtivas) {
		this.cotasAtivas = cotasAtivas;
	}

	public String getCotasProduto() {
		return cotasProduto;
	}

	public void setCotasProduto(String cotasProduto) {
		this.cotasProduto = cotasProduto;
	}

	public String getCotasEsmagadas() {
		return cotasEsmagadas;
	}

	public void setCotasEsmagadas(String cotasEsmagadas) {
		this.cotasEsmagadas = cotasEsmagadas;
	}

	public String getVendaEsmagada() {
		return vendaEsmagada;
	}

	public void setVendaEsmagada(String vendaEsmagada) {
		this.vendaEsmagada = vendaEsmagada;
	}

	public String getReparteTotal() {
		return reparteTotal;
	}

	public void setReparteTotal(String reparteTotal) {
		this.reparteTotal = reparteTotal;
	}

	public String getReparteDistribuido() {
		return reparteDistribuido;
	}

	public void setReparteDistribuido(String reparteDistribuido) {
		this.reparteDistribuido = reparteDistribuido;
	}

	public String getVenda() {
		return venda;
	}

	public void setVenda(String venda) {
		this.venda = venda;
	}

	public String getEficienciaVenda() {
		return eficienciaVenda;
	}

	public void setEficienciaVenda(String eficienciaVenda) {
		this.eficienciaVenda = eficienciaVenda;
	}

	public String getAbrangenciaDistribuicao() {
		return abrangenciaDistribuicao;
	}

	public void setAbrangenciaDistribuicao(String abrangenciaDistribuicao) {
		this.abrangenciaDistribuicao = abrangenciaDistribuicao;
	}

	public String getAbrangenciaVenda() {
		return abrangenciaVenda;
	}

	public void setAbrangenciaVenda(String abrangenciaVenda) {
		this.abrangenciaVenda = abrangenciaVenda;
	}

	public String getReparteMedio() {
		return reparteMedio;
	}

	public void setReparteMedio(String reparteMedio) {
		this.reparteMedio = reparteMedio;
	}

	public String getVendaMedia() {
		return vendaMedia;
	}

	public void setVendaMedia(String vendaMedia) {
		this.vendaMedia = vendaMedia;
	}

	public String getEncalheMedio() {
		return encalheMedio;
	}

	public void setEncalheMedio(String encalheMedio) {
		this.encalheMedio = encalheMedio;
	}
    
    
}