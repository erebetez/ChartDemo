package gui;

import java.util.Iterator;
import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import domain.ChartItem;
import domain.ChartItemContainer;

public class ChartContainersTab extends VerticalLayout {

	private List<ChartItemContainer> containers;
	
	private VerticalLayout containerItemsLayout = new VerticalLayout();
	
	private NativeSelect containerSelect = null;
	private NativeSelect containerItemsSelect = null;
	private TextField tfCaption = null;
	private TextField tfValue = null;
	private TextField tfInfo = null;
	
	public ChartContainersTab(List<ChartItemContainer> chartItemContainers)
	{
		containers = chartItemContainers;
		
		/*
		 * Creation of the containers with items
		*/
		
		ChartItemContainer container1 = new ChartItemContainer();
		container1.setName("container 1");
		
		ChartItem item11 = new ChartItem();
		item11.setCaption("1.1");
		item11.setValue(50);
		item11.setInfo("Info von Item 1.1");
		// add the item to the container
		container1.addChartItem(item11);
		
		ChartItem item12 = new ChartItem();
		item12.setCaption("1.2");
		item12.setValue(100);
		item12.setInfo("Info von Item 1.2");
		// add the item to the container
		container1.addChartItem(item12);
		
		ChartItem item13 = new ChartItem();
		item13.setCaption("1.3");
		item13.setValue(130);
		item13.setInfo("Info von Item 1.3");
		// add the item to the container
		container1.addChartItem(item13);
		
		// add the container to the list of containers
		containers.add(container1);
		
		ChartItemContainer container2 = new ChartItemContainer();
		container2.setName("container 2");
		
		ChartItem item21 = new ChartItem();
		item21.setCaption("2.1");
		item21.setValue(20);
		item21.setInfo("Info von Item 2.1");
		// add the item to the container
		container2.addChartItem(item21);
		
		ChartItem item22 = new ChartItem();
		item22.setCaption("2.2");
		item22.setValue(90);
		item22.setInfo("Info von Item 2.2");
		// add the item to the container
		container2.addChartItem(item22);
		
		// add the container to the list of containers
		containers.add(container2);
		
		addComponent(constructContainerSelect());
		addComponent(constructContainerItemsSelect());
		addComponent(createContainerItemsLayout());
	}
	
	private Component constructContainerItemsSelect() {
		containerItemsSelect = new NativeSelect("Container Items");
		containerItemsSelect.setNullSelectionAllowed(false);
		containerItemsSelect.addListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				updateContainerItemsLayout();
			}
		});
		
		return containerItemsSelect;
	}

	private Component constructContainerSelect() {
		
		containerSelect = new NativeSelect("Container");
		containerSelect.setNullSelectionAllowed(false);
		
		for (ChartItemContainer container : containers) {
			containerSelect.addItem(container);
			containerSelect.setItemCaption(container, container.getName());
		}
		
		if(containers.size() > 0){
			// set initial selection
			containerSelect.setValue(containers.get(0));
		}
		containerSelect.setImmediate(true);
		
		containerSelect.addListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				updateContainerItemsSelectBox();
			}
		});
		
		return containerSelect;
	}
	
	private Component createContainerItemsLayout()
	{
		tfCaption = new TextField("Beschriftung");
		tfValue = new TextField("Value");
		tfInfo = new TextField("Info");
		
		Button btnUpdate = new Button("Update");
		
		btnUpdate.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				// add the current values into the currently selected container item
				Object selectedContainerItem = containerItemsSelect.getValue();
				if(selectedContainerItem != null)
				{
					try{
						ChartItem item = (ChartItem)selectedContainerItem;
						item.setCaption(tfCaption.getValue().toString());
						item.setValue(Double.parseDouble(tfValue.getValue().toString()));
						item.setInfo(tfInfo.getValue().toString());
						
						// update the containeritem select box
						updateContainerItemsSelectBox();
						// update the container
						((ChartItemContainer)containerSelect.getValue()).Update();
					}
					catch(Exception ex)
					{
						// TODO : handle possible exceptions
					}
				}
			}
		});
		
		containerItemsLayout.addComponent(tfCaption);
		containerItemsLayout.addComponent(tfValue);
		containerItemsLayout.addComponent(tfInfo);
		containerItemsLayout.addComponent(btnUpdate);
		
		updateContainerItemsSelectBox();
		
		return containerItemsLayout;
	}
	
	private void updateContainerItemsLayout() {
		
		Object selectedContainerItem = containerItemsSelect.getValue();
		if(selectedContainerItem != null)
		{
			try{
				ChartItem item = (ChartItem)selectedContainerItem;
				// update values
				tfCaption.setValue(item.getCaption());
				tfValue.setValue(item.getValue());
				tfInfo.setValue(item.getInfo());
			}
			catch(Exception ex)
			{
				// TODO : handle possible exceptions
			}
		}
	}

	private void updateContainerItemsSelectBox() {
		
		// check if a container is selected
		Object selectedContainer = containerSelect.getValue();
		if(selectedContainer != null)
		{
			// refresh the items selection of the container
			containerItemsSelect.removeAllItems();
			
			List<ChartItem> containerItems = ((ChartItemContainer)selectedContainer).getItems();
			
			for (ChartItem containerItem : containerItems) {
				containerItemsSelect.addItem(containerItem);
				containerItemsSelect.setItemCaption(containerItem, containerItem.getCaption());
			}
			
			if(containerItems.size() > 0){
				// set initial selection
				containerItemsSelect.setValue(containerItems.get(0));
			}
			containerItemsSelect.setImmediate(true);
			
			updateContainerItemsLayout();
		}
	}
}
