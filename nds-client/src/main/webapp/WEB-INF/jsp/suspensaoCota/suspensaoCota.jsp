<head>

<script language="javascript" type="text/javascript">
	
	function getDetalhes(idCota) {
		
		$.postJSON("<c:url value='/suspensaoCota/getInadinplenciasDaCota'/>", 
				"idCota="+idCota+"&method='get'", 
				popupDetalhes);	
	};
	
	function popupDetalhes(result) {
				
		gerarTabelaDetalhes(result);
		
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:'auto',
			width:360,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					
				},
			}
		});
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
		
		gerarRelatorio(result);
		
		table.innerHTML="";
		
		$( "#divRelatorio" ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Fechar": function() {
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
	
	function gerarRelatorio() {
		
		var div = document.getElementById('divRelatorio')
		
		div.innerHTML="GUILHERME";
		
	}
	
	
 
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
	<p><strong>Proceder com a devolução de Documentação</strong></p>
     
</div>

<div id="dialog-excluir" title="Suspensão da Cota">
	<p>Confirma a exclusão desta suspensão?</p>
  
</div>

<div id="dialog-suspender" title="Suspensão da Cota">
	<p><strong>Confirma Suspensão da Cota?</strong></p>
</div>





<div id="dialog-detalhes" title="Suspensão de Cota">
     
    <table id="tabelaDetalhesId" width="300" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td colspan="2" align="left"><strong>Cota:&nbsp;</strong>9999 - José da Silva Pereira</td>
  </tr>
  <tr>
    <td width="136" align="left"><strong>Dia Vencimento</strong></td>
    <td width="157" align="right"><strong>Valor R$</strong></td>
  </tr>
  <tr class="">
    <td align="left">99/99/9999</td>
    <td align="right">120,00</td>
  </tr>
  <tr>
    <td align="left">99/99/9999</td>
    <td align="right">125,00</td>
  </tr>
  <tr>
    <td align="left">99/99/9999</td>
    <td align="right">87,00</td>
  </tr>
</table>


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
                	3
                </td>
                
                <td width="7%">
                	<strong>
                		Total R$:
                	</strong>
                </td>
                
                <td width="17%" id="total">
                	
                	10.567,00
                </td>
                
                <td width="17%">
                
<!-- SELECIONAR TODOS -->	                
	                <span class="bt_sellAll">
	                	<label for="sel">Selecionar Todos</label>
	                	<input type="checkbox" id="sel" name="Todos" onclick="checkAll();" style="float:left;"/>
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
			colModel : [  {
				display : 'Cota',
				name : 'cota',
				width : 55,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 320,
				sortable : true,
				align : 'left'
			},{
				display : 'Valor Consignado Total R$',
				name : 'vlrConsignado',
				width : 200,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor Reparte do Dia R$',
				name : 'vlrReparte',
				width : 200,
				sortable : true,
				align : 'right'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 70,
				sortable : true,
				align : 'center',
			},{
				display : '  ',
				name : 'selecionado',
				width : 20,
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
			height : 260
		})); 	
		
		$(".flexigrid").css("zIndex",-10);
	});
	
	function processaRetornoPesquisa(data) {
		
		var grid = data[0];
		var mensagens = data[1];
		var status = data[2];
		var totalSugerida = data[3];
		var total = data[4];
		
		
		if(mensagens!=null && mensagens.length!=0) {
			exibirMensagem(status,mensagens);
		}
				
		if(!grid.rows || status=="error") {
			return grid;
		} else {
			$(".corpo").show();	
		}
		
		document.getElementById("total").innerText = total;
		document.getElementById("totalSugerida").innerText = totalSugerida;		
		
		for(var i=0; i<grid.rows.length; i++) {			
			
			var cell = grid.rows[i].cell;
						
			cell.acao = gerarAcoes(cell.cota,cell.dividas);
			cell.selecionado = gerarCheckbox('idCheck'+i,'selecao', cell.cota,cell.selecionado);;					
		}
		
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
	
	function gerarAcoes(idCota,dividas) {
		
		var a = document.createElement("A");
		a.href = "javascript:;";
		a.setAttribute("onclick","getDetalhes("+idCota+");");
		
		var img =document.createElement("IMG");
		img.src="${pageContext.request.contextPath}/images/ico_detalhes.png";
		img.border="0";
		img.hspace="5";
		img.title="Detalhes";		
		a.innerHTML = img.outerHTML;
		
		var a2 = document.createElement("A");
		a2.href = "javascript:;";
		a2.setAttribute("onclick","popup_excluir();");
		
		var img2 =document.createElement("IMG");
		img2.src="${pageContext.request.contextPath}/images/ico_excluir.gif";
		img2.border="0";
		img2.hspace="5";
		img2.title="Excluir da Suspensão";		
		a2.innerHTML = img2.outerHTML;
		
		return a.outerHTML + a2.outerHTML;
	}
	
	function gerarTabelaDetalhes(dividas) {
				
		var table = document.getElementById('tabelaDetalhesId')
		
		table.innerHTML="";
	
		var linhaCota = document.createElement("TR");
	 	 
	 	var tdCota = document.createElement("TD");
	 	tdCota.setAttribute("colspan","2");
	 	tdCota.align="left";
	 	tdCota.innerHTML="Cota: ".bold() + " - Guilherme de Morais Leandro";
	 	 	
	 	linhaCota.appendChild(tdCota);
	 	
	 	table.appendChild(linhaCota);
	 	
	 	 var cabecalho = document.createElement("TR");
	 	 
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
		 	
		 	table.appendChild(cabecalho);
		
		 $(dividas).each(function (index, divida) {
			 
			 var linha = document.createElement("TR");
		 	 
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
			 	
			 	table.appendChild(linha);
			 
			
		 });		 		
	}
	
</script>
</body>
</html>
