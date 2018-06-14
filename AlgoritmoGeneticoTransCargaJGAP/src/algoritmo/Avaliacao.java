/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmo;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

/**
 *
 * @author Leandro
 */
public class Avaliacao extends FitnessFunction{

    private final AlgoritmoGeneticoJGAP algoritmoGeneticoJGAP;
    
    public Avaliacao(AlgoritmoGeneticoJGAP ag){
        this.algoritmoGeneticoJGAP = ag;
    }
    @Override
    protected double evaluate(IChromosome cromossomo) {
        Double nota = 0.0;
        Double somaEspacos = 0.0;
        
        //atribui a nota e soma o total de espaço que os produtos ocupam
        for (int i = 0; i < cromossomo.size(); i++) {
            if (cromossomo.getGene(i).getAllele().toString().equals("1")) {
                nota += this.algoritmoGeneticoJGAP.listaProdutos.get(i).getValor();
                somaEspacos += this.algoritmoGeneticoJGAP.listaProdutos.get(i).getEspaco();
                
            }
        }
        if (somaEspacos > this.algoritmoGeneticoJGAP.limite) {
            nota = 1.0;// se a soma dos espaços for maior que o limite ela é reduzida para 1 ou é rebaixada
        }
        return nota;
    }
    
}
