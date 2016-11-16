package br.com.helpdev.supportlib.sistema.bluetooth;

/**
 * Created by Guilherme Biff Zarelli on 25/07/15.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

/**
 * <uses-permission android:name="android.permission.BLUETOOTH"/>
 * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
 *
 * @author Guilherme Biff Zarelli
 */
public class Bluetooth {

    /**
     * Método responsável por "pegar" os dispositivos pareados com o aparelho
     *
     * @param applicationContext
     * @return
     * @throws IOException
     */
    public static Bluetooth getBondedDevices(Context applicationContext) throws IOException {
        Bluetooth bluetooth = new Bluetooth();
        bluetooth.dispositivo = BluetoothAdapter.getDefaultAdapter();

        if (!bluetooth.dispositivo.isEnabled()) {
            bluetooth.dispositivo.enable();
            return null;
        }

        // Pega a lista de dispositivos pareados
        Set<BluetoothDevice> pairedDevices = bluetooth.dispositivo.getBondedDevices();

        // Adiciono na lista e depois retorno a mesma.
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                bluetooth.lista.add(device);
            }
        }

        return bluetooth;
    }

    private ArrayList<BluetoothDevice> lista;
    private BluetoothAdapter dispositivo;

    public BluetoothAdapter getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(BluetoothAdapter dispositivo) {
        this.dispositivo = dispositivo;
    }

    private Bluetooth() {
        lista = new ArrayList<BluetoothDevice>();
    }

    public void desabilitarAdapter() {
        BluetoothAdapter.getDefaultAdapter().disable();
    }

    public void unpairDevice(BluetoothDevice blDevice) throws Exception, Throwable {
        Method m = blDevice.getClass().getMethod("removeBond", (Class[]) null);
        m.invoke(blDevice, (Object[]) null);
    }

    public boolean cancelDiscovery() {
        return dispositivo.cancelDiscovery();
    }

    public ArrayList<BluetoothDevice> getDispositivos() {
        return lista;
    }

}
