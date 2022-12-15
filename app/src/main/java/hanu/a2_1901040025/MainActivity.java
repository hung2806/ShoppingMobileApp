package hanu.a2_1901040025;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import hanu.a2_1901040025.adapters.ProductAdapter;
import hanu.a2_1901040025.models.Constants;
import hanu.a2_1901040025.models.Product;

public class MainActivity extends AppCompatActivity {
    List<Product> products= new ArrayList<>();
    ProductAdapter productAdapter;
    RecyclerView listProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchProduct();

        getSupportActionBar().setTitle(Html.fromHtml("<font color =\"black\">" + getString(R.string.app_name) + "</font>" ));



        listProduct = findViewById(R.id.rvList);
        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        //load name and price
        Constants.executor.execute(new Runnable() {
            @Override
            public void run() {
                String api = "https://mpr-cart-api.herokuapp.com/products";
                String json = loadJSON(api);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (json == null) {
                            Toast.makeText(MainActivity.this, "Load product fail!", Toast.LENGTH_LONG).show();

                        } else {

                            try {
                                JSONArray jsonArray =  new JSONArray(json);

                                for (int i = 0; i < jsonArray.length(); i++ ) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    int productId = obj.getInt("id");
                                    String productName = obj.getString("name");
                                    double productPrice = Double.parseDouble(obj.getString("unitPrice"));
                                    String productThumbnail = obj.getString("thumbnail");


                                    products.add(new Product(productId, productName, productPrice, productThumbnail));
                                }
                                productAdapter = new ProductAdapter(products);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,2, GridLayoutManager.VERTICAL, false);
                                listProduct.setLayoutManager(gridLayoutManager);
                                listProduct.setAdapter(productAdapter);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

        //load thumbnail

//        listProduct = findViewById(R.id.rvList);
//
//
//        productAdapter = new ProductAdapter(products);
//
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false);
//        listProduct.setLayoutManager(gridLayoutManager);
//        listProduct.setAdapter(productAdapter);

    }

    public Bitmap loadImg(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            InputStream a = httpURLConnection.getInputStream();
            Bitmap bm = BitmapFactory.decodeStream(a);
            return  bm;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public String loadJSON(String link) {
        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while(sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            return result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void searchProduct() {
        SearchView search = findViewById(R.id.viewSearch);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String word) {
                List <Product> result = new ArrayList<>();
                for (Product product: products) {
                    if (product.getName().toLowerCase().contains(word.toLowerCase())) {
                        result.add(product);
                    }
                }
                productAdapter = new ProductAdapter(result);
                listProduct.setAdapter(productAdapter);

                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shopping_cart, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.cart) {

            Intent intent = new Intent(MainActivity.this, ShoppingCart.class);
            startActivity(intent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}