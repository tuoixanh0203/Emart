package com.example.ecomapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecomapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class HistorySearchAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> history;
    private final FirebaseFirestore firestore;

    public HistorySearchAdapter(@NonNull Context context, List<String> history) {
        super(context, R.layout.history_search_item, history);
        this.context = context;
        this.history = history;
        this.firestore = FirebaseFirestore.getInstance();
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.history_search_item, parent, false);

        TextView historyResult = view.findViewById(R.id.historySearch);
        historyResult.setText(history.get(position));

        historyResult.setOnTouchListener(((view1, motionEvent) -> {
            final int DRAWABLE_RIGHT = 0;

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (motionEvent.getRawX() >= (historyResult.getRight() - historyResult.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    firestore.collection("SearchHistory")
                            .whereEqualTo("text", history.get(position))
                            .get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String docId = document.getId();

                                        firestore.collection("SearchHistory")
                                                .document(docId).delete().addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        Toast.makeText(getContext(),"Delete search " + history.get(position), Toast.LENGTH_SHORT).show();
                                                        history.remove(history.get(position));
                                                        notifyDataSetChanged();
                                                    }
                                        });
                                    }
                                }
                            });

                    return true;
                }
            }
            return false;
        }));

        return view;
    }
}
