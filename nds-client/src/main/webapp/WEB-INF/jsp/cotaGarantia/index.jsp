<script language="text/javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/cotaGarantia.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

<script language="text/javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script language="text/javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/confirmDialog.js"></script>
<script type="text/javascript">
	var tipoCotaGarantia = new TipoCotaGarantia();
		
</script>


<table width="755" cellpadding="2" cellspacing="2"
	style="text-align: left;">
	<tr>
		<td width="112">Tipo de Garantia:</td>
		<td width="631"><select id="tipoGarantiaSelect"
			onchange="tipoCotaGarantia.onChange($(this).val());"
			style="width: 250px;">
				<option value="" selected="selected">Selecione...</option>
		</select></td>
	</tr>
</table>

<div id="cotaGarantiaNotaPromissoriaPanel" style="display: none;">
	<fieldset>
		<legend>Nota Promiss&oacute;ria</legend>

		<table width="755" cellpadding="2" cellspacing="2"
			style="text-align: left;">
			<tr>
				<td width="128">N&#176; Nota Promiss&oacute;ria:</td>
				<td width="219"><span id="cotaGarantiaNotaPromissoriaId"></span></td>
				<td width="78">Vencimento:</td>
				<td width="302"><input type="text"
					name="cotaGarantiaNotaPromissoriaVencimento"
					id="cotaGarantiaNotaPromissoriaVencimento" style="width: 120px;" /></td>

			</tr>
			<tr>
				<td>Valor R&#36;</td>

				<td><input type="text" name="cotaGarantiaNotaPromissoriaValor"
					id="cotaGarantiaNotaPromissoriaValor"
					style="width: 120px; text-align: right;" /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>por extenso:</td>
				<td colspan="3"><input type="text"
					name="cotaGarantiaNotaPromissoriavalorExtenso"
					id="cotaGarantiaNotaPromissoriavalorExtenso" style="width: 425px;" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td colspan="3"><span class="bt_novos" title="Imprimir"><a
						href="javascript:;"
						id="cotaGarantiaNotaPromissoriaImprimir" target="_blank"><img
							src="${pageContext.request.contextPath}/images/ico_impressora.gif"
							alt="Imprimir" hspace="5" border="0" />Imprimir</a></span></td>
			</tr>
		</table>
	</fieldset>
</div>

<div id="cotaGarantiaFiadorPanel" style="display: none;">

	<fieldset>

		<legend>Fiador</legend>

		<table width="755" cellpadding="2" cellspacing="2"
			style="text-align: left;">
			<tr>
				<td width="150"><strong>Nome / Raz&atilde;o Social:</strong></td>

				<td width="212"><input type="text"
					id="cotaGarantiaFiadorSearchName" /></td>

				<td width="92"><strong>CPF/CNPJ:</strong></td>

				<td width="273"><input type="text"
					id="cotaGarantiaFiadorSearchDoc" style="width: 200px;" /></td>
			</tr>

		</table>

	</fieldset>

	<br clear="all" /> <br />
	<div id="cotaGarantiaFiadorDadosPanel" style="display: none;">
		<fieldset>
			<legend>Dados do Fiador</legend>
			<table width="755" cellpadding="2" cellspacing="2"
				style="text-align: left;">
				<tr>
					<td width="154"><strong>Nome / Raz&atilde;o Social:</strong></td>

					<td width="272"><span id="cotaGarantiaFiadorNome"></span></td>

					<td width="87"><strong>CPF/CNPJ:</strong></td>

					<td width="214"><span id="cotaGarantiaFiadorDoc"></span></td>
				</tr>
				<tr>
					<td><strong>Endere&ccedil;o:</strong></td>
					<td colspan="3"><span id="cotaGarantiaFiadorEndereco"></span></td>
				</tr>
				<tr>
					<td><strong>Telefone:</strong></td>

					<td><span id="cotaGarantiaFiadorTelefone"></span></td>

					<td>&nbsp;</td>

					<td>&nbsp;</td>

				</tr>

			</table>

		</fieldset>
		<br clear="all" /> <br />
		<fieldset class="cotaGarantiaFiadorDadosPanel" style="">
			<legend>Garantias Cadastradas</legend>
			<table id="cotaGarantiaFiadorGarantiasGrid"></table>
		</fieldset>
	</div>
</div>

