<div id="tabFiscal">
	<br />
	
	<fieldset style="width: 98% !important; margin-bottom: 5px; float: left;">
		<legend>Fiscal</legend>
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="2" cellpadding="0">
						<tr>
							<td width="35%">Cnae:</td>
							<td><input type="text" name="fiscal-cnae" id="fiscal-cnae" value="${parametrosDistribuidor.cnae}" /></td>
							<td width="35%"></td>
							<td></td>
						</tr>
						
						<tr>
							<td width="35%">Tipo Prestador:</td>
							<td><select name="tipoAtividade" id="tipoAtividade" style="width: 150px;">
									<c:forEach var="tipoPrestador" items="${listaTipoPrestador}">
										<option value="${tipoPrestador.key}" <c:if test="${parametrosDistribuidor.tipoAtividade.descricao == tipoPrestador.value}">selected="selected"</c:if>>${tipoPrestador.value}</option>
									</c:forEach>
							</select></td>
							<td width="35%"></td>
							<td></td>
						</tr>
						<tr>
							<td width="35%">Regime Tributário:</td>
							<td><select name="regimeTributario" id="regimeTributario" onchange="parametrosDistribuidorController.obterTributosPeloRegimeTributario()" style="width: 150px;">
									<c:forEach var="regimeTributario" items="${listaRegimeTributario}">
										<option value="${regimeTributario.key}" <c:if test="${parametrosDistribuidor.regimeTributario.id == regimeTributario.key}">selected="selected"</c:if>>${regimeTributario.value}</option>
									</c:forEach>
							</select></td>
							<td width="35%"><table id="regimeTributarioTributos"></table></td>
							<td></td>
						</tr>
						<tr>
							<td width="35%">Informações Adicionais:</td>
							<td colspan="3"><textarea rows="2" cols="100" name="informacoesAdicionais" id="informacoesAdicionais" style="width: 90%;" >${parametrosDistribuidor.nfInformacoesAdicionais}</textarea></td>
						</tr>
						<tr>
							<td>Obrigação Fiscal possui Regime Especial ou Dispensa Interna?</td>
							<td><input type="checkbox" name="possuiRegimeEspecialDispensaInterna" id="possuiRegimeEspecialDispensaInterna" 
										onchange="parametrosDistribuidorController.changeFlagRegimeEspecial()"
										<c:if test="${parametrosDistribuidor.possuiRegimeEspecialDispensaInterna}">checked="checked"</c:if> />
							</td>
							<td><c:if test="${parametrosDistribuidor.possuiRegimeEspecialDispensaInterna && possuiDistribuicaoOutrosEstados}">
								Possui anuência em outros estados?
								</c:if>
							</td>
							<td>
							<c:if test="${parametrosDistribuidor.possuiRegimeEspecialDispensaInterna && possuiDistribuicaoOutrosEstados}">
							<c:forEach var="estadoAnuencia" varStatus="status" items="${listaEstadosAnuencia}">
								<c:if test="${estadoAnuencia != parametrosDistribuidor.endereco.uf}">
								<input type="checkbox" name="estadoAnuencia" id="estadoAnuencia_${status.index}" /> ${estadoAnuencia} </br>
								</c:if>
							</c:forEach>
							</c:if>
							</td>
						</tr>
						<tr class="camposEspecificosRegimeEspecial">
							<td>Número do dispositivo legal</td>
							<td><input type="text" name="numeroDispositivoLegal" id="numeroDispositivoLegal" value="${parametrosDistribuidor.numeroDispositivoLegal}" /></td>
							<td></td>
							<td></td>
						</tr>
						<tr class="camposEspecificosRegimeEspecial">
							<td>Data de Término da Vigência</td>
							<td><input type="text" name="dataLimiteVigenciaRegimeEspecial" id="dataLimiteVigenciaRegimeEspecial" value="<fmt:formatDate value="${parametrosDistribuidor.dataLimiteVigenciaRegimeEspecial}" pattern="dd/MM/yyyy"/>" /></td>
							<td></td>
							<td></td>
						</tr>
						</div>
					</table>
				</td>
			</tr>
		</table>

	</fieldset>

	<br clear="all" />

	<fieldset class="camposEspecificosRegimeEspecial" style="width: 98% !important; margin-bottom: 5px; float: left;">
	
		<legend>Tipos de Nota Fiscal</legend>

		<table class="tiposNotasFiscaisDistribuidor" width="100%" border="0" cellspacing="2" cellpadding="0">
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="2" cellpadding="0">
						<tr>
							<td></td>
						<c:forEach var="tipoEmissaoNotaFiscal" items="${listaTiposEmissaoNotaFiscal}">
							<td align="center">${tipoEmissaoNotaFiscal.descricao}</td>
						</c:forEach>
						</tr>
						<c:forEach var="tiposNotaFiscal" items="${listaTiposNotaFiscal}">
						<tr>
							<td>${tiposNotaFiscal.descricao}
								<%-- <option value="${tipoEmissao.id}">${tipoEmissao.descricao}</option> --%>
							</td>
							<c:forEach var="tipoEmissaoNotaFiscalDistribuidor" items="${listaTiposEmissaoNotaFiscal}">
								<td align="center">
								<c:forEach var="tipoEmissaoNotaFiscal" items="${tiposNotaFiscal.tipoEmissaoDisponiveis}">
									<c:if test="${tipoEmissaoNotaFiscal.descricao == tipoEmissaoNotaFiscalDistribuidor.descricao}">
										<c:choose>
											<c:when test="${tiposNotaFiscal.tipoEmissao.descricao == tipoEmissaoNotaFiscal.descricao}">
												<input type="radio" name="${tiposNotaFiscal.nomeCampoTela}" id="tipoEmissao_${tiposNotaFiscal.id}_${tipoEmissaoNotaFiscal.id}" value="${tipoEmissaoNotaFiscal.id}" checked="checked" />
											</c:when>
											<c:otherwise>
												<input type="radio" name="${tiposNotaFiscal.nomeCampoTela}" id="tipoEmissao_${tiposNotaFiscal.id}_${tipoEmissaoNotaFiscal.id}" value="${tipoEmissaoNotaFiscal.id}" />
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
								</td>
							</c:forEach>
						</tr>
						</c:forEach>
					</table>
				</td>
			</tr>
		</table>

	</fieldset>
</div>