package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.TipoCopia;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Pessoa;

public class CopiaMixFixacaoDTO implements Serializable {

	private static final long serialVersionUID = -3398645450272617733L;

	private TipoCopia tipoCopia;
	private Integer cotaNumeroOrigem;
	private String nomeCotaOrigem;
	
	private Integer cotaNumeroDestino;
	private String nomeCotaDestino;
	
	private String codigoProdutoOrigem;
	private String nomeProdutoOrigem;
	
	private String codigoProdutoDestino;
	private String nomeProdutoDestino;

    private String classificacaoProduto;
	
	public Cota getCotaOrigem(){
		Cota cota = createCota(this.cotaNumeroOrigem,nomeCotaOrigem);
		return cota;
	}
	
	public Cota getCotaDestino(){
		Cota cota = createCota(this.cotaNumeroDestino,nomeCotaDestino);
		return cota;
	}
	
	private Cota createCota(Integer cotaNumero,final String nomeCota) {
		Cota cota = new Cota();
		cota.setNumeroCota(cotaNumero);
		cota.setPessoa(new Pessoa(){

			@Override
			public String getNome() {
				
				return nomeCota;
			}

			@Override
			public String getDocumento() {
				return null;
			}
			
		});
		
		return cota;
	}
	
	public TipoCopia getTipoCopia() {
		return tipoCopia;
	}
	public void setTipoCopia(TipoCopia tipoCopia) {
		this.tipoCopia = tipoCopia;
	}

	public Integer getCotaNumeroOrigem() {
		return cotaNumeroOrigem;
	}

	public void setCotaNumeroOrigem(Integer cotaNumeroOrigem) {
		this.cotaNumeroOrigem = cotaNumeroOrigem;
	}

	public String getNomeCotaOrigem() {
		return nomeCotaOrigem;
	}

	public void setNomeCotaOrigem(String nomeCotaOrigem) {
		this.nomeCotaOrigem = nomeCotaOrigem;
	}

	public Integer getCotaNumeroDestino() {
		return cotaNumeroDestino;
	}

	public void setCotaNumeroDestino(Integer cotaNumeroDestino) {
		this.cotaNumeroDestino = cotaNumeroDestino;
	}

	public String getNomeCotaDestino() {
		return nomeCotaDestino;
	}

	public void setNomeCotaDestino(String nomeCotaDestino) {
		this.nomeCotaDestino = nomeCotaDestino;
	}

	public String getCodigoProdutoOrigem() {
		return codigoProdutoOrigem;
	}

	public void setCodigoProdutoOrigem(String codigoProdutoOrigem) {
		this.codigoProdutoOrigem = codigoProdutoOrigem;
	}

	public String getNomeProdutoOrigem() {
		return nomeProdutoOrigem;
	}

	public void setNomeProdutoOrigem(String nomeProdutoOrigem) {
		this.nomeProdutoOrigem = nomeProdutoOrigem;
	}

	public String getCodigoProdutoDestino() {
		return codigoProdutoDestino;
	}

	public void setCodigoProdutoDestino(String codigoProdutoDestino) {
		this.codigoProdutoDestino = codigoProdutoDestino;
	}

	public String getNomeProdutoDestino() {
		return nomeProdutoDestino;
	}

	public void setNomeProdutoDestino(String nomeProdutoDestino) {
		this.nomeProdutoDestino = nomeProdutoDestino;
	}


    public String getClassificacaoProduto() {
        return classificacaoProduto;
    }

    public void setClassificacaoProduto(String classificacaoProduto) {
        this.classificacaoProduto = classificacaoProduto;
    }
}
