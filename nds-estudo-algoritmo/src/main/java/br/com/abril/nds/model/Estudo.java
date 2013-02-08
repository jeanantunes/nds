package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.List;

public class Estudo {

    private Integer id;
    private BigDecimal reparteDistribuir; // Reparte Total a ser distribuído
    private BigDecimal reparteDistribuirInicial; // Reparte Total a ser distribuído (valor não deverá ser alterado durante o processo)
    private ProdutoEdicao produto; // produto sobre o qual se trata o estudo
    private List<ProdutoEdicao> edicoesBaseInsercaoManual;
    private List<Cota> cotas;
    private Parametro parametro; // parâmetros a serem carregados durante o estudo
    private BigDecimal somatoriaVendaMedia; // Soma da Venda Média de todas as cotas (exceto as marcadas com 'FX', 'PR' e 'RD')
    // TODO: verificar se essa somatória é do estudo total ou somente da cota (Processo: Reparte Proporcional)
    private BigDecimal somatoriaReparteEdicoesAbertas; // Soma de Reparte de todas as edições abertas

    public void calculate() {
    	somatoriaVendaMedia = new BigDecimal(0);
    	for (Cota cota : cotas) {
    		if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
    				|| !cota.getClassificacao().equals(ClassificacaoCota.BancaSoComEdicaoBaseAberta)
    				|| !cota.getClassificacao().equals(ClassificacaoCota.RedutorAutomatico))
    			somatoriaVendaMedia.add(cota.getVendaMedia());
    	}
    }
    
	public BigDecimal getReparteDistribuir() {
		return reparteDistribuir;
	}

	public void setReparteDistribuir(BigDecimal reparteDistribuir) {
		this.reparteDistribuir = reparteDistribuir;
	}

	public ProdutoEdicao getProduto() {
		return produto;
	}

	public void setProduto(ProdutoEdicao produto) {
		this.produto = produto;
	}

	public List<ProdutoEdicao> getEdicoesBaseInsercaoManual() {
	    return edicoesBaseInsercaoManual;
	}

	public void setEdicoesBaseInsercaoManual(
		List<ProdutoEdicao> edicoesBaseInsercaoManual) {
	    this.edicoesBaseInsercaoManual = edicoesBaseInsercaoManual;
	}

	public List<Cota> getCotas() {
		return cotas;
	}

	public void setCotas(List<Cota> cotas) {
		this.cotas = cotas;
	}

	public Parametro getParametro() {
		return parametro;
	}

	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}
	
	@Override
    public String toString() {
        return "\nEstudo{\n\t"
                + "id: " + id
                + ", \n\treparteCalculado: " + reparteDistribuir +"\n}";
    }

	public BigDecimal getSomatoriaVendaMedia() {
		return somatoriaVendaMedia;
	}

	public void setSomatoriaVendaMediaFinal(BigDecimal somatoriaVendaMedia) {
		this.somatoriaVendaMedia = somatoriaVendaMedia;
	}

	public BigDecimal getReparteDistribuirInicial() {
		return reparteDistribuirInicial;
	}

	public void setReparteDistribuirInicial(BigDecimal reparteDistribuirInicial) {
		this.reparteDistribuirInicial = reparteDistribuirInicial;
	}

	public BigDecimal getSomatoriaReparteEdicoesAbertas() {
		return somatoriaReparteEdicoesAbertas;
	}

	public void setSomatoriaReparteEdicoesAbertas(BigDecimal somatoriaReparteEdicoesAbertas) {
		this.somatoriaReparteEdicoesAbertas = somatoriaReparteEdicoesAbertas;
	}
}
