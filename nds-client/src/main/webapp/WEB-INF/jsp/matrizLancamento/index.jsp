
<head>

<style>
.linkDisabled {
	cursor: default;
	opacity: 0.4;
}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/balanceamento.js"></script>

<script type="text/javascript">


var pathTela = "${pageContext.request.contextPath}";

var B = new Balanceamento(pathTela, "B");

var lancamentosSelecionados = [];


$(function() {
	
	B.definirAcaoPesquisaTeclaEnter();
	
	$("#lancamentosProgramadosGrid").flexigrid({
		colModel : [  {
			display : 'Código',
			name : 'codigoProduto',
			width : 45,
			sortable : true,
			align : 'center'
		}, {
			display : 'Produto',
			name : 'nomeProduto',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'numeroEdicao',
			width : 35,
			sortable : true,
			align : 'center'
		}, {
			display : 'Preço Capa R$',
			name : 'precoVenda',
			width : 78,
			sortable : true,
			align : 'right'
		}, {
			display : 'Reparte',
			name : 'repartePrevisto',
			width : 43,
			sortable : true,
			align : 'center'
		}, {
			display : 'Lançamento',
			name : 'descricaoLancamento',
			width : 68,
			sortable : true,
			align : 'left'
		}, {
			display : 'Recolhimento',
			name : 'dataRecolhimentoPrevista',
			width : 76,
			sortable : true,
			align : 'center'
		},{
			display : 'Total R$',
			name : 'valorTotal',
			width : 42,
			sortable : true,
			align : 'right'
		}, {
			display : 'Físico',
			name : 'reparteFisico',
			width : 40,
			sortable : true,
			align : 'center'
		}, {
			display : 'Distribuição',
			name : 'distribuicao',
			width : 65,
			sortable : true,
			align : 'center'
		}, {
			display : 'Previsto',
			name : 'dataLancamentoPrevista',
			width : 60,
			sortable : true,
			align : 'center'
		}, {
			display : 'Matriz/Distrib.',
			name : 'novaData',
			width : 107,
			sortable : false,
			align : 'center'
		},{
			display : 'Reprogramar',
			name : 'reprogramar',
			width : 65,
			sortable : false,
			align : 'center'
		}],
		sortname : "codigoProduto",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 180,
		disableSelect : true
		});
	
});


