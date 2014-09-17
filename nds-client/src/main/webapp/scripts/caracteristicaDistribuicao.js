var caracteristicaDistribuicaoController = $.extend(true, {

	init : function() {
		
		$("#pesquisaDetalheGrid", caracteristicaDistribuicaoController.workspace).flexigrid({
			preProcess: function(data) { return caracteristicaDistribuicaoController.preProcessPesquisaDetalheGrid(data); },
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
				align : 'left'
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
			width : 980,
			height : 200,
			sortname : "dataLancamentoString",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true
		
		});
		
		$("#pesquisaSimplesGrid",caracteristicaDistribuicaoController.workspace).flexigrid({
			preProcess: function(data){return caracteristicaDistribuicaoController.preProcessPesquisaSimplesGrid(data);},
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 100,
				sortable : true,
				align : 'left'
			},{
				display : 'Produto',
				name : 'nomeProduto',
				width : 300,
				sortable : true,
				align : 'center'
			},{
				display : 'Editor',
				name : 'nomeEditor',
				width : 300,
				sortable : true,
				align : 'center'
			},{
				display : 'Ação',
				name : 'acao',
				width : 100,
				sortable : true,
				align : 'center'
			}],
			width : 900,
			height : 200,
			sortname : "nomeProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true
		
		});
		
		
		
		$("#pesquisaDetalheGridModal",caracteristicaDistribuicaoController.workspace).flexigrid({
			preProcess: function(data){return caracteristicaDistribuicaoController.preProcessPesquisaDetalheGridModal(data);},
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
			sortname : "dataLancamentoString",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true
		
		});
	},
	
	
	preProcessPesquisaSimplesGrid:function(resultado){
		caracteristicaDistribuicaoController.validarBotaoExportarSimples(resultado.rows);
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			var capa ="<input type='image' src='images/ico_detalhes.png' onclick='caracteristicaDistribuicaoController.exibirDetalhesPesquisaModal(\""+row.cell.codigoProduto+"\",\""+row.cell.nomeProduto+"\");' />";

			row.cell.acao=capa;
			if(row.cell.reparteString){
				row.cell.reparteString = parseFloat(row.cell.reparteString).toFixed(0);
			}
			if(row.cell.vendaString){
				row.cell.vendaString = parseFloat(row.cell.vendaString).toFixed(0);
			}
			if(row.cell.precoCapa){
				row.cell.precoCapa = parseFloat(row.cell.precoCapa).toFixed(2);
			}
			
		});
		
		return resultado;
		
	},
	
	preProcessPesquisaDetalheGridModal:function(resultado){
		caracteristicaDistribuicaoController.validarBotaoExportarDetalhe(resultado.rows);
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var capa ="<input type='image' src='images/ico_detalhes.png' onclick='caracteristicaDistribuicaoController.popup_detalhes(\""+row.cell.codigoProduto+ "\" ," +row.cell.numeroEdicao +");' />";
//			var capa= "<a onclick='caracteristicaDistribuicaoController.popup_detalhes("+row.cell.codigoProduto+ "," +row.cell.numeroEdicao +");'  href='javascript:void(0);'><img src='images/ico_detalhes.png'></a>";
			row.cell.acao=capa;
			if(row.cell.reparteString){
				row.cell.reparteString = parseFloat(row.cell.reparteString).toFixed(0);
			}
			if(row.cell.vendaString){
				row.cell.vendaString = parseFloat(row.cell.vendaString).toFixed(0);
			}
			if(row.cell.precoCapa){
				row.cell.precoCapa = parseFloat(row.cell.precoCapa).toFixed(2);
			};
			
		});
		
		return resultado;
		
	},
	
	exibirDetalhesPesquisaModal:function(codProduto , nomeProduto){
		var data = [];
		data.push({name:'filtro.codigoProduto',	value: codProduto});
		data.push({name:'filtro.nomeProduto',	value: nomeProduto});
		
		$("#modal-pesquisa-detalhe").dialog({
			resizable: false,
			height:'350',
			width:'1150',
			modal: false,
			open: caracteristicaDistribuicaoController.atualizarGridModal(data),
			close:function(){
				var clearData = {
			            total: 0,    
			            page:1,
			            rows: []
			    };
				$("#pesquisaDetalheGridModal").flexAddData(clearData);
				
			}
			
			 
		});
		
	},
	
	atualizarGridModal:function(data){
		
		$(".pesquisaDetalheGridModal").flexOptions({
			url: contextPath + "/distribuicao/caracteristicaDistribuicao/pesquisarDetalhe",
			dataType : 'json',
			params: data
		});
		$(".pesquisaDetalheGridModal").flexReload();
		
		
		caracteristicaDistribuicaoController.exibeGridDetalheModal();
	},
	
	preProcessPesquisaDetalheGrid:function(resultado){
		caracteristicaDistribuicaoController.validarBotaoExportarDetalhe(resultado.rows);
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var capa= "<a onclick='caracteristicaDistribuicaoController.popup_detalhes("+row.cell.codigoProduto+ "," +row.cell.numeroEdicao +");'  href='javascript:void(0);'><img src='images/ico_detalhes.png'></a>";

			row.cell.acao=capa;			
			if(row.cell.precoCapa){
				row.cell.precoCapa = parseFloat(row.cell.precoCapa).toFixed(2);
			};
			
		});
		
		return resultado;
		
	},
	
	 moeda:function(z){ 
		v = z.value; 
		v=v.replace(/\D/g,""); // permite digitar apenas numero 
		v=v.replace(/(\d{1})(\d{1,2})$/,"$1,$2"); // coloca virgula antes dos ultimos 2 digitos 
		z.value = v; 
	},
	
	validarCamposDeAte:function(){
		if($("#faixaDe").val() ==''|| $("#faixaAte").val()=='' || $("#faixaDe").val() == undefined || $("#faixaAte").val()==undefined){
			return false;
		}else{
			return floatValue($("#faixaDe").val()) > floatValue($("#faixaAte").val());
		}
		 
	},
	
	validarBotaoExportarSimples:function(numRegistros){
		if(numRegistros == 0){
			$("#linkGerarSimples").attr("href","#");
			$("#linkGerarSimples").attr("onclick","caracteristicaDistribuicaoController.exibeMsgValidacaoExportarErro()");
			$("#linkImprimirSimples").attr("href","#");
			$("#linkImprimirSimples").attr("onclick","caracteristicaDistribuicaoController.exibeMsgValidacaoExportarErro()");
		}else{
			$("#linkGerarSimples").attr("href", contextPath+"/distribuicao/caracteristicaDistribuicao/exportarSimples?fileType=XLS");
			$("#linkGerarSimples").attr("onclick", "javascript:void(0)");
			$("#linkImprimirSimples").attr("href",contextPath+"/distribuicao/caracteristicaDistribuicao/exportarSimples?fileType=PDF");
			$("#linkImprimirSimples").attr("onclick","javascript:void(0)");
		}
	},
	
	validarBotaoExportarDetalhe:function(numRegistros){
		if(numRegistros == 0){
			$("#linkGerarDetalhe").attr("href","#");
			$("#linkGerarDetalhe").attr("onclick","caracteristicaDistribuicaoController.exibeMsgValidacaoExportarErro()");
			$("#linkImprimirDetalhe").attr("href","#");
			$("#linkImprimirDetalhe").attr("onclick","caracteristicaDistribuicaoController.exibeMsgValidacaoExportarErro()");
		}else{
			$("#linkGerarDetalhe").attr("href", contextPath+"/distribuicao/caracteristicaDistribuicao/exportarDetalhe?fileType=XLS");
			$("#linkGerarDetalhe").attr("onclick", "javascript:void(0)");
			$("#linkImprimirDetalhe").attr("href",contextPath+"/distribuicao/caracteristicaDistribuicao/exportarDetalhe?fileType=PDF");
			$("#linkImprimirDetalhe").attr("onclick","javascript:void(0)");
		}
	},
	
	
	exibeMsgValidacaoExportarErro:function(){
		exibirMensagem("WARNING",["Não há registros a serem exportados!"]);
	},
	
	exibirCapa:function(codProduto, numeroEdicao){
		var params = [];
		params.push({name:'codigoProduto',	value: codProduto});
		params.push({name:'numeroEdicao', value: numeroEdicao});
		$.postJSON(contextPath + '/distribuicao/caracteristicaDistribuicao/pesquisarDetalhe',params);
	},
	
	

	 popup_detalhes_close:function() {
	  $( "#dialog-detalhes" ).dialog( "close" );
	  
	},
	
	popup_detalhes:function(codigoProduto,numeroEdicao) {
		console.log(codigoProduto+","+numeroEdicao);
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
				
				$("#imagemCapaEdicao").attr("src",contextPath
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
	
	
	pesquisar:function(){
		
		if(caracteristicaDistribuicaoController.isSimples()) {
			
			caracteristicaDistribuicaoController.pesquisarSimples();
		} else {
			
			caracteristicaDistribuicaoController.pesquisarDetalhe();
		}
	},
	
	pesquisarDetalhe:function(){
		if(caracteristicaDistribuicaoController.camposVazios()){
			exibirMensagem("WARNING",["Preencha pelo menos um campo dos filtros para realizar a pesquisa!"]);
		}else if (caracteristicaDistribuicaoController.validarCamposDeAte()){
			exibirMensagem("WARNING",["O campo 'De' não pode conter valores maiores que o campo 'Até'!"]);
		}else{
				caracteristicaDistribuicaoController.exibeGridDetalhe();
				$(".pesquisaDetalheGrid",caracteristicaDistribuicaoController.workspace).flexOptions({
					url: contextPath + "/distribuicao/caracteristicaDistribuicao/pesquisarDetalhe",
					dataType : 'json',
					params: caracteristicaDistribuicaoController.getDadosFiltroPesquisaDetalhe()
				});
				
				$(".divPesquisaSimplesGrid").hide();
				$(".pesquisaDetalheGrid",caracteristicaDistribuicaoController.workspace).flexReload();
		}
		
	},
	
	pesquisarSimples: function() {
		
		if(caracteristicaDistribuicaoController.camposVazios()) {
			
			exibirMensagem("WARNING",["Preencha pelo menos um campo dos filtros para realizar a pesquisa!"]);
		} else if (caracteristicaDistribuicaoController.validarCamposDeAte()) {
			
			exibirMensagem("WARNING",["O campo 'De' não pode conter valores maiores que o campo 'Até'!"]);
		} else {
				caracteristicaDistribuicaoController.exibeGridSimples();
				$(".pesquisaSimplesGrid",caracteristicaDistribuicaoController.workspace).flexOptions({
					url: contextPath + "/distribuicao/caracteristicaDistribuicao/pesquisarSimples",
					dataType : 'json',
					params: caracteristicaDistribuicaoController.getDadosFiltroPesquisaDetalhe()
				});
				
				$(".divPesquisaDetalheGrid").hide();
				$(".pesquisaSimplesGrid",caracteristicaDistribuicaoController.workspace).flexReload();
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
	
	
	isSimples:function(){
		var codigoPreenchido = $("#codigoProduto").val().length >0;
		var produtoPreenchido = $("#nomeProduto").val().length>0;
		var nomeEditorPreenchido = $("#nomeEditor").val().length>0;
		var segmentoVazio =  $("#segmento").val()=="";
		var brindeVazio = $("#brinde").val()=="";
		var classificacaoVazio =  $("#classificacao").val()=="";
		var chamadaCapaVazio = $("#chamadaCapa").val()==""; 
		var faixaDeVazio=$("#faixaDe").val()=="";
		var faixaAteVazio=$("#faixaAte").val()=="";
		
		var outrosCamposVazios =classificacaoVazio  &&  segmentoVazio && brindeVazio && faixaDeVazio && faixaAteVazio && chamadaCapaVazio;
		
		return (outrosCamposVazios &&(codigoPreenchido || produtoPreenchido ||nomeEditorPreenchido) );
	},

	
	getDadosFiltroPesquisaDetalhe:function() {
		var valDe = $("#faixaDe", this.workspace).val();
		var valAte = $("#faixaAte", this.workspace).val();
		var data = [];
		data.push({name:'filtro.codigoProduto',	value: $("#codigoProduto", this.workspace).val()});
		data.push({name:'filtro.nomeProduto', value: $("#nomeProduto", this.workspace).val()});
		data.push({name:'filtro.nomeEditor', value: $("#nomeEditor", this.workspace).val()});
		data.push({name:'filtro.opcaoFiltroPublicacao', value:$('#checkPublicacaoExato').is(":checked")});
		
		data.push({name:'filtro.classificacaoProduto', value: $("#classificacao option:selected", this.workspace).val()});
		data.push({name:'filtro.segmento', value: $("#segmento option:selected", this.workspace).val()});
		data.push({name:'filtro.brinde', value: $("#brinde option:selected", this.workspace).val()});
		data.push({name:'filtro.chamadaCapa', value:$('#chamadaCapa', this.workspace).val()});
		data.push({name:'filtro.faixaPrecoDe', value: valDe.replace(",",".")});
		data.push({name:'filtro.faixaPrecoAte', value: valAte.replace(",",".")});
		
		return data;
	},
	
	exibeGridSimples:function() {
		$('.divPesquisaSimplesGrid').show();
		
	},
	escondeGridSimples:function() {
		$(".divPesquisaSimplesGrid").hide();
		
	},
	exibeGridDetalhe:function(){
		$('.divPesquisaDetalheGrid').show();
	},
	escondeGridDetalhe:function(){
		
		$('.divPesquisaDetalheGrid').hide();
	},
	exibeGridDetalheModal:function(){
		$('#divPesquisaDetalheModal').show();
	}

}, BaseController);
//@ sourceURL=caracteristicaDistribuicao.js