package jp.ac.asojuku.st.familyapps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Roulette extends AppCompatActivity {
    String result = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rolette);

        Intent intent = getIntent();
        String data = intent.getStringExtra("a");
        data = data + data;
        data = data + data;
        data = data + data;
        String[] food = data.split(" ", 0);
        // 配列からListへ変換します。
        List<String> list=Arrays.asList(food);

        // リストの並びをシャッフルします。
        Collections.shuffle(list);

        // listから配列へ戻します。
        String[] array2 =(String[])list.toArray(new String[list.size()]);

        // シャッフルされた配列の先頭を取得します。
        result = array2[0];

        System.out.println("シャッフルされた配列の先頭 : " + result);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cardList);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llManager = new LinearLayoutManager(this);
        //縦スクロール
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llManager);

        ArrayList<AnbayasiData> anbayasi = new ArrayList<AnbayasiData>();
        for(int i = array2.length-1;i >=0;i--){
            anbayasi.add(new AnbayasiData(
                    array2[i]
            ));
        }

        RecyclerView.Adapter adapter = new AnbayasiAdapter(anbayasi);
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(anbayasi.size()-1); //最後までスクロール
        Toast.makeText(getApplicationContext(), "今日の夕飯は 「"+result+"」 に決定しました",
                Toast.LENGTH_LONG).show();

        Button mail = (Button) findViewById(R.id.button2);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> emails = new ArrayList<String>() {
                    {
                        add("papa@s.co.jp");
                        add("mama@s.co.jp");
                        add("baba@s.co.jp");
                    }
                };
                StringBuilder mailTo = new StringBuilder();
                mailTo.append("mailto:");
                for (String email : emails) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        //正しいメアドの時だけ追加する
                        mailTo.append(email);
                        mailTo.append(",");
                    }
                }
//メール送信起動
// インテントのインスタンス生成
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_SENDTO);
//宛先の設定
                intent2.setData(Uri.parse(mailTo.toString()));
//件名の設定
                intent2.putExtra(Intent.EXTRA_SUBJECT, "夕飯");
//本文の設定
                intent2.putExtra(Intent.EXTRA_TEXT, result);
// メール起動
                startActivity(intent2);
            }
        });
    }
}