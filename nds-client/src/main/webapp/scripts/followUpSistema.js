var followUpSistemaController = $.extend(true, {
	
	init : function () {
		this.tabs();
		this.distribuicaoGrid();
		this.pendencias();
		this.atualizacaoCadastral();
		this.negociacao();
		this.atualizacaoCadastralParcial();
		this.initPesqProdutosNotaGrid();
	},

	tabs : function () {
		$( "#tab-followup", followUpSistemaController.workspace ).tabs();
	},
	
	chamadao : function (){

		$(".chamadaoGrid", followUpSistemaController.workspace).flexigrid($.extend({},{
			//url : contextPath + '/followup/pesquisaDadosChamadao',
			dataType : 'json',
			preProcess: followUpSistemaController.exPreProcFollowupChamadao,
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 300,
				sortable : true,
				align : 'left'
			}, {
				display : 'Consignado R$',
				name : 'valorTotalConsignadoFormatado',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Suspenso (dias)',
				name : 'qtdDiasSuspensao',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Programado',
				name : 'dataProgramadoChamadao',
				width : 120,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 40,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 950,
			height : 255
	    }));
	},
	
	distribuicaoGrid : function () {
		$(".distribuicaoGrid", followUpSistemaController.workspace).flexigrid($.extend({},{
			//url : contextPath + '/followup/pesquisaDistribuicaoCotasAjustes',
	        preProcess:  followUpSistemaController.exPreProcFollowupDistribuicao, 
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 290,
				sortable : true,
				align : 'left'
			}, {
				display : 'Mensagem',
				name : 'mensagem',
				width : 290,
				sortable : true,
				align : 'left'
			}, {
				display : 'Dias restantes',
				name : 'qtdDiasRestantes',
				width : 150,
				sortable : true,
				align : 'left'
			}],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 255
		}));
	},
	
	pendencias : function () {

		$(".pendenciasGrid", followUpSistemaController.workspace).flexigrid($.extend({},{
			//url : contextPath + '/followup/pesquisaDadosPendenciaNFEEncalhe',
			dataType : 'json',
			preProcess: followUpSistemaController.exPreProcFollowupPendenciasnfe,
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 302,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo de Pend&ecirc;ncia',
				name : 'tipoPendencia',
				width : 100,
				sortable : true,
				align : 'left'
			},{
				display : 'Dt. Entrada',
				name : 'dataEntrada',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Vlr. Diferença R$',
				name : 'valorDiferencaFormatado',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Telefone',
				name : 'numeroTelefone',
				width : 125,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 950,
			height : 255
		}));
	},
	
	alteracaoStatus : function (){
		$(".alteracaoStatusGrid", followUpSistemaController.workspace).flexigrid($.extend({},{
			//url : contextPath + '/followup/pesquisaDadosStatusCota',
			preProcess:  followUpSistemaController.exPreProcFollowupStatusCota, 
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Per&iacuteodo',
				name : 'periodoStatus',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status Atual',
				name : 'statusAtual',
				width : 83,
				sortable : true,
				align : 'left'
			}, {
				display : 'Novo Status',
				name : 'statusNovo',
				width : 83,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 40,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 950,
			height : 255
		}));
	},
	
	atualizacaoCadastral : function () {
		
		$(".atualizacaoCadastralGrid", followUpSistemaController.workspace).flexigrid($.extend({},{
			//url : contextPath + '/followup/pesquisaDadosCadastrais',
			preProcess:  followUpSistemaController.exPreProcFollowupCadastro, 
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 290,
				sortable : true,
				align : 'left'
			}, {
				display : 'Responsável',
				name : 'responsavel',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Documento',
				name : 'tipo',
				width : 125,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 125,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dt. Vencto.',
				name : 'dataVencimento',
				width : 85,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 30,
			showTableToggleBtn : true,
			width : 950,
			height : 255
		}));
	},
	
	negociacao : function() {
		$(".negociacaoGrid", followUpSistemaController.workspace).flexigrid({
			url : contextPath + '/followup/pesquisaDadosNegociacao',
	        preProcess:  followUpSistemaController.exPreProcFollowupNegociacao, 
			dataType : 'json',
			newp: 1,
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 65,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 210,
				sortable : true,
				align : 'left'
			}, {
				display : 'Negociação',
				name : 'valorParcelaFormatado',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Parcela',
				name : 'descricaoParcelamento',
				width : 100,
				sortable : false,
				align : 'left'
			}, {
				display : 'Forma de Pagamento',
				name : 'descricaoFormaPagamento',
				width : 140,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Vencto',
				name : 'dataVencimentoFormatada',
				width : 85,
				sortable : true,
				align : 'center'
			},{
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : false,
				align : 'center'
			}],
			sortname : "nomeJornaleiro",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 950,
			height : 255
		});
	},
	
	atualizacaoCadastralParcial : function() {
		$(".atualizacaoCadastralParcialGrid", followUpSistemaController.workspace).flexigrid($.extend({},{
			//url : contextPath + '/followup/pesquisaDadosCadastroParcial',
	        preProcess:  followUpSistemaController.exPreProcFollowupCadastroParcial, 
			dataType : 'json',
			colModel : [ {
				display : 'Codigo',
				name : 'codigoProduto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeProduto',
				width : 290,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edi&ccedil;&atilde;o',
				name : 'numeroEdicao',
				width : 150,
				sortable : true,
				align : 'left'
			}],
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 950,
			height : 255
		}));
	},
	
	
	exPreProcFollowupNegociacao : function (resultado) {
	
		$.each(resultado.rows, function(index, negociacao) {
			negociacao.cell.acao = "<a href=\"javascript:;\" onclick=\"followUpSistemaController.cancelarNegociacao("+negociacao.cell.idNegociacao+");\"> "+
			 "<img src=\"" + contextPath + "/images/ico_negociar.png\" title=\"Reverter Pagamento Negociação\" hspace=\"5\" border=\"0\" /></a>";
		});
		
		if(resultado.rows.length == 0){
			$('#botoesArquivoNegociacao').hide();
		}
		
		if (resultado.total > 0){
			$('.areaBtsNegociacao', followUpSistemaController.workspace).show();
		}
		else{
			$('.areaBtsNegociacao', followUpSistemaController.workspace).hide();
		}
		
		return resultado;
	},
	
	cancelarNegociacao : function(idNegociacao) {
		
		var parametro = {'idNegociacao' : idNegociacao};
		
		$.postJSON(contextPath + "/followup/cancelarNegociacao", 
					parametro, 
					function(result){
							exibirMensagem(
									result.tipoMensagem, 
									result.listaMensagens
							);
					});
		
	},
	
	exPreProcFollowupChamadao : function(resultado) {
		$.each(resultado.rows, function(index, row) {						
			
			url = '\'devolucao/chamadao/popularGridFollowUp?numeroCota='+row.cell.numeroCota+'&data='+row.cell.dataProgramadoChamadao + '\'';
				
			if (!row.cell.dataProgramadoChamadao || row.cell.dataProgramadoChamadao == ''){
				
				var linkExcluir = '<a href="javascript:;" onclick="$(\'#workspace\').tabs(\'addTab\', \'Chamadão\', ' + url + ')"  style="cursor:pointer">' +
								   	 '<img title="Programar" src="' + contextPath + '/images/ico_reprogramar.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkExcluir;
			}
			else{
				
				row.cell.acao = '';
			}
		});
		
		if(resultado.rows.length == 0){
			$('#botoesArquivoChamadao').hide();
		}
		
		$(".grids", followUpSistemaController.workspace).show();
		return resultado;
	},

	exPreProcFollowupStatusCota : function(resultado) {	
		
		$.each(resultado.rows, function(index, row) {						
			
			url = '\'financeiro/manutencaoStatusCota/popularGridFollowUp?numeroCota='+row.cell.numeroCota + '\'';
			
			var linkExcluir = '<a href="javascript:;" onclick="$(\'#workspace\').tabs(\'addTab\', \'Manutenção de Status Cota\', ' + url + ')"  style="cursor:pointer">' +
							   	 '<img title="Manutenção Status" src="' + contextPath + '/images/ico_negociar.png" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkExcluir;
			
			row.cell.statusAtual = row.cell.statusAtual?row.cell.statusAtual:'';
		});
		
		$(".grids", followUpSistemaController.workspace).show();
		
		if(resultado.rows.length == 0){
			$('#botoesArquivoAlteracaoStatusCota').hide();
		}
		return resultado;
	},
	
	exPreProcFollowupCadastro : function(resultado) {
	$.each(resultado.rows, function(index, row) {		
			
		if ( row.cell.dataVencimento == undefined ){
				row.cell.dataVencimento='';
		}
		
		if ( row.cell.numeroCota == undefined ){
			row.cell.numeroCota='';
		}
		
		if ((row.cell.tipo == 'Contrato fornecedor a vencer/Vencido')){
			row.cell.numeroCota='Fornecedor';
		}
		
		if ( row.cell.responsavel == undefined ){
			row.cell.responsavel='';
		}
			
	});
	
	if(resultado.rows.length == 0){
		$('#botoesArquivoAtualizacaoCadastral').hide();
	}
		return resultado;
	},
	
	exPreProcFollowupPendenciasnfe : function(resultado) {
		
		if(resultado.rows.length == 0){
			$('#botoesArquivoPendencia').hide();
		}
		
		$.each(resultado.rows, function(index, row) {						
			
			if(row.cell.numeroTelefone = "undefined"){
				row.cell.numeroTelefone = " - ";
			}
			
			var linkLancamento = '<a isEdicao="true" href="javascript:;"  onclick="followUpSistemaController.popup_nfe(\''+
			row.cell.numeroCota+'\',\''+row.cell.nomeJornaleiro+'\',\''+row.cell.idControleConferenciaEncalheCota+
			'\');" style="cursor:pointer">' +
		   	'<img title="Lançamentos da Edição" src="' + contextPath + '/images/bt_lancamento.png" hspace="5" border="0px" />' +
		    '</a>';
			
			var linkCadastro = '<a isEdicao="true" href="javascript:;" onclick="followUpSistemaController.popup_dadosNotaFiscal(\''+row.cell.numeroNfe +'\',\''
				+ row.cell.dataEncalhe +'\',\''
				+ row.cell.chaveAcesso +'\',\''
				+ row.cell.serie +'\',\''
				+ row.cell.valorNota +'\',\''
				+ row.cell.idControleConferenciaEncalheCota +'\',\''
				+ row.cell.idNotaFiscalEntrada +'\');" style="cursor:pointer">' +
				'<img title="Lançamentos da Edição" src="' + contextPath + '/images/bt_cadastros.png" hspace="5" border="0px" />' +
				'</a>';

			row.cell.acao = linkLancamento + linkCadastro;
			
		});
		
   		return resultado;
	},
	
	exPreProcFollowupDistribuicao : function (resultado) {

		if(resultado.rows.length == 0){
			$('#botoesArquivoDistribuicao').hide();
		}
		
		$.each(resultado.rows, function(index, row) {
		
		    row.cell.qtdDiasRestantes = Math.abs(row.cell.qtdDiasRestantes);
		});
		
		return resultado;
	},
	
	exPreProcFollowupCadastroParcial : function (resultado) {
		
		if(resultado.rows.length == 0){
			$('#botoesArquivoCadastroParcial').hide();
		}
		
		if (resultado.total > 0){
			$('.areaBtsParcial', followUpSistemaController.workspace).show();
		}
		else{
			$('.areaBtsParcial', followUpSistemaController.workspace).hide();
		}
		
		return resultado;
	},
	
	toggleButtons : function(idDivActive, tabActive) {
		
		if(idDivActive != 'noBtns'){
			$(".divButtonsWrapper",  followUpSistemaController.workspace).hide();
			$("#"+idDivActive,  followUpSistemaController.workspace).show();
		}
		
		followUpSistemaController.executarRequisicao(tabActive);
	},
	
	chamadaoTab : function(){
		
		$(".chamadaoGrid", this.workspace).flexOptions({
			url: contextPath + '/followup/pesquisaDadosChamadao',
			dataType : 'json'
		});
		
		$(".chamadaoGrid", this.workspace).flexReload();
		
	},
	
	distribuicaoTab : function(){
		
		$(".distribuicaoGrid", this.workspace).flexOptions({
			url: contextPath + '/followup/pesquisaDistribuicaoCotasAjustes',
			dataType : 'json'
		});
		
		$(".distribuicaoGrid", this.workspace).flexReload();
		
	},
	
	tabPendencias : function(){
		
		$(".pendenciasGrid", this.workspace).flexOptions({
			url: contextPath + '/followup/pesquisaDadosPendenciaNFEEncalhe',
			dataType : 'json'
		});
		
		$(".pendenciasGrid", this.workspace).flexReload();
		
	},
	
	
	 tabAlteracaoStatus : function(){
		
		$(".alteracaoStatusGrid", this.workspace).flexOptions({
			url: contextPath + '/followup/pesquisaDadosStatusCota',
			dataType : 'json'
		});
		
		$(".alteracaoStatusGrid", this.workspace).flexReload();
		
	},
		
	tabAtualizacaoCadastral : function(){
		
		$(".atualizacaoCadastralGrid", this.workspace).flexOptions({
			url: contextPath + '/followup/pesquisaDadosCadastrais',
			dataType : 'json'
		});
		
		$(".atualizacaoCadastralGrid", this.workspace).flexReload();
		
	},
	
	tabNegociacao : function(){
		
		$(".negociacaoGrid", this.workspace).flexOptions({
			url: contextPath + '/followup/pesquisaDadosNegociacao',
			dataType : 'json'
		});
		
		$(".negociacaoGrid", this.workspace).flexReload();
		
	},
		
	tabParcial : function(){
		
		$(".atualizacaoCadastralParcialGrid", this.workspace).flexOptions({
			url: contextPath + '/followup/pesquisaDadosCadastroParcial',
			dataType : 'json'
		});
		
		$(".atualizacaoCadastralParcialGrid", this.workspace).flexReload();
		
	},
	
	popup_nfe : function(numeroCota, nome, idControleConferenciaEncalheCota){
		
		$('#followupSerieNotaCadastroNota', this.workspace).val('');
		$('#followupChaveAcessoCadastroNota', this.workspace).val('');
		$('#followupValorNotaCadastroNota', this.workspace).val('');
		$('#followupNumeroNotaCadastroNota', this.workspace).val('');
		
		if(numeroCota != '0'){
			$('#followupCotaCadastroNota', this.workspace).val(numeroCota);
			$('#followupNomeCotaCadastroNota', this.workspace).attr('readonly', true);
			$('#followupNomeCotaCadastroNota', this.workspace).val(nome);			
		}else{
			$('#followupCotaCadastroNota', this.workspace).val('');
			$('#followupCotaCadastroNota', this.workspace).attr('readonly', false);
			$('#followupNomeCotaCadastroNota', this.workspace).val('');
		}
		$('#followupNomeCotaCadastroNota', this.workspace).attr('readonly', true);
		
		$('#followupValorNotaCadastroNota', this.workspace).priceFormat({
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});

		$( "#followup-dialog-nfe", this.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: true,
			buttons: {
				"Confirmar": function() {
					followUpSistemaController.cadastrarNota(idControleConferenciaEncalheCota);
					$( this ).dialog( "close" );					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#followup-dialog-nfe", this.workspace).parents("form")
		});
	},
	
	
	executarRequisicao : function(tab){
		
		switch (tab) {
		case "tabChamadao":
			followUpSistemaController.chamadaoTab();
			break;
		case "tabDistribuicao":
			followUpSistemaController.distribuicaoTab();
			break;	
		case "tabPendencia":
			followUpSistemaController.tabPendencias();
			break;
		case "tabAlteracao":
			followUpSistemaController.tabAlteracaoStatus();
			break;
		case "tabAtualizacao":
			followUpSistemaController.tabAtualizacaoCadastral();
			break;
		case "tabNegocia":
			followUpSistemaController.tabNegociacao();
			break;
		case "tabCadastroParcial":
			followUpSistemaController.tabParcial();
			break;
		}
	},
	
	
	popup_dadosNotaFiscal : function(numeroNfe, dataEncalhe, chaveAcesso, serie, valorNota, idControleConferenciaEncalheCota, statusNotaFiscalEntrada) {
		
		$('#followupNumeroNotaFiscalPopUp', this.workspace).text(numeroNfe);
		$('#followupDataNotaFiscalPopUp', this.workspace).text(dataEncalhe);
		$('#followupChaveAcessoNotaFiscalPopUp', this.workspace).text(chaveAcesso);
		$('#followupSerieNotaFiscalPopUp', this.workspace).text(serie);
		$('#followupValorNotaFiscalPopUp', this.workspace).text(valorNota);
		
		$(".pesquisarProdutosNotaGrid", followUpSistemaController.workspace).flexOptions({
			url: contextPath + "/followup/pesquisarItensPorNota",
			params: [{name:"idControleConferencia", value:idControleConferenciaEncalheCota}],
			dataType : 'json'
		});

		$(".pesquisarProdutosNotaGrid", followUpSistemaController.workspace).flexReload();
		$( "#followup-dialog-dadosNotaFiscal", this.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:860,
			modal: true,
			buttons: {
				"Confirmar": function() {
					followUpSistemaController.cadastrarNota(idControleConferenciaEncalheCota);
					$( this ).dialog( "close" );					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#followup-dialog-dadosNotaFiscal", this.workspace).parents("form")
		});
		
	},
	
	cadastrarNota : function(idControleConferenciaEncalheCota){		

		$.postJSON(
				this.path +'cadastrarNota',
				[
					{ name: "nota.numero", value: $('#followupNumeroNotaCadastroNota', this.workspace).val() },
					{ name: "nota.serie", value: $('#followupSerieNotaCadastroNota', this.workspace).val() },
					{ name: "nota.chaveAcesso", value: $('#followupChaveAcessoCadastroNota', this.workspace).val() },
					{ name: "nota.valorNF", value: $('#followupValorNotaCadastroNota', this.workspace).val() },
					{ name: "numeroCota", value: $('#followupCotaCadastroNota', this.workspace).val() },
					{ name: "idControleConferenciaEncalheCota", value: idControleConferenciaEncalheCota }
				],
				function(result) {
					if (result.listaMensagens) {
						exibirMensagem(result.tipoMensagem, result.listaMensagens);
					}
				},
				null, true
			);
	},
	
	initPesqProdutosNotaGrid : function() {
		$(".pesquisarProdutosNotaGrid", this.workspace).flexigrid({
			preProcess: this.executarPreProcessamentoProdutosNotaFiscal,
			dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigoProduto',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Dia',
					name : 'dia',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Qtde. Info',
					name : 'qtdInformada',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Qtde. Recebida',
					name : 'qtdRecebida',
					width : 90,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço Capa R$',
					name : 'precoCapaFormatado',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço Desc R$',
					name : 'precoDescontoFormatado',
					width : 80,
					sortable : true,
					align : 'right'
				}, {
					display : 'Total R$',
					name : 'totalDoItemFormatado',
					width : 80,
					sortable : true,
					align : 'right'
				}],
				sortname : "codigoProduto",
				sortorder : "asc",
				width : 810,
				height : 250
			});
	},
	
}, BaseController);
//@ sourceURL=followUpSistema.js