function montarDados(){

	var params = new Array();
	
	params.push({name : "filtro.produtoDto.codigoProduto", value :  codigoProduto_HistogramaVenda});
	for ( var int = 0; int < edicoesEscolhidas_HistogramaVenda.length; int++) {
		params.push({name : "filtro.listProdutoEdicaoDTO["+int+"].numeroEdicao", value :  edicoesEscolhidas_HistogramaVenda[int]});
		params.push({name : "edicoesSelecionadas["+int+"].numeroEdicao", value :  edicoesEscolhidas_HistogramaVenda[int]});
	}

	//carregando popup superior para as edições
	var url = contextPath + "/distribuicao/historicoVenda/pesquisaProduto";
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

var resultadoAnalise;
var anaLiseHistogramaController = $.extend(true, {
	MIN_FAIXA : 0, 
	MAX_FAIXA : 999999,
	tempData:null,
	voltarFiltro:function(){
		$("#analiseHistogramaVendasContent").clear();
		$("#histogramaVendasContent").show();
	},
	
	createInput : function createInput(id, value){
		return '<input type="text" id="input' + id + '" onkeydown="anaLiseHistogramaController.alterarFaixaAte(' + id + ', event);" value=' + value + ' />';
	},
	
	createImgExcluir : function createImgExcluir(rowId){
		return '<a href="javascript:;" onclick="anaLiseHistogramaController.excluirFaixa('+rowId+', event);" style="cursor:pointer">' +
			   '<img title="Excluir Exceção" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
			   '</a>';
	},
	
	formatarFaixasVenda : function formatarFaixasVenda(rowCell){
		var cotasEsmagadasFormatado = '';
		
			rowCell.repTotal = formatarMilhar(Math.round(rowCell.repTotal));
			rowCell.vdaTotal = formatarMilhar(Math.round(rowCell.vdaTotal));
			
			cotasEsmagadasFormatado = formatarMilhar($(rowCell.cotasEsmagadas).text());
			
			if (cotasEsmagadasFormatado == "0") {
				rowCell.cotasEsmagadas = 0;
			}
			 
			rowCell.vendaEsmagadas = formatarMilhar(rowCell.vendaEsmagadas);
			rowCell.qtdeCotasAtivas = formatarMilhar(rowCell.qtdeCotasAtivas);
			rowCell.qtdeCotas = formatarMilhar(rowCell.qtdeCotas);
			rowCell.qtdeCotasSemVendas = formatarMilhar(rowCell.qtdeCotasSemVendas);
			rowCell.encalheMedio = floatToPrice(rowCell.encalheMedio);
			rowCell.partReparte = floatToPrice(rowCell.partReparte * 100);
			rowCell.partVenda = floatToPrice(rowCell.partVenda * 100);
			rowCell.repMedio = floatToPrice(rowCell.repMedio);
			rowCell.vdaMedio = floatToPrice(rowCell.vdaMedio);
			rowCell.percVenda = floatToPrice(rowCell.percVenda * 100);
	},
	
	buildResumoEstudo : function buildResumoEstudo(lastRow){
		
		$("#cotasAtivasCell").text(formatarMilhar(lastRow.cell.qtdeTotalCotasAtivas || 0));
		$("#reparteDistribuidoCell").text(formatarMilhar(lastRow.cell.repTotal || 0 ));
		$("#repMedioCell").text(floatToPrice(lastRow.cell.repMedio || 0));
		$("#vdaMedioCell").text(floatToPrice(lastRow.cell.vdaMedio || 0));
		$("#cotasEsmagadasCell").text(formatarMilhar(lastRow.cell.cotasEsmagadas || 0));
		$("#vdaTotalCell").text(formatarMilhar(lastRow.cell.vdaTotal || 0));
		$("#vendaEsmagadasCell").text(formatarMilhar(lastRow.cell.vendaEsmagadas || 0));
		$("#encalheMedioCell").text(floatToPrice(lastRow.cell.encalheMedio || 0));
		$("#cotasProdutoCell").text(formatarMilhar(lastRow.cell.qtdeCotas || 0));
		
		
		var vdaTotal = parseInt(lastRow.cell.vdaTotal);
		var repTotal = parseInt(lastRow.cell.repTotal);
		
		var qtdeCotas = parseInt(lastRow.cell.qtdeCotas);
		//cotas ativas da faixa de venda
		var qtdeCotasAtivas = parseInt(lastRow.cell.qtdeCotasAtivas);
		//total de cotas ativas 
		var qtdeTotalCotasAtivas = parseInt(lastRow.cell.qtdeTotalCotasAtivas);
		
		var qtdeCotasSemVenda = parseInt(lastRow.cell.qtdeCotasSemVenda);
		
		var eficVenda = floatToPrice(parseFloat(vdaTotal/ lastRow.cell.repTotal*100).toFixed(2));
		$("#eficienciaDeVendaCell").text((eficVenda +"%").replace(".", ","));
		
		var r = floatToPrice(parseFloat((qtdeCotas/qtdeTotalCotasAtivas)*100 ).toFixed(2));
		$("#abrangenciaDistribuicaoCell").text((r +"%").replace(".", ","));

		r = parseFloat( (qtdeCotas-qtdeCotasSemVenda)/qtdeTotalCotasAtivas*100 ).toFixed(2);
		
		$("#abrangenciaVendaCell").text((r +"%").replace(".", ","));
		
	},
	
	iniciarGridAnalise:function(){
		
		var flexGridService = new FlexGridService();
		
		$('#alterarFaixaReparte').click(function(){
			$( "#dialog-alterar-faixa" ).dialog({
				resizable: false,
				height:340,
				width:400,
				modal: true,
				open: function(){
					var grids = anaLiseHistogramaController.Grids,
						createInput = anaLiseHistogramaController.createInput,
						createImgExcluir = anaLiseHistogramaController.createImgExcluir,
						arrayFaixasVenda = $(".histogramafaixaVenda"),
						faixaVenda = "",
						rowId = 0,
						faixaVendaDe = 0,
						faixaVendaAte = 0;
						
					grids.FaixasReparteGrid.tableModel.rows = [];
					
					// obter as faixas de venda utilizada pelo usuário
					// $(".histogramafaixaVenda")[0].text
					
					for ( var i = 0; i < arrayFaixasVenda.length; i += 1) {
						faixaVenda = arrayFaixasVenda[i].text;
						
						if (i != arrayFaixasVenda.length - 1) {
							
							rowId = parseInt(i) + 1;
							
							faixaVendaDe = replaceAll(faixaVenda.split("a")[0].replace(" ", "").replace("De",""),".","") + " a";
							faixaVendaAte = parseInt(faixaVenda.split("a")[1]);
							
							newRow = {
								id : rowId,
								cell : {
									faixaVendaDe : faixaVendaDe,
									faixaVendaAte : createInput(rowId, faixaVendaAte),
									acao : createImgExcluir(rowId)
								}
							};
						
							grids.FaixasReparteGrid.addData(newRow);
						}
					};
				},
				buttons: {
					"Confirmar": function () {
						$( this ).dialog( "close" );
						
						var faixasReparteGrid = anaLiseHistogramaController.Grids.FaixasReparteGrid,
							faixasVenda = [];
						
						for ( var int = 0; int < faixasReparteGrid.tableModel.rows.length; int++) {
							row = faixasReparteGrid.tableModel.rows[int];
							
							faixa = row.cell.faixaVendaDe.replace(" a","-") + $(row.cell.faixaVendaAte).val();
							
							faixasVenda.push(replaceAll(faixa," ",""));
						}
						
						anaLiseHistogramaController.refazerHistograma(faixasVenda);
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		});
		
		anaLiseHistogramaController.Grids = {
				FaixasReparteGrid : flexGridService.GridFactory.createGrid({
					gridName : "faixasVendaGrid",
					cached : true,
					gridConfiguration : {
						dataType : 'json',
						colModel : [ {
							display : 'Faixa de Venda De',
							name : 'faixaVendaDe',
							width : 130,
							sortable : true,
							align : 'left'
						},{
							display : 'Faixa de Venda Até',
							name : 'faixaVendaAte',
							width : 130,
							sortable : true,
							align : 'left'
						}, {
							display : 'Ação',
							name : 'acao',
							width : 30,
							sortable : true,
							align : 'center'
						}],
						width : 350,
						height : 180
					}
				}),
		};
		
		$("#estudosAnaliseHistGrid",anaLiseHistogramaController.workspace).flexigrid({
			url: contextPath + "/distribuicao/histogramaVendas/populateHistograma",
			dataType : 'json',
			preProcess: function (data){
				
				if (data.result){
					
					data = data.result;
				}
				
				resultadoAnalise=data.rows;
				
				var lastRow = $(data.rows).last()[0];
				
				if (data.rows[0].cell.faixaVenda === 'De 0 a 0') {
					lastRow.cell.qtdeCotasSemVenda = data.rows[0].cell.qtdeCotas;
		    	}
				
				anaLiseHistogramaController.buildResumoEstudo(lastRow);
				
				// Adicionar o link as cotas esmagadas
				$.each(data.rows, function(index, row) {
					rowCell = row.cell;
					rowCell.partVenda =  (rowCell.vdaTotal  /lastRow.cell.vdaTotal) || 0;
					rowCell.partReparte =  (rowCell.repTotal /lastRow.cell.repTotal) || 0;
					rowCell.percVenda =  (rowCell.vdaTotal /rowCell.repTotal) || 0;
					rowCell.faixaVenda="<a class='histogramafaixaVenda' href=\"javascript:anaLiseHistogramaController.executarAnaliseHistoricoVenda("+index+",'idCotaStr');\">"+rowCell.faixaVenda+"</a>";						
					
					if(parseInt(rowCell.qtdeCotas)>0){
						rowCell.cotasEsmagadas="<a href=\"javascript:anaLiseHistogramaController.executarAnaliseHistoricoVenda("+index+",'idCotasEsmagadas');\">"+formatarMilhar(rowCell.cotasEsmagadas)+"</a>";
					}
					
					if ($(rowCell.faixaVenda).text() === 'De 0 a 0') {
//						rowCell.vdaTotal = 0;
			    	}
					
					anaLiseHistogramaController.formatarFaixasVenda(rowCell);
				});

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
			height : 240
		});
	},
	
	executarAnaliseHistoricoVenda:function(idx, propriedade){
		var idCotaArray = resultadoAnalise[idx].cell[propriedade].split(','),
			url = contextPath + "/distribuicao/historicoVenda/analiseHistorico",
			params = new Array(),
			qtdEdicoes = edicoesEscolhidas_HistogramaVenda.length;
		
		//popular lista de ID de cotas
		$.each(idCotaArray, function(index, val) {
			params.push({name : "cotas["+index+"].numeroCota", value : val });
		});
		
		for ( var int = 0; int < qtdEdicoes; int++) {
			params.push({name : "listProdutoEdicaoDto["+int+"].numeroEdicao", value :  edicoesEscolhidas_HistogramaVenda[int]});
			params.push({name : "listProdutoEdicaoDto["+int+"].codigoProduto", value :  codigoProduto_HistogramaVenda});
		}
		
		$.post(url, params, function(data){
		      if(data){
		    	  $("#analiseHistogramaVendasContent").hide();
		    	  $("#analiseHistoricoVendasContent").html(data).show();
		    	  montarDados();
		    	  analiseHistoricoVendaController.Grids.BaseHistoricoGrid.reload();
		    	  
		    	  if ($(resultadoAnalise[idx].cell.faixaVenda).text() === 'De 0 a 0') {
		    		  analiseHistoricoVendaController.isFaixaZero = true;
		    	  }
		      }
		});
		
	},

	refazerHistograma:function(faixas){
		var formData = new Array();
		formData.push({name:"edicoes",value:edicoesEscolhidas_HistogramaVenda.sort().toString()});
		formData.push({name:"faixasVenda",value:faixas});
		formData.push({name:"codigoProduto",value:codigoProduto_HistogramaVenda});
		
		$("#estudosAnaliseHistGrid").flexOptions({
			url: contextPath + "/distribuicao/histogramaVendas/populateHistograma",
			dataType : 'json',
			params: formData
		});
		$("#estudosAnaliseHistGrid").flexReload();
	},
	
	alterarFaixaAte : function (rowId, event){
		var	faixaReparteGrid = anaLiseHistogramaController.Grids.FaixasReparteGrid,
			selectedRow = {},
			nextRow = {},
			newRow = {};
			
		
		if (event.keyIdentifier == "Enter") {
			event.cancelBubble = true;
			event.bubbles = false;

			selectedRow = $.grep(faixaReparteGrid.tableModel.rows, function(val, index) {
				return val.id == rowId;
			})[0];
			
			if (parseInt(event.target.value) < parseInt(selectedRow.cell.faixaVendaDe)) {
				event.target.value = $(selectedRow.cell.faixaVendaAte).val();
				exibirMensagem("WARNING", ['Valor "ATÉ" deve ser maior que "DE".']);
				return;
			}
			
			nextRow = $.grep(faixaReparteGrid.tableModel.rows, function(val, index) {
				return val.id == rowId+1;
			})[0];
			
			if (nextRow) {
				selectedRow.cell.faixaVendaAte = anaLiseHistogramaController.createInput(selectedRow.id,parseInt(event.target.value));
				nextRow.cell.faixaVendaDe = (parseInt(event.target.value) + 1 ) + " a";
			} else {
				// copia a última linha
				newRow = {
						id : 0,
						cell : {
							faixaVendaDe : 0,
							faixaVendaAte : 0
						}
					};
				
				// altera seu id
				newRow.id = parseInt(selectedRow.id) + 1;
				
				// altera o valor do input 
				selectedRow.cell.faixaVendaAte = anaLiseHistogramaController.createInput(selectedRow.id,parseInt(event.target.value));
				
				// cria a última linha do grid
				newRow.cell.faixaVendaDe = (parseInt(event.target.value) + 1 ) + " a";
				newRow.cell.faixaVendaAte = anaLiseHistogramaController.createInput(newRow.id,parseInt(anaLiseHistogramaController.MAX_FAIXA));
				
				// adiciona ao tableModel do grid
				faixaReparteGrid.tableModel.rows.push(newRow);
			}
			
			anaLiseHistogramaController.organizarRowId(faixaReparteGrid);
			
			faixaReparteGrid.addTableModel(faixaReparteGrid.tableModel);
			
			$('#input'+ (selectedRow.id + 1)).focus();
		}
	},
	
	excluirFaixa : function (rowId, event){
		var	faixaReparteGrid = anaLiseHistogramaController.Grids.FaixasReparteGrid,
			createInput = anaLiseHistogramaController.createInput,
			createImgExcluir = anaLiseHistogramaController.createImgExcluir,
			selectedRow = {},
			previousRow = {},
			nextRow = {};
			

		if (faixaReparteGrid.tableModel.rows.length > 1) {
				
			selectedRow = $.grep(faixaReparteGrid.tableModel.rows, function(val, index) {
				return val.id == rowId;
			})[0];
			
			previousRow = $.grep(faixaReparteGrid.tableModel.rows, function(val, index) {
				return val.id == rowId - 1;
			})[0];
			
			nextRow  = $.grep(faixaReparteGrid.tableModel.rows, function(val, index) {
				return val.id == rowId + 1;
			})[0];
	
			// usuário deleta a primeira linha
			if (!previousRow) {
				nextRow.cell.faixaVendaDe = 0 + " a";
			}
			// usuário deleta a última linha
			else if (!nextRow) {
				previousRow.cell.faixaVendaAte = createInput(previousRow.id, $(selectedRow.cell.faixaVendaAte).val());
			} 
			else {
				previousRow.cell.faixaVendaAte = createInput(previousRow.id, $(selectedRow.cell.faixaVendaAte).val());
			} 
			
			faixaReparteGrid.removeRow(selectedRow.id);
			
			anaLiseHistogramaController.organizarRowId(faixaReparteGrid);
			
			faixaReparteGrid.addTableModel(faixaReparteGrid.tableModel);
		}else {
			exibirMensagem("WARNING", ["Deve existir pelo menos uma faixa."]);
		}
	},
	
	organizarRowId : function (grid){
		var row = {};
		
		// organizando os ID das linhas
		for ( var int = 0; int < grid.tableModel.rows.length; int++) {
			row = grid.tableModel.rows[int];
			row.id = int + 1;
			row.cell.faixaVendaAte = anaLiseHistogramaController.createInput(row.id, $(row.cell.faixaVendaAte).val());
			row.cell.acao = anaLiseHistogramaController.createImgExcluir(row.id);
		}
	}
	
});

function popup_divergencias() {
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
