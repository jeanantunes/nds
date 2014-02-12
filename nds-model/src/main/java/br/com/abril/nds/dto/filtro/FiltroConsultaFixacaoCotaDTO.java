package br.com.abril.nds.dto.filtro;

@SuppressWarnings("serial")
public class FiltroConsultaFixacaoCotaDTO extends FiltroDTO {
 
	private String cota;
	private String nomeCota;
	private String codigoProduto;
	private String classificacaoProduto;

	public String getCota() {
		return cota;
	}
	public void setCota(String cota) {
		this.cota = cota;
	}
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

    public String getClassificacaoProduto() {
        return classificacaoProduto;
    }

    public void setClassificacaoProduto(String classificacaoProduto) {
        this.classificacaoProduto = classificacaoProduto;
    }
}
