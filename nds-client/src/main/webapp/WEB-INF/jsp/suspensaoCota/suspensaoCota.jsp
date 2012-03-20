<head>

<script language="javascript" type="text/javascript">
	
	function getDetalhes(idCota, nome) {
		nomeCota = nome;
		$.postJSON("<c:url value='/suspensaoCota/getInadinplenciasDaCota'/>", 
				"idCota="+idCota+"&method='get'", 
				popupDetalhes);	
	};
	
	var nomeCota;
	
	function popupDetalhes(result) {
				
		gerarTabelaDetalhes(result, nomeCota);
		
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					
				},
			}
		});
	}
	
	function selecionarTodos(element) {
		$.postJSON("<c:url value='/suspensaoCota/selecionarTodos'/>", 
				"selecionado="+element.checked, 
				checkAll(element,'selecao'));	
		
	}
	
	function popupConfirmar() {
	
		$( "#dialog-suspender" ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					$.postJSON("<c:url value='/suspensaoCota/suspenderCotas'/>", 
							"", 
							popupRelatorio);	
					
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	
	function popupRelatorio(result) {
		
		var cotas = result[0];
		var mensagens = result[1];
		var status = result[2];	
		
		
		if(mensagens!=null && mensagens.length!=0) {
			exibirMensagem(status,mensagens);
		}
		
		if(status=="ERROR" || status=="WARNING") {
			return null;
		}
		
		gerarRelatorio(cotas);
				
		$( "#divRelatorio" ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Fechar": function() {
					$(".suspensaoGrid").flexReload();
					exibirMensagem(status,["Suspensão realizada com sucesso."]);
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	
	function adicionarSelecao(id, check) {
		
		$.postJSON("<c:url value='/suspensaoCota/selecionarItem'/>", 
				"idCota="+id +"&selecionado="+check.checked, 
				retornoSemAcao);				
	}
	
	function retornoSemAcao(data) {
		
	}
	
	function popup_excluir() {
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:'auto',
			width:380,
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
	
	
 
	function mostrar(){
		$(".grids").show();
	}	
	
	$(function() {
		$( "#datepickerDe" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerDe1" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
	});
		
</script>

</head>

<body>
<form action="" method="get" id="form1" name="form1">

<div id="divRelatorio" title="Cota Suspensa" style="display:none">

	<fieldset style="width:330px;">
     
     <legend>Proceder com a devolução de Documentação das Cotas</legend>
     
   	<table id="tabelaRelatorio" width="330" border="0" cellspacing="1" cellpadding="1">
     
    </table>
</fieldset>
        
</div>

<div id="dialog-suspender" title="Suspensão da Cota">
	<p><strong>Confirma Suspensão da Cota?</strong></p>
</div>





<div id="dialog-detalhes" title="Suspensão de Cota">     
    
  </div>



<div class="corpo" style="display:none;">
    
   
    <div class="container">
      
      
       <fieldset class="classFieldset">
       	  <legend>Suspender Cotas</legend>
        <div class="grids" style="display:block;">
			<table class="suspensaoGrid"></table>
          	<table width="100%" border="0" cellspacing="2" cellpadding="2">
              <tr>
                <td width="36%">
                	 
                     <span class="bt_novos" title="Suspender Cota">
<!-- SUSPENDER COTAS -->
                     	<a href="javascript:;" onclick="popupConfirmar();">
                     		<img src="${pageContext.request.contextPath}/images/ico_suspender.gif" hspace="5" border="0"/>
                     		Suspender Cotas
                     	</a>
                     	
                     </span>
<!-- GERAR EXCEL -->
                     <span class="bt_novos" title="Gerar Arquivo">
	                     <a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
	                     	Arquivo
	                     </a>
                     </span>

				<span class="bt_novos" title="Imprimir">
<!-- IMPRIMIR -->
					<a href="javascript:;">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Imprimir
					</a>
				</span>
                    
                </td>
                
                <td width="18%">
                	<strong>
                		&nbsp;Total de Cotas Sugeridas:
                	</strong>
                </td>
                
                <td width="5%" id="totalSugerida">                	
                </td>
                
                <td width="7%">
                	<strong>
                		Total R$:
                	</strong>
                </td>
                
                <td width="17%" id="total">                	
                	
                </td>
                
                <td width="17%">
                
<!-- SELECIONAR TODOS -->	                
	                <span class="bt_sellAll">
	                	<label for="sel">Selecionar Todos</label>
	                	<input type="checkbox" id="sel" name="Todos" onclick="selecionarTodos(this)" style="float:left;"/>
	                </span>
	                
                </td>
              </tr>
            </table>

		
        
</div>
		
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>

        

    
    </div>
</div> 
</form>
<script>	
	$(function() {	
		
		$(".suspensaoGrid").flexigrid($.extend({},{
			url : '<c:url value="/suspensaoCota/obterCotasSuspensaoJSON"/>',
			dataType : 'json',
			preProcess:processaRetornoPesquisa,
			onChangeSort: function(name, order) { sortGrid(".suspensaoGrid", order);}, 
			colModel : [  {
				display : 'Cota',
				name : 'cota',
				width : 55,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 210,
				sortable : true,
				align : 'left'
			},{
				display : 'Valor Consignado Total R$',
				name : 'vlrConsignado',
				width : 150,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor Reparte do Dia R$',
				name : 'vlrReparte',
				width : 150,
				sortable : true,
				align : 'right'
			}, {
				display : 'Divida Acumulada R$',
				name : 'dividaAcumulada',
				width : 140,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dias em Aberto',
				name : 'diasAberto',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 40,
				sortable : false,
				align : 'center',
			},{
				display : '  ',
				name : 'selecionado',
				width : 20,
				sortable : false,
				align : 'center'
			}],
			sortname : "name",
			sortorder : "asc",
			usepager : false,
			useRp : false,
			showTableToggleBtn : true,
			width : 960,
			height : 260
		})); 	
	});
	
	function processaRetornoPesquisa(data) {
		
		var grid = data[0];
		var mensagens = data[1];
		var status = data[2];
		
		var checkTodos = document.getElementById("sel");
		
		if(checkTodos.checked==true) {
			
		}
		
		if(mensagens != null && mensagens.length!=0 && checkTodos.checked!=true) {
			exibirMensagem(status,mensagens);
			checkTodos.checked=false;
		}
				
		if(!grid.rows || status=="error") {
			return grid;
		} else {
			$(".corpo").show();	
		}
		

		var totalSugerida = grid.rows.length;
		var total = 0.0;
		
		for(var i=0; i<grid.rows.length; i++) {			
			
			var cell = grid.rows[i].cell;
			
			total += parseFloat(cell.vlrConsignado.replace(".","").replace(",","."));
			
			cell.acao = gerarAcoes(cell.idCota,cell.dividas,cell.nome);
			cell.selecionado = gerarCheckbox('idCheck'+i,'selecao', cell.idCota,cell.selecionado);;					
		}
		
		document.getElementById("total").innerText = total.toFixed(2).replace(".",",");
		document.getElementById("totalSugerida").innerText = totalSugerida;	
		
		return grid;
	}
	
	function gerarCheckbox(id,name,idCota,selecionado) {
		
		var input = document.createElement("INPUT");
		input.id=id;
		input.name=name;
		input.style.setProperty("float","left");
		input.type="checkbox";
		input.setAttribute("onclick","adicionarSelecao("+idCota+",this);");
		
		if(selecionado==true) {
			input.checker=true;
		}
		
		return input.outerHTML; 
	}
	
	function gerarAcoes(idCota,dividas,nome) {
		
		var a = document.createElement("A");
		a.href = "javascript:;";
		a.setAttribute("onclick","getDetalhes("+idCota+",'"+nome+"');");
		
		var img =document.createElement("IMG");
		img.src="${pageContext.request.contextPath}/images/ico_detalhes.png";
		img.border="0";
		img.hspace="5";
		img.title="Detalhes";		
		a.innerHTML = img.outerHTML;		
		
		return a.outerHTML;
	}
	
	function gerarTabelaDetalhes(dividas, nome) {
		var div = document.getElementById("dialog-detalhes");
		
		div.innerHTML="";
		
		var fieldset  = document.createElement("FIELDSET");
		fieldset.style.setProperty("width","330px");
		
		div.appendChild(fieldset);
		
		var legend = document.createElement("LEGEND");
		legend.innerHTML = "Cota: ".bold() + " - " + nome;
		
		fieldset.appendChild(legend);
		
		var table = document.createElement("TABLE");
		table.id = "tabelaDetalhesId";
		table.width = "330";
		table.border = "0";
		table.cellspacing = "1";
		table.cellpadding = "1";
		
		fieldset.appendChild(table);
		
		var tbody = document.createElement("TBODY");
		
		table.appendChild(tbody);
		
	 	var cabecalho = document.createElement("TR");
	 	cabecalho.className="header_table";
	 	
	 	var tdDia = document.createElement("TD");
	 	tdDia.width="136";
	 	tdDia.align="left";
	 	tdDia.innerHTML="Dia Vencimento".bold();
	 	cabecalho.appendChild(tdDia);
	 	
	 	var tdValor = document.createElement("TD");
	 	tdValor.width="157";
	 	tdValor.align="right";
	 	tdValor.innerHTML="Valor R$".bold();		 	
	 	cabecalho.appendChild(tdValor);
	 	
	 	tbody.appendChild(cabecalho);
		
		 $(dividas).each(function (index, divida) {
			 
			 var linha = document.createElement("TR");
			 
			 var lin = (index%2==0) ? 1:2;
			 
			 linha.className="class_linha_" + lin ;
	 	 
		 	var cel = document.createElement("TD");
		 	cel.align="left";
		 	text = document.createTextNode(divida.vencimento);
		 	cel.appendChild(text);			 	
		 	linha.appendChild(cel);
		 	
		 	var cel2 = document.createElement("TD");
		 	cel2.align="right";
		 	text2 = document.createTextNode(divida.valor);
		 	cel2.appendChild(text2);			 	
		 	linha.appendChild(cel2);
		 	
		 	tbody.appendChild(linha);
			 
			
		 });		 		
	}
	
	function gerarRelatorio(cotas) {
		
		var table = document.getElementById("tabelaRelatorio");
		table.width = "330";
		table.border = "0";
		table.cellspacing = "1";
		table.cellpadding = "1";
		
		table.innerHTML="";
		
		var tbody = document.createElement("TBODY");
		
		table.appendChild(tbody);
		
	 	var cabecalho = document.createElement("TR");
	 	cabecalho.className="header_table";
	 	
	 	var tdDia = document.createElement("TD");
	 	tdDia.width="136";
	 	tdDia.align="left";
	 	tdDia.innerHTML="Dia Vencimento".bold();
	 	cabecalho.appendChild(tdDia);
	 	
	 	var tdValor = document.createElement("TD");
	 	tdValor.width="157";
	 	tdValor.align="right";
	 	tdValor.innerHTML="Valor R$".bold();		 	
	 	cabecalho.appendChild(tdValor);
	 	
	 	tbody.appendChild(cabecalho);
		
		 $(cotas).each(function (index, cota) {
			 
			 var linha = document.createElement("TR");
			 
			 var lin = (index%2==0) ? 1:2;
			 
			 linha.className="class_linha_" + lin ;
	 	 
		 	var cel = document.createElement("TD");
		 	cel.align="left";
		 	text = document.createTextNode(cota.cota);
		 	cel.appendChild(text);			 	
		 	linha.appendChild(cel);
		 	
		 	var cel2 = document.createElement("TD");
		 	cel2.align="right";
		 	text2 = document.createTextNode(cota.nome);
		 	cel2.appendChild(text2);			 	
		 	linha.appendChild(cel2);
		 	
		 	tbody.appendChild(linha);
			 
			
		 });		 		
	}
	
	function sortGrid(table, order) {
		// Remove all characters in c from s.
		var stripChar = function(s, c) {
			var r = "";
			for ( var i = 0; i < s.length; i++) {
				r += c.indexOf(s.charAt(i)) >= 0 ? "" : s.charAt(i);
			}
			return r;
		}
		// Test for characters accepted in numeric values.
		var isNumeric = function(s) {
			var valid = "0123456789.,- ";
			var result = true;
			var c;
			for ( var i = 0; i < s.length && result; i++) {
				c = s.charAt(i);
				if (valid.indexOf(c) <= -1) {
					result = false;
				}
			}
			return result;
		}
		// Sort table rows.
		var asc = order == "asc";
		var rows = $(table).find("tbody > tr").get();
		var column = $(table).parent(".bDiv").siblings(".hDiv").find("table tr th")
				.index($("th.sorted", ".flexigrid:has(" + table + ")"));
		rows.sort(function(a, b) {
			var keyA = $(asc ? a : b).children("td").eq(column).text()
					.toUpperCase();
			var keyB = $(asc ? b : a).children("td").eq(column).text()
					.toUpperCase();
			if ((isNumeric(keyA) || keyA.length < 1)
					&& (isNumeric(keyB) || keyB.length < 1)) {
				keyA = stripChar(stripChar(keyA, ","), ".");
				keyB = stripChar(stripChar(keyB, ","), ".");
				if (keyA.length < 1)
					keyA = 0;
				if (keyB.length < 1)
					keyB = 0;
				keyA = new Number(parseFloat(keyA));
				keyB = new Number(parseFloat(keyB));
			}
			return keyA > keyB ? 1 : keyA < keyB ? -1 : 0;
		});
		// Rebuild the table body.
		$.each(rows, function(index, row) {
			$(table).children("tbody").append(row);
		});
		// Fix styles
		$(table).find("tr").removeClass("erow"); // Clear the striping.
		$(table).find("tr:odd").addClass("erow"); // Add striping to odd numbered
													// rows.
		$(table).find("td.sorted").removeClass("sorted"); // Clear sortedclass
															// from table cells.
		$(table).find("tr").each(function() {
			$(this).find("td:nth(" + column + ")").addClass("sorted"); // Add sorted class to sorted column cells.
		});
	}
	
</script>
</body>
</html>
