<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="../css/NDS.css" />
<link rel="stylesheet" type="text/css" href="../css/menu_superior.css" />
<link rel="stylesheet" href="../scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.core.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.effects.core.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.effects.highlight.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.widget.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.position.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.accordion.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/NDS.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.dialog.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.tabs.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.datepicker.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.autocomplete.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/shortcut.js"></script>
<link rel="stylesheet" type="text/css" href="../scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<script language="javascript" type="text/javascript">



$(function() {
	$(".garantiasGrid").flexigrid({
		url : '../xml/garantias-xml.xml',
		dataType : 'xml',
		colModel : [ {
			display : 'Descrição',
			name : 'descricao',
			width : 550,
			sortable : true,
			align : 'left'
		}, {
			display : 'Valor R$',
			name : 'valor',
			width : 140,
			sortable : true,
			align : 'right'
		}],
		width : 740,
		height : 150
	});
});



$(function() {
	$(".imoveisGarantiaGrid").flexigrid({
		url : '../xml/imovel-garantias-xml.xml',
		dataType : 'xml',
		colModel : [ {
			display : 'Proprietário',
			name : 'proprietario',
			width : 115,
			sortable : true,
			align : 'left'
		},{
			display : 'Endereço',
			name : 'endereco',
			width : 150,
			sortable : true,
			align : 'left'
		},{
			display : 'N° Registro',
			name : 'numRegistro',
			width : 60,
			sortable : true,
			align : 'left'
		},{
			display : 'Valor R$',
			name : 'valorImovel',
			width : 60,
			sortable : true,
			align : 'right'
		},{
			display : 'Observação',
			name : 'observacao',
			width : 200,
			sortable : true,
			align : 'left'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 60,
			sortable : true,
			align : 'center'
		}],
		width : 740,
		height : 150
	});
});



$(function() {
	$(".caucaoGrid").flexigrid({
		url : '../xml/caucao-xml.xml',
		dataType : 'xml',
		colModel : [ {
			display : 'Data',
			name : 'data',
			width : 225,
			sortable : true,
			align : 'center'
		},{
			display : '%',
			name : 'percent',
			width : 225,
			sortable : true,
			align : 'center'
		},{
			display : 'Valor R$',
			name : 'valor',
			width : 225,
			sortable : true,
			align : 'right'
		}],
		width : 740,
		height : 150
	});
});



$(function() {
	$(".pessoasGrid").flexigrid({
		url : '../xml/cotas-xml.xml',
		dataType : 'xml',
		colModel : [  {
			display : 'Código',
			name : 'codigo',
			width : 60,
			sortable : true,
			align : 'left'
		},{
			display : 'Nome / Razão Social',
			name : 'nome',
			width : 180,
			sortable : true,
			align : 'left'
		}, {
			display : 'CPF/CNPJ',
			name : 'cpf_cnpj',
			width : 120,
			sortable : true,
			align : 'left'
		}, {
			display : 'RG / Insc.Estadual',
			name : 'rg_inscricao',
			width : 110,
			sortable : true,
			align : 'left'
		}, {
			display : 'Telefone',
			name : 'telefone',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'E-Mail',
			name : 'email',
			width : 220,
			sortable : true,
			align : 'left'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 60,
			sortable : true,
			align : 'center'
		}],
		sortname : "Nome",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	});
});



$(function() {
	$(".enderecosGrid").flexigrid({
		url : '../xml/enderecos-xml.xml',
		dataType : 'xml',
		colModel : [  {
			display : 'Tipo Endereço',
			name : 'tipoendereco',
			width : 80,
			sortable : true,
			align : 'left'
		},{
			display : 'Logradouro',
			name : 'logradouro',
			width : 205,
			sortable : true,
			align : 'left'
		}, {
			display : 'Bairro',
			name : 'bairro',
			width : 120,
			sortable : true,
			align : 'left'
		}, {
			display : 'Cep',
			name : 'cep',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Cidade',
			name : 'cidade',
			width : 90,
			sortable : true,
			align : 'left'
		}, {
			display : 'Principal',
			name : 'principal',
			width : 50,
			sortable : true,
			align : 'center'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 60,
			sortable : true,
			align : 'center'
		}],
		width : 770,
		height : 150
	});
});



$(function() {
	$(".telefonesGrid").flexigrid({
		url : '../xml/telefones-xml.xml',
		dataType : 'xml',
		colModel : [  {
			display : 'Tipo Telefone',
			name : 'tipotelefone',
			width : 165,
			sortable : true,
			align : 'left'
		},{
			display : 'DDD',
			name : 'ddd',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Número',
			name : 'numero',
			width : 150,
			sortable : true,
			align : 'left'
		}, {
			display : 'Ramal / ID',
			name : 'ramal',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Principal',
			name : 'principal',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 60,
			sortable : true,
			align : 'center'
		}],
		width : 770,
		height : 150
	});
});



$(function() {
	$(".bancosGrid").flexigrid({
		url : '../xml/bancos_1-xml.xml',
		dataType : 'xml',
		colModel : [  {
			display : 'Banco',
			name : 'banco',
			width : 110,
			sortable : true,
			align : 'left'
		},{
			display : 'Nome',
			name : 'nome',
			width : 450,
			sortable : true,
			align : 'left'
		}, {
			display : 'Principal',
			name : 'principal',
			width : 150,
			sortable : true,
			align : 'center'
		}],
		width : 770,
		height : 180
	});
});



$(function() {
	$(".bancosPjGrid").flexigrid({
		url : '../xml/bancos_1-xml.xml',
		dataType : 'xml',
		colModel : [  {
			display : 'Banco',
			name : 'banco',
			width : 110,
			sortable : true,
			align : 'left'
		},{
			display : 'Nome',
			name : 'nome',
			width : 450,
			sortable : true,
			align : 'left'
		}, {
			display : 'Principal',
			name : 'principal',
			width : 150,
			sortable : true,
			align : 'center'
		}],
		width : 770,
		height : 180
	});
});


$(function() {
	$(".sociosPjGrid").flexigrid({
		url : '../xml/socios-xml.xml',
		dataType : 'xml',
		colModel : [  {
			display : 'Nome',
			name : 'nome',
			width : 380,
			sortable : true,
			align : 'left'
		},{
			display : 'Cargo',
			name : 'cargo',
			width : 190,
			sortable : true,
			align : 'left'
		}, {
			display : 'Principal',
			name : 'principal',
			width : 70,
			sortable : true,
			align : 'center'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 60,
			sortable : true,
			align : 'center'
		}],
		width : 770,
		height : 180
	});
});



$(function() {
	$(".endPDVGrid").flexigrid({
		url : '../xml/pdvs-xml.xml',
		dataType : 'xml',
		colModel : [  {
			display : ' ',
			name : 'sel',
			width : 20,
			sortable : true,
			align : 'center'
		},{
			display : 'ID',
			name : 'id',
			width : 30,
			sortable : true,
			align : 'left'
		}, {
			display : 'Endereço',
			name : 'endereco',
			width : 315,
			sortable : true,
			align : 'left'
		}, {
			display : 'Bairro',
			name : 'bairro',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Cidade',
			name : 'cidade',
			width : 145,
			sortable : true,
			align : 'left'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 60,
			sortable : true,
			align : 'center'
		}],
		width : 770,
		height : 150
	});
});



$(function() {
	$(".PDVsGrid").flexigrid({
		url : '../xml/pdvsTodos-xml.xml',
		dataType : 'xml',
		colModel : [  {
			display : 'Nome PDV',
			name : 'nomePdv',
			width : 100,
			sortable : true,
			align : 'left'
		},{
			display : 'Tipo de Ponto',
			name : 'tipoPonto',
			width : 100,
			sortable : true,
			align : 'left'
		},{
			display : 'Contato',
			name : 'contato',
			width : 80,
			sortable : true,
			align : 'left'
		}, {
			display : 'Telefone',
			name : 'telefone',
			width : 80,
			sortable : true,
			align : 'left'
		}, {
			display : 'Endereço',
			name : 'endereco',
			width : 150,
			sortable : true,
			align : 'left'
		}, {
			display : 'Bairro',
			name : 'bairro',
			width : 90,
			sortable : true,
			align : 'left'
		}, {
			display : 'Cidade',
			name : 'cidade',
			width : 110,
			sortable : true,
			align : 'left'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 55,
			sortable : true,
			align : 'center'
		}],
		width : 880,
		height : 150
	});
});



