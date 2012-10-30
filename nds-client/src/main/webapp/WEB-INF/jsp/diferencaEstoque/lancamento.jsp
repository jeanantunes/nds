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
	
		#dialogConfirmacaoPerdaDados {
			display:none;
		}
	</style>
</head>

<body>
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
		
		<form id="form-salvar-lancamentos">
			<div id="dialog-salvar-lancamentos" title="Lançamento Faltas e Sobras" style="display: none">
				<p>Salvar estes Lan&ccedil;amentos?</p>
			</div>
		</form>
		
		<form id="form-cancelar-lancamentos">
			<div id="dialog-cancelar-lancamentos" title="Lançamento Faltas e Sobras" style="display: none">
				<p>Cancelar estes Lan&ccedil;amentos?</p>
			</div>
		</form>
		
		<form id="lancamento-consulta">
			
			<div class="areaBts">
					<div class="area">
						<span class="bt_novos">
							<a href="javascript:;" onclick="lancamentoNovoController.popupNovasDiferencas();" rel="tipsy"
							   title="Incluir Novo">
								<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" border="0" />
							</a>
						</span>
						
							
						<span class="bt_novos">
							<a href="javascript:"  onclick="lancamentoController.imprimirRelatorioFaltasSobras();" rel="tipsy" title="Imprimir confer&ecirc;cia manual">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif"  border="0" />
							</a>
						</span>
						
						
						<div id="btnsControleDiferenca">
							<span class="bt_novos">
								<a href="javascript:;" onclick="lancamentoController.popupConfirmar();"
								   title="Confirmar Lançamento de Faltas e Sobras">
									<img src="${pageContext.request.contextPath}/images/ico_check.gif" border="0" />
								</a>
							</span>
							
							<span class="bt_novos">
								<a href="javascript:;" onclick="lancamentoController.popupSalvarLancamentos();"
								   title="Salvar">
									<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" border="0" />
								</a>
							</span>
							
							<span class="bt_novos">
								<a href="javascript:;" onclick="lancamentoController.popupCancelarLancamentos();"
								   title="Cancelar Lançamento de Faltas e Sobras">
							 		<img src="${pageContext.request.contextPath}/images/ico_bloquear.gif" border="0" />
							 	</a>
							</span>
						</div>
					</div>
			</div>
			
			<div class="linha_separa_fields">&nbsp;</div>	
				
			<fieldset class="fieldFiltro">
			
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
									 style="width: 220px;">
									 
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

			<fieldset class="fieldGrid">
			
				<legend>Lançamento Faltas e Sobras</legend>
				
				<div class="grids" style="display: none;">
					<table id="gridLancamentos" class="gridLancamentos"></table>
				</div>
				
				<table width="931" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="459">&nbsp;</td>
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
	</form>
	
	<form id="form-dialogConfirmacaoDirecionamentoDiferencaProdutoCota">
		<div id="dialogConfirmacaoDirecionamentoDiferencaProdutoCota" title="Lançamento Faltas e Sobras" style="display: none">
			<p> O valor total da diferença não foi direcionado para a(s) cota(s)! </p>
			<br></br>
			<p> Deseja direcionar o restante para o estoque ?</p>
		</div>
	</form>
	
	<form id="formNovoDialog">
		<jsp:include page="novoDialog.jsp" />
	</form>

</body>