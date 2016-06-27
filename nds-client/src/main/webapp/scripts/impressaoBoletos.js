var impressaoBoletosController = $.extend(true, {
	
	init : function() {
		$( "#dataMovimento", impressaoBoletosController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$("#descricaoCota", impressaoBoletosController.workspace).autocomplete({source: ""});
		
		$('input[id^="data"]', impressaoBoletosController.workspace).mask("99/99/9999");
		
		$("input[name='impressao-boleto-numCota']", impressaoBoletosController.workspace).numeric();
		
		$("#dataMovimento", impressaoBoletosController.workspace).focus();
		
	    $("#impressao-dialog-banco").dialog({
			autoOpen : false,
			resizable : false,
			width : 400,
			modal : true,
			buttons : {
				"Confirmar	" : function() {
					impressaoBoletosController.downloadArquivo();					
				},
				"Cancelar" : function (){
					$("#impressao-dialog-banco").dialog("close");
				}
			}
		});
		
		$("#impressosGrid", impressaoBoletosController.workspace).flexigrid({
			onSuccess: function() {bloquearItensEdicao(impressaoBoletosController.workspace);},
			preProcess:impressaoBoletosController.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Box',
				name : 'box',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Roteiro',
				name : 'roteiro',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'Rota',
				name : 'rota',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Cota',
				name : 'numeroCota',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Vencimento',
				name : 'dataVencimento',
				width : 75,
				sortable : true,
				align : 'center'
			}, {
				display : 'Via',
				name : 'vias',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Emissão',
				name : 'dataEmissao',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor',
				name : 'valor',
				width : 60,
				sortable : true,
				align : 'right'
			},{
				display : 'Tipo',
				name : 'tipoCobranca',
				width : 100,
				sortable : true,
				align : 'left'
			},{
				display : 'Ação',
				name : 'acao',
				width : 70,
				sortable : false,
				align : 'center'
			}],
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
		
	},
	
	executarPreProcessamento : function (resultado){
		
		//Verifica mensagens de erro do retorno da chamada ao controller.
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$("#grids", impressaoBoletosController.workspace).hide();

			return resultado.tableModel;
		}
		
		// Monta as colunas com os inputs do grid
		$.each(resultado.rows, function(index, row) {
			
			var nossoNumero  = row.cell.nossoNumero;
			
			var linkImpressao = '<a isEdicao="true" href="javascript:;" onclick="impressaoBoletosController.imprimirDivida(\'' + nossoNumero + '\');" style="cursor:pointer">' +
				 '<img src="' + contextPath + '/images/ico_impressora.gif" hspace="5" border="0px" title="Imprime" />' +
				 '</a>';			
			
			var linkEmail ='<a isEdicao="true" href="javascript:;" style="cursor:default; opacity:0.4; filter:alpha(opacity=40)">' +
            				'<img src="' + contextPath + '/images/ico_email.png" hspace="5" border="0px" title="Divida não tem suporte para Envio de Arquivo por E-Mail" />' +
            				'</a>';
			
			if(row.cell.suportaEmail == "true"){
			 
				linkEmail ='<a isEdicao="true" href="javascript:;" onclick="impressaoBoletosController.enviarDivida(\'' + nossoNumero + '\');" style="cursor:pointer">' +
                 '<img src="' + contextPath + '/images/ico_email.png" hspace="5" border="0px" title="Enviar Arquivo por E-Mail" />' +
                  '</a>';		 					 
				
			}	 
			
             row.cell.acao = linkImpressao + linkEmail ; 
		});
		
		$("#grids", impressaoBoletosController.workspace).show();
			
		$("#divImpressoes").show();
		
		return resultado;
	},
	
	enviarDivida:function(nossoNumero) {
		var data = [{ name: 'nossoNumero', value: nossoNumero}];
		
		$.postJSON(contextPath + "/financeiro/impressaoBoletos/enviarDivida", data,function(){
			impressaoBoletosController.pesquisar();
		});
	},
	
	formData: function(){
		
		var formData = [ {name:"dataMovimento",value:$("#dataMovimento", impressaoBoletosController.workspace).val()},
			             {name:"box",value:$("#impressao-boleto-box", impressaoBoletosController.workspace).val()},
			             {name:"rota",value:$("#rota", impressaoBoletosController.workspace).val()},
			             {name:"roteiro",value:$("#roteiro", impressaoBoletosController.workspace).val()},
			             {name:"numCota",value:$("#impressao-boleto-numCota", impressaoBoletosController.workspace).val()},
			             {name:"tipoCobranca",value:$("#tipoCobranca", impressaoBoletosController.workspace).val()},
			             {name:"banco",value:$("#impressao-boleto-banco", impressaoBoletosController.workspace).val()}
			            ];
		return formData;
	},
	/**
		Executa a pesquisa de dividas geradas
	**/
	pesquisar: function (){
		
		$("#divImpressoes").hide();
		
		if( $("#impressao-boleto-numCota", impressaoBoletosController.workspace).val().length ) {
			$("#impressosGrid", impressaoBoletosController.workspace).flexOptions({
				url: contextPath + "/financeiro/impressaoBoletos/consultar",
				params: impressaoBoletosController.formData(),
				newp: 1,
				sortorder: "desc",
				sortname: "dataVencimento"
			});
		} else {
			$("#impressosGrid", impressaoBoletosController.workspace).flexOptions({
				url: contextPath + "/financeiro/impressaoBoletos/consultar",
				params: impressaoBoletosController.formData(),
				newp: 1,
				sortname : "numeroCota",
				sortorder : "asc",
			});
		}
		
		
		$("#impressosGrid", impressaoBoletosController.workspace).flexReload();

	},
	
	pesquisarCotaErrorCallBack:function(){
		$("#impressao-boleto-box", impressaoBoletosController.workspace).val("");
		$("#rota", impressaoBoletosController.workspace).val("");
		$("#roteiro", impressaoBoletosController.workspace).val("");
		$("#tipoCobranca", impressaoBoletosController.workspace).val("");
	},
	
	imprimirDividas:function(tipoImpressao){
		
		$.postJSON(contextPath + "/financeiro/impressaoBoletos/validarImpressaoDividas",
				[{name:"tipoImpressao",value:tipoImpressao}], function(result){
			
			$("#impressosGrid", impressaoBoletosController.workspace).flexOptions({
				url: contextPath + "/financeiro/impressaoBoletos/consultar",
				params: impressaoBoletosController.formData(),newp: 1,
				onSuccess:impressaoBoletosController.renderizarArquivos(result)
			});	
			
			$("#impressosGrid", impressaoBoletosController.workspace).flexReload();
		});
	},
	
	renderizarArquivos:function(result){
		
		var file = contextPath + '/financeiro/impressaoBoletos/';
		
		if("BOLETO" == result || "BOLETO_SLIP" == result){
			
			$('#download-iframe', impressaoBoletosController.workspace).attr('src', file + 'imprimirBoletosEmMassa');
		}
		else if ("DIVIDA" == result) {
			
			$('#download-iframe', impressaoBoletosController.workspace).attr('src', file + 'imprimirDividasEmMassa');
		}
	},
	
	imprimirDivida:function(nossoNumero){
		
		$.postJSON(contextPath + "/financeiro/impressaoBoletos/validarImpressaoDivida", [{name:"nossoNumero",value:nossoNumero}], function(result){
			
			if(result == "true"){
				
				$("#impressosGrid", impressaoBoletosController.workspace).flexOptions({
					url: contextPath + "/financeiro/impressaoBoletos/consultar",
					params: impressaoBoletosController.formData(),
					onSuccess:impressaoBoletosController.renderizarArquivo(nossoNumero)
				});
				
				$("#impressosGrid", impressaoBoletosController.workspace).flexReload();
			}	
		});
	},
	
	renderizarArquivo: function (nossoNumero){
		var file = contextPath + "/financeiro/impressaoBoletos/imprimirDivida?nossoNumero="+ nossoNumero;
		$('#download-iframe', impressaoBoletosController.workspace).attr('src', file);	
	},
	
	recarregarComboRotas:function(idRoteiro){
		
		$.postJSON(contextPath + "/financeiro/impressaoBoletos/recarregarListaRotas",
				[{name:"roteiro",value:idRoteiro}], function(result){
			
			var comboRotas =  montarComboBox(result, true);
			
			$("#rota", impressaoBoletosController.workspace).html(comboRotas);	
		});
	},
	
	recarregarComboRoteiroRotas:function(idBox){
		
		$.postJSON(contextPath + "/financeiro/impressaoBoletos/recarregarRoteiroRota",
				[{name:"idBox",value:idBox}], function(result){
			
			var comboRotas =  montarComboBoxCustomJson(result.rotas, true);
			var comboRoteiros = montarComboBoxCustomJson(result.roteiros, true);
			
			$("#rota", impressaoBoletosController.workspace).html(comboRotas);
			$("#roteiro", impressaoBoletosController.workspace).html(comboRoteiros);
		});
	},
	
	gerarArquivo : function () {
		$("#impressao-dialog-banco").dialog("open");
    },
    
    downloadArquivo : function () {
    	
    	var banco = $("#impressao-boleto-banco").val();
    	
    	if(banco == '-1') {
    		$("#impressao-dialog-banco").dialog("close");
    		
			exibirMensagem('WARNING', 'Favor selecionar um Banco');
			return false;
    	} else {
    		$("#impressao-dialog-banco").dialog("close");
    	}
    	
    	var path = contextPath + "/financeiro/impressaoBoletos/gerarArquivo";
    	
    	var params = [];
		
		params.push({name: 'filtro.dataMovimento',      	value: $("#dataMovimento", impressaoBoletosController.workspace).val()});
		params.push({name: 'filtro.codigoBox', 	value:$("#impressao-boleto-box", impressaoBoletosController.workspace).val()});
		params.push({name: 'filtro.idBanco', value: banco});
		
    	$.fileDownload(path, {
			httpMethod : "POST",
			data : params,
			successCallback: function(result) {
				if (result.mensagens) {
					exibirMensagem(
						result.mensagens.tipoMensagem, 
						result.mensagens.listaMensagens
					);
				}			
			},
            failCallback: function(result) {
	
        		res = $.parseJSON($(result).text());
        		if ((typeof res != "undefined") && (typeof res.mensagens != "undefined")) {
        			
					exibirMensagem(
							res.mensagens.tipoMensagem, 
							res.mensagens.listaMensagens
					);
				}	
			}
		});
    }
    
}, BaseController);
//@ sourceURL=impressaoBoletos.js