package com.example.growvision;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.growvision.databinding.TranslateBinding;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TranslateActivity extends Activity {
    TranslateBinding translateBinding;
    private Content content;
    private final GenerativeModel gm =
            new GenerativeModel("gemini-2.5-flash-preview-04-17", BuildConfig.GEMINI_API_KEY);
    private final GenerativeModelFutures model = GenerativeModelFutures.from(gm);
    String language, number, translated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        translateBinding = TranslateBinding.inflate(getLayoutInflater());
        setContentView(translateBinding.getRoot());

        translateBinding.messageTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                send_message();
            }
        });

        get_personalInfo_from_firebase();
    }
    void get_personalInfo_from_firebase() {
        MainActivity.db.collection("member")
                .whereEqualTo("name", MainActivity.name)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            language = doc.getString("language");
                            number = doc.getString("number");
                            makeString();
                        }
                    }
                });
    }
    void makeString() {
        content = new Content.Builder()
                .addText("\""+MainActivity.message+"\"" + "\n\n \"\"안의 문장들을 " + language +
                                                "및 "+ language + "의 문자로 다음 규칙들을 지켜서 번역해." +
                                                "1. 정확하게 빠뜨리는 내용없이." +
                                                "2. 번역 결과는 1종류만." +
                                                "3. 기본적으로 공손한 어투로" +
                                                "4. 의미없는 문장이면 번역하지말고 그대로 반환" +
                                                "5. 부연설명 및 규칙 따라 읽기 금지.")
                .build();

        Executor executor = Executors.newSingleThreadExecutor();
        ListenableFuture<GenerateContentResponse> future =
                model.generateContent(content);

        Futures.addCallback(future, new FutureCallback<>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                translated = result.getText();
                translateBinding.messageTv.setText("Finish!");
            }
            @Override
            public void onFailure(Throwable t) {}
        }, executor);
    }
    void send_message() {
        Uri sms = Uri.parse("sms:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, sms);
        intent.putExtra("sms_body", translated);
        startActivity(intent);
    }
}
