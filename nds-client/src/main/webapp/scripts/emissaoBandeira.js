var emissaoBandeiraController = $.extend(true, {
	
	init : function() {
		this.iniciarGrid();
		$("#senama", this.workspace).numeric();
		$("#numeroPallets", this.workspace).numeric();
				
	},
	
	iniciarGrid : function() {
		
		
		$(".bandeirasRcltoGrid", this.workspace).flexigrid({
			dataType : 'json',
			preProcess: emissaoBandeiraController.executarPreProcessamento,
			colModel : [ {
				display : 'C&oacute;digo',
				name : 'codigoProduto',
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
				name : 'edicao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Pacote Padr&atilde;o',
				name : 'pacote',
				width : 140,
				sortable : true,
				align : 'right'
			}],
			sortname : "codigoProduto",
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
		
		$(".bandeirasRcltoGrid", this.workspace).flexOptions({
			url: contextPath + "/devolucao/emissaoBandeira/pesquisar",
			params: {name:'semana', value:$("#semana", this.workspace).val()} 
		   ,
			newp: 1
		});
	
		
		$(".bandeirasRcltoGrid", this.workspace).flexReload();
		$(".grids", this.workspace).show();
				

	},
	
	executarPreProcessamento : function(resultado) {
		var tipoMensagem = resultado.mensagens.tipoMensagem;
        var listaMensagens = resultado.mensagens.listaMensagens;
        if (tipoMensagem && listaMensagens) {
              exibirMensagem(tipoMensagem, listaMensagens);
         } else { 
        	 $(".grids", emissaoBandeiraController.workspace).show();
         } 	 
		
		return resultado;
	},
	
	imprimirArquivo : function(fileType) {

		var semana = $("#semana", emissaoBandeiraController.workspace).val();
		
		window.location = contextPath + "/devolucao/emissaoBandeira/imprimirArquivo?"
			+ "semana=" + semana
			+ "&sortname=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).flexGetSortName()
			+ "&sortorder=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).getSortOrder()
			+ "&rp=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).flexGetRowsPerPage()
			+ "&page=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).flexGetPageNumber()
			+ "&fileType=" + fileType;

		return false;
	},
	
	imprimirBandeira:function(){
		
		var semana = $.trim($("#senama", this.workspace).val());
		var numeroPallets =$.trim( $("#numeroPallets").val());
		
		$( "#dialog-pallets", this.workspace).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					window.location = contextPath + "/devolucao/emissaoBandeira/imprimirBandeira?semana=" + semana+ "&numeroPallets=" + $.trim( $("#numeroPallets").val());
			

					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
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
					"semana=" + $.trim( $("#semanaBandeiraManual").val())
					+ "&numeroPallets=" + $.trim( $("#numeroPalletsBandeiraManual").val())
					+"&tipoOperacao="+$.trim( $("#tipoOperacaoBandeiraManual").val())
					+"&codigoPracaProcon="+$.trim( $("#codigoPracaProconBandeiraManual").val())
					+"&praca="+$.trim( $("#pracaBandeiraManual").val())
					+"&destino="+$.trim( $("#destinoBandeiraManual").val())
					+"&canal="+$.trim( $("#canalBandeiraManual").val());

					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
		return false;

	},
	
}, BaseController);


//@ sourceURL=emissaoBandeira.js
