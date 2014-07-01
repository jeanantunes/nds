<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>

<script type="text/javascript" src="scripts/suspensaoCota.js"></script>

<script type="text/javascript">
$(function(){
	suspensaoCotaController.init();
	bloquearItensEdicao(suspensaoCotaController.workspace);
});
</script>

</head>

<body>
<form action="" method="get" id="form1" name="form1">

<div class="areaBts">
		<div class="area">
			
			<span class="bt_novos" title="Suspender Cota">
                   	<a isEdicao="true" href="javascript:;" onclick="suspensaoCotaController.popupConfirmar();">
                   		<img src="${pageContext.request.contextPath}/images/ico_suspender.gif" hspace="5" border="0"/>
                   	</a>
                   	
                   </span>
                   <span class="bt_novos" title="Gerar Arquivo">
                    <a href="${pageContext.request.contextPath}/suspensaoCota/exportar?fileType=XLS">
                    	<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
                    </a>
                   </span>

			<span class="bt_novos" title="Imprimir">
				<a href="${pageContext.request.contextPath}/suspensaoCota/exportar?fileType=PDF">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>
		</div>
	</div>
	
	<div class="linha_separa_fields">&nbsp;</div>

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

<div id="dialog-nao-selecionada"  style="display: none" title="Suspensão da Cota">
	<p><strong>Nenhuma Cota foi selecionada.</strong></p>
</div>

<div id="dialog-detalhes" title="Suspensão de Cota">     
    
  </div>


<div class="corpo" style="display:none;">
    
   
    <div class="container">
      
      
       <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
       	  <legend>Suspender Cotas</legend>
        <div class="grids" style="display:block;">
			<table class="suspensaoGrid"></table>
          	<table width="100%" border="0" cellspacing="2" cellpadding="2">
              <tr>
                <td width="36%">
                	
                </td>
                
                <td width="18%">
                	<strong>
                		&nbsp;Total de Cotas Sugeridas:
                	</strong>
                </td>
                
                <td width="5%" >
                	<div id="totalSugerida"></div>                	
                </td>
                
                <td width="7%">
                	<strong>
                		Total R$:
                	</strong>
                </td>
                
                <td width="17%">                	
                	<div id="total"></div>
                </td>
                
                <td width="17%">
                
<!-- SELECIONAR TODOS -->	                
	                <span class="bt_sellAll">
	                	<label for="sel">Selecionar Todos</label>
	                	<input isEdicao="true" type="checkbox" id="sel" name="Todos" onclick="suspensaoCotaController.selecionarTodos(this)" style="float:left;"/>
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
</body>
</html>
