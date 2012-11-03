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
		
		$("input[name='numCota']", impressaoBoletosController.workspace).numeric();
		
		$("#dataMovimento", impressaoBoletosController.workspace).focus();
		
		$("#impressosGrid", impressaoBoletosController.workspace).flexigrid({
			preProcess:impressaoBoletosController.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Box',
				name : 'box',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rota',
				name : 'rota',
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
			sortname : "rota,roteiro,numeroCota",
			sortorder : "asc",
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
			
			var linkImpressao = '<a href="javascript:;" onclick="impressaoBoletosController.imprimirDivida(' + nossoNumero + ');" style="cursor:pointer">' +
				 '<img src="' + contextPath + '/images/ico_impressora.gif" hspace="5" border="0px" title="Imprime" />' +
				 '</a>';			
			
			var linkEmail ='<a href="javascript:;" style="cursor:default; opacity:0.4; filter:alpha(opacity=40)">' +
            				'<img src="' + contextPath + '/images/ico_email.png" hspace="5" border="0px" title="Divida não tem suporte para Envio de Arquivo por E-Mail" />' +
            				'</a>';
			
			if(row.cell.suportaEmail == "true"){
			 
				linkEmail ='<a href="javascript:;" onclick="impressaoBoletosController.enviarDivida(' + nossoNumero + ');" style="cursor:pointer">' +
                 '<img src="' + contextPath + '/images/ico_email.png" hspace="5" border="0px" title="Enviar Arquivo por E-Mail" />' +
                  '</a>';		 					 
				
			}	 
			
             row.cell.acao = linkImpressao + linkEmail ; 
		});
		
		$("#grids", impressaoBoletosController.workspace).show();
			
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
			             {name:"box",value:$("#box", impressaoBoletosController.workspace).val()},
			             {name:"rota",value:$("#rota", impressaoBoletosController.workspace).val()},
			             {name:"roteiro",value:$("#roteiro", impressaoBoletosController.workspace).val()},
			             {name:"numCota",value:$("#numCota", impressaoBoletosController.workspace).val()},
			             {name:"tipoCobranca",value:$("#tipoCobranca", impressaoBoletosController.workspace).val()}
			            ];
		return formData;
	},
	/**
		Executa a pesquisa de dividas geradas
	**/
	pesquisar: function (){
		
		$("#impressosGrid", impressaoBoletosController.workspace).flexOptions({
			url: contextPath + "/financeiro/impressaoBoletos/consultar",
			params: impressaoBoletosController.formData(),newp: 1
		});
		
		$("#impressosGrid", impressaoBoletosController.workspace).flexReload();

	},
	
	/**
		Executa o m�todo de gera��o de dividas
	**/
	gerarDivida : function (){
		$("#aguarde", impressaoBoletosController.workspace).dialog({
			title: 'Processando',
			resizable: false,
			height:60,
			width:50,
			modal: true,
			open: function (){
					$(this).parent().children().children('.ui-dialog-titlebar-close').remove();
				  },
			form: $("#aguarde", this.workspace).parents("form")
		});
		
		$("#aguarde", impressaoBoletosController.workspace).show();
		
		$.postJSON(
			contextPath + '/financeiro/impressaoBoletos/gerarDivida',
			null,
			function(result) {
				$("#aguarde", impressaoBoletosController.workspace).dialog("close");
				mostrar();
			},
			function(result) {
				$("#aguarde", impressaoBoletosController.workspace).dialog("close");
			},
			false
		);
	},
	pesquisarCotaSuccessCallBack:function(){
		
		var data = {numeroCota: $("#numCota", impressaoBoletosController.workspace).val()};
		
		$.postJSON(impressaoBoletosController.workspace + "/financeiro/impressaoBoletos/pesquisarInfoCota",
				   data, function(result){
			
			$("#box", impressaoBoletosController.workspace).val(result.box);
			$("#rota", impressaoBoletosController.workspace).val(result.rota);
			$("#roteiro", impressaoBoletosController.workspace).val(result.roteiro);
			$("#tipoCobranca", impressaoBoletosController.workspace).val(result.tipoCobranca);
		});
		//Efetuar a pesquisa de box, rota roteiro
	},
	pesquisarCotaErrorCallBack:function(){
		$("#box", impressaoBoletosController.workspace).val("");
		$("#rota", impressaoBoletosController.workspace).val("");
		$("#roteiro", impressaoBoletosController.workspace).val("");
		$("#tipoCobranca", impressaoBoletosController.workspace).val("");
		
		impressaoBoletosController.recarregarComboRoteiroRotas(null);
	},
	validarPesquisa:function(){
		
		var data = {dataMovimento:$("#dataMovimento", impressaoBoletosController.workspace).val()};
		
		$.postJSON(contextPath + "/financeiro/impressaoBoletos/validarPesquisaDivida",
				data, function(result){
			
			if(result == "false"){
				impressaoBoletosController.dialogPesquisaInvalida();
				$("#pesquisaInvalida", impressaoBoletosController.workspace).show();
				$("#grids", impressaoBoletosController.workspace).hide();
			}else{
				impressaoBoletosController.pesquisar();
			}	
		});
	},
	dialogPesquisaInvalida:function(){
		
		$("#pesquisaInvalida", impressaoBoletosController.workspace).dialog({
			title: 'Atenção',
			resizable: false,
			height:120,
			width:330,
			modal: true,
			buttons : {
				"Fechar" : function() {
					$(this).dialog("close");
				}
			},
			form: $("#pesquisaInvalida", this.workspace).parents("form")			
		});
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
		
		if("BOLETO" == result){
			window.open(contextPath + "/financeiro/impressaoBoletos/imprimirBoletosEmMassa",'page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=1,height=1');
		}
		else if ("DIVIDA" == result) {
			window.open(contextPath + "/financeiro/impressaoBoletos/imprimirDividasEmMassa",'page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=1,height=1');
		}
	},
	
	imprimirDivida:function(nossoNumero){
		
		$.postJSON(contextPath + "/financeiro/impressaoBoletos/validarImpressaoDivida",
				[{name:"nossoNumero",value:nossoNumero}], function(result){
			
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

		window.open(contextPath + "/financeiro/impressaoBoletos/imprimirDivida?nossoNumero="+ nossoNumero +"'",'page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=1,height=1');

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

	habilitarAcaoGeracaoDivida:function(valor){
		
		$.postJSON(contextPath + "/financeiro/impressaoBoletos/habilitarAcaoGeracaoDivida",
				[{name:"dataPesquisa",value:valor}], function(result){
			
			if(result.isAcaoGeraDivida == true){
				$("#divGerarDivida", impressaoBoletosController.workspace).show();	
			}
			else{
				$("#divGerarDivida", impressaoBoletosController.workspace).hide();
			}
		});
	}

}, BaseController);
