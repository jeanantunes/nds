var PesquisaConferenciaEncalhe = $.extend(true, {
	
	autoCompletarCodigoDeBarras : function(result) {

		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({
			
			source: result,
			
			select: function(event, ui){			
				
				var idProdutoEdicao = ui.item.chave.$;	
				
				$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({
					source: []
				});

				var data = [{name: "produtoEdicaoId", value: idProdutoEdicao}, 
				            {name: "qtdExemplares", value: $("#qtdeExemplar", ConferenciaEncalhe.workspace).val()}];
				
			},
			delay : 0,
		});	
		
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete("search", codBarra);
		
	},
	
	adicionarProdutoConferido : function(data){
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/adicionarProdutoConferido', data,
			
			function(result){
				
				ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);	
				
                ConferenciaEncalhe.limparDadosProduto(true);
				
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
				
				$("#qtdeExemplar", ConferenciaEncalhe.workspace).val(1);
			},
			function(){
				
				ConferenciaEncalhe.limparDadosProduto(true);
				
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
				
				$("#qtdeExemplar", ConferenciaEncalhe.workspace).val(1);
				
			});
		
		ConferenciaEncalhe.numeroCotaEditavel(false);
	},
	
	pesquisarPorCodigoDeBarras: function() {

		var codBarra = $("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val().trim();
		
		var data = 
			[{name: 'numeroCota', value: $("#numeroCota", ConferenciaEncalhe.workspace).val()}, 
			 {name: 'codigoBarra', value: codBarra}];

		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/pesquisarPorCodigoDeBarras", data,
			
			function(result){
				
			    if (result.produtosEdicao){				
			    	
			    	PesquisaConferenciaEncalhe.autoCompletarCodigoDeBarras(result.produtosEdicao);
					
				} else{
			    	
        		    $("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({
						source: []
					});
        		    
					ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);
        		    
			    }    
			}, 
			
			tratarErroEncalhePorCodigoBarras);
	},
	
	tratarErroEncalhePorCodigoBarras : function() {
		
		$("#qtdeExemplar", ConferenciaEncalhe.workspace).val("1");
		
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({
			source: []
		});
			
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val("");
		
		focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
		
	}
	
}, BaseController);
