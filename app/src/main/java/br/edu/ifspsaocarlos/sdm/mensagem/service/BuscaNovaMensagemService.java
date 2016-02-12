package br.edu.ifspsaocarlos.sdm.mensagem.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
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
import java.util.List;

import br.edu.ifspsaocarlos.sdm.mensagem.R;
import br.edu.ifspsaocarlos.sdm.mensagem.data.ContatoDAO;
import br.edu.ifspsaocarlos.sdm.mensagem.model.Contato;
import br.edu.ifspsaocarlos.sdm.mensagem.view.MainActivity;
import br.edu.ifspsaocarlos.sdm.mensagem.view.MensagemActivity;

/**
 * Created by Abner - Manutençao on 03/07/2015.
 */
public class BuscaNovaMensagemService extends Service implements Runnable {
    private boolean appAberta;
    private boolean primeiraBusca;
    private static int ultimoNumeroMensagens;
    private static int novoNumeroMensagens;
    private static int origem;
    List<Contato> contatosArray;
    MensagemActivity mensagemActivity = new MensagemActivity();
    MainActivity mainActivity = new MainActivity();
    ContatoDAO contatoDAO;

    public BuscaNovaMensagemService() {
    }
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void onCreate() {
        super.onCreate();

        contatoDAO = new ContatoDAO(this);
        contatoDAO.open();

        appAberta = true;
        primeiraBusca = true;
        ultimoNumeroMensagens = 0;
        origem = 1;
        new Thread(this).start();
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    public void run() {
        while (appAberta) {
            try {
                Thread.sleep(getResources().getInteger(R.integer.tempo_inatividade_servico));
                buscaNumeroMensagens();
                if (!primeiraBusca && novoNumeroMensagens > ultimoNumeroMensagens) {
                    NotificationManager nm = (NotificationManager)
                            getSystemService(NOTIFICATION_SERVICE);
                    Intent intent = new Intent(this, MensagemActivity.class);
                    intent.putExtra("notif", String.valueOf(origem));
                    PendingIntent p = PendingIntent.getActivity(this, 0, intent, 0);
                    Notification.Builder builder = new Notification.Builder(this);
                    builder.setSmallIcon(R.drawable.ic_mensageiro);
                    builder.setTicker(getString(R.string.nova_mensagem_recebida));
                    builder.setContentTitle(getString(R.string.nova_mensagem));
                    builder.setContentText("Pedro, não terminei isso.");
                    //builder.setContentText(getString(R.string.clique_aqui));
                    builder.setWhen(System.currentTimeMillis());
                    builder.setDefaults(Notification.DEFAULT_SOUND);
                    builder.setContentIntent(p);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.drawable.ic_mensageiro));
                    Notification notification = builder.build();
                    notification.vibrate = new long[] {100, 250};
                    nm.notify(R.mipmap.ic_launcher, notification);
                }
                ultimoNumeroMensagens = novoNumeroMensagens;
                primeiraBusca = false;
            }
            catch (InterruptedException ie) {
                Log.e("SDM", "Erro na thread de recuperação de mensagem");
            }
        }
    }

    private void buscaNumeroMensagens() {
        RequestQueue queue = Volley.newRequestQueue(BuscaNovaMensagemService.this);
        String url = getString(R.string.url_base) + "/mensagem" +
                "/" + ultimoNumeroMensagens + "/" + origem + "/" + mainActivity.meuContato.getId();
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject s) {
                            JSONArray jsonArray;
                            try {
                                jsonArray = s.getJSONArray("mensagens");
                                for (int indice = 0; indice < jsonArray.length(); indice++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(indice);
                                    if (jsonObject.getString("destino_id").equals(mainActivity.meuContato.getId()) &&
                                            jsonObject.getString("assunto").equals("msg_abner")) {
                                        novoNumeroMensagens = jsonArray.length();
                                    }
                                }
                            }
                            catch (JSONException je) {
                                Toast.makeText(BuscaNovaMensagemService.this,
                                        "Erro na conversão de objeto JSON!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(BuscaNovaMensagemService.this,
                            "Erro na recuperação do número de mensagens!", Toast.LENGTH_SHORT).show();
                }
            });
            int numeroContatos = buscaNumeroContatos();
            if(origem < numeroContatos){
                origem += 1;
            }else{
                origem = 1;
            }
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int buscaNumeroContatos() {
        int numeroContatos = 0;
        try{
            contatosArray = contatoDAO.buscaTodosContatos(mainActivity.meuContato.getId());
            for(int indice = 0; indice < contatosArray.size(); indice++){
                if(numeroContatos < Integer.parseInt(contatosArray.get(indice).getId())){
                    numeroContatos = Integer.parseInt(contatosArray.get(indice).getId());
                }
            }
        }catch (Exception e){
            Log.i("Erro Server", "Erro ao buscar numero de contatos");
        }
        return  numeroContatos;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appAberta = false;
        stopSelf();
    }
}
