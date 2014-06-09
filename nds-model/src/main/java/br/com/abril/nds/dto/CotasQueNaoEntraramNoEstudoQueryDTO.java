package br.com.abril.nds.dto;

import br.com.abril.nds.vo.PaginacaoVO;

public class CotasQueNaoEntraramNoEstudoQueryDTO {

    private Long estudo;
    private Long cota;
    private String nome;
    private String motivo;
    private String elemento;
    private Long tipoSegmentoProduto;
    
    private PaginacaoVO paginacao;

    public Long getEstudo() {
	return estudo;
    }

    public void setEstudo(Long estudo) {
	this.estudo = estudo;
    }

    public Long getCota() {
	return cota;
    }

    public void setCota(Long cota) {
	this.cota = cota;
    }

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public String getMotivo() {
	return motivo;
    }

    public void setMotivo(String motivo) {
	this.motivo = motivo;
    }

    public String getElemento() {
	return elemento;
    }

    public void setElemento(String elemento) {
	this.elemento = elemento;
    }

    public boolean possuiCota() {
	return cota != null && cota != 0;
    }

    public boolean possuiNome() {
	return nome != null && !nome.isEmpty();
    }

    public boolean possuiElemento() {
	return elemento != null && !elemento.isEmpty();
    }

    public boolean elementoIsTipoPontoVenda() {
	return getTipoElemento().equals("tipo_ponto_venda");
    }

    public boolean elementoIsGeradoorDeFluxo() {
	return getTipoElemento().equals("gerador_de_fluxo");
    }

    public boolean elementoIsBairro() {
	return getTipoElemento().equals("bairro");
    }

    public boolean elementoIsRegiao() {
	return getTipoElemento().equals("regiao");
    }

    public boolean elementoIsAreaDeInfluencia() {
	return getTipoElemento().equals("area_de_influencia");
    }

    public boolean elementoIsDistrito() {
	return getTipoElemento().equals("distrito");
    }

    public boolean elementoIsCotasAVista() {
	return getTipoElemento().equals("cotas_a_vista");
    }

    public boolean elementoIsCotasNovas() {
	return getTipoElemento().equals("cotas_novas");
    }

    public String getTipoElemento() {
	if (elemento.lastIndexOf("_") >= 0) {
	    return elemento.substring(0, elemento.lastIndexOf("_"));
	}
	return "";
    }

    public String getValorElemento() {
	if (elemento.lastIndexOf("_") >= 0) {
	    return elemento.substring(elemento.lastIndexOf("_") + 1);
	}
	return "";
    }

    public Long getTipoSegmentoProduto() {
        return tipoSegmentoProduto;
    }

    public void setTipoSegmentoProduto(Long tipoSegmentoProduto) {
        this.tipoSegmentoProduto = tipoSegmentoProduto;
    }

	/**
	 * @return the paginacao
	 */
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	/**
	 * @param paginacao the paginacao to set
	 */
	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
}
