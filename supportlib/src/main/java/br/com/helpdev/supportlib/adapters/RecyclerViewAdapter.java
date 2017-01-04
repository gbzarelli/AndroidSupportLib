package br.com.helpdev.supportlib.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.List;

import br.com.helpdev.supportlib.utils.ThisObjects;
import br.com.helpdev.supportlib.utils.UnitUtils;


/**
 * Created by Felipe Barata on 11/05/16.
 *
 * @param <T> is type of list
 */
public abstract class RecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected static final int NORMAL_ITEM = Integer.MIN_VALUE;
    protected static final int LAST_ITEM = Integer.MAX_VALUE;

    private final int marginBottom;
    private final Context context;
    private final LayoutInflater inflater;
    private final int layout;

    private List<T> lista;
    private RecyclerAdapterListener<T> listener;
    private Class classViewHolder;

    /**
     * @param context
     * @param lista
     * @param layout
     * @param marginBottom    in DP
     * @param classViewHolder if inner Class, must be static.
     */
    public RecyclerViewAdapter(@NonNull Context context, @NonNull List<T> lista, @NonNull int layout,
                               @Nullable int marginBottom, @NonNull Class classViewHolder) {
        this.classViewHolder = ThisObjects.requireNonNull(classViewHolder);
        this.context = ThisObjects.requireNonNull(context);
        this.lista = ThisObjects.requireNonNull(lista);
        this.layout = ThisObjects.requireNonNull(layout);
        inflater = LayoutInflater.from(context);
        this.marginBottom = marginBottom;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View view = inflater.inflate(layout, parent, false);
            if (viewType == LAST_ITEM && marginBottom > 0) {
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
                lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin + UnitUtils.dpToPx(marginBottom));
                view.setLayoutParams(lp);
            }

            Constructor constructor = classViewHolder.getConstructor(View.class);
            VH vh = (VH) constructor.newInstance(view);

            view.setTag(vh);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        VH vh = (VH) view.getTag();
                        int position = vh.getAdapterPosition();
                        listener.onClickItem(view, position, lista.get(position));
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        VH vh = (VH) view.getTag();
                        int position = vh.getAdapterPosition();
                        return listener.onLongClickItem(view, position, lista.get(position));
                    }
                    return true;
                }
            });
            return vh;
        } catch (Exception e) {
            throw new RuntimeException("Error onCreateViewHolder", e);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (marginBottom > 0) {
            return position == (lista.size() - 1) ? LAST_ITEM : NORMAL_ITEM;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, getLista().get(position), position);
    }

    abstract void onBindViewHolder(VH holder, T item, int position);

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public Context getContext() {
        return context;
    }

    public void setListener(RecyclerAdapterListener<T> listener) {
        this.listener = listener;
    }

    public void setLista(List<T> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    public List<T> getLista() {
        return lista;
    }

    interface RecyclerAdapterListener<T> {
        void onClickItem(View v, int position, T t);

        boolean onLongClickItem(View v, int position, T t);
    }
}
