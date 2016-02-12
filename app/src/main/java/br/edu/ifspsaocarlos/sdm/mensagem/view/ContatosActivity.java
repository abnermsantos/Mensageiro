package br.edu.ifspsaocarlos.sdm.mensagem.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.List;

import br.edu.ifspsaocarlos.sdm.mensagem.R;
import br.edu.ifspsaocarlos.sdm.mensagem.adapter.ContatoArrayAdapter;
import br.edu.ifspsaocarlos.sdm.mensagem.data.ContatoDAO;
import br.edu.ifspsaocarlos.sdm.mensagem.data.MensagemDAO;
import br.edu.ifspsaocarlos.sdm.mensagem.model.Contato;
import br.edu.ifspsaocarlos.sdm.mensagem.model.Mensagem;


/**
 * Created by Abner - Manutençao on 23/06/2015.
 */
public class ContatosActivity extends ListActivity implements View.OnClickListener{
    ContatoDAO contatoDAO;
    MensagemDAO mensagemDAO;
    MainActivity mainActivity = new MainActivity();
    List<Contato> contatosArray;
    TextView tv_novo_contato;
    public static Contato contato = new Contato();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        tv_novo_contato = (TextView) findViewById(R.id.tv_novo_contato);
        tv_novo_contato.setOnClickListener(ContatosActivity.this);

        contatoDAO = new ContatoDAO(this);
        contatoDAO.open();
        mensagemDAO = new MensagemDAO(this);
        mensagemDAO.open();

        listaContatos();
    }

    public void listaContatos(){
        try{
            contatosArray = contatoDAO.buscaTodosContatos(mainActivity.meuContato.getId());
            ContatoArrayAdapter adapter = new ContatoArrayAdapter(this, contatosArray);
            setListAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(ContatosActivity.this, "Erro ao listar contatos!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onListItemClick(ListView l, View v, int position, long id) {

        for (int indice = 0; indice < contatosArray.size(); indice++){
            if(contatosArray.get(indice).equals(getListAdapter().getItem(position))) {
                contato.setId(contatosArray.get(indice).getId());
                contato.setNomeCompleto(contatosArray.get(indice).getNomeCompleto());
                contato.setApelido(contatosArray.get(indice).getApelido());
            }
        }

        buscaMensagens();
    }

    private void buscaMensagens() {

        String origem = mainActivity.meuContato.getId();
        String destino = contato.getId();
        String ultimaMensagem = "0";

        RequestQueue queue = Volley.newRequestQueue(ContatosActivity.this);
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
                            Intent mostraMensagensIntent = new Intent(ContatosActivity.this,
                                    MensagemActivity.class);
                            startActivity(mostraMensagensIntent);
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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ContatosActivity.this, NovoContatoActivity.class);
        startActivity(intent);
    }
}