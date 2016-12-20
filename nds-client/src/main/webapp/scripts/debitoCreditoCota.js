var debitoCreditoCotaController = $.extend(true, {
	
	processarResultadoConsultaDebitosCreditos : function(data) {
		
		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);

			$(".grids", debitoCreditoCotaController.workspace).hide();

			return;
		}

		var i;

		for (i = 0 ; i < data.tableModel.rows.length; i++) {

			var lastIndex = data.tableModel.rows[i].cell.length;

			var isLancamentoManual = data.tableModel.rows[i].cell[lastIndex - 1];
			
			data.tableModel.rows[i].cell[lastIndex - 1] = debitoCreditoCotaController.getAction(data.tableModel.rows[i].id, isLancamentoManual);
		}

		if ($(".grids", debitoCreditoCotaController.workspace).css('display') == 'none') {

			$(".grids", debitoCreditoCotaController.workspace).show();
		}

		$("#footerValorTotal", debitoCreditoCotaController.workspace).html("Total: R$ " + data.valorTotal);
		
		return data.tableModel;
	},

	getAction : function(idMovimento, isLancamentoManual) {

		var acoes;
		
		if (isLancamentoManual == "true") {
			
			acoes = 
				'<a isEdicao="true" href="javascript:;" onclick="debitoCreditoCotaController.editarMovimento(' + idMovimento + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Editar movimento">' +
				'<img src="' + contextPath + '/images/ico_editar.gif" border="0px"/>' +
				'</a>' +
				'<a isEdicao="true href="javascript:;" onclick="debitoCreditoCotaController.confirmarExclusaoMovimento(' + idMovimento + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Excluir movimento">' +
				'<img src="' + contextPath + '/images/ico_excluir.gif" border="0px"/>' +
				'</a>';	
			
		} else {
			
			acoes = 
				'<a href="javascript:;" onclick="debitoCreditoCotaController.detalheMovimento(' + idMovimento + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Detalhes do Movimento">' +
				'<img src="' + contextPath + '/images/ico_detalhes.png" border="0px"/>' +
				'</a>' +
				'<span style="border:0px;margin:5px">' +
				'<img src="' + contextPath + '/images/ico_excluir.gif" border="0px" style="opacity:0.4"/>' +
				'</span>';
		}
		
		return acoes;		
	},
	
	popularGridDebitosCreditos : function() {

		var formData = $("#formPesquisaDebitosCreditos", debitoCreditoCotaController.workspace).serializeArray();

		$(".debitosCreditosGrid", debitoCreditoCotaController.workspace).flexigrid({
			url : contextPath + '/financeiro/debitoCreditoCota/pesquisarDebitoCredito',
			dataType : 'json',
			onSuccess: function() {bloquearItensEdicao(debitoCreditoCotaController.workspace);},
			preProcess: debitoCreditoCotaController.processarResultadoConsultaDebitosCreditos,
			colModel : [  {
				display : 'Data de Lanç.',
				name : 'dataLancamento',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Venci.',
				name : 'dataVencimento',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Cota',
				name : 'numeroCota',
				width : 30,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 160,
				sortable : true,
				align : 'left'
			},{
				display : 'Tipo de Lançamento',
				name : 'tipoLancamento',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 50,
				sortable : true,
				align : 'right'
			},{
				display : 'Observação',
				name : 'observacao',
				width : 190,
				sortable : true,
				align : 'left'
			}, {	display : 'Usuário',
				name : 'usuario',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			sortname : "dataLancamento",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			params: formData,
			rp : 15,
			singleSelect: true,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto'
		});

		$(".debitosCreditosGrid", debitoCreditoCotaController.workspace).flexOptions({
			url : contextPath + '/financeiro/debitoCreditoCota/pesquisarDebitoCredito',
			preProcess: debitoCreditoCotaController.processarResultadoConsultaDebitosCreditos,
			params: formData
		});

		$(".debitosCreditosGrid", debitoCreditoCotaController.workspace).flexReload();
	},

	editarMovimento : function(idMovimento) {

		$.postJSON(
			contextPath + '/financeiro/debitoCreditoCota/prepararMovimentoFinanceiroCotaParaEdicao',
			{ "idMovimento" : idMovimento },
			function(result) {
				
				$("#edicaoId", debitoCreditoCotaController.workspace).val(result.id);
				$("#edicaoTipoMovimento", debitoCreditoCotaController.workspace).val(result.tipoMovimentoFinanceiro.id);
				$("#edicaoNumeroCota", debitoCreditoCotaController.workspace).val(result.numeroCota);
				$("#edicaoNomeCota", debitoCreditoCotaController.workspace).val(result.nomeCota);
				$("#edicaoDataLancamento", debitoCreditoCotaController.workspace).val(result.dataLancamento);
				$("#edicaoDataVencimento", debitoCreditoCotaController.workspace).val(result.dataVencimento);
				$("#edicaoValor", debitoCreditoCotaController.workspace).val(result.valor);
				$("#edicaoObservacao", debitoCreditoCotaController.workspace).val(result.observacao);

				debitoCreditoCotaController.configuraTelaEdicao(result.tipoMovimentoFinanceiro.id);

				debitoCreditoCotaController.popup_alterar(result.permiteAlteracao);

			},
			function(result) {
				
				debitoCreditoCotaController.processarResultadoConsultaDebitosCreditos(result);
			},
			true
		);
		
	},
	
	detalheMovimento : function(idMovimento) {

		$.postJSON(
			contextPath + '/financeiro/debitoCreditoCota/prepararMovimentoFinanceiroCotaParaEdicao',
			{ "idMovimento" : idMovimento },
			function(result) {
				
				$("#detalheId", debitoCreditoCotaController.workspace).val(result.id);
				$("#detalheTipoMovimento", debitoCreditoCotaController.workspace).val(result.tipoMovimentoFinanceiro.descricao);
				$("#detalheNumeroCota", debitoCreditoCotaController.workspace).val(result.numeroCota);
				$("#detalheNomeCota", debitoCreditoCotaController.workspace).val(result.nomeCota);
				$("#detalheDataLancamento", debitoCreditoCotaController.workspace).val(result.dataLancamento);
				$("#detalheDataVencimento", debitoCreditoCotaController.workspace).val(result.dataVencimento);
				$("#detalheValor", debitoCreditoCotaController.workspace).val(result.valor);
				$("#detalheObservacao", debitoCreditoCotaController.workspace).val(result.observacao);
			},
			function(result) {
				
				debitoCreditoCotaController.processarResultadoConsultaDebitosCreditos(result);
			},
			true
		);
		
		debitoCreditoCotaController.popup_detalhe();
	},
	
	popup_alterar : function(permiteAlteracao) {
	
		if (permiteAlteracao) {
			$( "#dialog-editar", debitoCreditoCotaController.workspace ).dialog({
				resizable: false,
				height:500,
				width:550,
				modal: true,
				buttons:[ 
				          {
					           id:"bt_confirmar",
					           text:"Confirmar", 
					           click: function() {
					        	   debitoCreditoCotaController.salvarMovimentoFinanceiro(true);
					           }
				           },
				           {
					           id:"bt_cancelar",
					           text:"Cancelar", 
					           click: function() {
					        	   $( this ).dialog( "close" );
					           }
				           }
		        ],
				beforeClose: function() {
					clearMessageDialogTimeout();
			    },
			    form : $( "#form-dialog-editar", debitoCreditoCotaController.workspace )
			});	
		} else {
			$( "#dialog-editar", debitoCreditoCotaController.workspace ).dialog({
				resizable: false,
				height:450,
				width:500,
				modal: true,
				buttons:[ 
				           {
					           id:"bt_cancelar",
					           text:"Cancelar", 
					           click: function() {
					        	   $( this ).dialog( "close" );
					           }
				           }
		        ],
				beforeClose: function() {
					clearMessageDialogTimeout();
			    },
			    form : $( "#form-dialog-editar", debitoCreditoCotaController.workspace )
			});	
		}
	
	},
	
	popup_detalhe : function() {
	
		$( "#dialog-detalhe", debitoCreditoCotaController.workspace ).dialog({
			resizable: false,
			height:300,
			width:500,
			modal: true,
			buttons:[ 
			           {
				           id:"bt_fechar",
				           text:"Fechar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
	        form : $( "#form-dialog-detalhe", debitoCreditoCotaController.workspace )
		});	
	},
	
	salvarMovimentoFinanceiro : function(isEdicao) {

		var url;
		var formData;
		var dialogId = isEdicao ? "#dialog-editar" : "#dialog-novo";
		
		if (isEdicao) {
			
			formData = $("#formEdicaoMovimentoFinanceiro", debitoCreditoCotaController.workspace).serializeArray();
			url = contextPath + '/financeiro/debitoCreditoCota/editarMovimentoFincanceiroCota';
		
		} else {
			
			formData = novoDialogDebitoCreditoCotaController.obterListaMovimentos() + 'idTipoMovimento=' + $("#novoTipoMovimento", debitoCreditoCotaController.workspace).val();
			url = contextPath + '/financeiro/debitoCreditoCota/criarMovimentoFincanceiroCota';
		}

		$.postJSON(
			url,
			formData,
			function(result) {

				debitoCreditoCotaController.popularGridDebitosCreditos();

				$(dialogId,debitoCreditoCotaController.workspace).dialog( "close" );
				$(".grids",debitoCreditoCotaController.workspace).show();
				
				exibirMensagem(
					result.tipoMensagem, 
					result.listaMensagens
				);
			},
			function(result) {

				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens, ""
				);

			},
			true
		);
	},

	removerMovimento : function(idMovimento) {

		$.postJSON(
			contextPath + '/financeiro/debitoCreditoCota/removerMovimentoFinanceiroCota',
			{ "idMovimento" : idMovimento },
			function(result) {

				debitoCreditoCotaController.popularGridDebitosCreditos();

				$("#dialog-excluir", debitoCreditoCotaController.workspace).dialog( "close" );
				
				exibirMensagem(result.tipoMensagem, result.listaMensagens);
			},
			function(result) {

				$("#dialog-excluir", debitoCreditoCotaController.workspace).dialog( "close" );
				
				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens, ""
				);
			}
		);
	},
	
	confirmarExclusaoMovimento : function(idMovimento) {
		
		$( "#dialog-excluir", debitoCreditoCotaController.workspace ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   debitoCreditoCotaController.removerMovimento(idMovimento);
				           }
			           },
			           {
				           id:"bt_cancelar",
				           text:"Cancelar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
	        form : $( "#form-dialog-excluir", debitoCreditoCotaController.workspace )
		});
	},
	
	
 	pesquisarCota : function(isModalAlteracao) {
 		
 		var numeroCota;
 		
 		if (isModalAlteracao) {
 			
 			numeroCota = $("#edicaoNumeroCota", debitoCreditoCotaController.workspace).val();
 		
 		} else {
 			
 			numeroCota = $("#debito-credito-numeroCota", debitoCreditoCotaController.workspace).val();
 		}
 		
 		$.postJSON(
			contextPath + '/financeiro/debitoCreditoCota/buscarCotaPorNumero',
			{ "numeroCota": numeroCota },
			function(result) {

				if (isModalAlteracao) {

					$("#edicaoNomeCota", debitoCreditoCotaController.workspace).val(result);
					
				} else {

					$("#nomeCota", debitoCreditoCotaController.workspace).html(result);
				}
			},
			null,
			true
		);
 	},

 	//VERIFICA SE O TIPO DE LANÇAMENTO CONSIDERA FATURAMENTO DA COTA
	configuraTelaEdicao : function(idTipoLancamento){
		var data = [{name: 'idTipoMovimento', value: idTipoLancamento}];
		$.postJSON(contextPath + "/financeiro/debitoCreditoCota/obterGrupoFaturamento",
				   data,
				   debitoCreditoCotaController.sucessCallbackConfiguraTelaEdicao,
				   null,
				   true);
	},
	
	sucessCallbackConfiguraTelaEdicao : function(result){
		
		$("#grupoMovimentoHidden", debitoCreditoCotaController.workspace).val(result);
		
		if (result=='DEBITO_SOBRE_FATURAMENTO'){
			
			$('#edicaoBaseCalculo', debitoCreditoCotaController.workspace).show();
			$('#edicaoPercentual', debitoCreditoCotaController.workspace).show();
			$('#edicaoDataPeriodo', debitoCreditoCotaController.workspace).show();
			
			$('#tituloEdicaoBaseCalculo', debitoCreditoCotaController.workspace).show();
			$('#tituloEdicaoPercentual', debitoCreditoCotaController.workspace).show();
			$('#tituloEdicaoDataPeriodo', debitoCreditoCotaController.workspace).show();
			
			$("#edicaoValor", debitoCreditoCotaController.workspace).attr('readonly','readonly'); 
		}
		else{	
			
			$("#edicaoBaseCalculo", debitoCreditoCotaController.workspace).val(0);
			$("#edicaoPercentual", debitoCreditoCotaController.workspace).val('');
			$("#edicaoDataPeriodoDe", debitoCreditoCotaController.workspace).val('');
			$("#edicaoDataPeriodoAte", debitoCreditoCotaController.workspace).val('');
			
			$('#edicaoBaseCalculo', debitoCreditoCotaController.workspace).hide();
			$('#edicaoPercentual', debitoCreditoCotaController.workspace).hide();
			$('#edicaoDataPeriodo', debitoCreditoCotaController.workspace).hide();
			
			$('#tituloEdicaoBaseCalculo', debitoCreditoCotaController.workspace).hide();
			$('#tituloEdicaoPercentual', debitoCreditoCotaController.workspace).hide();
			$('#tituloEdicaoDataPeriodo', debitoCreditoCotaController.workspace).hide();
			
			$('#edicaoValor', debitoCreditoCotaController.workspace).removeAttr("readonly"); 
		}
	},
	
	limparValor : function(){
		$("#edicaoValor", debitoCreditoCotaController.workspace).val('');
	},
 	
 	obterInformacoesParaEdicao : function(){

		var grupoMovimento = $("#grupoMovimentoHidden", debitoCreditoCotaController.workspace).val();
		var percentual = $("#edicaoPercentual", debitoCreditoCotaController.workspace).val();
		var baseCalculo = $("#edicaoBaseCalculo", debitoCreditoCotaController.workspace).val();
		var dataPeriodoInicial = $("#edicaoDataPeriodoDe", debitoCreditoCotaController.workspace).val();
		var dataPeriodoFinal = $("#edicaoDataPeriodoAte", debitoCreditoCotaController.workspace).val();
        var numeroCota = $("#edicaoNumeroCota", debitoCreditoCotaController.workspace).val();
        var indexValor = 0;
		
		if ((grupoMovimento == "DEBITO_SOBRE_FATURAMENTO") && (percentual!='') && (baseCalculo!='') && (dataPeriodoInicial!='') && (dataPeriodoFinal!='')){

			var data = [{name:'numeroCota', value:numeroCota},
				        {name:'grupoMovimento', value:grupoMovimento},
				        {name:'percentual', value:percentual},
				        {name:'baseCalculo', value:baseCalculo},
				        {name:'dataPeriodoInicial', value:dataPeriodoInicial},
				        {name:'dataPeriodoFinal', value:dataPeriodoFinal},
				        {name:'index', value:indexValor}];
			
			$.postJSON(contextPath + "/financeiro/debitoCreditoCota/obterInformacoesParaLancamentoIndividual",
					    data,
						function(result){
							$("#edicaoValor", debitoCreditoCotaController.workspace).val(result.valor);
						},
						null,
						true);
			
		}	
	},

	init : function () {
	
		$( "#datepickerDe", debitoCreditoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerAte", debitoCreditoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerDeVenc", debitoCreditoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerAteVenc", debitoCreditoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerData", debitoCreditoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#datepickerDe", debitoCreditoCotaController.workspace ).mask("99/99/9999");
		$( "#datepickerAte", debitoCreditoCotaController.workspace ).mask("99/99/9999");
		$( "#datepickerDeVenc", debitoCreditoCotaController.workspace ).mask("99/99/9999");
		$( "#datepickerAteVenc", debitoCreditoCotaController.workspace ).mask("99/99/9999");
		$( "#datepickerData", debitoCreditoCotaController.workspace ).mask("99/99/9999");
		
		$('#edicaoDataLancamento', debitoCreditoCotaController.workspace).datepicker("option", {minDate:-1,maxDate:-2})
		
		$( "#edicaoDataVencimento", debitoCreditoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#edicaoDataPeriodoDe", debitoCreditoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#edicaoDataPeriodoAte", debitoCreditoCotaController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#edicaoNumeroCota", debitoCreditoCotaController.workspace).numeric();
		$("#edicaoPercentual", debitoCreditoCotaController.workspace).numeric();

		$("input[id^='edicaoValor']", debitoCreditoCotaController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});

	}
	
}, BaseController);
//@ sourceURL=debitoCreditoCota.js