function popup_reprogramar() {
	$( "#dialog-reprogramar" ).dialog({
		resizable: false,
		height:160,
		width:320,
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
	
$(function() {
	$( "#datepickerDe" ).datepicker({
		showOn: "button",
		dateFormat: 'dd/mm/yy',
		buttonImage: "<c:url value='scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif'/>",
		buttonImageOnly: true
	});
	$( "#datepickerDe_1" ).datepicker({
		showOn: "button",
		dateFormat: 'dd/mm/yy',
		buttonImage: "<c:url value='scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif'/>",
		buttonImageOnly: true
	});
	
	$("#datepickerDe").mask("99/99/9999");
	$("#datepickerDe_1").mask("99/99/9999");
	
	
	$( "#novaDataLancamento" ).datepicker({
		showOn: "button",
		dateFormat: 'dd/mm/yy',
		buttonImage: "<c:url value='scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif'/>",
		buttonImageOnly: true
	});
	
	$("#novaDataLancamento").mask("99/99/9999");
	
	
});

function reprogramarSelecionados() {
	
	$("#dialogReprogramarBalanceamento").dialog({
		resizable: false,
		height:'auto',
		width:"300px",
		modal: true,
		buttons: [
		    {
		    	id: "dialogReprogramarBtnConfirmar",
		    	text: "Confirmar",
		    	click: function() {
				
		    		B.reprogramarLancamentosSelecionados();
		    	}
		    },
		    {
		    	id: "dialogReprogramarBtnCancelar",
		    	text: "Cancelar",
		    	click: function() {
		    
		    		$(this).dialog("close");
		    	}
			}
		],
		beforeClose: function() {
			
			$("#novaDataLancamento").val("");
			
			clearMessageDialogTimeout();
		}
	});
}


</script>

<style>

.ui-datepicker { z-index: 1000 !important; }
.ui-datepicker-today a { display:block !important; }
.dialog-detalhe-produto { display:none; }
.dialog-confirm-balanceamento { display:none; }

.gridLinhaDestacada {
  background:#F00; 
  font-weight:bold; 
  color:#fff;
}

.gridLinhaDestacada:hover {
   color:#000;
}

.gridLinhaDestacada a {
   color:#fff;
}

.gridLinhaDestacada a:hover {
   color:#000;
}

</style>

</head>

<body>


<div id="dialog-confirm" title="Balanceamento da Matriz de Lançamento">
			
			<jsp:include page="../messagesDialog.jsp" />
			
			<p>Existem lançamentos não confirmados. Ao prosseguir com essa ação você perderá os dados. Deseja prosseguir?</p>
			   
</div>

<div id="dialogVoltarConfiguracaoOriginal" title="Balanceamento da Matriz de Lançamento" style="display:none">
			
			<p>Ao voltar a configuração original, você perdará os dados confirmados. Deseja prosseguir?</p>
			   
</div>

<div id="dialog-pagincao-confirmada" title="Balanceamento da Matriz de Lançamento" style="display:none">
			
			<p>As seleções de lançamentos não serão salvas, deseja continuar?</p>
			   
</div>

	
		<div id="dialogReprogramarBalanceamento" title="Reprogramar Lançamentos">
		    
		    <jsp:include page="../messagesDialog.jsp" />

		    <p>
			    <strong>Nova Data:</strong>
			    <input name="novaDataLancamento" type="text"
			    	   style="width:80px;" id="novaDataLancamento" />
		    </p>
		</div>


	<form action="" method="get" id="form1" name="form1">
	
		<div id="dialog-reprogramar" title="Reprogramar Lançamento">
			<p><strong>Nova Data Matriz/Distrib:</strong> 
		      <input name="datepickerDe_1" type="text" style="width:80px;" id="datepickerDe_1" />
		    </p>
		</div>
		
		<div id="dialog-novo" title="Consulta de Lançamentos Programados">
		     <fieldset style="width:365px;">
		     	<legend>988989 - Nome do Fornecedor</legend>
		        <table class="lancamentoProgFornecedorGrid"></table>
		     </fieldset>
		</div>
		
		<div class="corpo">
		   
		     <div class="container">	
		   
		   	<jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialog-novo" name="messageDialog"/>
			</jsp:include>
		     
		   
		      <fieldset class="classFieldset">
		   	    <legend>Pesquisar Balanceamento da Matriz de Lançamento
		        </legend>
		   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		   	      <tr>
		   	        <td width="68">Fornecedor:</td>
		   	        <td width="228">
		            <a href="#" id="selFornecedor" onclick="return false;">Clique e Selecione o Fornecedor</a>
		              <div class="menu_fornecedor" style="display:none;">
		                	<span class="bt_sellAll">

<input type="checkbox" id="selTodos1" name="selTodos1" onclick="checkAll(this, 'checkgroup_menu');" style="float:left;"/>

							<label for="selTodos1">Selecionar Todos</label></span>
		                    <br clear="all" />
		                    <c:forEach items="${fornecedores}" var="fornecedor">
		                      <input id="fornecedor_${fornecedor.id}" value="${fornecedor.id}"  name="checkgroup_menu" onclick="verifyCheck($('#selTodos1'));" type="checkbox"/>
		                      <label for="fornecedor_${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</label>
		                      <br clear="all" />
		                   </c:forEach> 
		              </div>
		            
		            </td>
		   	        <td colspan="3">Data de Lançamento Matriz/Distribuidor:</td>
		   	        <td width="109"><input class="campoDePesquisa" type="text" name="datepickerDe" id="datepickerDe" style="width:80px;" value="${data}" /></td>
		   	        <td width="47" align="center">&nbsp;</td>
		   	        <td width="112">&nbsp;</td>
		   	        <td width="104"><span class="bt_pesquisar" title="Pesquisar">
		   	        
<!-- Pesquisar -->
<a class="botaoPesquisar" id="linkPesquisar" href="javascript:;" onclick="B.verificarBalanceamentosAlterados(B.pesquisar);">Pesquisar</a></span></td>



		          </tr>
		        </table>
		      </fieldset>
		          <div class="linha_separa_fields">&nbsp;</div>
		      <fieldset class="classFieldset">
		       	  <legend>Balanceamento da Matriz de Lançamento Cadastrados</legend>
		        <div class="grids" style="display:none;">
		        <span class="bt_configura_inicial" title="Voltar Configuração Inicial">
		        
		        
		              
<!-- Voltar Configuração Inicial -->
<a id="linkVoltarConfiguracaoInicial" href="javascript:;" onclick="B.abrirAlertaVoltarConfiguracaoInicial();"><img src="<c:url value='images/bt_devolucao.png'/>" title="Voltar Configuração Inicial" border="0" hspace="5" />Voltar Configuração Inicial</a></span>

		
		           <br clear="all" />
		       	   <table id="lancamentosProgramadosGrid" class="lancamentosProgramadosGrid"></table>
		          
		            
		                <span class="bt_novos" title="Gerar Arquivo">
							<!-- ARQUIVO -->
							<a id="linkArquivo" href="${pageContext.request.contextPath}/matrizLancamento/exportar?fileType=XLS">
							    <img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
							    Arquivo
						    </a>
						</span>
		            
					
						<span class="bt_novos" title="Imprimir">
							<!-- IMPRIMIR -->	
							<a id="linkImprimir" href="${pageContext.request.contextPath}/matrizLancamento/exportar?fileType=PDF">
							    <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
							    Imprimir
						    </a>
						</span>
		             
		                <span class="bt_novos" title="Reprogramar">
		                    
		                    
<!-- Reprogramar -->              
<a id="linkReprogramar" href="javascript:;" onclick="reprogramarSelecionados();"><img src="<c:url value='images/ico_reprogramar.gif'/>"  hspace="5" border="0" />Reprogramar</a>

		                    
		                    
		                </span>
		                
		                <span class="bt_novos" style="border-width: 2px; border-color: #00CD00;" title="Confirmar">
		                    <!-- CONFIRMAR -->	
		                    <a id="linkConfirmar" href="javascript:;" onclick="B.obterConfirmacaoBalanceamento();">
		                        <img src="<c:url value='images/ico_check.gif'/>"  hspace="5" border="0" />
		                        Confirmar
		                    </a>
		                </span>
		         	  
		         	  <div style="margin-top:15px; margin-left:30px; float:left;"><strong>Valor Total R$: <span id="valorTotal"></span></strong></div>
		          
		              <span class="bt_sellAll" style="float:right; margin-right:60px;"><label for="selTodos">Selecionar Todos</label><input type="checkbox" id="selTodos" name="Todos" onclick="B.checkUncheckLancamentos()"/></span>
		        </div>
		      </fieldset>
		      <div class="linha_separa_fields">&nbsp;</div>      
		      <fieldset class="classFieldset" id="resumoPeriodo"; style="display:none;" >
		      	<legend>Resumo do Período</legend>
		        <div style="width: 950px; overflow-x: auto;">
		        <table width="100%" border="0" cellspacing="2" cellpadding="2" id="tableResumoPeriodo">
		        </table>
		        </div>
		      </fieldset>
		    </div>
		</div>
		
        <div id="dialog-detalhe-produto" title="Detalhes do Produto" style="display:none;">
		    <jsp:include page="../produtoEdicao/detalheProduto.jsp" />
        </div>

		<div id="dialog-confirm-balanceamento" title="Balanceamento" style="display:none;">
		    
		    <jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialog-confirmar" name="messageDialog"/>
			</jsp:include>
			
		    <fieldset style="width:250px!important;">
		    	<legend>Confirmar Balanceamento</legend>

		        <table width="240" border="0" cellspacing="1" cellpadding="1" id="tableConfirmaBalanceamento">
		        </table>

		    </fieldset>
		</div>
	
	</form>

</body>
