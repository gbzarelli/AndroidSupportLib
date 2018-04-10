package br.com.grupocriar.swapandroid.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.List;

import br.com.grupocriar.swapandroid.ui.UnitUtils;
import br.com.grupocriar.swapandroid.utils.ThisObjects;


/**
 * Created by Felipe Barata on 11/05/16.
 */
public abstract class RecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private static final int NORMAL_ITEM = Integer.MIN_VALUE;
    private static final int LAST_ITEM = Integer.MAX_VALUE;
    protected final int marginBottom;

    protected final Context context;
    protected final LayoutInflater inflater;
    private final int layout;
    private Class tClass;

    private List<T> lista;
    protected RecyclerAdapterListener listener;

    /**
     * @param context
     * @param lista
     * @param layout
     * @param marginBottom in px
     * @param tClass       if inner class, must be static
     */
    public RecyclerViewAdapter(Context context, List<T> lista, int layout, int marginBottom, Class tClass) {
        this.context = ThisObjects.requireNonNull(context);
        this.lista = ThisObjects.requireNonNull(lista);
        this.layout = ThisObjects.requireNonNull(layout);
        inflater = LayoutInflater.from(context);
        this.marginBottom = marginBottom;
        this.tClass = tClass;
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

            Constructor constructor = tClass.getConstructor(View.class);
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
            e.printStackTrace();
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
        T item = getList().get(position);
        onBindViewHolder(holder, item, position);
    }

    protected abstract void onBindViewHolder(VH holder, T item, int position);

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public void setListener(RecyclerAdapterListener listener) {
        this.listener = listener;
    }

    public List<T> getList() {
        return lista;
    }

    public void setList(List<T> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    public interface RecyclerAdapterListener<T> {
        void onClickItem(View v, int position, T t);

        boolean onLongClickItem(View v, int position, T t);
    }
}
