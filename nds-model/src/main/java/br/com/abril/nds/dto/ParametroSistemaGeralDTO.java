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
	private static final long serialVersionUID = 4527330351986164865L;
	
	
	private Map<TipoParametroSistema, ParametroSistema> params = 
			new HashMap<TipoParametroSistema, ParametroSistema>();
	
	
	/** Método Construtor padrão. */
	public ParametroSistemaGeralDTO() {
	}
	
	
	/**
	 * Obtém todos os parâmetros de sistema contidos nesta DTO.
	 *  
	 * @return
	 */
	public List<ParametroSistema> getParametrosSistema() {
		return new ArrayList<ParametroSistema>(params.values());
	}
	
	/**
	 * Popula o DTO com os parametros do sistema.
	 * 
	 * @param parametrosSistema
	 */
	public void setParametrosSistema(Collection<ParametroSistema> parametrosSistema) {
		for (ParametroSistema ps : parametrosSistema) {
			params.put(ps.getTipoParametroSistema(), ps);
		}
	}
	
	
	/**
	 * @return the pathLogo
	 */
	public ParametroSistema getPathLogo() {
		return this.params.get(TipoParametroSistema.PATH_LOGO_SISTEMA);
	}

	/**
	 * @param pathLogo
	 *            the pathLogo to set
	 */
	public void setPathLogo(ParametroSistema pathLogo) {
		this.params.put(TipoParametroSistema.PATH_LOGO_SISTEMA, pathLogo);
	}

	/**
	 * @return the cnpj
	 */
	public ParametroSistema getCnpj() {
		return this.params.get(TipoParametroSistema.CNPJ);
	}

	/**
	 * @param cnpj
	 *            the cnpj to set
	 */
	public void setCnpj(ParametroSistema cnpj) {
		this.params.put(TipoParametroSistema.CNPJ, cnpj);
	}

	/**
	 * @return the razaoSocial
	 */
	public ParametroSistema getRazaoSocial() {
		return this.params.get(TipoParametroSistema.RAZAO_SOCIAL);
	}

	/**
	 * @param razaoSocial
	 *            the razaoSocial to set
	 */
	public void setRazaoSocial(ParametroSistema razaoSocial) {
		this.params.put(TipoParametroSistema.RAZAO_SOCIAL, razaoSocial);
	}

	/**
	 * @return the email
	 */
	public ParametroSistema getEmail() {
		return this.params.get(TipoParametroSistema.EMAIL_USUARIO);
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(ParametroSistema email) {
		this.params.put(TipoParametroSistema.EMAIL_USUARIO, email);
	}

	/**
	 * @return the uf
	 */
	public ParametroSistema getUf() {
		return this.params.get(TipoParametroSistema.UF);
	}

	/**
	 * @param uf
	 *            the uf to set
	 */
	public void setUf(ParametroSistema uf) {
		this.params.put(TipoParametroSistema.UF, uf);
	}

	/**
	 * @return the codDistribuidorDinap
	 */
	public ParametroSistema getCodDistribuidorDinap() {
		return this.params.get(TipoParametroSistema.CODIGO_DISTRIBUIDOR_DINAP);
	}

	/**
	 * @param codDistribuidorDinap
	 *            the codDistribuidorDinap to set
	 */
	public void setCodDistribuidorDinap(ParametroSistema codDistribuidorDinap) {
		this.params.put(TipoParametroSistema.CODIGO_DISTRIBUIDOR_DINAP, codDistribuidorDinap);
	}

	/**
	 * @return the codDistribuidorFc
	 */
	public ParametroSistema getCodDistribuidorFc() {
		return this.params.get(TipoParametroSistema.CODIGO_DISTRIBUIDOR_FC);
	}

	/**
	 * @param codDistribuidorFc
	 *            the codDistribuidorFc to set
	 */
	public void setCodDistribuidorFc(ParametroSistema codDistribuidorFc) {
		this.params.put(TipoParametroSistema.CODIGO_DISTRIBUIDOR_FC, codDistribuidorFc);
	}

	/**
	 * @return the login
	 */
	public ParametroSistema getLogin() {
		return this.params.get(TipoParametroSistema.LOGIN_DISTRIBUIDOR);
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(ParametroSistema login) {
		this.params.put(TipoParametroSistema.LOGIN_DISTRIBUIDOR, login);
	}

	/**
	 * @return the senha
	 */
	public ParametroSistema getSenha() {
		return this.params.get(TipoParametroSistema.SENHA_DISTRIBUIDOR);
	}

	/**
	 * @param senha
	 *            the senha to set
	 */
	public void setSenha(ParametroSistema senha) {
		this.params.put(TipoParametroSistema.SENHA_DISTRIBUIDOR, senha);
	}

	/**
	 * @return the versaoSistema
	 */
	public ParametroSistema getVersaoSistema() {
		return this.params.get(TipoParametroSistema.VERSAO_SISTEMA);
	}

	/**
	 * @param versaoSistema
	 *            the versaoSistema to set
	 */
	public void setVersaoSistema(ParametroSistema versaoSistema) {
		this.params.put(TipoParametroSistema.VERSAO_SISTEMA, versaoSistema);
	}

	/**
	 * @return the pathCeExportacao
	 */
	public ParametroSistema getPathCeExportacao() {
		return this.params.get(TipoParametroSistema.PATH_INTERFACE_CE_EXPORTACAO);
	}

	/**
	 * @param pathCeExportacao
	 *            the pathCeExportacao to set
	 */
	public void setPathCeExportacao(ParametroSistema pathCeExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_CE_EXPORTACAO, pathCeExportacao);
	}

	/**
	 * @return the pathProdinImportcao
	 */
	public ParametroSistema getPathProdinImportcao() {
		return this.params.get(TipoParametroSistema.PATH_INTERFACE_PRODIN_IMPORTCAO);
	}

	/**
	 * @param pathProdinImportcao
	 *            the pathProdinImportcao to set
	 */
	public void setPathProdinImportcao(ParametroSistema pathProdinImportcao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_PRODIN_IMPORTCAO, pathProdinImportcao);
	}

	/**
	 * @return the pathProdinExportacao
	 */
	public ParametroSistema getPathProdinExportacao() {
		return this.params.get(TipoParametroSistema.PATH_INTERFACE_PRODIN_EXPORTACAO);
	}

	/**
	 * @param pathProdinExportacao
	 *            the pathProdinExportacao to set
	 */
	public void setPathProdinExportacao(ParametroSistema pathProdinExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_PRODIN_EXPORTACAO, pathProdinExportacao);
	}

	/**
	 * @return the pathMdcImportcao
	 */
	public ParametroSistema getPathMdcImportcao() {
		return this.params.get(TipoParametroSistema.PATH_INTERFACE_MDC_IMPORTCAO);
	}

	/**
	 * @param pathMdcImportcao
	 *            the pathMdcImportcao to set
	 */
	public void setPathMdcImportcao(ParametroSistema pathMdcImportcao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_MDC_IMPORTCAO, pathMdcImportcao);
	}

	/**
	 * @return the pathMdcExportacao
	 */
	public ParametroSistema getPathMdcExportacao() {
		return this.params.get(TipoParametroSistema.PATH_INTERFACE_MDC_EXPORTACAO);
	}

	/**
	 * @param pathMdcExportacao
	 *            the pathMdcExportacao to set
	 */
	public void setPathMdcExportacao(ParametroSistema pathMdcExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_MDC_EXPORTACAO, pathMdcExportacao);
	}

	/**
	 * @return the pathBancasExportacao
	 */
	public ParametroSistema getPathBancasExportacao() {
		return this.params.get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO);
	}

	/**
	 * @param pathBancasExportacao
	 *            the pathBancasExportacao to set
	 */
	public void setPathBancasExportacao(ParametroSistema pathBancasExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO, pathBancasExportacao);
	}

	/**
	 * @return the pathGfsImportcao
	 */
	public ParametroSistema getPathGfsImportcao() {
		return this.params.get(TipoParametroSistema.PATH_INTERFACE_GFS_IMPORTCAO);
	}

	/**
	 * @param pathGfsImportcao
	 *            the pathGfsImportcao to set
	 */
	public void setPathGfsImportcao(ParametroSistema pathGfsImportcao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_GFS_IMPORTCAO, pathGfsImportcao);
	}

	/**
	 * @return the pathGfsExportacao
	 */
	public ParametroSistema getPathGfsExportacao() {
		return this.params.get(TipoParametroSistema.PATH_INTERFACE_GFS_EXPORTACAO);
	}

	/**
	 * @param pathGfsExportacao
	 *            the pathGfsExportacao to set
	 */
	public void setPathGfsExportacao(ParametroSistema pathGfsExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_GFS_EXPORTACAO, pathGfsExportacao);
	}

	/**
	 * @return the pathNfeImportcao
	 */
	public ParametroSistema getPathNfeImportcao() {
		return this.params.get(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTCAO);
	}

	/**
	 * @param pathNfeImportcao
	 *            the pathNfeImportcao to set
	 */
	public void setPathNfeImportcao(ParametroSistema pathNfeImportcao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_NFE_IMPORTCAO, pathNfeImportcao);
	}

	/**
	 * @return the pathNfeExportacao
	 */
	public ParametroSistema getPathNfeExportacao() {
		return this.params.get(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO);
	}

	/**
	 * @param pathNfeExportacao
	 *            the pathNfeExportacao to set
	 */
	public void setPathNfeExportacao(ParametroSistema pathNfeExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO, pathNfeExportacao);
	}

	/**
	 * @return the nfeDpec
	 */
	public ParametroSistema getNfeDpec() {
		return this.params.get(TipoParametroSistema.NFE_DPEC);
	}

	/**
	 * @param nfeDpec
	 *            the nfeDpec to set
	 */
	public void setNfeDpec(ParametroSistema nfeDpec) {
		this.params.put(TipoParametroSistema.NFE_DPEC, nfeDpec);
	}

	/**
	 * @return the dtOperacaoCorrente
	 */
	public ParametroSistema getDtOperacaoCorrente() {
		return this.params.get(TipoParametroSistema.DATA_OPERACAO_CORRENTE);
	}

	/**
	 * @param dtOperacaoCorrente
	 *            the dtOperacaoCorrente to set
	 */
	public void setDtOperacaoCorrente(ParametroSistema dtOperacaoCorrente) {
		this.params.put(TipoParametroSistema.DATA_OPERACAO_CORRENTE, dtOperacaoCorrente);
	}

}
