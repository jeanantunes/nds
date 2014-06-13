var PesquisaConferenciaEncalhe = {
    
	autorizarVendaNegativa : function(idProdutoEdicao){
		
		var params = [
		    {name: "produtoEdicaoId", value: idProdutoEdicao}, 
		    {name: "qtdExemplares", value: $("#qtdeExemplar", ConferenciaEncalhe.workspace).val()},          
			{name:"usuario", value:$("#inputUsuarioSup", ConferenciaEncalhe.workspace).val()},
			{name:"senha",value:$("#inputSenha", ConferenciaEncalhe.workspace).val()}
		];
		
		if (params.usuario == '' || params.senha == ''){
			
			exibirMensagem(
				'WARNING', 
				['Usuário e senha são obrigatórios.']
			);
			
			return;
		}
		
		$.postJSON(
			contextPath + "/devolucao/conferenciaEncalhe/autorizarVendaNegativa", 
			params,
			function(result) {
				
				$("#inputUsuarioSup", ConferenciaEncalhe.workspace).val("");
				$("#inputSenha", ConferenciaEncalhe.workspace).val("");
				
				if (result.msgErroSupervisor){
					exibirMensagem('WARNING', [result.msgErroSupervisor]);
					return;
				}
				
				$("#dialog-autenticar-supervisor", ConferenciaEncalhe.workspace).dialog("close");
				
			}
			
		);
	},	
		
	abrirDialogAutenticacaoSupervisor : function(idProdutoEdicao, msgErroSupervisor) {
		
		$("#msgSupervisor", ConferenciaEncalhe.workspace).text(msgErroSupervisor);
		
		$("#dialog-autenticar-supervisor", ConferenciaEncalhe.workspace).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				
				"Ok": function() {
					
					PesquisaConferenciaEncalhe.autorizarVendaNegativa(idProdutoEdicao);
					
				},
				
				"Cancelar": function() {
						
					ConferenciaEncalhe.limparDadosProduto(true);
					
					$(this).dialog("close");
				}
			},
			
			form: $("#dialog-autenticar-supervisor", this.workspace).parents("form"),
			
			close: function(){
					ConferenciaEncalhe.limparDadosProduto(true);
				
			},
			open: function(){
				focusSelectRefField($("#inputUsuarioSup", ConferenciaEncalhe.workspace));
			}
		});		
		
	},
		
    bindkeypressCodigoBarras : function(){
    	
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).keypress(function(e) {
			
			if (e.keyCode == 13) {
				
				PesquisaConferenciaEncalhe.pesquisarPorCodigoDeBarras();
			}
			
		});
	},
    
	autoCompletarCodigoDeBarras : function(result, codBarra) {
        
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({
			
			source: result,
			
			select: function(event, ui){			
				
				var idProdutoEdicao = ui.item.chave.$;	
				
				$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({
					source: []
				});

				var data = [{name: "produtoEdicaoId", value: idProdutoEdicao}, 
				            {name: "qtdExemplares", value: $("#qtdeExemplar", ConferenciaEncalhe.workspace).val()}];
                
                PesquisaConferenciaEncalhe.adicionarProdutoConferido(data);
				
			},
			delay : 0,
		});	
		
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete("search", codBarra);
		
	},
	
	adicionarProdutoConferido : function(data){
        
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/adicionarProdutoEdicaoConferidoDiretamente', data,
			
			function(result){
			
				if(result.msgErroSupervisor){
					
					PesquisaConferenciaEncalhe.abrirDialogAutenticacaoSupervisor(result.idProdutoEdicao, result.msgErroSupervisor);
					
					return;
					
				} else if(result.msgInformativa) {
				
					exibirMensagem('WARNING', [result.msgInformativa]);
				
				}
			
				ConferenciaEncalhe.limparDadosProduto(true);
			
				ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);	
				
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
				
			},
			function(){
				
				ConferenciaEncalhe.limparDadosProduto(true);
				
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
				
			});
		
		ConferenciaEncalhe.numeroCotaEditavel(false);
	},
	
	pesquisarPorCodigoDeBarras: function() {

		var codBarra = $("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val().trim();
		
        
        var data = [{name: "codigoBarra", value: codBarra}, 
				            {name: "qtdExemplares", value: $("#qtdeExemplar", ConferenciaEncalhe.workspace).val()}];

		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/encalharProdutoEdicaoPorCodigoDeBarras", data,
			
			function(result){
				
			    if (result.produtosEdicao){				
			    	
			    	PesquisaConferenciaEncalhe.autoCompletarCodigoDeBarras(result.produtosEdicao, codBarra);
					
				} else if(result.msgErroSupervisor){

					PesquisaConferenciaEncalhe.abrirDialogAutenticacaoSupervisor(result.idProdutoEdicao, result.msgErroSupervisor);
					
				} else {
					
        		    $("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({
						source: []
					});
        		    
        		    ConferenciaEncalhe.limparDadosProduto(true);
        		    
					ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);

					if(result.msgInformativa) {
						exibirMensagem('WARNING', [result.msgInformativa]);
					}

					
			    }    
			}, 
			
			PesquisaConferenciaEncalhe.tratarErroEncalhePorCodigoBarras);
	},
	
	tratarErroEncalhePorCodigoBarras : function() {
		
		$("#qtdeExemplar", ConferenciaEncalhe.workspace).val("1");
		
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({
			source: []
		});
			
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val("");
		
		focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
		
	}
	
};

//@ sourceURL=pesquisaConferenciaEncalhe.js
