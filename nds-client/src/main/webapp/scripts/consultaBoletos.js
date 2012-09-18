var consultaBoletosController = $.extend(true, {
	
	init : function() {

		$(".boletosCotaGrid", consultaBoletosController.workspace).flexigrid({
		    preProcess: consultaBoletosController.getDataFromResult,
		    dataType : 'json',
			colModel : [ {
				display : 'Nosso Número',
				name : 'nossoNumero',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Emissão',
				name : 'dtEmissao',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Vencimento',
				name : 'dtVencimento',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Pagamento',
				name : 'dtPagto',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encargos',
				name : 'encargos',
				width : 80,
				sortable : true,
				align : 'right',
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 90,
				sortable : true,
				align : 'right',
			}, {
				display : 'Tipo Baixa',
				name : 'tipoBaixa',
				width : 100,
				sortable : true,
				align : 'left',
			}, {
				display : 'Status',
				name : 'status',
				width : 80,
				sortable : true,
				align : 'left',
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'left',
			}],
			sortname : "dtVencimento",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});

		$( "#dataDe", consultaBoletosController.workspace ).datepicker({
			showOn : "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});
		
		$( "#dataAte", consultaBoletosController.workspace ).datepicker({
			showOn : "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});
		
		$("#numCota", consultaBoletosController.workspace).numeric();
		$("#dataDe", consultaBoletosController.workspace).mask("99/99/9999");
		$("#dataAte", consultaBoletosController.workspace).mask("99/99/9999");
		
		$("#descricaoCota", consultaBoletosController.workspace).autocomplete({source: ""});
		
	},
		
	mostrarGridConsulta : function() {
		
		/*PASSAGEM DE PARAMETROS*/
		$(".boletosCotaGrid", consultaBoletosController.workspace).flexOptions({
			
			/*METODO QUE RECEBERA OS PARAMETROS*/
			url: contextPath + "/financeiro/boletos/consultaBoletos",
			params: [
			         {name:'numCota', value:$("#numCota", consultaBoletosController.workspace).val()},
			         {name:'descricaoCota', value:$("#descricaoCota", consultaBoletosController.workspace).val()},
			         {name:'dataDe', value:$("#dataDe", consultaBoletosController.workspace).val()},
			         {name:'dataAte', value:$("#dataAte", consultaBoletosController.workspace).val()},
			         {name:'status', value:$("#status", consultaBoletosController.workspace).val()}
			        ] ,
			        newp: 1
		});
		
		/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
		$(".boletosCotaGrid", consultaBoletosController.workspace).flexReload();
		
		$(".grids", consultaBoletosController.workspace).show();
	},
	
	getDataFromResult : function(resultado) {
		
		//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids", consultaBoletosController.workspace).hide();
			return resultado.tableModel;
		}
		
		var dadosPesquisa;
		$.each(resultado, function(index, value) {
			  if(value[0] == "TblModelBoletos") {
				  dadosPesquisa = value[1];
			  }
	    });
		
		$.each(dadosPesquisa.rows, 
				function(index, row) {

			         var linkEmail='';
			         var linkImpressao='';
			         
		        	 linkImpressao = '<a href="javascript:;" onclick="consultaBoletosController.imprimeBoleto(' + row.cell[0] + ');" style="cursor:pointer">' +
				 					 '<img src="' + contextPath + '/images/ico_impressora.gif" hspace="5" border="0px" title="Imprime boleto" />' +
				 					 '</a>';
			         			 					     
			         linkEmail = '<a href="javascript:;" onclick="consultaBoletosController.enviaBoleto(' + row.cell[0] + ');" style="cursor:pointer">' +
			                     '<img src="' + contextPath + '/images/ico_email.png" hspace="5" border="0px" title="Envia boleto por e-mail" />' +
 					             '</a>';		 					 
									
				     row.cell[8] = linkImpressao + linkEmail;
		         }
		);
		
		return dadosPesquisa;
	},
		
	enviaBoleto : function(nossoNumero) {
		var data = [
	   				   {
	   					   name: 'nossoNumero', value: nossoNumero
	   				   }
	   			   ];
		$.postJSON(contextPath + "/financeiro/boletos/enviaBoleto", data);
	},
	
	imprimeBoleto : function(nossoNumero) {
		var data = [
	   				   {
	   					   name: 'nossoNumero', value: nossoNumero
	   				   }
	   			   ];
		$.postJSON(contextPath + "/financeiro/boletos/verificaBoleto", data, imprimirBoleto );
	},
	
	imprimirBoleto : function(result){
		if (result!=''){
			document.location.assign(contextPath + "/financeiro/boletos/imprimeBoleto?nossoNumero="+ result);
		}
	}

}, BaseController);
//@ sourceURL=consultaBoletos.js