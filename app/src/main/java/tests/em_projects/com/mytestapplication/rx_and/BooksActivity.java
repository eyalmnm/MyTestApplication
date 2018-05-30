package tests.em_projects.com.mytestapplication.rx_and;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.rx_and.adapters.SimpleStringAdapter;
import tests.em_projects.com.mytestapplication.rx_and.server.RestClient;

public class BooksActivity extends AppCompatActivity {

    private Disposable bookSubscription;
    private RecyclerView booksRecyclerView;
    private ProgressBar progressBar;
    private SimpleStringAdapter simpleStringAdapter;
    private RestClient restClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restClient = new RestClient(this);
        configureLayout();
        createObservable();
    }

    private void configureLayout() {
        setContentView(R.layout.activity_rx_books);
        progressBar = (ProgressBar) findViewById(R.id.loader);
        booksRecyclerView = (RecyclerView) findViewById(R.id.books_list);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        simpleStringAdapter = new SimpleStringAdapter(this);
        booksRecyclerView.setAdapter(simpleStringAdapter);
    }

    private void createObservable() {
        Observable<List<String>> booksObservable =
                Observable.fromCallable(() -> restClient.getFavoriteBooks());
        bookSubscription = booksObservable.
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(strings -> displayBooks(strings));
    }

    private void displayBooks(List<String> books) {
        simpleStringAdapter.setStrings(books);
        progressBar.setVisibility(View.GONE);
        booksRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bookSubscription != null && !bookSubscription.isDisposed()) {
            bookSubscription.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bookSubscription != null && !bookSubscription.isDisposed()) {
            bookSubscription.dispose();
        }
    }
}
