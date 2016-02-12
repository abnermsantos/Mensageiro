package br.edu.ifspsaocarlos.sdm.mensagem.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.mensagem.R;
import br.edu.ifspsaocarlos.sdm.mensagem.model.Mensagem;
import br.edu.ifspsaocarlos.sdm.mensagem.view.ContatosActivity;
import br.edu.ifspsaocarlos.sdm.mensagem.view.MainActivity;

/**
 * Created by Abner - Manutençao on 30/06/2015.
 */
public class MensagemArrayAdapter extends ArrayAdapter<Mensagem>{
    private LayoutInflater inflater;
    MainActivity mainActivity = new MainActivity();
    ContatosActivity contatosActivity = new ContatosActivity();

    public MensagemArrayAdapter(Activity activity, List<Mensagem> objects) {
        super(activity, R.layout.mensagem_celula, objects);
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mensagem_celula, null);
            holder = new ViewHolder();
            holder.origem = (TextView) convertView.findViewById(R.id.tv_origem);
            convertView.setTag(holder);
            holder.corpo = (TextView) convertView.findViewById(R.id.tv_corpo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Mensagem m = getItem(position);
        /*if(m.getOrigem().equals(mainActivity.meuContato.getId())){
            holder.origem.setText(mainActivity.meuContato.getNomeCompleto() + " diz:");
        }else if(m.getOrigem().equals(contatosActivity.contato.getId())){
            holder.origem.setText(contatosActivity.contato.getNomeCompleto() + " diz:");
        }*/
        holder.origem.setText(m.getOrigem_nome() + " diz:");
        holder.corpo.setText(m.getCorpo());
        return convertView;
    }

    static class ViewHolder {
        public TextView origem;
        public TextView corpo;
    }
}

