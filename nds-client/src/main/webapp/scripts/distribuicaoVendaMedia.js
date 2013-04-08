function DistribuicaoVendaMedia(pathTela, workspace) {
	
	var url = pathTela;
	var T = this;
//	var _workspace = workspace;
	
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
					T.produtoEdicaoBases = result;
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
		row.pesoInput = '<select name="select'+index+'" id="select'+index+'" onchange="distribuicaoVendaMedia.selecionarPesoProduto('+index+', this)" ><option value="1">1</option><option value="2">2</option><option value="3">3</option></select>';
		row.peso = 1;
		row.select = '<input onclick="distribuicaoVendaMedia.selecionarProdutoBase(' + index + ', this)" type="checkbox" value=""/>';
		
		if(row.statusSituacao == undefined){
			row.statusSituacao = '';
		}
		if(row.reparte == undefined){
			row.reparte = '';
		}
		if(row.venda == undefined){
			row.venda = '';
		}
		if(row.percentualVenda == undefined){
			row.percentualVenda = '';
		}
	};
	
	this.selecionarPesoProduto = function(index, select){
		T.produtoEdicaoBases[index].peso = select.value;
	};
	
	this.processarLinhaPesquisaBases = function(index, row){
		row.capa = '<a onmouseover="distribuicaoVendaMedia.popup_detalhes('+row.codigoProduto+', '+row.numeroEdicao+');" onmouseout="popup_detalhes_close();" href="javascript:;"><img src="../images/ico_detalhes.png" border="0"/></a>';
		row.select = '<input onclick="distribuicaoVendaMedia.selecionarProdutoBasePopUp(' + index + ', this)" type="checkbox" value=""/>';
		if(row.reparte == undefined){
			row.reparte = '';
		}
		if(row.venda == undefined){
			row.venda = '';
		}
		if(row.percentualVenda == undefined){
			row.percentualVenda = '';
		}
		if(row.statusSituacao == undefined){
			row.statusSituacao = '';
		}
	};
	
	this.selecionarProdutoBasePopUp = function(index, checkbox){
		T.produtoEdicaoPesquisaBases[index].selecionado = checkbox.checked;
	};
	
	this.selecionarProdutoBase = function(index, checkbox){
		T.produtoEdicaoBases[index].selecionado = checkbox.checked;
	};
	
	this.removerProdutoEdicaoDaBase = function(){
		var data = [];
		$.each(T.produtoEdicaoBases, function(index, item){
			if(item.selecionado){
				data.push({name :"indexes", value: index});
			}
		});
		
		$.postJSON(
				pathTela + "/distribuicaoVendaMedia/removerProdutoEdicaoDaBase", 
				data,
				function(result) {
					T.produtoEdicaoBases = result;
					T.preencherGridBases(result);
				},
				function(){
					exibirMensagem("ERROR", ["Erro ao excluir itens da lista. Tente novamente mais tarde."]);
				}
			);
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
	
	this.popup_detalhes = function(codigoProduto,numeroEdicao) {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		//histogramaVendasController.getCapaEdicao(codigo,edicao);
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: false,
			escondeHeader: false,
			open : function(event, ui) {
				
				$("#imagemCapaEdicao").one('load', function() {
						$("#imagemCapaEdicao").show();
						$("#loadingCapa").hide();
					}).each(function() {
					  if(this.complete) $(this).load();
					});
				
				var randomnumber=Math.floor(Math.random()*11);
				
				$("#imagemCapaEdicao")
						.attr("src",pathTela
										+ "/distribuicao/histogramaVendas/getCapaEdicaoJson?random="+randomnumber+"&codigoProduto="
										+ codigoProduto
										+ "&numeroEdicao="
										+ numeroEdicao);
				console.log($("#imagemCapaEdicao").attr("src"));
			},
			close:function(event, ui){
				$("#imagemCapaEdicao").removeAttr("src").hide();
				$("#loadingCapa").show();
				
			}
		});
	};
	
	this.selectElementoRegiaoDistribuicao = function(select, elementoSelect){
		selectedItem = $("#" + select).val();
		
		if(selectedItem != 'Selecione...'){
			carregarCombo(pathTela + "/distribuicao/historicoVenda/carregarElementos", 
				  {"componente":selectedItem},
		            $("#" + elementoSelect, T._workspace), null, null);
		} else {
			$('#' + elementoSelect).html('');
			$('#' + elementoSelect).append("<option value='-1'>Selecione...</option>");
		}
	};
	
	this.checkComponenteBonificaca = function(value, descricao, enumValue){
		if(T.componenteBonificacaoSelecionado != null){
			$("#componenteBonificacao" + T.componenteBonificacaoSelecionado.value).removeAttr("checked");
		}
		$("#componenteBonificacao" + value).attr("checked", "checked");
		T.componenteBonificacaoSelecionado = {descricao : descricao, value : value, enumValue : enumValue};
	};
	
	this.selectComponenteBonificacao = function(value, descricao, enumValue){
		var selectedItem = value;
		T.checkComponenteBonificaca(value, descricao, enumValue);
		$.postJSON(
				url + "/distribuicao/historicoVenda/carregarElementos", 
					{"componente":selectedItem},
					function(result) {
						T.elementosBonificacao = result;
						$.each(result, function(index,row){ T.processarLinhaElemento(index, row);});
						$("#bonificacoesGrid").flexAddData({
							rows : toFlexiGridObject(result),
							page : 1,
							total : 1
						});
					},
					
					function(){
						exibirMensagem("ERROR", ["Erro ao processar a pesquisa. Tente novamente mais tarde."]);
					}
				);
		
	};
	
	this.selecionarElementoBonificacao = function(index, checkbox){
		T.elementosBonificacao[index].selecionado = checkbox.checked;
	};
	
	this.processarLinhaElemento = function(index, row){
		row.descricao = row.value.$;
		row.acao = '<input onclick="distribuicaoVendaMedia.selecionarElementoBonificacao(' + index + ', this)" type="checkbox" value="" class="bonificacaoElementoInput"/>';
	};
	
	this.alterarBonificacao = function(input, index){
		T.bonificacaoSelecionados[index].percBonificacao = input.value;
	};
	
	this.alterarReparteMinimo = function(input, index){
		T.bonificacaoSelecionados[index].reparteMinimo = input.value;
	};
	
	this.alterarTodasAsCotas = function(input, index){
		T.bonificacaoSelecionados[index].todasAsCotas = input.checked;
	};
	
	this.confirmarSelecaoBonificacao = function(){
		if(T.bonificacaoSelecionados == undefined){
			T.bonificacaoSelecionados = [];
		}
		for(var i = 0; i < T.elementosBonificacao.length; i++){
			var elemento = T.elementosBonificacao[i];
			if(elemento.selecionado){
				if( ! T.containsBonificacao(elemento)){
					var bonificacao = {
							index : T.bonificacaoSelecionados.length,
							componenteDesc : T.componenteBonificacaoSelecionado.descricao,
							componente : T.componenteBonificacaoSelecionado,
							elementoDesc : elemento.descricao,
							elemento : elemento,
							percBonificacaoInput : '<input id="percBonificacao-' + T.bonificacaoSelecionados.length + '" style="width: 40px; text-align: right;" onchange="distribuicaoVendaMedia.alterarBonificacao(this, '+T.bonificacaoSelecionados.length+')"/>',
							percBonificacao : '',
							reparteMinimoInput : '<input id="reparteMinimo-' + T.bonificacaoSelecionados.length + '" style="width: 40px; text-align: right;" onchange="distribuicaoVendaMedia.alterarReparteMinimo(this, '+T.bonificacaoSelecionados.length+')"/>',
							reparteMinimo : '',
							sel : '<input id="percBonificacao-' + T.bonificacaoSelecionados.length + '" type="checkbox" onchange="distribuicaoVendaMedia.alterarTodasAsCotas(this, '+T.bonificacaoSelecionados.length+')" />',
							todasAsCotas : false,
							acao : '<a onclick="popup_excluir_bonificacao('+T.bonificacaoSelecionados.length+');" href="javascript:;"><img src="images/ico_excluir.gif" border="0"/></a>',
					};
					T.bonificacaoSelecionados.push(bonificacao);
				}
			}
		}
		
		$("#elemento1Grid").flexAddData({
			rows : toFlexiGridObject(T.bonificacaoSelecionados),
			page : 1,
			total : 1
		});
		
		$.each($(".bonificacaoElementoInput"), function(index, input){ 
			input.checked = false;
		});
		
	};
	
	this.removerBonificacao = function(index){
		T.bonificacaoSelecionados.splice(index, 1);
		$("#elemento1Grid").flexAddData({
			rows : toFlexiGridObject(T.bonificacaoSelecionados),
			page : 1,
			total : 1
		});
	};
	
	this.containsBonificacao = function(elemento){
		for(var i = 0; i < T.bonificacaoSelecionados.length; i++){
			var bonificacao = T.bonificacaoSelecionados[i];
			
			if(bonificacao.componente.value == T.componenteBonificacaoSelecionado.value
					&& bonificacao.elemento.key.$ == elemento.key.$){
				return true;
			}
		}
		return false;
	};
	
	this.errorCallBack = function(){
		
	};
	
	this.gerar = function(){
		var data = [];
		
		data.push({name : "dto.reparteDistribuir", value : $("#reparteDistribuir").val() });
		data.push({name : "dto.reparteMinimo", value : $("#reparteMinimo").val() });
		data.push({name : "dto.usarFixacao", value : $("#usarFixacao")[0].checked });
		data.push({name : "dto.distribuicaoPorMultiplo", value : $("#distribuicaoPorMultiplo")[0].checked });
		data.push({name : "dto.multiplo", value : $("#multiplo").val() });
		
		if(T.produtoEdicaoBases != undefined){
			for(var i = 0; i < T.produtoEdicaoBases.length; i++){
				var produtoEdicao = T.produtoEdicaoBases[i];
				data.push({name: "dto.bases["+i+"].id", value : produtoEdicao.id});
				data.push({name: "dto.bases["+i+"].peso", value : produtoEdicao.peso});
			}
		}
		
		if(T.bonificacaoSelecionados != undefined){
			for(var i = 0; i < T.bonificacaoSelecionados.length; i++){
				data.push({name: "dto.bonificacoes["+i+"].componente", value : T.bonificacaoSelecionados[i].componente.enumValue});
				data.push({name: "dto.bonificacoes["+i+"].elemento", value : T.bonificacaoSelecionados[i].elemento.key.$});
				data.push({name: "dto.bonificacoes["+i+"].bonificacao", value : T.bonificacaoSelecionados[i].percBonificacao});
				data.push({name: "dto.bonificacoes["+i+"].reparteMinimo", value : T.bonificacaoSelecionados[i].reparteMinimo});
				data.push({name: "dto.bonificacoes["+i+"].todasAsCotas", value : T.bonificacaoSelecionados[i].todasAsCotas});
			}
		}
		data.push({name : "dto.todasAsCotas", value : $("#RDtodasAsCotas")[0].checked });
		if($("#RDcomponente")[0].checked){
			data.push({name : "dto.componente", value : $("#componenteRegiaoDistribuicao").val() });
			data.push({name : "dto.elemento", value : $("#elementoRegiaoDistribuicao").val() });
		}
		if($("#RDAbrangencia")[0].checked){
			data.push({name : "dto.abrangenciaCriterio", value : $("#RDabrangenciaCriterio").val() });
			data.push({name : "dto.abrangencia", value : $("#RDabrangencia").val() });
		}
		
		if($("#RDroteiroEntrega")[0].checked){
			data.push({name : "dto.roteiroEntregaId", value : $("#selRoteiro").val() });
		}
		data.push({name : "dto.complementarAutomatico", value : $("#complementarAutomatico")[0].checked });
		data.push({name : "dto.cotasAVista", value : $("#distribuicaoPorMultiplo")[0].checked });
		if($("#RDExcecaoBancas")[0].checked){
			data.push({name : "dto.excecaoDeBancasComponente", value : $("#componenteInformacoesComplementares").val() });
			data.push({name : "dto.excecaoDeBancasElemento", value : $("#elementoInformacoesComplementares").val() });
		}
		
		$.post(pathTela + "/distribuicaoVendaMedia/gerarEstudo", data, function(response) {
			var currentTab = getCurrentTabContainer();
			currentTab.html(response);
			currentTab.innerHeight(650);
			redimensionarWorkspace();
		});
	};
	
	this.cancelar = function(){
		
		$(".ui-tabs-selected").find("span").click();
		$("a[href='"+pathTela+"/matrizDistribuicao']").click();
	};
	
};

//@ sourceURL=distribuicaoVendaMedia.js
