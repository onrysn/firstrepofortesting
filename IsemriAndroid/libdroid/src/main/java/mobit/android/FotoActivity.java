package mobit.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        String[] ulkeler =
                {"Türkiye", "Almanya", "Avusturya", "Amerika","İngiltere",
                        "Macaristan", "Yunanistan", "Rusya", "Suriye", "İran", "Irak",
                        "Şili", "Brezilya", "Japonya", "Portekiz", "İspanya",
                        "Makedonya", "Ukrayna", "İsviçre"};
        ListView fotoliste=(ListView) findViewById(R.id.liste);
        ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, ulkeler);

        fotoliste.setAdapter(veriAdaptoru);
    }
}