function popup_cpf() {
	    
	    //NUMERO_COTA=123; PROVISORIO ATÉ EXISTIR O CADASTRO DA COTA.
	    editarCotaCobranca(123);
	    
		$( "#dialog-cpf" ).dialog({
			resizable: false,
			height:590,
			width:950,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					postarCotaCobranca();
					
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
		
};


function popup_cnpj() {
	
		$( "#dialog-cnpj" ).dialog({
			resizable: false,
			height:590,
			width:950,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
		
};

function popup_excluir() {
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
};
	
	
	function popup_excluir_end() {
	
		$( "#dialog-excluir-end" ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function popup_excluir_tel() {
		
			$( "#dialog-excluir-tel" ).dialog({
				resizable: false,
				height:'auto',
				width:380,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
	};
		
	function popup_novoPdv() {
		
			$( "#dialog-pdv" ).dialog({
				resizable: false,
				height:600,
				width:890,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
	};	

	function popup_img() {
	
		$( "#dialog-img" ).dialog({
			resizable: false,
			height:'auto',
			width:450,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
    };
	function popup_telefone() {
	
		$( "#dialog-telefone" ).dialog({
			resizable: false,
			height:'auto',
			width:450,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function popup_endereco() {
	
		$( "#dialog-endereco" ).dialog({
			resizable: false,
			height:'auto',
			width:860,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	
	function popup_gerador() {
	
		$( "#dialog-gerador" ).dialog({
			resizable: false,
			height:'auto',
			width:625,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$(".geradores").show();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
};

	$(function() {
			$( "#tabpf" ).tabs();
			$( "#tabpj" ).tabs();
			$( "#tabpdv" ).tabs();
	});
	$(function() {
		$( "#cotaDe" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#cotaAte" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#cotaPjDe" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#cotaPjAte" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
	});
	
	
	$(function() {
		var availableTags = [
			"Antonio Jose da Silva",
			"Paulo Roberto Pereira",
			"John Snow",
			"Tiririca"
		];
		$( "#nomes" ).autocomplete({
			source: availableTags			
		});
	});


	function mostra_dados_fiador(){
			$('#nomes').keydown(function(e) { 
			if(e.keyCode == 13) { 
				$( ".dadosFiador" ).show();		
			} 
		}); 
	}
	
	
	$(function() {
		var availableTags = [
			"Antonio Jose da Silva",
			"Paulo Roberto Pereira",
			"John Snow",
			"Tiririca"
		];
		$( "#nomes_pf" ).autocomplete({
			source: availableTags			
		});
	});
	
	function mostra_dados_fiador_pf(){
			$('#nomes_pf').keydown(function(e) { 
			if(e.keyCode == 13) { 
				$( ".dadosFiador_pf" ).show();		
			} 
		}); 
	}

	shortcut.add("F2",function() 
	{
		popup_cnpj();
	});










//FINANCEIRO PESSOA FISICA


function opcaoPagtoPf(op){
	
	$('#tabpf-financeiro-divBoleto').hide();
	$('#tabpf-financeiro-divComboBanco').hide();
	$('#tabpf-financeiro-divDeposito').hide();
	
	if ((op=='BOLETO')||(op=='BOLETO_EM_BRANCO')){
		$('#tabpf-financeiro-divComboBanco').show();
		$('#tabpf-financeiro-divBoleto').show();
    }
	else if ((op=='CHEQUE')||(op=='TRANSFERENCIA_BANCARIA')){
		$('#tabpf-financeiro-divComboBanco').show();
	}    
	else if (op=='DEPOSITO'){
		$('#tabpf-financeiro-divDeposito').show();
	}    
};


function editarCotaCobranca(idCota){
	var data = [{name: 'idCota', value: idCota}];
	$.postJSON("<c:url value='/cadastro/cota/editarCotaCobranca' />",
			   data,
			   sucessCallbackCotaCobranca, 
			   null,
			   true);
}


function sucessCallbackCotaCobranca(resultado) {
	
	$("#tabpf-financeiro-tipoCobranca").val(resultado.tipoCobranca);
	$("#tabpf-financeiro-banco").val(resultado.idBanco);
	$("#tabpf-financeiro-recebeEmail").val(resultado.recebeEmail);
	$("#tabpf-financeiro-numBanco").val(resultado.numBanco);
	$("#tabpf-financeiro-nomeBanco").val(resultado.nomeBanco);
	$("#tabpf-financeiro-agencia").val(resultado.agencia);
	$("#tabpf-financeiro-agenciaDigito").val(resultado.agenciaDigito);
	$("#tabpf-financeiro-conta").val(resultado.conta);
    $("#tabpf-financeiro-contaDigito").val(resultado.contaDigito);
	$("#tabpf-financeiro-fatorVencimento").val(resultado.fatorVencimento);

	$("#tabpf_financeiro_sugereSuspensao").val(resultado.sugereSuspensao);
	document.formFinanceiroPF.tabpf_financeiro_sugereSuspensao.checked = resultado.sugereSuspensao;
	
	$("#tabpf_financeiro_contrato").val(resultado.contrato);
	document.formFinanceiroPF.tabpf_financeiro_contrato.checked = resultado.contrato;
	
	
	$("#tabpf_financeiro_PS").val(resultado.segunda);
	document.formFinanceiroPF.tabpf_financeiro_PS.checked = resultado.segunda;
	
	$("#tabpf_financeiro_PT").val(resultado.terca);
	document.formFinanceiroPF.tabpf_financeiro_PT.checked = resultado.terca;
	
	$("#tabpf_financeiro_PQ").val(resultado.quarta);
	document.formFinanceiroPF.tabpf_financeiro_PQ.checked = resultado.quarta;
	
	$("#tabpf_financeiro_PQu").val(resultado.quinta);
	document.formFinanceiroPF.tabpf_financeiro_PQu.checked = resultado.quinta;
	
	$("#tabpf_financeiro_PSex").val(resultado.sexta);
	document.formFinanceiroPF.tabpf_financeiro_PSex.checked = resultado.sexta;
	
	$("#tabpf_financeiro_PSab").val(resultado.sabado);
	document.formFinanceiroPF.tabpf_financeiro_PSab.checked = resultado.sabado;
	
	$("#tabpf_financeiro_PDom").val(resultado.domingo);
	document.formFinanceiroPF.tabpf_financeiro_PDom.checked = resultado.domingo;
	
	$("#tabpf-financeiro-valorMinimo").val(resultado.valorMinimo);
	$("#tabpf-financeiro-comissao").val(resultado.comissao);
	$("#tabpf-financeiro-qtdDividasAberto").val(resultado.qtdDividasAberto);
	$("#tabpf-financeiro-vrDividasAberto").val(resultado.vrDividasAberto);
}





function postarCotaCobranca() {
	
	var tipoCobranca        = $("#tabpf-financeiro-tipoCobranca").val();
	var idBanco             = $("#tabpf-financeiro-banco").val();
	var recebeEmail         = $("#tabpf-financeiro-recebeEmail").val();
	var numBanco            = $("#tabpf-financeiro-numBanco").val();
	var nomeBanco           = $("#tabpf-financeiro-nomeBanco").val();
	var agencia             = $("#tabpf-financeiro-agencia").val();
	var agenciaDigito       = $("#tabpf-financeiro-agenciaDigito").val();
	var conta               = $("#tabpf-financeiro-conta").val();
	var contaDigito         = $("#tabpf-financeiro-contaDigito").val();
	var fatorVencimento     = $("#tabpf-financeiro-fatorVencimento").val();
	
	$("#tabpf_financeiro_sugereSuspensao").val(0);
	if (document.formFinanceiroPF.tabpf_financeiro_sugereSuspensao.checked){
		$("#tabpf_financeiro_sugereSuspensao").val(1);
	}
	var sugereSuspensao   = $("#tabpf_financeiro_sugereSuspensao").val();
	
	$("#tabpf_financeiro_contrato").val(0);
	if (document.formFinanceiroPF.tabpf_financeiro_contrato.checked){
		$("#tabpf_financeiro_contrato").val(1);
	}
	var contrato          = $("#tabpf_financeiro_contrato").val();
	
	$("#tabpf_financeiro_PS").val(0);
	if (document.formFinanceiroPF.tabpf_financeiro_PS.checked){
		$("#tabpf_financeiro_PS").val(1);
	}
	var segunda           = $("#tabpf_financeiro_PS").val();
	
	$("#tabpf_financeiro_PT").val(0);
	if (document.formFinanceiroPF.tabpf_financeiro_PT.checked){
		$("#tabpf_financeiro_PT").val(1);
	}
	var terca           = $("#tabpf_financeiro_PT").val();
	
	$("#tabpf_financeiro_PQ").val(0);
	if (document.formFinanceiroPF.tabpf_financeiro_PQ.checked){
		$("#tabpf_financeiro_PQ").val(1);
	}
	var quarta           = $("#tabpf_financeiro_PQ").val();
	
	$("#tabpf_financeiro_PQu").val(0);
	if (document.formFinanceiroPF.tabpf_financeiro_PQu.checked){
		$("#tabpf_financeiro_PQu").val(1);
	}
	var quinta           = $("#tabpf_financeiro_PQu").val();
	
	$("#tabpf_financeiro_PSex").val(0);
	if (document.formFinanceiroPF.tabpf_financeiro_PSex.checked){
		$("#tabpf_financeiro_PSex").val(1);
	}
	var sexta           = $("#tabpf_financeiro_PSex").val();
	
	$("#tabpf_financeiro_PSab").val(0);
	if (document.formFinanceiroPF.tabpf_financeiro_PSab.checked){
		$("#tabpf_financeiro_PSab").val(1);
	}
	var sabado           = $("#tabpf_financeiro_PSab").val();
	
	$("#tabpf_financeiro_PDom").val(0);
	if (document.formFinanceiroPF.tabpf_financeiro_PDom.checked){
		$("#tabpf_financeiro_PDom").val(1);
	}
	var domingo           = $("#tabpf_financeiro_PDom").val();
	 
	var valorMinimo       = $("#tabpf-financeiro-valorMinimo").val();
	var comissao          = $("#tabpf-financeiro-comissao").val();
	var qtdDividasAberto  = $("#tabpf-financeiro-qtdDividasAberto").val();
	var vrDividasAberto   = $("#tabpf-financeiro-vrDividasAberto").val();
	
	var numCota=123;//NUMERO_COTA=123; PROVISORIO ATÉ EXISTIR O CADASTRO DA COTA.
	
	$.postJSON("<c:url value='/cadastro/cota/postarCotaCobranca'/>",
			   "cotaCobranca.numCota="+numCota+  
			   "&cotaCobranca.tipoCobranca="+tipoCobranca+ 
			   "&cotaCobranca.idBanco="+idBanco+            
			   "&cotaCobranca.recebeEmail="+recebeEmail+        
			   "&cotaCobranca.numBanco="+numBanco+        
			   "&cotaCobranca.nomeBanco="+nomeBanco+          
			   "&cotaCobranca.agencia="+agencia+            
			   "&cotaCobranca.agenciaDigito="+agenciaDigito+     
			   "&cotaCobranca.conta="+conta+              
			   "&cotaCobranca.contaDigito="+contaDigito+        
			   "&cotaCobranca.fatorVencimento="+fatorVencimento+    
			   "&cotaCobranca.sugereSuspensao="+sugereSuspensao+    
			   "&cotaCobranca.contrato="+contrato+   
			   "&cotaCobranca.domingo="+domingo+    
			   "&cotaCobranca.segunda="+segunda+            
			   "&cotaCobranca.terca="+terca+            
			   "&cotaCobranca.quarta="+quarta+            
			   "&cotaCobranca.quinta="+quinta+            
			   "&cotaCobranca.sexta="+sexta+            
			   "&cotaCobranca.sabado="+sabado+           
			   "&cotaCobranca.valorMinimo="+valorMinimo+        
			   "&cotaCobranca.comissao="+comissao+          
			   "&cotaCobranca.qtdDividasAberto="+qtdDividasAberto+   
			   "&cotaCobranca.vrDividasAberto="+vrDividasAberto);
}







function buscaBancos(tipoCobranca){
	/*
	var data = [{name: 'tipoCobranca', value: tipoCobranca}];
	$.postJSON("<c:url value='/cadastro/cota/buscaComboBancos' />",
			   data);
	*/
}






//------------------------


</script>


<style>

.diasFunc label, .finceiro label, .materialPromocional label{ 
    vertical-align:super;
}
.complementar label, .distribuicao label{ 
    vertical-align:super; margin-right:5px; margin-left:5px;
}

#dialog-pdv fieldset{
    width:777px!important; margin-bottom:10px;  margin-left: -11px;
}
#dialog-pdv fieldset label{
    float:none!important;
}
.geradorFluxo fieldset, .especialidades fieldset{ 
    width:245px!important; padding:11px; margin:0px;
}
#dialog-telefone, #dialog-endereco{
    display:none;
}
.bt_imprimir{
    display:none;
}
#tabpj-7 fieldset, #tabpf-7 fieldset {
    width:755px!important;
}

#tabpf-financeiro-divBoleto,
#tabpf-financeiro-divTrasnsferenciaBancaria,
#tabpf-financeiro-divDeposito {
    display:none;
}

</style>


</head>

<body>

<div id="dialog-endereco" title="Incluir Endereço">
<table width="754" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				<td width="103">Tipo Endereço:</td>
				<td width="252"><select  style="width:157px"><option>Comercial</option><option>Local de Entrega</option><option>Residencial</option><option>Cobrança</option></select></td>
				<td width="95">CEP:</td>
				<td width="276"><input type="text" style="float:left; margin-right:5px;" /><span class="classPesquisar" title="Pesquisar Cep."><a href="javascript:;">&nbsp;</a></span></td>
			</tr>
			<tr>
				<td>Tipo :</td>
				<td><select name="select"  style="width:80px">
				  <option>Av.</option>
				  <option>Rua</option>
				  <option>Travessa</option>
			  </select></td>
				<td>Logradouro:</td>
				<td><input type="text" style="width:250px" /></td>
			</tr>
			<tr>
				<td>N&uacute;mero:</td>
				<td><input type="text" style="width:50px" /></td>
				<td>Complemento:</td>
				<td><input type="text" style="width:250px" /></td>
			</tr>
			<tr>
				<td>Bairro:</td>
				<td><input type="text"  style="width:230px" /></td>
				<td>Cidade:</td>
				<td><input type="text" style="width:250px" /></td>
			</tr>
			<tr>
			  <td>UF:</td>
			  <td><input type="text" style="width:50px" /></td>
			  <td>Principal:</td>
			  <td><input type="checkbox" name="checkbox" id="checkbox" /></td>
  </tr>
			<tr>
			  <td>&nbsp;</td>
			  <td><span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span></td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
		  </tr>		
		</table>
</div>


<div id="dialog-telefone" title="Incluir Telefone">
<table width="280" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				<td width="72">Tipo:</td>
				<td width="192">
                <select onchange="opcaoTel(this.value);" style="width:174px;">   
                  <option value="" selected="selected">Selecione</option>
                  <option value="1">Comercial</option>
				  <option value="">Celular</option>
				  <option value="1">Fax</option>
				  <option value="">Residencial</option>
				  <option value="2">Rádio</option>  
                </select>  
                <!--<select name="select" style="width:174px;" onchange="tel();">
				  <option selected="selected"> </option>
				  
              </select>--></td>
			</tr>
			<tr>
			  <td>Telefone: </td>
			  <td><input type="text" style="width:40px" />
			    -
		      <input type="text" style="width:110px" /></td>
  </tr>
			<tr>
			  <td colspan="2">
              	<div id="divOpcao1" style="display:none;">   
                  <div style="width:80px; float:left;">Ramal:</div>
                  <input type="text" style="width:40px; float:left;" />
                </div>   
                <div id="divOpcao2" style="display:none;">   
                  <div style="width:80px; float:left;;">ID:</div>
                  <input type="text" style="width:167px; float:left;" /> 
                </div>   
              
              
              </td>
			  </tr>
			<tr>
			  <td><label for="principal1">Principal:</label></td>
			  <td class="complementar">
			    <input type="checkbox" name="principal1" id="principal1" />
		      </td>
  </tr>
			<tr>
			  <td>&nbsp;</td>
			  <td ><span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span></td>
  </tr>
	  </table>
</div>



<div id="dialog-pdv" title="PDV Cota">
<div id="tabpdv">
    <ul>
        <li><a href="#tabpdv-1">Dados Básicos</a></li>
        <li><a href="#tabpdv-2">Endereços</a></li>
        <li><a href="#tabpdv-3">Telefones</a></li>
        <li><a href="#tabpdv-4">Caract. / Segmentação</a></li>
        <li><a href="#tabpdv-5">Especialidade</a></li>
        <li><a href="#tabpdv-6">Gerador de Fluxo</a></li>
        <li><a href="#tabpdv-7">MAP</a></li>
        <!--<li><a href="#tabpdv-8">Segmentação</a></li>-->
  </ul>
  
  <div id="tabpdv-1">
  <fieldset>
  	<legend>Dados Básicos</legend>
    <table width="777" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="572" valign="top">
    <table width="572" border="0" cellspacing="1" cellpadding="1">
      <tr>
        <td>Data Início:</td>
        <td><input type="text"  style="width:80px;" disabled="disabled"/></td>
      </tr>
      <tr>
        <td width="135">Nome PDV:</td>
        <td width="430"><input type="text" name="textfield12" id="textfield12" style="width:300px;"/></td>
      </tr>
      <tr>
        <td>Contato:</td>
        <td><input type="text" name="textfield6" id="textfield6" style="width:300px;"/></td>
      </tr>
      <tr>
        <td>Site:</td>
        <td><input type="text" name="textfield7" id="textfield7" style="width:300px;"/></td>
      </tr>
      <tr>
        <td>E-mail:</td>
        <td><input type="text" name="textfield8" id="textfield8" style="width:300px;"/></td>
      </tr>
      <tr>
        <td>Ponto de Referência:</td>
        <td><input type="text" name="textfield11" id="textfield11" style="width:300px;"/></td>
      </tr>
      <tr>
        <td align="right"><input type="checkbox" name="checkbox10" id="checkbox3" value="1" onclick="opcaoEstabelecimento(this.value);" /></td>
        <td>Dentro de Outro Estabelecimento?</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>
        <div id="divOpcao12" style="display:none;">
        Tipo Estabelecimento:
          <select name="select15" id="select13" style="width:180px;">
          <option selected="selected">Selecione</option>
          <option>Hiper / Supermercado</option>
          <option>Shopping</option>
          <option>Galeria</option>
          <option>Posto Serviço</option>
        </select></div></td>
      </tr>
      </table>
      <br />

    </td>
    <td width="191" align="center" valign="top"><img src="../images/bancaJornal.jpg" width="191" height="136" alt="Banca" /><br />
    <a href="javascript:" onclick="popup_img();"><img src="../images/bt_cadastros.png" alt="Editar Imagem" width="15" height="15" hspace="10" vspace="3" border="0"  /></a><a href="javascript:"><img src="../images/ico_excluir.gif" alt="Excluir Imagem" width="15" height="15" hspace="10" vspace="3" border="0" /></a></td>
  </tr>
</table>
    <table width="777" border="0" cellspacing="1" cellpadding="1">
      <tr>
    <td width="135">Dias Funcionamento:</td>
    <td width="252" class="diasFunc"><select name="select9" id="select9" style="width:230px;">
      <option selected="selected">Selecione</option>
      <option>Diário</option>
      <option>Segunda - Sexta</option>
      <option>Finais de Semana</option>
      <option>Feriados</option>
      <option>24 Horas</option>
      <option>-----------------------------------------</option>
      <option>Domingo</option>
      <option>Segunda-feira</option>
      <option>Terça-feira</option>
      <option>Quarta-feira</option>
      <option>Quinta-feira</option>
      <option>Sexta-feira</option>
      <option>Sábado</option>
      </select></td>
    <td width="47">Horário:</td>
    <td width="179"><input type="text" name="textfield10" id="textfield9" style="width:60px;"/>
      As
      <input type="text" name="textfield10" id="textfield10" style="width:60px;"/></td>
    <td width="148"><span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span></td>
  </tr>
</table>
<table width="777" border="0" cellspacing="1" cellpadding="1">
      <tr class="class_linha_1">
        <td width="138">&nbsp;</td>
        <td width="249" class="diasFunc">Segunda-feira</td>
        <td width="47">&nbsp;</td>
        <td width="100">06:00 as 23:00</td>
        <td width="227"><a href="javascript:;"><img src="../images/ico_excluir.gif" alt="Excluir" width="15" height="15" border="0" /></a></td>
      </tr>
      <tr class="class_linha_2">
        <td>&nbsp;</td>
        <td class="diasFunc">Sábado</td>
        <td>&nbsp;</td>
        <td>06:00 as 22:00</td>
        <td><a href="javascript:;"><img src="../images/ico_excluir.gif" alt="Excluir" width="15" height="15" border="0" /></a></td>
      </tr>
</table>
<br />
<table width="777" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td>Tamanho:</td>
    <td><select name="select13" id="select15" style="width:200px;">
      <option>Selecione...</option>
      <option>P (2m² até 4m² )</option>
      <option>M (4m² até 8m²)</option>
      <option>G (8m² até 12m²)</option>
      <option>SG  (acima de 12m²)</option>
    </select></td>
    <td>Qtde  Funcionários:</td>
    <td><input type="text" name="textfield23" id="textfield24" style="width:60px;"/></td>
  </tr>
  <tr>
    <td>Sistema IPV:</td>
    <td><input name="" type="checkbox" value="" /></td>
    <td>% Faturamento:</td>
    <td><input type="text" name="textfield9" id="textfield25" style="width:60px;"/></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td width="136">Tipo de Licença:</td>
    <td width="239"><select name="select10" id="select10" style="width:232px;">
      <option selected="selected">Selecione</option>
      </select></td>
    <td width="132">Número da Licença:</td>
    <td width="257"><input type="text" name="textfield13" id="textfield13" style="width:225px;"/></td>
  </tr>
  <tr>
    <td>Nome  da Licença:</td>
    <td><span class="diasFunc">
      <input type="text" name="textfield14" id="textfield14" style="width:225px;"/>
      </span></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
<br />

  </fieldset>
  
  </div>
 
  
  <div id="tabpdv-2">
  <table width="754" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				<td width="103">Tipo Endereço:</td>
				<td width="252"><select  style="width:157px"><option>Comercial</option><option>Local de Entrega</option><option>Residencial</option><option>Cobrança</option></select></td>
				<td width="95">CEP:</td>
				<td width="276"><input type="text" style="float:left; margin-right:5px;" /><span class="classPesquisar" title="Pesquisar Cep."><a href="javascript:;">&nbsp;</a></span></td>
			</tr>
			<tr>
				<td>Tipo :</td>
				<td><select name="select"  style="width:80px">
				  <option>Av.</option>
				  <option>Rua</option>
				  <option>Travessa</option>
			  </select></td>
				<td>Logradouro:</td>
				<td><input type="text" style="width:250px" /></td>
			</tr>
			<tr>
				<td>N&uacute;mero:</td>
				<td><input type="text" style="width:50px" /></td>
				<td>Complemento:</td>
				<td><input type="text" style="width:250px" /></td>
			</tr>
			<tr>
				<td>Bairro:</td>
				<td><input type="text"  style="width:230px" /></td>
				<td>Cidade:</td>
				<td><input type="text" style="width:250px" /></td>
			</tr>
			<tr>
			  <td>UF:</td>
			  <td><input type="text" style="width:50px" /></td>
			  <td>Principal:</td>
			  <td><input type="checkbox" name="checkbox" id="checkbox" /></td>
  </tr>
			<tr>
			  <td>&nbsp;</td>
			  <td><span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span></td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
		  </tr>		
		</table>
        <br />
        <label><strong>Endereços Cadastrados</strong></label><br />

        <table class="enderecosGrid"></table>
        
  </div>
  
  <div id="tabpdv-3">
  <table width="280" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				<td width="72">Tipo:</td>
				<td width="192">
                <select onchange="opcaoTel(this.value);" style="width:174px;">   
                  <option value="" selected="selected">Selecione</option>
                  <option value="1">Comercial</option>
				  <option value="">Celular</option>
				  <option value="1">Fax</option>
				  <option value="">Residencial</option>
				  <option value="2">Rádio</option>  
                </select>  
                <!--<select name="select" style="width:174px;" onchange="tel();">
				  <option selected="selected"> </option>
				  
              </select>--></td>
			</tr>
			<tr>
			  <td>Telefone: </td>
			  <td><input type="text" style="width:40px" />
			    -
		      <input type="text" style="width:110px" /></td>
  </tr>
			<tr>
			  <td colspan="2">
              	<div id="divOpcaoPj1" style="display:none;">   
                  <div style="width:80px; float:left;">Ramal:</div>
                  <input type="text" style="width:40px; float:left;" />
                </div>   
                <div id="divOpcaoPj2" style="display:none;">   
                  <div style="width:80px; float:left;;">ID:</div>
                  <input type="text" style="width:167px; float:left;" /> 
                </div>   
              
              
              </td>
			  </tr>
			<tr>
			  <td><label for="principal1">Principal:</label></td>
			  <td class="complementar">
			    <input type="checkbox" name="principal1" id="principal1" />
		      </td>
  </tr>
			<tr>
			  <td>&nbsp;</td>
			  <td ><span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span></td>
  </tr>
	  </table>
      <br />
        <label><strong>Telefones Cadastrados</strong></label><br />

        <table class="telefonesGrid"></table>
  
  </div>

  
  
  <div id="tabpdv-4">
  <fieldset>
  	<legend>Características</legend>
    <table width="777" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td colspan="2" class="complementar"><input name="ptoPrincipal" type="checkbox" value="" id="ptoPrincipal" />
      <label for="ptoPrincipal">Ponto Principal</label>
      <br clear="all" />
      <input name="balcaoCentral" type="checkbox" value="" id="balcaoCentral" />
      <label for="balcaoCentral">Balcão Central</label>
      <br clear="all" />
      <input name="temComputador" type="checkbox" value="" id="temComputador" />
      <label for="temComputador">Tem Computador?</label>
      <br clear="all" />
      <input name="luminoso" type="checkbox" value="" id="luminoso" />
      <label for="luminoso">Luminoso</label>
      <textarea name="textarea" cols="" rows="2" style="width:610px;"></textarea>
      </td>
  </tr>
  </table>
  </fieldset>
  <fieldset>
  	<legend>Segmentação</legend>
    <table width="522" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td width="123">Tipo de Ponto:</td>
    <td width="392"><select name="select8" id="select9" style="width:232px;">
      <option selected="selected">Selecione...</option>
      <option>Banca</option>
      <option>Livraria</option>
      <option>Revistaria</option>
    </select></td>
    </tr>
  <tr>
    <td>Área de Influência:</td>
    <td><select name="" style="width:232px;">
      <option selected="selected">Selecione....</option>
    </select></td>
    </tr>
  <tr>
    <td>Cluster:</td>
    <td><select name="select16" id="select14" style="width:232px;">
      <option selected="selected">Selecione...</option>
      <option>Popular</option>
    </select></td>
    </tr>
    </table>
  </fieldset>
  </div>
  
  
  
  <div id="tabpdv-5">
  <fieldset>
  	<legend>Especialidade</legend>
    <table width="605" border="0" align="center" cellpadding="2" cellspacing="2">
  <tr class="especialidades">
    <td width="264" valign="top">
    <fieldset style="width:250px!important;">
    <legend>Caracteristicas</legend>
    
    <select name="select4" size="10" multiple="multiple" id="select3" style="height:270px; width:245px;">
      <option>Decoração</option>
      <option>Fasciculos</option>
      <option>Figurinhas</option>
      <option>Generalista</option>
      <option>Informática</option>
      <option>Moda</option>
      <option>Quadrinhos</option>
      <option>Sexo</option>
    </select>
    </fieldset></td>
    <td width="69" align="center"><img src="../images/seta_vai_todos.png" width="39" height="30" /><br />
      <br />      <img src="../images/seta_volta_todos.png" width="39" height="30" /> <br /></td>
    <td width="252" valign="top">
    <fieldset style="width:250px!important;">
    <legend>Especialidades</legend>
    <select name="select5" size="10" multiple="multiple" id="select4" style="height:270px; width:245px;">
</select>
</fieldset></td>
  </tr>
</table>
  </fieldset>
  </div>
  
  <div id="tabpdv-6">
  <fieldset>
  	<legend>Gerador de Fluxo</legend>
    <table width="620" border="0" align="center" cellpadding="2" cellspacing="2">
  <tr class="geradorFluxo">
    <td width="252" valign="top">
    <fieldset style="width:250px!important;">
    <legend>Tipo Gerador Fluxo</legend>
    <select name="select11" size="10" multiple="multiple" id="select5" style="height:267px; width:245px;">
      <option>Academia de Ginastica</option>
      <option>Aeroporto</option>
      <option>Agência de Turismo</option>
      <option>Banco</option>
      <option>Bazares</option>
      <option>Biblioteca</option>
      <option>Estética</option>
    </select>
    </fieldset></td>
    <td width="68" align="center" valign="top"><table width="53%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="122" valign="top"><br />          <img src="../images/seta_vai_todos.png" width="39" height="30" /></td>
      </tr>
      <tr>
        <td><img src="../images/seta_vai_todos.png" width="39" height="30" /><br />
          <br />
          <img src="../images/seta_volta_todos.png" width="39" height="30" /></td>
      </tr>
    </table></td>
    <td width="280" valign="top">
    <fieldset style="width:250px!important;">
    <legend>Gerador Fluxo Principal</legend>
    	<input name="" type="text" style="width:240px; " />
    </fieldset>
    

    <fieldset style="margin-top:5px; width:250px!important;">
    <legend>Gerador Fluxo Secundário</legend>
	    <select name="select11" size="10" multiple="multiple" id="select6" style="height:200px; width:245px;">
    </select>
    </fieldset>
   
    </td>
  </tr>
  <tr class="geradorFluxo">
    <td valign="top"><strong>Gerador:</strong></td>
    <td align="center" valign="top">&nbsp;</td>
    <td valign="top"><strong>Gerador:</strong></td>
  </tr>
  <tr class="geradorFluxo">
    <td valign="top"><span style="width:250px!important;">
      <input name="input" type="text" style="width:240px; " />
    </span></td>
    <td align="center" valign="top">&nbsp;</td>
    <td valign="top"><span style="width:250px!important;">
      <input name="input2" type="text" style="width:240px; " />
    </span></td>
  </tr>
    </table>
  </fieldset>
  </div>
  <div id="tabpdv-7">
  <fieldset>
  	<legend>MAP</legend>
    <table width="605" border="0" align="center" cellpadding="2" cellspacing="2">
  <tr class="especialidades">
    <td width="264" valign="top">
    <fieldset style="width:250px!important;">
    <legend>Material Promocional</legend>
    
    <select name="select4" size="10" multiple="multiple" id="select3" style="height:270px; width:245px;">
      <option>Adesivo</option>
      <option>Bandeirola</option>
      <option>Brindes</option>
      <option>Cartaz Grande</option>
      <option>Display Acrilico</option>
      <option>Display Aramado de Chão</option>
      <option>Faixa de Banca</option>
      <option>Móbile</option>
      <option>Poster</option>
      <option>Reprint Capa</option>
      <option>Sapateira</option>
      <option>Totem</option>
    </select>
    </fieldset></td>
    <td width="69" align="center"><img src="../images/seta_vai_todos.png" width="39" height="30" /><br />
      <br />      <img src="../images/seta_volta_todos.png" width="39" height="30" /> <br /></td>
    <td width="252" valign="top">
    <fieldset style="width:250px!important;">
    <legend>Tem espaço para receber o MAP</legend>
    <select name="select5" size="10" multiple="multiple" id="select4" style="height:270px; width:245px;">
</select>
</fieldset></td>
  </tr>
</table>
  </fieldset>
  </div>
  
  
  <!--<div id="tabpdv-8">
  <fieldset>
  	<legend>Segmentação</legend>
    <table width="522" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td width="123">Tipo de Ponto:</td>
    <td width="392"><select name="select8" id="select9" style="width:232px;">
      <option selected="selected">Selecione...</option>
      <option>Banca</option>
      <option>Livraria</option>
      <option>Revistaria</option>
    </select></td>
    </tr>
  <tr>
    <td>Área de Influência:</td>
    <td><select name="" style="width:232px;">
      <option selected="selected">Selecione....</option>
    </select></td>
    </tr>
  <tr>
    <td>Cluster:</td>
    <td><select name="select16" id="select14" style="width:232px;">
      <option selected="selected">Selecione...</option>
      <option>Popular</option>
    </select></td>
    </tr>
    </table>
  </fieldset>
  </div>-->
  
  
  <br clear="all" />

</div>



</div>





<div id="dialog-img" title="Incluir Foto do PDV">
<br />

	<input type="file" size="40" />
</div>

<div id="dialog-excluir" title="Excluir Cota">
	<p>Confirma a exclusão desta Cota?</p>
</div>

<div id="dialog-excluir-end" title="Excluir Endereço">
	<p>Confirma a exclusão desse endereço?</p>
</div>

<div id="dialog-excluir-tel" title="Excluir Telefone">
	<p>Confirma a exclusão desse telefone?</p>
</div>

<div id="dialog-cnpj" title="Nova Cota">

  <div id="tabpj">
            <ul>
                <li><a href="#tabpj-1">Dados Cadastrais</a></li>
                <li><a href="#tabpj-2">Endereços</a></li>
                <li><a href="#tabpj-3">Telefones</a></li>
                <li><a href="#tabpj-4">PDV</a></li>
                <li><a href="#tabpj-5">Desconto</a></li>
                <li><a href="#tabpj-financeiro">Financeiro</a></li>
                <li><a href="#tabpj-7">Garantia</a></li>
                <li><a href="#tabpj-8">Distribuição</a></li>
                <li><a href="#tabpj-9">Sócios</a></li>
          </ul>
        
        
        <div id="tabpj-1">
        <table width="765" cellpadding="2" cellspacing="2" style="text-align:left;">
    <tr>
      <td colspan="4"><table width="755" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="119"><strong>Cota:</strong></td>
          <td width="143"><input type="text" style="width:100px" /></td>
          <td width="37"><strong>Ativo:</strong></td>
          <td width="20"><input name="radio" type="radio" id="radio3" value="radio" checked="checked" /></td>
          <td width="45">Sim</td>
          <td width="20"><input type="radio" name="radio" id="radio4" value="radio" /></td>
          <td width="76">Não</td>
          <td width="125"><strong>Início de Atividade:</strong></td>
          <td width="170">13/04/1998</td>
        </tr>
      </table></td>
      </tr>
    <tr>
      <td>Razão Social:</td>
      <td><input type="text" style="width:230px " /></td>
      <td>Nome Fantasia:</td>
      <td><input type="text" style="width:230px " /></td>
    </tr>
    <tr>
      <td>CNPJ:</td>
      <td><input type="text" style="width:150px" /></td>
      <td>Inscrição Estadual:</td>
      <td><input type="text" style="width:230px" /></td>
    </tr>
    <tr>
        <td width="116">Inscrição Municipal:</td>
        <td width="259"><input type="text" style="width:150px" /></td>
        <td width="125">E-mail:</td>
        <td width="237"><input type="text" style="width:230px" /></td>
    </tr>
    <tr>
      <td>E-mail NF-e:</td>
      <td><input type="text" style="width:230px " /></td>
      <td>Status:</td>
      <td><select name="select" id="select5" style="width:178px">
        <option> </option>
        <option>Ativo</option>
        <option>Pendente</option>
        <option>Aprovado</option>
        <option>Suspenso</option>
        <option>Inativo</option>
      </select></td>
    </tr>
     </table>
     <br />

     <b>Cota Base</b><br />
        
        <table width="880" border="0" cellspacing="2" cellpadding="2">
          <tr>
            <td width="123">Utilizar Histórico:</td>
            <td width="119"><select name="select" id="select" style="width:100px;" onchange="opcaoHistCotaPj(this.value);">
              <option value=""> </option>
              <option value="2">Própria Cota</option>
              <option value="1">Outra Cota</option>
              </select></td>
            <td width="193"><div id="histCotaPj" style="display:none;">
              <input type="text" name="textfield9" id="textfield9" style="width:80px; float:left;margin-right:5px;" />
              <span class="classPesquisar" title="Pesquisar Cota"><a href="javascript:;" onclick="mostraNome();">&nbsp;</a></span></div></td>
            <td width="55">Período:</td>
            <td width="120"><input type="text" name="cotaPjDe" id="cotaPjDe" style="width:80px;" /></td>
            <td width="22" align="center">Até</td>
            <td width="129"><input type="text" name="cotaPjAte" id="cotaPjAte" style="width:80px;" /></td>
            <td width="13">%</td>
            <td width="50"><input type="text" style="width:50px;" /></td>
          </tr>
          <tr>
            <td width="123">Classificação:</td>
            <td width="119">
            <select name="" style="width:100px;">
              <option> </option>
              <option>A</option>
              <option>B</option>
              <option>C</option>
              <option>D</option>
             </select>
           </td>
            <td colspan="3"><div class="nomeCotaSel" style="display:none;"><strong>Nome:</strong> Antonio José da Silva</div></td>
            <td width="22" align="center">&nbsp;</td>
            <td width="129">&nbsp;</td>
            <td width="13">&nbsp;</td>
            <td width="50">&nbsp;</td>
          </tr>
          </table>

		</div>
        
        
        
        
        
        
        
        <div id="tabpj-2">
        	<table width="754" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				<td width="103">Tipo Endereço:</td>
				<td width="252"><select  style="width:157px"><option>Comercial</option><option>Local de Entrega</option><option>Residencial</option><option>Cobrança</option></select></td>
				<td width="95">CEP:</td>
				<td width="276"><input type="text" style="float:left; margin-right:5px;" /><span class="classPesquisar" title="Pesquisar Cep."><a href="javascript:;">&nbsp;</a></span></td>
			</tr>
			<tr>
				<td>Tipo :</td>
				<td><select name="select"  style="width:80px">
				  <option>Av.</option>
				  <option>Rua</option>
				  <option>Travessa</option>
			  </select></td>
				<td>Logradouro:</td>
				<td><input type="text" style="width:250px" /></td>
			</tr>
			<tr>
				<td>N&uacute;mero:</td>
				<td><input type="text" style="width:50px" /></td>
				<td>Complemento:</td>
				<td><input type="text" style="width:250px" /></td>
			</tr>
			<tr>
				<td>Bairro:</td>
				<td><input type="text"  style="width:230px" /></td>
				<td>Cidade:</td>
				<td><input type="text" style="width:250px" /></td>
			</tr>
			<tr>
			  <td>UF:</td>
			  <td><input type="text" style="width:50px" /></td>
			  <td>Principal:</td>
			  <td><input type="checkbox" name="checkbox" id="checkbox" /></td>
  </tr>
			<tr>
			  <td>&nbsp;</td>
			  <td><span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span></td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
		  </tr>		
		</table>
        <br />
        <label><strong>Endereços Cadastrados</strong></label><br />

        <table class="enderecosGrid"></table>
        
    </div>
        <div id="tabpj-3">
        <table width="280" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				<td width="72">Tipo:</td>
				<td width="192">
                <select onchange="opcaoTel(this.value);" style="width:174px;">   
                  <option value="" selected="selected">Selecione</option>
                  <option value="1">Comercial</option>
				  <option value="">Celular</option>
				  <option value="1">Fax</option>
				  <option value="">Residencial</option>
				  <option value="2">Rádio</option>  
                </select>  
                <!--<select name="select" style="width:174px;" onchange="tel();">
				  <option selected="selected"> </option>
				  
              </select>--></td>
			</tr>
			<tr>
			  <td>Telefone: </td>
			  <td><input type="text" style="width:40px" />
			    -
		      <input type="text" style="width:110px" /></td>
  </tr>
			<tr>
			  <td colspan="2">
              	<div id="divOpcaoPj1" style="display:none;">   
                  <div style="width:80px; float:left;">Ramal:</div>
                  <input type="text" style="width:40px; float:left;" />
                </div>   
                <div id="divOpcaoPj2" style="display:none;">   
                  <div style="width:80px; float:left;;">ID:</div>
                  <input type="text" style="width:167px; float:left;" /> 
                </div>   
              
              
              </td>
			  </tr>
			<tr>
			  <td><label for="principal1">Principal:</label></td>
			  <td class="complementar">
			    <input type="checkbox" name="principal1" id="principal1" />
		      </td>
  </tr>
			<tr>
			  <td>&nbsp;</td>
			  <td ><span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span></td>
  </tr>
	  </table>
      <br />
        <label><strong>Telefones Cadastrados</strong></label><br />

        <table class="telefonesGrid"></table>
        
        
    </div>
    
    <div id="tabpj-4">
    
      <label><strong>PDVs Cadastrados</strong></label>
      <br />

        <table class="PDVsGrid"></table>
        <br />
        <span class="bt_novo"><a href="javascript:;" onclick="popup_novoPdv();">Novo</a></span>
        <br clear="all" />
    </div>
    
    <div id="tabpj-5">
    <table width="597" border="0" align="center" cellpadding="2" cellspacing="2">
  <tr class="especialidades">
    <td width="278" valign="top"><fieldset>
      <legend>Tipos de Desconto</legend>
      <select name="select2" size="10" multiple="multiple" id="select11" style="height:270px; width:245px;">
        <option>051 - TESTE</option>
      </select>
    </fieldset></td>
    <td width="39" align="center"><img src="../images/seta_vai_todos.png" width="39" height="30" /><br />
      <br />
      <img src="../images/seta_volta_todos.png" width="39" height="30" /> <br /></td>
    <td width="285" valign="top"><fieldset>
      <legend>Desconto Cota</legend>
      <select name="select2" size="10" multiple="multiple" id="select12" style="height:270px; width:245px;">
        <option>001 - Normal</option>
        <option>002 - Produtos Tributados</option>
        <option>003 - Vídeo Print de 1/1/96 A</option>
        <option>004 - Cromos - Normal Exc Ju</option>
        <option>005 - Importadas - Eletrolibe</option>
        <option>006 - Promoções</option>
        <option>007 - Especial Globo</option>
        <option>008 - Magali Fome Zero</option>
        <option>009 - Impratadas Mag</option>
        <option>011 - Importadas MagExpress</option>
      </select>
      <br />
      <br clear="all" />
      <strong>% Desc:</strong> <input name="input" type="text" style="width:80px; text-align:center;" />

    </fieldset></td>
  </tr>
</table>
    </div>
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    <!--PESSOA JURIDICA - FINANCEIRO -->
    
    <div id="tabpj-financeiro">
    	
     <table width="671" border="0" cellspacing="2" cellpadding="2">
      <tr>
        <td width="171">Tipo de Pagamento:</td>
        <td width="486">
        <select onchange="opcaoPagtoPj(this.value);">   
            <option value="">Selecione</option>   
            <option value="1">Boleto</option>
            <option value="1">Boleto em Branco</option>
            <option value="2">Cheque</option>
            <option value="">Dinheiro</option>
            <option value="3">Depósito</option>
            <option value="2">Transferência Bancária</option>
            <option value="">Outros</option>   
        </select>   
        </td>
      </tr>
      <tr>
        <td colspan="2" nowrap="nowrap">
        <div id="divOpcaoPj8" style="display:none;">   
           
      		
  		<table width="417" border="0" cellpadding="2" cellspacing="2">
      	<tr>
      	  <td colspan="2"><b>Dados do Banco</b></td>
       	  </tr>
      	<tr>
      	  <td width="57">Nome:</td>
      	  <td width="346"><select name="select2" id="select3" style="width:160px;">
      	    <option selected="selected"> </option>
      	    <option>Banco Itaú</option>
      	    <option>Banco Real</option>
      	    <option>Bradesco</option>
      	    <option>Santander</option>
    	    </select></td>
      	  </tr>
      	<tr>
      	  <td align="right"><input type="checkbox" name="checkbox" id="checkbox" /></td>
      	  <td>Receber por E-mail?</td>
  </tr>
  </table>
        </div>   
        <div id="divOpcaoPj9" style="display:none;">   
           
      	 	<table width="558" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td colspan="4"><strong>Dados Bancários - Cota:</strong></td>
    </tr>
  <tr>
    <td width="88">Num. Banco:</td>
    <td width="120"><input type="text" name="textfield3" id="textfield3" style="width:60px;"/></td>
    <td width="47">Nome:</td>
    <td width="277"><input type="text" name="textfield6" id="textfield7" style="width:150px;"/></td>
  </tr>
  <tr>
    <td>Agência:</td>
    <td><input type="text" name="textfield2" id="textfield2" style="width:60px;"/>
      -
      <input type="text" name="textfield4" id="textfield4" style="width:30px;"/></td>
    <td>Conta:</td>
    <td><input type="text" name="textfield5" id="textfield5" style="width:60px;"/>
      -
      <input type="text" name="textfield5" id="textfield6" style="width:30px;"/></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  </table>
        </div>
        <div id="divOpcaoPj10" style="display:none;">   
           
      		
  		<table width="417" border="0" cellpadding="2" cellspacing="2">
      	<tr>
      	  <td colspan="2"><b>Dados do Banco</b></td>
       	  </tr>
      	<tr>
      	  <td width="57">Nome:</td>
      	  <td width="346"><select name="select2" id="select3" style="width:160px;">
      	    <option selected="selected"> </option>
      	    <option>Banco Itaú</option>
      	    <option>Banco Real</option>
      	    <option>Bradesco</option>
      	    <option>Santander</option>
    	    </select></td>
      	  </tr>
       </table>
        </div>   
		
        
        
        
        
        </td>
      </tr>
      <tr>
        <td>Fator Vencimento em D+:</td>
        <td><select name="select18" size="1" multiple="multiple" id="select19" style="width:50px; height:19px;">
          <option>1</option>
          <option>2</option>
          <option>3</option>

          <option>4</option>
          <option>5</option>
          <option>6</option>
          <option>7</option>
          <option>8</option>
          <option>9</option>
          <option>10</option>
          <option>11</option>
          <option>12</option>
          <option>13</option>
          <option>14</option>
          <option>15</option>
          <option>16</option>
          <option>17</option>
          <option>18</option>
          <option>19</option>
          <option>20</option>
        </select></td>
      </tr>
      <tr>
        <td>Sugere Suspensão:</td>
        <td><input name="" type="checkbox" value="" checked /></td>
      </tr>
      
      <tr>
        <td>Contrato:</td>
        <td><input type="checkbox" name="checkbox9" id="checkbox2" style="float:left;" onclick="imprimir_contrato();" /> <span class="bt_imprimir"><a href="javascript:;">Contrato</a></span></td>
      </tr>
      <tr>
        <td>Concentração Pagamento:</td>
        <td><input type="checkbox" name="checkbox" id="PS" />
            <label for="PS">S</label>
            <input type="checkbox" name="checkbox2" id="PT" />
            <label for="PT">T</label>
            <input type="checkbox" name="checkbox3" id="PQ" />
            <label for="PQ">Q</label>
            <input type="checkbox" name="checkbox4" id="PQu" />
            <label for="PQu">Q</label>
            <input type="checkbox" name="checkbox5" id="PSex" />
            <label for="PSex">S</label>
            <input type="checkbox" name="checkbox6" id="PSab" />
            <label for="PSab">S</label>
            <input type="checkbox" name="checkbox7" id="PDom" />
            <label for="PDom">D</label></td>
      </tr>
      <tr>
        <td>Valor Mínimo R$:</td>
        <td><input type="text" name="textfield12" id="textfield12" style="width:60px;"/></td>
      </tr>
      <tr>
        <td>Comissão %:</td>
        <td><input type="text" name="textfield" id="textfield" style="width:60px;"/></td>
      </tr>
      <tr>
        <td height="23">Sugere Suspensão quando:</td>
        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="36%">Qtde de dividas em aberto:</td>
            <td width="15%"><input type="text" name="textfield9" id="textfield10" style="width:60px;"/></td>
            <td width="6%">ou</td>
            <td width="7%">R$: </td>
            <td width="36%"><input type="text" name="textfield9" id="textfield11" style="width:60px;"/></td>
          </tr>
        </table></td>
  </tr>
</table>
        <br clear="all" />

    </div>
    
    
<!-- /PESSOA JURIDICA - FINANCEIRO -->
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    <div id="tabpj-7">
    
    <table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
          <tr>
            <td width="129">Tipo de Garantia:</td>
            <td width="610"><select name="" style="width:250px;" onchange="opcaoTipoGarantia(this.value);">
              <option value="" selected="selected">Selecione...</option>
              <option value="1">Fiador</option>
              <option value="2">Cheque Caução</option>
              <option value="3">Nota Promissória</option>
              <option value="4">Imóvel</option>
              <option value="5">Caução Liquida</option>
            </select></td>
          </tr>
     </table>
     
     <div id="garantia_fiador" style="display:none;">
     <fieldset>
   	   <legend>Fiador</legend>
             <table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
              <tr>
                <td width="150"><strong>Nome / Razão Social:</strong></td>
                <td width="212"><input type="text" name="nomes" id="nomes" style="width:200px;" onkeypress="mostra_dados_fiador();" /></td>
                <td width="92"><strong>CPF/CNPJ:</strong></td>
                <td width="273"><input type="text" name="textfield2" id="textfield2" style="width:200px;" /></td>
              </tr>
            </table>
   	</fieldset>
    <br clear="all" />
    <br />

	<fieldset class="dadosFiador" style="display:none;">
      <legend>Dados do Fiador</legend>
             <table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
              <tr>
                <td width="154"><strong>Nome / Razão Social:</strong></td>
                <td width="272">Antonio José da Silva</td>
                <td width="87"><strong>CPF/CNPJ:</strong></td>
                <td width="214">999.999.999-00</td>
              </tr>
              <tr>
                <td><strong>Endereço:</strong></td>
                <td colspan="3">Rua Antonio Batista de Souza Pereira, 455 - Jd. Moraes Moreira - São Paulo/SP</td>
               </tr>
              <tr>
                <td><strong>Telefone:</strong></td>
                <td>11 9999.9999</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
            </table>
   	</fieldset>
    
    <br clear="all" />
    <br />

	<fieldset class="dadosFiador" style="display:none;">
      <legend>Garantias Cadastradas</legend>
      <table class="garantiasGrid"></table>
   	</fieldset>
     </div>
     
     
     
     <div id="garantia_cheque_caucao" style="display:none;">
   	   <fieldset>
       	 <legend>Dados Bancários</legend>
         <table width="601" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="138">Num. Banco:</td>
    <td width="122"><input type="text" name="textfield3" id="textfield3" style="width:60px;"/></td>
    <td width="82">Nome:</td>
    <td width="233"><input type="text" name="textfield6" id="textfield7" style="width:200px;"/></td>
  </tr>
  <tr>
    <td>Agência:</td>
    <td><input type="text" name="textfield2" id="textfield2" style="width:60px;"/>
      -
      <input type="text" name="textfield4" id="textfield4" style="width:30px;"/></td>
    <td>Conta:</td>
    <td><input type="text" name="textfield5" id="textfield5" style="width:60px;"/>
      -
      <input type="text" name="textfield5" id="textfield6" style="width:30px;"/></td>
  </tr>
  <tr>
    <td>N° Cheque:</td>
    <td><input type="text" name="textfield" id="textfield" style="width:107px;"/></td>
    <td>Valor R$:</td>
    <td><input type="text" name="textfield7" id="textfield8" style="width:108px; text-align:right;"/></td>
  </tr>
  <tr>
    <td>Nome Correntista:</td>
    <td colspan="3"><input type="text" name="textfield8" id="textfield9" style="width:345px;"/></td>
    </tr>
  <tr>
    <td>Imagem Cheque:</td>
    <td colspan="3"><input name="fileField" type="file" id="fileField" size="58" /></td>
  </tr>
         </table>
  </fieldset>
  <br clear="all" />
  <br />
  <fieldset>
  	<legend>Foto Cheque</legend>
    <br />
    <div align="center">
    	<img src="../images/cheque.jpg" />
    </div>
  </fieldset>


     </div>
     
     
     <div id="garantia_nota_promissoria" style="display:none;">
   	   <fieldset>
       	 <legend>Nota Promissória</legend>
            <table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
              <tr>
                <td width="128">N° Nota Promissória:</td>
                <td width="219"><input type="text" name="textfield8" id="textfield9" style="width:120px;" /></td>
                <td width="78">Vencimento:</td>
                <td width="302"><input type="text" name="textfield9" id="textfield11" style="width:120px;" /></td>
              </tr>
              <tr>
                <td>Valor R$</td>
                <td><input type="text" name="textfield11" id="textfield12" style="width:120px; text-align:right;" /></td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>por extenso:</td>
                <td colspan="3"><input type="text" name="textfield10" id="textfield10" style="width:425px;" /></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td colspan="3">
                	<span class="bt_novos" title="Imprimir"><a href="../nota_promissoria.htm" target="_blank"><img src="../images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
                </td>
              </tr>
           </table>
      </fieldset>
     </div>
     
     
     <div id="garantia_imovel" style="display:none;">
   	   <fieldset>
       	 <legend>Imóvel</legend>
         <table width="755" border="0" cellpadding="2" cellspacing="2" style="text-align:left;">
          <tr>
            <td width="106">Proprietário:</td>
            <td width="635"><input type="text" name="textfield10" id="textfield10" style="width:250px;" /></td>
          </tr>
          <tr>
            <td>Endereço:</td>
            <td><input type="text" name="textfield12" id="textfield13" style="width:450px;" /></td>
          </tr>
          <tr>
            <td>Número Registro:</td>
            <td><input type="text" name="textfield13" id="textfield14" style="width:250px;" /></td>
          </tr>
          <tr>
            <td>Valor R$:</td>
            <td><input type="text" name="textfield14" id="textfield15" style="width:100px; text-align:right;" /></td>
          </tr>
          <tr>
            <td>Observação:</td>
            <td><textarea name="textfield15" rows="3" id="textfield16" style="width:450px;"></textarea></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td><span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span></td>
          </tr>
     	 </table>
        </fieldset>
        <br clear="all" />
        <br />

        <fieldset>
          <legend>Imóveis Cadastradas</legend>
          <table class="imoveisGarantiaGrid"></table>
        </fieldset>
     </div>
     
     
     <div id="garantia_caucao_liquida" style="display:none;">
     	<fieldset>
        	<legend>Caução Líquida</legend>
             <table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
              <tr>
                <td width="94">Valor Inicial R$:</td>
                <td width="151"><input type="text" name="textfield17" id="textfield17" style="width:150px; text-align:right;" /></td>
                <td width="488"><a href="javascript:;"><img src="../images/ico_check.gif" alt="Incluir" width="16" height="16" border="0" /></a></td>
              </tr>
            </table>
        </fieldset>
        <br clear="all" />
        <br />
        <fieldset>
        	<legend>Caução Líquida</legend>
            <table class="caucaoGrid"></table>
         </fieldset>


     </div>

    <br clear="all" />

    
    </div>
    
    
    
    <div id="tabpj-8" class="distribuicao">
  	<table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
          <tr>
            <td> Qtde. Cota:</td>
            <td><input type="text" style="width:100px" /></td>
            <td>Qtde. PDV:</td>
            <td><input type="text" style="width:100px" /></td>
          </tr>
          <tr>
            <td>Box:</td>
            <td><input type="text" style="width:100px" /></td>
            <td>Tipo de Ponto:</td>
            <td><select name="select3" id="select2" style="width:155px">
              <option selected="selected"> </option>
              <option>Banca</option>
              <option>Supermercado</option>
            </select></td>
          </tr>
          <tr>
            <td width="120">Assist. Comercial:</td>
            <td width="320"><input type="text" style="width:150px" /></td>
            <td width="116">Tipo de Entrega:</td>
            <td width="171"><select name="select4" id="select4" style="width:155px">
              <option selected="selected"> </option>
              <option>Cota Retira</option>
              <option>Entrega em Banca</option>
              <option>Entregador</option>
            </select></td>
          </tr>
          <tr>
            <td>Arrendatário:</td>
            <td><input type="checkbox" name="arrendaPj2" id="arrendaPj2" /></td>
            <td>Tipo de Cota:</td>
            <td><select name="select2" id="select" style="width:155px">
              <option selected="selected"> </option>
              <option>Convencional</option>
              <option>Alternativo</option>
            </select></td>
          </tr>
          <tr>
            <td valign="top">Observação:</td>
            <td colspan="3"><textarea name="textarea" rows="4" style="width:605px"></textarea></td>
          </tr>
  </table>
  
  <table width="755" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="377" valign="top">
    <input name="cposterior" type="checkbox" value="" id="cposterior" /><label for="cposterior">Credito Posterior de Encalhe</label>
<br clear="all" />

          
        <input name="notaDev" type="checkbox" value="" id="notaDev" /><label for="notaDev">Nota Devolução Antecipada</label>
        <br clear="all" />
          
        <input name="repPtoVnda" type="checkbox" value="" id="repPtoVnda" /><label for="repPtoVnda">Reparte por Ponto de Venda</label>
          <br clear="all" />

        <input name="solNumAtrs" type="checkbox" value="" id="solNumAtrs" /><label for="solNumAtrs">Solicitação Num. Atrasados - Internet</label>
     
     
    <br />
    </td>
    <td width="378" valign="top"><table width="373" border="0" cellspacing="2" cellpadding="2">
      <tr class="header_table">
        <td width="142" align="left">Documentos</td>
        <td width="105" align="center">Impresso</td>
        <td width="106" align="center">E-mail</td>
      </tr>
      <tr class="class_linha_1">
        <td>Nota de Envio</td>
        <td align="center"><input type="checkbox" name="checkbox2" id="checkbox2" /></td>
        <td align="center"><input type="checkbox" name="checkbox5" id="checkbox5" /></td>
      </tr>
      <tr class="class_linha_2">
        <td>Chamada de Encalhe</td>
        <td align="center"><input type="checkbox" name="checkbox3" id="checkbox3" /></td>
        <td align="center"><input type="checkbox" name="checkbox6" id="checkbox6" /></td>
      </tr>
      <tr class="class_linha_1">
        <td>Slip</td>
        <td align="center"><input type="checkbox" name="checkbox4" id="checkbox4" /></td>
        <td align="center"><input type="checkbox" name="checkbox" id="checkbox" /></td>
      </tr>
    </table></td>
  </tr>
</table>




</div>
      
      <div id="tabpj-9">
      	<table width="300" border="0" cellspacing="2" cellpadding="2">
          <tr>
            <td>Nome:</td>
            <td><input name="" type="text" /></td>
          </tr>
          <tr>
            <td>Cargo:</td>
            <td><input name="" type="text" /></td>
          </tr>
          <tr>
            <td><label for="ePrincipal">Principal:</label></td>
            <td><input name="ePrincipal" type="checkbox" value="" id="ePrincipal" /></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td><span class="bt_add"><a href="javascript:;">Incluir Novo</a></span></td>
          </tr>
        </table>
        <br />
        <label><strong>Sócios Cadastrados</strong></label><br />

        <table class="sociosPjGrid"></table>

        
        
        <br />
        
  </div>
</div>
    
</div>












<div id="dialog-cpf" title="Nova Cota">


  <div id="tabpf">
            <ul>
                <li><a href="#tabpf-1">Dados Cadastrais</a></li>
                <li><a href="#tabpf-2">Endereços</a></li>
                <li><a href="#tabpf-3">Telefones</a></li>
                <li><a href="#tabpf-4">PDV</a></li>
                <li><a href="#tabpf-5">Desconto</a></li>
                <li><a href="#tabpf-financeiro">Financeiro</a></li>
                <li><a href="#tabpf-7">Garantia</a></li>
                <li><a href="#tabpf-8">Distribuição</a></li>
          </ul>
        
        
        <div id="tabpf-1">
        <table width="765" cellpadding="2" cellspacing="2" style="text-align:left;">
          <tr>
            <td colspan="6"><table width="755" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="114"><strong>Cota:</strong></td>
                <td width="146"><input type="text" style="width:100px" /></td>
                <td width="41"><strong>Ativo:</strong></td>
                <td width="20"><input name="radio" type="radio" id="radio" value="radio" checked="checked" /></td>
                <td width="44">Sim</td>
                <td width="20"><input type="radio" name="radio" id="radio2" value="radio" /></td>
                <td width="69">Não</td>
                <td width="130"><strong>Início de Atividade:</strong></td>
                <td width="171">13/04/1998</td>
                
              </tr>
            </table></td>
          </tr>
          <tr>
            <td width="110">Nome:</td>
            <td width="278"><input type="text" style="width:230px " /></td>
            <td width="124">E-mail:</td>
            <td colspan="3"><input type="text" style="width:230px" /></td>
          </tr>
          <tr>
            <td>CPF:</td>
            <td><input type="text" style="width:150px" /></td>
            <td>R. G.:</td>
            <td colspan="3"><input type="text" style="width:175px" /></td>
          </tr>
          <tr>
            <td>Data Nascimento:</td>
            <td><input type="text" style="width:150px" /></td>
            <td>Orgão Emissor:</td>
            <td width="64"><input type="text" style="width:50px" /></td>
            <td width="19">UF:</td>
            <td width="135"><select name="select6" id="select6" style="width:50px">
              <option selected="selected"> </option>
              <option>RJ</option>
              <option>SP</option>
            </select></td>
          </tr>
          <tr>
            <td>Estado Civil:</td>
            <td><select name="" style="width:155px;">
            <option selected="selected">Selecione...</option>
            <option>Solteiro</option>
            <option>Casado</option>
            <option>Divorciado</option>
            <option>Víuvo</option>
          </select></td>
            <td>Sexo:</td>
            <td colspan="3"><select name="select11" id="select12" style="width:178px">
              <option selected="selected">Selecione... </option>
              <option>Masculino</option>
              <option>Feminino</option>
            </select></td>
          </tr>
          <tr>
            <td>Nacionalidade:</td>
            <td><input type="text" style="width:150px" /></td>
            <td>Natural:</td>
            <td colspan="3"><input type="text" style="width:175px" /></td>
          </tr>
          <tr>
            <td>E-mail NF-e:</td>
            <td><input type="text" style="width:230px " /></td>
            <td>Status:</td>
            <td colspan="3"><select name="select5" id="select3" style="width:178px">
              <option> </option>
              <option>Ativo</option>
              <option>Pendente</option>
              <option>Aprovado</option>
              <option>Suspenso</option>
              <option>Inativo</option>
            </select></td>
          </tr>
</table>
        <br clear="all" />

        <b>Cota Base</b><br />
        
        <table width="880" border="0" cellspacing="2" cellpadding="2">
          <tr>
            <td width="123">Utilizar Histórico:</td>
            <td width="119"><select name="select" id="select" style="width:100px;" onchange="opcaoHistCotaPf(this.value);">
              <option value=""> </option>
              <option value="2">Própria Cota</option>
              <option value="1">Outra Cota</option>
              </select></td>
            <td width="193"><div id="histCotaPf" style="display:none;">
              <input type="text" name="textfield9" id="textfield9" style="width:80px; float:left;margin-right:5px;" />
              <span class="classPesquisar" title="Pesquisar Produto"><a href="javascript:;" onclick="mostraNome();">&nbsp;</a></span></div></td>
            <td width="55">Período:</td>
            <td width="120"><input type="text" name="cotaDe" id="cotaDe" style="width:80px;" /></td>
            <td width="22" align="center">Até</td>
            <td width="129"><input type="text" name="cotaAte" id="cotaAte" style="width:80px;" /></td>
            <td width="13">%</td>
            <td width="50"><input type="text" style="width:50px;" /></td>
          </tr>
          <tr>
            <td width="123">Classificação:</td>
            <td width="119">
            <select name="" style="width:100px;">
              <option> </option>
              <option>A</option>
              <option>B</option>
              <option>C</option>
              <option>D</option>
             </select>
           </td>
            <td colspan="3"><div class="nomeCotaSel" style="display:none;"><strong>Nome:</strong> Antonio José da Silva</div></td>
            <td width="22" align="center">&nbsp;</td>
            <td width="129">&nbsp;</td>
            <td width="13">&nbsp;</td>
            <td width="50">&nbsp;</td>
          </tr>
          </table>

  </div>
        <div id="tabpf-2">
        	<table width="754" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				<td width="103">Tipo Endereço:</td>
				<td width="252"><select  style="width:157px"><option>Comercial</option><option>Local de Entrega</option><option>Residencial</option><option>Cobrança</option></select></td>
				<td width="95">CEP:</td>
				<td width="276"><input type="text" style="float:left; margin-right:5px;" /><span class="classPesquisar" title="Pesquisar Cep."><a href="javascript:;">&nbsp;</a></span></td>
			</tr>
			<tr>
				<td>Tipo :</td>
				<td><select name="select"  style="width:80px">
				  <option>Av.</option>
				  <option>Rua</option>
				  <option>Travessa</option>
			  </select></td>
				<td>Logradouro:</td>
				<td><input type="text" style="width:250px" /></td>
			</tr>
			<tr>
				<td>N&uacute;mero:</td>
				<td><input type="text" style="width:50px" /></td>
				<td>Complemento:</td>
				<td><input type="text" style="width:250px" /></td>
			</tr>
			<tr>
				<td>Bairro:</td>
				<td><input type="text"  style="width:230px" /></td>
				<td>Cidade:</td>
				<td><input type="text" style="width:250px" /></td>
			</tr>
			<tr>
			  <td>UF:</td>
			  <td><input type="text" style="width:50px" /></td>
			  <td>Principal:</td>
			  <td><input type="checkbox" name="checkbox" id="checkbox" /></td>
  </tr>
			<tr>
			  <td>&nbsp;</td>
			  <td><span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span></td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
		  </tr>		
		</table>
        <br />
        <label><strong>Endereços Cadastrados</strong></label><br />

        <table class="enderecosGrid"></table>
        
    </div>
        <div id="tabpf-3">
        	<table width="280" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				<td width="72">Tipo:</td>
				<td width="192">
                <select onchange="opcaoTelPf(this.value);" style="width:174px;">   
                  <option value="" selected="selected">Selecione</option>
                  <option value="1">Comercial</option>
				  <option value="">Celular</option>
				  <option value="1">Fax</option>
				  <option value="">Residencial</option>
				  <option value="2">Rádio</option>  
                </select>  
                <!--<select name="select" style="width:174px;" onchange="tel();">
				  <option selected="selected"> </option>
				  
              </select>--></td>
			</tr>
			<tr>
			  <td>Telefone: </td>
			  <td><input type="text" style="width:40px" />
			    -
		      <input type="text" style="width:110px" /></td>
  </tr>
			<tr>
			  <td colspan="2">
              	<div id="divOpcaoPf3" style="display:none;">   
                   <div style="width:80px; float:left;">Ramal:</div>
                   <input type="text" style="width:40px; float:left;" />
                </div>   
                <div id="divOpcaoPf4" style="display:none;">   
                    <div style="width:80px; float:left;;">ID:</div>
                   <input type="text" style="width:167px; float:left;" /> 
                </div>   
              
              
              </td>
			  </tr>
			<tr>
			  <td><label for="principal1">Principal:</label></td>
			  <td class="complementar">
			    <input type="checkbox" name="principal1" id="principal1" />
		      </td>
  </tr>
			<tr>
			  <td>&nbsp;</td>
			  <td ><span class="bt_add"><a href="javascript:;">Incluir Novo</a></span></td>
  </tr>
	  </table>
      <br />
        <label><strong>Telefones Cadastrados</strong></label><br />

        <table class="telefonesGrid"></table>
    </div>
    
    <div id="tabpf-4">
    
      <label><strong>PDVs Cadastrados</strong></label>
      <br />

        <table class="PDVsGrid"></table>
        <br />
        <span class="bt_novo"><a href="javascript:;" onclick="popup_novoPdv();">Novo</a></span>
        <br clear="all" />
    </div>
    
    
    <div id="tabpf-5">
    <table width="597" border="0" align="center" cellpadding="2" cellspacing="2">
  <tr class="especialidades">
    <td width="278" valign="top"><fieldset>
      <legend>Tipos de Desconto</legend>
      <select name="select2" size="10" multiple="multiple" id="select11" style="height:270px; width:245px;">
        <option>051 - TESTE</option>
      </select>
    </fieldset></td>
    <td width="39" align="center"><img src="../images/seta_vai_todos.png" width="39" height="30" /><br />
      <br />
      <img src="../images/seta_volta_todos.png" width="39" height="30" /> <br /></td>
    <td width="285" valign="top"><fieldset>
      <legend>Desconto Cota</legend>
      <select name="select2" size="10" multiple="multiple" id="select12" style="height:270px; width:245px;">
        <option>001 - Normal</option>
        <option>002 - Produtos Tributados</option>
        <option>003 - Vídeo Print de 1/1/96 A</option>
        <option>004 - Cromos - Normal Exc Ju</option>
        <option>005 - Importadas - Eletrolibe</option>
        <option>006 - Promoções</option>
        <option>007 - Especial Globo</option>
        <option>008 - Magali Fome Zero</option>
        <option>009 - Impratadas Mag</option>
        <option>011 - Importadas MagExpress</option>
      </select>
      <br />
      <br clear="all" />
      <strong>% Desc:</strong> <input name="input" type="text" style="width:80px; text-align:center;" />

    </fieldset></td>
  </tr>
</table>
</div>
    
    
    
    
    
    

    
    
    
    
    
    <!--PESSOA FISICA - FINANCEIRO -->
    
    <form name="formFinanceiroPF" id="formFinanceiroPF"> 
    
	    <div id="tabpf-financeiro" >
	    
		   <table width="671" border="0" cellspacing="2" cellpadding="2">
		      
		      <tr>
		        <td width="171">Tipo de Pagamento:</td>
		        <td width="486">

			        <select name="tabpf-financeiro-tipoCobranca" id="tabpf-financeiro-tipoCobranca" style="width:150px;" onchange="opcaoPagtoPf(this.value);buscaBancos(this.value);">
                        <option value="">Selecione</option>
                        <c:forEach varStatus="counter" var="tipoCobranca" items="${listaTiposCobranca}">
		                    <option value="${tipoCobranca.key}">${tipoCobranca.value}</option>
		                </c:forEach>
                    </select> 

		        </td>
		      </tr>
		      
		      
		      <tr>
		      
		        <td colspan="2" nowrap="nowrap">
		        
		        
		        
		            <div id="tabpf-financeiro-divComboBanco" style="display:none;">   
		            
		                <table width="417" border="0" cellpadding="2" cellspacing="2">
		                    <tr>
					      	  <td colspan="2"><b>Dados do Banco</b></td>
					       	</tr>
					       	  
					      	<tr>
					      	  <td width="57">Nome:</td>
					      	  <td width="346">
					    	    
					    	    <select name="tabpf-financeiro-banco" id="tabpf-financeiro-banco" style="width:150px;">
		                            <c:forEach varStatus="counter" var="banco" items="${listaBancos}">
						                <option value="${banco.key}">${banco.value}</option>
						            </c:forEach>
		                        </select>
					    	 
					      	</tr>
		                 </table>
				        
			        </div>    
			        
		        
		        
			        <div id="tabpf-financeiro-divBoleto" style="display:none;">   
	
				  		<table width="417" border="0" cellpadding="2" cellspacing="2">
				  		
					      	<tr>
					      	  <td align="right">
					      	      <input type="checkbox" id="tabpf-financeiro-recebeEmail" name="tabpf-financeiro-recebeEmail" /></td>
					      	  <td>Receber por E-mail?</td>
					        </tr>
					        
				        </table>
				        
			        </div>   
			        
	
	
					<div id="tabpf-financeiro-divDeposito" style="display:none;">   
			            
			      	 	<table width="558" border="0" cellspacing="2" cellpadding="2">
							  
							  <tr>
							    <td colspan="4"><strong>Dados Bancários - Cota:</strong></td>
							  </tr>
							  
							  <tr>
							    <td width="88">Num. Banco:</td>
							    <td width="120"><input type="text" id="tabpf-financeiro-numBanco" name="tabpf-financeiro-numBanco" style="width:60px;"/></td>
							    <td width="47">Nome:</td>
							    <td width="277"><input type="text" id="tabpf-financeiro-nomeBanco" name="tabpf-financeiro-nomeBanco" style="width:150px;"/></td>
							  </tr>
							  
							  <tr>
							    <td>Agência:</td>
							    <td><input type="text" id="tabpf-financeiro-agencia" name="tabpf-financeiro-agencia" style="width:60px;"/>
							      -
							      <input type="text" id="tabpf-financeiro-agenciaDigito" name="tabpf-financeiro-agenciaDigito" style="width:30px;"/></td>
							    <td>Conta:</td>
							    <td>
							        <input type="text" id="tabpf-financeiro-conta" name="tabpf-financeiro-conta" style="width:60px;"/>
							      -
							        <input type="text" id="tabpf-financeiro-contaDigito" name="tabpf-financeiro-contaDigito" style="width:30px;"/></td>
							  </tr>
							  
							  <tr>
							    <td>&nbsp;</td>
							    <td>&nbsp;</td>
							    <td>&nbsp;</td>
							    <td>&nbsp;</td>
							  </tr>
							  
						 </table>
						 
			        </div>
			        
			        
			        
		
		        </td>
		        
		      </tr>
		      
		      
		      <tr>
		       
		        <td>Fator Vencimento em D+:</td>
		       
		        <td>
			        <select id="tabpf-financeiro-fatorVencimento" name="tabpf-financeiro-fatorVencimento" size="1" multiple="multiple" style="width:50px; height:19px;">
			          <option>1</option>
			          <option>2</option>
			          <option>3</option>
			          <option>4</option>
			          <option>5</option>
			          <option>6</option>
			          <option>7</option>
			          <option>8</option>
			          <option>9</option>
			          <option>10</option>
			          <option>11</option>
			          <option>12</option>
			          <option>13</option>
			          <option>14</option>
			          <option>15</option>
			          <option>16</option>
			          <option>17</option>
			          <option>18</option>
			          <option>19</option>
			          <option>20</option>
			        </select>
		        </td>
		        
		      </tr>
		      
		      <tr>
		        <td>Sugere Suspensão:</td>
		        <td><input id="tabpf_financeiro_sugereSuspensao" name="tabpf_financeiro_sugereSuspensao" type="checkbox" value="" checked /></td>
		      </tr>
		      
		      <tr>
		        <td>Contrato:</td>
		        <td><input id="tabpf_financeiro_contrato" name="tabpf_financeiro_contrato" type="checkbox" style="float:left;" onclick="imprimir_contrato();" /> <span class="bt_imprimir"><a href="javascript:;">Contrato</a></span></td>
		      </tr>
		      
		      <tr>
		        <td>Concentração Pagamento:</td>
		        <td>
		            <input type="checkbox" name="tabpf_financeiro_PS" id="tabpf_financeiro_PS" />
		            <label for="PS">S</label>
		            <input type="checkbox" name="tabpf_financeiro_PT" id="tabpf_financeiro_PT" />
		            <label for="PT">T</label>
		            <input type="checkbox" name="tabpf_financeiro_PQ" id="tabpf_financeiro_PQ" />
		            <label for="PQ">Q</label>
		            <input type="checkbox" name="tabpf_financeiro_PQu" id="tabpf_financeiro_PQu" />
		            <label for="PQu">Q</label>
		            <input type="checkbox" name="tabpf_financeiro_PSex" id="tabpf_financeiro_PSex" />
		            <label for="PSex">S</label>
		            <input type="checkbox" name="tabpf_financeiro_PSab" id="tabpf_financeiro_PSab" />
		            <label for="PSab">S</label>
		            <input type="checkbox" name="tabpf_financeiro_PDom" id="tabpf_financeiro_PDom" />
		            <label for="PDom">D</label>
		        </td>
		      </tr>
		      
		      <tr>
		        <td>Valor Mínimo R$:</td>
		        <td>
		            <input name="tabpf-financeiro-valorMinimo" id="tabpf-financeiro-valorMinimo" type="text" style="width:60px;"/>
		        </td>
		      </tr>
		      
		      <tr>
		        <td>Comissão %:</td>
		        <td>
		            <input name="tabpf-financeiro-comissao" id="tabpf-financeiro-comissao" type="text" style="width:60px;"/>
		        </td>
		      </tr>
		      
		      <tr>
		        <td height="23">Sugere Suspensão quando:</td>
		        <td>
			        <table width="100%" border="0" cellspacing="0" cellpadding="0">
			           <tr>
			              <td width="36%">Qtde de dividas em aberto:</td>
			            
			              <td width="15%">
			                  <input type="text" name="tabpf-financeiro-qtdDividasAberto" id="tabpf-financeiro-qtdDividasAberto" style="width:60px;"/>
			              </td>
			            
			              <td width="6%">ou</td>
			              <td width="7%">R$: </td>
			            
			              <td width="36%">
			                  <input type="text" name="tabpf-financeiro-vrDividasAberto" id="tabpf-financeiro-vrDividasAberto" style="width:60px;"/>
			              </td>
			            
			          </tr>
			         </table>
		         </td>
		      
		      </tr>
		  
		  </table>
	      <br clear="all" />
	    </div>
    
    </form>
  <!-- /PESSOA FISICA - FINANCEIRO -->  
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    <div id="tabpf-7">
    <table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
          <tr>
            <td width="108">Tipo de Garantia:</td>
            <td width="631"><select name="" style="width:250px;" onchange="opcaoTipoGarantiaPf(this.value);">
              <option value="" selected="selected">Selecione...</option>
              <option value="1">Fiador</option>
              <option value="2">Cheque Caução</option>
              <option value="3">Nota Promissória</option>
              <option value="4">Imóvel</option>
              <option value="5">Caução Liquida</option>
            </select></td>
          </tr>
  </table>
     
     <div id="garantia_fiador_pf" style="display:none;">
     <fieldset>
   	   <legend>Fiador</legend>
             <table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
              <tr>
                <td width="52"><strong>Nome:</strong></td>
                <td width="221"><input type="text" name="nomes_pf" id="nomes_pf" style="width:200px;" onkeypress="mostra_dados_fiador_pf();" /></td>
                <td width="83"><strong>CPF/CNPJ:</strong></td>
                <td width="371"><input type="text" name="textfield2" id="textfield2" style="width:200px;" /></td>
              </tr>
            </table>
   	</fieldset>
    <br clear="all" />
    <br />

	<fieldset class="dadosFiador_pf" style="display:none;">
      <legend>Dados do Fiador</legend>
             <table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
              <tr>
                <td width="86"><strong> Nome:</strong></td>
                <td width="340">Antonio José da Silva</td>
                <td width="44"><strong>CPF:</strong></td>
                <td width="257">999.999.999-00</td>
              </tr>
              <tr>
                <td><strong>Endereço:</strong></td>
                <td colspan="3">Rua Antonio Batista de Souza Pereira, 455 - Jd. Moraes Moreira - São Paulo/SP</td>
               </tr>
              <tr>
                <td><strong>Telefone:</strong></td>
                <td>11 9999.9999</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
            </table>
   	</fieldset>
    
    <br clear="all" />
    <br />

	<fieldset class="dadosFiador_pf" style="display:none;">
      <legend>Garantias Cadastradas</legend>
      <table class="garantiasGrid"></table>
   	</fieldset>
     </div>
     
     
     
     <div id="garantia_cheque_caucao_pf" style="display:none;">
   	   <fieldset>
       	 <legend>Dados Bancários</legend>
         <table width="630" border="0" cellspacing="2" cellpadding="2">
          <tr>
            <td width="139">Num. Banco:</td>
            <td width="166"><input type="text" name="textfield3" id="textfield3" style="width:60px;"/></td>
            <td width="67">Nome:</td>
            <td width="232"><input type="text" name="textfield6" id="textfield7" style="width:150px;"/></td>
          </tr>
          <tr>
            <td>Agência:</td>
            <td><input type="text" name="textfield2" id="textfield2" style="width:60px;"/>
              -
              <input type="text" name="textfield4" id="textfield4" style="width:30px;"/></td>
            <td>Conta:</td>
            <td><input type="text" name="textfield5" id="textfield5" style="width:60px;"/>
              -
              <input type="text" name="textfield5" id="textfield6" style="width:30px;"/></td>
          </tr>
          <tr>
            <td>N° Cheque:</td>
            <td><input type="text" name="textfield" id="textfield" style="width:107px;"/></td>
            <td>Valor R$:</td>
            <td><input type="text" name="textfield7" id="textfield8" style="width:108px; text-align:right;"/></td>
          </tr>
          <tr>
            <td>Nome Correntista:</td>
            <td colspan="3"><input type="text" name="textfield8" id="textfield9" style="width:332px;"/></td>
            </tr>
          <tr>
            <td>Imagem Cheque:</td>
            <td colspan="3"><input name="fileField" type="file" id="fileField" size="58" /></td>
          </tr>
         </table>
  </fieldset>
  <br clear="all" />
  <br />
  <fieldset>
  	<legend>Foto Cheque</legend>
    <br />
    <div align="center">
    	<img src="../images/cheque.jpg" />
    </div>
  </fieldset>


     </div>
     
     
     <div id="garantia_nota_promissoria_pf" style="display:none;">
   	   <fieldset>
       	 <legend>Nota Promissória</legend>
            <table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
              <tr>
                <td width="128">N° Nota Promissória:</td>
                <td width="219"><input type="text" name="textfield8" id="textfield9" style="width:120px;" /></td>
                <td width="78">Vencimento:</td>
                <td width="302"><input type="text" name="textfield9" id="textfield11" style="width:120px;" /></td>
              </tr>
              <tr>
                <td>Valor R$</td>
                <td><input type="text" name="textfield11" id="textfield12" style="width:120px; text-align:right;" /></td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>por extenso:</td>
                <td colspan="3"><input type="text" name="textfield10" id="textfield10" style="width:425px;" /></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td colspan="3">
                	<span class="bt_novos" title="Imprimir"><a href="../nota_promissoria.htm" target="_blank"><img src="../images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
                </td>
              </tr>
           </table>
      </fieldset>
     </div>
     
     
     <div id="garantia_imovel_pf" style="display:none;">
   	   <fieldset>
       	 <legend>Imóvel</legend>
         <table width="755" border="0" cellpadding="2" cellspacing="2" style="text-align:left;">
          <tr>
            <td width="106">Proprietário:</td>
            <td width="635"><input type="text" name="textfield10" id="textfield10" style="width:250px;" /></td>
          </tr>
          <tr>
            <td>Endereço:</td>
            <td><input type="text" name="textfield12" id="textfield13" style="width:450px;" /></td>
          </tr>
          <tr>
            <td>Número Registro:</td>
            <td><input type="text" name="textfield13" id="textfield14" style="width:250px;" /></td>
          </tr>
          <tr>
            <td>Valor R$:</td>
            <td><input type="text" name="textfield14" id="textfield15" style="width:100px; text-align:right;" /></td>
          </tr>
          <tr>
            <td>Observação:</td>
            <td><textarea name="textfield15" rows="3" id="textfield16" style="width:450px;"></textarea></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td><span class="bt_add"><a href="javascript:;" onclick="popup();">Incluir Novo</a></span></td>
          </tr>
     	 </table>
        </fieldset>
        <br clear="all" />
        <br />

        <fieldset>
          <legend>Imóveis Cadastradas</legend>
          <table class="imoveisGarantiaGrid"></table>
        </fieldset>
     </div>
     
     
     <div id="garantia_caucao_liquida_pf" style="display:none;">
     	<fieldset>
        	<legend>Caução Líquida</legend>
             <table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
              <tr>
                <td width="94">Valor Inicial R$:</td>
                <td width="151"><input type="text" name="textfield17" id="textfield17" style="width:150px; text-align:right;" /></td>
                <td width="488"><a href="javascript:;"><img src="../images/ico_check.gif" alt="Incluir" width="16" height="16" border="0" /></a></td>
              </tr>
            </table>
        </fieldset>
        <br clear="all" />
        <br />
        <fieldset>
        	<legend>Caução Líquida</legend>
            <table class="caucaoGrid"></table>
         </fieldset>


     </div>
     <br clear="all" />

    
    
    
    </div>
    
   
    
<div id="tabpf-8" class="distribuicao">

    <table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
          <tr>
            <td>Qtde. Cota:</td>
            <td><input type="text" style="width:100px" /></td>
            <td>Qtde. PDV:</td>
            <td><input type="text" style="width:100px" /></td>
          </tr>
          <tr>
            <td width="120">Box:</td>
            <td width="320"><input type="text" style="width:100px" /></td>
            <td width="116">Tipo de Ponto:</td>
            <td width="171"><select name="select3" id="select2" style="width:155px">
              <option selected="selected"> </option>
              <option>Banca</option>
              <option>Supermercado</option>
            </select></td>
          </tr>
          <tr>
            <td>Assist. Comercial:</td>
            <td><input type="text" style="width:150px" /></td>
            <td>Tipo de Entrega:</td>
            <td><select name="select4" id="select4" style="width:155px">
              <option selected="selected"> </option>
              <option>Cota Retira</option>
              <option>Entrega em Banca</option>
              <option>Entregador</option>
            </select></td>
          </tr>
          <tr>
            <td>Arrendatário:</td>
            <td><input type="checkbox" name="arrendaPj2" id="arrendaPj2" /></td>
            <td>Tipo de Cota:</td>
            <td><select name="select2" id="select" style="width:155px">
              <option selected="selected"> </option>
              <option>Convencional</option>
              <option>Alternativo</option>
            </select></td>
          </tr>
          <tr>
            <td valign="top">Observação:</td>
            <td colspan="3"><textarea name="textarea" rows="4" style="width:605px"></textarea></td>
          </tr>
  </table>
  
  <table width="755" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="377" valign="top">
    <input name="cposterior" type="checkbox" value="" id="cposterior" /><label for="cposterior">Credito Posterior de Encalhe</label>

        <br clear="all" />
          
        <input name="notaDev" type="checkbox" value="" id="notaDev" /><label for="notaDev">Nota Devolução Antecipada</label>
        <br clear="all" />
          
        <input name="repPtoVnda" type="checkbox" value="" id="repPtoVnda" /><label for="repPtoVnda">Reparte por Ponto de Venda</label>
          <br clear="all" />

        <input name="solNumAtrs" type="checkbox" value="" id="solNumAtrs" /><label for="solNumAtrs">Solicitação Num. Atrasados - Internet</label>
     
     
    <br />
    </td>
    <td width="378" valign="top"><table width="373" border="0" cellspacing="2" cellpadding="2">
      <tr class="header_table">
        <td width="142" align="left">Documentos</td>
        <td width="105" align="center">Impresso</td>
        <td width="106" align="center">E-mail</td>
      </tr>
      <tr class="class_linha_1">
        <td>Nota de Envio</td>
        <td align="center"><input type="checkbox" name="checkbox2" id="checkbox2" /></td>
        <td align="center"><input type="checkbox" name="checkbox5" id="checkbox5" /></td>
      </tr>
      <tr class="class_linha_2">
        <td>Chamada de Encalhe</td>
        <td align="center"><input type="checkbox" name="checkbox3" id="checkbox3" /></td>
        <td align="center"><input type="checkbox" name="checkbox6" id="checkbox6" /></td>
      </tr>
      <tr class="class_linha_1">
        <td>Slip</td>
        <td align="center"><input type="checkbox" name="checkbox4" id="checkbox4" /></td>
        <td align="center"><input type="checkbox" name="checkbox" id="checkbox" /></td>
      </tr>
    </table></td>
  </tr>
</table>

<br />
<br />
<br />

</div>
</div>
</div>
    


   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Cota < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Cotas</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="31">Cota:</td>
              <td colspan="3"><input type="text" name="textfield2" id="textfield2" style="width:100px;"/>
              </td>
                <td width="127">Nome / Razão Social:</td>
                <td width="204"><input type="text" name="textfield" id="textfield" style="width:180px;"/></td>
                <td width="72">CPF / CNPJ:</td>
                <td width="253"><input type="text" name="textfield" id="textfield" style="width:150px;"/></td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Cotas Cadastradas</legend>
        <div class="grids" style="display:none;">
        	<table class="pessoasGrid"></table>
        </div>

        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup_cpf();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>CPF</a></span>
        
        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup_cnpj();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>CNPJ</a></span>
           
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</body>