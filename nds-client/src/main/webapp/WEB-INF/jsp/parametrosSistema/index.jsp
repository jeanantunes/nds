<head>
</head>

<body>

<div id="dialog-confirm" title="Salvar Par&acirc;metro de Cobrança">
	<p>Confirma os Par&acirc;metro de Cobrança?</p>
</div>



<form id="formUpload" action="${pageContext.request.contextPath}/administracao/parametrosSistema/salvar" method="post" enctype="multipart/form-data" >


<div class="corpo">
	<br />
	<div class="container">
		<fieldset class="classFieldset">
			<legend>Par&acirc;metros do Sistema</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<thead />
				<tbody>
					<tr>
						<td>
						
							<!-- TODO: exibir logo já cadastrado, caso houver -->
							<img src="${pageContext.request.contextPath}/images/${parametroSistemaGeralDTO.logoSistema}" width="110" height="70" alt="Logo Sistema" />
						
							<!-- TODO: evento para excluir a image (grava 'elemento neutro' no path da imagem) -->
							<a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Excluir Logo" width="15" height="15" border="0" /></a>
						</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>Logo:</td>
						
						<!-- TODO: ver como pegar isso  -->
						<td><input type="file"  name="imgLogoSistema" id="imgLogoSistema" /></td>
						<td>Interface CE Exporta&ccedil;&atilde;o:</td>
						<td>
							<input type="text" name="dto.pathCeExportacao" id="dto.pathCeExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathCeExportacao}"/>
							
							<!-- 
							<span class="bt_novos" style="margin:0px!important; padding-top:5px; padding-bottom:5px; padding-left:5px;">
								<img src="${pageContext.request.contextPath}/images/ico_pasta.jpg" width="21" height="15" alt="Caminho" />
							</span>
							<input name="file_original" type="file" id="file_original" class="file_original" onchange="document.getElementById('file_falso').value = this.value;" size="30"  style="border:1px solid #000; "/>
							<input name="file_falso" type="text" id="file_falso" class="file_falso" style="float:left; " />
							 -->
						</td>
					</tr>
					<tr>
						<td width="166">CNPJ:</td>
						<td width="305"><input type="text" name="dto.cnpj" id="dto.cnpj" style="width:220px;" value="${parametroSistemaGeralDTO.cnpj}"/></td>
						<td width="164">Interface PRODIN Importa&ccedil;&atilde;o: </td>
						<td width="294">
							<input type="text" name="dto.pathProdinImportacao" id="dto.pathProdinImportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathProdinImportacao}"/>
						</td>
					</tr>
					<tr>
						<td>Raz&atilde;o Social:</td>
						<td><input type="text" name="dto.razaoSocial" id="dto.razaoSocial" style="width:220px;" value="${parametroSistemaGeralDTO.razaoSocial}"/></td>
						<td>Interface PRODIN Exporta&ccedil;&atilde;o:</td>
						<td>
							<input type="text" name="dto.pathProdinExportacao" id="dto.pathProdinExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathProdinExportacao}"/>
						</td>
					</tr>
					<tr>
						<td>E-mail:</td>
						<td><input type="text" name="dto.email" id="dto.email" style="width:220px;" value="${parametroSistemaGeralDTO.email}" /></td>
						<td>Interface MDC Importa&ccedil;&atilde;o:</td>
						<td>
							<input type="text" name="dto.pathMdcImportacao" id="dto.pathMdcImportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathMdcImportacao}" />
						</td>
					</tr>
					<tr>
						<td>UF:</td>
						<td>
							<select name="dto.uf" id="dto.uf" style="width:65px;" >
								<option value="" ></option>
								<c:forEach varStatus="index" var="itemUf" items="${listaUf}">
									<c:choose>
									
										<c:when test="${parametroSistemaGeralDTO.uf eq itemUf.sigla}" >
											<option value="${itemUf.sigla}" selected="selected" >${itemUf.sigla}</option>
										</c:when>
										<c:otherwise>
											<option value="${itemUf.sigla}">${itemUf.sigla}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</td>
						
						<td>Interface MDC Exporta&ccedil;&atilde;o:</td>
						<td>
							<input type="text" name="dto.pathMdcExportacao" id="dto.pathMdcExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathMdcExportacao}" />
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>Interface MDC Backup:</td>
						<td>
							<input type="text" name="dto.pathMdcBackup" id="dto.pathMdcBackup" style="width:220px;" value="${parametroSistemaGeralDTO.pathMdcBackup}" />
						</td>
					</tr>
					<tr>
						<td>C&oacute;digo Distribuidor Dinap:</td>
						<td><input type="text" name="dto.codDistribuidorDinap" id="dto.codDistribuidorDinap" style="width:220px;" value="${parametroSistemaGeralDTO.codDistribuidorDinap}" /></td>
						<td>Interface Bancas Exporta&ccedil;&atilde;o: </td>
						<td>
							<input type="text" name="dto.pathBancasExportacao" id="dto.pathBancasExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathBancasExportacao}" />
						</td>
					</tr>
					<tr>
						<td>C&oacute;digo Distribuidor FC:</td>
						<td><input type="text" name="dto.codDistribuidorFc" id="dto.codDistribuidorFc" style="width:220px;" value="${parametroSistemaGeralDTO.codDistribuidorFc}"/></td>
						<td>Interface GFS Importa&ccedil;&atilde;o: </td>
						<td>
							<input type="text" name="dto.pathGfsImportacao" id="dto.pathGfsImportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathGfsImportacao}" />
						</td>
					</tr>
					<tr>
						<td>Login:</td>
						<td><input type="text" name="dto.login" id="dto.login" style="width:220px;" value="${parametroSistemaGeralDTO.login}" /></td>
						<td>Interface GFS Exporta&ccedil;&atilde;o: </td>
						<td>
							<input type="text" name="dto.pathGfsExportacao" id="dto.pathGfsExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathGfsExportacao}" />
						</td>
					</tr>
					<tr>
						<td>Senha:</td>
						<td><input type="password" name="dto.senha" id="dto.senha" style="width:220px;" value="${parametroSistemaGeralDTO.senha}"/></td>
						<td>Interface NFe Importa&ccedil;&atilde;o: </td>
						<td>
							<input type="text" name="dto.pathNfeImportacao" id="dto.pathNfeImportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathNfeImportacao}" />
						</td>
					</tr>
					<tr>
						<td>Vers&atilde;o Sistema:</td>
						<td><input type="text" name="dto.versaoSistema" id="dto.versaoSistema" style="width:220px;" value="${parametroSistemaGeralDTO.versaoSistema}" /></td>
						<td>Interface NFe Exporta&ccedil;&atilde;o: </td>
						<td>
							<input type="text" id="dto.pathNfeExportacao" name="dto.pathNfeExportacao" style="width:220px;" value="${parametroSistemaGeralDTO.pathNfeExportacao}" />
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>NF-e em DPEC:</td>
						<td>
							<c:choose>
								<c:when test='${parametroSistemaGeralDTO.nfeDpec == "TRUE"}'>
									<input type="checkbox" id="dto.nfeDpec" name="dto.nfeDpec" checked="checked" value="TRUE" /> 
								</c:when>
								<c:otherwise>
									<input type="checkbox" id="dto.nfeDpec" name="dto.nfeDpec" value="TRUE" />
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>Imagem Capa com campo texto</td>
						<td><input type="text" name="dto.pathImageCapa" id="dto.pathImageCapa" style="width:220px;" value="${parametroSistemaGeralDTO.pathImageCapa}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>Imagem banca PDV com campo texto</td>
						<td><input type="text" name="dto.pathImageBancaPdv" id="dto.pathImageBancaPdv" style="width:220px;" value="${parametroSistemaGeralDTO.pathImageBancaPdv}" /></td>
					</tr>
					<tr>
						<td>
							<span class="bt_novos" title="Novo">
								<a href="javascript:;" onclick="javascript:document.forms[0].submit();">
									<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Salvar
								</a>
							</span>
						</td>
						<td>&nbsp;</td>
						<td>Data Opera&ccedil;&atilde;o Corrente:</td>
						<td><input type="text" name="dto.dtOperacaoCorrente" id="dto.dtOperacaoCorrente" style="width:100px; text-align:center;" disabled="disabled" /></td>
					</tr>
				</tbody>
			</table>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>
	</div>
</div>

</form>
</body>
