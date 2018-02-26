package br.com.helpdev.supportlib.xml

import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister

import java.io.StringWriter

/**
 * Created by Guilherme Biff Zarelli on 11/29/17.
 */

class ControllerSimpleXML {
    companion object {

        @Throws(Exception::class)
        fun <T> xmlToObject(tClass: Class<*>, xml: String): T {
            val serializer = Persister()
            return serializer.read(tClass, xml) as T
        }

        @Throws(Exception::class)
        fun objectToXml(`object`: Any): String {
            val serializer = Persister()
            val sw = StringWriter()
            serializer.write(`object`, sw)
            return sw.toString()
        }
    }
}
