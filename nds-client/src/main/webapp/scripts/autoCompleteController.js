function AutoCompleteController(workspace) {

	var autoComplete = this;
	
	this.workspace = workspace; 
	this.tamanhoInicial = 3;
	
	this.pesquisarPorCodigo = function(action, idCampoCodigo, idCampoNome, successCallBack, errorCallBack, isFromModal) {
		
		var codigo = $.trim($(idCampoCodigo, autoComplete.workspace).val());

		$(idCampoNome, autoComplete.workspace).val("");
		
		if (codigo && codigo.length > 0) {
			
			$.postJSON(contextPath + action,
					{codigo:codigo},
				function(result) { 
						
					autoComplete.pesquisarPorCodigoSuccessCallBack(result, idCampoNome, successCallBack); 
				},
				function() {
					
					autoComplete.pesquisarPorCodigoErrorCallBack(idCampoCodigo, errorCallBack); 
				}, 
				
				isFromModal
			);

		} else {
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	this.pesquisarPorCodigoSuccessCallBack = function(result, idCampoNome, successCallBack) {

		if (result.nome != undefined) {
			$(idCampoNome, autoComplete.workspace).val(result.nome);
		}
		
		if (successCallBack) {
			
			successCallBack(result);
		}
	},
	
	//Error callback para pesquisa por número da cota
	this.pesquisarPorCodigoErrorCallBack = function(idCampoCodigo, errorCallBack) {
		
		$(idCampoCodigo, autoComplete.workspace).val("");
		$(idCampoCodigo, autoComplete.workspace).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},
	
	this.autoCompletar = function(action, idCampoCodigo, idCampoNome, disparoDaPesquisa, successCallBack, isFromModal) {
			
			if (!disparoDaPesquisa) {
				disparoDaPesquisa = 2;
			}
		
			var nome = $(idCampoNome, autoComplete.workspace).val();
			
			nome = $.trim(nome);
			
			$(idCampoNome, autoComplete.workspace).autocomplete({source: [""]});
			
			if (nome && nome.length > disparoDaPesquisa) {
				
				$.postJSON(
					contextPath + action, {nome:nome},
					function(result) { 
						autoComplete.exibirAutoComplete(result, idCampoCodigo, idCampoNome, successCallBack);
					},
					null, 
					isFromModal
				);
			}
	},
	
	//Exibe o auto complete no campo
	this.exibirAutoComplete = function(result, idCampoCodigo, idCampoNome, successCallBack) {
		
		$(idCampoNome, autoComplete.workspace).autocomplete({
			source: result,
			
			select : function(event, ui) {
				
				$(idCampoCodigo, autoComplete.workspace).val(ui.item.chave.numero);
				$(idCampoNome, autoComplete.workspace).val(ui.item.chave.label);
				
				if (successCallBack) {
					
					successCallBack(ui.item);
				}
			},
			minLength: autoComplete.tamanhoInicial,
			delay : 0,
		});
	};
	
	
	//Success callback para pesquisa por nome da cota
	this.pesquisarPorNomeSuccessCallBack = function(result) {
		
		if (result != "") {
			
			$(autoComplete.idCampoCodigo, autoComplete.workspace).val(result.numero);
			$(autoComplete.idCampoNome,   autoComplete.workspace).val(result.nome);
			
			if (autoComplete.successCallBack) {
				autoComplete.successCallBack(result);
			}
		}
	},
	
	//Error callback para pesquisa por nome da cota
	this.pesquisarPorNomeErrorCallBack = function() {
		
		$(autoComplete.idCampoNome, autoComplete.workspace).val("");
		$(autoComplete.idCampoNome, autoComplete.workspace).focus();
		
		if (autoComplete.errorCallBack) {
			
			autoComplete.errorCallBack();
		}
	},
	
	this.limparCampoOnChange = function(campo, camposLimpar) {
		
		$(campo, autoComplete.workspace).change(function (event){

			if ($(event.target).val() == '') {
				
				for (var i=0;i < camposLimpar.length; i++) {
					
					$(camposLimpar[i], autoComplete.workspace).val("");
				}
				
			}
			
		});
	};
}
//@ sourceURL=autoCompleteController.js