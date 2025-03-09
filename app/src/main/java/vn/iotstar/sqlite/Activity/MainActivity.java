package vn.iotstar.sqlite.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import vn.iotstar.sqlite.Adapter.NotesAdapter;
import vn.iotstar.sqlite.DatabaseSQL.DatabaseHandler;
import vn.iotstar.sqlite.Model.NotesModel;
import vn.iotstar.sqlite.R;

public class MainActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    ListView listView;
    ArrayList<NotesModel> arrayList;
    NotesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Quan trọng! Gán Toolbar cho Activity

        InitDatabaseSQLite(); // Khởi tạo database
        createDatabaseSQLite();

        listView = findViewById(R.id.listView1);
        arrayList = new ArrayList<>();
        adapter = new NotesAdapter(this, R.layout.row_notes, arrayList);
        listView.setAdapter(adapter);

        databaseSQLite();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Log.d("DEBUG", "onCreateOptionsMenu đã được gọi!");
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d("DEBUG", "ID menu được nhấn: " + id); // Kiểm tra ID của menu

        if (id == R.id.menuAddNotes) {
            Log.d("DEBUG", "Nút thêm được nhấn!"); // Kiểm tra xem sự kiện có chạy
            DialogThem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private  void createDatabaseSQLite(){
        Cursor cursor = databaseHandler.GetData("SELECT COUNT(*) FROM Notes");
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        if(count==0) {
            databaseHandler.QueryData("INSERT INTO Notes VALUES(null,' Vi du SQLite 1')");
            databaseHandler.QueryData("INSERT INTO Notes VALUES(null,' Vi du SQLite 2')");
        }
    }
    private void databaseSQLite() {
        Cursor cursor = databaseHandler.GetData("SELECT * FROM Notes");
        arrayList.clear();
        while (cursor.moveToNext()){
            String name = cursor.getString(1);
            int id = cursor.getInt(0);
            arrayList.add(new NotesModel(id,name));
        }
        adapter.notifyDataSetChanged();
    }

    private void InitDatabaseSQLite() {
        databaseHandler = new DatabaseHandler(this,"notes.sqlite", null,1);
        databaseHandler.QueryData("CREATE TABLE IF NOT EXISTS Notes(Id INTEGER PRIMARY KEY AUTOINCREMENT, NameNotes VARCHAR(200))");
    }

    private void DialogThem() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_note);

        // Ánh xạ trong dialog
        EditText editText = dialog.findViewById(R.id.editTextName);
        Button buttonAdd = dialog.findViewById(R.id.buttonThem);
        Button buttonHuy = dialog.findViewById(R.id.buttonHuy);

        // Bắt sự kiện khi nhấn nút thêm
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString().trim();
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên Notes", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHandler.QueryData("INSERT INTO Notes VALUES(null, '" + name + "')");
                    Toast.makeText(MainActivity.this, "Đã thêm Notes", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    databaseSQLite(); // Gọi hàm load lại dữ liệu
                }
            }
        });

        buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void DialogCapNhatNotes(String name, int id){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_note);

        // Ánh xạ
        EditText editText = dialog.findViewById(R.id.editTextName);
        Button buttonEdit = dialog.findViewById(R.id.buttonEdit);
        Button buttonHuy = dialog.findViewById(R.id.buttonHuy);
        editText.setText(name);

        // Bắt sự kiện
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString().trim();
                databaseHandler.QueryData("UPDATE Notes SET NameNotes = '" + name + "' WHERE Id = '" + id + "'");
                Toast.makeText(MainActivity.this, "Đã cập nhật Notes thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                databaseSQLite(); // Gọi hàm load lại dữ liệu
            }
        });

        buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void DialogDelete(String name, final int id)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn xóa Notes \"" + name + "\" này không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHandler.QueryData("DELETE FROM Notes WHERE Id = '" + id + "'");
                Toast.makeText(MainActivity.this, "Đã xóa Notes \"" + name + "\" thành công", Toast.LENGTH_SHORT).show();
                databaseSQLite(); // Gọi hàm load lại dữ liệu
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }
}