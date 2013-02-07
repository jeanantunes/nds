var edicoesEscolhidas = new Array();
var nomeProduto='';
var descricaoTipoProduto='';
var codigoProduto="";

function checkEdicao(check){
	//console.log(check.checked);
	
	var obj = jQuery.parseJSON(check.value);
	
	if(edicoesEscolhidas.indexOf(obj.edicao)>-1){
		
		var len = edicoesEscolhidas.length;
		
		//remover do array
		edicoesEscolhidas.splice(edicoesEscolhidas.indexOf(obj.edicao),1);
		//liberar os checkboxs para remarcação
		if(len==6)
			$(".checkEdicao").removeAttr("disabled");
		
	}else{
		//Adicionar no array
		edicoesEscolhidas.push(obj.edicao);
		//Verfificar se alcançou 6, case seja 6, desabilitar todos os checks
		if(edicoesEscolhidas.length==6){
			$(".checkEdicao:not(:checked)").attr("disabled","disabled");
		}
	}
	//console.log(edicoesEscolhidas.toString());
}

var histogramaVendasController = $.extend(true, { 
	
	
	iniciarGrid: function(){
		
		$(".edicaoProdCadastradosGrid",this.workspace).flexigrid({
			dataType : 'json',
			preProcess: function (data){
				
				if (data.result){
					
					data = data.result;
				}
				
				$.each(data.rows, function(index, value) {						

					nomeProduto=value.cell.nomeProduto;
					descricaoTipoProduto=value.cell.descricaoTipoProduto;
					codigoProduto=value.cell.codigoProduto;
					
					var objString = 
						'{"codigo":"'+ value.cell.codigoProduto
					+ '","edicao":"'+ value.cell.edicao
					+ '","nomeProduto":"'+ value.cell.nomeProduto
					+ '","descricaoTipoProduto":"'+ value.cell.descricaoTipoProduto
					+ '"}'; 
					
					//verificando se já estão escolhidas 6 edicoes para analise do histograma
					var disabled=(edicoesEscolhidas.length==6)?"disabled='disabled'":""
					value.cell.sel = "<input type='checkbox'  class='checkEdicao' value='"+objString+"' "+disabled+" onclick='checkEdicao(this)'/>";
					
					//setando atributo para capa
					value.cell.capa = "<a onmouseover='popup_detalhes("+value.cell.codigoProduto+","+value.cell.edicao+");' onmouseout='popup_detalhes_close();' href='javascript:void(0);'><img src='images/ico_detalhes.png'  /></a>";
				});
				
				return data;
			},
			colModel : [ {
				display : 'Edição',
				name : 'edicao',
				width : 60,
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
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Venda',
				name : 'venda',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Data Lançamento',
				name : 'dataLancamento',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Recolhimento',
				name : 'dataRecolhimento',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : 'Status',
				name : 'status',
				width : 240,
				sortable : true,
				align : 'left'
			}, {
				display : 'Capa',
				name : 'capa',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			width : 960,
			height : 160,
			rp : 15,
			showTableToggleBtn : true,
		});
	},
	
	realizarAnalise:function(){
		if(edicoesEscolhidas.length==0){
			exibirMensagem("WARNING", ["Selecione ao menos uma edição."]);
			return;
		}
		
		//$.getJSON(contextPath + "/distribuicao/histogramaVendas/analiseHistograma");
		
		var faixas = new Array();
		
		for ( var int = 0; int < faixasVenda.length; int++) {
			var obj = faixasVenda[int];
			if(obj.cell.enabled)
				faixas.push(obj.cell.faixaReparteDe+"-"+obj.cell.faixaReparteAte);
		}
//		console.log(faixas);
		
		var labelComponente="",labelElemento="";
		if($("#inserirComponentes").is(":checked") && $("#componente").val()!="-1" && $("#elemento").val()!="-1"){
			labelComponente=$("#componente").children("option:selected:first").text();
			labelElemento=$("#elemento").children("option:selected:first").text();
			
		}
		
		var data = {"edicoes":edicoesEscolhidas.sort().toString(),
				"segmento":descricaoTipoProduto,
				"nomeProduto":nomeProduto,
				"faixasVenda":faixas,
				"codigoProduto":codigoProduto,
				"labelComponente":labelComponente,
				"labelElemento":labelElemento};
		
		$.post(contextPath + "/distribuicao/histogramaVendas/analiseHistograma", data, function(data){
	      if(data){ 
	    	  $("#histogramaVendasContent").hide();
	    	  $('#analiseHistogramaVendasContent').html(data);
	    	  
	    	  anaLiseHistogramaController.iniciarGridAnalise();
	      }
	    });
		
	},
	
	init: function(){
		this.iniciarGrid();
		
		//
		$("#componente").change(function(){
			  carregarCombo(contextPath + "/distribuicao/histogramaVendas/carregarElementos", 
					  {"componente":$("#componente").val()},
			            $("#elemento", this.workspace), null, null);
		});
	},
	
	getFormFiltro: function(){
		var selector="input[type='radio'][name='filtroPor']:checked,#inserirComponentes,#componente,#elemento,#codigo,#produto,#edicao";
		
		var formData = new Array();
		
		$(selector).each(function(idx,comp){
//			console.log("filtro."+comp.getAttribute('name')+"=="+comp.value);
			formData.push({name:"filtro."+comp.getAttribute('name'),value:comp.value});
		});
		
		return formData;
	},
	
	pesquisarFiltro: function (){
		
		$("#edicaoProdCadastradosGrid", histogramaVendasController.workspace).flexOptions({
			url: contextPath + "/distribuicao/histogramaVendas/consultar",
			dataType : 'json',
			params: histogramaVendasController.getFormFiltro(),newp: 1
		});
		
		$("#edicaoProdCadastradosGrid", histogramaVendasController.workspace).flexReload();

	}


});
	
	
function popup_detalhes(codigoProduto,numeroEdicao) {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	//histogramaVendasController.getCapaEdicao(codigo,edicao);
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
			
			var randomnumber=Math.floor(Math.random()*11)
			
			$("#imagemCapaEdicao")
					.attr("src",contextPath
									+ "/distribuicao/histogramaVendas/getCapaEdicaoJson?random="+randomnumber+"&codigoProduto="
//									+ "/capas/revista-nautica-11.jpg?codigoProduto="
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

function popup_detalhes_close() {
  $( "#dialog-detalhes" ).dialog( "close" );
  
}

function filtroTodas(){
	$('.filtroTodas').show();
	$('.filtroPracaSede').hide();
	$('.filtroPracaAtendida').hide();
	$('.filtroComponentes').hide();
}
function filtroSede(){
	$('.filtroTodas').hide();
	$('.filtroPracaSede').show();
	$('.filtroPracaAtendida').hide();
	$('.filtroComponentes').hide();
}
function filtroAtendida(){
	$('.filtroTodas').hide();
	$('.filtroPracaSede').hide();
	$('.filtroPracaAtendida').show();
	$('.filtroComponentes').hide();
}
function filtroComponentes(){
	$('.filtroTodas').hide();
	$('.filtroPracaSede').hide();
	$('.filtroPracaAtendida').hide();
	$('.filtroComponentes').show();
}

/*
$(".edicaoSelecionadaGrid").flexigrid({
			url : '../xml/pesqEdicaoB-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'CÃ³digo',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			},{
				display : 'Produto',
				name : 'produto',
				width : 180,
				sortable : true,
				align : 'left'
			},{
				display : 'EdiÃ§Ã£o',
				name : 'edicao',
				width : 45,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'Venda',
				name : 'venda',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'AÃ§Ã£o',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			width : 480,
			height : 110
		});
$(".edicaoProdCadastradosGrid").flexigrid({
			url : '../xml/pesqEdicaoC-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'EdiÃ§Ã£o',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'PerÃ­odo',
				name : 'periodo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Venda',
				name : 'venda',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Data LanÃ§amento',
				name : 'dtLancamento',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Recolhimento',
				name : 'dtRecolhimento',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : 'Status',
				name : 'status',
				width : 240,
				sortable : true,
				align : 'left'
			}, {
				display : 'Capa',
				name : 'capa',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			width : 960,
			height : 160
		});
$(".segmentoCotaGrid").flexigrid({
			url : '../xml/segmentoCotaGrid-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Segmento',
				name : 'segmento',
				width : 260,
				sortable : true,
				align : 'left'
			}, {
				display : 'UsuÃ¡rio',
				name : 'usuario',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora',
				name : 'hora',
				width : 80,
				sortable : true,
				align : 'center'
			},  {
				display : 'AÃ§Ã£o',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 510,
			height : 250
		});
	$(".pesqBancasGrid").flexigrid({
			url : '../xml/pesqBancas-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 400,
				sortable : true,
				align : 'left'
			},  {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});
		
		$(".pesqHistoricoGrid").flexigrid({
			url : '../xml/pesqHistorico-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			},  {
				display : 'Nome',
				name : 'nome',
				width : 270,
				sortable : true,
				align : 'left'
			},  {
				display : 'AÃ§Ã£o',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			width : 415,
			height : 205
		});
		$(".segmentosGrid").flexigrid({
			url : '../xml/segmentos-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 160,
				sortable : true,
				align : 'left'
			},  {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			width : 300,
			height : 235
		});
		
	$(".segmentoNaoRecebidaGrid").flexigrid({
			url : '../xml/segmentoNaoRecebidaGrid-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'UsuÃ¡rio',
				name : 'usuario',
				width : 115,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora',
				name : 'hora',
				width : 60,
				sortable : true,
				align : 'center'
			},  {
				display : 'AÃ§Ã£o',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 630,
			height : 250
		});
	*/