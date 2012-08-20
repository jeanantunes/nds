<head>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	
	<script type="text/javascript">

		var pesquisaCotaManutencaoStatusCota = new PesquisaCota();
	
		function configurarFlexiGrid() {
			
			$(".manutencaoStatusCotaGrid").flexigrid({
				preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [{
					display : 'Data',
					name : 'data',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Status Anterior',
					name : 'statusAnterior',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Status Atualizado',
					name : 'statusAtualizado',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Usuário',
					name : 'usuario',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Motivo',
					name : 'motivo',
					width : 140,
					sortable : true,
					align : 'left'
				}, {
					display : 'Descrição',
					name : 'descricao',
					width : 345,
					sortable : true,
					align : 'left'
				}],
				sortname : "data",
				sortorder : "desc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255
			});
		}

		function executarPreProcessamento(data) {
			
			if (data.mensagens) {

				exibirMensagem(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);
				
				$(".grids").hide();

				return;
			}

			if ($(".grids").css('display') == 'none') {	

				$(".grids").show();
			}

			return data.result;
		}

		function configurarCamposData() {

			$("#dataInicialStatusCota").datepicker({
				showOn : "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
			});

			$("#dataFinalStatusCota").datepicker({
				showOn : "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
			});

			$("#novaDataInicialStatusCota").datepicker({
				showOn : "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
			});

			$("#novaDataFinalStatusCota").datepicker({
				showOn : "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
			});

			$("#dataInicialStatusCota").mask("99/99/9999");
			$("#dataFinalStatusCota").mask("99/99/9999");
			$("#novaDataInicialStatusCota").mask("99/99/9999");
			$("#novaDataFinalStatusCota").mask("99/99/9999");
		}

		function configurarCamposNumericos() {

			$("input[id='numeroCota']").numeric();
		}

		function novo() {

			var filtro = obterDadosFiltro();
			
			$.postJSON(
				"<c:url value='/financeiro/manutencaoStatusCota/novo' />", 
				filtro,
				function(result) {

					$("#numeroCotaNovo").html(result.numero);
					$("#boxNovo").html(result.codigoBox);
					$("#novoNomeCota").html(result.nome);

					$("#novoStatusCota").val("");
   					$("#novaDataInicialStatusCota").val("");
   					$("#novaDataFinalStatusCota").val("");
   					$("#novoMotivo").val("");
   					$("#novaDescricao").val("");
					
					popupDialogNovo();
				}
			);    
		}

		function popupDialogNovo() {

			$("#dialog-novo").dialog({
				resizable: false,
				height:300,
				width:590,
				modal: true,
				buttons: {
					"Confirmar": function() {
						confirmarNovo();
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				}
			});
		}

		function popupAviso(mensagens) {

			montarTextoMensagem($("#mensagemAviso"), mensagens);
			$("#mensagemConfirmacao").html("Deseja continuar mesmo assim ?");

			$("#dialog-aviso").dialog({
				resizable: false,
				height:200,
				width:590,
				modal: true,
				buttons: {
					"Sim": function() {
						$(this).dialog("close");
					},
					"Não": function() {
						$(this).dialog("close");
						$("#dialog-novo").dialog("close");
					}
				}
			});
		}
		
		function carregarCodigoBox() {
			
			cota.obterPorNumeroCota($("#numeroCota").val(), false, function(result) {

				if (!result) {

					return;
				}

				$("#box").val(result.codigoBox);
			});
		}

		function pesquisarHistoricoStatusCota() {

			var followUp = $('#numeroCotaFollowUp').val();
			var filtro;			
			if(followUp != ''){			
				filtro = [
							{name: 'filtro.numeroCota' , value: followUp },
							{name: 'filtro.periodo.dataInicial' , value: null },
							{name: 'filtro.periodo.dataFinal' , value: null }
							]
			}else{
				var filtro = obterDadosFiltro();
			}


			$(".manutencaoStatusCotaGrid").flexOptions({
				url : '<c:url value="/financeiro/manutencaoStatusCota/pesquisar" />', 
				params: filtro,
				newp: 1
			});
			
			$(".manutencaoStatusCotaGrid").flexReload();
		}

		function obterDadosFiltro() {

			var filtro = [
   				{
   					name: 'filtro.numeroCota', value: $("#numeroCota").val()
   				},
   				{
   					name: 'filtro.statusCota', value: $("#statusCota").val()
   				},
   				{
   					name: 'filtro.periodo.dataInicial', value: $("#dataInicialStatusCota").val()
   				},
   				{
   					name: 'filtro.periodo.dataFinal', value: $("#dataFinalStatusCota").val()
   				},
   				{
   					name: 'filtro.motivoStatusCota', value: $("#motivo").val()
   				}
   			];

   			return filtro;
		}

		function confirmarNovo() {

			var novoHistoricoSituacaoCota = [
   				{
   					name: 'novoHistoricoSituacaoCota.cota.numeroCota', value: $("#numeroCota").val()
   				},
   				{
   					name: 'novoHistoricoSituacaoCota.novaSituacao', value: $("#novoStatusCota").val()
   				},
   				{
   					name: 'novoHistoricoSituacaoCota.dataInicioValidade', value: $("#novaDataInicialStatusCota").val()
   				},
   				{
   					name: 'novoHistoricoSituacaoCota.dataFimValidade', value: $("#novaDataFinalStatusCota").val()
   				},
   				{
   					name: 'novoHistoricoSituacaoCota.motivo', value: $("#novoMotivo").val()
   				},
   				{
   					name: 'novoHistoricoSituacaoCota.descricao', value: $("#novaDescricao").val()
   				}
   			];

			$.postJSON(
				"<c:url value='/financeiro/manutencaoStatusCota/novo/confirmar' />", 
				novoHistoricoSituacaoCota,
				function(result) {

					exibirMensagem(
						result.tipoMensagem, 
						result.listaMensagens
					);
					
					$("#dialog-novo").dialog("close");
				},
				null,
				true
			); 	
		}
		
		function inicializar() {

			configurarFlexiGrid();

			configurarCamposData();

			configurarCamposNumericos();
		}

		function ifInativo(status){
			if (status=="INATIVO"){
				$("#novaDataFinalStatusCota").val('');
				$("#novaDataFinalStatusCota").attr('disabled','disabled');
				$("#novaDataFinalStatusCota").datepicker('disable');
				
			}
			else{
				$('#novaDataFinalStatusCota').removeAttr("disabled"); 
				$("#novaDataFinalStatusCota").datepicker('enable');
			}
		}
		
		function dividasAbertoCota(status) {
			
			if (status=="ATIVO"){
				
				var numeroCota = $("#numeroCotaNovo").html();
				var data = [{name:'numeroCota', value:numeroCota}];
				$.postJSON("<c:url value='/financeiro/manutencaoStatusCota/dividasAbertoCota' />",
						    data,
						    function(result){
							    if (result!=""){
								    var mensagens = result.listaMensagens;
								    if (mensagens) {
								        popupAviso(mensagens);
							        }
							    }    
				            },
							null,
				            false);
			}	
		}

		$(function() {

			var followUp = $('#numeroCotaFollowUp').val();
			
			inicializar();
			if(followUp != ''){			
				pesquisarHistoricoStatusCota();
			}

			
		});
		
		
	</script>
</head>
<body>
<input type="hidden" value="${numeroCotaFollowUp}" id="numeroCotaFollowUp" name="numeroCotaFollowUp">
	<!-- Filtro da Pesquisa -->
	<fieldset class="classFieldset">
	
		<legend>Pesquisar Manutenção de Status</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="47">Cota:</td>
				<td colspan="3">
					<input name="numeroCota" 
						   type="text"
						   id="numeroCota"
						   maxlength="255"
						   style="width: 80px; margin-right: 5px; float: left;"
						   onchange="pesquisaCotaManutencaoStatusCota.pesquisarPorNumeroCota('#numeroCota', '#nomeCota', false, carregarCodigoBox);" />
				</td>
				<td width="42">Nome:</td>
				<td width="240">
					<input name="nomeCota" 
						   type="text"
						   id="nomeCota" 
						   maxlength="255" 
						   style="width: 200px;"
						   onkeyup="pesquisaCotaManutencaoStatusCota.autoCompletarPorNome('#nomeCota');" 
		      		 	   onblur="pesquisaCotaManutencaoStatusCota.pesquisarPorNomeCota('#numeroCota', '#nomeCota', false, carregarCodigoBox);" />
				</td>
				<td width="55">Box:</td>
				<td width="149">
					<input type="text" name="box" id="box" style="width: 100px;" disabled="disabled" />
				</td>
				<td width="49">Status:</td>
				<td width="154">
					<select name="statusCota" id="statusCota" style="width: 100px;">
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
						   style="width: 80px; float: left; margin-right: 5px;" />
				</td>
				<td>Até:</td>
				<td>
					<input name="dataFinalStatusCota" 
						   type="text" 
						   id="dataFinalStatusCota" 
						   style="width: 80px; float: left; margin-right: 5px;" />
				</td>
				<td>Motivo:</td>
				<td>
					<select name="motivo" id="motivo" style="width: 150px;">
						<option value="Todos" selected="selected">Todos</option>
						<c:forEach var="motivoStatusCota" items="${listaMotivosStatusCota}">
							<option value="${motivoStatusCota.key}">${motivoStatusCota.value}</option>
						</c:forEach>
					</select>
				</td>
				<td>&nbsp;</td>
				<td>
					<span class="bt_pesquisar" title="Pesquisar">
						<a href="javascript:;" onclick="pesquisarHistoricoStatusCota();">Pesquisar</a>
					</span>
				</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<!-- Grid de Resultados da Pesquisa -->
	<fieldset class="classFieldset">
	
		<legend>Históricos de Status</legend>
		
		<div class="grids" style="display: none;">
			<table class="manutencaoStatusCotaGrid"></table>
		</div>
		
		<span class="bt_novo" title="Novo">
			<a href="javascript:;" onclick="novo();">Novo</a>
		</span>
	</fieldset>
	
	<jsp:include page="novo.jsp" />
</body>