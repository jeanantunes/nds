package br.com.abril.nds.dto;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CertificadoNFEDTO implements Serializable {

	private static final long serialVersionUID = -6593247184450400133L;

	private Long id;
	
	private Long idDistribuidor;	
	
	private String nomeArquivo;
	
	private String extensao;
	
	@Export(label="alias")
	private String alias;
	
	private String senha;

	private Date dataInicio;
	
	private Date dataFim;
	
	
	private File tempFile;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getIdDistribuidor() {
		return idDistribuidor;
	}

	public void setIdDistribuidor(Long idDistribuidor) {
		this.idDistribuidor = idDistribuidor;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
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
	

	public File getTempFile() {
		return tempFile;
	}

	public void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}

}