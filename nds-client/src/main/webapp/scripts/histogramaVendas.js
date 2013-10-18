var edicoesEscolhidas_HistogramaVenda = new Array();
var nomeProduto='';
var descricaoTipoSegmento='';
var codigoProduto_HistogramaVenda="";
var descricaoTipoClassificacao_histogramaVenda="";

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
				$.each(data.rows, function(index,row){
					row.cell.reparte = parseInt(row.cell.reparte, 10);
					row.cell.venda = parseInt(row.cell.venda, 10);
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

					nomeProduto=value.cell.nomeProduto;
					descricaoTipoSegmento=value.cell.descricaoTipoSegmento;
					codigoProduto_HistogramaVenda=value.cell.codigoProduto;
					descricaoTipoClassificacao_histogramaVenda=value.cell.descricaoTipoClassificacao;
					
					var objString = 
						'{"codigo":"'+ value.cell.codigoProduto
					+ '","edicao":"'+ value.cell.edicao
					+ '","nomeProduto":"'+ value.cell.nomeProduto
					+ '","descricaoTipoSegmento":"'+ value.cell.descricaoTipoSegmento
					+ '"}'; 
					
					//verificando se já estão escolhidas 6 edicoes para analise do histograma
					var disabled=(edicoesEscolhidas_HistogramaVenda.length==6)?"disabled='disabled'":"";
					value.cell.sel = "<input type='checkbox'  class='checkEdicao' value='"+objString+"' "+disabled+" onclick='checkEdicao(this)'/>";
					
					//setando atributo para capa
					value.cell.capa = "<a onmouseover='popup_detalhes("+value.cell.codigoProduto+","+value.cell.edicao+");' onmouseout='popup_detalhes_close();' href='javascript:void(0);'><img src='images/ico_detalhes.png'  /></a>";
				});
				
				return data;
			},
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Classificação',
				name : 'descricaoTipoClassificacao',
				width : 100,
				sortable : true,
				align : 'left'
			},{
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
				width : 100,
				sortable : true,
				align : 'left'
			},
			{
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
			useRp : true,
			sortname : "codigoProduto",
            sortorder : "asc"
		});
	},
	
	realizarAnalise:function(){
		if(edicoesEscolhidas_HistogramaVenda.length==0){
			exibirMensagem("WARNING", ["Selecione ao menos uma edição."]);
			return;
		}
		
		var labelComponente="",labelElemento="";
		if($("#inserirComponentes").is(":checked") && $("#componente").val()!="-1" && $("#elemento").val()!="-1"){
			labelComponente=$("#componente").children("option:selected:first").text();
			labelElemento=$("#elemento").children("option:selected:first").text();
			
		}
		
		var data = {"edicoes":edicoesEscolhidas_HistogramaVenda.sort().toString(),
				"segmento":descricaoTipoSegmento,
				"nomeProduto":nomeProduto,
				"codigoProduto":codigoProduto_HistogramaVenda,
				"classificacaoLabel":descricaoTipoClassificacao_histogramaVenda,
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
		
		var autoComplete = new AutoCompleteController(histogramaVendasController.workspace);
		
		$('#produto').keyup(function () {
			
			autoComplete.autoCompletar("/produto/autoCompletarPorNomeProdutoAutoComplete",'#codigo','#produto');
		});
		
		autoComplete.limparCampoOnChange('#produto', new Array('#codigo','#edicao'));
		
		$('#codigo').change(function () {
			
			autoComplete.pesquisarPorCodigo("/produto/pesquisarPorCodigoProdutoAutoComplete",'#codigo','#produto');
		});
		
		autoComplete.limparCampoOnChange('#codigo', new Array('#produto','#edicao'));
		
		this.iniciarGrid();
		
		//
		$("#componente").change(function(){
			
			if ($('#componente').val() !== "-1") {
			  carregarCombo(contextPath + "/distribuicao/histogramaVendas/carregarElementos", 
					  {"componente":$("#componente").val()},
			            $("#elemento", this.workspace), null, null);
			}else{
				$('#elemento').html('');
				$('#elemento').append("<option value='-1'>Selecione...</option>");
			}
		});
	},
	
	getFormFiltro: function(){
		var selector = "",
			formData = new Array();
		
		if ($('#inserirComponentes').is(':checked')) {
			selector = "input[type='radio'][name='filtroPor']:checked,#inserirComponentes,#componente,#elemento,#codigo,#produto,#edicao";
		}else {
			selector = "input[type='radio'][name='filtroPor']:checked,#codigo,#produto,#edicao";
		}
		
		
		$(selector).each(function(idx,comp){
			formData.push({name:"filtro."+comp.getAttribute('name'),value:comp.value});
		});
		
		formData.push({name:"classificacaoId", value:$("#idTipoClassificacaoProduto").val()});
		
		return formData;
	},
	
	pesquisarFiltro: function (){
		
		$("#edicaoProdCadastradosGrid", histogramaVendasController.workspace).flexOptions({
			url: contextPath + "/distribuicao/histogramaVendas/consultar",
			dataType : 'json',
			params: histogramaVendasController.getFormFiltro(),newp: 1
		});
		
		$("#edicaoProdCadastradosGrid", histogramaVendasController.workspace).flexReload();
		
		edicoesEscolhidas_HistogramaVenda = new Array();
		nomeProduto='';
		descricaoTipoSegmento='';
		codigoProduto_HistogramaVenda="";
		descricaoTipoClassificacao_histogramaVenda="";

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
	$('#inserirComponentes').attr('checked', false);
}
function filtroAtendida(){
	$('.filtroTodas').hide();
	$('.filtroPracaSede').hide();
	$('.filtroPracaAtendida').show();
	$('.filtroComponentes').hide();
	$('#inserirComponentes').attr('checked', false);
}
function filtroComponentes(){
	$('.filtroTodas').hide();
	$('.filtroPracaSede').hide();
	$('.filtroPracaAtendida').hide();
	$('.filtroComponentes').show();
}
//@ sourceURL=historicoVenda.js
