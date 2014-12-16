<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/manutencaoStatusCota.js"></script>
	
	<script type="text/javascript">

		var pesquisaCotaManutencaoStatusCota = new PesquisaCota(manutencaoStatusCotaController.workspace);
		
		$(function(){
			manutencaoStatusCotaController.init(pesquisaCotaManutencaoStatusCota);
			
			bloquearItensEdicao(manutencaoStatusCotaController.workspace);
		});
		
	</script>
</head>
<body>

<form id="frm-mantencao-status-cota-corpo-tela-consulta">

	<input type="hidden" value="${numeroCotaFollowUp}" id="manutencao-status-numeroCotaFollowUp" name="manutencao-status-numeroCotaFollowUp">
	
	<div class="areaBts">
		<div class="area">

			<span class="bt_novos" id="botao-novo-status-cota">
				<a isEdicao="true" href="javascript:;" onclick="manutencaoStatusCotaController.novo();" rel="tipsy" title="Incluir Novo Status"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" border="0" /></a>
			</span>
		</div>
	</div>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<!-- Filtro da Pesquisa -->
	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
	
		<legend>Pesquisar Manutenção de Status</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="47">Cota:</td>
				<td colspan="3">
					<input name="numeroCotaManutencaoStatusCota"  class="campoDePesquisa"
						   type="text"
						   id="numeroCotaManutencaoStatusCota"
						   maxlength="255"
						   style="width: 80px; margin-right: 5px; float: left;"
						   onchange="pesquisaCotaManutencaoStatusCota.pesquisarPorNumeroCota('#numeroCotaManutencaoStatusCota', '#nomeCota', false, manutencaoStatusCotaController.callBackSuccess,manutencaoStatusCotaController.callBackErro);" />

				</td>
				<td width="42">Nome:</td>
				<td width="240">
					<input name="nomeCota" class="campoDePesquisa"
						   type="text"
						   id="nomeCota" 
						   maxlength="255" 
						   style="width: 200px;"
						   onkeyup="pesquisaCotaManutencaoStatusCota.autoCompletarPorNome('#nomeCota');" 
		      		 	   onblur="pesquisaCotaManutencaoStatusCota.pesquisarPorNomeCota('#numeroCotaManutencaoStatusCota', '#nomeCota', false, manutencaoStatusCotaController.callBackSuccess,manutencaoStatusCotaController.callBackErro);" />
				</td>
				<td width="55">Box:</td>
				<td width="149">
					<input type="text" class="campoDePesquisa" name="box" id="box" style="width: 100px;" disabled="disabled" />
				</td>
				<td width="49">Status:</td>
				<td width="154">
					<select name="statusCota" id="statusCota" style="width: 100px;" class="campoDePesquisa">
						<option value="Todos" selected="selected">Todos</option>
						<c:forEach var="statusCota" items="${listaSituacoesStatusCota}">
							<option value="${statusCota.key}">${statusCota.value}</option>
						</c:forEach>
					</select>
				</td>
				<td width="49">&nbsp;</td>
			</tr>
			<tr>
				<td>Período:</td>
				<td colspan="3">
					<input name="dataInicialStatusCota" 
						   type="text" id="dataInicialStatusCota" 
						   style="width: 80px; float: left; margin-right: 5px;" class="campoDePesquisa" />
				</td>
				<td>Até:</td>
				<td>
					<input name="dataFinalStatusCota" 
						   type="text" 
						   id="dataFinalStatusCota" 
						   style="width: 80px; float: left; margin-right: 5px;" class="campoDePesquisa" />
				</td>
				<td>Motivo:</td>
				<td>
					<select name="motivo" id="motivo" style="width: 150px;" class="campoDePesquisa">
						<option value="Todos" selected="selected">Todos</option>
						<c:forEach var="motivoStatusCota" items="${listaMotivosStatusCota}">
							<option value="${motivoStatusCota.key}">${motivoStatusCota.value}</option>
						</c:forEach>
					</select>
				</td>
				<td>&nbsp;</td>
				<td>
					<span class="bt_novos" title="Pesquisar">
						<a class="botaoPesquisar" href="javascript:;" onclick="manutencaoStatusCotaController.pesquisarHistoricoStatusCota();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
					</span>
				</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<!-- Grid de Resultados da Pesquisa -->
	<fieldset class="fieldGrid">
	
		<legend>Históricos de Status</legend>
		
		<div class="grids" style="display: none;">
			<table class="manutencaoStatusCotaGrid"></table>
			
		</div>
	</fieldset>
</form>
	
<jsp:include page="novo.jsp" />

</body>