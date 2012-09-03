<head>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>
<script language="text/javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/confirmDialog.js"></script>
<script language="javascript" type="text/javascript" 
	src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/parametroSistema.js"></script>

<script type="text/javascript">
	
$(function(){
	parametroSistemaController.init();
	
});
	
</script>

</head>

<body>

<form id="formParametroSistema" method="post">

		<div class="areaBts">
			<div class="area">
				<span class="bt_novos">
					<a onclick="parametroSistemaController.salvar();" href="javascript:;" rel="tipsy" title="Salvar">
					<img hspace="5" border="0" src="${pageContext.request.contextPath}/images/ico_salvar.gif">
					</a>
				</span>
			</div>
		</div>
		<div class="linha_separa_fields">&nbsp;</div>
		<fieldset class="fieldFiltro">
			<legend>Par&acirc;metros do Sistema</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="2">
				<thead />
				<tbody>
					<tr>
						<td>Vers&atilde;o Sistema:</td>
						<td><input disabled="disabled" type="text" name="dto.versaoSistema" id="versaoSistema" style="width:220px;" value="${parametroSistemaGeralDTO.versaoSistema}" /></td>
						<td>Interface GFS Exporta&ccedil;&atilde;o: </td>
						<td>
							<input disabled="disabled"  type="text" name="dto.pathGfsExportacao" id="pathGfsExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathGfsExportacao}" />
						</td>
					</tr>
					
					<tr>
						<td>Interface CE Exporta&ccedil;&atilde;o:</td>
						<td>
							<input disabled="disabled"  type="text" name="dto.pathCeExportacao" id="pathCeExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathCeExportacao}"/>
						</td>
						
						<td>Interface NFe Importa&ccedil;&atilde;o: </td>
						<td>
							<input disabled="disabled"  type="text" name="dto.pathNfeImportacao" id="pathNfeImportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathNfeImportacao}" />
						</td>
					</tr>
					
					<tr>
						<td>Interface PRODIN Exporta&ccedil;&atilde;o:</td>
						<td>
							<input disabled="disabled"  type="text" name="dto.pathProdinExportacao" id="pathProdinExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathProdinExportacao}"/>
						</td>
						
						<td>Interface NFe Exporta&ccedil;&atilde;o: </td>
						<td>
							<input disabled="disabled"  type="text" id="dto.pathNfeExportacao" name="pathNfeExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathNfeExportacao}" />
						</td>	
					</tr>
					
					<tr>
						<td width="164">Interface PRODIN Importa&ccedil;&atilde;o: </td>
						<td width="294">
							<input disabled="disabled"  type="text" name="dto.pathProdinImportacao" id="pathProdinImportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathProdinImportacao}"/>
						</td>	
						<td>NF-e em DPEC:</td>
						<td>
							<c:choose>
								<c:when test='${parametroSistemaGeralDTO.nfeDpec == "TRUE"}'>
									<input type="checkbox" id="nfeDpec" name="nfeDpec" checked="checked" value="TRUE" class="checkboxNfeDpec"/> 
								</c:when>
								<c:otherwise>
									<input type="checkbox" id="nfeDpec" name="nfeDpec" value="TRUE" class="checkboxNfeDpec"/>
								</c:otherwise>
							</c:choose>
							<input type="hidden" name="dto.nfeDpec" id="nfeDpecHidden" />
						</td>
					</tr>
					<tr>
						<td>Interface MDC Importa&ccedil;&atilde;o:</td>
						<td>
							<input disabled="disabled" type="text" name="dto.pathMdcImportacao" id="pathMdcImportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathMdcImportacao}" />
						</td>
						<td>Imagem Capa:</td>
						<td><input type="text" name="dto.pathImageCapa" id="pathImageCapa" style="width:220px;" value="${parametroSistemaGeralDTO.pathImageCapa}" /></td>
					</tr>
					<tr>
						<td>Interface MDC Exporta&ccedil;&atilde;o:</td>
						<td>
							<input disabled="disabled" type="text" name="dto.pathMdcExportacao" id="pathMdcExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathMdcExportacao}" />
						</td>
						<td>Imagem banca PDV</td>
						<td><input type="text" name="dto.pathImageBancaPdv" id="pathImageBancaPdv" style="width:220px;" value="${parametroSistemaGeralDTO.pathImageBancaPdv}" /></td>
					</tr>
					
					<tr>	
						<td>Interface Bancas Exporta&ccedil;&atilde;o: </td>
						<td>
							<input disabled="disabled" type="text" name="dto.pathBancasExportacao" id="pathBancasExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathBancasExportacao}" />
						</td>
						<td>Data Opera&ccedil;&atilde;o Corrente:</td>
						<td><input type="text" name="dto.dtOperacaoCorrente" id="dtOperacaoCorrente" style="width:100px; text-align:center;" disabled="disabled" value="${parametroSistemaGeralDTO.dtOperacaoCorrente}" /></td>
					</tr>
					
					<tr>
						<td>Interface GFS Importa&ccedil;&atilde;o: </td>
						<td>
							<input disabled="disabled" type="text" name="dto.pathGfsImportacao" id="pathGfsImportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathGfsImportacao}" />
						</td>	
						<td>Frequência Expurgo:</td>
						<td>
							<input type="text" maxlength="4" name="dto.frequenciaExpurgo" id="frequenciaExpurgo" style="width:100px;" value="${parametroSistemaGeralDTO.frequenciaExpurgo}" />
							meses
						</td>
					</tr>
				</tbody>
			</table>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>


</form>
</body>
