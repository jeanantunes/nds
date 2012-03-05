<head>
	<script language="javascript" type="text/javascript">

		function executarPreProcessamento(resultado) {

			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$(".grids").hide();
				$("#btnConfirmar").hide();
				$("#labelTotalGeral").hide();
				$("#qtdeTotalDiferencas").hide();
				$("#valorTotalDiferencas").hide();

				return resultado.tableModel;
			}

			$("#qtdeTotalDiferencas").html(resultado.qtdeTotalDiferencas);
			
			$("#valorTotalDiferencas").html(resultado.valorTotalDiferencas);

			$.each(resultado.tableModel.rows, function(index, row) {

				var linkRateioDiferenca = '<a href="javascript:;" onclick="popupRateioDiferenca(' + row.cell.id + ');" style="cursor:pointer">' +
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
				$("#labelTotalGeral").show();
				$("#qtdeTotalDiferencas").show();
				$("#valorTotalDiferencas").show();
			}

			return resultado.tableModel;
		} 

		function pesquisar() { 

			var formData = $('#pesquisaLancamentoDiferencaForm').serializeArray();

			$("#gridLancamentos").flexOptions({url : '<c:url value="/estoque/diferenca/lancamento/pesquisa" />', params: formData});
			
			$("#gridLancamentos").flexReload();
		}

		function popupExclusaoDiferenca(idMovimentoEstoque) {

			$("#dialog-excluir" ).dialog({
				
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").hide("highlight", {}, 1000, callback);
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});	     
		}

		function exibirBotaoNovo(tipoDiferenca) {

			if (tipoDiferenca) {
			
				$("#btnNovo").show();
				
			} else {
				
				$("#btnNovo").hide();
			}
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
	
	<style type="text/css">
		fieldset label 
		{
			width: auto;
			margin-bottom: 0px !important;
		}
		
		.ui-datepicker 
		{
			z-index: 1000 !important;		
		}
		
		.ui-datepicker-today a
		{
			display:block !important;
		}
	</style>
</head>

<body>
	<div class="corpo">
	
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
								<a href="javascript:;" onclick="popup();">Confirmar</a>
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
	
	<jsp:include page="novo.jsp" />
	
	<jsp:include page="rateio.jsp" />
</body>