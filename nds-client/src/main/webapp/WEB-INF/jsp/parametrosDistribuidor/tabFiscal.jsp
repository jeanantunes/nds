<div id="tabFiscal">
	<br />

	<fieldset
		style="width: 98% !important; margin-bottom: 5px; float: left;">
		<legend>Fiscal</legend>
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="2" cellpadding="0">
						<tr>
							<td width="35%">Regime Tributário:</td>
							<td><select name="regimeTributario" id="regimeTributario"
								style="width: 150px;">
									<%-- <option selected="selected"></option> --%>
									<c:forEach var="regimeTributario"
										items="${listaRegimeTributario}">
										<option value="${regimeTributario.key}">${regimeTributario.value}</option>
									</c:forEach>
							</select></td>
							<td width="35%"></td>
							<td></td>
						</tr>
						<tr>
							<td>Obrigação Fiscal possui Regime Especial ou Dispensa
								Interna?</td>
							<td><c:if test="${parametrosDistribuidor.possuiRegimeEspecialDispensaInterna}">
									<input type="checkbox" name="regimeEspecial"
										id="regimeEspecial" checked="checked" />
								</c:if> <c:if
									test="${empty parametrosDistribuidor.possuiRegimeEspecialDispensaInterna or (not parametrosDistribuidor.possuiRegimeEspecialDispensaInterna)}">
									<input type="checkbox" name="regimeEspecial"
										id="regimeEspecial" />
								</c:if></td>
							<td>Possui anuência em outros estados?</td>
							<td><input type="checkbox" name="estadoAnuencia"
								id="estadoAnuencia_1" checked="checked" /> MT </br> <input
								type="checkbox" name="estadoAnuencia" id="estadoAnuencia_2" />
								MG</td>
						</tr>
						<tr>
							<td>Número do dispositivo legal</td>
							<td><input type="text" name="numeroDispositivoLegal"
								id="numeroDispositivoLegal" /></td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td>Data de Vigência</td>
							<td><input type="text" name="dataVigencia" id="dataVigencia" />
							</td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td></td>
							<td></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

	</fieldset>

	<br clear="all" />

	<fieldset
		style="width: 98% !important; margin-bottom: 5px; float: left;">
		<legend>Tipos de Nota Fiscal</legend>

		<table class="tiposNotasFiscaisDistribuidor" width="100%" border="0" cellspacing="2" cellpadding="0">
			<tr>
				<td>
					<table width="100%" border="1" cellspacing="2" cellpadding="0">
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
											<c:when test="${tiposNotaFiscal.tipoEmissao.id == tipoEmissaoNotaFiscal.id}">
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
							
							<%--<c:forEach var="tipoEmissaoNotaFiscal" varStatus="status" items="${tiposNotaFiscal.tipoEmissaoDisponiveis[0]}">
								<td align="center">
									${tiposNotaFiscal.tipoEmissaoDisponiveis[0].descricao}
									<c:if test="tiposNotaFiscal.descricao == 'Desobriga Emissão'" >
									<input type="radio" name="notaFiscalEnvioCota" id="notaFiscalEnvioCota" 
									checked="checked" /> 
									</c:if>
								</td>
								<td align="center">
									<c:if test="tiposNotaFiscal.descricao == 'Consolida emissão a Jornaleiros Diversos'" >
									<input type="radio" name="notaFiscalEnvioCota" id="notaFiscalEnvioCota" 
									checked="checked" /> 
									</c:if>
								</td>
								<td align="center">
									<c:if test="tiposNotaFiscal.descricao == 'Consolida emissão por Destinatário'" >
									<input type="radio" name="notaFiscalEnvioCota" id="notaFiscalEnvioCota" 
									checked="checked" /> 
									</c:if>
								</td>
							</c:forEach>  --%>
						</tr>
						</c:forEach>
						<%-- <tr>
							<td>Nota Fiscal de Envio para a Cota</td>
							<td align="center"><input type="radio" name="notaFiscalEnvioCota" id="notaFiscalEnvioCota" /></td>
							<td align="center"><input type="radio" name="notaFiscalEnvioCota" id="notaFiscalEnvioCota" checked="checked"/></td>
							<td align="center"><input type="radio" name="notaFiscalEnvioCota" id="notaFiscalEnvioCota" /></td>
						</tr>
						<tr>
							<td>Nota Fiscal de Devolução pela Cota</td>
							<td align="center"><input type="radio" name="notaFiscalDevolucaoPelaCota" id="notaFiscalEnvioCota" /></td>
							<td align="center"><input type="radio" name="notaFiscalDevolucaoPelaCota" id="notaFiscalEnvioCota" checked="checked"/></td>
							<td align="center"><input type="radio" name="notaFiscalDevolucaoPelaCota" id="notaFiscalEnvioCota" /></td>
						</tr>
						<tr>
							<td>Nota Fiscal de Venda</td>
							<td align="center"><input type="radio" name="notaFiscalVenda" id="notaFiscalEnvioCota" /></td>
							<td align="center"><input type="radio" name="notaFiscalVenda" id="notaFiscalEnvioCota" checked="checked"/></td>
							<td align="center"><input type="radio" name="notaFiscalVenda" id="notaFiscalEnvioCota" /></td>
						</tr>
						<tr>
							<td>Nota Fiscal de Devolução ao Fornecedor</td>
							<td align="center"><input type="radio" name="notaFiscalDevolucaoFornecedor" id="notaFiscalEnvioCota" checked="checked"/></td>
							<td align="center"></td>
							<td align="center"><input type="radio" name="notaFiscalDevolucaoFornecedor" id="notaFiscalEnvioCota" /></td>
						</tr>
						<tr>
							<td>Nota Fiscal Simbólica de Venda ao Fornecedor</td>
							<td align="center"><input type="radio" name="notaFiscalVendaFornecedor" id="notaFiscalEnvioCota" /></td>
							<td align="center"></td>
							<td align="center"><input type="radio" name="notaFiscalVendaFornecedor" id="notaFiscalEnvioCota" checked="checked"/></td>
						</tr> --%>
					</table>
				</td>
			</tr>
		</table>

	</fieldset>
</div>