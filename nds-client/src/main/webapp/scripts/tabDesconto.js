var descontoController = $.extend(true,
    {
	
	    initDescontos : function(numCota){
	    	descontoController.initDescontoCota();
	    	descontoController.initDescontoProduto();
	    	descontoController.obterDescontoCota(numCota); 
	    	descontoController.obterDescontoProduto(numCota);
	    	
	    },
	
	    initDescontoProduto : function(){
		    $(".descProdutosGrid", this.workspace).flexigrid({
		    	preProcess: descontoController.getDataFromResult,
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigoProduto',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 350,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'nomeProduto',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : '% Desconto',
					name : 'desconto',
					width : 115,
					sortable : true,
					align : 'right'
				}, {
					display : 'Data da Alteração',
					name : 'dataAlteracao',
					width : 120,
					sortable : true,
					align : 'center'
				}],
				sortname : "dataAlteracao",
				sortorder : "asc",
				width : 810,
				height : 150
			});
	    },	
		
	    initDescontoCota : function(){
			$(".descCotaGrid", this.workspace).flexigrid({
				preProcess: descontoController.getDataFromResult,
				dataType : 'json',
				colModel : [ {
					display : 'Fornecedor',
					name : 'fornecedor',
					width : 440,
					sortable : true,
					align : 'left'
				}, {
					display : '% Desconto',
					name : 'desconto',
					width : 80,
					sortable : true,
					align : 'right'
				}, {
					display : 'Tipo',
					name : 'descTipoDesconto',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Última Atualização',
					name : 'dataAlteracao',
					width : 100,
					sortable : true,
					align : 'center'
				}],
				sortname : "dataAlteracao",
				sortorder : "asc",
				width : 810,
				height : 150
			});
	    },
	    
        obterDescontoProduto : function(numCota){
        	
        	$(".descProdutosGrid", this.workspace).flexOptions({
				url: contextPath+'/cadastro/cota/obterTiposDescontoProduto',
				params: [
				         {name:'numCota', value:numCota}
				        ] ,
				        newp: 1
			});
			
			$(".descProdutosGrid", this.workspace).flexReload();
			
			$(".grids", this.workspace).show();
	    },
	    
	    obterDescontoCota : function(numCota){
	    	
			$(".descCotaGrid", this.workspace).flexOptions({
				url: contextPath+'/cadastro/cota/obterTiposDescontoCota',
				params: [
				         {name:'numCota', value:numCota}
				        ] ,
				        newp: 1
			});
			
			$(".descCotaGrid", this.workspace).flexReload();
			
			$(".grids", this.workspace).show();
	    },

	    
        getDataFromResult : function(resultado){
			
			//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
			if (resultado.mensagens) {
				exibirMensagemDialog(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids", this.workspace).hide();
				return resultado;
			}	
				
			$(".grids", this.workspace).show();
			
			return resultado;
		}
		 
    }
	,
	BaseController
);
