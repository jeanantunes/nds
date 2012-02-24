function pesquisarPorCodigoProduto() {
	var codigoProduto = $("#codigo").val();
	
	if (codigoProduto && codigoProduto.length > 0) {
		$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
				   "codigoProduto=" + codigoProduto, exibirNomeProduto);
	}
}

function exibirNomeProduto(result) {
	$("#produto").val(result.nome);
	$("#edicao").focus();
	
	chamarFuncaoCallBack();
}

function pesquisarPorNomeProduto() {
	var produto = $("#produto").val();
	
	if (produto && produto.length > 0) {
		$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto",
				   "nomeProduto=" + produto, exibirAutoComplete);
	}
}

function exibirAutoComplete(result) {
	//TODO tratar retorno pesquisa
	$("#produto").autocomplete({
		source: result,
		select: function(event, ui) {
			completarPesquisa(ui.item.chave);
		}
	});
}

function completarPesquisa(chave){
	$("#codigo").val(chave.codigo);
	$("#edicao").focus();
	
	chamarFuncaoCallBack();
}

function validarNumEdicao() {
	var data = "codigoProduto=" + $("#codigo").val() +
       		   "&numeroEdicao=" + $("#edicao").val();

	$.postJSON(contextPath + "/produto/validarNumeroEdicao",
			   data, null, tratarErroValidacao);
}

function tratarErroValidacao() {
	$("#edicao").val("");
	$("#edicao").focus();
}

function chamarFuncaoCallBack() {
	if (typeof pesquisarProdutoCallBack == 'function') {
		pesquisarProdutoCallBack();
	}
}
