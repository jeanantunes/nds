<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}" />

<head>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>
<script language="text/javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/confirmDialog.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/parametroSistema.js"></script>

<script type="text/javascript">
	
$(function(){
	parametroSistemaController.init();
	bloquearItensEdicao(parametroSistemaController.workspace);
});
	
</script>

</head>

<body>

<form id="formParametroSistema" method="post">

		<div class="areaBts">
			<div class="area">
				<span class="bt_novos">
					<a isEdicao="true" onclick="parametroSistemaController.salvar();" href="javascript:;" rel="tipsy" title="Salvar">
					<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_salvar.gif">
					</a>
				</span>
			</div>
		</div>
		<div class="linha_separa_fields">&nbsp;</div>
		<fieldset class="fieldFiltro ">
			<legend>Par&acirc;metros do Sistema</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="2">
				<thead />
				<tbody>
					<tr>
						<td>
							<table>
								<tr>
									<td>E-mail</td>
									<td><input type="text" name="dto.emailRemetente" id="emailRemetente" style="width:220px;" value="${parametroSistemaGeralDTO.emailRemetente}" /></td>
								</tr>
								<tr>
									<td>Host</td>
									<td><input type="text" name="dto.host" id="host" style="width:220px;" value="${parametroSistemaGeralDTO.host}" /></td>
								</tr>
								<tr>
									<td>Porta</td>
									<td><input type="text" name="dto.porta" id="porta" style="width:220px;" value="${parametroSistemaGeralDTO.porta}" /></td>
								</tr>
								<tr>
									<td>Protocolo</td>
									<td><input type="text" name="dto.protocolo" id="protocolo" style="width:220px;" value="${parametroSistemaGeralDTO.protocolo}" /></td>
								</tr>
								<tr>
									<td>Usuário</td>
									<td><input type="text" name="dto.emailUsuario" id="emailUsuario" style="width:220px;" value="${parametroSistemaGeralDTO.emailUsuario}" /></td>
								</tr>
								<tr>
									<td>Senha</td>
									<td><input type="text" name="dto.senha" id="senha" style="width:220px;" value="${parametroSistemaGeralDTO.senha}" /></td>
								</tr>
								<tr>
									<td>Autenticar e-mail?</td>
									<td>
										<c:choose>
											<c:when test='${parametroSistemaGeralDTO.autenticaEmail == "TRUE"}'>
												<input type="checkbox" id="autenticaEmail" name="autenticaEmail" checked="checked" value="TRUE" /> 
											</c:when>
											<c:otherwise>
												<input type="checkbox" id="autenticaEmail" name="autenticaEmail" value="TRUE" />
											</c:otherwise>
										</c:choose>
										<input type="hidden" name="dto.autenticaEmail" id="autenticaEmailHidden" />
									</td>
								</tr>
								<tr>
									<td>Interface CE Exporta&ccedil;&atilde;o:</td>
									<td>
										<input disabled="disabled"  type="text" name="dto.pathCeExportacao" id="pathCeExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathCeExportacao}"/>
									</td>
								</tr>
								<tr>
									<td>Interface PRODIN Exporta&ccedil;&atilde;o:</td>
									<td>
										<input disabled="disabled"  type="text" name="dto.pathProdinExportacao" id="pathProdinExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathProdinExportacao}"/>
									</td>
								</tr>
								<tr>
									<td width="164">Interface PRODIN Importa&ccedil;&atilde;o: </td>
									<td width="294">
										<input disabled="disabled"  type="text" name="dto.pathProdinImportacao" id="pathProdinImportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathProdinImportacao}"/>
									</td>	
								</tr>
								<tr>
									<td>Interface MDC Importa&ccedil;&atilde;o:</td>
									<td>
										<input disabled="disabled" type="text" name="dto.pathMdcImportacao" id="pathMdcImportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathMdcImportacao}" />
									</td>
								</tr>
							</table>
						</td>
							
						<td valign="top">
							<table>
								<tr>
									<td>Interface Bancas Exporta&ccedil;&atilde;o: </td>
									<td>
										<input disabled="disabled" type="text" name="dto.pathBancasExportacao" id="pathBancasExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathBancasExportacao}" />
									</td>
								</tr>
								<tr>
									<td>Interface Picking Exporta&ccedil;&atilde;o: </td>
									<td>
										<input disabled="disabled" type="text" name="dto.pathPickingExportacao" id="pathPickingExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathPickingExportacao}" />
									</td>
								</tr>
								
								<tr>
									<td>Interface Contrato Importa&ccedil;&atilde;o: </td>
									<td>
										<input type="text" name="dto.pathContrato" id="pathContrato" style="width:220px;" value="${parametroSistemaGeralDTO.pathContrato}" />
									</td>
								</tr>
								<tr>
									<td>Interface GFS Importa&ccedil;&atilde;o: </td>
									<td>
										<input disabled="disabled" type="text" name="dto.pathGfsImportacao" id="pathGfsImportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathGfsImportacao}" />
									</td>
								</tr>
								<tr>
									<td>Interface GFS Exporta&ccedil;&atilde;o: </td>
									<td>
										<input disabled="disabled"  type="text" name="dto.pathGfsExportacao" id="pathGfsExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathGfsExportacao}" />
									</td>
								</tr>
								<tr>
									<td>Interface NFe Importa&ccedil;&atilde;o: </td>
									<td>
										<input disabled="disabled"  type="text" name="dto.pathNfeImportacao" id="pathNfeImportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathNfeImportacao}" />
									</td>
								</tr>
								<tr>
									<td>Interface NFe Exporta&ccedil;&atilde;o: </td>
									<td>
										<input disabled="disabled"  type="text" id="dto.pathNfeExportacao" name="pathNfeExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathNfeExportacao}" />
									</td>
								</tr>
								<tr>
									<td>Imagem Capa:</td>
									<td><input type="text" name="dto.pathImageCapa" id="pathImageCapa" style="width:220px;" value="${parametroSistemaGeralDTO.pathImageCapa}" /></td>
								</tr>
								<tr>
									<td>Imagem banca PDV</td>
									<td><input type="text" name="dto.pathImageBancaPdv" id="pathImageBancaPdv" style="width:220px;" value="${parametroSistemaGeralDTO.pathImageBancaPdv}" /></td>
								</tr>
								<tr>
									<td>Data Opera&ccedil;&atilde;o Corrente:</td>
									<td><input type="text" name="dto.dtOperacaoCorrente" id="dtOperacaoCorrente" style="width:100px; text-align:center;" disabled="disabled" value="${parametroSistemaGeralDTO.dtOperacaoCorrente}" /></td>
								</tr>
								<tr>
									<td>Interface MDC Exporta&ccedil;&atilde;o:</td>
									<td>
										<input disabled="disabled" type="text" name="dto.pathMdcExportacao" id="pathMdcExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathMdcExportacao}" />
									</td>
								</tr>
								<%--
								Funcionalidade desabilitada ate a criação das rotinas de expurgo
								<tr>
									<td>Frequência Expurgo:</td>
									<td>
										<input type="text" maxlength="4" name="dto.frequenciaExpurgo" id="frequenciaExpurgo" style="width:100px;" value="${parametroSistemaGeralDTO.frequenciaExpurgo}" />
										meses
									</td>
								</tr>
								--%>
							</table>
						</td>
					</tr>
					
				</tbody>
			</table>
		</fieldset>
		
		<fieldset class="fieldFiltro">
			<legend>Parâmetros NF-e</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="2">
				<thead />
				<tbody>
					<tr>
						<td>
							<table>
								<tr>
									<td width="164">Ambiente: </td>
									<td width="294">
										<select name="dto.nfeInformacoesAmbiente" id="nfeInformacoesAmbiente" style="width:220px;">
											<c:forEach  var="tipoAmbiente" items="${tiposAmbientes}">
												<option value="${tipoAmbiente.key}" <c:if test="${parametroSistemaGeralDTO.nfeInformacoesAmbiente == tipoAmbiente.key}">selected="selected"</c:if>>${tipoAmbiente.value}</option>
											</c:forEach>											
										</select>
									</td>	
								</tr>
								<tr>
									<td>Formato Impressão:</td>
									<td>
										<select name="dto.nfeInformacoesFormatoImpressao" id="nfeInformacoesFormatoImpressao" style="width:220px;">
											<c:forEach  var="formatoImpressao" items="${formatosImpressao}">
												<option value="${formatoImpressao.key}" <c:if test="${parametroSistemaGeralDTO.nfeInformacoesFormatoImpressao == formatoImpressao.key}">selected="selected"</c:if>>${formatoImpressao.value}</option>
											</c:forEach>											
										</select>
									</td>
								</tr>
								<tr>
									<td>Modelo Documento Fiscal: </td>
									<td>
										<input type="text" name="dto.nfeInformacoesModeloDocumento" id="nfeInformacoesModeloDocumento" style="width:220px;" maxlength="5" value="${parametroSistemaGeralDTO.nfeInformacoesModeloDocumento}" />
									</td>
								</tr>
							</table>
						</td>
							
						<td valign="top">
							<table>
								<tr>
									<td>Tipo do Emissor de NF-e:</td>
									<td>
										<select name="dto.nfeInformacoesTipoEmissor" id="nfeInformacoesTipoEmissor" style="width:220px;">
											<c:forEach var="processoEmissaoNFe" items="${processosEmissaoNFe}">
												<option value="${processoEmissaoNFe.key}" <c:if test="${parametroSistemaGeralDTO.nfeInformacoesTipoEmissor == processoEmissaoNFe.key}">selected="selected"</c:if>>${processoEmissaoNFe.value}</option>
											</c:forEach>											
										</select>
									</td>
								</tr>
								<tr>
									<td>Versão do Emissor:</td>
									<td>
										<input type="text" name="dto.nfeInformacoesVersaoEmissor" id="nfeInformacoesVersaoEmissor" style="width:220px;" maxlength="10" value="${parametroSistemaGeralDTO.nfeInformacoesVersaoEmissor}"/>
									</td>
								</tr>
								<tr>
									<td>Caminho do Certificado NF-e:</td>
									<td>
										<input disabled="disabled" type="text" name="dto.nfePathCertficado" id="nfePathCertficado" style="width:220px;" maxlength="10" value="${parametroSistemaGeralDTO.nfePathCertficado}"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					
				</tbody>
			</table>
			
		</fieldset>
		
		<c:if test="${utilizaFTF}">
		<fieldset class="fieldFiltro">
			<legend>FTF</legend>
		
			<table width="950" border="0" cellpadding="2" cellspacing="2">
				<thead />
				<tbody>
					<tr>
						<td>
							<table>
								<tr>
									<td> C&oacute;digo do Estabelecimento Emissor:</td>
									<td> <input id="ftfCodigoEstabelecimentoEmissor" name="dto.ftfCodigoEstabelecimentoEmissor" value="${parametroSistemaGeralDTO.ftfCodigoEstabelecimentoEmissor}" type="text" /> </td>
								</tr>
								<tr>
									<td>CNPJ Estabelecimento Emissor:</td>
									<td> <input id="ftfCnpjEstabelecimentoEmissor" name="dto.ftfCnpjEstabelecimentoEmissor" value="${parametroSistemaGeralDTO.ftfCnpjEstabelecimentoEmissor}" type="text" /></td>
								</tr>
							</table>
						</td>
						<td>
							<table>
								<tr>
									<td> C&oacute;digo local:</td>
									<td> <input id="ftfCodigoLocal" name="dto.ftfCodigoLocal" value="${parametroSistemaGeralDTO.ftfCodigoLocal}" type="text" /> </td>
								</tr>
								<tr>
									<td> C&oacute;digo do Centro Emissor: </td>
									<td><input id="ftfCodigoCentroEmissor" name="dto.ftfCodigoCentroEmissor" value="${parametroSistemaGeralDTO.ftfCodigoCentroEmissor}" type="text" /></td>
								</tr>
							</table>
						</td>
					</tr>
				</tbody>
				</table>
		</fieldset>
		</c:if>
		
		<div class="linha_separa_fields">&nbsp;</div>

</form>
</body>
