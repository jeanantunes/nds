package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.List;

public class Estudo {

    private Integer id;
    private BigDecimal reparteDistribuir;
    private Produto produto;
    private List<EdicaoBase> edicoesBase;
    private List<Cota> cotas;
    private Parametro parametro;
    private BigDecimal somatoriaVendaMediaFinal;

    public void calculate() {
    	somatoriaVendaMediaFinal = new BigDecimal(0);
    	for (Cota cota : cotas) {
    		if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
    				|| !cota.getClassificacao().equals(ClassificacaoCota.BancaSoComEdicaoBaseAberta)
    				|| !cota.getClassificacao().equals(ClassificacaoCota.RedutorAutomatico))
    			somatoriaVendaMediaFinal.add(cota.getVendaMediaFinal());
    	}
    }
    
	public BigDecimal getReparteDistribuir() {
		return reparteDistribuir;
	}

	public void setReparteDistribuir(BigDecimal reparteDistribuir) {
		this.reparteDistribuir = reparteDistribuir;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<EdicaoBase> getEdicoesBase() {
		return edicoesBase;
	}

	public void setEdicoesBase(List<EdicaoBase> edicoesBase) {
		this.edicoesBase = edicoesBase;
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

	public BigDecimal getSomatoriaVendaMediaFinal() {
		return somatoriaVendaMediaFinal;
	}

	public void setSomatoriaVendaMediaFinal(BigDecimal somatoriaVendaMediaFinal) {
		this.somatoriaVendaMediaFinal = somatoriaVendaMediaFinal;
	}
}
