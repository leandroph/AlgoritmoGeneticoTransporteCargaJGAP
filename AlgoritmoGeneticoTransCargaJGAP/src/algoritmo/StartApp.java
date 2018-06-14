package algoritmo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.jfree.ui.RefineryUtilities;
import org.jgap.InvalidConfigurationException;

/**
 *
 * @author Leandro
 */
public class StartApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, InvalidConfigurationException {

        AlgoritmoGeneticoJGAP ag = new AlgoritmoGeneticoJGAP();
        ag.carregar();
        ag.procurarMelhorSolucao();

        int geracao = 0;

        for (int i = 0; i < ag.melhoresCromossomos.size(); i++) {
            if (ag.melhor == null) {
                ag.melhor = ag.melhoresCromossomos.get(i);
            } else if (ag.melhor.getFitnessValue() < ag.melhoresCromossomos.get(i).getFitnessValue()) {
                ag.melhor = ag.melhoresCromossomos.get(i);
                geracao = i;
            }
        }

        System.out.println("\nMelhor Solução");
        ag.visualizaGeracao(ag.melhor, geracao);

        for (int i = 0; i < ag.listaProdutos.size(); i++) {
            if (ag.melhor.getGene(i).getAllele().toString().equals("1")) {
                System.out.println("Nome " + ag.listaProdutos.get(i).getNome());
            }
        }

//        Grafico g = new Grafico("Algoritmo Genético", "Evolução das Soluções", ag.melhoresCromossomos);
//        g.pack();
//        RefineryUtilities.centerFrameOnScreen(g);
//        g.setVisible(true);

    }

}
