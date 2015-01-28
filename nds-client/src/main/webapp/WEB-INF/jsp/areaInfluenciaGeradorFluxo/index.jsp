<head>

<script type="text/javascript" src="scripts/pesquisaCota.js"/>
<script type="text/javascript" src="scripts/areaInfluenciaGeradorFluxo.js"/>
<script language="javascript" type="text/javascript">

	$(function() {
		areaInfluenciaGeradorFluxoController.init();
	});
</script>
</head>

<body>

<div class="areaBts">
	<div class="area">
		<div class="porArea porCota" style="display:none;">
			<span class="bt_arq" >
				<a href="${pageContext.request.contextPath}/distribuicao/areaInfluenciaGeradorFluxo/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
			<span class="bt_arq">
				<a href="${pageContext.request.contextPath}/distribuicao/areaInfluenciaGeradorFluxo/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>
		</div>
	</div>
</div>
<div class="corpo">
	<!-- Início da pesquisa -->
	<br clear="all" />
	<br />

	<div class="container">

		<fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
			<legend>Pesquisar Área de Influência</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1"
					class="filtro">
				<tr>
					<td width="20">
						<input type="radio" name="pesqPor" id="radio"
							value="radio" onclick="areaInfluenciaGeradorFluxoController.filtroPorArea();" />
					</td>
					<td width="208">Área de Influência / Gerador de Fluxo</td>
					<td width="20">
						<input type="radio" name="pesqPor" id="radio2"
							value="radio" onclick="areaInfluenciaGeradorFluxoController.filtroPorCota();" />
					</td>
					<td width="33">Cota</td>
					<td width="643">
						<table width="642" border="0" cellpadding="2"
								cellspacing="1" class="filtro filtroPorCota"
								style="display: none;">
							<tr>
								<td width="35">Cota:</td>
								<td width="93">
									<input type="text" name="area-influencia-numeroCota" id="area-influencia-numeroCota" style="width: 80px;" />
								</td>
								<td width="36">Nome:</td>
								<td width="348">
									<input type="text" name="area-influencia-nomeCota" id="area-influencia-nomeCota" style="width: 200px;" />
								</td>
								<td width="104">
									<span class="bt_pesquisar">
										<a
											href="javascript:;" onclick="areaInfluenciaGeradorFluxoController.pesquisarPorCota();">Pesquisar</a>
									</span>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<table width="950" border="0" cellpadding="2" cellspacing="1"
					class="filtro filtroPorArea" style="display: none;">
				<tr>
					<td width="104">Área de Influência:</td>
					<td width="189">
						<select name="areaInfluencia" id="areaInfluencia"	style="width: 180px;">
							<option selected="selected" value="0">Selecione...</option>
							<c:forEach items="${listaAreaInfluenciaPDV}" var="areaInfluenciaPDV">
								<option value="${areaInfluenciaPDV.key}">${areaInfluenciaPDV.value}</option>
							</c:forEach>
						</select>
					</td>
					<td width="162">Geradores de Fluxo Principal:</td>
					<td width="209">
						<select name="geradorFluxoPrincipal" id="geradorFluxoPrincipal" style="width: 180px;">
							<option selected="selected" value="0">Selecione...</option>
							<c:forEach items="${listaTipoGeradorFluxoPDV}" var="tipoGeradorFluxoPDV">
								<option value="${tipoGeradorFluxoPDV.key}">${tipoGeradorFluxoPDV.value}</option>
							</c:forEach>
						</select>
					</td>
					<td width="61">Secundário:</td>
					<td width="194">
						<select name="geradorFluxoSecundario" id="geradorFluxoSecundario"
							style="width: 180px;">
							<option selected="selected" value="0">Selecione...</option>
							<c:forEach items="${listaTipoGeradorFluxoPDV}" var="tipoGeradorFluxoPDV">
								<option value="${tipoGeradorFluxoPDV.key}">${tipoGeradorFluxoPDV.value}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<table width="426" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="23">
									<input type="radio" name="radio" id="todasCotas" value="radio" />
								</td>
								<td width="99">Todas as Cotas</td>
								<td width="20">
									<input type="radio" name="radio" id="area-influencia-cotasAtivas" value="radio" checked="checked" />
								</td>
								<td width="284">Cotas Ativas</td>
							</tr>
						</table>
					</td>
					<td>&nbsp;</td>
					<td>
						<span class="bt_pesquisar">
							<a href="javascript:;"
								onclick="areaInfluenciaGeradorFluxoController.pesquisarPorArea();">Pesquisar</a>
						</span>
					</td>
				</tr>
			</table>
		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>
		<div class="grids" style="display: none;">

			<div class="porArea" style="display: none;">
				<fieldset class="classFieldset">
					<legend>Área de Influência / Gerador de Fluxos</legend>
					<table class="areaInfluenciaGrid"></table>
				</fieldset>
			</div>

			<div class="porCota" style="display: none;">
				<fieldset class="classFieldset">
					<legend>Área de Influência / Gerador de Fluxos</legend>
					<table class="areaInfluenciaGrid"></table>
				</fieldset>
			</div>

		</div>
	</div>
</div>

</body>