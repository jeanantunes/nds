select `movimento_estoque_cota`.`COTA_ID` AS `COTA_ID`,
		  `produto_edicao`.`PRODUTO_ID` AS `PRODUTO_ID`,
		  `movimento_estoque_cota`.`PRODUTO_EDICAO_ID` AS `PRODUTO_EDICAO_ID`,
		  `produto_edicao`.`NUMERO_EDICAO` AS `NUMERO_EDICAO`,
		  `movimento_estoque_cota`.`DATA` AS `DATA_MOVIMENTO`,
		  `produto_edicao`.`PRECO_VENDA` AS `PRECO_VENDA`
		  ,coalesce((select sum(`mov_sub`.`QTDE`) AS `valor_venda` 
		  				 from ((`movimento_estoque_cota` `mov_sub` 
							join `produto_edicao` `prod_sub` on((`mov_sub`.`PRODUTO_EDICAO_ID` = `prod_sub`.`ID`))) 
							join `tipo_movimento` `tipo_mov_sub` on((`mov_sub`.`TIPO_MOVIMENTO_ID` = `tipo_mov_sub`.`ID`))) 
							where ((`tipo_mov_sub`.`OPERACAO_ESTOQUE` = _latin1'ENTRADA') 
							and (`mov_sub`.`COTA_ID` = `movimento_estoque_cota`.`COTA_ID`) 
							and (`mov_sub`.`ID` = `movimento_estoque_cota`.`ID`) 
							and (`prod_sub`.`ID` = `produto_edicao`.`ID`))),0) 
			AS `QNT_ENTRADA_PRODUTO`,
			
			coalesce((select sum(`mov_sub_sd`.`QTDE`) AS `valor_venda` 
						 from ((`movimento_estoque_cota` `mov_sub_SD` 
						 join `produto_edicao` `prod_sub_SD` on((`mov_sub_sd`.`PRODUTO_EDICAO_ID` = `prod_sub_sd`.`ID`))) 
						 join `tipo_movimento` `tipo_mov_sub_SD` on((`mov_sub_sd`.`TIPO_MOVIMENTO_ID` = `tipo_mov_sub_sd`.`ID`))) 
						 where ((`tipo_mov_sub_sd`.`OPERACAO_ESTOQUE` = _latin1'SAIDA') 
						 and (`mov_sub_sd`.`COTA_ID` = `movimento_estoque_cota`.`COTA_ID`) 
						 and (`mov_sub_sd`.`ID` = `movimento_estoque_cota`.`ID`) 
						 and (`prod_sub_sd`.`ID` = `produto_edicao`.`ID`))),0) 
			AS `QNT_SAIDA_PRODUTO`,
			
			sum(((coalesce((select sum(`mov_sub`.`QTDE`) AS `valor_venda` 
								 from ((`movimento_estoque_cota` `mov_sub` 
								 join `produto_edicao` `prod_sub` on((`mov_sub`.`PRODUTO_EDICAO_ID` = `prod_sub`.`ID`))) 
								 join `tipo_movimento` `tipo_mov_sub` on((`mov_sub`.`TIPO_MOVIMENTO_ID` = `tipo_mov_sub`.`ID`))) 
								 where ((`tipo_mov_sub`.`OPERACAO_ESTOQUE` = _latin1'ENTRADA') 
								 and (`mov_sub`.`COTA_ID` = `movimento_estoque_cota`.`COTA_ID`) 
								 and (`mov_sub`.`ID` = `movimento_estoque_cota`.`ID`) 
								 and (`prod_sub`.`ID` = `produto_edicao`.`ID`))),0) 
					- coalesce((select sum(`mov_sub_sd`.`QTDE`) AS `valor_venda` 
									from ((`movimento_estoque_cota` `mov_sub_SD` 
									join `produto_edicao` `prod_sub_SD` on((`mov_sub_sd`.`PRODUTO_EDICAO_ID` = `prod_sub_sd`.`ID`))) 
									join `tipo_movimento` `tipo_mov_sub_SD` on((`mov_sub_sd`.`TIPO_MOVIMENTO_ID` = `tipo_mov_sub_sd`.`ID`))) 
									where ((`tipo_mov_sub_sd`.`OPERACAO_ESTOQUE` = _latin1'SAIDA') 
									and (`mov_sub_sd`.`COTA_ID` = `movimento_estoque_cota`.`COTA_ID`) 
									and (`mov_sub_sd`.`ID` = `movimento_estoque_cota`.`ID`) 
									and (`prod_sub_sd`.`ID` = `produto_edicao`.`ID`))),0)) 
					* `produto_edicao`.`PRECO_VENDA`)) 
			AS `VALOR_TOTAL_VENDA`,
			
			coalesce((select `view_desconto`.`DESCONTO` AS `DESCONTO` 
							from (`view_desconto` join `produto_fornecedor`) 
							where ((`view_desconto`.`COTA_ID` = `movimento_estoque_cota`.`COTA_ID`) 
							and (`view_desconto`.`PRODUTO_EDICAO_ID` = `movimento_estoque_cota`.`PRODUTO_EDICAO_ID`) 
							and (`produto_fornecedor`.`PRODUTO_ID` = `view_desconto`.`PRODUTO_EDICAO_ID`) 
							and (`produto_fornecedor`.`fornecedores_ID` = `view_desconto`.`FORNECEDOR_ID`))),0) 
			AS `DESCONTO_PRODUTO`,
			
			sum(((coalesce((select sum(`mov_sub`.`QTDE`) AS `valor_venda` 
								from ((`movimento_estoque_cota` `mov_sub` 
								join `produto_edicao` `prod_sub` on((`mov_sub`.`PRODUTO_EDICAO_ID` = `prod_sub`.`ID`))) 
								join `tipo_movimento` `tipo_mov_sub` on((`mov_sub`.`TIPO_MOVIMENTO_ID` = `tipo_mov_sub`.`ID`))) 
								where ((`tipo_mov_sub`.`OPERACAO_ESTOQUE` = _latin1'ENTRADA') 
								and (`mov_sub`.`COTA_ID` = `movimento_estoque_cota`.`COTA_ID`) 
								and (`mov_sub`.`ID` = `movimento_estoque_cota`.`ID`) 
								and (`prod_sub`.`ID` = `produto_edicao`.`ID`))),0) 
					- coalesce((select sum(`mov_sub_sd`.`QTDE`) AS `valor_venda` 
									from ((`movimento_estoque_cota` `mov_sub_SD` 
									join `produto_edicao` `prod_sub_SD` on((`mov_sub_sd`.`PRODUTO_EDICAO_ID` = `prod_sub_sd`.`ID`))) 
									join `tipo_movimento` `tipo_mov_sub_SD` on((`mov_sub_sd`.`TIPO_MOVIMENTO_ID` = `tipo_mov_sub_sd`.`ID`))) 
									where ((`tipo_mov_sub_sd`.`OPERACAO_ESTOQUE` = _latin1'SAIDA') 
									and (`mov_sub_sd`.`COTA_ID` = `movimento_estoque_cota`.`COTA_ID`) 
									and (`mov_sub_sd`.`ID` = `movimento_estoque_cota`.`ID`) 
									and (`prod_sub_sd`.`ID` = `produto_edicao`.`ID`))),0)) 
					* (`produto_edicao`.`PRECO_VENDA` - ( `produto_edicao`.`PRECO_VENDA` *( coalesce((select `view_desconto`.`DESCONTO` AS `DESCONTO` 
																				 from (`view_desconto` join `produto_fornecedor`) 
																				 where ((`view_desconto`.`COTA_ID` = `movimento_estoque_cota`.`COTA_ID`) 
																				 and (`view_desconto`.`PRODUTO_EDICAO_ID` = `movimento_estoque_cota`.`PRODUTO_EDICAO_ID`) 
																				 and (`produto_fornecedor`.`PRODUTO_ID` = `view_desconto`.`PRODUTO_EDICAO_ID`) 
																				 and (`produto_fornecedor`.`fornecedores_ID` = `view_desconto`.`FORNECEDOR_ID`))),0))/100)))) 
			AS `VALOR_TOTAL_VENDA_COM_DESCONTO` 
		
		from (`movimento_estoque_cota` 
		join `produto_edicao` on((`movimento_estoque_cota`.`PRODUTO_EDICAO_ID` = `produto_edicao`.`ID`))) 
		group by `movimento_estoque_cota`.`PRODUTO_EDICAO_ID`,`movimento_estoque_cota`.`COTA_ID`,`movimento_estoque_cota`.`DATA` 
		order by `movimento_estoque_cota`.`DATA` desc