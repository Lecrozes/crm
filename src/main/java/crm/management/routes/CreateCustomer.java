package crm.management.routes;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import crm.management.entity.ContactEntity;
import crm.management.main.Main;
import crm.management.repository.CustomerRepository;
import jakarta.annotation.security.PermitAll;

@Route(value = "/customer/create", layout = Main.class)
@PermitAll
public class CreateCustomer extends HorizontalLayout {

	@Autowired
	CustomerRepository customerRepo;

	private Grid<ContactEntity> grid;
	private TextField name;
	private TextField email;
	private TextField telefonnummer;
	private TextField adresse;
	private TextField plz;
	private TextField ort;
	private TextArea kommentar;

	public CreateCustomer() {
		add(customerView());
		// add(customerRealtions());
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
				String saveEmail = email.getValue();
				String saveTelefonnummer = telefonnummer.getValue();
				String saveAdresse = adresse.getValue();
				String savePlz = plz.getValue();
				String saveOrt = ort.getValue();
				String saveKommentar = kommentar.getValue();

				if (saveName.equals("") || saveEmail.equals("") || saveTelefonnummer.equals("")
						|| saveAdresse.equals("") || savePlz.equals("") || saveOrt.equals("")) {
					return;
				}

				customerRepo.insertCustomer(saveName, saveEmail, saveTelefonnummer, saveAdresse, savePlz, saveOrt,
						saveKommentar);

				UI.getCurrent().navigate(Customer.class);
			}
		});

		Button back = new Button("Abbrechen");
		back.addThemeVariants(ButtonVariant.LUMO_ERROR);
		back.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				UI.getCurrent().navigate(Customer.class);
			}
		});
		form.add(save, back);

		layout.add(form);
		layout.add(new Div(new Text("*Diese Felder müssen ausgefüllt werden.")), new Div(new Text(
				"**Beim Anlegen wird ein Kunde in der Übersicht ohne Beziehung zu Kontakten erstellt. Diese Beziehungen müssen nachträglich beim Kunden angelegt werden.")));
		return layout;
	}

	private VerticalLayout customerDetails() {
		VerticalLayout wrapper = new VerticalLayout();
		FormLayout layout = new FormLayout();

		name = new TextField("Kundenname*");

		email = new TextField("Email*");

		telefonnummer = new TextField("Telefonnummer*");

		adresse = new TextField("Adresse*");

		plz = new TextField("PLZ*");

		ort = new TextField("Ort*");

		layout.add(name, email, telefonnummer, adresse, plz, ort);
		wrapper.add(new H2("Neuen Kunden anlegen"));
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
