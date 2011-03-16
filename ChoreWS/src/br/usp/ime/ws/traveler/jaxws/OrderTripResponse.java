
package br.usp.ime.ws.traveler.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "orderTripResponse", namespace = "http://traveler.ws.ime.usp.br/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orderTripResponse", namespace = "http://traveler.ws.ime.usp.br/")
public class OrderTripResponse {

    @XmlElement(name = "return", namespace = "")
    private br.usp.ime.ws.traveler.Flight _return;

    /**
     * 
     * @return
     *     returns Flight
     */
    public br.usp.ime.ws.traveler.Flight getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(br.usp.ime.ws.traveler.Flight _return) {
        this._return = _return;
    }

}
