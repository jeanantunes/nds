function DistribuicaoVendaMedia(pathTela, workspace) {
	
	var url = pathTela;
	var T = this;
	var produtoEdicaoPesquisaBases = null;
		
	
	this.confirmarProdutosEdicaoBasePopup = function(){
		var data = [];
		$.each(T.produtoEdicaoPesquisaBases, function(index, item){
			if(item.selecionado){
				data.push({name :"indexes", value: index});
			}
		});
		
		$.postJSON(
				pathTela + "/distribuicaoVendaMedia/adicionarProdutoEdicaoABase", 
				data,
				function(result) {
					T.preencherGridBases(result);
				},
				function(){
					exibirMensagem("ERROR", ["Erro ao processar a pesquisa. Tente novamente mais tarde."]);
				}
			);
	};
	
	this.preencherGridBases = function(resultado){
		$.each(resultado, function(index,row){ T.processarLinhaBases(index, row);});
		
		$(".dadosBasesGrid").flexAddData({
			rows : toFlexiGridObject(resultado),
			page : 1,
			total : 1
		});
	};
	
	this.processarLinhaBases = function(index, row){
		row.peso = '<a onmouseover="popup_detalhes();" onmouseout="popup_detalhes_close();" href="javascript:;"><img src="../images/ico_detalhes.png" border="0"/></a>';
		row.select = '<input onclick="distribuicaoVendaMedia.selecionarProdutoBase(' + index + ', this)" type="checkbox" value=""/>';
	};
	
	this.processarLinhaPesquisaBases = function(index, row){
		row.capa = '<a onmouseover="popup_detalhes();" onmouseout="popup_detalhes_close();" href="javascript:;"><img src="../images/ico_detalhes.png" border="0"/></a>';
		row.select = '<input onclick="distribuicaoVendaMedia.selecionarProdutoBasePopUp(' + index + ', this)" type="checkbox" value=""/>';
	};
	
	this.selecionarProdutoBasePopUp = function(index, checkbox){
		T.produtoEdicaoPesquisaBases[index].selecionado = checkbox.checked;
	};

	this.preencherGridBasesPesquisa = function(resultado){
		$.each(resultado, function(index,row){ T.processarLinhaPesquisaBases(index, row);});
		
		$("#edicaoProdCadastradosGrid").flexAddData({
			rows : toFlexiGridObject(resultado),
			page : 1,
			total : 1
		});
	};
	
	this.pesquisarBases = function(){

		var data = [];
		var codigo = $("#codigoPesquisaBases").val();
		var produto = $("#produtoPesquisaBases").val();
		var edicao = $("#edicaoPesquisaBases").val();
		data.push({name:"codigo", value:codigo});
		data.push({name:"nome", value:produto});
		data.push({name:"edicao", value:edicao});
		$.postJSON(
				url + "/distribuicaoVendaMedia/pesquisarProdutosEdicao", 
					data,
					function(result) {
						T.produtoEdicaoPesquisaBases = result;
						T.preencherGridBasesPesquisa(result);
					},
					
					function(){
						exibirMensagem("ERROR", ["Erro ao processar a pesquisa. Tente novamente mais tarde."]);
					}
				);
	};
	

};

//@ sourceURL=distribuicaoVendaMedia.js