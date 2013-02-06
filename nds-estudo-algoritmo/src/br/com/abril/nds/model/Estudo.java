package br.com.abril.nds.model;

import java.util.List;

public class Estudo {

    private Integer id;
    private Integer reparteDistribuido;
    private Produto produto;
    private List<EdicaoBase> edicoesBase;
    private List<Cota> cotas;
    private Parametro parametro;

	public Integer getReparteDistribuido() {
		return reparteDistribuido;
	}

	public void setReparteDistribuido(Integer reparteDistribuido) {
		this.reparteDistribuido = reparteDistribuido;
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
	
//	@Override
//    public String toString() {
//        return "\nEstudo{\n\t"
//                + "id: " + id
//                + ", \n\treparteCalculado: " + reparteDistribuido +"\n}";
//    }
}
