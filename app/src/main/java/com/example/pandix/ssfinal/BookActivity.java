package com.example.pandix.ssfinal;

/**
 * Created by pandix on 2016/6/20.
 */

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookActivity extends Activity {

    private ImageView bg;
    private Button bt;
    private EditText book_name;

    private ListView listv;
    List<Result> result_list = new ArrayList<Result>();
    private MyAdapter adapter;

    private ArrayList<String> books = new ArrayList<String>(50);
    private ArrayList<String> catorgory = new ArrayList<String>(50);
    private ArrayList<String> author = new ArrayList<String>(50);
    private ArrayList<String> index = new ArrayList<String>(50);
    private ArrayList<String> site = new ArrayList<String>(50);

    private int books_num = 0;
    private int catorgory_num = 0;
    private int author_num = 0;
    private int index_num = 0;
    private int site_num = 0;


    String url = "http://webpac.lib.nthu.edu.tw/F?func=find-b&local_base=TOP01&find_code=WRD&request=%20";
    String title;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);
        bg = (ImageView) findViewById(R.id.imageView);
        bt = (Button) findViewById(R.id.button);
        book_name = (EditText) findViewById(R.id.editText);
        listv = (ListView)findViewById(R.id.listView);



        bt.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View view){
                        Toast.makeText(getApplication(), book_name.getText().toString(), Toast.LENGTH_LONG).show();
                        url = url + book_name.getText().toString();
                        Toast.makeText(getApplication(), url, Toast.LENGTH_LONG).show();
                        new Thread(runnable).start();
                    }
                }
        );
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    Runnable runnable = new Runnable(){
        @Override
        public void run(){
            try{
                Document doc = Jsoup.connect(url).get();
                Elements n = doc.select("tr[valign=baseline]");
                Elements name = n.select("script");
                Elements site_e = n.select("td[width=20%]");
                for(Element e : site_e.select("script")){
                    e.remove();
                }
                //String q = n.select("td[width=20%").text();
                //System.out.print(site_e.html());
                //System.out.println(q);
                Pattern site_p = Pattern.compile("</script>\n.*\n?<script>");
                Matcher site_m = site_p.matcher(site_e.html());
                updateSite(site_e, site_m);
                //System.out.print(n.html());
                Pattern p = Pattern.compile("var title = \'.*\'?;"); // Regex for the value of the key
                Matcher m = p.matcher(name.html());
                updateBookName(m);

                Pattern cator = Pattern.compile("var format = \'.*\'?;");
                Matcher cator_m = cator.matcher(name.html());
                updateCatorgary(cator_m);

                Pattern authorAndIndex = Pattern.compile("<td width=\"15%\" valign=\"top\">.*?</td>");
                Matcher authorAndIndex_m = authorAndIndex.matcher(n.html());
                updateAuthorAndIndex(authorAndIndex_m);

                updateResult();
                adapter = new MyAdapter(BookActivity.this, result_list);
                listv.setAdapter(adapter);
                print_result();

            }catch(IOException e){
                e.printStackTrace();
            }
            // handler.sendEmptyMessage(0);
        }
    };

    public void updateCatorgary(Matcher m){
        String cat;
        String result;

        catorgory_num = 0;
        while(m.find()){
            cat = m.group().substring(14, 16);
            if(cat.equals("BK")){
                result = "圖書";
            }else if(cat.equals("SE")){
                result = "期刊";
            }else if(cat.equals("AV")){
                result = "視聽資料";
            }else if(cat.equals("VM")){
                result = "視聽資料";
            }else if(cat.equals("CF")){
                result = "電腦檔";
            }else if(cat.equals("DI")){
                result = "博碩士論文";
            }else if(cat.equals("FR")){
                result = "教室指定參考書";
            }else if(cat.equals("MP")){
                result = "地圖";
            }else if(cat.equals("MU")){
                result = "視聽資料";
            }else if(cat.equals("TR")){
                result = "技術報告";
            }else if(cat.equals("EB")){
                result = "電子書";
            }else if(cat.equals("ET")){
                result = "電子期刊";
            }else if(cat.equals("MX")){
                result = "綜合組件";
            }else if(cat.equals("MS")){
                result = "樂譜";
            }else if(cat.equals("MF")){
                result = "縮影資料";
            }else if(cat.equals("PP")){
                result = "報紙";
            }else{
                result = "圖書";
            }
            catorgory.add(catorgory_num++, result);
        }

    }

    public void updateBookName(Matcher m){
        String b;
        books_num = 0;
        while(m.find()){
            b =  m.group().substring(13, m.group().length()-2).replace("&nbsp;", " ");
            books.add(books_num++, b);
        }

    }

    public void updateAuthorAndIndex(Matcher m){
        String b;
        index_num = 0;
        author_num = 0;
        while(m.find()){
            //System.out.println(m.group());

            b = m.group().substring(29, m.group().length()-5);
            if(author_num > index_num){

                if(b.equals("<br>")){
                    index.add(index_num++, "");
                }else{
                    index.add(index_num++, b);
                }
            }else{
                if(b.equals("<br>")){
                    author.add(author_num++, "");
                }else{
                    author.add(author_num++, b);
                }
            }
        }


    }

    public void updateSite(Elements e, Matcher m){
        String s;
        site_num = 0;

        for(Element element : e){
            if(element.html().equals("<br>")){
                site.add(site_num++, "");
            }else if(element.toString().equals("<td width=\"20%\" valign=\"top\">  </td>")){

            }else{
                site.add(site_num++, element.text());
            }
            // System.out.println(element.html());
        }


    }

    public void print_result(){
        System.out.println("book_num = " + Integer.toString(books_num));
        System.out.println("author_num = " + Integer.toString(author_num));
        System.out.println("catorgary_num = " + Integer.toString(catorgory_num));
        System.out.println("index_num = " + Integer.toString(index_num));
        System.out.println("site_num = " + Integer.toString(site_num));

    }

    public void updateResult(){
        if(books_num==author_num && author_num==catorgory_num && catorgory_num==site_num && site_num==index_num && books_num!=0){
            for(int i = 0; i < books_num; i++){
                result_list.add(new Result(books.get(i), author.get(i), catorgory.get(i), site.get(i), index.get(i)));
            }
        }else{

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Book Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.pandix.ssfinal/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Book Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.pandix.ssfinal/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
