package gui;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.vaadin.charts.BarChart;
import com.vaadin.charts.BarChart.Orientation;
import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import domain.ChartItemContainer;

public class BarChartTab extends VerticalLayout implements Observer{

	private BarChart barChart = new BarChart();

	private List<ChartItemContainer> containers;

	private VerticalLayout propertiesLayout = new VerticalLayout();

	private HorizontalLayout textFieldsLayout = new HorizontalLayout();
	
	private NativeSelect containerSelect = null;

	public BarChartTab(String[] colorsForChart, List<ChartItemContainer> chartItemContainers) {
		
		containers = chartItemContainers;
		
		setCaption("Bar chart");
		
		// barchart initialization
		barChart.setImmediate(true);
		barChart.setItemCaptionPropertyId("caption");
		barChart.setItemValuePropertyId("value");
		barChart.setColors(colorsForChart);
		addComponent(barChart);
		
		addComponent(constructContainerSelect());
		
		/*
		 * Properties show / hide
		*/
		CheckBox toggleCheckbox = new CheckBox("Show properties");
		toggleCheckbox.setImmediate(true);
		toggleCheckbox.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				propertiesLayout.setVisible(!propertiesLayout.isVisible());
			}
		});
		addComponent(toggleCheckbox);

		/*
		 * The properties section
		*/
		propertiesLayout.addComponent(constructOrientationButton());
		propertiesLayout.addComponent(constructAnimationDelayTextField());
		propertiesLayout.addComponent(textFieldsLayout);
		propertiesLayout.addComponent(constructInfoLabel());
		propertiesLayout.setVisible(false);
		addComponent(propertiesLayout);
		
		
		//fillTextFieldsLayout();
	}

	
	/*
	 * Changes Orientation of the chart
	 */
	private Button constructOrientationButton() {
		CheckBox cbx = new CheckBox("Vertical", true);
		cbx.setImmediate(true);
		cbx.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				if ((Boolean) event.getButton().getValue()) {
					barChart.setOrientation(Orientation.VERTICAL);
				} else {
					barChart.setOrientation(Orientation.HORIZONTAL);
				}
			}
		});
		return cbx;
	}
	
	
	/*
	 * the info label shows the info text of a selected chart item
	 */
	private Component constructInfoLabel() {
		final Label l = new Label();
		barChart.addListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object value = barChart.getValue();
				if (value != null) {
					l.setValue(barChart.getContainerDataSource().getItem(
							barChart.getValue()).getItemProperty("info"));
				} else {
					l.setValue("");
				}
			}
		});
		return l;
	}

	/*
	 * Text field to change the animation delay
	 */
	private TextField constructAnimationDelayTextField() {
		final TextField tf = new TextField("Animation");
		tf.setValue(1500);
		tf.setImmediate(true);
		tf.addListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object value = tf.getValue();
				try {
					barChart.setAnimationDelay(Integer.parseInt((String) value));
				} catch (NumberFormatException e) {
					// TODO : fehlermeldung ausgeben
				}
			}
		});
		return tf;
	}

	
	private void fillTextFieldsLayout() {
		textFieldsLayout.removeAllComponents();
		for (Object o : barChart.getContainerDataSource().getItemIds()) {
			TextField tf = new TextField();
			tf.setPropertyDataSource(barChart.getContainerDataSource().getItem(o).getItemProperty(barChart.getItemValuePropertyId()));
			tf.setReadThrough(true);
			tf.setReadOnly(true);
			textFieldsLayout.addComponent(tf);
		}
	}

	
	private Component constructContainerSelect() {
		
		containerSelect = new NativeSelect("Container");
		containerSelect.setNullSelectionAllowed(false);
		
		for (ChartItemContainer container : containers) {
			containerSelect.addItem(container);
			containerSelect.setItemCaption(container, container.getName());
			containerSelect.addListener(new ValueChangeListener() {
				public void valueChange(ValueChangeEvent event) {
					/*
					 * Update the datasource of the chart and the maximum / minimum value of the grid
					*/
					UpdateSelectedContainer();
					
					barChart.setValue(null);
					//fillTextFieldsLayout();
					
				}

			});
		}
		
		UpdateSelectedContainer();
		containerSelect.setImmediate(true);

		return containerSelect;
	}
	
	private void UpdateSelectedContainer()
	{
		if(containers.size() > 0){
			// check if there is already a container selected
			ChartItemContainer selectedContainer = null;
			if(containerSelect.getValue() == null)
			{
				// set initial selection
				selectedContainer = (ChartItemContainer)containers.get(0);	
				containerSelect.setValue(selectedContainer);
			}
			else
			{
				selectedContainer = (ChartItemContainer)containerSelect.getValue();
			}
			selectedContainer.addObserver(this);
			
			// update the datasource of the barchart
			barChart.setContainerDataSource(selectedContainer.getContainer());
			
			barChart.setGridMaxValue(selectedContainer.getMaxValue());
			barChart.setGridMinValue(0);

		}
	}

	public void update(Observable o, Object arg) {
		UpdateSelectedContainer();
	}
}