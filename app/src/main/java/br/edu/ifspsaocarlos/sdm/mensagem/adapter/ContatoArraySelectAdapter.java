package br.edu.ifspsaocarlos.sdm.mensagem.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.mensagem.R;
import br.edu.ifspsaocarlos.sdm.mensagem.model.Contato;

/**
 * Created by Abner - Manutençao on 04/07/2015.
 */
public class ContatoArraySelectAdapter extends ArrayAdapter<Contato> {
    private LayoutInflater inflater;

    public ContatoArraySelectAdapter(Activity activity, List<Contato> objects) {
        super(activity, R.layout.contato_celula_select, objects);
        this.inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contato_celula_select, null);
            holder = new ViewHolder();
            holder.nome = (TextView) convertView.findViewById(R.id.tv_nome);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.ck_nome);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contato c = getItem(position);
        holder.nome.setText(c.getNomeCompleto());
        return convertView;
    }

    static class ViewHolder {
        public TextView nome;
        public CheckBox checkbox;
    }
}

