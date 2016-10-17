function PesquisaProduto(workspace) {

	var pesquisaProduto = this;
	
	this.descricaoAtribuida = true,
	
	this.pesquisaRealizada = false,
	
	this.intervalo = null,
	
	this.workspace = workspace;
	
	//Pesquisa por código de produto
	this.pesquisarPorCodigoProduto = function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		
		var codigoProduto = $(idCodigo, pesquisaProduto.workspace).attr("value");
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo, pesquisaProduto.workspace).val(codigoProduto);
		
		$(idProduto, pesquisaProduto.workspace).val("");
		$(idEdicao, pesquisaProduto.workspace).val("");
		$(idEdicao, pesquisaProduto.workspace).attr("disabled", "disabled");
		
		if (codigoProduto && codigoProduto.length > 0) {
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   {codigoProduto:codigoProduto},
					   function(result) { pesquisaProduto.pesquisarPorCodigoSuccessCallBack(result, idProduto, idEdicao, successCallBack); },
					   function() { pesquisaProduto.pesquisarPorCodigoErrorCallBack(idCodigo, errorCallBack); }, isFromModal);
		
		} else {
		
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	
	//Pesquisa por código de produto
	this.pesquisarPorCodigoProdutoIcd = function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		
		var codigoProduto = $(idCodigo, pesquisaProduto.workspace).attr("value");
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo, pesquisaProduto.workspace).val(codigoProduto);
		
		$(idProduto, pesquisaProduto.workspace).val("");
		$(idEdicao, pesquisaProduto.workspace).val("");
		$(idEdicao, pesquisaProduto.workspace).attr("disabled", "disabled");
		
		if (codigoProduto && codigoProduto.length > 0) {
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProdutoIcd",
					   {codigoProduto:codigoProduto},
					   function(result) { pesquisaProduto.pesquisarPorCodigoSuccessCallBack(result, idProduto, idEdicao, successCallBack); },
					   function() { pesquisaProduto.pesquisarPorCodigoErrorCallBack(idCodigo, errorCallBack); }, isFromModal);
		
		} else {
		
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Mostrar auto complete por nome do produto
	this.autoCompletarPorNomeProduto = function(idProduto, isFromModal) {
		
		pesquisaProduto.pesquisaRealizada = false;
		var nomeProduto = $(idProduto, pesquisaProduto.workspace).attr("value");
		
		if (nomeProduto && nomeProduto.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorNomeProduto", {nomeProduto:nomeProduto},
					   function(result) { pesquisaProduto.exibirAutoComplete(result, idProduto); },
					   null, isFromModal);
		}
	},
	
	//Pesquisar por nome do produto
	this.pesquisarPorNomeProduto = function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		
		setTimeout(function() { clearInterval(pesquisaProduto.intervalo); }, 10 * 1000);
		
		pesquisaProduto.intervalo = $().interval(function() {
			
			if (pesquisaProduto.descricaoAtribuida) {
				
				if (pesquisaProduto.pesquisaRealizada) {
					
					clearInterval(pesquisaProduto.intervalo);
					
					return;
				}
				
				pesquisaProduto.pesquisarPorNomeProdutoAposIntervalo(idCodigo, idProduto, idEdicao,
															isFromModal, successCallBack, errorCallBack);
			}
			
		}, 100);
	},
	
	//Validação do número da edição
	this.validarNumEdicao = function(idCodigo, idEdicao, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo, pesquisaProduto.workspace).val();
		var numeroEdicao = $(idEdicao, pesquisaProduto.workspace).val();
		
		if (codigoProduto && codigoProduto.length > 0
				&& numeroEdicao && numeroEdicao.length > 0) {
			
			var data = {codigoProduto:codigoProduto,numeroEdicao:numeroEdicao};

			$.postJSON(contextPath + "/produto/validarNumeroEdicao",
					data, function(result) { pesquisaProduto.validaNumeroEdicaoSucessoCallBack(idCodigo, idEdicao, successCallBack); },
					function() { pesquisaProduto.validarNumeroEdicaoErrorCallBack(idEdicao, errorCallBack); }, isFromModal);
		} else {
			
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	/*
	 * Bloqueia um dos campos de edições do produto para não permitir preenchimento de ambos
	 */
	this.validarCamposEdicoes = function(elId) {
		
		if(elId == 'edicaoProduto') {
			if($('#quantidadeEdicoes').val() == '') {
				$('#quantidadeEdicoes').attr('disabled', 'disabled');
			}
		} else {
			if($('#edicaoProduto').val() == '') {
				$('#edicaoProduto').attr('disabled', 'disabled');
			}
		}
		
		if($('#quantidadeEdicoes').val() == '' && $('#edicaoProduto').val() == '') {
			$('#quantidadeEdicoes').removeAttr('disabled');
			$('#edicaoProduto').removeAttr('disabled');
		}
	},
	
	this.pesquisarPorCodigoSuccessCallBack = function(result, idProduto, idEdicao, successCallBack,idCodigo, isFromModal) {
		
		$(idEdicao, pesquisaProduto.workspace).removeAttr("disabled");
		$(idProduto, pesquisaProduto.workspace).val(result.nome);
		$(idEdicao, pesquisaProduto.workspace).focus();
		
		pesquisaProduto.pesquisaRealizada = true;
		
		if (successCallBack) {
			successCallBack(result);
		}
	},
	
	this.pesquisarPorCodigoErrorCallBack = function(idCodigo, errorCallBack) {
		$(idCodigo, pesquisaProduto.workspace).val("");
		$(idCodigo, pesquisaProduto.workspace).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},
	
	this.exibirAutoComplete = function(result, idProduto) {
		
		$(idProduto, pesquisaProduto.workspace).autocomplete({
			source : result,
			focus : function(event, ui) {
				pesquisaProduto.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				pesquisaProduto.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				pesquisaProduto.descricaoAtribuida = true;
				
			},
			minLength: 4,
			delay : 0,
		});
	},
	
	this.pesquisarPorNomeProdutoAposIntervalo = function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(pesquisaProduto.intervalo);
		
		pesquisaProduto.pesquisaRealizada = true;
		
		var nomeProduto = $(idProduto, pesquisaProduto.workspace).val();
		
		var codigoProduto = $(idCodigo, pesquisaProduto.workspace).val();
		
		nomeProduto = $.trim(nomeProduto);
		codigoProduto = $.trim(codigoProduto);
		
		//$(idCodigo, pesquisaProduto.workspace).val("");
		$(idEdicao, pesquisaProduto.workspace).val("");
		$(idEdicao, pesquisaProduto.workspace).attr("disabled", "disabled");
		
		 var data = [{name:"nomeProduto",  value:nomeProduto},
        			 {name:"codigoProduto",value:codigoProduto}
					];
		
		if (nomeProduto && nomeProduto.length > 0) {
			$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto",data,
					   function(result) { 
							
							pesquisaProduto.pesquisarPorNomeSuccessCallBack(result, idCodigo, idProduto, idEdicao, successCallBack); 
						},
					   function() { 
							
							$(idCodigo, pesquisaProduto.workspace).val("");
							
							pesquisaProduto.pesquisarPorNomeErrorCallBack(idCodigo, idProduto, idEdicao, errorCallBack);
							
					   }, isFromModal);
		} else {
			
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	this.validaNumeroEdicaoSucessoCallBack = function(idCodigo, idEdicao, successCallBack) {
		if (successCallBack) {
			successCallBack(idCodigo, idEdicao);
		}
	},
	
	this.validarNumeroEdicaoErrorCallBack = function(idEdicao, errorCallBack) {
		$(idEdicao, pesquisaProduto.workspace).val("");
		$(idEdicao, pesquisaProduto.workspace).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},
	
	this.pesquisarPorNomeSuccessCallBack = function(result, idCodigo, idProduto, idEdicao, successCallBack) {
		if (result != "") {
			$(idCodigo, pesquisaProduto.workspace).val(result.codigo);
			$(idProduto, pesquisaProduto.workspace).val(result.nome);
			
			$(idEdicao, pesquisaProduto.workspace).removeAttr("disabled");
			$(idEdicao, pesquisaProduto.workspace).focus();
			
			if (successCallBack) {
				successCallBack();
			}
		}
	},
	
	this.pesquisarPorNomeErrorCallBack = function(idCodigo, idProduto, idEdicao, errorCallBack) {
		$(idProduto, pesquisaProduto.workspace).val("");
		$(idProduto, pesquisaProduto.workspace).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},
	
	this.validarEdicaoSuccessCallBack = function(){
		
		 var data = [{name:"codigoProduto",value:$("#codigoProduto", pesquisaProduto.workspace).val()},
        			 {name:"numeroEdicao",value:$("#edicao", pesquisaProduto.workspace).val()},
					];
		
		 $.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisarDataProgramada",
				   data, function(result) {
			 $("#dataProgramada", pesquisaProduto.workspace).val(result);
		 });
	},
	
	this.validarEdicaoErrorCallBack = function() {
		 $("#dataProgramada", pesquisaProduto.workspace).val("");
	},
	
	//Pesquisa por código de produto
	this.pesquisarPorCodigoProdutoAutoCompleteEdicao = function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		
		var codigoProduto = $(idCodigo, pesquisaProduto.workspace).val();
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo, pesquisaProduto.workspace).val(codigoProduto);
		
		$(idProduto, pesquisaProduto.workspace).val("");
		$(idEdicao, pesquisaProduto.workspace).val("");
		
		if (codigoProduto && codigoProduto.length > 0) {
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   {codigoProduto:codigoProduto},
					   function(result) { pesquisaProduto.pesquisarPorCodigoSuccessCallBack(result, idProduto, idEdicao, successCallBack, idCodigo, isFromModal); 
					   		pesquisaProduto.autoCompletarEdicaoPorProduto(idCodigo, idEdicao, isFromModal);
						},
					   null, isFromModal);
		
		} else {
		
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Mostrar auto complete por nome do produto
	this.autoCompletarEdicaoPorProduto = function(idCodigoProduto, idEdicao, isFromModal) {
		
		pesquisaProduto.pesquisaRealizada = false;
		
		var codigoProduto = $(idCodigoProduto, pesquisaProduto.workspace).val();
		
		$.postJSON(contextPath + "/produto/autoCompletarEdicaoPorProduto", {codigoProduto:codigoProduto},
					   function(result) { pesquisaProduto.exibirAutoCompleteEdicao(result, idEdicao); },
					   null, isFromModal);
		
	},
	
	//Mostrar auto complete por código do produto
	//Auto complete após a 5° posição 
	this.autoCompletarPorCodigoProduto = function(idCampoCodProduto, isFromModal) {
		
		pesquisaProduto.pesquisaRealizada = false;
		
		var codProduto = $(idCampoCodProduto, pesquisaProduto.workspace).val();
		
		if (codProduto && codProduto.length >= 5) {
			$.postJSON(contextPath + "/produto/autoCompletarPorCodProduto", {codigoProduto:codProduto},
					   function(result) { pesquisaProduto.exibirAutoComplete(result, idCampoCodProduto); },
					   null, isFromModal);
		}
	},
	
	
	this.exibirAutoCompleteEdicao = function(result, idEdicao) {
		
		$(idEdicao, pesquisaProduto.workspace).autocomplete({
			source : result
		});
	};
	
	
	
	
}
//@ sourceURL=pesquisaProduto.js
