package br.com.abril.nds.integracao.ems0117.inbound;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

/**
 * Classe de entrada do arquivo COTA.NEW
 * @author erick.dzadotz
 * @version 1.0
 */
@Record
public class EMS0117Input implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codCota;
	private String nomeJornaleiro;
	private Integer qtdeCotas;
	private String endereco;
	private Integer codBairro;
	private String municipio;
	private String siglaUF;
	private String cep;
	private String ddd;
	private String telefone;
	private String situacaoCota;
	private String condPrazoPagamento;
	private Long codBox;
	private String codTipoBox;
	private String codCapataz;
	private String repartePDV;
	private String cpfCNPJ;
	private String tipoPessoa;
	private Integer numLogradouro;
	private Integer codCidadeIbge;
	private String inscrEstadual;
	private String inscrMunicipal;
	
	@Field(offset = 1, length = 4)
	public Integer getCodCota() {
		return codCota;
	}
	
	public void setCodCota(Integer codCota) {
		this.codCota = codCota;
	}
	
	@Field(offset = 5, length = 30)
	public String getNomeJornaleiro() {
		return nomeJornaleiro;
	}
	
	public void setNomeJornaleiro(String nomeJornaleiro) {
		this.nomeJornaleiro = nomeJornaleiro;
	}
	
	@Field(offset = 35, length = 4)
	public Integer getQtdeCotas() {
		return qtdeCotas;
	}
	
	public void setQtdeCotas(Integer qtdeCotas) {
		this.qtdeCotas = qtdeCotas;
	}
	
	@Field(offset = 39, length = 40)
	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	@Field(offset = 79, length = 5)
	public Integer getCodBairro() {
		return codBairro;
	}
	
	public void setCodBairro(Integer codBairro) {
		this.codBairro = codBairro;
	}
	
	@Field(offset = 84, length = 20)
	public String getMunicipio() {
		return municipio;
	}
	
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	
	@Field(offset = 104, length = 2)
	public String getSiglaUF() {
		return siglaUF;
	}
	
	public void setSiglaUF(String siglaUF) {
		this.siglaUF = siglaUF;
	}
	
	@Field(offset = 106, length = 8)
	public String getCep() {
		return cep;
	}
	
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	@Field(offset = 114, length = 4)
	public String getDdd() {
		return ddd;
	}
	
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	
	@Field(offset = 118, length = 7)
	public String getTelefone() {
		return telefone;
	}
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	@Field(offset = 125, length = 1)
	public String getSituacaoCota() {
		return situacaoCota;
	}
	
	public void setSituacaoCota(String situacaoCota) {
		this.situacaoCota = situacaoCota;
	}
	
	@Field(offset = 126, length = 1)
	public String getCondPrazoPagamento() {
		return condPrazoPagamento;
	}
	
	public void setCondPrazoPagamento(String condPrazoPagamento) {
		this.condPrazoPagamento = condPrazoPagamento;
	}
	
	@Field(offset = 127, length = 3)
	public Long getCodBox() {
		return codBox;
	}
	
	public void setCodBox(Long codBox) {
		this.codBox = codBox;
	}
	
	@Field(offset = 130, length = 1)
	public String getCodTipoBox() {
		return codTipoBox;
	}
	
	public void setCodTipoBox(String codTipoBox) {
		this.codTipoBox = codTipoBox;
	}
	
	@Field(offset = 131, length = 1)
	public String getRepartePDV() {
		return repartePDV;
	}
	
	public void setRepartePDV(String repartePDV) {
		this.repartePDV = repartePDV;
	}
	
	@Field(offset = 132, length = 5)
	public String getCodCapataz() {
		return codCapataz;
	}

	public void setCodCapataz(String codCapataz) {
		this.codCapataz = codCapataz;
	}

	@Field(offset = 137, length = 14)
	public String getCpfCNPJ() {
		return cpfCNPJ;
	}
	
	public void setCpfCNPJ(String cpfCNPJ) {
		this.cpfCNPJ = cpfCNPJ;
	}
	
	@Field(offset = 151, length = 1)
	public String getTipoPessoa() {
		return tipoPessoa;
	}
	
	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
	
	@Field(offset = 152, length = 6)
	public Integer getNumLogradouro() {
		return numLogradouro;
	}
	
	public void setNumLogradouro(Integer numLogradouro) {
		this.numLogradouro = numLogradouro;
	}
	
	@Field(offset = 158, length = 7)
	public Integer getCodCidadeIbge() {
		return codCidadeIbge;
	}
	
	public void setCodCidadeIbge(Integer codCidadeIbge) {
		this.codCidadeIbge = codCidadeIbge;
	}
	
	@Field(offset = 165, length = 20)
	public String getInscrEstadual() {
		return inscrEstadual;
	}
	
	public void setInscrEstadual(String inscrEstadual) {
		this.inscrEstadual = inscrEstadual;
	}
	
	@Field(offset = 185, length = 15)
	public String getInscrMunicipal() {
		return inscrMunicipal;
	}
	
	public void setInscrMunicipal(String inscrMunicipal) {
		this.inscrMunicipal = inscrMunicipal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
		result = prime * result
				+ ((codBairro == null) ? 0 : codBairro.hashCode());
		result = prime * result + ((codBox == null) ? 0 : codBox.hashCode());
		result = prime * result
				+ ((codCapataz == null) ? 0 : codCapataz.hashCode());
		result = prime * result
				+ ((codCidadeIbge == null) ? 0 : codCidadeIbge.hashCode());
		result = prime * result + ((codCota == null) ? 0 : codCota.hashCode());
		result = prime * result
				+ ((codTipoBox == null) ? 0 : codTipoBox.hashCode());
		result = prime
				* result
				+ ((condPrazoPagamento == null) ? 0 : condPrazoPagamento
						.hashCode());
		result = prime * result + ((cpfCNPJ == null) ? 0 : cpfCNPJ.hashCode());
		result = prime * result + ((ddd == null) ? 0 : ddd.hashCode());
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result
				+ ((inscrEstadual == null) ? 0 : inscrEstadual.hashCode());
		result = prime * result
				+ ((inscrMunicipal == null) ? 0 : inscrMunicipal.hashCode());
		result = prime * result
				+ ((municipio == null) ? 0 : municipio.hashCode());
		result = prime * result
				+ ((nomeJornaleiro == null) ? 0 : nomeJornaleiro.hashCode());
		result = prime * result
				+ ((numLogradouro == null) ? 0 : numLogradouro.hashCode());
		result = prime * result
				+ ((qtdeCotas == null) ? 0 : qtdeCotas.hashCode());
		result = prime * result
				+ ((repartePDV == null) ? 0 : repartePDV.hashCode());
		result = prime * result + ((siglaUF == null) ? 0 : siglaUF.hashCode());
		result = prime * result
				+ ((situacaoCota == null) ? 0 : situacaoCota.hashCode());
		result = prime * result
				+ ((telefone == null) ? 0 : telefone.hashCode());
		result = prime * result
				+ ((tipoPessoa == null) ? 0 : tipoPessoa.hashCode());
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
		EMS0117Input other = (EMS0117Input) obj;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (codBairro == null) {
			if (other.codBairro != null)
				return false;
		} else if (!codBairro.equals(other.codBairro))
			return false;
		if (codBox == null) {
			if (other.codBox != null)
				return false;
		} else if (!codBox.equals(other.codBox))
			return false;
		if (codCapataz == null) {
			if (other.codCapataz != null)
				return false;
		} else if (!codCapataz.equals(other.codCapataz))
			return false;
		if (codCidadeIbge == null) {
			if (other.codCidadeIbge != null)
				return false;
		} else if (!codCidadeIbge.equals(other.codCidadeIbge))
			return false;
		if (codCota == null) {
			if (other.codCota != null)
				return false;
		} else if (!codCota.equals(other.codCota))
			return false;
		if (codTipoBox == null) {
			if (other.codTipoBox != null)
				return false;
		} else if (!codTipoBox.equals(other.codTipoBox))
			return false;
		if (condPrazoPagamento == null) {
			if (other.condPrazoPagamento != null)
				return false;
		} else if (!condPrazoPagamento.equals(other.condPrazoPagamento))
			return false;
		if (cpfCNPJ == null) {
			if (other.cpfCNPJ != null)
				return false;
		} else if (!cpfCNPJ.equals(other.cpfCNPJ))
			return false;
		if (ddd == null) {
			if (other.ddd != null)
				return false;
		} else if (!ddd.equals(other.ddd))
			return false;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (inscrEstadual == null) {
			if (other.inscrEstadual != null)
				return false;
		} else if (!inscrEstadual.equals(other.inscrEstadual))
			return false;
		if (inscrMunicipal == null) {
			if (other.inscrMunicipal != null)
				return false;
		} else if (!inscrMunicipal.equals(other.inscrMunicipal))
			return false;
		if (municipio == null) {
			if (other.municipio != null)
				return false;
		} else if (!municipio.equals(other.municipio))
			return false;
		if (nomeJornaleiro == null) {
			if (other.nomeJornaleiro != null)
				return false;
		} else if (!nomeJornaleiro.equals(other.nomeJornaleiro))
			return false;
		if (numLogradouro == null) {
			if (other.numLogradouro != null)
				return false;
		} else if (!numLogradouro.equals(other.numLogradouro))
			return false;
		if (qtdeCotas == null) {
			if (other.qtdeCotas != null)
				return false;
		} else if (!qtdeCotas.equals(other.qtdeCotas))
			return false;
		if (repartePDV == null) {
			if (other.repartePDV != null)
				return false;
		} else if (!repartePDV.equals(other.repartePDV))
			return false;
		if (siglaUF == null) {
			if (other.siglaUF != null)
				return false;
		} else if (!siglaUF.equals(other.siglaUF))
			return false;
		if (situacaoCota == null) {
			if (other.situacaoCota != null)
				return false;
		} else if (!situacaoCota.equals(other.situacaoCota))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		if (tipoPessoa == null) {
			if (other.tipoPessoa != null)
				return false;
		} else if (!tipoPessoa.equals(other.tipoPessoa))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EMS0117Input [codCota=" + codCota + ", nomeJornaleiro="
				+ nomeJornaleiro + ", qtdeCotas=" + qtdeCotas + ", endereco="
				+ endereco + ", codBairro=" + codBairro + ", municipio="
				+ municipio + ", siglaUF=" + siglaUF + ", cep=" + cep
				+ ", ddd=" + ddd + ", telefone=" + telefone + ", situacaoCota="
				+ situacaoCota + ", condPrazoPagamento=" + condPrazoPagamento
				+ ", codBox=" + codBox + ", codTipoBox=" + codTipoBox
				+ ", codCapataz=" + codCapataz + ", repartePDV=" + repartePDV
				+ ", cpfCNPJ=" + cpfCNPJ + ", tipoPessoa=" + tipoPessoa
				+ ", numLogradouro=" + numLogradouro + ", codCidade="
				+ codCidadeIbge + ", inscrEstadual=" + inscrEstadual
				+ ", inscrMunicipal=" + inscrMunicipal + "]";
	}
}
