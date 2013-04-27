function montarDados(){

	params = new Array();
	
	params.push({name : "filtro.produtoDto.codigoProduto", value :  codigoProduto_HistogramaVenda});
	for ( var int = 0; int < edicoesEscolhidas_HistogramaVenda.length; int++) {
		params.push({name : "filtro.listProdutoEdicaoDTO["+int+"].numeroEdicao", value :  edicoesEscolhidas_HistogramaVenda[int]});
		
	}

	//carregando popup superior para as edições
	url = contextPath + "/distribuicao/historicoVenda/pesquisaProduto";
	$.post(url, params, function(data){
		if(data){
						anaLiseHistogramaController.tempData = data;
						
						/* montando popup superior */ 
						$("#voltarBaseAnalise").click(function(){
							$("#analiseHistogramaVendasContent").show();
							$("#analiseHistoricoVendasContent").hide();
						});
						
						$("#analiseHistoricoPopUpNomeProduto").clear();
						$("#analiseHistoricoPopUpNomeProduto").append('<td class="class_linha_1"><strong>Produto:</strong></td>');
						
						// tr numeroEdicao
						$('#analiseHistoricoPopUpNumeroEdicao').html('')
						.append('<td class="class_linha_1"><strong>Edição:</strong></td>');
						
						// tr dataLancamento
						$('#analiseHistoricoPopUpDatalancamento').html('')
						.append('<td width="136" class="class_linha_2"><strong>Data Lançamento:</strong></td>');
						
						// tr reparte
						$('#analiseHistoricoPopUpReparte').html('')
						.append('<td class="class_linha_1"><strong>Reparte:</strong></td>');
						
						// tr venda
						$('#analiseHistoricoPopUpVenda').html('')
						.append('<td class="class_linha_2"><strong>Venda:</strong></td>');
						
						// carregando popUp_analiseHistoricoVenda
						for ( var int2 = 0; int2 < anaLiseHistogramaController.tempData.rows.length; int2++) {
							row = anaLiseHistogramaController.tempData.rows[int2];
							
							$("#analiseHistoricoPopUpNomeProduto").append('<td class="class_linha_1">'+row.cell.nomeProduto+'</td>');
							$("#analiseHistoricoPopUpNumeroEdicao").append('<td class="class_linha_1">'+row.cell.numeroEdicao+'</td>');
							$("#analiseHistoricoPopUpDatalancamento").append('<td width="130" align="center" class="class_linha_2">' + row.cell.dataLancamentoFormatada + '</td>');
							$("#analiseHistoricoPopUpReparte").append('<td align="right" class="class_linha_1">' + row.cell.repartePrevisto +'</td>');
							$("#analiseHistoricoPopUpVenda").append('<td align="right" class="class_linha_1">' + row.cell.qtdVendasFormatada + '</td>');
						}
						
						qtdEdicoesSelecionadas = 6 - anaLiseHistogramaController.tempData.rows.length; 
						
						// por estética de layout, insiro elementos td vazios
						for ( var int = 0; int < qtdEdicoesSelecionadas; int++) {
							$("#analiseHistoricoPopUpNomeProduto").append('<td class="class_linha_1"></td>');
							$("#analiseHistoricoPopUpNumeroEdicao").append('<td class="class_linha_1"></td>');
							$("#analiseHistoricoPopUpDatalancamento").append('<td width="130" align="center" class="class_linha_2"></td>');
							$("#analiseHistoricoPopUpReparte").append('<td align="right" class="class_linha_1"></td>');
							$("#analiseHistoricoPopUpVenda").append('<td align="right" class="class_linha_1"></td>');
						}
						
		}
	});
}

