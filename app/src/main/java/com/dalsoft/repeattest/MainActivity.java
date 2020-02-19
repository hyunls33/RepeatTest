package com.dalsoft.repeattest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dalsoft.repeattest.Util.CommentAdapter;
import com.dalsoft.repeattest.Util.DialogHelper;
import com.dalsoft.repeattest.Util.Service.APIGenerator;
import com.dalsoft.repeattest.Util.Dto.Comments;
import com.dalsoft.repeattest.Util.Service.RetrofitGeneratorTest;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView_totalCount;
    private RecyclerView recyclerView_list;
    private Button button_start;
    private Button button_clear;

    private int startIndex;
    private int endIndex;

    private CommentAdapter commentAdapter;
    private ArrayList<Comments> commentList;
    private CompositeDisposable compositeDisposable;

    private DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_totalCount = findViewById(R.id.textView_totalCount);
        recyclerView_list   = findViewById(R.id.recyclerView_list);
        button_start        = findViewById(R.id.button_start);
        button_clear        = findViewById(R.id.button_clear);

        startIndex          = 1;
        endIndex            = 5;
        compositeDisposable = new CompositeDisposable();
        commentList         = new ArrayList<Comments>();
        commentAdapter      = new CommentAdapter(commentList);

        recyclerView_list.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_list.setAdapter(commentAdapter);

        button_start.setOnClickListener(this);
        button_clear.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        commentList.clear();
        textView_totalCount.setText(String.format(getResources().getString(R.string.total_count), commentList.size()));
        commentAdapter.notifyDataSetChanged();

        button_start.setEnabled(true);
        button_clear.setEnabled(false);
    }

    @Override
    protected void onStop() {
        //CompositeDisposable 클래스를 이용하여 사용한 Observable을 한번에 해지 가능.
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

        dialogHelper.dialogDismiss();

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_start) {
            commentList.clear();
            button_start.setEnabled(false);
            button_clear.setEnabled(false);
            commentAdapter.notifyDataSetChanged();

            // CASE 1
            // 반복 여부
            AtomicBoolean check = new AtomicBoolean(true);

            // 서버에서 줄 데이터가 없을 때까지 반복적으로 api를 호출하여 데이터를 받아올 수 있음
            Disposable disposable1 = Observable.just(0)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .concatMap(test -> RetrofitGeneratorTest.getService().getTestData(startIndex)
                                                    .subscribeOn(Schedulers.newThread())
                                                    .observeOn(AndroidSchedulers.mainThread()))
                            .repeatUntil(() -> !check.get()) // 반복 여부 체크
                            .subscribe(comments -> {

                                commentList.addAll(comments);

                                startIndex++;

                                if (comments.size() == 0) {
                                    check.set(false); // 넘어온 데이터가 없을 경우 더이상 호출하지 않도록 반복여부 변경
                                }

                                System.out.println("결과 목록 갯수 : " + commentList.size());
                            }, this::apiCallError, this::complete);


            // CASE 2
            // 시작과 종료할 범위를 알 경우 해당 범위내로 반복적 api 호출 가능
            Disposable disposable2 = Observable.range(startIndex, endIndex)
                            .concatMap(i -> RetrofitGeneratorTest.getService().getTestData(i)
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread()))
                            .subscribe(comments -> commentList.addAll(comments), this::apiCallError, this::complete);

            // 해지를 위해 compositeDisposable에 추가
            compositeDisposable.add(disposable1);
            compositeDisposable.add(disposable2);
        } else if (view.getId() == R.id.button_clear) {
            dialogHelper = new DialogHelper();
            dialogHelper.dialogShow(this, getResources().getString(R.string.clear), getResources().getString(R.string.message), clearClickListener);
        }
    }

    // Dialog positiveButton Click event
    private DialogInterface.OnClickListener clearClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            commentList.clear();
            textView_totalCount.setText(String.format(getResources().getString(R.string.total_count), commentList.size()));
            commentAdapter.notifyDataSetChanged();

            button_start.setEnabled(true);
            button_clear.setEnabled(false);

            dialogHelper.dialogDismiss();
        }
    };

    private void apiCallError(Throwable throwable) {
        String message = "";

        if (throwable != null) {
            message = throwable.getMessage();
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        button_start.setEnabled(true);
        button_clear.setEnabled(true);
        textView_totalCount.setText(String.format(getResources().getString(R.string.total_count), commentList.size()));
        commentAdapter.notifyDataSetChanged();
    }

    private void complete() {
        button_start.setEnabled(true);
        button_clear.setEnabled(true);
        textView_totalCount.setText(String.format(getResources().getString(R.string.total_count), commentList.size()));
        commentAdapter.notifyDataSetChanged();
    }
}

