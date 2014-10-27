var histogramaVendasController = $.extend(true, { 
	
	edicoesEscolhidas_HistogramaVenda: new Array(),
	descricaoTipoSegmento: '',
	codigoProduto_HistogramaVenda: '',
	descricaoTipoClassificacao_histogramaVenda: "",
	
	checkEdicao : function(check){
		//console.log(check.checked);
		
		var obj = jQuery.parseJSON(check.value);
		
		if(histogramaVendasController.edicoesEscolhidas_HistogramaVenda.indexOf(obj.edicao)>-1){
			
			var len = histogramaVendasController.edicoesEscolhidas_HistogramaVenda.length;
			
			//remover do array
			histogramaVendasController.edicoesEscolhidas_HistogramaVenda.splice(
				histogramaVendasController.edicoesEscolhidas_HistogramaVenda.indexOf(obj.edicao),1);
			
			//liberar os checkboxs para remarcação
			if(len==6){
				$(".checkEdicao", histogramaVendasController.workspace).removeAttr("disabled");
			}
			
		}else{
			//Adicionar no array
			histogramaVendasController.edicoesEscolhidas_HistogramaVenda.push(obj.edicao);
			
			//Verfificar se alcançou 6, case seja 6, desabilitar todos os checks
			if(histogramaVendasController.edicoesEscolhidas_HistogramaVenda.length==6){
				$(".checkEdicao:not(:checked)", histogramaVendasController.workspace).attr("disabled","disabled");
			}
		}
		//console.log(edicoesEscolhidas.toString());
	},
	
	iniciarGrid: function(){
		
		$("#edicaoProdCadastradosGrid", histogramaVendasController.workspace).flexigrid({
			dataType : 'json',
			preProcess: function (data){
				$.each(data.rows, function(index,row){
					row.cell.reparte = parseInt(row.cell.reparte, 10);
					row.cell.venda = row.cell.venda ? parseInt(row.cell.venda, 10) : '';
					
					if(row.cell.periodo == undefined){
						row.cell.periodo = "";
					}
					
				});
				
				if (data.mensagens) {

					exibirMensagem(
						data.mensagens.tipoMensagem, 
						data.mensagens.listaMensagens
					);
					
					return data;
				}
				
				if (data.result){
					
					data = data.result;
				}
				
				$.each(data.rows, function(index, value) {						
					
					histogramaVendasController.descricaoTipoSegmento=value.cell.descricaoTipoSegmento;
					
					var objString = 
						'{"codigo":"'+ value.cell.codigoProduto
					+ '","edicao":"'+ value.cell.edicao
					+ '","nomeProduto":"'+ value.cell.nomeProduto
					+ '","descricaoTipoSegmento":"'+ value.cell.descricaoTipoSegmento
					+ '"}'; 
					
					//verificando se já estão escolhidas 6 edicoes para analise do histograma
					var disabled=
						(histogramaVendasController.edicoesEscolhidas_HistogramaVenda.length==6)?"disabled='disabled'":"";
					value.cell.sel = "<input type='checkbox'  class='checkEdicao' value='"+objString+"' "+disabled+" onclick='histogramaVendasController.checkEdicao(this)'/>";
					
					//setando atributo para capa
					value.cell.capa = "<a onmouseover='histogramaVendasController.popup_detalhes("+value.cell.codigoProduto+","+value.cell.edicao+");' onmouseout='histogramaVendasController.popup_detalhes_close();' href='javascript:void(0);'><img src='images/ico_detalhes.png'  /></a>";
				});
				
				$("#fieldsetEdicoesProduto", histogramaVendasController.workspace).show();
				
				return data;
			},
			colModel : [{
				display : 'Sel',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			},{
				display : 'Código',
				name : 'codigoProduto',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Classificação',
				name : 'descricaoTipoClassificacao',
				width : 90,
				sortable : true,
				align : 'left'
			},{
				display : 'Edição',
				name : 'edicao',
				width : 55,
				sortable : true,
				align : 'left'
			},{
				display : 'Período',
				name : 'periodo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Venda',
				name : 'venda',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Data Lançamento',
				name : 'dataLancamento',
				width : 120,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Recolhimento',
				name : 'dataRecolhimento',
				width : 120,
				sortable : true,
				align : 'center'
			}, {
				display : 'Status',
				name : 'status',
				width : 90,
				sortable : true,
				align : 'left'
			},
			{
				display : 'Capa',
				name : 'capa',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			width : 950,
			height : 160,
			rp : 10,
			showTableToggleBtn : true,
			usepager : true,
			useRp : true,
			sortname : "dataLancamento",
            sortorder : "desc"
		});
	},
	
	realizarAnalise:function(){
		if(histogramaVendasController.edicoesEscolhidas_HistogramaVenda.length==0){
			exibirMensagem("WARNING", ["Selecione ao menos uma edição."]);
			return;
		}
		
		
		var codigoProduto = undefined, nomeProduto = undefined, json;
		$.each(
			$(".checkEdicao:checked", histogramaVendasController.workspace), 
			function(index, item){
				
				json = $.parseJSON(item.value);
				
				if (!codigoProduto){
					
					codigoProduto = json.codigo;
					nomeProduto = json.nomeProduto;
					histogramaVendasController.codigoProduto_HistogramaVenda = json.codigo;
					return true;
				}
				
				if (codigoProduto !== json.codigo){
					
					codigoProduto = undefined;
					return false;
				}
			}
		);
		
		if (!codigoProduto){
			
			exibirMensagem("WARNING", ["Selecione apenas edições de um mesmo produto."]);
			return;
		}
		
		var labelComponente="",labelElemento="";
		if($("#inserirComponentes", histogramaVendasController.workspace).is(":checked") && 
				$("#componente", histogramaVendasController.workspace).val()!="-1" && 
				$("#elemento", histogramaVendasController.workspace).val()!="-1"){
			
			labelComponente=
				$("#componente", histogramaVendasController.workspace).children("option:selected:first").text();
			
			labelElemento=
				$("#elemento", histogramaVendasController.workspace).children("option:selected:first").text();
		}

        var classificacoes = 
        	$('table#edicaoProdCadastradosGrid:visible input[type="checkbox"]:checked', histogramaVendasController.workspace)
        		.closest('tr').find('td[abbr="descricaoTipoClassificacao"]').map(function(){return $(this).text();}).toArray();
        var uniqueClassificacoes = [];
        $.each(classificacoes, function(i, el){
            if($.inArray(el, uniqueClassificacoes) === -1) uniqueClassificacoes.push(el);
        });

		var data = {"edicoes":histogramaVendasController.edicoesEscolhidas_HistogramaVenda.sort().toString(),
				"segmento":histogramaVendasController.descricaoTipoSegmento,
				"nomeProduto":nomeProduto,
				"codigoProduto":codigoProduto,
				"classificacaoLabel":uniqueClassificacoes.join(', '),
				"labelComponente":labelComponente,
				"labelElemento":labelElemento};
		
		$.post(contextPath + "/distribuicao/histogramaVendas/analiseHistograma", data, 
			function(data){
				
				if (data.mensagens){
					
					exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
					return;
				}
				
				if(data){ 
					$("#histogramaVendasContent", histogramaVendasController.workspace).hide();
					$('#analiseHistogramaVendasContent', histogramaVendasController.workspace).html(data);
					anaLiseHistogramaController.iniciarGridAnalise();
					$('#histogramaVenda_botaoAnalise').hide();
					bloquearItensEdicao(histogramaVendasController.workspace);
				}
	    	}
		);
		
	},
	
	init: function(){
		
		var autoComplete = new AutoCompleteController(histogramaVendasController.workspace);
		
		$('#produto', histogramaVendasController.workspace).keyup(function () {
			
			autoComplete.autoCompletar("/produto/autoCompletarPorNomeProdutoAutoComplete",'#codigo','#produto');
		});
		
		autoComplete.limparCampoOnChange('#produto', new Array('#codigo','#edicao'));
		
		$('#codigo', histogramaVendasController.workspace).change(function () {
			
			autoComplete.pesquisarPorCodigo("/produto/pesquisarPorCodigoProdutoAutoComplete",'#codigo','#produto');
		});
		
		autoComplete.limparCampoOnChange('#codigo', new Array('#produto','#edicao'));
		
		this.iniciarGrid();
		
		//
		$("#componente", histogramaVendasController.workspace).change(function(){
			
			if ($('#componente', histogramaVendasController.workspace).val() !== "-1") {
			  carregarCombo(contextPath + "/distribuicao/histogramaVendas/carregarElementos", 
					  {"componente":$("#componente", histogramaVendasController.workspace).val()},
			            $("#elemento", histogramaVendasController.workspace), null, null);
			}else{
				$('#elemento', histogramaVendasController.workspace).html('');
				$('#elemento', histogramaVendasController.workspace).append("<option value='-1'>Selecione...</option>");
			}
		});
	},
	
	getFormFiltro: function(){
		var selector = "",
			formData = new Array();
		
		if ($('#inserirComponentes', histogramaVendasController.workspace).is(':checked')) {
			selector = "input[type='radio'][name='filtroPor']:checked,#inserirComponentes,#componente,#elemento,#codigo,#produto,#edicao";
		}else {
			selector = "input[type='radio'][name='filtroPor']:checked,#codigo,#produto,#edicao,#idTipoClassificacaoProduto";
		}
		
		
		$(selector, histogramaVendasController.workspace).each(function(idx,comp){
			
			if (comp.type == 'radio'){
				
				if (comp.checked){
					
					formData.push({name:"filtro."+comp.getAttribute('name'),value:comp.value});
				}
				
				return true;
			}
			
			formData.push({name:"filtro."+comp.getAttribute('name'),value:comp.value});
		});
		
		formData.push({name:"classificacaoId", value:$("#idTipoClassificacaoProduto", histogramaVendasController.workspace).val()});
		
		return formData;
	},
	
	pesquisarFiltro: function (){
		
		$("#edicaoProdCadastradosGrid", histogramaVendasController.workspace).flexOptions({
			url: contextPath + "/distribuicao/histogramaVendas/consultar",
			dataType : 'json',
			params: histogramaVendasController.getFormFiltro(),newp: 1
		});
		
		$("#edicaoProdCadastradosGrid", histogramaVendasController.workspace).flexReload();
		
		histogramaVendasController.edicoesEscolhidas_HistogramaVenda = new Array();
		histogramaVendasController.descricaoTipoClassificacao_histogramaVenda = $("#idTipoClassificacaoProduto", histogramaVendasController.workspace).val();

	}, 
	
	popup_detalhes : function(codigoProduto,numeroEdicao) {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		//histogramaVendasController.getCapaEdicao(codigo,edicao);
		$( "#dialog-detalhes", histogramaVendasController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: false,
			open : function(event, ui) {
				
				$("#imagemCapaEdicao", histogramaVendasController.workspace).one('load', function() {
						$("#imagemCapaEdicao", histogramaVendasController.workspace).show();
						$("#loadingCapa", histogramaVendasController.workspace).hide();
					}).each(function() {
					  if(this.complete) $(this).load();
					});
				
				var randomnumber=Math.floor(Math.random()*11);
				
				$("#imagemCapaEdicao", histogramaVendasController.workspace)
						.attr("src",contextPath
										+ "/capa/getCapaEdicaoJson?random="+randomnumber+"&codigoProduto="
										+ codigoProduto
										+ "&numeroEdicao="
										+ numeroEdicao);
			},
			close:function(event, ui){
				$("#imagemCapaEdicao", histogramaVendasController.workspace).removeAttr("src").hide();
				$("#loadingCapa", histogramaVendasController.workspace).show();
			}
		});
	},

	popup_detalhes_close : function() {
	  $( "#dialog-detalhes", histogramaVendasController.workspace).dialog( "close" );
	  
	},
	
	filtroTodas : function(){
		$('.filtroTodas', histogramaVendasController.workspace).show();
		$('.filtroPracaSede', histogramaVendasController.workspace).hide();
		$('.filtroPracaAtendida', histogramaVendasController.workspace).hide();
		$('.filtroComponentes', histogramaVendasController.workspace).hide();
	},
	
	filtroSede : function(){
		$('.filtroTodas', histogramaVendasController.workspace).hide();
		$('.filtroPracaSede', histogramaVendasController.workspace).show();
		$('.filtroPracaAtendida', histogramaVendasController.workspace).hide();
		$('.filtroComponentes', histogramaVendasController.workspace).hide();
		$('#inserirComponentes', histogramaVendasController.workspace).attr('checked', false);
	},
	
	filtroAtendida : function(){
		$('.filtroTodas', histogramaVendasController.workspace).hide();
		$('.filtroPracaSede', histogramaVendasController.workspace).hide();
		$('.filtroPracaAtendida', histogramaVendasController.workspace).show();
		$('.filtroComponentes', histogramaVendasController.workspace).hide();
		$('#inserirComponentes', histogramaVendasController.workspace).attr('checked', false);
	},
	
	filtroComponentes : function(){
		$('.filtroTodas', histogramaVendasController.workspace).hide();
		$('.filtroPracaSede', histogramaVendasController.workspace).hide();
		$('.filtroPracaAtendida', histogramaVendasController.workspace).hide();
		$('.filtroComponentes', histogramaVendasController.workspace).show();
	}
},BaseController);
//@ sourceURL=histogramaVendas.js
