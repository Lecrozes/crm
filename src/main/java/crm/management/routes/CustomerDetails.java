package crm.management.routes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import crm.management.entity.ContactEntity;
import crm.management.entity.CustomerEntity;
import crm.management.main.Main;
import crm.management.repository.ContactRepository;
import crm.management.repository.Contact_Customer_Repository;
import crm.management.repository.CustomerRepository;
import crm.management.repository.DocumentRepository;
import jakarta.annotation.security.PermitAll;

@Route(value = "customer/details/:customerID", layout = Main.class)
@PermitAll
public class CustomerDetails extends HorizontalLayout implements HasUrlParameter<Integer> {

	private String customerID;

	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	ContactRepository contactRepo;

	@Autowired
	DocumentRepository docuRepo;

	@Autowired
	Contact_Customer_Repository relationRepo;

	private Grid<ContactEntity> grid;
	private TextField name;
	private TextField email;
	private TextField telefonnummer;
	private TextField adresse;
	private TextField plz;
	private TextField ort;
	private TextArea kommentar;

	// Div was kommt, wenn kein Inhalt vorhanden ist.
	private final Div hint = new Div(new Text("No Entries found"));

	public CustomerDetails() {
		// Nur static Inhalt, der ohne customerID zu tun hat
		// Die customerID wird erst nach dem Konstruktor gesetzt
		hint.setVisible(false);
	}

