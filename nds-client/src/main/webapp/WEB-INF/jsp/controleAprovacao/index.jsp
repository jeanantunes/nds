<head>

<script>

	$(function() {
		
		inicializar();
	});
	
	function iniciarGrid() {
		
		$(".solicitacoesAprovacao").flexigrid({
			preProcess: executarPreProcessamento,
			dataType : 'json',
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
				sortable : false,
				align : 'center'
			} ],
			sortname : "tipoMovimento",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180,
			singleSelect : true
		});
	}
	
	function iniciarData() {
		
		$("#dataMovimento").datepicker({
			showOn : "button",
			buttonImage : "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly : true
		});
	}
	
	function inicializar() {
		
		iniciarGrid();
		
		iniciarData();
		
		$("#tipoMovimento").focus();
	}
		
	function aprovarMovimento(idMovimento) {

		$("#dialog-confirm").dialog({
			resizable : false,
			height : 'auto',
			width : 280,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.postJSON("<c:url value='/administracao/controleAprovacao/aprovarMovimento' />", 
							   "idMovimento=" + idMovimento,
							   function(result) {
							   		
									$("#dialog-confirm").dialog("close");
									
									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}
									
									$(".solicitacoesAprovacao").flexReload();
							   },
							   null,
							   true
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout();
			}
		});
	}

	function rejeitarMovimento(idMovimento) {
		
		$("#dialog-rejeitar").dialog({
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					var motivoRejeicao = $("#motivoRejeicao").val();
					
					$("#motivoRejeicao").val(motivoRejeicao.trim());
					
					$.postJSON("<c:url value='/administracao/controleAprovacao/rejeitarMovimento' />", 
							   "idMovimento=" + idMovimento +
							   "&motivo=" + motivoRejeicao,
							   function(result) {
							   		
									$("#dialog-rejeitar").dialog("close");
									
									$("#motivoRejeicao").val("");
									
									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}
									
									$(".solicitacoesAprovacao").flexReload();
							   },
							   null,
							   true
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					
					$("#motivoRejeicao").val("");
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout();
			}
		});
	}

	function pesquisar() {
		
		var idTipoMovimento = $("#tipoMovimento").val();
		var dataMovimento = $("#dataMovimento").val();
		
		$(".solicitacoesAprovacao").flexOptions({
			url: "<c:url value='/administracao/controleAprovacao/pesquisarAprovacoes' />",
			onSuccess: executarAposProcessamento,
			params: [
		         {name:'idTipoMovimento', value: idTipoMovimento},
		         {name:'dataMovimentoFormatada', value: dataMovimento}
		    ],
		    newp: 1,
		});
		
		$(".solicitacoesAprovacao").flexReload();
	}
	
	function executarPreProcessamento(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids").hide();

			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var linkAprovar = '<a href="javascript:;" onclick="aprovarMovimento(' + row.cell.id + ');" style="cursor:pointer">' +
					     	  	'<img title="Aprovar" src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0px" />' +
					  		  '</a>';
			
			var linkRejeitar = '<a href="javascript:;" onclick="rejeitarMovimento(' + row.cell.id + ');" style="cursor:pointer">' +
							   	 '<img title="Rejeitar" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkAprovar + linkRejeitar;
		});
			
		$(".grids").show();
		
		return resultado;
	}
	
	function executarAposProcessamento() {
		
	}
		
</script>

</head>

<body>

	<div id="dialog-confirm" title="Aprovar Solicitação">
		
		<div class="effectDialog ui-state-highlight ui-corner-all" 
			 style="display: none; position: absolute; z-index: 2000; width: 250px;">
			 
			<p>
				<span style="float: left;" class="ui-icon ui-icon-info"></span>
				<b class="effectDialogText"></b>
			</p>
		</div>
		
		<p>Confirmar Aprovação?</p>
	</div>

	<div id="dialog-rejeitar" title="Rejeitar Solicitação">
		
		<div class="effectDialog ui-state-highlight ui-corner-all" 
			 style="display: none; position: absolute; z-index: 2000; width: 420px;">
			 
			<p>
				<span style="float: left;" class="ui-icon ui-icon-info"></span>
				<b class="effectDialogText"></b>
			</p>
		</div>
		
		<p>
			<strong>Confirmar Rejeição?</strong>
		</p>
		<p>
			<strong>Motivo:</strong>
		</p>
		<textarea id=motivoRejeicao rows="4" style="width: 420px;"></textarea>
	</div>

	<fieldset class="classFieldset">
		
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
				<td width="118">
					<input name="dataMovimento" type="text" id="dataMovimento"
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