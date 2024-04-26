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

import crm.management.entity.CustomerEntity;
import crm.management.main.Main;
import crm.management.repository.CustomerRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;

@Route(value = "customer", layout = Main.class)
@PermitAll
public class Customer extends VerticalLayout {

	@Autowired
	CustomerRepository customerRepo;

	private Grid<CustomerEntity> customerGrid;
	// Div was kommt, wenn kein Inhalt vorhanden ist.
	private final Div hint = new Div(new Text("No Entries found"));

	public Customer() {
		add(new H2("Kundenübersicht"));
		hint.setVisible(false);
		add(createCustomer());
		add(hint);
		configureGrid();
	}

	@PostConstruct
	public void init() {
		getAllCustomers();
	}

	// Grid wird erstellt und Buttons mit Funktionalitäten sowie RowClick
	// Funktionalität
	private void configureGrid() {
		customerGrid = new Grid<CustomerEntity>(CustomerEntity.class, false);
		customerGrid.addColumn(CustomerEntity::getName).setHeader("Name");
		customerGrid.addColumn(CustomerEntity::getEmail).setHeader("Email");
		customerGrid.addColumn(CustomerEntity::getTelefonnummer).setHeader("Telefonnummer");
		customerGrid.addColumn(CustomerEntity::getAdresse).setHeader("Adresse");
		customerGrid.addColumn(CustomerEntity::getPlz).setHeader("PLZ");
		customerGrid.addColumn(CustomerEntity::getOrt).setHeader("Ort");
		customerGrid.addColumn(CustomerEntity::getKommentar).setHeader("Kommentar");
		customerGrid.addColumn(new ComponentRenderer<>(customer -> {
			Button delete = new Button(new Icon(VaadinIcon.TRASH));
			delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
			delete.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

				@Override
				public void onComponentEvent(ClickEvent<Button> event) {
					customerRepo.deleteById(customer.getKunden_Id());
					getAllCustomers();
				}

			});
			return delete;
		})).setHeader("Aktionen");

		addAndExpand(customerGrid);

		customerGrid.addItemClickListener(new ComponentEventListener<ItemClickEvent<CustomerEntity>>() {

			@Override
			public void onComponentEvent(ItemClickEvent<CustomerEntity> event) {
				CustomerEntity clickedEntity = event.getItem();
				// Wenn auf löschen gedrückt wurde, dann ignoriere das Event
				if (clickedEntity == null) {
					return;
				}
				String customerID = String.valueOf(clickedEntity.getIdentifier());

				// Navigiert zur CustomerDetails Klasse mit Parameter aus der geklickten Row
				UI.getCurrent().navigate(CustomerDetails.class, new RouteParameters("customerID", customerID));
			}
		});
	}

	// Holt alle Daten aus der Datenbank, speichert die in dem Grid und Lädt es neu
	private void getAllCustomers() {
		try {
			hint.setVisible(false);
			customerGrid.setVisible(true);
			customerGrid.setItems(customerRepo.findAll());
		} catch (Exception e) {
			customerGrid.setVisible(false);
			hint.setVisible(true);
		} finally {
			customerGrid.getDataProvider().refreshAll();
		}
	}

	private Button createCustomer() {
		Button create = new Button("Kunde anlegen");
		create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		create.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				UI.getCurrent().navigate(CreateCustomer.class);
			}
		});

		return create;
	}

}
