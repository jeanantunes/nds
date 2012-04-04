<head>

<script>

	$(function() {
		
		inicializar();
	});
	
	function iniciarGrid() {
		
		$(".solicitacoesAprovacao").flexigrid({
			dataType : 'xml',
			colModel : [ {
				display : 'Movimento',
				name : 'tipoMovimento',
				width : 120,
				sortable : true,
				align : 'left',
			}, {
				display : 'Data',
				name : 'dataMovimento',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Cota',
				name : 'numeroCota',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Parcelas',
				name : 'parcelas',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Prazo',
				name : 'prazo',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Requerente',
				name : 'requerente',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			} ],
			sortname : "Nome",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	}
	
	function iniciarData() {
		
		$("#data").datepicker({
			showOn : "button",
			buttonImage : "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly : true
		});
	}
	
	function inicializar() {
		
		iniciarGrid();
		
		iniciarData();
	}
		
	function popup_confirm() {

		$("#dialog-confirm").dialog({
			resizable : false,
			height : 'auto',
			width : 280,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").hide("highlight", {}, 1000, callback);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});
	}

	function popup_rejeitar() {
		
		$("#dialog-rejeitar").dialog({
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});
	}

	function pesquisar() {
		
		var tipoMovimento = $("#tipoMovimento").val();
		var data = $("#data").val();
		
		$(".solicitacoesAprovacao").flexOptions({
			url: "<c:url value='/administracao/controleAprovacao/pesquisarAprovacoes' />",
			onSuccess: executarAposProcessamento,
			params: [
		         {name:'tipoMovimento', value: tipoMovimento},
		         {name:'data', value: data}
		    ],
		});
		
		$(".solicitacoesAprovacao").flexReload();
		
		$(".grids").show();
	}
	
	function executarAposProcessamento() {
		
	}
		
</script>

</head>

<body>

	<div id="dialog-confirm" title="Aprovar Solicitação">
		<p>Confirmar Aprovação?</p>
	</div>

	<div id="dialog-rejeitar" title="Rejeitar Solicitação">
		<p>
			<strong>Confirmar Rejeição?</strong>
		</p>
		<p>
			<strong>Motivo:</strong>
		</p>
		<textarea rows="4" style="width: 420px;"></textarea>
	</div>

	<fieldset class="classFieldset">
		
		<legend>Pesquisar Aprovações</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="72">Movimento:</td>
				<td width="271">
					<select name="tipoMovimento" id="tipoMovimento" style="width: 250px;">
						<option>Todos</option>
					</select>
				</td>
				
				<td colspan="3">Data:</td>
				<td width="118">
					<input name="data" type="text" id="data"
						   style="width: 80px; float: left; margin-right: 5px;" />
				</td>
				
				<td width="422">
					<span class="bt_pesquisar" title="Pesquisar Recebimento">
						<a href="javascript:;" onclick="pesquisar();">Pesquisar</a>
					</span>
				</td>
			</tr>
		</table>
		
	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="classFieldset">
		
		<legend>Solicitações de Aprovação</legend>
		
		<div class="grids" style="display: none;">
			<table class="solicitacoesAprovacao"></table>
		</div>
		
	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>

</body>