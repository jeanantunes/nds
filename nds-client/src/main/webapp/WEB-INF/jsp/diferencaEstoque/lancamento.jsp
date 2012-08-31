<head>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lancamento.js"></script>

	<script language="javascript" type="text/javascript">

		var pesquisaProdutoLancamentoFaltasSobras = new PesquisaProduto();
		
		var pesquisaCotaLancamentoFaltasSobras = new PesquisaCota();
	
		lancamentoController.inicializar();
		
	</script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<style type="text/css">
		fieldset label {
			width: auto;
			margin-bottom: 0px !important;
		}
		
		#dialogConfirmacaoPerdaDados {
			display:none;
		}
	</style>
</head>

<body>
	<div class="corpo">
		<form id="form-excluir">
		<div id="dialog-excluir" title="Lançamento Faltas e Sobras">
			<p>Confirma esta Exclus&atilde;o?</p>
		</div>
		</form>
		<form id="form-confirmar-lancamentos">
		<div id="dialog-confirmar-lancamentos" title="Lançamento Faltas e Sobras">
			<p>Confirma estes Lan&ccedil;amentos?</p>
		</div>
		</form>
		<form id="formConfirmacaoPerdaDados">
		<div id="dialogConfirmacaoPerdaDados" title="Lançamento Faltas e Sobras">
			<p>Ao prosseguir com essa a&ccedil;&atilde;o voc&ecirc; perder&aacute; seus dados n&atilde;o salvos. Deseja prosseguir?</p>
		</div>
		</form>
		<div class="container">

			<fieldset class="classFieldset">
			
				<legend>Lan&ccedil;amento Faltas e Sobras</legend>
					  
				<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
					<tr>
						<td width="111">Data Movimento:</td>
						<td width="124">
							<input type="text" 
								   name="dataMovimentoFormatada" 
								   id="datePickerDataMovimento" 
								   style="width: 70px; float: left; margin-right: 5px;"
								   maxlength="10"
								   value="${dataAtual}" />
						</td>
						<td width="115">Tipo de Diferen&ccedil;a:</td>
						<td width="294">
							<select id="selectTiposDiferenca" 
									name="tipoDiferenca"
									 style="width: 220px;"
									 onchange="lancamentoController.exibirBotaoNovo(this.value);">
									 
								<option selected="selected"></option>
								<c:forEach var="tipoDiferenca" items="${listaTiposDiferenca}">
									<option value="${tipoDiferenca.key}">${tipoDiferenca.value}</option>
								</c:forEach>
							</select>
						</td>
						<td width="280">
							<span class="bt_pesquisar">
								<a href="javascript:;" onclick="lancamentoController.pesquisar(false);">Pesquisar</a>
							</span>
						</td>
					</tr>
				</table>
			</fieldset>
			
			<div class="linha_separa_fields">&nbsp;</div>

			<fieldset id="fieldsetPesquisa" class="classFieldset">
			
				<legend>Lançamento Faltas e Sobras</legend>
				
				<div class="grids" style="display: none;">
					<table id="gridLancamentos" class="gridLancamentos"></table>
				</div>
				
				<table width="931" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="459">
							<span id="btnNovo" class="bt_novo">
								<a href="javascript:;" onclick="lancamentoController.popupNovasDiferencas();">Novo</a>
							</span>
							<span id="btnConfirmar" class="total bt_confirmar" style="display: none;">
								<a href="javascript:;" onclick="lancamentoController.popupConfirmar();">Confirmar</a>
							</span>
						</td>
						<td id="labelTotalGeral" width="99" class="total" style="display: none">
							<strong>Total Geral:</strong>
						</td>
					    <td id="qtdeTotalDiferencas" width="108" class="total"></td>
					    <td width="104" align="center" class="total">&nbsp;</td>
					    <td id="valorTotalDiferencas" width="145" class="total"></td>
					</tr>
				</table>
			</fieldset>
			
			<div class="linha_separa_fields">&nbsp;</div>
		</div>
	</div>
	
	<form id="formNovoDialog">
		<jsp:include page="novoDialog.jsp" />
	</form>
	
	<form id="formRateioDialog">
		<jsp:include page="rateioDialog.jsp" />
	</form>
</body>