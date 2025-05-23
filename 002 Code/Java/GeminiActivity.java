package com.example.growvision;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.growvision.databinding.AiResponseBinding;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeminiActivity extends Activity {
    private AiResponseBinding aiResponseBinding;
    private String area_name, rail_code, rail_path;
    private long total_num, ripe_num, unripe_num;
    private int task;
    private String language, message;
    private Content content;
    private final GenerativeModel gm =
            new GenerativeModel("gemini-2.5-flash-preview-04-17", BuildConfig.GEMINI_API_KEY);
    private final GenerativeModelFutures model = GenerativeModelFutures.from(gm);
    private TextToSpeech tts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 바인딩 & 타이틀 숨기기
        aiResponseBinding = AiResponseBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(aiResponseBinding.getRoot());

        // 스크롤 + TTS 연동
        aiResponseBinding.aiResponseTv.setMovementMethod(
                ScrollingMovementMethod.getInstance()
        );
        aiResponseBinding.aiResponseTv.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable e) {}
            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                if (tts != null) {
                    tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });

        // 뒤로 버튼
        Button backBtn = aiResponseBinding.backBtn;
        backBtn.setOnClickListener(v -> {
            if (tts != null) {
                tts.stop();
                tts.shutdown();
            }
            finish();
        });

        // ① SharedPreferences에서 선택된 언어 읽어오기
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        language = prefs.getString("language", "English");

        // ② Intent extras 처리
        task = getIntent().getIntExtra("Task", 0);
        if (task == 1) {
            area_name = getIntent().getStringExtra("AreaName");
            rail_code = getIntent().getStringExtra("RailCode");
            rail_path = getIntent().getStringExtra("RailPath");
            total_num = getIntent().getLongExtra("TotalNum", 0);
            ripe_num   = getIntent().getLongExtra("RipeNum", 0);
            unripe_num = getIntent().getLongExtra("UnripeNum", 0);
        }

        // ③ TTS 초기화
        init_tts();

        // ④ AI 호출 및 결과 표시
        display_response();
    }

    private void init_tts() {
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                Locale locale;
                float rate = 1.0f;

                switch (language) {
                    case "English":
                        locale = Locale.ENGLISH;
                        rate = 0.9f;
                        break;
                    case "Korean":
                        // 한국어
                        locale = Locale.KOREA;
                        rate = 1.0f;
                        break;
                    case "Chinese":
                        // 중국어(간체)
                        locale = Locale.CHINA;
                        rate = 1.0f;
                        break;
                    case "Japanese":
                        locale = Locale.JAPAN;
                        rate = 1.0f;
                        break;
                    default:
                        // 디폴트 시스템 언어
                        locale = Locale.getDefault();
                        rate = 1.0f;
                }

                // 설정 적용
                int result = tts.setLanguage(locale);
                tts.setSpeechRate(rate);

                // 지원되지 않는 언어 체크(Optional)
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // 데이터 부족 또는 미지원 언어일 경우 디폴트 영어로 폴백
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }

    private String makeString() {
        if (task == 0) {
            StringBuilder sb = new StringBuilder(
                    "아래 데이터는 우리 딸기 스마트팜의 통계 데이터야. " +
                            "구역명, 전체 딸기 수, 성숙 딸기 수, 미성숙 딸기 수 카테고리 순서로 여러 데이터가 있어. " +
                            "데이터를 백분율을 활용해서 해석한 후 간단하게 " + language +
                            "로만 말해줘. Okay나 잡다한 설명 제외."
            );
            Iterator<HarvestArea> iter = MainActivity.dataset.listIterator();
            while (iter.hasNext()) {
                HarvestArea d = iter.next();
                sb.append(d.getArea_name()).append(", ")
                        .append(d.getTotal_num()).append(", ")
                        .append(d.getRipe_num()).append(", ")
                        .append(d.getUnripe_num()).append(", ");
            }
            // 끝에 붙은 쉼표 제거
            if (sb.length() > 0) sb.setLength(sb.length() - 2);
            return sb.toString();
        } else {
            return String.format(
                    Locale.KOREA,
                    "구역: %s\n레일 코드: %s\n레일 경로: %s\n전체 딸기 수: %d\n" +
                            "성숙 딸기 수: %d\n미성숙 딸기 수: %d\n위 내용은 우리 딸기 스마트팜의 통계 데이터야. " +
                            "위 내용을 백분율을 활용해서 해석한 후 간단하게 %s로만 말해줘. Okay나 잡다한 설명 제외.",
                    area_name, rail_code, rail_path,
                    total_num, ripe_num, unripe_num,
                    language
            );
        }
    }

    private void display_response() {
        content = new Content.Builder()
                .addText(makeString())
                .build();

        Executor executor = Executors.newSingleThreadExecutor();
        ListenableFuture<GenerateContentResponse> future =
                model.generateContent(content);

        Futures.addCallback(
                future,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        message = result.getText();
                        aiResponseBinding.aiResponseTv.setText(message);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // 실패 처리 (생략)
                    }
                },
                executor
        );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }
}
