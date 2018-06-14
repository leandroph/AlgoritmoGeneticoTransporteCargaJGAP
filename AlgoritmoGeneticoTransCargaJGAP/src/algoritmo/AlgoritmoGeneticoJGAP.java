/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.event.EventManager;
import org.jgap.impl.BestChromosomesSelector;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.StockRandomGenerator;
import org.jgap.impl.SwappingMutationOperator;

/**
 *
 * @author Leandro
 */
public class AlgoritmoGeneticoJGAP {

    Configuration configuracao;//vai representar o ambiente para o algotitmo genético
    int numeroGeracoes = 100;
    Double limite = 3.0;
    int tamanhoPopulacao = 20;
    int taxaMutacao = 100;// é colocado 100 pois o JGAP trabalha com uma padronização onde ele pega o 1 e divide por 100 
    //assim tendo 0.01 que seria 1%
    //IChromosome não é uma classe é uma interface
    List<IChromosome> melhoresCromossomos = new ArrayList<>();//vai armazenar as melhores soluções
    List<Produto> listaProdutos = new ArrayList<>();
    IChromosome melhor;

    //função para carregar os produtos
    public void carregar() throws ClassNotFoundException, SQLException {
        listaProdutos.add(new Produto("Geladeira Dako", 0.751, 999.90));
        listaProdutos.add(new Produto("IPhone 6", 0.0000899, 2199.12));
        listaProdutos.add(new Produto("TV 55'", 0.400, 4346.99));
        listaProdutos.add(new Produto("TV 50'", 0.290, 3999.90));
        listaProdutos.add(new Produto("TV 42'", 0.200, 2999.90));
        listaProdutos.add(new Produto("Notebook Dell", 0.00350, 2499.90));
        listaProdutos.add(new Produto("Ventilador Panasonic", 0.496, 199.90));
        listaProdutos.add(new Produto("Microondas Eletrolux", 0.0424, 308.66));
        listaProdutos.add(new Produto("Microondas LG", 0.0544, 429.90));
        listaProdutos.add(new Produto("Microondas Panasonic", 0.0319, 299.29));
        listaProdutos.add(new Produto("Geladeira Brastemp", 0.635, 849.00));
        listaProdutos.add(new Produto("Geladeira Consul", 0.870, 1199.89));
        listaProdutos.add(new Produto("Notebook Lenovo", 0.498, 1999.90));
        listaProdutos.add(new Produto("Notebook Asus", 0.527, 3999.00));
        
    }
    
    public Double somaEspacos(IChromosome cromossomo){
        Double soma = 0.0;
        
        for (int i = 0; i < cromossomo.size(); i++) {
            if (cromossomo.getGene(i).getAllele().toString().equals("1")) {
                soma += this.listaProdutos.get(i).getEspaco();
            }
        }
        return soma;
    }
    
    //metodo para printar na tela omelhor cromossomo de cada geração
    public void visualizaGeracao(IChromosome cromossomo, int geracao){
        List lista = new ArrayList<>();
        Gene[] genes = cromossomo.getGenes();//pega cada gene do cromossomo e joga dentro do vetor Gene que é da biblioteca JGAP
        
        for (int i = 0; i < cromossomo.size(); i++) {
            lista.add(genes[i].getAllele().toString() + " ");
        }
        
        System.out.println("Geração " + geracao + 
                "Valor " + cromossomo.getFitnessValue() +
                "Espaço " + somaEspacos(cromossomo)+
                "Cromossomo " + lista);
    }
    
    //metodo especifico para a criação do cromossomo
    public IChromosome criarCromossomo() throws InvalidConfigurationException{
        Gene[] genes = new Gene[listaProdutos.size()];// o genes vai ter os 14 produtos da lista no seu vetor
        
        for (int i = 0; i < genes.length; i++) {//se for menor que o tamanho do cromossomo
            genes[i] = new IntegerGene(configuracao, 0, 1);
            genes[i].setAllele(i);//vai setar 0 ou 1 para o alelo do vetor
        }
        //aqui efetivamente o cromossomo é criado
        IChromosome modelo = new Chromosome(configuracao, genes);
        return modelo;
        
    }
    
    public FitnessFunction criarFuncaoFitness(){
        return new Avaliacao(this);
    }
    
    //metodo que cria a  configuração 
    public Configuration criarConfiguracao() throws InvalidConfigurationException{
        Configuration configuracao = new Configuration();
        configuracao.removeNaturalSelectors(true);
        
        configuracao.addNaturalSelector(new BestChromosomesSelector(configuracao, 0.4), true);
        // se estiver com o valor true:
        // primeiro ele faz o operador de selação e operador genético 
        // e se estiver false:
        // primeiro ele faz a aplicação do crossover e depois ele faz a seleção dos individuos
        configuracao.setRandomGenerator(new StockRandomGenerator());//gera aleatoriamente os cromossomos
        configuracao.addGeneticOperator(new CrossoverOperator(configuracao));// faz o crossover
        configuracao.addGeneticOperator(new SwappingMutationOperator(configuracao, taxaMutacao));// faz a mutação
        configuracao.setKeepPopulationSizeConstant(true);//esse metodo  mantem o mesmo tmanho de população ao longo das gerações
        configuracao.setEventManager(new EventManager());// o Event Manager que será responsavel pela captura dos dados
        configuracao.setFitnessEvaluator(new DefaultFitnessEvaluator());//vai considerar o individuo com a maior nota de avaliação
        return configuracao;
    }
    
    public void procurarMelhorSolucao() throws InvalidConfigurationException{
        this.configuracao = criarConfiguracao();
        FitnessFunction funcaoFitness = criarFuncaoFitness();
        configuracao.setFitnessFunction(funcaoFitness);
        IChromosome modeloCromossomo = criarCromossomo();
        configuracao.setSampleChromosome(modeloCromossomo);
        configuracao.setPopulationSize(this.tamanhoPopulacao);//passa o tamanho da população
        
        IChromosome[] cromossomo = new IChromosome[tamanhoPopulacao];
        for (int i = 0; i < this.tamanhoPopulacao; i++) {
            cromossomo[i] = criarCromossomo();
        }
        //criando a população
        Genotype populacao = new Genotype(configuracao, new Population(configuracao, cromossomo));
        
        for(int j = 0; j < this.numeroGeracoes; j++){
            this.visualizaGeracao(populacao.getFittestChromosome(), j);//pega o melhor cromossomo da população
            this.melhoresCromossomos.add(populacao.getFittestChromosome());
            populacao.evolve();
        }
    }

 //metodo carregar para pegar os  produtos do banco de dados    
//  public void carregar() throws ClassNotFoundException, SQLException {
//        List<Produto> listaProdutos = new ArrayList<>();
//        
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/produtos", "root", "heck1592");
//        Statement consulta = con.createStatement();
//        ResultSet rs = consulta.executeQuery("select nome, espaco, valor, quantidade from tb_produtos");
//        
//        while(rs.next()){
//            for (int i = 0; i < rs.getInt("quantidade"); i++) {
//                //System.out.println("Nome " + rs.getString("nome"));
//                listaProdutos.add(new Produto(rs.getString("nome"), rs.getDouble("espaco"), rs.getDouble("valor")));
//            }
//    }

    
    }
