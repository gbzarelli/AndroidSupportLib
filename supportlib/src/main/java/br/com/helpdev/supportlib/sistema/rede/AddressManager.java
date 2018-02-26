package br.com.helpdev.supportlib.sistema.rede;

import android.util.Log;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Guilherme Biff Zarelli on 12/9/17.
 */

public abstract class AddressManager implements Serializable {
    private static final String LOG = AddressManager.class.getName();
    private final List<String> address;
    private int indexAddrs = 0;

    public AddressManager(String... addrs) {
        address = new ArrayList<>(Arrays.asList(addrs));
    }

    public AddressManager(List<String> addrs) {
        address = addrs;
    }

    public String getHost() {
        return address.get(indexAddrs);
    }

    public String changeHostAvailable() throws ConnectException {
        if (findHostAvailable()) {
            return getHost();
        } else {
            throw new ConnectException("No ChangeHostavailable");
        }
    }

    public String changeHost() {
        indexAddrs++;
        if (indexAddrs > address.size() - 1) {
            indexAddrs = 0;
        }
        return getHost();
    }


    private boolean findHostAvailable() {
        int contador = 0;
        while (contador < address.size()) {
            try {
                if (isHostAvailable(address.get(indexAddrs))) {
                    return true;
                }
            } catch (Exception t) {
                Log.e(LOG, "isHostAvailable return Exception: ", t);
            }

            contador++;
            indexAddrs++;

            if (indexAddrs == address.size()) {
                indexAddrs = 0;
            }
        }
        return false;
    }

    public abstract boolean isHostAvailable(String host) throws Exception;
}
