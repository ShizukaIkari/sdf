/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.trabalhosd;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Serenna
 */
public class TrabFinalSD {
    /*Aplicação (quaquer tecnologia de Back-end) recebe os dados do Middleware, faz os tratamentos 
    e validações necessários(regras de validação - elimina dados fora do intervalo esperado, valores estranhos).
    Permite a emisssão de relatórios gráficos sobre a variável monitorada(seleeção por data e devices).*/
    public static void main(String[] args) throws IOException, PythonExecutionException {
        ConnectPostgres cp = new ConnectPostgres();
        ConnectPostgres bancoFinal = null;
       
        ArrayList<List> dadosRaw = cp.retrieveRawData("SELECT * FROM variaveis");
        bancoFinal = new ConnectPostgres("jdbc:postgresql://tuffi.db.elephantsql.com:5432/cnaqwjpy","cnaqwjpy","kJeRmt7qkAMF5YAKNyUhcpoDge2zHnoJ");
        //validação
        for (int i = 0; i < dadosRaw.size(); i++) {
            String dataColeta = (dadosRaw.get(i).get(0)).toString();
            float temp = Float.parseFloat(dadosRaw.get(i).get(1).toString());
            float umidade = Integer.parseInt(dadosRaw.get(i).get(2).toString());
            float pressao = Float.parseFloat(dadosRaw.get(i).get(3).toString());
            if ((temp >= 16.0f && temp <= 40f) && (umidade >= 0 && umidade <= 100) && (pressao >= 1000 && pressao <= 1100)) {
                if(!dataColeta.equals("0")){
                    
                    bancoFinal.executeSql("INSERT INTO dados VALUES ('"+dataColeta+"',"+temp+","+umidade+","+pressao+");");
                }
            }

        }
        
        
        /*Código para gerar gráficos exibindo 
         relatórios gráficos sobre a variável monitorada(seleção por data e devices).*/
        
        //Exibir datas disponiveis no banco
        ArrayList allDatas = bancoFinal.selectUniqData();
        System.out.println(allDatas.toString());
        
        //"Dicionario" com chave da data escolhida e da medida escolhida
        System.out.println("Temperatura, pressao ou umidade?");
        BufferedReader in1 = new BufferedReader(new InputStreamReader(System.in));
        String m = in1.readLine();
        String medida = m;
        System.out.println("Qual data?");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = in.readLine();
        String dataE = str;
        
        HashMap dadosV = bancoFinal.selectValidData(medida,dataE);
        Plot plt = Plot.create();
        plt.hist().add((List)dadosV.get(dataE));
        plt.xlabel("Valor");
        plt.ylabel("Quantidade");
        plt.title(medida+" na data "+dataE);
        plt.legend();
        plt.show();
        
        
        
        cp.closeConnection();
        cp.closeStatement();
        cp.closeResultSet();
        bancoFinal.closeStatement();
        bancoFinal.closeResultSet();
        bancoFinal.closeConnection();
    }
}
