function PesquisaEditor(workspace) {

	var pesquisaEditor = this;
	
	this.workspace = workspace;
		
	//Pesquisa por número do editor
	this.pesquisarPorNumeroEditor = function(idCampoNumeroEditor, idCampoNomeEditor, isFromModal, successCallBack, errorCallBack,checkValidateResult) {
		
		var numeroEditor = $(idCampoNumeroEditor, pesquisaEditor.workspace).val();

		numeroEditor = $.trim(numeroEditor);
		
		$(idCampoNomeEditor, pesquisaEditor.workspace).val("");
		
		if (numeroEditor && numeroEditor.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/editor/pesquisarPorNumero", { numeroEditor:numeroEditor },
				function(result) {
						
						if(checkValidateResult && pesquisaEditor.validateResult){
							var msgArray = pesquisaEditor.validateResult(result);
							if(msgArray && msgArray.length > 0){
								exibirMensagem("WARNING", msgArray);
							}
							
						}
					pesquisaEditor.pesquisarPorNumeroSuccessCallBack(result, idCampoNomeEditor, successCallBack); 
				},
				function() {
					pesquisaEditor.pesquisarPorNumeroErrorCallBack(idCampoNumeroEditor, errorCallBack); 
				}, 
				isFromModal
			);

		} else {
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	this.pesquisarNomeEditor = function(idCampoNomeEditor, idCampoNumeroEditor, isFromModal, successCallBack, errorCallBack) {
		
		var nomeEditor = $(idCampoNomeEditor, pesquisaEditor.workspace).val();

		nomeEditor = $.trim(nomeEditor);
		
		$(idCampoNumeroEditor, pesquisaEditor.workspace).val("");
		
		if (nomeEditor && nomeEditor.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/editor/pesquisarPorNome", {nomeEditor:nomeEditor},
				function(result) { 
					pesquisaEditor.pesquisarPorNomeSuccessCallBack(result, idCampoNumeroEditor, successCallBack); 
				},
				function() {
					pesquisaEditor.pesquisarPorNomeErrorCallBack(idCampoNomeEditor, errorCallBack); 
				}, 
				isFromModal
			);

		} else {
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Success callback para pesquisa por número do editor
	this.pesquisarPorNumeroSuccessCallBack = function(result, idCampoNomeEditor, successCallBack) {

		pesquisaEditor.pesquisaRealizada = true;
		
		$(idCampoNomeEditor, pesquisaEditor.workspace).val(result.nome);
		
		if (successCallBack) {
			
			successCallBack(result);
		}
	},
	
	
	//Error callback para pesquisa por número do editor
	this.pesquisarPorNumeroErrorCallBack = function(idCampoNumeroEditor, errorCallBack) {
		
		$(idCampoNumeroEditor, pesquisaEditor.workspace).val("");
		
		$(idCampoNumeroEditor, pesquisaEditor.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	
	//Busca dados para o auto complete do nome do editor
	this.autoCompletarPorNome = function(idCampoNomeEditor, isFromModal) {
		
		pesquisaEditor.pesquisaRealizada = false;
		
		var nomeEditor = $(idCampoNomeEditor, pesquisaEditor.workspace).val();
		
		nomeEditor = $.trim(nomeEditor);
		
		$(idCampoNomeEditor, pesquisaEditor.workspace).autocomplete({source: [""]});
		
		if (nomeEditor && nomeEditor.length > 2) {
			
			$.postJSON(
				contextPath + "/cadastro/editor/autoCompletarPorNome", {nomeEditor:nomeEditor},
				function(result) { 
					pesquisaEditor.exibirAutoComplete(result, idCampoNomeEditor, 3);
				},
				null, 
				isFromModal
			);
		}
	},
	
	this.descricaoAtribuida = true,
	
	this.pesquisaRealizada = false,
	
	this.numeroEditorSelecionado = null,
	
	this.intervalo = null,
	
	//Exibe o auto complete no campo
	this.exibirAutoComplete = function(result, idCampoEditor, tamanhoInicial) {
		
		$(idCampoEditor, pesquisaEditor.workspace).autocomplete({
			source: result,
			focus : function(event, ui) {
				pesquisaEditor.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				pesquisaEditor.descricaoAtribuida = true;
				
				if (ui.item){
					pesquisaEditor.numeroEditorSelecionado = ui.item.chave.numero;
				}
			},
			select : function(event, ui) {
				pesquisaEditor.descricaoAtribuida = true;
				pesquisaEditor.numeroEditorSelecionado = ui.item.chave.numero;
			},
			minLength: tamanhoInicial,
			delay : 0,
		});
	},
	
	//Pesquisar por nome do editor
	this.pesquisarPorNomeEditor = function(idCampoNumeroEditor, idCampoNomeEditor, isFromModal, successCallBack, errorCallBack,checkValidateResult) {
		
		setTimeout(function() { clearInterval(pesquisaEditor.intervalo); }, 10 * 1000);
		
		pesquisaEditor.intervalo = $().interval(function() {
			
			if (pesquisaEditor.descricaoAtribuida) {
				
				if (pesquisaEditor.pesquisaRealizada) {
					
					clearInterval(pesquisaEditor.intervalo);
					
					return;
				}
				
				pesquisaEditor.pesquisarPorNomeEditorAposIntervalo(idCampoNumeroEditor, idCampoNomeEditor, isFromModal, successCallBack, errorCallBack,checkValidateResult);
			}
			
		}, 100);
	},
	
	//Pesquisa por nome do editor após o intervalo
	this.pesquisarPorNomeEditorAposIntervalo = function(idCampoNumeroEditor, idCampoNomeEditor, isFromModal, successCallBack, errorCallBack,checkValidateResult) {
		
		clearInterval(pesquisaEditor.intervalo);
		
		var nomeEditor = $(idCampoNomeEditor, pesquisaEditor.workspace).val();
		
		nomeEditor = $.trim(nomeEditor);
		
		$(idCampoNumeroEditor, pesquisaEditor.workspace).val("");
		
		if (nomeEditor && nomeEditor.length > 0) {
			$.postJSON(
				contextPath + "/cadastro/editor/pesquisarPorNome", 
				{nomeEditor:nomeEditor},
				function(result) {

					if (result.length > 1){

						if (pesquisaEditor.numeroEditorSelecionado){
							
							pesquisaEditor.pesquisaRealizada = true;
							
							$(idCampoNumeroEditor, pesquisaEditor.workspace).val(pesquisaEditor.numeroEditorSelecionado);
						}
					}
					else if(result.numero){

						pesquisaEditor.pesquisaRealizada = true;
						
						$(idCampoNumeroEditor, pesquisaEditor.workspace).val(result.numero);
						
						if(checkValidateResult && pesquisaEditor.validateResult){
							var msgArray = pesquisaEditor.validateResult(result);
							if(msgArray && msgArray.length>0){
								exibirMensagem("WARNING", msgArray);
							}
							
						}
					}
					else{
					
					    pesquisaEditor.pesquisarPorNomeSuccessCallBack(result, idCampoNumeroEditor, idCampoNomeEditor, successCallBack);
					}
				},
				function() {
					pesquisaEditor.pesquisarPorNomeErrorCallBack(idCampoNomeEditor, errorCallBack);
				}, 
				isFromModal
			);
		} else {

			if (errorCallBack && typeof(errorCallBack) === 'function') {
				errorCallBack();
			}
		}
	},
	
	//Success callback para pesquisa por nome do editor
	this.pesquisarPorNomeSuccessCallBack = function(result, idCampoNumeroEditor, idCampoNomeEditor, successCallBack) {
		
		if (result != "") {
			
			$(idCampoNumeroEditor, pesquisaEditor.workspace).val(result.idEditor);
			$(idCampoNomeEditor, pesquisaEditor.workspace).val(result.nome);
			
			if (successCallBack) {
				successCallBack(result);
			}
		}
	},
	
	//Error callback para pesquisa por nome do editor
	this.pesquisarPorNomeErrorCallBack = function(idCampoNomeEditor, errorCallBack) {
		
		$(idCampoNomeEditor, pesquisaEditor.workspace).val("");
		
		$(idCampoNomeEditor, pesquisaEditor.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	//Obtém um editor pelo número
	this.obterPorNumeroEditor = function(numeroEditor, isFromModal, successCallback, errorCallBack) {
		
		if (numeroEditor && numeroEditor.length > 0) {

			$.postJSON(contextPath + "/cadastro/editor/pesquisarPorNumero",
				{numeroEditor:numeroEditor},
				function(result) { 
					successCallback(result); 
				},
				function() {
					errorCallBack();
				}, 
				isFromModal
			);
		}
	};
		
}
//@ sourceURL=pesquisaEditor.js