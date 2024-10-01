package edu.sjsu.android.placeholdername;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Iterator;

import edu.sjsu.android.placeholdername.databinding.CalcNavBtnBinding;

public class CalcNavAdapter extends RecyclerView.Adapter<CalcNavAdapter.ViewHolder> {

    private final HashMap<String, Class<? extends Fragment>> mValues;
    private final Iterator<HashMap.Entry<String, Class<? extends Fragment>>> iterator;
    private final Context context;

    public CalcNavAdapter(HashMap<String, Class<? extends Fragment>> items, Context context) {
        mValues = items;
        iterator = mValues.entrySet().iterator();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(CalcNavBtnBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (iterator.hasNext()) {
            HashMap.Entry<String, Class<? extends Fragment>> entry = iterator.next();
            holder.binding.calcFragmentTitle.setText(entry.getKey());
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected final CalcNavBtnBinding binding;

        public ViewHolder(CalcNavBtnBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                String buttonTitle = binding.calcFragmentTitle.getText().toString();
                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.fragmentContainerView, mValues.get(buttonTitle), null)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }
}