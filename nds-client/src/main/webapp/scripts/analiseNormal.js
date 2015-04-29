function selecionarElementos(tipo){
	$.getJSON("/nds-client/componentes/elementos",{"tipo":tipo},function(data){
		var options = $("#elementos");
		options.empty();
		$.each(data, function() {
		    options.append($("<option />").val(this.tipo+"_"+this.id).text(this.value));
		});
		
	});
}
$(function(){
	$("#filterOrder").change(function(){
		if(this.value==0){
			$("#filterOrderFields").hide();
			$("input[name=filterSortFrom]").val("");
			$("input[name=filterSortTo]").val("");
			$(".baseEstudo2Grid").flexOptions({"params":readParams()}).flexReload();
		}else{
			$("#filterOrderFields").show();
			$("#filtroReparte,#filtroRanking,#filtroPercVenda,#filtroReducao").hide();
			$("#"+this.value).show();
		}
	});
	
	$("#elementos").change(function(){
		$(".baseEstudo2Grid").flexOptions({"params":readParams()}).flexReload();
	});
	
	$("#newSearchByFilterOrder").click(function(event){
		$(".baseEstudo2Grid").flexOptions({
			"params":readParams()
		}).flexReload();
		event.preventDefault();
		return false;
	});
	
	$("#botao_mudar_base").click(function(event){
		popup_mudar_base();
		event.preventDefault();
		return false;
	});
	$("#liberar").click(function(event){
		$.post("liberar",{"id":$("#estudo_id").text()},function(){
			alert("Estudo liberado com sucesso");
			if(typeof(matrizDistribuicao)=="object"){
         		matrizDistribuicao.carregarGrid();
			}
		});
		event.preventDefault();
		return false;
	});
	
	$('#naoLiberar').click(function(event){
    	analiseParcialController.exibirMsg('WARNING', ['Já existe um estudo liberado para este lançamento.']);
        event.preventDefault();
    });
	
	$(".arrowUp[src$='sobe.gif']").click(arrowUpClick);
	$(".arrowDown[src$='desce.gif']").click(arrowDownClick);
	$(".editarEdicao").click(function(event){
		popup_edicoes_produto($(this).closest("tr"));
		event.preventDefault();
		return false;
	});
	$(".excluirEdicao").click(function(event){
		$(this).closest("tr").remove();
		event.preventDefault();
		return false;
	});
});

