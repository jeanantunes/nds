var manutencaoStatusCotaController = $.extend(true, {

	pesquisaCotaManutencaoStatusCota : null,

	init : function(pesquisaCota) {
		
		definirAcaoPesquisaTeclaEnter(manutencaoStatusCotaController.workspace);
		
		this.pesquisaCotaManutencaoStatusCota = pesquisaCota;

		manutencaoStatusCotaController.configurarCamposData();

		manutencaoStatusCotaController.configurarCamposNumericos();
		
		var followUp = $('#manutencao-status-numeroCotaFollowUp', manutencaoStatusCotaController.workspace).val();
		
		if(followUp != ''){			
			manutencaoStatusCotaController.pesquisarHistoricoStatusCota();
		}
	
	},
	
obterColunasGridPesquisaSemCota:function(){
		
		var colModel = [ 
		{
			display : 'Cota',
			name : 'numeroCota',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome',
			name : 'nomeCota',
			width : 120,
			sortable : true,
			align : 'left'
		}, {
			display : 'Data',
			name : 'data',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Status Anterior',
			name : 'statusAnterior',
			width : 80,
			sortable : true,
			align : 'left'
		}, {
			display : 'Status Atualizado',
			name : 'statusAtualizado',
			width : 90,
			sortable : true,
			align : 'left'
		}, {
			display : 'Usuário',
			name : 'usuario',
			width : 80,
			sortable : true,
			align : 'left'
		}, {
			display : 'Motivo',
			name : 'motivo',
			width : 120,
			sortable : true,
			align : 'left'
		}, {
			display : 'Descrição',
			name : 'descricao',
			width : 200,
			sortable : true,
			align : 'left'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 30,
			sortable : false,
			align : 'center'
		}];

		return colModel;
	},
	
	obterColunasGridPesquisaComCota:function(){
		
		var colModel = [{
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
		}];
		
		return colModel;
	},

	
	configurarFlexiGrid : function(colunas) {
		
		$(".grids", manutencaoStatusCotaController.workspace).empty();
		$(".grids", manutencaoStatusCotaController.workspace).append($("<table>").attr("class", "manutencaoStatusCotaGrid"));
		
		$(".manutencaoStatusCotaGrid", manutencaoStatusCotaController.workspace).flexigrid({
			onSuccess: function() {bloquearItensEdicao(manutencaoStatusCotaController.workspace);},
			preProcess: manutencaoStatusCotaController.executarPreProcessamento,
			dataType : 'json',
			colModel : colunas,
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
			$(".botoesGrid", manutencaoStatusCotaController.workspace).hide();	
			
			return;
		}

		if ($(".grids", manutencaoStatusCotaController.workspace).css('display') == 'none') {	

			$(".grids", manutencaoStatusCotaController.workspace).show();
			$(".botoesGrid", manutencaoStatusCotaController.workspace).show();	
		}
		
		$.each(data.result.rows, function(index, row) {

			row.cell.statusAtualizado = 
				(row.cell.processado)
					? row.cell.statusAtualizado : row.cell.statusAtualizado + ' (Não processado)';
			
			if(row.cell.numeroCota){
				
				var linkEdicao = '<a isEdicao="true" href="javascript:;" onclick="manutencaoStatusCotaController.novo('+ row.cell.numeroCota +');" style="cursor:pointer">' +
				 '<img src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" title="Incluir Novo Status" />' +
				 '</a>';			
			
				row.cell.acao = linkEdicao;
				
				row.cell.statusAnterior = row.cell.statusAnterior ? row.cell.statusAnterior : "";
				
				row.cell.data = row.cell.data.$;
			}
			
		});

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

	novo : function(numeroCota) {

		var filtro = manutencaoStatusCotaController.obterDadosFiltro(numeroCota);
		
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
					manutencaoStatusCotaController.confirmarNovo();
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
			beforeClose: function() {
				$(".manutencaoStatusCotaGrid", manutencaoStatusCotaController.workspace).flexReload();
		    },
			form: $("#dialog-novo", manutencaoStatusCotaController.workspace).parents("form")
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
			form: $("#dialog-aviso", manutencaoStatusCotaController.workspace).parents("form")
		});
	},
	
	
	callBackSuccess:function () {
		
		pesquisaCotaManutencaoStatusCota.obterPorNumeroCota($("#numeroCotaManutencaoStatusCota", manutencaoStatusCotaController.workspace).val(), false, function(result) {

			if (!result) {

				return;
			}

			$("#manutencao-status-box", manutencaoStatusCotaController.workspace).val(result.codigoBox);
		});

	},
	
	callBackErro:function(){
		
		$("#manutencao-status-box", manutencaoStatusCotaController.workspace).val("");
	
	},

	pesquisarHistoricoStatusCota : function() {

		var colunas;
		
		var followUp = $('#manutencao-status-numeroCotaFollowUp', manutencaoStatusCotaController.workspace).val();
		var filtro;			
		if(followUp != ''){			
			filtro = [
						{name: 'filtro.numeroCota' , value: followUp },
						{name: 'filtro.periodo.dataInicial' , value: null },
						{name: 'filtro.periodo.dataFinal' , value: null }
						];
		}else{
			
			filtro = manutencaoStatusCotaController.obterDadosFiltro();
			
			
		}
		
		if(manutencaoStatusCotaController.isCotainformadaParaPesquisa()){
			
			colunas = manutencaoStatusCotaController.obterColunasGridPesquisaComCota();
		}
		else{
			
			colunas = manutencaoStatusCotaController.obterColunasGridPesquisaSemCota();
		}
		
		manutencaoStatusCotaController.configurarFlexiGrid(colunas);
		
		$(".manutencaoStatusCotaGrid", manutencaoStatusCotaController.workspace).flexOptions({
			url : contextPath + '/financeiro/manutencaoStatusCota/pesquisar', 
			params: filtro,
			newp: 1
		});
		
		$(".manutencaoStatusCotaGrid", manutencaoStatusCotaController.workspace).flexReload();
	},

	obterDadosFiltro : function(numeroCota) {
		
		var numCota = $("#numeroCotaManutencaoStatusCota",manutencaoStatusCotaController.workspace).val();
		
		if(numeroCota){
			
			numCota = numeroCota;
		}
		
		var filtro = [
				{
					name: 'filtro.numeroCota', value: numCota
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
					name: 'novoHistoricoSituacaoCota.cota.numeroCota', value: $("#numeroCotaNovo", manutencaoStatusCotaController.workspace).html()
					// Comentado pq este número da cota é da pesquisa!
					//name: 'novoHistoricoSituacaoCota.cota.numeroCota', value: $("#numeroCotaManutencaoStatusCota", manutencaoStatusCotaController.workspace).val()
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
			novoHistoricoSituacaoCota,
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
	},
	
	isCotainformadaParaPesquisa:function(){
		return ($("#numeroCotaManutencaoStatusCota",manutencaoStatusCotaController.workspace).val().trim().length > 0);
	}

}, BaseController);
//@ sourceURL=manutencaoStatusCota.js