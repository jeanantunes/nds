package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.StringUtils;

import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.model.integracao.ParametroSistema;

/**
 * DTO que agrupa os parâmetros do sistema a serem exibidos/alterados na tela
 * "Administração > Parâmetros do Sistema".
 * 
 * @author Discover Technology
 */
public class ParametroSistemaGeralDTO implements Serializable {
	
	/** */
	private static final long serialVersionUID = -7477289076396360578L;
	

	private Map<TipoParametroSistema, String> params = new HashMap<TipoParametroSistema, String>();
	
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
		for (Entry<TipoParametroSistema, String> entry : params.entrySet()) {
			ParametroSistema ps = new ParametroSistema();
			ps.setTipoParametroSistema(entry.getKey());
			ps.setValor(StringUtils.trimWhitespace(entry.getValue()));
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
	 * @param emailRemetente
	 *            the email to set
	 */
	public void setEmailRemetente(String emailRemetente) {
		this.params.put(TipoParametroSistema.EMAIL_REMETENTE, emailRemetente);
	}

	/**
	 * @return the emailRemetente
	 */
	public String getEmailRemetente() {
		return this.getParametroSistemaString(TipoParametroSistema.EMAIL_REMETENTE);
	}
	
	/**
	 * @return the autenticaEmail
	 */
	public String getAutenticaEmail() {
		return this.getParametroSistemaString(TipoParametroSistema.EMAIL_AUTENTICAR);
	}

	/**
	 * @param autenticaEmail
	 *            the autenticaEmail to set
	 */
	public void setAutenticaEmail(String autenticaEmail) {
		this.params.put(TipoParametroSistema.EMAIL_AUTENTICAR, Boolean.valueOf(autenticaEmail) ? "TRUE" : "FALSE");
	} 
	
	/**
	 * @param emailUsuario
	 *            the email to set
	 */
	public void setEmailUsuario(String emailUsuario) {
		this.params.put(TipoParametroSistema.EMAIL_USUARIO, emailUsuario);
	}

	/**
	 * @return the emailUsuario
	 */
	public String getEmailUsuario() {
		return this.getParametroSistemaString(TipoParametroSistema.EMAIL_USUARIO);
	}

	/**
	 * @param host
	 */
	public void setHost(String host) {
		this.params.put(TipoParametroSistema.EMAIL_HOST, host);
	}
	
	/**
	 * @return the host
	 */
	public String getHost() {
		return this.getParametroSistemaString(TipoParametroSistema.EMAIL_HOST);
	}
	
	/**
	 * @param protocolo
	 */
	public void setProtocolo(String protocolo) {
		this.params.put(TipoParametroSistema.EMAIL_PROTOCOLO, protocolo);
	}
	
	/**
	 * @return the protocolo
	 */
	public String getProtocolo() {
		return this.getParametroSistemaString(TipoParametroSistema.EMAIL_PROTOCOLO);
	}
	
	/**
	 * @param porta
	 */
	public void setPorta(String porta) {
		this.params.put(TipoParametroSistema.EMAIL_PORTA, porta);
	}
	
	/**
	 * @return the porta
	 */
	public String getPorta() {
		return this.getParametroSistemaString(TipoParametroSistema.EMAIL_PORTA);
	}
	
	/**
	 * @param senha
	 */
	public void setSenha(String senha) {
		this.params.put(TipoParametroSistema.EMAIL_SENHA, senha);
	}
	
	/**
	 * @return the senha
	 */
	public String getSenha() {
		return this.getParametroSistemaString(TipoParametroSistema.EMAIL_SENHA);
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
	 * @return the pathPickingExportacao
	 */
	public String getPathPickingExportacao() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_INTERFACE_PICKING_EXPORTACAO);
	}

