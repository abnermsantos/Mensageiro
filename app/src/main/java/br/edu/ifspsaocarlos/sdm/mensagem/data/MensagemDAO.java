package br.edu.ifspsaocarlos.sdm.mensagem.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.mensagem.model.Mensagem;

/**
 * Created by Abner - Manutençao on 30/06/2015.
 */
public class MensagemDAO {
    private Context context;
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public MensagemDAO(Context context) {
        this.context = context;
    }

    public void open() throws SQLException {
        dbHelper = new SQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        database.close();
    }

    public List<Mensagem> buscaTodosMensagensGrupo(String destino) {

        List<Mensagem> mensagens = new ArrayList<Mensagem>();

        Cursor cursor = database.query(SQLiteHelper.DATABASE_TABLE2, new String[] { SQLiteHelper.KEY_ID_MENSAGEM,
                        SQLiteHelper.KEY_ORIGEM, SQLiteHelper.KEY_DESTINO, SQLiteHelper.KEY_ASSUNTO,
                        SQLiteHelper.KEY_CORPO, SQLiteHelper.KEY_ORIGEM_NOME}, SQLiteHelper.KEY_DESTINO + "=?",
                new String[] {destino}, null, null, SQLiteHelper.KEY_ID_MENSAGEM);

        if (cursor!=null)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Mensagem mensagem = new Mensagem();
                mensagem.setId(cursor.getString(0));
                mensagem.setOrigem(cursor.getString(1));
                mensagem.setDestino(cursor.getString(2));
                mensagem.setAssunto(cursor.getString(3));
                mensagem.setCorpo(cursor.getString(4));
                mensagem.setOrigem_nome(cursor.getString(5));
                mensagens.add(mensagem);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return mensagens;
    }


    public List<Mensagem> buscaTodosMensagens(String origem, String destino) {

        List<Mensagem> mensagens = new ArrayList<Mensagem>();

        Cursor cursor = database.query(SQLiteHelper.DATABASE_TABLE2, new String[] { SQLiteHelper.KEY_ID_MENSAGEM,
                        SQLiteHelper.KEY_ORIGEM, SQLiteHelper.KEY_DESTINO, SQLiteHelper.KEY_ASSUNTO, SQLiteHelper.KEY_CORPO,
                        SQLiteHelper.KEY_ORIGEM_NOME}, SQLiteHelper.KEY_ORIGEM + "=? AND " + SQLiteHelper.KEY_DESTINO + "=? OR " +
                        SQLiteHelper.KEY_ORIGEM + "=? AND " + SQLiteHelper.KEY_DESTINO + "=?",
                new String[] {origem, destino, destino, origem}, null, null, SQLiteHelper.KEY_ID_MENSAGEM);

        if (cursor!=null)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Mensagem mensagem = new Mensagem();
                mensagem.setId(cursor.getString(0));
                mensagem.setOrigem(cursor.getString(1));
                mensagem.setDestino(cursor.getString(2));
                mensagem.setAssunto(cursor.getString(3));
                mensagem.setCorpo(cursor.getString(4));
                mensagem.setOrigem_nome(cursor.getString(5));
                mensagens.add(mensagem);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return mensagens;
    }

    public  Mensagem  buscaMensagem(String id) {

        Mensagem mensagem = new Mensagem();

        Cursor cursor = database.query(SQLiteHelper.DATABASE_TABLE2, new String[] { SQLiteHelper.KEY_ID_MENSAGEM,
                        SQLiteHelper.KEY_ORIGEM, SQLiteHelper.KEY_DESTINO, SQLiteHelper.KEY_ASSUNTO, SQLiteHelper.KEY_CORPO,
                        SQLiteHelper.KEY_ORIGEM_NOME}, SQLiteHelper.KEY_ID_MENSAGEM + "=?", new String[] { id }, null, null,
                SQLiteHelper.KEY_ID_MENSAGEM);

        if (cursor!=null)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mensagem.setId(cursor.getString(0));
                mensagem.setOrigem(cursor.getString(1));
                mensagem.setDestino(cursor.getString(2));
                mensagem.setAssunto(cursor.getString(3));
                mensagem.setCorpo(cursor.getString(4));
                mensagem.setOrigem_nome(cursor.getString(5));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return mensagem;
    }

    public void createMensagem( Mensagem m) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_ID_MENSAGEM, m.getId());
        values.put(SQLiteHelper.KEY_ORIGEM, m.getOrigem());
        values.put(SQLiteHelper.KEY_DESTINO, m.getDestino());
        values.put(SQLiteHelper.KEY_ASSUNTO, m.getAssunto());
        values.put(SQLiteHelper.KEY_CORPO, m.getCorpo());
        values.put(SQLiteHelper.KEY_ORIGEM_NOME, m.getOrigem_nome());
        database.insert(SQLiteHelper.DATABASE_TABLE2, null, values);
    }
}
