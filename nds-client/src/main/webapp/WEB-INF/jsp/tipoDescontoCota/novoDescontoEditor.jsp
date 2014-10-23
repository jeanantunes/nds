<form action="" id="dialog-cotas_form">
	<div id="dialog-cotas" title="Cotas" style="display:none;">
		<fieldset style="width:350px!important;">
	    	<legend>Cotas</legend>
	        <table class="lstCotaGrid"></table>
	    </fieldset>
	</div>
</form>

<form action="" id="formTipoDescontoProduto">

	<div id="dialog-editor" title="Novo Tipo de Desconto Editor" style="display:none;">

	<jsp:include page="../messagesDialog.jsp" />    

	  <table width="394" border="0" cellpadding="2" cellspacing="1" class="filtro" style="font-size:8pt">
	          <tr>
	            <td width="100">Editor:</td>
	            <td width="100">
	            	<input name="codigoEditor" 
           		   id="codigoEditor" 
           		   type="text"
           		   maxlength="11"
           		   style="width:70px;float:left;"
           		   onchange="pesquisaEditorTipoDescontoCota.pesquisarPorCodigoEditor('#codigoEditor', '#descricaoEditor',true,
           	  											descontoEditorController.pesquisarEditorSuccessCallBack, 
           	  											descontoEditorController.pesquisarEditorErrorCallBack);" />
	            </td>
	          </tr>
	          <tr>
	            <td>Nome:</td>
	            <td>
	            	<input  name="descricaoEditor" 
			      		 id="descricaoEditor" 
			      		 type="text" 
			      		 class="nome_jornaleiro" 
			      		 maxlength="255"
			      		 style="width:200px;float:left;"
			      		 onkeyup="pesquisaEditorTipoDescontoCota.autoCompletarPorNome('#descricaoEditor');" 
			      		 onblur="pesquisaEditorTipoDescontoCota.pesquisarPorNomeEditor('#codigoEditor', '#descricaoEditor', false,
				      			null,
				      			null);" />
	            </td>
	          </tr>

			  <tr>
	            <td>Desconto %:</td>
	            <td><input type="text" name="descontoEditor" id="descontoEditor" style="width:100px;"/></td>
	          </tr>
	          <tr>
	            <td>Cotas:</td>
	            <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
	              <tr>
	                <td width="10%"><input type="radio" name="radioCotasEditor" id="radioEditorTodasCotas" value="radio" onchange="descontoEditorController.esconderGridCota();" /></td>
	                <td width="29%">Todas</td>
	                <td width="8%"><input type="radio" name="radioCotasEditor" id="radioEditorCotasEspecificas" value="radio" onchange="descontoEditorController.mostrarGridCota();" /></td>
	                <td width="53%">Específica</td>
	              </tr>
	            </table></td>
	          </tr>
	    </table>       


			<div id="fieldCota" class="especificaCota" style="display:none;">

				<fieldset style="width:395px!important;">
					<legend>Cotas</legend>
					<div style="overflow: auto; height: 240px;">
		    			<table border="0" cellspacing="1" cellpadding="1" class="especificaCota" id="gridCotasEditor" style="display:none;width:100%" >

							<tr class="header_table">
				                <td width="34%">Cota</td>
				                <td width="66%">Nome</td>
							</tr>
							<tr class="trCotasEditor" id="trCotaEditor1">
								<td>
									<input type="text" name="cotaEditorInput" id="cotaEditorInput1" style="width:120px;" maxlength="255"
										onblur="pesquisaCotaTipoDescontoCota.pesquisarPorNumeroCota('#cotaEditorInput1', '#nomeCotaEditorInput1', true);"/>
								</td>
								<td>
									<input type="text" name="nomeCotaEditorInput" id="nomeCotaEditorInput1" style="width:245px;" maxlength="255"
										onkeyup="pesquisaCotaTipoDescontoCota.autoCompletarPorNome('#nomeCotaEditorInput1');" 
										onblur="pesquisaCotaTipoDescontoCota.pesquisarPorNomeCota('#cotaEditorInput1', '#nomeCotaEditorInput1', descontoEditorController.adicionarLinhaCota(1));"/>
								</td>
							</tr>
						</table>
					</div>
				</fieldset>
			</div>  

	</div>
</form>
