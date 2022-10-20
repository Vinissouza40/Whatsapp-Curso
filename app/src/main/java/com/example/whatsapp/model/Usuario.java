package com.example.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Usuario {
    private DatabaseReference mDatabase;
    private String id;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }

    public void salvar() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("usuarios").child(this.id).setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
