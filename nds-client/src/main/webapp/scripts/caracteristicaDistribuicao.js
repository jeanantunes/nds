var caracteristicaDistribuicaoController = $.extend(true, {
	
	init : function() {
		
		$("#pesquisaDetalheGrid",caracteristicaDistribuicaoController.workspace).flexigrid({
			preProcess: function(data){return caracteristicaDistribuicaoController.preProcessPesquisaDetalheGrid(data);},
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 65,
				sortable : true,
				align : 'left'
			},{
				display : 'Produto',
				name : 'nomeProduto',
				width : 110,
				sortable : true,
				align : 'center'
			},{
				display : 'Editor',
				name : 'nomeEditor',
				width : 60,
				sortable : true,
				align : 'center'
			},{
				display : 'Edição',
				name : 'numeroEdicao',
				width : 40,
				sortable : true,
				align : 'center'
			},{
				display : 'Preço de Capa',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : 'Classificação',
				name : 'classificacao',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : 'Segmento',
				name : 'segmento',
				width : 100,
				sortable : true,
				align : 'center'
			},{
				display : 'Lancamento',
				name : 'dataLancamentoString',
				width : 70,
				sortable : true,
				align : 'center'
			},{
				display : 'Recolhimento',
				name : 'dataRecolhimentoString',
				width : 70,
				sortable : true,
				align : 'center'
			},{
				display : 'Reparte',
				name : 'reparteString',
				width : 50,
				sortable : true,
				align : 'center'
			},{
				display : 'Venda',
				name : 'vendaString',
				width : 50,
				sortable : true,
				align : 'center'
			},{
				display : 'Chamada de Capa',
				name : 'chamadaCapa',
				width : 100,
				sortable : true,
				align : 'center'
			},{
				display : 'Capa',
				name : 'acao',
				width : 50,
				sortable : true,
				align : 'center'
			}],
			width : 1100,
			height : 200,
			sortname : "nomeProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true
		
		});
		
	},
	
	
	preProcessPesquisaDetalheGrid:function(resultado){
		caracteristicaDistribuicaoController.validarBotaoExportar(resultado.rows);
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var capa= "<a onclick='caracteristicaDistribuicaoController.popup_detalhes("+row.cell.codigoProduto+ "," +row.cell.numeroEdicao + ");'  href='javascript:void(0);'><img src='images/ico_detalhes.png'></a>";

			row.cell.acao=capa;
			if(row.cell.reparteString){
				row.cell.reparteString = parseFloat(row.cell.reparteString).toFixed(2);
			}
			if(row.cell.vendaString){
				row.cell.vendaString = parseFloat(row.cell.vendaString).toFixed(2);
			}
			if(row.cell.precoCapa){
				row.cell.precoCapa = parseFloat(row.cell.precoCapa).toFixed(2);
			}
			
		});
		
		return resultado;
		
	},
	
	validarBotaoExportar:function(numRegistros){
		if(numRegistros == 0){
			$("#linkGerarArquivoCaracDist").attr("href","#");
			$("#linkGerarArquivoCaracDist").attr("onclick","caracteristicaDistribuicaoController.exibeMsgValidacaoExportarErro()");
			$("#linkImprimirCaracDist").attr("href","#");
			$("#linkImprimirCaracDist").attr("onclick","caracteristicaDistribuicaoController.exibeMsgValidacaoExportarErro()");
		}else{
			$("#linkGerarArquivoCaracDist").attr("href", contextPath+"/distribuicao/caracteristicaDistribuicao/exportar?fileType=XLS");
			$("#linkGerarArquivoCaracDist").attr("onclick", "javascript:void(0)");
			$("#linkImprimirCaracDist").attr("href",contextPath+"/distribuicao/caracteristicaDistribuicao/exportar?fileType=PDF");
			$("#linkImprimirCaracDist").attr("onclick","javascript:void(0)");
		}
	},
	
	exibeMsgValidacaoExportarErro:function(){
		exibirMensagem("WARNING",["Não há registros a serem exportados!"]);
	},
	
	exibirCapa:function(codProduto, numeroEdicao){
		var params = [];
		params.push({name:'codigoProduto',	value: codProduto});
		params.push({name:'numeroEdicao', value: numeroEdicao});
		$.postJSON(contextPath + '/distribuicao/caracteristicaDistribuicao/exibirCapa',params);
	},
	
	popup_detalhes:function(codigoProduto,numeroEdicao) {
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: false,
			open : function(event, ui) {
				
				$("#imagemCapaEdicao").one('load', function() {
						$("#imagemCapaEdicao").show();
						$("#loadingCapa").hide();
					}).each(function() {
					  if(this.complete) $(this).load();
					});
				
				var randomnumber=Math.floor(Math.random()*11);
				
				$("#imagemCapaEdicao")
						.attr("src",contextPath
								
										+ "/capa/getCapaEdicaoJson?random="+randomnumber+"&codigoProduto="
										+ codigoProduto
										+ "&numeroEdicao="
										+ numeroEdicao);
			},
			close:function(event, ui){
				$("#imagemCapaEdicao").removeAttr("src").hide();
				$("#loadingCapa").show();
				
			}
		});
	},

	 popup_detalhes_close:function() {
	  $( "#dialog-detalhes" ).dialog( "close" );
	  
	},
	
	pesquisar:function(){
	if(caracteristicaDistribuicaoController.camposVazios()){
		exibirMensagem("WARNING",["Preencha pelo menos um campo dos filtros para realizar a pesquisa!"]);
	}else{
		
			console.log("detalhe");
			caracteristicaDistribuicaoController.exibeGridDetalhe();
			$(".pesquisaDetalheGrid",caracteristicaDistribuicaoController.workspace).flexOptions({
				url: contextPath + "/distribuicao/caracteristicaDistribuicao/pesquisarDetalhe",
				dataType : 'json',
				params: caracteristicaDistribuicaoController.getDadosFiltroPesquisaDetalhe()
			});
			
			$(".pesquisaDetalheGrid",caracteristicaDistribuicaoController.workspace).flexReload();
		
	}
		
	},
	
	camposVazios:function(){
		var codigoVazio = $("#codigoProduto").val() =="" ;
		var produtoVazio = $("#nomeProduto").val() =="" ;
		var nomeEditorVazio = $("#nomeEditor").val() =="";
		var segmentoVazio =  $("#segmento").val()=="";
		var brindeVazio = $("#brinde").val()=="";
		var classificacaoVazio =  $("#classificacao").val()=="";
		var chamadaCapaVazio = $("#chamadaCapa").val()==""; 
		var faixaDeVazio=$("#faixaDe").val()=="";
		var faixaAteVazio=$("#faixaAte").val()=="";
		
		return (codigoVazio && produtoVazio && classificacaoVazio && nomeEditorVazio &&  segmentoVazio && brindeVazio && faixaDeVazio && faixaAteVazio && chamadaCapaVazio);
	},
	
	
	getDadosFiltroPesquisaDetalhe:function(){
		
		var data = [];
		data.push({name:'filtro.codigoProduto',	value: $("#codigoProduto").val()});
		data.push({name:'filtro.nomeProduto', value: $("#nomeProduto").val()});
		data.push({name:'filtro.nomeEditor',  value: $("#nomeEditor").val()});
		data.push({name:'filtro.opcaoFiltroPublicacao', value:$('input:radio[name=radioPublicacao]:checked').val()});
		data.push({name:'filtro.opcaoFiltroEditor', value:$('input:radio[name=radioEditor]:checked').val()});
		data.push({name:'filtro.opcaoFiltroChamadaCapa', value:$('input:radio[name=radioChamadaCapa]:checked').val()});
		
		data.push({name:'filtro.classificacao',  value: $("#classificacao option:selected").val()});
		data.push({name:'filtro.segmento',  value: $("#segmento option:selected").val()});
		data.push({name:'filtro.brinde',  value: $("#brinde option:selected").val()});
		
		data.push({name:'filtro.precoDe',  value: $("#faixaDe").val()});
		data.push({name:'filtro.precoAte',  value: $("#faixaAte").val()});
		
		return data;
	},
	
	
	exibeGridDetalhe:function(){
		$('#divPesquisaDetalheGrid').show();
		$('#grid1').show();
	},
	escondeGridDetalhe:function(){
		
		$('#divPesquisaDetalheGrid').hide();
		$('#grid2').show();
	}

	
}, BaseController);
//@ sourceURL=caracteristicaDistribuicao.js