function updateFaixa(input,idx){
	if(input.value=='' || input.value==null || parseInt(input.value)==0 ||(parseInt(input.value) <= faixasVenda[idx-1].cell.faixaReparteDe)){
		if(faixasVenda[idx-1].cell.bkp){
			input.value = faixasVenda[idx-1].cell.bkp;
		}else{
			input.value = faixasVenda[idx-1].cell.faixaReparteAte;
		}
		return;
	}
	
	var val = parseInt(input.value);
	
	
	//atualiza primeiro o valor da faixa
	
	faixasVenda[idx-1].cell.faixaReparteAte = val;
	//bkp para o valor digitado
	faixasVenda[idx-1].cell.bkp = faixasVenda[idx-1].cell.faixaReparteAte;
	faixasVenda[idx-1].cell.inputfaixaReparteAte="<input type='text' value='"+val+"'style='width:60px;'"+((faixasVenda[idx-1].cell.enabled)?"":"disabled='disabled'")+" onblur='updateFaixa(this,"+(idx)+");' onkeyup='if(event.keyCode==13){updateFaixa(this,"+(idx)+");}' onkeydown='onlyNumeric(event);' />";
	
	//Atualiza o valor do campo "De" da próxima faixa
	if(idx<5){
		faixasVenda[idx].cell.faixaReparteDe = val+1;
		faixasVenda[idx].cell.formatFaixaDe="<font color='"+((faixasVenda[idx-1].cell.enabled)?"black":"gray")+"'  >"+(val+1)+" a</font>";
	}
	
	//atualiza o grid 
	var data = {
	        total: faixasVenda.length,    
	        page:1,
	        rows: faixasVenda
	};
	
	
	$('#faixasReparteGrid',anaLiseHistogramaController.workspace).flexAddData(data);
}

var faixasVendaOriginal=[
                  {id:1,cell:{"nome":"faixa1",
                	  "faixaReparteDe":0,
                	  "faixaReparteAte":10,
                	  formatFaixaDe:"<font>"+0+" a</font>",
                	  "enabled":true,
                	  "acao":"<input type='checkbox' checked='checked' onclick='checkFaixa(this);' value='1' />",
                	  inputfaixaReparteAte:"<input type='text' value='10'style='width:60px;' onkeyup='if(event.keyCode==13){updateFaixa(this,1);}' onblur='updateFaixa(this,1);' onkeydown='onlyNumeric(event);' />"}},
		                 {
			id : 2,
			cell : {
				"nome" : "faixa2",
				"faixaReparteDe" : 11,
				formatFaixaDe : "<font>" + 11 + " a</font>",
				"faixaReparteAte" : 20,
				"enabled" : true,
				"acao" : "<input type='checkbox' checked='checked' onclick='checkFaixa(this);' value='2' />",
				inputfaixaReparteAte : "<input type='text' value='20' style='width:60px;' onkeyup='if(event.keyCode==13){updateFaixa(this,2);}' onblur='updateFaixa(this,2);' onkeydown='onlyNumeric(event);' />"
			}
		},
		{
			id : 3,
			cell : {
				"nome" : "faixa3",
				"faixaReparteDe" : 21,
				formatFaixaDe : "<font>" + 21 + " a</font>",
				"faixaReparteAte" : 30,
				"enabled" : true,
				"acao" : "<input type='checkbox' checked='checked' onclick='checkFaixa(this);' value='3' />",
				inputfaixaReparteAte : "<input type='text' value='30' style='width:60px;' onkeyup='if(event.keyCode==13){updateFaixa(this,3);}' onblur='updateFaixa(this,3);' onkeydown='onlyNumeric(event);' />"
			}
		},
		{
			id : 4,
			cell : {
				"nome" : "faixa4",
				"faixaReparteDe" : 31,
				formatFaixaDe : "<font>" + 31 + " a</font>",
				"faixaReparteAte" : 40,
				"enabled" : true,
				"acao" : "<input type='checkbox' checked='checked' onclick='checkFaixa(this);' value='4' />",
				inputfaixaReparteAte : "<input type='text' value='40' style='width:60px;' onkeyup='if(event.keyCode==13){updateFaixa(this,4);}' onblur='updateFaixa(this,4);' onkeydown='onlyNumeric(event);'/>"
			}
		},
		{
			id : 5,
			cell : {
				"nome" : "faixa5",
				"faixaReparteDe" : 41,
				formatFaixaDe : "<font>" + 41 + " a</font>",
				"faixaReparteAte" : 999999,
				"enabled" : true,
				"acao" : "<input type='checkbox' checked='checked' onclick='checkFaixa(this);' value='5' />",
				//<input type='text' value='99999999' style='width:60px;' onkeyup='if(event.keyCode==13){updateFaixa(this,5);}' onblur='updateFaixa(this,5);' onkeydown='onlyNumeric(event);'/>
				inputfaixaReparteAte : "999.999"
			}
		} ];

