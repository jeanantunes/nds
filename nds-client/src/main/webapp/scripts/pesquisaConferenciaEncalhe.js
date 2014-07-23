var PesquisaConferenciaEncalhe = {
    
	autorizarVendaNegativa : function(idProdutoEdicao, qtdExemplares){
		
		var params = [
		    {name: "produtoEdicaoId", value: idProdutoEdicao}, 
		    {name: "qtdExemplares", value: qtdExemplares},          
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
		
	abrirDialogAutenticacaoSupervisor : function(idProdutoEdicao, qtdExemplares, msgErroSupervisor) {
		
		$("#msgSupervisor", ConferenciaEncalhe.workspace).text(msgErroSupervisor);
		
		$("#dialog-autenticar-supervisor", ConferenciaEncalhe.workspace).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
				
			close: function() {
				setTimeout (function () {
					$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).select();
				}, 1);
			},
			
			buttons: {
				
				"Ok": function() {
					
					PesquisaConferenciaEncalhe.autorizarVendaNegativa(idProdutoEdicao, qtdExemplares);
					
				},
				
				"Cancelar": function() {
					
					$(this).dialog("close");
				}
			},
			
			form: $("#dialog-autenticar-supervisor", this.workspace).parents("form")
			
		});		
		
	},
		
    bindkeypressCodigoBarras : function(){
    	
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).keypress(function(e) {
			
			if (e.keyCode == 13) {
				
				PesquisaConferenciaEncalhe.pesquisarPorCodigoDeBarras();
			}
			
		});
	},
    
	autoCompletarCodigoDeBarras : function(result, codBarra, qtdExemplares) {
        
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({
			
			source: result,
			
			autoFocus: true,
			
			select: function(event, ui){			
				
				var idProdutoEdicao = ui.item.chave;	
				
				$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({
					source: []
				});

				var data = [{name: "produtoEdicaoId", value: idProdutoEdicao}, 
				            {name: "qtdExemplares", value: qtdExemplares}];
				
                PesquisaConferenciaEncalhe.adicionarProdutoConferido(data);
                
                ConferenciaEncalhe.limparDadosProduto(true);
				
			},
			
			close: function() {
				
				ConferenciaEncalhe.limparDadosProduto(true);
				
				$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete('option', 'source', []);
			},
			
			delay : 0,
		});	
		
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete("search", codBarra);
		
	},
	
	adicionarProdutoConferido : function(data){
        
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/adicionarProdutoEdicaoConferidoDiretamente', data,
			
			function(result){
			
				if(result.msgErroSupervisor){
					
					PesquisaConferenciaEncalhe.abrirDialogAutenticacaoSupervisor(result.idProdutoEdicao, result.qtdExemplares, result.msgErroSupervisor);
					
					return;
					
				} else if(result.msgInformativa) {
				
					exibirMensagem('WARNING', [result.msgInformativa]);
				
				}
			
				ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result, true);	
				
			});
		
		ConferenciaEncalhe.numeroCotaEditavel(false);
	},
	
	pesquisarPorCodigoDeBarras: function() {

		var codBarra = $("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val().trim();
        
		var qtdExemplares = $("#qtdeExemplar", ConferenciaEncalhe.workspace).val();
		
		ConferenciaEncalhe.limparDadosProduto(true);
		
		var data = [{name: "codigoBarra", value: codBarra}, 
				            {name: "qtdExemplares", value: qtdExemplares}];
        
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/encalharProdutoEdicaoPorCodigoDeBarras", data,
			
			function(result){
				
			    if (result.produtosEdicao){				
			    	
			    	PesquisaConferenciaEncalhe.autoCompletarCodigoDeBarras(result.produtosEdicao, codBarra, result.qtdExemplares);
					
				} else if(result.msgErroSupervisor){

					PesquisaConferenciaEncalhe.abrirDialogAutenticacaoSupervisor(result.idProdutoEdicao, result.qtdExemplares, result.msgErroSupervisor);
					
				} else {
					
        		    $("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({
						source: []
					});
        		    
					ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result, true);

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
		
	}
	
};

//@ sourceURL=pesquisaConferenciaEncalhe.js
