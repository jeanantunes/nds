<head>

<script language="javascript" type="text/javascript">
	
	function popup_detalhes() {
		
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
	};
	
	function popup_suspender() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-suspender" ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					popup_confirm();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	
	function popup_excluir() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					/*popup_chamadao();*/
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function popup_confirm() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-confirm" ).dialog({
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
	
  //callback function to bring a hidden box back
		function callback() {
			setTimeout(function() {
				$( "#effect:visible").removeAttr( "style" ).fadeOut();

			}, 1000 );
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

	<style type="text/css">
		  #effect { display: none; margin: 0px; width:980px;  position: absolute; }
		  #effect_1 { display: none; margin: 0px; width:800px;  position: absolute; }
		  #dialog-detalhes, #dialog-alterar, #dialog-excluir, #dialog-confirm{display:none; font-size:12px;}
 	 </style>
</head>

<body>
<form action="" method="get" id="form1" name="form1">
<div id="dialog-confirm" title="Cota Suspensa">
	<p><strong>Proceder com a devolução de Documentação</strong></p>
     
</div>

<div id="dialog-excluir" title="Suspensão da Cota">
	<p>Confirma a exclusão desta suspensão?</p>
  
</div>

<div id="dialog-suspender" title="Suspensão da Cota">
	<p><strong>Confirma Suspensão da Cota?</strong></p>
</div>





<div id="dialog-detalhes" title="Suspensão de Cota">
     
    <table width="300" border="0" cellspacing="1" cellpadding="1">
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








<div class="corpo">
    
   
    <div class="container">
      
      
       <fieldset class="classFieldset">
       	  <legend>Suspender Cotas</legend>
        <div class="grids" style="display:Block;">
			<table class="suspensaoGrid"></table>
          	<table width="100%" border="0" cellspacing="2" cellpadding="2">
              <tr>
                <td width="36%">
                	 
                     <span class="bt_novos" title="Suspender Cota">
<!-- SUSPENDER COTAS -->
                     	<a href="javascript:;" onclick="popup_suspender();">
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
                
                <td width="5%">
                	3
                </td>
                
                <td width="7%">
                	<strong>
                		Total R$:
                	</strong>
                </td>
                
                <td width="17%">
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
		
		$(".grids").show();	
		
	});
	
	function processaRetornoPesquisa(data) {
		
		var grid = data[0];
		var mensagens = data[1];
		var status = data[2];
		
		if(mensagens!=null && mensagens.length!=0) {
			exibirMensagem(status,mensagens);
		}
		
		if(!grid.rows) {
			return grid;
		}
		
		for(var i=0; i<grid.rows.length; i++) {			
			
			var cell = grid.rows[i].cell;
						
			cell.acao = gerarAcoes(cell.cota,cell.dividas);
			cell.selecionado = gerarCheckbox('idCheck'+i,'selecao', cell.cota,cell.selecionado);;
					
		/*	if(cell.estudo) {
				cell.selecionado = gerarCheckbox('idCheck'+i,'selecao', cell.idLancamento,cell.selecionado);
			} else {
				cell.estudo="";
				cell.selecionado="";
			}
		*/
		}
		
		return grid;
	}
	
	function gerarCheckbox(id,name,idLancamento,selecionado) {
		
		var input = document.createElement("INPUT");
		input.id=id;
		input.name=name;
		input.style.setProperty("float","left");
		input.type="checkbox";
		input.setAttribute("onclick","adicionarSelecao(idLancamento,this);");
		
		if(selecionado==true) {
			input.checker="checked";
		}
		
		return input.outerHTML; 
	}
	
	function gerarAcoes(idCota,dividas) {
		
		var a = document.createElement("A");
		a.href = "javascript:;";
		a.setAttribute("onclick","popup_detalhes();");
		
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
		
		var table = documento.createElement("TABLE");
		table.width="300";
		table.border="0";
		table.cellspacing="1";
		table.cellpadding="1";
		//TODO - esconder
		table.style="display:none;"
		
		var linhaCota = document.createElement("TR");
	 	 
	 	var tdCota = document.createElement("TD");
	 	tdCota.colspan="2";
	 	tdCota.align="left";
	 	textoLinhaCota = document.createTextNode("Cota: ".bold() + " - Guilherme de Morais Leandro");
	 	tdCota.appendChild(textoLinhaCota);
	 	
	 	linhaCota.appendChild(tdCota);
	 	
	 	table.appendChild(linhaCota);
	 	
	 	 var cabecalho = document.createElement("TR");
	 	 
		 	var tdDia = document.createElement("TD");
		 	tdDia.width="136";
		 	tdDia.align="left";
		 	textoDia = document.createTextNode("Dia Vencimento".bold());
		 	tdDia.appendChild(textoDia);			 	
		 	cabecalho.appendChild(tdDia);
		 	
		 	var tdValor = document.createElement("TD");
		 	tdValor.width="157";
		 	tdValor.align="rigth";
		 	textoValor = document.createTextNode("Valor R$".bold());
		 	tdValor.appendChild(textoValor);			 	
		 	cabecalho.appendChild(tdValor);
		 	
		 	table.appendChild(cabecalho);
		
		 $(dividas).each(function (index, domEle) {
			 
			 var linha = document.createElement("TR");
		 	 
			 	var cel = document.createElement("TD");
			 	cel.align="left";
			 	text = document.createTextNode("99/99/9999");
			 	cel.appendChild(text);			 	
			 	linha.appendChild(cel);
			 	
			 	var cel2 = document.createElement("TD");
			 	cel2.align="rigth";
			 	text2 = document.createTextNode("125,00");
			 	cel2.appendChild(text2);			 	
			 	linha.appendChild(cel2);
			 	
			 	table.appendChild(linha);
			 
			
		 });
		
	}
	
</script>
</body>
</html>
