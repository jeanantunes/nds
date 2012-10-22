var emissaoBandeiraController = $.extend(true, {
	
	init : function() {
		this.iniciarGrid();
		$("#senama", this.workspace).numeric();
				
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
		
		document.location.assign(contextPath + "/devolucao/emissaoBandeira/imprimirBandeira");
	}

	
}, BaseController);


//@ sourceURL=alteracaoCota.js
