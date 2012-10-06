<head>
	<style>
		span {font-weight: bold;}
		td   {font-weight: bold;}
	</style>
</head>
<table border="0" align="center" cellpadding="2" cellspacing="2" width="100%" id="tableDados">
	<tr>
		<td valign="top" style="float:right">

			<table id="tableDadosArquivo" width="269" border="0" align="center" cellpadding="2"
				   cellspacing="1" style="display: inline-block; margin-right: 15px;">
				<tr>
					<td colspan="2" align="center" class="header_table">Dados do
						Arquivo</td>
				</tr>
				<tr>
					<td width="121" align="left" class="linha_borda"><strong>Nome
							do Arquivo:</strong></td>
					<td id="nomeArquivo" width="137" align="right" class="linha_borda"></td>
				</tr>
				<tr>
					<td align="left" class="linha_borda"><strong>Data
							Competência:</strong></td>
					<td id="dataCompetencia" align="right" class="linha_borda"></td>
				</tr>
				<tr>
					<td align="left" class="linha_borda"><strong>Valor
							R$:</strong></td>
					<td id="somaPagamentos" align="right" class="linha_borda"></td>
				</tr>
				<tr>
					<td align="left" class="linha_borda">&nbsp;</td>
					<td align="right" class="linha_borda">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2" align="left" style="line-height: 28px; border: 1px solid #0C0;">
						<img src="${pageContext.request.contextPath}/images/bt_check.gif" width="22" height="22"
							 alt="Arquivo Integrado com Sucesso" align="left" /> 
						<span>
							<strong>Arquivo Integrado com Sucesso!</strong>
						</span>
					</td>
				</tr>
			</table>
		</td>
		<td valign="top">
			<table width="275" border="0" align="center" id="tableDadosResumoBaixa"
				   cellpadding="2" cellspacing="1" style="display: inline-block;">
				<tr>
					<td colspan="2" align="center" class="header_table"
						class="linha_borda">Baixa Automática</td>
				</tr>
			    <tr>
				    <td align="left" class="linha_borda"><strong>Previsão:</strong></td>
				    <td align="right" class="linha_borda">
				    	<a href="javascript:;" onclick="baixaFinanceiraController.mostrarGridBoletosPrevisao();">
				    		<span id="quantidadePrevistos"></span>
				    	</a>
				    </td>
			    </tr>
				<tr>
					<td width="162" align="left" class="linha_borda">
						<strong>Registros Lidos:</strong>
					</td>
					<td width="102" align="right" class="linha_borda">
						<span id="quantidadeLidos"></span> 
					</td>
				</tr>
				<tr>
					<td align="left" class="linha_borda">
						<strong>Registros Baixados:</strong>
					</td>
					<td align="right" class="linha_borda">
						<a href="javascript:;" onclick="baixaFinanceiraController.mostrarGridBoletosBaixados();">
							<span id="quantidadeBaixados"></span>
						</a>
					</td>
				</tr>
				<tr>
					<td align="left" class="linha_borda">
						<strong>Registros Rejeitados:</strong>
					</td>
					<td align="right" class="linha_borda">
						<a href="javascript:;" onclick="baixaFinanceiraController.mostrarGridBoletosRejeitados();">
							<span id="quantidadeRejeitados"></span>
						</a>
					</td>
				</tr>
				<tr>
					<td align="left" class="linha_borda">
						<strong>Baixados com Divergência:</strong>
					</td>
					<td align="right" class="linha_borda">
						<a href="javascript:;" onclick="baixaFinanceiraController.mostrarGridBoletosBaixadosComDivergencia();">
							<span id="quantidadeBaixadosComDivergencia"></span>
						</a>
					</td>
				</tr>
				<tr>
					<td align="left" class="linha_borda"><strong>Inadimplentes:</strong></td>
					<td align="right" class="linha_borda">
						<a href="javascript:;" onclick="baixaFinanceiraController.mostrarGridBoletosInadimplentes();">
							<span id="quantidadeInadimplentes"></span>
						</a>
					</td>
				</tr>
				<tr>
					<td align="left" class="linha_borda"><strong>Total Bancário:</strong></td>
					<td align="right" class="linha_borda" id="tdValorTotal">
					</td>
				</tr>
				<tr>
					<td valign="top" colspan="2">
						<span class="bt_novos" title="Suspender Cota">
							<a href="suspensao_jornaleiro.htm">
								<img src="${pageContext.request.contextPath}/images/ico_suspender.gif" hspace="5" border="0"/>
								<strong>Suspender Cota</strong>
							</a>
						</span>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>