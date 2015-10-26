var anoSemanaPesquisa;
var emissaoBandeiraController = $.extend(true, {
	
	init : function() {
		
		this.iniciarGrid();
		
		this.initFiltroDatas();
		
		$("#emissaoBandeiras-numeroSemana", this.workspace).numeric();
		$("#numeroPallets", this.workspace).numeric();
		
		$("#dataEnvio", this.workspac ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$("#dataEnvio", this.workspace).mask("99/99/9999");
		
		$("#emissaoBandeiras-dataEmissao", this.workspac ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$("#emissaoBandeiras-dataEmissao", this.workspace).mask("99/99/9999");
		
		$(".bt_arq",this.workspace).hide();
				
	},
	
	initBandeiraManual : function() {
		$("#dataEnvioManual", emissaoBandeiraController.workspace).mask("99/99/9999");
	},
	
	initFiltroDatas : function(){
		
	    $.postJSON(contextPath + '/cadastro/distribuidor/obterDataDistribuidor',
				null, 
				function(result) {
					
					$("#emissaoBandeiras-dataEmissao", this.workspace).val(result);
		        }
		); 
	},
	
	iniciarGrid : function() {
		
		
		$(".bandeirasRcltoGrid", this.workspace).flexigrid({
			dataType : 'json',
			preProcess: emissaoBandeiraController.executarPreProcessamento,
			colModel : [ {
				display : 'Número NF',
				name : 'numeroNotaFiscal',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Série',
				name : 'serieNotaFiscal',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Código',
				name : 'codigoEditor',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Nome',
				name : 'nomeEditor',
				width : 240,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data de Saída',
				name : 'dataSaida',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Volumes',
				name : 'volumes',
				width : 100,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigoEditor",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : false,
			width : 960,
			height : 180,
			onSuccess : function() {
				$(".emissaoBandeiras-dataSaida").datepicker({
					showOn : "button",
					buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
					buttonImageOnly : true
				});
				$(".emissaoBandeiras-dataSaida").mask("99/99/9999");
				$(".emissaoBandeiras-volumes").numeric();
			}
		});

	},
	
	pesquisar : function() {
		
		emissaoBandeiraController.dataEmissao = $("#emissaoBandeiras-dataEmissao", this.workspace).val();
		emissaoBandeiraController.fornecedor = $("#fornecedor", this.workspace).val();
		emissaoBandeiraController.numeroNotaDe = $("#emissaoBandeiras-numero-nota-de", this.workspace).val();
		emissaoBandeiraController.numeroNotaAte = $("#emissaoBandeiras-numero-nota-ate", this.workspace).val();
		
		$(".bandeirasRcltoGrid", this.workspace).flexOptions({
			url: contextPath + "/devolucao/emissaoBandeira/pesquisar",
			params: [{name:'dataEmissao', value:emissaoBandeiraController.dataEmissao},
			         {name:'fornecedor', value:emissaoBandeiraController.fornecedor},
					 {name:'numeroNotaDe', value:emissaoBandeiraController.numeroNotaDe},
					 {name:'numeroNotaAte', value:emissaoBandeiraController.numeroNotaAte}] 
		   ,
			newp: 1
		});
		
		$(".bandeirasRcltoGrid", this.workspace).flexReload();
		
	},
	
	executarPreProcessamento : function(resultado) {
		
		var tipoMensagem = null; 
		var listaMensagens = null;
		if (resultado.mensagens) {
			tipoMensagem = resultado.mensagens.tipoMensagem;
        	listaMensagens = resultado.mensagens.listaMensagens;
		}
		
        if (tipoMensagem && listaMensagens) {
              exibirMensagem(tipoMensagem, listaMensagens);
              $(".grids", emissaoBandeiraController.workspace).hide();
              $(".bt_arq", emissaoBandeiraController.workspace).hide();
         } else { 
        	 
        	 if(resultado && resultado.rows) {
 
        		 for(var index in resultado.rows) {
        			 
        			 resultado.rows[index].cell["numeroNotaFiscal"] = '<input value="'+ resultado.rows[index].cell["numeroNotaFiscal"] +'" type="text" maxlength="10" size="12" class="emissaoBandeiras-numeroNotaFiscal" name="emissaoBandeiras-numeroNotaFiscal" id="emissaoBandeiras-numeroNotaFiscal'+ index +'" disabled />';
        			 resultado.rows[index].cell["serieNotaFiscal"] = '<input value="'+ resultado.rows[index].cell["serieNotaFiscal"] +'" type="text" maxlength="10" size="12" class="emissaoBandeiras-serieNotaFiscal" name="emissaoBandeiras-serieNotaFiscal" id="emissaoBandeiras-serieNotaFiscal'+ index +'" disabled />';
        			 resultado.rows[index].cell["dataSaida"] = '<input value="'+ (resultado.rows[index].cell["dataSaida"].length> 0 ? resultado.rows[index].cell["dataSaida"]:new Date() )+'" type="text" maxlength="10" size="12" class="emissaoBandeiras-dataSaida" name="emissaoBandeiras-dataSaida" id="emissaoBandeiras-dataSaida'+ index +'" />';
        			 resultado.rows[index].cell["volumes"] = '<input value="'+ (resultado.rows[index].cell["volumes"] != null ?resultado.rows[index].cell["volumes"]:'') +'" type="text" maxlength="10" size="12" class="emissaoBandeiras-volumes"  name="emissaoBandeiras-volumes" id="emissaoBandeiras-volumes'+ index +'" />';
        			
        		 }
        		 
        	 }
        	 
        	 $(".grids", emissaoBandeiraController.workspace).show();
        	 $(".bt_arq", emissaoBandeiraController.workspace).show();
         } 	 
		
		return resultado;
	},
	
	imprimirArquivo : function(fileType) {
		
		window.location = contextPath + "/devolucao/emissaoBandeira/imprimirArquivo?"
			+ "anoSemana=" + emissaoBandeiraController.anoSemanaPesquisa
			+ "&fornecedor=" + emissaoBandeiraController.fornecedor
			+ "&sortname=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).flexGetSortName()
			+ "&sortorder=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).getSortOrder()
			+ "&rp=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).flexGetRowsPerPage()
			+ "&page=" + $(".bandeirasRcltoGrid", emissaoBandeiraController.workspace).flexGetPageNumber()
			+ "&fileType=" + fileType;

		return false;
	},
	
	imprimirBandeira: function() {
		
		var liberaImpressaoBandeira = true;
		var params = [];
		
		params.push({'name': 'dataEmissao', 'value': emissaoBandeiraController.dataEmissao});
		params.push({'name': 'fornecedor', 'value': emissaoBandeiraController.fornecedor});
		var prazo   = new Date();
		prazo.setHours(0,0,0,0);
		prazo.setDate(prazo.getDate()-15); // permitir imprimir ate 15 dias atras
		$.each($('input[name="emissaoBandeiras-dataSaida"]'), function(k, v) {
			if(typeof(v.value) == 'undefined' || '' == v.value || new Date(v.value.split('/').reverse().join('/')).getTime() < prazo.getTime()) {
				exibirMensagem('WARNING', ['Valor incorreto para a impressão da data.']);
				liberaImpressaoBandeira = false;
				return false;
			}
			params.push({'name': 'dataEnvio[]', 'value': v.value});
		});
		
		$.each($('input[name="emissaoBandeiras-volumes"]'), function(k, v) {
			
			params.push({'name': 'numeroPallets[]', 'value': v.value});
		});
		
		$.each($('input[name="emissaoBandeiras-numeroNotaFiscal"]'), function(k, v) {
			if(typeof(v.value) == 'undefined' || '' == v.value || v.value < 1) {
				liberaImpressaoBandeira = false;
				return false;
			}
			params.push({'name': 'nota[]', 'value': v.value});
		});
		
		$.each($('input[name="emissaoBandeiras-serieNotaFiscal"]'), function(k, v) {
			if(typeof(v.value) == 'undefined' || '' == v.value || v.value < 1) {
				liberaImpressaoBandeira = false;
				return false;

			}
			params.push({'name': 'serie[]', 'value': v.value});
		});
		
		params.push({'name': 'numeroNotaDe',  'value': $("#emissaoBandeiras-numero-nota-de", this.workspace).val()});
		params.push({'name': 'numeroNotaAte', 'value': $("#emissaoBandeiras-numero-nota-ate", this.workspace).val()});
		
		if(liberaImpressaoBandeira) {
			
			$.fileDownload(contextPath + "/devolucao/emissaoBandeira/imprimirBandeira", {
				httpMethod : "POST",
				data : params
			});
		}
		
		return false;
	},
	
	bandeiraManual : function() {
		$('#workspace').tabs('addTab', "Bandeira Manual", contextPath + "/devolucao/emissaoBandeira/bandeiraManual");
		
	},
	imprimirBandeiraManual:function(){
		
		var _this = this;
		$( "#dialog-pallets-bandeira-manual", _this.workspace).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					var semana = $.trim( $("#semana", _this.workspace).val()),
					nrPallets = $.trim( $("#numeroPalletsBandeiraManual", _this.workspace).val()),
					titulo = escape($.trim( $("#titulo", _this.workspace).val())),
					praca = $.trim( $("#praca", _this.workspace).val()),
					dtEnvio = $.trim($("#dataEnvioManual", _this.workspace).val()),
					forncedor = $.trim($("#inputfornecedor", _this.workspace).val()),
					canal = $.trim($("#canal", _this.workspace).val());
					
					if (!semana || !nrPallets || !titulo || !praca || !dtEnvio || !forncedor || !canal){
						
						exibirMensagem('WARNING', ['Todos os campos são obrigatórios.']);
						return;
					}
					
					$( this ).dialog( "close" );
					window.location = contextPath + "/devolucao/emissaoBandeira/imprimirBandeiraManual?"+
					"anoSemana=" + semana
					+"&numeroPallets=" + nrPallets
					+"&titulo=" + titulo
					+"&praca=" + praca
					+"&dataEnvio=" + dtEnvio
					+"&fornecedor=" + forncedor
					+"&canal=" + canal;
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-pallets-bandeira-manual", this.workspace).parents("form")
		});
		return false;

	},
		
}, BaseController);


//@ sourceURL=emissaoBandeira.js
