package com.sistemahotel.demo.model;

import java.util.Objects;

public class Hospede {

    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;

    public Hospede() {
    }

    public Hospede(Long id, String nome, String cpf, String telefone, String email) {
        this.id = id;
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
        setEmail(email);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || !cpf.replaceAll("[^0-9]", "").matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido. Informe 11 dígitos numéricos.");
        }
        this.cpf = cpf.replaceAll("[^0-9]", "");
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone == null || !telefone.replaceAll("[^0-9]", "").matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Telefone inválido. Informe 10 ou 11 dígitos.");
        }
        this.telefone = telefone.replaceAll("[^0-9]", "");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w+.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        this.email = email.trim().toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hospede)) return false;
        Hospede hospede = (Hospede) o;
        return Objects.equals(cpf, hospede.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public String toString() {
        return "\nID: " + id +
               "\nNome: " + nome +
               "\nCPF: " + cpf +
               "\nTelefone: " + telefone +
               "\nEmail: " + email;
    }
}
