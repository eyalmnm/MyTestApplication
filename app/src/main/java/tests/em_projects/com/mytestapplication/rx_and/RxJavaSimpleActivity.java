package tests.em_projects.com.mytestapplication.rx_and;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.rx_and.adapters.SimpleStringAdapter;

public class RxJavaSimpleActivity extends AppCompatActivity {

    final Observable<Integer> serverDownloadObservable = Observable.create(emitter -> {
        SystemClock.sleep(10000);
        emitter.onNext(5);
        emitter.onComplete();
    });
    public int value;
    private RecyclerView colorListView;
    private SimpleStringAdapter simpleStringAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_simple);

        View view = findViewById(R.id.button);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                Disposable subscribe = serverDownloadObservable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(integer -> {
                            updateTheUserInterface(integer);
                            v.setEnabled(true);
                        });
                disposable.add(subscribe);
            }
        });
    }

    private void updateTheUserInterface(int integer) {
        TextView view = findViewById(R.id.resultView);
        view.setText(String.valueOf(integer));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void onClick(View view) {
        Toast.makeText(this, "Still active " + value++, Toast.LENGTH_SHORT).show();
    }
}
