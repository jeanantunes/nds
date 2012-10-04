function PesquisaCota(workspace) {

	var pesquisaCota = this;
	
	this.workspace = workspace;
		
	//Pesquisa por número da cota
	this.pesquisarPorNumeroCota = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		var numeroCota = $(idCampoNumeroCota, pesquisaCota.workspace).val();

		numeroCota = $.trim(numeroCota);
		
		$(idCampoNomeCota, pesquisaCota.workspace).val("");
		
		if (numeroCota && numeroCota.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNumero",
				"numeroCota=" + numeroCota,
				function(result) { 
					pesquisaCota.pesquisarPorNumeroSuccessCallBack(result, idCampoNomeCota, successCallBack); 
				},
				function() {
					pesquisaCota.pesquisarPorNumeroErrorCallBack(idCampoNumeroCota, errorCallBack); 
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

		pesquisaCota.pesquisaRealizada = true;
		
		$(idCampoNomeCota, pesquisaCota.workspace).val(result.nome);
		
		if (successCallBack) {
			
			successCallBack();
		}
	},
	
	//Error callback para pesquisa por número da cota
	this.pesquisarPorNumeroErrorCallBack = function(idCampoNumeroCota, errorCallBack) {
		
		$(idCampoNumeroCota, pesquisaCota.workspace).val("");
		
		$(idCampoNumeroCota, pesquisaCota.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	//Busca dados para o auto complete do nome da cota
	this.autoCompletarPorNome = function(idCampoNomeCota, isFromModal) {
		
		pesquisaCota.pesquisaRealizada = false;
		
		var nomeCota = $(idCampoNomeCota, pesquisaCota.workspace).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNomeCota, pesquisaCota.workspace).autocomplete({source: ""});
		
		if (nomeCota && nomeCota.length > 2) {
			
			$.postJSON(
				contextPath + "/cadastro/cota/autoCompletarPorNome", "nomeCota=" + nomeCota,
				function(result) { 
					pesquisaCota.exibirAutoComplete(result, idCampoNomeCota); 
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
		
		$(idCampoNomeCota, pesquisaCota.workspace).autocomplete({
			source: result,
			focus : function(event, ui) {
				pesquisaCota.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				pesquisaCota.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				pesquisaCota.descricaoAtribuida = true;
			},
			minLength: 4,
			delay : 0,
		});
	},
	
	//Pesquisar por nome da cota
	this.pesquisarPorNomeCota = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		setTimeout(function() { clearInterval(pesquisaCota.intervalo); }, 10 * 1000);
		
		pesquisaCota.intervalo = $().interval(function() {
			
			if (pesquisaCota.descricaoAtribuida) {
				
				if (pesquisaCota.pesquisaRealizada) {
					
					clearInterval(pesquisaCota.intervalo);
					
					return;
				}
				
				pesquisaCota.pesquisarPorNomeCotaAposIntervalo(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack);
			}
			
		}, 100);
	},
	
	//Pesquisa por nome da cota após o intervalo
	this.pesquisarPorNomeCotaAposIntervalo = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(pesquisaCota.intervalo);
		
		pesquisaCota.pesquisaRealizada = true;
		
		var nomeCota = $(idCampoNomeCota, pesquisaCota.workspace).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNumeroCota, pesquisaCota.workspace).val("");
		
		if (nomeCota && nomeCota.length > 0) {
			$.postJSON(
				contextPath + "/cadastro/cota/pesquisarPorNome", 
				"nomeCota=" + nomeCota,
				function(result) {
					pesquisaCota.pesquisarPorNomeSuccessCallBack(result, idCampoNumeroCota, idCampoNomeCota, successCallBack);
				},
				function() {
					pesquisaCota.pesquisarPorNomeErrorCallBack(idCampoNomeCota, errorCallBack);
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
			
			$(idCampoNumeroCota, pesquisaCota.workspace).val(result.numero);
			$(idCampoNomeCota, pesquisaCota.workspace).val(result.nome);
			
			if (successCallBack) {
				debugger;
				successCallBack();
			}
		}
	},
	
	//Error callback para pesquisa por nome da cota
	this.pesquisarPorNomeErrorCallBack = function(idCampoNomeCota, errorCallBack) {
		
		$(idCampoNomeCota, pesquisaCota.workspace).val("");
		
		$(idCampoNomeCota, pesquisaCota.workspace).focus();
		
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