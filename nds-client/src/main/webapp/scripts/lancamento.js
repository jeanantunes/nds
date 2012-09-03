var lancamentoController = $.extend(true, {

	inicializar : function() {
		
		lancamentoController.configurarFlexiGrid();

		$(".grids", lancamentoController.workspace).hide();
		$("#btnConfirmar", lancamentoController.workspace).hide();
		$("#btnNovo", lancamentoController.workspace).hide();
		$("#labelTotalGeral", lancamentoController.workspace).hide();
		$("#qtdeTotalDiferencas", lancamentoController.workspace).hide();
		$("#valorTotalDiferencas", lancamentoController.workspace).hide();
			
		$("#datePickerDataMovimento", lancamentoController.workspace).datepicker({
			showOn : "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});

		$("#datePickerDataMovimento", lancamentoController.workspace).mask("99/99/9999");

		$("#selectTiposDiferenca", lancamentoController.workspace).val(null);
	},
	
	verificarExistenciaEstudo : function(idDiferenca) {

		var data = [
				{
					name: 'idDiferenca', value: idDiferenca
				}
			];
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/rateio/validarEstudo", 
			data,
			function(result) {
				
				//popupRateioDiferenca(idDiferenca);
				lancamentoController.popupNovasDiferencas(idDiferenca);
			},
			null
		);
	},

	executarPreProcessamento : function(data) {
		
		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);
			
			$(".grids", lancamentoController.workspace).hide();
			$("#btnConfirmar", lancamentoController.workspace).hide();
			$("#labelTotalGeral", lancamentoController.workspace).hide();
			$("#qtdeTotalDiferencas", lancamentoController.workspace).hide();
			$("#valorTotalDiferencas", lancamentoController.workspace).hide();

			return;
		}
		
		var resultado = data.result;

		if (!resultado) {

			return;
		}
		
		$("#qtdeTotalDiferencas", lancamentoController.workspace).html(resultado.qtdeTotalDiferencas);
		
		$("#valorTotalDiferencas", lancamentoController.workspace).html(resultado.valorTotalDiferencas);

		if (resultado.qtdeTotalDiferencas == 0) {

			$("#labelTotalGeral", lancamentoController.workspace).hide();
			$("#qtdeTotalDiferencas", lancamentoController.workspace).hide();
			
		} else {

			$("#labelTotalGeral", lancamentoController.workspace).show();
			$("#qtdeTotalDiferencas", lancamentoController.workspace).show();
		}

		if (!resultado.tableModel) {

			return;
		}
		
		$.each(resultado.tableModel.rows, function(index, row) {

			var linkRateioDiferenca = '<a id="ratearDiferenca' + row.cell.id + '" href="javascript:;" onclick="lancamentoController.verificarExistenciaEstudo(' + row.cell.id + ');" style="cursor:pointer">' +
									     '<img src="' + contextPath + '/images/bt_cadastros.png" hspace="5" border="0px" />' +
									  '</a>';

			if (row.cell.automatica) {
				
				var linkExclusaoDiferenca = '<a id="excluirDiferenca' + row.cell.id + '" href="javascript:;" style="cursor:default; opacity:0.4; filter:alpha(opacity=40);">' +
												'<img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
											'</a>';
				
			} else {

				var linkExclusaoDiferenca = '<a id="excluirDiferenca' + row.cell.id + '" href="javascript:;" onclick="lancamentoController.popupExclusaoDiferenca(' + row.cell.id + ');" style="cursor:pointer">' +
												'<img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
											'</a>';
			}
							
			row.cell.acao = linkRateioDiferenca + linkExclusaoDiferenca;
		});

		if ($(".grids", lancamentoController.workspace).css('display') == 'none') {	

			$(".grids", lancamentoController.workspace).show();
			$("#btnConfirmar", lancamentoController.workspace).show();
			$("#labelTotalGeral", lancamentoController.workspace).show();
			$("#qtdeTotalDiferencas", lancamentoController.workspace).show();
			$("#valorTotalDiferencas", lancamentoController.workspace).show();
		}

		return resultado.tableModel;
	},

	pesquisar : function(confirmado) { 
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/limparSessao", 
			'confirmado=' + confirmado,
			function(result) {

				if (!result.confirmado) {

					lancamentoController.popupMensagemConfirmacao();
					
				} else {

					var formData = [
		   				{
		   					name: 'dataMovimentoFormatada', value: $("#datePickerDataMovimento", lancamentoController.workspace).val()
		   				},
		   				{
		   					name: 'tipoDiferenca', value: $("#selectTiposDiferenca", lancamentoController.workspace).val()
		   				}
		   			];

					$("#gridLancamentos", lancamentoController.workspace).flexOptions({
						url : contextPath + '/estoque/diferenca/lancamento/pesquisa', 
						params: formData,
						newp: 1
					});
					
					$("#gridLancamentos", lancamentoController.workspace).flexReload();

					$("#dialogConfirmacaoPerdaDados", lancamentoController.workspace).dialog("close");
				}
			}
		);
	},

	popupMensagemConfirmacao : function() {

		$("#dialogConfirmacaoPerdaDados", lancamentoController.workspace).dialog({
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
			},
			form: $("#dialogConfirmacaoPerdaDados", this.workspace).parents("form")
		});
		
		$("#dialogConfirmacaoPerdaDados", lancamentoController.workspace).show();
	},

	popupExclusaoDiferenca : function(idDiferenca) {

		$("#dialog-excluir", lancamentoController.workspace).dialog({
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
					
					$("#gridLancamentos", lancamentoController.workspace).flexOptions(
						{
							url : contextPath + '/estoque/diferenca/lancamento/excluir',
							params: data
						}
					);
					
					$("#gridLancamentos", lancamentoController.workspace).flexReload();
					
				}, "Cancelar": function() {
					
					$(this).dialog("close");
				}
			},
			form: $("#dialog-excluir", this.workspace).parents("form")
		});
		
		$("#dialog-excluir", lancamentoController.workspace).show();
	},

	exibirBotaoNovo : function(tipoDiferenca) {

		if (tipoDiferenca) {
		
			$("#btnNovo", lancamentoController.workspace).show();
			
		} else {
			
			$("#btnNovo", lancamentoController.workspace).hide();
		}
	},
	
	popupConfirmar : function(){
		$("#dialog-confirmar-lancamentos", lancamentoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: 
			{
				"Confirmar": function() {
					
					$.postJSON(
						contextPath + "/estoque/diferenca/confirmarLancamentos", 
						null,
						function(result) {

							lancamentoController.inicializar();
						}
					);

					$(this).dialog("close");
				
				}, "Cancelar": function() {
					
					$(this).dialog("close");
				}
			},
			form: $("#dialog-confirmar-lancamentos", this.workspace).parents("form")			
		});
		
		$("#dialog-confirmar-lancamentos", lancamentoController.workspace).show();
	},

	configurarFlexiGrid : function() {

		$("#gridLancamentos", lancamentoController.workspace).flexigrid({
			preProcess: lancamentoController.executarPreProcessamento,
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
	
}, BaseController);
