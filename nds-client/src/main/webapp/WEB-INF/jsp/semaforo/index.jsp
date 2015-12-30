<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/semaforo.js"></script>
	
	<script type="text/javascript">
		$(function() {
			semaforoController.init();
		});
	</script>
</head>

<body>

	<form action="${pageContext.request.contextPath}/devolucao/semaforo" id="statusProcessosEncalheForm" method="post">
		<div class="grids">
			<fieldset class="classFieldset">
				<legend>Processos de Encalhe</legend>
				<div id="tabPainel">
	                <div id="tabPainel-Interfaces">
		            	<table width="900" border="0" align="right" cellpadding="2" cellspacing="1" >
							<tr>
							     <td id="totalId"  width="300px">Processamento ..  </td>
		                    	 <td  width="90px"  id="idrefresh" style="height:10px;padding:2px; border:2px outset #aaa;background:#ccc " title="Ativar/Desativar refresh automatico (30s)"  
		                    	 onclick="semaforoController.stopRefresh=!semaforoController.stopRefresh;
			                    	 if (semaforoController.stopRefresh){ $('#idrefresh', this.workspace).html('Ativar Refresh');} else {
			                    		 $('#idrefresh', this.workspace).html('Desativar Refresh');	semaforoController.obterStatusProcessosEncalhe();}semaforoController.pollStatusProcessosEncalhe(); " 
			                    	 >Ativar Refresh</td>
							 
		                    	<td width="17"><img src="${pageContext.request.contextPath}/images/ico_operando.png" alt="Operando" /></td>
		                        <td width="143">Finalizado com sucesso</td>
		                        <td width="17"><img src="${pageContext.request.contextPath}/images/ico_semdados.png" alt="Off-line" /></td>
		                        <td width="143">Processo em andamento</td>
		                        <td width="17"><img src="${pageContext.request.contextPath}/images/ico_encerrado.png" alt="Off-line" /></td>
		                    	<td width="123">Erro no processamento</td>
		                	</tr>
		               	</table>
		                <br clear="all"/>
		            </div>  
	            </div>
	       	  	<table class="statusProcessosEncalheGrid"></table>
			</fieldset>
		</div>
	</form>
</body>