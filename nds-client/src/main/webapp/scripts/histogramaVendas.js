var edicoesEscolhidas_HistogramaVenda = new Array();
var nomeProduto='';
var descricaoTipoProduto='';
var codigoProduto_HistogramaVenda="";

function checkEdicao(check){
	//console.log(check.checked);
	
	var obj = jQuery.parseJSON(check.value);
	
	if(edicoesEscolhidas_HistogramaVenda.indexOf(obj.edicao)>-1){
		
		var len = edicoesEscolhidas_HistogramaVenda.length;
		
		//remover do array
		edicoesEscolhidas_HistogramaVenda.splice(edicoesEscolhidas_HistogramaVenda.indexOf(obj.edicao),1);
		//liberar os checkboxs para remarcação
		if(len==6)
			$(".checkEdicao").removeAttr("disabled");
		
	}else{
		//Adicionar no array
		edicoesEscolhidas_HistogramaVenda.push(obj.edicao);
		//Verfificar se alcançou 6, case seja 6, desabilitar todos os checks
		if(edicoesEscolhidas_HistogramaVenda.length==6){
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
					codigoProduto_HistogramaVenda=value.cell.codigoProduto;
					
					var objString = 
						'{"codigo":"'+ value.cell.codigoProduto
					+ '","edicao":"'+ value.cell.edicao
					+ '","nomeProduto":"'+ value.cell.nomeProduto
					+ '","descricaoTipoProduto":"'+ value.cell.descricaoTipoProduto
					+ '"}'; 
					
					//verificando se já estão escolhidas 6 edicoes para analise do histograma
					var disabled=(edicoesEscolhidas_HistogramaVenda.length==6)?"disabled='disabled'":""
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
			rp : 10,
			showTableToggleBtn : true,
			usepager : true,
			useRp : true
		});
	},
	
	realizarAnalise:function(){
		if(edicoesEscolhidas_HistogramaVenda.length==0){
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
		
		var data = {"edicoes":edicoesEscolhidas_HistogramaVenda.sort().toString(),
				"segmento":descricaoTipoProduto,
				"nomeProduto":nomeProduto,
				"faixasVenda":faixas,
				"codigoProduto":codigoProduto_HistogramaVenda,
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
			
			var randomnumber=Math.floor(Math.random()*11);
			
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