	// Nach dem Konstruktor ausgeführte Methode: Speichert die customerID, welche
	// als Paramater mitgebene wurde
	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Integer parameter) {
		customerID = event.getRouteParameters().get("customerID").orElse("0");
		CustomerEntity customer = customerRepo.getByUUID(customerID);
		customerView(customer);
		fillCustomerGrid(customerID);
	}

	private void customerView(CustomerEntity customer) {
		VerticalLayout customerInfos = new VerticalLayout();
		customerInfos.add(customerDetails(customer));
		customerInfos.add(customerComment(customer));
		customerInfos.setHeightFull();
		customerInfos.setWidth("60%");

		HorizontalLayout form = new HorizontalLayout();
		Button save = new Button("Änderungen speichern");
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				try {
					String saveName = name.getValue();
					String saveEmail = email.getValue();
					String saveTelefonnummer = telefonnummer.getValue();
					String saveAdresse = adresse.getValue();
					String savePlz = plz.getValue();
					String saveOrt = ort.getValue();
					String saveKommentar = kommentar.getValue();

					customerRepo.updateCustomer(customerID, saveName, saveEmail, saveTelefonnummer, saveAdresse,
							savePlz, saveOrt, saveKommentar);
					Notification notification = Notification
							.show("Daten zu " + saveName + " wurden erfolgreich aktualisiert!");
					notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					notification.setPosition(Notification.Position.BOTTOM_CENTER);
				} catch (Exception e) {
					Notification notification = Notification.show("FEHLER, kontaktieren Sie einen Administrator!");
					notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
					notification.setPosition(Notification.Position.BOTTOM_CENTER);
				}
				UI.getCurrent().navigate(Customer.class);
			}
		});

		Button back = new Button("Zurück zur Kundenübersicht");
		back.addThemeVariants(ButtonVariant.LUMO_ERROR);
		back.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				UI.getCurrent().navigate(Customer.class);
			}
		});
		form.add(save, back);

		customerInfos.add(form);

		add(customerInfos);
		add(customerRealtions());
		Documents doc = new Documents();
		add(doc.getComponent(customerID, docuRepo, "20%"));

	}

	private VerticalLayout customerDetails(CustomerEntity customer) {
		VerticalLayout wrapper = new VerticalLayout();
		FormLayout layout = new FormLayout();
		layout.setWidth("70%");

		name = new TextField("Kundenname");
		name.setValue(customer.getName());

		email = new TextField("Email");
		email.setValue(customer.getEmail());

		telefonnummer = new TextField("Telefonnummer");
		telefonnummer.setValue(customer.getTelefonnummer());

		adresse = new TextField("Adresse");
		adresse.setValue(customer.getAdresse());

		plz = new TextField("PLZ");
		plz.setValue(customer.getPlz());

		ort = new TextField("Ort");
		ort.setValue(customer.getOrt());

		layout.add(name, email, telefonnummer, adresse, plz, ort);
		wrapper.add(new H2("Kundeninformationen zu " + customer.getName()));
		wrapper.add(layout);
		return wrapper;
	}

	private VerticalLayout customerComment(CustomerEntity customer) {
		VerticalLayout layout = new VerticalLayout();
		layout.add(new H3("Kommentar"));
		kommentar = new TextArea();
		kommentar.setValue(customer.getKommentar());
		kommentar.setWidth("90%");

		layout.add(kommentar);
		return layout;
	}

	private VerticalLayout customerRealtions() {
		VerticalLayout layout = new VerticalLayout();
		layout.add(new H3("Kontakte"));
		layout.add(createRealtion());

		grid = new Grid<ContactEntity>();
		grid.addColumn(ContactEntity::getVorname).setHeader("Vorname");
		grid.addColumn(ContactEntity::getName).setHeader("Name");
		grid.addColumn(ContactEntity::getEmail).setHeader("Email").setWidth("300px").setFlexGrow(0);
		grid.addColumn(ContactEntity::getTelefonnummer).setHeader("Telefonnummer").setWidth("200px").setFlexGrow(0);
		grid.addColumn(ContactEntity::isIntern).setHeader("Intern");
		grid.addColumn(new ComponentRenderer<>(contact -> {
			Button delete = new Button(new Icon(VaadinIcon.TRASH));
			delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
			delete.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

				@Override
				public void onComponentEvent(ClickEvent<Button> event) {
					relationRepo.deleteRelation(contact.getIdentifier(), customerID);
					fillCustomerGrid(customerID);
				}

			});
			return delete;
		})).setHeader("Aktionen");

		grid.addItemClickListener(new ComponentEventListener<ItemClickEvent<ContactEntity>>() {

			@Override
			public void onComponentEvent(ItemClickEvent<ContactEntity> event) {
				ContactEntity entity = event.getItem();
				List<String> contactUUIDs = relationRepo.getContacts(customerID);
				List<ContactEntity> contacts = new ArrayList<ContactEntity>();
				for (String uuid : contactUUIDs) {
					contacts.add(contactRepo.getByUUID(uuid));
				}
				if (entity == null || !contacts.contains(entity)) {
					return;
				}

				Dialog dialog = new Dialog();
				dialog.setHeaderTitle("Details zu " + entity.getVorname() + ", " + entity.getName());

				FormLayout layout = new FormLayout();
				TextField email = new TextField("Email");
				email.setValue(entity.getEmail());
				email.setReadOnly(true);

				TextField telefonnummer = new TextField("Telefonnummer");
				telefonnummer.setValue(entity.getTelefonnummer());
				telefonnummer.setReadOnly(true);

				TextField isIntern = new TextField("Interner Kontakt");
				isIntern.setValue(String.valueOf(entity.isIntern()));
				isIntern.setReadOnly(true);

				layout.add(email, telefonnummer, isIntern);

				dialog.add(layout);

				TextArea kommentar = new TextArea("Kommentar");
				kommentar.setMinWidth("600px");
				kommentar.setMinHeight("250px");
				String value = relationRepo.getCommentFromRelation(customerID, entity.getIdentifier());
				kommentar.setValue(value == null ? "" : value);

				dialog.add(kommentar);

				FlexLayout footer = new FlexLayout();
				footer.setWidthFull();
				Button save = new Button("Kommentar speichern");
				save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
				save.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

					@Override
					public void onComponentEvent(ClickEvent<Button> event) {
						relationRepo.setComment(customerID, entity.getIdentifier(), kommentar.getValue());
						dialog.close();
					}
				});
				Button back = new Button("Abbrechen");
				back.addThemeVariants(ButtonVariant.LUMO_ERROR);
				back.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

					@Override
					public void onComponentEvent(ClickEvent<Button> event) {
						dialog.close();
					}
				});
				footer.add(back, save);
				footer.setJustifyContentMode(JustifyContentMode.BETWEEN);

				dialog.getFooter().add(footer);

				dialog.open();

			}
		});

		layout.add(hint);
		layout.add(grid);
		return layout;
	}

	private Button createRealtion() {
		Button create = new Button("Kontakt hinzufügen");
		create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		create.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				showAllContacts();
			}
		});

		return create;
	}

	private void showAllContacts() {
		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Alle Kontakte");
		Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> dialog.close());
		closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		dialog.getHeader().add(closeButton);

		MultiSelectListBox<ContactEntity> contactBox = new MultiSelectListBox<ContactEntity>();
		contactBox.setItems(contactRepo.findAll());
		dialog.add(contactBox);

		Button save = new Button("Hinzufügen");
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				boolean success = false;
				boolean warning = false;
				try {
					List<String> contactUUIDs = relationRepo.getContacts(customerID);
					List<ContactEntity> contacts = new ArrayList<ContactEntity>();
					for (String uuid : contactUUIDs) {
						contacts.add(contactRepo.getByUUID(uuid));
					}
					for (ContactEntity contact : contactBox.getSelectedItems()) {
						if (!contacts.contains(contact)) {
							relationRepo.insertRelation(contact.getIdentifier(), customerID);
							success = true;
						} else {
							warning = true;
						}
					}
					fillCustomerGrid(customerID);
					dialog.close();
					if (success) {
						Notification notification = Notification.show("Kontakt/e erfolgreich hinzugefügt!");
						notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
						notification.setPosition(Notification.Position.BOTTOM_CENTER);
					}
					if (warning) {
						Notification notification = Notification.show("Warnung! Kontakte sind bereits hinzugefügt");
						notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
						notification.setPosition(Notification.Position.BOTTOM_CENTER);
					}
				} catch (Exception e) {
					Notification notification = Notification.show("FEHLER, kontaktieren Sie einen Administrator!");
					notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
					notification.setPosition(Notification.Position.BOTTOM_CENTER);
				}
			}
		});

		dialog.getFooter().add(save);
		dialog.open();
	}

	private void fillCustomerGrid(String customerUUID) {
		List<String> contactUUIDs = relationRepo.getContacts(customerUUID);
		List<ContactEntity> contacts = new ArrayList<ContactEntity>();
		for (String uuid : contactUUIDs) {
			contacts.add(contactRepo.getByUUID(uuid));
		}
		if (contacts.size() == 0) {
			hint.setVisible(true);
			grid.setVisible(false);
			return;
		} else {
			hint.setVisible(false);
			grid.setItems(contacts);
			grid.getDataProvider().refreshAll();
			grid.setVisible(true);
		}
	}

}
