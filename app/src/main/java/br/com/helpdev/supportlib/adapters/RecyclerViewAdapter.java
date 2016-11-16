package br.com.helpdev.supportlib.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.grupocriar.swapandroid.utils.ThisObjects;
import br.com.grupocriar.swapandroid.utils.UnitUtils;


/**
 * Created by felipe on 11/05/16.
 */
public abstract class RecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected static final int NORMAL_ITEM = Integer.MIN_VALUE;
    protected static final int LAST_ITEM = Integer.MAX_VALUE;
    protected final int marginBottom;

    protected final Context context;
    protected final LayoutInflater inflater;
    private final int layout;

    protected List<T> lista;
    protected RecyclerAdapterListener listener;

    /**
     * @param context
     * @param lista
     * @param layout
     * @param marginBottom in px
     */
    public RecyclerViewAdapter(Context context, List<T> lista, int layout, int marginBottom) {
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
                lp.setMargins(0, 0, 0, UnitUtils.pxToDp(marginBottom));
                view.setLayoutParams(lp);
            }
            VH vh = getViewHolder(view);

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
            return position == lista.size() ? LAST_ITEM : NORMAL_ITEM;
        }
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public void setListener(RecyclerAdapterListener listener) {
        this.listener = listener;
    }

    public abstract VH getViewHolder(View view);

    public interface RecyclerAdapterListener<T> {
        void onClickItem(View v, int position, T t);

        boolean onLongClickItem(View v, int position, T t);
    }
}
