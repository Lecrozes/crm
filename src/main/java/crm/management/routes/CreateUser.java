package crm.management.routes;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import crm.management.main.Main;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "createUser", layout = Main.class)
@RolesAllowed("ADMIN")
public class CreateUser extends HorizontalLayout{

    public CreateUser() {
        TextField firstName = new TextField("First name");
        TextField lastName = new TextField("Last name");
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm password");
        Checkbox admin = new Checkbox();
        admin.setLabel("Admin");
        Button createUser = new Button("Anlegen");
        createUser.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createUser.setMaxWidth("150px");
        Button reset = new Button("Zurücksetzen");
        reset.addThemeVariants(ButtonVariant.LUMO_ERROR);
        reset.setMaxWidth("150px");
        
        FormLayout formLayout = new FormLayout();
        setJustifyContentMode( FlexComponent.JustifyContentMode.CENTER );
        formLayout.add(firstName, lastName, username, password,
                confirmPassword, admin, createUser, reset);
        
        add(formLayout);
    }

}