var faixasVenda = [jQuery.extend(true, {}, faixasVendaOriginal[0]),
		jQuery.extend(true, {}, faixasVendaOriginal[1]),
		jQuery.extend(true, {}, faixasVendaOriginal[2]),
		jQuery.extend(true, {}, faixasVendaOriginal[3]),
		jQuery.extend(true, {}, faixasVendaOriginal[4])
				];


function checkFaixa(checkbox){
	var val = parseInt(checkbox.value); 
	var divChildrens = $(checkbox).parent()./*div*/parent()./*td*/parent()./*tr*/children().find("div");
	
	//desabilitar faixa de venda
	if(checkbox.checked){
		//console.log($(divChildrens).length);
		//text
		$(divChildrens).children().eq(0).css("color", "black");
		//input
		$(divChildrens).eq(1).children("input:first").removeAttr("disabled");
		//console.log($(divChildrens).eq(2).text());
		
	}else{
		//console.log($(divChildrens).length);
		//text
		$(divChildrens).children().eq(0).css("color", "gray");
		//input
		$(divChildrens).eq(1).children("input:first").attr("disabled","disabled");
		//console.log($(divChildrens).eq(2).text());

	}
	
	//alterando nos objetos que criam o grid
	faixasVenda[val-1].cell.enabled = checkbox.checked;
	faixasVenda[val-1].cell.acao = "<input type='checkbox'"+((checkbox.checked)?"checked='checked'":"")+" onclick='checkFaixa(this);' value='"+val+"' />";
	faixasVenda[val-1].cell.inputfaixaReparteAte = "<input type='text' value='"+faixasVenda[val-1].cell.faixaReparteAte+"'style='width:60px;' "+((!checkbox.checked)?"disabled='disabled'":"")+" onkeydown='onlyNumeric(event);' />";
	faixasVenda[val-1].cell.formatFaixaDe = "<font color='"+((checkbox.checked)?"black":"gray")+"'>"+faixasVenda[val-1].cell.faixaReparteDe+" a</font>";
	
}

