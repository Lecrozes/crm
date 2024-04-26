package crm.management.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import crm.management.routes.Contact;
import crm.management.routes.CreateUser;
import crm.management.routes.Customer;
import crm.management.security.SecurityService;
import jakarta.annotation.security.PermitAll;

@Route("/")
@PermitAll
public class Main extends AppLayout {

	private static SecurityService securityService;

	public Main(@Autowired SecurityService securityService) {
		Main.securityService = securityService;
		createDrawer();
		createHeader();
	}

	private void createHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.setAlignItems(Alignment.CENTER);
		header.setWidthFull();

		VerticalLayout layout = new VerticalLayout();
		layout.setAlignItems(Alignment.CENTER);
		H2 h2 = new H2("Customer Relationship Management");
		layout.add(h2);

		header.add(new DrawerToggle());
		header.add(layout);

		addToNavbar(header);
	}

	private void createDrawer() {
		VerticalLayout layout = new VerticalLayout();
		RouterLink customer = new RouterLink("Kunden", Customer.class);
		RouterLink contact = new RouterLink("Kontakte", Contact.class);
		layout.add(customer, contact);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			RouterLink createUser = new RouterLink("Nutzer anlegen", CreateUser.class);
			layout.add(createUser);
		}

		if (securityService.getAuthenticatedUser() != null) {
			Button logout = new Button("Logout", click -> securityService.logout());
			layout.add(logout);
		}
		addToDrawer(layout);
	}
}
