var PainelMonitorNFE = $.extend(true, {
	
	/**
	 * path de geração de nfe
	 */
	path : contextPath + '/nfe/painelMonitorNFe/',
	
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
			sortname : "numero",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960
		});
		
		this.initFiltroDatas();
		this.initInputs();
		
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
	    
	    $(document).ready(function() {
	    	if($("input:radio[name='painelNfe-radioTipoDoc']:checked").val()=="cnpj"){
				$("input[id=painelNfe-documento]").mask("99.999.999/9999-99");
		                $("#painelNfe-documento").text('Digite o CNPJ:');
			} else {
				$("input[id=painelNfe-documento]").mask("999.999.999-99");
                $(this).attr('checked', false);
			}

	    });
	    
	    params = [];
	    params.push({name: 'tipoEmitente', value: 'DISTRIBUIDOR'});
		params.push({name: 'tipoDestinatario', value: 'COTA'});
		
		$.postJSON(contextPath + '/administracao/naturezaOperacao/obterNaturezasOperacoesPorEmitenteDestinatario', params, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}
			
			$("#painelNfe-filtro-naturezaOperacao").empty();
			
			$('#painelNfe-filtro-naturezaOperacao').append($('<option>', { 
		        value: '-1',
		        text : 'Selecione...'
		    }));
			
			$.each(data.rows, function (i, row) {
			    $('#painelNfe-filtro-naturezaOperacao').append($('<option>', { 
			        value: row.cell.key,
			        text : row.cell.value
			    }));
			});
			
		});
	},
	
	initFiltroDatas : function(){
		
	    $.postJSON(contextPath + '/cadastro/distribuidor/obterDataDistribuidor',
				null, 
				function(result) {
			
					$("#dataInicial", this.workspace).val(result);
					
					$("#dataFinal", this.workspace).val(result);
		        }
		); 
	},
	
	initInputs : function() {
		
		
		$("#painelNfe-filtro-selectFornecedoresDestinatarios").multiselect({
			selectedList : 6,
		});
		$("#painelNfe-filtro-selectFornecedoresDestinatarios").multiselect("disable");
		
		$("#painelNfe-filtro-selectFornecedores").multiselect({
			selectedList : 6
		}).multiselect("checkAll");
		
		$("#selFornecedor", PainelMonitorNFE.workspace).click(function() {
			$(".menu_fornecedor", PainelMonitorNFE.workspace).show().fadeIn("fast");
		});

		$(".menu_fornecedor", PainelMonitorNFE.workspace).mouseleave(function() {
			$(".menu_fornecedor", PainelMonitorNFE.workspace).hide();
		});
		
	},
	
	pesquisar: function() {
		
		var box = $("#box", PainelMonitorNFE.workspace).val();
		var dataInicial = $("#dataInicial", PainelMonitorNFE.workspace).val();
		var dataFinal = $("#dataFinal", PainelMonitorNFE.workspace).val();
		var documentoPessoa = $('input:radio[name=painelNfe-radioTipoDoc]:checked', PainelMonitorNFE.workspace).val();
		var documento = $("#documento", PainelMonitorNFE.workspace).val();
		var tipoNfe = $("#painelNfe-filtro-naturezaOperacao", PainelMonitorNFE.workspace).val();
		var numeroInicial = $("#numeroInicial", PainelMonitorNFE.workspace).val();
		var numeroFinal = $("#numeroFinal", PainelMonitorNFE.workspace).val();
		var chaveAcesso = $("#chaveAcesso", PainelMonitorNFE.workspace).val();
		var situacaoNfe = $("#situacaoNfe", PainelMonitorNFE.workspace).val();
		var serieNfe	= $("#serieNfe", PainelMonitorNFE.workspace).val();	
		var nrDocumento	= $("#painelNfe-documento", PainelMonitorNFE.workspace).val();
		
		var params = [
		        {name:'filtro.box', value: box },
		        {name:'filtro.dataInicial', value: dataInicial },
		        {name:'filtro.dataFinal', value: dataFinal },
		        {name:'filtro.documentoPessoa', value: documentoPessoa },
		        {name:'filtro.documento', value: documento },
		        {name:'filtro.tipoNfe', value: tipoNfe },
		        {name:'filtro.numeroNotaInicial', value: numeroInicial },
		        {name:'filtro.numeroNotaFinal', value: numeroFinal },
		        {name:'filtro.chaveAcesso', value: chaveAcesso },
		        {name:'filtro.situacaoNfe', value: situacaoNfe },
		        {name:'filtro.serie',    value: serieNfe},
		        {name:'filtro.numeroDocumento', value: nrDocumento}
		];
		
		$("#nfeGrid", PainelMonitorNFE.workspace).flexOptions({
			url: this.path + 'pesquisar', params: params
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
			
			var colunaSelecao = linha.find("td")[9];
			
			$(colunaSelecao).find("div").find('input[name="checkgroup"]', PainelMonitorNFE.workspace).attr("checked",true);
			
		});
	},
	
	limparAll: function (){
		
		var linhasDaGrid = $("#nfeGrid tr", PainelMonitorNFE.workspace);
		
		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);

			var colunaSelecao = linha.find("td")[9];
			
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

	exportar : function() {
		document.cookie="username=John Doe; expires=Thu, 18 Dec 2013 12:00:00 GMT; path=/";
		var box = $("#box", PainelMonitorNFE.workspace).val();
		var dataInicial = $("#dataInicial", PainelMonitorNFE.workspace).val();
		var dataFinal = $("#dataFinal", PainelMonitorNFE.workspace).val();
		var documentoPessoa = $('input:radio[name=painelNfe-radioTipoDoc]:checked', PainelMonitorNFE.workspace).val();
		var documento = $("#documento", PainelMonitorNFE.workspace).val();
		var tipoNfe = $("#painelNfe-filtro-naturezaOperacao", PainelMonitorNFE.workspace).val();
		var numeroInicial = $("#numeroInicial", PainelMonitorNFE.workspace).val();
		var numeroFinal = $("#numeroFinal", PainelMonitorNFE.workspace).val();
		var chaveAcesso = $("#chaveAcesso", PainelMonitorNFE.workspace).val();
		var situacaoNfe = $("#situacaoNfe", PainelMonitorNFE.workspace).val();
		var serieNfe	= $("#serieNfe", PainelMonitorNFE.workspace).val();	
		var nrDocumento	= $("#painelNfe-documento", PainelMonitorNFE.workspace).val();
		
		var params = [
		        {name:'filtro.box', value: box },
		        {name:'filtro.dataInicial', value: dataInicial },
		        {name:'filtro.dataFinal', value: dataFinal },
		        {name:'filtro.documentoPessoa', value: documentoPessoa },
		        {name:'filtro.documento', value: documento },
		        {name:'filtro.tipoNfe', value: tipoNfe },
		        {name:'filtro.numeroNotaInicial', value: numeroInicial },
		        {name:'filtro.numeroNotaFinal', value: numeroFinal },
		        {name:'filtro.chaveAcesso', value: chaveAcesso },
		        {name:'filtro.situacaoNfe', value: situacaoNfe },
		        {name:'filtro.serie',    value: serieNfe},
		        {name:'filtro.numeroDocumento', value: nrDocumento}
		];
		
		var preparingFileModal = $("#preparing-file-modal").dialog({ modal: true });
		
		$.fileDownload(this.path+"exportar?fileType=XLS", {
			data: params, 
		    httpMethod: "GET",
		    successCallback: function (url) {
		    	console.log('success');
		    	$("#preparing-file-modal").dialog('close');
		    },
		    failCallback: function (responseHtml, url) {
		        preparingFileModal.dialog('close');
		        $("#error-modal").dialog({ modal: true });
		    }
		    
		});
		
		$("#preparing-file-modal").dialog('close');
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
	
	verificarRadioCnpjCpf : function() {
		
         $('input[id=painelNfe-documento]').unmask();//Remove a mascara
         if($("input:radio[name='painelNfe-radioTipoDoc']:checked").val()=="cpf"){//Acaso seja CPF
                $("input[id=painelNfe-documento]").mask("999.999.999-99");
                $("#painelNfe-documento").text('Digite o CPF:');
         } else {//Acaso seja Cnpj
                $("input[id=painelNfe-documento]").mask("99.999.999/9999-99");
                $("#painelNfe-documento").text('Digite o CNPJ:');
         }
		
	},
	
	verificarTipoDestinatario : function(element) {
		if(element.value != "FORNECEDOR") {
			$("#painelNfe-filtro-selectFornecedoresDestinatarios option:selected").removeAttr("selected");
			$("#painelNfe-filtro-selectFornecedoresDestinatarios").multiselect("disable");
		} else {
			$("#painelNfe-filtro-selectFornecedoresDestinatarios").multiselect("enable");
		}
		
		var emitente = '';
		if(element.value == 'COTA') {
			emitente = 'DISTRIBUIDOR';
		} else if(element.value == 'DISTRIBUIDOR') {
			emitente = 'COTA';
		} else if(element.value == 'FORNECEDOR') {
			emitente = 'DISTRIBUIDOR';
		}
		
		params = [];
		params.push({name: 'tipoEmitente', value: emitente});
		params.push({name: 'tipoDestinatario', value: element.value});
		
		$.postJSON(contextPath + '/administracao/naturezaOperacao/obterNaturezasOperacoesPorEmitenteDestinatario', params, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}
			
			$("#painelNfe-filtro-naturezaOperacao").empty();
			
			$('#painelNfe-filtro-naturezaOperacao').append($('<option>', { 
		        value: '-1',
		        text : 'Selecione...'
		    }));
			
			$.each(data.rows, function (i, row) {
			    $('#painelNfe-filtro-naturezaOperacao').append($('<option>', { 
			        value: row.cell.key,
			        text : row.cell.value
			    }));
			});
			
		});
		
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
				display : 'CNPJ / CPF Remetente',
				name : 'cnpjRemetente',
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
				width : 220,
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
	},
	
}, BaseController);
//@ sourceURL=painelMonitorNFE.js