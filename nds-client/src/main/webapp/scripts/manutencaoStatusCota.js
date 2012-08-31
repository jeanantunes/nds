var manutencaoStatusCotaController = $.extend(true, {

	pesquisaCotaManutencaoStatusCota : null,

	init : function(pesquisaCota) {
		this.pesquisaCotaManutencaoStatusCota = pesquisaCota;

		var followUp = $('#numeroCotaFollowUp', manutencaoStatusCotaController.workspace).val();
		
		if(followUp != ''){			
			manutencaoStatusCotaController.pesquisarHistoricoStatusCota();
		}
	
	},
	
	configurarFlexiGrid : function() {
		
		$(".manutencaoStatusCotaGrid", manutencaoStatusCotaController.workspace).flexigrid({
			preProcess: manutencaoStatusCotaController.executarPreProcessamento,
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
				width : 365,
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
			height : 'auto'
		});
	},

	executarPreProcessamento : function(data) {
		
		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);
			
			$(".grids", manutencaoStatusCotaController.workspace).hide();

			return;
		}

		if ($(".grids", manutencaoStatusCotaController.workspace).css('display') == 'none') {	

			$(".grids", manutencaoStatusCotaController.workspace).show();
		}

		return data.result;
	},

	configurarCamposData : function() {

		$("#dataInicialStatusCota", manutencaoStatusCotaController.workspace).datepicker({
			showOn : "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});

		$("#dataFinalStatusCota", manutencaoStatusCotaController.workspace).datepicker({
			showOn : "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});

		$("#novaDataInicialStatusCota", manutencaoStatusCotaController.workspace).datepicker({
			showOn : "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});

		$("#novaDataFinalStatusCota", manutencaoStatusCotaController.workspace).datepicker({
			showOn : "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});

		$("#dataInicialStatusCota", manutencaoStatusCotaController.workspace).mask("99/99/9999");
		$("#dataFinalStatusCota", manutencaoStatusCotaController.workspace).mask("99/99/9999");
		$("#novaDataInicialStatusCota", manutencaoStatusCotaController.workspace).mask("99/99/9999");
		$("#novaDataFinalStatusCota", manutencaoStatusCotaController.workspace).mask("99/99/9999");
	},

	configurarCamposNumericos : function() {

		$("input[id='numeroCota']", manutencaoStatusCotaController.workspace).numeric();
	},

	novo : function() {

		var filtro = obterDadosFiltro();
		
		$.postJSON(
			contextPath + "/financeiro/manutencaoStatusCota/novo", 
			filtro,
			function(result) {

				$("#numeroCotaNovo", manutencaoStatusCotaController.workspace).html(result.numero);
				$("#boxNovo", manutencaoStatusCotaController.workspace).html(result.codigoBox);
				$("#novoNomeCota", manutencaoStatusCotaController.workspace).html(result.nome);

				$("#novoStatusCota", manutencaoStatusCotaController.workspace).val("");
					$("#novaDataInicialStatusCota", manutencaoStatusCotaController.workspace).val("");
					$("#novaDataFinalStatusCota", manutencaoStatusCotaController.workspace).val("");
					$("#novoMotivo", manutencaoStatusCotaController.workspace).val("");
					$("#novaDescricao", manutencaoStatusCotaController.workspace).val("");
				
					manutencaoStatusCotaController.popupDialogNovo();
			}
		);    
	},

	popupDialogNovo : function() {

		$("#dialog-novo", manutencaoStatusCotaController.workspace).dialog({
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
			},
			form: $("#dialog-novo", this.workspace).parents("form")
		});
	},

	popupAviso : function(mensagens) {

		montarTextoMensagem($("#mensagemAviso", manutencaoStatusCotaController.workspace), mensagens);
		$("#mensagemConfirmacao", manutencaoStatusCotaController.workspace).html("Deseja continuar mesmo assim ?");

		$("#dialog-aviso", manutencaoStatusCotaController.workspace).dialog({
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
					$("#dialog-novo", manutencaoStatusCotaController.workspace).dialog("close");
				}
			},
			form: $("#dialog-aviso", this.workspace).parents("form")
		});
	},
	
	carregarCodigoBox : function() {
		
		this.pesquisaCotaManutencaoStatusCota.obterPorNumeroCota($("#numeroCota", manutencaoStatusCotaController.workspace).val(), false, function(result) {

			if (!result) {

				return;
			}

			$("#box", manutencaoStatusCotaController.workspace).val(result.codigoBox);
		});
	},

	pesquisarHistoricoStatusCota : function() {

		var followUp = $('#numeroCotaFollowUp', manutencaoStatusCotaController.workspace).val();
		var filtro;			
		if(followUp != ''){			
			filtro = [
						{name: 'filtro.numeroCota' , value: followUp },
						{name: 'filtro.periodo.dataInicial' , value: null },
						{name: 'filtro.periodo.dataFinal' , value: null }
						]
		}else{
			var filtro = manutencaoStatusCotaController.obterDadosFiltro();
		}


		$(".manutencaoStatusCotaGrid", manutencaoStatusCotaController.workspace).flexOptions({
			url : contextPath + '/financeiro/manutencaoStatusCota/pesquisar', 
			params: filtro,
			newp: 1
		});
		
		$(".manutencaoStatusCotaGrid", manutencaoStatusCotaController.workspace).flexReload();
	},

	obterDadosFiltro : function() {

		var filtro = [
				{
					name: 'filtro.numeroCota', value: $("#numeroCota", manutencaoStatusCotaController.workspace).val()
				},
				{
					name: 'filtro.statusCota', value: $("#statusCota", manutencaoStatusCotaController.workspace).val()
				},
				{
					name: 'filtro.periodo.dataInicial', value: $("#dataInicialStatusCota", manutencaoStatusCotaController.workspace).val()
				},
				{
					name: 'filtro.periodo.dataFinal', value: $("#dataFinalStatusCota", manutencaoStatusCotaController.workspace).val()
				},
				{
					name: 'filtro.motivoStatusCota', value: $("#motivo", manutencaoStatusCotaController.workspace).val()
				}
			];

			return filtro;
	},

	confirmarNovo : function() {

		var novoHistoricoSituacaoCota = [
				{
					name: 'novoHistoricoSituacaoCota.cota.numeroCota', value: $("#numeroCota", manutencaoStatusCotaController.workspace).val()
				},
				{
					name: 'novoHistoricoSituacaoCota.novaSituacao', value: $("#novoStatusCota", manutencaoStatusCotaController.workspace).val()
				},
				{
					name: 'novoHistoricoSituacaoCota.dataInicioValidade', value: $("#novaDataInicialStatusCota", manutencaoStatusCotaController.workspace).val()
				},
				{
					name: 'novoHistoricoSituacaoCota.dataFimValidade', value: $("#novaDataFinalStatusCota", manutencaoStatusCotaController.workspace).val()
				},
				{
					name: 'novoHistoricoSituacaoCota.motivo', value: $("#novoMotivo", manutencaoStatusCotaController.workspace).val()
				},
				{
					name: 'novoHistoricoSituacaoCota.descricao', value: $("#novaDescricao", manutencaoStatusCotaController.workspace).val()
				}
			];

		$.postJSON(
			contextPath + "/financeiro/manutencaoStatusCota/novo/confirmar", 
			manutencaoStatusCotaController.novoHistoricoSituacaoCota,
			function(result) {

				exibirMensagem(
					result.tipoMensagem, 
					result.listaMensagens
				);
				
				$("#dialog-novo", manutencaoStatusCotaController.workspace).dialog("close");
			},
			null,
			true
		); 	
	},
	
	inicializar : function () {

		manutencaoStatusCotaController.configurarFlexiGrid();

		manutencaoStatusCotaController.configurarCamposData();

		manutencaoStatusCotaController.configurarCamposNumericos();
	},

	ifInativo : function(status){
		if (status=="INATIVO"){
			$("#novaDataFinalStatusCota", manutencaoStatusCotaController.workspace).val('');
			$("#novaDataFinalStatusCota", manutencaoStatusCotaController.workspace).attr('disabled','disabled');
			$("#novaDataFinalStatusCota", manutencaoStatusCotaController.workspace).datepicker('disable');
			
		}
		else{
			$('#novaDataFinalStatusCota', manutencaoStatusCotaController.workspace).removeAttr("disabled"); 
			$("#novaDataFinalStatusCota", manutencaoStatusCotaController.workspace).datepicker('enable');
		}
	},
	
	dividasAbertoCota : function(status) {
		
		if (status=="ATIVO"){
			
			var numeroCota = $("#numeroCotaNovo", manutencaoStatusCotaController.workspace).html();
			var data = [{name:'numeroCota', value:numeroCota}];
			$.postJSON(contextPath + "/financeiro/manutencaoStatusCota/dividasAbertoCota",
					    data,
					    function(result){
						    if (result!=""){
							    var mensagens = result.listaMensagens;
							    if (mensagens) {
							    	manutencaoStatusCotaController.popupAviso(mensagens);
						        }
						    }
			            },
						null,
			            false);
		}
	}

}, BaseController);
