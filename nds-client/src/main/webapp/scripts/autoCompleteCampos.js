function AutoCompleteController(workspace) {

	var autoComplete = this;
	
	this.workspace = workspace; 
	this.tamanhoInicial = 3;
	
	//Pesquisar por código
	this.pesquisarPorCodigo = function(action, idCampoCodigo, idCampoNome, successCallBack, errorCallBack, isFromModal) {
		
		var codigo = $.trim($(idCampoCodigo, autoComplete.workspace).val());

		$(idCampoNome, autoComplete.workspace).val("");
		
		if (codigo && codigo.length > 0) {
			
			$.postJSON(contextPath + action,
					{codigoProduto:codigo},
				function(result) { 
						
					autoComplete.pesquisarPorCodigoSuccessCallBack(result, idCampoNome, idCampoCodigo, successCallBack); 
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
	
	
	//Pesquisar por nome
	this.pesquisarPorNome = function(action, idCampoCodigo, idCampoNome, successCallBack, errorCallBack, isFromModal) {
		
		var nome = $.trim($(idCampoNome, autoComplete.workspace).val());

		$(idCampoNome, autoComplete.workspace).val("");
		
		if (nome && nome.length > 0) {
			
			$.postJSON(contextPath + action,
					{nomeProduto:nome},
				function(result) { 
						
					autoComplete.pesquisarPorCodigoSuccessCallBack(result, idCampoNome, idCampoCodigo, successCallBack); 
				},
				function() {
					
					autoComplete.pesquisarPorCodigoErrorCallBack(idCampoNome, errorCallBack); 
				}, 
				
				isFromModal
			);

		} else {
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	
	this.autoCompletarPorNome = function(action, idCampoCodigo, idCampoNome, isFromModal, disparoDaPesquisa) {
		
		if(!disparoDaPesquisa){
			disparoDaPesquisa = 2;
		}
		
		var nome = $(idCampoNome, autoComplete.workspace).val();
		
		nome = $.trim(nome);
		
		$(idCampoNome, autoComplete.workspace).autocomplete({source: [""]});
		
		if (nome && nome.length > disparoDaPesquisa) {
			
			$.postJSON(
				contextPath + action, {nomeProduto:nome},
				function(result) { 
					autoComplete.exibirAutoCompletePorNome(result, idCampoCodigo, idCampoNome);
				},
				null, 
				isFromModal
			);
		}
},
	
	this.autoCompletarPorCodigo = function(action, idCampoCodigo, idCampoNome, isFromModal, disparoDaPesquisa) {
	
	if(!disparoDaPesquisa){
		disparoDaPesquisa = 2;
	}
	
	var cod = $(idCampoCodigo, autoComplete.workspace).val();
	
	cod = $.trim(cod);
	
	$(idCampoCodigo, autoComplete.workspace).autocomplete({source: [""]});
	
	if (cod && cod.length > disparoDaPesquisa) {
		
		$.postJSON(
			contextPath + action, {codigoProduto:cod},
			function(result) { 
				autoComplete.exibirAutoCompletePorCodigo(result, idCampoCodigo, idCampoNome);
			},
			null, 
			isFromModal
		);
	}
},


	//Exibe o auto complete no campo
	this.exibirAutoCompletePorCodigo = function(result, idCampoCodigo, idCampoNome) {
		
			$(idCampoCodigo, autoComplete.workspace).autocomplete({
				source: result,
				
				select : function(event, ui) {
					$(idCampoCodigo, autoComplete.workspace).val(ui.item.chave.numero);
					$(idCampoNome, autoComplete.workspace).val(ui.item.chave.label);
				},
					
				focus : function(event, ui) {
					$(idCampoCodigo, autoComplete.workspace).val(ui.item.chave.numero);
					$(idCampoNome, autoComplete.workspace).val(ui.item.chave.label);
				},
				close : function(event, ui) {
					$(idCampoCodigo, autoComplete.workspace).val(ui.item.chave.numero);
					$(idCampoNome, autoComplete.workspace).val(ui.item.chave.label);
				},

				minLength: autoComplete.tamanhoInicial,
				delay : 0,
			});
	};
	
	//Exibe o auto complete no campo
	this.exibirAutoCompletePorNome = function(result, idCampoCodigo, idCampoNome) {
	
			$(idCampoNome, autoComplete.workspace).autocomplete({
				source: result,
				
				select : function(event, ui) {
					$(idCampoCodigo, autoComplete.workspace).val(ui.item.chave.numero);
					$(idCampoNome, autoComplete.workspace).val(ui.item.chave.label);
				},
				focus : function(event, ui) {
					$(idCampoCodigo, autoComplete.workspace).val(ui.item.chave.numero);
					$(idCampoNome, autoComplete.workspace).val(ui.item.chave.label);
				},
				close : function(event, ui) {
					$(idCampoCodigo, autoComplete.workspace).val(ui.item.chave.numero);
					$(idCampoNome, autoComplete.workspace).val(ui.item.chave.label);
				},
				minLength: autoComplete.tamanhoInicial,
				delay : 0,
			});
	};
	
	this.pesquisarPorCodigoSuccessCallBack = function(result, idCampoNome, idCampoCodigo, successCallBack) {
		
		$(idCampoNome, autoComplete.workspace).val(result.nome);
		$(idCampoCodigo, autoComplete.workspace).val(result.codigo);
		
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
	
	this.limparCampoOnChange = function(campo, campoLimpar, workspace) {
		
		$(campo, workspace).change(function (event){

			if ($(event.target).val() == '') {
				$(campoLimpar, workspace).val("");
			}
			
		});
	};
}
//@ sourceURL=autoCompleteController.js