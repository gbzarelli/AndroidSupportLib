package br.com.grupocriar.swapandroid.xml;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;

/**
 * Created by Guilherme Biff Zarelli on 11/29/17.
 */

public class ControllerSimpleXML {
    private ControllerSimpleXML() {
        throw new RuntimeException("No ControllerSimpleXML");
    }

    @SuppressWarnings("unchecked")
    public static <T> T xmlToObject(Class tClass, String xml) throws Exception {
        Serializer serializer = new Persister();
        return (T) serializer.read(tClass, xml);
    }

    public static String objectToXml(Object object) throws Exception {
        Serializer serializer = new Persister();
        StringWriter sw = new StringWriter();
        serializer.write(object, sw);
        return sw.toString();
    }
}
