var analiseHistoricoVendaController = $.extend(true, {
	
	init : function() {
		
		var flexGridService = new FlexGridService();
		
		// EXPORTAÇÃO
		$('#analiseHistoricoVendaXLS', analiseHistoricoVendaController.workspace).attr(
				'href', contextPath + "/distribuicao/historicoVenda/exportar?fileType=XLS");
		
		$('#analiseHistoricoVendaPDF', analiseHistoricoVendaController.workspace).attr(
				'href', contextPath + "/distribuicao/historicoVenda/exportar?fileType=PDF");
		
		analiseHistoricoVendaController.Grids = {
				BaseHistoricoGrid : flexGridService.GridFactory.createGrid({
					gridName : "baseHistoricoGrid",
					url : contextPath + "/distribuicao/historicoVenda/carregarGridAnaliseHistorico",
					cached : true,
					preProcess : analiseHistoricoVendaController.preProcessAnaliseGrid,
					gridConfiguration : {
						dataType: 'json',
						colModel : [ {
							display : 'Cota',
							name : 'numeroCota',
							width : 35,
							sortable : true,
							align : 'left'
						}, {
							display : 'Status',
							name : 'statusCota',
							width : 50,
							sortable : true,
							align : 'left'
						}, {
							display : 'Nome',
							name : 'nomePessoa',
							width : 100,
							sortable : true,
							align : 'left'
						}, {
							display : 'NPDV',
							name : 'qtdPdv',
							width : 30,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'reparteMedio',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'vendaMedia',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'ed1Reparte',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'ed1Venda',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'ed2Reparte',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'ed2Venda',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'ed3Reparte',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'ed3Venda',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'ed4Reparte',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'ed4Venda',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'ed5Reparte',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'ed5Venda',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'REP',
							name : 'ed6Reparte',
							width : 35,
							sortable : true,
							align : 'right'
						}, {
							display : 'VDA',
							name : 'ed6Venda',
							width : 35,
							sortable : true,
							align : 'right'
						}],
						width : 950,
						height : 270
					}
				}),
				CotasDetalhesGrid : flexGridService.GridFactory.createGrid({
					gridName : "cotasDetalhesGrid",
					url : contextPath + "/distribuicao/historicoVenda/carregarPdv",
					cached : true,
					preProcess : analiseHistoricoVendaController.preProcessPopUpGrid,
					gridConfiguration : {
						dataType : 'json',
						colModel : [ {
							display : 'Código',
							name : 'id',
							width : 40,
							sortable : true,
							align : 'left'
						}, {
							display : 'Tipo',
							name : 'descricaoTipoPontoPDV',
							width : 90,
							sortable : true,
							align : 'left'
						}, {
							display : '% Fat.',
							name : 'porcentagemFaturamento',
							width : 30,
							sortable : true,
							align : 'right'
						}, {
							display : 'Princ.',
							name : 'principal',
							width : 30,
							sortable : true,
							align : 'center'
						}, {
							display : 'Endereço',
							name : 'endereco',
							width : 420,
							sortable : true,
							align : 'left'
						}],
						width : 690,
						height : 200
					}
				})
		};
	},
	
	popularPopUpInformacoesCota : function(cotaDto){
		if(cotaDto){
		       $('#popUpNumeroCota', analiseHistoricoVendaController.workspace).text(cotaDto.numeroCota);
		       $('#popUpNomePessoa', analiseHistoricoVendaController.workspace).text(cotaDto.nomePessoa);
		       $('#popUpTipoCota', analiseHistoricoVendaController.workspace).text(cotaDto.tipoDistribuicaoCota);
		       $('#popUpRanking', analiseHistoricoVendaController.workspace).text(cotaDto.rankId);
		       $('#popUpFaturamentoCota', analiseHistoricoVendaController.workspace).text(cotaDto.faturamentoFormatado);
		       $('#popUpData', analiseHistoricoVendaController.workspace).text(cotaDto.dataGeracaoFormat);
		  }
	},
	
	preProcessPopUpGrid : function preProcessPopUpGrid(response){
		
		analiseHistoricoVendaController.popularPopUpInformacoesCota(response.cotaDto);
		
		if (response.tableModel) {
			for ( var i in response.tableModel.rows) {
				row = response.tableModel.rows[i];
				
				if (row.cell.principal) {
					row.cell.principal = "<img src='images/ico_check.gif' />";
				}else {
					row.cell.principal = "";
				}
				
			}
		}
		
		return response.tableModel;
		
	},
	
	preProcessAnaliseGrid : function preProcessAnaliseGrid(result){
		var resumo = {
				qtdCota  : 0,
				qtdPdv  :  0,
				reparteMedio : 0,
				vendaMedia : 0,
				ed1Rep : 0,
				ed1Venda : 0,
				ed2Rep :  0,
				ed2Venda : 0,
				ed3Rep :  0,
				ed3Venda : 0,
				ed4Rep :  0,
				ed4Venda : 0,
				ed5Rep :  0,
				ed5Venda : 0,
				ed6Rep :  0,
				ed6Venda : 0
		};
		
		for ( var index in result.rows) {
			row = result.rows[index];
			
			// montar o link no nome da cota
			link = '<a href="javascript:;" onclick="analiseHistoricoVendaController.popup_cotas_detalhes('+row.cell.numeroCota+');" style="cursor:pointer">'+row.cell.nomePessoa+'</a>';
			row.cell.nomePessoa = link;
			
			resumo.reparteMedio  = parseFloat(Math.round(resumo.reparteMedio)) + parseFloat(Math.round(row.cell.reparteMedio ||  0));
			resumo.vendaMedia = parseFloat(Math.round(resumo.vendaMedia)) + parseFloat(Math.round(row.cell.vendaMedia ||  0));
			resumo.qtdCota  +=  parseInt((row.cell.numeroCota ? 1 : 0));
			resumo.qtdPdv  +=  parseInt((row.cell.qtdPdv || 0));
			resumo.ed1Rep += parseInt((row.cell.ed1Reparte  ||  0));
			resumo.ed1Venda +=  parseInt((row.cell.ed1Venda ||  0));
			resumo.ed2Rep +=  parseInt((row.cell.ed2Reparte ||  0));
			resumo.ed2Venda +=  parseInt((row.cell.ed2Venda ||  0));
			resumo.ed3Rep +=  parseInt((row.cell.ed3Reparte ||  0));
			resumo.ed3Venda +=  parseInt((row.cell.ed3Venda ||  0));
			resumo.ed4Rep +=  parseInt((row.cell.ed4Reparte ||  0));
			resumo.ed4Venda +=  parseInt((row.cell.ed4Venda ||  0));
			resumo.ed5Rep +=  parseInt((row.cell.ed5Reparte ||  0));
			resumo.ed5Venda +=  parseInt((row.cell.ed5Venda ||  0));
			resumo.ed6Rep +=  parseInt((row.cell.ed6Reparte ||  0));
			resumo.ed6Venda +=  parseInt((row.cell.ed6Venda || 0));
			
			row.cell.reparteMedio = (row.cell.reparteMedio || 0).toFixed(0);
			row.cell.vendaMedia = (row.cell.vendaMedia || 0).toFixed(0);
			
		}
		
		analiseHistoricoVendaController.calcularMediaVendaEReparteCota(resumo);
		
		
		if(resumo.ed1Rep == 0){
			resumo.ed1Rep = "";
		}
		if(resumo.ed1Venda == 0){
			resumo.ed1Venda = "";
		}
		
		if(resumo.ed2Rep == 0){
			resumo.ed2Rep = "";
		}
		if(resumo.ed2Venda == 0){
			resumo.ed2Venda = "";
		}
		
		if(resumo.ed3Rep == 0){
			resumo.ed3Rep = "";
		}
		if(resumo.ed3Venda == 0){
			resumo.ed3Venda = "";
		}
		
		if(resumo.ed4Rep == 0){
			resumo.ed4Rep = "";
		}
		if(resumo.ed4Venda == 0){
			resumo.ed4Venda = "";
		}
		
		if(resumo.ed5Rep == 0){
			resumo.ed5Rep = "";
		}
		if(resumo.ed5Venda == 0){
			resumo.ed5Venda = "";
		}
		
		if(resumo.ed6Rep == 0){
			resumo.ed6Rep = "";
		}
		if(resumo.ed6Venda == 0){
			resumo.ed6Venda = "";
		}
		if(resumo.reparteMedio == 0){
			resumo.reparteMedio = "";
		}
		if(resumo.vendaMedia == 0){
			resumo.vendaMedia = "";
		}
		
		html = '<td width="50" >Qtde Cotas:</td>' +
		        '<td width="103" >' + resumo.qtdCota + '</td>' +
		        '<td width="30" align="right">' + resumo.qtdPdv + '</td>' +
		        '<td width="32" align="right">' + resumo.reparteMedio + '</td>' +
		        '<td width="32" align="right">' + (analiseHistoricoVendaController.isFaixaZero ? 0 : resumo.vendaMedia) + '</td>' +
		        '<td width="32" align="right">' + resumo.ed1Rep + '</td>' +
		        '<td width="32" align="right">' + resumo.ed1Venda + '</td>' +
		        '<td width="32" align="right">' + resumo.ed2Rep + '</td>' +
		        '<td width="32" align="right">' + resumo.ed2Venda + '</td>' +
		        '<td width="32" align="right">' + resumo.ed3Rep + '</td>' +
		        '<td width="32" align="right">' + resumo.ed3Venda + '</span></td>' +
		        '<td width="32" align="right">' + resumo.ed4Rep + '</td>' +
		        '<td width="32" align="right">' + resumo.ed4Venda + '</span></td>' +
		        '<td width="32" align="right">' + resumo.ed5Rep + '</td>' +
		        '<td width="32" align="right">' + resumo.ed5Venda + '</span></td>' +
		        '<td width="32" align="right">' + resumo.ed6Rep + '</td>' +
		        '<td width="32" align="right">' + resumo.ed6Venda + '</span></td>' +
		        '<td width="15" >&nbsp;</td>';
		 
		 $('#rodapeAnaliseHistorico', analiseHistoricoVendaController.workspace).html(html);
		 
		 return result;
	},

	calcularMediaVendaEReparteCota : function(resumo){
		if (!resumo.reparteMedio && !resumo.vendaMedia){
			var qtdEdicoes,
				reparteMedio = 0,
				vendaMedia = 0;
	
			if (typeof histogramaVendasController !== "undefined" && typeof histogramaVendasController.edicoesEscolhidas_HistogramaVenda !== "undefined") {
				// EMS 2029
				 qtdEdicoes = histogramaVendasController.edicoesEscolhidas_HistogramaVenda.length;
			}else{
				// EMS 2028
				qtdEdicoes = historicoVendaController.Grids.EdicaoSelecionadaGrid.tableModel.rows.length;
			}
			
			
			for ( var i = 1; i <= qtdEdicoes + 1; i++) {
				reparteMedio += resumo['ed' + i + 'Rep'];
				vendaMedia += resumo['ed' + i + 'Venda'];
			}
			
			resumo.reparteMedio  = parseInt(Math.round(reparteMedio / qtdEdicoes)); 
			resumo.vendaMedia = parseInt(Math.round(vendaMedia / qtdEdicoes));
		}
	},
	
	// PopUp visulizado quando o usuário clica no nome da cota dentro do Grid Principal
	popup_cotas_detalhes : function popup_cotas_detalhes(numeroCota) {
		$( "#dialog-cotas-detalhes" ).dialog({
			resizable: false,
			height:450,
			width:740,
			modal: true,
			open: function(){
				analiseHistoricoVendaController.Grids.CotasDetalhesGrid.reload({
					params : [{ name : 'numeroCota' , value : numeroCota}]
				});
			},
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			}
		});
	}
	
}, BaseController);
//@ sourceURL=analiseHistoricoVenda.js