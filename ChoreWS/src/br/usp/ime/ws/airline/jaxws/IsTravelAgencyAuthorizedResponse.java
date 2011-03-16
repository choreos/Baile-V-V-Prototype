
package br.usp.ime.ws.airline.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "isTravelAgencyAuthorizedResponse", namespace = "http://airline.ws.ime.usp.br/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "isTravelAgencyAuthorizedResponse", namespace = "http://airline.ws.ime.usp.br/")
public class IsTravelAgencyAuthorizedResponse {

    @XmlElement(name = "return", namespace = "")
    private boolean _return;

    /**
     * 
     * @return
     *     returns boolean
     */
    public boolean isReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(boolean _return) {
        this._return = _return;
    }

}
