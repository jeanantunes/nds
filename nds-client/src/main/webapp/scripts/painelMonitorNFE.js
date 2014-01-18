var PainelMonitorNFE = $.extend(true, {

	init : function() {

		var colunas = PainelMonitorNFE.obterColModel();
		
		$("#serieNfe", PainelMonitorNFE.workspace).numeric();
		
		$("#numeroInicial", PainelMonitorNFE.workspace).numeric();
		
		$("#numeroFinal", PainelMonitorNFE.workspace).numeric();
		
		$("#nfeGrid", PainelMonitorNFE.workspace).flexigrid({
			onSuccess: function() {bloquearItensEdicao(PainelMonitorNFE.workspace);},
			colModel : colunas,
			preProcess: PainelMonitorNFE.executarPreProcessamento,
			dataType : 'json',
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960
		});
		
		$('#dataInicial', PainelMonitorNFE.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$('#dataInicial', PainelMonitorNFE.workspace).mask("99/99/9999");	
	
		
		$('#dataFinal', PainelMonitorNFE.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$('#dataFinal', PainelMonitorNFE.workspace).mask("99/99/9999");	
		
		$(".grids", PainelMonitorNFE.workspace).hide();
		
	    $(".bt_arq", PainelMonitorNFE.workspace).hide();
	},
	
	pesquisar: function() {
		
		var box = $("#box", PainelMonitorNFE.workspace).val();
		var dataInicial = $("#dataInicial", PainelMonitorNFE.workspace).val();
		var dataFinal = $("#dataFinal", PainelMonitorNFE.workspace).val();
		var tipoDocumento = $('input:radio[name=radioTipoDoc]:checked', PainelMonitorNFE.workspace).val();
		var documento = $("#documento", PainelMonitorNFE.workspace).val();
		var tipoNfe = $("#tipoNfe", PainelMonitorNFE.workspace).val();
		var numeroInicial = $("#numeroInicial", PainelMonitorNFE.workspace).val();
		var numeroFinal = $("#numeroFinal", PainelMonitorNFE.workspace).val();
		var chaveAcesso = $("#chaveAcesso", PainelMonitorNFE.workspace).val();
		var situacaoNfe = $("#situacaoNfe", PainelMonitorNFE.workspace).val();
		var serieNfe	= $("#serieNfe", PainelMonitorNFE.workspace).val();	
		
		var params = [
		        {name:'filtro.box', value: box },
		        {name:'filtro.dataInicial', value: dataInicial },
		        {name:'filtro.dataFinal', value: dataFinal },
		        {name:'filtro.tipoDocumento', value: tipoDocumento },
		        {name:'filtro.documento', value: documento },
		        {name:'filtro.tipoNfe', value: tipoNfe },
		        {name:'filtro.numeroInicial', value: numeroInicial },
		        {name:'filtro.numeroFinal', value: numeroFinal },
		        {name:'filtro.chaveAcesso', value: chaveAcesso },
		        {name:'filtro.situacaoNfe', value: situacaoNfe },
		        {name:'filtro.serie',    value: serieNfe}
		];
		
		$("#nfeGrid", PainelMonitorNFE.workspace).flexOptions({
			url: contextPath + "/nfe/painelMonitorNFe/pesquisar", params: params
		});
		
		$("#nfeGrid", PainelMonitorNFE.workspace).flexReload();
	},
	
	limparCheck:function (id){
		
		$('#'+id, PainelMonitorNFE.workspace).attr("checked",false);	
	},
	
	checkAll: function (todos) {
		
		if(todos.checked == false) {
			
			PainelMonitorNFE.limparAll();
		}		
		else {										
			
			PainelMonitorNFE.selecionarAll();
		}	
	},
	
	selecionarAll: function(){
		
		var linhasDaGrid = $("#nfeGrid tr", PainelMonitorNFE.workspace);
		
		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);
			
			var colunaSelecao = linha.find("td")[11];
			
			$(colunaSelecao).find("div").find('input[name="checkgroup"]', PainelMonitorNFE.workspace).attr("checked",true);
			
		});
	},
	
	limparAll: function (){
		
		var linhasDaGrid = $("#nfeGrid tr", PainelMonitorNFE.workspace);
		
		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);

			var colunaSelecao = linha.find("td")[11];
			
			$(colunaSelecao).find("div").find('input[name="checkgroup"]', PainelMonitorNFE.workspace).attr("checked",false);
			
		});
	},
	
	executarPreProcessamento: function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", PainelMonitorNFE.workspace).hide();
				
		    $(".bt_arq", PainelMonitorNFE.workspace).hide();

			return {total: 0, rows: {}};
		}
		
		$.each(resultado.rows, function(index, value){
			
			var hiddenField = '<input type="hidden" name="lineId" value="'+value.id+'" />';
			
			value.cell.imprimirLinha = '<a isEdicao="true" href="javascript:;" onclick="PainelMonitorNFE.imprimirDanfeUnica('+value.id+')">'+
			'<img title="Imprimir" src="'+contextPath+'/images/ico_impressora.gif" border="0"/></a>';
			
			value.cell.sel = '<input isEdicao="true" type="checkbox" name="checkgroup" style="float: left; margin-right: 25px;"/>'+hiddenField;
			
			var descMovimentoIntegracao = value.cell.movimentoIntegracao;

			var tooltipMovimentoIntegracao = '';
			
			if(typeof descMovimentoIntegracao == 'undefined') {
				tooltipMovimentoIntegracao = " - ";
			} else {
				tooltipMovimentoIntegracao = '<a href="javascript:;"><img title="'+descMovimentoIntegracao+'" src="' + contextPath + '/images/ico_detalhes.png" border="0" hspace="3"/></a>';
			}
			
			
			value.cell.movimentoIntegracao = tooltipMovimentoIntegracao;
			
		});
		
		$('.grids', PainelMonitorNFE.workspace).show();
		
	    $(".bt_arq", PainelMonitorNFE.workspace).show();
		
		return resultado;
		
	},
	
	imprimirDanfes : function(indEmissaoDepec) {		
		
		var selecionados = new Array();
		
		var linhasDaGrid = $("#nfeGrid tr", PainelMonitorNFE.workspace);
				
		$.each(linhasDaGrid, function(index, value) {
			
			var linha = $(value, PainelMonitorNFE.workspace);
			
			var colunaSelecao = linha.find("td")[11];
		
			var inputSelecao = $(colunaSelecao, PainelMonitorNFE.workspace).find("div").find('input[name="checkgroup"]');
			
			var lineId = $(colunaSelecao, PainelMonitorNFE.workspace).find("div").find('input[name="lineId"]').val();
			
			if(inputSelecao.attr('checked')) {				
				selecionados.push(lineId);
			}
			
		});
		var params = {indEmissaoDepec:indEmissaoDepec };
		params = serializeArrayToPost('listaLineIdsImpressaoDanfes',selecionados, params );
		
		$.postJSON(contextPath + "/nfe/painelMonitorNFe/prepararDanfesImpressao", params, 
			function(){
			window.location = contextPath + "/nfe/painelMonitorNFe/imprimirDanfes" + '?indEmissaoDepec='+indEmissaoDepec;
		});
		
	},
	
	cancelarNfe : function() {
		
		$.postJSON(contextPath + "/nfe/painelMonitorNFe/cancelarNfe");
		
	},

	
	imprimirDanfeUnica : function(lineId) {
		
		var params = {lineIdImpressaoDanfe:+lineId};
		
		$.postJSON(contextPath + "/nfe/painelMonitorNFe/prepararDanfeUnicaImpressao",
			params,
			function(){
				window.location = contextPath + "/nfe/painelMonitorNFe/imprimirDanfes" ;
			}		
		);
		
	},
	
	obterColModel : function() {


			var colModel = [ {
				display : 'Nota',
				name : 'numero',
				width : 55,
				sortable : true,
				align : 'left'
			}, {
				display : 'Série',
				name : 'serie',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Emissão',
				name : 'emissao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo Emissão',
				name : 'tipoEmissao',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'CNPJ Destinatário',
				name : 'cnpjDestinatario',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'CNPJ Remetente',
				name : 'cnpjRemetente',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'CPF Remetente',
				name : 'cpfRemetente',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status NF-e',
				name : 'statusNfe',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo NF-e',
				name : 'tipoNfe',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Movimento Integração',
				name : 'movimentoIntegracao',
				width : 140,
				sortable : true,
				align : 'left'
			}, {
				display : ' ',
				name : 'imprimirLinha',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : ' ',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			} ];	
			
			return colModel;
	}
	
}, BaseController);

//@ sourceURL=PainelNFE.js
