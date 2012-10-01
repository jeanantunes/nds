package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.util.Util;

public class PdvRoteirizacaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String FORMATO_ENDERECO = "%s %s, %s - %s, %s - CEP: %s";
	
	private Long id;
	
	private String pdv;
	
	private String origem;
	
	private String endereco;
	
	private Integer cota;
	
	private String nome;
	
	private Integer ordem;
	
	public PdvRoteirizacaoDTO(){
	}

	public PdvRoteirizacaoDTO(Long id, String pdv, OrigemEndereco origem,
			Endereco endereco, Integer cota, String nome, Integer ordem) {
		this.id = id;
		this.pdv = pdv;
		this.origem = origem.getDescricao();
		if (endereco != null) {
            this.endereco = String.format(FORMATO_ENDERECO,
                    Util.nvl(endereco.getTipoLogradouro(), ""), Util.nvl(endereco.getLogradouro(), ""),
                    Util.nvl(endereco.getNumero(), ""), Util.nvl(endereco.getBairro(), ""),
                    Util.nvl(endereco.getCidade(), ""), Util.nvl(endereco.getCep(), ""));
		} else {
		    this.endereco = "";
		}
		this.cota = cota;
		this.nome = nome;
		this.ordem = ordem;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPdv() {
		return pdv;
	}

	public void setPdv(String pdv) {
		this.pdv = pdv;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Integer getCota() {
		return cota;
	}

	public void setCota(Integer cota) {
		this.cota = cota;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	/**
	 * Origem do endereço (Entrega do PDV/Principal da Cota) 
	 *
	 */
	public static enum OrigemEndereco {
	    COTA("Cota"), 
	    PDV("PDV");
	    
	    private String descricao;
	    
	    private OrigemEndereco(String descricao) {
	        this.descricao = descricao;
        }
	    
	    public String getDescricao() {
            return descricao;
        }
	    
        /**
         * Recupera a origem do endereço pela descrição
         * 
         * @param descricao
         *            descrição da origem para recuperação da origem
         * @return origem do endereço ou null caso não exista origem com a
         *         descrição recebida
         */
	    public static OrigemEndereco getByDescricao(String descricao) {
	        for (OrigemEndereco origem : OrigemEndereco.values()) {
	            if (origem.getDescricao().equals(descricao)) {
	                return origem;
	            }
	        }
	        return null;
	    }
	}

}
