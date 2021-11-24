package com.autentikar.clarotest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;


import org.json.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.initialButton);

        button.setOnClickListener(v -> {
            Intent intentSend = new Intent();
            intentSend.setAction("autentikar.intent.SEND_EXTERNAL_DATA");
            intentSend.setType("text/json");

            intentSend.putExtra("authToken", "token");
            intentSend.putExtra("stage", "QA");
            try {
                getToken(token -> {
                    intentSend.putExtra("nextToken", token);
                    //intentSend.putExtra(Intent.EXTRA_TITLE, "Vamos a Autentikar");
                    Intent shareIntent = Intent.createChooser(intentSend, null);
                    mGetContent.launch(shareIntent);
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            final Intent data = result.getData();
            String resultData = data.getSerializableExtra("RESULT").toString();
            Toast.makeText(this, resultData, Toast.LENGTH_LONG).show();
        }
        if (result.getResultCode() == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Activity finalizada", Toast.LENGTH_LONG).show();
        }
    });



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void getToken(CallbackToken callbackToken) throws JSONException {

        //Puede ser con RequestBody y JSONObject para tener mayor control del request
        String json = "{\n" +
                "\t\"flowId\": \"6197c72b91bb19220c2ef1a9\",\n" +
                "\t\"user\": {\n" +
                "\t\t\"country\": \"CL\",\n" +
                "\t\t\"doc_type\": \"CI\",\n" +
                "        \"id_num\": \"159058387\"\n" +
                "\t},\n" +
                "\t\"tx\": {\n" +
                "\t\t\"id\": \"123\",\n" +
                "\t\t\"device\": {\n" +
                "\t\t\t\t\"os_name\": \"test\",\n" +
                "\t\t\t\t\"os_version\": \"test\",\n" +
                "\t\t\t\t\"model\": \"test\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "    \"customFields\": {},\n" +
                "    \"files\": {}\n" +
                "}\n";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180,TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .build();

        JSONObject parameters = new JSONObject(json);

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url( "hostQA")
                .header("Content-Type", "application/json")
                .header("ak-sync", "true")
                .header("Authorization", "Bearer authToken")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameters.toString()))
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseString = response.body().string();
                response.body().close();
                try {
                    JSONObject responseJSON = new JSONObject(responseString);
                    JSONObject result = responseJSON.getJSONObject("result");
                    callbackToken.call(result.getString("token"));
                } catch(JSONException e) {

                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });
    }

    public interface CallbackToken {
        void call(String token);
    }
}