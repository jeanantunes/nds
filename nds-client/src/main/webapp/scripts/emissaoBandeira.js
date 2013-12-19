var anoSemanaPesquisa;
var emissaoBandeiraController = $.extend(true, {
	
	init : function() {
		this.iniciarGrid();
		$("#semanaPesquisa", this.workspace).numeric();
		$("#numeroPallets", this.workspace).numeric();
		
		$("#dataEnvio", this.workspac ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$("#dataEnvio", this.workspace).mask("99/99/9999");
		
		$(".bt_arq",this.workspace).hide();
				
	},
	
	initBandeiraManual : function() {
		$("#dataEnvioManual", emissaoBandeiraController.workspace).mask("99/99/9999");
	},
	
	
	iniciarGrid : function() {
		
		
		$(".bandeirasRcltoGrid", this.workspace).flexigrid({
			dataType : 'json',
			preProcess: emissaoBandeiraController.executarPreProcessamento,
			colModel : [ {
				display : 'C&oacute;digo',
				name : 'codProduto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 590,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edi&ccedil;&atilde;o',
				name : 'edProduto',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Pacote Padr&atilde;o',
				name : 'pctPadrao',
				width : 140,
				sortable : true,
				align : 'right'
			}],
			sortname : "codProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});

	},
	
	pesquisar : function() {
		
		emissaoBandeiraController.anoSemanaPesquisa = $("#semanaPesquisa", this.workspace).val();
		emissaoBandeiraController.fornecedor = $("#fornecedor", this.workspace).val();
		
		$(".bandeirasRcltoGrid", this.workspace).flexOptions({
			url: contextPath + "/devolucao/emissaoBandeira/pesquisar",
			params: [{name:'anoSemana', value:emissaoBandeiraController.anoSemanaPesquisa},
			         {name:'fornecedor', value:emissaoBandeiraController.fornecedor}] 
		   ,
			newp: 1
		});
		
		$(".bandeirasRcltoGrid", this.workspace).flexReload();
	},
	
	executarPreProcessamento : function(resultado) {
		var tipoMensagem = null; 
		var listaMensagens = null;
		if (resultado.mensagens){
			tipoMensagem = resultado.mensagens.tipoMensagem;
        	listaMensagens = resultado.mensagens.listaMensagens;
		}	
        if (tipoMensagem && listaMensagens) {
              exibirMensagem(tipoMensagem, listaMensagens);
              $(".grids", emissaoBandeiraController.workspace).hide();
              $(".bt_arq", emissaoBandeiraController.workspace).hide();
         } else { 
        	 $(".grids", emissaoBandeiraController.workspace).show();
        	 $(".bt_arq", emissaoBandeiraController.workspace).show();
         } 	 
		
		return resultado;
	},
	
	imprimirArquivo : function(fileType) {
		
		window.location = contextPath + "/devolucao/emissaoBandeira/imprimirArquivo?"
			+ "anoSemana=" + emissaoBandeiraController.anoSemanaPesquisa
			+ "&fornecedor=" + emissaoBandeiraController.fornecedor
			+ "&sortname=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).flexGetSortName()
			+ "&sortorder=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).getSortOrder()
			+ "&rp=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).flexGetRowsPerPage()
			+ "&page=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).flexGetPageNumber()
			+ "&fileType=" + fileType;

		return false;
	},
	
	imprimirBandeira:function(){
		
		var _this = this;
		
		$("#dialog-pallets", _this.workspace).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					var	nrPallets = $.trim($("#numeroPallets", _this.workspace).val()),
						dtEnvio = $("#dataEnvio", _this.workspace).val();
					
					if (!nrPallets || !dtEnvio){
						
						exibirMensagem('WARNING', ['Número de Pallets e Data de Envio são obrigatórios.']);
						return;
					}
					
					$( this ).dialog( "close" );
					window.location = contextPath + "/devolucao/emissaoBandeira/imprimirBandeira?anoSemana=" + 
					emissaoBandeiraController.anoSemanaPesquisa+ 
					"&fornecedor=" + emissaoBandeiraController.fornecedor+
					"&numeroPallets=" + nrPallets +
					"&dataEnvio=" + dtEnvio;
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-pallets", _this.workspace).parents("form")
		});
		
		return false;
	},
	
	bandeiraManual : function() {
		$('#workspace').tabs('addTab', "Bandeira Manual", contextPath + "/devolucao/emissaoBandeira/bandeiraManual");
		
	},
	imprimirBandeiraManual:function(){
		
		var _this = this;
		$( "#dialog-pallets-bandeira-manual", _this.workspace).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					var semana = $.trim( $("#semana", _this.workspace).val()),
					nrPallets = $.trim( $("#numeroPalletsBandeiraManual", _this.workspace).val()),
					titulo = escape($.trim( $("#titulo", _this.workspace).val())),
					praca = $.trim( $("#praca", _this.workspace).val()),
					dtEnvio = $.trim($("#dataEnvioManual", _this.workspace).val()),
					forncedor = $.trim($("#inputfornecedor", _this.workspace).val()),
					canal = $.trim($("#canal", _this.workspace).val());
					
					if (!semana || !nrPallets || !titulo || !praca || !dtEnvio || !forncedor || !canal){
						
						exibirMensagem('WARNING', ['Todos os campos são obrigatórios.']);
						return;
					}
					
					$( this ).dialog( "close" );
					window.location = contextPath + "/devolucao/emissaoBandeira/imprimirBandeiraManual?"+
					"anoSemana=" + semana
					+"&numeroPallets=" + nrPallets
					+"&titulo=" + titulo
					+"&praca=" + praca
					+"&dataEnvio=" + dtEnvio
					+"&fornecedor=" + forncedor
					+"&canal=" + canal;
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-pallets-bandeira-manual", this.workspace).parents("form")
		});
		return false;

	},
		
}, BaseController);


//@ sourceURL=emissaoBandeira.js
