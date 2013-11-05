var anoSemanaPesquisa;
var emissaoBandeiraController = $.extend(true, {
	
	init : function() {
		this.iniciarGrid();
		$("#semanaPesquisa", this.workspace).numeric();
		$("#numeroPallets", this.workspace).numeric();
		
		$(".areaBts",this.workspace).hide();
				
	},
	
	initBandeiraManual : function() {
		$("#semanaBandeiraManual").numeric();
				
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
		
		emissaoBandeiraController.anoSemanaPesquisa = $("#semanaPesquisa", this.workspace).val;
		
		$(".bandeirasRcltoGrid", this.workspace).flexOptions({
			url: contextPath + "/devolucao/emissaoBandeira/pesquisar",
			params: [{name:'anoSemana', value:emissaoBandeiraController.anoSemanaPesquisa}] 
		   ,
			newp: 1
		});
	
		
		$(".bandeirasRcltoGrid", this.workspace).flexReload();
				
		$(".areaBts",this.workspace).show();
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
         } else { 
        	 $(".grids", emissaoBandeiraController.workspace).show();
         } 	 
		
		return resultado;
	},
	
	imprimirArquivo : function(fileType) {
		
		window.location = contextPath + "/devolucao/emissaoBandeira/imprimirArquivo?"
			+ "anoSemana=" + emissaoBandeiraController.anoSemanaPesquisa
			+ "&sortname=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).flexGetSortName()
			+ "&sortorder=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).getSortOrder()
			+ "&rp=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).flexGetRowsPerPage()
			+ "&page=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).flexGetPageNumber()
			+ "&fileType=" + fileType;

		return false;
	},
	
	imprimirBandeira:function(){
		
		$( "#dialog-pallets", this.workspace).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					window.location = contextPath + "/devolucao/emissaoBandeira/imprimirBandeira?semana=" + emissaoBandeiraController.anoSemanaPesquisa+ "&numeroPallets=" + $.trim( $("#numeroPallets").val());
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-pallets", this.workspace).parents("form")
		});
		
		
		
		return false;

	},
	
	bandeiraManual : function() {
		$('#workspace').tabs('addTab', "Bandeira Manual", contextPath + "/devolucao/emissaoBandeira/bandeiraManual");
		
	},
	imprimirBandeiraManual:function(){
	
		
		$( "#dialog-pallets-bandeira-manual", this.workspace).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					window.location = contextPath + "/devolucao/emissaoBandeira/imprimirBandeiraManual?"+
					"anoSemana=" + $.trim( $("#semanaBandeiraManual").val())
					+ "&numeroPallets=" + $.trim( $("#numeroPalletsBandeiraManual").val())
					+"&nome="+$.trim( $("#tipoOperacaoBandeiraManual").val())
					+"&codigoPracaNoProdin="+$.trim( $("#codigoPracaProconBandeiraManual").val())
					+"&praca="+$.trim( $("#pracaBandeiraManual").val())
					+"&destino="+$.trim( $("#destinoBandeiraManual").val())
					+"&canal="+$.trim( $("#canalBandeiraManual").val());

					
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