function popup_mudar_base() {
	var rows = [];
	for ( var i = 6; i > 0; i--) {
		var e = $("#edicao"+i).text();
		if(!!e && e!="" && e!="-" && e!=""){
			rows.push({cell:{codigo:$("#analise-normal-codigoProduto").text(),produto:$("#analise-normal-nomeProduto").text(),edicao:e}});
		}
	}
	
	
	$(".prodCadastradosGrid").flexigrid({
		dataType:'json',
		params:[{"name":"id","value":$("#estudo_id").text()}],
		width:500,
		disableSelect:true,
		colModel:[ 
			{display : 'Código',name : 'codigo',width : 50,sortable : false,align : 'left'}, 
			{display : 'Produto',name : 'produto',width : 235,sortable : false,align : 'left'},
			{display : 'Edição',name : 'edicao',width : 50,sortable : false,align : 'left'},
			{display : 'Ação', name : 'acao', width : 50, sortable : true, align : 'center' }, 
			{display : 'Ordenar', name : 'ordenar', width : 50, sortable : true, align : 'left' }
		 ],
		 onSuccess:function(){
			$("td[abbr=acao] div").html($(".acoes"));
			$("td[abbr=ordenar] div").html($(".setas"));
			checkArrow();
		 }
	}).flexAddData({page:1,total:6,'rows':rows}).flexReload();
	
	$( "#dialog-mudar-base" ).dialog({
		resizable: false,
		height:470,
		width:550,
		modal: true,
		buttons: {
			"Confirmar": function() {
				var edicoes = [];
				var tds = $(".prodCadastradosGrid tr td:nth-child(3)");
				for ( var i=0;i<tds.length;i++){
					edicoes.push($(tds[i]).text());
				}
				$( this ).dialog( "close" );
				var params = readParams();
				params.push({name:"edicoes","value":edicoes});
				$(".baseEstudo2Grid").flexOptions({
					"params":params
				}).flexReload();
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
};

	
	var analiseNormalController = $.extend(true,{
	init:function(_id, edicoes){
		var params = [{name:"id",value:_id}];
		if(!!edicoes && edicoes.length && edicoes.length > 0){
			params.push({name:"edicoes",value:edicoes});
		}
		$(".baseEstudo2Grid").flexigrid({
			url : contextPath + '/lancamento/analise/normal/init',
			dataType : 'json',
			"params":params,
			onChangeSort:newOrder,
			disableSelect:true,
			preProcess:preProcessPrincipal,
			colModel : [ 
			    { display : 'Cota', name : 'cota', width : 50, sortable : true, align : 'left' }, 
			    { display : 'Class.', name : 'classificacao', width : 30, sortable : true, align : 'center' }, 
			    { display : 'Nome', name : 'nome', width : 80, sortable : true, align : 'left' }, 
			    { display : 'NPDV', name : 'npdv', width : 30, sortable : true, align : 'right' }, 
			    { display : 'Rep. Sugerido', name : 'reparteSugerido', width : 40, sortable : true, align : 'right' }, 
			    { display : 'LEG', name : 'leg', width : 20, sortable : true, align : 'center' }, 
			    { display : 'Méd.VDA', name : 'mediaVenda', width : 35, sortable : true, align : 'right' }, 
			    { display : 'Út. Reparte', name : 'ultimoReparte', width : 30, sortable : true, align : 'right' }, 
			    { display : 'REP', name : 'reparte6', width : 30, sortable : true, align : 'right' }, 
			    { display : 'VDA', name : 'venda6', width : 30, sortable : true, align : 'right' },
			    { display : 'REP', name : 'reparte5', width : 30, sortable : true, align : 'right' }, 
			    { display : 'VDA', name : 'venda5', width : 30, sortable : true, align : 'right' },
			    { display : 'REP', name : 'reparte4', width : 30, sortable : true, align : 'right' }, 
			    { display : 'VDA', name : 'venda4', width : 30, sortable : true, align : 'right' },
			    { display : 'REP', name : 'reparte3', width : 30, sortable : true, align : 'right' }, 
			    { display : 'VDA', name : 'venda3', width : 30, sortable : true, align : 'right' },
			    { display : 'REP', name : 'reparte2', width : 33, sortable : true, align : 'right' }, 
			    { display : 'VDA', name : 'venda2', width : 32, sortable : true, align : 'right' },
			    { display : 'REP', name : 'reparte1', width : 33, sortable : true, align : 'right' }, 
			    { display : 'VDA', name : 'venda1', width : 32, sortable : true, align : 'right' }
			],
			width : 950,
			height : 200,
			sortorder:'asc',
			sortname:'reparteSugerido',
			onSuccess:function(x){
				soma("#total_reparte_sugerido", "td[abbr=reparteSugerido]");
				soma("#total_media_venda","td[abbr=mediaVenda]");
				soma("#total_ultimo_reparte","td[abbr=ultimoReparte]");
				soma("#total_reparte6","td[abbr=reparte6]");
				soma("#total_reparte5","td[abbr=reparte5]");
				soma("#total_reparte4","td[abbr=reparte4]");
				soma("#total_reparte3","td[abbr=reparte3]");
				soma("#total_reparte2","td[abbr=reparte2]");
				soma("#total_reparte1","td[abbr=reparte1]");
				soma("#total_vendas6","td[abbr=venda6]");
				soma("#total_vendas5","td[abbr=venda5]");
				soma("#total_vendas4","td[abbr=venda4]");
				soma("#total_vendas3","td[abbr=venda3]");
				soma("#total_vendas2","td[abbr=venda2]");
				soma("#total_vendas1","td[abbr=venda1]");
				addDbClickSupport(".baseEstudo2Grid", "td[abbr=reparteSugerido]");
				pintaDeVermelho(["td[abbr=venda6]",
						"td[abbr=venda5]",
						"td[abbr=venda4]",
						"td[abbr=venda3]",
						"td[abbr=venda2]",
						"td[abbr=venda1]"]);
				$("#qtdDeCotas").text($("td[abbr=cota]").length);
			}
		});
	}
});

function popup_edicoes_produto(tr) {
	$(".edicaoProdCadastradosGrid").flexigrid({
		url : 'produtosParaGrid',
		params:[{name:"id",value:$("#estudo_id").text()}],
		dataType : 'json',
		disableSelect:true,
		onSuccess:function(){
			var check = $("<input type='checkbox' class='edicaoSelecao'/>").click(function(){
				$(".edicaoSelecao").removeAttr("checked");
				$(this).attr("checked","true");
			});
			$("td[abbr=sel] div").html(check);
		},
		colModel : [ {
			display : 'Edição',
			name : 'edicao',
			width : 45,
			sortable : true,
			align : 'left'
		}, {
			display : 'Data Lançamento',
			name : 'dataLancamento',
			width : 100,
			sortable : false,
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
		} ],
		width : 500,
		height : 200
	});

	$("#dialog-edicoes-produtos").dialog({
		resizable : false,
		height : 420,
		width : 550,
		modal : true,
		buttons : {
			"Confirmar" : function() {
				tr.find("td:nth-child(3) div").text($(".edicaoSelecao:checked").closest("tr").find("td:nth-child(1)").text());
				$(this).dialog("close");
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});
};	
	
	
function preProcessPrincipal(json){
	
	if (json.cell) {
		for ( var i = 1; i <= 6; i++) {
			if(json["rows"][0]["cell"]["edicao"+i]){
				$("#edicao"+i).text(json["rows"][0]["cell"]["edicao"+i]);
			}
		}
	}
	return json;
}
	
function pintaDeVermelho(selectors){
	for(s in selectors){
		$(selectors[s]).addClass("vermelho");
	}
}
function addDbClickSupport(){
	$(arguments[0]).find(arguments[1]).dblclick(function(event){
		var td = $(this);
		var html = td.html();
		var numeroCota = td.parent().find("td[abbr=cota]").text();
		var estudoId = $("#estudo_id").text();
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
		totalSugerido+=parseFloat(value.textContent);
	});
	$(idDest).text(Math.round(totalSugerido));
}


function newOrder(sortname, sortorder){
	var params = $.merge([{
		"name":"sortname",
		"value":sortname
	},{
		"name":"sortorder",
		"value":sortorder
	}],readParams());
	$(".baseEstudo2Grid").flexOptions({
		"params":params
	}).flexReload();
	
}

function readParams(){
	var params = [];
	var elemento = $("#elementos").val();
	if(elemento && elemento != ""){
		params.push({name:"elemento",value:elemento});
	}
	var filterName = $("#filterOrder").val();
	if(filterName && filterName!=0){
		params.push({"name":"filterSortName",
			"value":filterName}) ;
	}
	var from = $("input[name=filterSortFrom]").val();
	if(from && from != ""){
		params.push({"name":"filterSortFrom",
			"value":from});
	}
	var to = $("input[name=filterSortTo]").val();
	if(to && to != ""){
		params.push({"name":"filterSortTo",
			"value":to});
	}
	params.push({"name":"id",
				"value":$("#estudo_id").text()});
	return params;
}


function checkArrow(){
	$(".arrowUp").each(function(i,e){
		e = $(e);
		var tr = e.closest("tr");
		tr.removeClass("erow");
		e.unbind('click');
		var src = e.attr('src');
		src = src.slice(0,src.lastIndexOf("/"));
		if(tr.is(":first-child")){
			src+="/seta_sobe_desab.gif";
		}else{
			src+="/seta_sobe.gif";
		}
		e.attr('src',src);
	});
	$(".arrowDown").each(function(i,e){
		e = $(e);
		e.unbind('click');
		var src = e.attr('src');
		src = src.slice(0,src.lastIndexOf("/"));
		if(e.closest("tr").is(":last-child")){
			src+="/seta_desce_desab.gif";
		}else{
			src+="/seta_desce.gif";
		}
		e.attr('src',src);
	});
	
	var trs = $(".prodCadastradosGrid tr");
	for ( var i = 0; i < trs.length; i++) {
		if(i%2==1){
			$(trs[i]).addClass("erow");
		}
	}
	
	$(".arrowUp[src$='sobe.gif']").click(arrowUpClick);
	$(".arrowDown[src$='desce.gif']").click(arrowDownClick);
}

function arrowUpClick(event){
	var a = $(this);
	var tr = a.closest("tr")[0];
	var tbody = a.closest("tbody");
	var trs = tbody.find("tr");
	var tmp = [];
	for ( var i = trs.length-1; i >= 0; i--) {
		if(trs[i]==tr){
			tmp.push(trs[i-1]);
			tmp.push(tr);
			i--;
		}else{
			tmp.push(trs[i]);
		}
	}
	tmp.reverse();
	tbody.append(tmp);
	checkArrow();
	event.preventDefault();
	return false;
}

function arrowDownClick(event){
	var a = $(this);
	var tr = a.closest("tr")[0];
	var tbody = a.closest("tbody");
	var trs = tbody.find("tr");
	var tmp = [];
	for ( var i = 0; i < trs.length; i++) {
		if(trs[i]==tr){
			tmp.push(trs[i+1]);
			tmp.push(tr);
			i++;
		}else{
			tmp.push(trs[i]);
		}
	}
	tbody.append(tmp);
	checkArrow();
	event.preventDefault();
	return false;
}
//@ sourceURL=analiseNormal.js

//$(".pdvCotaGrid").flexigrid({
//url : 'init?id=9',
//dataType : 'json',
//colModel : [ {
//	display : 'CÃ³digo',
//	name : 'codigo',
//	width : 50,
//	sortable : true,
//	align : 'left'
//}, {
//	display : 'Nome PDV',
//	name : 'nomePdv',
//	width : 120,
//	sortable : true,
//	align : 'left'
//}, {
//	display : 'EndereÃ§o',
//	name : 'endereco',
//	width : 320,
//	sortable : true,
//	align : 'left'
//},  {
//	display : 'Reparte',
//	name : 'reparte',
//	width : 40,
//	sortable : true,
//	align : 'center'
//}],
//sortname : "codigo",
//sortorder : "asc",
//usepager : true,
//useRp : true,
//rp : 15,
//showTableToggleBtn : true,
//width : 600,
//height : 200
//});