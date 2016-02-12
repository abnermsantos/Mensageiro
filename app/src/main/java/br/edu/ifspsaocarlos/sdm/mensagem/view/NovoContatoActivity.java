package br.edu.ifspsaocarlos.sdm.mensagem.view;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifspsaocarlos.sdm.mensagem.R;
import br.edu.ifspsaocarlos.sdm.mensagem.adapter.ContatoArrayAdapter;
import br.edu.ifspsaocarlos.sdm.mensagem.adapter.ContatoArraySelectAdapter;
import br.edu.ifspsaocarlos.sdm.mensagem.data.ContatoDAO;
import br.edu.ifspsaocarlos.sdm.mensagem.model.Contato;

public class NovoContatoActivity extends ListActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    MainActivity mainActivity = new MainActivity();
    ContatoDAO contatoDAO;
    List<Contato> contatosArray;
    Button btCadastrar;
    EditText etNome;
    EditText etApelido;
    CheckBox ckGrupo;
    String nome;
    String apelido;
    TextView tv01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_contato);

        contatoDAO = new ContatoDAO(this);
        contatoDAO.open();

        ckGrupo = (CheckBox) findViewById(R.id.ck_grupo);
        btCadastrar = (Button) findViewById(R.id.bt_cadastrar_contato);
        etNome = (EditText) findViewById(R.id.editText1);
        etApelido = (EditText) findViewById(R.id.editText2);
        tv01 = (TextView) findViewById(R.id.textView3);
        btCadastrar.setOnClickListener(NovoContatoActivity.this);
        ckGrupo.setOnCheckedChangeListener(NovoContatoActivity.this);

    }

    @Override
    public void onClick(View v) {
        nome = etNome.getText().toString();
        apelido = etApelido.getText().toString();
        if(nome.equals("") || apelido.equals("")){
            Toast.makeText(NovoContatoActivity.this, "Por favor, insira os dados corretamente!",
                    Toast.LENGTH_SHORT).show();
        }else{
            RequestQueue queue = Volley.newRequestQueue(NovoContatoActivity.this);
            String url = getString(R.string.url_base) + "/contato";
            try {
                Map<String,String> params = new HashMap<String, String>();
                params.put("nome_completo", nome);
                params.put("apelido", apelido);
                JSONObject jsonObject = new JSONObject(params);

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject s) {
                                Log.e("Contato Cadastrado", "SUCESSO " + s.toString());
                                etNome.setText("");
                                etApelido.setText("");
                                Toast.makeText(NovoContatoActivity.this, "Contato Cadastrado com Sucesso!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Cadastro Erro - Volley", "Erro no Cadastro!");
                    }
                });
                queue.add(jsonObjectRequest);
            } catch (Exception e) {
                Log.e("SDM", "Erro ao cadastrar contato, verifique sua conexão!");
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(ckGrupo.isChecked()){
            etApelido.setEnabled(false);
            etApelido.setText("btpp_grp");
            //apelido = "abner_grupo";

            //tv01.setVisibility(View.VISIBLE);
            //listaContatos();

        }else{
            etApelido.setEnabled(true);
            etNome.setText("");
            etApelido.setText("");
            setListAdapter(null);
            tv01.setVisibility(View.INVISIBLE);
        }
    }

    public void listaContatos(){
        try{
            contatosArray = contatoDAO.buscaTodosContatos(mainActivity.meuContato.getId());
            ContatoArraySelectAdapter adapter = new ContatoArraySelectAdapter(this, contatosArray);
            setListAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(NovoContatoActivity.this, "Erro ao listar contatos!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}