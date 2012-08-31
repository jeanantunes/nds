<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lancamentoRateio.js"></script>

<div id="dialogRateioDiferencas" title="Incluir Novo Tipo de Movimento" style="display: none;">

	<jsp:include page="../messagesDialog.jsp" />

	<table id="gridRateioDiferencas" class="gridRateioDiferencas"></table>
	
	<br />
	
	<table width="420" border="0" cellspacing="2" cellpadding="2">
		<tr style="font-size:11px;">
			<td id="labelTotalRateio" width="100"><strong>Total:</strong></td>
			<td id="totalRateio" width="10" align="center"></td>
		</tr>
    </table>
	
	<script language="javascript" type="text/javascript">
		$(function(){
			lancamentoRateioController.init();
		});
	</script>
	
</div>