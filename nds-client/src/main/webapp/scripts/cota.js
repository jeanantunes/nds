var cota = {
	
	//Pesquisa por número da cota
	pesquisarPorNumeroCota : function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		var numeroCota = $(idCampoNumeroCota).val();

		numeroCota = $.trim(numeroCota);
		
		$(idCampoNomeCota).val("");
		
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
	pesquisarPorNumeroSuccessCallBack : function(result, idCampoNomeCota, successCallBack) {

		cota.pesquisaRealizada = true;
		
		$(idCampoNomeCota).val(result.nome);
		
		if (successCallBack) {
			
			successCallBack();
		}
	},
	
	//Error callback para pesquisa por número da cota
	pesquisarPorNumeroErrorCallBack : function(idCampoNumeroCota, errorCallBack) {
		
		$(idCampoNumeroCota).val("");
		
		$(idCampoNumeroCota).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	//Busca dados para o auto complete do nome da cota
	autoCompletarPorNome : function(idCampoNomeCota, isFromModal) {
		
		cota.pesquisaRealizada = false;
		
		var nomeCota = $(idCampoNomeCota).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNomeCota).autocomplete({source: ""});
		
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
	
	descricaoAtribuida : true,
	
	pesquisaRealizada : false,
	
	intervalo : null,
	
	//Exibe o auto complete no campo
	exibirAutoComplete : function(result, idCampoNomeCota) {
		
		$(idCampoNomeCota).autocomplete({
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
	pesquisarPorNomeCota : function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
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
	pesquisarPorNomeCotaAposIntervalo : function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(cota.intervalo);
		
		cota.pesquisaRealizada = true;
		
		var nomeCota = $(idCampoNomeCota).val();
		
		nomeCota = $.trim(nomeCota);
		
		$(idCampoNumeroCota).val("");
		
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
	pesquisarPorNomeSuccessCallBack : function(result, idCampoNumeroCota, idCampoNomeCota, successCallBack) {
		
		if (result != "") {
			
			$(idCampoNumeroCota).val(result.numero);
			$(idCampoNomeCota).val(result.nome);
			
			if (successCallBack) {
				
				successCallBack();
			}
		}
	},
	
	//Error callback para pesquisa por nome da cota
	pesquisarPorNomeErrorCallBack : function(idCampoNomeCota, errorCallBack) {
		
		$(idCampoNomeCota).val("");
		
		$(idCampoNomeCota).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	//Obtém uma cota pelo número
	obterPorNumeroCota : function(numeroCota, isFromModal, successCallback, errorCallBack) {
		
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
	}
	
};