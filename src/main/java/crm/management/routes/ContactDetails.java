package crm.management.routes;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import crm.management.entity.ContactEntity;
import crm.management.main.Main;
import crm.management.repository.ContactRepository;
import crm.management.repository.DocumentRepository;
import jakarta.annotation.security.PermitAll;

@Route(value = "contact/details/:contactID", layout = Main.class)
@PermitAll
public class ContactDetails extends VerticalLayout implements HasUrlParameter<Integer> {

	private String contactID;

	@Autowired
	DocumentRepository docuRepo;

	@Autowired
	ContactRepository contactRepo;

	private TextField name;
	private TextField vorname;
	private TextField pronomen;
	private TextField email;
	private TextField telefonnummer;
	private Checkbox intern;
	private TextArea kommentar;

	public ContactDetails() {
		// Nur static Inhalt, der ohne customerID zu tun hat
		// Die customerID wird erst nach dem Konstruktor gesetzt
	}

	// Nach dem Konstruktor ausgeführte Methode: Speichert die customerID, welche
	// als Paramater mitgebene wurde
	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Integer parameter) {
		contactID = event.getRouteParameters().get("contactID").orElse("0");
		ContactEntity contact = contactRepo.getByUUID(contactID);
		customerView(contact);
	}

	private void customerView(ContactEntity contact) {
		VerticalLayout contactInfos = new VerticalLayout();
		contactInfos.add(customerDetails(contact));
		contactInfos.add(customerComment(contact));
		contactInfos.setHeightFull();
		contactInfos.setWidth("60%");

		HorizontalLayout form = new HorizontalLayout();
		Button save = new Button("Änderungen speichern");
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				try {
					String saveName = name.getValue();
					String saveVorname = vorname.getValue();
					String savePronomen = pronomen.getValue();
					String saveEmail = email.getValue();
					String saveTelefonnummer = telefonnummer.getValue();
					boolean saveIntern = intern.getValue();
					String saveKommentar = kommentar.getValue();

					contactRepo.updateCustomer(contactID, saveName, saveVorname, savePronomen, saveEmail,
							saveTelefonnummer, saveIntern, saveKommentar);
					Notification notification = Notification
							.show("Daten zu " + saveName + " wurden erfolgreich aktualisert!");
					notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					notification.setPosition(Notification.Position.BOTTOM_CENTER);
				} catch (Exception e) {
					Notification notification = Notification.show("FEHLER, kontaktieren Sie einen Administrator!");
					notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
					notification.setPosition(Notification.Position.BOTTOM_CENTER);
				}
				UI.getCurrent().navigate(Contact.class);
			}
		});

		Button back = new Button("Zurück zur Kontakübersicht");
		back.addThemeVariants(ButtonVariant.LUMO_ERROR);
		back.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				UI.getCurrent().navigate(Contact.class);
			}
		});
		form.add(save, back);

		contactInfos.add(form);
		contactInfos.setWidth("80%");

		HorizontalLayout layout = new HorizontalLayout();
		Documents doc = new Documents();
		layout.add(contactInfos, doc.getComponent(contactID, docuRepo, "20%"));
		

		add(layout);
	}

	private VerticalLayout customerDetails(ContactEntity contact) {
		VerticalLayout wrapper = new VerticalLayout();
		FormLayout layout = new FormLayout();
		layout.setWidth("70%");

		name = new TextField("Name");
		name.setValue(contact.getName());

		vorname = new TextField("Vorname");
		vorname.setValue(contact.getVorname());

		pronomen = new TextField("Pronomen");
		pronomen.setValue(contact.getPronomen());

		email = new TextField("Email");
		email.setValue(contact.getEmail());

		telefonnummer = new TextField("Telefonnummer");
		telefonnummer.setValue(contact.getTelefonnummer());

		intern = new Checkbox("Interner Kontakt");
		intern.setValue(contact.isIntern());

		layout.add(name, vorname, pronomen, email, telefonnummer, intern);
		wrapper.add(new H2("Kontaktinformationen zu " + contact.getVorname() + ", " + contact.getName() + "."));
		wrapper.add(layout);
		return wrapper;
	}

	private VerticalLayout customerComment(ContactEntity contact) {
		VerticalLayout layout = new VerticalLayout();
		layout.add(new H3("Kommentar"));
		kommentar = new TextArea();
		kommentar.setValue(contact.getKommentar());
		kommentar.setWidth("90%");

		layout.add(kommentar);
		return layout;
	}

}
