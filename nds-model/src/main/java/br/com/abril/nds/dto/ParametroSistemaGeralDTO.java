package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;

/**
 * DTO que agrupa os parâmetros do sistema a serem exibidos/alterados na tela
 * "Administração > Parâmetros do Sistema".
 * 
 * @author Discover Technology
 */
public class ParametroSistemaGeralDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -7477289076396360578L;
	
	
	private Map<TipoParametroSistema, String> params = 
			new HashMap<TipoParametroSistema, String>();
	
	/** Data da Operação Corrente (usado apenas para exibição). */
	private String dtOperacaoCorrente;
	
	/** Método Construtor padrão. */
	public ParametroSistemaGeralDTO() {
	}
	
	
	/**
	 * Trata o valor do parâmetro para que não retorne 'null':
	 * @param tps
	 * @return
	 */
	private String getParametroSistemaString(TipoParametroSistema tps) {
		
		String str = this.params.get(tps);
		return str == null ? "" : str;
	}
	
	/**
	 * Popula o DTO com os parametros do sistema.
	 * 
	 * @param parametrosSistema
	 */
	public void setParametrosSistema(Collection<ParametroSistema> parametrosSistema) {
		for (ParametroSistema ps : parametrosSistema) {
			
			// Data Operação não é editável, por isso não entra no 'params':
			if (TipoParametroSistema.DATA_OPERACAO_CORRENTE.equals(ps.getTipoParametroSistema())) {
				this.dtOperacaoCorrente = ps.getValor();
			} else {
				this.params.put(ps.getTipoParametroSistema(), ps.getValor());
			}
		}
	}
	
	/**
	 * Retorna todos os parâmetros do sistema com os dados alterados pelo usuário.
	 * 
	 * @return
	 */
	public List<ParametroSistema> getParametrosSistema() {
		
		List<ParametroSistema> lst = new ArrayList<ParametroSistema>();
		for (TipoParametroSistema tps : params.keySet()) {
			ParametroSistema ps = new ParametroSistema();
			ps.setTipoParametroSistema(tps);
			ps.setValor(params.get(tps));
			lst.add(ps);
		}
		
		return lst;
	}
	
	public String getFrequenciaExpurgo(){
		return getParametroSistemaString(TipoParametroSistema.FREQUENCIA_EXPURGO);
	}
	
	public void setFrequenciaExpurgo(String qntMeses){
		this.params.put(TipoParametroSistema.FREQUENCIA_EXPURGO,qntMeses);
	}
	

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.getParametroSistemaString(TipoParametroSistema.EMAIL_USUARIO);
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.params.put(TipoParametroSistema.EMAIL_USUARIO, email);
	}

	/**
	 * @return the versaoSistema
	 */
	public String getVersaoSistema() {
		return this.getParametroSistemaString(TipoParametroSistema.VERSAO_SISTEMA);
	}

	/**
	 * @param versaoSistema
	 *            the versaoSistema to set
	 */
	public void setVersaoSistema(String versaoSistema) {
		this.params.put(TipoParametroSistema.VERSAO_SISTEMA, versaoSistema);
	}

	/**
	 * @return the pathCeExportacao
	 */
	public String getPathCeExportacao() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_INTERFACE_CE_EXPORTACAO);
	}

	/**
	 * @param pathCeExportacao
	 *            the pathCeExportacao to set
	 */
	public void setPathCeExportacao(String pathCeExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_CE_EXPORTACAO, pathCeExportacao);
	}

	/**
	 * @return the pathProdinImportacao
	 */
	public String getPathProdinImportacao() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_INTERFACE_PRODIN_IMPORTACAO);
	}

	/**
	 * @param pathProdinImportacao
	 *            the pathProdinImportacao to set
	 */
	public void setPathProdinImportacao(String pathProdinImportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_PRODIN_IMPORTACAO, pathProdinImportacao);
	}

	/**
	 * @return the pathProdinExportacao
	 */
	public String getPathProdinExportacao() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_INTERFACE_PRODIN_EXPORTACAO);
	}

	/**
	 * @param pathProdinExportacao
	 *            the pathProdinExportacao to set
	 */
	public void setPathProdinExportacao(String pathProdinExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_PRODIN_EXPORTACAO, pathProdinExportacao);
	}

	/**
	 * @return the pathMdcImportacao
	 */
	public String getPathMdcImportacao() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_INTERFACE_MDC_IMPORTACAO);
	}

	/**
	 * @param pathMdcImportacao
	 *            the pathMdcImportacao to set
	 */
	public void setPathMdcImportacao(String pathMdcImportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_MDC_IMPORTACAO, pathMdcImportacao);
	}

	/**
	 * @return the pathMdcExportacao
	 */
	public String getPathMdcExportacao() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_INTERFACE_MDC_EXPORTACAO);
	}

	/**
	 * @param pathMdcExportacao
	 *            the pathMdcExportacao to set
	 */
	public void setPathMdcExportacao(String pathMdcExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_MDC_EXPORTACAO, pathMdcExportacao);
	}
	
	/**
	 * @return the pathMdcBackup
	 */
	public String getPathMdcBackup() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_INTERFACE_MDC_BACKUP);
	}

	/**
	 * @param pathMdcBackup
	 *            the pathMdcBackup to set
	 */
	public void setPathMdcBackup(String pathMdcBackup) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_MDC_BACKUP, pathMdcBackup);
	}
	
	/**
	 * @return the pathBancasExportacao
	 */
	public String getPathBancasExportacao() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO);
	}

	/**
	 * @param pathBancasExportacao
	 *            the pathBancasExportacao to set
	 */
	public void setPathBancasExportacao(String pathBancasExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO, pathBancasExportacao);
	}

	/**
	 * @return the pathGfsImportacao
	 */
	public String getPathGfsImportacao() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_INTERFACE_GFS_IMPORTACAO);
	}

	/**
	 * @param pathGfsImportacao
	 *            the pathGfsImportacao to set
	 */
	public void setPathGfsImportacao(String pathGfsImportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_GFS_IMPORTACAO, pathGfsImportacao);
	}

	/**
	 * @return the pathGfsExportacao
	 */
	public String getPathGfsExportacao() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_INTERFACE_GFS_EXPORTACAO);
	}

	/**
	 * @param pathGfsExportacao
	 *            the pathGfsExportacao to set
	 */
	public void setPathGfsExportacao(String pathGfsExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_GFS_EXPORTACAO, pathGfsExportacao);
	}

	/**
	 * @return the pathNfeImportacao
	 */
	public String getPathNfeImportacao() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTACAO);
	}

	/**
	 * @param pathNfeImportacao
	 *            the pathNfeImportacao to set
	 */
	public void setPathNfeImportacao(String pathNfeImportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTACAO, pathNfeImportacao);
	}

	/**
	 * @return the pathNfeExportacao
	 */
	public String getPathNfeExportacao() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO);
	}

	/**
	 * @param pathNfeExportacao
	 *            the pathNfeExportacao to set
	 */
	public void setPathNfeExportacao(String pathNfeExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO, pathNfeExportacao);
	}

	/**
	 * @return the nfeDpec
	 */
	public String getNfeDpec() {
		return this.getParametroSistemaString(TipoParametroSistema.NFE_DPEC);
	}

	/**
	 * @param nfeDpec
	 *            the nfeDpec to set
	 */
	public void setNfeDpec(String nfeDpec) {
		this.params.put(TipoParametroSistema.NFE_DPEC, Boolean.valueOf(nfeDpec) ? "TRUE" : "FALSE");
	}
	
	/**
	 * @return pathImageCapa
	 */
	public String getPathImageCapa() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_IMAGENS_CAPA);
	}

	/**
	 * 
	 * @param pathImageCapa
	 */
	public void setPathImageCapa(String pathImageCapa) {
		this.params.put(TipoParametroSistema.PATH_IMAGENS_CAPA, pathImageCapa);
	}

	/**
	 * @return pathImageBancaPdv
	 */
	public String getPathImageBancaPdv() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_IMAGENS_PDV);
	}

	/**
	 * @param pathImageBancaPdv
	 */
	public void setPathImageBancaPdv(String pathImageBancaPdv) {
		this.params.put(TipoParametroSistema.PATH_IMAGENS_PDV, pathImageBancaPdv);
	}

	/**
	 * @return the dtOperacaoCorrente
	 */
	public String getDtOperacaoCorrente() {
		return this.dtOperacaoCorrente;
	}

	/**
	 * @param dtOperacaoCorrente
	 *            the dtOperacaoCorrente to set
	 */
	public void setDtOperacaoCorrente(String dtOperacaoCorrente) {
		this.dtOperacaoCorrente = dtOperacaoCorrente;
	}

}
