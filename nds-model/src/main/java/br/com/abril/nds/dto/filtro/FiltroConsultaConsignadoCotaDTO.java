package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO.ColunaOrdenacaoRomaneio;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroConsultaConsignadoCotaDTO implements Serializable {

	private static final long serialVersionUID = -642561468775306010L;
	
	private Long idCota;
	
	private Long idFornecedor;
	
	@Export(label="Cota")
	private String nomeCota;
	
	@Export(label="Fornecedor")
	private String nomeFornecedor;
	
	private PaginacaoVO paginacao;
	
	private Boolean addCotaVista;
	
	private Boolean addOutrasCotas;
	
	private Date dataInicio;
	
	private Date dataFim;
	
	private Date dataOperacao;
	
	private TipoOperacao tipoOperacao;
    	
	private ColunaOrdenacaoConsultaConsignadoCota ordenacaoColuna;
	
	private boolean isCotaAvista;
	
	private boolean isCotaDevolveEncalhe;
	
	public FiltroConsultaConsignadoCotaDTO() {
		
	}
	
	public FiltroConsultaConsignadoCotaDTO(Boolean addCotaVista, Boolean addOutrasCotas, Date dataInicio, Date dataFim, TipoOperacao tipoOperacao) {
	    this.addCotaVista = addCotaVista;
	    this.addOutrasCotas = addOutrasCotas;
	    this.dataInicio = dataInicio;
	    this.dataFim = dataFim;
	    this.tipoOperacao = tipoOperacao;
    }
	
	public FiltroConsultaConsignadoCotaDTO(Long idCota) {
		
		this.idCota = idCota;
	}
	
	public enum ColunaOrdenacaoConsultaConsignadoCota {

		COTA("cota");
		
		private String nomeColuna;
		
		private ColunaOrdenacaoConsultaConsignadoCota(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacaoRomaneio getPorDescricao(String descricao) {
			for(ColunaOrdenacaoRomaneio coluna: ColunaOrdenacaoRomaneio.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}

	
    public TipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }
    
    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }
	
	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public ColunaOrdenacaoConsultaConsignadoCota getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(
			ColunaOrdenacaoConsultaConsignadoCota ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	
    public Boolean getAddCotaVista() {
        return addCotaVista;
    }

    
    public void setAddCotaVista(Boolean addCotaVista) {
        this.addCotaVista = addCotaVista;
    }

    
    public Boolean getAddOutrasCotas() {
        return addOutrasCotas;
    }

    
    public void setAddOutrasCotas(Boolean addOutrasCotas) {
        this.addOutrasCotas = addOutrasCotas;
    }

    
    public Date getDataInicio() {
        return dataInicio;
    }

    
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    
    public Date getDataFim() {
        return dataFim;
    }

    
    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCota == null) ? 0 : idCota.hashCode());
		result = prime * result
				+ ((idFornecedor == null) ? 0 : idFornecedor.hashCode());
		result = prime * result
				+ ((nomeCota == null) ? 0 : nomeCota.hashCode());
		result = prime * result
				+ ((nomeFornecedor == null) ? 0 : nomeFornecedor.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroConsultaConsignadoCotaDTO other = (FiltroConsultaConsignadoCotaDTO) obj;
		if (idCota == null) {
			if (other.idCota != null)
				return false;
		} else if (!idCota.equals(other.idCota))
			return false;
		if (idFornecedor == null) {
			if (other.idFornecedor != null)
				return false;
		} else if (!idFornecedor.equals(other.idFornecedor))
			return false;
		if (nomeCota == null) {
			if (other.nomeCota != null)
				return false;
		} else if (!nomeCota.equals(other.nomeCota))
			return false;
		if (nomeFornecedor == null) {
			if (other.nomeFornecedor != null)
				return false;
		} else if (!nomeFornecedor.equals(other.nomeFornecedor))
			return false;
		return true;
	}

	public Date getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	public boolean isCotaAvista() {
		return isCotaAvista;
	}

	public void setCotaAvista(boolean isCotaAvista) {
		this.isCotaAvista = isCotaAvista;
	}

	public boolean isCotaDevolveEncalhe() {
		return isCotaDevolveEncalhe;
	}

	public void setCotaDevolveEncalhe(boolean isCotaDevolveEncalhe) {
		this.isCotaDevolveEncalhe = isCotaDevolveEncalhe;
	}
	
}
