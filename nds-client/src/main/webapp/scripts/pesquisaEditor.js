function PesquisaEditor(workspace) {

	var pesquisaEditor = this;
	
	this.workspace = workspace;
		
	//Pesquisa por número do editor
	this.pesquisarPorCodigoEditor = function(idCampocodigoEditor, idCampoNomeEditor, isFromModal, successCallBack, errorCallBack,checkValidateResult) {
		
		var codigoEditor = $(idCampocodigoEditor, pesquisaEditor.workspace).val();

		codigoEditor = $.trim(codigoEditor);
		
		$(idCampoNomeEditor, pesquisaEditor.workspace).val("");
		
		if (codigoEditor && codigoEditor.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/editor/pesquisarPorCodigo", { codigoEditor:codigoEditor },
				function(result) {
						
						if(checkValidateResult && pesquisaEditor.validateResult){
							var msgArray = pesquisaEditor.validateResult(result);
							if(msgArray && msgArray.length > 0){
								exibirMensagem("WARNING", msgArray);
							}
							
						}
					pesquisaEditor.pesquisarPorCodigoSuccessCallBack(result, idCampoNomeEditor, successCallBack); 
				},
				function() {
					pesquisaEditor.pesquisarPorCodigoErrorCallBack(idCampocodigoEditor, errorCallBack); 
				}, 
				isFromModal
			);

		} else {
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	this.pesquisarNomeEditor = function(idCampoNomeEditor, idCampocodigoEditor, isFromModal, successCallBack, errorCallBack) {
		
		var nomeEditor = $(idCampoNomeEditor, pesquisaEditor.workspace).val();

		nomeEditor = $.trim(nomeEditor);
		
		$(idCampocodigoEditor, pesquisaEditor.workspace).val("");
		
		if (nomeEditor && nomeEditor.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/editor/pesquisarPorNome", {nomeEditor:nomeEditor},
				function(result) { 
					pesquisaEditor.pesquisarPorNomeSuccessCallBack(result, idCampocodigoEditor, successCallBack); 
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
	this.pesquisarPorCodigoSuccessCallBack = function(result, idCampoNomeEditor, successCallBack) {

		pesquisaEditor.pesquisaRealizada = true;
		
		$(idCampoNomeEditor, pesquisaEditor.workspace).val(result.nome);
		
		if (successCallBack) {
			
			successCallBack(result);
		}
	},
	
	
	//Error callback para pesquisa por número do editor
	this.pesquisarPorCodigoErrorCallBack = function(idCampocodigoEditor, errorCallBack) {
		
		$(idCampocodigoEditor, pesquisaEditor.workspace).val("");
		
		$(idCampocodigoEditor, pesquisaEditor.workspace).focus();
		
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
	
	this.codigoEditorSelecionado = null,
	
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
					pesquisaEditor.codigoEditorSelecionado = ui.item.chave.codigo;
				}
			},
			select : function(event, ui) {
				pesquisaEditor.descricaoAtribuida = true;
				pesquisaEditor.codigoEditorSelecionado = ui.item.chave.codigo;
			},
			minLength: tamanhoInicial,
			delay : 0,
		});
	},
	
	//Pesquisar por nome do editor
	this.pesquisarPorNomeEditor = function(idCampocodigoEditor, idCampoNomeEditor, isFromModal, successCallBack, errorCallBack,checkValidateResult) {
		
		setTimeout(function() { clearInterval(pesquisaEditor.intervalo); }, 10 * 1000);
		
		pesquisaEditor.intervalo = $().interval(function() {
			
			if (pesquisaEditor.descricaoAtribuida) {
				
				if (pesquisaEditor.pesquisaRealizada) {
					
					clearInterval(pesquisaEditor.intervalo);
					
					return;
				}
				
				pesquisaEditor.pesquisarPorNomeEditorAposIntervalo(idCampocodigoEditor, idCampoNomeEditor, isFromModal, successCallBack, errorCallBack, checkValidateResult);
			}
			
		}, 100);
	},
	
	//Pesquisa por nome do editor após o intervalo
	this.pesquisarPorNomeEditorAposIntervalo = function(idCampocodigoEditor, idCampoNomeEditor, isFromModal, successCallBack, errorCallBack,checkValidateResult) {
		
		clearInterval(pesquisaEditor.intervalo);
		
		var nomeEditor = $(idCampoNomeEditor, pesquisaEditor.workspace).val();
		
		nomeEditor = $.trim(nomeEditor);
		
		$(idCampocodigoEditor, pesquisaEditor.workspace).val("");
		
		if (nomeEditor && nomeEditor.length > 0) {
			$.postJSON(
				contextPath + "/cadastro/editor/pesquisarPorNome", 
				{nomeEditor:nomeEditor},
				function(result) {

					if (result.length > 1){

						if (pesquisaEditor.codigoEditorSelecionado){
							
							pesquisaEditor.pesquisaRealizada = true;
							
							$(idCampocodigoEditor, pesquisaEditor.workspace).val(pesquisaEditor.codigoEditorSelecionado);
						}
					}
					else if(result.numero){

						pesquisaEditor.pesquisaRealizada = true;
						
						$(idCampocodigoEditor, pesquisaEditor.workspace).val(result.numero);
						
						if(checkValidateResult && pesquisaEditor.validateResult){
							var msgArray = pesquisaEditor.validateResult(result);
							if(msgArray && msgArray.length>0){
								exibirMensagem("WARNING", msgArray);
							}
							
						}
					}
					else{
					
					    pesquisaEditor.pesquisarPorNomeSuccessCallBack(result, idCampocodigoEditor, idCampoNomeEditor, successCallBack);
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
	this.pesquisarPorNomeSuccessCallBack = function(result, idCampocodigoEditor, idCampoNomeEditor, successCallBack) {
		
		if (result != "") {
			
			$(idCampocodigoEditor, pesquisaEditor.workspace).val(result.codigo);
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
	this.obterPorcodigoEditor = function(codigoEditor, isFromModal, successCallback, errorCallBack) {
		
		if (codigoEditor && codigoEditor.length > 0) {

			$.postJSON(contextPath + "/cadastro/editor/pesquisarPorCodigo",
				{codigoEditor:codigoEditor},
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