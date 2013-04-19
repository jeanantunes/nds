function ConsultaCota(workspace) {

	var consultaCota = this;
	var numeroCotaSelecionada = null;
	
	this.workspace = workspace;
		
	//Pesquisa por número da cota
	this.pesquisarPorNumeroCota = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		var numeroCota = $(idCampoNumeroCota, consultaCota.workspace).val();

		numeroCota = $.trim(numeroCota);
		
		$(idCampoNomeCota, consultaCota.workspace).val("");
		
		if (numeroCota && numeroCota.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNumero",
					{numeroCota:numeroCota},
				function(result) { 
					consultaCota.pesquisarPorNumeroSuccessCallBack(result, idCampoNomeCota, successCallBack); 
				},
				function() {
					consultaCota.pesquisarPorNumeroErrorCallBack(idCampoNumeroCota, errorCallBack); 
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

		$(idCampoNomeCota, consultaCota.workspace).val(result.nome);
		
		if (successCallBack) {
			
			successCallBack(result);
		}
	},
	
	
	//Error callback para pesquisa por número da cota
	this.pesquisarPorNumeroErrorCallBack = function(idCampoNumeroCota, errorCallBack) {
		
		$(idCampoNumeroCota, consultaCota.workspace).val("");
		
		$(idCampoNumeroCota, consultaCota.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	
	//Busca dados para o auto complete do nome da cota
	this.autoCompletarPorNome = function(inputsCota) {
		
		var idCampoNomeCota = inputsCota.idCampoNomeCota;
		var isFromModal = inputsCota.isFromModal;
		
		var nomeCota = $(idCampoNomeCota, consultaCota.workspace).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNomeCota, consultaCota.workspace).autocomplete({source: [""]});
		
		if (nomeCota && nomeCota.length > 2) {
			
			$.postJSON(
				contextPath + "/cadastro/cota/autoCompletarPorNome", {nomeCota:nomeCota},
				function(result) { 
					consultaCota.exibirAutoComplete(result, inputsCota, 3);
				},
				null, 
				isFromModal
			);
		}
	},
	
	//Exibe o auto complete no campo
	this.exibirAutoComplete = function(result, inputsCota, tamanhoInicial) {
		
		idCampoCota = inputsCota.idCampoNomeCota;
		
		$(idCampoCota, consultaCota.workspace).autocomplete({
			source: result,
			
			select : function(event, ui) {
				consultaCota.numeroCotaSelecionada = ui.item.chave.numero;
				consultaCota.pesquisarPorNomeCotaAposIntervalo(inputsCota.idCampoNumeroCota, inputsCota.idCampoNomeCota, 
						inputsCota.isFromModal, inputsCota.successCallBack, inputsCota.errorCallBack);
			},
			minLength: tamanhoInicial,
			delay : 0,
		});
	},
	
	//Pesquisar por nome da cota
	this.pesquisarPorNomeCota = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		setTimeout(function() { clearInterval(consultaCota.intervalo); }, 10 * 1000);
		
		consultaCota.intervalo = $().interval(function() {
			
			if (consultaCota.descricaoAtribuida) {
				
				if (consultaCota.pesquisaRealizada) {
					
					clearInterval(consultaCota.intervalo);
					
					return;
				}
				
				consultaCota.pesquisarPorNomeCotaAposIntervalo(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack);
			}
			
		}, 100);
	},
	
	//Pesquisa por nome da cota após o intervalo
	this.pesquisarPorNomeCotaAposIntervalo = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		
		
		var nomeCota = $(idCampoNomeCota, consultaCota.workspace).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNumeroCota, consultaCota.workspace).val("");
		
		if (nomeCota && nomeCota.length > 0) {
			$.postJSON(
				contextPath + "/cadastro/cota/pesquisarPorNome", 
				{nomeCota:nomeCota},
				function(result) {
					
					if (result.length > 1){

						if (consultaCota.numeroCotaSelecionada){
							
							consultaCota.pesquisaRealizada = true;
							$(idCampoNumeroCota, consultaCota.workspace).val(consultaCota.numeroCotaSelecionada);
						}
					}
					else{
					
					    consultaCota.pesquisarPorNomeSuccessCallBack(result, idCampoNumeroCota, idCampoNomeCota, successCallBack);
					}
				},
				function() {
					consultaCota.pesquisarPorNomeErrorCallBack(idCampoNomeCota, errorCallBack);
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
			
			$(idCampoNumeroCota, consultaCota.workspace).val(result.numero);
			$(idCampoNomeCota, consultaCota.workspace).val(result.nome);
			
			if (successCallBack) {
				successCallBack(result);
			}
		}
	},
	
	//Error callback para pesquisa por nome da cota
	this.pesquisarPorNomeErrorCallBack = function(idCampoNomeCota, errorCallBack) {
		
		$(idCampoNomeCota, consultaCota.workspace).val("");
		
		$(idCampoNomeCota, consultaCota.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	//Obtém uma cota pelo número
	this.obterPorNumeroCota = function(numeroCota, isFromModal, successCallback, errorCallBack) {
		
		if (numeroCota && numeroCota.length > 0) {

			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNumero",
				{numeroCota:numeroCota},
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
//@ sourceURL=consultaCota.js