package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;
import br.com.abril.nds.util.Ordenavel;
import br.com.abril.nds.util.Util;

public class PdvRoteirizacaoDTO implements Serializable, Ordenavel, Comparable<PdvRoteirizacaoDTO> {

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
		
		if(origem==null) {
			this.origem = "";
		} else {
			this.origem = origem.getDescricao();
		}
		
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

	public PdvRoteirizacaoDTO(Long id, String nomePDV, Integer numeroCota, Integer ordem) {
		this.id = id;
		this.cota = numeroCota;
		this.nome = nomePDV;
		this.ordem = ordem;
	}
	
	public static List<PdvRoteirizacaoDTO> getDTOFrom(List<RotaPDV> rotasPDVs) {
	
		List<PdvRoteirizacaoDTO> pdvs = new ArrayList<PdvRoteirizacaoDTO>();
		
		for(RotaPDV rotaPDV : rotasPDVs) {
			
			PdvRoteirizacaoDTO pdvDTO = new PdvRoteirizacaoDTO(rotaPDV.getId(), 
															   rotaPDV.getPdv().getNome(), 
															   rotaPDV.getPdv().getCota().getNumeroCota(), 
															   rotaPDV.getOrdem());
			
			pdvs.add(pdvDTO);
		}
		
		return pdvs;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PdvRoteirizacaoDTO other = (PdvRoteirizacaoDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(PdvRoteirizacaoDTO other) {
		
		return this.ordem.compareTo(other.getOrdem());
	}

}