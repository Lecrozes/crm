package crm.management.routes;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

import crm.management.entity.ContactEntity;
import crm.management.main.Main;
import crm.management.repository.ContactRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;

@Route(value = "contact", layout = Main.class)
@PermitAll
public class Contact extends VerticalLayout {

	@Autowired
	ContactRepository contactRepo;

	private Grid<ContactEntity> contactGrid;
	// Div was kommt, wenn kein Inhalt vorhanden ist.
	private final Div hint = new Div(new Text("No Entries found"));

	public Contact() {
		add(new H2("Kontaktübersicht"));
		hint.setVisible(false);
		add(createContact());
		add(hint);
		configureGrid();
	}

	@PostConstruct
	public void init() {
		getAllContacts();
	}

	// Grid wird erstellt und Buttons mit Funktionalitäten sowie RowClick
	// Funktionalität
	private void configureGrid() {
		contactGrid = new Grid<ContactEntity>(ContactEntity.class, false);
		contactGrid.addColumn(ContactEntity::getName).setHeader("Name");
		contactGrid.addColumn(ContactEntity::getVorname).setHeader("Vorname");
		contactGrid.addColumn(ContactEntity::getPronomen).setHeader("Pronomen");
		contactGrid.addColumn(ContactEntity::getEmail).setHeader("Email");
		contactGrid.addColumn(ContactEntity::getTelefonnummer).setHeader("Telefonnummer");
		contactGrid.addColumn(ContactEntity::isIntern).setHeader("Intern");
		contactGrid.addColumn(ContactEntity::getKommentar).setHeader("Kommentar");
		contactGrid.addColumn(new ComponentRenderer<>(contact -> {
			Button delete = new Button(new Icon(VaadinIcon.TRASH));
			delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
			delete.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

				@Override
				public void onComponentEvent(ClickEvent<Button> event) {
					contactRepo.deleteById(contact.getKontakt_id());
					getAllContacts();
				}

			});
			return delete;
		})).setHeader("Aktionen");

		addAndExpand(contactGrid);

		contactGrid.addItemClickListener(new ComponentEventListener<ItemClickEvent<ContactEntity>>() {

			@Override
			public void onComponentEvent(ItemClickEvent<ContactEntity> event) {
				ContactEntity clickedEntity = event.getItem();
				// Wenn auf löschen gedrückt wurde, dann ignoriere das Event
				if (clickedEntity == null) {
					return;
				}
				String contactID = String.valueOf(clickedEntity.getIdentifier());

				// Navigiert zur CustomerDetails Klasse mit Parameter aus der geklickten Row
				UI.getCurrent().navigate(ContactDetails.class, new RouteParameters("contactID", contactID));
			}
		});
	}

	// Holt alle Daten aus der Datenbank, speichert die in dem Grid und Lädt es neu
	private void getAllContacts() {
		try {
			hint.setVisible(false);
			contactGrid.setVisible(true);
			contactGrid.setItems(contactRepo.findAll());
			contactGrid.getDataProvider().refreshAll();
		} catch (Exception e) {
			contactGrid.setVisible(false);
			hint.setVisible(true);
		} finally {
			contactGrid.getDataProvider().refreshAll();
		}
	}

	private Button createContact() {
		Button create = new Button("Kontakt anlegen");
		create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		create.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				UI.getCurrent().navigate(CreateContact.class);
			}
		});

		return create;
	}
}
