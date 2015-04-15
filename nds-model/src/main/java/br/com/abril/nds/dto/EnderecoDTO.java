package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Endereco;

/**
 * DTO com as informações de endereço
 * 
 * @author francisco.garcia
 *
 */
public class EnderecoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String bairro;

    private String cep;

    private Integer codigoCidadeIBGE;

    private String cidade;

    private String complemento;

    private String tipoLogradouro;

    private String logradouro;

    private String numero;

    private String uf;

    private Integer codigoUf;
    
    public EnderecoDTO() {
    }    
    
    public EnderecoDTO(String bairro, String cep,
            Integer codigoCidadeIBGE, String cidade, String complemento,
            String tipoLogradouro, String logradouro, String numero, String uf,
            Integer codigoUf) {
        this.bairro = bairro;
        this.cep = cep;
        this.codigoCidadeIBGE = codigoCidadeIBGE;
        this.cidade = cidade;
        this.complemento = complemento;
        this.tipoLogradouro = tipoLogradouro;
        this.logradouro = logradouro;
        this.numero = numero;
        this.uf = uf;
        this.codigoUf = codigoUf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Integer getCodigoCidadeIBGE() {
        return codigoCidadeIBGE;
    }

    public void setCodigoCidadeIBGE(Integer codigoCidadeIBGE) {
        this.codigoCidadeIBGE = codigoCidadeIBGE;
    }

    public Integer getCodigoUf() {
        return codigoUf;
    }

    public void setCodigoUf(Integer codigoUf) {
        this.codigoUf = codigoUf;
    }
    
    
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bairro == null) ? 0 : bairro.hashCode());
        result = prime * result + ((cep == null) ? 0 : cep.hashCode());
        result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
        result = prime
                * result
                + ((codigoCidadeIBGE == null) ? 0 : codigoCidadeIBGE.hashCode());
        result = prime * result
                + ((codigoUf == null) ? 0 : codigoUf.hashCode());
        result = prime * result
                + ((complemento == null) ? 0 : complemento.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((logradouro == null) ? 0 : logradouro.hashCode());
        result = prime * result + ((numero == null) ? 0 : numero.hashCode());
        result = prime * result
                + ((tipoLogradouro == null) ? 0 : tipoLogradouro.hashCode());
        result = prime * result + ((uf == null) ? 0 : uf.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EnderecoDTO other = (EnderecoDTO) obj;
        if (bairro == null) {
            if (other.bairro != null) {
                return false;
            }
        } else if (!bairro.equals(other.bairro)) {
            return false;
        }
        if (cep == null) {
            if (other.cep != null) {
                return false;
            }
        } else if (!cep.equals(other.cep)) {
            return false;
        }
        if (cidade == null) {
            if (other.cidade != null) {
                return false;
            }
        } else if (!cidade.equals(other.cidade)) {
            return false;
        }
        if (codigoCidadeIBGE == null) {
            if (other.codigoCidadeIBGE != null) {
                return false;
            }
        } else if (!codigoCidadeIBGE.equals(other.codigoCidadeIBGE)) {
            return false;
        }
        if (codigoUf == null) {
            if (other.codigoUf != null) {
                return false;
            }
        } else if (!codigoUf.equals(other.codigoUf)) {
            return false;
        }
        if (complemento == null) {
            if (other.complemento != null) {
                return false;
            }
        } else if (!complemento.equals(other.complemento)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (logradouro == null) {
            if (other.logradouro != null) {
                return false;
            }
        } else if (!logradouro.equals(other.logradouro)) {
            return false;
        }
        if (numero == null) {
            if (other.numero != null) {
                return false;
            }
        } else if (!numero.equals(other.numero)) {
            return false;
        }
        if (tipoLogradouro == null) {
            if (other.tipoLogradouro != null) {
                return false;
            }
        } else if (!tipoLogradouro.equals(other.tipoLogradouro)) {
            return false;
        }
        if (uf == null) {
            if (other.uf != null) {
                return false;
            }
        } else if (!uf.equals(other.uf)) {
            return false;
        }
        return true;
    }

    /**
     * Method Factory para criação do {@link EnderecoDTO} à
     * partir da entidade Endereço
     * @param endereco entidade endereço para criação do DTO
     * @return {@link EnderecoDTO} criado à partir da entidade endereço
     */
    public static EnderecoDTO fromEndereco(Endereco endereco) {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(endereco.getId());
        dto.setTipoLogradouro(endereco.getTipoLogradouro());
        dto.setLogradouro(endereco.getLogradouro());
        dto.setNumero(endereco.getNumero());
        dto.setComplemento(endereco.getComplemento());
        dto.setBairro(endereco.getBairro());
        dto.setCep(endereco.getCep());
        dto.setCidade(endereco.getCidade());
        dto.setCodigoCidadeIBGE(endereco.getCodigoCidadeIBGE());
        dto.setUf(endereco.getUf());
        dto.setCodigoUf(endereco.getCodigoUf());
        return dto;
    }

}