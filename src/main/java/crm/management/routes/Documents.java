package crm.management.routes;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.StreamResource;

import crm.management.entity.DocumentEntity;
import crm.management.repository.DocumentRepository;

public class Documents{
	private  Grid<DocumentEntity> grid;
	private  final Div hint = new Div(new Text("No Entries found"));
	
	public  VerticalLayout getComponent(String ownerID, DocumentRepository docuRepo, String width) {
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMaxWidth(width);
		layout.add(new H3("Dokumente"));
		
		grid = new Grid<DocumentEntity>();
		grid.addColumn(DocumentEntity::getName).setHeader("Name");
		grid.addColumn(new ComponentRenderer<>(document -> {
			HorizontalLayout hl = new HorizontalLayout();
			Button delete = new Button(new Icon(VaadinIcon.TRASH));
			delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
			delete.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
				@Override
				public void onComponentEvent(ClickEvent<Button> event) {
					docuRepo.deleteByIdentifier(document.getIdentifier());
					showRelatedDocs(docuRepo, ownerID);
				}
			});
			
			Anchor download = new Anchor(getStreamResource(document.getName(), document.getFile()), "");
		    download.getElement().setAttribute("download",true);
		    download.removeAll();
		    download.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
		    hl.add(download);
			hl.add(delete);
			return hl;
		})).setHeader("Aktionen");
		hint.setVisible(false);
		showRelatedDocs(docuRepo, ownerID);
		layout.add(createUploadButton(ownerID, docuRepo));
		layout.add(hint);
		layout.add(grid);
		
		return layout;

	}
	
	public StreamResource getStreamResource(String filename, byte[] content) {
        return new StreamResource(filename,
                () -> new ByteArrayInputStream(content));
    }
	
	private  void showRelatedDocs(DocumentRepository docuRepo, String ownerID){
		List<DocumentEntity> documents = new ArrayList<DocumentEntity>();
		documents = docuRepo.getDocsForOwner(ownerID);
		grid.setItems(documents);
		if (documents.size() == 0) {
			hint.setVisible(true);
			grid.setVisible(false);
			return;
		} else {
			hint.setVisible(false);
			grid.setItems(documents);
			grid.getDataProvider().refreshAll();
			grid.setVisible(true);
		}
	}
	
	private  Button createUploadButton(String ownerID, DocumentRepository docuRepo) {
		Button create = new Button("Dokument hinzufügen");
		create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		create.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				openUpload(ownerID, docuRepo);
			}
		});

		return create;
	}
	
	private  void openUpload(String ownerID, DocumentRepository docuRepo) {
		Dialog dialog = new Dialog();
		dialog.setHeaderTitle("Upload");
		Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> dialog.close());
		closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		dialog.getHeader().add(closeButton);
		
		MemoryBuffer memoryBuffer = new MemoryBuffer();
		Upload singleFileUpload = new Upload(memoryBuffer);
		singleFileUpload.addSucceededListener(event -> {
		    InputStream fileData = memoryBuffer.getInputStream();
		    String name = event.getFileName();
		    try {
				byte[] file = fileData.readAllBytes();
				docuRepo.insertDocument(name, file, ownerID);
				showRelatedDocs(docuRepo, ownerID);
				boolean success = true;
				if (success) {
					Notification notification = Notification.show("Dokument erfolgreich hinzugefügt!");
					notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					notification.setPosition(Notification.Position.BOTTOM_CENTER);
				}
			} catch (Exception e) {
				Notification notification = Notification.show("FEHLER, kontaktieren Sie einen Administrator!");
				notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
				notification.setPosition(Notification.Position.BOTTOM_CENTER);
			}
		});
		dialog.add(singleFileUpload);
		
		Button done = new Button("Fertig",(e) -> dialog.close());
		done.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		dialog.getFooter().add(done);
		dialog.open();
	}

}
