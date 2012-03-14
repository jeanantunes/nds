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
					
					popupRateioDiferenca(idDiferenca);
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
				$("#btnCancelar").hide();
				$("#labelTotalGeral").hide();
				$("#qtdeTotalDiferencas").hide();
				$("#valorTotalDiferencas").hide();

				return;
			}

			if (!data.result) {

				return;
			}
			
			var resultado = data.result;
			
			$("#qtdeTotalDiferencas").html(resultado.qtdeTotalDiferencas);
			
			$("#valorTotalDiferencas").html(resultado.valorTotalDiferencas);

			$.each(resultado.tableModel.rows, function(index, row) {

				var linkRateioDiferenca = '<a href="javascript:;" onclick="verificarExistenciaEstudo(' + row.cell.id + ');" style="cursor:pointer">' +
										     '<img src="${pageContext.request.contextPath}/images/bt_cadastros.png" hspace="5" border="0px" />' +
										  '</a>';

				var linkExclusaoDiferenca = '<a href="javascript:;" onclick="popupExclusaoDiferenca(' + row.cell.id + ');" style="cursor:pointer">' +
												'<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
											'</a>';
								
				row.cell.acao = linkRateioDiferenca + linkExclusaoDiferenca;
			});

			if ($(".grids").css('display') == 'none') {	

				$(".grids").show();
				$("#btnConfirmar").show();
				$("#btnCancelar").show();
				$("#labelTotalGeral").show();
				$("#qtdeTotalDiferencas").show();
				$("#valorTotalDiferencas").show();
			}

			return resultado.tableModel;
		} 

		function pesquisar() { 

			$.postJSON(
				"<c:url value='/estoque/diferenca/lancamento/limparSessao' />", 
				null,
				function() {
					var formData = $('#pesquisaLancamentoDiferencaForm').serializeArray();

					$("#gridLancamentos").flexOptions({
						url : '<c:url value="/estoque/diferenca/lancamento/pesquisa" />', 
						params: formData
					});
					
					$("#gridLancamentos").flexReload();
				}
			);
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
					
					var data = "?idDiferenca=" + idDiferenca;
					
					$("#gridLancamentos").flexOptions({url : '<c:url value="/estoque/diferenca/excluirFaltaSobra" />'+data});
					$("#gridLancamentos").flexReload();
					
					//var data = "idDiferenca=" + idMovimentoEstoque;
					//$.postJSON("<c:url value='/estoque/diferenca/excluirFaltaSobra'/>", data);
				},
				"Cancelar": function() {
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
				buttons: {
					"Confirmar": function() {
					$(this).dialog("close");
					
					$.postJSON("<c:url value='/estoque/diferenca/confirmarLancamentos'/>");
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
				}
			});
			
			$("#dialog-confirmar-lancamentos").show();
		}
		
		function cancelarModificacoes(){
			$("#gridLancamentos").flexOptions({url : '<c:url value="/estoque/diferenca/cancelar" />'});
			$("#gridLancamentos").flexReload();
		}
		
		$(function() {

			$("#gridLancamentos").flexigrid({
				preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [{
					display : 'Código',
					name : 'codigoProduto',
					width : 60,
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
					width : 90,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço Venda R$',
					name : 'precoVenda',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Pacote Padrão',
					name : 'pacotePadrao',
					width : 110,
					sortable : true,
					align : 'center'
				}, {
					display : 'Exemplares',
					name : 'quantidade',
					width : 110,
					sortable : true,
					align : 'center'
				}, {
					display : 'Tipo de Diferença',
					name : 'tipoDiferenca',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Total R$',
					name : 'valorTotalDiferenca',
					width : 90,
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
			
			$("#btnNovo").hide();
				
			$("#datePickerDataMovimento").datepicker({
				showOn : "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
			});

			$("#datePickerDataMovimento").mask("99/99/9999");
		});
	</script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produto.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
	
	<style type="text/css">
		fieldset label 
		{
			width: auto;
			margin-bottom: 0px !important;
		}
	</style>
</head>

<body>
	<div class="corpo">
		<div id="dialog-excluir" title="Lançamento Faltas e Sobras">
			<p>Confirma esta Exclusão?</p>
		</div>
		<div id="dialog-confirmar-lancamentos" title="Lançamento Faltas e Sobras">
			<p>Confirma estes Lançamentos?</p>
		</div>
		<div class="container">

			<fieldset class="classFieldset">
			
				<legend>Lançamento Faltas e Sobras</legend>
				
				<form id="pesquisaLancamentoDiferencaForm"
					  name="pesquisaLancamentoDiferencaForm" 
					  action="estoque/diferenca/lancamento/pesquisa" 
					  method="post">
					  
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
							<td width="115">Tipo de Diferença:</td>
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
									<a href="javascript:;" onclick="pesquisar();">Pesquisar</a>
								</span>
							</td>
						</tr>
					</table>
				</form>
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
							<span id="btnCancelar" class="total bt_cancelar" style="display: none;">
								<a href="javascript:;" onclick="cancelarModificacoes();">Cancelar</a>
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