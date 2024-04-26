package crm.management.routes;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import crm.management.main.Main;
import crm.management.repository.ContactRepository;
import jakarta.annotation.security.PermitAll;

@Route(value = "/contact/create", layout = Main.class)
@PermitAll
public class CreateContact extends HorizontalLayout {

	@Autowired
	ContactRepository contactRepo;

	private TextField name;
	private TextField vorname;
	private TextField pronomen;
	private TextField email;
	private TextField telefonnummer;
	private Checkbox intern;
	private TextArea kommentar;

	public CreateContact() {
		add(customerView());
	}

	private VerticalLayout customerView() {
		VerticalLayout layout = new VerticalLayout();
		layout.add(customerDetails());
		layout.add(customerComment());
		layout.setHeightFull();

		HorizontalLayout form = new HorizontalLayout();
		Button save = new Button("Anlegen");
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				String saveName = name.getValue();
				String saveVorname = vorname.getValue();
				String savePronomen = pronomen.getValue();
				String saveEmail = email.getValue();
				String saveTelefonnummer = telefonnummer.getValue();
				boolean saveIntern = intern.getValue();
				String saveKommentar = kommentar.getValue();

				if (saveName.equals("") || saveEmail.equals("") || saveTelefonnummer.equals("")
						|| saveVorname.equals("") || savePronomen.equals("")) {
					return;
				}

				contactRepo.insertContact(saveName, saveVorname, savePronomen, saveEmail, saveTelefonnummer, saveIntern,
						saveKommentar);

				UI.getCurrent().navigate(Contact.class);
			}
		});

		Button back = new Button("Abbrechen");
		back.addThemeVariants(ButtonVariant.LUMO_ERROR);
		back.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				UI.getCurrent().navigate(Contact.class);
			}
		});
		form.add(save, back);

		layout.add(form);

		VerticalLayout infoText = new VerticalLayout();
		layout.add(new Div(new Text("*Diese Felder müssen ausgefüllt werden.")),
				new Div(new Text("**Beim Anlegen wird ein Kontakt in der Kontaktübersicht erstellt.")));
		return layout;
	}

	private VerticalLayout customerDetails() {
		VerticalLayout wrapper = new VerticalLayout();
		FormLayout layout = new FormLayout();

		name = new TextField("Nachname*");

		vorname = new TextField("Vorname*");

		pronomen = new TextField("Pronomen*");

		email = new TextField("Email*");

		telefonnummer = new TextField("Telefonnummer*");

		intern = new Checkbox("Interner Kontakt*");

		layout.add(name, vorname, pronomen, email, telefonnummer, intern);
		wrapper.add(new H2("Neuen Kontakt anlegen"));
		wrapper.add(layout);
		return wrapper;
	}

	private VerticalLayout customerComment() {
		VerticalLayout layout = new VerticalLayout();
		layout.add(new H3("Kommentar"));
		kommentar = new TextArea();
		kommentar.setWidth("90%");

		layout.add(kommentar);
		return layout;
	}

}
