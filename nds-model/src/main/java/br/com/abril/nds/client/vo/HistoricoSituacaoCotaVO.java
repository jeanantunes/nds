package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * Value Object de histórico de situação da cota.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class HistoricoSituacaoCotaVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4573189935810707316L;
	
	@Export(label="Data", exhibitionOrder = 0)
	private Date data;

	@Export(label="Status Anterior", exhibitionOrder = 1)
	private String statusAnterior;
	
	@Export(label="Status Atualizado", exhibitionOrder = 2)
	private String statusAtualizado;
	
	@Export(label="Usuário", exhibitionOrder = 3)
	private String usuario;
	
	@Export(label="Motivo", exhibitionOrder = 4)
	private String motivo;
	
	@Export(label="Descrição", exhibitionOrder = 5)
	private String descricao;
	
	@Export(label="Cota", exhibitionOrder = 6)
	private String nomeCota;
	
	@Export(label="Numero da Cota", exhibitionOrder = 7)
	private Integer numeroCota;
	
	private boolean processado;
	
	/**
	 * Construtor padrão.
	 */
	public HistoricoSituacaoCotaVO() {
		
	}
	
	

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}



	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
    

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}



	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @return the statusAnterior
	 */
	public String getStatusAnterior() {
		return statusAnterior;
	}

	/**
	 * @param statusAnterior the statusAnterior to set
	 */
	public void setStatusAnterior(SituacaoCadastro statusAnterior) {
		this.statusAnterior = statusAnterior == null ? "" : statusAnterior.toString();
	}

	/**
	 * @return the statusAtualizado
	 */
	public String getStatusAtualizado() {
		return statusAtualizado;
	}

	/**
	 * @param statusAtualizado the statusAtualizado to set
	 */
	public void setStatusAtualizado(SituacaoCadastro statusAtualizado) {
		this.statusAtualizado = statusAtualizado == null ? "" : statusAtualizado.toString();
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the motivo
	 */
	public String getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(MotivoAlteracaoSituacao motivo) {
		this.motivo = motivo == null ? "" : motivo.toString();
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao == null ? "" : descricao;
	}
	
	public boolean isProcessado() {
		return processado;
	}

	public void setProcessado(boolean processado) {
		this.processado = processado;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((motivo == null) ? 0 : motivo.hashCode());
		result = prime * result
				+ ((statusAnterior == null) ? 0 : statusAnterior.hashCode());
		result = prime
				* result
				+ ((statusAtualizado == null) ? 0 : statusAtualizado.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		HistoricoSituacaoCotaVO other = (HistoricoSituacaoCotaVO) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (motivo == null) {
			if (other.motivo != null)
				return false;
		} else if (!motivo.equals(other.motivo))
			return false;
		if (statusAnterior == null) {
			if (other.statusAnterior != null)
				return false;
		} else if (!statusAnterior.equals(other.statusAnterior))
			return false;
		if (statusAtualizado == null) {
			if (other.statusAtualizado != null)
				return false;
		} else if (!statusAtualizado.equals(other.statusAtualizado))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
}
