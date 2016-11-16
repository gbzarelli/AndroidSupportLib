package br.com.helpdev.supportlib.sistema.telefonia;

/**
 * Created by Guilherme Biff Zarelli on 26/08/16.
 */
public class ObSIM {

    private boolean mobileNetworkActive;
    private String lineNumber;
    private String serialNumber;
    private String imei;
    private String versaoIMEI;
    private String networkOperator;
    private String simOperator;

    public boolean isMobileNetworkActive() {
        return mobileNetworkActive;
    }

    public void setMobileNetworkActive(boolean mobileNetworkActive) {
        this.mobileNetworkActive = mobileNetworkActive;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getVersaoIMEI() {
        return versaoIMEI;
    }

    public void setVersaoIMEI(String versaoIMEI) {
        this.versaoIMEI = versaoIMEI;
    }

    public String getNetworkOperator() {
        return networkOperator;
    }

    public void setNetworkOperator(String networkOperator) {
        this.networkOperator = networkOperator;
    }

    public String getSimOperator() {
        return simOperator;
    }

    public void setSimOperator(String simOperator) {
        this.simOperator = simOperator;
    }

    @Override
    public String toString() {
        return "ObSIM{" +
                "mobileNetworkActive=" + mobileNetworkActive +
                ", lineNumber='" + lineNumber + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", imei='" + imei + '\'' +
                ", versaoIMEI='" + versaoIMEI + '\'' +
                ", networkOperator='" + networkOperator + '\'' +
                ", simOperator='" + simOperator + '\'' +
                '}';
    }
}
