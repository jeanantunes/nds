<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/movimentoFinanceiroCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/data.holder.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>

	<script type="text/javascript">
	
	    var pesquisaCota = new PesquisaCota(movimentoFinanceiroCotaController.workspace);
	
		$(function(){
		
			movimentoFinanceiroCotaController.init();
		});
		
		var pesquisaProduto = new PesquisaProduto(movimentoFinanceiroCotaController.workspace);
		
		$('#processsamentoFinanceiroTableFiltroCota').hide();
		$('#processsamentoFinanceiroTableFiltroProduto').hide();
		
		function filtroPorCota(){
			$('#processsamentoFinanceiroTableFiltroCota').show();
			$('#processsamentoFinanceiroTableFiltroProduto').hide();
		}

		function filtroPorProduto(){
			$('#processsamentoFinanceiroTableFiltroCota').hide();
			$('#processsamentoFinanceiroTableFiltroProduto').show();
		}
		
	</script>
	
	<style>

		#dialog-confirma-processamento{display:none;}
        
	</style>
	
    </head>

<body>

	<div class="areaBts">
	
		<div class="area">
		
            <div id="botoesDividasPagas">
                <span class="bt_novos">
	             	<a onclick="movimentoFinanceiroCotaController.processarFinanceiroCota();"
	             	   href="javascript:;" rel="tipsy" title="Consolidar e Gerar Cobrança">
	             	   	<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">
	             	</a>
                </span>
                
                <span class="bt_novos">
	             	<a onclick="movimentoFinanceiroCotaController.postergarFinanceiroCota();"
	             	   href="javascript:;" rel="tipsy" title="Consolidar e Postergar Dívida">
	             	   	<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_reprogramar.gif">
	             	</a>
                </span>
            </div>

		</div>
	</div>

	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
		
		<legend>Processamento Financeiro</legend>
		
		<!-- BAIXA MANUAL -->
		
		<table width="200" border="0" class="filtro" id="processsamentoFinanceiroTableFiltro">
			<tr>
	            <td style="width: 10px;"><input type="radio" name="filtroPrincipalRadio" id="radio"  value="Cota" onclick="filtroPorCota();" /></td>
	            <td style="width: 10px;" ><label >Cota</label></td>
	            <td style="width: 10px;"><input type="radio" name="filtroPrincipalRadio" id="radio2" value="Produto" onclick="filtroPorProduto()" /></td>
	            <td style="width: 10px;"><label >Produto</label></td>
            </tr>
		</table>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro" id="processsamentoFinanceiroTableFiltroCota">
            <tr>
					<td width="20">Cota:</td>
	                
	                <td style="width: 108px; ">
		                <input name="filtroNumCota" 
		             	       id="filtroNumCota" 
		             		   type="text"
		             		   maxlength="11"
		             		   style="width: 80px; 
		             		   float:left; 
		             		   margin-right:5px;"
		             		   onchange="pesquisaCota.pesquisarPorNumeroCota('#filtroNumCota', '#descricaoCota');" />
					</td>
					
					<td width="30">Nome:</td>
	             	
	             	<td width="150" style="width: 348px; ">
			        	<input name="descricaoCota" 
			      		 	   id="descricaoCota" 
			      		 	   type="text"
			      		 	   class="nome_jornaleiro" 
			      		 	   maxlength="255"
			      		 	   style="width: 322px;"
			      		 	   onkeyup="pesquisaCota.autoCompletarPorNome('#descricaoCota');" 
			      		 	   onblur="pesquisaCota.pesquisarPorNomeCota('#filtroNumCota', '#descricaoCota');" />
			        </td>
			        <td width="30">
				    <span class="bt_pesquisar">
				        <a href="javascript:;" onclick="movimentoFinanceiroCotaController.buscarCotas();"></a>
				    </span>
				</td>
			</tr>
		</table>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro" id="processsamentoFinanceiroTableFiltroProduto">
            <tr>
			    	<td width="129">Código:</td>
			        <td>
						<input id="processamentoFinanceiro-codigoProdutoEd" name="textfield2" type="text" style="width:80px;" value="" onchange="pesquisaProduto.pesquisarPorCodigoProduto('#processamentoFinanceiro-codigoProdutoEd','#processamentoFinanceiro-nomeProdutoEd',false,undefined,undefined )"/ />
					</td>
			    	<td>Produto:</td>
			        <td>
						<input id="processamentoFinanceiro-nomeProdutoEd" name="textfield7" type="text" style="width:250px;" value="" />
					</td>
			        <td>Edição:</td>
			        <td><input id="processamentoFinanceiro-numEdicaoEd" name="textfield" type="text" style="width:80px;" value="" /></td>
				
				<td width="30">
				    <span class="bt_pesquisar">
				        <a href="javascript:;" onclick="movimentoFinanceiroCotaController.buscarCotas();"></a>
				    </span>
				</td>
            
            </tr>
		</table>

		        
        <!--  -->
        
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<form name="formularioListaCotas" id="formularioListaCotas">

	    <input type="hidden" id="totalDividasHidden" />
	    
	    <input type="hidden" id="totalDividasSelecionadasHidden" />
	 
		<fieldset class="classFieldset" id="extratoBaixaManual" >
		
	      	<legend>Cotas</legend>
	      	
	        <br />
	        
	        <div id="dialog-confirma-processamento" title="Processamento Financeiro">
			    <p>Deseja confirmar processamento financeiro para a Cota ?</p>
		    </div>
			
	      	<div  id="divCotas">
	      	
		       <table class="gridCotas" id="tabelaCotas"></table>
		    
		       <table width="100%" border="0" cellspacing="2" cellpadding="2">
		            <tr>
		            
		                <td width="20%">

		                </td>
						
						<td width="30%">   

		                </td>
		                
		                <td width="14%">
		                    <strong>Total Selecionado R$:</strong>
		                </td>
		                <td width="7%" id="totalDividasSelecionadas"></td>
		                
		                <td width="7%">
		                    <strong>Total R$:</strong>
		                </td>
		                <td width="7%" id="totalDividas"></td>
		                
		                <td width="20%">
		                
		                    <span class="checar">
		                        
		                        <label for="textoSelTodos" id="textoSelTodos">
		                            Marcar Todos
		                        </label>
		                        
		                        <input isEdicao="true" title="Selecionar todas as Dívidas" type="checkbox" id="selTodos" name="selTodos" onclick="movimentoFinanceiroCotaController.selecionarTodos(this.checked);" style="float:left;"/>
		                    </span>

		                </td>
		            </tr>
		        </table>
	        </div>

	    </fieldset>
    
    </form>
    
    <!--  -->
    
</body>