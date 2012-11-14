var PESSOA = {
		//Busca dados para o auto complete do nome da cota
		autoCompletarPorNome : function(idCampoNome, isFromModal) {
			
			var nomePessoa = $(idCampoNome).val();
			
			nomePessoa = $.trim(nomePessoa);
			
			$(idCampoNome).autocomplete({source: ""});
			
			if (nomePessoa && nomePessoa.length > 2) {
				
				$.postJSON(
					contextPath + "/pessoa/autoCompletarPorNome", {nomePessoa:nomePessoa},
					function(result) { 
						PESSOA.exibirAutoComplete(result, idCampoNome,""); 
					},
					null, 
					isFromModal
				);
			}
		},
		
		//Exibe o auto complete no campo
		exibirAutoComplete : function(result, idCampoNome,workspace) {
			
			$(idCampoNome,workspace).autocomplete({
				source: result,
				minLength: 4,
				delay : 0,
			});
		},
		
		autoCompletarPorNomeFiador : function(idCampoNome, workspace) {
			
			var nomeFiador = $(idCampoNome,workspace).val();
			
			nomeFiador = $.trim(nomeFiador);
			
			$(idCampoNome,workspace).autocomplete({source: ""});
			
			if (nomeFiador && nomeFiador.length > 2) {
				
				$.postJSON(
					contextPath + "/pessoa/autoCompletarPorNomeFiador", {nomeFiador:nomeFiador},
					function(result) { 
						PESSOA.exibirAutoComplete(result, idCampoNome,workspace); 
					},
					null
				);
			}
		},
		
		autoCompletarPorNomeEntregador : function(idCampoNome, workspace) {
			
			var nomeEntregador = $(idCampoNome,workspace).val();
			
			nomeEntregador = $.trim(nomeEntregador);
			
			$(idCampoNome,workspace).autocomplete({source: ""});
			
			if (nomeEntregador && nomeEntregador.length > 2) {
				
				$.postJSON(
					contextPath + "/pessoa/autoCompletarPorNomeEntregador", {nomeEntregador:nomeEntregador},
					function(result) { 
						PESSOA.exibirAutoComplete(result, idCampoNome,workspace); 
					},
					null
				);
			}
		},
		
		autoCompletarPorApelidoEntregador : function(idCampoNome, workspace) {
			
			var apelidoEntregador = $(idCampoNome,workspace).val();
			
			apelidoEntregador = $.trim(apelidoEntregador);
			
			$(idCampoNome,workspace).autocomplete({source: ""});
			
			if (apelidoEntregador && apelidoEntregador.length > 2) {
				
				$.postJSON(
					contextPath + "/pessoa/autoCompletarPorApelidoEntregador", {apelidoEntregador:apelidoEntregador},
					function(result) { 
						PESSOA.exibirAutoComplete(result, idCampoNome,workspace); 
					},
					null
				);
			}
		},
		
		autoCompletarPorNomeTransportador : function(idCampoNome, workspace) {
			
			var nomeEntregador = $(idCampoNome,workspace).val();
			
			nomeEntregador = $.trim(nomeEntregador);
			
			$(idCampoNome,workspace).autocomplete({source: ""});
			
			if (nomeEntregador && nomeEntregador.length > 2) {
				
				$.postJSON(
					contextPath + "/pessoa/autoCompletarPorNomeTransportador", {nomeTransportador:nomeTransportador},
					function(result) { 
						PESSOA.exibirAutoComplete(result, idCampoNome,workspace); 
					},
					null
				);
			}
		},
		
		autoCompletarPorNomeFantasiaTransportador : function(idCampoNome, workspace) {
			
			var apelidoEntregador = $(idCampoNome,workspace).val();
			
			apelidoEntregador = $.trim(apelidoEntregador);
			
			$(idCampoNome,workspace).autocomplete({source: ""});
			
			if (apelidoEntregador && apelidoEntregador.length > 2) {
				
				$.postJSON(
					contextPath + "/pessoa/autoCompletarPorNomeFantasiaTransportador", {nomeFantasia:nomeFantasia},
					function(result) { 
						PESSOA.exibirAutoComplete(result, idCampoNome,workspace); 
					},
					null
				);
			}
		},
		
		autoCompletarPorNomeFornecedor: function(idCampoNome, workspace) {
			
			var nomeEntregador = $(idCampoNome,workspace).val();
			
			nomeEntregador = $.trim(nomeEntregador);
			
			$(idCampoNome,workspace).autocomplete({source: ""});
			
			if (nomeEntregador && nomeEntregador.length > 2) {
				
				$.postJSON(
					contextPath + "/pessoa/autoCompletarPorNomeTransportador", {nomeTransportador:nomeTransportador},
					function(result) { 
						PESSOA.exibirAutoComplete(result, idCampoNome,workspace); 
					},
					null
				);
			}
		},
		
		autoCompletarPorNomeFantasiaFornecedor : function(idCampoNome, workspace) {
			
			var nomeFantasiaFornecedor = $(idCampoNome,workspace).val();
			
			nomeFantasiaFornecedor = $.trim(nomeFantasiaFornecedor);
			
			$(idCampoNome,workspace).autocomplete({source: ""});
			
			if (nomeFantasiaFornecedor && nomeFantasiaFornecedor.length > 2) {
				
				$.postJSON(
					contextPath + "/pessoa/autoCompletarPorNomeFantasiaFornecedor", {nomeFantasia:nomeFantasiaFornecedor},
					function(result) { 
						PESSOA.exibirAutoComplete(result, idCampoNome,workspace); 
					},
					null
				);
			}
		},
		
		autoCompletarPorNomeFornecedor : function(idCampoNome, workspace) {
			
			var nomeFornecedor = $(idCampoNome,workspace).val();
			
			nomeFornecedor = $.trim(nomeFornecedor);
			
			$(idCampoNome,workspace).autocomplete({source: ""});
			
			if (nomeFornecedor && nomeFornecedor.length > 2) {
				
				$.postJSON(
					contextPath + "/pessoa/autoCompletarPorNomeFornecedor", {nomeFornecedor:nomeFornecedor},
					function(result) { 
						PESSOA.exibirAutoComplete(result, idCampoNome,workspace); 
					},
					null
				);
			}
		}
};

//@ sourceURL=pessoa.js