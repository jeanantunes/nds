<div id="tabFiscal">
	<br/>
	
	<fieldset
		style="width: 440px !important; margin-bottom: 5px; float: left;">
		<legend>Fiscal</legend>
		<table width="307" border="0" cellspacing="2" cellpadding="0">
			<tr>
				<td>Regime Tributário:</td>
				<td>
					<select name="regimeTributario" id="regimeTributario"
							style="width: 150px;">
						<%-- <option selected="selected"></option> --%>
						<c:forEach var="regimeTributario" items="${listaRegimeTributario}">
							<option value="${regimeTributario.key}">${regimeTributario.value}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td width="102">Obrigação Fiscal</td>
				<td width="185">
					<select name="obrigacaoFiscal" id="obrigacaoFiscal"
							style="width: 150px;">
						<option selected="selected"></option>
						<c:forEach var="obrigacaoFiscal" items="${listaObrigacaoFiscal}">
							<option value="${obrigacaoFiscal.key}">${obrigacaoFiscal.value}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Regime Especial</td>
				<td>
					<c:if test="${parametrosDistribuidor.regimeEspecial}">
						<input type="checkbox" name="regimeEspecial"
						   	   id="regimeEspecial" checked="checked" />
					</c:if>
                    <c:if test="${empty parametrosDistribuidor.regimeEspecial or (not parametrosDistribuidor.regimeEspecial)}">
						<input type="checkbox" name="regimeEspecial"
						   	   id="regimeEspecial" />
			   	   	</c:if>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</fieldset>
	
	<br clear="all" />
</div>