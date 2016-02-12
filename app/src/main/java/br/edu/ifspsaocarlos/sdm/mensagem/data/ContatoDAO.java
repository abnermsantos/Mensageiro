package br.edu.ifspsaocarlos.sdm.mensagem.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.mensagem.model.Contato;

/**
 * Created by Abner - Manutençao on 28/06/2015.
 */
public class ContatoDAO {
    private Context context;
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public ContatoDAO(Context context) {
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

    public List<Contato> buscaTodosContatos(String id) {

        List<Contato> contacts = new ArrayList<Contato>();

        Cursor cursor = database.query(SQLiteHelper.DATABASE_TABLE, new String[] { SQLiteHelper.KEY_ID,
                        SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_APELIDO}, SQLiteHelper.KEY_ID + "!=?", new String[] { id },
                null, null, SQLiteHelper.KEY_NAME);

        if (cursor!=null)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Contato contato = new Contato();
                contato.setId(cursor.getString(0));
                contato.setNomeCompleto(cursor.getString(1));
                contato.setApelido(cursor.getString(2));
                contacts.add(contato);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return contacts;
    }

    public  Contato  buscaContato(String busca)
    {

        Contato contact = new Contato();

        Cursor cursor = database.query(SQLiteHelper.DATABASE_TABLE, new String[] { SQLiteHelper.KEY_ID,
                        SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_APELIDO}, SQLiteHelper.KEY_ID + "=?", new String[] { busca },
                null, null, SQLiteHelper.KEY_ID);

        if (cursor!=null)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contact.setId(cursor.getString(0));
                contact.setNomeCompleto(cursor.getString(1));
                contact.setApelido(cursor.getString(2));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return contact;
    }

    public void updateContact(Contato c) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(SQLiteHelper.KEY_NAME, c.getNomeCompleto());
        database.update(SQLiteHelper.DATABASE_TABLE, updateValues, SQLiteHelper.KEY_ID + "=" + c.getId(), null);
    }

    public void createContact( Contato c) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_ID, c.getId());
        values.put(SQLiteHelper.KEY_NAME, c.getNomeCompleto());
        values.put(SQLiteHelper.KEY_APELIDO, c.getApelido());
        database.insert(SQLiteHelper.DATABASE_TABLE, null, values);
    }

    public void deleteContact(Contato c)
    {
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID +"="+ c.getId(), null);

    }
}
