package br.edu.ifspsaocarlos.sdm.mensagem.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.mensagem.R;
import br.edu.ifspsaocarlos.sdm.mensagem.model.Contato;

/**
 * Created by Abner - Manutençao on 29/06/2015.
 */
public class ContatoArrayAdapter extends ArrayAdapter<Contato> {
    private LayoutInflater inflater;

    public ContatoArrayAdapter(Activity activity, List<Contato> objects) {
        super(activity, R.layout.contato_celula, objects);
        this.inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contato_celula, null);
            holder = new ViewHolder();
            holder.nome = (TextView) convertView.findViewById(R.id.nome);
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
    }
}
