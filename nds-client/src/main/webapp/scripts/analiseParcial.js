function popup_mudar_base() {
	// $( "#dialog:ui-dialog" ).dialog( "destroy" );

	$("#dialog-mudar-base").dialog({
		resizable : false,
		height : 470,
		width : 550,
		modal : true,
		buttons : {
			"Confirmar" : function() {
				$(this).dialog("close");
				$("#effect").show("highlight", {}, 1000, callback);

			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});
};
function popup_cotas_estudo() {
	// $( "#dialog:ui-dialog" ).dialog( "destroy" );

	$("#dialog-cotas-estudos").dialog({
		resizable : false,
		height : 530,
		width : 550,
		modal : true,
		buttons : {
			"Confirmar" : function() {
				$(this).dialog("close");
				$("#effect").show("highlight", {}, 1000, callback);

			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});
};

function popup_cotas_detalhes() {
	// $( "#dialog:ui-dialog" ).dialog( "destroy" );

	$("#dialog-cotas-detalhes").dialog({
		resizable : false,
		height : 560,
		width : 740,
		modal : true,
		buttons : {
			"Fechar" : function() {
				$(this).dialog("close");

			}
		}
	});
};

function popup_edicoes_produto() {
	// $( "#dialog:ui-dialog" ).dialog( "destroy" );

	$("#dialog-edicoes-produtos").dialog({
		resizable : false,
		height : 420,
		width : 550,
		modal : true,
		buttons : {
			"Confirmar" : function() {
				$(this).dialog("close");

			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});
};

function selecionarElementos(tipo,optionsId){
	
	$.getJSON("/nds-client/componentes/elementos",{"tipo":tipo},function(data){
		var options = $("#"+optionsId);
		options.empty();
		$.each(data, function() {
		    options.append($("<option />").val(this.tipo+"_"+this.id).text(this.value));
		});
		
	});
}
function apresentarOpcoesOrdenarPor(opcao){
	if(opcao === "selecione"){
		$("#opcoesOrdenarPor").hide();
	}else{
		$(".label").hide();
		$("#label_"+opcao).show();
		$("#opcoesOrdenarPor").show();
	}
}


function filtrarOrdenarPor(estudo){
	
	$(".baseEstudoGrid").flexOptions({
		params:[
		        { 'name':'filterSortName', 'value':$("#filtroOrdenarPor").val(), }, 
		        { 'name':'filterSortFrom', 'value':$("#ordenarPorDe").val(), }, 
		        { 'name':'filterSortTo', 'value':$("#ordenarPorAte").val(), }, 
		        { 'name':'id', 'value':estudo }, 
		        { 'name':'elemento', 'value':$("#elementos :selected").val(), }
		       ]
	}).flexReload();
	if(event)
		event.preventDefault();
	return false;
}

var analiseParcialController = $.extend(true,{
	
	preProcessGrid : function(resultado) {
		for (var i = 0; i < resultado.rows.length; i++) {
			if (resultado.rows[i].cell.classificacao == undefined) {
				resultado.rows[i].cell.classificacao = '';
			}
			if (resultado.rows[i].cell.reparte1 == 0) {
				resultado.rows[i].cell.reparte1 = "";
				resultado.rows[i].cell.venda1 = "";
			}
			if (resultado.rows[i].cell.reparte2 == 0) {
				resultado.rows[i].cell.reparte2 = "";
				resultado.rows[i].cell.venda2 = "";
			}
			if (resultado.rows[i].cell.reparte3 == 0) {
				resultado.rows[i].cell.reparte3 = "";
				resultado.rows[i].cell.venda3 = "";
			}
			if (resultado.rows[i].cell.reparte4 == 0) {
				resultado.rows[i].cell.reparte4 = "";
				resultado.rows[i].cell.venda4 = "";
			}
			if (resultado.rows[i].cell.reparte5 == 0) {
				resultado.rows[i].cell.reparte5 = "";
				resultado.rows[i].cell.venda5 = "";
			}
			if (resultado.rows[i].cell.reparte6 == 0) {
				resultado.rows[i].cell.reparte6 = "";
				resultado.rows[i].cell.venda6 = "";
			}
		};
		return resultado;
	},
	
	init:function(_id, _faixaDe, _faixaAte){
		
		$("#baseEstudoGridParcial").flexigrid({
			preProcess : analiseParcialController.preProcessGrid,
			url : contextPath + '/distribuicao/analise/parcial/init',
			params: [{name: 'id', value: _id}, {name: 'faixaDe', value: _faixaDe}, {name: 'faixaAte', value: _faixaAte}],
			dataType : 'json',
			colModel : [ { display : 'Cota', name : 'cota', width : 33, sortable : true, align : 'left' }, 
			             { display : 'Class.', name : 'classificacao', width : 30, sortable : true, align : 'center' }, 
			             { display : 'Nome', name : 'nome', width : 100, sortable : true, align : 'left' }, 
			             { display : 'NPDV', name : 'npdv', width : 30, sortable : true, align : 'right' }, 
			             { display : 'Rep. Sugerido', name : 'reparteSugerido', width : 50, sortable : true, align : 'right' }, 
			             { display : 'LEG', name : 'leg', width : 20, sortable : true, align : 'center' }, 
			             { display : 'Juram.', name : 'juramento', width : 40, sortable : true, align : 'right' }, 
			             { display : 'Média.VDA', name : 'mediaVenda', width : 50, sortable : true, align : 'right' }, 
			             { display : 'Último. Reparte', name : 'ultimoReparte', width : 50, sortable : true, align : 'right' },
			             { display : 'REP', name : 'reparte1', width : 23, sortable : true, align : 'right' }, 
			             { display : 'VDA', name : 'venda1',   width : 23, sortable : true, align : 'right' }, 
			             { display : 'REP', name : 'reparte2', width : 23, sortable : true, align : 'right' }, 
			             { display : 'VDA', name : 'venda2',   width : 23, sortable : true, align : 'right' }, 
			             { display : 'REP', name : 'reparte3', width : 23, sortable : true, align : 'right' }, 
			             { display : 'VDA', name : 'venda3',   width : 23, sortable : true, align : 'right' }, 
			             { display : 'REP', name : 'reparte4', width : 23, sortable : true, align : 'right' }, 
			             { display : 'VDA', name : 'venda4',   width : 23, sortable : true, align : 'right' }, 
			             { display : 'REP', name : 'reparte5', width : 23, sortable : true, align : 'right' }, 
			             { display : 'VDA', name : 'venda5',   width : 23, sortable : true, align : 'right' }, 
			             { display : 'REP', name : 'reparte6', width : 23, sortable : true, align : 'right' }, 
			             { display : 'VDA', name : 'venda6',   width : 23, sortable : true, align : 'right' }, 
			             { display : 'VDA', name : 'numeroEdicao1',   width : 23, sortable : true, align : 'right', hide: true },
			             { display : 'VDA', name : 'numeroEdicao2',   width : 23, sortable : true, align : 'right', hide: true },
			             { display : 'VDA', name : 'numeroEdicao3',   width : 23, sortable : true, align : 'right', hide: true },
			             { display : 'VDA', name : 'numeroEdicao4',   width : 23, sortable : true, align : 'right', hide: true },
			             { display : 'VDA', name : 'numeroEdicao5',   width : 23, sortable : true, align : 'right', hide: true },
			             { display : 'VDA', name : 'numeroEdicao6',   width : 23, sortable : true, align : 'right', hide: true }
			           ],
			width : 950,
			height : 200,
			sortorder:'desc',
			sortname:'reparteSugerido',
			onSuccess:function(x){
				addDbClickSupport(".baseEstudoGrid", "td[abbr=reparteSugerido]");
				
				$('#edicao_base_1').html($('td[abbr=numeroEdicao1] div').html());
				$('#edicao_base_2').html($('td[abbr=numeroEdicao2] div').html());
				$('#edicao_base_3').html($('td[abbr=numeroEdicao3] div').html());
				$('#edicao_base_4').html($('td[abbr=numeroEdicao4] div').html());
				$('#edicao_base_5').html($('td[abbr=numeroEdicao5] div').html());
				$('#edicao_base_6').html($('td[abbr=numeroEdicao6] div').html());
				
				$('td[abbr^=venda]', $('.baseEstudoGrid')).each(function(i, el) {
					$(el).css({'color': 'red', 'font-weight': 'bold'});
				});
				$('td[abbr^=ultimoReparte]', $('.baseEstudoGrid')).each(function(i, el) {
					$(el).css({'font-weight': 'bold'});
				});
				$('td[abbr^=reparteSugerido]', $('.baseEstudoGrid')).each(function(i, el) {
					$(el).css({'font-weight': 'bold'});
				});
				
				soma("#total_juramento", "td[abbr=juramento]");
				soma("#total_media_venda", "td[abbr=mediaVenda]");
				soma("#total_ultimo_reparte", "td[abbr=ultimoReparte]");
				soma("#total_reparte1", "td[abbr=reparte1]");
				soma("#total_venda1", "td[abbr=venda1]");
				soma("#total_reparte2", "td[abbr=reparte2]");
				soma("#total_venda2", "td[abbr=venda2]");
				soma("#total_reparte3", "td[abbr=reparte3]");
				soma("#total_venda3", "td[abbr=venda3]");
				soma("#total_reparte4", "td[abbr=reparte4]");
				soma("#total_venda4", "td[abbr=venda4]");
				soma("#total_reparte5", "td[abbr=reparte5]");
				soma("#total_venda5", "td[abbr=venda5]");
				soma("#total_reparte6", "td[abbr=reparte6]");
				soma("#total_venda6", "td[abbr=venda6]");
				conta("#total_de_cotas","td[abbr=cota]");
				
			}
		});
		
		$("#liberar").click(function(event){
			$.post("liberar",{"id":$("#estudoId").text()},function(){
				alert("Estudo liberado com sucesso");
			});
			event.preventDefault();
		});
		
		$(".cotasEstudoGrid").flexigrid({
			url:"/nds-client/cotas-que-nao-entraram-no-estudo/filtrar/",
			dataType : 'json',
			colModel : [ 
			             { display : 'Cota', name : 'cota', width : 40, sortable : true, align : 'left' }, 
			             { display : 'Nome', name : 'nome', width : 160, sortable : true, align : 'left' }, 
			             { display : 'Motivo', name : 'motivo', width : 160, sortable : true, align : 'left' }, 
			             { display : 'Qtde', name : 'quantidade', width : 60, sortable : true, align : 'center' }
			             ],
			width : 490,
			height : 200,
			sortorder:'desc',
			sortname:'cota',
			
		});
		
		cotasQueNaoEntraramNoEstudo();
	}
});

function cotasQueNaoEntraramNoEstudo(){
	var filtrarCotasQueNaoEntraramNoEstudo = function(event){
		console.log(1);
		var cota = $("#cotasQueNaoEntraramNoEstudo_cota").val();
		var nome = $("#cotasQueNaoEntraramNoEstudo_nome").val();
		var motivo = $("#cotasQueNaoEntraramNoEstudo_motivo").val();
		var elemento = $("#cotasQueNaoEntraramNoEstudo_elementos").val();
		var estudo = $("#estudoId").val();
		console.log(2);
		
		$(".cotasEstudoGrid").flexOptions({
			params:[
			        { 'name':'queryDTO.cota', 'value':cota, }, 
			        { 'name':'queryDTO.nome', 'value':nome, }, 
			        { 'name':'queryDTO.motivo', 'value':motivo, }, 
			        { 'name':'queryDTO.elemento', 'value':elemento }, 
			        { 'name':'queryDTO.estudo', 'value':estudo, }
			       ]
		}).flexReload();
		console.log(3);
	};
	$("#cotasQueNaoEntraramNoEstudo_cota").blur(filtrarCotasQueNaoEntraramNoEstudo);
	$("#cotasQueNaoEntraramNoEstudo_nome").blur(filtrarCotasQueNaoEntraramNoEstudo);
	$("#cotasQueNaoEntraramNoEstudo_motivo").change(filtrarCotasQueNaoEntraramNoEstudo);
	$("#cotasQueNaoEntraramNoEstudo_elementos").change(filtrarCotasQueNaoEntraramNoEstudo);
	console.log(filtrarCotasQueNaoEntraramNoEstudo);
	
}

function addDbClickSupport(){
	$(arguments[0]).find(arguments[1]).dblclick(function(event){
		var td = $(this);
		var html = td.html();
		var numeroCota = td.parent().find("td[abbr=cota]").text();
		var estudoId = $("#estudoId").text();
		var reparteAtual = td.text();
		$(this).html("");
		var input = $("<input>")
			.attr("id","reparteAtual")
			.val(reparteAtual)
			.css("width","40px")
			.keyup(function(e){
				if(e.keyCode==27){
					$(this).val(reparteAtual);
					$(this).blur();
				}
			})
			.keypress(function(e){
					if(e.charCode==13){
						$(this).blur();
					}
			})
			.blur(function(){
				if(input.val()!=reparteAtual){
					console.log(input.val())
					$.ajax({
						url:"mudarReparte",
						data:{'numeroCota':numeroCota,'estudoId':estudoId,'reparte':input.val()},
						success:function(){
							td.html(html);
							td.find("div").text(input.val());
						},
						error:function(){
							alert("Erro ao enviar novo reparte!");
							$(".baseEstudo2Grid").flexReload();
						}
					});
				}else{
					td.html(html);
					td.find("div").text(input.val());
				}
			});
			input.appendTo(td);
			input.focus();
	});
}
function soma(idDest, abbr){
	var totalSugerido=0;
	$(abbr).each(function(i,value){
		if (!isNaN(parseFloat(value.textContent))) {
			totalSugerido += parseFloat(value.textContent);
		}
	});
	
	var vermelho = $(idDest).find(".vermelho");
	if(vermelho.length == 0){
		$(idDest).text(Math.round(totalSugerido));
	}else{
		vermelho.text(Math.round(totalSugerido));
	}
}

function conta(idDest, abbr){
	var total=0;
	$(abbr).each(function(i,value){
		total++;
	});
	$(idDest).text(total);
}



$(".cotasDetalhesGrid").flexigrid({
	url : '${pageContext.request.contextPath}/xml/cotasDetalhes-xml.xml',
	dataType : 'xml',
	colModel : [ {
		display : 'Código',
		name : 'codigo',
		width : 40,
		sortable : true,
		align : 'left'
	}, {
		display : 'Tipo',
		name : 'tipo',
		width : 90,
		sortable : true,
		align : 'left'
	}, {
		display : '% Fat.',
		name : 'percFaturamento',
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
});

$(".prodCadastradosGrid").flexigrid({
	url : '${pageContext.request.contextPath}/xml/prodCadastrados-xml.xml',
	dataType : 'xml',
	colModel : [ {
		display : 'Código',
		name : 'codigo',
		width : 70,
		sortable : true,
		align : 'left'
	}, {
		display : 'Produto',
		name : 'produto',
		width : 180,
		sortable : true,
		align : 'left'
	}, {
		display : 'Edição',
		name : 'edicao',
		width : 70,
		sortable : true,
		align : 'left'
	}, {
		display : 'Ação',
		name : 'acao',
		width : 50,
		sortable : true,
		align : 'center'
	}, {
		display : 'Ordenar',
		name : 'ordenar',
		width : 40,
		sortable : true,
		align : 'left'
	}],
	width : 490,
	height : 240
});
$(".edicaoProdCadastradosGrid").flexigrid({
	url : '${pageContext.request.contextPath}/xml/pesqEdicao-xml.xml',
	dataType : 'xml',
	colModel : [ {
		display : 'Edição',
		name : 'edicao',
		width : 45,
		sortable : true,
		align : 'left'
	}, {
		display : 'Data Lançamento',
		name : 'dtLancamento',
		width : 100,
		sortable : true,
		align : 'center'
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
		display : 'Status',
		name : 'status',
		width : 110,
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
		width : 30,
		sortable : true,
		align : 'center'
	}],
	width : 500,
	height : 200
});

function popup_detalhes() {
	$( "#dialog-detalhes" ).dialog({
		resizable: false,
		height:'auto',
		width:'auto',
		modal: false,
	});
};
function popup_detalhes_close() {
  $("#dialog-detalhes").dialog("close");
}

//@ sourceURL=analiseParcial.js
