function PesquisaEntregador(workspace) {

	var T = this;
	
	this.workspace = workspace;
		
	//Pesquisa por número da cota
	this.pesquisarPorNumeroCota = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		var numeroCota = $(idCampoNumeroCota, T.workspace).val();

		numeroCota = $.trim(numeroCota);
		
		$(idCampoNomeCota, T.workspace).val("");
		
		if (numeroCota && numeroCota.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNumero",
				"numeroCota=" + numeroCota,
				function(result) { 
					T.pesquisarPorNumeroSuccessCallBack(result, idCampoNomeCota, successCallBack); 
				},
				function() {
					T.pesquisarPorNumeroErrorCallBack(idCampoNumeroCota, errorCallBack); 
				}, 
				isFromModal
			);

		} else {
			
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Success callback para pesquisa por número da cota
	this.pesquisarPorNumeroSuccessCallBack = function(result, idCampoNomeCota, successCallBack) {

		T.pesquisaRealizada = true;
		
		$(idCampoNomeCota, T.workspace).val(result.nome);
		
		if (successCallBack) {
			
			successCallBack();
		}
	},
	
	//Error callback para pesquisa por número da cota
	this.pesquisarPorNumeroErrorCallBack = function(idCampoNumeroCota, errorCallBack) {
		
		$(idCampoNumeroCota, T.workspace).val("");
		
		$(idCampoNumeroCota, T.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	//Busca dados para o auto complete do nome da cota
	this.autoCompletarPorNome = function(idCampoNomeCota, isFromModal) {
		
		T.pesquisaRealizada = false;
		
		var nomeCota = $(idCampoNomeCota, T.workspace).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNomeCota, T.workspace).autocomplete({source: ""});
		
		if (nomeCota && nomeCota.length > 2) {
			
			$.postJSON(
				contextPath + "/cadastro/cota/autoCompletarPorNome", "nomeCota=" + nomeCota,
				function(result) { 
					T.exibirAutoComplete(result, idCampoNomeCota); 
				},
				null, 
				isFromModal
			);
		}
	},
	
	this.descricaoAtribuida = true,
	
	this.pesquisaRealizada = false,
	
	this.intervalo = null,
	
	//Exibe o auto complete no campo
	this.exibirAutoComplete = function(result, idCampoNomeCota) {
		
		$(idCampoNomeCota, T.workspace).autocomplete({
			source: result,
			focus : function(event, ui) {
				T.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				T.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				T.descricaoAtribuida = true;
			},
			minLength: 4,
			delay : 0,
		});
	},
	
	//Pesquisar por nome da cota
	this.pesquisarPorNomeCota = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		setTimeout(function() { clearInterval(T.intervalo); }, 10 * 1000);
		
		T.intervalo = $().interval(function() {
			
			if (T.descricaoAtribuida) {
				
				if (T.pesquisaRealizada) {
					
					clearInterval(T.intervalo);
					
					return;
				}
				
				T.pesquisarPorNomeCotaAposIntervalo(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack);
			}
			
		}, 100);
	},
	
	//Pesquisa por nome da cota após o intervalo
	this.pesquisarPorNomeCotaAposIntervalo = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(T.intervalo);
		
		T.pesquisaRealizada = true;
		
		var nomeCota = $(idCampoNomeCota, T.workspace).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNumeroCota, T.workspace).val("");
		
		if (nomeCota && nomeCota.length > 0) {
			$.postJSON(
				contextPath + "/cadastro/cota/pesquisarPorNome", 
				"nomeCota=" + nomeCota,
				function(result) {
					T.pesquisarPorNomeSuccessCallBack(result, idCampoNumeroCota, idCampoNomeCota, successCallBack);
				},
				function() {
					T.pesquisarPorNomeErrorCallBack(idCampoNomeCota, errorCallBack);
				}, 
				isFromModal
			);
		} else {
			
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Success callback para pesquisa por nome da cota
	this.pesquisarPorNomeSuccessCallBack = function(result, idCampoNumeroCota, idCampoNomeCota, successCallBack) {
		
		if (result != "") {
			
			$(idCampoNumeroCota, T.workspace).val(result.numero);
			$(idCampoNomeCota, T.workspace).val(result.nome);
			
			if (successCallBack) {
				
				successCallBack();
			}
		}
	},
	
	//Error callback para pesquisa por nome da cota
	this.pesquisarPorNomeErrorCallBack = function(idCampoNomeCota, errorCallBack) {
		
		$(idCampoNomeCota, T.workspace).val("");
		
		$(idCampoNomeCota, T.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	//Obtém uma cota pelo número
	this.obterPorNumeroCota = function(numeroCota, isFromModal, successCallback, errorCallBack) {
		
		if (numeroCota && numeroCota.length > 0) {

			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNumero",
				"numeroCota=" + numeroCota,
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