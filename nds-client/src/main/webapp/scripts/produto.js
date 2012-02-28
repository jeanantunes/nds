function pesquisarPorCodigoProduto() {
	var codigoProduto = $("#codigo").val();
	
	if (!isNumeric(codigoProduto)) {
		//TODO: mostrar msg
		
		$("#codigo").val("");
		$("#produto").val("");
		$("#edicao").val("");
		$("#edicao").attr("disabled", "disabled");
		
		return;
	}
	
	if (codigoProduto && codigoProduto.length > 0) {
		$("#produto").val("");
		$("#edicao").val("");
		
		$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
				   "codigoProduto=" + codigoProduto, exibirNomeProduto, null);
	}
}

function exibirNomeProduto(result) {
	$("#edicao").removeAttr("disabled");
	$("#produto").val(result.nome);
	$("#edicao").focus();
	
	chamarFuncaoCallBackPesquisaProduto();
}

function pesquisarPorNomeProduto() {
	var produto = $("#produto").val();
	
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

function validarNumEdicao() {
	var codigoProduto = $("#codigo").val();
	var numeroEdicao = $("#edicao").val();
	
	if (!isNumeric(numeroEdicao)) {
		//TODO: mostrar msg
		
		$("#edicao").val("");
		
		return;
	}
	
	if (codigoProduto && codigoProduto.length > 0
			&& numeroEdicao && numeroEdicao.length > 0) {
		
		var data = "codigoProduto=" + codigoProduto +
		   		   "&numeroEdicao=" + numeroEdicao;
		
		$.postJSON(contextPath + "/produto/validarNumeroEdicao",
				   data, chamarFuncaoCallBackValidacaoEdicao, tratarErroValidacao);
	}
}

function tratarErroValidacao() {
	$("#edicao").val("");
	$("#edicao").focus();
}

function chamarFuncaoCallBackPesquisaProduto() {
	if (typeof pesquisarProdutoCallBack == 'function') {
		pesquisarProdutoCallBack();
	}
}

function chamarFuncaoCallBackValidacaoEdicao() {
	if (typeof validarEdicaoCallBack == 'function') {
		validarEdicaoCallBack();
	}
}