<div id="cotaGarantiaChequeCaucaoPanel" style="display: none;">
	<fieldset>
		<legend>Dados Banc&aacute;rios</legend>
		<table width="601" border="0" cellspacing="2" cellpadding="2">
			<tr>
				<td width="138">Num. Banco:</td>
				<td width="122"><input type="text"
					name="cotaGarantiaChequeCaucaoNumeroBanco"
					id="cotaGarantiaChequeCaucaoNumeroBanco" style="width: 60px;" /></td>

				<td width="82">Nome:</td>
				<td width="233"><input type="text"
					name="cotaGarantiaChequeCaucaoNomeBanco"
					id="cotaGarantiaChequeCaucaoNomeBanco" style="width: 200px;" /></td>
			</tr>

			<tr>
				<td>Ag&ecirc;ncia:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoAgencia"
					id="cotaGarantiaChequeCaucaoAgencia" style="width: 60px;" />- <input
					type="text" name="cotaGarantiaChequeCaucaoDvAgencia"
					id="cotaGarantiaChequeCaucaoDvAgencia" style="width: 30px;" /></td>

				<td>Conta:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoConta"
					id="cotaGarantiaChequeCaucaoConta" style="width: 60px;" />- <input
					type="text" name="cotaGarantiaChequeCaucaoDvConta"
					id="cotaGarantiaChequeCaucaoDvConta" style="width: 30px;" /></td>
			</tr>

			<tr>
				<td>N&ordm; Cheque:</td>
				<td><input type="text"
					name="cotaGarantiaChequeCaucaoNumeroCheque"
					id="cotaGarantiaChequeCaucaoNumeroCheque" style="width: 107px;" />
				</td>
				<td>Valor R$:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoValor"
					id="cotaGarantiaChequeCaucaoValor"
					style="width: 108px; text-align: right;" /></td>
			</tr>
			<tr>
				<td>Data do Cheque:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoEmissao"
					id="cotaGarantiaChequeCaucaoEmissao" style="width: 107px;" /></td>
				<td>Validade:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoValidade"
					id="cotaGarantiaChequeCaucaoValidade"
					style="width: 108px; text-align: right;" /></td>
			</tr>
			<tr>
				<td>Nome Correntista:</td>
				<td colspan="3"><input type="text"
					name="cotaGarantiaChequeCaucaoCorrentista"
					id="cotaGarantiaChequeCaucaoCorrentista" style="width: 345px;" />
				</td>
			</tr>

			<tr>
				<td>Imagem Cheque:</td>
				<td colspan="3">
					<form id="cotaGarantiaChequeCaucaoFormUpload" enctype="multipart/form-data">
						<input name="image" type="file" id="cotaGarantiaChequeCaucaoUpload" size="58" />
						<input type="submit" value="Enviar"/> 
					</form>
				</td>
			</tr>
		</table>
	</fieldset>
	<br clear="all" /> <br />
	<div id="cotaGarantiaChequeCaucaoImagemPanel" style="display: none;">
		<fieldset>
			<legend>Foto Cheque</legend>
			<br />
			<div align="center" id="cotaGarantiaChequeCaucaoImagem"></div>
		</fieldset>
	</div>
</div>

<div id="cotaGarantiaImovelPanel" style="display: none;">
	<div id="dialog-excluir-imovel" title="Excluir Im&oacute;vel" style="display:none;" >
		<p>Confirma a exclus&atilde;o desse im&oacute;vel?</p>
	</div>
	<fieldset>
		<legend>Im&oacute;vel</legend>
		<table width="755" border="0" cellpadding="2" cellspacing="2"
			style="text-align: left;">
			<tbody>
				<tr>
					<td width="106">Propriet&aacute;rio:</td>
					<td width="635"><input type="text"
						name="cotaGarantiaImovelProprietario"
						id="cotaGarantiaImovelProprietario" style="width: 250px;"></td>
				</tr>
				<tr>
					<td>Endere&ccedil;o:</td>
					<td><input type="text" name="cotaGarantiaImovelEndereco"
						id="cotaGarantiaImovelEndereco" style="width: 450px;"></td>
				</tr>
				<tr>
					<td>N&uacute;mero Registro:</td>
					<td><input type="text" name="cotaGarantiaImovelNumeroRegistro"
						id="cotaGarantiaImovelNumeroRegistro" style="width: 250px;"></td>
				</tr>
				<tr>
					<td>Valor R$:</td>
					<td><input type="text" name="cotaGarantiaImovelValor"
						id="cotaGarantiaImovelValor"
						style="width: 100px; text-align: right;"></td>
				</tr>
				<tr>
					<td>Observa&ccedil;&atilde;o:</td>
					<td><textarea name="cotaGarantiaImovelObservacao" rows="3"
							id="cotaGarantiaImovelObservacao" style="width: 450px;"></textarea></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
					
					<span class="bt_add"> <a href="javascript:;"
							id="cotaGarantiaImovelIncluirNovo">Incluir Novo</a>
					</span>
					
					<span class="bt_novos">
			  			<a href="javascript:;" id="cotaGarantiaImovelSalvaEdicao" style="display:none;">
			  			<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"> Salvar</a>
			  		</span>
					
					</td>

				</tr>
			</tbody>
		</table>
	</fieldset>
	<br clear="all"> <br>

	<fieldset>
		<legend>Im&oacute;veis Cadastradas</legend>
		<div class="flexigrid" style="width: 740px;">
			<table class="cotaGarantiaImovelGrid"></table>
		</div>
	</fieldset>
