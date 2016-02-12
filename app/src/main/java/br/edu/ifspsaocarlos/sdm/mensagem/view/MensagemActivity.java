package br.edu.ifspsaocarlos.sdm.mensagem.view;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifspsaocarlos.sdm.mensagem.R;
import br.edu.ifspsaocarlos.sdm.mensagem.adapter.MensagemArrayAdapter;
import br.edu.ifspsaocarlos.sdm.mensagem.data.ContatoDAO;
import br.edu.ifspsaocarlos.sdm.mensagem.data.MensagemDAO;
import br.edu.ifspsaocarlos.sdm.mensagem.model.Contato;
import br.edu.ifspsaocarlos.sdm.mensagem.model.Mensagem;

public class MensagemActivity extends ListActivity implements View.OnClickListener {
    ContatosActivity contatosActivity = new ContatosActivity();
    Contato contato= new Contato();
    ContatoDAO contatoDAO;
    MensagemDAO mensagemDAO;
    MainActivity mainActivity = new MainActivity();
    List<Mensagem> mensagemArray;
    Button btEnviar;
    EditText etMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mensagem);

        contatoDAO = new ContatoDAO(this);
        contatoDAO.open();
        mensagemDAO = new MensagemDAO(this);
        mensagemDAO.open();

        Bundle b = getIntent().getExtras();
        if(b != null){
            String id = (String) b.get("notif");
            buscaMensagensNotificacao(id);
        }

        setTitle(contatosActivity.contato.getNomeCompleto());

        listaMensagens();

        btEnviar = (Button) findViewById(R.id.bt_enviar_mensagem);
        etMensagem = (EditText) findViewById(R.id.et_mensagem);
        btEnviar.setOnClickListener(MensagemActivity.this);
    }

    public void listaMensagens(){
        try{
            if(contatosActivity.contato.getApelido().equals("btpp_grp")){
                mensagemArray = mensagemDAO.buscaTodosMensagensGrupo(contatosActivity.contato.getId());

            }else{
                mensagemArray = mensagemDAO.buscaTodosMensagens(mainActivity.meuContato.getId(), contatosActivity.contato.getId());
            }
            MensagemArrayAdapter adapter = new MensagemArrayAdapter(this, mensagemArray);
            setListAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(MensagemActivity.this, "Erro ao listar mensagens!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        String msg = etMensagem.getText().toString();
        if(msg.equals("")){
            Toast.makeText(MensagemActivity.this, "Por favor, insira uma mensagem!",
                    Toast.LENGTH_SHORT).show();
        }else{
            final String origem = mainActivity.meuContato.getId();
            final String destino = contatosActivity.contato.getId();

            Mensagem mensagem = new Mensagem();
            mensagem.setAssunto("msg_abner");
            mensagem.setCorpo(msg);
            mensagem.setDestino(destino);
            mensagem.setOrigem(origem);
            mensagem.setOrigem_nome(mainActivity.meuContato.getNomeCompleto());

            enviarMensagem(mensagem);
        }
    }
    private void enviarMensagem(final Mensagem mensagem){
        RequestQueue queue = Volley.newRequestQueue(MensagemActivity.this);
        String url = getString(R.string.url_base) + "/mensagem";
        try {
            Map<String,String> params = new HashMap<String, String>();
            params.put("origem_id", mensagem.getOrigem());
            params.put("destino_id", mensagem.getDestino());
            params.put("assunto", mensagem.getAssunto());
            params.put("corpo", mensagem.getCorpo());
            params.put("origem_nome", mensagem.getOrigem_nome());
            JSONObject jsonObject = new JSONObject(params);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject s) {
                            Log.e("Mensagem enviada", "SUCESSO "+ s.toString());
                            etMensagem.setText("");
                            mensagemDAO.createMensagem(mensagem);
                            //buscaMensagens();
                            listaMensagens();
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("Cadastro Erro - Volley", "Erro na recuperação das Mensagens!");
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.e("SDM", "Erro ao enviar mensagem, verifique sua conexão!");
        }
    }

    public void buscaMensagensNotificacao(String id) {
        contato = contatoDAO.buscaContato(id);

        contatosActivity.contato.setId(contato.getId());
        contatosActivity.contato.setNomeCompleto(contato.getNomeCompleto());
        contatosActivity.contato.setApelido(contato.getApelido());

        String origem = mainActivity.meuContato.getId();
        String destino = contatosActivity.contato.getId();
        String ultimaMensagem = "0";

        RequestQueue queue = Volley.newRequestQueue(MensagemActivity.this);
        String url = getString(R.string.url_base) + "/mensagem" +
                "/" + ultimaMensagem + "/" + origem + "/" + destino;
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject s) {
                            Mensagem mensagem = new Mensagem();
                            JSONArray jsonArray;
                            try {
                                jsonArray = s.getJSONArray("mensagens");
                                for (int indice = 0; indice < jsonArray.length(); indice++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(indice);
                                    if(jsonObject.getString("assunto").equals("msg_abner")) {
                                        mensagem.setId(jsonObject.getString("id"));
                                        mensagem.setAssunto(jsonObject.getString("assunto"));
                                        mensagem.setCorpo(jsonObject.getString("corpo"));
                                        mensagem.setOrigem(jsonObject.getString("origem_id"));
                                        mensagem.setDestino(jsonObject.getString("destino_id"));
                                        Mensagem busca = mensagemDAO.buscaMensagem(mensagem.getId());
                                        try{
                                            mensagemDAO.createMensagem(mensagem);
                                        }catch (Exception e){
                                            Log.e("Cadastro Erro - JSON", "Erro ao salvar a mensagem no BD!");
                                        }
                                    }
                                }
                            }catch (JSONException je) {
                                Log.e("Cadastro Erro - JSON", "Erro na conversão de objeto JSON!");
                            }
                            listaMensagens();
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("Cadastro Erro - Volley", "Erro na recuperação das Mensagens!");
                }
            });
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.e("SDM", "Erro na leitura de mensagens");
        }
    }
}