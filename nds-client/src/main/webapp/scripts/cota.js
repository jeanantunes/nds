function Cota(workspace) {

	var cota = this;
	
	this.workspace = workspace;
		
	//Pesquisa por número da cota
	this.pesquisarPorNumeroCota = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		var numeroCota = $(idCampoNumeroCota, cota.workspace).val();

		numeroCota = $.trim(numeroCota);
		
		$(idCampoNomeCota, cota.workspace).val("");
		
		if (numeroCota && numeroCota.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNumero",
				"numeroCota=" + numeroCota,
				function(result) { 
					cota.pesquisarPorNumeroSuccessCallBack(result, idCampoNomeCota, successCallBack); 
				},
				function() {
					cota.pesquisarPorNumeroErrorCallBack(idCampoNumeroCota, errorCallBack); 
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

		cota.pesquisaRealizada = true;
		
		$(idCampoNomeCota, cota.workspace).val(result.nome);
		
		if (successCallBack) {
			
			successCallBack();
		}
	},
	
	//Error callback para pesquisa por número da cota
	this.pesquisarPorNumeroErrorCallBack = function(idCampoNumeroCota, errorCallBack) {
		
		$(idCampoNumeroCota, cota.workspace).val("");
		
		$(idCampoNumeroCota, cota.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	//Busca dados para o auto complete do nome da cota
	this.autoCompletarPorNome = function(idCampoNomeCota, isFromModal) {
		
		cota.pesquisaRealizada = false;
		
		var nomeCota = $(idCampoNomeCota, cota.workspace).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNomeCota, cota.workspace).autocomplete({source: ""});
		
		if (nomeCota && nomeCota.length > 2) {
			
			$.postJSON(
				contextPath + "/cadastro/cota/autoCompletarPorNome", "nomeCota=" + nomeCota,
				function(result) { 
					cota.exibirAutoComplete(result, idCampoNomeCota); 
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
		
		$(idCampoNomeCota, cota.workspace).autocomplete({
			source: result,
			focus : function(event, ui) {
				cota.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				cota.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				cota.descricaoAtribuida = true;
			},
			minLength: 4,
			delay : 0,
		});
	},
	
	//Pesquisar por nome da cota
	this.pesquisarPorNomeCota = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		setTimeout(function() { clearInterval(cota.intervalo); }, 10 * 1000);
		
		cota.intervalo = setInterval(function() {
			
			if (cota.descricaoAtribuida) {
				
				if (cota.pesquisaRealizada) {
					
					clearInterval(cota.intervalo);
					
					return;
				}
				
				cota.pesquisarPorNomeCotaAposIntervalo(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack);
			}
			
		}, 100);
	},
	
	//Pesquisa por nome da cota após o intervalo
	this.pesquisarPorNomeCotaAposIntervalo = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(cota.intervalo);
		
		cota.pesquisaRealizada = true;
		
		var nomeCota = $(idCampoNomeCota, cota.workspace).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNumeroCota, cota.workspace).val("");
		
		if (nomeCota && nomeCota.length > 0) {
			$.postJSON(
				contextPath + "/cadastro/cota/pesquisarPorNome", 
				"nomeCota=" + nomeCota,
				function(result) {
					cota.pesquisarPorNomeSuccessCallBack(result, idCampoNumeroCota, idCampoNomeCota, successCallBack);
				},
				function() {
					cota.pesquisarPorNomeErrorCallBack(idCampoNomeCota, errorCallBack);
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
			
			$(idCampoNumeroCota, cota.workspace).val(result.numero);
			$(idCampoNomeCota, cota.workspace).val(result.nome);
			
			if (successCallBack) {
				
				successCallBack();
			}
		}
	},
	
	//Error callback para pesquisa por nome da cota
	this.pesquisarPorNomeErrorCallBack = function(idCampoNomeCota, errorCallBack) {
		
		$(idCampoNomeCota, cota.workspace).val("");
		
		$(idCampoNomeCota, cota.workspace).focus();
		
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