	/**
	 * @param pathPickingExportacao
	 *            the pathPickingExportacao to set
	 */
	public void setPathPickingExportacao(String pathPickingExportacao) {
		this.params.put(TipoParametroSistema.PATH_INTERFACE_PICKING_EXPORTACAO, pathPickingExportacao);
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
	 * @return pathImportacaoContrato
	 */
	public String getPathContrato() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_IMPORTACAO_CONTRATO);
	}
	
	/**
	 * @param pathContrato
	 */
	public void setPathContrato(String pathContrato) {
		this.params.put(TipoParametroSistema.PATH_IMPORTACAO_CONTRATO, pathContrato);
	}
	
	public String getNfeInformacoesAmbiente() {
		return this.getParametroSistemaString(TipoParametroSistema.NFE_INFORMACOES_AMBIENTE);
	}
	
	public void setNfeInformacoesAmbiente(String nfeInformacoesAmbiente) {
		this.params.put(TipoParametroSistema.NFE_INFORMACOES_AMBIENTE, nfeInformacoesAmbiente);
	}
	
	public String getNfeInformacoesFormatoImpressao() {
		return this.getParametroSistemaString(TipoParametroSistema.NFE_INFORMACOES_FORMATO_IMPRESSAO);
	}
	
	public void setNfeInformacoesFormatoImpressao(String nfeInformacoesFormatoImpressao) {
		this.params.put(TipoParametroSistema.NFE_INFORMACOES_FORMATO_IMPRESSAO, nfeInformacoesFormatoImpressao);
	}
	
	public String getNfeInformacoesModeloDocumento() {
		return this.getParametroSistemaString(TipoParametroSistema.NFE_INFORMACOES_MODELO_DOCUMENTO);
	}
	
	public void setNfeInformacoesModeloDocumento(String nfeInformacoesModeloDocumento) {
		this.params.put(TipoParametroSistema.NFE_INFORMACOES_MODELO_DOCUMENTO, nfeInformacoesModeloDocumento);
	}
	
	public String getNfeInformacoesTipoEmissor() {
		return this.getParametroSistemaString(TipoParametroSistema.NFE_INFORMACOES_TIPO_EMISSOR);
	}
	
	public void setNfeInformacoesTipoEmissor(String nfeInformacoesTipoEmissor) {
		this.params.put(TipoParametroSistema.NFE_INFORMACOES_TIPO_EMISSOR, nfeInformacoesTipoEmissor);
	}
	
	public String getNfeInformacoesVersaoEmissor() {
		return this.getParametroSistemaString(TipoParametroSistema.NFE_INFORMACOES_VERSAO_EMISSOR);
	}
	
	public void setNfeInformacoesVersaoEmissor(String nfeInformacoesVersaoEmissor) {
		this.params.put(TipoParametroSistema.NFE_INFORMACOES_VERSAO_EMISSOR, nfeInformacoesVersaoEmissor);
	}
	
	public void setNfePathCertficado(String nfePathCertficado) {
		this.params.put(TipoParametroSistema.NFE_PATH_CERTIFICADO, nfePathCertficado);
	}
	
	public String getNfePathCertficado() {
		return this.getParametroSistemaString(TipoParametroSistema.NFE_PATH_CERTIFICADO);
	}
	
	public void setFtfCodigoEstabelecimentoEmissor(String ftfCodigoEstabelecimentoEmissor) {
		this.params.put(TipoParametroSistema.FTF_CODIGO_ESTABELECIMENTO_EMISSOR, ftfCodigoEstabelecimentoEmissor);
	}
	
	public String getFtfCodigoEstabelecimentoEmissor() {
		return this.getParametroSistemaString(TipoParametroSistema.FTF_CODIGO_ESTABELECIMENTO_EMISSOR);
	}
	
	public void setFtfCnpjEstabelecimentoEmissor(String ftfCnpjEstabelecimentoEmissor) {
		this.params.put(TipoParametroSistema.FTF_CNPJ_ESTABELECIMENTO_EMISSOR, ftfCnpjEstabelecimentoEmissor);
	}
	
	public String getFtfCnpjEstabelecimentoEmissor() {
		return this.getParametroSistemaString(TipoParametroSistema.FTF_CNPJ_ESTABELECIMENTO_EMISSOR);
	}
    
	public void setFtfCodigoLocal(String ftfCodigoLocal) {
		this.params.put(TipoParametroSistema.FTF_CODIGO_LOCAL, ftfCodigoLocal);
	}
	
	public String getFtfCodigoLocal() {
		return this.getParametroSistemaString(TipoParametroSistema.FTF_CODIGO_LOCAL);
	}
	
	public void setFtfCodigoCentroEmissor(String ftfCodigoCentroEmissor) {
		this.params.put(TipoParametroSistema.FTF_CODIGO_CENTRO_EMISSOR, ftfCodigoCentroEmissor);
	}
	
	public String getFtfCodigoCentroEmissor() {
		return this.getParametroSistemaString(TipoParametroSistema.FTF_CODIGO_CENTRO_EMISSOR);
	}
	
	public String getPathTransfArquivos() {
		return this.getParametroSistemaString(TipoParametroSistema.PATH_TRANSFERENCIA_ARQUIVO);
	}
	
	public void setPathTransfArquivos(String PathTransfArquivos) {
		this.params.put(TipoParametroSistema.PATH_TRANSFERENCIA_ARQUIVO, PathTransfArquivos);
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