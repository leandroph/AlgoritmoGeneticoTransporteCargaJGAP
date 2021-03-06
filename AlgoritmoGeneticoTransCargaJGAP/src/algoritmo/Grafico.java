/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmo;

import java.awt.BasicStroke;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.Line;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jgap.IChromosome;

/**
 * 
 * @author Leandro
 * @author Cristiano
 * @
 */
public class Grafico extends ApplicationFrame{//cria uma janela para a visualização do gráfico
    
    private List<IChromosome> melhoresCromossomos = new ArrayList<>();
    /**
     * 
     * @param tituloJanela
     * @param tituloGrafico
     * @param melhores 
     * Método para gerar o gráfico 
     */
    public Grafico(String tituloJanela, String tituloGrafico, List melhores){
        super(tituloJanela);
       
        this.melhoresCromossomos = melhores;
        JFreeChart graficoLinha = ChartFactory.createLineChart(tituloGrafico, 
                "Geração", "Valor", 
                carregarDados(), 
                PlotOrientation.VERTICAL, 
                true, true, false);
        
       /**
        * aumenta o tamanho da linha no gráfico
        */
        CategoryPlot plot = (CategoryPlot) graficoLinha.getPlot();
        LineAndShapeRenderer render = (LineAndShapeRenderer) plot.getRenderer();
        render.setSeriesStroke(0, new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, new float[]{1.0f,1.0f}, 0.0f));
       //render.setSeriesShapesVisible(0, true);
        //render.setSeriesItemLabelsVisible(0, true);//seta o valor da melhor solução da geração na linha do grafico
       
        
        ChartPanel janelaGrafico = new ChartPanel(graficoLinha);
        janelaGrafico.setPreferredSize(new java.awt.Dimension(800,600));//cria a dimenção da janela
        setContentPane(janelaGrafico);
    }
    
    private DefaultCategoryDataset carregarDados(){//usado para mostrar os resultados em um gráfico
        DefaultCategoryDataset dados = new DefaultCategoryDataset();
        for (int i = 0; i < melhoresCromossomos.size(); i++) {
            dados.addValue(melhoresCromossomos.get(i).getFitnessValue(), "Melhor solução", "" + i);//pega a função fitness direto do jar
        }
        return dados; // essa função percorre os cromossomos e pega a nota de avaliação de cada um 
        // e coloca no padrao DefaultCategoryDataset, que é usado por esse blioteca
    }
}
