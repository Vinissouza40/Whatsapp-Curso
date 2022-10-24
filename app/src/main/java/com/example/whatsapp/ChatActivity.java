package com.example.whatsapp;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.whatsapp.adapter.MensagensAdapter;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Mensagem;
import com.example.whatsapp.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private Usuario usuarioDestinatario;
    private EditText editMensagem;
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;
    private DatabaseReference msgDatabase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewNome = findViewById(R.id.textViewnomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageViewFotoChat);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);

        idUsuarioRemetente = UsuarioFirebase.getIdentificadorUsuario();


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
            textViewNome.setText(usuarioDestinatario.getNome());

            String foto = usuarioDestinatario.getFoto();

            if (foto != null) {
                Uri uri = Uri.parse(usuarioDestinatario.getFoto());
                Glide.with(ChatActivity.this).load(uri).into(circleImageViewFoto);
            } else {
                circleImageViewFoto.setImageResource(R.drawable.padrao);
            }

            idUsuarioDestinatario = Base64Custom.codificarBase64(usuarioDestinatario.getEmail());
        }

        adapter = new MensagensAdapter(mensagens, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapter);

    }

    public void enviarMensagem(View view) {
        String textoMensagem = editMensagem.getText().toString();

        if (!textoMensagem.isEmpty()) {

            Mensagem mensagem = new Mensagem();
            mensagem.setIdUsuario(idUsuarioRemetente);
            mensagem.setMensagem(textoMensagem);

            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

        } else {
            Toast.makeText(ChatActivity.this, "Digite uma mensagem para enviar", Toast.LENGTH_LONG).show();
        }
    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem msg) {

        DatabaseReference mensagemRef = msgDatabase.child("mensagens");

        mensagemRef.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(msg);


        editMensagem.setText("");

    }
}