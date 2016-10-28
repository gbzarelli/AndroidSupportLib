package br.com.helpdev.supportlib.sistema;

import java.io.IOException;

/**
 * Created by demantoide on 17/03/16.
 */
public class RuntimeAndroid {

    public Process getProcess(String comando) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        return runtime.exec(comando);
    }

}
