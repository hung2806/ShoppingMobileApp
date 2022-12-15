package hanu.a2_1901040025.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import hanu.a2_1901040025.models.Product;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cart.db";
    private static final int VERSION = 1;
    public DbHelper(@Nullable Context context ) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cart("+
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "productId INTEGER NOT NULL," +
                        "name TEXT NOT NULL," +
                        "price DOUBLE NOT NULL, "+
                        "imgUrl TEXT NOT NULL," +
                        "quantity INTEGER  )");
        // add demo data
        //db.execSQL("insert into cart (id,productId, name, price,imgUrl, quantity) values(1,1, 'Duy Anh 345' , 120.0,'https://cf.shopee.vn/file/beca50e46d2088fc5ad3c74aff5cc112', 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE cart");

        onCreate(db);
    }
}
