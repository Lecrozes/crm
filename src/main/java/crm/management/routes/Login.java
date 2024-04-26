package crm.management.routes;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class Login extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm login = new LoginForm();

    public Login() {
        addClassName("login-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        login.setI18n(createGerman());
        login.setAction("login");
        
        add(login);
    }

    private LoginI18n createGerman() {
    	LoginI18n german = LoginI18n.createDefault();

    	LoginI18n.Form germanForm = german.getForm();
    	germanForm.setTitle("Authentifizierung");
    	germanForm.setUsername("Nutzername");
    	germanForm.setPassword("Password");
    	germanForm.setSubmit("Einloggen");
    	germanForm.setForgotPassword("Passwort Vergessen");
    	german.setForm(germanForm);
    	
    	LoginI18n.ErrorMessage germanErrorMessage = german.getErrorMessage();
    	germanErrorMessage.setTitle("FehlerTitel");
    	germanErrorMessage.setMessage(
    	        "Fehler Nachricht");
    	german.setErrorMessage(germanErrorMessage);
    	return german;
	}

	@Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
            .getQueryParameters()
            .getParameters()
            .containsKey("error")) {
            login.setError(true);
        }
    }
}