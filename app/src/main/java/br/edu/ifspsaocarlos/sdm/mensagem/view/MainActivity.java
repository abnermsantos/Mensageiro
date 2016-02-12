package br.edu.ifspsaocarlos.sdm.mensagem.view;

import android.app.Activity;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.mensagem.R;
import br.edu.ifspsaocarlos.sdm.mensagem.adapter.ContatoArrayAdapter;
import br.edu.ifspsaocarlos.sdm.mensagem.data.ContatoDAO;
import br.edu.ifspsaocarlos.sdm.mensagem.model.Contato;

public class MainActivity extends Activity implements View.OnClickListener{
    private Intent serviceIntent;
    public static Contato meuContato = null;
    ContatoDAO contatoDAO;
    Button btEntrar;
    EditText etId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contatoDAO = new ContatoDAO(this);
        contatoDAO.open();

        buscaContatos();

        btEntrar = (Button) findViewById(R.id.bt_entrar);
        etId = (EditText) findViewById(R.id.et_id);
        btEntrar.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        String id = etId.getText().toString();
        meuContato = contatoDAO.buscaContato(id);
        if(id != null && id.matches("[0-9]+") && meuContato.getId() != null){
            serviceIntent = new Intent("BUSCAR_NOVA_MENSAGEM_SERVICE");
            startService(serviceIntent);

            Intent mostraContatosIntent = new Intent(MainActivity.this, ContatosActivity.class);
            startActivity(mostraContatosIntent);
            etId.setText("");
        }
        else{
            Toast.makeText(MainActivity.this, "ID incorreto! Tente Novamente.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void buscaContatos(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = getString(R.string.url_base) + "/contato";
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject s) {
                            JSONArray jsonArray;

                            try {
                                jsonArray = s.getJSONArray("contatos");
                                Contato contato = new Contato();
                                for (int indice = 0; indice < jsonArray.length(); indice++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(indice);
                                    contato.setId(jsonObject.getString("id"));
                                    contato.setNomeCompleto(jsonObject.getString("nome_completo"));
                                    contato.setApelido(jsonObject.getString("apelido"));
                                    try {

                                        Contato busca = contatoDAO.buscaContato(contato.getId());
                                        if(busca.getId() != contato.getId()){
                                            contatoDAO.createContact(contato);
                                        }
                                    } catch (Exception e) {
                                        //Log.e("Cadastro Erro", "Contato já cadastrado!");
                                    }
                                }
                            }catch (JSONException je) {
                                Log.e("Cadastro Erro - JSON", "Erro na conversão de objeto JSON!");
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("Cadastro Erro - Volley", "Erro na recuperação dos Contatos!");
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.e("Cadastro Erro - Request", "Erro na leitura dos Contatos!");
            Toast.makeText(MainActivity.this, "Erro ao ler contatos, verifique sua conexão.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void onDestroy() {
        stopService(serviceIntent);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();
        super.onDestroy();
    }

}