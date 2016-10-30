package jp.ac.asojuku.st.familyapps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/*
 * ListViewのアイテムを追加，削除するクラス
 * テキストボックスでアイテムを入力する．
 * ListViewアイテムの長押しで当該アイテムを削除する．
 */
public class MainActivity extends Activity {

    private static final String DEBUG = "DEBUG";
    private ArrayAdapter<String> adapter = null;
    private Button _button = null;
    private ListView _listView = null;
    private Button next=null;
    private Button mailAdd=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // LayoutファイルのListViewのリソースID
        _listView = (ListView) findViewById(R.id.list_view);

        // Androidフレームワーク標準のレイアウト
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1);


        _listView.setAdapter(adapter);

        // ListViewアイテムを選択した場合の動作
        _listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 選択したListViewアイテムを表示する
                ListView list = (ListView) parent;
                String selectedItem = (String) list.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), selectedItem,
                        Toast.LENGTH_LONG).show();
                Log.d(DEBUG, selectedItem);
            }
        });

        // ListViewアイテムの長押しでListViewアイテムを削除する
        // リスナーはAdapaterView.onItemLongClickListener()を利用する
        // 利用しないとListViewのアイテムを取得できない
        _listView.setOnItemLongClickListener
                (new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view, int position, long id) {
                        ListView list = (ListView) parent;
                        String selectedItem = (String) list
                                .getItemAtPosition(position);
                        Log.d(DEBUG, "Long click : " + selectedItem);

                        showDialogFragment(selectedItem);
                        return false;
                    }
                });

        // EditTextのエントリをListViewアイテムに追加する
        _button = (Button) findViewById(R.id.btn);
        _button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.edit_text);
                String entry = editText.getText().toString();

                if (entry.equals("")) {
                    Log.d(DEBUG, "Entry is empty");
                } else {
                    addListData(entry);
                }
                // ボタン押下後のエントリ文字列を削除する
                editText.setText("");
            }
        });

        next = (Button) findViewById(R.id.button);
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = (ArrayAdapter)_listView.getAdapter();
                String a="";
                for(int i = 0, length = adapter.getCount(); i < length; i++) {
                    a = a+ adapter.getItem(i)+" ";
                }
                //明示的なIntentオブジェクトを生成する
                Intent intent = new Intent(MainActivity.this,Roulette.class);
                intent.putExtra("a",a);
                //startActivity(intent)メソッドで画面を遷移する
                startActivity(intent);
            }
        });

    }

    // ListViewアイテムにエントリを追加するメソッド
    private void addListData(String entry) {
        adapter.add(entry);
    }

    // FragmentManagerでDialogを管理するクラス
    private void showDialogFragment(String selectedItem) {
        FragmentManager manager = getFragmentManager();
        DeleteDialog dialog = new DeleteDialog();
        dialog.setSelectedItem(selectedItem);

        dialog.show(manager, "dialog");
    }

    /*
     * 削除ダイアログを生成する内部クラス
     * 内部クラスは外部クラスのインスタンスを直接参照できないため，
     * Activity#getActivity()で外部クラスのインスタンスを取得している．
     */
    public static class DeleteDialog extends DialogFragment {

        private static final String DEBUG = "DEBUG";
        /* 選択したListViewアイテム */
        private String selectedItem = null;

        // 削除ダイアログの作成．
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Log.d(DEBUG, "onCreateDialog()");

            Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Delete entry.");
            builder.setMessage("Are you really?");

            // positiveを選択した場合の処理．
            // リスナーはDialogINterface#onClickListener()
            // を使うことに注意．
            builder.setPositiveButton("Yes I'm serious.",
                    new DialogInterface.OnClickListener() {

                        // 外部クラスのインスタンスを直接参照することができないため，
                        // Activity#getActivity()でActivityのインスタンスを取得する
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity activity = (MainActivity) getActivity();
                            activity.removeItem(selectedItem);
                        }
                    });
            AlertDialog dialog = builder.create();
            return dialog;
        }

        // 選択したアイテムをセットする．
        // HACK:削除ダイアログ自身に選択したアイテムを渡せないため，
        // ダイアログをユーザが呼び出した際に，Activityで選択した項目を保持しておく．
        public void setSelectedItem(String selectedItem) {
            Log.d(DEBUG, "setSelectedItem() - item : " + selectedItem);
            this.selectedItem = selectedItem;
        }
    }

    // 選択したアイテムを削除する．
    protected void removeItem(String selectedItem) {
        Log.d(DEBUG, "doPositiveClick() - item : " + selectedItem);
        adapter.remove(selectedItem);
    }

}