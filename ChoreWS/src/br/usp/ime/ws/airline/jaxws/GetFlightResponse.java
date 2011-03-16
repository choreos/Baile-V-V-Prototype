
package br.usp.ime.ws.airline.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getFlightResponse", namespace = "http://airline.ws.ime.usp.br/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getFlightResponse", namespace = "http://airline.ws.ime.usp.br/")
public class GetFlightResponse {

    @XmlElement(name = "return", namespace = "")
    private br.usp.ime.ws.airline.FlightResult _return;

    /**
     * 
     * @return
     *     returns FlightResult
     */
    public br.usp.ime.ws.airline.FlightResult getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(br.usp.ime.ws.airline.FlightResult _return) {
        this._return = _return;
    }

}