//    public Observable<ArrayList<Comments>> getComments(int index) {
//        return APIGenerator.apiService.getTestData(index)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

//            // 이예!!!!!3
//            Disposable disposable = Observable.just(0)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .concatMap(test -> APIGenerator.apiService.getTestData(startIndex)
//                                            .subscribeOn(Schedulers.newThread())
//                                            .observeOn(AndroidSchedulers.mainThread()))
//                    .repeatUntil(() -> !check.get())
//                    .subscribe(comments -> {
//
//                        commentList.addAll(comments);
//
//                        startIndex++;
//
//                        if (comments.size() == 0) {
//                            check.set(false);
//                        }
//
//                        System.out.println("결과 목록 갯수 : " + commentList.size());
//                    }, this::apiCallError, this::complete);



//            // 이예!!!!!2
//            AtomicBoolean check = new AtomicBoolean(true);
//
//            Disposable disposable = Observable.just(0)
//                .concatMap(test -> getComments(startIndex))
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .repeatUntil(() -> !check.get())
//                .subscribe(comments -> {


//                    if (comments.size() == 0) {
//                        check.set(false);
//                    }
//
//                    commentList.addAll(comments);
//
//                    startIndex++;
//
//
//                    System.out.println("결과 목록 갯수 : " + commentList.size());
//                }, this::apiCallError, this::complete);




//            Disposable disposable = getComments(startIndex)
//                    .repeat()
//                    .takeUntil(test -> test.size() == 0)
//                    .subscribe(comments -> {
//                        commentList.addAll(comments);
//                        System.out.println("결과 목록 갯수 : " + commentList.size());
//
//                        startIndex++;
//                    }, this::apiCallError, this::complete);



// 이예!!!!
//            Disposable disposable = Observable.range(startIndex, endIndex)
//                            .concatMap(i -> getComments(i))
//                            .retry()
//                            .subscribe(comments -> {
//                                commentList.addAll(comments);
//                                System.out.println("결과 목록 갯수 : " + commentList.size());
//                            }, this::apiCallError, this::complete);





//            Disposable disposable = APIGenerator.apiService.getTestData(startIndex)
//                                        .subscribeOn(Schedulers.newThread())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .range(startIndex, endIndex)
//                                        .concatMap(s -> {
//                                            return Obser
//                                        })
//                                        .subscribe(result, this::apiCallError, this::complete);




//                    .concatMap(i -> makeRequest(i))



//                                        .repeatUntil(() -> startIndex > endIndex)
//                                        .subscribe(comments -> {
//                                            commentList.addAll(comments);
//
//                                            startIndex++;
//                                        }, this::apiCallError, this::complete);




//                                        .takeUntil(test -> test.size() == 0)
//                                        .concatMap(test2 -> {
//                                            commentList.addAll(test2);
//
//                                            startIndex++;
//                                            return APIGenerator.apiService.getTestData(startIndex)
//                                        })


//                                        .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
//                                            @Override
//                                            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
//                                                if (startIndex > endIndex) {
//                                                    return Observable.empty();
//                                                }
//
//                                                return objectObservable.delay(1, TimeUnit.SECONDS).flatMap();
//                                            }
//                                        })
//                                        .subscribe(result -> {
//                                            commentList.addAll(result);
//
//                                            startIndex++;
//                                        }, this::apiCallError, this::complete);
