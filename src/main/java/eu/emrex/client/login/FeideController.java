package eu.emrex.client.login;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import no.ntnu.it.fw.saml2api.ConfigurationException;
import no.ntnu.it.fw.saml2api.IDPConf;
import no.ntnu.it.fw.saml2api.SAML2Exception;
import no.ntnu.it.fw.saml2api.SAML2Util;
import no.ntnu.it.fw.saml2api.SPConf;

@Named
@ApplicationScoped
public class FeideController {

    SPConf spconf;
    IDPConf idpconf;


    @PostConstruct
    public void init() {
        String spPropertiesFileName = "norex-sp.properties";

        String konfigMappe = System.getProperty("norex.konfigmappe");
        if (konfigMappe == null) {
            throw new RuntimeException(
                                       "fant ikke prop norex.konfigmappe, feideinnlogging kan ikke settes opp");
        }
        konfigMappe += File.separator;
        String spPropFile = konfigMappe + spPropertiesFileName;

        try {
            spconf = new SPConf(spPropFile);
            String idpPropFile = konfigMappe + spconf.getIdpConfFile();
            idpconf = new IDPConf(idpPropFile);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }

    }


    public void goFeide() throws SAML2Exception, IOException {
        String redirectUrl = SAML2Util.createSAMLAuthnRequest(idpconf, spconf, "hepp");
        FacesContext.getCurrentInstance().getExternalContext().redirect(redirectUrl);
    }


    public SPConf getSPConf() {
        return spconf;
    }


    public IDPConf getIDPConf() {
        return idpconf;
    }
}