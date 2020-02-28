package com.company;

import java.util.List;
import java.util.Objects;

public class Animal {
    private String nome;

    private List<String> caracteristica;

    public Animal(String nome, List<String> caracteristica) {
        this.nome = nome;
        this.caracteristica = caracteristica;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getCaracteristica() { return this.caracteristica;  }

    public void setCaracteristica(List<String> caracteristica) {
        this.caracteristica = caracteristica;
    }

}
