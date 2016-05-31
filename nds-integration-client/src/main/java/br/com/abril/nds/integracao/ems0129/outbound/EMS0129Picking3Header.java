package br.com.abril.nds.integracao.ems0129.outbound;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0129Picking3Header implements Serializable {

	private static final long serialVersionUID = 3412500510620035057L;
	
	private String identificadorLinha;
	
	private String codigoCota;
	
	private String nomeDistribuidor;
	
	private String nomeCota;
	
	private String codigoBox;
	
	private String consignado;
	
	private String cpf;
	
	private String cnpj;
	
	private String inscricaoMunicipal;
	
	private String dataLancamento;
	
	@Field(offset = 1, length = 2)
	public String getIdentificadorLinha() {
		return identificadorLinha;
	}

	@Field(offset = 3, length = 5)
	public String getCodigoCota() {
		return codigoCota;
	}
	
	@Field(offset = 8, length = 41)
	public String getNomeDistribuidor() {
		return nomeDistribuidor;
	}
	
	@Field(offset = 49, length = 9)
	public String getDataLancamento() {
		return dataLancamento;
	}

	@Field(offset = 58, length = 31)
	public String getNomeCota() {
		return nomeCota;
	}

	@Field(offset = 89, length = 4)
	public String getCodigoBox() {
		return codigoBox;
	}
	
	@Field(offset = 93, length = 11)
	public String getConsignado() {
		return consignado;
	}

	@Field(offset = 104, length = 12)
	public String getCpf() {
		return cpf;
	}

	@Field(offset = 116, length = 15)
	public String getCnpj() {
		return cnpj;
	}

	@Field(offset = 131, length = 21)
	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}

	public void setNomeDistribuidor(String nomeDistribuidor) {
		this.nomeDistribuidor = nomeDistribuidor;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public void setCodigoBox(String codigoBox) {
		this.codigoBox = codigoBox;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public void setIdentificadorLinha(String identificadorLinha) {
		this.identificadorLinha = identificadorLinha;
	}

	public void setConsignado(String consignado) {
		this.consignado = consignado;
	}
	
}
