
package br.usp.ime.ws.traveler.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "bookReserveResponse", namespace = "http://traveler.ws.ime.usp.br/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bookReserveResponse", namespace = "http://traveler.ws.ime.usp.br/")
public class BookReserveResponse {

    @XmlElement(name = "return", namespace = "", nillable = true)
    private String[] _return;

    /**
     * 
     * @return
     *     returns String[]
     */
    public String[] getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(String[] _return) {
        this._return = _return;
    }

}
