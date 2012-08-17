<head>
	<script language="javascript" type="text/javascript">

		function verificarExistenciaEstudo(idDiferenca) {

			var data = [
   				{
   					name: 'idDiferenca', value: idDiferenca
   				}
   			];
			
			$.postJSON(
				"<c:url value='/estoque/diferenca/lancamento/rateio/validarEstudo' />", 
				data,
				function(result) {
					
					//popupRateioDiferenca(idDiferenca);
					popupNovasDiferencas(idDiferenca);
				},
				null
			);
		}
	
		function executarPreProcessamento(data) {
			
			if (data.mensagens) {

				exibirMensagem(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);
				
				$(".grids").hide();
				$("#btnConfirmar").hide();
				$("#labelTotalGeral").hide();
				$("#qtdeTotalDiferencas").hide();
				$("#valorTotalDiferencas").hide();

				return;
			}
			
			var resultado = data.result;

			if (!resultado) {

				return;
			}
			
			$("#qtdeTotalDiferencas").html(resultado.qtdeTotalDiferencas);
			
			$("#valorTotalDiferencas").html(resultado.valorTotalDiferencas);

			if (resultado.qtdeTotalDiferencas == 0) {

				$("#labelTotalGeral").hide();
				$("#qtdeTotalDiferencas").hide();
				
			} else {

				$("#labelTotalGeral").show();
				$("#qtdeTotalDiferencas").show();
			}

			if (!resultado.tableModel) {

				return;
			}
			
			$.each(resultado.tableModel.rows, function(index, row) {

				var linkRateioDiferenca = '<a id="ratearDiferenca' + row.cell.id + '" href="javascript:;" onclick="verificarExistenciaEstudo(' + row.cell.id + ');" style="cursor:pointer">' +
										     '<img src="${pageContext.request.contextPath}/images/bt_cadastros.png" hspace="5" border="0px" />' +
										  '</a>';

				if (row.cell.automatica) {
					
					var linkExclusaoDiferenca = '<a id="excluirDiferenca' + row.cell.id + '" href="javascript:;" style="cursor:default; opacity:0.4; filter:alpha(opacity=40);">' +
													'<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
												'</a>';
					
				} else {

					var linkExclusaoDiferenca = '<a id="excluirDiferenca' + row.cell.id + '" href="javascript:;" onclick="popupExclusaoDiferenca(' + row.cell.id + ');" style="cursor:pointer">' +
													'<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
												'</a>';
				}
								
				row.cell.acao = linkRateioDiferenca + linkExclusaoDiferenca;
			});

			if ($(".grids").css('display') == 'none') {	

				$(".grids").show();
				$("#btnConfirmar").show();
				$("#labelTotalGeral").show();
				$("#qtdeTotalDiferencas").show();
				$("#valorTotalDiferencas").show();
			}

			return resultado.tableModel;
		} 

		function pesquisar(confirmado) { 
			
			$.postJSON(
				"<c:url value='/estoque/diferenca/lancamento/limparSessao' />", 
				'confirmado=' + confirmado,
				function(result) {

					if (!result.confirmado) {

						popupMensagemConfirmacao();
						
					} else {

						var formData = [
			   				{
			   					name: 'dataMovimentoFormatada', value: $("#datePickerDataMovimento").val()
			   				},
			   				{
			   					name: 'tipoDiferenca', value: $("#selectTiposDiferenca").val()
			   				}
			   			];
	
						$("#gridLancamentos").flexOptions({
							url : '<c:url value="/estoque/diferenca/lancamento/pesquisa" />', 
							params: formData,
							newp: 1
						});
						
						$("#gridLancamentos").flexReload();

						$("#dialogConfirmacaoPerdaDados").dialog("close");
					}
				}
			);
		}

		function popupMensagemConfirmacao() {

			$("#dialogConfirmacaoPerdaDados").dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: 
				{
					"Confirmar": function() {
						
						pesquisar(true);
						
					}, "Cancelar": function() {
						
						$(this).dialog("close");
					}
				}
			});
			
			$("#dialogConfirmacaoPerdaDados").show();
		}

		function popupExclusaoDiferenca(idDiferenca) {

			$("#dialog-excluir").dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						$(this).dialog("close");

						var data = [
		    				{
		    					name: 'idDiferenca', value: idDiferenca
		    				}
		    			];
						
						$("#gridLancamentos").flexOptions(
							{
								url : '<c:url value="/estoque/diferenca/lancamento/excluir" />',
								params: data
							}
						);
						
						$("#gridLancamentos").flexReload();
						
					}, "Cancelar": function() {
						
						$(this).dialog("close");
					}
				}
			});
			
			$("#dialog-excluir").show();
		}

		function exibirBotaoNovo(tipoDiferenca) {

			if (tipoDiferenca) {
			
				$("#btnNovo").show();
				
			} else {
				
				$("#btnNovo").hide();
			}
		}
		
		function popupConfirmar(){
			$("#dialog-confirmar-lancamentos").dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: 
				{
					"Confirmar": function() {
						
						$.postJSON(
							"<c:url value='/estoque/diferenca/confirmarLancamentos' />", 
							null,
							function(result) {
	
								inicializar();
							}
						);
	
						$(this).dialog("close");
					
					}, "Cancelar": function() {
						
						$(this).dialog("close");
					}
				}
			});
			
			$("#dialog-confirmar-lancamentos").show();
		}

		function configurarFlexiGrid() {

			$("#gridLancamentos").flexigrid({
				preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [{
					display : 'Código',
					name : 'codigoProduto',
					width : 50,
					sortable : true,
					align : 'left'
				},{
					display : 'Produto',
					name : 'descricaoProduto',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço Venda R$',
					name : 'precoVenda',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Preço Desconto R$',
					name : 'precoDesconto',
					width : 110,
					sortable : true,
					align : 'right'
				}, {
					display : 'Pacote Padrão',
					name : 'pacotePadrao',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Exemplares',
					name : 'quantidade',
					width : 90,
					sortable : true,
					align : 'center'
				}, {
					display : 'Tipo de Diferença',
					name : 'tipoDiferenca',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Total R$',
					name : 'valorTotalDiferenca',
					width : 50,
					sortable : true,
					align : 'right'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				}],
				sortname : "descricaoProduto",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180,
				singleSelect: true
			});
		}

		function inicializar() {
			
			configurarFlexiGrid();

			$(".grids").hide();
			$("#btnConfirmar").hide();
			$("#btnNovo").hide();
			$("#labelTotalGeral").hide();
			$("#qtdeTotalDiferencas").hide();
			$("#valorTotalDiferencas").hide();
				
			$("#datePickerDataMovimento").datepicker({
				showOn : "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
			});

			$("#datePickerDataMovimento").mask("99/99/9999");

			$("#selectTiposDiferenca").val(null);
		}
		
		$(function() {

			inicializar();
		});
	</script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produto.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	
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
		<div id="dialog-excluir" title="Lançamento Faltas e Sobras">
			<p>Confirma esta Exclus&atilde;o?</p>
		</div>
		<div id="dialog-confirmar-lancamentos" title="Lançamento Faltas e Sobras">
			<p>Confirma estes Lan&ccedil;amentos?</p>
		</div>
		<div id="dialogConfirmacaoPerdaDados" title="Lançamento Faltas e Sobras">
			<p>Ao prosseguir com essa a&ccedil;&atilde;o voc&ecirc; perder&aacute; seus dados n&atilde;o salvos. Deseja prosseguir?</p>
		</div>
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
									 onchange="exibirBotaoNovo(this.value);">
									 
								<option selected="selected"></option>
								<c:forEach var="tipoDiferenca" items="${listaTiposDiferenca}">
									<option value="${tipoDiferenca.key}">${tipoDiferenca.value}</option>
								</c:forEach>
							</select>
						</td>
						<td width="280">
							<span class="bt_pesquisar">
								<a href="javascript:;" onclick="pesquisar(false);">Pesquisar</a>
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
								<a href="javascript:;" onclick="popupNovasDiferencas();">Novo</a>
							</span>
							<span id="btnConfirmar" class="total bt_confirmar" style="display: none;">
								<a href="javascript:;" onclick="popupConfirmar();">Confirmar</a>
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
	
	<jsp:include page="novoDialog.jsp" />
	
	<jsp:include page="rateioDialog.jsp" />
</body>