var resultadoAnalise;
var anaLiseHistogramaController = $.extend(true, {
	tempData:null,
	voltarFiltro:function(){
		$("#analiseHistogramaVendasContent").clear();
		$("#histogramaVendasContent").show();
	},
	
	formatarFaixasVenda : function formatarFaixasVenda(rowCell){
		var cotasEsmagadasFormatado = '';
		
			rowCell.repTotal = formatarMilhar(rowCell.repTotal);
			rowCell.vdaTotal = formatarMilhar(parseInt(rowCell.vdaTotal));
			
			cotasEsmagadasFormatado = formatarMilhar($(rowCell.cotasEsmagadas).text());
			
			if (cotasEsmagadasFormatado == "0") {
				rowCell.cotasEsmagadas = 0;
			}
			 
			rowCell.vendaEsmagadas = formatarMilhar(rowCell.vendaEsmagadas);
			rowCell.qtdeCotasAtivas = formatarMilhar(rowCell.qtdeCotasAtivas);
			rowCell.qtdeCotas = formatarMilhar(rowCell.qtdeCotas);
			rowCell.qtdeCotasSemVendas = formatarMilhar(rowCell.qtdeCotasSemVendas);
			rowCell.encalheMedio = floatToPrice(rowCell.encalheMedio);
			rowCell.partReparte = floatToPrice(rowCell.partReparte);
			rowCell.partVenda = floatToPrice(rowCell.partVenda);
			rowCell.repMedio = floatToPrice(rowCell.repMedio);
			rowCell.vdaMedio = floatToPrice(rowCell.vdaMedio);
	},
	
	iniciarGridAnalise:function(){
		
		$("#estudosAnaliseHistGrid",anaLiseHistogramaController.workspace).flexigrid({
			url: contextPath + "/distribuicao/histogramaVendas/populateHistograma",
			dataType : 'json',
			preProcess: function (data){
				
				if (data.result){
					
					data = data.result;
				}
				
				resultadoAnalise=data.rows;
				
				var idArray=["cotasAtivasCell","repartTotalCell","repMedioCell","vdaMedioCell","cotasEsmagadasCell","vdaTotalCell","vendaEsmagadasCell","encalheMedioCell","cotasProdutoCell","reparteDistribuidoCell"];
				var valueArray=["qtdeCotasAtivas","repTotal","repMedio","vdaMedio","cotasEsmagadas","vdaTotal","vendaEsmagadas","encalheMedio","qtdeCotas","reparteDistribuido"];
				
				var lastRow = $(data.rows).last()[0];
				
				for ( var i = 0; i < idArray.length; i++) {
					$("#"+idArray[i]).text(lastRow.cell[valueArray[i]]);
				}
				
				// Adicionar o link as cotas esmagadas
				$.each(data.rows, function(index, row) {
					rowCell = row.cell;
					rowCell.partVenda =  rowCell.vdaTotal /lastRow.cell.vdaTotal;
					rowCell.partReparte =  rowCell.repTotal /lastRow.cell.repTotal;
					
					if(parseInt(rowCell.qtdeCotas)>0){
						rowCell.faixaVenda="<a href=\"javascript:anaLiseHistogramaController.executarAnaliseHistoricoVenda("+index+",'idCotaStr');\">"+rowCell.faixaVenda+"</a>";						
						rowCell.cotasEsmagadas="<a href=\"javascript:anaLiseHistogramaController.executarAnaliseHistoricoVenda("+index+",'idCotasEsmagadas');\">"+formatarMilhar(rowCell.cotasEsmagadas)+"</a>";
					}
					
					anaLiseHistogramaController.formatarFaixasVenda(rowCell);
				});

				var vdaTotal = parseInt(lastRow.cell.vdaTotal);
				var repTotal = parseInt(lastRow.cell.repTotal);
				
				var qtdeCotas = parseInt(lastRow.cell.qtdeCotas);
				//cotas ativas da faixa de venda
				var qtdeCotasAtivas = parseInt(lastRow.cell.qtdeCotasAtivas);
				//total de cotas ativas 
				var qtdeTotalCotasAtivas = parseInt(lastRow.cell.qtdeTotalCotasAtivas);
				var qtdeCotasSemVenda = parseInt(lastRow.cell.qtdeCotasSemVenda);
				
				var eficVenda = parseFloat(vdaTotal/repTotal*100).toFixed(2);
				$("#eficienciaDeVendaCell").text(eficVenda+"%");
				
				var r = parseFloat(Math.round( (qtdeCotas/qtdeTotalCotasAtivas)*100 )).toFixed(2);
				$("#abrangenciaDistribuicaoCell").text(r+"%");

				r = parseFloat(Math.round( (qtdeCotas-qtdeCotasSemVenda)/qtdeTotalCotasAtivas*100 )).toFixed(2);
				
				$("#abrangenciaVendaCell").text(r+"%");
				
//				anaLiseHistogramaController.formatarFaixasVenda(data.rows);
				
				return data;
			},
			colModel : [ {
				display : 'Faixa de Venda',
				name : 'faixaVenda',
				width : 135,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rep. Total',
				name : 'repTotal',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Rep. Médio',
				name : 'repMedio',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Vda Nominal',
				name : 'vdaTotal',
				width : 65,
				sortable : true,
				align : 'right'
			}, {
				display : 'Vda Média',
				name : 'vdaMedio',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : '% Vda',
				name : 'percVenda',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'Enc. Médio',
				name : 'encalheMedio',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Reparte',
				name : 'partReparte',
				width : 65,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Venda',
				name : 'partVenda',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Qtde. Cotas',
				name : 'qtdeCotas',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Cotas Esmag.',
				name : 'cotasEsmagadas',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Vda Esmag.',
				name : 'vendaEsmagadas',
				width : 60,
				sortable : true,
				align : 'right'
			}],
			sortname : "faixaReparte",
			sortorder : "asc",
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
		
	},
	
	executarAnaliseHistoricoVenda:function(idx, propriedade){
		var idCotaArray = resultadoAnalise[idx].cell[propriedade].split(',');
		
		
		url = contextPath + "/distribuicao/historicoVenda/analiseHistorico";

		var params = new Array();
		
		//popular lista de ID de cotas
		$.each(idCotaArray, function(index, val) {
			params.push({name : "cotas["+index+"].numeroCota", value : val });
		});
		
		//
		
		for ( var int = 0; int < edicoesEscolhidas_HistogramaVenda.length; int++) {
			params.push({name : "listProdutoEdicaoDto["+int+"].numeroEdicao", value :  edicoesEscolhidas_HistogramaVenda[int]});
			params.push({name : "listProdutoEdicaoDto["+int+"].codigoProduto", value :  codigoProduto_HistogramaVenda});
			
		}
		
		$.post(url, params, function(data){
		      if(data){
		    	  
		    	  
		    	  $("#analiseHistogramaVendasContent").hide();
		    	  $("#analiseHistoricoVendasContent").html(data).show();
		    	  montarDados();
		    	  analiseHistoricoVendaController.Grids.BaseHistoricoGrid.reload();
		      }
		});
		
		/*
		params = new Array();
		
		params.push({name : "filtro.produtoDto.codigoProduto", value :  codigoProduto_HistogramaVenda});
		for ( var int = 0; int < edicoesEscolhidas_HistogramaVenda.length; int++) {
			params.push({name : "filtro.listProdutoEdicaoDTO["+int+"].numeroEdicao", value :  edicoesEscolhidas_HistogramaVenda[int]});
			
		}

		//carregando pop superior para as edições
		url = contextPath + "/distribuicao/historicoVenda/pesquisaProduto";
		$.post(url, params, function(data){
			if(data){
				console.log("retorno do post...");
				anaLiseHistogramaController.tempData = data;
				console.log(anaLiseHistogramaController.data);
			}
		});
		*/
			
		
	},
	refazerHistograma:function(){
		/*var data = {"edicoes":edicoesEscolhidas.sort().toString(),
				"faixasVenda":faixas,
				"codigoProduto":codigoProduto};
		
		*/
		
		var faixas = new Array();
		
		for ( var int = 0; int < faixasVenda.length; int++) {
			var obj = faixasVenda[int];
			if(obj.cell.enabled)
				faixas.push(obj.cell.faixaReparteDe+"-"+obj.cell.faixaReparteAte);
		}
		
		var formData = new Array();
		formData.push({name:"edicoes",value:edicoesEscolhidas_HistogramaVenda.sort().toString()});
		formData.push({name:"faixasVenda",value:faixas});
		formData.push({name:"codigoProduto",value:codigoProduto_HistogramaVenda});
		
//		console.log($.param(formData));
		$("#estudosAnaliseHistGrid").flexOptions({
			url: contextPath + "/distribuicao/histogramaVendas/populateHistograma",
			dataType : 'json',
			params: formData
		});
		$("#estudosAnaliseHistGrid").flexReload();
		
		
		/*$.post(contextPath + "/distribuicao/histogramaVendas/analiseHistograma", data, function(data){
	      if(data){ 
	    	  $("#histogramaVendasContent").hide();
	    	  $('#analiseHistogramaVendasContent').html(data);
	    	  
	    	  anaLiseHistogramaController.iniciarGridAnalise();
	      }
	    });*/
		
	}

});


/*
$(".baseSugeridaGrid").flexigrid({
			url : '../xml/baseSugerida2-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'CÃ³digo',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Produto',
				name : 'produto',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'EdiÃ§Ã£o',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'left'
			}],
			width : 300,
			height : 180
		});

*/



function popup_histograma() {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$("#faixasReparteGrid",anaLiseHistogramaController.workspace).flexigrid({
		singleSelect:true,
		dataType : 'json',
		showToggleBtn: false,
		resizable: true,
		colModel : [ {
			display : 'Faixa de Venda De',
			name : 'formatFaixaDe',
			width : 130,
			sortable : false,
			align : 'center'
		},{
			display : 'Faixa de Venda Até',
			name : 'inputfaixaReparteAte',
			width : 130,
			sortable : false,
			align : 'center'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 30,
			sortable : false,
			align : 'center'
		}],
		width : 350,
		height : 190
	});
	
	
	var data = {
	        total: faixasVenda.length,    
	        page:1,
	        rows: faixasVenda
	};
	
	$('#faixasReparteGrid',anaLiseHistogramaController.workspace).flexAddData(data);//.flexReload();
	
	
	$( "#dialog-alterar-faixa" ).dialog({
		resizable: false,
		height:360,
		width:400,
		modal: true,
		buttons: {
			"Confirmar": function() {
				$( this ).dialog( "close" );
				$("#effect").show("highlight", {}, 1000, callback);
				$(".grids").show();
				anaLiseHistogramaController.refazerHistograma();
				
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
};


function popup_divergencias() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-divergencia" ).dialog({
			resizable: false,
			height:360,
			width:690,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
function divergencia(){
	$('.classDivergencias').toggle();
	}
//@ sourceURL=analiseHistograma.js
