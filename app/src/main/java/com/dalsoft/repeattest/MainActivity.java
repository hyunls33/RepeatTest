package com.dalsoft.repeattest;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_start;

    private int startIndex;
    private int endIndex;

    private ArrayList<Comments> commentList;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_start = findViewById(R.id.button_start);
        startIndex   = 1;
        endIndex     = 5;

        commentList         = new ArrayList<Comments>();
        compositeDisposable = new CompositeDisposable();

        button_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_start) {
            commentList.clear();

            AtomicBoolean check = new AtomicBoolean(true);

            Disposable disposable = Observable.just(0)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .concatMap(test -> RetrofitGeneratorTest.getService().getTestData(startIndex)
                                            .subscribeOn(Schedulers.newThread())
                                            .observeOn(AndroidSchedulers.mainThread()))
                    .repeatUntil(() -> !check.get())
                    .subscribe(comments -> {

                        commentList.addAll(comments);

                        startIndex++;

                        if (comments.size() == 0) {
                            check.set(false);
                        }

                        System.out.println("결과 목록 갯수 : " + commentList.size());
                    }, this::apiCallError, this::complete);

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

            compositeDisposable.add(disposable);
        }
    }

    public Observable<ArrayList<Comments>> getComments(int index) {
        return APIGenerator.apiService.getTestData(index)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void apiCallError(Throwable throwable) {
        String message = "";

        if (throwable != null) {
            message = throwable.getMessage();
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void complete() {
        long test = commentList.stream().filter(comments -> comments.getPostId() == 2).count();
   }

    @Override
    protected void onStop() {
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

        super.onDestroy();
    }
}
