<form action="/administracao/naturezaOperacao" id="dialog-cotas_form">
	<div id="dialog-cotas-produto" title="Cotas" style="display:none;">
		<fieldset style="width:350px!important;">
	    	<legend>Cotas</legend>
	        <table class="lstCotaGrid"></table>
	    </fieldset>
	</div>
</form>

<form action="/administracao/naturezaOperacao" id="formTipoDescontoProduto">

	<div id="dialog-produto" title="Novo Tipo de Desconto Produto" style="display:none;">
	
	<div id="dialog-copiarDescontoCotas" title="C&oacute;pia de descontos" style="display: none;">
		<fieldset style="width: 450px !important;">
			<legend>Cota origem</legend>
			<table>
				<tr>
					<td style="width: auto !important;">
						Cota:
					</td> 
					<td>
						<input name="numCotaOrigem" id="numCotaOrigem" type="text" maxlength="11" style="width: 70px;"
							onchange="descontoProdutoController.pesquisaCota.pesquisarPorNumeroCota('#numCotaOrigem', '#nomeCotaOrigem', true, null, null, null, false);"/>
					</td>
					<td style="width: auto !important; padding-left: 20px;">
						Nome:
					</td>
					<td> 
						<input name="nomeCotaOrigem" id="nomeCotaOrigem" type="text" maxlength="255" style="width: 200px;" 
							onkeyup="descontoProdutoController.pesquisaCota.autoCompletarPorNome('#nomeCotaOrigem', null, false);"
							onblur="descontoProdutoController.pesquisaCota.pesquisarPorNomeCota('#numCotaOrigem', '#nomeCotaOrigem', false, null, null, null, false);" />
					</td>
          		</tr>
			</table>
		</fieldset>
		<fieldset style="width: 450px !important;">
			<legend>Cota destino</legend>
			<table>
				<tr>
					<td style="width: auto !important;">
						Cota:
					</td>
					<td>
						<input name="numCotaDestino" id="numCotaDestino" type="text" maxlength="11" style="width: 70px;"
							onchange="descontoProdutoController.pesquisaCota.pesquisarPorNumeroCota('#numCotaDestino', '#nomeCotaDestino', false, null, null, null, false);" />
					</td>
					<td style="width: auto !important; padding-left: 20px;">
						Nome:
					</td>
					<td>
						<input name="nomeCotaDestino" id="nomeCotaDestino" type="text" maxlength="255" style="width: 200px;" 
							onkeyup="descontoProdutoController.pesquisaCota.autoCompletarPorNome('#nomeCotaDestino', null, false);"
							onblur="descontoProdutoController.pesquisaCota.pesquisarPorNomeCota('#numCotaDestino', '#nomeCotaDestino', false, null, null, null, false);" />
					</td>
          		</tr>
			</table>
		</fieldset>
	</div>

	<jsp:include page="../messagesDialog.jsp" />    

	  <table width="450" border="0" cellpadding="2" cellspacing="1" class="filtro" style="font-size:8pt">
	          <tr>
	            <td width="100">Código:</td>
	            <td width="100">
	            	<input type="text" name="pCodigoProduto" id="pCodigoProduto" maxlength="255" 
						   style="width:100px; float:left; margin-right:5px;"
						   onblur="pesquisaProdutoTipoDescontoCota.pesquisarPorCodigoProduto('#pCodigoProduto', '#pNomeProduto', true, undefined, undefined);"/>
	            </td>
	          </tr>
	          <tr>
	            <td>Produto:</td>
	            <td>
	            	<input type="text" name="pNomeProduto" id="pNomeProduto" maxlength="255" 
										style="width:160px;"
										onkeyup="pesquisaProdutoTipoDescontoCota.autoCompletarPorNomeProduto('#pNomeProduto', false);"
										onblur="pesquisaProdutoTipoDescontoCota.pesquisarPorNomeProduto('#pCodigoProduto', '#pNomeProduto', true,
											undefined,
											undefined);" />
	            </td>
	          </tr>
	          <tr>
	            <td>Edição:</td>
	            <td><input type="checkbox" name="checkbox" id="mostrarEdicao" onclick="descontoProdutoController.mostraEdicao();" /></td>
	          </tr>

	          <tr class="aEdicao" style="display:none;">

		          <td>Edição Específica:</td>
		          <td>
		          <input 	type="text" 
		          			name="edicaoProduto" 
		          			id="edicaoProduto" 
		          			style="width:60px;"
		          			onchange="pesquisaProdutoTipoDescontoCota.validarNumEdicao('#pCodigoProduto', '#edicaoProduto', true);"
		          			onfocus="pesquisaProdutoTipoDescontoCota.validarCamposEdicoes(this.id);"
		          			onkeyup="pesquisaProdutoTipoDescontoCota.validarCamposEdicoes(this.id);"
		          />
		          ou por
		          <input 	type="text" name="quantidadeEdicoes" id="quantidadeEdicoes" style="width:60px;"
		          			onfocus="pesquisaProdutoTipoDescontoCota.validarCamposEdicoes(this.id);"
		          			onkeyup="pesquisaProdutoTipoDescontoCota.validarCamposEdicoes(this.id);"
		          />
		          Edições</td>

			  </tr>
			  <tr>
	            <td>Desconto %:</td>
	            <td><input type="text" name="descontoProduto" id="descontoProduto" style="width:100px;"/></td>
	          </tr>
	          <tr>
	            <td>Cotas:</td>
	            <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
	              <tr>
	                <td width="10%"><input type="radio" name="cotas" id="radioTodasCotas" value="radio" onchange="descontoProdutoController.esconderGridCota();" checked/></td>
	                <td width="29%">Todas</td>
	                <td width="8%"><input type="radio" name="cotas" id="radioCotasEspecificas" value="radio" onchange="descontoProdutoController.mostrarGridCota();" /></td>
	                <td width="53%">Específica</td>
	              </tr>
	            </table></td>
	          </tr>
	          <tr>
	            <td colspan="3"><strong>Este desconto predomina sobre os demais (Geral / Editor / Específico)?</strong>
					<input type="checkbox" name="descontoPredominante" id="descontoPredominante" /></td>
	          </tr>
	    </table>       

		<div id="trRegiao" style="display: none;">
			<table width="450" border="0" cellpadding="2" cellspacing="1" class="filtro" style="font-size:8pt">
				<tr>
					<td width="48">Região:</td>
					<td width="450">
						<select name="comboRegioes" id="comboRegioesCotaDesconto" style="width: 200px;"
									onchange="descontoProdutoController.pesquisarCotasRegiao();">
							<option selected="selected">Selecione...</option>
							<c:forEach items="${listaRegiao}" var="regiao">
								<option value="${regiao.key}" label="${regiao.value}" />
							</c:forEach>
						</select>
					</td>
				  </tr>
			  </table>
		</div>

		<div id="fieldCota" class="especificaCota" style="display:none;">

			<fieldset style="width:395px!important;">
				<legend>Cotas</legend>
				<div style="overflow: auto; height: 240px;">
	    			<table border="0" cellspacing="1" cellpadding="1" class="especificaCota" id="gridCotas" style="display:none;width:100%" >

						<tr class="header_table">
			                <td width="34%">Cota</td>
			                <td width="66%">Nome</td>
						</tr>
						<tr id="trCota1">
							<td>
								<input type="text" name="cotaInput" id="cotaInput1" style="width:120px;" maxlength="255"
									onblur="pesquisaCotaTipoDescontoCota.pesquisarPorNumeroCota('#cotaInput1', '#nomeInput1', true);"/>
							</td>
							<td>
								<input type="text" name="nomeInput" id="nomeInput1" style="width:245px;" maxlength="255"
									onkeyup="pesquisaCotaTipoDescontoCota.autoCompletarPorNome('#nomeInput1');" 
									onblur="pesquisaCotaTipoDescontoCota.pesquisarPorNomeCota('#cotaInput1', '#nomeInput1',descontoProdutoController.adicionarLinhaCota(1));"/>
							</td>
						</tr>
					</table>
				</div>
			</fieldset>
		</div>  
			
	</div>
</form>