var controleAprovacaoController = $.extend(true, {

	inicializar : function() {
		
		controleAprovacaoController.iniciarGrid();
		
		controleAprovacaoController.iniciarData();
		
		$("#tipoMovimento", controleAprovacaoController.workspace).focus();
	},
	
	iniciarGrid : function() {
		
		$(".solicitacoesAprovacao", controleAprovacaoController.workspace).flexigrid({
			preProcess: controleAprovacaoController.executarPreProcessamento,
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
				width : 60,
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
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 45,
				sortable : true,
				align : 'center'
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
	},
	
	iniciarData : function() {
		
		$("#dataMovimento",controleAprovacaoController.workspace).datepicker({
			showOn : "button",
			buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly : true
		});
		
		$("#dataMovimento", controleAprovacaoController.workspace).mask("99/99/9999");
	},
	
	aprovarMovimento : function(idMovimento) {

		$("#dialog-confirm", controleAprovacaoController.workspace).dialog({
			resizable : false,
			height : 'auto',
			width : 280,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.postJSON(contextPath + "/administracao/controleAprovacao/aprovarMovimento", 
							   {idMovimento:idMovimento},
							   function(result) {
							   		
									$("#dialog-confirm", controleAprovacaoController.workspace).dialog("close");
									
									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}
									
									$(".solicitacoesAprovacao", controleAprovacaoController.workspace).flexReload();
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
			},
			form: $("#dialog-confirm", this.workspace).parents("form")
		});
	},

	rejeitarMovimento : function(idMovimento) {
		
		$("#dialog-rejeitar", controleAprovacaoController.workspace).dialog({
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					var motivoRejeicao = $("#motivoRejeicao", controleAprovacaoController.workspace).val();
					
					$("#motivoRejeicao", controleAprovacaoController.workspace).val(motivoRejeicao.trim());
					
					$.postJSON(contextPath + "/administracao/controleAprovacao/rejeitarMovimento", 
							   {idMovimento:idMovimento,motivo:motivoRejeicao},
							   function(result) {
							   		
									$("#dialog-rejeitar", controleAprovacaoController.workspace).dialog("close");
									
									$("#motivoRejeicao", controleAprovacaoController.workspace).val("");
									
									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}
									
									$(".solicitacoesAprovacao", controleAprovacaoController.workspace).flexReload();
							   },
							   null,
							   true
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					
					$("#motivoRejeicao", controleAprovacaoController.workspace).val("");
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout();
			},
			form: $("#dialog-rejeitar", this.workspace).parents("form")
		});
	},

	pesquisar : function() {
		
		var idTipoMovimento = $("#tipoMovimento", controleAprovacaoController.workspace).val();
		var dataMovimento = $("#dataMovimento", controleAprovacaoController.workspace).val();
		var statusAprovacao = $("#statusAprovacao", controleAprovacaoController.workspace).val();
		
		$(".solicitacoesAprovacao", controleAprovacaoController.workspace).flexOptions({
			url: contextPath + "/administracao/controleAprovacao/pesquisarAprovacoes",
			onSuccess: controleAprovacaoController.executarAposProcessamento,
			params: [
		         {name:'idTipoMovimento', value: idTipoMovimento},
		         {name:'dataMovimentoFormatada', value: dataMovimento},
		         {name: 'statusAprovacao', value:statusAprovacao}
		    ],
		    newp: 1,
		});
		
		$(".solicitacoesAprovacao", controleAprovacaoController.workspace).flexReload();
	},

	executarAposProcessamento : function() {
		$("span[name='status']", controleAprovacaoController.workspace).tooltip();
	},
	
	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", controleAprovacaoController.workspace).hide();

			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {

			var cursor = "cursor:pointer";

			var classLink = "";

			var aprovarMovimento = 'controleAprovacaoController.aprovarMovimento(' + row.cell.id + ');';

			var rejeitarMovimento = 'controleAprovacaoController.rejeitarMovimento(' + row.cell.id + ');';
			
			if (row.cell.status != 'P') {

				cursor = "cursor:default";
				aprovarMovimento = "";
				rejeitarMovimento = "";
				classLink = "linkDisabled";
															
			}		
				
			linkAprovar = '<a href="javascript:;" class="'+classLink+'" onclick="'+aprovarMovimento+'" style="'+cursor+'">' +
				     	  	'<img title="Aprovar" src="' + contextPath + '/images/ico_check.gif" hspace="5" border="0px" />' +
				  		  '</a>';
			
			linkRejeitar = '<a href="javascript:;" class="'+classLink+'" onclick="'+rejeitarMovimento+'" style="'+cursor+'">' +
						   	 '<img title="Rejeitar" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
						   '</a>';
			
			row.cell.acao = linkAprovar + linkRejeitar;

			if (row.cell.motivo) {
							
				var spanAprovacao = "<span name='status' title='" + row.cell.motivo + "'>"
									+ row.cell.status + "</span>";
				
				row.cell.status = spanAprovacao;
			}
			
		});
			
		$(".grids", controleAprovacaoController.workspace).show();
		
		return resultado;
	}

}, BaseController);