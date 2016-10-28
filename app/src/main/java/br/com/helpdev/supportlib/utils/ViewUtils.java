package br.com.helpdev.supportlib.utils;
import android.app.Activity;
import android.view.View;

public final class ViewUtils {

    private ViewUtils() {
        throw new IllegalArgumentException("No ViewUtils");
    }

    /**
     * <pre>
     * Retorna a referência de uma <i>View</i> da <i>Activity</i>, à partir de seu ID
     * Button btn = ViewUtils.find(MainActivity.this, R.id.btn);
     * </pre>
     *
     * @param activity utilizada para buscar a referencia da <i>View</i>
     * @param id       da <i>View</i> presente da classe <i>R</i> do projeto
     * @param <T>      tipo da <i>View</i> retornada
     * @return referência à <i>View</i>
     */
    public static <T extends View> T find(Activity activity, int id) {
        if (null == activity) {
            throw new IllegalArgumentException("activity is null");
        }

        return (T) activity.findViewById(id);
    }

    /**
     * <pre>
     * Retorna a referência de uma <i>View</i> à partir de seu ID com base na <i>View</i> que representa seu <i>Parent</i>
     * Button btn = ViewUtils.find(MainActivity.this, R.id.btn);
     * </pre>
     *
     * @param view que representa o <i>Parent</i> utilizado para buscar a referencia da <i>View</i>
     * @param id       da <i>View</i> presente da classe <i>R</i> do projeto
     * @param <T>      tipo da <i>View</i> retornada
     * @return referência à <i>View</i>
     */
    public static <T extends View> T find(View view, int id) {
        if (null == view) {
            throw new IllegalArgumentException("parent view is null");
        }

        return (T) view.findViewById(id);
    }
}
