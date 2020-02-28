package com.company;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class Main {
    private boolean acerteiAnimal = false;

    private boolean acerteiCaracteristica = false;

    private List<Animal> animais = new ArrayList<>();

    private Set<String> todasCaracteristicas = new HashSet<>();

    private Set<String> caracteristicasPerguntadas = new HashSet<>();

    private String caracteristicaAtual;

    private Animal animalAtual;

    public static void main(String[] args) {
        Main main = new Main();
        main.adicionarAnimaisIniciais();
        main.mensagemDeBoasVindas();
        main.sorteiaCaracteristica();
        main.penseEmUmAnimal();
    }

    private void mensagemDeBoasVindas() {
        JOptionPane.showMessageDialog(null, "Bem vindo ao jogo dos animais", "Jogo dos Animais", JOptionPane.INFORMATION_MESSAGE);
    }

    private void adicionarAnimaisIniciais() {
        Animal tubarao = new Animal("Tubarão", new ArrayList<>(Arrays.asList("Vive na água")));
        adicionarAnimal(tubarao);
    }

    private void adicionarAnimal(Animal animal) {
        this.animais.add(animal);
        this.todasCaracteristicas.addAll(animal.getCaracteristica());
    }


    private void sorteiaCaracteristica() {
        //busca um animal que não seja o animal atual
        Optional<Animal> oAnimal = this.animais.stream().filter(animal -> !animal.equals(this.animalAtual)).findAny();
        //caso o optional não encontre um animal, seta o animal atual
        this.animalAtual = oAnimal.orElse(this.animalAtual);

        //caso não tenha nas caracteristicas perguntadas e a caracteristica perguntada não seja igual a caracteristica atual
        Optional<String> oCaracteristica = this.todasCaracteristicas.stream().filter(carac -> (!this.caracteristicasPerguntadas.contains(carac) && !carac.equals(this.caracteristicaAtual))).findAny();
        //caso o optional não encontre uma caracteristica, seta a atual
        this.caracteristicaAtual = oCaracteristica.orElse(this.caracteristicaAtual);
    }

    private void penseEmUmAnimal() {
        JOptionPane.showMessageDialog(null, "Pense em um animal!", "Jogo dos Animais", JOptionPane.INFORMATION_MESSAGE);
        descobreAnimal(procuraAnimalPorCaracteristica());
    }

    private void descobreAnimal(List<Animal> possiveis) {
        int opcao = JOptionPane.showConfirmDialog(null, "O animal que você pensou " + this.caracteristicaAtual, "Jogo do Animais", JOptionPane.YES_NO_OPTION);
        if (opcao == -1) //apertou o X
            sair();
        this.acerteiCaracteristica = (opcao == 0);
        this.animalAtual = possiveis.remove(0);
        this.caracteristicasPerguntadas.add(this.caracteristicaAtual);
        if (this.acerteiCaracteristica) {
            perguntaSeEhAnimalDaVez(possiveis);
        } else if (this.caracteristicasPerguntadas.size() < this.todasCaracteristicas.size()) {
            sorteiaCaracteristica();
            descobreAnimal(procuraAnimalPorCaracteristica());
        } else {
            novoAnimal();
        }
    }

    private void perguntaSeEhAnimalDaVez(List<Animal> possiveis) {
        int i = JOptionPane.showConfirmDialog(null, "O animal é um " + this.animalAtual.getNome(), "Jogo do Animais", JOptionPane.YES_NO_OPTION);
        if (i == -1) //apertou o X
            sair();
        this.acerteiAnimal = (i == 0);
        if (this.acerteiAnimal) {
            JOptionPane.showMessageDialog(null, "Acertei!", "Jogo dos Animais", JOptionPane.INFORMATION_MESSAGE);
            continuar();
        } else {
            verificaSeAindaTemAnimal(possiveis);
        }
    }

    private void verificaSeAindaTemAnimal(List<Animal> possiveis) {
        if (possiveis.isEmpty()) {
            novoAnimal();
        } else {
            this.animalAtual = possiveis.remove(0);
            Optional<String> oCaracteristica = this.animalAtual.getCaracteristica().stream().filter(carac -> (!this.caracteristicasPerguntadas.contains(carac) && !carac.equals(this.caracteristicaAtual))).findAny();
            if (oCaracteristica.isPresent()) {
                this.caracteristicaAtual = oCaracteristica.orElse(this.caracteristicaAtual);
                if (possiveis.isEmpty()) {
                    descobreAnimal(new ArrayList<Animal>(){{add(animalAtual);}});
                } else {
                    descobreAnimal(possiveis);
                }
            } else {
                perguntaSeEhAnimalDaVez(possiveis);
            }
        }
    }

    private void novoAnimal() {
        String nomeAnimal = JOptionPane.showInputDialog("Qual Animal você pensou?");
        if(nomeAnimal == null)
            sair();
        String detalhes = JOptionPane.showInputDialog("Um(a)" + nomeAnimal + "____ mas um(a) " + this.animalAtual.getNome() + " não");
        if(detalhes == null)
            sair();
        Animal novoAnimal = new Animal(nomeAnimal, new ArrayList<String>(){{add(detalhes);}});

        if (this.acerteiCaracteristica)
            novoAnimal.getCaracteristica().add(this.caracteristicaAtual);
        adicionarAnimal(novoAnimal);
        continuar();
    }

    private void continuar() {
        boolean vamosContinuar = (JOptionPane.showConfirmDialog(null, "Vamos continuar?", "Jogo dos Animais", JOptionPane.YES_NO_OPTION) == 0);
        if (vamosContinuar) {
            this.caracteristicasPerguntadas.clear();
            sorteiaCaracteristica();
            penseEmUmAnimal();
        } else {
            sair();
        }
    }

    private void sair() {
        JOptionPane.showMessageDialog(null, "Obrigado por Jogar!", "Jogo dos Animais", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    private List<Animal> procuraAnimalPorCaracteristica() {
        return this.animais.stream().filter(animal -> animal.getCaracteristica().contains(this.caracteristicaAtual))
                .collect(Collectors.toList());
    }
}
