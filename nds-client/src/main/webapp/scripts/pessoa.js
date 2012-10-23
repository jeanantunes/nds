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
						PESSOA.exibirAutoComplete(result, idCampoNome); 
					},
					null, 
					isFromModal
				);
			}
		},
		
		//Exibe o auto complete no campo
		exibirAutoComplete : function(result, idCampoNome) {
			
			$(idCampoNome).autocomplete({
				source: result,
				minLength: 4,
				delay : 0,
			});
		},
};