</div>

<div id="cotaGarantiaCaucaoLiquida" style="display: none;">
	
	<div id="dialog-confirma-resgate" title="Confirmar Resgate Caução Líquida" style="display:none;" >
		<fieldset style="width: 380px;">
			<legend>Resgate de Cau&ccedil;&atilde;o L&iacute;quida</legend>
    		<p>Confirma o Resgate da Cau&ccedil;&atilde;o L&iacute;quida?</p>
		</fieldset>
	</div>
	<fieldset>
	   	<legend>Cau&ccedil;&atilde;o L&iacute;quida</legend>
        <table width="755" cellpadding="2" cellspacing="2" style="text-align:left;">
        <tbody>
        	<tr>
               <td width="110">Valor Inicial R$:</td>
            
               <td width="150">
                	<input type="text" name="cotaGarantiaCaucaoLiquidaValor" id="cotaGarantiaCaucaoLiquidaValor" style="width:150px; text-align:right;">
                </td>
               
                <td width="270">
                	<a id="cotaGarantiaCaucaoLiquidaIncluir" href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_check.gif" alt="Incluir" width="16" height="16" border="0"></a>
                </td>
                
                <td width="203">
                	<span class="bt_confirmar_novo" title="Confirmar">
                		<a id="cotaGarantiaCaucaoLiquidaResgatar" href="javascript:;">
                			<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_negociar.png">Resgatar Valor Cau&ccedil;&atilde;o
                		</a>
                	</span>
                </td>
              </tr>
        </tbody>
        </table>
	</fieldset>
	
    <br clear="all">
    
    <br>
    
    <fieldset>
    	<legend>Cau&ccedil;&atilde;o L&iacute;quida</legend>
    	
    	<div class="flexigrid" style="width: 740px; ">
    		<table id="cotaGarantiaCaucaoLiquidaGrid"></table>
        </div>
	</fieldset>

</div>


<div id="cotaGarantiaOutros" style="display: none;">
	
	<div id="dialog-excluir-outros" title="Excluir Garantia" style="display:none;" >
	
		<p>Confirma a exclus&atilde;o dessa garantia?</p>
	
	</div>
	
	<fieldset>

	<legend>Informe</legend>
	
        <table style="text-align: left;" border="0" cellSpacing="2" cellPadding="2" width="755">
    
         <tbody><tr>
           
           <td width="106">Descrição:</td>
           
           <td width="635"><textarea style="width: 450px;" id="descricaoCotaGarantiaOutros" rows="3" name="descricaoCotaGarantiaOutros"></textarea></td>
         
         </tr>
    
         <tr>
         
           <td>Valor R$:</td>
         
           <td><input style="width: 100px; text-align: right;" id="valorCotaGarantiaOutros" name="valorCotaGarantiaOutros" type="text"></td>
         
         </tr>
    
         <tr>
         
           <td>Validade:</td>
         
           <td><input style="width: 100px; text-align: right;" id="validadeCotaGarantiaOutros" name="validadeCotaGarantiaOutros" type="text"></td>
         
         </tr>
    
         <tr>
         
           <td>&nbsp;</td>
         
           <td>
               
           		<span class="bt_add"> 
           			<a href="javascript:;" id="cotaGarantiaOutrosIncluirNovo">Incluir Novo</a>
				</span>
				
				<span class="bt_novos">
				
		  			<a href="javascript:;" id="cotaGarantiaOutrosSalvaEdicao" style="display:none;">
		  			<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"> Salvar</a>
		  			
		  		</span>
               
           	</td>
         
         </tr>
    
    	 </tbody></table>

	</fieldset>
	
    <br clear="all">
    
    <br>
    
    <fieldset>
    
    	<legend>Outras Garantias Cadastradas</legend>
    	
    	<div class="flexigrid" style="width: 740px; ">
    	
    		<table id="cotaGarantiaOutrosGrid"></table>
        
        </div>
        
	</fieldset>

</div>



<br clear="all" />