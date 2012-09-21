<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/controleAprovacao.js"></script>
<script>
$(function(){
	controleAprovacaoController.inicializar();
});
</script>

</head>

<body>

	<form id="form-confirm">
	<div id="dialog-confirm" title="Aprovar Solicitação">
		
		<jsp:include page="../messagesDialog.jsp" />
		
		<p>Confirmar Aprovação?</p>
	</div>
	</form>

	<form id="form-rejeitar">
	<div id="dialog-rejeitar" title="Rejeitar Solicitação">
		
		<jsp:include page="../messagesDialog.jsp" />
		
		<p>
			<strong>Confirmar Rejeição?</strong>
		</p>
		<p>
			<strong>Motivo:</strong>
		</p>
		<textarea id=motivoRejeicao rows="4" style="width: 420px;"></textarea>
	</div>
	</form>
	<div class="areaBts">
		<div class="area">
			&nbsp;
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="fieldFiltro">
		
		<legend>Pesquisar Aprovações</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="72">Movimento:</td>
				<td width="271">
					<select name="tipoMovimento" id="tipoMovimento" style="width: 250px;">
						<option value="" selected="selected">Todos</option>
						<c:forEach var="tipoMovimento" items="${listaTipoMovimentoCombo}">
							<option value="${tipoMovimento.key}">${tipoMovimento.value}</option>
						</c:forEach>
					</select>
				</td>
				
				<td colspan="3">Data:</td>
				<td width="160">
					<input name="dataMovimento" type="text" id="dataMovimento"
						   style="width: 80px; float: left; margin-right: 5px;" />
				</td>
				
				<td width="72">Status:</td>
				<td width="271">
					<select name="statusAprovacao" id="statusAprovacao" style="width: 100px;">
						<option value="" selected="selected">Todos</option>
						<c:forEach var="statusAprovacao" items="${listaStatusAprovacao}">
							<option value="${statusAprovacao.key}">${statusAprovacao.value}</option>
						</c:forEach>
					</select>
				</td>
				
				<td width="400">
					<span class="bt_novos">
						<a href="javascript:;" onclick="controleAprovacaoController.pesquisar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
					</span>
				</td>
			</tr>
		</table>
		
	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="fieldGrid">
		
		<legend>Solicitações de Aprovação</legend>
		
		<div class="grids" style="display: none;">
			<table class="solicitacoesAprovacao"></table>
		</div>
		
	</fieldset>

</body>