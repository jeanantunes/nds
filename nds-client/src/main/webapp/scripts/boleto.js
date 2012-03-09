function pesquisarBoletoPorNumeroCota() {
	var numCota = $("#numCota").val();


	
	if (!isNumeric(codigoProduto)) {
		$("#numCota").val("");
		return;
	}
	
	if (numCota && numCota.length > 0) {
		$("#produto").val("");
		$("#edicao").val("");
		
		$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
				   "numCota=" + numCota, exibirDescricaoCota, null);
	}
}

function exibirDescricaoCota(result) {
	$("#edicao").removeAttr("disabled");
	$("#produto").val(result.nome);
	$("#edicao").focus();
	chamarFuncaoCallBackPesquisaCota();
}

function pesquisarPorDescricaoCota() {
	var produto = $("#produto").val();
	
	if(typeof limparCamposPrePesquisaProduto == "function") {
		limparCamposPrePesquisaProduto();
	}
	
	if (produto && produto.length > 0) {
		$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto",
				   "nomeProduto=" + produto, exibirAutoComplete);
	}
}

function exibirAutoComplete(result) {
	$("#produto").autocomplete({
		source: result,
		select: function(event, ui) {
			completarPesquisa(ui.item.chave);
		}
	});
}

function completarPesquisa(chave){
	$("#edicao").removeAttr("disabled");
	$("#edicao").val("");
	$("#codigo").val(chave.codigo);
	$("#edicao").focus();
	
	chamarFuncaoCallBackPesquisaProduto();
}


function tratarErroValidacao() {
	$("#edicao").val("");
	$("#edicao").focus();
}

function chamarFuncaoCallBackPesquisaCota() {
	if (typeof pesquisarProdutoCallBack == 'function') {
		pesquisarCotaCallBack();
	}
}

