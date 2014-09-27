package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

public class ChamadaAntecipadaEncalheDTO implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	private Integer codBox;
	
	private String nomeBox;
	
	private Integer numeroCota;
	
	private String nomeCota;
	
	private BigInteger qntExemplares;
	
	private Long codigoChamadaEncalhe;
	
	private Long idLancamento;

	public ChamadaAntecipadaEncalheDTO() {}

	public ChamadaAntecipadaEncalheDTO(Integer codBox,String nomeBox ,Integer numeroCota,BigInteger qntExemplares,Long idLancamento,String nomeCota) {
		
		this.codBox = codBox;
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.qntExemplares = qntExemplares;
		this.nomeBox = nomeBox;
		this.idLancamento = idLancamento;
	}
	
	public ChamadaAntecipadaEncalheDTO(Integer codBox,String nomeBox, Integer numeroCota,BigInteger qntExemplares,String nomeCota, Long codigoChamadaEncalhe) {
		
		this.codBox = codBox;
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.qntExemplares = qntExemplares;
		this.codigoChamadaEncalhe = codigoChamadaEncalhe;
		this.nomeBox = nomeBox;
	}
	
	/**
	 * Construtor utilizado em ChamadaEncalheCotaRepositoryImpl nos métodos:
	 * 
	 * 		List<ChamadaAntecipadaEncalheDTO> obterCotasProgramadaParaAntecipacoEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro);
	 * 
	 * 
	 * @param codBox
	 * @param nomeBox
	 * @param numeroCota
	 * @param qntExemplares
	 * @param idLancamento
	 * @param nomeCota
	 * @param codigoChamadaEncalhe
	 */
	public ChamadaAntecipadaEncalheDTO(Integer codBox,String nomeBox, Integer numeroCota,BigInteger qntExemplares, Long idLancamento, String nomeCota, Long codigoChamadaEncalhe) {
		
		this.codBox = codBox;
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.qntExemplares = qntExemplares;
		this.idLancamento = idLancamento;
		this.codigoChamadaEncalhe = codigoChamadaEncalhe;
		this.nomeBox = nomeBox;
	}
	
	public ChamadaAntecipadaEncalheDTO(Integer numeroCota,BigInteger qntExemplares,Long codigoChamadaEncalhe, Long idLancamento) {
		
		this.numeroCota = numeroCota;
		this.qntExemplares = qntExemplares;
		this.codigoChamadaEncalhe = codigoChamadaEncalhe;
		this.idLancamento = idLancamento;
	}
	
	
	
	/**
	 * @return the idLancamento
	 */
	public Long getIdLancamento() {
		return idLancamento;
	}

	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}

	/**
	 * @return the nomeBox
	 */
	public String getNomeBox() {
		return nomeBox;
	}

	/**
	 * @param nomeBox the nomeBox to set
	 */
	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

	/**
	 * @return the codigoChamadaEncalhe
	 */
	public Long getCodigoChamadaEncalhe() {
		return codigoChamadaEncalhe;
	}

	/**
	 * @param codigoChamadaEncalhe the codigoChamadaEncalhe to set
	 */
	public void setCodigoChamadaEncalhe(Long codigoChamadaEncalhe) {
		this.codigoChamadaEncalhe = codigoChamadaEncalhe;
	}

	/**
	 * @return the codBox
	 */
	public Integer getCodBox() {
		return codBox;
	}

	/**
	 * @param codBox the codBox to set
	 */
	public void setCodBox(Integer codBox) {
		this.codBox = codBox;
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
	 * @return the qntExemplares
	 */
	public BigInteger getQntExemplares() {
		return qntExemplares;
	}

	/**
	 * @param qntExemplares the qntExemplares to set
	 */
	public void setQntExemplares(BigInteger qntExemplares) {
		this.qntExemplares = qntExemplares;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codBox == null) ? 0 : codBox.hashCode());
		result = prime * result
				+ ((idLancamento == null) ? 0 : idLancamento.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
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
		ChamadaAntecipadaEncalheDTO other = (ChamadaAntecipadaEncalheDTO) obj;
		if (codBox == null) {
			if (other.codBox != null)
				return false;
		} else if (!codBox.equals(other.codBox))
			return false;
		if (idLancamento == null) {
			if (other.idLancamento != null)
				return false;
		} else if (!idLancamento.equals(other.idLancamento))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		return true;
	}

}
