package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.vaadin.data.util.IndexedContainer;

public class ChartItemContainer extends Observable{

	private IndexedContainer container;
	private List<ChartItem> items;
	private String name = "";

	public ChartItemContainer()
	{
		items = new ArrayList<ChartItem>();
		container = new IndexedContainer();
		container.addContainerProperty("value", Double.class, null);
		container.addContainerProperty("caption", String.class, null);
		container.addContainerProperty("info", String.class, null);
	}
	
	public void Clear()
	{
		container.removeAllItems();
	}
	
	public void addChartItem(ChartItem item)
	{
		items.add(item);
		insertChartItemIntoContainer(item);
	}
	
	private void insertChartItemIntoContainer(ChartItem item)
	{
		Object itemId = container.addItem();
		container.getContainerProperty(itemId, "value").setValue(item.getValue());
		container.getContainerProperty(itemId, "caption").setValue(item.getCaption());
		container.getContainerProperty(itemId, "info").setValue(item.getInfo());
	}

	public IndexedContainer getContainer() {
		return container;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<ChartItem> getItems() {
		return items;
	}
	
	public void Update()
	{
		// refresh container
		Clear();
		for (ChartItem item : items) {
			insertChartItemIntoContainer(item);			
		}
		
		setChanged();
        notifyObservers();
	}

	public double getMaxValue() {
		
		if(items.size() == 0)
			return 0;
		
		double maxValue = items.get(0).getValue();
		for (ChartItem item : items) {
			if(item.getValue() > maxValue)
				maxValue = item.getValue();
		}
		
		return maxValue;
	}
}
