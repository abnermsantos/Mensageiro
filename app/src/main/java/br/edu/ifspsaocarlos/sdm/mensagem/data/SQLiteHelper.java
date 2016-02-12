package br.edu.ifspsaocarlos.sdm.mensagem.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Abner - Manutençao on 28/06/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mensageiro.db";
    public static final String DATABASE_TABLE = "contato";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "nome";
    public static final String KEY_APELIDO = "apelido";

    public static final String DATABASE_TABLE2 = "mensagem";

    public static final String KEY_ID_MENSAGEM = "id";
    public static final String KEY_ORIGEM = "origem";
    public static final String KEY_DESTINO = "destino";
    public static final String KEY_ASSUNTO = "assunto";
    public static final String KEY_CORPO = "corpo";
    public static final String KEY_ORIGEM_NOME = "origem_nome";

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_CREATE = "CREATE TABLE "+ DATABASE_TABLE +" " +
            "("+ KEY_ID  + " INTEGER PRIMARY KEY, " + KEY_NAME +" TEXT NOT NULL, " + KEY_APELIDO +" TEXT NOT NULL);";

    public static final String DATABASE_CREATE2 = "CREATE TABLE "+ DATABASE_TABLE2 +
            " ("+ KEY_ID_MENSAGEM  + " INTEGER PRIMARY KEY, " + KEY_ORIGEM +" TEXT NOT NULL, " +
            KEY_DESTINO +" TEXT NOT NULL, "+ KEY_ASSUNTO +" TEXT NOT NULL, "+ KEY_CORPO +" TEXT NOT NULL, "+
            KEY_ORIGEM_NOME +" TEXT);";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATABASE_CREATE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int 	newVersion) {
        onCreate(database);
    }
}
